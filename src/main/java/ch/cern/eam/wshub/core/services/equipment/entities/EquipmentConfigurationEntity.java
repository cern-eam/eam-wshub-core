package ch.cern.eam.wshub.core.services.equipment.entities;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class EquipmentConfigurationEntity implements Serializable {

    @Transient
    private String equipmentConfigStatusCode;
    @Transient
    private String equipmentConfigStatusDesc;
    @Transient
    private String configurationDepartmentCode;
    @Transient
    private String configurationDepartmentDesc;
    @Transient
    private String configurationClassCode;
    @Transient
    private String configurationClassDesc;
    @Transient
    private String configurationCategoryCode;
    @Transient
    private String configurationCategoryDesc;
    @Transient
    private BigDecimal revisionNum;
    @Transient
    private String equipmentPrefix;
    @Transient
    private String equipmentSuffix;
    @Transient
    private Date dateCreated;
    @Transient
    private Date dateUpdated;
    @Transient
    private String createdBy;
    @Transient
    private String updatedBy;
    @Transient
    private String equipmentStatusCode;
    @Transient
    private String equipmentStatusDesc;
    @Transient
    private String createAsSpecific;
    @Transient
    private String equipmentType;
    @Transient
    private Long sequenceLength;
    @Transient
    private String sampleCode;
    @Transient
    private String commissioningWONum;
    @Transient
    private String commissioningWODesc;
    @Transient
    private String autoNumber;
    @Transient
    private String equipmentConfigCode;
    @Transient
    private String equipmentConfigDesc;
    @Transient
    private String organizationCode;
    @Transient
    private String organizationDesc;

    public String getEquipmentConfigStatusCode() {
        return equipmentConfigStatusCode;
    }

    public void setEquipmentConfigStatusCode(String equipmentConfigStatusCode) {
        this.equipmentConfigStatusCode = equipmentConfigStatusCode;
    }

    public String getEquipmentConfigStatusDesc() {
        return equipmentConfigStatusDesc;
    }

    public void setEquipmentConfigStatusDesc(String equipmentConfigStatusDesc) {
        this.equipmentConfigStatusDesc = equipmentConfigStatusDesc;
    }

    public String getConfigurationDepartmentCode() {
        return configurationDepartmentCode;
    }

    public void setConfigurationDepartmentCode(String configurationDepartmentCode) {
        this.configurationDepartmentCode = configurationDepartmentCode;
    }

    public String getConfigurationDepartmentDesc() {
        return configurationDepartmentDesc;
    }

    public void setConfigurationDepartmentDesc(String configurationDepartmentDesc) {
        this.configurationDepartmentDesc = configurationDepartmentDesc;
    }

    public String getConfigurationClassCode() {
        return configurationClassCode;
    }

    public void setConfigurationClassCode(String configurationClassCode) {
        this.configurationClassCode = configurationClassCode;
    }

    public String getConfigurationClassDesc() {
        return configurationClassDesc;
    }

    public void setConfigurationClassDesc(String configurationClassDesc) {
        this.configurationClassDesc = configurationClassDesc;
    }

    public String getEquipmentPrefix() {
        return equipmentPrefix;
    }

    public void setEquipmentPrefix(String equipmentPrefix) {
        this.equipmentPrefix = equipmentPrefix;
    }

    public String getEquipmentSuffix() {
        return equipmentSuffix;
    }

    public void setEquipmentSuffix(String equipmentSuffix) {
        this.equipmentSuffix = equipmentSuffix;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getEquipmentStatusCode() {
        return equipmentStatusCode;
    }

    public void setEquipmentStatusCode(String equipmentStatusCode) {
        this.equipmentStatusCode = equipmentStatusCode;
    }

    public String getEquipmentStatusDesc() {
        return equipmentStatusDesc;
    }

    public void setEquipmentStatusDesc(String equipmentStatusDesc) {
        this.equipmentStatusDesc = equipmentStatusDesc;
    }

    public String getCreateAsSpecific() {
        return createAsSpecific;
    }

    public void setCreateAsSpecific(String createAsSpecific) {
        this.createAsSpecific = createAsSpecific;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Long getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(Long sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getCommissioningWONum() {
        return commissioningWONum;
    }

    public void setCommissioningWONum(String commissioningWONum) {
        this.commissioningWONum = commissioningWONum;
    }

    public String getCommissioningWODesc() {
        return commissioningWODesc;
    }

    public void setCommissioningWODesc(String commissioningWODesc) {
        this.commissioningWODesc = commissioningWODesc;
    }

    public String getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        this.autoNumber = autoNumber;
    }

    public String getEquipmentConfigCode() {
        return equipmentConfigCode;
    }

    public void setEquipmentConfigCode(String equipmentConfigCode) {
        this.equipmentConfigCode = equipmentConfigCode;
    }

    public String getEquipmentConfigDesc() {
        return equipmentConfigDesc;
    }

    public void setEquipmentConfigDesc(String equipmentConfigDesc) {
        this.equipmentConfigDesc = equipmentConfigDesc;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public BigDecimal getRevisionNum() {
        return revisionNum;
    }

    public void setRevisionNum(BigDecimal revisionNum) {
        this.revisionNum = revisionNum;
    }

    public String getConfigurationCategoryCode() {
        return configurationCategoryCode;
    }

    public void setConfigurationCategoryCode(String configurationCategoryCode) {
        this.configurationCategoryCode = configurationCategoryCode;
    }

    public String getConfigurationCategoryDesc() {
        return configurationCategoryDesc;
    }

    public void setConfigurationCategoryDesc(String configurationCategoryDesc) {
        this.configurationCategoryDesc = configurationCategoryDesc;
    }

    @Override
    public String toString() {
        return "EquipmentConfigurationEntity{" +
                "equipmentConfigStatusCode='" + equipmentConfigStatusCode + '\'' +
                ", equipmentConfigStatusDesc='" + equipmentConfigStatusDesc + '\'' +
                ", configurationDepartmentCode='" + configurationDepartmentCode + '\'' +
                ", configurationDepartmentDesc='" + configurationDepartmentDesc + '\'' +
                ", configurationClassCode='" + configurationClassCode + '\'' +
                ", configurationClassDesc='" + configurationClassDesc + '\'' +
                ", configurationCategoryCode='" + configurationCategoryCode + '\'' +
                ", configurationCategoryDesc='" + configurationCategoryDesc + '\'' +
                ", revisionNum='" + revisionNum + '\'' +
                ", equipmentPrefix='" + equipmentPrefix + '\'' +
                ", equipmentSuffix='" + equipmentSuffix + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", equipmentStatusCode='" + equipmentStatusCode + '\'' +
                ", equipmentStatusDesc='" + equipmentStatusDesc + '\'' +
                ", createAsSpecific='" + createAsSpecific + '\'' +
                ", equipmentType='" + equipmentType + '\'' +
                ", sequenceLength=" + sequenceLength +
                ", sampleCode='" + sampleCode + '\'' +
                ", commissioningWONum='" + commissioningWONum + '\'' +
                ", commissioningWODesc='" + commissioningWODesc + '\'' +
                ", autoNumber='" + autoNumber + '\'' +
                ", equipmentConfigCode='" + equipmentConfigCode + '\'' +
                ", equipmentConfigDesc='" + equipmentConfigDesc + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", organizationDesc='" + organizationDesc + '\'' +
                '}';
    }
}
