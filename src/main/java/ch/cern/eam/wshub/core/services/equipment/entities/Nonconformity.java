package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.InforField;

public class Nonconformity {
    // Main identifying fields
    @InforField(xpath = "NONCONFORMITYID/STANDARDENTITYCODE")
    private String code;

    @InforField(xpath = "NONCONFORMITYID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organizationCode;

    // Nonconformity Details
    @InforField(xpath = "NONCONFORMITYID/DESCRIPTION")
    private String description;

    @InforField(xpath = "EQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;

    @InforField(xpath = "DEPARTMENTID/DEPARTMENTCODE")
    private String departmentCode;

    @InforField(xpath = "STATUS/STATUSCODE")
    private String statusCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
