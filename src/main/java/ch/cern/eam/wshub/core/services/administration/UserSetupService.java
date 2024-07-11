package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.administration.entities.EAMUser;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;

public interface UserSetupService {
    @Operation(logOperation = EAM_OPERATION.LOGIN)
    String login(EAMContext context, String userCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERSETUP_R, logDataReference1 = LogDataReferenceType.INPUT)
    EAMUser readUserSetup(EAMContext context, String userCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERSETUP_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createUserSetup(EAMContext context, EAMUser userParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERSETUP_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "userCode")
    String updateUserSetup(EAMContext context, EAMUser userParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERSETUP_UB, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> updateUserSetupBatch(EAMContext context, List<EAMUser> userParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERSETUP_D)
    String deleteUserSetup(EAMContext context, String userCode) throws EAMException;
}
