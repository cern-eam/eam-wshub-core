package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.SafetyService;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.*;
import net.datastream.schemas.mp_entities.entitysafety_001.EntitySafety;
import net.datastream.schemas.mp_entities.worksafety_001.WorkSafety;
import net.datastream.schemas.mp_functions.mp3219_001.MP3219_AddEntitySafety_001;
import net.datastream.schemas.mp_functions.mp3220_001.MP3220_SyncEntitySafety_001;
import net.datastream.schemas.mp_functions.mp3221_001.MP3221_DeleteEntitySafety_001;
import net.datastream.schemas.mp_functions.mp3222_001.MP3222_GetEntitySafety_001;
import net.datastream.schemas.mp_functions.mp7983_001.MP7983_GetWorkSafety_001;
import net.datastream.schemas.mp_functions.mp7984_001.MP7984_AddWorkSafety_001;
import net.datastream.schemas.mp_functions.mp7985_001.MP7985_SyncWorkSafety_001;
import net.datastream.schemas.mp_functions.mp7986_001.MP7986_DeleteWorkSafety_001;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import org.openapplications.oagis_segments.QUANTITY;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SafetyServiceImpl implements SafetyService {
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private GridsService gridsService;

    public SafetyServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
    }

    @Override
    public BatchResponse<List<Safety>> readSafetiesBatch(InforContext context, String entityType, List<String> entityCode) {
        List<Callable<List<Safety>>> callableList = entityCode.stream()
                .<Callable<List<Safety>>>map(code -> () -> readSafeties(context, entityType, code))
                .collect(Collectors.toList());

        return tools.processCallables(callableList);
    }

    @Override
    public BatchResponse<String> setSafetiesBatch(InforContext context, String entityType, Map<String, List<Safety>> entityCodeToSafeties) {
        List<Callable<String>> callableList = entityCodeToSafeties.keySet().stream()
                .<Callable<String>>map(code -> () -> setSafeties(context, entityType, code, entityCodeToSafeties.get(code)))
                .collect(Collectors.toList());

        return tools.processCallables(callableList);
    }

    // The entityType argument takes either "EVNT" or "OBJ"
    // Note that this method does not return the user defined fields, use the readSafety method for now to get these
    @Override
    public List<Safety> readSafeties(InforContext context, String entityType, String entityCode) throws InforException {
        GridRequest request = new GridRequest();
        request.setGridType(GridRequest.GRIDTYPE.LIST);

        if (isWorkOrder(entityType)) {
            request.setGridName("WSJOBS_KSF");
            request.addParam("param.workordernum", entityCode);
            request.addParam("parameter.r5role", "");
            request.setUserFunctionName("WSJOBS");
        } else if (isObject(entityType)) {
            request.setGridName("OSOBJA_ESF");
            request.setUserFunctionName("OSOBJA");
            request.addParam("parameter.object", entityCode);
            request.addParam("parameter.objorganization", tools.getOrganizationCode(context));
        } else {
            throw Tools.generateFault("Invalid entityType");
        }

        GridRequestResult result = gridsService.executeQuery(context, request);
        List<Safety> safeties = GridTools.convertGridResultToObject(Safety.class, null, result);

        safeties.stream().forEach(safety -> safety.setUserDefinedFields(null));

        return safeties;
    }

    // The entityType argument takes either "EVNT" or "OBJ"
    @Override
    public String setSafeties(InforContext context, String entityType, String entityCode, List<Safety> safeties) throws InforException {
        if (!isWorkOrder(entityType) && !isObject(entityType)) {
            throw Tools.generateFault("Invalid entityType");
        }

        Map<String, Safety> currentSafetiesMap = toMap(readSafeties(context, entityType, entityCode));

        // manage existing/new safeties
        for (Safety safety: safeties) {
            if (safety == null) {
                continue;
            }

            String id = safety.getId();
            Safety currentSafety = currentSafetiesMap.get(id);
            currentSafetiesMap.remove(id);

            if (safety.getReadOnly()) {
                continue;
            }

            if (currentSafety == null) {
                // add new safeties
                addSafety(context, entityType, entityCode, safety);
                continue;
            }

            // synchronize safeties that have not changed
            if (Safety.canBeChangedBy(currentSafety, safety)) {
                syncSafety(context, entityType, entityCode, safety);
            }
        };

        // remove old safeties
        for(Safety safety : currentSafetiesMap.values()) {
            removeSafety(context, entityType, safety.getId());
        }

        return "OK";
    }

    @Override
    public Safety readSafety(InforContext context, String entityType, String safetyCode) throws InforException {
        if (isObject(entityType)) {
            MP3222_GetEntitySafety_001 getEntitySafety = new MP3222_GetEntitySafety_001();
            getEntitySafety.setSAFETYCODE(safetyCode);
            EntitySafety inforSafety = tools.performInforOperation(context, inforws::getEntitySafetyOp, getEntitySafety)
                    .getResultData().getEntitySafety();
            Safety safety = new Safety();
            return tools.getInforFieldTools().transformInforObject(safety, inforSafety, context);
        } else if(isWorkOrder(entityType)) {
            MP7983_GetWorkSafety_001 getWorkSafety = new MP7983_GetWorkSafety_001();
            getWorkSafety.setSAFETYCODE(safetyCode);
            WorkSafety workSafety = tools.performInforOperation(context, inforws::getWorkSafetyOp, getWorkSafety)
                    .getResultData().getWorkSafety();
            Safety safety = new Safety();
            return tools.getInforFieldTools().transformInforObject(safety, workSafety, context);
        } else {
            throw Tools.generateFault("Invalid entityType");
        }
    }

    private String getFullEntityCode(InforContext context, String entityType, String entityCode) {
        String extension = isObject(entityType) ? "#" + tools.getOrganizationCode(context) : "";
        return entityCode + extension;
    }

    private EntitySafety transformToEntitySafety(
            InforContext context,
            String entityType,
            String entityCode,
            Safety safety,
            EntitySafety original
    ) throws InforException {

        EntitySafety entitySafetyInfor = original == null ? new EntitySafety() : original;
        tools.getInforFieldTools().transformWSHubObject(entitySafetyInfor, safety, context);

        if (safety.getId() == null) {
            entitySafetyInfor.setSAFETYCODE("0");
        }

        entitySafetyInfor.setENTITY(entityType);
        entitySafetyInfor.setENTITYSAFETYCODE(getFullEntityCode(context, entityType, entityCode));
        entitySafetyInfor.getHAZARDID().setREVISIONNUM(getRevisionQUANTITY(
                context, safety, entitySafetyInfor.getHAZARDID().getREVISIONNUM(), true));
        entitySafetyInfor.getPRECAUTIONID().setREVISIONNUM(getRevisionQUANTITY(
                context, safety, entitySafetyInfor.getPRECAUTIONID().getREVISIONNUM(), false));

        return entitySafetyInfor;
    }

    private WorkSafety transformToWorkSafety(
            InforContext context,
            String entityType,
            String entityCode,
            Safety safety,
            WorkSafety original
    ) throws InforException {

        WorkSafety workSafetyInfor = original == null ? new WorkSafety() : original;
        tools.getInforFieldTools().transformWSHubObject(workSafetyInfor, safety, context);

        if (safety.getId() == null) {
            workSafetyInfor.setSAFETYCODE("0");
        }

        workSafetyInfor.setENTITY(entityType);
        workSafetyInfor.setENTITYSAFETYCODE(getFullEntityCode(context, entityType, entityCode));
        workSafetyInfor.getHAZARDID().setREVISIONNUM(getRevisionQUANTITY(
                context, safety, workSafetyInfor.getHAZARDID().getREVISIONNUM(), true));
        workSafetyInfor.getPRECAUTIONID().setREVISIONNUM(getRevisionQUANTITY(
                context, safety, workSafetyInfor.getPRECAUTIONID().getREVISIONNUM(), false));

        return workSafetyInfor;
    }

    private void addSafety(InforContext context, String entityType, String entityCode, Safety safety)
            throws InforException {
        if (isObject(entityType)) {
            EntitySafety entitySafetyInfor = transformToEntitySafety(context, entityType, entityCode, safety, null);
            entitySafetyInfor.setSAFETYCODE("0"); // Safety code must be set (Infor will assign value later)

            MP3219_AddEntitySafety_001 addEntitySafety = new MP3219_AddEntitySafety_001();
            addEntitySafety.getEntitySafety().add(entitySafetyInfor);

            tools.performInforOperation(context, inforws::addEntitySafetyOp, addEntitySafety);
        } else if(isWorkOrder(entityType)) {
            WorkSafety workSafetyInfor = transformToWorkSafety(context, entityType, entityCode, safety, null);
            workSafetyInfor.setSAFETYCODE("0"); // Safety code must be set (Infor will assign value later)

            MP7984_AddWorkSafety_001 addWorkSafety_001 = new MP7984_AddWorkSafety_001();
            addWorkSafety_001.getWorkSafety().add(workSafetyInfor);

            tools.performInforOperation(context, inforws::addWorkSafetyOp, addWorkSafety_001);
        } else {
            throw Tools.generateFault("Invalid entityType");
        }
    }

    private void removeSafety(InforContext context, String entityType, String safetyId) throws InforException {
        if (isObject(entityType)) {
            MP3221_DeleteEntitySafety_001 deleteEntitySafety = new MP3221_DeleteEntitySafety_001();
            deleteEntitySafety.setSAFETYCODE(safetyId);
            tools.performInforOperation(context, inforws::deleteEntitySafetyOp, deleteEntitySafety);
        } else if (isWorkOrder(entityType)) {
            MP7986_DeleteWorkSafety_001 deleteWorkSafety = new MP7986_DeleteWorkSafety_001();
            deleteWorkSafety.setSAFETYCODE(safetyId);
            tools.performInforOperation(context, inforws::deleteWorkSafetyOp, deleteWorkSafety);
        } else {
            throw Tools.generateFault("Invalid entityType");
        }
    }

    private void syncSafety(InforContext context, String entityType, String entityCode, Safety safety)
            throws InforException {
        if (isObject(entityType)) {
            MP3222_GetEntitySafety_001 getEntitySafety = new MP3222_GetEntitySafety_001();
            getEntitySafety.setSAFETYCODE(safety.getId());
            EntitySafety previousSafety = tools.performInforOperation(
                    context, inforws::getEntitySafetyOp, getEntitySafety
            ).getResultData().getEntitySafety();

            EntitySafety entitySafetyInfor =
                    transformToEntitySafety(context, entityType, entityCode, safety, previousSafety);
            MP3220_SyncEntitySafety_001 syncEntitySafety = new MP3220_SyncEntitySafety_001();
            syncEntitySafety.setEntitySafety(entitySafetyInfor);
            tools.performInforOperation(context, inforws::syncEntitySafetyOp, syncEntitySafety);
        } else if(isWorkOrder(entityType)) {
            MP7983_GetWorkSafety_001 getWorkSafety = new MP7983_GetWorkSafety_001();
            getWorkSafety.setSAFETYCODE(safety.getId());
            WorkSafety previousSafety = tools.performInforOperation(
                    context, inforws::getWorkSafetyOp, getWorkSafety
            ).getResultData().getWorkSafety();

            WorkSafety workSafetyInfor =
                    transformToWorkSafety(context, entityType, entityCode, safety, previousSafety);

            MP7985_SyncWorkSafety_001 syncWorkSafety = new MP7985_SyncWorkSafety_001();
            syncWorkSafety.setWorkSafety(workSafetyInfor);
            tools.performInforOperation(context, inforws::syncWorkSafetyOp, syncWorkSafety);
        } else {
            throw Tools.generateFault("Invalid entityType");
        }
    }

    private QUANTITY getRevisionQUANTITY(
            InforContext context,
            Safety safety,
            QUANTITY oldQuantity,
            boolean isHazard) throws InforException {

        String id = isHazard ? safety.getHazardCode() : safety.getPrecautionCode();
        String label = isHazard ? "Hazard Code" : "Precaution Code";
        return oldQuantity == null
                ? DataTypeTools.encodeQuantity(getLatestRevision(context, id, isHazard), label)
                : oldQuantity;
    }

    private BigDecimal getLatestRevision(InforContext context, String id, boolean isHazard) throws InforException {
        if (id == null) {
            throw Tools.generateFault(isHazard ? "Hazard Code is null" : "Precaution Code is null");
        }

        GridRequest gridRequest = new GridRequest(isHazard ? "LVSAFETYHAZARD" : "LVPRECAUTION", GridRequest.GRIDTYPE.LOV);
        gridRequest.addFilter(isHazard ? "hazardcode" : "precaution", id, "=");

        // there should be always either one row, or no row at all
        gridRequest.setRowCount(1);

        String revision = GridTools.extractSingleResult(
                gridsService.executeQuery(context, gridRequest), "revision");

        return new BigDecimal(revision);
    }

    private Map<String, Safety> toMap(List<Safety> safeties) {
        return safeties.stream().collect(Collectors.toMap(Safety::getId, Function.identity(),
                (firstSafety, otherSafety) -> firstSafety)); // if there's more than one safety, keep the first
    }

    private boolean isWorkOrder(String entityType) {
        return entityType.equals("EVNT");
    }

    private boolean isObject(String entityType) {
        return entityType.equals("OBJ");
    }
}
