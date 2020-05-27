package ch.cern.eam.wshub.core.services.userdefinedscreens.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class EntityId implements Serializable {
    private static final long serialVersionUID = -3645911522233123342L;

    private String entityType;
    private String entityCode;
    
    public EntityId(String entityType, String entityCode) {
        if(entityType == null || entityCode == null) {
            throw new IllegalArgumentException("Invalid EntityId arguments: entityType/code");
        }

        this.entityType = entityType;
        this.entityCode = entityCode;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    @Override
    public String toString() {
        return "EntityId["
                + "entityType=" + entityType + ", "
                + "entityCode=" + entityCode
                + "]";
    }
}
