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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.jcryptool.analysis.graphtools.MColor;

/**
 * @author SLeischnig
 * a Bar derivate that displays labels and uses a overlay-ish transparency drawing.
 */
public class OverlayLabelBar extends LabelBar {
    private int transparencyHighest = -1, transparencyLowest = -1;

    /**
     * Creates a default label overlay bar which is capable of being drawn on any graphical context. <br>
     * You cann add it to a Graph class/subclass. This bar is black and has a fixed 65% width and
     * the drawing when leaving it unchanged.
     *
     * @param pHeight
     * @param pIndex The x-Axis position
     * @param pOrder lower values: drawn in the background. higher values: drawin in the foreground.
     * @param pUpperLabel the label which is displayed above the Bar (e. g. detailed bar's height)
     * @param pLlowerLabel the label which is displayed below the Bar (e. g. x-axis-label)
     */
    public OverlayLabelBar(double height, int index, int order, String pUpperLabel, String pLowerLabel) {
        super(height, index, order, pUpperLabel, pLowerLabel);
    }

    /**
     * Creates a default label overlay bar which is capable to be drawn on any graphical context. <br>
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
    public OverlayLabelBar(double height, double width, int index, int order, MColor colorMainBar,
            String pUpperLabel, String pLowerLabel) {
        super(height, width, index, order, colorMainBar, pUpperLabel, pLowerLabel);
    }

    /**
     * Creates a default label overlay bar which is capable to be drawn on any graphical context. <br>
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
    public OverlayLabelBar(double height, double width, int index, int order, Rectangle box,
            MColor colorMainBar, GC gc, String pUpperLabel, String pLowerLabel) {
        super(height, width, index, order, box, colorMainBar, gc, pUpperLabel, pLowerLabel);
    }


    @Override
    /*
     * * draws the bar on a graphical context.
     * @param myBox the drawing area (and limitation for a 100% height and width bar)
     * @param myHeight height of the bar as a ratio of the box's height
     * @param myWidth width of the bar as a ratio of the box's width
     * @param myColorMainbar main color of the bar
     * @param myGC graphical context
     */
    public void drawBar(Rectangle myBox, double myHeight, double myWidth, MColor myColorMainbar,
            GC myGC) {
        int barWidth = (int) Math.round(myWidth * myBox.width);
        int barHeight = (int) Math.round(myHeight * myBox.height);
        int topleftX = (int) Math.round((double) (myBox.width - myWidth * myBox.width) / 2
                + myBox.x);
        int topleftY = myBox.y + myBox.height - barHeight;
        int transparencyShades = 20;
        int transparencySteps = Math.max(1, (barHeight / transparencyShades));
        Rectangle myBar = new Rectangle(topleftX, topleftY, barWidth, barHeight);

        myColorMainbar.setBGColor(myGC);

        // For low cpu usage have transparency steps
        MColor myColor = myColorMainbar;

        for (int k = myBar.y + myBar.height; k > myBar.y; k -= transparencySteps) {
            double actNormDistrPercentage = (double) (myBar.y + myBar.height - k)
                    / (double) (myBar.height);
            double nextNormDistrPercentage = (double) (myBar.y + myBar.height - (k - transparencySteps))
                    / (double) (myBar.height);
            int alpha = (int) Math.round((getTransparencyHighest() - getTransparencyLowest())
                    * (actNormDistrPercentage + nextNormDistrPercentage) / 2)
                    + getTransparencyLowest();
            myColor.setAlpha(alpha, gc, false);

            // falls es nich der letzte schrit ist isses ok...
            if (k - transparencySteps > myBar.y) {
                gc.fillRectangle(myBar.x, k, myBar.width, -transparencySteps);
            } else {
                gc.fillRectangle(myBar.x, k, myBar.width, myBar.y - k);
            }
        }
    }

	public void setTransparencyHighest(int transparencyHighest) {
		this.transparencyHighest = transparencyHighest;
	}

	public int getTransparencyHighest() {
		return transparencyHighest;
	}

	public void setTransparencyLowest(int transparencyLowest) {
		this.transparencyLowest = transparencyLowest;
	}

	public int getTransparencyLowest() {
		return transparencyLowest;
	}

}
