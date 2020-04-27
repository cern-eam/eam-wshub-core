package ch.cern.eam.wshub.core.services.entities;

import ch.cern.eam.wshub.core.annotations.GridField;

public class Department {

    @GridField(name = "departmentcode")
    private String code;

    @GridField(name = "deptdescription")
    private String description;

    @GridField(name = "deptreadonly")
    private Boolean readOnly;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }
}
