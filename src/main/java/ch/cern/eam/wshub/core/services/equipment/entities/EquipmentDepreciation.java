package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name="R5OBJDEPTYPES")
@NamedQuery(name = EquipmentDepreciation.GETDEPRECIATION, query = "SELECT ed FROM EquipmentDepreciation ed where ed.equipmentCode = :equipmentCode")
public class EquipmentDepreciation implements Serializable {

	public static final String GETDEPRECIATION = "GETDEPRECIATION";
	@Id
	@Column(name="OBD_PK")
	private BigDecimal depreciationPK;
	@Transient
	private String depreciationMethod;
	@Transient
	private String depreciationType;
	@Transient
	private BigDecimal originalValue;
	@Transient
	private BigDecimal estimatedUsefulLife;
	@Transient
	private BigDecimal residualValue;
	@Column(name="OBD_OBJECT")
	private String equipmentCode;
	@Transient
	private String estimatedUsefulLifeUOM;
	@Transient
	private String fromDate;
	@Transient
	private String depreciationCategory;

	@Transient
	@InforField(xpath = "StandardUserDefinedFields")
	private UserDefinedFields userDefinedFields;

	@Transient
	private BigDecimal changeValue;
	@Transient
	private BigDecimal changeLife;
	@Transient
	private BigDecimal changeEstimatedLifetimeOutput;
	
	
	public String getDepreciationMethod() {
		return depreciationMethod;
	}
	public void setDepreciationMethod(String depreciationMethod) {
		this.depreciationMethod = depreciationMethod;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(BigDecimal originalValue) {
		this.originalValue = originalValue;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getEstimatedUsefulLife() {
		return estimatedUsefulLife;
	}
	public void setEstimatedUsefulLife(BigDecimal estimatedUsefulLife) {
		this.estimatedUsefulLife = estimatedUsefulLife;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getResidualValue() {
		return residualValue;
	}
	public void setResidualValue(BigDecimal residualValue) {
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

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getDepreciationPK() {
		return depreciationPK;
	}
	public void setDepreciationPK(BigDecimal depreciationPK) {
		this.depreciationPK = depreciationPK;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getChangeValue() {
		return changeValue;
	}
	public void setChangeValue(BigDecimal changeValue) {
		this.changeValue = changeValue;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getChangeLife() {
		return changeLife;
	}
	public void setChangeLife(BigDecimal changeLife) {
		this.changeLife = changeLife;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getChangeEstimatedLifetimeOutput() {
		return changeEstimatedLifetimeOutput;
	}
	public void setChangeEstimatedLifetimeOutput(
			BigDecimal changeEstimatedLifetimeOutput) {
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
