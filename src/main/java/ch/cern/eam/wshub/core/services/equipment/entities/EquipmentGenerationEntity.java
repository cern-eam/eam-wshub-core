package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class EquipmentGenerationEntity implements Serializable {

    @Transient
    private String equipmentGenerationCode;
    @Transient
    private String equipmentGenerationDesc;
    @Transient
    private String organizationCode;
    @Transient
    private String organizationDesc;
    @Transient
    private String description;
    @Transient
    private String equipmentConfigurationCode;
    @Transient
    private String equipmentConfigurationDesc;
    @Transient
    private String equipmentDepartmentCode;
    @Transient
    private String equipmentDepartmentDesc;
    @Transient
    private String equipmentLocationCode;
    @Transient
    private String equipmentLocationDesc;
    @Transient
    private String equipmentCostCode;
    @Transient
    private String equipmentCostCodeDesc;
    @Transient
    private String equipmentAssignedToCode;
    @Transient
    private String equipmentAssignedToDesc;
    @Transient
    private BigDecimal revisionNum;
    @Transient
    private Date lastUpdatedDate;
    @Transient
    private Date createdDate;
    @Transient
    private Date dateUpdated;
    @Transient
    private Date commissionDate;
    @Transient
    private String createdBy;
    @Transient
    private String updatedBy;
    @Transient
    private String equipmentStatusCode;
    @Transient
    private String equipmentStatusDesc;
    @Transient
    private String statusCode;
    @Transient
    private String statusDesc;
    @Transient
    private BigDecimal generateCount;
    @Transient
    private String processed;
    @Transient
    private String active;
    @Transient
    private String awaitingPurchase;
    @Transient
    private String processError;
    @Transient
    private String processRunning;
    @Transient
    private String allSpecific;
    @Transient
    private String setDueValues;
    @Transient
    private String activateMps;
    @Transient
    private String createCommissioningWO;
    @Transient
    private String topLevelOnly;
    @Transient
    private String allDependent;
    @Transient
    private String allCostRollup;
    @Transient
    private String copyComments;
    @Transient
    private String copyDocuments;
    @Transient
    private String copyCustomfields;
    @Transient
    private String copyDepreciation;
    @Transient
    private String copyMeters;
    @Transient
    private String copyPartsAssociated;
    @Transient
    private String copyWarranties;
    @Transient
    private String copyPMSchedules;
    @Transient
    private String copyMaintenancePatterns;
    @Transient
    private String copySafety;
    @Transient
    private String copyPermits;
    @Transient
    private String copyCalibration;
    @Transient
    private String copyTestPoints;
    @Transient
    private String commissioningWONumber;
    @Transient
    private String commissioningWODesc;
    @Transient
    private String commissioningWOCostCode;
    @Transient
    private String commissioningWOCostDesc;
    @Transient
    private String commissioningWOAssignedTo;
    @Transient
    private String commissioningWOStatusCode;
    @Transient
    private String commissioningWOStatusDesc;
    @Transient
    private String commissioningWOLocationCode;
    @Transient
    private String commissioningWOLocationDesc;
    @Transient
    private String commissioningWODepartmentCode;
    @Transient
    private String commissioningWODepartmentDesc;



    public String getEquipmentGenerationCode() {
        return equipmentGenerationCode;
    }

    public void setEquipmentGenerationCode(String equipmentGenerationCode) {
        this.equipmentGenerationCode = equipmentGenerationCode;
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

    public String getEquipmentConfigurationCode() {
        return equipmentConfigurationCode;
    }

    public void setEquipmentConfigurationCode(String equipmentConfigurationCode) {
        this.equipmentConfigurationCode = equipmentConfigurationCode;
    }
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Date getCommissionDate() {
        return commissionDate;
    }

    public void setCommissionDate(Date commissionDate) {
        this.commissionDate = commissionDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getEquipmentGenerationDesc() {
        return equipmentGenerationDesc;
    }

    public void setEquipmentGenerationDesc(String equipmentGenerationDesc) {
        this.equipmentGenerationDesc = equipmentGenerationDesc;
    }

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public String getEquipmentConfigurationDesc() {
        return equipmentConfigurationDesc;
    }

    public void setEquipmentConfigurationDesc(String equipmentConfigurationDesc) {
        this.equipmentConfigurationDesc = equipmentConfigurationDesc;
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

    public BigDecimal getRevisionNum() {
        return revisionNum;
    }

    public void setRevisionNum(BigDecimal revisionNum) {
        this.revisionNum = revisionNum;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public BigDecimal getGenerateCount() {
        return generateCount;
    }

    public void setGenerateCount(BigDecimal generateCount) {
        this.generateCount = generateCount;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void getStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAwaitingPurchase() {
        return awaitingPurchase;
    }

    public void setAwaitingPurchase(String awaitingPurchase) {
        this.awaitingPurchase = awaitingPurchase;
    }

    public String getProcessError() {
        return processError;
    }

    public void setProcessError(String processError) {
        this.processError = processError;
    }

    public String getProcessRunning() {
        return processRunning;
    }

    public void setProcessRunning(String processRunning) {
        this.processRunning = processRunning;
    }

    public String getAllSpecific() {
        return allSpecific;
    }

    public void setAllSpecific(String allSpecific) {
        this.allSpecific = allSpecific;
    }

    public String getSetDueValues() {
        return setDueValues;
    }

    public void setSetDueValues(String setDueValues) {
        this.setDueValues = setDueValues;
    }

    public String getActivateMps() {
        return activateMps;
    }

    public void setActivateMps(String activateMps) {
        this.activateMps = activateMps;
    }

    public String getCreateCommissioningWO() {
        return createCommissioningWO;
    }

    public void setCreateCommissioningWO(String createCommissioningWO) {
        this.createCommissioningWO = createCommissioningWO;
    }

    public String getTopLevelOnly() {
        return topLevelOnly;
    }

    public void setTopLevelOnly(String topLevelOnly) {
        this.topLevelOnly = topLevelOnly;
    }

    public String getAllDependent() {
        return allDependent;
    }

    public void setAllDependent(String allDependent) {
        this.allDependent = allDependent;
    }

    public String getAllCostRollup() {
        return allCostRollup;
    }

    public void setAllCostRollup(String allCostRollup) {
        this.allCostRollup = allCostRollup;
    }

    public String getCopyComments() {
        return copyComments;
    }

    public void setCopyComments(String copyComments) {
        this.copyComments = copyComments;
    }

    public String getCopyDocuments() {
        return copyDocuments;
    }

    public void setCopyDocuments(String copyDocuments) {
        this.copyDocuments = copyDocuments;
    }

    public String getCopyCustomfields() {
        return copyCustomfields;
    }

    public void setCopyCustomfields(String copyCustomfields) {
        this.copyCustomfields = copyCustomfields;
    }

    public String getCopyDepreciation() {
        return copyDepreciation;
    }

    public void setCopyDepreciation(String copyDepreciation) {
        this.copyDepreciation = copyDepreciation;
    }

    public String getCopyMeters() {
        return copyMeters;
    }

    public void setCopyMeters(String copyMeters) {
        this.copyMeters = copyMeters;
    }

    public String getCopyPartsAssociated() {
        return copyPartsAssociated;
    }

    public void setCopyPartsAssociated(String copyPartsAssociated) {
        this.copyPartsAssociated = copyPartsAssociated;
    }

    public String getCopyWarranties() {
        return copyWarranties;
    }

    public void setCopyWarranties(String copyWarranties) {
        this.copyWarranties = copyWarranties;
    }

    public String getCopyPMSchedules() {
        return copyPMSchedules;
    }

    public void setCopyPMSchedules(String copyPMSchedules) {
        this.copyPMSchedules = copyPMSchedules;
    }

    public String getCopyMaintenancePatterns() {
        return copyMaintenancePatterns;
    }

    public void setCopyMaintenancePatterns(String copyMaintenancePatterns) {
        this.copyMaintenancePatterns = copyMaintenancePatterns;
    }

    public String getCopySafety() {
        return copySafety;
    }

    public void setCopySafety(String copySafety) {
        this.copySafety = copySafety;
    }

    public String getCopyPermits() {
        return copyPermits;
    }

    public void setCopyPermits(String copyPermits) {
        this.copyPermits = copyPermits;
    }

    public String getCopyCalibration() {
        return copyCalibration;
    }

    public void setCopyCalibration(String copyCalibration) {
        this.copyCalibration = copyCalibration;
    }

    public String getCopyTestPoints() {
        return copyTestPoints;
    }

    public void setCopyTestPoints(String copyTestPoints) {
        this.copyTestPoints = copyTestPoints;
    }

    public String getCommissioningWONumber() {
        return commissioningWONumber;
    }

    public void setCommissioningWONumber(String commissioningWONumber) {
        this.commissioningWONumber = commissioningWONumber;
    }

    public String getCommissioningWODesc() {
        return commissioningWODesc;
    }

    public void setCommissioningWODesc(String commissioningWODesc) {
        this.commissioningWODesc = commissioningWODesc;
    }

    public String getCommissioningWOCostCode() {
        return commissioningWOCostCode;
    }

    public void setCommissioningWOCostCode(String commissioningWOCostCode) {
        this.commissioningWOCostCode = commissioningWOCostCode;
    }

    public String getCommissioningWOCostDesc() {
        return commissioningWOCostDesc;
    }

    public void setCommissioningWOCostDesc(String commissioningWOCostDesc) {
        this.commissioningWOCostDesc = commissioningWOCostDesc;
    }

    public String getCommissioningWOAssignedTo() {
        return commissioningWOAssignedTo;
    }

    public void setCommissioningWOAssignedTo(String commissioningWOAssignedTo) {
        this.commissioningWOAssignedTo = commissioningWOAssignedTo;
    }

    public String getCommissioningWOStatusCode() {
        return commissioningWOStatusCode;
    }

    public void setCommissioningWOStatusCode(String commissioningWOStatusCode) {
        this.commissioningWOStatusCode = commissioningWOStatusCode;
    }

    public String getCommissioningWOStatusDesc() {
        return commissioningWOStatusDesc;
    }

    public void setCommissioningWOStatusDesc(String commissioningWOStatusDesc) {
        this.commissioningWOStatusDesc = commissioningWOStatusDesc;
    }

    public String getCommissioningWOLocationCode() {
        return commissioningWOLocationCode;
    }

    public void setCommissioningWOLocationCode(String commissioningWOLocationCode) {
        this.commissioningWOLocationCode = commissioningWOLocationCode;
    }

    public String getCommissioningWOLocationDesc() {
        return commissioningWOLocationDesc;
    }

    public void setCommissioningWOLocationDesc(String commissioningWOLocationDesc) {
        this.commissioningWOLocationDesc = commissioningWOLocationDesc;
    }

    public String getCommissioningWODepartmentCode() {
        return commissioningWODepartmentCode;
    }

    public void setCommissioningWODepartmentCode(String commissioningWODepartmentCode) {
        this.commissioningWODepartmentCode = commissioningWODepartmentCode;
    }

    public String getCommissioningWODepartmentDesc() {
        return commissioningWODepartmentDesc;
    }

    public void setCommissioningWODepartmentDesc(String commissioningWODepartmentDesc) {
        this.commissioningWODepartmentDesc = commissioningWODepartmentDesc;
    }

    public String getEquipmentDepartmentCode() {
        return equipmentDepartmentCode;
    }

    public void setEquipmentDepartmentCode(String equipmentDepartmentCode) {
        this.equipmentDepartmentCode = equipmentDepartmentCode;
    }

    public String getEquipmentDepartmentDesc() {
        return equipmentDepartmentDesc;
    }

    public void setEquipmentDepartmentDesc(String equipmentDepartmentDesc) {
        this.equipmentDepartmentDesc = equipmentDepartmentDesc;
    }

    public String getEquipmentLocationCode() {
        return equipmentLocationCode;
    }

    public void setEquipmentLocationCode(String equipmentLocationCode) {
        this.equipmentLocationCode = equipmentLocationCode;
    }

    public String getEquipmentLocationDesc() {
        return equipmentLocationDesc;
    }

    public void setEquipmentLocationDesc(String equipmentLocationDesc) {
        this.equipmentLocationDesc = equipmentLocationDesc;
    }

    public String getEquipmentCostCode() {
        return equipmentCostCode;
    }

    public void setEquipmentCostCode(String equipmentCostCode) {
        this.equipmentCostCode = equipmentCostCode;
    }

    public String getEquipmentCostCodeDesc() {
        return equipmentCostCodeDesc;
    }

    public void setEquipmentCostCodeDesc(String equipmentCostCodeDesc) {
        this.equipmentCostCodeDesc = equipmentCostCodeDesc;
    }

    public String getEquipmentAssignedToCode() {
        return equipmentAssignedToCode;
    }

    public void setEquipmentAssignedToCode(String equipmentAssignedToCode) {
        this.equipmentAssignedToCode = equipmentAssignedToCode;
    }

    public String getEquipmentAssignedToDesc() {
        return equipmentAssignedToDesc;
    }

    public void setEquipmentAssignedToDesc(String equipmentAssignedToDesc) {
        this.equipmentAssignedToDesc = equipmentAssignedToDesc;
    }

    @Override
    public String toString() {
        return "EquipmentGenerationEntity{" +
                "equipmentGenerationCode='" + equipmentGenerationCode + '\'' +
                ", equipmentGenerationDesc='" + equipmentGenerationDesc + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", organizationDesc='" + organizationDesc + '\'' +
                ", description='" + description + '\'' +
                ", equipmentConfigurationCode='" + equipmentConfigurationCode + '\'' +
                ", equipmentConfigurationDesc='" + equipmentConfigurationDesc + '\'' +
                ", revisionNum='" + revisionNum + '\'' +
                ", lastUpdatedDate=" + lastUpdatedDate +
                ", createdDate=" + createdDate +
                ", dateUpdated=" + dateUpdated +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", equipmentStatusCode='" + equipmentStatusCode + '\'' +
                ", equipmentStatusDesc='" + equipmentStatusDesc + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", generateCount='" + generateCount + '\'' +
                ", processed='" + processed + '\'' +
                ", active='" + active + '\'' +
                ", awaitingPurchase='" + awaitingPurchase + '\'' +
                ", processError='" + processError + '\'' +
                ", processRunning='" + processRunning + '\'' +
                ", allSpecific='" + allSpecific + '\'' +
                ", setDueValues='" + setDueValues + '\'' +
                ", activateMps='" + activateMps + '\'' +
                ", createCommissioningWO='" + createCommissioningWO + '\'' +
                ", topLevelOnly='" + topLevelOnly + '\'' +
                ", allDependent='" + allDependent + '\'' +
                ", allCostRollup='" + allCostRollup + '\'' +
                ", copyComments='" + copyComments + '\'' +
                ", copyDocuments='" + copyDocuments + '\'' +
                ", copyCustomfields='" + copyCustomfields + '\'' +
                ", copyDepreciation='" + copyDepreciation + '\'' +
                ", copyMeters='" + copyMeters + '\'' +
                ", copyPartsAssociated='" + copyPartsAssociated + '\'' +
                ", copyWarranties='" + copyWarranties + '\'' +
                ", copyPMSchedules='" + copyPMSchedules + '\'' +
                ", copyMaintenancePatterns='" + copyMaintenancePatterns + '\'' +
                ", copySafety='" + copySafety + '\'' +
                ", copyPermits='" + copyPermits + '\'' +
                ", copyCalibration='" + copyCalibration + '\'' +
                ", copyTestPoints='" + copyTestPoints + '\'' +
                ", commissioningWONumber='" + commissioningWONumber + '\'' +
                ", commissioningWODesc='" + commissioningWODesc + '\'' +
                ", commissioningWOCostCode='" + commissioningWOCostCode + '\'' +
                ", commissioningWOCostDesc='" + commissioningWOCostDesc + '\'' +
                ", commissioningWOAssignedTo='" + commissioningWOAssignedTo + '\'' +
                ", commissioningWOStatusCode='" + commissioningWOStatusCode + '\'' +
                ", commissioningWOStatusDesc='" + commissioningWOStatusDesc + '\'' +
                ", commissioningWOLocationCode='" + commissioningWOLocationCode + '\'' +
                ", commissioningWOLocationDesc='" + commissioningWOLocationDesc + '\'' +
                ", commissioningWODepartmentCode='" + commissioningWODepartmentCode + '\'' +
                ", commissioningWODepartmentDesc='" + commissioningWODepartmentDesc + '\'' +
                '}';
    }
}
