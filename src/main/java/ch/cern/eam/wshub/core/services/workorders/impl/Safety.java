package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import java.io.Serializable;
import java.math.BigDecimal;

public class Safety implements Serializable {
    private static final long serialVersionUID = -4988635355683432596L;

    @InforField(xpath = "SAFETYCODE")
    @GridField(name = "entitysafetyid", alternativeNames = { "worksafety" })
    private String id;

    @InforField(xpath = "HAZARDID/HAZARDCODE")
    @GridField(name = "hazardcode", alternativeNames = { "hazard" })
    private String hazardCode;

    @InforField(xpath = "HAZARDID/DESCRIPTION")
    @GridField(name = "hazarddescription", alternativeNames = { "hazarddesc" })
    private String hazardDescription;

    @InforField(xpath = "HAZARDID/REVISIONNUM")
    private BigDecimal hazardRevision;

    @InforField(xpath = "PRECAUTIONID/PRECAUTIONCODE")
    @GridField(name = "precaution")
    private String precautionCode;

    @InforField(xpath = "PRECAUTIONID/DESCRIPTION")
    @GridField(name = "precautiondescription", alternativeNames = { "precautiondesc" })
    private String precautionDescription;

    @InforField(xpath = "PRECAUTIONID/REVISIONNUM")
    private BigDecimal precautionRevision;

    @InforField(xpath = "StandardUserDefinedFields")
    @GridField(name = "")
    private UserDefinedFields userDefinedFields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHazardCode() {
        return hazardCode;
    }

    public void setHazardCode(String hazardCode) {
        this.hazardCode = hazardCode;
    }

    public String getHazardDescription() {
        return hazardDescription;
    }

    public void setHazardDescription(String hazardDescription) {
        this.hazardDescription = hazardDescription;
    }

    public BigDecimal getHazardRevision() {
        return hazardRevision;
    }

    public void setHazardRevision(BigDecimal hazardRevision) {
        this.hazardRevision = hazardRevision;
    }

    public String getPrecautionCode() {
        return precautionCode;
    }

    public void setPrecautionCode(String precautionCode) {
        this.precautionCode = precautionCode;
    }

    public String getPrecautionDescription() {
        return precautionDescription;
    }

    public void setPrecautionDescription(String precautionDescription) {
        this.precautionDescription = precautionDescription;
    }

    public BigDecimal getPrecautionRevision() {
        return precautionRevision;
    }

    public void setPrecautionRevision(BigDecimal precautionRevision) {
        this.precautionRevision = precautionRevision;
    }

    public UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }

        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }

        Safety other = (Safety) otherObject;

        return getId() != null && getId().equals(other.getId())
                && getHazardCode() != null && getHazardCode().equals(other.getHazardCode())
                && getPrecautionCode() != null && getPrecautionCode().equals(other.getPrecautionCode())

                // It is not possible to get the hazard revision/precaution revision using the grid OSOBJA_ESF
                // && getHazardRevision() != null && getHazardRevision().equals(other.getHazardRevision())
                // && getPrecautionRevision() != null && getPrecautionRevision().equals(other.getPrecautionRevision())

                && userDefinedFields != null && userDefinedFields.equals(other.getUserDefinedFields());
    }
}
