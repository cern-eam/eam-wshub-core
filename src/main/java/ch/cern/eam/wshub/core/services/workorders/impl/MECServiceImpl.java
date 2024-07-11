package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.MECService;
import ch.cern.eam.wshub.core.services.workorders.entities.MEC;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.workorderequipment_001.AdditionalDetails;
import net.datastream.schemas.mp_entities.workorderequipment_001.LinearReferenceInfo;
import net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment;
import net.datastream.schemas.mp_fields.ORGANIZATIONID_Type;
import net.datastream.schemas.mp_fields.WOID_Type;
import net.datastream.schemas.mp_functions.mp7394_001.MP7394_AddWorkOrderEquipment_001;
import net.datastream.schemas.mp_functions.mp7395_001.MP7395_SyncWorkOrderEquipment_001;
import net.datastream.schemas.mp_functions.mp7396_001.MP7396_RemoveWorkOrderEquipment_001;
import net.datastream.schemas.mp_results.mp7394_001.MP7394_AddWorkOrderEquipment_001_Result;
import net.datastream.schemas.mp_results.mp7395_001.MP7395_SyncWorkOrderEquipment_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.Tools.extractEntityCode;
import static ch.cern.eam.wshub.core.tools.Tools.extractOrganizationCode;

public class MECServiceImpl implements MECService {
    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;
    private GridsService gridsService;

