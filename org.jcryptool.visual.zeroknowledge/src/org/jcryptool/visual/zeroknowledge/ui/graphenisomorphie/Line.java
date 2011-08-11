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

/**
 * Klasse zur Repräsentation einer graphischen Linie zwischen zwei Punkten.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Line {

    private Point p;

    private Point q;

    /**
     * Konstruktor einer Linie von p1 zu p2.
     * 
     * @param p1 Startpunkt der Linie
     * @param p2 Endpunkt der Linie
     */
    public Line(Point p1, Point p2) {
        this.p = p1;
        this.q = p2;
    }

    /**
     * Methode zum Erhalten der x-Koordinate des Startpunktes
     * 
     * @return x-Koordinate des Startpunktes
     */
    public int getX1() {
        return p.getXCoord();
    }

    /**
     * Methode zum Erhalten der x-Koordinate des Endpunktes
     * 
     * @return x-Koordinate des Endpunktes
     */
    public int getX2() {
        return q.getXCoord();
    }

    /**
     * Methode zum Erhalten der y-Koordinate des Startpunktes
     * 
     * @return y-Koordinate des Startpunktes
     */
    public int getY1() {
        return p.getYCoord();
    }

    /**
     * Methode zum Erhalten der y-Koordinate des Endpunktes
     * 
     * @return y-Koordinate des Endpunktes
     */
    public int getY2() {
        return q.getYCoord();
    }

    /**
     * Methode zum Rotieren der Linie
     * 
     * @param angle Winkel, um den gedreht werden soll (radient - Einteilung)
     */
    public void rotate(double angle) {
        p.rotate(angle);
        q.rotate(angle);
    }

    /**
     * Methode zum Translatieren der Linie
     * 
     * @param x Wert, um den die x-Koordinate verschoben werden soll
     * @param y Wert, um den die y-Koordinate verschoben werden soll
     */
    public void translate(int x, int y) {
        p.translate(x, y);
        q.translate(x, y);
    }
}
