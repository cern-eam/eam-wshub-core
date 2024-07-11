package ch.cern.eam.wshub.core.services.contractmanagement;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.contractmanagement.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface EquipmentReservationAdjustmentService {

    @Operation(logOperation = EAM_OPERATION.EQP_RES_ADJ_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createEquipmentReservationAdjustment(EAMContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_RES_ADJ_R, logDataReference1 = LogDataReferenceType.INPUT)
    EquipmentReservationAdjustment readEquipmentReservationAdjustment(EAMContext context, String number) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_RES_ADJ_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateEquipmentReservationAdjustment(EAMContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_RES_ADJ_D)
    String deleteEquipmentReservationAdjustment(EAMContext context, String number) throws EAMException;
}
