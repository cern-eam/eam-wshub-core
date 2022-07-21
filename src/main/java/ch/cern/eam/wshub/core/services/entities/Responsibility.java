package ch.cern.eam.wshub.core.services.entities;

import ch.cern.eam.wshub.core.annotations.GridField;

public class Responsibility {

    @GridField(name = "responsibilitycode")
    private String code;

    @GridField(name = "description")
    private String description;

    @GridField(name = "rresponsibilitydesc")
    private String typeDesc;

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getTypeDesc() { return typeDesc; }

    public void setTypeDesc(String typeDesc) { this.typeDesc = typeDesc; }

    @Override
    public String toString() {
        return "[code=" + code + ", description=" + description + ", typeDesc=" + typeDesc + "]";
    }
}
