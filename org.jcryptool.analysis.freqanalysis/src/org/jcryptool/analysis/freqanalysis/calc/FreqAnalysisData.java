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
package org.jcryptool.analysis.freqanalysis.calc;

/**
 * This class contains the frequency analysis data for one character.
 * 
 * @author SLeischnig
 */
public class FreqAnalysisData {
    public FreqAnalysisData() {
    };

    public String charPrinted = ""; //$NON-NLS-1$
    public int absOcc = 0;
    public double relOcc = 0.0;
    public char ch = 'y';
}