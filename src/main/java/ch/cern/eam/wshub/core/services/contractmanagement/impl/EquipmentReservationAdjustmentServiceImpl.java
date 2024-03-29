package ch.cern.eam.wshub.core.services.contractmanagement.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.contractmanagement.EquipmentReservationAdjustmentService;
import ch.cern.eam.wshub.core.services.contractmanagement.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.customerrentaladjustment_001.CustomerRentalAdjustment;
import net.datastream.schemas.mp_fields.CUSTOMERRENTALADJUSTMENTID_Type;
import net.datastream.schemas.mp_fields.STATUS_Type;
import net.datastream.schemas.mp_fields.TYPE_Type;
import net.datastream.schemas.mp_functions.mp7863_001.MP7863_GetCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_functions.mp7864_001.MP7864_AddCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_functions.mp7865_001.MP7865_SyncCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_functions.mp7866_001.MP7866_DeleteCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_results.mp7863_001.MP7863_GetCustomerRentalAdjustment_001_Result;
import net.datastream.schemas.mp_results.mp7864_001.MP7864_AddCustomerRentalAdjustment_001_Result;
import net.datastream.schemas.mp_results.mp7865_001.MP7865_SyncCustomerRentalAdjustment_001_Result;
import net.datastream.schemas.mp_results.mp7866_001.MP7866_DeleteCustomerRentalAdjustment_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class EquipmentReservationAdjustmentServiceImpl implements EquipmentReservationAdjustmentService {
    private final Tools tools;
    private final InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public EquipmentReservationAdjustmentServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String createEquipmentReservationAdjustment(InforContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws InforException {
        MP7864_AddCustomerRentalAdjustment_001 addCustomerRentalAdjustment = new MP7864_AddCustomerRentalAdjustment_001();
        CustomerRentalAdjustment customerRentalAdjustment = tools.getInforFieldTools().transformWSHubObject(createDefaultEquipmentReservationAdjustment(), equipmentReservationAdjustment, context);
        addCustomerRentalAdjustment.setCustomerRentalAdjustment(customerRentalAdjustment);
        MP7864_AddCustomerRentalAdjustment_001_Result addOperationResult = tools.performInforOperation(context, inforws::addCustomerRentalAdjustmentOp, addCustomerRentalAdjustment);
        return addOperationResult.getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    @Override
    public EquipmentReservationAdjustment readEquipmentReservationAdjustment(InforContext context, String number) throws InforException {
        CustomerRentalAdjustment customerRentalAdjustment = readCustomerRentalAdjustment(context, number);
        return tools.getInforFieldTools().transformInforObject(new EquipmentReservationAdjustment(), customerRentalAdjustment, context);
    }

    @Override
    public String updateEquipmentReservationAdjustment(InforContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws InforException {
        MP7865_SyncCustomerRentalAdjustment_001 syncCustomerRentalAdjustment = new MP7865_SyncCustomerRentalAdjustment_001();
        CustomerRentalAdjustment prevCustomerRentalAdjustment = readCustomerRentalAdjustment(context, equipmentReservationAdjustment.getCode());
        CustomerRentalAdjustment newCustomerRentalAdjustment = tools.getInforFieldTools().transformWSHubObject(prevCustomerRentalAdjustment, equipmentReservationAdjustment, context);
        syncCustomerRentalAdjustment.setCustomerRentalAdjustment(newCustomerRentalAdjustment);
        MP7865_SyncCustomerRentalAdjustment_001_Result updateOperationResult = tools.performInforOperation(context, inforws::syncCustomerRentalAdjustmentOp, syncCustomerRentalAdjustment);
        return updateOperationResult.getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    @Override
    public String deleteEquipmentReservationAdjustment(InforContext context, String number) throws InforException {
        MP7866_DeleteCustomerRentalAdjustment_001 deleteCustomerRentalAdjustment = new MP7866_DeleteCustomerRentalAdjustment_001();
        CUSTOMERRENTALADJUSTMENTID_Type idType = new CUSTOMERRENTALADJUSTMENTID_Type();
        idType.setCUSTOMERRENTALADJUSTMENTPK(number);
        deleteCustomerRentalAdjustment.setCUSTOMERRENTALADJUSTMENTID(idType);
        MP7866_DeleteCustomerRentalAdjustment_001_Result deleteOperationResult = tools.performInforOperation(context, inforws::deleteCustomerRentalAdjustmentOp, deleteCustomerRentalAdjustment);
        return deleteOperationResult.getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    private CustomerRentalAdjustment readCustomerRentalAdjustment(InforContext context, String number) throws InforException {
        MP7863_GetCustomerRentalAdjustment_001 getCustomerRentalAdjustment = new MP7863_GetCustomerRentalAdjustment_001();
        CUSTOMERRENTALADJUSTMENTID_Type idType = new CUSTOMERRENTALADJUSTMENTID_Type();
        idType.setCUSTOMERRENTALADJUSTMENTPK(number);
        getCustomerRentalAdjustment.setCUSTOMERRENTALADJUSTMENTID(idType);
        MP7863_GetCustomerRentalAdjustment_001_Result getOperationResult = tools.performInforOperation(context, inforws::getCustomerRentalAdjustmentOp, getCustomerRentalAdjustment);
        return getOperationResult.getResultData().getCustomerRentalAdjustment();
    }

    private CustomerRentalAdjustment createDefaultEquipmentReservationAdjustment() {
        CustomerRentalAdjustment defaultObject = new CustomerRentalAdjustment();
        STATUS_Type rStatus = new STATUS_Type();
        rStatus.setSTATUSCODE("0");
        defaultObject.setADJUSTMENTRSTATUS(rStatus);
        TYPE_Type rType = new TYPE_Type();
        rType.setTYPECODE("0");
        defaultObject.setADJUSTMENTRTYPE(rType);
        CUSTOMERRENTALADJUSTMENTID_Type code = new CUSTOMERRENTALADJUSTMENTID_Type();
        code.setCUSTOMERRENTALADJUSTMENTPK("0");
        defaultObject.setCUSTOMERRENTALADJUSTMENTID(code);
        return defaultObject;
    }

}
