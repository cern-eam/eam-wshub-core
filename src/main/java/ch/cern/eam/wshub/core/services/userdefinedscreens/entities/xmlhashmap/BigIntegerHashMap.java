package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BigIntegerHashMap implements Serializable {

    private List<BigIntegerHashMapEntry> entries = new ArrayList<>();

    @XmlElementWrapper(name = "entries")
    @XmlElement(name = "entry")
    public List<BigIntegerHashMapEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<BigIntegerHashMapEntry> entries) {
        this.entries = entries;
    }
}
