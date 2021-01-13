package ch.cern.eam.wshub.core.services.userdefinedscreens.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UDLValue implements Serializable {
    private static final long serialVersionUID = 3824835369799503639L;

    private String stringValue;
    private BigDecimal numberValue;
    private Date dateValue;

    public UDLValue() {
    }

    public UDLValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public UDLValue(BigDecimal numberValue) {
        this.numberValue = numberValue;
    }

    public UDLValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public void setString(String value) {
        if (value != null) {
            if (value.length() > 64) {
                throw new IllegalArgumentException("String too large to include in a UDLValue");
            }
            this.stringValue = value;
            this.numberValue = null;
            this.dateValue = null;
        }
    }

    public void setNumeric(BigDecimal value) {
        if (value != null) {
            this.stringValue = null;
            this.numberValue = value;
            this.dateValue = null;
        }
    }

    public void setDate(Date value) {
        if (value != null) {
            this.stringValue = null;
            this.numberValue = null;
            this.dateValue = value;
        }
    }

    public String getString() {
        return stringValue;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getNumeric() {
        return numberValue;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDate() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public UDLValueType getType() {
        return this.stringValue != null ? UDLValueType.STRING
                : this.numberValue != null ? UDLValueType.NUMERIC
                : this.dateValue != null ? UDLValueType.DATE
                : UDLValueType.NULL;
    }

    public void setType(UDLValueType type) {
        //To prevent unmarshaller errors
    }

    public String toString() {
        switch (this.getType()) {
            case STRING: return "\"" + this.stringValue + "\"";
            case NUMERIC: return this.numberValue.toString();
            case DATE: return this.dateValue.toString();
            case NULL: return "*NULL*";
            default: return null;
        }
    }
}
