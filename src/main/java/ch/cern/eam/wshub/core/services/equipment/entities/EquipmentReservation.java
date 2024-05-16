package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
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
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date completedDate;
    @InforField(xpath = "RentalDetails/CREATEDBY/USERCODE")
    private String createdBy;
    @InforField(xpath = "RentalDetails/CREATEDDATE")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date createdDate;

    // Issue Details
    @InforField(xpath = "IssueDetails/ESTIMATEDISSUEDATE")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date estimatedIssueDate;
    @InforField(xpath = "IssueDetails/ISSUEDDATE")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date issuedDate;
    @InforField(xpath = "IssueDetails/ISSUELOCATION/LOCATIONCODE")
    private String issueLocation;
    @InforField(xpath = "IssueDetails/ISSUEDBY/USERCODE")
    private String issuedBy;

    // Return Details
    @InforField(xpath = "ReturnDetails/RETURNDATE")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date returnDate;
    @InforField(xpath = "ReturnDetails/ESTIMATEDRETURNDATE")
    @XmlJavaTypeAdapter(DateAdapter.class)
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
    @InforField(xpath = "IssueDetails/ISSUEVEFUELLEVEL")
    private BigDecimal issueFuelLevel;
    @InforField(xpath = "IssueDetails/ISSUEREADING")
    private BigDecimal issueReading;
    @InforField(xpath = "ReturnDetails/RETURNEDTO/USERCODE")
    private String returnedTo;
    @InforField(xpath = "ReturnDetails/RETURNFUELLEVEL")
    private BigDecimal returnFuelLevel;
    @InforField(xpath = "ReturnDetails/RETURNREADING")
    private BigDecimal returnReading;
    @InforField(xpath = "IssueDetails/UOMID/UOMCODE")
    private String uom;
    @Transient
    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRentalType() {
        return rentalType;
    }

    public void setRentalType(String rentalType) {
        this.rentalType = rentalType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getIssueTo() {
        return issueTo;
    }

    public void setIssueTo(String issueTo) {
        this.issueTo = issueTo;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getRentalTemplate() {
        return rentalTemplate;
    }

    public void setRentalTemplate(String rentalTemplate) {
        this.rentalTemplate = rentalTemplate;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEstimatedIssueDate() {
        return estimatedIssueDate;
    }

    public void setEstimatedIssueDate(Date estimatedIssueDate) {
        this.estimatedIssueDate = estimatedIssueDate;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getIssueLocation() {
        return issueLocation;
    }

    public void setIssueLocation(String issueLocation) {
        this.issueLocation = issueLocation;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getEstimatedReturnDate() {
        return estimatedReturnDate;
    }

    public void setEstimatedReturnDate(Date estimatedReturnDate) {
        this.estimatedReturnDate = estimatedReturnDate;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }

    public BigDecimal getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getCalculatedDays() {
        return calculatedDays;
    }

    public void setCalculatedDays(BigDecimal calculatedDays) {
        this.calculatedDays = calculatedDays;
    }

    public BigDecimal getCalculatedHours() {
        return calculatedHours;
    }

    public void setCalculatedHours(BigDecimal calculatedHours) {
        this.calculatedHours = calculatedHours;
    }

    public BigDecimal getCorrectedDays() {
        return correctedDays;
    }

    public void setCorrectedDays(BigDecimal correctedDays) {
        this.correctedDays = correctedDays;
    }

    public BigDecimal getCorrectedHours() {
        return correctedHours;
    }

    public void setCorrectedHours(BigDecimal correctedHours) {
        this.correctedHours = correctedHours;
    }

    public BigDecimal getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(BigDecimal adjustments) {
        this.adjustments = adjustments;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getIssueFuelLevel() {
        return issueFuelLevel;
    }

    public void setIssueFuelLevel(BigDecimal issueFuelLevel) {
        this.issueFuelLevel = issueFuelLevel;
    }

    public BigDecimal getIssueReading() {
        return issueReading;
    }

    public void setIssueReading(BigDecimal issueReading) {
        this.issueReading = issueReading;
    }

    public String getReturnedTo() {
        return returnedTo;
    }

    public void setReturnedTo(String returnedTo) {
        this.returnedTo = returnedTo;
    }

    public BigDecimal getReturnFuelLevel() {
        return returnFuelLevel;
    }

    public void setReturnFuelLevel(BigDecimal returnFuelLevel) {
        this.returnFuelLevel = returnFuelLevel;
    }

    public BigDecimal getReturnReading() {
        return returnReading;
    }

    public void setReturnReading(BigDecimal returnReading) {
        this.returnReading = returnReading;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
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
                ", issueFuelLevel=" + issueFuelLevel +
                ", issueReading=" + issueReading +
                ", returnedTo='" + returnedTo + '\'' +
                ", returnFuelLevel=" + returnFuelLevel +
                ", returnReading=" + returnReading +
                ", uom='" + uom + '\'' +
                ", userDefinedFields=" + userDefinedFields +
                '}';
    }
}
