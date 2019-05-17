package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

public class EquipmentGeneration implements Serializable {

//    MP3231_AddEquipmentGeneration_001

//    @Transient
//    public String equipmentGenerationId;
//    @Transient
//    public String organizationID ;
//    @Transient
//    public String equipmentConfigurationID;

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
    private String revisionNum;
    @Transient
    private Date lastUpdatedDate;
    @Transient
    private Date createdDate;
    @Transient
    private Date dateUpdated;
    @Transient
    private String createdBy;
    @Transient
    private String updatedBy;
    @Transient
    private String equipmentStatus;
    @Transient
    private String statusCode;
    @Transient
    private String statusDesc;
    @Transient
    private String generateCount;
    @Transient
    private String value;
    @Transient
    private String numofDec ;
    @Transient
    private String sign;
    @Transient
    private String uom;
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

    public String getRevisionNum() {
        return revisionNum;
    }

    public void setRevisionNum(String revisionNum) {
        this.revisionNum = revisionNum;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(String equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
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

    public String getGenerateCount() {
        return generateCount;
    }

    public void setGenerateCount(String generateCount) {
        this.generateCount = generateCount;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNumofDec() {
        return numofDec;
    }

    public void setNumofDec(String numofDec) {
        this.numofDec = numofDec;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
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

    @Override
    public String toString() {
        return "EquipmentGeneration{" +
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
                ", equipmentStatus='" + equipmentStatus + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", generateCount='" + generateCount + '\'' +
                ", value='" + value + '\'' +
                ", numofDec='" + numofDec + '\'' +
                ", sign='" + sign + '\'' +
                ", uom='" + uom + '\'' +
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
                '}';
    }
}
