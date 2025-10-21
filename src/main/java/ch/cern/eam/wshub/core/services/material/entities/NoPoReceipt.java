package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoPoReceipt implements Serializable {

    @InforField(xpath = "TRANSACTIONID/TRANSACTIONCODE")
    private String code;

    @InforField(xpath = "TRANSACTIONID/DESCRIPTION")
    private String description;

    @InforField(xpath = "TRANSACTIONID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organization;

    @InforField(xpath = "TRANSACTIONSTATUS/STATUSCODE")
    private String status;

    @InforField(xpath = "SUPPLIERID/SUPPLIERCODE")
    private String supplier;

    @InforField(xpath = "STOREID/STORECODE")
    private String store;

    @InforField(xpath = "ADVICENUMBER")
    private String referenceNumber;

    @InforField(xpath = "TRANSACTIONDATE")
    private Date createdDate;

    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    private List<NoPoReceiptPart> parts;

}
