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

	@GridField(name = "plo_elementtype")
	private String elementType;

	@GridField(name = "plo_fieldconttype")
	private String fieldContType;

	@GridField(name = "plo_positioningroup")
	private BigInteger positionInGroup;

	@GridField(name = "plo_tabindex")
	private BigInteger tabIndex;

	@GridField(name = "pld_onlookup")
	private String onLookup;


	public String getElementId() {
		return elementId;
	}

	public void setElementId(final String elementId) {
		this.elementId = elementId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(final String pageName) {
		this.pageName = pageName;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(final String xpath) {
		this.xpath = xpath;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(final String maxLength) {
		this.maxLength = maxLength;
	}

	public String getCharacterCase() {
		return characterCase;
	}

	public void setCharacterCase(final String characterCase) {
		this.characterCase = characterCase;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(final String attribute) {
		this.attribute = attribute;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(final String userGroup) {
		this.userGroup = userGroup;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(final String fieldType) {
		this.fieldType = fieldType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getPresentInJSP() {
		return presentInJSP;
	}

	public void setPresentInJSP(final String presentInJSP) {
		this.presentInJSP = presentInJSP;
	}

	public String getFieldContainer() {
		return fieldContainer;
	}

	public void setFieldContainer(final String fieldContainer) {
		this.fieldContainer = fieldContainer;
	}

	public BigInteger getFieldGroup() {
		return fieldGroup;
	}

	public void setFieldGroup(final BigInteger fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public String getUdfLookupType() {
		return udfLookupType;
	}

	public void setUdfLookupType(final String udfLookupType) {
		this.udfLookupType = udfLookupType;
	}

	public String getUdfLookupEntity() {
		return udfLookupEntity;
	}

	public void setUdfLookupEntity(final String udfLookupEntity) {
		this.udfLookupEntity = udfLookupEntity;
	}

	public String getUdfUom() {
		return udfUom;
	}

	public void setUdfUom(final String udfUom) {
		this.udfUom = udfUom;
	}

	public boolean isReadonly() {
		return this.readonly || "P".equals(attribute);
	}

	public void setReadonly(final boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isNotValid() {
		return notValid;
	}

	public void setNotValid(final boolean notValid) {
		this.notValid = notValid;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(final String elementType) {
		this.elementType = elementType;
	}

	public String getFieldContType() {
		return fieldContType;
	}

	public void setFieldContType(final String fieldContType) {
		this.fieldContType = fieldContType;
	}

	public BigInteger getPositionInGroup() {
		return positionInGroup;
	}

	public void setPositionInGroup(final BigInteger positionInGroup) {
		this.positionInGroup = positionInGroup;
	}

	public BigInteger getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(final BigInteger tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getOnLookup() {
		return onLookup;
	}

	public void setOnLookup(final String onLookup) {
		this.onLookup = onLookup;
	}

	@Override
	public String toString() {
		return "ElementInfo{" +
				"elementId='" + elementId + '\'' +
				", pageName='" + pageName + '\'' +
				", xpath='" + xpath + '\'' +
				", maxLength='" + maxLength + '\'' +
				", characterCase='" + characterCase + '\'' +
				", attribute='" + attribute + '\'' +
				", userGroup='" + userGroup + '\'' +
				", fieldType='" + fieldType + '\'' +
				", defaultValue='" + defaultValue + '\'' +
				", presentInJSP='" + presentInJSP + '\'' +
				", fieldContainer='" + fieldContainer + '\'' +
				", fieldGroup=" + fieldGroup +
				", text='" + text + '\'' +
				", udfLookupType='" + udfLookupType + '\'' +
				", udfLookupEntity='" + udfLookupEntity + '\'' +
				", udfUom='" + udfUom + '\'' +
				", readonly=" + readonly +
				", notValid=" + notValid +
				", elementType='" + elementType + '\'' +
				", fieldContType='" + fieldContType + '\'' +
				", positionInGroup=" + positionInGroup +
				", tabIndex=" + tabIndex +
				'}';
	}
}
