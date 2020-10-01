package ch.cern.eam.wshub.core.services.workorders.entities;

import java.io.Serializable;

public class WorkOrderActivityChecklistSignatureResult implements Serializable {
    private String type;

    private String signer;

    private String responsibilityCode;

    private String responsibilityDescription;

    private Boolean viewAsPerformer;

    private Boolean viewAsReviewer;

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public String getResponsibilityCode() {
        return responsibilityCode;
    }

    public void setResponsibilityCode(String responsibilityCode) {
        this.responsibilityCode = responsibilityCode;
    }

    public String getResponsibilityDescription() {
        return responsibilityDescription;
    }

    public void setResponsibilityDescription(String responsibilityDescription) {
        this.responsibilityDescription = responsibilityDescription;
    }
}
