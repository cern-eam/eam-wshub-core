package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.TaskPlan;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface TaskPlanService {

    @Operation(logOperation = EAM_OPERATION.TASKPLAN_R)
    TaskPlan getTaskPlan(EAMContext context, TaskPlan taskPlan) throws EAMException;
}
