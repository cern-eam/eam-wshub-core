package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCaseTask;
import ch.cern.eam.wshub.core.tools.InforException;

public interface CaseTaskService {

    @Operation(logOperation = INFOR_OPERATION.CASE_TASK_R, logDataReference1 = LogDataReferenceType.INPUT)
    InforCaseTask readCaseTask(InforContext context, String caseTaskID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.CASE_TASK_C, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "caseCode", logDataReference2 = LogDataReferenceType.RESULT)
    String createCaseTask(InforContext context, InforCaseTask caseTaskMT) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.CASE_TASK_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "caseCode", logDataReference2 = LogDataReferenceType.RESULT)
    String updateCaseTask(InforContext context, InforCaseTask caseTaskMT) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.CASE_TASK_D)
    String deleteCaseTask(InforContext context, String caseTaskID) throws InforException;
}
