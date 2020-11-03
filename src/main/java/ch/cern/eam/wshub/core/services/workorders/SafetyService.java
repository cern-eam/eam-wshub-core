package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.impl.Safety;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;


public interface SafetyService {
    @Operation(logOperation = INFOR_OPERATION.SAFETY_HLR)
    List<Safety> readSafeties(InforContext context, String entityType, String entityCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_HLU)
    void setSafeties(InforContext context, String entityType, String entityCode, List<Safety> safeties) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_R)
    Safety readSafety(InforContext context, String entityType, String safetyCode) throws InforException;
}
