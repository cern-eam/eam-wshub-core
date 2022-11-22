package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.Transient;
import java.math.BigDecimal;
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
    @InforField(xpath = "RentalDetails/COSTCODE")
    private String costCode;
    @InforField(xpath = "RentalDetails/RENTALTEMPLATEID/RENTALTEMPLATECODE")
    private String rentalTemplate;
    @InforField(xpath = "RentalDetails/CUSTOMERID/CUSTOMERCODE")
    private String customer;

    @InforField(xpath = "RentalDetails/COMPLETEDDATE")
    private Date completedDate;
    @InforField(xpath = "RentalDetails/CREATEDBY/USERCODE")
    private String createdBy;
    @InforField(xpath = "RentalDetails/CREATEDDATE")
    private Date createdDate;

    // Issue Details
    @InforField(xpath = "IssueDetails/ESTIMATEDISSUEDATE")
    private Date estimatedIssueDate;
    @InforField(xpath = "IssueDetails/ISSUEDDATE")
    private Date issuedDate;
    @InforField(xpath = "IssueDetails/ISSUELOCATION/LOCATIONCODE")
    private String issueLocation;
    @InforField(xpath = "IssueDetails/ISSUEDBY/USERCODE")
    private String issuedBy;

    // Return Details
    @InforField(xpath = "ReturnDetails/RETURNDATE")
    private Date returnDate;
    @InforField(xpath = "ReturnDetails/ESTIMATEDRETURNDATE")
    private Date estimatedReturnDate;
    @InforField(xpath = "ReturnDetails/RETURNLOCATION/LOCATIONCODE")
    private String returnLocation;

    // Invoicing Details
    @InforField(xpath = "InvoicingDetails/INVOICEDAMOUNT")
    private BigDecimal invoicedAmount;
    @InforField(xpath = "InvoicingDetails/CALCULATEDDAYS")
    private BigDecimal calculatedDays;
    @InforField(xpath = "InvoicingDetails/CALCULATEDHOURS")
    private BigDecimal calculatedHours;
    @InforField(xpath = "InvoicingDetails/CORRECTEDDAYS")
    private BigDecimal correctedDays;
    @InforField(xpath = "InvoicingDetails/CORRECTEDHOURS")
    private BigDecimal correctedHours;
    @InforField(xpath = "InvoicingDetails/ADJUSTMENTS")
    private BigDecimal adjustments;
    @InforField(xpath = "InvoicingDetails/NETAMOUNT")
    private BigDecimal netAmount;
    @InforField(xpath = "InvoicingDetails/GROSSAMOUNT")
    private BigDecimal grossAmount;
    @InforField(xpath = "InvoicingDetails/TAXAMOUNT")
    private BigDecimal taxAmount;

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

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(final String costCode) {
        this.costCode = costCode;
    }

    public String getRentalTemplate() {
        return rentalTemplate;
    }

    public void setRentalTemplate(final String rentalTemplate) {
        this.rentalTemplate = rentalTemplate;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(final String customer) {
        this.customer = customer;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(final Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEstimatedIssueDate() {
        return estimatedIssueDate;
    }

    public void setEstimatedIssueDate(final Date estimatedIssueDate) {
        this.estimatedIssueDate = estimatedIssueDate;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(final Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getIssueLocation() {
        return issueLocation;
    }

    public void setIssueLocation(final String issueLocation) {
        this.issueLocation = issueLocation;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(final String issuedBy) {
        this.issuedBy = issuedBy;
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

    public BigDecimal getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(final BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getCalculatedDays() {
        return calculatedDays;
    }

    public void setCalculatedDays(final BigDecimal calculatedDays) {
        this.calculatedDays = calculatedDays;
    }

    public BigDecimal getCalculatedHours() {
        return calculatedHours;
    }

    public void setCalculatedHours(final BigDecimal calculatedHours) {
        this.calculatedHours = calculatedHours;
    }

    public BigDecimal getCorrectedDays() {
        return correctedDays;
    }

    public void setCorrectedDays(final BigDecimal correctedDays) {
        this.correctedDays = correctedDays;
    }

    public BigDecimal getCorrectedHours() {
        return correctedHours;
    }

    public void setCorrectedHours(final BigDecimal correctedHours) {
        this.correctedHours = correctedHours;
    }

    public BigDecimal getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(final BigDecimal adjustments) {
        this.adjustments = adjustments;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(final BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(final BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(final BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
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
                ", vehicleType='" + vehicleType + '\'' +
                ", issueTo='" + issueTo + '\'' +
                ", costCode='" + costCode + '\'' +
                ", rentalTemplate='" + rentalTemplate + '\'' +
                ", customer='" + customer + '\'' +
                ", completedDate=" + completedDate +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", estimatedIssueDate=" + estimatedIssueDate +
                ", issuedDate=" + issuedDate +
                ", issueLocation='" + issueLocation + '\'' +
                ", issuedBy='" + issuedBy + '\'' +
                ", returnDate=" + returnDate +
                ", estimatedReturnDate=" + estimatedReturnDate +
                ", returnLocation='" + returnLocation + '\'' +
                ", invoicedAmount=" + invoicedAmount +
                ", calculatedDays=" + calculatedDays +
                ", calculatedHours=" + calculatedHours +
                ", correctedDays=" + correctedDays +
                ", correctedHours=" + correctedHours +
                ", adjustments=" + adjustments +
                ", netAmount=" + netAmount +
                ", grossAmount=" + grossAmount +
                ", taxAmount=" + taxAmount +
                ", userDefinedFields=" + userDefinedFields +
                '}';
    }
}
