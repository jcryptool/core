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
package org.jcryptool.analysis.graphtools;

import java.util.Vector;

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * The standard Graph class. Displays a range of Bars, which have to be added to the Graph. Designed for extension for
 * customized use
 *
 * @author SLeischnig
 */

public class Graph {
    // ## superclass implementation related fields

    // Sequence of bars (contain order of painting and index, too)
    protected Vector<Bar> bars = new Vector<Bar>(0, 1);
    // height and width of the painting area
    protected int areaWidth;
    protected int areaHeight;
    // the graphical context
    protected GC gc;

    // ## Parameters that has to be fields

    protected int biggestBarIndex = 0;

    // ## Parameters that could be local but would be nice to adjust in a subclass
    // ## without overkilling a procedures parameter list or overwriting the function

    // Distance of the bar area to top, bottom, etc
    // This values define the description areas
    protected int distTop, distBottom, distLeft, distRight;
    // distance of the real bar space to top, left, ...
    protected int marginLeft, marginRight, marginTop, marginBottom;
    // Rectangles for the different painting areas
    protected Rectangle barAreaRect, descTopRect, descBottomRect, descRightRect, descLeftRect;
    // Color for labels
    protected MColor descTopFontColor, descBottomFontColor;
    // Color for description Areas and main paint Area
    protected MColor descTopBGColor, descBottomBGColor, descLeftBGColor, descRightBGColor, barAreaBGColor;
    protected double barWidth;
    protected MColor barColor;

    /**
     * Creates the Graph Element with the absolutely required components
     *
     * @param pGc The drawing context
     * @param pAreaWidth The Width of the painting area
     * @param pAreaHeight The height of the painting area
     */
    public Graph(GC pGc, int pAreaWidth, int pAreaHeight) {
        areaHeight = pAreaHeight;
        areaWidth = pAreaWidth;
        gc = pGc;
        resetBars();

        setBarWidth(0.61);
        setBarColor(new MColor("000000", 240)); //$NON-NLS-1$

        distTop = 0;
        distBottom = 18;
        distLeft = 0;
        distRight = 0;
        marginTop = 20;
        marginBottom = 0;
        marginLeft = 20;
        marginRight = 20;
        calcAreas();

        descTopFontColor = new MColor("000000", 255); //$NON-NLS-1$
        descBottomFontColor = new MColor("000000", 255); //$NON-NLS-1$
        descTopBGColor = new MColor("FFFFFF", 255); //$NON-NLS-1$
        descBottomBGColor = new MColor("000000", 255); //$NON-NLS-1$
        descLeftBGColor = new MColor("FFFFFF", 255); //$NON-NLS-1$
        descRightBGColor = new MColor("FFFFFF", 255); //$NON-NLS-1$
        barAreaBGColor = new MColor("FFFFFF", 255); //$NON-NLS-1$
    }

    public void setGC(GC pGc) {
        gc = pGc;
    }

    public void updateBounds(int pAreaWidth, int pAreaHeight) {
        areaHeight = pAreaHeight;
        areaWidth = pAreaWidth;
        calcAreas();
    }

    /**
     * sets the parameters for the calculation of the different drawing areas
     *
     * @param pDistTop height of a upper border description area
     * @param pDistBottom height of a lower border description area
     * @param pDistLeft width of a left border description area
     * @param pDistRight width of a right border description area
     * @param pMarginLeft main area: spacing of all bars to the left
     * @param pMarginRight main area: spacing of all bars to the right
     * @param pMarginTop main area: spacing of all bars on the top
     * @param pMarginBottom main area: spacing of all bars on the bottom
     */
    public void setFullAreaParams(int pDistTop, int pDistBottom, int pDistLeft, int pDistRight, int pMarginLeft,
            int pMarginRight, int pMarginTop, int pMarginBottom) {
        distTop = pDistTop;
        distBottom = pDistBottom;
        distLeft = pDistLeft;
        distRight = pDistRight;
        marginLeft = pMarginLeft;
        marginRight = pMarginRight;
        marginTop = pMarginTop;
        marginBottom = pMarginBottom;
        calcAreas();
    }

    public void setBarAreaParams(MColor bg) {
        barAreaBGColor = bg;
    }

    public void setDescLeftAreaParams(MColor bg) {
        descLeftBGColor = bg;
    }

    public void setDescRightAreaParams(MColor bg) {
        descRightBGColor = bg;
    }

    public void setDescTopAreaParams(MColor bg, MColor fontColor) {
        descTopBGColor = bg;
        descTopFontColor = fontColor;
    }

