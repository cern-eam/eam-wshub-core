package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="R5OBJDEPTYPES")
@NamedQuery(name = EquipmentDepreciation.GETDEPRECIATION, query = "SELECT ed FROM EquipmentDepreciation ed where ed.equipmentCode = :equipmentCode")
public class EquipmentDepreciation implements Serializable {

	public static final String GETDEPRECIATION = "GETDEPRECIATION";
	@Id
	@Column(name="OBD_PK")
	private String depreciationPK;
	@Transient
	private String depreciationMethod;
	@Transient
	private String depreciationType;
	@Transient
	private String originalValue;
	@Transient
	private String estimatedUsefulLife;
	@Transient
	private String residualValue;
	@Column(name="OBD_OBJECT")
	private String equipmentCode;
	@Transient
	private String estimatedUsefulLifeUOM;
	@Transient
	private String fromDate;
	@Transient
	private String depreciationCategory;
	@Transient
	private UserDefinedFields userDefinedFields;
	@Transient
	private String changeValue;
	@Transient
	private String changeLife;
	@Transient
	private String changeEstimatedLifetimeOutput;
	
	
	public String getDepreciationMethod() {
		return depreciationMethod;
	}
	public void setDepreciationMethod(String depreciationMethod) {
		this.depreciationMethod = depreciationMethod;
	}
	public String getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}
	public String getEstimatedUsefulLife() {
		return estimatedUsefulLife;
	}
	public void setEstimatedUsefulLife(String estimatedUsefulLife) {
		this.estimatedUsefulLife = estimatedUsefulLife;
	}
	public String getResidualValue() {
		return residualValue;
	}
	public void setResidualValue(String residualValue) {
		this.residualValue = residualValue;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	

	
	public String getEstimatedUsefulLifeUOM() {
		return estimatedUsefulLifeUOM;
	}
	public void setEstimatedUsefulLifeUOM(String estimatedUsefulLifeUOM) {
		this.estimatedUsefulLifeUOM = estimatedUsefulLifeUOM;
	}
	public UserDefinedFields getUserDefinedFields() {
		return userDefinedFields;
	}
	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getDepreciationCategory() {
		return depreciationCategory;
	}
	public void setDepreciationCategory(String depreciationCategory) {
		this.depreciationCategory = depreciationCategory;
	}

	public String getDepreciationType() {
		return depreciationType;
	}
	public void setDepreciationType(String depreciationType) {
		this.depreciationType = depreciationType;
	}
	public String getDepreciationPK() {
		return depreciationPK;
	}
	public void setDepreciationPK(String depreciationPK) {
		this.depreciationPK = depreciationPK;
	}
	public String getChangeValue() {
		return changeValue;
	}
	public void setChangeValue(String changeValue) {
		this.changeValue = changeValue;
	}
	public String getChangeLife() {
		return changeLife;
	}
	public void setChangeLife(String changeLife) {
		this.changeLife = changeLife;
	}
	public String getChangeEstimatedLifetimeOutput() {
		return changeEstimatedLifetimeOutput;
	}
	public void setChangeEstimatedLifetimeOutput(
			String changeEstimatedLifetimeOutput) {
		this.changeEstimatedLifetimeOutput = changeEstimatedLifetimeOutput;
	}
	@Override
	public String toString() {
		return "EquipmentDepreciation ["
				+ (depreciationPK != null ? "depreciationPK=" + depreciationPK
						+ ", " : "")
				+ (depreciationMethod != null ? "depreciationMethod="
						+ depreciationMethod + ", " : "")
				+ (depreciationType != null ? "depreciationType="
						+ depreciationType + ", " : "")
				+ (originalValue != null ? "originalValue=" + originalValue
						+ ", " : "")
				+ (estimatedUsefulLife != null ? "estimatedUsefulLife="
						+ estimatedUsefulLife + ", " : "")
				+ (residualValue != null ? "residualValue=" + residualValue
						+ ", " : "")
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode
						+ ", " : "")
				+ (estimatedUsefulLifeUOM != null ? "estimatedUsefulLifeUOM="
						+ estimatedUsefulLifeUOM + ", " : "")
				+ (fromDate != null ? "fromDate=" + fromDate + ", " : "")
				+ (depreciationCategory != null ? "depreciationCategory="
						+ depreciationCategory + ", " : "")
				+ (userDefinedFields != null ? "userDefinedFields="
						+ userDefinedFields + ", " : "")
				+ (changeValue != null ? "changeValue=" + changeValue + ", "
						: "")
				+ (changeLife != null ? "changeLife=" + changeLife + ", " : "")
				+ (changeEstimatedLifetimeOutput != null ? "changeEstimatedLifetimeOutput="
						+ changeEstimatedLifetimeOutput
						: "") + "]";
	}
	
	
	
}
