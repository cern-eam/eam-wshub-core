package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.NonConformityObservation;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface NonConformityObservationService {
    @Operation(logOperation = EAM_OPERATION.NONCONF_OBS_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createNonConformityObservation(EAMContext context, NonConformityObservation NonConformityObservation) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.NONCONF_OBS_R, logDataReference1 = LogDataReferenceType.INPUT)
    NonConformityObservation readNonConformityObservation(EAMContext context, String number) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.NONCONF_OBS_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateNonConformityObservation(EAMContext context, NonConformityObservation NonConformityObservation) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.NONCONF_OBS_D)
    String deleteNonConformityObservation(EAMContext context, String number) throws EAMException;

}
