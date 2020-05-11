package ch.cern.eam.wshub.core.services.safety.entities;

import ch.cern.eam.wshub.core.annotations.InforField;

import java.math.BigDecimal;
import java.math.BigInteger;

public class EntitySafetyWSHub {
    @InforField(xpath = "ENTITY")
    private String entity;

    @InforField(xpath = "ENTITYSAFETYCODE")
    private String entitysafetycode;

    @InforField(xpath = "HAZARDID/HAZARDCODE")
    private String hazardid;

    @InforField(xpath = "PRECAUTIONID/PRECAUTIONCODE")
    private String precautionid;

    @InforField(xpath = "HAZARDID/REVISIONNUM")
    private BigDecimal hazardrevision;

    @InforField(xpath = "PRECAUTIONID/REVISIONNUM")
    private BigDecimal precautionrevision;

    @InforField(xpath = "SAFETYCODE")
    private String safetycode;

    @InforField(xpath = "DELETEPENDING")
    private String deletepending;

    public void setEntitySafetyCode(String entitysafetycode) {
        this.entitysafetycode = entitysafetycode;
    }

    public void setHazardID(String hazardid) {
        this.hazardid = hazardid;
    }

    public void setPrecautionID(String precautionid) {
        this.precautionid = precautionid;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setHazardRevision(BigDecimal hazardrevision) {
        this.hazardrevision = hazardrevision;
    }

    public void setPrecautionRevision(BigDecimal precautionrevision) { this.precautionrevision = precautionrevision; }

    public void setSafetyCode(String safetycode) { this.safetycode = safetycode; }

    public String getEntity() {
        return entity;
    }

    public BigDecimal getHazardRevision() {
        return hazardrevision;
    }

    public BigDecimal getPrecautionRevision() {
        return precautionrevision;
    }

    public String getEntitySafetyCode() { return entitysafetycode; }

    public String getHazardID() { return hazardid; }

    public String getPrecautionID() { return precautionid; }

    public String getSafetyCode() { return safetycode; }

    public String getDeletePending() {
        return deletepending;
    }

    public void setDeletePending(String deletepending) {
        this.deletepending = deletepending;
    }
}
