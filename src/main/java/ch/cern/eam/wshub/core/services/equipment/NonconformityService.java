package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.Nonconformity;
import ch.cern.eam.wshub.core.tools.InforException;

public interface NonconformityService {

    @Operation(logOperation = INFOR_OPERATION.NONCONF_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createNonconformity(InforContext context, Nonconformity nonconformityParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_R_DEF)
    Nonconformity readNonconformityDefault(InforContext context) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_R, logDataReference1 = LogDataReferenceType.INPUT)
    Nonconformity readNonconformity(InforContext context, String nonconformityCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateNonconformity(InforContext context, Nonconformity nonconformityParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_D)
    String deleteNonconformity(InforContext context, String nonconformityCode) throws InforException;

}
