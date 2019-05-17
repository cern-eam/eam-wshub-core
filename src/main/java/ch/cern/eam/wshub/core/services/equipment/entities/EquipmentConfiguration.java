package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentConfigurationService;
import ch.cern.eam.wshub.core.tools.InforException;

public class EquipmentConfiguration implements EquipmentConfigurationService {

    @Override
    public EquipmentConfiguration createEquipmentConfiguration(InforContext context, EquipmentConfiguration equipmentConfigurationCode) throws InforException {
        return null;
    }

    @Override
    public String updateEquipmentConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException {
        return null;
    }

    @Override
    public String readEquipmentConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException {
        return null;
    }

    @Override
    public String deleteEquipmentConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException {
        return null;
    }
}
