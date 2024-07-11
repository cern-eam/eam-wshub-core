package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.impl.Safety;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;
import java.util.Map;


public interface SafetyService {
    @Operation(logOperation = EAM_OPERATION.SAFETY_BHLR)
    BatchResponse<List<Safety>> readSafetiesBatch(EAMContext context, String entityType, List<String> entityCode);

    @Operation(logOperation = EAM_OPERATION.SAFETY_BHLU)
    BatchResponse<String> setSafetiesBatch(EAMContext context, String entityType, Map<String, List<Safety>> entityCodeToSafeties);

    @Operation(logOperation = EAM_OPERATION.SAFETY_HLR)
    List<Safety> readSafeties(EAMContext context, String entityType, String entityCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.SAFETY_HLU)
    String setSafeties(EAMContext context, String entityType, String entityCode, List<Safety> safeties) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.SAFETY_R)
    Safety readSafety(EAMContext context, String entityType, String safetyCode) throws EAMException;
}
