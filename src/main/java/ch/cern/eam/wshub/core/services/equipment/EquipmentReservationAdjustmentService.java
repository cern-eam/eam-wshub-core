package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.tools.InforException;

public interface EquipmentReservationAdjustmentService {

    @Operation(logOperation = INFOR_OPERATION.EQP_RES_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createEquipmentReservationAdjustment(InforContext context, EquipmentReservationAdjustment adjustmentParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_RES_R, logDataReference1 = LogDataReferenceType.INPUT)
    EquipmentReservationAdjustment readEquipmentReservationAdjustment(InforContext context, String customerRentalAdjustmentCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_RES_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateEquipmentReservationAdjustment(InforContext context, EquipmentReservationAdjustment adjustmentParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_RES_D)
    String deleteEquipmentReservationAdjustment(InforContext context, String customerRentalAdjustmentCode) throws InforException;

}
