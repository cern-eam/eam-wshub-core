package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

public class MeterReading implements Serializable {
    private static final long serialVersionUID = -3004563411794265577L;

    private String UOM;
    private String equipmentCode;
    private String actualValue;
    private String differenceValue;
    private Date readingDate;
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

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getDifferenceValue() {
        return differenceValue;
    }

    public void setDifferenceValue(String differenceValue) {
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
