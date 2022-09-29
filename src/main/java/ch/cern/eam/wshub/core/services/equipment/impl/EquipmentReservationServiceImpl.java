package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentReservationService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentReservation;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.customerrental_001.CustomerRental;
import net.datastream.schemas.mp_fields.CUSTOMERRENTALID_Type;
import net.datastream.schemas.mp_functions.mp7832_001.MP7832_GetCustomerRental_001;
import net.datastream.schemas.mp_functions.mp7833_001.MP7833_AddCustomerRental_001;
import net.datastream.schemas.mp_functions.mp7834_001.MP7834_SyncCustomerRental_001;
import net.datastream.schemas.mp_functions.mp7835_001.MP7835_DeleteCustomerRental_001;
import net.datastream.schemas.mp_results.mp7832_001.MP7832_GetCustomerRental_001_Result;
import net.datastream.schemas.mp_results.mp7833_001.MP7833_AddCustomerRental_001_Result;
import net.datastream.schemas.mp_results.mp7834_001.MP7834_SyncCustomerRental_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class EquipmentReservationServiceImpl implements EquipmentReservationService {

    private ApplicationData applicationData;
    private Tools tools;
    private InforWebServicesPT inforws;

    public EquipmentReservationServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String createEquipmentReservation(InforContext context, EquipmentReservation reservationParam) throws InforException {
        reservationParam.setCode("0");

        CustomerRental reservation = new CustomerRental();
        tools.getInforFieldTools().transformWSHubObject(reservation, reservationParam, context);

        MP7833_AddCustomerRental_001 addReservation = new MP7833_AddCustomerRental_001();
        addReservation.setCustomerRental(reservation);

        MP7833_AddCustomerRental_001_Result result =
                tools.performInforOperation(context, inforws::addCustomerRentalOp, addReservation);

        return result.getResultData().getCUSTOMERRENTALID().getCUSTOMERRENTALCODE();
    }

    @Override
    public EquipmentReservation readEquipmentReservation(InforContext context, String customerRentalCode) throws InforException {
        CustomerRental reservation = readEquipmentReservationInfor(context, customerRentalCode);

        return tools.getInforFieldTools().transformInforObject(new EquipmentReservation(), reservation);
    }

    private CustomerRental readEquipmentReservationInfor(InforContext context, String customerRentalCode) throws InforException {
        MP7832_GetCustomerRental_001 getReservation = new MP7832_GetCustomerRental_001();
        getReservation.setCUSTOMERRENTALID(new CUSTOMERRENTALID_Type());
        getReservation.getCUSTOMERRENTALID().setCUSTOMERRENTALCODE(customerRentalCode);
        getReservation.getCUSTOMERRENTALID().setORGANIZATIONID(tools.getOrganization(context));

        MP7832_GetCustomerRental_001_Result result =
                tools.performInforOperation(context, inforws::getCustomerRentalOp, getReservation);

        return result.getResultData().getCustomerRental();
    }

    @Override
    public String updateEquipmentReservation(InforContext context, EquipmentReservation reservationParam) throws InforException {
        CustomerRental reservation = readEquipmentReservationInfor(context, reservationParam.getCode());
        tools.getInforFieldTools().transformWSHubObject(reservation, reservationParam, context);

        MP7834_SyncCustomerRental_001 syncReservation = new MP7834_SyncCustomerRental_001();
        syncReservation.setCustomerRental(reservation);

        MP7834_SyncCustomerRental_001_Result syncResult =
                tools.performInforOperation(context, inforws::syncCustomerRentalOp, syncReservation);

        return syncResult.getResultData().getCUSTOMERRENTALID().getCUSTOMERRENTALCODE();
    }

    @Override
    public String deleteEquipmentReservation(InforContext context, String customerRentalCode) throws InforException {
        MP7835_DeleteCustomerRental_001 deleteReservation = new MP7835_DeleteCustomerRental_001();
        deleteReservation.setCUSTOMERRENTALID(new CUSTOMERRENTALID_Type());
        deleteReservation.getCUSTOMERRENTALID().setCUSTOMERRENTALCODE(customerRentalCode);
        deleteReservation.getCUSTOMERRENTALID().setORGANIZATIONID(tools.getOrganization(context));

        tools.performInforOperation(context, inforws::deleteCustomerRentalOp, deleteReservation);

        return customerRentalCode;
    }
}
