package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentReservation;
import ch.cern.eam.wshub.core.tools.InforException;

public interface EquipmentReservationService {

    @Operation(logOperation = INFOR_OPERATION.EQP_RES_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createEquipmentReservation(InforContext context, EquipmentReservation reservationParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_RES_R, logDataReference1 = LogDataReferenceType.INPUT)
    EquipmentReservation readEquipmentReservation(InforContext context, String customerRentalCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_RES_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateEquipmentReservation(InforContext context, EquipmentReservation reservationParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_RES_D)
    String deleteEquipmentReservation(InforContext context, String customerRentalCode) throws InforException;

}
