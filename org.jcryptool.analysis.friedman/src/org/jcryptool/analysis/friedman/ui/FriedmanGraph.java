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
package org.jcryptool.analysis.friedman.ui;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.jcryptool.analysis.graphtools.Bar;
import org.jcryptool.analysis.graphtools.Graph;
import org.jcryptool.analysis.graphtools.MColor;
import org.jcryptool.analysis.graphtools.derivates.OverlayBar;
import org.jcryptool.analysis.graphtools.derivates.OverlayLabelBar;

/**
 * @author SLeischnig
 * This custom Graph is not restricted to display all bars only at once, but can zoom in and out, and shift the graph.
 */
public class FriedmanGraph extends Graph {

    int overlayTranspHighest, overlayTranspLowest;
    MColor overlayBarColor;
    double overlayBarWidth = 1.0;
	private int currentShift = 0;
	private int savedShift = 0;
	private double lowestValue = 999999.0;

	private int beginIndex=0, barCount=0;
	private double zoomFactor = 1.628;

	/**
	 * Change the standard settings from the super class
	 */
	private void iniSettings() {

    	overlayTranspHighest = 230;
    	overlayTranspLowest = 0;

        distTop = 3;
        distBottom = 16;
        distLeft = 0;
        distRight = 0;
        marginTop = 0;
        marginBottom = 0;
        marginLeft = 4;
        marginRight = 4;
        calcAreas();

        descTopFontColor = new MColor("0095C9", 255); //$NON-NLS-1$
        descBottomFontColor = new MColor("FFFFFF", 255); //$NON-NLS-1$
        descTopBGColor = new MColor("306A90", 255); //$NON-NLS-1$
        descBottomBGColor = new MColor("B60000", 255); //$NON-NLS-1$
        descLeftBGColor = new MColor("B60000", 255); //$NON-NLS-1$
        descRightBGColor = new MColor("B60000", 255); //$NON-NLS-1$
        barAreaBGColor = new MColor("306A90", 255); //$NON-NLS-1$
        overlayBarColor = new MColor("006AB0", 255); //$NON-NLS-1$
    }

    public FriedmanGraph(final GC gc, final int areaWidth, final int areaHeight) {
        super(gc, areaWidth, areaHeight);
        iniSettings();
    }

    /** Calculate which space a bar label can fill
     * @param textSpace the whole bar description area
     * @param thisIndex the bar's index
     * @return the actual space reserved for the label
     */
    private Rectangle calculateTextContainer(final Rectangle textSpace, final int thisIndex) {
		int myIndex = ( thisIndex - currentShift);
		double rectWidth = (double) textSpace.width / (double) (getBarCount());
        double dRX = textSpace.x + rectWidth * myIndex;
        double dRW = textSpace.x + rectWidth * (myIndex + 1) - dRX;
        int RX = (int) Math.round(dRX);
        int RW = (int) Math.round(dRW);
        int RY = textSpace.y;
        int RH = textSpace.height;
        return new Rectangle(RX, RY, RW, RH);
    }

    /**
	 * calculates the whole space a bar can fill, integrating the offset value field
	 * @param barSpace the space that is reserved for bars
	 * @param thisIndex the x-axis value of the bar you want to calculate
	 * @return
	 */
	protected final Rectangle calculateBarContainer(final Rectangle barSpace, final int thisIndex)
	{
		int myIndex = ( thisIndex - currentShift);
		double rectWidth = (double)barSpace.width/(double)(getBarCount());
		double dRX = barSpace.x+rectWidth*myIndex;
		double dRW = barSpace.x+rectWidth*(myIndex+1)-dRX;
		int RX = (int)Math.round(dRX);
		int RW = (int)Math.round(dRW);
		int RY = barSpace.y;
		int RH = barSpace.height;
		return new Rectangle(RX, RY, RW, RH);
	}

    @Override
	protected final void paintBarArea(final Rectangle thisArea, final MColor thisBGColor)
	{
		//The bar drawing rectangle without margins
		Rectangle barDrawingRect = new Rectangle(barAreaRect.x+marginLeft, barAreaRect.y+marginTop, barAreaRect.width-marginLeft-marginRight, barAreaRect.height-marginTop-marginBottom);
		//actual bar box
		Rectangle barBox;

		//calculate the biggest bar Index
		biggestBarIndex = calcBiggestBarIndex();
		//Draw the background
		thisBGColor.setColor(gc); thisBGColor.setBGColor(gc);
		gc.fillRectangle(thisArea);
		if(bars.size()>0)
		{
			for(int i=currentShift; i<currentShift+barCount; i++)
			{
				//Only shift when the bar is no overlay bar.
				barBox = calculateBarContainer(barDrawingRect, ((Bar)bars.get(i)).getIndex());
				//barBox = calculateBarContainer(barDrawingRect, ((Bar)bars.get(i)).getIndex(), biggestBarIndex);
				((Bar)bars.get(i)).setBox(barBox); ((Bar)bars.get(i)).setGC(gc);
				((Bar)bars.get(i)).drawBar();
			}
		}
	}


