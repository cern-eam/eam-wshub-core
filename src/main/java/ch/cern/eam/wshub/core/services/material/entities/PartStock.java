package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.annotations.GridField;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;

public class PartStock implements Serializable {


	@GridField(name="bisstore")
	private String storeCode;
	@GridField(name="storedesc")
	private String storeDesc;
	@GridField(name="bisbin")
	private String bin;
	@GridField(name="bislot")
	private String lot;
	@GridField(name="bisqty")
	private BigDecimal qtyOnHand;
	@GridField(name="conditionpartcode")
	private String partCode;
	@GridField(name="repairquantity")
	private String repairQuantity;
	@GridField(name="bisassetid")
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

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getQtyOnHand() {
		return qtyOnHand;
	}
	public void setQtyOnHand(BigDecimal qtyOnHand) {
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
