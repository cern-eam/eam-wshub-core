package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.TaskPlan;
import ch.cern.eam.wshub.core.tools.InforException;

public interface TaskPlanService {

    @Operation(logOperation = INFOR_OPERATION.TASKPLAN_R)
    TaskPlan getTaskPlan(InforContext context, TaskPlan taskPlan) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.TASKPLAN_R)
    TaskPlan getTaskPlan(InforContext context, String taskCode) throws InforException;
}
