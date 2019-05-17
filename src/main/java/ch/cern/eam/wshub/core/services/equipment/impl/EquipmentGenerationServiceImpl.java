package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentGenerationService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentGeneration;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.EQUIPMENTGENERATIONID_Type;
import net.datastream.schemas.mp_fields.ORGANIZATIONID_Type;
import net.datastream.schemas.mp_fields.STATUS_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp3230_001.MP3230_GetEquipmentGenerationDefault_001;
import net.datastream.schemas.mp_functions.mp3231_001.MP3231_AddEquipmentGeneration_001;
import net.datastream.schemas.mp_functions.mp3232_001.MP3232_SyncEquipmentGeneration_001;
import net.datastream.schemas.mp_functions.mp3233_001.MP3233_DeleteEquipmentGeneration_001;
import net.datastream.schemas.mp_functions.mp3234_001.MP3234_GetEquipmentGeneration_001;
import net.datastream.schemas.mp_functions.mp3251_001.MP3251_GenerateEquipmentGeneration_001;
import net.datastream.schemas.mp_results.mp3230_001.MP3230_GetEquipmentGenerationDefault_001_Result;
import net.datastream.schemas.mp_results.mp3230_001.ResultData;
import net.datastream.schemas.mp_results.mp3231_001.MP3231_AddEquipmentGeneration_001_Result;
import net.datastream.schemas.mp_results.mp3232_001.MP3232_SyncEquipmentGeneration_001_Result;
import net.datastream.schemas.mp_results.mp3234_001.MP3234_GetEquipmentGeneration_001_Result;
import net.datastream.schemas.mp_results.mp3251_001.MP3251_GenerateEquipmentGeneration_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;

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
    public String createEquipmentGeneration(InforContext context, EquipmentGeneration equipmentGeneration) throws InforException {

        net.datastream.schemas.mp_entities.equipmentgeneration_001.EquipmentGeneration inforEquipmentGeneration = new  net.datastream.schemas.mp_entities.equipmentgeneration_001.EquipmentGeneration();
        MP3231_AddEquipmentGeneration_001 addEquipmentGeneration = new MP3231_AddEquipmentGeneration_001();

        this.initializeInforEquipmentGenerationObject(inforEquipmentGeneration, equipmentGeneration, context);

        MP3231_AddEquipmentGeneration_001_Result addEquipmentGenerationResult;
        if (context.getCredentials() != null) {
            addEquipmentGenerationResult = inforws.addEquipmentGenerationOp(addEquipmentGeneration, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), applicationData.getTenant());
        } else {
            addEquipmentGenerationResult = inforws.addEquipmentGenerationOp(addEquipmentGeneration, tools.getOrganizationCode(context), null, "",
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), applicationData.getTenant());
        }

        return addEquipmentGenerationResult.getResultData().getEQUIPMENTGENERATIONID().toString();//TODO: CAN I TRANSFORM IT TO STRING????????????????????????????????????
    }

    @Override
    public String updateEquipmentGeneration(InforContext context, EquipmentGeneration equipmentGeneration) throws InforException {
        MP3232_SyncEquipmentGeneration_001 syncEquipmentGeneration = new MP3232_SyncEquipmentGeneration_001();
        MP3232_SyncEquipmentGeneration_001_Result syncEquipmentGenerationResult = new MP3232_SyncEquipmentGeneration_001_Result();

//        if (context.getCredentials() != null) {
//            syncEquipmentGenerationResult = inforws.deleteEquipmentGenerationOp(deleteEquipmentGeneration, "*",
//                    tools.createSecurityHeader(context), "TERMINATE", null,
//                    tools.createMessageConfig(), applicationData.getTenant());
//        } else {
//            syncEquipmentGenerationResult = inforws.deleteEquipmentGenerationOp(deleteEquipmentGeneration, "*", null, null, new Holder<>(tools.createInforSession(context)),
//                    tools.createMessageConfig(), applicationData.getTenant());
//        }


//        MP3232_SyncEquipmentGeneration_001_Result
//        EQUIPMENTGENERATIONID
//        ORGANIZATIONID


        return null;
    }

    @Override
    public EquipmentGeneration readEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException {

        MP3234_GetEquipmentGeneration_001 getEquipmentGeneration = new MP3234_GetEquipmentGeneration_001();
        EQUIPMENTGENERATIONID_Type equipmentGenerationID = new EQUIPMENTGENERATIONID_Type();

        equipmentGenerationID.setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);
        equipmentGenerationID.setORGANIZATIONID(tools.getOrganization(context));
        equipmentGenerationID.setDESCRIPTION(tools.getOrganization(context).getDESCRIPTION());

        MP3234_GetEquipmentGeneration_001_Result getEquipmentGenerationResult = new MP3234_GetEquipmentGeneration_001_Result();
        if (context.getCredentials() != null) {
            getEquipmentGenerationResult = inforws.getEquipmentGenerationOp(getEquipmentGeneration, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), applicationData.getTenant());
        } else {
            getEquipmentGenerationResult = inforws.getEquipmentGenerationOp(getEquipmentGeneration, tools.getOrganizationCode(context), null, "",
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), applicationData.getTenant());
        }

        net.datastream.schemas.mp_entities.equipmentgeneration_001.EquipmentGeneration inforEquipmentGeneration = getEquipmentGenerationResult.getResultData().getEquipmentGeneration();

        EquipmentGeneration equipmentGeneration = new EquipmentGeneration();

        if(inforEquipmentGeneration.getEQUIPMENTGENERATIONID() != null){
            equipmentGeneration.setEquipmentGenerationCode(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getEQUIPMENTGENERATIONCODE());
            equipmentGeneration.setEquipmentConfigurationDesc(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getDESCRIPTION());
        }

        if(inforEquipmentGeneration.getPROCESSED() != null){
            equipmentGeneration.setProcessed(inforEquipmentGeneration.getPROCESSED());
        }

        if(inforEquipmentGeneration.getEQUIPMENTGENERATIONID() != null){
            equipmentGeneration.setEquipmentConfigurationCode(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getEQUIPMENTGENERATIONCODE());
            equipmentGeneration.setEquipmentConfigurationDesc(inforEquipmentGeneration.getEQUIPMENTGENERATIONID().getDESCRIPTION());

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

//        EquipmentDetails
//        PMScheduleandMaintenancePatternDetails
//        CommissioningWorkOrderDetails
//        CreateEquipmentStructure
//        CopyData

        return equipmentGeneration;
    }

    @Override
    public EquipmentGeneration readEquipmentGenerationDefault(InforContext context, String equipmentGenerationCode) throws InforException {

        MP3230_GetEquipmentGenerationDefault_001 getEquipmentGenerationDefault = new MP3230_GetEquipmentGenerationDefault_001();

        getEquipmentGenerationDefault.setORGANIZATIONID(new ORGANIZATIONID_Type());
        getEquipmentGenerationDefault.getORGANIZATIONID().setORGANIZATIONCODE(context.getOrganizationCode());

        MP3230_GetEquipmentGenerationDefault_001_Result getEGDefaultResult = new MP3230_GetEquipmentGenerationDefault_001_Result();

        if (context.getCredentials() != null) {
            getEGDefaultResult = inforws.getEquipmentGenerationDefaultOp(getEquipmentGenerationDefault, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), applicationData.getTenant());
        } else {
            getEGDefaultResult = inforws.getEquipmentGenerationDefaultOp(getEquipmentGenerationDefault, tools.getOrganizationCode(context), null, "",
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), applicationData.getTenant());
        }

        EquipmentGeneration equipmentGeneration = new EquipmentGeneration();
        net.datastream.schemas.mp_entities.equipmentgenerationdefault_001.EquipmentGenerationDefault inforEquipmentGenerationDefault = getEGDefaultResult.getResultData().getEquipmentGenerationDefault();

        if(inforEquipmentGenerationDefault.getSTATUS() != null){
            equipmentGeneration.setStatusCode(inforEquipmentGenerationDefault.getSTATUS().getSTATUSCODE());
            equipmentGeneration.setStatusDesc(inforEquipmentGenerationDefault.getSTATUS().getDESCRIPTION());
        }

        if(inforEquipmentGenerationDefault.getPROCESSED() != null){
            equipmentGeneration.setProcessed(inforEquipmentGenerationDefault.getPROCESSED());
        }

        if(inforEquipmentGenerationDefault.getACTIVE() != null){
            equipmentGeneration.setRevisionNum(inforEquipmentGenerationDefault.getACTIVE());
        }

        if(inforEquipmentGenerationDefault.getAWAITINGPURCHASE() != null){
            equipmentGeneration.setEquipmentStatus(inforEquipmentGenerationDefault.getAWAITINGPURCHASE());
        }

        if(inforEquipmentGenerationDefault.getPROCESSERROR() != null){
            equipmentGeneration.setProcessError(inforEquipmentGenerationDefault.getPROCESSERROR());
        }

        if(inforEquipmentGenerationDefault.getPROCESSRUNNING() != null){
            equipmentGeneration.setGenerateCount(inforEquipmentGenerationDefault.getPROCESSRUNNING());
        }

        if(inforEquipmentGenerationDefault.getALLSPECIFIC() != null){
            equipmentGeneration.setValue(inforEquipmentGenerationDefault.getALLSPECIFIC());
        }

        if(inforEquipmentGenerationDefault.getSETDUEVALUES() != null){
            equipmentGeneration.setNumofDec(inforEquipmentGenerationDefault.getSETDUEVALUES());
        }

        if(inforEquipmentGenerationDefault.getACTIVATEMPS() != null){
            equipmentGeneration.setSign(inforEquipmentGenerationDefault.getACTIVATEMPS());
        }

        if(inforEquipmentGenerationDefault.getCREATECOMMISSIONINGWO() != null){
            equipmentGeneration.setUom(inforEquipmentGenerationDefault.getCREATECOMMISSIONINGWO());
        }

        if(inforEquipmentGenerationDefault.getTOPLEVELONLY() != null){
            equipmentGeneration.setTopLevelOnly(inforEquipmentGenerationDefault.getTOPLEVELONLY());
        }

        if(inforEquipmentGenerationDefault.getALLDEPENDENT() != null){
            equipmentGeneration.setAllSpecific(inforEquipmentGenerationDefault.getALLDEPENDENT());
        }

        if(inforEquipmentGenerationDefault.getALLCOSTROLLUP() != null){
            equipmentGeneration.setSetDueValues(inforEquipmentGenerationDefault.getALLCOSTROLLUP());
        }

        if(inforEquipmentGenerationDefault.getCOPYCOMMENTS() != null){
            equipmentGeneration.setActivateMps(inforEquipmentGenerationDefault.getCOPYCOMMENTS());
        }

        if(inforEquipmentGenerationDefault.getCOPYDOCUMENTS() != null){
            equipmentGeneration.setCreateCommissioningWO(inforEquipmentGenerationDefault.getCOPYDOCUMENTS());
        }

        if(inforEquipmentGenerationDefault.getCOPYCUSTOMFIELDS() != null){
            equipmentGeneration.setTopLevelOnly(inforEquipmentGenerationDefault.getCOPYCUSTOMFIELDS());
        }

        if(inforEquipmentGenerationDefault.getCOPYDEPRECIATION() != null){
            equipmentGeneration.setAllDependent(inforEquipmentGenerationDefault.getCOPYDEPRECIATION());
        }

        if(inforEquipmentGenerationDefault.getCOPYMETERS() != null){
            equipmentGeneration.setAllCostRollup(inforEquipmentGenerationDefault.getCOPYMETERS());
        }

        if(inforEquipmentGenerationDefault.getCOPYPARTSASSOCIATED() != null){
            equipmentGeneration.setCopyComments(inforEquipmentGenerationDefault.getCOPYPARTSASSOCIATED());
        }

        if(inforEquipmentGenerationDefault.getCOPYWARRANTIES() != null){
            equipmentGeneration.setCopyDocuments(inforEquipmentGenerationDefault.getCOPYWARRANTIES());
        }

        if(inforEquipmentGenerationDefault.getCOPYPMSCHEDULES() != null){
            equipmentGeneration.setCopyCustomfields(inforEquipmentGenerationDefault.getCOPYPMSCHEDULES());
        }

        if(inforEquipmentGenerationDefault.getCOPYMAINTENANCEPATTERNS() != null){
            equipmentGeneration.setCopyDepreciation(inforEquipmentGenerationDefault.getCOPYMAINTENANCEPATTERNS());
        }

        if(inforEquipmentGenerationDefault.getCOPYSAFETY() != null){
            equipmentGeneration.setCopyMeters(inforEquipmentGenerationDefault.getCOPYSAFETY());
        }

        if(inforEquipmentGenerationDefault.getCOPYPERMITS() != null){
            equipmentGeneration.setCopyPartsAssociated(inforEquipmentGenerationDefault.getCOPYPERMITS());
        }

        if(inforEquipmentGenerationDefault.getCOPYCALIBRATION() != null){
            equipmentGeneration.setCopyWarranties(inforEquipmentGenerationDefault.getCOPYCALIBRATION());
        }

        if(inforEquipmentGenerationDefault.getCOPYCALIBRATION() != null){
            equipmentGeneration.setCopyPMSchedules(inforEquipmentGenerationDefault.getCOPYCALIBRATION());
        }

        if(inforEquipmentGenerationDefault.getCOPYTESTPOINTS() != null){
            equipmentGeneration.setCopyMaintenancePatterns(inforEquipmentGenerationDefault.getCOPYTESTPOINTS());
        }

        return equipmentGeneration;
    }

    @Override
    public String deleteEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException {
        MP3233_DeleteEquipmentGeneration_001 deleteEquipmentGeneration = new MP3233_DeleteEquipmentGeneration_001();
        deleteEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
        deleteEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
        deleteEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

        if (context.getCredentials() != null) {
            inforws.deleteEquipmentGenerationOp(deleteEquipmentGeneration, "*",
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), applicationData.getTenant());
        } else {
            inforws.deleteEquipmentGenerationOp(deleteEquipmentGeneration, "*", null, null, new Holder<>(tools.createInforSession(context)),
                    tools.createMessageConfig(), applicationData.getTenant());
        }

        return equipmentGenerationCode;
    }

    @Override
    public String generateEquipmentGeneration(InforContext context, String equipmentGenerationCode) throws InforException {

        MP3251_GenerateEquipmentGeneration_001 generateEquipmentGeneration = new  MP3251_GenerateEquipmentGeneration_001();
        generateEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
        generateEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
        generateEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

        MP3251_GenerateEquipmentGeneration_001_Result generateEquipmentGenerationResult;

        if (context.getCredentials() != null) {
            generateEquipmentGenerationResult = inforws.generateEquipmentGenerationOp(generateEquipmentGeneration, "*",
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), applicationData.getTenant());
        } else {
            generateEquipmentGenerationResult = inforws.generateEquipmentGenerationOp(generateEquipmentGeneration, "*", null, null, new Holder<>(tools.createInforSession(context)),
                    tools.createMessageConfig(), applicationData.getTenant());
        }

        return generateEquipmentGenerationResult.getResultData().getEQUIPMENTGENERATIONID().toString(); //TODO: CAN I TRANSFORM IT TO STRING????????????????????????????????????
    }

    private void initializeInforEquipmentGenerationObject(net.datastream.schemas.mp_entities.equipmentgeneration_001.EquipmentGeneration inforEquipmentGeneration, EquipmentGeneration equipmentGeneration, InforContext context) throws InforException {

//        EQUIPMENTGENERATIONID
//        ORGANIZATIONID
//        EQUIPMENTCONFIGURATIONID
//        REVISIONNUM
//        EQUIPMENTSTATUS
//        GENERATECOUNT

        if (inforEquipmentGeneration.getEQUIPMENTGENERATIONID() == null) {
            inforEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
            inforEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
            inforEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE("0");
        }

        if (equipmentGeneration.getDescription() != null) {
            inforEquipmentGeneration.getEQUIPMENTGENERATIONID().setDESCRIPTION(equipmentGeneration.getDescription());
        }

        if (equipmentGeneration.getCreatedDate() != null) {
            inforEquipmentGeneration.setCREATEDDATE(tools.getDataTypeTools().encodeInforDate(equipmentGeneration.getCreatedDate(), "Created Date"));
        }
        //TODO put the rest of properties

    }
 }
