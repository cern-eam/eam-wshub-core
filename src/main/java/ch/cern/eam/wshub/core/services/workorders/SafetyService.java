package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.services.workorders.impl.Safety;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;
import java.util.Map;


public interface SafetyService {
    @Operation(logOperation = INFOR_OPERATION.SAFETY_BHLR)
    BatchResponse<List<Safety>> readSafetiesBatch(InforContext context, String entityType, List<String> entityCode);

    @Operation(logOperation = INFOR_OPERATION.SAFETY_BHLU)
    BatchResponse<String> setSafetiesBatch(InforContext context, String entityType, Map<String, List<Safety>> entityCodeToSafeties);

    @Operation(logOperation = INFOR_OPERATION.SAFETY_HLR)
    List<Safety> readSafeties(InforContext context, String entityType, String entityCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_HLU)
    String setSafeties(InforContext context, String entityType, String entityCode, List<Safety> safeties) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_R)
    Safety readSafety(InforContext context, String entityType, String safetyCode) throws InforException;
}
