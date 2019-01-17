package ch.cern.eam.wshub.core.services.equipment.entities;

import java.io.Serializable;

public class EquipmentCampaign implements Serializable {

	private String campaign;
	private String equipment;
	
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	
	@Override
	public String toString() {
		return "EquipmentCampaign [" + (campaign != null ? "campaign=" + campaign + ", " : "")
				+ (equipment != null ? "equipment=" + equipment : "") + "]";
	}
	
	
}
