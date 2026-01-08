package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.workorders.ContractTemplateService;
import ch.cern.eam.wshub.core.services.workorders.entities.ContractTemplate;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.CONTRACTTEMPLATEID_Type;
import net.datastream.schemas.mp_functions.mp7794_001.MP7794_GetContractTemplate_001;
import net.datastream.schemas.mp_functions.mp7795_001.MP7795_AddContractTemplate_001;
import net.datastream.schemas.mp_functions.mp7796_001.MP7796_SyncContractTemplate_001;
import net.datastream.schemas.mp_functions.mp7797_001.MP7797_DeleteContractTemplate_001;
import net.datastream.schemas.mp_results.mp7794_001.MP7794_GetContractTemplate_001_Result;
import net.datastream.schemas.mp_results.mp7795_001.MP7795_AddContractTemplate_001_Result;
import net.datastream.schemas.mp_results.mp7796_001.MP7796_SyncContractTemplate_001_Result;
import net.datastream.schemas.mp_results.mp7797_001.MP7797_DeleteContractTemplate_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

public class ContractTemplateServiceImpl implements ContractTemplateService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public ContractTemplateServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesPT) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesPT;
    }

    @Override
    public String createContractTemplate(EAMContext context, ContractTemplate contractTemplate) throws EAMException {
        net.datastream.schemas.mp_entities.contracttemplate_001.ContractTemplate contractTemplateEAM = new net.datastream.schemas.mp_entities.contracttemplate_001.ContractTemplate();
        tools.getEAMFieldTools().transformWSHubObject(contractTemplateEAM, contractTemplate, context);
        MP7795_AddContractTemplate_001 addContractTemplate = new MP7795_AddContractTemplate_001();
        addContractTemplate.setContractTemplate(contractTemplateEAM);
        MP7795_AddContractTemplate_001_Result result = tools.performEAMOperation(context, eamws::addContractTemplateOp,addContractTemplate);
        return result.getResultData().getCONTRACTTEMPLATEID().getCONTRACTTEMPLATECODE();
    }

    @Override
    public ContractTemplate getContractTemplate(EAMContext context, String contractTemplateId) throws EAMException {
        net.datastream.schemas.mp_entities.contracttemplate_001.ContractTemplate contractTemplate = readContractTemplateEAM(context, contractTemplateId);
        return tools.getEAMFieldTools().transformEAMObject(new ContractTemplate(), contractTemplate, context);
    }

    @Override
    public String updateContractTemplate(EAMContext context, ContractTemplate contractTemplate) throws EAMException {
        MP7796_SyncContractTemplate_001 syncContractTemplate001 = new MP7796_SyncContractTemplate_001();
        net.datastream.schemas.mp_entities.contracttemplate_001.ContractTemplate prev = readContractTemplateEAM(context, contractTemplate.getContractTemplateId());
        tools.getEAMFieldTools().transformWSHubObject(prev, contractTemplate, context);
        syncContractTemplate001.setContractTemplate(prev);
        MP7796_SyncContractTemplate_001_Result result = tools.performEAMOperation(context, eamws::syncContractTemplateOp, syncContractTemplate001);
        return result.getResultData().getCONTRACTTEMPLATEID().getCONTRACTTEMPLATECODE();
    }

    @Override
    public String deleteContractTemplate(EAMContext context, String contractTemplateId) throws EAMException {
        MP7797_DeleteContractTemplate_001 deleteContractTemplate001 = new MP7797_DeleteContractTemplate_001();
        CONTRACTTEMPLATEID_Type type =  new CONTRACTTEMPLATEID_Type();
        type.setCONTRACTTEMPLATECODE(contractTemplateId);
        type.setORGANIZATIONID(tools.getOrganization(context));
        deleteContractTemplate001.setCONTRACTTEMPLATEID(type);
        MP7797_DeleteContractTemplate_001_Result result = tools.performEAMOperation(context, eamws::deleteContractTemplateOp, deleteContractTemplate001);
        return result.getResultData().getCONTRACTTEMPLATEID().getCONTRACTTEMPLATECODE();
    }

    private net.datastream.schemas.mp_entities.contracttemplate_001.ContractTemplate readContractTemplateEAM(EAMContext eamContext, String contractTemplateId ) throws EAMException {
        MP7794_GetContractTemplate_001 contractTemplate001 = new MP7794_GetContractTemplate_001();
        CONTRACTTEMPLATEID_Type type = new CONTRACTTEMPLATEID_Type();
        type.setCONTRACTTEMPLATECODE(contractTemplateId);
        type.setORGANIZATIONID(tools.getOrganization(eamContext));
        contractTemplate001.setCONTRACTTEMPLATEID(type);
        MP7794_GetContractTemplate_001_Result result = tools.performEAMOperation(eamContext, eamws::getContractTemplateOp, contractTemplate001);
        return result.getResultData().getContractTemplate();
    }
}
