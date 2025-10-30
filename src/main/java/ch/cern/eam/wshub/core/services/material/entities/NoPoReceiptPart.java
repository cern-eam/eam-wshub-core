package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoPoReceiptPart implements Serializable {

    @GridField(name = "receiptline")
    @InforField(xpath = "TRANSACTIONLINEID/TRANSACTIONLINENUM")
    private BigInteger transactionLineId;

    @GridField(name = "receiptcode")
    @InforField(xpath = "TRANSACTIONLINEID/TRANSACTIONID/TRANSACTIONCODE")
    private String transactionCode;

    @GridField(name = "partcode")
    @InforField(xpath = "PARTID/PARTCODE")
    private String part;

    @GridField(name = "partdesc")
    @InforField(xpath = "PARTID/DESCRIPTION")
    private String partDescription;

    @GridField(name = "receiptqty")
    @InforField(xpath = "RECEIPTQUANTITY/QUANTITY/VALUE")
    private BigDecimal quantity;

    @GridField(name = "partuom")
    @InforField(xpath = "UOMID/UOMCODE")
    private String unit;

    @GridField(name = "bincode")
    @InforField(xpath = "BINID/BIN")
    private String bin;

    @GridField(name = "lotcode")
    @InforField(xpath = "LOTID/LOTCODE")
    private String lot;

    @GridField(name = "manufacturerlot")
    @InforField(xpath = "MANUFACTLOT")
    private String manufacturer;

    @GridField(name = "printqty")
    @InforField(xpath = "PRINT/QUANTITY/VALUE")
    private String printQuantity;

    @GridField(name = "price")
    @InforField(xpath = "PRICE/AMOUNT/VALUE")
    private BigDecimal price;

    @GridField(name = "assetid")
    @InforField(xpath = "ASSETID/EQUIPMENTCODE")
    private String asset;

    @GridField(name = "byasset")
    @InforField(xpath = "BYASSET")
    private String trackByAsset;

    @InforField(xpath = "PARTCONDITIONTEMPLATECONDITIONCODE")
    private String condition;

    private UserDefinedFields userDefinedFields;


}
