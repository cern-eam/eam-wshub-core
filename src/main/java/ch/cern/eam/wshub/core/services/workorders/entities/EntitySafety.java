package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.InforField;

import java.math.BigDecimal;

public class EntitySafety {
    @InforField(xpath = "ENTITY")
    private String entity;

    @InforField(xpath = "ENTITYSAFETYCODE")
    private String entitySafetyCode;

    @InforField(xpath = "HAZARDID/HAZARDCODE")
    private String hazardID;

    @InforField(xpath = "PRECAUTIONID/PRECAUTIONCODE")
    private String precautionID;

    @InforField(xpath = "HAZARDID/REVISIONNUM")
    private BigDecimal hazardRevision;

    @InforField(xpath = "PRECAUTIONID/REVISIONNUM")
    private BigDecimal precautionRevision;

    @InforField(xpath = "SAFETYCODE")
    private String safetyCode;

    @InforField(xpath = "DELETEPENDING")
    private String deletePending;

    @InforField(xpath = "TIMINGID/USERDEFINEDCODE")
    private String timingIDCode;

    @InforField(xpath = "APPLYTOCHILDREN")
    private String applyToChildren;


    public EntitySafety() {};

    // Shallow copy constructor
    public EntitySafety(EntitySafety other) {
        this.setEntitySafetyCode(other.getEntitySafetyCode());
        this.setEntity(other.getEntity());
        this.setDeletePending(other.getDeletePending());
        this.setApplyToChildren(other.getApplyToChildren());
        this.setHazardID(other.getHazardID());
        this.setHazardRevision(other.getHazardRevision());
        this.setPrecautionID(other.getPrecautionID());
        this.setPrecautionRevision(other.getPrecautionRevision());
        this.setTimingID(other.getTimingID());
        this.setSafetyCode(other.getSafetyCode());
    }

    public void setEntitySafetyCode(String entitySafetyCode) { this.entitySafetyCode = entitySafetyCode; }
    public void setHazardID(String hazardID) {
        this.hazardID = hazardID;
    }
    public void setPrecautionID(String precautionID) {
        this.precautionID = precautionID;
    }
    public void setEntity(String entity) {
        this.entity = entity;
    }
    public void setHazardRevision(BigDecimal hazardRevision) {
        this.hazardRevision = hazardRevision;
    }
    public void setPrecautionRevision(BigDecimal precautionRevision) { this.precautionRevision = precautionRevision; }
    public void setSafetyCode(String safetyCode) { this.safetyCode = safetyCode; }
    public void setDeletePending(String deletePending) { this.deletePending = deletePending; }
    public void setTimingID(String timingIDCode) { this.timingIDCode = timingIDCode; }
    public void setApplyToChildren(String applyToChildren) { this.applyToChildren = applyToChildren; }

    public String getEntity() {
        return entity;
    }
    public BigDecimal getHazardRevision() {
        return hazardRevision;
    }
    public BigDecimal getPrecautionRevision() {
        return precautionRevision;
    }
    public String getEntitySafetyCode() { return entitySafetyCode; }
    public String getHazardID() { return hazardID; }
    public String getPrecautionID() { return precautionID; }
    public String getSafetyCode() { return safetyCode; }
    public String getDeletePending() { return deletePending; }
    public String getTimingID() { return timingIDCode; }
    public String getApplyToChildren() { return applyToChildren; }
}
