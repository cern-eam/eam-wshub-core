package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import net.datastream.schemas.mp_fields.USERDEFINEDCODEID_Type;

import java.io.Serializable;
import java.util.List;

public class WorkOrderActivityCheckListDefaultResult implements Serializable {
    @InforField(xpath = "PERFORMEDBYESIGN/ESIGNATURE/USERID/DESCRIPTION")
    private String performer1Name;

    @InforField(xpath = "PERFORMEDBYESIGN2/ESIGNATURE/USERID/DESCRIPTION")
    private String performer2Name;

    @InforField(xpath = "REVIEWEDBYESIGN/ESIGNATURE/USERID/DESCRIPTION")
    private String reviewerName;

    @InforField(xpath = "PERFORMBYRESPONSIBILITY/USERDEFINEDCODE")
    private String performer1Qualification;

    @InforField(xpath = "PERFORMBY2RESPONSIBILITY/USERDEFINEDCODE")
    private String performer2Qualification;

    @InforField(xpath = "REVIEWRESPONSIBILITY/USERDEFINEDCODE")
    private String  reviewerQualification;

    @InforField(xpath = "REJECTIONREASON")
    private String rejectionReason;

    @InforField(xpath = "REJECTPERFORMEDBY")
    private String rejectPerformedBy;

    @InforField(xpath = "REJECTPERFORMEDBY2")
    private String rejectPerformedBy2;

    @InforField(xpath = "USERRESPONSIBILITY")
    List<USERDEFINEDCODEID_Type> userResponsibilities;

    public WorkOrderActivityCheckListDefaultResult(){
        rejectPerformedBy = "false";
        rejectPerformedBy2 = "false";
    }

    public String getPerformer1Name() {
        return performer1Name;
    }

    public void setPerformer1Name(String performer1Name) {
        this.performer1Name = performer1Name;
    }

    public String getPerformer2Name() {
        return performer2Name;
    }

    public void setPerformer2Name(String performer2Name) {
        this.performer2Name = performer2Name;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getPerformer1Qualification() {
        return performer1Qualification;
    }

    public void setPerformer1Qualification(String performer1Qualification) {
        this.performer1Qualification = performer1Qualification;
    }

    public String getPerformer2Qualification() {
        return performer2Qualification;
    }

    public void setPerformer2Qualification(String performer2Qualification) {
        this.performer2Qualification = performer2Qualification;
    }

    public String getReviewerQualification() {
        return reviewerQualification;
    }

    public void setReviewerQualification(String reviewerQualification) {
        this.reviewerQualification = reviewerQualification;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRejectPerformedBy() {
        return rejectPerformedBy;
    }

    public void setRejectPerformedBy(String rejectPerformedBy) {
        this.rejectPerformedBy = rejectPerformedBy;
    }

    public String getRejectPerformedBy2() {
        return rejectPerformedBy2;
    }

    public void setRejectPerformedBy2(String rejectPerformedBy2) {
        this.rejectPerformedBy2 = rejectPerformedBy2;
    }

    public List<USERDEFINEDCODEID_Type> getUserResponsibilities() {
        return userResponsibilities;
    }

    public void setUserResponsibilities(List<USERDEFINEDCODEID_Type> userResponsibilities) {
        this.userResponsibilities = userResponsibilities;
    }
}
