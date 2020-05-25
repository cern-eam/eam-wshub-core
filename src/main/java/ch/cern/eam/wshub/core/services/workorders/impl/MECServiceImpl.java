package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.MECService;
import ch.cern.eam.wshub.core.services.workorders.entities.MEC;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.ORGANIZATIONID_Type;
import net.datastream.schemas.mp_fields.WOID_Type;
import net.datastream.schemas.mp_functions.mp7394_001.MP7394_AddWorkOrderEquipment_001;
import net.datastream.schemas.mp_functions.mp7396_001.MP7396_RemoveWorkOrderEquipment_001;
import net.datastream.schemas.mp_results.mp7394_001.MP7394_AddWorkOrderEquipment_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Adds an equipment with the specified properties to the specified workorder.
     *
     * @param context       the user credentials
     * @param mecToAdd      the MEC object to add
     * @return              the ID of the added equipment
     * @throws InforException
     */
    @Override
    public String addWorkOrderEquipment(InforContext context, MEC mecToAdd) throws InforException {
        MECService.validateInput(mecToAdd);

        MP7394_AddWorkOrderEquipment_001 mp7394_addWorkOrderEquipment_001 = new MP7394_AddWorkOrderEquipment_001();
        net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment workOrderEquipment = new net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment();

        tools.getInforFieldTools().transformWSHubObject(workOrderEquipment, mecToAdd, context);
        mp7394_addWorkOrderEquipment_001.getWorkOrderEquipment().add(workOrderEquipment);
        MP7394_AddWorkOrderEquipment_001_Result res = tools.performInforOperation(context, inforws::addWorkOrderEquipmentOp, mp7394_addWorkOrderEquipment_001);

        return res.getResultData().getRELATEDWORKORDERID().get(0).getJOBNUM();
    }

    /**
     * Deletes a MEC from the specified parent workorder.
     *
     * @param context           the user credentials
     * @param parentWorkorderID the ID of the parent workorder
     * @param mecID             the ID of the MEC to delete
     * @return
     * @throws InforException
     */
    @Override
    public String deleteWorkOrderMEC(InforContext context, String parentWorkorderID, String mecID) throws InforException {
        MECService.validateInput(parentWorkorderID, mecID);

        MP7396_RemoveWorkOrderEquipment_001 mp7396_removeWorkOrderEquipment_001 = new MP7396_RemoveWorkOrderEquipment_001();

        ORGANIZATIONID_Type organizationid_type = new ORGANIZATIONID_Type();
        organizationid_type.setORGANIZATIONCODE("*");

        WOID_Type woid_typeParent = new WOID_Type();
        woid_typeParent.setJOBNUM(parentWorkorderID);
        woid_typeParent.setORGANIZATIONID(organizationid_type);

        WOID_Type woid_typeEquipment = new WOID_Type();
        woid_typeEquipment.setJOBNUM(mecID);
        woid_typeEquipment.setORGANIZATIONID(organizationid_type);

        mp7396_removeWorkOrderEquipment_001.setWORKORDERID(woid_typeParent);
        mp7396_removeWorkOrderEquipment_001.setRELATEDWORKORDERID(woid_typeEquipment);

        tools.performInforOperation(context, inforws::removeWorkOrderEquipmentOp, mp7396_removeWorkOrderEquipment_001);

        return "OK";
    }

    /**
     * Returns a list of all the MECs of the target workorder.
     *
     * @param context     the user credentials
     * @param workorderID the ID of the parent workorder
     * @return            the list of MEC ids
     * @throws InforException
     */
    public List<String> getWorkOrderMecIDList(InforContext context, String workorderID) throws InforException {
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

    /**
     * Updates the target workorder with the specified properties.
     *
     * @param context    the user credentials
     * @param updatedMEC the updated mec object
     * @return
     * @throws InforException
     */
    @Override
    public String syncWorkOrderEquipment(InforContext context, MEC updatedMEC) throws InforException {
//        MECService.validateInput(parentWorkorderID, mecID, mecProperties);
        //TODO WIP
//        MP73

//        GridRequestResult woList = this.GetWorkorderMecIDs(context, parentWorkorderID);
//        GridRequestCell[] relatedWO = Arrays.stream(woList.getRows()).
//                filter(eq -> eq.getCell()[4].getContent().equals(equipmentID)).
//                collect(Collectors.toList()).get(0).getCell();
//
//        System.out.println(relatedWO);
//
//        net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment workOrderEquipment = new net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment();
//        tools.getInforFieldTools().transformWSHubObject(workOrderEquipment, entitySafetywshub, context);


        // eqorg, eqname, eqdescri, eqtype, id

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

