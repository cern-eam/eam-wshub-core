package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

public class DateHashMapEntry {
    private String key;

    private Date value;

    public DateHashMapEntry() {

    }

    public DateHashMapEntry(String key, Date value) {
        this.key = key;
        this.value = value;
    }

    @XmlAttribute
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlValue
    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }
}
