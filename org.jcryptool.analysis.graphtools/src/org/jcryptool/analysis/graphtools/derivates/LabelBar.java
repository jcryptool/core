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
package org.jcryptool.analysis.graphtools.derivates;

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.jcryptool.analysis.graphtools.Bar;
import org.jcryptool.analysis.graphtools.MColor;

/**
 * @author SLeischnig
 * A bar derivate that displays custom labels on the top and bottom.
 */
public class LabelBar extends Bar {

    private String upperLabel, lowerLabel;

    /**
     * Creates a default bar which is capable of being drawn on any graphical context. <br>
     * You cann add it to a Graph class/subclass. This bar is black and has a fixed 65% width and
     * the drawing when leaving it unchanged.
     *
     * @param pHeight
     * @param pIndex The x-Axis position
     * @param pOrder lower values: drawn in the background. higher values: drawin in the foreground.
     * @param pUpperLabel the label which is displayed above the Bar (e. g. detailed bar's height)
     * @param pLlowerLabel the label which is displayed below the Bar (e. g. x-axis-label)
     */
    public LabelBar(double height, int index, int order, String pUpperLabel, String pLowerLabel) {
        super(height, index, order);
        upperLabel = pUpperLabel;
        lowerLabel = pLowerLabel;
    }

    /**
     * Creates a default bar which is capable to be drawn on any graphical context. <br>
     * You cann add it to a Graph class/subclass. <br />
     * You may have to deliver the gc and drawing outline box, if you're not using the default Graph
     * Class
     *
     * @param pHeight Height of the bar as a ratio of the draw box height (min: 0, max: 1)
     * @param pWidth Width as a ratio of the draw box width #
     * @param pIndex The x-Axis position
     * @param pOrder lower values: drawn in the background. higher values: drawin in the foreground.
     * @param pColorMainBar Color of the main Bar #
     * @param pUpperLabel the label which is displayed above the Bar (e. g. detailed bar's height)
     * @param pLlowerLabel the label which is displayed below the Bar (e. g. x-axis-label)
     */
    public LabelBar(double height, double width, int index, int order, MColor colorMainBar,
            String pUpperLabel, String pLowerLabel) {
        super(height, width, index, order, colorMainBar);
        upperLabel = pUpperLabel;
        lowerLabel = pLowerLabel;
    }

    /**
     * Creates a default bar which is capable to be drawn on any graphical context. <br>
     * You cann add it to a Graph class/subclass.
     *
     * @param pHeight Height of the bar as a ratio of the draw box height (min: 0, max: 1)
     * @param pWidth Width as a ratio of the draw box width
     * @param pIndex The x-Axis position
     * @param pOrder lower values: drawn in the background. higher values: drawin in the foreground.
     * @param pBox the drawing area on the canvas #
     * @param pColorMainBar Color of the main Bar
     * @param pGc the graphical context #
     * @param pUpperLabel the label which is displayed above the Bar (e. g. detailed bar's height)
     * @param pLlowerLabel the label which is displayed below the Bar (e. g. x-axis-label)
     */
    public LabelBar(double height, double width, int index, int order, Rectangle box,
            MColor colorMainBar, GC gc, String pUpperLabel, String pLowerLabel) {
        super(height, width, index, order, box, colorMainBar, gc);
        upperLabel = pUpperLabel;
        lowerLabel = pLowerLabel;
    }

    protected Point calcTextXY(String myText, int midX, int topMidY, FontMetrics metrics) {
        int leftX = midX - (myText.length() * metrics.getAverageCharWidth()) / 2;
        int leftY = topMidY - metrics.getAscent() / 2 - 1;
        return new Point(leftX, leftY);
    }

    @Override
    /**
     * * Draws the lower label of the bar to your graph. Preferred to draw this in the Lower
     * description area - drawing procedure.<br /> Font color and other things have to be specified
     * manually.
     * @param textSpace the Rectangle that your text can fill.
     * @param myGC the graphical context
     */
    public void drawLowerLabel(Rectangle textSpace, GC myGC) {
        Point myTextPos = calcTextXY(lowerLabel, textSpace.x + textSpace.width / 2, textSpace.y
                + textSpace.height / 2, myGC.getFontMetrics());
        gc.drawText(lowerLabel, myTextPos.x, myTextPos.y, true);
    }

    /**
     * Draws the lower label of the bar to your graph. Preferred to draw this in the Lower
     * description area - drawing procedure.<br /> Font color and other things have to be specified
     * manually.
     * @param textSpace the Rectangle that your text can fill.
     * @param myGC the graphical context
     */
    @Override
    public void drawUpperLabel(Rectangle textSpace, GC myGC) {
        Point myTextPos = calcTextXY(upperLabel, textSpace.x + textSpace.width / 2, textSpace.y
                + textSpace.height / 2, myGC.getFontMetrics());
        gc.drawText(upperLabel, myTextPos.x, myTextPos.y, true);
    }

}
