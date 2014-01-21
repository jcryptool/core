package org.jcryptool.visual.ssl.views;

//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.geom.AffineTransform;
import java.util.Vector;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

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
	 * Represents the size of the arrowhead.
	 */
	private final int ARR_SIZE = 5;

	/**
	 * A vector which saves all arrows which need to be painted
	 */
	private Vector<int[]> arrows;

	/**
	 * Constructor of the class, creates a new Vector for the arrows
	 */
	public Arrows(Composite parent, int style, Color color) {
		this(parent, style);
	}
	public Arrows(Composite parent, int style) {
		super(parent, style);
		arrows = new Vector();
		addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) 
            {
            	int a[] = { 0, 0, 0, 0, 0, 0, 0 };
        		int i;
        		Device device = Display.getCurrent ();
        		e.gc.setBackground(new Color(device, 240, 240, 240));
        		//e.gc.fillRectangle(0,0,350,760);
        		e.gc.fillRectangle(getClientArea());
        		for (i = 0; i < arrows.size(); i++) {
        			a = arrows.get(i);
        			e.gc.setForeground(new Color(device, a[4], a[5], a[6]));
        			drawArrow(e, a[0], a[1], a[2], a[3]);
        		}
            }
        });
	}

	/**
	 * Draws a arrow from x, y to x1, y1 with an arrowhead in the right
	 * direction. The coordinations need to be chosen inside the JPanel.
	 * 
	 * @param g1
	 *            gives the Graphic set that is used
	 * @param x1
	 *            X-coordination of the start point
	 * @param y1
	 *            Y-coordination of the start point
	 * @param x2
	 *            X-coordination of the end point
	 * @param y2
	 *            Y-coordination of the end point
	 */
	private void drawArrow(PaintEvent e, int x1, int y1, int x2, int y2) {
		PaintEvent g = (PaintEvent) e;

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		
		//Alter Code
		//AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		//at.concatenate(AffineTransform.getRotateInstance(angle));

		//Neuer Code - funkt nicht
		//Transform awt = new Transform(Display.getCurrent(),1,0,0,1,x1,y1);
		
		/*Transform t;
            t = new Transform(Display.getCurrent());
            double[] matrix = new double[6];
            at.getMatrix(matrix);
            t.setElements((float) matrix[0], (float) matrix[1],
                    (float) matrix[2], (float) matrix[3],
                    (float) matrix[4], (float) matrix[5]);

		//Neuer Code
		g.gc.setTransform(t);*/
		
		
		g.gc.drawLine(x1, y1, x2,y2);
		
		//Alter Code
		//e.gc.fillPolygon(new g.drawLine(0, 0, len, 0);int[]{len,0,len - ARR_SIZE,-ARR_SIZE,len - ARR_SIZE,0,len});
		//g.drawLine(0, 0, len, 0);
		//g.fillPolygon(new int[] { len, len - ARR_SIZE, len - ARR_SIZE, len },
				//new int[] { 0, -ARR_SIZE, ARR_SIZE, 0 }, 4);
	}

	/**
	 * Overwrites the paint method. Paints all arrows in the Vector arrow inside
	 * the panel and sets the background to grey.
	 */
	/*public void paint(Graphics g) {
		int a[] = { 0, 0, 0, 0, 0, 0, 0 };
		int i;
		g.setColor(new Color(240, 240, 240));
		g.fillRect(0, 0, 350, 760);
		for (i = 0; i < arrows.size(); i++) {
			a = arrows.get(i);
			g.setColor(new Color(a[4], a[5], a[6]));
			this.drawArrow(g, a[0], a[1], a[2], a[3]);
		}
	}*/

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
		arrows.addElement(aro);
		redraw();
	}

	/**
	 * Removes the arrow that has been added latest
	 */
	public void removeLastArrow() {
		arrows.removeElementAt(arrows.size() - 1);
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
		int i = 0;
		int a[] = { 0, 0, 0, 0, 0, 0, 0 };
		for (; i < arrows.size(); i++) {
			a = arrows.get(i);
			a[1] = a[1] - distance;
			a[3] = a[3] - distance;
			arrows.set(i, a);
		}
		redraw();
	}
}
