package ch.cern.eam.wshub.core.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeBoolean;

public class BooleanAdapter extends XmlAdapter<String, Boolean> {

    @Override
    public String marshal(Boolean value) throws Exception {
		if (value == null || !value) {
			return "false";
		} else {
			return "true";
		}
    }

    @Override
    public Boolean unmarshal(String value) throws Exception {
    	return decodeBoolean(value);
    }

}