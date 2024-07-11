package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.EAMCaseTask;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface CaseTaskService {

    @Operation(logOperation = EAM_OPERATION.CASE_TASK_R, logDataReference1 = LogDataReferenceType.INPUT)
    EAMCaseTask readCaseTask(EAMContext context, String caseTaskID) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CASE_TASK_C, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "caseCode", logDataReference2 = LogDataReferenceType.RESULT)
    String createCaseTask(EAMContext context, EAMCaseTask caseTaskMT) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CASE_TASK_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "caseCode", logDataReference2 = LogDataReferenceType.RESULT)
    String updateCaseTask(EAMContext context, EAMCaseTask caseTaskMT) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CASE_TASK_D)
    String deleteCaseTask(EAMContext context, String caseTaskID) throws EAMException;
}
