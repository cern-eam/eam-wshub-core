package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.CustomField;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class PickTicket implements Serializable {

    @EAMField(xpath = "PICKLISTID/PICKLIST")
    private String code;
    @EAMField(xpath = "PICKLISTID/PICKLISTDESC")
    private String description;
    @EAMField(xpath = "STOREID/STORECODE")
    private String storeCode;
    @EAMField(xpath = "STATUS/STATUSCODE")
    private String status;
    @EAMField(xpath = "DATEREQUIRED")
    private Date requestedEndDate;
    @EAMField(xpath = "CLASSID/CLASSCODE")
    private String classCode;
    @EAMField(xpath = "WORKORDERACTIVITY/ACTIVITYID/WORKORDERID")
    private String workorderCode;
    @EAMField(xpath = "WORKORDERACTIVITY/ACTIVITYID/ACTIVITYCODE")
    private Long activityNumber;
    @EAMField(xpath = "JOBSEQUENCE")
    private Long jobSequence;
    @EAMField(xpath = "ASSETID/EQUIPMENTCODE")
    private String assetCode;
    @EAMField(xpath = "ASSETID/EQUIPMENTCODE")
    private String eventCode;
    @EAMField(xpath = "DELIVERYADDRESSID/DELADDRESSCODE")
    private String deliveryAddressId;
    @EAMField(xpath = "DELIVERYSUPPLIERID/SUPPLIERCODE")
    private String deliverySupplierId;
    @EAMField(xpath = "DELIVERPERSONNEL/PERSONCODE")
    private String deliverPersonnelCode;
    @EAMField(xpath = "ORIGINID/USERCODE")
    private String originCode;
    @EAMField(xpath = "DEFAULTAPPROVER/USERCODE")
    private String defaultApproverCode;
    @EAMField(xpath = "APPROVER/USERCODE")
    private String approverCode;
    @EAMField(xpath = "APPROVEDATE")
    private Date approvedate;
    @EAMField(xpath = "PRINTDATE")
    private Date printDate;
    @EAMField(xpath = "USERDEFINEDAREA")
    private CustomField[] customFields;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(final String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Date getRequestedEndDate() {
        return requestedEndDate;
    }

    public void setRequestedEndDate(final Date requestedEndDate) {
        this.requestedEndDate = requestedEndDate;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(final String classCode) {
        this.classCode = classCode;
    }

    public String getWorkorderCode() {
        return workorderCode;
    }

    public void setWorkorderCode(final String workorderCode) {
        this.workorderCode = workorderCode;
    }

    public Long getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(final Long activityNumber) {
        this.activityNumber = activityNumber;
    }

    public Long getJobSequence() {
        return jobSequence;
    }

    public void setJobSequence(final Long jobSequence) {
        this.jobSequence = jobSequence;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(final String assetCode) {
        this.assetCode = assetCode;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(final String eventCode) {
        this.eventCode = eventCode;
    }

    public String getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(final String deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public String getDeliverySupplierId() {
        return deliverySupplierId;
    }

    public void setDeliverySupplierId(final String deliverySupplierId) {
        this.deliverySupplierId = deliverySupplierId;
    }

    public String getDeliverPersonnelCode() {
        return deliverPersonnelCode;
    }

    public void setDeliverPersonnelCode(final String deliverPersonnelCode) {
        this.deliverPersonnelCode = deliverPersonnelCode;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(final String originCode) {
        this.originCode = originCode;
    }

    public String getDefaultApproverCode() {
        return defaultApproverCode;
    }

    public void setDefaultApproverCode(final String defaultApproverCode) {
        this.defaultApproverCode = defaultApproverCode;
    }

    public String getApproverCode() {
        return approverCode;
    }

    public void setApproverCode(final String approverCode) {
        this.approverCode = approverCode;
    }

    public Date getApprovedate() {
        return approvedate;
    }

    public void setApprovedate(final Date approvedate) {
        this.approvedate = approvedate;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(final Date printDate) {
        this.printDate = printDate;
    }

    public CustomField[] getCustomFields() {
        return customFields;
    }

    public void setCustomFields(final CustomField[] customFields) {
        this.customFields = customFields;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PickTicket that = (PickTicket) o;
        return Objects.equals(code, that.code) && Objects.equals(description, that.description) && Objects.equals(storeCode, that.storeCode) && Objects.equals(status, that.status) && Objects.equals(requestedEndDate, that.requestedEndDate) && Objects.equals(classCode, that.classCode) && Objects.equals(workorderCode, that.workorderCode) && Objects.equals(activityNumber, that.activityNumber) && Objects.equals(jobSequence, that.jobSequence) && Objects.equals(assetCode, that.assetCode) && Objects.equals(eventCode, that.eventCode) && Objects.equals(deliveryAddressId, that.deliveryAddressId) && Objects.equals(deliverySupplierId, that.deliverySupplierId) && Objects.equals(deliverPersonnelCode, that.deliverPersonnelCode) && Objects.equals(originCode, that.originCode) && Objects.equals(defaultApproverCode, that.defaultApproverCode) && Objects.equals(approverCode, that.approverCode) && Objects.equals(approvedate, that.approvedate) && Objects.equals(printDate, that.printDate) && Arrays.equals(customFields, that.customFields);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(code, description, storeCode, status, requestedEndDate, classCode, workorderCode, activityNumber, jobSequence, assetCode, eventCode, deliveryAddressId, deliverySupplierId, deliverPersonnelCode, originCode, defaultApproverCode, approverCode, approvedate, printDate);
        result = 31 * result + Arrays.hashCode(customFields);
        return result;
    }

    @Override
    public String toString() {
        return "PickTicket{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", storeCode='" + storeCode + '\'' +
                ", status='" + status + '\'' +
                ", requestedEndDate=" + requestedEndDate +
                ", classCode='" + classCode + '\'' +
                ", workorderCode='" + workorderCode + '\'' +
                ", activityNumber=" + activityNumber +
                ", jobSequence=" + jobSequence +
                ", assetCode='" + assetCode + '\'' +
                ", eventCode='" + eventCode + '\'' +
                ", deliveryAddressId='" + deliveryAddressId + '\'' +
                ", deliverySupplierId='" + deliverySupplierId + '\'' +
                ", deliverPersonnelCode='" + deliverPersonnelCode + '\'' +
                ", originCode='" + originCode + '\'' +
                ", defaultApproverCode='" + defaultApproverCode + '\'' +
                ", approverCode='" + approverCode + '\'' +
                ", approvedate=" + approvedate +
                ", printDate=" + printDate +
                ", customFields=" + Arrays.toString(customFields) +
                '}';
    }
}

