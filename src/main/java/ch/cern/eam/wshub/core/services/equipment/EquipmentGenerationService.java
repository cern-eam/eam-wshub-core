package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentGenerationEntity;
import ch.cern.eam.wshub.core.tools.InforException;

public interface EquipmentGenerationService {
    @Operation(logOperation = INFOR_OPERATION.EQP_GEN_C)
    String createEquipmentGeneration(InforContext context, EquipmentGenerationEntity equipmentGeneration) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_GEN_G)
    String generateEquipmentGeneration(InforContext context, String equipmentGeneration) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_GEN_U)
    String updateEquipmentGeneration(InforContext context, EquipmentGenerationEntity equipmentGeneration) throws InforException;

    EquipmentGenerationEntity readEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException;

    EquipmentGenerationEntity readEquipmentGenerationDefault(InforContext context, String equipmentGenerationCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_GEN_D)
    String deleteEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException;

    String createEquipmentGenerationPreview(InforContext context, String equipmentGenerationCode) throws InforException;
}
