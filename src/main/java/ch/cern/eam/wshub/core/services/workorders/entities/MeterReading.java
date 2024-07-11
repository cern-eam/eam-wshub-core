package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.EAMField;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MeterReading implements Serializable {
    private static final long serialVersionUID = -3004563411794265577L;

    @EAMField(xpath = "USAGEUOMID/UOMCODE")
    private String UOM;
    @EAMField(xpath = "TARGETEQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;
    @EAMField(xpath = "TARGETEQUIPMENTID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String equipmentOrganization;
    private BigDecimal actualValue;
    private BigDecimal differenceValue;
    private Date readingDate;
    @EAMField(xpath = "WORKORDERID/JOBNUM")
    private String woNumber;

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String uOM) {
        UOM = uOM;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentOrganization() {
        return equipmentOrganization;
    }

    public void setEquipmentOrganization(String equipmentOrganization) {
        this.equipmentOrganization = equipmentOrganization;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getActualValue() {
        return actualValue;
    }
    public void setActualValue(BigDecimal actualValue) {
        this.actualValue = actualValue;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getDifferenceValue() {
        return differenceValue;
    }
    public void setDifferenceValue(BigDecimal differenceValue) {
        this.differenceValue = differenceValue;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }

    @Override
    public String toString() {
        return "MeterReading [UOM=" + UOM + ", equipmentCode=" + equipmentCode + ", actualValue=" + actualValue
                + ", differenceValue=" + differenceValue + ", readingDate=" + readingDate + "]";
    }

    public String getWoNumber() {
        return woNumber;
    }

    public void setWoNumber(String woNumber) {
        this.woNumber = woNumber;
    }


}
