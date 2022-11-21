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
    @InforField(xpath = "RentalDetails/VEHICLETYPE/TYPECODE")
    private String vehicleType;
    @InforField(xpath = "RentalDetails/ISSUETO/PERSONCODE")
    private String issueTo;

    // Issue Details
    @InforField(xpath = "IssueDetails/ISSUEDDATE")
    private Date issuedDate;
    @InforField(xpath = "IssueDetails/ESTIMATEDISSUEDATE")
    private Date estimatedIssueDate;
    @InforField(xpath = "IssueDetails/ISSUELOCATION/LOCATIONCODE")
    private String issueLocation;

    // Return Details
    @InforField(xpath = "ReturnDetails/RETURNDATE")
    private Date returnDate;
    @InforField(xpath = "ReturnDetails/ESTIMATEDRETURNDATE")
    private Date estimatedReturnDate;

    @InforField(xpath = "ReturnDetails/RETURNLOCATION/LOCATIONCODE")
    private String returnLocation;

    @Transient
    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(final String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getRentalType() {
        return rentalType;
    }

    public void setRentalType(final String rentalType) {
        this.rentalType = rentalType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(final String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(final String locationCode) {
        this.locationCode = locationCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(final String classCode) {
        this.classCode = classCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(final Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Date getEstimatedIssueDate() {
        return estimatedIssueDate;
    }

    public void setEstimatedIssueDate(final Date estimatedIssueDate) {
        this.estimatedIssueDate = estimatedIssueDate;
    }

    public String getIssueLocation() {
        return issueLocation;
    }

    public void setIssueLocation(final String issueLocation) {
        this.issueLocation = issueLocation;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(final String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getIssueTo() {
        return issueTo;
    }

    public void setIssueTo(final String issueTo) {
        this.issueTo = issueTo;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(final Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getEstimatedReturnDate() {
        return estimatedReturnDate;
    }

    public void setEstimatedReturnDate(final Date estimatedReturnDate) {
        this.estimatedReturnDate = estimatedReturnDate;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(final String returnLocation) {
        this.returnLocation = returnLocation;
    }

    public UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(final UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }

    @Override
    public String toString() {
        return "EquipmentReservation{" +
                "code='" + code + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", description='" + description + '\'' +
                ", rentalType='" + rentalType + '\'' +
                ", status='" + status + '\'' +
                ", equipmentCode='" + equipmentCode + '\'' +
                ", locationCode='" + locationCode + '\'' +
                ", classCode='" + classCode + '\'' +
                ", reference='" + reference + '\'' +
                ", issuedDate=" + issuedDate +
                ", estimatedIssueDate=" + estimatedIssueDate +
                ", issueLocation='" + issueLocation + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", returnDate=" + returnDate +
                ", estimatedReturnDate=" + estimatedReturnDate +
                ", userDefinedFields=" + userDefinedFields +
                '}';
    }
}
