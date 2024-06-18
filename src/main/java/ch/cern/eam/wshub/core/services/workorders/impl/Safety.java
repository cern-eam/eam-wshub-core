package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

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

    @GridField(name = "sourcecode")
    private String sourceCode;

    @GridField(name = "sourceentity")
    private String sourceEntity;

    @GridField(name = "sourceorg")
    private String sourceOrg;

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

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceEntity() {
        return sourceEntity;
    }

    public void setSourceEntity(String sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public String getSourceOrg() {
        return sourceOrg;
    }

    public void setSourceOrg(String sourceOrg) {
        this.sourceOrg = sourceOrg;
    }

    @XmlTransient
    @JsonIgnore
    public boolean getReadOnly() {
        return sourceEntity != null && !"".equals(sourceEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Safety safety = (Safety) o;
        return Objects.equals(id, safety.id)
                && Objects.equals(hazardCode, safety.hazardCode)
                && Objects.equals(hazardDescription, safety.hazardDescription)
                && Objects.equals(hazardRevision, safety.hazardRevision)
                && Objects.equals(precautionCode, safety.precautionCode)
                && Objects.equals(precautionDescription, safety.precautionDescription)
                && Objects.equals(precautionRevision, safety.precautionRevision)
                && Objects.equals(userDefinedFields, safety.userDefinedFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hazardCode, hazardDescription, hazardRevision, precautionCode, precautionDescription, precautionRevision, userDefinedFields);
    }

    public static boolean canBeChangedBy(Safety original, Safety modification) {
        if (original == null
                || modification == null
                || !Objects.equals(original.getId(), modification.getId())
                || modification.getUserDefinedFields() == null) {
            return false;
        }

        return UserDefinedFields.canBeChangedBy(original.getUserDefinedFields(), modification.getUserDefinedFields());
    }
}
