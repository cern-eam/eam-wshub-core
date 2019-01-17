package ch.cern.eam.wshub.core.services.grids.exceptions;

/**
 * Exception for missing joiner on the grid filter list defined by user.
 *
 */
public class MissingJoinerGridFilterException extends Exception{

	private static final long serialVersionUID = 5530665379658288148L;

	public MissingJoinerGridFilterException() {}

	public MissingJoinerGridFilterException(String message) {
		super(message);
	}
	
}