    public void setDescBottomAreaParams(MColor bg, MColor fontColor) {
        descBottomBGColor = bg;
        descBottomFontColor = fontColor;
    }

    protected void calcAreas() {
        descTopRect = new Rectangle(0, 0, areaWidth, distTop);
        descBottomRect = new Rectangle(0, areaHeight - distBottom, areaWidth, distBottom);
        descLeftRect = new Rectangle(0, distTop, distLeft, areaHeight - distTop - distBottom);
        descRightRect = new Rectangle(areaWidth - distRight, distTop, distRight, areaHeight - distTop - distBottom);
        barAreaRect = new Rectangle(distLeft, distTop, areaWidth - distLeft - distRight, areaHeight - distTop
                - distBottom);
    }

    /**
     * reset the bar array and set a new length for it
     *
     * @param barCount the maximum number of bars you want to add before reset
     */
    public void resetBars() {
        bars = new Vector<Bar>(0, 1);
    }

    /**
     * compare two bars for which is drawn first.
     *
     * @return false, if bar1 is drawn earlier than bar2, true, if bar1 is drawn later than bar2.
     */
    protected boolean compareBarOrder(Bar bar1, Bar bar2) {
        if (bar1.order != bar2.order) {
            return bar1.order <= bar2.order;
        } else {
            return bar1.index <= bar2.index;
        }
    }

    /**
     * calculates the biggest index a bar in the bars-array has.
     */
    protected int calcBiggestBarIndex() {
        int biggest = -9999999;
        for (int i = 0; i < bars.size(); i++)
            if (((Bar) bars.get(i)).index > biggest)
                biggest = ((Bar) bars.get(i)).index;
        return biggest;
    }

    /**
     * calculates the whole space a bar can fill
     *
     * @param barSpace the space that is reserved for bars
     * @param thisIndex the x-axis value of the bar you want to calculate
     * @param maxIndex the highest x-value that occurs in this graph
     * @return
     */
    protected Rectangle calculateBarContainer(Rectangle barSpace, int thisIndex, int maxIndex) {
        double rectWidth = (double) barSpace.width / (double) (maxIndex + 1);
        double dRX = barSpace.x + rectWidth * thisIndex;
        double dRW = barSpace.x + rectWidth * (thisIndex + 1) - dRX;
        int RX = (int) Math.round(dRX);
        int RW = (int) Math.round(dRW);
        int RY = barSpace.y;
        int RH = barSpace.height;
        return new Rectangle(RX, RY, RW, RH);
    }

    /**
     * Ensures that a bar, when added to a graph, has all needed properties
     *
     * @param rawBar The bar that has to be added to the graph, but has possibly properties that have to be filled by
     *        standard settings.
     * @return the Bar completed with standard settings
     */
    protected Bar setBarStandardSettings(Bar rawBar) {
        rawBar.setGC(gc);
        if (rawBar.getColorMainBar() == null)
            rawBar.setColorMainBar(barColor);
        if (rawBar.getWidth() == -1)
            rawBar.setWidth(barWidth);

        return rawBar;
    }

    /**
     * add a new bar into the bar array. It will be automatically sorted in for drawing order.
     *
     * @param myBar the new bar
     */
    public void addBar(Bar myBar) {
        // before which bar should this bar be inserted? -1, if it is the lat index.
        myBar = setBarStandardSettings(myBar);

        int beforeWhich = -1;
        for (int i = 0; i < bars.size(); i++) {
            if (compareBarOrder(myBar, (Bar) bars.get(i)) && beforeWhich == -1)
                beforeWhich = i;
        }

        bars.setSize(bars.size() + 1);
        if (beforeWhich > -1) {
            for (int i = bars.size() - 1; i > beforeWhich; i--)
                bars.set(i, bars.get(i - 1));
            bars.set(beforeWhich, myBar);
        } else
            bars.set(bars.size() - 1, myBar);
    }

    // ################# PAINTING #################
    // ################# -------- #################

    /**
     * Paint procedure for the whole painting Area. Calls the painting procedures for the other paint areas. \nOverride
     * this only if you have structural changes to make e. g. concerning the number of painting areas or adding
     * parameters to an area's painting procedure.
     */
    public void paintArea() {
        paintBarArea();
        paintDescTopArea();
        paintDescBottomArea();
        paintDescLeftArea();
        paintDescRightArea();
    }

