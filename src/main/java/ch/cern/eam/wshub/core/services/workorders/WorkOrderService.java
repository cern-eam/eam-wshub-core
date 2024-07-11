package ch.cern.eam.wshub.core.services.workorders;

import java.util.List;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface WorkOrderService {

    @Operation(logOperation = EAM_OPERATION.WORKORDER_BC, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> createWorkOrderBatch(EAMContext context, List<WorkOrder> workOrderParam)
            throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WORKORDER_BR, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<WorkOrder> readWorkOrderBatch(EAMContext context, List<String> numbers);

    @Operation(logOperation = EAM_OPERATION.WORKORDER_BU, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> updateWorkOrderBatch(EAMContext context, List<WorkOrder> workOrderParam)
                    throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WORKORDER_BD, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> deleteWorkOrderBatch(EAMContext context, List<String> workOrderParam)
            throws EAMException;


    //
    // WORK ORDER CRUD
    //
    @Operation(logOperation = EAM_OPERATION.WORKORDER_READ, logDataReference1 = LogDataReferenceType.INPUT)
    WorkOrder readWorkOrder(EAMContext context, String number) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WORKORDER_READ_DEFAULT)
    WorkOrder readWorkOrderDefault(EAMContext context, String number) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String createWorkOrder(EAMContext context, WorkOrder workorder) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WORKORDER_UPDATE, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "number")
    String updateWorkOrder(EAMContext context, WorkOrder workorder) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WORKORDER_DELETE)
    String deleteWorkOrder(EAMContext context, String workOrderNumber) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_STATUS_UPDATE)
    String updateWorkOrderStatus(EAMContext context, String workOrderNumber, String statusCode) throws EAMException;
}
