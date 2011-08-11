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
package org.jcryptool.analysis.transpositionanalysis.calc;




/**
 * The Analysis class provides arbitrary framework for cryptographic
 * analysis 
 * 
 * @author Simon L
 *
 */
public abstract class Analysis {
	/**
	 * @return a short, descriptive name of the analysis
	 */
	public abstract String getAnalysisName();
}