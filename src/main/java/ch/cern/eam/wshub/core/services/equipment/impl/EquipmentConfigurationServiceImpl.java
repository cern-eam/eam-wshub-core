package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentConfigurationService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationEntity;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.equipmentconfiguration_001.EquipmentConfiguration;
import net.datastream.schemas.mp_fields.EQUIPMENTCONFIGURATIONID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp3228_001.MP3228_GetEquipmentConfiguration_001;
import net.datastream.schemas.mp_results.mp3228_001.MP3228_GetEquipmentConfiguration_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;

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

    private EquipmentConfiguration readInforEquipmentConfiguration(InforContext context, String equipmentConfigurationCode, String revisionNum)
            throws InforException {

        MP3228_GetEquipmentConfiguration_001 getEquipmentConfiguration = new MP3228_GetEquipmentConfiguration_001();
        getEquipmentConfiguration.setEQUIPMENTCONFIGURATIONID(new EQUIPMENTCONFIGURATIONID_Type());
        getEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().setORGANIZATIONID(tools.getOrganization(context));
        getEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().setEQUIPMENTCONFIGURATIONCODE(equipmentConfigurationCode);
        getEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().setREVISIONNUM(tools.getDataTypeTools().encodeQuantity(revisionNum, "Revision number"));

        MP3228_GetEquipmentConfiguration_001_Result getEquipmentConfigurationResult;
        if (context.getCredentials() != null) {
            getEquipmentConfigurationResult = inforws.getEquipmentConfigurationOp(getEquipmentConfiguration,
                    tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), applicationData.getTenant());
        } else {
            getEquipmentConfigurationResult = inforws.getEquipmentConfigurationOp(getEquipmentConfiguration, tools.getOrganizationCode(context), null, "",
                    new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), applicationData.getTenant());
        }
        return getEquipmentConfigurationResult.getResultData().getEquipmentConfiguration();
    }

    @Override
    public EquipmentConfigurationEntity readEquipmentConfiguration(InforContext context, String equipmentConfigurationCode, String revisionNum) throws InforException {

        EquipmentConfiguration inforEquipmentConfiguration = readInforEquipmentConfiguration(context, equipmentConfigurationCode, revisionNum);
        EquipmentConfigurationEntity equipmentConfiguration = new EquipmentConfigurationEntity();


        if(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID() != null){
            equipmentConfiguration.setEquipmentConfigCode(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE());
            equipmentConfiguration.setEquipmentConfigDesc(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().getDESCRIPTION());
        }

        if(inforEquipmentConfiguration.getEQUIPMENTCONFIGSTATUS().getSTATUSCODE() != null){
            equipmentConfiguration.setEquipmentConfigStatusCode(inforEquipmentConfiguration.getEQUIPMENTCONFIGSTATUS().getSTATUSCODE());
        }

        if(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().getREVISIONNUM() != null){
            equipmentConfiguration.setRevisionNum(tools.getDataTypeTools().decodeQuantity(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().getREVISIONNUM()));
        }

        if(inforEquipmentConfiguration.getEQUIPMENTCONFIGSTATUS().getDESCRIPTION() != null){
            equipmentConfiguration.setEquipmentConfigStatusDesc(inforEquipmentConfiguration.getEQUIPMENTCONFIGSTATUS().getDESCRIPTION());
        }

        if(inforEquipmentConfiguration.getDEPARTMENTID().getDEPARTMENTCODE() != null){
            equipmentConfiguration.setConfigurationDepartmentCode(inforEquipmentConfiguration.getDEPARTMENTID().getDEPARTMENTCODE());
        }

        if(inforEquipmentConfiguration.getDEPARTMENTID().getDESCRIPTION() != null){
            equipmentConfiguration.setConfigurationDepartmentDesc(inforEquipmentConfiguration.getDEPARTMENTID().getDESCRIPTION());
        }

        if(inforEquipmentConfiguration.getConfigurationDetails() != null) {

            if(inforEquipmentConfiguration.getConfigurationDetails().getCLASSID().getCLASSCODE() != null){
                equipmentConfiguration.setConfigurationClassCode(inforEquipmentConfiguration.getConfigurationDetails().getCLASSID().getCLASSCODE());
            }

            if(inforEquipmentConfiguration.getConfigurationDetails().getCLASSID().getDESCRIPTION() != null){
                equipmentConfiguration.setConfigurationClassDesc(inforEquipmentConfiguration.getConfigurationDetails().getCLASSID().getDESCRIPTION());
            }

            if(inforEquipmentConfiguration.getConfigurationDetails().getCREATEDDATE() != null){
                equipmentConfiguration.setDateCreated(tools.getDataTypeTools().decodeInforDate(inforEquipmentConfiguration.getConfigurationDetails().getCREATEDDATE()));
            }

            if(inforEquipmentConfiguration.getConfigurationDetails().getCREATEDBY() != null){
                equipmentConfiguration.setCreatedBy(inforEquipmentConfiguration.getConfigurationDetails().getCREATEDBY().getUSERCODE());
            }

            if(inforEquipmentConfiguration.getConfigurationDetails().getUPDATEDBY() != null){
                equipmentConfiguration.setUpdatedBy(inforEquipmentConfiguration.getConfigurationDetails().getUPDATEDBY().getUSERCODE());
            }

            if(inforEquipmentConfiguration.getConfigurationDetails().getDATEUPDATED() != null){
                equipmentConfiguration.setDateUpdated(tools.getDataTypeTools().decodeInforDate(inforEquipmentConfiguration.getConfigurationDetails().getDATEUPDATED()));
            }

        }

        if(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID() != null){
            equipmentConfiguration.setEquipmentConfigCode(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE());
            equipmentConfiguration.setEquipmentConfigDesc(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().getDESCRIPTION());
        }

        if(inforEquipmentConfiguration.getEquipmentGenerationDetails() != null) {

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails().getEQUIPMENTSUFFIX() != null){
                equipmentConfiguration.setEquipmentSuffix(inforEquipmentConfiguration.getEquipmentGenerationDetails().getEQUIPMENTSUFFIX());
            }

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails().getEQUIPMENTPREFIX() != null){
                equipmentConfiguration.setEquipmentPrefix(inforEquipmentConfiguration.getEquipmentGenerationDetails().getEQUIPMENTPREFIX());
            }

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails().getSTATUS().getSTATUSCODE() != null){
                equipmentConfiguration.setEquipmentStatusCode(inforEquipmentConfiguration.getEquipmentGenerationDetails().getSTATUS().getSTATUSCODE());
            }

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails().getSTATUS().getDESCRIPTION() != null){
                equipmentConfiguration.setEquipmentStatusDesc(inforEquipmentConfiguration.getEquipmentGenerationDetails().getSTATUS().getDESCRIPTION());
            }

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails().getCREATEASSPECIFIC() != null){
                equipmentConfiguration.setCreateAsSpecific(inforEquipmentConfiguration.getEquipmentGenerationDetails().getCREATEASSPECIFIC());
            }

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails().getSEQUENCELENGTH() != null){
                equipmentConfiguration.setSequenceLength(inforEquipmentConfiguration.getEquipmentGenerationDetails().getSEQUENCELENGTH());
            }

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails().getSAMPLECODE() != null){
                equipmentConfiguration.setSampleCode(inforEquipmentConfiguration.getEquipmentGenerationDetails().getSAMPLECODE());
            }

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails() != null){
                equipmentConfiguration.setCommissioningWONum(inforEquipmentConfiguration.getEquipmentGenerationDetails().getCOMMISSIONINGWORKORDERID().getJOBNUM());
            }

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails() != null){
                equipmentConfiguration.setCommissioningWODesc(inforEquipmentConfiguration.getEquipmentGenerationDetails().getCOMMISSIONINGWORKORDERID().getDESCRIPTION());
            }

            if(inforEquipmentConfiguration.getEquipmentGenerationDetails().getAUTONUMBER() != null){
                equipmentConfiguration.setAutoNumber(inforEquipmentConfiguration.getEquipmentGenerationDetails().getAUTONUMBER());
            }
        }

        if(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().getORGANIZATIONID() != null){
            equipmentConfiguration.setOrganizationCode(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().getORGANIZATIONID().getORGANIZATIONCODE());
            equipmentConfiguration.setOrganizationDesc(inforEquipmentConfiguration.getEQUIPMENTCONFIGURATIONID().getORGANIZATIONID().getDESCRIPTION());
        }

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
