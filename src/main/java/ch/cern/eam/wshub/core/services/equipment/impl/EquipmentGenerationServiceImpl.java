package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentGenerationService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentGenerationEntity;
import org.openapplications.oagis_segments.QUANTITY;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeQuantity;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeQuantity;

import jakarta.xml.ws.Holder;
import java.math.BigDecimal;

public class EquipmentGenerationServiceImpl implements EquipmentGenerationService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public EquipmentGenerationServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    @Override
    public String createEquipmentGeneration(EAMContext context, EquipmentGenerationEntity equipmentGeneration) throws EAMException {

            EquipmentGeneration eamEquipmentGeneration = new EquipmentGeneration();
            MP3231_AddEquipmentGeneration_001 addEquipmentGeneration = new MP3231_AddEquipmentGeneration_001();
            initializeEquipmentGenerationObject(eamEquipmentGeneration, equipmentGeneration, context);
            addEquipmentGeneration.setEquipmentGeneration(eamEquipmentGeneration);
            MP3231_AddEquipmentGeneration_001_Result addEquipmentGenerationResult =
                tools.performEAMOperation(context, eamws::addEquipmentGenerationOp, addEquipmentGeneration);
            return addEquipmentGenerationResult.getResultData().getEQUIPMENTGENERATIONID().getEQUIPMENTGENERATIONCODE();
    }

    private void updateEAMEquipmentGeneration(EAMContext context, EquipmentGeneration equipmentGeneration) throws EAMException {
        MP3232_SyncEquipmentGeneration_001 syncEquipmentGeneration = new MP3232_SyncEquipmentGeneration_001();
        syncEquipmentGeneration.setEquipmentGeneration(equipmentGeneration);
        tools.performEAMOperation(context, eamws::syncEquipmentGenerationOp, syncEquipmentGeneration);
    }

    @Override
    public String updateEquipmentGeneration(EAMContext context, EquipmentGenerationEntity equipmentGeneration) throws EAMException {
        try {
            EquipmentGeneration eamEquipmentGeneration = readEAMEquipmentGeneration(context, equipmentGeneration.getEquipmentGenerationCode());
            initializeEquipmentGenerationObject(eamEquipmentGeneration, equipmentGeneration, context);
            this.updateEAMEquipmentGeneration(context, eamEquipmentGeneration);

        }catch(Exception exception){
            exception.printStackTrace();
        }
        return equipmentGeneration.getEquipmentGenerationCode();
    }

    @Override
    public String deleteEquipmentGeneration(EAMContext context, String equipmentGenerationCode) throws EAMException {
        MP3233_DeleteEquipmentGeneration_001 deleteEquipmentGeneration = new MP3233_DeleteEquipmentGeneration_001();

        deleteEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
        deleteEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
        deleteEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID().setORGANIZATIONCODE(tools.getOrganizationCode(context));
        deleteEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

        tools.performEAMOperation(context, eamws::deleteEquipmentGenerationOp, deleteEquipmentGeneration);

        return equipmentGenerationCode;
    }

    @Override
    public String createEquipmentGenerationPreview(EAMContext context, String equipmentGenerationCode) throws EAMException {
        MP3235_CreateEquipmentGenerationPreview_001 createEquipmentGenerationPreview = new MP3235_CreateEquipmentGenerationPreview_001();

        createEquipmentGenerationPreview.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
        createEquipmentGenerationPreview.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
        createEquipmentGenerationPreview.getEQUIPMENTGENERATIONID().getORGANIZATIONID().setORGANIZATIONCODE(tools.getOrganizationCode(context));
        createEquipmentGenerationPreview.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

        MP3235_CreateEquipmentGenerationPreview_001_Result createEquipmentGenerationPreviewResult =
            tools.performEAMOperation(
                context,
                eamws::createEquipmentGenerationPreviewOp,
                createEquipmentGenerationPreview);

        return createEquipmentGenerationPreviewResult.getResultData().getEQUIPMENTGENERATIONID().toString();
    }

    @Override
    public String generateEquipmentGeneration(EAMContext context, String equipmentGenerationCode) throws EAMException {

        MP3251_GenerateEquipmentGeneration_001 generateEquipmentGeneration = new  MP3251_GenerateEquipmentGeneration_001();
        generateEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
        generateEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
        generateEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID().setORGANIZATIONCODE(tools.getOrganizationCode(context));
        generateEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

        MP3251_GenerateEquipmentGeneration_001_Result generateEquipmentGenerationResult =
            tools.performEAMOperation(context, eamws::generateEquipmentGenerationOp, generateEquipmentGeneration);

        return generateEquipmentGenerationResult.getResultData().getEQUIPMENTGENERATIONID().toString();
    }

    private EquipmentGeneration readEAMEquipmentGeneration(EAMContext context, String equipmentGenerationCode)
            throws EAMException {

        MP3234_GetEquipmentGeneration_001 getEquipmentGeneration = new MP3234_GetEquipmentGeneration_001();
        getEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
        getEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
        getEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE(equipmentGenerationCode);

        MP3234_GetEquipmentGeneration_001_Result getEquipmentGenerationResult =
            tools.performEAMOperation(context, eamws::getEquipmentGenerationOp, getEquipmentGeneration);
        return getEquipmentGenerationResult.getResultData().getEquipmentGeneration();

    }

    @Override
    public EquipmentGenerationEntity readEquipmentGeneration(EAMContext context, String equipmentGenerationCode) throws EAMException {

        EquipmentGeneration eamEquipmentGeneration = readEAMEquipmentGeneration(context, equipmentGenerationCode);
        EquipmentGenerationEntity equipmentGeneration = new EquipmentGenerationEntity();


        if(eamEquipmentGeneration.getEQUIPMENTGENERATIONID() != null){
            equipmentGeneration.setEquipmentGenerationCode(eamEquipmentGeneration.getEQUIPMENTGENERATIONID().getEQUIPMENTGENERATIONCODE());
            equipmentGeneration.setEquipmentGenerationDesc(eamEquipmentGeneration.getEQUIPMENTGENERATIONID().getDESCRIPTION());
            equipmentGeneration.setDescription(eamEquipmentGeneration.getEQUIPMENTGENERATIONID().getDESCRIPTION());
        }

        if(eamEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID() != null){
            equipmentGeneration.setOrganizationCode(eamEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID().getORGANIZATIONCODE());
            equipmentGeneration.setOrganizationDesc(eamEquipmentGeneration.getEQUIPMENTGENERATIONID().getORGANIZATIONID().getDESCRIPTION());
        }

        if(eamEquipmentGeneration.getPROCESSED() != null){
            equipmentGeneration.setProcessed(eamEquipmentGeneration.getPROCESSED());
        }

        if(eamEquipmentGeneration.getACTIVE() != null){
            equipmentGeneration.setActive(eamEquipmentGeneration.getACTIVE());
        }

        if(eamEquipmentGeneration.getAWAITINGPURCHASE() != null){
            equipmentGeneration.setAwaitingPurchase(eamEquipmentGeneration.getAWAITINGPURCHASE());
        }

        if(eamEquipmentGeneration.getPROCESSERROR() != null){
            equipmentGeneration.setProcessError(eamEquipmentGeneration.getPROCESSERROR());
        }

        if(eamEquipmentGeneration.getPROCESSRUNNING() != null){
            equipmentGeneration.setProcessRunning(eamEquipmentGeneration.getPROCESSRUNNING());
        }

        if(eamEquipmentGeneration.getLASTUPDATEDDATE() != null){
            equipmentGeneration.setLastUpdatedDate(tools.getDataTypeTools().decodeEAMDate(eamEquipmentGeneration.getLASTUPDATEDDATE()));
        }

        if(eamEquipmentGeneration.getCREATEDDATE() != null){
            equipmentGeneration.setCreatedDate(tools.getDataTypeTools().decodeEAMDate(eamEquipmentGeneration.getCREATEDDATE()));
        }

        if(eamEquipmentGeneration.getCREATEDBY() != null){
            equipmentGeneration.setCreatedBy(eamEquipmentGeneration.getCREATEDBY().getUSERCODE());
        }

        if(eamEquipmentGeneration.getUPDATEDBY() != null){
            equipmentGeneration.setUpdatedBy(eamEquipmentGeneration.getUPDATEDBY().getUSERCODE());
        }
        if(eamEquipmentGeneration.getDATEUPDATED() != null){
            equipmentGeneration.setDateUpdated(tools.getDataTypeTools().decodeEAMDate(eamEquipmentGeneration.getDATEUPDATED()));
        }
        if(eamEquipmentGeneration.getEquipmentDetails() != null){
            EquipmentDetails equipmentDetails = eamEquipmentGeneration.getEquipmentDetails();
            if(equipmentDetails.getGENERATECOUNT() != null){
                equipmentGeneration.setGenerateCount(tools.getDataTypeTools().decodeQuantity(equipmentDetails.getGENERATECOUNT()));
            }

            if(equipmentDetails.getEQUIPMENTCONFIGURATIONID() != null){
                equipmentGeneration.setEquipmentConfigurationCode(eamEquipmentGeneration.getEquipmentDetails().getEQUIPMENTCONFIGURATIONID().getEQUIPMENTCONFIGURATIONCODE());
                equipmentGeneration.setEquipmentConfigurationDesc(eamEquipmentGeneration.getEquipmentDetails().getEQUIPMENTCONFIGURATIONID().getDESCRIPTION());
            }

            if(equipmentDetails.getEQUIPMENTCONFIGURATIONID() != null && equipmentDetails.getEQUIPMENTCONFIGURATIONID().getREVISIONNUM() != null){
                equipmentGeneration.setRevisionNum(tools.getDataTypeTools().decodeQuantity(eamEquipmentGeneration.getEquipmentDetails().getEQUIPMENTCONFIGURATIONID().getREVISIONNUM()));
            }

            if(equipmentDetails.getEQUIPMENTSTATUS() != null){
                equipmentGeneration.setEquipmentStatusCode(eamEquipmentGeneration.getEquipmentDetails().getEQUIPMENTSTATUS().getSTATUSCODE());
                equipmentGeneration.setEquipmentStatusDesc(eamEquipmentGeneration.getEquipmentDetails().getEQUIPMENTSTATUS().getDESCRIPTION());
            }

            if(equipmentDetails.getALLSPECIFIC() != null){
                equipmentGeneration.setAllSpecific(eamEquipmentGeneration.getEquipmentDetails().getALLSPECIFIC());
            }

            if(equipmentDetails.getCOMMISSIONDATE() != null){
                equipmentGeneration.setCommissionDate(tools.getDataTypeTools().decodeEAMDate(eamEquipmentGeneration.getEquipmentDetails().getCOMMISSIONDATE()));
            }

            if(equipmentDetails.getLOCATIONID() != null){
                equipmentGeneration.setEquipmentLocationCode(eamEquipmentGeneration.getEquipmentDetails().getLOCATIONID().getLOCATIONCODE());
                equipmentGeneration.setEquipmentLocationDesc(eamEquipmentGeneration.getEquipmentDetails().getLOCATIONID().getDESCRIPTION());
            }

            if(equipmentDetails.getCOSTCODEID() != null){
                equipmentGeneration.setEquipmentCostCode(eamEquipmentGeneration.getEquipmentDetails().getCOSTCODEID().getCOSTCODE());
                equipmentGeneration.setEquipmentCostCodeDesc(eamEquipmentGeneration.getEquipmentDetails().getCOSTCODEID().getDESCRIPTION());
            }

            if(equipmentDetails.getDEPARTMENTID() != null){
                equipmentGeneration.setEquipmentDepartmentCode(eamEquipmentGeneration.getEquipmentDetails().getDEPARTMENTID().getDEPARTMENTCODE());
                equipmentGeneration.setEquipmentDepartmentDesc(eamEquipmentGeneration.getEquipmentDetails().getDEPARTMENTID().getDESCRIPTION());
            }

            if(equipmentDetails.getASSIGNEDTO() != null){
                equipmentGeneration.setEquipmentAssignedToCode(eamEquipmentGeneration.getEquipmentDetails().getASSIGNEDTO().getPERSONCODE());
                equipmentGeneration.setEquipmentAssignedToDesc(eamEquipmentGeneration.getEquipmentDetails().getASSIGNEDTO().getDESCRIPTION());
            }
        }

        if(eamEquipmentGeneration.getCommissioningWorkOrderDetails() != null) {
            CommissioningWorkOrderDetails workOrderDetails = eamEquipmentGeneration.getCommissioningWorkOrderDetails();

            if(workOrderDetails.getCOMMISSIONINGWORKORDERID() != null) {
                equipmentGeneration.setCommissioningWONumber(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getCOMMISSIONINGWORKORDERID().getJOBNUM());
                equipmentGeneration.setCommissioningWODesc(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getCOMMISSIONINGWORKORDERID().getDESCRIPTION());
            }

            if(workOrderDetails.getASSIGNEDTO() != null){
                equipmentGeneration.setCommissioningWOAssignedTo(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getASSIGNEDTO().getPERSONCODE());
            }

            if(workOrderDetails.getCOSTCODEID() != null){
                equipmentGeneration.setCommissioningWOCostCode(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getCOSTCODEID().getCOSTCODE());
                equipmentGeneration.setCommissioningWOCostDesc(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getCOSTCODEID().getDESCRIPTION());
            }

            if(workOrderDetails.getCREATECOMMISSIONINGWO() != null){
                equipmentGeneration.setCreateCommissioningWO(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getCREATECOMMISSIONINGWO());
            }

            if(workOrderDetails.getSTATUS() != null) {
                equipmentGeneration.setCommissioningWOStatusCode(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getSTATUS().getSTATUSCODE());
                equipmentGeneration.setCommissioningWOStatusDesc(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getSTATUS().getDESCRIPTION());
            }

            if(workOrderDetails.getDEPARTMENTID() != null) {
                equipmentGeneration.setCommissioningWODepartmentCode(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getDEPARTMENTID().getDEPARTMENTCODE());
                equipmentGeneration.setCommissioningWODepartmentDesc(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getDEPARTMENTID().getDESCRIPTION());
            }

            if(workOrderDetails.getLOCATIONID() != null) {
                equipmentGeneration.setCommissioningWOLocationCode(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getLOCATIONID().getLOCATIONCODE());
                equipmentGeneration.setCommissioningWOLocationDesc(eamEquipmentGeneration.getCommissioningWorkOrderDetails().getLOCATIONID().getDESCRIPTION());
            }
        }

        if( eamEquipmentGeneration.getCopyData() != null ) {

            if(eamEquipmentGeneration.getCopyData().getCOPYCALIBRATION() != null) {
                equipmentGeneration.setCopyCalibration(eamEquipmentGeneration.getCopyData().getCOPYCALIBRATION());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYCOMMENTS() != null) {
                equipmentGeneration.setCopyComments(eamEquipmentGeneration.getCopyData().getCOPYCOMMENTS());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYCUSTOMFIELDS() != null) {
                equipmentGeneration.setCopyCustomfields(eamEquipmentGeneration.getCopyData().getCOPYCUSTOMFIELDS());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYDEPRECIATION() != null) {
                equipmentGeneration.setCopyDepreciation(eamEquipmentGeneration.getCopyData().getCOPYDEPRECIATION());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYDOCUMENTS() != null) {
                equipmentGeneration.setCopyDocuments(eamEquipmentGeneration.getCopyData().getCOPYDOCUMENTS());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYMAINTENANCEPATTERNS() != null) {
                equipmentGeneration.setCopyMaintenancePatterns(eamEquipmentGeneration.getCopyData().getCOPYMAINTENANCEPATTERNS());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYMETERS() != null) {
                equipmentGeneration.setCopyMeters(eamEquipmentGeneration.getCopyData().getCOPYMETERS());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYPARTSASSOCIATED() != null) {
                equipmentGeneration.setCopyPartsAssociated(eamEquipmentGeneration.getCopyData().getCOPYPARTSASSOCIATED());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYPERMITS() != null) {
                equipmentGeneration.setCopyPermits(eamEquipmentGeneration.getCopyData().getCOPYPERMITS());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYPMSCHEDULES() != null) {
                equipmentGeneration.setCopyPMSchedules(eamEquipmentGeneration.getCopyData().getCOPYPMSCHEDULES());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYSAFETY() != null) {
                equipmentGeneration.setCopySafety(eamEquipmentGeneration.getCopyData().getCOPYSAFETY());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYTESTPOINTS() != null) {
                equipmentGeneration.setCopyTestPoints(eamEquipmentGeneration.getCopyData().getCOPYTESTPOINTS());
            }
            if(eamEquipmentGeneration.getCopyData().getCOPYWARRANTIES() != null){
                equipmentGeneration.setCopyWarranties(eamEquipmentGeneration.getCopyData().getCOPYWARRANTIES());
            }
        }

        return equipmentGeneration;
    }

    @Override
    public EquipmentGenerationEntity readEquipmentGenerationDefault(EAMContext context, String equipmentGenerationCode) throws EAMException {

        MP3230_GetEquipmentGenerationDefault_001 getEquipmentGenerationDefault = new MP3230_GetEquipmentGenerationDefault_001();

        getEquipmentGenerationDefault.setORGANIZATIONID(tools.getOrganization(context));
        getEquipmentGenerationDefault.getORGANIZATIONID().setORGANIZATIONCODE(tools.getOrganizationCode(context));

        MP3230_GetEquipmentGenerationDefault_001_Result getEGDefaultResult = new MP3230_GetEquipmentGenerationDefault_001_Result();

        getEGDefaultResult = tools.performEAMOperation(context,
            eamws::getEquipmentGenerationDefaultOp,
            getEquipmentGenerationDefault);
        EquipmentGenerationEntity equipmentGeneration = new EquipmentGenerationEntity();
        net.datastream.schemas.mp_entities.equipmentgenerationdefault_001.EquipmentGenerationDefault eamEquipmentGenerationDefault =
                getEGDefaultResult.getResultData().getEquipmentGenerationDefault();

        if(context.getOrganizationCode() != null) {
            equipmentGeneration.setOrganizationCode(context.getOrganizationCode());
        }

        if(eamEquipmentGenerationDefault.getSTATUS() != null){
            equipmentGeneration.setStatusCode(eamEquipmentGenerationDefault.getSTATUS().getSTATUSCODE());
            equipmentGeneration.setStatusDesc(eamEquipmentGenerationDefault.getSTATUS().getDESCRIPTION());
        }

        if(eamEquipmentGenerationDefault.getPROCESSED() != null){
            equipmentGeneration.setProcessed(eamEquipmentGenerationDefault.getPROCESSED());
        }

        if(eamEquipmentGenerationDefault.getACTIVE() != null){
            equipmentGeneration.setActive(eamEquipmentGenerationDefault.getACTIVE());
        }

        if(eamEquipmentGenerationDefault.getAWAITINGPURCHASE() != null){
            equipmentGeneration.setAwaitingPurchase(eamEquipmentGenerationDefault.getAWAITINGPURCHASE());
        }

        if(eamEquipmentGenerationDefault.getPROCESSERROR() != null){
            equipmentGeneration.setProcessError(eamEquipmentGenerationDefault.getPROCESSERROR());
        }

        if(eamEquipmentGenerationDefault.getPROCESSRUNNING() != null){
            equipmentGeneration.setProcessRunning(eamEquipmentGenerationDefault.getPROCESSRUNNING());
        }

        if(eamEquipmentGenerationDefault.getTOPLEVELONLY() != null){
            equipmentGeneration.setTopLevelOnly(eamEquipmentGenerationDefault.getTOPLEVELONLY());
        }

        if(eamEquipmentGenerationDefault.getALLDEPENDENT() != null){
            equipmentGeneration.setAllDependent(eamEquipmentGenerationDefault.getALLDEPENDENT());
        }

        if(eamEquipmentGenerationDefault.getALLCOSTROLLUP() != null){
            equipmentGeneration.setAllCostRollup(eamEquipmentGenerationDefault.getALLCOSTROLLUP());
        }

        if(eamEquipmentGenerationDefault.getCOPYCOMMENTS() != null){
            equipmentGeneration.setCopyComments(eamEquipmentGenerationDefault.getCOPYCOMMENTS());
        }

        if(eamEquipmentGenerationDefault.getCOPYDOCUMENTS() != null){
            equipmentGeneration.setCopyDocuments(eamEquipmentGenerationDefault.getCOPYDOCUMENTS());
        }

        if(eamEquipmentGenerationDefault.getCOPYCUSTOMFIELDS() != null){
            equipmentGeneration.setCopyCustomfields(eamEquipmentGenerationDefault.getCOPYCUSTOMFIELDS());
        }

        if(eamEquipmentGenerationDefault.getCOPYDEPRECIATION() != null){
            equipmentGeneration.setCopyDepreciation(eamEquipmentGenerationDefault.getCOPYDEPRECIATION());
        }

        if(eamEquipmentGenerationDefault.getCOPYMETERS() != null){
            equipmentGeneration.setCopyMeters(eamEquipmentGenerationDefault.getCOPYMETERS());
        }

        if(eamEquipmentGenerationDefault.getCOPYPARTSASSOCIATED() != null){
            equipmentGeneration.setCopyPartsAssociated(eamEquipmentGenerationDefault.getCOPYPARTSASSOCIATED());
        }

        if(eamEquipmentGenerationDefault.getCOPYWARRANTIES() != null){
            equipmentGeneration.setCopyWarranties(eamEquipmentGenerationDefault.getCOPYWARRANTIES());
        }

        if(eamEquipmentGenerationDefault.getCOPYPMSCHEDULES() != null){
            equipmentGeneration.setCopyPMSchedules(eamEquipmentGenerationDefault.getCOPYPMSCHEDULES());
        }

        if(eamEquipmentGenerationDefault.getCOPYMAINTENANCEPATTERNS() != null){
            equipmentGeneration.setCopyDepreciation(eamEquipmentGenerationDefault.getCOPYMAINTENANCEPATTERNS());
        }

        if(eamEquipmentGenerationDefault.getCOPYSAFETY() != null){
            equipmentGeneration.setCopySafety(eamEquipmentGenerationDefault.getCOPYSAFETY());
        }

        if(eamEquipmentGenerationDefault.getCOPYPERMITS() != null){
            equipmentGeneration.setCopyPermits(eamEquipmentGenerationDefault.getCOPYPERMITS());
        }

        if(eamEquipmentGenerationDefault.getCOPYCALIBRATION() != null){
            equipmentGeneration.setCopyCalibration(eamEquipmentGenerationDefault.getCOPYCALIBRATION());
        }

        if(eamEquipmentGenerationDefault.getCOPYTESTPOINTS() != null){
            equipmentGeneration.setCopyTestPoints(eamEquipmentGenerationDefault.getCOPYTESTPOINTS());
        }
        return equipmentGeneration;
    }


    private void initializeEquipmentGenerationObject(EquipmentGeneration eamEquipmentGeneration, EquipmentGenerationEntity equipmentGeneration, EAMContext context) throws EAMException {

        EquipmentDetails equipmentDetails = new EquipmentDetails();
        CommissioningWorkOrderDetails workOrderDetails = new CommissioningWorkOrderDetails();
        CopyData copyData = new CopyData();

        if (eamEquipmentGeneration.getEQUIPMENTGENERATIONID() == null) {
            eamEquipmentGeneration.setEQUIPMENTGENERATIONID(new EQUIPMENTGENERATIONID_Type());
            eamEquipmentGeneration.getEQUIPMENTGENERATIONID().setORGANIZATIONID(tools.getOrganization(context));
            eamEquipmentGeneration.getEQUIPMENTGENERATIONID().setEQUIPMENTGENERATIONCODE("0");
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
            equipmentDetails.getEQUIPMENTCONFIGURATIONID().setREVISIONNUM(encodeQuantity(equipmentGeneration.getRevisionNum(), "Revision Number"));
        }


        if(equipmentGeneration.getGenerateCount() != null){
            equipmentDetails.setGENERATECOUNT(encodeQuantity(equipmentGeneration.getGenerateCount(), "Generate Count"));
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
            equipmentDetails.setCOMMISSIONDATE(tools.getDataTypeTools().encodeEAMDate(equipmentGeneration.getCommissionDate(), "Commission Date"));
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
            eamEquipmentGeneration.getEQUIPMENTGENERATIONID().setDESCRIPTION(equipmentGeneration.getDescription());
        }

        if (equipmentGeneration.getCreatedDate() != null) {
            eamEquipmentGeneration.setCREATEDDATE(tools.getDataTypeTools().encodeEAMDate(equipmentGeneration.getCreatedDate(), "Created Date"));
        }

        if (equipmentGeneration.getProcessed() != null) {
            eamEquipmentGeneration.setPROCESSED(equipmentGeneration.getProcessed());
        }

        if (equipmentGeneration.getActive() != null) {
            eamEquipmentGeneration.setACTIVE(equipmentGeneration.getActive());
        }

        if (equipmentGeneration.getAwaitingPurchase() != null) {
            eamEquipmentGeneration.setAWAITINGPURCHASE(equipmentGeneration.getAwaitingPurchase());
        }

        if (equipmentGeneration.getProcessError() != null) {
            eamEquipmentGeneration.setPROCESSERROR(equipmentGeneration.getProcessError());
        }

        if (equipmentGeneration.getProcessRunning() != null) {
            eamEquipmentGeneration.setPROCESSRUNNING(equipmentGeneration.getProcessRunning());
        }

        if (equipmentGeneration.getLastUpdatedDate() != null) {
            eamEquipmentGeneration.setLASTUPDATEDDATE(tools.getDataTypeTools().encodeEAMDate(equipmentGeneration.getLastUpdatedDate(), "Last Updated Date"));
        }

        if (equipmentGeneration.getDateUpdated() != null) {
            eamEquipmentGeneration.setDATEUPDATED(tools.getDataTypeTools().encodeEAMDate(equipmentGeneration.getDateUpdated(), "Date Updated"));
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
            eamEquipmentGeneration.setEquipmentDetails(equipmentDetails);
        }

        if(workOrderDetails != null){
            eamEquipmentGeneration.setCommissioningWorkOrderDetails(workOrderDetails);
        }

        if(copyData != null){
            eamEquipmentGeneration.setCopyData(copyData);
        }

    }
 }
