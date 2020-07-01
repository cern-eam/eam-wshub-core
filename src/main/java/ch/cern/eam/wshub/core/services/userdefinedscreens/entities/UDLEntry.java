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

    private UDLEntryId entryId;
    private UDLValue value;

    public UDLEntry(UDLEntryId entryId, UDLValue value) {
        this.entryId = entryId;
        this.value = value == null ? new UDLValue() : value;
    }

    public UDLEntry(UDLEntryId entryId) {
        this(entryId, null);
    }

    @Override
    public String toString() {
        return "UDLEntry[entryId=" + entryId + ", value=" + value + "]";
    }

    public UDLEntryId getEntryId() {
        return entryId;
    }

    public void setEntryId(UDLEntryId entryId) {
        this.entryId = entryId;
    }

    public UDLValue getValue() {
        return value;
    }

    public void setValue(UDLValue value) {
        this.value = value;
    }
}
