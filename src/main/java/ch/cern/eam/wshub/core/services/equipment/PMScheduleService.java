package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentPMSchedule;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PMScheduleService {

    @Operation(logOperation = INFOR_OPERATION.EQP_PMSCH_C)
    String createEquipmentPMSchedule(InforContext context, EquipmentPMSchedule pmSchedule) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_PMSCH_D)
    String deleteEquipmentPMSchedule(InforContext context, EquipmentPMSchedule pmSchedule) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_PMSCH_U)
    String updateEquipmentPMSchedule(InforContext context, EquipmentPMSchedule pmSchedule) throws InforException;
}
