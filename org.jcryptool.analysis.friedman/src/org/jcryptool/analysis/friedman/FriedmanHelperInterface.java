//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.friedman;

/**
 * @author SLeischnig
 * Interface for accessing the Vigenère helper.
 */
public interface FriedmanHelperInterface {

	/** exports a result from an analysis plugin to the Vigenère helper.
	 * @param pluginID the exporting plugin's ID
	 * @param result the result to be exported
	 */
	public void setExtResult(String pluginID, String result);

}
