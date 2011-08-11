// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.gui;

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.aco.tutorial.Model;

/**
 * Klasse zur Darstellung der Ablaeufe bei der Kryptanalyse mit Hilfe eines
 * Ameisenalgorithmus. Ist darstellendes Element des Tutorial.
 *
 * @author Philipp Blohm
 * @version 03.08.07
 *
 */
public class Show extends Composite implements Observer {
	private Model m;
	private Canvas canv;
	private int x; // Position Ameise
	private int y;
	private int x2; // Ziel Ameise
	private int y2;
	private Composite desc;
	private GridLayout layout;
	private final Color highlightColor = new Color(this.getDisplay(), 200, 80,
			10);
	private Color normalColor = new Color(this.getDisplay(), 160, 125, 0);
	private Color probabilityColor = highlightColor;

	@Override
	public void dispose() {
		highlightColor.dispose();
		normalColor.dispose();
		super.dispose();
	}

	/**
	 * Konstruktor erhaelt Model und Parent.
	 *
	 * @param model
	 * @param c
	 */
	public Show(Model model, Composite c) {
		super(c, SWT.BORDER);
		this.m = model;
		layout = new GridLayout(2, false);
		setLayout(layout);

		showPermutationMatrix();

	}

	public void showPermutationMatrix() {
		if (desc != null)
			desc.dispose();

		GridData data = new GridData(SWT.FILL, SWT.FILL, false, true);
		desc = new Composite(this, SWT.NONE);
		desc.setLayoutData(data);
		desc.setLayout(new GridLayout(m.getSize(), true));

		int len = m.getSize();
		int depth = (int) Math.ceil(m.getText().length() / (double) len);
		int[] p = m.getPerm();

		Label label = new Label(desc, SWT.NONE);
		label.setText(Messages.getString("Show.permutationMatrix")); //$NON-NLS-1$
		label.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false,
				len, 1));

		// Permutation
		for (int i = 0; i < len; i++) {
			label = new Label(desc, SWT.NONE);
			data = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
			label.setLayoutData(data);
			label.setText("   " + (p[i] + 1) + ".   "); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// Matrix
		for (int i = 0; i < depth; i++) {
			for (int j = 0; j < len; j++) {
				label = new Label(desc, SWT.NONE);
				data = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
				label.setLayoutData(data);
				if (i * len + p[j] < m.getText().length())
					label.setText(m.getText().substring(i * len + p[j],
							i * len + p[j] + 1));
				else
					label.setText("*"); //$NON-NLS-1$
			}
		}

		if (depth < 5) {
			label = new Label(desc, SWT.NONE);
			data = new GridData(SWT.LEFT, SWT.CENTER, false, false,
					m.getSize(), 1);
			label.setLayoutData(data);
			label.setText(""); //$NON-NLS-1$
		}

		label = new Label(desc, SWT.NONE);
		data = new GridData(SWT.LEFT, SWT.CENTER, false, false, m.getSize(), 1);
		label.setLayoutData(data);
		label.setText(Messages.getString("Show.encryptedText")); //$NON-NLS-1$

		Text text = new Text(desc, SWT.BORDER);
		data = new GridData(SWT.FILL, SWT.CENTER, false, false, m.getSize(), 1);
		text.setLayoutData(data);
		text.setEditable(false);
		String s = m.getCipher();
		text.setText(s);

		layout();
	}

	public void showPeromoneMatrix() {
		if (desc != null)
			desc.dispose();

		GridData data = new GridData(SWT.LEFT, SWT.TOP, false, true);
		desc = new Composite(this, SWT.NONE);
		desc.setLayoutData(data);
		desc.setLayout(new GridLayout(m.getSize(), true));

		Label label = new Label(desc, SWT.NONE);
		data = new GridData(SWT.CENTER, SWT.CENTER, false, false, m.getSize(),
				1);
		label.setLayoutData(data);
		label.setText(Messages.getString("Show.pheromoneMatrix")); //$NON-NLS-1$

		double[][] matrix = m.getMatrix();
		for (int row = 0; row < matrix.length; row++) {
			for (int line = 0; line < matrix[row].length; line++) {
				label = new Label(desc, SWT.NONE);
				data = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
				label.setLayoutData(data);
				if (row == line) {
					label.setText("-"); //$NON-NLS-1$
				} else {
					label.setText("" + Math.round(matrix[row][line] * 10) / (double) 10); //$NON-NLS-1$
				}
			}
		}

		label = new Label(desc, SWT.NONE);
		data = new GridData(SWT.LEFT, SWT.CENTER, false, false, m.getSize(), 1);
		label.setLayoutData(data);
		label.setText(""); //$NON-NLS-1$

		label = new Label(desc, SWT.NONE);
		data = new GridData(SWT.LEFT, SWT.CENTER, false, false, m.getSize(), 1);
		label.setLayoutData(data);
		label.setText(Messages.getString("Show.decryptedByAnt1") + //$NON-NLS-1$
				" " + m.getAntNr() + " " + //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("Show.decryptedByAnt2")); //$NON-NLS-1$ //$NON-NLS-2$

		Text text = new Text(desc, SWT.BORDER);
		data = new GridData(SWT.FILL, SWT.CENTER, false, false, m.getSize(), 1);
		text.setLayoutData(data);
		text.setEditable(false);
		text.setText(m.toText(false) + " " + ConvertToString(m.getTrail())); //$NON-NLS-1$

		label = new Label(desc, SWT.NONE);
		label.setText(""); //$NON-NLS-1$

		label = new Label(desc, SWT.NONE);
		data = new GridData(SWT.LEFT, SWT.CENTER, false, false, m.getSize(), 1);
		label.setLayoutData(data);
		label.setText(Messages.getString("Show.decryptedBestKnown")); //$NON-NLS-1$

		text = new Text(desc, SWT.BORDER);
		data = new GridData(SWT.FILL, SWT.CENTER, false, false, m.getSize(), 1);
		text.setLayoutData(data);
		text.setEditable(false);
		text.setText(m.toText(true) + " " + ConvertToString(m.getBestTrail())); //$NON-NLS-1$

		label = new Label(desc, SWT.NONE);
		data = new GridData(SWT.LEFT, SWT.CENTER, false, false, m.getSize(), 1);
		label.setLayoutData(data);
		label.setText(Messages.getString("Show.encryptionKey") + ConvertToString(invert(m.getBestTrail()))); //$NON-NLS-1$
		layout();
	}

	@SuppressWarnings("unchecked")
	private Vector<Integer> invert(Vector<Integer> bestTrail) {
		if (bestTrail == null)
			return null;
		Vector<Integer> newTrail = (Vector<Integer>) bestTrail.clone();
		for (int i = 0; i < bestTrail.size(); i++)
			newTrail.set(bestTrail.get(i), i);

		return newTrail;
	}

	private String ConvertToString(Vector<Integer> input) {
		if (input == null)
			return ""; //$NON-NLS-1$

		String s = ""; //$NON-NLS-1$
		for (Integer i : input)
			s += "," + (i + 1); //$NON-NLS-1$

		return "(" + s.replaceFirst(",", "") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	public void showGraph() {
		if (desc != null)
			desc.dispose();
		if (canv != null)
			canv.dispose();

		GridData data = new GridData(SWT.NONE, SWT.TOP, false, false);
		data.widthHint = 365;
		data.heightHint = 365;
		canv = new Canvas(this, SWT.NONE);
		canv.setLayoutData(data);

		// Listener, der festlegt was geschieht wenn redraw aufgerufen wird
		canv.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				// Fall: Entschluesselung darstellen
				if (m.getNr() > 0) {
					drawTrail(e);
					drawKnots(e); // Graph zeichnen
					if (m.getEnAni()) // Ameise zeichnen
						drawAnt(e, new Point(x, y), new Point(x2, y2));
				}
			}
		});

		canv.layout();
	}

	/**
	 * Setzt die Koordinaten, wo sich die Ameise befindet in Abhaengigkeit von
	 * dem jeweils aktuellen Knoten.
	 *
	 */
	public void setAnt() {
		int fall = m.getKnot();
		if (fall == 0) {
			x = 55;
			y = 115;
			x2 = 230;
			y2 = 115;
		} else if (fall == 1) {
			x = 305;
			y = 115;
			x2 = 230;
			y2 = 115;
		} else if (fall == 2) {
			x = 305;
			y = 185;
			x2 = 230;
			y2 = 185;
		} else if (fall == 3) {
			x = 55;
			y = 185;
			x2 = 230;
			y2 = 185;
		} else if (fall == 4) {
			x = 180;
			y = 80;
			x2 = 180;
			y2 = 250;
		}
	}

	/**
	 * Zeichnet den Graphen auf das Canvas.
	 *
	 * @param e
	 *            PaintEvent
	 */
	public void drawKnots(PaintEvent e) {
		e.gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		String[] knots = m.getKnots();
		Vector<Integer> trail = m.getTrail();
		double[] probs = m.getProbabilities(); // W'keiten holen
		drawKnot(e, knots[0], 30, 50, trail.contains(0), probs[0], 1);
		drawKnot(e, knots[1], 290, 50, trail.contains(1), probs[1], 2);
		drawKnot(e, knots[2], 290, 200, trail.contains(2), probs[2], 3);
		if (knots.length > 3)
			drawKnot(e, knots[3], 30, 200, trail.contains(3), probs[3], 4);
		if (knots.length > 4)
			drawKnot(e, knots[4], 155, 0, trail.contains(4), probs[4], 5);
	}

	/**
	 * Zeichnet den von der Ameise zurueckgelegten Weg auf das Canvas
	 *
	 * @param e
	 *            PaintEvent
	 */
	public void drawTrail(PaintEvent e) {
		Vector<Integer> weg = m.getTrail();
		if (weg.size() > 1) {
			for (int i = 0; i < weg.size() - 1; i++) {
				drawKante(e, weg.elementAt(i), weg.elementAt(i + 1));
			}
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
	public void drawKante(PaintEvent e, int i, int j) {
		e.gc.setForeground(highlightColor);
		int x1, x2, y1, y2;
		if (i == 0 || i == 3)
			x1 = 62;
		else if (i == 1 || i == 2)
			x1 = 322;
		else
			x1 = 187;
		if (i == 0 || i == 1)
			y1 = 82;
		else if (i == 2 || i == 3)
			y1 = 232;
		else
			y1 = 32;
		if (j == 0 || j == 3)
			x2 = 62;
		else if (j == 1 || j == 2)
			x2 = 322;
		else
			x2 = 187;
		if (j == 0 || j == 1)
			y2 = 82;
		else if (j == 2 || j == 3)
			y2 = 232;
		else
			y2 = 32;
		e.gc.drawLine(x1, y1, x2, y2);
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
	public void drawKnot(PaintEvent e, String s, int x, int y, boolean set,
			double prob, int nr) {
		if (set) // wenn bereits passiert in gruen
			e.gc.setBackground(highlightColor);
		else
			e.gc.setBackground(normalColor);
		// Kreis und Nummer zeichnen
		e.gc.fillOval(x, y, 65, 65);
		e.gc.drawString(nr + "", x + 10, y + 20); //$NON-NLS-1$
		if (s.length() > 5)
			s = s.substring(0, 5);
		for (int i = 0; i < s.length(); i++) { // Text
			e.gc.drawString(
					Character.toUpperCase(s.charAt(i)) + "", x + 30, y + 12 * (i + 1) - 8); //$NON-NLS-1$
		}
		if (prob > 0) { // W'keit
			e.gc.setBackground(probabilityColor);
			String str = prob * 100 + ""; //$NON-NLS-1$
			str = str.substring(0, str.indexOf('.') + 2) + "%"; //$NON-NLS-1$
			e.gc.drawString(str, x - 10, y);
		}
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
	public void drawAnt(PaintEvent e, Point p, Point p2) {
		Color col = normalColor;
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
		col = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		e.gc.setBackground(col);
		e.gc.fillOval((int) (p.x + 7 + 17 * x + 3 * y),
				(int) (p.y + 7 + 17 * y - 3 * x), 3, 3);
		e.gc.fillOval((int) (p.x + 7 + 17 * x - 3 * y),
				(int) (p.y + 7 + 17 * y + 3 * x), 3, 3);
		col.dispose();
	}

	/**
	 * Update-Methode reagiert auf Aenderungen am Model. Resultierende
	 * aenderungen werden vorgenommen und die redraw-Methode des Canvas wird
	 * aufgerufen.
	 */
	public void update(Observable arg0, Object arg1) {
		// Animation laufende Ameise
		if (m.getAni() && m.getEnAni()) {
			final Display d = this.getDisplay();
			m.setWorking(true);
			x2 = getCoord(m.getKnot())[0];
			y2 = getCoord(m.getKnot())[1];
			// Schrittweite
			final int diffx = (int) (Math.abs(x - x2) / 10 + 0.5);
			final int diffy = (int) (Math.abs(y - y2) / 10 + 0.5);
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
					if (Math.abs(x - x2) > 5 || Math.abs(y - y2) > 5) {
						canv.redraw(); // neu zeichnen
						d.timerExec(400, this); // Schleife
					} else { // angekommen
						setAnt();
						canv.redraw(); // neu zeichnen
						m.setWorking(false);
						if (m.getAll()) { // falls weitere
							m.steps(); // Schritte kommen
						}
					}
				}
			};
			d.timerExec(1, runnable); // Starten
		} else if (m.getNr() != 0) { // Neu zeichnen ohne Animation
			showGraph();
			showPeromoneMatrix();
			setAnt();
			canv.redraw();
			if (m.getAll())
				m.steps();
		} else
			// Fuer Verschluesselungsteil neuzeichnen
			showPermutationMatrix();
		// canv.redraw();

	}

	/**
	 * Liefert die Koordinaten, wo die Ameise platziert werden muss, wenn sie
	 * sich bei Knoten i befindet.
	 *
	 * @param i
	 *            Knoten, bei dem sich die Ameise befindet
	 * @return c Array mit zugehoerigen Koordinaten
	 */
	public int[] getCoord(int i) {
		int[] c = new int[2];
		if (i == 0) {
			c[0] = 55;
			c[1] = 115;
		} else if (i == 1) {
			c[0] = 305;
			c[1] = 115;
		} else if (i == 2) {
			c[0] = 305;
			c[1] = 185;
		} else if (i == 3) {
			c[0] = 55;
			c[1] = 185;
		} else {
			c[0] = 180;
			c[1] = 80;
		}
		return c;
	}
}
