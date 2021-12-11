package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderScheduleService;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderSchedule;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_functions.mp0057_001.MP0057_AddWorkOrderSchedule_001;
import net.datastream.schemas.mp_results.mp0057_001.MP0057_AddWorkOrderSchedule_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class WorkOrderScheduleServiceImpl implements WorkOrderScheduleService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public WorkOrderScheduleServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    public String createWorkOrderSchedule(InforContext inforContext, WorkOrderSchedule workOrderSchedule) throws InforException {
        net.datastream.schemas.mp_entities.workorderschedule_001.WorkOrderSchedule workOrderScheduleInfor = new net.datastream.schemas.mp_entities.workorderschedule_001.WorkOrderSchedule();

        tools.getInforFieldTools().transformWSHubObject(workOrderScheduleInfor, workOrderSchedule, inforContext);
        MP0057_AddWorkOrderSchedule_001 addWorkOrderSchedule = new MP0057_AddWorkOrderSchedule_001();
        addWorkOrderSchedule.setWorkOrderSchedule(workOrderScheduleInfor);

        MP0057_AddWorkOrderSchedule_001_Result result = tools.performInforOperation(inforContext, inforws::addWorkOrderScheduleOp, addWorkOrderSchedule);
        return result.getResultData().getWOSCHEDULEID().getWOSCHEDULECODE();
    }

}
