package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.udlmap;

import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLValue;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class UDLValueMapEntry {
    @XmlElement
    private String key;

    @XmlElement(name="entry")
    private List<UDLValue> entries = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public List<UDLValue> getEntries() {
        return entries;
    }

    public void setEntries(List<UDLValue> entries) {
        this.entries = entries;
    }
}
