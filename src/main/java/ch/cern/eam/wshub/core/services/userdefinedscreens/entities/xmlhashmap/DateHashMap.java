package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DateHashMap implements Serializable {

    private List<DateHashMapEntry> entries = new ArrayList<>();

    @XmlElementWrapper(name = "entries")
    @XmlElement(name = "entry")
    public List<DateHashMapEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<DateHashMapEntry> entries) {
        this.entries = entries;
    }
}
