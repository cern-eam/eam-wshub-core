package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;

public class BigDecimalHashMapEntry {
    private String key;

    private BigDecimal value;

    public BigDecimalHashMapEntry() {

    }

    public BigDecimalHashMapEntry(String key, BigDecimal value) {
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

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    @XmlValue
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
