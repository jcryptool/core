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

import java.util.Vector;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.jcryptool.analysis.graphtools.Bar;
import org.jcryptool.analysis.graphtools.Graph;
import org.jcryptool.analysis.graphtools.MColor;
import org.jcryptool.analysis.graphtools.derivates.OverlayBar;
import org.jcryptool.analysis.graphtools.derivates.OverlayLabelBar;

/**
 * @author SLeischnig
 *
 */
public class FreqAnalysisGraph extends Graph {
    /**
     * Differences: - Definition of width and color: - Usage of Specific Bars: LabelBars -
     * mouseover-Display of the top labels - Always displaying the lower labels - OverlayBars -
     * shaded Bars Adapts with shifting events from other controlling classes.
     */

    protected Vector<Bar> barsBeforeSort = new Vector<Bar>(0, 1);

    // global design settings
    // double barWidth, overlayBarWidth;
    int overlayTranspHighest, overlayTranspLowest;
    MColor overlayBarColor;
    double overlayBarWidth = 1.0;
    private int currentShift = 0;
    private int savedShift = 0;

    private boolean dragged = false;

    /**
     * In this method, standard settings from the super class are overridden
     */
    private void iniSettings() {

        overlayTranspHighest = 200;
        overlayTranspLowest = 10;

        distTop = 16;
        distBottom = 16;
        distLeft = 0;
        distRight = 0;
        marginTop = 20;
        marginBottom = 0;
        marginLeft = 20;
        marginRight = 20;
        calcAreas();

        descTopFontColor = new MColor("FFFFFF"/*"0095C9"*/, 255); //$NON-NLS-1$
        descBottomFontColor = new MColor("FFFFFF", 255); //$NON-NLS-1$
        descTopBGColor = new MColor("306A90", 255); //$NON-NLS-1$
        descBottomBGColor = new MColor("B60000", 255); //$NON-NLS-1$
        descLeftBGColor = new MColor("B60000", 255); //$NON-NLS-1$
        descRightBGColor = new MColor("B60000", 255); //$NON-NLS-1$
        barAreaBGColor = new MColor("306A90", 255); //$NON-NLS-1$
        overlayBarColor = new MColor("FFFFFF", 255); //$NON-NLS-1$
        //MColor("006AB0", 255);  //$NON-NLS-1$
    }

    public FreqAnalysisGraph(final GC gc, final int areaWidth, final int areaHeight) {
        super(gc, areaWidth, areaHeight);
        iniSettings();
    }

    /**
     * a correct modulo function
     */
    private int modulo(final int a, final int mod) {
        int howmany = 0;
        if (a < 0) {
            howmany = (int) Math.round(Math.ceil((-1.0) * (double) a / (double) mod));
            return a + howmany * mod;
        } else {
            return a % mod;
        }
    }

    /**
     * calculates the whole space a bar label can fill
     *
     * @param textSpace the space that is reserved for bar description labels
     * @param thisIndex the x-axis value of the bar label you want to calculate
     * @param maxIndex the highest x-value that occurs in this graph
     * @return the rectangle in which a text can be drawn
     */
    private Rectangle calculateTextContainer(final Rectangle textSpace, final int thisIndex,
            final int maxIndex) {
        double rectWidth = (double) textSpace.width / (double) (maxIndex + 1);
        double dRX = textSpace.x + rectWidth * thisIndex;
        double dRW = textSpace.x + rectWidth * (thisIndex + 1) - dRX;
        int RX = (int) Math.round(dRX);
        int RW = (int) Math.round(dRW);
        int RY = textSpace.y;
        int RH = textSpace.height;
        return new Rectangle(RX, RY, RW, RH);
    }

    /**
     * calculates the whole space a bar can fill, integrating the offset value field
     *
     * @param barSpace the space that is reserved for bars
     * @param thisIndex the x-axis value of the bar you want to calculate
     * @param maxIndex the highest x-value that occurs in this graph
     * @return the rectangle in which a bar can be drawn
     */
    protected final Rectangle calculateBarContainerShifted(final Rectangle barSpace,
            final int thisIndex, final int maxIndex) {
        int myIndex = (thisIndex + getCurrentShift()) % (maxIndex + 1);
        double rectWidth = (double) barSpace.width / (double) (maxIndex + 1);
        double dRX = barSpace.x + rectWidth * myIndex;
        double dRW = barSpace.x + rectWidth * (myIndex + 1) - dRX;
        int RX = (int) Math.round(dRX);
        int RW = (int) Math.round(dRW);
        int RY = barSpace.y;
        int RH = barSpace.height;
        return new Rectangle(RX, RY, RW, RH);
    }

    @Override
    protected final void paintBarArea(final Rectangle thisArea, final MColor thisBGColor) {
        // The bar drawing rectangle without margins
        Rectangle barDrawingRect = new Rectangle(barAreaRect.x + marginLeft, barAreaRect.y
                + marginTop, barAreaRect.width - marginLeft - marginRight, barAreaRect.height
                - marginTop - marginBottom);
        // actual bar box
        Rectangle barBox;

        // calculate the biggest bar Index
        biggestBarIndex = calcBiggestBarIndex();
        // Draw the background
        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);

