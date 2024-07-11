package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class XmlHashMap<T> implements Serializable {

    private List<XmlHashMapEntry<T>> entries = new ArrayList<>();

    @XmlElementWrapper(name = "dates")
    @XmlElement(name = "entry")
    public List<XmlHashMapEntry<T>> getEntries() {
        return entries;
    }

    public void setEntries(List<XmlHashMapEntry<T>> entries) {
        this.entries = entries;
    }
}
