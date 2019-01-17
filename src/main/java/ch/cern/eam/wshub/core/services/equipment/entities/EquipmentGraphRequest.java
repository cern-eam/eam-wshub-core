package ch.cern.eam.wshub.core.services.equipment.entities;

import java.io.Serializable;

public class EquipmentGraphRequest implements Serializable {

	private String equipmentCode;
	private String linkTypes;
	private int maxDepth;
	

	
	public String getLinkTypes() {
		return linkTypes;
	}
	public void setLinkTypes(String linkTypes) {
		this.linkTypes = linkTypes;
	}
	public int getMaxDepth() {
		return maxDepth;
	}
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	
}
