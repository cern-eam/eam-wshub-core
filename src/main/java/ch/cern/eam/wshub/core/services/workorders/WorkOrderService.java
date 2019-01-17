package ch.cern.eam.wshub.core.services.workorders;

import java.util.List;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;

public interface WorkOrderService {

    @Operation(logOperation = INFOR_OPERATION.WORKORDERS_BC, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> createWorkOrderBatch(InforContext context, List<WorkOrder> workOrderParam)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_BR, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<WorkOrder> readWorkOrderBatch(InforContext context, List<String> numbers);

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_BU, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> updateWorkOrderBatch(InforContext context, List<WorkOrder> workOrderParam)
                    throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_BD, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> deleteWorkOrderBatch(InforContext context, List<String> workOrderParam)
            throws InforException;


    //
    // WORK ORDER CRUD
    //
    @Operation(logOperation = INFOR_OPERATION.WORKORDER_READ, logDataReference1 = LogDataReferenceType.INPUT)
    WorkOrder readWorkOrder(InforContext context, String number) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWO_READ)
    WorkOrder readStandardWorkOrder(InforContext context, WorkOrder workorderParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String createWorkOrder(InforContext context, WorkOrder workorderParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "number")
    String updateWorkOrder(InforContext context, WorkOrder workorderParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_D)
    String deleteWorkOrder(InforContext context, String workOrderNumber) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_STATUS_U)
    String changeWOStatus(InforContext context, String woNumber, String statusCode) throws InforException;
}
