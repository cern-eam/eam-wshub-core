package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.MECService;
import ch.cern.eam.wshub.core.services.workorders.entities.MEC;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment;
import net.datastream.schemas.mp_fields.EQUIPMENTID_Type;
import net.datastream.schemas.mp_fields.ORGANIZATIONID_Type;
import net.datastream.schemas.mp_fields.WOID_Type;
import net.datastream.schemas.mp_functions.mp7394_001.MP7394_AddWorkOrderEquipment_001;
import net.datastream.schemas.mp_functions.mp7395_001.MP7395_SyncWorkOrderEquipment_001;
import net.datastream.schemas.mp_functions.mp7396_001.MP7396_RemoveWorkOrderEquipment_001;
import net.datastream.schemas.mp_results.mp7394_001.MP7394_AddWorkOrderEquipment_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class MECServiceImpl implements MECService {
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private GridsService gridsService;

    public MECServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
    }

//    @Override
//    public String getWorkOrderEquipment(InforContext context, String equipmentID) throws InforException {
//        return null;
//    }

    @Override
    public String getWorkOrderEquipmentOfWorkorder(InforContext context, String equipmentID) throws InforException {
        return null;
    }

    @Override
    public String addWorkOrderEquipment(InforContext context, String workOrderID, MEC mecProperties) throws InforException {
        WOID_Type woID = new WOID_Type();
        woID.setJOBNUM(workOrderID);
        mecProperties.setWorkorderid(woID);

        MP7394_AddWorkOrderEquipment_001 mp7394_addWorkOrderEquipment_001 = new MP7394_AddWorkOrderEquipment_001();
        net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment workOrderEquipment = new net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment();

        WOID_Type woid_type = new WOID_Type();
        woid_type.setJOBNUM(workOrderID);

        // Inject organization
        ORGANIZATIONID_Type organizationid_type = new ORGANIZATIONID_Type();
        organizationid_type.setORGANIZATIONCODE("*");
        woid_type.setORGANIZATIONID(organizationid_type);
        mecProperties.setWorkorderid(woid_type);
        mecProperties.getEquipmentid().setORGANIZATIONID(organizationid_type);

        tools.getInforFieldTools().transformWSHubObject(workOrderEquipment, mecProperties, context);
        mp7394_addWorkOrderEquipment_001.getWorkOrderEquipment().add(workOrderEquipment);
        MP7394_AddWorkOrderEquipment_001_Result res = tools.performInforOperation(context, inforws::addWorkOrderEquipmentOp, mp7394_addWorkOrderEquipment_001);

        return res.getResultData().getRELATEDWORKORDERID().get(0).getJOBNUM();
    }

    @Override
    public String deleteWorkOrderEquipment(InforContext context, String parentWorkorderID, String equipmentID) throws InforException {
        MP7396_RemoveWorkOrderEquipment_001 mp7396_removeWorkOrderEquipment_001 = new MP7396_RemoveWorkOrderEquipment_001();

        ORGANIZATIONID_Type organizationid_type = new ORGANIZATIONID_Type();
        organizationid_type.setORGANIZATIONCODE("*");

        WOID_Type woid_typeParent = new WOID_Type();
        woid_typeParent.setJOBNUM(parentWorkorderID);
        woid_typeParent.setORGANIZATIONID(organizationid_type);

        WOID_Type woid_typeEquipment = new WOID_Type();
        woid_typeEquipment.setJOBNUM(equipmentID);
        woid_typeEquipment.setORGANIZATIONID(organizationid_type);

        mp7396_removeWorkOrderEquipment_001.setWORKORDERID(woid_typeParent);
        mp7396_removeWorkOrderEquipment_001.setRELATEDWORKORDERID(woid_typeEquipment);

        tools.performInforOperation(context, inforws::removeWorkOrderEquipmentOp, mp7396_removeWorkOrderEquipment_001);

        return "OK";
    }

    public String getWorkOrderEquipment(InforContext context, String equipmentID) throws InforException {
        GridRequest gridRequest = new GridRequest("WSJOBS_MEC", GridRequest.GRIDTYPE.LIST, 50);
        gridRequest.addParam("param.workordernum", "28021934");
        gridRequest.addParam("param.organization", tools.getOrganizationCode(context));
        gridRequest.addParam("param.workorderrtype", "BR");
        gridRequest.addParam("param.tenant", tools.getTenant(context));
//        gridRequest.addParam("control.org", applicationData.getc);

        GridRequestResult res = gridsService.executeQuery(context, gridRequest);

        System.out.println("HI PEDRINHO");

        return "res";
    }

    @Override
    public String syncWorkOrderEquipment(InforContext context, String equipmentID) throws InforException {

//        SafetyService.validateInput(entitySafetywshub);
//
//        MP3222_GetEntitySafety_001 getEntitySafety = new MP3222_GetEntitySafety_001();
//        getEntitySafety.setSAFETYCODE(entitySafetywshub.getSafetyCode());
//        MP3222_GetEntitySafety_001_Result resGet = tools.performInforOperation(context, inforws::getEntitySafetyOp, getEntitySafety);
//        EntitySafety entitySafetyInfor = resGet.getResultData().getEntitySafety();
//
//        tools.getInforFieldTools().transformWSHubObject(entitySafetyInfor, entitySafetywshub, context);
//
//        MP3220_SyncEntitySafety_001 syncEntitySafety = new MP3220_SyncEntitySafety_001();
//
//        syncEntitySafety.setEntitySafety(entitySafetyInfor);
//        MP3220_SyncEntitySafety_001_Result resSync = tools.performInforOperation(context, inforws::syncEntitySafetyOp, syncEntitySafety);
//
//        return resSync.getResultData().getSAFETYCODE();
        return "res";
    }
}

