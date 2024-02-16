package ch.cern.eam.wshub.core.services.contractmanagement;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.contractmanagement.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.tools.InforException;

public interface EquipmentReservationAdjustmentService {

    @Operation(logOperation = INFOR_OPERATION.COMMENT_C, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "entityKeyCode", logDataReference2 = LogDataReferenceType.RESULT)
    String createEquipmentReservationAdjustment(InforContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.COMMENT_R)
    EquipmentReservationAdjustment readEquipmentReservationAdjustment(InforContext context, String number) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.COMMENT_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "entityKeyCode", logDataReference2 = LogDataReferenceType.RESULT)
    String updateEquipmentReservationAdjustment(InforContext context, EquipmentReservationAdjustment equipmentReservationAdjustment) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.COMMENT_D)
    String deleteEquipmentReservationAdjustment(InforContext context, String number) throws InforException;
}
