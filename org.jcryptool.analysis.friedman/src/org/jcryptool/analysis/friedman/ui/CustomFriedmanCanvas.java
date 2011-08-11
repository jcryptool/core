//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.friedman.ui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.analysis.friedman.calc.FriedmanCalc;
import org.jcryptool.analysis.graphtools.derivates.LabelBar;

/**
 * @author SLeischnig
 * Custom Canvas for the Friedman test, utilizes the Graph class
 */
public class CustomFriedmanCanvas extends org.eclipse.swt.widgets.Canvas implements PaintListener, MouseMoveListener{

	private Composite mycomp;
	private FriedmanCalc myAnalysis;
	private FriedmanGraph graph = new FriedmanGraph(null, this.getSize().x, this.getSize().y);
	
	//graph dragging variables
	private boolean draggingEnabled=true;
	private boolean dragging, clicking=false;
	private int dragBeginX, dragBeginY;
	
	
	public CustomFriedmanCanvas(final org.eclipse.swt.widgets.Composite parent, final int style) {
		super(parent, SWT.DOUBLE_BUFFERED);

		mycomp = parent;

		//myAnalysis = analysis;
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		int width = 0, height=0;
		width = mycomp.getSize().x;
		height = mycomp.getSize().y;
		setSize(width, height);
		
		addPaintListener(this);
		this.addPaintListener(this);
		this.addMouseMoveListener(this);
		
		this.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseExit(final MouseEvent evt) {
				dragging = false;
				if(graph.setDraggedPixels(0, false)) {
					redraw(); //reset the drag
				}
			}
		});
		this.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent evt) {
				if(dragging && draggingEnabled)
				{
					int myPixelsDifference = evt.x - dragBeginX;
					graph.setDraggedPixels(myPixelsDifference, false);
				}
				dragging = false;
				if(clicking)
				{
					if(evt.button == 1) //zoom in
					{
						if(graph.zoomin())
						{
							redraw();
							//graph.paintArea();
						}
					}
					if(evt.button == 3) //zoom out
					{
						if(graph.zoomout())
						{
							redraw();
							//graph.paintArea();
						}
					}
					clicking = false;
				}
			}
			
			public void mouseDown(final MouseEvent evt) {
				dragging = true;
				dragBeginX = evt.x; dragBeginY = evt.y;
				clicking = true;
			}
			
			public void mouseDoubleClick(final MouseEvent evt) {
				graph.resetDrag(40);
				redraw();
			}
		});
	}

	/** sets the Friedman analysis data
	 * @param in Friedman analysis data
	 */
	public final void setAnalysis(final FriedmanCalc in)
	{
		myAnalysis = in;
		buildBars();
		graph.resetDrag(40);
	}

	/**
	 * Generates the bars for the drawing procedures
	 */
	private void buildBars()
	{
		
		double barHeight=0;
		double maxValue = max(myAnalysis.getAnalysis());

		//generate Bars
		graph.resetBars();
		for(int i=0; i<myAnalysis.getAnalysis().length; i++)
		{
				barHeight = (double)(myAnalysis.getAnalysis()[i]) / (maxValue);
				graph.addBar(new LabelBar(barHeight, i, 10, ""+myAnalysis.getAnalysis()[i], ""+(int)(i+1))); //$NON-NLS-1$ //$NON-NLS-2$
		}
	graph.optimizeBarHeights(1);

	}

	/** returns the highest value from the given array
	 * @param t the array
	 * @return the highest value
	 */
	private static double max(final double[] t) {
	    double maximum = -9999;   // start with the first value
	    for (int i=1; i<t.length; i++) {
	        if (t[i] > maximum) {
	            maximum = t[i];   // new maximum
	        }
	    }
	    return maximum;
	}

	public final void paintControl(final PaintEvent e)
	{
		GC gc = e.gc;
		int width = 0, height=0;
		width = this.getSize().x;
		height = this.getSize().y;

		graph.setGC(gc);
		graph.updateBounds(width, height);

		graph.paintArea();
	}

	public final void mouseMove(final MouseEvent e) {
		
		if(dragging && draggingEnabled)
		{
			int myPixelsDifference = e.x - dragBeginX;
			if(graph.setDraggedPixels(myPixelsDifference, true)) {
				redraw();
			}
		}
		if(e.x!=dragBeginX && e.y != dragBeginY && clicking) 
		{
			clicking = false;
		}
	}

	public final void setDraggingEnabled(final boolean dragEnabled) {
		this.draggingEnabled = dragEnabled;
	}

	public final boolean isDraggingEnabled() {
		return draggingEnabled;
	}

	public final void setFrequencyGraph(final FriedmanGraph frequencyGraph) {
		graph = frequencyGraph;
	}

	public final FriedmanGraph getFrequencyGraph() {
		return graph;
	}

}
