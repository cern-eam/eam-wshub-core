package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;

public class EquipmentReservationAdjustment {
    // Adjustment identifier
    @InforField(xpath = "CUSTOMERRENTALADJUSTMENTID/CUSTOMERRENTALADJUSTMENTPK")
    private String code;

    // Identifier of the corresponding reservation
    @InforField(xpath = "CUSTOMERRENTALID/CUSTOMERRENTALCODE")
    private String reservationCode;

    // Adjustment details
    @InforField(xpath = "ADJUSTMENTID/ADJUSTMENTCODE")
    private String adjustmentCode;
    @InforField(xpath = "TAXID/TAXCODE")
    private String taxCode;
    @InforField(xpath = "ADJUSTMENTDATE")
    private Date adjustmentDate;
    @InforField(xpath = "ADJUSTMENTQUANTITY")
    private BigDecimal adjustmentQuantity;
    @InforField(xpath = "ADJUSTMENTTYPE/TYPECODE")
    private String adjustmentTypeCode;
    @InforField(xpath = "ADJUSTMENTRTYPE/TYPECODE")
    private String adjustmentSystemTypeCode;
    @InforField(xpath = "ADJUSTMENTSTATUS/STATUSCODE")
    private String adjustmentStatusCode;
    @InforField(xpath = "ADJUSTMENTRSTATUS/STATUSCODE")
    private String adjustmentSystemStatusCode;
    @InforField(xpath = "AJUSTMENTCOMMENTS")
    private String adjustmentComments;
    @InforField(xpath = "RATE")
    private BigDecimal rate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getAdjustmentCode() {
        return adjustmentCode;
    }

    public void setAdjustmentCode(String adjustmentCode) {
        this.adjustmentCode = adjustmentCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getAdjustmentQuantity() {
        return adjustmentQuantity;
    }

    public void setAdjustmentQuantity(BigDecimal adjustmentQuantity) {
        this.adjustmentQuantity = adjustmentQuantity;
    }

    public String getAdjustmentTypeCode() {
        return adjustmentTypeCode;
    }

    public void setAdjustmentTypeCode(String adjustmentTypeCode) {
        this.adjustmentTypeCode = adjustmentTypeCode;
    }

    public String getAdjustmentSystemTypeCode() {
        return adjustmentSystemTypeCode;
    }

    public void setAdjustmentSystemTypeCode(String adjustmentSystemTypeCode) {
        this.adjustmentSystemTypeCode = adjustmentSystemTypeCode;
    }

    public String getAdjustmentStatusCode() {
        return adjustmentStatusCode;
    }

    public void setAdjustmentStatusCode(String adjustmentStatusCode) {
        this.adjustmentStatusCode = adjustmentStatusCode;
    }

    public String getAdjustmentSystemStatusCode() {
        return adjustmentSystemStatusCode;
    }

    public void setAdjustmentSystemStatusCode(String adjustmentSystemStatusCode) {
        this.adjustmentSystemStatusCode = adjustmentSystemStatusCode;
    }

    public String getAdjustmentComments() {
        return adjustmentComments;
    }

    public void setAdjustmentComments(String adjustmentComments) {
        this.adjustmentComments = adjustmentComments;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "EquipmentReservationAdjustment{" +
                "code='" + code + '\'' +
                ", reservationCode='" + reservationCode + '\'' +
                ", adjustmentCode='" + adjustmentCode + '\'' +
                ", taxCode='" + taxCode + '\'' +
                ", adjustmentDate=" + adjustmentDate +
                ", adjustmentQuantity=" + adjustmentQuantity +
                ", adjustmentTypeCode='" + adjustmentTypeCode + '\'' +
                ", adjustmentSystemTypeCode='" + adjustmentSystemTypeCode + '\'' +
                ", adjustmentStatusCode='" + adjustmentStatusCode + '\'' +
                ", adjustmentSystemStatusCode='" + adjustmentSystemStatusCode + '\'' +
                ", adjustmentComments='" + adjustmentComments + '\'' +
                ", rate=" + rate +
                '}';
    }
}
