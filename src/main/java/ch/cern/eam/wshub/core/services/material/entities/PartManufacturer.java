package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.BooleanAdapter;
import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.annotations.EAMField;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

public class PartManufacturer implements Serializable {

	@EAMField(xpath = "MANUFACTURERID/MANUFACTURERCODE", nullifyParentLevel = 0)
	private String manufacturerCode;

	@EAMField(xpath = "MANUFACTURERID/DESCRIPTION")
	private String manufacturerDesc;

	@EAMField(xpath = "PARTID/PARTCODE", nullifyParentLevel = 0)
	private String partCode;

	@EAMField(xpath = "MANUFACTURERDRAW")
	private String drawingNumber;

	@EAMField(xpath = "OUTOFSERVICE", booleanType = BooleanType.TRUE_FALSE)
	private Boolean outOfService;

	@EAMField(xpath = "ISPRIMARY", booleanType = BooleanType.TRUE_FALSE, nullifyParentLevel = 0)
	private Boolean primary;

	@EAMField(xpath = "MANUFACTURERPARTCODE", nullifyParentLevel = 0)
	private String manufacturerPartNumber;

	@EAMField(xpath = "manufacturerpartcode_new", nullifyParentLevel = 0)
	private String manufacturerPartNumberNew;
	
	public String getManufacturerCode() {
		return manufacturerCode;
	}
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getDrawingNumber() {
		return drawingNumber;
	}
	public void setDrawingNumber(String drawingNumber) {
		this.drawingNumber = drawingNumber;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getOutOfService() {
		return outOfService;
	}
	public void setOutOfService(Boolean outOfService) {
		this.outOfService = outOfService;
	}
	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}
	
	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}
	
	@Override
	public String toString() {
		return "PartManufacturer ["
				+ (manufacturerCode != null ? "manufacturerCode="
						+ manufacturerCode + ", " : "")
				+ (partCode != null ? "partCode=" + partCode + ", " : "")
				+ (drawingNumber != null ? "drawingNumber=" + drawingNumber
						+ ", " : "")
				+ (outOfService != null ? "outOfService=" + outOfService + ", "
						: "")
				+ (manufacturerPartNumber != null ? "manufacturerPartNumber="
						+ manufacturerPartNumber : "") + "]";
	}
	public String getManufacturerPartNumberNew() {
		return manufacturerPartNumberNew;
	}
	public void setManufacturerPartNumberNew(String manufacturerPartNumberNew) {
		this.manufacturerPartNumberNew = manufacturerPartNumberNew;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getPrimary() {
		return primary;
	}
	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}
	public String getManufacturerDesc() {
		return manufacturerDesc;
	}
	public void setManufacturerDesc(String manufacturerDesc) {
		this.manufacturerDesc = manufacturerDesc;
	}

	
}
