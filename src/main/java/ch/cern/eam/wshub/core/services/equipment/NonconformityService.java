package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.NonConformity;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface NonconformityService {

    @Operation(logOperation = EAM_OPERATION.NONCONF_RD)
    NonConformity readNonconformityDefault(EAMContext context) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.NONCONF_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createNonconformity(EAMContext context, NonConformity nonconformityParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.NONCONF_R, logDataReference1 = LogDataReferenceType.INPUT)
    NonConformity readNonconformity(EAMContext context, String nonconformityCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.NONCONF_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateNonconformity(EAMContext context, NonConformity nonconformityParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.NONCONF_D)
    String deleteNonconformity(EAMContext context, String nonconformityCode) throws EAMException;

}
