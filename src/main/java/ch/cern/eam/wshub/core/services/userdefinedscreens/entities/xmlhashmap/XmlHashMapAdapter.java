package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;

public class XmlHashMapAdapter<T> extends XmlAdapter<XmlHashMap, Map<String, T>> {

    @Override
    public HashMap<String, T> unmarshal(XmlHashMap v) throws Exception {
        HashMap<String, T> hashMap = new LinkedHashMap<>();
        List<XmlHashMapEntry<T>> entries = v.getEntries();
        for(XmlHashMapEntry<T> entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    @Override
    public XmlHashMap marshal(Map<String, T> v) throws Exception {
        XmlHashMap dhm = new XmlHashMap();
        List<XmlHashMapEntry> list = new ArrayList<>();
        for(Map.Entry<String, T> entry : v.entrySet()) {
            XmlHashMapEntry<T> xmlentry = supplier();
            xmlentry.setKey(entry.getKey());
            xmlentry.setValue(entry.getValue());
            list.add(xmlentry);
        }
        dhm.setEntries(list);
        return dhm;
    }

    protected XmlHashMapEntry supplier() {
        return new XmlHashMapEntry<T>();
    }
}
