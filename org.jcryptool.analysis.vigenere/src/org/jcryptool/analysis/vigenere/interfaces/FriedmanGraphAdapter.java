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
import org.jcryptool.analysis.friedman.ui.CustomFriedmanCanvas;

/**
 * A container for {@link CustomFriedmanCanvas} of Friedman test plug-in.
 * <p>
 * The named object is wrapped to minimize later changes in source code. This
 * means that by changes in imported classes of Friedman test plug-in, only
 * adjustment in this (and/or depended) adaptor classes are necessary.
 * 
 * @author Ronny Wolf
 * @version 0.0.1, 2010/07/08
 */
public class FriedmanGraphAdapter {
    private final CustomFriedmanCanvas object;
    private FriedmanCalcAdapter calc;

    /**
     * Constructs a new adaptor for the graphical visualization of the Friedman
     * test.
     * 
     * @param parent
     *            the composite object (widget) to which this graph is added.
     */
    public FriedmanGraphAdapter(final Composite parent) {
        this.object = new CustomFriedmanCanvas(parent, SWT.DOUBLE_BUFFERED);
        setLayout();
    }

    /**
     * Sets layout specification for this graph.
     */
    private void setLayout() {
        GridLayout objectLayout = new GridLayout();
        object.setLayout(objectLayout);
        GridData objectLData = new GridData();
        objectLData.verticalAlignment = GridData.FILL;
        objectLData.grabExcessHorizontalSpace = true;
        objectLData.horizontalAlignment = GridData.FILL;
        objectLData.grabExcessVerticalSpace = true;
        object.setLayoutData(objectLData);
    }

    /**
     * Runs Friedman analysis on this chiffre text and redraws the graphical
     * visualization after getting results.
     * 
     * @param chiffre
     *            the text to analyze.
     */
    public void showCalculation(final String chiffre) {
        this.calc = new FriedmanCalcAdapter(chiffre);
        object.setAnalysis(calc.getAdaptorObject());
        object.redraw();
    }

    public double[] getAnalysis() {
        return calc.getAnalysis();
    }
}
