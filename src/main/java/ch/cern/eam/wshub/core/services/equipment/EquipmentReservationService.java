package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.contractmanagement.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentReservation;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;

public interface EquipmentReservationService {

    @Operation(logOperation = EAM_OPERATION.EQP_RES_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createEquipmentReservation(EAMContext context, EquipmentReservation reservationParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_RES_R, logDataReference1 = LogDataReferenceType.INPUT)
    EquipmentReservation readEquipmentReservation(EAMContext context, String customerRentalCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_RES_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateEquipmentReservation(EAMContext context, EquipmentReservation reservationParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_RES_D)
    String deleteEquipmentReservation(EAMContext context, String customerRentalCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_RES_RA, logDataReference1 = LogDataReferenceType.INPUT)
    List<EquipmentReservationAdjustment> readEquipmentReservationAdjustments(EAMContext context, String customerRentalCode) throws EAMException;

}
