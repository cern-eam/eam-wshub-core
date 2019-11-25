package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.InforField;

import java.math.BigInteger;

public class ReleasedPMSchedule
{
    @InforField(xpath="WORKORDERID/JOBNUM")
    private String workOrder;
    @InforField(xpath="STATUS/STATUSCODE")
    private String statusCode;
    @InforField(xpath="TARGETDATE")
    private String targetDate;
    @InforField(xpath="recordid")
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

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
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
                "statusCode='" + statusCode + '\'' +
                ", targetDate='" + targetDate + '\'' +
                ", updateCount=" + updateCount +
                ", workOrder='" + workOrder + '\'' +
                '}';
    }
}
