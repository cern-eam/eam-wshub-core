package ch.cern.eam.wshub.core.services.userdefinedscreens.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UDLValue implements Serializable {
    private static final long serialVersionUID = 3824835369799503639L;

    private Object value;
    private UDLValueType type;

    public UDLValue() {
        type = UDLValueType.NULL;
    }

    public UDLValue(String value) {
        setString(value);
    }

    public UDLValue(Date value) {
        setDate(value);
    }

    public UDLValue(BigDecimal value) {
        setNumeric(value);
    }


    public void setString(String value) {
        if(value != null && value.length() > 64) {
            throw new IllegalArgumentException("String too large to include in a UDLValue");
        }

        type = UDLValueType.STRING;
        this.value = value;
    }

    public void setDate(Date value) {
        type = UDLValueType.DATE;
        this.value = value;
    }

    public void setNumeric(BigDecimal value) {
        type = UDLValueType.NUMERIC;
        this.value = value;
    }

    public String getString() {
        return type == UDLValueType.STRING ? (String) value : null;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDate() {
        return type == UDLValueType.DATE ? (Date) value : null;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getNumeric() {
        return type == UDLValueType.NUMERIC ? (BigDecimal) value : null;
    }

    public String toString() {
        switch(type) {
            case NULL: return "null";
            case STRING: return "\"" + value + "\"";
            default: return value.toString();
        }
    }
}
