package ch.cern.eam.wshub.core.services.grids.entities;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;

public class GridRequestFilter implements Serializable {
	private static final long serialVersionUID = 2336324664740111857L;

	public enum JOINER {AND, OR};

	private String fieldName;
	private String fieldValue;
	private String operator;
	private JOINER joiner;
	private Boolean leftParenthesis;
	private Boolean rightParenthesis;
	private Boolean forceCaseInsensitive = false;
	private Boolean upperCase = false;

	public GridRequestFilter(String fieldName, String fieldValue, String operator) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.operator = operator;
	}

	public GridRequestFilter(String fieldName, String fieldValue, String operator, JOINER joiner) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.operator = operator;
		this.joiner = joiner;
	}

	public GridRequestFilter(String fieldName, String fieldValue, String operator, JOINER joiner,
							 Boolean leftParenthesis, Boolean rightParenthesis) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.operator = operator;
		this.joiner = joiner;
		this.leftParenthesis = leftParenthesis;
		this.rightParenthesis = rightParenthesis;
	}
	
	public GridRequestFilter(String fieldName, String fieldValue, String operator, JOINER joiner,
							 Boolean leftParenthesis, Boolean rightParenthesis, Boolean forceCaseInsensitive, Boolean upperCase) {
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

	public Boolean getLeftParenthesis() {
		return leftParenthesis;
	}

	public void setLeftParenthesis(Boolean leftParenthesis) {
		this.leftParenthesis = leftParenthesis;
	}

	public Boolean getRightParenthesis() {
		return rightParenthesis;
	}

	public void setRightParenthesis(Boolean rightParenthesis) {
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

	public JOINER getJoiner() {
		return joiner;
	}

	public void setJoiner(JOINER joiner) {
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
