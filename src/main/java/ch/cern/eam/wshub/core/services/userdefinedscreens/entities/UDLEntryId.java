package ch.cern.eam.wshub.core.services.userdefinedscreens.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.math.BigInteger;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class UDLEntryId implements Serializable {
    private static final long serialVersionUID = -4357516470357622070L;

    private EntityId entityId;
    private String property;
    private BigInteger sequenceNumber;

    public UDLEntryId(EntityId entityId) {
        this.entityId = entityId;
    }

    public UDLEntryId(EntityId entityId, String property) {
        this(entityId);

        if(property == null) {
            throw new IllegalArgumentException("Invalid UDLProperty arguments: property");
        }

        this.property = property;
    }

    public UDLEntryId(EntityId entityId, String property, BigInteger sequenceNumber) {
        this(entityId, property);

        if(sequenceNumber == null) {
            throw new IllegalArgumentException("Invalid UDLProperty arguments: sequenceNumber");
        }

        this.sequenceNumber = sequenceNumber;
    }

    public EntityId getEntityId() {
        return entityId;
    }

    public void setEntityId(EntityId entityId) {
        this.entityId = entityId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public BigInteger getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(BigInteger sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return "UDLEntryId["
            + "entityId=" + entityId
            + (property == null ? "" : ", property=" + property)
            + (sequenceNumber == null ? "" : ", sequenceNumber=" + sequenceNumber)
            + "]";
    }
}