        for (int i = 0; i < bars.size(); i++) {
            // Only shift when the bar is no overlay bar.
            if (((Bar) bars.get(i)) instanceof OverlayBar
                    || ((Bar) bars.get(i)) instanceof OverlayLabelBar) {
                barBox = calculateBarContainer(barDrawingRect, ((Bar) bars.get(i)).getIndex(),
                        biggestBarIndex);
            } else {
                barBox = calculateBarContainerShifted(barDrawingRect, ((Bar) bars.get(i))
                        .getIndex(), biggestBarIndex);
            }
            // barBox = calculateBarContainer(barDrawingRect, ((Bar)bars.get(i)).getIndex(),
            // biggestBarIndex);
            ((Bar) bars.get(i)).setBox(barBox);
            ((Bar) bars.get(i)).setGC(gc);
            ((Bar) bars.get(i)).drawBar();
        }
    }

    @Override
    protected final void paintDescTopArea(final Rectangle thisArea, final MColor thisBGColor,
            final MColor thisFontColor) {

        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);

        thisFontColor.setColor(gc);
        if (dragged) {
            gc.drawText(NLS.bind(Messages.FreqAnalysisGraph_shiftgraph1, getCurrentShift()),
                            3, (int) Math.round(Math.floor((double) (descTopRect.height - gc
                                    .getFontMetrics().getAscent()) / 2)));
        }else {
            gc.drawText(NLS.bind(Messages.FreqAnalysisGraph_shiftgraph0, getCurrentShift()),
                    3, (int) Math.round(Math.floor((double) (descTopRect.height - gc
                            .getFontMetrics().getAscent()) / 2)));
        }

    }

    @Override
    protected final void paintDescBottomArea(final Rectangle thisArea, final MColor thisBGColor,
            final MColor thisFontColor) {
        // The bar drawing rectangle without margins
        Rectangle descDrawingRect = new Rectangle(barAreaRect.x + marginLeft, descBottomRect.y,
                barAreaRect.width - marginLeft - marginRight, descBottomRect.height);
        // actual bar box
        Rectangle textBox;

        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);

        thisFontColor.setColor(gc);
        for (int i = 0; i < bars.size(); i++) {
            textBox = calculateTextContainer(descDrawingRect, ((Bar) bars.get(i)).getIndex(),
                    biggestBarIndex);
            ((Bar) bars.get(i)).drawLowerLabel(textBox, gc);
        }
    }

    @Override
    protected final Bar setBarStandardSettings(final Bar rawBar) {
        // set OVERLAY BAR Standards - they override standard bar settings (because theyre
        // specialised bars)
        if (rawBar instanceof OverlayBar || rawBar instanceof OverlayLabelBar) {
            if ((rawBar).getWidth() == -1.0) {
                (rawBar).setWidth(overlayBarWidth);
            }
            if ((rawBar).getColorMainBar() == null) {
                (rawBar).setColorMainBar(overlayBarColor);
            }
        }

        rawBar.setGC(gc);
        if (rawBar.getColorMainBar() == null) {
            rawBar.setColorMainBar(barColor);
        }
        if (rawBar.getWidth() == -1) {
            rawBar.setWidth(barWidth);
        }

        // Set Transparency
        if (rawBar instanceof OverlayBar) {
            if (((OverlayBar) rawBar).getTransparencyHighest() == -1) {
                ((OverlayBar) rawBar).setTransparencyHighest(overlayTranspHighest);
            }
            if (((OverlayBar) rawBar).getTransparencyLowest() == -1) {
                ((OverlayBar) rawBar).setTransparencyLowest(overlayTranspLowest);
            }
        }
        if (rawBar instanceof OverlayLabelBar) {

            if (((OverlayLabelBar) rawBar).getTransparencyHighest() == -1) {
                ((OverlayLabelBar) rawBar).setTransparencyHighest(overlayTranspHighest);
            }
            if (((OverlayLabelBar) rawBar).getTransparencyLowest() == -1) {
                ((OverlayLabelBar) rawBar).setTransparencyLowest(overlayTranspLowest);
            }
        }

        return rawBar;
    }

    /**
     * @return the max. horizontal space available for a bar in pixel.
     */
    protected final double calculateBarHorizSpace() {
        return (double) (areaWidth - descLeftRect.width - descRightRect.width)
                / (double) (calcBiggestBarIndex());
    }

    /**
     * if the Frequency Graph is dragged, this method determines whether a shifting of bars will
     * happen, and if, of which offset. <br />
     * Current Policy is, to not shift the Graph by the exact pixel value; the bars will rather snap
     * to the raster positions
     *
     * @param myPixelsDifference is the difference in horizontal pixels in the dragging procedure.
     * @param isMouseButtonHold if the mouseButton is held pressed.
     * @return if a shifting occurred (for redraw)
     */
    public final boolean setDraggedPixels(final int myPixelsDifference,
            final boolean isMouseButtonHold) {
        if (calcBiggestBarIndex() > 0) {
            int myShift = (int) Math.round((double) myPixelsDifference / calculateBarHorizSpace());
            boolean changed = savedShift + myShift != getCurrentShift();
            setCurrentShift(savedShift + myShift);
            setCurrentShift(modulo(getCurrentShift(), calcBiggestBarIndex() + 1));
            if (!isMouseButtonHold) {
                savedShift = getCurrentShift();
            }
            if (changed) {
                dragged = true;
                return true;
            }
            return false;
        } else {
            setCurrentShift(0);
            savedShift = 0;
            return true;
        }
    }

    /**
     * resets the mouse drag, setting the shift to zero.
     */
    public final void resetDrag() {
        setCurrentShift(0);
        savedShift = 0;
        dragged = false;
    }

    /**
     * @param currentShift the shift of the graph
     */
    public final void setCurrentShift(final int currentShift) {
        this.currentShift = currentShift;
    }

    /**
     * @return the shift of the graph
     */
    public final int getCurrentShift() {
        return currentShift;
    }

}