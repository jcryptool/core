package org.jcryptool.visual.ssl.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import javax.swing.JPanel;

/**
 * This class implements a JPanel that provides functions for drawing a list of
 * arrows inside the panel. The arrows can be represented in black or in red.
 * 
 * @author Denk Gandalf
 * 
 */
public class Arrows extends Panel {

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
	public Arrows() {
		arrows = new Vector();
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
	private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		Graphics2D g = (Graphics2D) g1.create();

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		g.drawLine(0, 0, len, 0);
		g.fillPolygon(new int[] { len, len - ARR_SIZE, len - ARR_SIZE, len },
				new int[] { 0, -ARR_SIZE, ARR_SIZE, 0 }, 4);
	}

	/**
	 * Overwrites the paint method. Paints all arrows in the Vector arrow inside
	 * the panel and sets the background to grey.
	 */
	public void paint(Graphics g) {
		int a[] = { 0, 0, 0, 0, 0, 0, 0 };
		int i;
		g.setColor(new Color(240, 240, 240));
		g.fillRect(0, 0, 350, 760);
		for (i = 0; i < arrows.size(); i++) {
			a = arrows.get(i);
			g.setColor(new Color(a[4], a[5], a[6]));
			this.drawArrow(g, a[0], a[1], a[2], a[3]);
		}
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
		arrows.addElement(aro);
		repaint();
	}

	/**
	 * Removes the arrow that has been added latest
	 */
	public void removeLastArrow() {
		arrows.removeElementAt(arrows.size() - 1);
		repaint();
	}

	/**
	 * Removes all arrows.
	 */
	public void resetArrows() {
		arrows.clear();
		repaint();
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
		repaint();
	}
}
