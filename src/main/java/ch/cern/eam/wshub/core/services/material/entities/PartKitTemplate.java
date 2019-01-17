package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;

public class PartKitTemplate implements Serializable {
	private static final long serialVersionUID = 7865040704362527306L;

	private String partCode;
	private String kitTemplatePartCode;
	private String qty;
	private String uomCode;
	
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getKitTemplatePartCode() {
		return kitTemplatePartCode;
	}
	public void setKitTemplatePartCode(String kitTemplatePartCode) {
		this.kitTemplatePartCode = kitTemplatePartCode;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getUomCode() {
		return uomCode;
	}
	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kitTemplatePartCode == null) ? 0 : kitTemplatePartCode.hashCode());
		result = prime * result + ((partCode == null) ? 0 : partCode.hashCode());
		result = prime * result + ((qty == null) ? 0 : qty.hashCode());
		result = prime * result + ((uomCode == null) ? 0 : uomCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartKitTemplate other = (PartKitTemplate) obj;
		if (kitTemplatePartCode == null) {
			if (other.kitTemplatePartCode != null)
				return false;
		} else if (!kitTemplatePartCode.equals(other.kitTemplatePartCode))
			return false;
		if (partCode == null) {
			if (other.partCode != null)
				return false;
		} else if (!partCode.equals(other.partCode))
			return false;
		if (qty == null) {
			if (other.qty != null)
				return false;
		} else if (!qty.equals(other.qty))
			return false;
		if (uomCode == null) {
			if (other.uomCode != null)
				return false;
		} else if (!uomCode.equals(other.uomCode))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PartKitTemplate [partCode=" + partCode + ", kitTemplatePartCode=" + kitTemplatePartCode + ", qty=" + qty
				+ ", uomCode=" + uomCode + "]";
	}  
	
}
