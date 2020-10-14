package ch.cern.eam.wshub.core.services.userdefinedscreens.entities.udlmap;

import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLValue;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UDLMapAdapter extends XmlAdapter<UDLValueMap, Map<String, List<UDLValue>>> {

    @Override
    public Map<String, List<UDLValue>> unmarshal(UDLValueMap value) throws Exception {
        Map<String, List<UDLValue>> map = new HashMap<>();

        if (value == null) {
            return null;
        }

        for (UDLValueMapEntry list : value.getProperty()) {
            map.put(list.getKey(), list.getEntries());
        }

        return map;
    }

    @Override
    public UDLValueMap marshal(Map<String, List<UDLValue>> value) throws Exception {
        UDLValueMap udlValueMap = new UDLValueMap();

        if (value == null) {
            return null;
        }

        for (Map.Entry<String, List<UDLValue>> property : value.entrySet()) {
            UDLValueMapEntry udlValueMapEntry = new UDLValueMapEntry();
            udlValueMapEntry.setKey(property.getKey());
            udlValueMapEntry.setEntries(property.getValue());
            udlValueMap.getProperty().add(udlValueMapEntry);
        }

        return udlValueMap;
    }
}
