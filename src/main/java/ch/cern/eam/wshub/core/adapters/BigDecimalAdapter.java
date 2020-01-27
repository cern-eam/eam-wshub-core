package ch.cern.eam.wshub.core.adapters;

import ch.cern.eam.wshub.core.tools.DataTypeTools;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public String marshal(BigDecimal value) throws Exception {
		return bigDecimalToString(value);
    }

    @Override
    public BigDecimal unmarshal(String value) throws Exception {
        return stringToBigDecimal(value);
    }

    public static String bigDecimalToString(BigDecimal value) {
        if (value == null) {
            return null;
        } else {
            return value.toPlainString();
        }
    }

    public static BigDecimal stringToBigDecimal(String value) {
        if (value == null) {
            return null;
        } else if (value.equals("") || value.equals("*NULL*")) {
            return BigDecimal.valueOf(DataTypeTools.NULLIFY_VALUE);
        } else {
            return new BigDecimal(value);
        }
    }
}