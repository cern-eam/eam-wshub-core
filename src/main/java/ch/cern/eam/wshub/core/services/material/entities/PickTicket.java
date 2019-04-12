package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;
import java.util.Date;

public class PickTicket implements Serializable {

    private String code;
    private String description;
    private String storeCode;
    private String status;
    private Date requestedEndDate;
    private String assetCode;
    private String eventCode;
    private String workorderCode;
    private Long activityNumber;

    // Origin is the username of the creator
    private String originCode;
    private String employeeCode;
    private String classCode;


    //Part list
    //Asset List

    public PickTicket() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequestedEndDate() {
        return requestedEndDate;
    }

    public void setRequestedEndDate(Date requestedEndDate) {
        this.requestedEndDate = requestedEndDate;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getWorkorderCode() {
        return workorderCode;
    }

    public void setWorkorderCode(String workorderCode) {
        this.workorderCode = workorderCode;
    }

    public Long getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(Long activityNumber) {
        this.activityNumber = activityNumber;
    }


    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}

