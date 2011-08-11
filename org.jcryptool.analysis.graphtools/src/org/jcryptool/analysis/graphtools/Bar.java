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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author SLeischnig
 * A standard bar, for use with the Graph class. Add a Bar to a Graph to display it.
 * Designed for extension for customized use.
 */
	public class Bar {
	// height, width of the bar (I=[0|1]) as a ratio
	protected double width, height;
	// bar drawing area (coord. is top left!)
	private Rectangle box;
	// Color
	protected MColor colorMainBar;
	// graphical context
	protected GC gc;
	// order of painting (lower numbers are drawn underlaying) and x-axis position (index)
	protected int order, index;


	/**
	 * Creates a default bar which is capable of being drawn on any graphical context. <br>
	 * You cann add it to a Graph class/subclass.
	 * This bar is black and has a fixed 65% width and the drawing when leaving it unchanged.
	 * @param pHeight
	 * @param pIndex The x-Axis position
	 * @param pOrder lower values: drawn in the background. higher values: drawin in the foreground.
	 */
	public Bar(double pHeight, int pIndex, int pOrder)
	{
		setHeight(pHeight);
		setWidth(-1); //=> standard values in Graph class apply
		//setBox(new Rectangle(0, 0, 0, 0)); => null => standard values in Graph class apply
		//setColorMainBar(new MColor("000000", 255)); => null => standard values in Graph class apply
		gc = null;
		index = pIndex;
		order = pOrder;
	}

	/**
	 * Creates a default bar which is capable to be drawn on any graphical context. <br>
	 * You cann add it to a Graph class/subclass. <br />
	 * You may have to deliver the gc and drawing outline box, if you're not using the default Graph Class
	 * @param pHeight Height of the bar as a ratio of the draw box height (min: 0, max: 1)
	 * @param pWidth Width as a ratio of the draw box width #
	 * @param pIndex The x-Axis position
	 * @param pOrder lower values: drawn in the background. higher values: drawin in the foreground.
	 * @param pColorMainBar Color of the main Bar #
	 */
	public Bar(double pHeight, double pWidth, int pIndex, int pOrder, MColor pColorMainBar)
	{
		setHeight(pHeight); setWidth(pWidth);
		//setBox(new Rectangle(0,0,0,0));
		//gc = null;
		setColorMainBar(pColorMainBar);
		index = pIndex; order = pOrder;
	}

	/**
	 * Creates a default bar which is capable to be drawn on any graphical context. <br>
	 * You cann add it to a Graph class/subclass.
	 * @param pHeight Height of the bar as a ratio of the draw box height (min: 0, max: 1)
	 * @param pWidth Width as a ratio of the draw box width
	 * @param pIndex The x-Axis position
	 * @param pOrder lower values: drawn in the background. higher values: drawin in the foreground.
	 * @param pBox the drawing area on the canvas #
	 * @param pColorMainBar Color of the main Bar
	 * @param pGc the graphical context #
	 */
	public Bar(double pHeight, double pWidth, int pIndex, int pOrder, Rectangle pBox, MColor pColorMainBar, GC pGc)
	{
		setHeight(pHeight); setWidth(pWidth);
		setBox(pBox);
		setColorMainBar(pColorMainBar);
		gc = pGc;
		index = pIndex; order = pOrder;
	}


	/**
	 * sets the graphical context.
	 * @param pGc the graphical context
	 */
	public void setGC(GC pGc)
	{
		gc = pGc;
	}


	// Getters and Setters

	public void setWidth(double width) { this.width = width;}
	public double getWidth() {return width;}
	public void setHeight(double height) {this.height = height;}
	public double getHeight() {return height;}
	public void setColorMainBar(MColor colorMainBar) {this.colorMainBar = colorMainBar;}
	public MColor getColorMainBar() {return colorMainBar;}
	public void setOrder(int order) {this.order = order;}
	public int getOrder() {return order;}
	public void setIndex(int index) {this.index = index;}
	public int getIndex() {return index;}
	public void setBox(Rectangle box) {this.box = box;}
	public Rectangle getBox() {return box;}


	/**
	 * Sublasses may add additional content to the upper and lower description area<br />
	 * These should be called in the procedures for drawing these areas
	 * @param textSpace the Rectangle that your text can fill.
	 * @param myGC the graphical context
	 */
	public void drawLowerLabel(Rectangle textSpace, GC myGC)
	{
		// Nothing happens here
	}

	/**
	 * Sublasses may add additional content to the upper and lower description area<br />
	 * These should be called in the procedures for drawing these areas
	 * @param textSpace the Rectangle that your text can fill.
	 * @param myGC the graphical context
	 */
	public void drawUpperLabel(Rectangle textSpace, GC myGC)
	{
		// Nothing happens here
	}


	/**
	 * draws the bar on a graphical context.
	 * This is a funbction call without parameters whch uses the fields in the Bar class.
	 */
	public void drawBar() {drawBar(getBox(), getHeight(), getWidth(), getColorMainBar(), gc);}
	/**
	 * draws the bar on a graphical context.
	 * @param myBox the drawing area (and limitation for a 100% height and width bar)
	 * @param myHeight height of the bar as a ratio of the box's height
	 * @param myWidth width of the bar as a ratio of the box's width
	 * @param myColorMainbar main color of the bar
	 * @param myGC graphical context
	 */
	public void drawBar(Rectangle myBox, double myHeight, double myWidth, MColor myColorMainbar, GC myGC)
	{
		int barWidth = (int)Math.round(myWidth*myBox.width);
		int barHeight = (int)Math.round(myHeight*myBox.height);
		int topleftX = (int)Math.round((double)(myBox.width-myWidth*myBox.width)/2 + myBox.x);
		int topleftY = myBox.y+myBox.height - barHeight;
		Rectangle myBar = new Rectangle(topleftX, topleftY, barWidth, barHeight);

		myColorMainbar.setBGColor(myGC);
		myGC.fillRectangle(myBar);
	}

}
