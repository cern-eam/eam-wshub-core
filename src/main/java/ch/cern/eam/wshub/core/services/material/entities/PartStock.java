package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;

public class PartStock implements Serializable {

	private String storeCode;
	private String storeDesc;
	private String bin;
	private String lot;
	private String qtyOnHand;
	private String partCode;
	private String repairQuantity;
	private String assetCode;
	
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

	public String getQtyOnHand() {
		return qtyOnHand;
	}
	public void setQtyOnHand(String qtyOnHand) {
		this.qtyOnHand = qtyOnHand;
	}

	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public String getStoreDesc() { return storeDesc; }
	public void setStoreDesc(String storeDesc) { this.storeDesc = storeDesc; }

	public String getRepairQuantity() { return repairQuantity; }
	public void setRepairQuantity(String repairQuantity) { this.repairQuantity = repairQuantity; }

	public String getAssetCode() { return assetCode; }
	public void setAssetCode(String assetCode) { this.assetCode = assetCode; }

	@Override
	public String toString() {
		return "PartStock ["
				+ (storeCode != null ? "storeCode=" + storeCode + ", " : "")
				+ (bin != null ? "bin=" + bin + ", " : "")
				+ (lot != null ? "lot=" + lot + ", " : "")
				+ (qtyOnHand != null ? "qtyOnHand=" + qtyOnHand + ", " : "")
				+ (partCode != null ? "partCode=" + partCode : "") + "]";
	}
	
	
}
