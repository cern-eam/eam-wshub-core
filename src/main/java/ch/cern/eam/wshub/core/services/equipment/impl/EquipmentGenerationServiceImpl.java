package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentGenerationService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.equipmentgeneration_001.*;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp3230_001.MP3230_GetEquipmentGenerationDefault_001;
import net.datastream.schemas.mp_functions.mp3231_001.MP3231_AddEquipmentGeneration_001;
import net.datastream.schemas.mp_functions.mp3232_001.MP3232_SyncEquipmentGeneration_001;
import net.datastream.schemas.mp_functions.mp3233_001.MP3233_DeleteEquipmentGeneration_001;
import net.datastream.schemas.mp_functions.mp3234_001.MP3234_GetEquipmentGeneration_001;
import net.datastream.schemas.mp_functions.mp3235_001.MP3235_CreateEquipmentGenerationPreview_001;
import net.datastream.schemas.mp_functions.mp3251_001.MP3251_GenerateEquipmentGeneration_001;
import net.datastream.schemas.mp_results.mp3230_001.MP3230_GetEquipmentGenerationDefault_001_Result;
import net.datastream.schemas.mp_results.mp3231_001.MP3231_AddEquipmentGeneration_001_Result;
import net.datastream.schemas.mp_results.mp3234_001.MP3234_GetEquipmentGeneration_001_Result;
import net.datastream.schemas.mp_results.mp3235_001.MP3235_CreateEquipmentGenerationPreview_001_Result;
import net.datastream.schemas.mp_results.mp3251_001.MP3251_GenerateEquipmentGeneration_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentGenerationEntity;
import org.openapplications.oagis_segments.QUANTITY;

import javax.xml.ws.Holder;
import java.math.BigDecimal;

public class EquipmentGenerationServiceImpl implements EquipmentGenerationService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public EquipmentGenerationServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String createEquipmentGeneration(InforContext context, EquipmentGenerationEntity equipmentGeneration) throws InforException {

            EquipmentGeneration inforEquipmentGeneration = new EquipmentGeneration();
            MP3231_AddEquipmentGeneration_001 addEquipmentGeneration = new MP3231_AddEquipmentGeneration_001();
            initializeEquipmentGenerationObject(inforEquipmentGeneration, equipmentGeneration, context);
            addEquipmentGeneration.setEquipmentGeneration(inforEquipmentGeneration);
            MP3231_AddEquipmentGeneration_001_Result addEquipmentGenerationResult;

            if (context.getCredentials() != null) {
                addEquipmentGenerationResult = inforws.addEquipmentGenerationOp(addEquipmentGeneration, tools.getOrganizationCode(context),
                        tools.createSecurityHeader(context), "TERMINATE", null,
                        tools.createMessageConfig(), tools.getTenant(context));
            } else {
                addEquipmentGenerationResult = inforws.addEquipmentGenerationOp(addEquipmentGeneration, tools.getOrganizationCode(context), null, "",
                        new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
            }
            return addEquipmentGenerationResult.getResultData().getEQUIPMENTGENERATIONID().getEQUIPMENTGENERATIONCODE();
    }

