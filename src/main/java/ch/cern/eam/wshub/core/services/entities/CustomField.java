package ch.cern.eam.wshub.core.services.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.Arrays;
import java.util.StringJoiner;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class CustomField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3069792089040946708L;
	private String code;
	private String value;
	private String valueDesc;
	private String type;
	private String label;
	private String systemEntity;
	private String UOM;
	private String rentCodeValue;
	private String[] cfc;
	private String[] cfd;
	private String minValue;
	private String maxValue;
	private String entityCode;
	private String classCode;
	private String lovType;
	private String lovValidate;

	public CustomField() {
	}

	public CustomField(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSystemEntity() {
		return systemEntity;
	}

	public void setSystemEntity(String systemEntity) {
		this.systemEntity = systemEntity;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String uOM) {
		UOM = uOM;
	}

	@XmlElementWrapper(name = "possibleCodes")
	@XmlElement(name = "pcode")
	public String[] getCfc() {
		return cfc;
	}

	public void setCfc(String[] cfc) {
		this.cfc = cfc;
	}

	@XmlElementWrapper(name = "possibleDescs")
	@XmlElement(name = "pdesc")
	public String[] getCfd() {
		return cfd;
	}

	public void setCfd(String[] cfd) {
		this.cfd = cfd;
	}

	public String getRentCodeValue() {
		return rentCodeValue;
	}

	public void setRentCodeValue(String rentCodeValue) {
		this.rentCodeValue = rentCodeValue;
	}

	public String getValueDesc() {
		return valueDesc;
	}

	public void setValueDesc(String valueDesc) {
		this.valueDesc = valueDesc;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getLovType() {
		return lovType;
	}

	public void setLovType(String lovType) {
		this.lovType = lovType;
	}

	public String getLovValidate() {
		return lovValidate;
	}

	public void setLovValidate(String lovValidate) {
		this.lovValidate = lovValidate;
	}

	@Override
	public String toString() {
		return "CustomField [" + (code != null ? "code=" + code + ", " : "")
				+ (value != null ? "value=" + value + ", " : "")
				+ (valueDesc != null ? "valueDesc=" + valueDesc + ", " : "")
				+ (type != null ? "type=" + type + ", " : "") + (label != null ? "label=" + label + ", " : "")
				+ (systemEntity != null ? "systemEntity=" + systemEntity + ", " : "")
				+ (UOM != null ? "UOM=" + UOM + ", " : "")
				+ (rentCodeValue != null ? "rentCodeValue=" + rentCodeValue + ", " : "")
				+ (cfc != null ? "cfc=" + Arrays.toString(cfc) + ", " : "")
				+ (cfd != null ? "cfd=" + Arrays.toString(cfd) + ", " : "")
				+ (minValue != null ? "minValue=" + minValue + ", " : "")
				+ (maxValue != null ? "maxValue=" + maxValue + ", " : "")
				+ (entityCode != null ? "entityCode=" + entityCode + ", " : "")
				+ (classCode != null ? "classCode=" + classCode + ", " : "")
				+ (lovType != null ? "lovType=" + lovType + ", " : "")
				+ (lovValidate != null ? "lovValidate=" + lovValidate : "") + "]";
	}

}
