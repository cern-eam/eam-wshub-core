package ch.cern.eam.wshub.core.adapters;

import ch.cern.eam.wshub.core.tools.DataTypeTools;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;
import java.math.BigInteger;

public class BigIntegerAdapter extends XmlAdapter<String, BigInteger> {

    @Override
    public String marshal(BigInteger value) throws Exception {
		if (value == null) {
			return null;
		} else {
			return value.toString();
		}
    }

    @Override
    public BigInteger unmarshal(String value) throws Exception {
        if (value == null) {
            return null;
        } else if (value.equals("") || value.equals("*NULL*")) {
            return BigInteger.valueOf(DataTypeTools.NULLIFY_VALUE);
        } else {
            return new BigInteger(value);
        }
    }

}