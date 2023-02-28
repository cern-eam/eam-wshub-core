package ch.cern.eam.wshub.core.services.casemanagement;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.casemanagement.entities.EAMCaseManagement;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCase;
import ch.cern.eam.wshub.core.tools.InforException;

public interface CaseManagementService {
    @Operation(logOperation = INFOR_OPERATION.CASEMGMT_C, logDataReference1 = LogDataReferenceType.RESULT, logDataReference2 = LogDataReferenceType.RESULT)
    String createCase(InforContext context, EAMCaseManagement eamCaseManagement) throws InforException;
    @Operation(logOperation = INFOR_OPERATION.CASEMGMT_R, logDataReference1 = LogDataReferenceType.INPUT)
    EAMCaseManagement readCase(InforContext context, String caseCode) throws InforException;
    @Operation(logOperation = INFOR_OPERATION.CASEMGMT_U)
    String deleteCase(InforContext context, String caseCode) throws InforException;
    @Operation(logOperation = INFOR_OPERATION.CASEMGMT_D, logDataReference1 = LogDataReferenceType.RESULT,
            logDataReference2 = LogDataReferenceType.RESULT)
    String updateCase(InforContext context, EAMCaseManagement eamCaseManagement) throws InforException;
}
