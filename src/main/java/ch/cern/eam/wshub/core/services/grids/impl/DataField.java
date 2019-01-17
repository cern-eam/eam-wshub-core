package ch.cern.eam.wshub.core.services.grids.impl;

public class DataField {
	
	private String tagName;
	private String sourceName;
	private DataType dataType;
	private Boolean isUppercase;
	
	public DataField(String tagName, String sourceName, DataType dataType, Boolean isUppercase) {
		this.tagName = tagName;
		this.sourceName = sourceName;
		this.dataType = dataType;
		this.isUppercase = isUppercase;
	}
	
	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @param tagName the tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * @return the sourcename
	 */
	public String getSourcename() {
		return sourceName;
	}

	/**
	 * @param sourcename the sourcename to set
	 */
	public void setSourcename(String sourcename) {
		this.sourceName = sourcename;
	}

	/**
	 * @return the datatype
	 */
	public DataType getDatatype() {
		return dataType;
	}

	/**
	 * @param datatype the datatype to set
	 */
	public void setDatatype(DataType datatype) {
		this.dataType = datatype;
	}
	
	/**
	 * @return the isUppercase
	 */
	public Boolean isUppercase() {
		return isUppercase;
	}

	/**
	 * @param uppercase the isUppercase to set
	 */
	public void setUppercase(Boolean uppercase) {
		this.isUppercase = uppercase;
	}

}
