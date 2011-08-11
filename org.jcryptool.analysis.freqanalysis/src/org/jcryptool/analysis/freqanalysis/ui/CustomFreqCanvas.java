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
package org.jcryptool.analysis.freqanalysis.ui;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.analysis.freqanalysis.calc.FreqAnalysisCalc;
import org.jcryptool.analysis.freqanalysis.calc.FreqAnalysisData;
import org.jcryptool.analysis.graphtools.derivates.LabelBar;
import org.jcryptool.analysis.graphtools.derivates.OverlayLabelBar;

/**
 * @author SLeischnig
 *
 */
public class CustomFreqCanvas extends org.eclipse.swt.widgets.Canvas implements PaintListener,
        MouseMoveListener {

    private Composite mycomp;
    private FreqAnalysisCalc myAnalysis, myOverlayAnalysis;
    // Flag if mouseover shows details for a single bar...
    private boolean overlay = false;

    // Class for drawing calculations
    private FreqAnalysisGraph frequencyGraph = new FreqAnalysisGraph(null, this.getSize().x, this
            .getSize().y);

    // graph dragging variables
    private boolean draggingEnabled = true;
    private boolean dragging = false;
    private int dragBeginX;

    public final void setOverlayActivated(final boolean isOverlay) {
        overlay = isOverlay;
        buildBars();
    }

    public CustomFreqCanvas(final org.eclipse.swt.widgets.Composite parent, final int style) {
        super(parent, SWT.DOUBLE_BUFFERED);

        mycomp = parent;

        // myAnalysis = analysis;
        setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
        int width = 0, height = 0;
        width = mycomp.getSize().x;
        height = mycomp.getSize().y;
        setSize(width, height);

        myAnalysis = new FreqAnalysisCalc("", 1, 0, null); //$NON-NLS-1$
        myOverlayAnalysis = new FreqAnalysisCalc("", 1, 0, null); //$NON-NLS-1$

        addPaintListener(this);
        this.addPaintListener(this);
        this.addMouseMoveListener(this);

        this.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(final MouseEvent evt) {
                dragging = false;
                if (getFrequencyGraph().setDraggedPixels(0, false)) {
                    redraw(); // reset the drag
                }
            }
        });
        this.addMouseListener(new MouseAdapter() {
            public void mouseUp(final MouseEvent evt) {
                if (dragging && draggingEnabled) {
                    int myPixelsDifference = evt.x - dragBeginX;
                    getFrequencyGraph().setDraggedPixels(myPixelsDifference, false);
                }
                dragging = false;
            }

            public void mouseDown(final MouseEvent evt) {
                dragging = true;
                dragBeginX = evt.x;
            }

            public void mouseDoubleClick(final MouseEvent evt) {
                getFrequencyGraph().resetDrag();
                redraw();
            }
        });
    }

    /**
     * sets the Frequency analysis
     */
    public final void setAnalysis(final FreqAnalysisCalc in) {
        myAnalysis = in;
        buildBars();
    }

    /**
     * Sets the overlay Analysis
     */
    public final void setOverlayAnalysis(final FreqAnalysisCalc in) {
        myOverlayAnalysis = in;
        buildBars();
    }

    /**
     * Generates the bars for the drawing procedures
     */
    private void buildBars() {
        class CustomFreqAnalysisData extends FreqAnalysisData {
            boolean isOverlayBar;

            public CustomFreqAnalysisData() {
                super();
            }

            public void copySuper(final FreqAnalysisData in) {
                this.absOcc = in.absOcc;
                this.ch = in.ch;
                this.charPrinted = in.charPrinted;
                this.relOcc = in.relOcc;
            }
        }

        /**
         * @author SLeischnig comparates two frequency analysis data sets for sorting
         */
        class MyComparator implements Comparator<FreqAnalysisData> {
            public int compare(final FreqAnalysisData a, final FreqAnalysisData b) {
                if ((int) (a.ch) > (int) (b.ch)) {
                    return 1;
                }
                if ((int) (a.ch) == (int) (b.ch)) {
                    return 0;
                }
                return -1;
            }
            // No need to override equals.
        }

        // Copy all Analysis Data in one Array
        CustomFreqAnalysisData[] allData = new CustomFreqAnalysisData[myAnalysis.getAnalysisArray().length
                + myOverlayAnalysis.getAnalysisArray().length];
        for (int i = 0; i < myAnalysis.getAnalysisArray().length; i++) {
            allData[i] = new CustomFreqAnalysisData();
            allData[i].copySuper(myAnalysis.getAnalysisArray()[i]);
            allData[i].isOverlayBar = false;
        }
        for (int i = 0; i < myOverlayAnalysis.getAnalysisArray().length; i++) {
            allData[myAnalysis.getAnalysisArray().length + i] = new CustomFreqAnalysisData();
            allData[myAnalysis.getAnalysisArray().length + i].copySuper(myOverlayAnalysis
                    .getAnalysisArray()[i]);
            allData[myAnalysis.getAnalysisArray().length + i].isOverlayBar = true;
        }

        // sort all Analysis Data in view of its character values (see comparator class)
        Arrays.sort(allData, new MyComparator());

        char actualChar = (char) 0;
        int actualIndex = -1;
        double maxValue = Math.max(max(myAnalysis.getAnalysisArray()), max(myOverlayAnalysis
                .getAnalysisArray()));
        double barHeight = 0;

        // generate Bars
        getFrequencyGraph().resetBars();
        for (int i = 0; i < allData.length; i++) {
            // Only draw overlay bars when it's activated
            if (overlay || (!allData[i].isOverlayBar)) {
                if (allData[i].ch != actualChar) {
                    actualChar = allData[i].ch;
                    actualIndex++;
                }
                barHeight = (allData[i].relOcc) / (maxValue);

                if (!allData[i].isOverlayBar) {
                    getFrequencyGraph().addBar(
                            new LabelBar(barHeight, actualIndex, 10,
                                    "" + allData[i].absOcc, allData[i].charPrinted)); //$NON-NLS-1$
                } else {
                    getFrequencyGraph().addBar(
                            new OverlayLabelBar(barHeight, actualIndex, 11,
                                    "" + allData[i].absOcc, allData[i].charPrinted)); //$NON-NLS-1$
                }
            }
        }
    }

    /**
     * returns the maximum relative occurence in the frequency analysis set.
     *
     * @param t the frequency analysis set
     * @return the highest relative occurence in the set
     */
    private static double max(final FreqAnalysisData[] t) {
        double maximum = -9999; // start with the first value
        for (int i = 1; i < t.length; i++) {
            if (t[i].relOcc > maximum) {
                maximum = t[i].relOcc; // new maximum
            }
        }
        return maximum;
    }

    public final void paintControl(final PaintEvent e) {
        GC gc = e.gc;
        int width = 0, height = 0;
        width = this.getSize().x;
        height = this.getSize().y;

        getFrequencyGraph().setGC(gc);
        getFrequencyGraph().updateBounds(width, height);

        getFrequencyGraph().paintArea();
    }

    public final void mouseMove(final MouseEvent e) {

        if (dragging && draggingEnabled) {
            int myPixelsDifference = e.x - dragBeginX;
            if (getFrequencyGraph().setDraggedPixels(myPixelsDifference, true)) {
                redraw();
            }
        }

    }

    public final void setDraggingEnabled(final boolean dragEnabled) {
        this.draggingEnabled = dragEnabled;
    }

    public final boolean isDraggingEnabled() {
        return draggingEnabled;
    }

    public final void setFrequencyGraph(final FreqAnalysisGraph frequencyGraph) {
        this.frequencyGraph = frequencyGraph;
    }

    public final FreqAnalysisGraph getFrequencyGraph() {
        return frequencyGraph;
    }

}
