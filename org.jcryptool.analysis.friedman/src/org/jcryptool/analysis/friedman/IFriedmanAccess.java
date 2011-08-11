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
 *
 */
public interface IFriedmanAccess {

	/** The remote control procedure - control this plugin by implementing this Interface on the frequency analysis view.
	 * @param executeCalc whether a calculation has now to be executed
	 */
	public void execute(boolean executeCalc);

}
