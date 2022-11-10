package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EqpMeterReading;
import ch.cern.eam.wshub.core.tools.InforException;

public interface EquipmentMeterReadingService {
    @Operation(logOperation = INFOR_OPERATION.EQP_METR_C)
    String createEquipmentMeterReading(InforContext context, EqpMeterReading eqpMeterReading,
                                       boolean rolloverAllowed) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_METR_R)
    EqpMeterReading readEquipmentMeterReading(InforContext context, String readingCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_METR_U)
    String updateEquipmentMeterReading(InforContext context, EqpMeterReading eqpMeterReading) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_METR_D)
    String deleteEquipmentMeterReading(InforContext context, String readingCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_METR_RD)
    EqpMeterReading readEquipmentMeterReadingDefault(InforContext context, EqpMeterReading eqpMeterReading) throws InforException;

}
