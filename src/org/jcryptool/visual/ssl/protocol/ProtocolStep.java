package org.jcryptool.visual.ssl.protocol;

/**
 * @author Kapfer
 *
 */
public interface ProtocolStep 
{
	/**
	 * Disables all controls of the ProtocolStep
	 */
	public void disableControls();
	
	/**
	 * Enables all controls of the ProtocolStep
	 */
	public void enableControls();
	
	/**
	 * Checks if all chosen parameters are correct
	 * 
	 * @return
	 * 		false if parameters are incorrect
	 * 		true if parameters are correct
	 */
	public boolean checkParameters();
}
