package ch.cern.eam.wshub.core.services.workorders.entities;

import java.io.Serializable;

public class WorkOrderActivityChecklistSignatureResult implements Serializable {
    private String type;

    private String signer;

    private String responsibility;

    private Boolean viewAsPerformer;

    private Boolean viewAsReviewer;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public Boolean getViewAsPerformer() {
        return viewAsPerformer;
    }

    public void setViewAsPerformer(Boolean viewAsPerformer) {
        this.viewAsPerformer = viewAsPerformer;
    }

    public Boolean getViewAsReviewer() {
        return viewAsReviewer;
    }

    public void setViewAsReviewer(Boolean viewAsReviewer) {
        this.viewAsReviewer = viewAsReviewer;
    }
}
