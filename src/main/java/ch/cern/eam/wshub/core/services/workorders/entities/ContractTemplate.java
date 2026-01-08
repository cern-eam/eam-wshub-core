package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractTemplate implements Serializable {
    @EAMField(xpath = "CONTRACTTEMPLATEID/CONTRACTTEMPLATECODE")
    private String contractTemplateId;

    @EAMField(xpath = "CONTRACTTEMPLATEID/DESCRIPTION")
    private String description;

    @EAMField(xpath = "CONTRACTTEMPLATEID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organisation;

    @EAMField(xpath = "CLASSID/CLASSCODE")
    private String classCode;

    @EAMField(xpath = "CONTRACTCLASSID/CONTRACTCLASSCODE")
    private String contractClass;

    @EAMField(xpath = "DEFAULTINVOICESTATUS")
    private String defaultInvoiceStatus;

    @EAMField(xpath = "WHEREUSED")
    private String whereUsed;

    @EAMField(xpath = "ROUNDINGHOURS")
    private String roudongHours;

    @EAMField(xpath = "ROUNDINGDAYS")
    private String roundingDays;

    @EAMField(xpath = "STARTTIMEHOURS")
    private String hourlyInvoicingStartTimeHours;

    @EAMField(xpath = "STARTTIMEMINUTES")
    private String hourlyInvoicingStartTimeMinutes;

    @EAMField(xpath = "ENDTIMEHOURS")
    private String hourlyInvoicingEndTimeHours;

    @EAMField(xpath = "ENDTIMEMINUTES")
    private String hourlyInvoicingEndTimeMinutes;

    @EAMField(xpath = "PERIOD")
    private String invoiceEveryPeriod;

    @EAMField(xpath = "PERIODUOM/PERIODUOMCODE")
    private String invoiceEveryPeriodCode;

    @EAMField(xpath = "OUTOFSERVICE")
    private String outOfService;

    @EAMField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;


}
