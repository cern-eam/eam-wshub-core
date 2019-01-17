package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;

public class BuildKitPartParam implements Serializable {
	private static final long serialVersionUID = 7865040704362527306L;

	private String codePk;
	private String partCode;
	private String Description;
	private String uom;
	private String bin;
	private String lot;
	private String assetCode;
	public String getCodePk() {
		return codePk;
	}
	public void setCodePk(String codePk) {
		this.codePk = codePk;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
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
	public String getAssetCode() {
		return assetCode;
	}
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	
}
