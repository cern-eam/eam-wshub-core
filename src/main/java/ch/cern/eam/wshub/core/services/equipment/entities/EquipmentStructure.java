package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.BooleanAdapter;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

public class EquipmentStructure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String childCode;
	private String childDesc;
	private String childType;
	private String parentCode;
	private String parentDesc;
	private String parentType;
	private String newParentCode;
	private String newParentDesc;
	private String newParentType;
	private Boolean costRollUp = false;
	private Boolean dependent = false;
	private String sequenceNumber;
	
	public String getChildCode() {
		return childCode;
	}
	
	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getCostRollUp() {
		return costRollUp;
	}
	public void setCostRollUp(Boolean costRollUp) {
		this.costRollUp = costRollUp;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getDependent() {
		return dependent;
	}
	public void setDependent(Boolean dependent) {
		this.dependent = dependent;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getNewParentCode() {
		return newParentCode;
	}
	public void setNewParentCode(String newParentCode) {
		this.newParentCode = newParentCode;
	}

	public String getChildDesc() {
		return childDesc;
	}

	public void setChildDesc(String childDesc) {
		this.childDesc = childDesc;
	}

	public String getChildType() {
		return childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public String getParentDesc() {
		return parentDesc;
	}

	public void setParentDesc(String parentDesc) {
		this.parentDesc = parentDesc;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	public String getNewParentDesc() {
		return newParentDesc;
	}

	public void setNewParentDesc(String newParentDesc) {
		this.newParentDesc = newParentDesc;
	}

	public String getNewParentType() {
		return newParentType;
	}

	public void setNewParentType(String newParentType) {
		this.newParentType = newParentType;
	}

	@Override
	public String toString() {
		return "EquipmentStructure ["
				+ (childCode != null ? "childCode=" + childCode + ", " : "")
				+ (parentCode != null ? "parentCode=" + parentCode + ", " : "")
				+ (newParentCode != null ? "newParentCode=" + newParentCode
				+ ", " : "")
				+ (costRollUp != null ? "costRollUp=" + costRollUp + ", " : "")
				+ (dependent != null ? "dependent=" + dependent + ", " : "")
				+ (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber
				: "") + "]";
	}
}
