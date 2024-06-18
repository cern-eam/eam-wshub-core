package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import ch.cern.eam.wshub.core.adapters.BigIntegerAdapter;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigInteger;

public class BigIntegerHashMapEntry {
    private String key;

    private BigInteger value;

    public BigIntegerHashMapEntry() {

    }

    public BigIntegerHashMapEntry(String key, BigInteger value) {
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

    @XmlJavaTypeAdapter(BigIntegerAdapter.class)
    @XmlValue
    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }
}
