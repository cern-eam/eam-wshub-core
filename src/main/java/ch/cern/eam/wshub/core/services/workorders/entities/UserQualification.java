package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.InforField;

public class UserQualification {
    @InforField(xpath = "ENTITY")
    String entity;

    @InforField(xpath = "USERDEFINEDCODE")
    String userDefinedCode;

    @InforField(xpath = "DESCRIPTION")
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
