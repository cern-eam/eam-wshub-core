package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.ContractTemplate;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface ContractTemplateService {
    @Operation(logOperation = EAM_OPERATION.CONTRACTTEMPLATE_CREATE)
    String createContractTemplate(EAMContext context, ContractTemplate contractTemplate) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CONTRACTTEMPLATE_READ)
    ContractTemplate getContractTemplate(EAMContext context, String contractTemplateId) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CONTRACTTEMPLATE_UPDATE)
    String updateContractTemplate(EAMContext context, ContractTemplate contractTemplate) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CONTRACTTEMPLATE_DELETE)
    String deleteContractTemplate(EAMContext context, String contractTemplateId) throws EAMException;
}
