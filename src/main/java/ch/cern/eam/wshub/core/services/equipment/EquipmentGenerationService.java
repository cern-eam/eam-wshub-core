package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentGenerationEntity;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface EquipmentGenerationService {
    @Operation(logOperation = EAM_OPERATION.EQP_GEN_C)
    String createEquipmentGeneration(EAMContext context, EquipmentGenerationEntity equipmentGeneration) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_GEN_G)
    String generateEquipmentGeneration(EAMContext context, String equipmentGeneration) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_GEN_U)
    String updateEquipmentGeneration(EAMContext context, EquipmentGenerationEntity equipmentGeneration) throws EAMException;

    EquipmentGenerationEntity readEquipmentGeneration(EAMContext context, String equipmentGenerationCode) throws EAMException;

    EquipmentGenerationEntity readEquipmentGenerationDefault(EAMContext context, String equipmentGenerationCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_GEN_D)
    String deleteEquipmentGeneration(EAMContext context, String equipmentGenerationCode) throws EAMException;

    String createEquipmentGenerationPreview(EAMContext context, String equipmentGenerationCode) throws EAMException;
}
