package ch.cern.eam.wshub.core.services.userdefinedscreens.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class UDLEntry implements Serializable {
    private static final long serialVersionUID = 3824835369799503639L;

    private Object value;
    private UDLEntryType type;

    private String property;
    private BigInteger sequenceNumber;

    public UDLEntry() {
        // to turn this into a Java Bean, avoid using in actual code
    }

    public UDLEntry(String property, BigInteger sequenceNumber, String value) {
        this(property, sequenceNumber);
        setString(value);
    }

    public UDLEntry(String property, BigInteger sequenceNumber, Date value) {
        this(property, sequenceNumber);
        setDate(value);
    }

    public UDLEntry(String property, BigInteger sequenceNumber, BigDecimal value) {
        this(property, sequenceNumber);
        setNumeric(value);
    }

    public UDLEntry(String property, BigInteger sequenceNumber) {
        if(property == null) {
            throw new IllegalArgumentException("UDL property must not be null");
        }

        if(property.length() > 30) {
            throw new IllegalArgumentException("UDL property string too large to include in a UDL");
        }

        this.value = null;
        this.property = property;
        this.sequenceNumber = sequenceNumber;
        type = UDLEntryType.NULL;
    }

    public void setString(String value) {
        if(value != null && value.length() > 64) {
            throw new IllegalArgumentException("String too large to include as a UDL value");
        }

        type = UDLEntryType.STRING;
        this.value = value;
    }

    public void setDate(Date value) {
        type = UDLEntryType.DATE;
        this.value = value;
    }

    public void setNumeric(BigDecimal value) {
        type = UDLEntryType.NUMERIC;
        this.value = value;
    }

    public String getString() {
        return type == UDLEntryType.STRING ? (String) value : null;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDate() {
        return type == UDLEntryType.DATE ? (Date) value : null;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getNumeric() {
        return type == UDLEntryType.NUMERIC ? (BigDecimal) value : null;
    }

    public BigInteger getSequenceNumber() {
        return sequenceNumber;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setSequenceNumber(BigInteger sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        String initialString = "UDLEntry[property=" + property + ", sequenceNumber=" + sequenceNumber + ", value=";

        if(type == UDLEntryType.NULL) {
            return initialString + "null]";
        }

        if(type == UDLEntryType.STRING) {
            return initialString + "\"" + value + "\"]";
        }

        return initialString + value + "]";
    }
}
