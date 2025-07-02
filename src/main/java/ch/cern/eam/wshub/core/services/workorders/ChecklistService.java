package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.tools.InforException;

public interface ChecklistService {

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_U)
    String updateWorkOrderActivityCheckList(
            InforContext context,
            WorkOrderActivityChecklist workOrderActivityChecklist,
            boolean shouldMergeExistingValues
    ) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_ITEM_U)
    String updateWorkOrderChecklistItem(
            InforContext context,
            WorkOrderActivityChecklistItem workOrderActivityChecklistItem,
            TaskPlan taskPlan
    ) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.TP_CHECKLI_C)
    String createTaskplanChecklist(InforContext context, TaskplanCheckList taskChecklist) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_ITEM_R)
    WorkOrderActivityChecklistItem[] readWorkOrderChecklistItems(InforContext context, Activity activity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_FOLLOWUPWO_C)
    Long createFollowUpWorkOrders(InforContext context, Activity activity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_ESGN_U)
    WorkOrderActivityChecklistSignatureResponse eSignWorkOrderActivityChecklist(InforContext context, WorkOrderActivityChecklistSignature workOrderActivityCheckListSignature)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_ESGN_R)
    WorkOrderActivityChecklistSignatureResult[] getSignatures(InforContext context, String workOrderCode, String activityCode, TaskPlan taskPlan)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKLDEF_R)
    WorkOrderActivityChecklistDefinition getChecklistDefinition(InforContext context, TaskPlan taskPlan, String code) throws InforException;
}
