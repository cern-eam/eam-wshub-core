package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StringHashMap implements Serializable {

    private List<StringHashMapEntry> entries = new ArrayList<>();

    @XmlElementWrapper(name = "entries")
    @XmlElement(name = "entry")
    public List<StringHashMapEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<StringHashMapEntry> entries) {
        this.entries = entries;
    }
}
