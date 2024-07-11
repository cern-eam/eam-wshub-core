package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.EquipmentConfigurationService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationEntity;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationId;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.DataTypeTools;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.math.BigDecimal;
import java.util.List;

public class EquipmentConfigurationServiceImpl implements EquipmentConfigurationService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public EquipmentConfigurationServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    @Override
    public String createEquipmentConfiguration(EAMContext context, EquipmentConfigurationEntity equipmentConfiguration) throws EAMException {
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

        EquipmentConfiguration equipmentConfigurationFinal = tools.getEAMFieldTools().transformWSHubObject(new EquipmentConfiguration(), equipmentConfiguration, context);

        final MP3225_AddEquipmentConfiguration_001 mp3225_addEquipmentConfiguration_001 = new MP3225_AddEquipmentConfiguration_001();
        mp3225_addEquipmentConfiguration_001.setEquipmentConfiguration(equipmentConfigurationFinal);

        MP3225_AddEquipmentConfiguration_001_Result result = tools.performEAMOperation(context, eamws::addEquipmentConfigurationOp, mp3225_addEquipmentConfiguration_001);
        return result.getResultData().getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE();
    }

    @Override
    public String updateEquipmentConfiguration(EAMContext context, EquipmentConfigurationEntity equipmentConfiguration) throws EAMException {
        final EquipmentConfiguration equipmentConfigurationOld = readEAMEquipmentConfiguration(context, new EquipmentConfigurationId(equipmentConfiguration.getEquipmentConfigCode(), equipmentConfiguration.getRevisionNum()));
        final EquipmentConfiguration equipmentConfigurationNew = tools.getEAMFieldTools().transformWSHubObject(equipmentConfigurationOld, equipmentConfiguration, context);

        final MP3226_SyncEquipmentConfiguration_001 syncEquipmentConfiguration = new MP3226_SyncEquipmentConfiguration_001();
        syncEquipmentConfiguration.setEquipmentConfiguration(equipmentConfigurationNew);
        MP3226_SyncEquipmentConfiguration_001_Result result = tools.performEAMOperation(context, eamws::syncEquipmentConfigurationOp, syncEquipmentConfiguration);
        return result.getResultData().getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE();
    }

    private EquipmentConfiguration readEAMEquipmentConfiguration(EAMContext context, EquipmentConfigurationId equipmentConfigurationId)
            throws EAMException {
        final MP3228_GetEquipmentConfiguration_001 mp3228_getEquipmentConfiguration_001 = tools.getEAMFieldTools().transformWSHubObject(new MP3228_GetEquipmentConfiguration_001(), equipmentConfigurationId, context);
        MP3228_GetEquipmentConfiguration_001_Result getEquipmentConfigurationResult =
            tools.performEAMOperation(context, eamws::getEquipmentConfigurationOp, mp3228_getEquipmentConfiguration_001);
        return getEquipmentConfigurationResult.getResultData().getEquipmentConfiguration();
    }

    @Override
    public EquipmentConfigurationEntity readEquipmentConfiguration(EAMContext context, EquipmentConfigurationId equipmentConfigurationId) throws EAMException {
        EquipmentConfiguration eamEquipmentConfiguration = readEAMEquipmentConfiguration(context, equipmentConfigurationId);
        EquipmentConfigurationEntity equipmentConfiguration = tools.getEAMFieldTools().transformEAMObject(new EquipmentConfigurationEntity(), eamEquipmentConfiguration, context);

        tools.processRunnables(
                () -> equipmentConfiguration.setConfigurationCategoryDesc(tools.getFieldDescriptionsTools().readCategoryDesc(context, equipmentConfiguration.getConfigurationCategoryCode())),
                () -> equipmentConfiguration.setConfigurationClassDesc(tools.getFieldDescriptionsTools().readClassDesc(context, "OBJ", equipmentConfiguration.getConfigurationClassCode())),
                () -> equipmentConfiguration.setEquipmentStatusDesc(tools.getFieldDescriptionsTools().readUserCodeDesc(context, "OBST", equipmentConfiguration.getEquipmentStatusCode())),
                () -> equipmentConfiguration.setEquipmentTypeDesc(tools.getFieldDescriptionsTools().readUserCodeDesc(context, "OBTP", equipmentConfiguration.getEquipmentTypeCode()))
        );
        return equipmentConfiguration;
    }

    @Override
    public EquipmentConfigurationDefault readEquipmentDefaultConfiguration(EAMContext context) throws EAMException {
        MP3224_GetEquipmentConfigurationDefault_001 readEquipmentDefaultConfiguration = new MP3224_GetEquipmentConfigurationDefault_001();
        readEquipmentDefaultConfiguration.setORGANIZATIONID(tools.getOrganization(context));
        final MP3224_GetEquipmentConfigurationDefault_001_Result mp3224_getEquipmentConfigurationDefault_001_result = tools.performEAMOperation(context, eamws::getEquipmentConfigurationDefaultOp, readEquipmentDefaultConfiguration);
        return mp3224_getEquipmentConfigurationDefault_001_result.getResultData().getEquipmentConfigurationDefault();
    }

    @Override
    public String deleteEquipmentConfiguration(EAMContext context, EquipmentConfigurationId equipmentConfigurationId) throws EAMException {
        final MP3227_DeleteEquipmentConfiguration_001 deleteEquipmentConfigurationOp = tools.getEAMFieldTools().transformWSHubObject(new MP3227_DeleteEquipmentConfiguration_001(), equipmentConfigurationId, context);
        final MP3227_DeleteEquipmentConfiguration_001_Result mp3227_deleteEquipmentConfiguration_001_result = tools.performEAMOperation(context, eamws::deleteEquipmentConfigurationOp, deleteEquipmentConfigurationOp);
        return mp3227_deleteEquipmentConfiguration_001_result.getResultData().getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE();
    }

    @Override
    public BatchResponse<String> createEquipmentConfigurationBatch(EAMContext context, List<EquipmentConfigurationEntity> equipmentConfigurationList) throws EAMException {
        return tools.batchOperation(context, this::createEquipmentConfiguration, equipmentConfigurationList);
    }

    @Override
    public BatchResponse<EquipmentConfigurationEntity> readEquipmentConfigurationBatch(EAMContext context, List<EquipmentConfigurationId> equipmentConfigurationIdList) throws EAMException {
        return tools.batchOperation(context, this::readEquipmentConfiguration, equipmentConfigurationIdList);
    }

    @Override
    public BatchResponse<String> updateEquipmentConfigurationBatch(EAMContext context, List<EquipmentConfigurationEntity> equipmentConfigurationList) throws EAMException {
        return tools.batchOperation(context, this::updateEquipmentConfiguration, equipmentConfigurationList);
    }

    @Override
    public BatchResponse<String> deleteEquipmentConfigurationBatch(EAMContext context, List<EquipmentConfigurationId> equipmentConfigurationIdList) throws EAMException {
        return tools.batchOperation(context, this::deleteEquipmentConfiguration, equipmentConfigurationIdList);
    }
}
