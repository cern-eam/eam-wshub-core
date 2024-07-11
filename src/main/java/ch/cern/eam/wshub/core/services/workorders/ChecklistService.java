package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface ChecklistService {

    @Operation(logOperation = EAM_OPERATION.WO_CHECKL_U)
    String updateWorkOrderChecklist(EAMContext context, WorkOrderActivityCheckList workOrderActivityCheckList) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.TP_CHECKLI_C)
    String createTaskplanChecklist(EAMContext context, TaskplanCheckList taskChecklist) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_CHECKL_R)
    WorkOrderActivityCheckList[] readWorkOrderChecklists(EAMContext context, Activity activity) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_CHECKL_FOLLOWUPWO_C)
    Long createFollowUpWorkOrders(EAMContext context, Activity activity) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_CHECKL_ESGN_U)
    WorkOrderActivityChecklistSignatureResponse eSignWorkOrderActivityChecklist(EAMContext context, WorkOrderActivityCheckListSignature workOrderActivityCheckListSignature)
            throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_CHECKL_ESGN_R)
    WorkOrderActivityChecklistSignatureResult[] getSignatures(EAMContext context, String workOrderCode, String activityCode, TaskPlan taskPlan)
            throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_CHECKLDEF_R)
    WorkOrderActivityCheckListDefinition getChecklistDefinition(EAMContext context, TaskPlan taskPlan, String code) throws EAMException;
}
