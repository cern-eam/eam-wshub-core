package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationEntity;
import ch.cern.eam.wshub.core.tools.InforException;

import java.math.BigDecimal;

public interface EquipmentConfigurationService {

    String createEquipmentConfiguration(InforContext context, EquipmentConfigurationEntity equipmentConfiguration) throws InforException;

    String updateEquipmentConfiguration(InforContext context, EquipmentConfigurationEntity equipmentConfiguration) throws InforException;

    EquipmentConfigurationEntity readEquipmentConfiguration(InforContext context, String equipmentConfigurationCode, BigDecimal revisionNum) throws InforException;

    EquipmentConfigurationEntity readEquipmentDefaultConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException;

    String deleteEquipmentConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException;

}
