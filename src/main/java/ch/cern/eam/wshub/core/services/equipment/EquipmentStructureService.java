package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentStructure;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface EquipmentStructureService {

    @Operation(logOperation = EAM_OPERATION.EQP_STR_C)
    String addEquipmentToStructure(EAMContext context, EquipmentStructure equipmentStructure) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_STR_D)
    String removeEquipmentFromStructure(EAMContext context, EquipmentStructure equipmentStructure) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_STR_U)
    String updateEquipmentStructure(EAMContext context, EquipmentStructure equipmentStructure) throws EAMException;
}
