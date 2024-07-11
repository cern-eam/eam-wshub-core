package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.workorders.TaskPlanService;
import ch.cern.eam.wshub.core.services.workorders.entities.TaskPlan;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_functions.mp0079_001.MP0079_GetTask_001;
import net.datastream.schemas.mp_results.mp0079_001.MP0079_GetTask_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.math.BigInteger;

public class TaskPlanServiceImpl implements TaskPlanService {
    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public TaskPlanServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient){
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }
    @Override
    public TaskPlan getTaskPlan(EAMContext context, TaskPlan taskPlan) throws EAMException {

        MP0079_GetTask_001 getTaskPlan = new MP0079_GetTask_001();
        if(taskPlan.getTaskRevision() == null) {
            taskPlan.setTaskRevision(new BigInteger("0"));
        }
        tools.getEAMFieldTools().transformWSHubObject(getTaskPlan, taskPlan, context);
        MP0079_GetTask_001_Result getTask_001_result =
                tools.performEAMOperation(context, eamws::getTaskOp, getTaskPlan);

        TaskPlan res = new TaskPlan();
        tools.getEAMFieldTools().transformEAMObject(res, getTask_001_result.getResultData().getTask(), context);
        return res;
    }
}
