package ch.cern.eam.wshub.core.services.grids.impl;

public enum Operator {
	BEGINS("BEGINS"),
	ENDS("ENDS"),
	IN("IN"),
	ALT_IN("ALT_IN"),
	NOT_IN("NOT_IN"),
	LIKE("LIKE"),
	CONTAINS("CONTAINS"),
	NOT_CONTAINS("NOTCONTAINS"),
	EQUALS("="),
	NOT_EQUAL("!="),
	IS_EMPTY("IS EMPTY"),
	NOT_EMPTY("NOT EMPTY"),
	LESS_THAN("<"),
	GREATER_THAN(">"),
	LESS_THAN_EQUALS("<="),
	GREATER_THAN_EQUALS(">="),
	SELECTED("-1"),
	NOT_SELECTED("0");
	
	private String value;
	
	private Operator(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public String toString(){
		return this.value;
	}
	
	public static Operator fromString(String value){
		
		// first check operator's name
		try {
			return Operator.valueOf(value);
		} catch (IllegalArgumentException e) {
			// do nothing
		}
		
		// then try with operator's value
		if(value != null){
			for(Operator op : Operator.values()){
				if(op.value.equalsIgnoreCase(value)){
					return op;
				}
			}
		}
		
		// if no result given then throws exception
		throw new IllegalArgumentException("No constant with value " + value + " found.");
	}
}
