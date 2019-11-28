package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;

public class PartSupplier implements Serializable {

	private String supplierCode;
	private String supplierPartDescription;
	private String catalogReference;
	private BigDecimal grossPrice;
	private BigDecimal minimumOrderQty;
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

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getGrossPrice() {
		return grossPrice;
	}
	public void setGrossPrice(BigDecimal grossPrice) {
		this.grossPrice = grossPrice;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getMinimumOrderQty() {
		return minimumOrderQty;
	}
	public void setMinimumOrderQty(BigDecimal minimumOrderQty) {
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
