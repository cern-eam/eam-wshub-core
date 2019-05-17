package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfiguration;
import ch.cern.eam.wshub.core.tools.InforException;

public interface EquipmentConfigurationService {

    String createEquipmentConfiguration(InforContext context, EquipmentConfiguration equipmentConfiguration) throws InforException;

    String updateEquipmentConfiguration(InforContext context, EquipmentConfiguration equipmentConfiguration) throws InforException;

    EquipmentConfiguration readEquipmentConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException;

    EquipmentConfiguration readEquipmentDefaultConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException;

    String deleteEquipmentConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException;

}
