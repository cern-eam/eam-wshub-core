package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.*;

public class BigIntegerHashMapAdapter extends XmlAdapter<BigIntegerHashMap, HashMap<String, BigInteger>> {

    @Override
    public HashMap<String, BigInteger> unmarshal(BigIntegerHashMap v) throws Exception {
        HashMap<String, BigInteger> hashMap = new LinkedHashMap<>();
        for(BigIntegerHashMapEntry entry : v.getEntries()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    @Override
    public BigIntegerHashMap marshal(HashMap<String, BigInteger> v) throws Exception {
        BigIntegerHashMap dhm = new BigIntegerHashMap();
        List<BigIntegerHashMapEntry> list = new ArrayList<>();
        for(Map.Entry<String, BigInteger> entry : v.entrySet()) {
            list.add(new BigIntegerHashMapEntry(entry.getKey(), entry.getValue()));
        }
        dhm.setEntries(list);
        return dhm;
    }
}
