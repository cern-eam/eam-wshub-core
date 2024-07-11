package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentPMSchedule;
import ch.cern.eam.wshub.core.services.equipment.entities.ReleasedPMSchedule;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PMScheduleService {

    @Operation(logOperation = EAM_OPERATION.EQP_PMSCH_C)
    String createEquipmentPMSchedule(EAMContext context, EquipmentPMSchedule pmSchedule) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_PMSCH_D)
    String deleteEquipmentPMSchedule(EAMContext context, EquipmentPMSchedule pmSchedule) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_PMSCH_U)
    String updateEquipmentPMSchedule(EAMContext context, EquipmentPMSchedule pmSchedule) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_PMSCH_U)
    String updateReleasedPMSchedule(EAMContext context, ReleasedPMSchedule releasedPMSchedule) throws EAMException;
}
