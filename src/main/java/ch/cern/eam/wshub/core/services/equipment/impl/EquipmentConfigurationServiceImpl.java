package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.EquipmentConfigurationService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationEntity;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationId;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.DataTypeTools;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.equipmentconfiguration_001.EquipmentConfiguration;
import net.datastream.schemas.mp_entities.equipmentconfigurationdefault_001.EquipmentConfigurationDefault;
import net.datastream.schemas.mp_fields.EQUIPMENTCONFIGURATIONID_Type;
import net.datastream.schemas.mp_functions.mp3224_001.MP3224_GetEquipmentConfigurationDefault_001;
import net.datastream.schemas.mp_functions.mp3225_001.MP3225_AddEquipmentConfiguration_001;
import net.datastream.schemas.mp_functions.mp3226_001.MP3226_SyncEquipmentConfiguration_001;
import net.datastream.schemas.mp_functions.mp3227_001.MP3227_DeleteEquipmentConfiguration_001;
import net.datastream.schemas.mp_functions.mp3228_001.MP3228_GetEquipmentConfiguration_001;
import net.datastream.schemas.mp_results.mp3224_001.MP3224_GetEquipmentConfigurationDefault_001_Result;
import net.datastream.schemas.mp_results.mp3227_001.MP3227_DeleteEquipmentConfiguration_001_Result;
import net.datastream.schemas.mp_results.mp3228_001.MP3228_GetEquipmentConfiguration_001_Result;
import net.datastream.schemas.mp_results.mp3225_001.MP3225_AddEquipmentConfiguration_001_Result;
import net.datastream.schemas.mp_results.mp3226_001.MP3226_SyncEquipmentConfiguration_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.math.BigDecimal;
import java.util.List;

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
        //Apply defaults
        final EquipmentConfigurationDefault equipmentConfigurationDefault = readEquipmentDefaultConfiguration(context);
        if (equipmentConfiguration.getRevisionNum() == null && equipmentConfigurationDefault.getREVISIONNUM() != null) {
            equipmentConfiguration.setRevisionNum(equipmentConfigurationDefault.getREVISIONNUM().getVALUE());
        }
        if (equipmentConfiguration.getEquipmentConfigStatusCode() == null && equipmentConfigurationDefault.getEQUIPMENTCONFIGSTATUS() != null) {
            equipmentConfiguration.setEquipmentConfigStatusCode(equipmentConfigurationDefault.getEQUIPMENTCONFIGSTATUS().getSTATUSCODE());
        }
        if (equipmentConfiguration.getEquipmentStatusCode() == null && equipmentConfigurationDefault.getSTATUS() != null) {
            equipmentConfiguration.setEquipmentStatusCode(equipmentConfigurationDefault.getSTATUS().getSTATUSCODE());
        }
        if (equipmentConfiguration.getEquipmentTypeCode() == null && equipmentConfigurationDefault.getTYPE() != null) {
            equipmentConfiguration.setEquipmentTypeCode(equipmentConfigurationDefault.getTYPE().getTYPECODE());
        }
        if (equipmentConfiguration.getOrganizationCode() == null && equipmentConfigurationDefault.getORGANIZATIONID() != null) {
            equipmentConfiguration.setOrganizationCode(equipmentConfigurationDefault.getORGANIZATIONID().getORGANIZATIONCODE());
        }
        if (equipmentConfiguration.getAutoNumber() == null && equipmentConfigurationDefault.getAUTONUMBER() != null) {
            equipmentConfiguration.setAutoNumber(DataTypeTools.decodeBoolean(equipmentConfigurationDefault.getAUTONUMBER()));
        }

        EquipmentConfiguration equipmentConfigurationFinal = tools.getInforFieldTools().transformWSHubObject(new EquipmentConfiguration(), equipmentConfiguration, context);

        final MP3225_AddEquipmentConfiguration_001 mp3225_addEquipmentConfiguration_001 = new MP3225_AddEquipmentConfiguration_001();
        mp3225_addEquipmentConfiguration_001.setEquipmentConfiguration(equipmentConfigurationFinal);

        MP3225_AddEquipmentConfiguration_001_Result result = tools.performInforOperation(context, inforws::addEquipmentConfigurationOp, mp3225_addEquipmentConfiguration_001);
        return result.getResultData().getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE();
    }

    @Override
    public String updateEquipmentConfiguration(InforContext context, EquipmentConfigurationEntity equipmentConfiguration) throws InforException {
        final EquipmentConfiguration equipmentConfigurationOld = readInforEquipmentConfiguration(context, new EquipmentConfigurationId(equipmentConfiguration.getEquipmentConfigCode(), equipmentConfiguration.getRevisionNum()));
        final EquipmentConfiguration equipmentConfigurationNew = tools.getInforFieldTools().transformWSHubObject(equipmentConfigurationOld, equipmentConfiguration, context);

        final MP3226_SyncEquipmentConfiguration_001 syncEquipmentConfiguration = new MP3226_SyncEquipmentConfiguration_001();
        syncEquipmentConfiguration.setEquipmentConfiguration(equipmentConfigurationNew);
        MP3226_SyncEquipmentConfiguration_001_Result result = tools.performInforOperation(context, inforws::syncEquipmentConfigurationOp, syncEquipmentConfiguration);
        return result.getResultData().getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE();
    }

    private EquipmentConfiguration readInforEquipmentConfiguration(InforContext context, EquipmentConfigurationId equipmentConfigurationId)
            throws InforException {
        final MP3228_GetEquipmentConfiguration_001 mp3228_getEquipmentConfiguration_001 = tools.getInforFieldTools().transformWSHubObject(new MP3228_GetEquipmentConfiguration_001(), equipmentConfigurationId, context);
        MP3228_GetEquipmentConfiguration_001_Result getEquipmentConfigurationResult =
            tools.performInforOperation(context, inforws::getEquipmentConfigurationOp, mp3228_getEquipmentConfiguration_001);
        return getEquipmentConfigurationResult.getResultData().getEquipmentConfiguration();
    }

    @Override
    public EquipmentConfigurationEntity readEquipmentConfiguration(InforContext context, EquipmentConfigurationId equipmentConfigurationId) throws InforException {
        EquipmentConfiguration inforEquipmentConfiguration = readInforEquipmentConfiguration(context, equipmentConfigurationId);
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
    public EquipmentConfigurationDefault readEquipmentDefaultConfiguration(InforContext context) throws InforException {
        MP3224_GetEquipmentConfigurationDefault_001 readEquipmentDefaultConfiguration = new MP3224_GetEquipmentConfigurationDefault_001();
        readEquipmentDefaultConfiguration.setORGANIZATIONID(tools.getOrganization(context));
        final MP3224_GetEquipmentConfigurationDefault_001_Result mp3224_getEquipmentConfigurationDefault_001_result = tools.performInforOperation(context, inforws::getEquipmentConfigurationDefaultOp, readEquipmentDefaultConfiguration);
        return mp3224_getEquipmentConfigurationDefault_001_result.getResultData().getEquipmentConfigurationDefault();
    }

    @Override
    public String deleteEquipmentConfiguration(InforContext context, EquipmentConfigurationId equipmentConfigurationId) throws InforException {
        final MP3227_DeleteEquipmentConfiguration_001 deleteEquipmentConfigurationOp = tools.getInforFieldTools().transformWSHubObject(new MP3227_DeleteEquipmentConfiguration_001(), equipmentConfigurationId, context);
        final MP3227_DeleteEquipmentConfiguration_001_Result mp3227_deleteEquipmentConfiguration_001_result = tools.performInforOperation(context, inforws::deleteEquipmentConfigurationOp, deleteEquipmentConfigurationOp);
        return mp3227_deleteEquipmentConfiguration_001_result.getResultData().getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE();
    }

    @Override
    public BatchResponse<String> createEquipmentConfigurationBatch(InforContext context, List<EquipmentConfigurationEntity> equipmentConfigurationList) throws InforException {
        return tools.batchOperation(context, this::createEquipmentConfiguration, equipmentConfigurationList);
    }

    @Override
    public BatchResponse<EquipmentConfigurationEntity> readEquipmentConfigurationBatch(InforContext context, List<EquipmentConfigurationId> equipmentConfigurationIdList) throws InforException {
        return tools.batchOperation(context, this::readEquipmentConfiguration, equipmentConfigurationIdList);
    }

    @Override
    public BatchResponse<String> updateEquipmentConfigurationBatch(InforContext context, List<EquipmentConfigurationEntity> equipmentConfigurationList) throws InforException {
        return tools.batchOperation(context, this::updateEquipmentConfiguration, equipmentConfigurationList);
    }

    @Override
    public BatchResponse<String> deleteEquipmentConfigurationBatch(InforContext context, List<EquipmentConfigurationId> equipmentConfigurationIdList) throws InforException {
        return tools.batchOperation(context, this::deleteEquipmentConfiguration, equipmentConfigurationIdList);
    }
}
