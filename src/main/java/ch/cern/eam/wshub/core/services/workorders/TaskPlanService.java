package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.entities.TaskPlan;
import ch.cern.eam.wshub.core.tools.InforException;

public interface TaskPlanService {

    TaskPlan getTaskPlan(InforContext context, TaskPlan taskPlan) throws InforException;
}
