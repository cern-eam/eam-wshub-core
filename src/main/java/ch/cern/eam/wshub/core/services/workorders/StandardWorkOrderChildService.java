package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrder;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrderChild;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface StandardWorkOrderChildService {

    @Operation(logOperation = INFOR_OPERATION.STANDARDWOCHILD_BC, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<StandardWorkOrderChild> createStandardWorkOrderChildBatch(InforContext context, List<StandardWorkOrderChild> standardWorkOrderChildren) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWOCHILD_BR, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<StandardWorkOrderChild> readStandardWorkOrderChildBatch(InforContext context, List<String> standardWOCodes) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWOCHILD_BU, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<StandardWorkOrderChild> updateStandardWorkOrderChildBatch(InforContext context, List<StandardWorkOrderChild> standardWorkOrderChildren) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWOCHILD_BD, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<StandardWorkOrderChild> deleteStandardWorkOrderChildBatch(InforContext context, List<String> standardWOCodes) throws InforException;


    @Operation(logOperation = INFOR_OPERATION.STANDARDWOCHILD_READ_DEFAULT)
    StandardWorkOrder readStandardWorkOrderChildDefault(InforContext context, String standardWOCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWOCHILD_CREATE)
    StandardWorkOrderChild createStandardWorkOrderChild(InforContext context, StandardWorkOrderChild standardWorkOrderChild) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWOCHILD_READ)
    StandardWorkOrderChild readStandardWorkOrderChild(InforContext context, StandardWorkOrderChild standardWorkOrderChild) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWOCHILD_UPDATE)
    StandardWorkOrderChild updateStandardWorkOrderChild(InforContext context, StandardWorkOrderChild standardWorkOrderChild) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWOCHILD_DELETE)
    StandardWorkOrderChild deleteStandardWorkOrderChild(InforContext context, StandardWorkOrderChild standardWorkOrderChild) throws InforException;
}
