package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.services.entities.CustomField;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.Arrays;

public class Location implements Serializable {

	private String code;
	private String desc;
	private CustomField[] customFields;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@XmlElementWrapper(name="customFields") 
    @XmlElement(name="customField")
	public CustomField[] getCustomFields() {
		return customFields;
	}
	public void setCustomFields(CustomField[] customFields) {
		this.customFields = customFields;
	}
	@Override
	public String toString() {
		return "Location ["
				+ (code != null ? "code=" + code + ", " : "")
				+ (desc != null ? "desc=" + desc + ", " : "")
				+ (customFields != null ? "customFields="
						+ Arrays.toString(customFields) : "") + "]";
	}

	
}
