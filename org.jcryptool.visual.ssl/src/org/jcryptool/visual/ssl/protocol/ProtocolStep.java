//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
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
	
	/**
	 * Refreshes the information shown in stxInformation
	 */
	public void refreshInformations();
	
	/**
	 * Resets the step
	 */
	public void resetStep();
	
}
