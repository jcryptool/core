package org.jcryptool.visual.aco.view;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.visual.aco.controller.AntColEventController;
import org.jcryptool.visual.aco.model.CommonModel;

public class AntColGraphComposite extends Composite {

	private static final int KNOTRADIUS = 120;
	private static final int ANTRADIUS = 90;
	
	private CommonModel m;
	private Canvas canv;

	private int x; // Position Ameise
	private int y;
	private int x2; // Ziel Ameise
	private int y2;
	private boolean isAnimationRunning = false;
	private final Color highlightColor = new Color(this.getDisplay(), 100, 190, 0);
	private Color normalColor = new Color(this.getDisplay(), 50, 120, 50);
	private Color antColor = new Color(this.getDisplay(), 139, 90, 40);
	private Color eyeColor = new Color(this.getDisplay(), 255, 255, 255);
	private AntColEventController controller;

	/**
	 * Konstruktor erhaelt Model und Parent.
	 * 
	 * @param model
	 * @param c
	 */
	public AntColGraphComposite(CommonModel model, Composite c) {
		super(c, SWT.NONE);
		this.m = model;
		setLayout(new GridLayout(1, false));
		redraw();
	}

	public void redraw() {
		super.redraw();
		// visualisierung nur zeigen, wenn die schlüssellänge zwischen 3 und 5
		// ist.
		if (canv != null) {
			canv.dispose();
		}

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = 365;
		// data.heightHint = 365;
		canv = new Canvas(this, SWT.NONE);
		canv.setLayoutData(data);

		// Listener, der festlegt was geschieht wenn redraw aufgerufen wird
		canv.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				// Fall: Entschluesselung darstellen
				drawKnots(e); // Graph zeichnen
				if (m.getState() != 0) {
					drawAllPropabilities(e);
					drawTrail(e);
					// if (m.isAnimateable()) // Ameise zeichnen
					drawAnt(e, new Point(x, y), new Point(x2, y2));
				}
			}
		});

		setAnt();
		layout();
	}

	public void animationStep() {
		final Display d = this.getDisplay();
		x2 = getAntCoord(m.getKnot())[0];
		y2 = getAntCoord(m.getKnot())[1];
		// Schrittweite
		isAnimationRunning = true;
		final int diffx = (int) (Math.abs(x - x2) / 15 + 0.5);
		final int diffy = (int) (Math.abs(y - y2) / 15 + 0.5);
		// Thread, der Bewegung uebernimmt
		Runnable runnable = new Runnable() {
			public void run() {
				// Schritt
				if (x - x2 > 0)
					x -= diffx;
				else
					x += diffx;
				if (y - y2 > 0)
					y -= diffy;
				else
					y += diffy;
				if (Math.abs(x - x2) > 15 || Math.abs(y - y2) > 15) {
					canv.redraw(); // neu zeichnen
					d.timerExec(150, this); // Schleife
				} else { // angekommen
					setAnt();

					canv.redraw(); // neu zeichnen
					controller.afterGraphDrawn();
					isAnimationRunning = false;

				}
			}
		};
		d.timerExec(1, runnable); // Starten
	}

	private void drawKnots(PaintEvent e) {
		e.gc.setForeground(ColorService.BLACK);
		String[] knots = m.getKnots();

		Vector<Integer> trail = m.getTrail();
		for (int i = 0; i < knots.length; i++) {
			int[] a = this.getKnotCoord(i);
			drawKnot(e, knots[i], a[0], a[1], trail.contains(i), i + 1);
		}
	}

	/**
	 * Zeichnet den von der Ameise zurueckgelegten Weg auf das Canvas
	 * 
	 * @param e
	 *            PaintEvent
	 */
	private void drawTrail(PaintEvent e) {
		Vector<Integer> weg = m.getTrail();
		if (weg.size() > 1) {
			for (int i = 0; i < weg.size() - 1; i++) {
				drawKante(e, weg.elementAt(i), weg.elementAt(i + 1));
			}
		}
	}

	public void drawAllPropabilities(PaintEvent e) {
		String[] knots = m.getKnots();
		double[] probs = m.getProbabilities(); // Wahrscheinlichkeiten holen
		for(int i = 0; i < knots.length; i++){
			int[] a = getKnotCoord(i);
			drawPropability(e, a[0], a[1], probs[i]);
		}
	}

	/**
	 * Draw the probability of the node.
	 * @param e
	 * @param x The center of the node.
	 * @param y The center of the node.
	 * @param prob The probability.
	 */
	private void drawPropability(PaintEvent e, int x, int y, double prob) {
		if (prob > 0) { // W'keit
			String str = prob * 100 + ""; //$NON-NLS-1$
			str = str.substring(0, str.indexOf('.') + 2) + " %"; //$NON-NLS-1$

			
			// The radius of the circle is 32.
			// To get the text a bit below the circle I used 37
			// Thus there are 5 px space between circle and upper bound
			// of the text.
			y = y + 37;
			e.gc.setBackground(ColorService.LIGHTGRAY);
			// This is the size of the string in pixels.
			// To get it centered textSize.x / 2.
			Point textSize = e.gc.textExtent(str);
			e.gc.drawString(str, x - (textSize.x / 2), y);
		}
	}


	/**
	 * Zeichnet eine Kante des Weges von Knoten i zu Knoten j;
	 * 
	 * @param e
	 *            PaintEvent
	 * @param i
	 *            Knoten i
	 * @param j
	 *            Knoten j
	 */
	private void drawKante(PaintEvent e, int i, int j) {
		e.gc.setForeground(highlightColor);

		int[] a = this.getKnotCoord(i);
		int[] b = this.getKnotCoord(j);
		e.gc.drawLine(a[0], a[1], b[0], b[1]);
	}


	/**
	 * Zeichnet einen Knoten bei den uebergebenen Koordinaten und mit dem
	 * uebergebenen Text, Nummer und Wahrscheinlichkeit auf das Canvas.
	 * 
	 * @param e
	 *            PaintEvent
	 * @param s
	 *            Spalte die zum Knoten gehoert
	 * @param x
	 *            x-Koordinate Knoten
	 * @param y
	 *            y-Koordinate Knoten
	 * @param set
	 *            Knoten bereits passiert?
	 * @param prob
	 *            W'keit, dass Ameise zu diesem Knoten im naechsten Schritt geht
	 * @param nr
	 *            Knotennummer
	 */
	private void drawKnot(PaintEvent e, String s, int x, int y, boolean set, int nr) {
		if (set) // wenn bereits passiert in grün
			e.gc.setBackground(highlightColor);
		else
			e.gc.setBackground(normalColor);
		
		// Kreis und Nummer zeichnen
		e.gc.fillOval(x - 32, y - 32, 65, 65);
		Point nrSize = e.gc.textExtent(Integer.toString(nr));
		e.gc.drawString(nr + "", x  - nrSize.x / 2, y - nrSize.y / 2); //$NON-NLS-1$
		if (s.length() > 5) {
			s = s.substring(0, 5);
		}
		e.gc.setBackground(ColorService.LIGHTGRAY);
		
		// Calculate the y Position of the first character.
		int fontHeight = e.gc.getFontMetrics().getHeight();
		x = x + 37;
		switch (s.length()) {
		case 1:
			y = y - fontHeight / 2;
			break;
		case 2:
			y = y - fontHeight;
			break;
		case 3:
			y = y - fontHeight / 2 - fontHeight;
			break;
		case 4:
			y = y - 2 * fontHeight;
			break;
		case 5:
			y = y - fontHeight / 2 - 2 * fontHeight;
			break;
		default:
			break;
		}

		for (int i = 0; i < s.length(); i++) { // Text
			e.gc.drawString(
					Character.toUpperCase(s.charAt(i)) + "", x, y + fontHeight * i); //$NON-NLS-1$
		}
	}

	public void destroyGraph() {
		canv.dispose();
	}

	/**
	 * Zeichnet eine Ameise beim Punkt p auf das Canvas mit Orientierung
	 * Richtung Punkt p2
	 * 
	 * @param e
	 *            PaintEvent
	 * @param p
	 *            Ort der Ameise
	 * @param p2
	 *            Orientierung der Ameise
	 */
	private void drawAnt(PaintEvent e, Point p, Point p2) {
		Color col = antColor;
		e.gc.setBackground(col);
		e.gc.fillOval(p.x, p.y, 15, 15);
		double x = p2.x - p.x;
		double y = p2.y - p.y;
		// Laenge Vektor
		double n = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		x /= n;
		y /= n;
		// Rumpf
		e.gc.fillOval((int) (p.x + 15 * x), (int) (p.y + 15 * y), 15, 15);
		e.gc.fillOval((int) (p.x - 15 * x), (int) (p.y - 15 * y), 15, 15);

		// Fuehler
		col = new Color(this.getDisplay(), 150, 150, 150);
		e.gc.setForeground(col);
		e.gc.drawLine((int) (p.x + 7 + 20 * x + 3 * y),
				(int) (p.y + 7 + 20 * y - 3 * x),
				(int) (p.x + 7 + 26 * x + 3 * y),
				(int) (p.y + 7 + 26 * y - 3 * x));
		e.gc.drawLine((int) (p.x + 7 + 20 * x - 3 * y),
				(int) (p.y + 7 + 20 * y + 3 * x),
				(int) (p.x + 7 + 26 * x - 3 * y),
				(int) (p.y + 7 + 26 * y + 3 * x));

		// Augen
		// col = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		e.gc.setBackground(eyeColor);
		e.gc.fillOval((int) (p.x + 7 + 17 * x + 3 * y),
				(int) (p.y + 7 + 17 * y - 3 * x), 3, 3);
		e.gc.fillOval((int) (p.x + 7 + 17 * x - 3 * y),
				(int) (p.y + 7 + 17 * y + 3 * x), 3, 3);
		// col.dispose();*/
	}

	/**
	 * Liefert die Koordinaten, wo die Ameise platziert werden muss, wenn sie
	 * sich bei Knoten i befindet.
	 * 
	 * @param i
	 *            Knoten, bei dem sich die Ameise befindet
	 * @return c Array mit zugehoerigen Koordinaten
	 */
	private int[] getAntCoord(int i) {
		return getCircleCoord(i, ANTRADIUS, new Point(175, 152));
	}
	

	private int[] getKnotCoord(int i) {
		return getCircleCoord(i, KNOTRADIUS, new Point(183,160));
	}

	private int[] getCircleCoord(int i, int radius, Point center) {
		int size = m.getSize();
		int[] out = new int[2];
		out[0] = (int) (Math.cos(Math.PI * 2 * (double) i / size +  Math.PI /2-Math.PI/ size) * radius + center.x);
		out[1] = (int) (Math.sin(Math.PI * 2 * (double) i / size +  Math.PI /2-Math.PI/ size) * radius + center.y);
		return out;
	}
	/**
	 * Setzt die Koordinaten, wo sich die Ameise befindet in Abhaengigkeit von
	 * dem jeweils aktuellen Knoten.
	 * 
	 */
	private void setAnt() {
		int i = m.getKnot();
		int[] a = this.getAntCoord(i);
		int oldX = x;
		int oldY = y;
		x = a[0];
		y = a[1];

		x2 = -(a[1]-172) + a[0];
		y2 = (a[0]-172) + a[1];

		int scalarProd = (x-oldX)*(x2-x)+(y-oldY)*(y2-y);
		
		if(scalarProd < 0){
			x2 = (a[1]-172) + a[0];
			y2 = -(a[0]-172) + a[1];
		}
		
	}

	public boolean isAnimationRunning() {
		return isAnimationRunning;
	}

	public void addController(AntColEventController reg) {
		this.controller = reg;
	}

}
