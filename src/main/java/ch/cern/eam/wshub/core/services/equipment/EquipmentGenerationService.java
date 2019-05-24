package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentGenerationEntity;
import ch.cern.eam.wshub.core.tools.InforException;

public interface EquipmentGenerationService {

    String createEquipmentGeneration(InforContext context, EquipmentGenerationEntity equipmentGeneration) throws InforException;

    String generateEquipmentGeneration(InforContext context, String equipmentGeneration) throws InforException;

    String updateEquipmentGeneration(InforContext context, EquipmentGenerationEntity equipmentGeneration) throws InforException;

    EquipmentGenerationEntity readEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException;

    EquipmentGenerationEntity readEquipmentGenerationDefault(InforContext context, String equipmentGenerationCode) throws InforException;

    String deleteEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException;

}
