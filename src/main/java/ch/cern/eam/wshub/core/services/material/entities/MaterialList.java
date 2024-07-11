package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;

public class MaterialList {

	private String materialListCode;
	private String partCode;
	private String lineNumber;
	private BigDecimal quantity;
	private String reserve;
	private String equipmentCode;

	private String UOM;
	
	public String getMaterialListCode() {
		return materialListCode;
	}
	public void setMaterialListCode(String materialListCode) {
		this.materialListCode = materialListCode;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String UOM) {
		this.UOM = UOM;
	}

	@Override
	public String toString() {
		return "MaterialList ["
				+ (materialListCode != null ? "materialListCode="
						+ materialListCode + ", " : "")
				+ (partCode != null ? "partCode=" + partCode + ", " : "")
				+ (lineNumber != null ? "lineNumber=" + lineNumber + ", " : "")
				+ (quantity != null ? "quantity=" + quantity + ", " : "")
				+ (reserve != null ? "reserve=" + reserve + ", " : "")
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode
						: "") + "]";
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	

}
