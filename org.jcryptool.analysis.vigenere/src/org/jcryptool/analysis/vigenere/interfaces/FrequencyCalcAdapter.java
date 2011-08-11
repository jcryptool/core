/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.jcryptool.analysis.freqanalysis.calc.FreqAnalysisCalc;
import org.jcryptool.analysis.freqanalysis.calc.FreqAnalysisData;

/**
 * A container for {@link FreqAnalysisCalc} of frequency analysis plug-in.
 * <p>
 * The named object is wrapped to minimize later changes in source code. This
 * means that by changes in imported classes of frequency analysis plug-in, only
 * adjustment in this (and/or depended) adaptor classes are necessary.
 * 
 * @author Ronny Wolf
 * @version 0.0.1, 2010/07/08
 */
public class FrequencyCalcAdapter {

    /**
     * Contains the {@link FreqAnalysisCalc} object.
     */
    private final FreqAnalysisCalc object;

    /**
     * Constructs a new adaptor for the calculation of the frequency analysis.
     * 
     * @param chiffre
     *            the chiffre text to analyze.
     * @param passlength
     *            the total length of the pass phrase.
     * @param charno
     *            the number of the character of pass phase to analyze.
     * @param alphabet
     *            the reference alphabet for the analysis.
     */
    public FrequencyCalcAdapter(final String chiffre, final int passlength,
            final int charno, final String alphabet) {
        this.object = new FreqAnalysisCalc(chiffre, passlength, charno, null,
                alphabet);
    }

    /**
     * Returns the content of this adaptor.
     * 
     * @return the object the contained object.
     */
    protected FreqAnalysisCalc getAdaptorObject() {
        return object;
    }

    public FrequencyData[] getAnalyzedData() {
        List<FrequencyData> list = new ArrayList<FrequencyData>();
        FreqAnalysisData[] data = object.getAnalysisArray();

        for (FreqAnalysisData d : data) {
            list.add(new FrequencyData(d.ch, d.absOcc));
        }

        return list.toArray(new FrequencyData[list.size()]);
    }
}