    private void updateInforEquipmentGeneration(InforContext context, EquipmentGeneration equipmentGeneration) throws InforException {
        try {
            MP3232_SyncEquipmentGeneration_001 syncEquipmentGeneration = new MP3232_SyncEquipmentGeneration_001();
            syncEquipmentGeneration.setEquipmentGeneration(equipmentGeneration);

            if (context.getCredentials() != null) {
                inforws.syncEquipmentGenerationOp(syncEquipmentGeneration, "*",
                        tools.createSecurityHeader(context), "TERMINATE", null,
                        tools.createMessageConfig(), tools.getTenant(context));
            } else {
                inforws.syncEquipmentGenerationOp(syncEquipmentGeneration, "*", null, null, new Holder<>(tools.createInforSession(context)),
                        tools.createMessageConfig(), tools.getTenant(context));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String updateEquipmentGeneration(InforContext context, EquipmentGenerationEntity equipmentGeneration) throws InforException {
        try {
            EquipmentGeneration inforEquipmentGeneration = readInforEquipmentGeneration(context, equipmentGeneration.getEquipmentGenerationCode());
            initializeEquipmentGenerationObject(inforEquipmentGeneration, equipmentGeneration, context);
            this.updateInforEquipmentGeneration(context, inforEquipmentGeneration);

        }catch(Exception exception){
            exception.printStackTrace();
        }
        return equipmentGeneration.getEquipmentGenerationCode();
    }

    @Override
    public String deleteEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException {
        try {
            MP3233_DeleteEquipmentGeneration_001 deleteEquipmentGeneration = new MP3233_DeleteEquipmentGeneration_001();

            deleteEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
            deleteEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
            deleteEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID().setORGANIZATIONCODE(tools.getOrganizationCode(context));
            deleteEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

            if (context.getCredentials() != null) {
                inforws.deleteEquipmentGenerationOp(deleteEquipmentGeneration, "*",
                        tools.createSecurityHeader(context), "TERMINATE", null,
                        tools.createMessageConfig(), tools.getTenant(context));
            } else {
                inforws.deleteEquipmentGenerationOp(deleteEquipmentGeneration, "*", null, null, new Holder<>(tools.createInforSession(context)),
                        tools.createMessageConfig(), tools.getTenant(context));
            }
        }catch(InforException exception){
            exception.printStackTrace();
        }

        return equipmentGenerationCode;
    }

    @Override
    public String createEquipmentGenerationPreview(InforContext context, String equipmentGenerationCode) throws InforException {
        MP3235_CreateEquipmentGenerationPreview_001 createEquipmentGenerationPreview = new MP3235_CreateEquipmentGenerationPreview_001();

        createEquipmentGenerationPreview.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
        createEquipmentGenerationPreview.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
        createEquipmentGenerationPreview.getEQUIPMENTGENERATIONID().getORGANIZATIONID().setORGANIZATIONCODE(tools.getOrganizationCode(context));
        createEquipmentGenerationPreview.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

        MP3235_CreateEquipmentGenerationPreview_001_Result createEquipmentGenerationPreviewResult;

        if (context.getCredentials() != null) {
            createEquipmentGenerationPreviewResult = inforws.createEquipmentGenerationPreviewOp(createEquipmentGenerationPreview, "*",
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
            createEquipmentGenerationPreviewResult = inforws.createEquipmentGenerationPreviewOp(createEquipmentGenerationPreview, "*", null, null, new Holder<>(tools.createInforSession(context)),
                    tools.createMessageConfig(), tools.getTenant(context));
        }

        return createEquipmentGenerationPreviewResult.getResultData().getEQUIPMENTGENERATIONID().toString();
    }

    @Override
    public String generateEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException {

        MP3251_GenerateEquipmentGeneration_001 generateEquipmentGeneration = new  MP3251_GenerateEquipmentGeneration_001();
        generateEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
        generateEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
        generateEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID().setORGANIZATIONCODE(tools.getOrganizationCode(context));
        generateEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

        MP3251_GenerateEquipmentGeneration_001_Result generateEquipmentGenerationResult;

        if (context.getCredentials() != null) {
            generateEquipmentGenerationResult = inforws.generateEquipmentGenerationOp(generateEquipmentGeneration, "*",
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
            generateEquipmentGenerationResult = inforws.generateEquipmentGenerationOp(generateEquipmentGeneration, "*", null, null, new Holder<>(tools.createInforSession(context)),
                    tools.createMessageConfig(), tools.getTenant(context));
        }

        return generateEquipmentGenerationResult.getResultData().getEQUIPMENTGENERATIONID().toString();
    }

    private EquipmentGeneration readInforEquipmentGeneration(InforContext context, String equipmentGenerationCode)
            throws InforException {

        MP3234_GetEquipmentGeneration_001 getEquipmentGeneration = new MP3234_GetEquipmentGeneration_001();
        getEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
        getEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
        getEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

        MP3234_GetEquipmentGeneration_001_Result getEquipmentGenerationResult;
        if (context.getCredentials() != null) {
            getEquipmentGenerationResult = inforws.getEquipmentGenerationOp(getEquipmentGeneration,
                    tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
            getEquipmentGenerationResult = inforws.getEquipmentGenerationOp(getEquipmentGeneration, tools.getOrganizationCode(context), null, "",
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
        }
        return getEquipmentGenerationResult.getResultData().getEquipmentGeneration();

    }

    @Override
    public EquipmentGenerationEntity readEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException {

        EquipmentGeneration inforEquipmentGeneration = readInforEquipmentGeneration(context, equipmentGenerationCode);
        EquipmentGenerationEntity equipmentGeneration = new EquipmentGenerationEntity();


        if(inforEquipmentGeneration.getEQUIPMENTGENERATIONID() != null){
            equipmentGeneration.setEquipmentGenerationCode(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getEQUIPMENTGENERATIONCODE());
            equipmentGeneration.setEquipmentGenerationDesc(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getDESCRIPTION());
            equipmentGeneration.setDescription(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getDESCRIPTION());
        }

        if(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID() != null){
            equipmentGeneration.setOrganizationCode(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID().getORGANIZATIONCODE());
            equipmentGeneration.setOrganizationDesc(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID().getDESCRIPTION());
        }

        if(inforEquipmentGeneration.getPROCESSED() != null){
            equipmentGeneration.setProcessed(inforEquipmentGeneration.getPROCESSED());
        }

        if(inforEquipmentGeneration.getACTIVE() != null){
            equipmentGeneration.setActive(inforEquipmentGeneration.getACTIVE());
        }

        if(inforEquipmentGeneration.getAWAITINGPURCHASE() != null){
            equipmentGeneration.setAwaitingPurchase(inforEquipmentGeneration.getAWAITINGPURCHASE());
        }

        if(inforEquipmentGeneration.getPROCESSERROR() != null){
            equipmentGeneration.setProcessError(inforEquipmentGeneration.getPROCESSERROR());
        }

        if(inforEquipmentGeneration.getPROCESSRUNNING() != null){
            equipmentGeneration.setProcessRunning(inforEquipmentGeneration.getPROCESSRUNNING());
        }

        if(inforEquipmentGeneration.getLASTUPDATEDDATE() != null){
            equipmentGeneration.setLastUpdatedDate(tools.getDataTypeTools().decodeInforDate(inforEquipmentGeneration.getLASTUPDATEDDATE()));
        }

        if(inforEquipmentGeneration.getCREATEDDATE() != null){
            equipmentGeneration.setCreatedDate(tools.getDataTypeTools().decodeInforDate(inforEquipmentGeneration.getCREATEDDATE()));
        }

        if(inforEquipmentGeneration.getCREATEDBY() != null){
            equipmentGeneration.setCreatedBy(inforEquipmentGeneration.getCREATEDBY().getUSERCODE());
        }

        if(inforEquipmentGeneration.getUPDATEDBY() != null){
            equipmentGeneration.setUpdatedBy(inforEquipmentGeneration.getUPDATEDBY().getUSERCODE());
        }
        if(inforEquipmentGeneration.getDATEUPDATED() != null){
            equipmentGeneration.setDateUpdated(tools.getDataTypeTools().decodeInforDate(inforEquipmentGeneration.getDATEUPDATED()));
        }
        if(inforEquipmentGeneration.getEquipmentDetails() != null){
            EquipmentDetails equipmentDetails = inforEquipmentGeneration.getEquipmentDetails();
            if(equipmentDetails.getGENERATECOUNT() != null){
                equipmentGeneration.setGenerateCount(tools.getDataTypeTools().decodeQuantity(equipmentDetails.getGENERATECOUNT()));
            }

            if(equipmentDetails.getEQUIPMENTCONFIGURATIONID() != null){
                equipmentGeneration.setEquipmentConfigurationCode(inforEquipmentGeneration.getEquipmentDetails().getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE());
                equipmentGeneration.setEquipmentConfigurationDesc(inforEquipmentGeneration.getEquipmentDetails().getEQUIPMENTCONFIGURATIONID().getDESCRIPTION());
            }

            if(equipmentDetails.getEQUIPMENTCONFIGURATIONID() != null && equipmentDetails.getEQUIPMENTCONFIGURATIONID().getREVISIONNUM() != null){
                equipmentGeneration.setRevisionNum(tools.getDataTypeTools().decodeQuantity(inforEquipmentGeneration.getEquipmentDetails().getEQUIPMENTCONFIGURATIONID().getREVISIONNUM()));
            }

            if(equipmentDetails.getEQUIPMENTSTATUS() != null){
                equipmentGeneration.setEquipmentStatusCode(inforEquipmentGeneration.getEquipmentDetails().getEQUIPMENTSTATUS().getSTATUSCODE());
                equipmentGeneration.setEquipmentStatusDesc(inforEquipmentGeneration.getEquipmentDetails().getEQUIPMENTSTATUS().getDESCRIPTION());
            }

            if(equipmentDetails.getALLSPECIFIC() != null){
                equipmentGeneration.setAllSpecific(inforEquipmentGeneration.getEquipmentDetails().getALLSPECIFIC());
            }

            if(equipmentDetails.getCOMMISSIONDATE() != null){
                equipmentGeneration.setCommissionDate(tools.getDataTypeTools().decodeInforDate(inforEquipmentGeneration.getEquipmentDetails().getCOMMISSIONDATE()));
            }

            if(equipmentDetails.getLOCATIONID() != null){
                equipmentGeneration.setEquipmentLocationCode(inforEquipmentGeneration.getEquipmentDetails().getLOCATIONID().getLOCATIONCODE());
                equipmentGeneration.setEquipmentLocationDesc(inforEquipmentGeneration.getEquipmentDetails().getLOCATIONID().getDESCRIPTION());
            }

            if(equipmentDetails.getCOSTCODEID() != null){
                equipmentGeneration.setEquipmentCostCode(inforEquipmentGeneration.getEquipmentDetails().getCOSTCODEID().getCOSTCODE());
                equipmentGeneration.setEquipmentCostCodeDesc(inforEquipmentGeneration.getEquipmentDetails().getCOSTCODEID().getDESCRIPTION());
            }

            if(equipmentDetails.getDEPARTMENTID() != null){
                equipmentGeneration.setEquipmentDepartmentCode(inforEquipmentGeneration.getEquipmentDetails().getDEPARTMENTID().getDEPARTMENTCODE());
                equipmentGeneration.setEquipmentDepartmentDesc(inforEquipmentGeneration.getEquipmentDetails().getDEPARTMENTID().getDESCRIPTION());
            }

            if(equipmentDetails.getASSIGNEDTO() != null){
                equipmentGeneration.setEquipmentAssignedToCode(inforEquipmentGeneration.getEquipmentDetails().getASSIGNEDTO().getPERSONCODE());
                equipmentGeneration.setEquipmentAssignedToDesc(inforEquipmentGeneration.getEquipmentDetails().getASSIGNEDTO().getDESCRIPTION());
            }
        }

        if(inforEquipmentGeneration.getCommissioningWorkOrderDetails() != null) {
            CommissioningWorkOrderDetails workOrderDetails = inforEquipmentGeneration.getCommissioningWorkOrderDetails();

            if(workOrderDetails.getCOMMISSIONINGWORKORDERID() != null) {
                equipmentGeneration.setCommissioningWONumber(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getCOMMISSIONINGWORKORDERID().getJOBNUM());
                equipmentGeneration.setCommissioningWODesc(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getCOMMISSIONINGWORKORDERID().getDESCRIPTION());
            }

            if(workOrderDetails.getASSIGNEDTO() != null){
                equipmentGeneration.setCommissioningWOAssignedTo(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getASSIGNEDTO().getPERSONCODE());
            }

            if(workOrderDetails.getCOSTCODEID() != null){
                equipmentGeneration.setCommissioningWOCostCode(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getCOSTCODEID().getCOSTCODE());
                equipmentGeneration.setCommissioningWOCostDesc(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getCOSTCODEID().getDESCRIPTION());
            }

            if(workOrderDetails.getCREATECOMMISSIONINGWO() != null){
                equipmentGeneration.setCreateCommissioningWO(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getCREATECOMMISSIONINGWO());
            }

            if(workOrderDetails.getSTATUS() != null) {
                equipmentGeneration.setCommissioningWOStatusCode(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getSTATUS().getSTATUSCODE());
                equipmentGeneration.setCommissioningWOStatusDesc(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getSTATUS().getDESCRIPTION());
            }

            if(workOrderDetails.getDEPARTMENTID() != null) {
                equipmentGeneration.setCommissioningWODepartmentCode(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getDEPARTMENTID().getDEPARTMENTCODE());
                equipmentGeneration.setCommissioningWODepartmentDesc(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getDEPARTMENTID().getDESCRIPTION());
            }

            if(workOrderDetails.getLOCATIONID() != null) {
                equipmentGeneration.setCommissioningWOLocationCode(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getLOCATIONID().getLOCATIONCODE());
                equipmentGeneration.setCommissioningWOLocationDesc(inforEquipmentGeneration.getCommissioningWorkOrderDetails().getLOCATIONID().getDESCRIPTION());
            }
        }

        if( inforEquipmentGeneration.getCopyData() != null ) {

            if(inforEquipmentGeneration.getCopyData().getCOPYCALIBRATION() != null) {
                equipmentGeneration.setCopyCalibration(inforEquipmentGeneration.getCopyData().getCOPYCALIBRATION());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYCOMMENTS() != null) {
                equipmentGeneration.setCopyComments(inforEquipmentGeneration.getCopyData().getCOPYCOMMENTS());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYCUSTOMFIELDS() != null) {
                equipmentGeneration.setCopyCustomfields(inforEquipmentGeneration.getCopyData().getCOPYCUSTOMFIELDS());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYDEPRECIATION() != null) {
                equipmentGeneration.setCopyDepreciation(inforEquipmentGeneration.getCopyData().getCOPYDEPRECIATION());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYDOCUMENTS() != null) {
                equipmentGeneration.setCopyDocuments(inforEquipmentGeneration.getCopyData().getCOPYDOCUMENTS());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYMAINTENANCEPATTERNS() != null) {
                equipmentGeneration.setCopyMaintenancePatterns(inforEquipmentGeneration.getCopyData().getCOPYMAINTENANCEPATTERNS());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYMETERS() != null) {
                equipmentGeneration.setCopyMeters(inforEquipmentGeneration.getCopyData().getCOPYMETERS());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYPARTSASSOCIATED() != null) {
                equipmentGeneration.setCopyPartsAssociated(inforEquipmentGeneration.getCopyData().getCOPYPARTSASSOCIATED());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYPERMITS() != null) {
                equipmentGeneration.setCopyPermits(inforEquipmentGeneration.getCopyData().getCOPYPERMITS());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYPMSCHEDULES() != null) {
                equipmentGeneration.setCopyPMSchedules(inforEquipmentGeneration.getCopyData().getCOPYPMSCHEDULES());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYSAFETY() != null) {
                equipmentGeneration.setCopySafety(inforEquipmentGeneration.getCopyData().getCOPYSAFETY());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYTESTPOINTS() != null) {
                equipmentGeneration.setCopyTestPoints(inforEquipmentGeneration.getCopyData().getCOPYTESTPOINTS());
            }
            if(inforEquipmentGeneration.getCopyData().getCOPYWARRANTIES() != null){
                equipmentGeneration.setCopyWarranties(inforEquipmentGeneration.getCopyData().getCOPYWARRANTIES());
            }
        }

        return equipmentGeneration;
    }

    @Override
    public EquipmentGenerationEntity readEquipmentGenerationDefault(InforContext context, String equipmentGenerationCode) throws InforException {

        MP3230_GetEquipmentGenerationDefault_001 getEquipmentGenerationDefault = new MP3230_GetEquipmentGenerationDefault_001();

        getEquipmentGenerationDefault.setORGANIZATIONID(tools.getOrganization(context));
        getEquipmentGenerationDefault.getORGANIZATIONID().setORGANIZATIONCODE(tools.getOrganizationCode(context));

        MP3230_GetEquipmentGenerationDefault_001_Result getEGDefaultResult = new MP3230_GetEquipmentGenerationDefault_001_Result();

        if (context.getCredentials() != null) {
            getEGDefaultResult = inforws.getEquipmentGenerationDefaultOp(getEquipmentGenerationDefault, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
            getEGDefaultResult = inforws.getEquipmentGenerationDefaultOp(getEquipmentGenerationDefault, tools.getOrganizationCode(context), null, "",
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
        }
        EquipmentGenerationEntity equipmentGeneration = new EquipmentGenerationEntity();
        net.datastream.schemas.mp_entities.equipmentgenerationdefault_001.EquipmentGenerationDefault inforEquipmentGenerationDefault =
                getEGDefaultResult.getResultData().getEquipmentGenerationDefault();

        if(context.getOrganizationCode() != null) {
            equipmentGeneration.setOrganizationCode(context.getOrganizationCode());
        }

        if(inforEquipmentGenerationDefault.getSTATUS() != null){
            equipmentGeneration.setStatusCode(inforEquipmentGenerationDefault.getSTATUS().getSTATUSCODE());
            equipmentGeneration.setStatusDesc(inforEquipmentGenerationDefault.getSTATUS().getDESCRIPTION());
        }

        if(inforEquipmentGenerationDefault.getPROCESSED() != null){
            equipmentGeneration.setProcessed(inforEquipmentGenerationDefault.getPROCESSED());
        }

        if(inforEquipmentGenerationDefault.getACTIVE() != null){
            equipmentGeneration.setActive(inforEquipmentGenerationDefault.getACTIVE());
        }

        if(inforEquipmentGenerationDefault.getAWAITINGPURCHASE() != null){
            equipmentGeneration.setAwaitingPurchase(inforEquipmentGenerationDefault.getAWAITINGPURCHASE());
        }

        if(inforEquipmentGenerationDefault.getPROCESSERROR() != null){
            equipmentGeneration.setProcessError(inforEquipmentGenerationDefault.getPROCESSERROR());
        }

        if(inforEquipmentGenerationDefault.getPROCESSRUNNING() != null){
            equipmentGeneration.setProcessRunning(inforEquipmentGenerationDefault.getPROCESSRUNNING());
        }

        if(inforEquipmentGenerationDefault.getTOPLEVELONLY() != null){
            equipmentGeneration.setTopLevelOnly(inforEquipmentGenerationDefault.getTOPLEVELONLY());
        }

        if(inforEquipmentGenerationDefault.getALLDEPENDENT() != null){
            equipmentGeneration.setAllDependent(inforEquipmentGenerationDefault.getALLDEPENDENT());
        }

        if(inforEquipmentGenerationDefault.getALLCOSTROLLUP() != null){
            equipmentGeneration.setAllCostRollup(inforEquipmentGenerationDefault.getALLCOSTROLLUP());
        }

        if(inforEquipmentGenerationDefault.getCOPYCOMMENTS() != null){
            equipmentGeneration.setCopyComments(inforEquipmentGenerationDefault.getCOPYCOMMENTS());
        }

        if(inforEquipmentGenerationDefault.getCOPYDOCUMENTS() != null){
            equipmentGeneration.setCopyDocuments(inforEquipmentGenerationDefault.getCOPYDOCUMENTS());
        }

        if(inforEquipmentGenerationDefault.getCOPYCUSTOMFIELDS() != null){
            equipmentGeneration.setCopyCustomfields(inforEquipmentGenerationDefault.getCOPYCUSTOMFIELDS());
        }

        if(inforEquipmentGenerationDefault.getCOPYDEPRECIATION() != null){
            equipmentGeneration.setCopyDepreciation(inforEquipmentGenerationDefault.getCOPYDEPRECIATION());
        }

        if(inforEquipmentGenerationDefault.getCOPYMETERS() != null){
            equipmentGeneration.setCopyMeters(inforEquipmentGenerationDefault.getCOPYMETERS());
        }

        if(inforEquipmentGenerationDefault.getCOPYPARTSASSOCIATED() != null){
            equipmentGeneration.setCopyPartsAssociated(inforEquipmentGenerationDefault.getCOPYPARTSASSOCIATED());
        }

        if(inforEquipmentGenerationDefault.getCOPYWARRANTIES() != null){
            equipmentGeneration.setCopyWarranties(inforEquipmentGenerationDefault.getCOPYWARRANTIES());
        }

        if(inforEquipmentGenerationDefault.getCOPYPMSCHEDULES() != null){
            equipmentGeneration.setCopyPMSchedules(inforEquipmentGenerationDefault.getCOPYPMSCHEDULES());
        }

        if(inforEquipmentGenerationDefault.getCOPYMAINTENANCEPATTERNS() != null){
            equipmentGeneration.setCopyDepreciation(inforEquipmentGenerationDefault.getCOPYMAINTENANCEPATTERNS());
        }

        if(inforEquipmentGenerationDefault.getCOPYSAFETY() != null){
            equipmentGeneration.setCopySafety(inforEquipmentGenerationDefault.getCOPYSAFETY());
        }

        if(inforEquipmentGenerationDefault.getCOPYPERMITS() != null){
            equipmentGeneration.setCopyPermits(inforEquipmentGenerationDefault.getCOPYPERMITS());
        }

        if(inforEquipmentGenerationDefault.getCOPYCALIBRATION() != null){
            equipmentGeneration.setCopyCalibration(inforEquipmentGenerationDefault.getCOPYCALIBRATION());
        }

        if(inforEquipmentGenerationDefault.getCOPYTESTPOINTS() != null){
            equipmentGeneration.setCopyTestPoints(inforEquipmentGenerationDefault.getCOPYTESTPOINTS());
        }
        return equipmentGeneration;
    }


    private void initializeEquipmentGenerationObject(EquipmentGeneration inforEquipmentGeneration, EquipmentGenerationEntity equipmentGeneration, InforContext context) throws InforException {

        EquipmentDetails equipmentDetails = new EquipmentDetails();
        CommissioningWorkOrderDetails workOrderDetails = new CommissioningWorkOrderDetails();
        CopyData copyData = new CopyData();

        if (inforEquipmentGeneration.getEQUIPMENTGENERATIONID() == null) {
            inforEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
            inforEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
            inforEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE("0");
        }

        if (equipmentDetails.getEQUIPMENTCONFIGURATIONID() == null) {
            equipmentDetails.setEQUIPMENTCONFIGURATIONID(new EQUIPMENTCONFIGURATIONID_Type());
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().setORGANIZATIONID(tools.getOrganization(context));
        }

        if (workOrderDetails.getCOMMISSIONINGWORKORDERID() == null) {
            workOrderDetails.setCOMMISSIONINGWORKORDERID(new WOID_Type());
            workOrderDetails.getCOMMISSIONINGWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
        }

        if(equipmentGeneration.getEquipmentConfigurationCode() != null){
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().setEQUIPMENTCONFIGURATIONCODE(equipmentGeneration.getEquipmentConfigurationCode());
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().setDESCRIPTION(equipmentGeneration.getEquipmentConfigurationDesc());
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().setREVISIONNUM(new QUANTITY());
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().setORGANIZATIONID(tools.getOrganization(context));
        }

        if(equipmentGeneration.getRevisionNum() != null && equipmentGeneration.getEquipmentConfigurationCode() != null){
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().getREVISIONNUM().setUOM("default");
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().getREVISIONNUM().setSIGN("+");
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().getREVISIONNUM().setNUMOFDEC(new BigDecimal(0).toBigInteger());
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().getREVISIONNUM().setVALUE(new BigDecimal(equipmentGeneration.getRevisionNum()));
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().getREVISIONNUM().setQualifier("ACCEPTED");
        }


        if(equipmentGeneration.getGenerateCount() != null){
            equipmentDetails.setGENERATECOUNT(new QUANTITY());
            equipmentDetails.getGENERATECOUNT().setUOM("default");
            equipmentDetails.getGENERATECOUNT().setSIGN("+");
            equipmentDetails.getGENERATECOUNT().setNUMOFDEC(new BigDecimal(0).toBigInteger());
            equipmentDetails.getGENERATECOUNT().setVALUE(new BigDecimal(equipmentGeneration.getGenerateCount()));
            equipmentDetails.getGENERATECOUNT().setQualifier("ACCEPTED");
        }
        if(equipmentGeneration.getEquipmentStatusCode() != null){
            equipmentDetails.setEQUIPMENTSTATUS(new STATUS_Type());
            equipmentDetails.getEQUIPMENTSTATUS().setSTATUSCODE(equipmentGeneration.getEquipmentStatusCode());

        }
        if(equipmentGeneration.getEquipmentStatusDesc() != null){
            STATUS_Type statusType = new STATUS_Type();
            statusType.setDESCRIPTION(equipmentGeneration.getEquipmentStatusDesc());
            equipmentDetails.setEQUIPMENTSTATUS(statusType);
        }

        if(equipmentGeneration.getCommissionDate() != null){
            equipmentDetails.setCOMMISSIONDATE(tools.getDataTypeTools().encodeInforDate(equipmentGeneration.getCommissionDate(), "Commission Date"));
        }

        if(equipmentGeneration.getAllSpecific() != null){
            equipmentDetails.setALLSPECIFIC(equipmentGeneration.getAllSpecific());
        }

        if(equipmentGeneration.getEquipmentAssignedToCode() != null){
            PERSONID_Type personidType = new PERSONID_Type();
            personidType.setPERSONCODE(equipmentGeneration.getEquipmentAssignedToCode());
            equipmentDetails.setASSIGNEDTO(personidType);
        }

        if(equipmentGeneration.getEquipmentAssignedToDesc() != null){
            PERSONID_Type personid_type = new PERSONID_Type();
            personid_type.setDESCRIPTION(equipmentGeneration.getEquipmentAssignedToDesc());
            equipmentDetails.setASSIGNEDTO(personid_type);
        }

        if(equipmentGeneration.getEquipmentCostCode() != null){
            COSTCODEID_Type costcodeidType = new COSTCODEID_Type();
            costcodeidType.setCOSTCODE(equipmentGeneration.getEquipmentCostCode());
            equipmentDetails.setCOSTCODEID(costcodeidType);
        }


        if(equipmentGeneration.getEquipmentCostCodeDesc() != null){
            COSTCODEID_Type costcodeidType = new COSTCODEID_Type();
            costcodeidType.setDESCRIPTION(equipmentGeneration.getEquipmentCostCodeDesc());
            equipmentDetails.setCOSTCODEID(costcodeidType);
        }

        if(equipmentGeneration.getEquipmentDepartmentCode() != null){
            DEPARTMENTID_Type departmentidType = new DEPARTMENTID_Type();
            departmentidType.setDEPARTMENTCODE(equipmentGeneration.getEquipmentDepartmentCode());
            equipmentDetails.setDEPARTMENTID(departmentidType);
        }

        if(equipmentGeneration.getEquipmentDepartmentDesc() != null){
            DEPARTMENTID_Type departmentidType = new DEPARTMENTID_Type();
            departmentidType.setDESCRIPTION(equipmentGeneration.getEquipmentDepartmentDesc());
            equipmentDetails.setDEPARTMENTID(departmentidType);
        }

        if(equipmentGeneration.getEquipmentLocationCode() != null){
            LOCATIONID_Type locationidType = new LOCATIONID_Type();
            locationidType.setLOCATIONCODE(equipmentGeneration.getEquipmentLocationCode());
            equipmentDetails.setLOCATIONID(locationidType);
        }

        if(equipmentGeneration.getEquipmentLocationDesc() != null){
            LOCATIONID_Type locationidType = new LOCATIONID_Type();
            locationidType.setDESCRIPTION(equipmentGeneration.getEquipmentLocationDesc());
            equipmentDetails.setLOCATIONID(locationidType);
        }


        if (equipmentGeneration.getDescription() != null) {
            inforEquipmentGeneration.getEQUIPMENTGENERATIONID().setDESCRIPTION(equipmentGeneration.getDescription());
        }

        if (equipmentGeneration.getCreatedDate() != null) {
            inforEquipmentGeneration.setCREATEDDATE(tools.getDataTypeTools().encodeInforDate(equipmentGeneration.getCreatedDate(), "Created Date"));
        }

        if (equipmentGeneration.getProcessed() != null) {
            inforEquipmentGeneration.setPROCESSED(equipmentGeneration.getProcessed());
        }

        if (equipmentGeneration.getActive() != null) {
            inforEquipmentGeneration.setACTIVE(equipmentGeneration.getActive());
        }

        if (equipmentGeneration.getAwaitingPurchase() != null) {
            inforEquipmentGeneration.setAWAITINGPURCHASE(equipmentGeneration.getAwaitingPurchase());
        }

        if (equipmentGeneration.getProcessError() != null) {
            inforEquipmentGeneration.setPROCESSERROR(equipmentGeneration.getProcessError());
        }

        if (equipmentGeneration.getProcessRunning() != null) {
            inforEquipmentGeneration.setPROCESSRUNNING(equipmentGeneration.getProcessRunning());
        }

        if (equipmentGeneration.getLastUpdatedDate() != null) {
            inforEquipmentGeneration.setLASTUPDATEDDATE(tools.getDataTypeTools().encodeInforDate(equipmentGeneration.getLastUpdatedDate(), "Last Updated Date"));
        }

        if (equipmentGeneration.getDateUpdated() != null) {
            inforEquipmentGeneration.setDATEUPDATED(tools.getDataTypeTools().encodeInforDate(equipmentGeneration.getDateUpdated(), "Date Updated"));
        }

        if (equipmentGeneration.getCommissioningWONumber() != null) {
            workOrderDetails.getCOMMISSIONINGWORKORDERID().setJOBNUM(equipmentGeneration.getCommissioningWONumber());
            workOrderDetails.getCOMMISSIONINGWORKORDERID().setDESCRIPTION(equipmentGeneration.getCommissioningWODesc());
        }

        if (equipmentGeneration.getCommissioningWOAssignedTo() != null){
            workOrderDetails.setASSIGNEDTO(new PERSONID_Type());
            workOrderDetails.getASSIGNEDTO().setPERSONCODE(equipmentGeneration.getCommissioningWOAssignedTo());
        }

        if (equipmentGeneration.getCommissioningWOCostCode() != null){
            workOrderDetails.setCOSTCODEID(new COSTCODEID_Type());
            workOrderDetails.getCOSTCODEID().setCOSTCODE(equipmentGeneration.getCommissioningWOCostCode());
            workOrderDetails.getCOSTCODEID().setDESCRIPTION(equipmentGeneration.getCommissioningWOCostDesc());
        }

        if (equipmentGeneration.getCreateCommissioningWO() != null){
            workOrderDetails.setCREATECOMMISSIONINGWO(equipmentGeneration.getCreateCommissioningWO());
        }

        if (equipmentGeneration.getCommissioningWOStatusCode()!= null) {
            workOrderDetails.setSTATUS(new STATUS_Type());
            workOrderDetails.getSTATUS().setSTATUSCODE(equipmentGeneration.getCommissioningWOStatusCode());
            workOrderDetails.getSTATUS().setDESCRIPTION(equipmentGeneration.getCommissioningWOStatusDesc());
        }

        if (equipmentGeneration.getCommissioningWODepartmentCode()!= null) {
            workOrderDetails.setDEPARTMENTID(new DEPARTMENTID_Type());
            workOrderDetails.getDEPARTMENTID().setDEPARTMENTCODE(equipmentGeneration.getCommissioningWODepartmentCode());
            workOrderDetails.getDEPARTMENTID().setDESCRIPTION(equipmentGeneration.getCommissioningWODepartmentDesc());

        }

        if (equipmentGeneration.getCommissioningWOLocationCode()!= null) {
            workOrderDetails.setLOCATIONID(new LOCATIONID_Type());
            workOrderDetails.getLOCATIONID().setLOCATIONCODE(equipmentGeneration.getCommissioningWOLocationCode());
            workOrderDetails.getLOCATIONID().setDESCRIPTION(equipmentGeneration.getCommissioningWOLocationDesc());

        }
        if (equipmentGeneration.getCopyCalibration() != null) {
            copyData.setCOPYCALIBRATION(equipmentGeneration.getCopyCalibration());

        }
        if (equipmentGeneration.getCopyComments() != null) {
            copyData.setCOPYCOMMENTS(equipmentGeneration.getCopyComments());

        }
        if (equipmentGeneration.getCopyCustomfields() != null) {
            copyData.setCOPYCUSTOMFIELDS(equipmentGeneration.getCopyCustomfields());

        }
        if (equipmentGeneration.getCopyDepreciation() != null) {
            copyData.setCOPYDEPRECIATION(equipmentGeneration.getCopyDepreciation());

        }
        if (equipmentGeneration.getCopyDocuments() != null) {
            copyData.setCOPYDOCUMENTS(equipmentGeneration.getCopyDocuments());

        }
        if (equipmentGeneration.getCopyMaintenancePatterns() != null) {
            copyData.setCOPYMAINTENANCEPATTERNS(equipmentGeneration.getCopyMaintenancePatterns());

        }
        if (equipmentGeneration.getCopyMeters() != null) {
            copyData.setCOPYMETERS(equipmentGeneration.getCopyMeters());

        }
        if (equipmentGeneration.getCopyPartsAssociated() != null) {
            copyData.setCOPYPARTSASSOCIATED(equipmentGeneration.getCopyPartsAssociated());

        }
        if (equipmentGeneration.getCopyPermits() != null) {
            copyData.setCOPYPERMITS(equipmentGeneration.getCopyPermits());

        }
        if (equipmentGeneration.getCopyPMSchedules() != null) {
            copyData.setCOPYPMSCHEDULES(equipmentGeneration.getCopyPMSchedules());

        }
        if (equipmentGeneration.getCopySafety() != null) {
            copyData.setCOPYSAFETY(equipmentGeneration.getCopySafety());

        }
        if (equipmentGeneration.getCopyTestPoints() != null) {
            copyData.setCOPYTESTPOINTS(equipmentGeneration.getCopyTestPoints());

        }
        if (equipmentGeneration.getCopyWarranties() != null) {
            copyData.setCOPYWARRANTIES(equipmentGeneration.getCopyWarranties());
        }

        if(equipmentDetails != null){
            inforEquipmentGeneration.setEquipmentDetails(equipmentDetails);
        }

        if(workOrderDetails != null){
            inforEquipmentGeneration.setCommissioningWorkOrderDetails(workOrderDetails);
        }

        if(copyData != null){
            inforEquipmentGeneration.setCopyData(copyData);
        }

    }
 }
