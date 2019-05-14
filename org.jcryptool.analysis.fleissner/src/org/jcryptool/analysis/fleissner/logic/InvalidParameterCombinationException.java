package org.jcryptool.analysis.fleissner.logic;
/**
 * 
 */

/**
 * @author Dinah
 *
 */
public class InvalidParameterCombinationException extends Exception{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/**
	 * Instantiates a new person not found exception.
	 */
	public InvalidParameterCombinationException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new person not found exception.
	 *
	 * @param message the message
	 */
	public InvalidParameterCombinationException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new person not found exception.
	 *
	 * @param cause the cause
	 */
	public InvalidParameterCombinationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new person not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidParameterCombinationException(String message, Throwable cause) {
		super(message, cause);
	}
}
