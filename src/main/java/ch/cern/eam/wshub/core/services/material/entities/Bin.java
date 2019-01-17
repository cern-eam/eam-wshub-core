package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;

public class Bin implements Serializable {

	private String storeCode;
	private String binCode;
	private String binDesc;
	private String outOfService;
	
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getBinCode() {
		return binCode;
	}
	public void setBinCode(String binCode) {
		this.binCode = binCode;
	}
	public String getBinDesc() {
		return binDesc;
	}
	public void setBinDesc(String binDesc) {
		this.binDesc = binDesc;
	}
	public String getOutOfService() {
		return outOfService;
	}
	public void setOutOfService(String outOfService) {
		this.outOfService = outOfService;
	}
	
	
}
