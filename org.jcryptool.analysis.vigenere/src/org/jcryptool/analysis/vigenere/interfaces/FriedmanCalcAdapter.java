/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.interfaces;

import org.jcryptool.analysis.friedman.calc.FriedmanCalc;

/**
 * A container for {@link FriedmanCalc} of Friedman test plug-in.
 * <p>
 * The named object is wrapped to minimize later changes in source code. This
 * means that by changes in imported classes of Friedman test plug-in, only
 * adjustment in this (and/or depended) adaptor classes are necessary.
 * 
 * @author Ronny Wolf
 * @version 0.0.1, 2010/07/08
 */
public class FriedmanCalcAdapter {

    /**
     * Contains the {@link FriedmanCalc} object.
     */
    private FriedmanCalc object;

    /**
     * Constructs a new adaptor for the calculation of the Friedman test.
     * 
     * @param chiffre
     *            the chiffre text to analyze.
     */
    public FriedmanCalcAdapter(final String chiffre) {
        int max = Math.min(chiffre.length(), 2000);
        this.object = new FriedmanCalc(chiffre.toUpperCase(), max);
    }

    /**
     * Returns the content of this adaptor.
     * 
     * @return the object the contained object.
     */
    protected FriedmanCalc getAdaptorObject() {
        return object;
    }

    // TODO
    public double[] getAnalysis() {
        return object.getAnalysis();
    }
}
