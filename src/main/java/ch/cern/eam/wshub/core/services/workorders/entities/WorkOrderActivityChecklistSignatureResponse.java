package ch.cern.eam.wshub.core.services.workorders.entities;

import java.io.Serializable;

public class WorkOrderActivityChecklistSignatureResponse implements Serializable {
    private String signer;

    private String timeStamp;

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
