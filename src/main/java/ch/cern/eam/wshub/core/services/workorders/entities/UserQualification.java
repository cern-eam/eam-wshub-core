package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;

public class UserQualification {
    @EAMField(xpath = "ENTITY")
    String entity;

    @EAMField(xpath = "USERDEFINEDCODE")
    String userDefinedCode;

    @EAMField(xpath = "DESCRIPTION")
    String description;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getUserDefinedCode() {
        return userDefinedCode;
    }

    public void setUserDefinedCode(String userDefinedCode) {
        this.userDefinedCode = userDefinedCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
