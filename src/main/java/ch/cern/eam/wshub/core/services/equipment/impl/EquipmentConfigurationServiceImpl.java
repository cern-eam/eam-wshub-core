package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentConfigurationService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfiguration;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class EquipmentConfigurationServiceImpl implements EquipmentConfigurationService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public EquipmentConfigurationServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String createEquipmentConfiguration(InforContext context, EquipmentConfiguration equipmentConfiguration) throws InforException {
        return null;
    }

    @Override
    public String updateEquipmentConfiguration(InforContext context, EquipmentConfiguration equipmentConfiguration) throws InforException {
        return null;
    }

    @Override
    public EquipmentConfiguration readEquipmentConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException {
        return null;
    }

    @Override
    public EquipmentConfiguration readEquipmentDefaultConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException {
        return null;
    }

    @Override
    public String deleteEquipmentConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException {
        return null;
    }
}
