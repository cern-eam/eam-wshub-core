package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.udlmap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class UDLValueMap {
    @XmlElement
    private List<UDLValueMapEntry> property = new ArrayList<>();

    public List<UDLValueMapEntry> getProperty() {
        return property;
    }

    public void setProperty(List<UDLValueMapEntry> property) {
        this.property = property;
    }
}