package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.MECService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.activity_001.WorkOrderEquipment;
import net.datastream.schemas.mp_fields.EQUIPMENTID_Type;
import net.datastream.schemas.mp_fields.ORGANIZATIONID_Type;
import net.datastream.schemas.mp_fields.WOID_Type;
import net.datastream.schemas.mp_functions.mp7394_001.MP7394_AddWorkOrderEquipment_001;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class MECServiceImpl implements MECService {
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public MECServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String getWorkOrderEquipment(InforContext context, String equipmentID) throws InforException {
        return null;
    }

    @Override
    public String getWorkOrderEquipmentOfWorkorder(InforContext context, String equipmentID) throws InforException {
        return null;
    }

    @Override
    public String addWorkOrderEquipment(InforContext context, String workOrderID, String equipmentID) throws InforException {
//(context, String WorkorderId, MEC mecProperties)
        MP7394_AddWorkOrderEquipment_001 mp7394_addWorkOrderEquipment_001 = new MP7394_AddWorkOrderEquipment_001();
        net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment workOrderEquipment = new net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment();

        EQUIPMENTID_Type equipmentid_type = new EQUIPMENTID_Type();
        equipmentid_type.setEQUIPMENTCODE(equipmentID);
        workOrderEquipment.setEQUIPMENTID(equipmentid_type);

        WOID_Type woid_type = new WOID_Type();
        woid_type.setJOBNUM("28021934");
        ORGANIZATIONID_Type organizationid_type = new ORGANIZATIONID_Type();
        organizationid_type.setORGANIZATIONCODE("*");
        woid_type.setORGANIZATIONID(organizationid_type);
        workOrderEquipment.setWORKORDERID(woid_type);

        workOrderEquipment.getEQUIPMENTID().setORGANIZATIONID(organizationid_type);

        mp7394_addWorkOrderEquipment_001.getWorkOrderEquipment().add(workOrderEquipment);
        tools.performInforOperation(context, inforws::addWorkOrderEquipmentOp, mp7394_addWorkOrderEquipment_001);

        System.out.println("en");

        return null;
    }

    @Override
    public String deleteWorkOrderEquipment(InforContext context, String equipmentID) throws InforException {
        return null;
    }

    @Override
    public String syncWorkOrderEquipment(InforContext context, String equipmentID) throws InforException {
        return null;
    }
}

