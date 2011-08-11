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
package org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model;

import org.jcryptool.analysis.transpositionanalysis.calc.AnalysisInput;

public interface TranspositionAnalysisInput extends AnalysisInput {
	
	public boolean isUserEstimatedAnalysisWeight();
	public double getUserEstimatedAnalysisWeight();
	public String getCiphertext();
	
}