package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuildKitParam implements Serializable {
	private static final long serialVersionUID = 7865040704362527306L;

	private String sessionID;  
	private String storeCode;
	private String partKitCode;
	private String kitBin;
	private String numberofKit;
	private String kitLotDescription;
	private List<BuildKitPartParam> partLines;
	
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public List<BuildKitPartParam> getPartLines() {
		if (partLines == null) {
			partLines = new ArrayList();
		}
		return partLines;
	}
	public void setPartLines(List<BuildKitPartParam> partLines) {
		this.partLines = partLines;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getPartKitCode() {
		return partKitCode;
	}
	public void setPartKitCode(String partKitCode) {
		this.partKitCode = partKitCode;
	}
	public String getKitBin() {
		return kitBin;
	}
	public void setKitBin(String kitBin) {
		this.kitBin = kitBin;
	}
	public String getNumberofKit() {
		return numberofKit;
	}
	public void setNumberofKit(String numberofKit) {
		this.numberofKit = numberofKit;
	}
	public String getKitLotDescription() {
		return kitLotDescription;
	}
	public void setKitLotDescription(String kitLotDescription) {
		this.kitLotDescription = kitLotDescription;
	}
	
	
	
	
}
