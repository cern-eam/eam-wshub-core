package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

public class StringHashMapEntry {
    private String key;

    private String value;

    public StringHashMapEntry() {

    }

    public StringHashMapEntry(String key, String value) {
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

    //@XmlJavaTypeAdapter([REPLACEBYADAPTERIFNECESSARY].class)
    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