    @Override
	protected final void paintDescTopArea(final Rectangle thisArea, final MColor thisBGColor, final MColor thisFontColor) {
        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);

        thisFontColor.setColor(gc);
    }

    @Override
	protected final void paintDescBottomArea(final Rectangle thisArea, final MColor thisBGColor, final MColor thisFontColor) {
        // The bar drawing rectangle without margins
        Rectangle descDrawingRect = new Rectangle(barAreaRect.x + marginLeft, descBottomRect.y,
                barAreaRect.width - marginLeft - marginRight, descBottomRect.height);
        // actual bar box
        Rectangle textBox;

        thisBGColor.setColor(gc);
        thisBGColor.setBGColor(gc);
        gc.fillRectangle(thisArea);

        thisFontColor.setColor(gc);
        for (int i=currentShift; i<currentShift+barCount; i++) {
            textBox = calculateTextContainer(descDrawingRect, ((Bar) bars.get(i)).getIndex());
            ((Bar) bars.get(i)).drawLowerLabel(textBox, gc);
        }
    }

    @Override
	protected final Bar setBarStandardSettings(final Bar rawBar)
	{
    	//set OVERLAY BAR Standards - they override standard bar settings (because theyre specialised bars)
		if( rawBar instanceof OverlayBar || rawBar instanceof OverlayLabelBar )
		{
			if((rawBar).getWidth() == -1.0 ) {
				(rawBar).setWidth(overlayBarWidth);
			}
			if((rawBar).getColorMainBar() == null ) {
				(rawBar).setColorMainBar(overlayBarColor);
			}
		}

    	rawBar.setGC(gc);
		if(rawBar.getColorMainBar() == null) {
			rawBar.setColorMainBar(barColor);
		}
		if(rawBar.getWidth() == -1) {
			rawBar.setWidth(barWidth);
		}

		//Set Transparency
		if( rawBar instanceof OverlayBar )
		{
			if(((OverlayBar)rawBar).getTransparencyHighest() == -1 ) {
				((OverlayBar)rawBar).setTransparencyHighest(overlayTranspHighest);
			}
			if(((OverlayBar)rawBar).getTransparencyLowest() == -1 ) {
				((OverlayBar)rawBar).setTransparencyLowest(overlayTranspLowest);
			}
		}
		if( rawBar instanceof OverlayLabelBar )
		{

			if(((OverlayLabelBar)rawBar).getTransparencyHighest() == -1 ) {
				((OverlayLabelBar)rawBar).setTransparencyHighest(overlayTranspHighest);
			}
			if(((OverlayLabelBar)rawBar).getTransparencyLowest() == -1 ) {
				((OverlayLabelBar)rawBar).setTransparencyLowest(overlayTranspLowest);
			}
		}

		return rawBar;
	}

    @Override
	public final void addBar(Bar myBar)
	{
		// before which bar should this bar be inserted? -1, if it is the lat index.
		myBar = setBarStandardSettings(myBar);

		int beforeWhich = -1;
		for(int i=0; i<bars.size(); i++)
		{
			if(compareBarOrder(myBar, (Bar)bars.get(i)) && beforeWhich == -1) {
				beforeWhich = i;
			}
		}

		bars.setSize(bars.size()+1);
		if(beforeWhich > -1)
		{
			for(int i=bars.size()-1; i>beforeWhich; i--) {
				bars.set(i, bars.get(i-1));
			}
			bars.set(beforeWhich, myBar);
		} else {
			bars.set(bars.size()-1, myBar);
		}

		if(myBar.getHeight() < lowestValue) {
			lowestValue = myBar.getHeight();
		}
	}

    /** optimizes the displaying of the bars by relativating their displayed heights: The function seeks the bar with the lowest value, and cuts every bar by a percentage of this value.
     * @param cutPercentage the cutting percentage. Range: 0..1. 1 would mean, you cut the bars such, the lowest bar would not be displayed.
     */
    public final void optimizeBarHeights(final double cutPercentage)
    {
    	double subtractHeight = cutPercentage*lowestValue;
    	for(int i=0; i<bars.size(); i++)
    	{
    		bars.get(i).setHeight(bars.get(i).getHeight()-subtractHeight);
    	}
    	for(int i=0; i<bars.size(); i++)
    	{
    		bars.get(i).setHeight(bars.get(i).getHeight()/(1-subtractHeight));
    	}
    }


    /**
     * @return the max. horizontal space available for a bar in pixel.
     */
    protected final double calculateBarHorizSpace()
	{
		return (double)(areaWidth - descLeftRect.width-descRightRect.width) / (double)(barCount);
	}

	/**if the Frequency Graph is dragged, this method determines whether a shifting of bars will happen, and if, of which offset.
	 * <br />Current Policy is, to not shift the Graph by the exact pixel value; the bars will rather snap to the raster positions
	 * @param myPixelsDifference is the difference in horizontal pixels in the dragging procedure.
	 * @param isMouseButtonHold if the mouseButton is held pressed.
	 * @return if a shifting occured (for redraw)
	 */
	public final boolean setDraggedPixels(final int myPixelsDifference, final boolean isMouseButtonHold) {
		if(calcBiggestBarIndex() > 0)
		{
			int myShift = (-1)* (int)Math.round((double)myPixelsDifference / calculateBarHorizSpace());
			boolean changed = savedShift + myShift != getCurrentShift();
			setCurrentShift(Math.max(savedShift + myShift, 0));
			//setCurrentShift(modulo(getCurrentShift(), calcBiggestBarIndex()));
			if(! isMouseButtonHold) {
				savedShift = getCurrentShift();
			}
			if(changed)
				{ return true; } //Wenn sich was verändert hat, zeichnen!
			return false;
		}
		else
		{
			setCurrentShift(0);
			savedShift = 0;
			return true;
		}
	}

	/** reset the shift of the graph
	 * @param myBarCount sets the amount of bars to be displayed
	 */
	public final void resetDrag(final int myBarCount)
	{
		setCurrentShift(0);
		setBarCount(myBarCount);
		savedShift = 0;
	}

	/**
	 * @param currentShift sets the shift
	 */
	public final void setCurrentShift(final int currentShift) {
		if(bars.size() > 0) {
			this.currentShift = Math.min(currentShift, bars.size()-1);
		}
	}

	/** zoom in by the zoomFactor value
	 * @return whether the zoom has affected something or not.
	 */
	public final boolean zoomin()
	{
		int oldBeginIndex = beginIndex;
		boolean changed=false;
		if(bars.size() > 0)
		{
			double newBarCount = (double)barCount / zoomFactor;
			int newCount = (int)Math.round(newBarCount);
			if(newCount >= barCount) {
				newCount = barCount - 1;
			}

			if(barCount != newCount || beginIndex != oldBeginIndex) {
				changed = true;
			}
			if(barCount != newCount)
			{
				barCount = newCount;
			}
		}
		if(changed) {
			return true;
		} else {
			return false;
		}
	}

	/** zoom out by the zoomFactor value
	 * @return whether the zoom has affected something or not.
	 */
	public final boolean zoomout()
	{
		int oldBeginIndex = beginIndex;
		boolean changed=false;
		if(bars.size() > 0)
		{
			double newBarCount = (double)barCount * zoomFactor;
			int newCount = (int)Math.round(newBarCount);
			if(newCount <= barCount) {
				newCount = barCount + 1;
			}
			if(beginIndex+newCount > bars.size())
			{
				beginIndex = bars.size() - newCount;
				beginIndex = Math.max(0, beginIndex);
				newCount = bars.size() - beginIndex;
			}

			if(barCount != newCount || beginIndex != oldBeginIndex) {
				changed = true;
			}
			if(barCount != newCount)
			{
				barCount = newCount;
			}
		}
		if(changed) {
			return true;
		} else {
			return false;
		}
	}

	public final int getCurrentShift() {
		return currentShift;
	}

	public final void setBarCount(final int barCount) {
		if(bars.size() > 0) {
			this.barCount = Math.min(bars.size()-currentShift, barCount);
		}
	}

	public final int getBarCount() {
		return barCount;
	}

	public final void setZoomFactor(final double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public final double getZoomFactor() {
		return zoomFactor;
	}

}