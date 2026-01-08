package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargeDefinition implements Serializable {
    @EAMField(xpath = "TEMPLATECHARGEDEFINITIONID/TEMPLATECHARGEDEFINITIONCODE")
    private String chargeDefinitionId;

    @EAMField(xpath="CONTRACTTEMPLATEID/CONTRACTTEMPLATECODE")
    private String contractTemplateCode;

    @EAMField(xpath = "INVOICINGORGID/ORGANIZATIONCODE")
    private String invoicingOrg;

    @EAMField(xpath = "CHARGECATEGORY")
    private String chargeCategory;

    @EAMField(xpath = "CHARGELEVEL")
    private String chargeLevel;

    @EAMField(xpath = "CHARGESUBCATEGORY")
    private String chargeSubCategory;

    @EAMField(xpath = "DESCRIPTION")
    private String invoicingDescription;

    @EAMField(xpath = "INVOICE")
    private String invoice;

    @EAMField(xpath = "INVOICECONDITIONAL")
    private String invoiceConditional;

    @EAMField(xpath = "RATE")
    private BigDecimal rate;

    @EAMField(xpath = "ADJUSTMENTUNITPRICE")
    private BigDecimal adjustmentUnitPrice;

    @EAMField(xpath = "ADJUSTMENTTRANSACTION")
    private BigDecimal adjustmentTransaction;

    @EAMField(xpath = "ADJUSTMENTPERCENTAGEBEFORE")
    private BigDecimal adjustmentBefore;

    @EAMField(xpath = "ADJUSTMENTPERCENTAGEAFTER")
    private BigDecimal adjustmentAfter;

    @EAMField(xpath = "MINIMUMQUANTITY")
    private BigDecimal minimumQuantity;

    @EAMField(xpath = "MINIMUMCHARGE")
    private BigDecimal minimumCharge;

    @EAMField(xpath = "MAXIMUMCHARGE")
    private BigDecimal maximumCharge;

    @EAMField(xpath = "FREEUPTO")
    private BigDecimal freeUpTo;

    @EAMField(xpath = "TAXABLE")
    private String taxable;

    @EAMField(xpath = "UOMID/UOMCODE")
    private String usageUom;

    @EAMField(xpath = "CHARGEESTIMATEDUSAGE")
    private String chargeEstimatedUsage;

    @EAMField(xpath = "ROLLOVER")
    private String rollover;

    @EAMField(xpath = "TRADEID/TRADECODE")
    private String trade;

    @EAMField(xpath = "OCCUPATIONTYPE")
    private String typeOfHours;

    @EAMField(xpath = "PARTCLASSID/CLASSCODE")
    private String partClassCode;

    @EAMField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

}