    // -------------------------- Actual graph painting area -----------------------------------
    /**
     * Draws the actual graph area <br />
     * Used to call the function without the parameters (they already exist as fields)
     */
    protected void paintBarArea() {
        paintBarArea(barAreaRect, barAreaBGColor);
    }

    /**
     * Draws the actual graph area <br />
     *
     * @param thisArea Rectangle defining the painting area
     * @param thisBGColor the background color
     */
    protected void paintBarArea(Rectangle thisArea, MColor thisBGColor) {
        // The bar drawing rectangle without margins
        Rectangle barDrawingRect = new Rectangle(barAreaRect.x + marginLeft, barAreaRect.y + marginTop,
                barAreaRect.width - marginLeft - marginRight, barAreaRect.height - marginTop - marginBottom);
        // actual bar box
        Rectangle barBox;

        // calculate the biggest bar Index
        biggestBarIndex = calcBiggestBarIndex();
        // Draw the background
        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);

        for (int i = 0; i < bars.size(); i++) {
            barBox = calculateBarContainer(barDrawingRect, ((Bar) bars.get(i)).getIndex(), biggestBarIndex);
            ((Bar) bars.get(i)).setBox(barBox);
            ((Bar) bars.get(i)).setGC(gc);
            ((Bar) bars.get(i)).drawBar();
        }
    }

    // -------------------------- Top description area -----------------------------------------
    /**
     * Draws the top description area <br />
     * Used to call the function without the parameters (they already exist as fields)
     */
    protected void paintDescTopArea() {
        paintDescTopArea(descTopRect, descTopBGColor, descTopFontColor);
    }

    /**
     * Draws the top description area
     *
     * @param thisArea Rectangle defining the painting area
     * @param thisBGColor the background color
     * @param thisfontcolor the Color for text labels in this description area
     */
    protected void paintDescTopArea(Rectangle thisArea, MColor thisBGColor, MColor thisFontColor) {
        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);
    }

    // -------------------------- Bottom description area -----------------------------------------
    /**
     * Draws the bottom description area <br />
     * Used to call the function without the parameters (they already exist as fields)
     */
    protected void paintDescBottomArea() {
        paintDescBottomArea(descBottomRect, descBottomBGColor, descBottomFontColor);
    }

    /**
     * Draws the bottom description area
     *
     * @param thisArea Rectangle defining the painting area
     * @param thisBGColor the background color
     * @param thisfontcolor the Color for text labels in this description area
     */
    protected void paintDescBottomArea(Rectangle thisArea, MColor thisBGColor, MColor thisFontColor) {
        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);
    }

    // -------------------------- Right description area -----------------------------------------
    /**
     * Draws the right description area <br />
     * Used to call the function without the parameters (they already exist as fields)
     */
    protected void paintDescRightArea() {
        paintDescRightArea(descRightRect, descRightBGColor);
    }

    /**
     * Draws the right description area
     *
     * @param thisArea Rectangle defining the painting area
     * @param thisBGColor the background color
     */
    protected void paintDescRightArea(Rectangle thisArea, MColor thisBGColor) {
        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);
    }

    // -------------------------- Left description area -----------------------------------------
    /**
     * Draws the left description area <br />
     * Used to call the function without the parameters (they already exist as fields)
     */
    protected void paintDescLeftArea() {
        paintDescLeftArea(descLeftRect, descLeftBGColor);
    }

    /**
     * Draws the left description area
     *
     * @param thisArea Rectangle defining the painting area
     * @param thisBGColor the background color
     */
    protected void paintDescLeftArea(Rectangle thisArea, MColor thisBGColor) {
        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);
    }

    /**
     * Helps you to calculate the GC.drawText() position for a text you want to center on a given point
     *
     * @param myText is the String you want to draw
     * @param midX where do you want the text to center?
     * @param topMidY where do you want the text to center? (defines the text's top border)
     * @param metrics the fontMetrics, e. g. by your GC parameter
     * @return a point which coordinates you can use directly in your drawText() call
     */
    protected Point calcTextXY(String myText, int midX, int topMidY, FontMetrics metrics) {
        int leftX = midX - (myText.length() * metrics.getAverageCharWidth()) / 2;
        int leftY = topMidY;
        return new Point(leftX, leftY);
    }

    public void setBarWidth(double barWidth) {
        this.barWidth = barWidth;
    }

    public double getBarWidth() {
        return barWidth;
    }

    public void setBarColor(MColor barColor) {
        this.barColor = barColor;
    }

    public MColor getBarColor() {
        return barColor;
    }

}