package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.EAMCase;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface CaseService {

    @Operation(logOperation = EAM_OPERATION.CASE_R, logDataReference1 = LogDataReferenceType.INPUT)
    EAMCase readCase(EAMContext context, String caseID) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CASE_C, logDataReference1 = LogDataReferenceType.RESULT, logDataReference2 = LogDataReferenceType.RESULT)
    String createCase(EAMContext context, EAMCase caseMT) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CASE_D)
    String deleteCase(EAMContext context, String caseID) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CASE_U, logDataReference1 = LogDataReferenceType.RESULT, logDataReference2 = LogDataReferenceType.RESULT)
    String updateCase(EAMContext context, EAMCase caseMT) throws EAMException;
}
