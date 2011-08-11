/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.interfaces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.analysis.freqanalysis.ui.CustomFreqCanvas;

public class FrequencyGraphAdapter {
    private final CustomFreqCanvas object;
    private FrequencyCalcAdapter compare;
    private FrequencyCalcAdapter calc;

    public FrequencyGraphAdapter(final Composite comp) {
        this.object = new CustomFreqCanvas(comp, SWT.NONE);
        setLayout();
    }

    private void setLayout() {
        GridLayout myGraphLayout = new GridLayout();
        object.setLayout(myGraphLayout);
        GridData myGraphLData = new GridData();
        myGraphLData.verticalAlignment = GridData.FILL;
        myGraphLData.grabExcessVerticalSpace = true;
        myGraphLData.grabExcessHorizontalSpace = true;
        myGraphLData.horizontalAlignment = GridData.FILL;
        object.setLayoutData(myGraphLData);
    }

    public void setCalculation(final String chiffre, final int lenght,
            final int number, final String alphabet) {
        calc = new FrequencyCalcAdapter(chiffre, lenght, number - 1, alphabet);
        object.setAnalysis(calc.getAdaptorObject());
    }

    public void setComparator(final String reference, final String alphabet) {
        compare = new FrequencyCalcAdapter(reference, 1, 0, alphabet);
        object.setOverlayAnalysis(compare.getAdaptorObject());

    }

    public void activateComparator() {
        object.setOverlayActivated(true);
    }

    public void refresh() {
        object.redraw();
        object.getFrequencyGraph().resetDrag();
    }

    public int getShift() {
        return object.getFrequencyGraph().getCurrentShift();
    }

    /**
     * @return the object
     */
    protected CustomFreqCanvas getAdaptorObject() {
        return object;
    }

    public FrequencyData[] getAnalyzedData() {
        return calc.getAnalyzedData();
    }

    public Composite getParent() {
        return object.getParent();
    }
}
