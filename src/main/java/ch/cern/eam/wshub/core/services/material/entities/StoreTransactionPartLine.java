package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import net.datastream.schemas.mp_fields.*;
import org.openapplications.oagis_segments.AMOUNT;
import org.openapplications.oagis_segments.QUANTITY;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;

public class StoreTransactionPartLine implements Serializable {
    @InforField(xpath="PARTID/PARTCODE")
    private String partCode;
    @InforField(xpath="REPAIRABLE")
    private String repairable;
    @InforField(xpath="TRANSACTIONQUANTITY")
    private BigDecimal transactionQuantity;
    @InforField(xpath="REPAIRQUANTITY")
    private BigDecimal repairQuantity;
    @InforField(xpath="FROMBIN/BIN")
    private String fromBinCode;
    @InforField(xpath="TOBIN/BIN")
    private String toBinCode;
    @InforField(xpath="LOTID/LOTCODE")
    private String lotCode;
    @InforField(xpath="PRICE")
    private BigDecimal price;
    @InforField(xpath="ASSETID/EQUIPMENTCODE")
    private String assetCode;
    @InforField(xpath="SERIALNUMBER")
    private String serialNumber;
    @InforField(xpath="StandardUserDefinedFields")
    private UserDefinedFields StandardUserDefinedFields;
    @InforField(xpath="PARTCONDITIONTEMPLATECONDITIONCODE")
    private String partConditionTemplateConditionCode;

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(final String partCode) {
        this.partCode = partCode;
    }

    public String getRepairable() {
        return repairable;
    }

    public void setRepairable(final String repairable) {
        this.repairable = repairable;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getTransactionQuantity() {
        return transactionQuantity;
    }

    public void setTransactionQuantity(final BigDecimal transactionQuantity) {
        this.transactionQuantity = transactionQuantity;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getRepairQuantity() {
        return repairQuantity;
    }

    public void setRepairQuantity(final BigDecimal repairQuantity) {
        this.repairQuantity = repairQuantity;
    }

    public String getFromBinCode() {
        return fromBinCode;
    }

    public void setFromBinCode(final String fromBinCode) {
        this.fromBinCode = fromBinCode;
    }

    public String getToBinCode() {
        return toBinCode;
    }

    public void setToBinCode(final String toBinCode) {
        this.toBinCode = toBinCode;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(final String lotCode) {
        this.lotCode = lotCode;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(final String assetCode) {
        this.assetCode = assetCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public UserDefinedFields getStandardUserDefinedFields() {
        return StandardUserDefinedFields;
    }

    public void setStandardUserDefinedFields(final UserDefinedFields standardUserDefinedFields) {
        StandardUserDefinedFields = standardUserDefinedFields;
    }

    public String getPartConditionTemplateConditionCode() {
        return partConditionTemplateConditionCode;
    }

    public void setPartConditionTemplateConditionCode(final String partConditionTemplateConditionCode) {
        this.partConditionTemplateConditionCode = partConditionTemplateConditionCode;
    }

    @Override
    public String toString() {
        return "StoreTransactionPartLine{" +
                "partCode='" + partCode + '\'' +
                ", repairable='" + repairable + '\'' +
                ", transactionQuantity=" + transactionQuantity +
                ", repairQuantity=" + repairQuantity +
                ", fromBinCode='" + fromBinCode + '\'' +
                ", toBinCode='" + toBinCode + '\'' +
                ", lotCode='" + lotCode + '\'' +
                ", price=" + price +
                ", assetCode='" + assetCode + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", StandardUserDefinedFields=" + StandardUserDefinedFields +
                ", partConditionTemplateConditionCode='" + partConditionTemplateConditionCode + '\'' +
                '}';
    }
}
