package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

public class Lot {

    private String code;
    private String desc;
    private String classCode;
    private Date expirationDate;
    private String manufacturerLot;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getManufacturerLot() {
        return manufacturerLot;
    }

    public void setManufacturerLot(String manufacturerLot) {
        this.manufacturerLot = manufacturerLot;
    }

    @Override
    public String toString() {
        return "Lot{" +
                "classCode='" + classCode + '\'' +
                ", code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", expirationDate=" + expirationDate +
                ", manufacturerLot='" + manufacturerLot + '\'' +
                '}';
    }
}
