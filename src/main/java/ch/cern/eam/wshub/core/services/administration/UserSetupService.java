package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.administration.entities.EAMUser;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface UserSetupService {

    @Operation(logOperation = INFOR_OPERATION.LOGIN)
    String login(InforContext context, String userCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERSETUP_R, logDataReference1 = LogDataReferenceType.INPUT)
    EAMUser readUserSetup(InforContext context, String userCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERSETUP_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createUserSetup(InforContext context, EAMUser userParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERSETUP_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "userCode")
    String updateUserSetup(InforContext context, EAMUser userParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERSETUP_UB, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> updateUserSetupBatch(InforContext context, List<EAMUser> userParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERSETUP_D)
    String deleteUserSetup(InforContext context, String userCode) throws InforException;
}
