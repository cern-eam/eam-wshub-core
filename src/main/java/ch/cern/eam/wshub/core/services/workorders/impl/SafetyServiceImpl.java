package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import org.openapplications.oagis_segments.QUANTITY;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SafetyServiceImpl implements SafetyService {
    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;
    private GridsService gridsService;

    public SafetyServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
        this.gridsService = new GridsServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
    }

    @Override
    public BatchResponse<List<Safety>> readSafetiesBatch(EAMContext context, String entityType, List<String> entityCode) {
        List<Callable<List<Safety>>> callableList = entityCode.stream()
                .<Callable<List<Safety>>>map(code -> () -> readSafeties(context, entityType, code))
                .collect(Collectors.toList());

        return tools.processCallables(callableList);
    }

    @Override
    public BatchResponse<String> setSafetiesBatch(EAMContext context, String entityType, Map<String, List<Safety>> entityCodeToSafeties) {
        List<Callable<String>> callableList = entityCodeToSafeties.keySet().stream()
                .<Callable<String>>map(code -> () -> setSafeties(context, entityType, code, entityCodeToSafeties.get(code)))
                .collect(Collectors.toList());

        return tools.processCallables(callableList);
    }

    // The entityType argument takes either "EVNT" or "OBJ"
    // Note that this method does not return the user defined fields, use the readSafety method for now to get these
    @Override
    public List<Safety> readSafeties(EAMContext context, String entityType, String entityCode) throws EAMException {
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
    public String setSafeties(EAMContext context, String entityType, String entityCode, List<Safety> safeties) throws EAMException {
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
    public Safety readSafety(EAMContext context, String entityType, String safetyCode) throws EAMException {
        if (isObject(entityType)) {
            MP3222_GetEntitySafety_001 getEntitySafety = new MP3222_GetEntitySafety_001();
            getEntitySafety.setSAFETYCODE(safetyCode);
            EntitySafety eamSafety = tools.performEAMOperation(context, eamws::getEntitySafetyOp, getEntitySafety)
                    .getResultData().getEntitySafety();
            Safety safety = new Safety();
            return tools.getEAMFieldTools().transformEAMObject(safety, eamSafety, context);
        } else if(isWorkOrder(entityType)) {
            MP7983_GetWorkSafety_001 getWorkSafety = new MP7983_GetWorkSafety_001();
            getWorkSafety.setSAFETYCODE(safetyCode);
            WorkSafety workSafety = tools.performEAMOperation(context, eamws::getWorkSafetyOp, getWorkSafety)
                    .getResultData().getWorkSafety();
            Safety safety = new Safety();
            return tools.getEAMFieldTools().transformEAMObject(safety, workSafety, context);
        } else {
            throw Tools.generateFault("Invalid entityType");
        }
    }

    private String getFullEntityCode(EAMContext context, String entityType, String entityCode) {
        String extension = isObject(entityType) ? "#" + tools.getOrganizationCode(context) : "";
        return entityCode + extension;
    }

    private EntitySafety transformToEntitySafety(
            EAMContext context,
            String entityType,
            String entityCode,
            Safety safety,
            EntitySafety original
    ) throws EAMException {

        EntitySafety entitySafetyEAM = original == null ? new EntitySafety() : original;
        tools.getEAMFieldTools().transformWSHubObject(entitySafetyEAM, safety, context);

        if (safety.getId() == null) {
            entitySafetyEAM.setSAFETYCODE("0");
        }

        entitySafetyEAM.setENTITY(entityType);
        entitySafetyEAM.setENTITYSAFETYCODE(getFullEntityCode(context, entityType, entityCode));
        entitySafetyEAM.getHAZARDID().setREVISIONNUM(getRevisionQUANTITY(
                context, safety, entitySafetyEAM.getHAZARDID().getREVISIONNUM(), true));
        entitySafetyEAM.getPRECAUTIONID().setREVISIONNUM(getRevisionQUANTITY(
                context, safety, entitySafetyEAM.getPRECAUTIONID().getREVISIONNUM(), false));

        return entitySafetyEAM;
    }

    private WorkSafety transformToWorkSafety(
            EAMContext context,
            String entityType,
            String entityCode,
            Safety safety,
            WorkSafety original
    ) throws EAMException {

        WorkSafety workSafetyEAM = original == null ? new WorkSafety() : original;
        tools.getEAMFieldTools().transformWSHubObject(workSafetyEAM, safety, context);

        if (safety.getId() == null) {
            workSafetyEAM.setSAFETYCODE("0");
        }

        workSafetyEAM.setENTITY(entityType);
        workSafetyEAM.setENTITYSAFETYCODE(getFullEntityCode(context, entityType, entityCode));
        workSafetyEAM.getHAZARDID().setREVISIONNUM(getRevisionQUANTITY(
                context, safety, workSafetyEAM.getHAZARDID().getREVISIONNUM(), true));
        workSafetyEAM.getPRECAUTIONID().setREVISIONNUM(getRevisionQUANTITY(
                context, safety, workSafetyEAM.getPRECAUTIONID().getREVISIONNUM(), false));

        return workSafetyEAM;
    }

    private void addSafety(EAMContext context, String entityType, String entityCode, Safety safety)
            throws EAMException {
        if (isObject(entityType)) {
            EntitySafety entitySafetyEAM = transformToEntitySafety(context, entityType, entityCode, safety, null);
            entitySafetyEAM.setSAFETYCODE("0"); // Safety code must be set (EAM will assign value later)

            MP3219_AddEntitySafety_001 addEntitySafety = new MP3219_AddEntitySafety_001();
            addEntitySafety.getEntitySafety().add(entitySafetyEAM);

            tools.performEAMOperation(context, eamws::addEntitySafetyOp, addEntitySafety);
        } else if(isWorkOrder(entityType)) {
            WorkSafety workSafetyEAM = transformToWorkSafety(context, entityType, entityCode, safety, null);
            workSafetyEAM.setSAFETYCODE("0"); // Safety code must be set (EAM will assign value later)

            MP7984_AddWorkSafety_001 addWorkSafety_001 = new MP7984_AddWorkSafety_001();
            addWorkSafety_001.getWorkSafety().add(workSafetyEAM);

            tools.performEAMOperation(context, eamws::addWorkSafetyOp, addWorkSafety_001);
        } else {
            throw Tools.generateFault("Invalid entityType");
        }
    }

    private void removeSafety(EAMContext context, String entityType, String safetyId) throws EAMException {
        if (isObject(entityType)) {
            MP3221_DeleteEntitySafety_001 deleteEntitySafety = new MP3221_DeleteEntitySafety_001();
            deleteEntitySafety.setSAFETYCODE(safetyId);
            tools.performEAMOperation(context, eamws::deleteEntitySafetyOp, deleteEntitySafety);
        } else if (isWorkOrder(entityType)) {
            MP7986_DeleteWorkSafety_001 deleteWorkSafety = new MP7986_DeleteWorkSafety_001();
            deleteWorkSafety.setSAFETYCODE(safetyId);
            tools.performEAMOperation(context, eamws::deleteWorkSafetyOp, deleteWorkSafety);
        } else {
            throw Tools.generateFault("Invalid entityType");
        }
    }

    private void syncSafety(EAMContext context, String entityType, String entityCode, Safety safety)
            throws EAMException {
        if (isObject(entityType)) {
            MP3222_GetEntitySafety_001 getEntitySafety = new MP3222_GetEntitySafety_001();
            getEntitySafety.setSAFETYCODE(safety.getId());
            EntitySafety previousSafety = tools.performEAMOperation(
                    context, eamws::getEntitySafetyOp, getEntitySafety
            ).getResultData().getEntitySafety();

            EntitySafety entitySafetyEAM =
                    transformToEntitySafety(context, entityType, entityCode, safety, previousSafety);
            MP3220_SyncEntitySafety_001 syncEntitySafety = new MP3220_SyncEntitySafety_001();
            syncEntitySafety.setEntitySafety(entitySafetyEAM);
            tools.performEAMOperation(context, eamws::syncEntitySafetyOp, syncEntitySafety);
        } else if(isWorkOrder(entityType)) {
            MP7983_GetWorkSafety_001 getWorkSafety = new MP7983_GetWorkSafety_001();
            getWorkSafety.setSAFETYCODE(safety.getId());
            WorkSafety previousSafety = tools.performEAMOperation(
                    context, eamws::getWorkSafetyOp, getWorkSafety
            ).getResultData().getWorkSafety();

            WorkSafety workSafetyEAM =
                    transformToWorkSafety(context, entityType, entityCode, safety, previousSafety);

            MP7985_SyncWorkSafety_001 syncWorkSafety = new MP7985_SyncWorkSafety_001();
            syncWorkSafety.setWorkSafety(workSafetyEAM);
            tools.performEAMOperation(context, eamws::syncWorkSafetyOp, syncWorkSafety);
        } else {
            throw Tools.generateFault("Invalid entityType");
        }
    }

    private QUANTITY getRevisionQUANTITY(
            EAMContext context,
            Safety safety,
            QUANTITY oldQuantity,
            boolean isHazard) throws EAMException {

        String id = isHazard ? safety.getHazardCode() : safety.getPrecautionCode();
        String label = isHazard ? "Hazard Code" : "Precaution Code";
        return oldQuantity == null
                ? DataTypeTools.encodeQuantity(getLatestRevision(context, id, isHazard), label)
                : oldQuantity;
    }

    private BigDecimal getLatestRevision(EAMContext context, String id, boolean isHazard) throws EAMException {
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
