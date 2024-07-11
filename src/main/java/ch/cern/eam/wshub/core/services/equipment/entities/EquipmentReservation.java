package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class EquipmentReservation {
    // Main identifying fields
    @EAMField(xpath = "CUSTOMERRENTALID/CUSTOMERRENTALCODE")
    private String code;
    @EAMField(xpath = "CUSTOMERRENTALID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organizationCode;
    @EAMField(xpath = "CUSTOMERRENTALID/DESCRIPTION")
    private String description;

    // Rental Details
    @EAMField(xpath = "RentalDetails/RENTALTYPE/STATUSCODE")
    private String rentalType;
    @EAMField(xpath = "RentalDetails/STATUS/STATUSCODE")
    private String status;
    @EAMField(xpath = "RentalDetails/EQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;
    @EAMField(xpath = "RentalDetails/LOCATIONID/LOCATIONCODE")
    private String locationCode;
    @EAMField(xpath = "RentalDetails/CLASSID/CLASSCODE")
    private String classCode;
    @EAMField(xpath = "RentalDetails/REFERENCE")
    private String reference;
    @EAMField(xpath = "RentalDetails/VEHICLETYPE/TYPECODE")
    private String vehicleType;
    @EAMField(xpath = "RentalDetails/ISSUETO/PERSONCODE")
    private String issueTo;
    @EAMField(xpath = "RentalDetails/COSTCODE")
    private String costCode;
    @EAMField(xpath = "RentalDetails/RENTALTEMPLATEID/RENTALTEMPLATECODE")
    private String rentalTemplate;
    @EAMField(xpath = "RentalDetails/CUSTOMERID/CUSTOMERCODE")
    private String customer;

    @EAMField(xpath = "RentalDetails/COMPLETEDDATE")
    private Date completedDate;
    @EAMField(xpath = "RentalDetails/CREATEDBY/USERCODE")
    private String createdBy;
    @EAMField(xpath = "RentalDetails/CREATEDDATE")
    private Date createdDate;

    // Issue Details
    @EAMField(xpath = "IssueDetails/ESTIMATEDISSUEDATE")
    private Date estimatedIssueDate;
    @EAMField(xpath = "IssueDetails/ISSUEDDATE")
    private Date issuedDate;
    @EAMField(xpath = "IssueDetails/ISSUELOCATION/LOCATIONCODE")
    private String issueLocation;
    @EAMField(xpath = "IssueDetails/ISSUEDBY/USERCODE")
    private String issuedBy;

    // Return Details
    @EAMField(xpath = "ReturnDetails/RETURNDATE")
    private Date returnDate;
    @EAMField(xpath = "ReturnDetails/ESTIMATEDRETURNDATE")
    private Date estimatedReturnDate;
    @EAMField(xpath = "ReturnDetails/RETURNLOCATION/LOCATIONCODE")
    private String returnLocation;

    // Invoicing Details
    @EAMField(xpath = "InvoicingDetails/INVOICEDAMOUNT")
    private BigDecimal invoicedAmount;
    @EAMField(xpath = "InvoicingDetails/CALCULATEDDAYS")
    private BigDecimal calculatedDays;
    @EAMField(xpath = "InvoicingDetails/CALCULATEDHOURS")
    private BigDecimal calculatedHours;
    @EAMField(xpath = "InvoicingDetails/CORRECTEDDAYS")
    private BigDecimal correctedDays;
    @EAMField(xpath = "InvoicingDetails/CORRECTEDHOURS")
    private BigDecimal correctedHours;
    @EAMField(xpath = "InvoicingDetails/ADJUSTMENTS")
    private BigDecimal adjustments;
    @EAMField(xpath = "InvoicingDetails/NETAMOUNT")
    private BigDecimal netAmount;
    @EAMField(xpath = "InvoicingDetails/GROSSAMOUNT")
    private BigDecimal grossAmount;
    @EAMField(xpath = "InvoicingDetails/TAXAMOUNT")
    private BigDecimal taxAmount;
    @EAMField(xpath = "IssueDetails/ISSUEVEFUELLEVEL")
    private BigDecimal issueFuelLevel;
    @EAMField(xpath = "IssueDetails/ISSUEREADING")
    private BigDecimal issueReading;
    @EAMField(xpath = "ReturnDetails/RETURNEDTO/USERCODE")
    private String returnedTo;
    @EAMField(xpath = "ReturnDetails/RETURNFUELLEVEL")
    private BigDecimal returnFuelLevel;
    @EAMField(xpath = "ReturnDetails/RETURNREADING")
    private BigDecimal returnReading;
    @EAMField(xpath = "IssueDetails/UOMID/UOMCODE")
    private String uom;
    @Transient
    @EAMField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getCompletedDate() {
        return completedDate;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getCreatedDate() {
        return createdDate;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getEstimatedIssueDate() {
        return estimatedIssueDate;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getIssuedDate() {
        return issuedDate;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getReturnDate() {
        return returnDate;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getEstimatedReturnDate() {
        return estimatedReturnDate;
    }
}
