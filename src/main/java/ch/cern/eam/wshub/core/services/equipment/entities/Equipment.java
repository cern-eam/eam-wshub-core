package ch.cern.eam.wshub.core.services.equipment.entities;



import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "R5OBJECTS")
public class Equipment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7865040704362527306L;

	@Column(name = "OBJ_DESC")
	private String description;
	@Id
	@Column(name = "OBJ_CODE")
	private String code;
	@Column(name = "OBJ_OBTYPE")
	private String typeCode;
	@Transient
	private String alias;
	@Transient
	private String typeDesc;
	@Transient
	private String classCode;
	@Transient
	private String classDesc;
	@Transient
	private String categoryCode;
	@Transient
	private String categoryDesc;
	@Transient
	private String updateCount;
	@Transient
	private String outOfService;
	@Transient
	private String inProduction;
	@Transient
	private String profileCode;
	//
	@Transient
	private String statusCode;
	@Transient
	private String statusDesc;
	//
	@Transient
	private String costCode;

	@Transient
	private String departmentCode;
	@Transient
	private String departmentDesc;
	//
	@Transient
	private CustomField[] customFields;

	//
	@Transient
	private Date comissionDate;
	@Transient
	private String equipmentValue;
	@Transient
	private String assignedTo;
	@Transient
	private String assignedToDesc;
	@Transient
	private String meterUnit;
	@Transient
	private String criticality;
	@Transient
	private String cGMP;
	@Transient
	private String originalReceiptDate;
	@Transient
	private String stateCode;
	@Transient
	private String stateDesc;

	@Transient
	private String manufacturerCode;
	@Transient
	private String manufacturerDesc;
	@Transient
	private String serialNumber;
	@Transient
	private String model;
	@Transient
	private String revision;
	@Transient
	private String xCoordinate;
	@Transient
	private String yCoordinate;
	@Transient
	private String zCoordinate;

	@Transient
	private String costOfNeededRepairs;
	@Transient
	private String replacementValue;
	@Transient
	private String facilityConditionIndex;
	@Transient
	private String yearBuilt;
	@Transient
	private String serviceLifetime;

	// Hierarchy
	// Asset
	@Transient
	private String hierarchyAssetCode;
	@Transient
	private String hierarchyAssetDesc;
	@Transient
	private String hierarchyAssetDependent;
	@Transient
	private String hierarchyAssetCostRollUp;
	// Position
	@Transient
	private String hierarchyPositionCode;
	@Transient
	private String hierarchyPositionDesc;
	@Transient
	private String hierarchyPositionDependent;
	@Transient
	private String hierarchyPositionCostRollUp;
	// System
	@Transient
	private String hierarchyPrimarySystemCode;
	@Transient
	private String hierarchyPrimarySystemDesc;
	@Transient
	private String hierarchyPrimarySystemDependent;
	@Transient
	private String hierarchyPrimarySystemCostRollUp;
	// Location
	@Transient
	private String hierarchyLocationCode;
	@Transient
	private String hierarchyLocationDesc;
	// Part Association
	@Transient
	private String partCode;
	@Transient
	private String partDesc;
	@Transient
	private String storeCode;
	@Transient
	private String storeDesc;
	@Transient
	private String bin;
	@Transient
	private String binDesc;
	@Transient
	private String lot;
	// Linear Reference
	@Transient
	private String linearRefPrecision;
	@Transient
	private String linearRefUOM;
	@Transient
	private String linearRefEquipmentLength;
	@Transient
	private String linearRefEquipmentLengthUOM;
	@Transient
	private String linearRefGeographicalRef;
	// Variables
	@Transient
	private String variable1;
	@Transient
	private String variable2;
	@Transient
	private String variable3;
	@Transient
	private String variable4;
	@Transient
	private String variable5;
	@Transient
	private String variable6;
	// Dormant
	@Transient
	private String dormantStart;
	@Transient
	private String dormantEnd;
	@Transient
	private String dormantReusePeriod;

	@Transient
	private UserDefinedFields userDefinedFields;

	@Transient
	private String safety;

	@Transient
	private Date originalInstallDate;

	@Transient
	private String lastLocationCode;
	@Transient
	private String lastLocationDesc;

	// CERN Properties
	@Transient
	private String cernMachine;
	@Transient
	private Integer cernCao;
	@Transient
	private Double cernX;
	@Transient
	private Double cernY;
	@Transient
	private Double cernZ;
	@Transient
	private String cernPos;
	@Transient
	private String cernFonc;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}

	public String getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(String updateCount) {
		this.updateCount = updateCount;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentDesc() {
		return departmentDesc;
	}

	public void setDepartmentDesc(String departmentDesc) {
		this.departmentDesc = departmentDesc;
	}

	@XmlElementWrapper(name = "customFields")
	@XmlElement(name = "customField")
	public CustomField[] getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomField[] customFields) {
		this.customFields = customFields;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getCostCode() {
		return costCode;
	}

	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)

	public Date getComissionDate() {
		return comissionDate;
	}

	public void setComissionDate(Date comissionDate) {
		this.comissionDate = comissionDate;
	}

	public String getEquipmentValue() {
		return equipmentValue;
	}

	public void setEquipmentValue(String equipmentValue) {
		this.equipmentValue = equipmentValue;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getMeterUnit() {
		return meterUnit;
	}

	public void setMeterUnit(String meterUnit) {
		this.meterUnit = meterUnit;
	}

	public String getCriticality() {
		return criticality;
	}

	public void setCriticality(String criticality) {
		this.criticality = criticality;
	}

	public String getManufacturerCode() {
		return manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(String xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public String getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(String yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public String getzCoordinate() {
		return zCoordinate;
	}

	public void setzCoordinate(String zCoordinate) {
		this.zCoordinate = zCoordinate;
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

	public String getHierarchyAssetCode() {
		return hierarchyAssetCode;
	}

	public void setHierarchyAssetCode(String hierarchyAssetCode) {
		this.hierarchyAssetCode = hierarchyAssetCode;
	}

	public String getHierarchyAssetDesc() {
		return hierarchyAssetDesc;
	}

	public void setHierarchyAssetDesc(String hierarchyAssetDesc) {
		this.hierarchyAssetDesc = hierarchyAssetDesc;
	}

	public String getHierarchyAssetDependent() {
		return hierarchyAssetDependent;
	}

	public void setHierarchyAssetDependent(String hierarchyAssetDependent) {
		this.hierarchyAssetDependent = hierarchyAssetDependent;
	}

	public String getHierarchyAssetCostRollUp() {
		return hierarchyAssetCostRollUp;
	}

	public void setHierarchyAssetCostRollUp(String hierarchyAssetCostRollUp) {
		this.hierarchyAssetCostRollUp = hierarchyAssetCostRollUp;
	}

	public String getHierarchyPositionCode() {
		return hierarchyPositionCode;
	}

	public void setHierarchyPositionCode(String hierarchyPositionCode) {
		this.hierarchyPositionCode = hierarchyPositionCode;
	}

	public String getHierarchyPositionDesc() {
		return hierarchyPositionDesc;
	}

	public void setHierarchyPositionDesc(String hierarchyPositionDesc) {
		this.hierarchyPositionDesc = hierarchyPositionDesc;
	}

	public String getHierarchyPositionDependent() {
		return hierarchyPositionDependent;
	}

	public void setHierarchyPositionDependent(String hierarchyPositionDependent) {
		this.hierarchyPositionDependent = hierarchyPositionDependent;
	}

	public String getHierarchyPositionCostRollUp() {
		return hierarchyPositionCostRollUp;
	}

	public void setHierarchyPositionCostRollUp(String hierarchyPositionCostRollUp) {
		this.hierarchyPositionCostRollUp = hierarchyPositionCostRollUp;
	}

	public String getHierarchyLocationCode() {
		return hierarchyLocationCode;
	}

	public void setHierarchyLocationCode(String hierarchyLocationCode) {
		this.hierarchyLocationCode = hierarchyLocationCode;
	}

	public String getHierarchyLocationDesc() {
		return hierarchyLocationDesc;
	}

	public void setHierarchyLocationDesc(String hierarchyLocationDesc) {
		this.hierarchyLocationDesc = hierarchyLocationDesc;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getPartDesc() {
		return partDesc;
	}

	public void setPartDesc(String partDesc) {
		this.partDesc = partDesc;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public String getLinearRefPrecision() {
		return linearRefPrecision;
	}

	public void setLinearRefPrecision(String linearRefPrecision) {
		this.linearRefPrecision = linearRefPrecision;
	}

	public String getLinearRefUOM() {
		return linearRefUOM;
	}

	public void setLinearRefUOM(String linearRefUOM) {
		this.linearRefUOM = linearRefUOM;
	}

	public String getLinearRefGeographicalRef() {
		return linearRefGeographicalRef;
	}

	public void setLinearRefGeographicalRef(String linearRefGeographicalRef) {
		this.linearRefGeographicalRef = linearRefGeographicalRef;
	}

	public String getLinearRefEquipmentLength() {
		return linearRefEquipmentLength;
	}

	public void setLinearRefEquipmentLength(String linearRefEquipmentLength) {
		this.linearRefEquipmentLength = linearRefEquipmentLength;
	}

	public String getLinearRefEquipmentLengthUOM() {
		return linearRefEquipmentLengthUOM;
	}

	public void setLinearRefEquipmentLengthUOM(String linearRefEquipmentLengthUOM) {
		this.linearRefEquipmentLengthUOM = linearRefEquipmentLengthUOM;
	}

	public String getVariable1() {
		return variable1;
	}

	public void setVariable1(String variable1) {
		this.variable1 = variable1;
	}

	public String getVariable2() {
		return variable2;
	}

	public void setVariable2(String variable2) {
		this.variable2 = variable2;
	}

	public String getVariable3() {
		return variable3;
	}

	public void setVariable3(String variable3) {
		this.variable3 = variable3;
	}

	public String getVariable4() {
		return variable4;
	}

	public void setVariable4(String variable4) {
		this.variable4 = variable4;
	}

	public String getVariable5() {
		return variable5;
	}

	public void setVariable5(String variable5) {
		this.variable5 = variable5;
	}

	public String getVariable6() {
		return variable6;
	}

	public void setVariable6(String variable6) {
		this.variable6 = variable6;
	}

	public String getDormantStart() {
		return dormantStart;
	}

	public void setDormantStart(String dormantStart) {
		this.dormantStart = dormantStart;
	}

	public String getDormantEnd() {
		return dormantEnd;
	}

	public void setDormantEnd(String dormantEnd) {
		this.dormantEnd = dormantEnd;
	}

	public String getDormantReusePeriod() {
		return dormantReusePeriod;
	}

	public void setDormantReusePeriod(String dormantReusePeriod) {
		this.dormantReusePeriod = dormantReusePeriod;
	}

	public String getcGMP() {
		return cGMP;
	}

	public void setcGMP(String cGMP) {
		this.cGMP = cGMP;
	}

	public String getOutOfService() {
		return outOfService;
	}

	public void setOutOfService(String outOfService) {
		this.outOfService = outOfService;
	}

	public String getInProduction() {
		return inProduction;
	}

	public void setInProduction(String inProduction) {
		this.inProduction = inProduction;
	}

	public String getCostOfNeededRepairs() {
		return costOfNeededRepairs;
	}

	public void setCostOfNeededRepairs(String costOfNeededRepairs) {
		this.costOfNeededRepairs = costOfNeededRepairs;
	}

	public String getReplacementValue() {
		return replacementValue;
	}

	public void setReplacementValue(String replacementValue) {
		this.replacementValue = replacementValue;
	}

	public String getFacilityConditionIndex() {
		return facilityConditionIndex;
	}

	public void setFacilityConditionIndex(String facilityConditionIndex) {
		this.facilityConditionIndex = facilityConditionIndex;
	}

	public String getYearBuilt() {
		return yearBuilt;
	}

	public void setYearBuilt(String yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public String getServiceLifetime() {
		return serviceLifetime;
	}

	public void setServiceLifetime(String serviceLifetime) {
		this.serviceLifetime = serviceLifetime;
	}

	public String getOriginalReceiptDate() {
		return originalReceiptDate;
	}

	public void setOriginalReceiptDate(String originalReceiptDate) {
		this.originalReceiptDate = originalReceiptDate;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getSafety() {
		return safety;
	}

	public void setSafety(String safety) {
		this.safety = safety;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)

	public Date getOriginalInstallDate() {
		return originalInstallDate;
	}

	public void setOriginalInstallDate(Date originalInstallDate) {
		this.originalInstallDate = originalInstallDate;
	}

	public String getAssignedToDesc() {
		return assignedToDesc;
	}

	public void setAssignedToDesc(String assignedToDesc) {
		this.assignedToDesc = assignedToDesc;
	}

	public String getManufacturerDesc() {
		return manufacturerDesc;
	}

	public void setManufacturerDesc(String manufacturerDesc) {
		this.manufacturerDesc = manufacturerDesc;
	}

	public String getStoreDesc() {
		return storeDesc;
	}

	public void setStoreDesc(String storeDesc) {
		this.storeDesc = storeDesc;
	}

	public String getBinDesc() {
		return binDesc;
	}

	public void setBinDesc(String binDesc) {
		this.binDesc = binDesc;
	}

	/**
	 * @return the lastLocationCode
	 */
	public String getLastLocationCode() {
		return lastLocationCode;
	}

	/**
	 * @param lastLocationCode
	 *            the lastLocationCode to set
	 */
	public void setLastLocationCode(String lastLocationCode) {
		this.lastLocationCode = lastLocationCode;
	}

	/**
	 * @return the lastLocationDesc
	 */
	public String getLastLocationDesc() {
		return lastLocationDesc;
	}

	/**
	 * @param lastLocationDesc
	 *            the lastLocationDesc to set
	 */
	public void setLastLocationDesc(String lastLocationDesc) {
		this.lastLocationDesc = lastLocationDesc;
	}

	/**
	 * @return the cernMachine
	 */
	public String getCernMachine() {
		return cernMachine;
	}

	/**
	 * @param cernMachine
	 *            the cernMachine to set
	 */
	public void setCernMachine(String cernMachine) {
		this.cernMachine = cernMachine;
	}

	/**
	 * @return the cernCao
	 */
	public Integer getCernCao() {
		return cernCao;
	}

	/**
	 * @param cernCao
	 *            the cernCao to set
	 */
	public void setCernCao(Integer cernCao) {
		this.cernCao = cernCao;
	}

	/**
	 * @return the cernX
	 */
	public Double getCernX() {
		return cernX;
	}

	/**
	 * @param cernX
	 *            the cernX to set
	 */
	public void setCernX(Double cernX) {
		this.cernX = cernX;
	}

	/**
	 * @return the cernY
	 */
	public Double getCernY() {
		return cernY;
	}

	/**
	 * @param cernY
	 *            the cernY to set
	 */
	public void setCernY(Double cernY) {
		this.cernY = cernY;
	}

	/**
	 * @return the cernZ
	 */
	public Double getCernZ() {
		return cernZ;
	}

	/**
	 * @param cernZ
	 *            the cernZ to set
	 */
	public void setCernZ(Double cernZ) {
		this.cernZ = cernZ;
	}

	/**
	 * @return the cernPos
	 */
	public String getCernPos() {
		return cernPos;
	}

	/**
	 * @param cernPos
	 *            the cernPos to set
	 */
	public void setCernPos(String cernPos) {
		this.cernPos = cernPos;
	}

	/**
	 * @return the cernFonc
	 */
	public String getCernFonc() {
		return cernFonc;
	}

	/**
	 * @param cernFonc
	 *            the cernFonc to set
	 */
	public void setCernFonc(String cernFonc) {
		this.cernFonc = cernFonc;
	}

	public String getHierarchyPrimarySystemCode() {
		return hierarchyPrimarySystemCode;
	}

	public void setHierarchyPrimarySystemCode(String hierarchyPrimarySystemCode) {
		this.hierarchyPrimarySystemCode = hierarchyPrimarySystemCode;
	}

	public String getHierarchyPrimarySystemDesc() {
		return hierarchyPrimarySystemDesc;
	}

	public void setHierarchyPrimarySystemDesc(String hierarchyPrimarySystemDesc) {
		this.hierarchyPrimarySystemDesc = hierarchyPrimarySystemDesc;
	}

	public String getHierarchyPrimarySystemDependent() {
		return hierarchyPrimarySystemDependent;
	}

	public void setHierarchyPrimarySystemDependent(String hierarchyPrimarySystemDependent) {
		this.hierarchyPrimarySystemDependent = hierarchyPrimarySystemDependent;
	}

	public String getHierarchyPrimarySystemCostRollUp() {
		return hierarchyPrimarySystemCostRollUp;
	}

	public void setHierarchyPrimarySystemCostRollUp(String hierarchyPrimarySystemCostRollUp) {
		this.hierarchyPrimarySystemCostRollUp = hierarchyPrimarySystemCostRollUp;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public String getProfileCode() { return profileCode; }

	public void setProfileCode(String profileCode) { this.profileCode = profileCode; }

	@Override
	public String toString() {
		return "Equipment [" + (description != null ? "description=" + description + ", " : "")
				+ (code != null ? "code=" + code + ", " : "") + (typeCode != null ? "typeCode=" + typeCode + ", " : "")
				+ (alias != null ? "alias=" + alias + ", " : "")
				+ (typeDesc != null ? "typeDesc=" + typeDesc + ", " : "")
				+ (classCode != null ? "classCode=" + classCode + ", " : "")
				+ (classDesc != null ? "classDesc=" + classDesc + ", " : "")
				+ (categoryCode != null ? "categoryCode=" + categoryCode + ", " : "")
				+ (categoryDesc != null ? "categoryDesc=" + categoryDesc + ", " : "")
				+ (updateCount != null ? "updateCount=" + updateCount + ", " : "")
				+ (outOfService != null ? "outOfService=" + outOfService + ", " : "")
				+ (inProduction != null ? "inProduction=" + inProduction + ", " : "")
				+ (statusCode != null ? "statusCode=" + statusCode + ", " : "")
				+ (statusDesc != null ? "statusDesc=" + statusDesc + ", " : "")
				+ (costCode != null ? "costCode=" + costCode + ", " : "")
				+ (departmentCode != null ? "departmentCode=" + departmentCode + ", " : "")
				+ (departmentDesc != null ? "departmentDesc=" + departmentDesc + ", " : "")
				+ (customFields != null ? "customFields=" + Arrays.toString(customFields) + ", " : "")
				+ (comissionDate != null ? "comissionDate=" + comissionDate + ", " : "")
				+ (equipmentValue != null ? "equipmentValue=" + equipmentValue + ", " : "")
				+ (assignedTo != null ? "assignedTo=" + assignedTo + ", " : "")
				+ (assignedToDesc != null ? "assignedToDesc=" + assignedToDesc + ", " : "")
				+ (meterUnit != null ? "meterUnit=" + meterUnit + ", " : "")
				+ (criticality != null ? "criticality=" + criticality + ", " : "")
				+ (cGMP != null ? "cGMP=" + cGMP + ", " : "")
				+ (originalReceiptDate != null ? "originalReceiptDate=" + originalReceiptDate + ", " : "")
				+ (manufacturerCode != null ? "manufacturerCode=" + manufacturerCode + ", " : "")
				+ (manufacturerDesc != null ? "manufacturerDesc=" + manufacturerDesc + ", " : "")
				+ (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "")
				+ (model != null ? "model=" + model + ", " : "")
				+ (revision != null ? "revision=" + revision + ", " : "")
				+ (xCoordinate != null ? "xCoordinate=" + xCoordinate + ", " : "")
				+ (yCoordinate != null ? "yCoordinate=" + yCoordinate + ", " : "")
				+ (zCoordinate != null ? "zCoordinate=" + zCoordinate + ", " : "")
				+ (costOfNeededRepairs != null ? "costOfNeededRepairs=" + costOfNeededRepairs + ", " : "")
				+ (replacementValue != null ? "replacementValue=" + replacementValue + ", " : "")
				+ (facilityConditionIndex != null ? "facilityConditionIndex=" + facilityConditionIndex + ", " : "")
				+ (yearBuilt != null ? "yearBuilt=" + yearBuilt + ", " : "")
				+ (serviceLifetime != null ? "serviceLifetime=" + serviceLifetime + ", " : "")
				+ (hierarchyAssetCode != null ? "hierarchyAssetCode=" + hierarchyAssetCode + ", " : "")
				+ (hierarchyAssetDesc != null ? "hierarchyAssetDesc=" + hierarchyAssetDesc + ", " : "")
				+ (hierarchyAssetDependent != null ? "hierarchyAssetDependent=" + hierarchyAssetDependent + ", " : "")
				+ (hierarchyAssetCostRollUp != null ? "hierarchyAssetCostRollUp=" + hierarchyAssetCostRollUp + ", "
						: "")
				+ (hierarchyPositionCode != null ? "hierarchyPositionCode=" + hierarchyPositionCode + ", " : "")
				+ (hierarchyPositionDesc != null ? "hierarchyPositionDesc=" + hierarchyPositionDesc + ", " : "")
				+ (hierarchyPositionDependent != null
						? "hierarchyPositionDependent=" + hierarchyPositionDependent + ", "
						: "")
				+ (hierarchyPositionCostRollUp != null
						? "hierarchyPositionCostRollUp=" + hierarchyPositionCostRollUp + ", "
						: "")
				+ (hierarchyPrimarySystemCode != null
						? "hierarchyPrimarySystemCode=" + hierarchyPrimarySystemCode + ", "
						: "")
				+ (hierarchyPrimarySystemDesc != null
						? "hierarchyPrimarySystemDesc=" + hierarchyPrimarySystemDesc + ", "
						: "")
				+ (hierarchyPrimarySystemDependent != null
						? "hierarchyPrimarySystemDependent=" + hierarchyPrimarySystemDependent + ", "
						: "")
				+ (hierarchyPrimarySystemCostRollUp != null
						? "hierarchyPrimarySystemCostRollUp=" + hierarchyPrimarySystemCostRollUp + ", "
						: "")
				+ (hierarchyLocationCode != null ? "hierarchyLocationCode=" + hierarchyLocationCode + ", " : "")
				+ (hierarchyLocationDesc != null ? "hierarchyLocationDesc=" + hierarchyLocationDesc + ", " : "")
				+ (partCode != null ? "partCode=" + partCode + ", " : "")
				+ (partDesc != null ? "partDesc=" + partDesc + ", " : "")
				+ (storeCode != null ? "storeCode=" + storeCode + ", " : "")
				+ (storeDesc != null ? "storeDesc=" + storeDesc + ", " : "") + (bin != null ? "bin=" + bin + ", " : "")
				+ (binDesc != null ? "binDesc=" + binDesc + ", " : "") + (lot != null ? "lot=" + lot + ", " : "")
				+ (linearRefPrecision != null ? "linearRefPrecision=" + linearRefPrecision + ", " : "")
				+ (linearRefUOM != null ? "linearRefUOM=" + linearRefUOM + ", " : "")
				+ (linearRefEquipmentLength != null ? "linearRefEquipmentLength=" + linearRefEquipmentLength + ", "
						: "")
				+ (linearRefEquipmentLengthUOM != null
						? "linearRefEquipmentLengthUOM=" + linearRefEquipmentLengthUOM + ", "
						: "")
				+ (linearRefGeographicalRef != null ? "linearRefGeographicalRef=" + linearRefGeographicalRef + ", "
						: "")
				+ (variable1 != null ? "variable1=" + variable1 + ", " : "")
				+ (variable2 != null ? "variable2=" + variable2 + ", " : "")
				+ (variable3 != null ? "variable3=" + variable3 + ", " : "")
				+ (variable4 != null ? "variable4=" + variable4 + ", " : "")
				+ (variable5 != null ? "variable5=" + variable5 + ", " : "")
				+ (variable6 != null ? "variable6=" + variable6 + ", " : "")
				+ (dormantStart != null ? "dormantStart=" + dormantStart + ", " : "")
				+ (dormantEnd != null ? "dormantEnd=" + dormantEnd + ", " : "")
				+ (dormantReusePeriod != null ? "dormantReusePeriod=" + dormantReusePeriod + ", " : "")
				+ (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields + ", " : "")
				+ (safety != null ? "safety=" + safety + ", " : "")
				+ (originalInstallDate != null ? "originalInstallDate=" + originalInstallDate + ", " : "")
				+ (lastLocationCode != null ? "lastLocationCode=" + lastLocationCode + ", " : "")
				+ (lastLocationDesc != null ? "lastLocationDesc=" + lastLocationDesc + ", " : "")
				+ (cernMachine != null ? "cernMachine=" + cernMachine + ", " : "")
				+ (cernCao != null ? "cernCao=" + cernCao + ", " : "") + (cernX != null ? "cernX=" + cernX + ", " : "")
				+ (cernY != null ? "cernY=" + cernY + ", " : "") + (cernZ != null ? "cernZ=" + cernZ + ", " : "")
				+ (cernPos != null ? "cernPos=" + cernPos + ", " : "")
				+ (cernFonc != null ? "cernFonc=" + cernFonc : "") + "]";
	}

}
