package ch.cern.eam.wshub.core.services.grids.exceptions;

public class IncorrectParenthesesGridFilterException extends Exception {

	private static final long serialVersionUID = -4312787796654648282L;

	public IncorrectParenthesesGridFilterException() {}
	
	public IncorrectParenthesesGridFilterException(String message){
		super(message);
	}

}
