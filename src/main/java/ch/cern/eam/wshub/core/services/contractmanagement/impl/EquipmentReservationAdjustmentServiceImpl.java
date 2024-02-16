package ch.cern.eam.wshub.core.services.contractmanagement.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.contractmanagement.EquipmentReservationAdjustmentService;
import ch.cern.eam.wshub.core.services.contractmanagement.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.CUSTOMERRENTALADJUSTMENTID_Type;
import net.datastream.schemas.mp_functions.mp7863_001.MP7863_GetCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_functions.mp7864_001.MP7864_AddCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_functions.mp7865_001.MP7865_SyncCustomerRentalAdjustment_001;
import net.datastream.schemas.mp_functions.mp7866_001.MP7866_DeleteCustomerRentalAdjustment_001;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;

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
        return tools.performInforOperation(context, inforws::addCustomerRentalAdjustmentOp, createAddOperation(context, equipmentReservationAdjustment))
                .getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    @Override
    public EquipmentReservationAdjustment readEquipmentReservationAdjustment(InforContext context, String number) throws InforException {
        return tools.getInforFieldTools()
                .transformInforObject(new EquipmentReservationAdjustment(),
                        tools.performInforOperation(context, inforws::getCustomerRentalAdjustmentOp, createGetOperation(number))
                                .getResultData().getCustomerRentalAdjustment(), context);
    }

    @Override
    public String updateEquipmentReservationAdjustment(InforContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws InforException {
        return tools.performInforOperation(context, inforws::syncCustomerRentalAdjustmentOp, createUpdateOperation(context, equipmentReservationAdjustment))
                .getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    @Override
    public String deleteEquipmentReservationAdjustment(InforContext context, String number) throws InforException {
        return tools.performInforOperation(context, inforws::deleteCustomerRentalAdjustmentOp, createDeleteOperation(number)).getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    private MP7864_AddCustomerRentalAdjustment_001 createAddOperation(InforContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws InforException {
        MP7864_AddCustomerRentalAdjustment_001 addCustomerRentalAdjustment = new MP7864_AddCustomerRentalAdjustment_001();
        addCustomerRentalAdjustment.setCustomerRentalAdjustment(tools.getInforFieldTools()
                .transformWSHubObject(new net.datastream.schemas.mp_entities.customerrentaladjustment_001.CustomerRentalAdjustment(), validateCreateObject(equipmentReservationAdjustment), context));
        return addCustomerRentalAdjustment;
    }

    private MP7863_GetCustomerRentalAdjustment_001 createGetOperation(String number) {
        MP7863_GetCustomerRentalAdjustment_001 getCustomerRentalAdjustment = new MP7863_GetCustomerRentalAdjustment_001();
        CUSTOMERRENTALADJUSTMENTID_Type idType = new CUSTOMERRENTALADJUSTMENTID_Type();
        idType.setCUSTOMERRENTALADJUSTMENTPK(number);
        getCustomerRentalAdjustment.setCUSTOMERRENTALADJUSTMENTID(idType);
        return getCustomerRentalAdjustment;
    }

    private MP7865_SyncCustomerRentalAdjustment_001 createUpdateOperation(InforContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws InforException {
        MP7865_SyncCustomerRentalAdjustment_001 syncCustomerRentalAdjustment = new MP7865_SyncCustomerRentalAdjustment_001();
        syncCustomerRentalAdjustment.setCustomerRentalAdjustment(tools.getInforFieldTools()
                .transformWSHubObject(new net.datastream.schemas.mp_entities.customerrentaladjustment_001.CustomerRentalAdjustment(), validateUpdateObject(equipmentReservationAdjustment), context));
        return syncCustomerRentalAdjustment;
    }

    private MP7866_DeleteCustomerRentalAdjustment_001 createDeleteOperation(String number) {
        MP7866_DeleteCustomerRentalAdjustment_001 deleteCustomerRentalAdjustment = new MP7866_DeleteCustomerRentalAdjustment_001();
        CUSTOMERRENTALADJUSTMENTID_Type idType = new CUSTOMERRENTALADJUSTMENTID_Type();
        idType.setCUSTOMERRENTALADJUSTMENTPK(number);
        deleteCustomerRentalAdjustment.setCUSTOMERRENTALADJUSTMENTID(idType);
        return deleteCustomerRentalAdjustment;
    }

    private EquipmentReservationAdjustment validateCreateObject(EquipmentReservationAdjustment equipmentReservationAdjustment) {
        validateUpdateObject(equipmentReservationAdjustment);
        if (isEmpty(equipmentReservationAdjustment.getCode())) {
            equipmentReservationAdjustment.setCode("1");
        }
        return equipmentReservationAdjustment;
    }

    private EquipmentReservationAdjustment validateUpdateObject(EquipmentReservationAdjustment equipmentReservationAdjustment) {
        if (isEmpty(equipmentReservationAdjustment.getStatusRCode())) {
            equipmentReservationAdjustment.setStatusRCode(equipmentReservationAdjustment.getStatusCode());
        }
        if (isEmpty(equipmentReservationAdjustment.getTypeRCode())) {
            equipmentReservationAdjustment.setTypeRCode(equipmentReservationAdjustment.getTypeCode());
        }
        return equipmentReservationAdjustment;
    }
}
