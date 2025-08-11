package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.NonconformityObservation;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface NonconformityObservationService {
    @Operation(logOperation = INFOR_OPERATION.NONCONF_OBS_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createNonconformityObservation(InforContext context, NonconformityObservation NonConformityObservation) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_OBS_R, logDataReference1 = LogDataReferenceType.INPUT)
    NonconformityObservation readNonconformityObservation(InforContext context, String number) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_OBS_R_ALL, logDataReference1 = LogDataReferenceType.INPUT)
    List<NonconformityObservation> readNonconformityObservationsByCode(
            InforContext context,
            String nonconformityCode,
            String organization
    ) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_OBS_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateNonconformityObservation(InforContext context, NonconformityObservation NonConformityObservation) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NONCONF_OBS_D)
    String deleteNonconformityObservation(InforContext context, String number) throws InforException;

}
