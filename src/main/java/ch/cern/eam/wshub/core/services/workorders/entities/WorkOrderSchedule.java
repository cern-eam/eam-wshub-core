package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.InforField;

public class WorkOrderSchedule {

    @InforField(xpath = "WOSCHEDULEID/WOSCHEDULECODE")
    private String scheduleCode;

    // TODO: add all other properties for Work Order Schedule and decorate them with InforField annotation with valid XPath

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }
}
