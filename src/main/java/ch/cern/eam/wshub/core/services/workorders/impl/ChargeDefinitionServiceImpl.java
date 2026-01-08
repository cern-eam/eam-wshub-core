package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.workorders.ChargeDefinitionService;
import ch.cern.eam.wshub.core.services.workorders.entities.ChargeDefinition;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.contracttemplatechargedefinition_001.ContractTemplateChargeDefinition;
import net.datastream.schemas.mp_fields.TEMPLATECHARGEDEFINITIONID_Type;
import net.datastream.schemas.mp_functions.mp7827_001.MP7827_GetContractTemplateChargeDefinition_001;
import net.datastream.schemas.mp_functions.mp7828_001.MP7828_AddContractTemplateChargeDefinition_001;
import net.datastream.schemas.mp_functions.mp7829_001.MP7829_SyncContractTemplateChargeDefinition_001;
import net.datastream.schemas.mp_functions.mp7830_001.MP7830_DeleteContractTemplateChargeDefinition_001;
import net.datastream.schemas.mp_results.mp7827_001.MP7827_GetContractTemplateChargeDefinition_001_Result;
import net.datastream.schemas.mp_results.mp7828_001.MP7828_AddContractTemplateChargeDefinition_001_Result;
import net.datastream.schemas.mp_results.mp7829_001.MP7829_SyncContractTemplateChargeDefinition_001_Result;
import net.datastream.schemas.mp_results.mp7830_001.MP7830_DeleteContractTemplateChargeDefinition_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

public class ChargeDefinitionServiceImpl implements ChargeDefinitionService {
    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public ChargeDefinitionServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesPT) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesPT;
    }

    @Override
    public String createChargeDefinition(EAMContext context, ChargeDefinition chargeDefinition) throws EAMException {
        ContractTemplateChargeDefinition chargeDefinitionEAM = new ContractTemplateChargeDefinition();
        chargeDefinition.setChargeDefinitionId("0"); //default will be overwrite
        tools.getEAMFieldTools().transformWSHubObject(chargeDefinitionEAM, chargeDefinition, context);
        MP7828_AddContractTemplateChargeDefinition_001 addContractTemplateChargeDefinition001 = new MP7828_AddContractTemplateChargeDefinition_001();
        addContractTemplateChargeDefinition001.setContractTemplateChargeDefinition(chargeDefinitionEAM);
        MP7828_AddContractTemplateChargeDefinition_001_Result result = tools.performEAMOperation(context, eamws::addContractTemplateChargeDefinitionOp, addContractTemplateChargeDefinition001);
        return result.getResultData().getTEMPLATECHARGEDEFINITIONID().getTEMPLATECHARGEDEFINITIONCODE();
    }

    @Override
    public ChargeDefinition getChargeDefinition(EAMContext context, String chargeDefinitionId) throws EAMException {
        ContractTemplateChargeDefinition chargeDefinition = readChargeDefinitionEAM(context, chargeDefinitionId);
        return tools.getEAMFieldTools().transformEAMObject(new ChargeDefinition(), chargeDefinition, context);
    }

    @Override
    public String updateChargeDefinition(EAMContext context, ChargeDefinition chargeDefinition) throws EAMException {
        MP7829_SyncContractTemplateChargeDefinition_001 syncContractTemplateChargeDefinition = new MP7829_SyncContractTemplateChargeDefinition_001();

        ContractTemplateChargeDefinition prev = readChargeDefinitionEAM(context, chargeDefinition.getChargeDefinitionId());
        tools.getEAMFieldTools().transformWSHubObject(prev, chargeDefinition, context);
        syncContractTemplateChargeDefinition.setContractTemplateChargeDefinition(prev);

        MP7829_SyncContractTemplateChargeDefinition_001_Result result = tools.performEAMOperation(context, eamws::syncContractTemplateChargeDefinitionOp, syncContractTemplateChargeDefinition);
        return  result.getResultData().getTEMPLATECHARGEDEFINITIONID().getTEMPLATECHARGEDEFINITIONCODE();
    }

    @Override
    public String deleteChargeDefinition(EAMContext context, String chargeDefinitionId) throws EAMException {
        MP7830_DeleteContractTemplateChargeDefinition_001 deleteContractTemplateChargeDefinition001 = new MP7830_DeleteContractTemplateChargeDefinition_001();
        TEMPLATECHARGEDEFINITIONID_Type type = new TEMPLATECHARGEDEFINITIONID_Type();
        type.setTEMPLATECHARGEDEFINITIONCODE(chargeDefinitionId);
        deleteContractTemplateChargeDefinition001.setTEMPLATECHARGEDEFINITIONID(type);
        MP7830_DeleteContractTemplateChargeDefinition_001_Result result = tools.performEAMOperation(context, eamws::deleteContractTemplateChargeDefinitionOp, deleteContractTemplateChargeDefinition001);
        return result.getResultData().getTEMPLATECHARGEDEFINITIONID().getTEMPLATECHARGEDEFINITIONCODE();
    }

    private ContractTemplateChargeDefinition readChargeDefinitionEAM(EAMContext eamContext, String chargeDefinitionId ) throws EAMException {
        MP7827_GetContractTemplateChargeDefinition_001 contractTemplateChargeDefinition = new MP7827_GetContractTemplateChargeDefinition_001();
        TEMPLATECHARGEDEFINITIONID_Type type = new TEMPLATECHARGEDEFINITIONID_Type();
        type.setTEMPLATECHARGEDEFINITIONCODE(chargeDefinitionId);

        contractTemplateChargeDefinition.setTEMPLATECHARGEDEFINITIONID(type);
        MP7827_GetContractTemplateChargeDefinition_001_Result result = tools.performEAMOperation(eamContext, eamws::getContractTemplateChargeDefinitionOp, contractTemplateChargeDefinition);
        return result.getResultData().getContractTemplateChargeDefinition();
    }
}
