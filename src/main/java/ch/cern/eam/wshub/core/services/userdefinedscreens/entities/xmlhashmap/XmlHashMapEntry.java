package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class XmlHashMapEntry<T> {
    private String key;

    private T value;

    public XmlHashMapEntry() {

    }

    public XmlHashMapEntry(String key, T value) {
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

    @XmlValue
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
