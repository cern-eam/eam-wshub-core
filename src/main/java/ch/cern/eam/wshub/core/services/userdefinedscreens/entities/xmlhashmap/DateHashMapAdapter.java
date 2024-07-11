package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;

public class DateHashMapAdapter extends XmlAdapter<DateHashMap, HashMap<String, Date>> {

    @Override
    public HashMap<String, Date> unmarshal(DateHashMap v) throws Exception {
        HashMap<String, Date> hashMap = new LinkedHashMap<>();
        for(DateHashMapEntry entry : v.getEntries()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    @Override
    public DateHashMap marshal(HashMap<String, Date> v) throws Exception {
        DateHashMap dhm = new DateHashMap();
        List<DateHashMapEntry> list = new ArrayList<>();
        for(Map.Entry<String, Date> entry : v.entrySet()) {
            list.add(new DateHashMapEntry(entry.getKey(), entry.getValue()));
        }
        dhm.setEntries(list);
        return dhm;
    }
}
