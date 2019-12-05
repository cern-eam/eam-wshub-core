package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;

public class StringHashMapAdapter extends XmlAdapter<StringHashMap, HashMap<String, String>> {

    @Override
    public HashMap<String, String> unmarshal(StringHashMap v) throws Exception {
        HashMap<String, String> hashMap = new LinkedHashMap<>();
        for(StringHashMapEntry entry : v.getEntries()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    @Override
    public StringHashMap marshal(HashMap<String, String> v) throws Exception {
        StringHashMap dhm = new StringHashMap();
        List<StringHashMapEntry> list = new ArrayList<>();
        for(Map.Entry<String, String> entry : v.entrySet()) {
            list.add(new StringHashMapEntry(entry.getKey(), entry.getValue()));
        }
        dhm.setEntries(list);
        return dhm;
    }
}
