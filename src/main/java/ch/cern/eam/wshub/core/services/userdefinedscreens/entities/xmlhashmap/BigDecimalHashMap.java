package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BigDecimalHashMap implements Serializable {

    private List<BigDecimalHashMapEntry> entries = new ArrayList<>();

    @XmlElementWrapper(name = "entries")
    @XmlElement(name = "entry")
    public List<BigDecimalHashMapEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<BigDecimalHashMapEntry> entries) {
        this.entries = entries;
    }
}
