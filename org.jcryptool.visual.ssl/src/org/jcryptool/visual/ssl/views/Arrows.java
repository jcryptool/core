//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.ssl.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


/**
 * This class implements a JPanel that provides functions for drawing a list of
 * arrows inside the panel. The arrows can be represented in black or in red.
 * 
 * @author Denk Gandalf
 * 
 */
public class Arrows extends Canvas 
{
	/**
	 * A vector which saves all arrows which need to be painted
	 */
	private List<int[]> arrows;
	
	private GC gc;

	/**
	 * Constructor of the class, creates a new Vector for the arrows
	 */
	
	public Arrows(Composite parent, int style) {
		super(parent, style);
		arrows = new ArrayList<int[]>();
		addPaintListener(new PaintListener() {
            @Override
			public void paintControl(PaintEvent e) 
            {
            	paint(e);
            }
        });
	}
	
	/**
	 * Implements the paint method. Paints all arrows in the Vector arrow inside
	 * the panel and sets the background to grey.
	 */
	private void paint(PaintEvent g)
	{
		gc = g.gc;
		gc.setAntialias(SWT.ON);
		int a[] = { 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < arrows.size(); i++) 
		{
			a = arrows.get(i);
			int x1 = a[0];
			int y1 = a[1];
			int x2 = a[2];
			int y2 = a[3];
			
			if(a[5]==180)
			{
				gc.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
				gc.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
				
			}
			else
			{
				gc.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				gc.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			}
			gc.drawLine(x1, y1, x2,y2);
			if(y1!=y2)
			{
				gc.fillPolygon(new int[]{x2,y2,x2-10,y2-5,x2-3,y2-12});
			}
			else if(x2>x1)
			{
				gc.fillPolygon(new int[]{x2,y2,x2-10,y2+5,x2-10,y2-5});
			}
			else
			{
				gc.fillPolygon(new int[]{x2,y2,x2+10,y2+5,x2+10,y2-5});
			}
		}
		gc.dispose();
	}


	/**
	 * Adds an arrow to the list of arrows which are painted. The arrow can be
	 * red or black´and goes form x1, y1 to x2, y2 with a arrowhead in the
	 * pointing direction
	 * 
	 * @param x1
	 *            X-coordination of the start point
	 * @param y1
	 *            Y-coordination of the start point
	 * @param x2
	 *            X-coordination of the end point
	 * @param y2
	 *            Y-coordination of the end point
	 */
	public void nextArrow(int x1, int y1, int x2, int y2, int r, int g, int b) {
		int aro[] = { x1, y1, x2, y2, r, g, b };
		arrows.add(aro);
		redraw();
	}

	/**
	 * Removes the arrow that has been added latest
	 */
	public void removeLastArrow() {
		arrows.remove(arrows.size() - 1);
		redraw();
	}

	/**
	 * Removes all arrows.
	 */
	public void resetArrows() {
		arrows.clear();
		redraw();
	}

	/**
	 * Moves all arrows inside the panel for the value of distance positive
	 * values move the arrows upwards, negative once move them downwards.
	 * 
	 * @param distance
	 *            the distance the arrows are moved
	 */
	public void moveArrowsby(int distance) {
		int a[] = { 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < arrows.size(); i++) {
			a = arrows.get(i);
			a[1] = a[1] - distance;
			a[3] = a[3] - distance;
			arrows.set(i, a);
		}
		redraw();
	}
}
