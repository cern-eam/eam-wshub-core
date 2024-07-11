package ch.cern.eam.wshub.core.services.contractmanagement.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.contractmanagement.EquipmentReservationAdjustmentService;
import ch.cern.eam.wshub.core.services.contractmanagement.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.services.equipment.EquipmentReservationService;
import ch.cern.eam.wshub.core.services.equipment.impl.EquipmentReservationServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class EquipmentReservationAdjustmentServiceImpl implements EquipmentReservationAdjustmentService {
    private final Tools tools;
    private final EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    private EquipmentReservationService equipmentReservationService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public EquipmentReservationAdjustmentServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
        this.equipmentReservationService = new EquipmentReservationServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
    }

    @Override
    public String createEquipmentReservationAdjustment(EAMContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws EAMException {
        MP7864_AddCustomerRentalAdjustment_001 addCustomerRentalAdjustment = new MP7864_AddCustomerRentalAdjustment_001();
        CustomerRentalAdjustment customerRentalAdjustment = tools.getEAMFieldTools().transformWSHubObject(createDefaultEquipmentReservationAdjustment(), equipmentReservationAdjustment, context);
        addCustomerRentalAdjustment.setCustomerRentalAdjustment(customerRentalAdjustment);
        MP7864_AddCustomerRentalAdjustment_001_Result addOperationResult = tools.performEAMOperation(context, eamws::addCustomerRentalAdjustmentOp, addCustomerRentalAdjustment);

        // Since EAM always returns a 0 as the primary key of the created Equipment Reservation Adjustment, we have to use an alternative method to retrieve it
        // The solution might return incorrect results if multiple clients are creating requests at the same time, but it's still
        //better than the alternative of always not having the ID (for CERN's use case)
        List<EquipmentReservationAdjustment> equipmentReservationAdjustments = equipmentReservationService.readEquipmentReservationAdjustments(context, equipmentReservationAdjustment.getCustomerRentalCode());
        equipmentReservationAdjustments.removeIf(s ->
                !Objects.equals(dateFormat.format(s.getDate()), dateFormat.format(equipmentReservationAdjustment.getDate()))
                || (s.getRate() == null ? equipmentReservationAdjustment.getRate() != null
                        : s.getRate().compareTo(equipmentReservationAdjustment.getRate()) != 0)
                || !Objects.equals(s.getAdjustmentCode(), equipmentReservationAdjustment.getAdjustmentCode())
            );
        if (equipmentReservationAdjustments.isEmpty()) {
            throw new EAMException("Could not retrieve Equipment Reservation Adjustment id.", null, null);
        }
        return equipmentReservationAdjustments.get(0).getCode();
    }

    @Override
    public EquipmentReservationAdjustment readEquipmentReservationAdjustment(EAMContext context, String number) throws EAMException {
        CustomerRentalAdjustment customerRentalAdjustment = readCustomerRentalAdjustment(context, number);
        return tools.getEAMFieldTools().transformEAMObject(new EquipmentReservationAdjustment(), customerRentalAdjustment, context);
    }

    @Override
    public String updateEquipmentReservationAdjustment(EAMContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws EAMException {
        MP7865_SyncCustomerRentalAdjustment_001 syncCustomerRentalAdjustment = new MP7865_SyncCustomerRentalAdjustment_001();
        CustomerRentalAdjustment prevCustomerRentalAdjustment = readCustomerRentalAdjustment(context, equipmentReservationAdjustment.getCode());
        CustomerRentalAdjustment newCustomerRentalAdjustment = tools.getEAMFieldTools().transformWSHubObject(prevCustomerRentalAdjustment, equipmentReservationAdjustment, context);
        syncCustomerRentalAdjustment.setCustomerRentalAdjustment(newCustomerRentalAdjustment);
        MP7865_SyncCustomerRentalAdjustment_001_Result updateOperationResult = tools.performEAMOperation(context, eamws::syncCustomerRentalAdjustmentOp, syncCustomerRentalAdjustment);
        return updateOperationResult.getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    @Override
    public String deleteEquipmentReservationAdjustment(EAMContext context, String number) throws EAMException {
        MP7866_DeleteCustomerRentalAdjustment_001 deleteCustomerRentalAdjustment = new MP7866_DeleteCustomerRentalAdjustment_001();
        CUSTOMERRENTALADJUSTMENTID_Type idType = new CUSTOMERRENTALADJUSTMENTID_Type();
        idType.setCUSTOMERRENTALADJUSTMENTPK(number);
        deleteCustomerRentalAdjustment.setCUSTOMERRENTALADJUSTMENTID(idType);
        MP7866_DeleteCustomerRentalAdjustment_001_Result deleteOperationResult = tools.performEAMOperation(context, eamws::deleteCustomerRentalAdjustmentOp, deleteCustomerRentalAdjustment);
        return deleteOperationResult.getResultData().getCUSTOMERRENTALADJUSTMENTID().getCUSTOMERRENTALADJUSTMENTPK();
    }

    private CustomerRentalAdjustment readCustomerRentalAdjustment(EAMContext context, String number) throws EAMException {
        MP7863_GetCustomerRentalAdjustment_001 getCustomerRentalAdjustment = new MP7863_GetCustomerRentalAdjustment_001();
        CUSTOMERRENTALADJUSTMENTID_Type idType = new CUSTOMERRENTALADJUSTMENTID_Type();
        idType.setCUSTOMERRENTALADJUSTMENTPK(number);
        getCustomerRentalAdjustment.setCUSTOMERRENTALADJUSTMENTID(idType);
        MP7863_GetCustomerRentalAdjustment_001_Result getOperationResult = tools.performEAMOperation(context, eamws::getCustomerRentalAdjustmentOp, getCustomerRentalAdjustment);
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
