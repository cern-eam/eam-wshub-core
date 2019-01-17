package ch.cern.eam.wshub.core.services.grids.exceptions;

public class IncorrectSortTypeException extends Exception{

	private static final long serialVersionUID = -3900921367088916571L;

	public IncorrectSortTypeException() {}
	
	public IncorrectSortTypeException(String message){
		super(message);
	}

}
