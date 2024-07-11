package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;
import net.datastream.schemas.mp_fields.USERDEFINEDCODEID_Type;
import org.openapplications.oagis_segments.DATETIME;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkOrderActivityCheckListDefaultResult implements Serializable {
    @EAMField(xpath = "PERFORMEDBYESIGN/ESIGNATURE/USERID/DESCRIPTION")
    private String performer1Name;

    @EAMField(xpath = "PERFORMEDBYESIGN2/ESIGNATURE/USERID/DESCRIPTION")
    private String performer2Name;

    @EAMField(xpath = "REVIEWEDBYESIGN/ESIGNATURE/USERID/DESCRIPTION")
    private String reviewerName;

    @EAMField(xpath = "PERFORMBYRESPONSIBILITY/USERDEFINEDCODE")
    private String performer1Qualification;

    @EAMField(xpath = "PERFORMBY2RESPONSIBILITY/USERDEFINEDCODE")
    private String performer2Qualification;

    @EAMField(xpath = "REVIEWRESPONSIBILITY/USERDEFINEDCODE")
    private String  reviewerQualification;

    @EAMField(xpath = "REJECTIONREASON")
    private String rejectionReason;

    @EAMField(xpath = "REJECTPERFORMEDBY")
    private String rejectPerformedBy;

    @EAMField(xpath = "REJECTPERFORMEDBY2")
    private String rejectPerformedBy2;

    @EAMField(xpath = "USERRESPONSIBILITY")
    List<UserQualification> userQualifications;

    @EAMField(xpath = "PERFORMEDBYESIGN/ESIGNATURE/EXTERNALDATETIME")
    Date timePerf1;

    @EAMField(xpath = "PERFORMEDBYESIGN2/ESIGNATURE/EXTERNALDATETIME")
    Date timePerf2;

    @EAMField(xpath = "REVIEWEDBYESIGN/ESIGNATURE/EXTERNALDATETIME")
    Date timeRev1;

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

    public Date getTimePerf1() {
        return timePerf1;
    }

    public void setTimePerf1(Date timePerf1) {
        this.timePerf1 = timePerf1;
    }

    public Date getTimePerf2() {
        return timePerf2;
    }

    public void setTimePerf2(Date timePerf2) {
        this.timePerf2 = timePerf2;
    }

    public Date getTimeRev1() {
        return timeRev1;
    }

    public void setTimeRev1(Date timeRev1) {
        this.timeRev1 = timeRev1;
    }

    public List<UserQualification> getUserQualifications() {
        return userQualifications;
    }

    public void setUserQualifications(List<UserQualification> userQualifications) {
        this.userQualifications = userQualifications;
    }

}
