package ch.cern.eam.wshub.core.services.grids.entities;

import java.io.Serializable;

public class GridRequestFilter implements Serializable {
	private static final long serialVersionUID = 2336324664740111857L;

	private String fieldName;
	private String fieldValue;
	private String operator;
	private String joiner;
	private String leftParenthesis;
	private String rightParenthesis;
	private Boolean forceCaseInsensitive = false;
	private Boolean upperCase = false;
	
	public GridRequestFilter(String fieldName, String fieldValue, String operator, String joiner,
			String leftParenthesis, String rightParenthesis) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.operator = operator;
		this.joiner = joiner;
		this.leftParenthesis = leftParenthesis;
		this.rightParenthesis = rightParenthesis;
	}
	
	public GridRequestFilter(String fieldName, String fieldValue, String operator, String joiner,
			String leftParenthesis, String rightParenthesis, Boolean forceCaseInsensitive, Boolean upperCase) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.operator = operator;
		this.joiner = joiner;
		this.leftParenthesis = leftParenthesis;
		this.rightParenthesis = rightParenthesis;
		this.forceCaseInsensitive = forceCaseInsensitive!=null && forceCaseInsensitive;
		this.upperCase = upperCase!=null && upperCase;
	}
	
	public GridRequestFilter(){};

	public String getLeftParenthesis() {
		return leftParenthesis;
	}

	public void setLeftParenthesis(String leftParenthesis) {
		this.leftParenthesis = leftParenthesis;
	}

	public String getRightParenthesis() {
		return rightParenthesis;
	}

	public void setRightParenthesis(String rightParenthesis) {
		this.rightParenthesis = rightParenthesis;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getJoiner() {
		return joiner;
	}

	public void setJoiner(String joiner) {
		this.joiner = joiner;
	}
	
	public Boolean getForceCaseInsensitive() {
		return forceCaseInsensitive;
	}
	
	public void setForceCaseInsensitive(Boolean forceCaseInsensitive) {
		this.forceCaseInsensitive = forceCaseInsensitive!=null && forceCaseInsensitive;
	}
	
	public Boolean getUpperCase() {
		return upperCase;
	}
	
	public void setUpperCase(Boolean upperCase) {
		this.upperCase = upperCase;
	}

	@Override
	public String toString() {
		return "GridRequestFilter [fieldName=" + fieldName + ", fieldValue=" + fieldValue + ", operator=" + operator
				+ ", joiner=" + joiner + ", leftParenthesis=" + leftParenthesis + ", rightParenthesis="
				+ rightParenthesis + ", forceCaseInsensitive=" + forceCaseInsensitive + ", upperCase=" + upperCase + "]";
	}

}
