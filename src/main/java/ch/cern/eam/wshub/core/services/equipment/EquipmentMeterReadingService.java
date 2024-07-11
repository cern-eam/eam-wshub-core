package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EqpMeterReading;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface EquipmentMeterReadingService {
    @Operation(logOperation = EAM_OPERATION.EQP_METR_C)
    String createEquipmentMeterReading(EAMContext context, EqpMeterReading eqpMeterReading,
                                       boolean rolloverAllowed) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_METR_R)
    EqpMeterReading readEquipmentMeterReading(EAMContext context, String readingCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_METR_U)
    String updateEquipmentMeterReading(EAMContext context, EqpMeterReading eqpMeterReading) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_METR_D)
    String deleteEquipmentMeterReading(EAMContext context, String readingCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_METR_RD)
    EqpMeterReading readEquipmentMeterReadingDefault(EAMContext context, EqpMeterReading eqpMeterReading) throws EAMException;

}
