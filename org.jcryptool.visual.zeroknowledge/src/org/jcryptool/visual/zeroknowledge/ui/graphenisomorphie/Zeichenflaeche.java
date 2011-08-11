// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.graphenisomorphie;

import java.util.Collections;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Graph;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Kante;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Knoten;

/**
 * Zeichenfläche für einen Graphen, der aus Linien für die Kanten und Punkte für die Knoten sowie
 * Punkten für die Position der dazugehörigen Erklärungen besteht. Eine Erklärung besteht zumeist
 * aus zwei Teilen: erstens der Nummer des Punktes uns zweitens aus der Nummer, die der Knoten
 * hatte, bevor er transformiert wurde. Der erste Teil ist in der Farbe der Punkte, der zweite Teil
 * in der Farbe des Graphen, aus dem dieser Graph entstanden ist.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Zeichenflaeche extends Canvas {
    private int color_main;

    private int color_vorher;

    private GC gc;

    private Graph graph;

    private Vector<Line> lines;

    private Vector<Point> numbers;

    private Vector<Point> points;

    private boolean vorher;

    /**
     * Konstruktor für die Zeichenfläche
     *
     * @param parent Parent dieses Objektes
     * @param graph Graph, der dargestellt werden soll
     * @param color Farbe der Punkte
     * @param color_vorher Farbe für den zweiten Teil der Erklärung zu dne Punkten
     * @param vorher gibt an, ob ausgegeben werden soll, welche Nummer der Punkt vorher hatte
     */
    public Zeichenflaeche(Group parent, Graph graph, int color, int color_vorher, boolean vorher) {
        super(parent, SWT.NONE | SWT.BORDER | SWT.BACKGROUND);

        // Farben etc. setzen
        color_main = color;
        this.color_vorher = color_vorher;
        this.vorher = vorher;
        this.graph = graph;

        // Paint-Listener hinzufuegen
        addPaintListener(
        /**
         * PaintListener, der darauf achtet, ob sich der Canvas neu zeichnen soll.
         */
        new PaintListener() {
            /**
             * Methode, die das Canvas neu zeichnet.
             */
            public void paintControl(PaintEvent e) {
                paint(e);
            }
        });

        // allgemeine Angaben
        setSize(130, 130);
        setLocation(5, 15);
        setLayout(new FillLayout());
        this.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

        // Graphen in Linien und Punkte umwandeln
        calculate();
    }

    /**
     * Methode zum Zeichnen der Graphen
     *
     * @param e PaintEvent, das geschickt wird, wenn sich der Canvas neu zeichnen soll
     */
    public void paint(PaintEvent e) {
        // Werte im gc setzen
        gc = e.gc;
        gc.setLineWidth(1);
        gc.setAntialias(SWT.ON);
        gc.setBackground(this.getDisplay().getSystemColor(color_main));
        gc.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_BLACK));

        // jede Kante zeichnen
        for (Line l : lines) {
            gc.drawLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
        }
        // jeden Knoten zeichnen
        for (Point p : points) {
            gc.fillOval(p.getXCoord() - 5, p.getYCoord() - 5, 10, 10);
        }

        // Farben neu setzen
        gc.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gc.setForeground(this.getDisplay().getSystemColor(color_main));

        // Position der begleitenden Texte setzen
        int backx = -3;
        if (vorher)
            backx -= 7;

        // jeden Knoten beschriften
        for (Point n : numbers) {
            gc.drawString(String.valueOf(n.getNummer() + 1), n.getXCoord() + backx,
                    n.getYCoord() - 6);
        }

        // u.U. angeben, welche Nummer der Knoten frueher hatte
        if (vorher) {
            gc.setForeground(this.getDisplay().getSystemColor(color_vorher));
            for (Point n : numbers) {
                gc.drawString(n.getAlt(), n.getXCoord() + backx + 7, n.getYCoord() - 6);
            }
        }
        gc.dispose();
    }

    /**
     * Berechnet die einzelnen Positionen der Punkte, Linien und erläuternden Texte.
     */
    private void calculate() {
        if (graph == null)
            return;
        Vector<Knoten> knoten = graph.getKnoten();
        Vector<Kante> kanten = graph.getKanten();
        Collections.sort(knoten);
        Collections.sort(kanten);

        // neue Vektoren erstellen
        lines = new Vector<Line>(kanten.size());
        points = new Vector<Point>(knoten.size());
        numbers = new Vector<Point>(knoten.size());

        // Position des Textes
        int pos = -50;
        if (vorher)
            pos = -54;

        // Anzahl der Knoten und Berechnung des Winkels
        int anzahl = knoten.size();
        double angle = Math.PI * 2.0 / anzahl;

        for (int i = 0; i < anzahl; i++) {
            // Die einzelnen Knoten erstellen, rotieren und verschieben
            Knoten tmp = knoten.get(i);
            Point c = new Point(0, -35, tmp.getNummer());
            c.rotate(i * angle);
            c.translate(60, 60);
            points.add(c);

            // die einzelnen Positionen der Texte erstellen, rotieren und
            // verschieben
            c = new Point(0, pos, tmp.getNummer(), tmp.getVorher());
            c.rotate(i * angle);
            c.translate(60, 60);
            numbers.add(c);
        }
        // Anzahl der Kanten
        anzahl = graph.getKanten().size();
        for (int i = 0; i < anzahl; i++) {
            // Kante, die als Linie dargestellt werden soll
            Kante k = kanten.get(i);
            // die beiden Knoten und ihre Nummern
            Knoten[] kn = k.getKnoten();
            int nummer1 = kn[0].getNummer();
            int nummer2 = kn[1].getNummer();
            // die beiden Punkte
            Point p1 = null;
            Point p2 = null;
            // durchsuche alle Punkte
            for (Point c : points) {
                // Startpunkt gefunden
                if (c.getNummer() == nummer1) {
                    p1 = c;
                }
                // Endpunkt gefunden
                if (c.getNummer() == nummer2) {
                    p2 = c;
                }
            }
            // wenn Start- und Endpunkt gefunden: neue Linie erstellen und
            // hinzufuegen
            if (p1 != null && p2 != null) {
                Line l = new Line(p1, p2);
                lines.add(l);
            }
        }
    }
}