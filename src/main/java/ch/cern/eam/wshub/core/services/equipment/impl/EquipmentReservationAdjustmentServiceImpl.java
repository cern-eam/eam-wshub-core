package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentReservationAdjustmentService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.customerrentaladjustment_001.CustomerRentalAdjustment;
import net.datastream.schemas.mp_fields.CUSTOMERRENTALADJUSTMENTID_Type;
import net.datastream.schemas.mp_functions.mp7863_001.MP7863_GetCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_functions.mp7864_001.MP7864_AddCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_functions.mp7865_001.MP7865_SyncCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_functions.mp7866_001.MP7866_DeleteCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_results.mp7863_001.MP7863_GetCustomerRentalAdjustment_001_Result;
import net.datastream.schemas.mp_results.mp7864_001.MP7864_AddCustomerRentalAdjustment_001_Result;
import net.datastream.schemas.mp_results.mp7865_001.MP7865_SyncCustomerRentalAdjustment_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class EquipmentReservationAdjustmentServiceImpl implements EquipmentReservationAdjustmentService {

    private ApplicationData applicationData;
    private Tools tools;
    private InforWebServicesPT inforws;

    public EquipmentReservationAdjustmentServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String createEquipmentReservationAdjustment(InforContext context, EquipmentReservationAdjustment adjustmentParam) throws InforException {
        // Set it so the WSHub transformer sets the corresponding field (otherwise we get null)
        adjustmentParam.setCode("0");

        // Set corresponding system codes (because HxGN marks them as required)
        tools.processRunnables(
                () -> adjustmentParam.setAdjustmentSystemTypeCode(tools.getFieldDescriptionsTools().readSystemCodeForUserCode(context, "AJTP", adjustmentParam.getAdjustmentTypeCode())),
                () -> adjustmentParam.setAdjustmentSystemStatusCode(tools.getFieldDescriptionsTools().readSystemCodeForUserCode(context, "AJST", adjustmentParam.getAdjustmentStatusCode()))
        );

        CustomerRentalAdjustment reservationAdjustment = new CustomerRentalAdjustment();
        tools.getInforFieldTools().transformWSHubObject(reservationAdjustment, adjustmentParam, context);

        MP7864_AddCustomerRentalAdjustment_001 addReservationAdjustment = new MP7864_AddCustomerRentalAdjustment_001();
        addReservationAdjustment.setCustomerRentalAdjustment(reservationAdjustment);

        MP7864_AddCustomerRentalAdjustment_001_Result result =
                tools.performInforOperation(context, inforws::addCustomerRentalAdjustmentOp, addReservationAdjustment);

        return result.getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    @Override
    public EquipmentReservationAdjustment readEquipmentReservationAdjustment(InforContext context, String customerRentalAdjustmentCode) throws InforException {
        CustomerRentalAdjustment reservationAdjustment = readEquipmentReservationAdjustmentInfor(context, customerRentalAdjustmentCode);

        return tools.getInforFieldTools().transformInforObject(new EquipmentReservationAdjustment(), reservationAdjustment);
    }

    private CustomerRentalAdjustment readEquipmentReservationAdjustmentInfor(InforContext context, String customerRentalAdjustmentCode) throws InforException {
        MP7863_GetCustomerRentalAdjustment_001 getReservationAdjustment = new MP7863_GetCustomerRentalAdjustment_001();
        getReservationAdjustment.setCUSTOMERRENTALADJUSTMENTID(new CUSTOMERRENTALADJUSTMENTID_Type());
        getReservationAdjustment.getCUSTOMERRENTALADJUSTMENTID().setCUSTOMERRENTALADJUSTMENTPK(customerRentalAdjustmentCode);

        MP7863_GetCustomerRentalAdjustment_001_Result result =
                tools.performInforOperation(context, inforws::getCustomerRentalAdjustmentOp, getReservationAdjustment);

        return result.getResultData().getCustomerRentalAdjustment();
    }

    @Override
    public String updateEquipmentReservationAdjustment(InforContext context, EquipmentReservationAdjustment adjustmentParam) throws InforException {
        // Set corresponding system codes (required) if the client defines them
        adjustmentParam.setAdjustmentSystemStatusCode(tools.getFieldDescriptionsTools().readSystemCodeForUserCode(context, "AJST", adjustmentParam.getAdjustmentStatusCode()));

        CustomerRentalAdjustment reservationAdjustment = readEquipmentReservationAdjustmentInfor(context, adjustmentParam.getCode());
        tools.getInforFieldTools().transformWSHubObject(reservationAdjustment, adjustmentParam, context);

        MP7865_SyncCustomerRentalAdjustment_001 syncReservationAdjustment = new MP7865_SyncCustomerRentalAdjustment_001();
        syncReservationAdjustment.setCustomerRentalAdjustment(reservationAdjustment);

        MP7865_SyncCustomerRentalAdjustment_001_Result syncResult =
                tools.performInforOperation(context, inforws::syncCustomerRentalAdjustmentOp, syncReservationAdjustment);

        return syncResult.getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    @Override
    public String deleteEquipmentReservationAdjustment(InforContext context, String customerRentalAdjustmentCode) throws InforException {
        MP7866_DeleteCustomerRentalAdjustment_001 deleteAdjustment = new MP7866_DeleteCustomerRentalAdjustment_001();
        deleteAdjustment.setCUSTOMERRENTALADJUSTMENTID(new CUSTOMERRENTALADJUSTMENTID_Type());
        deleteAdjustment.getCUSTOMERRENTALADJUSTMENTID().setCUSTOMERRENTALADJUSTMENTPK(customerRentalAdjustmentCode);

        tools.performInforOperation(context, inforws::deleteCustomerRentalAdjustmentOp, deleteAdjustment);

        return customerRentalAdjustmentCode;
    }
}
