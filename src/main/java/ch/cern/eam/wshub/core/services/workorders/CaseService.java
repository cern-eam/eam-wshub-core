package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCase;
import ch.cern.eam.wshub.core.tools.InforException;

public interface CaseService {

    @Operation(logOperation = INFOR_OPERATION.CASE_R, logDataReference1 = LogDataReferenceType.INPUT)
    InforCase readCase(InforContext context, String caseID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.CASE_C, logDataReference1 = LogDataReferenceType.RESULT, logDataReference2 = LogDataReferenceType.RESULT)
    String createCase(InforContext context, InforCase caseMT) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.CASE_D)
    String deleteCase(InforContext context, String caseID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.CASE_U, logDataReference1 = LogDataReferenceType.RESULT, logDataReference2 = LogDataReferenceType.RESULT)
    String updateCase(InforContext context, InforCase caseMT) throws InforException;
}
