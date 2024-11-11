package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.NonConformityObservation;
import ch.cern.eam.wshub.core.tools.InforException;

public interface NonConformityObservationService {
    @Operation(logOperation = INFOR_OPERATION.NONCONF_OBS_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createNonConformityObservation(InforContext context, NonConformityObservation NonConformityObservation) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_OBS_R, logDataReference1 = LogDataReferenceType.INPUT)
    NonConformityObservation readNonConformityObservation(InforContext context, String number) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_OBS_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateNonConformityObservation(InforContext context, NonConformityObservation NonConformityObservation) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_OBS_D)
    String deleteNonConformityObservation(InforContext context, String number) throws InforException;

}
