package ch.cern.eam.wshub.core.services.entities;

public class EntityOrganizationCodePair {
    private String entityCode;
    private String organizationCode;

    public EntityOrganizationCodePair() {
    }

    public EntityOrganizationCodePair(String entityCode) {
        this.entityCode = entityCode;
    }

    public EntityOrganizationCodePair(String entityCode, String organizationCode) {
        this.entityCode = entityCode;
        this.organizationCode = organizationCode;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }
}
