package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

@Entity
public class EquipmentConfigurationEntity implements Serializable {

    @Transient
    @EAMField(xpath = "EQUIPMENTCONFIGURATIONID/EQUIPMENTCONFIGURATIONCODE")
    private String equipmentConfigCode;
    @Transient
    @EAMField(xpath = "EQUIPMENTCONFIGURATIONID/DESCRIPTION")
    private String equipmentConfigDesc;

    @Transient
    @EAMField(xpath = "EQUIPMENTCONFIGURATIONID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organizationCode;

    @Transient
    @EAMField(xpath = "EQUIPMENTCONFIGURATIONID/ORGANIZATIONID/DESCRIPTION", readOnly = true)
    private String organizationDesc;

    @Transient
    @EAMField(xpath = "EQUIPMENTCONFIGSTATUS/STATUSCODE")
    private String equipmentConfigStatusCode;

    @Transient
    @EAMField(xpath = "EQUIPMENTCONFIGSTATUS/DESCRIPTION", readOnly = true)
    private String equipmentConfigStatusDesc;

    @Transient
    @EAMField(xpath = "DEPARTMENTID/DEPARTMENTCODE", nullifyParentLevel = 1)
    private String configurationDepartmentCode;

    @Transient
    @EAMField(xpath = "DEPARTMENTID/DESCRIPTION", nullifyParentLevel = 0)
    private String configurationDepartmentDesc;

    @Transient
    @EAMField(xpath = "EQUIPMENTCONFIGURATIONID/REVISIONNUM")
    private BigDecimal revisionNum;

    @Transient
    @EAMField(xpath = "TYPE/TYPECODE")
    private String equipmentTypeCode;

    @Transient
    @EAMField(xpath = "TYPE/DESCRIPTION")
    private String equipmentTypeDesc;

    @Transient
    @EAMField(xpath = "USERDEFINEDAREA")
    private CustomField[] customFields;


    // Configuration Details
    @Transient
    @EAMField(xpath = "ConfigurationDetails/CLASSID/CLASSCODE")
    private String configurationClassCode;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/CLASSID/DESCRIPTION", readOnly = true)
    private String configurationClassDesc;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/CATEGORYID/CATEGORYCODE")
    private String configurationCategoryCode;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/CATEGORYID/DESCRIPTION", readOnly = true)
    private String configurationCategoryDesc;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/CREATEDDATE")
    private Date dateCreated;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/DATEUPDATED")
    private Date dateUpdated;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/CREATEDBY/USERCODE")
    private String createdBy;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/UPDATEDBY/USERCODE")
    private String updatedBy;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/CLASSID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String classOrganizationCode;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/CLASSID/ORGANIZATIONID/DESCRIPTION")
    private String getClassOrganizationDesc;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/COSTCODEID/COSTCODE")
    private String costCode;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/COSTCODEID/DESCRIPTION")
    private String costCodeDesc;

    @Transient
    @EAMField(xpath = "ConfigurationDetails/ASSETVALUE")
    private BigDecimal equipmentValue;


    // Equipment Generation Details
    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/EQUIPMENTPREFIX")
    private String equipmentPrefix;

    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/EQUIPMENTSUFFIX")
    private String equipmentSuffix;

    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/CREATEASSPECIFIC")
    private Boolean createAsSpecific;

    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/STATUS/STATUSCODE")
    private String equipmentStatusCode;

    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/STATUS/DESCRIPTION", readOnly = true)
    private String equipmentStatusDesc;

    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/SEQUENCELENGTH")
    private BigInteger equipmentSequenceLength;

    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/AUTONUMBER")
    private Boolean autoNumber;

    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/SAMPLECODE")
    private String sampleCode;

    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/COMMISSIONINGWORKORDERID/JOBNUM")
    private String commissioningWONum;

    @Transient
    @EAMField(xpath = "EquipmentGenerationDetails/COMMISSIONINGWORKORDERID/DESCRIPTION", readOnly = true)
    private String commissioningWODesc;


    // Tracking Details
    @Transient
    @EAMField(xpath = "TrackingDetails/MANUFACTURERCODE")
    private String manufacturerCode;

    @Transient
    @EAMField(xpath = "TrackingDetails/MODEL")
    private String model;

    @Transient
    @EAMField(xpath = "TrackingDetails/MODELREVISION")
    private String modelRevision;

    @Transient
    @EAMField(xpath = "PARTID/PARTCODE")
    private String partCode;

    @Transient
    @EAMField(xpath = "UserDefinedFields")
    private UserDefinedFields userDefinedFields;

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

    public BigDecimal getRevisionNum() {
        return revisionNum;
    }

    public void setRevisionNum(BigDecimal revisionNum) {
        this.revisionNum = revisionNum;
    }

    public String getEquipmentTypeCode() {
        return equipmentTypeCode;
    }

    public void setEquipmentTypeCode(String equipmentTypeCode) {
        this.equipmentTypeCode = equipmentTypeCode;
    }

    public String getEquipmentTypeDesc() {
        return equipmentTypeDesc;
    }

    public void setEquipmentTypeDesc(String equipmentTypeDesc) {
        this.equipmentTypeDesc = equipmentTypeDesc;
    }

    public CustomField[] getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomField[] customFields) {
        this.customFields = customFields;
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

    public String getClassOrganizationCode() {
        return classOrganizationCode;
    }

    public void setClassOrganizationCode(String classOrganizationCode) {
        this.classOrganizationCode = classOrganizationCode;
    }

    public String getGetClassOrganizationDesc() {
        return getClassOrganizationDesc;
    }

    public void setGetClassOrganizationDesc(String getClassOrganizationDesc) {
        this.getClassOrganizationDesc = getClassOrganizationDesc;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getCostCodeDesc() {
        return costCodeDesc;
    }

    public void setCostCodeDesc(String costCodeDesc) {
        this.costCodeDesc = costCodeDesc;
    }

    public BigDecimal getEquipmentValue() {
        return equipmentValue;
    }

    public void setEquipmentValue(BigDecimal equipmentValue) {
        this.equipmentValue = equipmentValue;
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

    public Boolean getCreateAsSpecific() {
        return createAsSpecific;
    }

    public void setCreateAsSpecific(Boolean createAsSpecific) {
        this.createAsSpecific = createAsSpecific;
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

    public BigInteger getEquipmentSequenceLength() {
        return equipmentSequenceLength;
    }

    public void setEquipmentSequenceLength(BigInteger equipmentSequenceLength) {
        this.equipmentSequenceLength = equipmentSequenceLength;
    }

    public Boolean getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(Boolean autoNumber) {
        this.autoNumber = autoNumber;
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

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelRevision() {
        return modelRevision;
    }

    public void setModelRevision(String modelRevision) {
        this.modelRevision = modelRevision;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }

    @Override
    public String toString() {
        return "EquipmentConfigurationEntity{" +
                "equipmentConfigCode='" + equipmentConfigCode + '\'' +
                ", equipmentConfigDesc='" + equipmentConfigDesc + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", organizationDesc='" + organizationDesc + '\'' +
                ", equipmentConfigStatusCode='" + equipmentConfigStatusCode + '\'' +
                ", equipmentConfigStatusDesc='" + equipmentConfigStatusDesc + '\'' +
                ", configurationDepartmentCode='" + configurationDepartmentCode + '\'' +
                ", configurationDepartmentDesc='" + configurationDepartmentDesc + '\'' +
                ", revisionNum=" + revisionNum +
                ", equipmentTypeCode='" + equipmentTypeCode + '\'' +
                ", equipmentTypeDesc='" + equipmentTypeDesc + '\'' +
                ", customFields=" + Arrays.toString(customFields) +
                ", configurationClassCode='" + configurationClassCode + '\'' +
                ", configurationClassDesc='" + configurationClassDesc + '\'' +
                ", configurationCategoryCode='" + configurationCategoryCode + '\'' +
                ", configurationCategoryDesc='" + configurationCategoryDesc + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", classOrganizationCode='" + classOrganizationCode + '\'' +
                ", getClassOrganizationDesc='" + getClassOrganizationDesc + '\'' +
                ", costCode='" + costCode + '\'' +
                ", costCodeDesc='" + costCodeDesc + '\'' +
                ", equipmentValue=" + equipmentValue +
                ", equipmentPrefix='" + equipmentPrefix + '\'' +
                ", equipmentSuffix='" + equipmentSuffix + '\'' +
                ", createAsSpecific=" + createAsSpecific +
                ", equipmentStatusCode='" + equipmentStatusCode + '\'' +
                ", equipmentStatusDesc='" + equipmentStatusDesc + '\'' +
                ", equipmentSequenceLength=" + equipmentSequenceLength +
                ", autoNumber=" + autoNumber +
                ", sampleCode='" + sampleCode + '\'' +
                ", commissioningWONum='" + commissioningWONum + '\'' +
                ", commissioningWODesc='" + commissioningWODesc + '\'' +
                ", manufacturerCode='" + manufacturerCode + '\'' +
                ", model='" + model + '\'' +
                ", modelRevision='" + modelRevision + '\'' +
                ", partCode='" + partCode + '\'' +
                ", userDefinedFields=" + userDefinedFields +
                '}';
    }
}
