package ch.cern.eam.wshub.core.services.entities;

import ch.cern.eam.wshub.core.annotations.GridField;

public class Department {

    @GridField(name = "departmentcode")
    public String code;

    @GridField(name = "deptdescription")
    public String description;

    @GridField(name = "deptreadonly")
    public Boolean readOnly;

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
