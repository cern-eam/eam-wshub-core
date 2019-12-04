package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;
import java.util.*;

public class BigDecimalHashMapAdapter extends XmlAdapter<BigDecimalHashMap, HashMap<String, BigDecimal>> {

    @Override
    public HashMap<String, BigDecimal> unmarshal(BigDecimalHashMap v) throws Exception {
        HashMap<String, BigDecimal> hashMap = new LinkedHashMap<>();
        for(BigDecimalHashMapEntry entry : v.getEntries()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    @Override
    public BigDecimalHashMap marshal(HashMap<String, BigDecimal> v) throws Exception {
        BigDecimalHashMap dhm = new BigDecimalHashMap();
        List<BigDecimalHashMapEntry> list = new ArrayList<>();
        for(Map.Entry<String, BigDecimal> entry : v.entrySet()) {
            list.add(new BigDecimalHashMapEntry(entry.getKey(), entry.getValue()));
        }
        dhm.setEntries(list);
        return dhm;
    }
}
