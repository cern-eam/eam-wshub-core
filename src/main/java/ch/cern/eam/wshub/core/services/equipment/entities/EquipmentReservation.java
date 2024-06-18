package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
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
