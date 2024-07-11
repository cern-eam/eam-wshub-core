package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;
import java.math.BigInteger;
import java.util.Date;

public class ReleasedPMSchedule {

    @EAMField(xpath = "WORKORDERID/JOBNUM")
    private String workOrder;
    @EAMField(xpath = "STATUS/STATUSCODE")
    private String statusCode;
    @EAMField(xpath = "TARGETDATE")
    private Date scheduledStart;
    @EAMField(xpath = "recordid")
    private BigInteger updateCount;

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getScheduledStart() {
        return scheduledStart;
    }

    public void setScheduledStart(Date scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public BigInteger getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(BigInteger updateCount) {
        this.updateCount = updateCount;
    }

    @Override
    public String toString() {
        return "ReleasedPMSchedule{" +
            "workOrder='" + workOrder + '\'' +
            ", statusCode='" + statusCode + '\'' +
            ", scheduledStart=" + scheduledStart +
            ", updateCount=" + updateCount +
            '}';
    }
}
