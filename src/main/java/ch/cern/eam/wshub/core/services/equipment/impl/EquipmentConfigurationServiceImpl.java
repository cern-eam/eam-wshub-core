package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentConfigurationService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationEntity;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.equipmentconfiguration_001.EquipmentConfiguration;
import net.datastream.schemas.mp_fields.EQUIPMENTCONFIGURATIONID_Type;
import net.datastream.schemas.mp_functions.mp3228_001.MP3228_GetEquipmentConfiguration_001;
import net.datastream.schemas.mp_results.mp3228_001.MP3228_GetEquipmentConfiguration_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;
import java.math.BigDecimal;

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
    public String createEquipmentConfiguration(InforContext context, EquipmentConfigurationEntity equipmentConfiguration) throws InforException {
        return null;
    }

    @Override
    public String updateEquipmentConfiguration(InforContext context, EquipmentConfigurationEntity equipmentConfiguration) throws InforException {
        return null;
    }

    private EquipmentConfiguration readInforEquipmentConfiguration(InforContext context, String equipmentConfigurationCode, BigDecimal revisionNum)
            throws InforException {

        MP3228_GetEquipmentConfiguration_001 getEquipmentConfiguration = new MP3228_GetEquipmentConfiguration_001();
        getEquipmentConfiguration.setEQUIPMENTCONFIGURATIONID(new EQUIPMENTCONFIGURATIONID_Type());
        getEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().setORGANIZATIONID(tools.getOrganization(context));
        getEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().setEQUIPMENTCONFIGURATIONCODE(equipmentConfigurationCode);
        getEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().setREVISIONNUM(tools.getDataTypeTools().encodeQuantity(revisionNum, "Revision number"));

        MP3228_GetEquipmentConfiguration_001_Result getEquipmentConfigurationResult =
            tools.performInforOperation(context, inforws::getEquipmentConfigurationOp, getEquipmentConfiguration);
        return getEquipmentConfigurationResult.getResultData().getEquipmentConfiguration();
    }

    @Override
    public EquipmentConfigurationEntity readEquipmentConfiguration(InforContext context, String equipmentConfigurationCode, BigDecimal revisionNum) throws InforException {
        EquipmentConfiguration inforEquipmentConfiguration = readInforEquipmentConfiguration(context, equipmentConfigurationCode, revisionNum);
        EquipmentConfigurationEntity equipmentConfiguration = tools.getInforFieldTools().transformInforObject(new EquipmentConfigurationEntity(), inforEquipmentConfiguration);

        tools.processRunnables(
                () -> equipmentConfiguration.setConfigurationCategoryDesc(tools.getFieldDescriptionsTools().readCategoryDesc(context, equipmentConfiguration.getConfigurationCategoryCode())),
                () -> equipmentConfiguration.setConfigurationClassDesc(tools.getFieldDescriptionsTools().readClassDesc(context, "OBJ", equipmentConfiguration.getConfigurationClassCode())),
                () -> equipmentConfiguration.setEquipmentStatusDesc(tools.getFieldDescriptionsTools().readUserCodeDesc(context, "OBST", equipmentConfiguration.getEquipmentStatusCode())),
                () -> equipmentConfiguration.setEquipmentTypeDesc(tools.getFieldDescriptionsTools().readUserCodeDesc(context, "OBTP", equipmentConfiguration.getEquipmentTypeCode()))
        );
        return equipmentConfiguration;
    }

    @Override
    public EquipmentConfigurationEntity readEquipmentDefaultConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException {
        return null;
    }

    @Override
    public String deleteEquipmentConfiguration(InforContext context, String equipmentConfigurationCode) throws InforException {
        return null;
    }
}
