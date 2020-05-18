package ch.cern.eam.wshub.core.services.userdefinedscreens.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.function.UnaryOperator;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class UDLProperty implements Serializable {
    private static final long serialVersionUID = -4357516470357622070L;

    private String entityType;
    private String code;
    private String property;
    private BigInteger sequenceNumber;

    public UDLProperty() {
        // to turn this into a Java Bean, avoid using in actual code
    }

    public UDLProperty(String entityType, String code) {
        if(entityType == null || code == null) {
            throw new IllegalArgumentException("Invalid UDLProperty arguments: entityType/code");
        }

        this.entityType = entityType;
        this.code = code;
    }

    public UDLProperty(String entityType, String code, String property) {
        this(entityType, code);

        if(property == null) {
            throw new IllegalArgumentException("Invalid UDLProperty arguments: property");
        }

        this.property = property;
    }

    public UDLProperty(String entityType, String code, String property, BigInteger sequenceNumber) {
        this(entityType, code, property);

        if(sequenceNumber == null) {
            throw new IllegalArgumentException("Invalid UDLProperty arguments: sequenceNumber");
        }

        this.sequenceNumber = sequenceNumber;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return "UDLProperty["
            + "entityType=" + entityType + ", "
            + "code=" + code
            + (property == null ? "" : ", property=" + property)
            + (sequenceNumber == null ? "" : ", sequenceNumber=" + sequenceNumber)
            + "]";
    }
}