    public MECServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
        this.gridsService = new GridsServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
    }

    /**
     * Adds an equipment with the specified properties to the specified workorder.
     *
     * @param context       the user credentials
     * @param mecToAdd      the MEC object to add
     * @return              the ID of the added equipment
     * @throws EAMException
     */
    @Override
    public String addWorkOrderEquipment(EAMContext context, MEC mecToAdd) throws EAMException {
        MECService.validateInput(mecToAdd);

        MP7394_AddWorkOrderEquipment_001 mp7394_addWorkOrderEquipment_001 = new MP7394_AddWorkOrderEquipment_001();
        net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment workOrderEquipment = new net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment();

        tools.getEAMFieldTools().transformWSHubObject(workOrderEquipment, mecToAdd, context);
        mp7394_addWorkOrderEquipment_001.getWorkOrderEquipment().add(workOrderEquipment);
        MP7394_AddWorkOrderEquipment_001_Result res = tools.performEAMOperation(context, eamws::addWorkOrderEquipmentOp, mp7394_addWorkOrderEquipment_001);

        return res.getResultData().getRELATEDWORKORDERID().get(0).getJOBNUM();
    }

    @Override
    public BatchResponse<String> addWorkOrderEquipmentBatch(EAMContext context, List<MEC> mecsToAdd) throws EAMException {
        return tools.batchOperation(context, this::addWorkOrderEquipment, mecsToAdd);
    }

    /**
     * Deletes a MEC from the specified parent workorder.
     *
     * @param context           the user credentials
     * @param parentWorkorderID the ID of the parent workorder
     * @param mecID             the ID of the MEC to delete
     * @return
     * @throws EAMException
     */
    @Override
    public String deleteWorkOrderMEC(EAMContext context, String parentWorkorderID, String mecID) throws EAMException {
        MECService.validateInput(parentWorkorderID, mecID);

        MP7396_RemoveWorkOrderEquipment_001 mp7396_removeWorkOrderEquipment_001 = new MP7396_RemoveWorkOrderEquipment_001();

        ORGANIZATIONID_Type organizationid_type = new ORGANIZATIONID_Type();
        organizationid_type.setORGANIZATIONCODE(tools.getOrganizationCode(context));

        WOID_Type woid_typeParent = new WOID_Type();
        woid_typeParent.setJOBNUM(parentWorkorderID);
        woid_typeParent.setORGANIZATIONID(organizationid_type);

        WOID_Type woid_typeEquipment = new WOID_Type();
        woid_typeEquipment.setJOBNUM(mecID);
        woid_typeEquipment.setORGANIZATIONID(organizationid_type);

        mp7396_removeWorkOrderEquipment_001.setWORKORDERID(woid_typeParent);
        mp7396_removeWorkOrderEquipment_001.setRELATEDWORKORDERID(woid_typeEquipment);

        tools.performEAMOperation(context, eamws::removeWorkOrderEquipmentOp, mp7396_removeWorkOrderEquipment_001);

        return "OK";
    }

    /**
     * Returns a list of all the MECs of the target workorder.
     *
     * @param context     the user credentials
     * @param workorderID the ID of the parent workorder
     * @return            the list of MEC ids
     * @throws EAMException
     */
    public List<String> getWorkOrderMecIDList(EAMContext context, String workorderID) throws EAMException {
        MECService.validateInput(workorderID);

        GridRequest gridRequest = new GridRequest(MECService.GRID_ID, GridRequest.GRIDTYPE.LIST, 50);
        gridRequest.addParam("param.workordernum", workorderID);
        gridRequest.addParam("param.organization", tools.getOrganizationCode(context));
        gridRequest.addParam("param.workorderrtype", MECService.GRID_WO_TYPE);
        gridRequest.addParam("param.tenant", tools.getTenant(context));

        GridRequestResult res = gridsService.executeQuery(context, gridRequest);

        List<GridField> targetColumn = res.getGridFields().stream()
                .filter(gridField -> gridField.getName().equals(MECService.MEC_ID_COLUMN_NAME))
                .collect(Collectors.toList());

        if (targetColumn.isEmpty()) {
            throw Tools.generateFault("Column with relatedWorkorderID (ID of the MEC) is not in dataspy");
        }

        int targetIndex = targetColumn.get(0).getOrder();

        List<String> listOfIDs = Arrays.stream(res.getRows())
                .map(gridRequestRow -> gridRequestRow.getCell()[targetIndex].getContent())
                .collect(Collectors.toList());

        return listOfIDs;
    }

    @Override
    public WorkOrderEquipment getWorkOrderMecEAM(EAMContext context, String workorderID) throws EAMException{
        WorkOrderServiceImpl wos = new WorkOrderServiceImpl(applicationData, tools, this.eamws); // Creating service here so it is easily removed when eam ws is implemented later
        net.datastream.schemas.mp_entities.workorder_001.WorkOrder res = wos.readWorkOrderEAM(context, extractEntityCode(workorderID), extractOrganizationCode(workorderID)).getResultData().getWorkOrder();
        WorkOrderEquipment woeq = new WorkOrderEquipment();
        woeq.setWORKORDERID(res.getPARENTWO());
        woeq.setEQUIPMENTID(res.getEQUIPMENTID());
        AdditionalDetails additionalDetails = new AdditionalDetails();
        additionalDetails.setWARRANTY(res.getWARRANTY());
        additionalDetails.setSAFETY(res.getSAFETY());
        additionalDetails.setDEPARTMENTID(res.getDEPARTMENTID());
        additionalDetails.setCOSTCODEID(res.getCOSTCODEID());
        if (res.getLINEARREFERENCEEVENT() != null) {
            additionalDetails.setLinearReferenceInfo(new LinearReferenceInfo());
            additionalDetails.getLinearReferenceInfo().setLINEARREFERENCEEVENT(res.getLINEARREFERENCEEVENT());
        }
        additionalDetails.setLOCATIONID(res.getLOCATIONID());
        additionalDetails.setOBJTYPE(res.getOBJTYPE());
        additionalDetails.setRecordid(res.getRecordid());
        additionalDetails.setRELATEDWORKORDERID(res.getWORKORDERID());
        woeq.setAdditionalDetails(additionalDetails);

        return woeq;
    }

    /**
     * Updates the target workorder with the specified properties.
     *
     * @param context    the user credentials
     * @param updatedMEC the updated mec object
     * @return
     * @throws EAMException
     */
    @Override
    public String syncWorkOrderEquipment(EAMContext context, MEC updatedMEC) throws EAMException {
        MECService.validateInput(updatedMEC);

        WorkOrderEquipment originalMecEAM = this.getWorkOrderMecEAM(context, updatedMEC.getRelatedWorkorderID());
        tools.getEAMFieldTools().transformWSHubObject(originalMecEAM, updatedMEC, context);

        MP7395_SyncWorkOrderEquipment_001 mp7395_syncWorkOrderEquipment_001 = new MP7395_SyncWorkOrderEquipment_001();
        mp7395_syncWorkOrderEquipment_001.setWorkOrderEquipment(originalMecEAM);

        MP7395_SyncWorkOrderEquipment_001_Result res = tools.performEAMOperation(context, eamws::syncWorkOrderEquipmentOp, mp7395_syncWorkOrderEquipment_001);

        return res.getResultData().getRELATEDWORKORDERID().getJOBNUM();
    }
}
