package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import net.datastream.schemas.mp_fields.ACTIVITYCODE;
import net.datastream.schemas.mp_fields.NOUN_Type;
import net.datastream.schemas.mp_fields.VERB_Type;

import java.io.Serializable;
import java.math.BigInteger;

public class WorkOrderActivityCheckListSignature implements Serializable {

    @InforField(xpath = "ACTIVITYID/WORKORDERID/JOBNUM")
    private String workOrderCode;

    @InforField(xpath = "ACTIVITYID/ACTIVITYCODE/value")
    private BigInteger activityCodeValue;

    @InforField(xpath = "ACTIVITYID/ACTIVITYCODE/auto_generated")
    private String activityCodeAutoGenerated;

    @InforField(xpath = "ACTIVITYID/ACTIVITYNOTE")
    private String note;

    @InforField(xpath = "JOBSEQUENCE")
    private BigInteger jobSequence;

    @InforField(xpath = "SEQUENCENUMBER")
    private BigInteger sequenceNumber;

    String userCode;

    String password;

    String signatureType;

    public WorkOrderActivityCheckListSignature() {
        activityCodeAutoGenerated = "false";
    }

    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
    }

    public void setActivityCodeValue(String activityCodeValue) {
        this.activityCodeValue = new BigInteger(activityCodeValue);
    }

    public void setActivityCodeAutoGenerated(String activityCodeAutoGenerated) {
        this.activityCodeAutoGenerated = activityCodeAutoGenerated;
    }

    public String getNote() { return note; }

    public void setNote(String note) {
        this.note = note;
    }

    public BigInteger getJobSequence() {
        return jobSequence;
    }

    public void setSequence(BigInteger jobSequence) {
        this.jobSequence = jobSequence;
    }

    public BigInteger getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(BigInteger sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public BigInteger getActivityCodeValue() {
        return activityCodeValue;
    }
}
