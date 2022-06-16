package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.Transient;
import java.util.Date;

public class EquipmentReservation {
    // Main identifying fields
    @InforField(xpath = "CUSTOMERRENTALID/CUSTOMERRENTALCODE")
    private String code;
    @InforField(xpath = "CUSTOMERRENTALID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organizationCode;
    @InforField(xpath = "CUSTOMERRENTALID/DESCRIPTION")
    private String description;

    // Rental Details
    @InforField(xpath = "RentalDetails/RENTALTYPE/STATUSCODE")
    private String rentalType;
    @InforField(xpath = "RentalDetails/STATUS/STATUSCODE")
    private String status;
    @InforField(xpath = "RentalDetails/EQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;
    @InforField(xpath = "RentalDetails/LOCATIONID/LOCATIONCODE")
    private String locationCode;
    @InforField(xpath = "RentalDetails/CLASSID/CLASSCODE")
    private String classCode;
    @InforField(xpath = "RentalDetails/REFERENCE")
    private String reference;

    // Issue Details
    @InforField(xpath = "IssueDetails/ISSUEDDATE")
    private Date issuedDate;
    @InforField(xpath = "IssueDetails/ESTIMATEDISSUEDATE")
    private Date estimatedIssueDate;

    // Return Details
    @InforField(xpath = "ReturnDetails/RETURNDATE")
    private Date returnDate;
    @InforField(xpath = "ReturnDetails/ESTIMATEDRETURNDATE")
    private Date estimatedReturnDate;

    @Transient
    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    public Date getEstimatedReturnDate() { return estimatedReturnDate; }

    public void setEstimatedReturnDate(Date estimatedReturnDate) { this.estimatedReturnDate = estimatedReturnDate; }

    public String getReference() { return reference; }

    public void setReference(String reference) { this.reference = reference; }

    public UserDefinedFields getUserDefinedFields() { return userDefinedFields; }

    public void setUserDefinedFields(UserDefinedFields userDefinedFields) { this.userDefinedFields = userDefinedFields; }

    public String getClassCode() { return classCode; }

    public void setClassCode(String classCode) { this.classCode = classCode; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getOrganizationCode() { return organizationCode; }

    public void setOrganizationCode(String organizationCode) { this.organizationCode = organizationCode; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getRentalType() { return rentalType; }

    public void setRentalType(String rentalType) { this.rentalType = rentalType; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getEquipmentCode() { return equipmentCode; }

    public void setEquipmentCode(String equipmentCode) { this.equipmentCode = equipmentCode; }

    public String getLocationCode() { return locationCode; }

    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    public Date getIssuedDate() { return issuedDate; }

    public void setIssuedDate(Date issuedDate) { this.issuedDate = issuedDate; }

    public Date getEstimatedIssueDate() { return estimatedIssueDate; }

    public void setEstimatedIssueDate(Date estimatedIssueDate) { this.estimatedIssueDate = estimatedIssueDate; }

    public Date getReturnDate() { return returnDate; }

    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    @Override
    public String toString() {
        return "EquipmentReservation ["
                + (code != null ? "code=" + code + ", " : "")
                + (organizationCode != null ? "organizationCode=" + organizationCode + ", " : "")
                + (description != null ? "description=" + description + ", " : "")
                + (rentalType != null ? "rentalType=" + rentalType + ", " : "")
                + (status != null ? "status=" + status + ", " : "")
                + (equipmentCode != null ? "equipmentCode=" + equipmentCode + ", " : "")
                + (locationCode != null ? "locationCode=" + locationCode + ", " : "")
                + (classCode != null ? "classCode=" + classCode + ", " : "")
                + (reference != null ? "reference=" + reference + ", " : "")
                + (issuedDate != null ? "issuedDate=" + issuedDate + ", " : "")
                + (estimatedIssueDate != null ? "estimatedIssueDate=" + estimatedIssueDate + ", " : "")
                + (returnDate != null ? "returnDate=" + returnDate + ", " : "")
                + (estimatedReturnDate != null ? "estimatedReturnDate=" + estimatedReturnDate + ", " : "")
                + (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields + ", " : "")
                + "]";
    }
}
