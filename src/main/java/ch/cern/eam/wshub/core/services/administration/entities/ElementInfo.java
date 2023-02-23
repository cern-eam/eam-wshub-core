package ch.cern.eam.wshub.core.services.administration.entities;

import ch.cern.eam.wshub.core.annotations.GridField;

import java.math.BigInteger;

public class ElementInfo {


	@GridField(name="plo_elementid")
	private String elementId;
	@GridField(name="plo_pagename")
	private String pageName;
	@GridField(name="pld_xpath")
	private String xpath;
	@GridField(name="pld_maxlength")
	private String maxLength;
	@GridField(name="pld_case")
	private String characterCase;
	// H = Hidden, O = Optional, R = Required, S = System Required,
	@GridField(name="plo_attribute")
	private String attribute;
	private String userGroup;
	// text, date, integer, number, button ...
	@GridField(name="pld_fieldtype")
	private String fieldType;
	//
	@GridField(name="plo_defaultvalue")
	private String defaultValue;

	@GridField(name="plo_presentinjsp")
	private String presentInJSP;

	@GridField(name="plo_fieldcontainer")
	private String fieldContainer;

	@GridField(name="plo_fieldgroup")
	private BigInteger fieldGroup;
	// Label
	private String text;
	// Lookup type for UDFs
	private String udfLookupType;
	// Lookup entity for UDFs
	private String udfLookupEntity;
	// UOM from UDF
	private String udfUom;
	private boolean readonly;
	private boolean notValid;

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getCharacterCase() {
		return characterCase;
	}

	public void setCharacterCase(String characterCase) {
		this.characterCase = characterCase;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	public boolean isReadonly() {
		return this.readonly || "P".equals(attribute);
	}

	public void setReadonly(boolean readOnly) {
		this.readonly = readOnly;
	}

	public boolean isNotValid() {
		return notValid;
	}

	public void setNotValid(boolean notValid) {
		this.notValid = notValid;
	}

	/**
	 * @return the udfLookupType
	 */
	public String getUdfLookupType() {
		return udfLookupType;
	}

	/**
	 * @param udfLookupType
	 *            the udfLookupType to set
	 */
	public void setUdfLookupType(String udfLookupType) {
		this.udfLookupType = udfLookupType;
	}

	/**
	 * @return the udfLookupEntity
	 */
	public String getUdfLookupEntity() {
		return udfLookupEntity;
	}

	/**
	 * @param udfLookupEntity
	 *            the udfLookupEntity to set
	 */
	public void setUdfLookupEntity(String udfLookupEntity) {
		this.udfLookupEntity = udfLookupEntity;
	}

	/**
	 * @return the udfUom
	 */
	public String getUdfUom() {
		return udfUom;
	}

	/**
	 * @param udfUom
	 *            the udfUom to set
	 */
	public void setUdfUom(String udfUom) {
		this.udfUom = udfUom;
	}

	public String getPresentInJSP() {
		return presentInJSP;
	}

	public void setPresentInJSP(String presentInJSP) {
		this.presentInJSP = presentInJSP;
	}

	public BigInteger getFieldGroup() {
		return fieldGroup;
	}

	public void setFieldGroup(BigInteger fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

	public String getFieldContainer() {
		return fieldContainer;
	}

	public void setFieldContainer(String fieldContainer) {
		this.fieldContainer = fieldContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ElementInfo [" + (elementId != null ? "elementId=" + elementId + ", " : "")
				+ (pageName != null ? "pageName=" + pageName + ", " : "")
				+ (xpath != null ? "xpath=" + xpath + ", " : "")
				+ (maxLength != null ? "maxLength=" + maxLength + ", " : "")
				+ (characterCase != null ? "characterCase=" + characterCase + ", " : "")
				+ (attribute != null ? "attribute=" + attribute + ", " : "")
				+ (userGroup != null ? "userGroup=" + userGroup + ", " : "")
				+ (fieldType != null ? "fieldType=" + fieldType + ", " : "")
				+ (defaultValue != null ? "defaultValue=" + defaultValue + ", " : "")
				+ (text != null ? "text=" + text + ", " : "")
				+ (udfLookupType != null ? "udfLookupType=" + udfLookupType + ", " : "")
				+ (udfLookupEntity != null ? "udfLookupEntity=" + udfLookupEntity + ", " : "")
				+ (udfUom != null ? "udfUom=" + udfUom + ", " : "") + "readonly=" + readonly + ", notValid=" + notValid
				+ "]";
	}

}
