package ch.cern.eam.wshub.core.services.equipment.entities;

import java.io.Serializable;

public class EquipmentStructure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String childCode;
	private String parentCode;
	private String newParentCode;
	private String costRollUp;
	private String dependent;
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
	public String getCostRollUp() {
		return costRollUp;
	}
	public void setCostRollUp(String costRollUp) {
		this.costRollUp = costRollUp;
	}
	public String getDependent() {
		return dependent;
	}
	public void setDependent(String dependent) {
		this.dependent = dependent;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
	public String getNewParentCode() {
		return newParentCode;
	}
	public void setNewParentCode(String newParentCode) {
		this.newParentCode = newParentCode;
	}
	
	
}
