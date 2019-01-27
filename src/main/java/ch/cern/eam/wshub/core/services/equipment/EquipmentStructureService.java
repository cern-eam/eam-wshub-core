package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentStructure;
import ch.cern.eam.wshub.core.tools.InforException;

public interface EquipmentStructureService {

    @Operation(logOperation = INFOR_OPERATION.EQP_STR_C)
    String addEquipmentToStructure(InforContext context, EquipmentStructure equipmentStructure) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_STR_D)
    String removeEquipmentFromStructure(InforContext context, EquipmentStructure equipmentStructure) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_STR_U)
    String updateEquipmentStructure(InforContext context, EquipmentStructure equipmentStructure) throws InforException;
}
