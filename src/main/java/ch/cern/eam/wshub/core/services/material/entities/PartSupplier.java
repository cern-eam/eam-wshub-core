package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;

public class PartSupplier implements Serializable {

	private String supplierCode;
	private String supplierPartDescription;
	private String catalogReference;
	private String grossPrice;
	private String minimumOrderQty;
	private String partCode;
	
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierPartDescription() {
		return supplierPartDescription;
	}
	public void setSupplierPartDescription(String supplierPartDescription) {
		this.supplierPartDescription = supplierPartDescription;
	}
	public String getCatalogReference() {
		return catalogReference;
	}
	public void setCatalogReference(String catalogReference) {
		this.catalogReference = catalogReference;
	}
	public String getGrossPrice() {
		return grossPrice;
	}
	public void setGrossPrice(String grossPrice) {
		this.grossPrice = grossPrice;
	}
	public String getMinimumOrderQty() {
		return minimumOrderQty;
	}
	public void setMinimumOrderQty(String minimumOrderQty) {
		this.minimumOrderQty = minimumOrderQty;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	@Override
	public String toString() {
		return "PartSupplier ["
				+ (supplierCode != null ? "supplierCode=" + supplierCode + ", "
						: "")
				+ (supplierPartDescription != null ? "supplierPartDescription="
						+ supplierPartDescription + ", " : "")
				+ (catalogReference != null ? "catalogReference="
						+ catalogReference + ", " : "")
				+ (grossPrice != null ? "grossPrice=" + grossPrice + ", " : "")
				+ (minimumOrderQty != null ? "minimumOrderQty="
						+ minimumOrderQty + ", " : "")
				+ (partCode != null ? "partCode=" + partCode : "") + "]";
	}
	
}
