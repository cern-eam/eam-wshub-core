package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.udlmap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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