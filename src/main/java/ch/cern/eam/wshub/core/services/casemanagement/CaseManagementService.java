package ch.cern.eam.wshub.core.services.casemanagement;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.casemanagement.entities.EAMCaseManagement;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface CaseManagementService {
    @Operation(logOperation = EAM_OPERATION.CASEMGMT_C, logDataReference1 = LogDataReferenceType.RESULT, logDataReference2 = LogDataReferenceType.RESULT)
    String createCase(EAMContext context, EAMCaseManagement eamCaseManagement) throws EAMException;
    @Operation(logOperation = EAM_OPERATION.CASEMGMT_R, logDataReference1 = LogDataReferenceType.INPUT)
    EAMCaseManagement readCase(EAMContext context, String caseCode) throws EAMException;
    @Operation(logOperation = EAM_OPERATION.CASEMGMT_U)
    String deleteCase(EAMContext context, String caseCode) throws EAMException;
    @Operation(logOperation = EAM_OPERATION.CASEMGMT_D, logDataReference1 = LogDataReferenceType.RESULT,
            logDataReference2 = LogDataReferenceType.RESULT)
    String updateCase(EAMContext context, EAMCaseManagement eamCaseManagement) throws EAMException;
}
