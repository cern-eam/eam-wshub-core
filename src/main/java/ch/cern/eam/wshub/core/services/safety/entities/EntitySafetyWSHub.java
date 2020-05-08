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


    public void setENTITYSAFETYCODE(String entitysafetycode) {
        this.entitysafetycode = entitysafetycode;
    }

    public void setHAZARDID(String hazardid) {
        this.hazardid = hazardid;
    }

    public void setPRECAUTIONID(String precautionid) {
        this.precautionid = precautionid;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public BigDecimal getHazardrevision() {
        return hazardrevision;
    }

    public void setHazardrevision(BigDecimal hazardrevision) {
        this.hazardrevision = hazardrevision;
    }

    public BigDecimal getPrecautionrevision() {
        return precautionrevision;
    }

    public void setPrecautionrevision(BigDecimal precautionrevision) {
        this.precautionrevision = precautionrevision;
    }
}
