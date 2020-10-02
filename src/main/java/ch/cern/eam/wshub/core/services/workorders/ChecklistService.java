package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.Signature;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.tools.InforException;

public interface ChecklistService {

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_U)
    String updateWorkOrderChecklist(InforContext context, WorkOrderActivityCheckList workOrderActivityCheckList) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.TP_CHECKLI_C)
    String createTaskplanChecklist(InforContext context, TaskplanCheckList taskChecklist) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_R)
    WorkOrderActivityCheckList[] readWorkOrderChecklists(InforContext context, Activity activity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_FOLLOWUPWO_C)
    Long createFollowUpWorkOrders(InforContext context, Activity activity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_ESGN_U)
    WorkOrderActivityChecklistSignatureResponse eSignWorkOrderActivityChecklist(InforContext context, WorkOrderActivityCheckListSignature workOrderActivityCheckListSignature)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_CHECKL_ESGN_R)
    WorkOrderActivityChecklistSignatureResult[] getSignatures(InforContext context, String workOrderCode, String activityCode)
            throws  InforException;
}
