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
 * Klasse zur Repräsentation eines graphischen Punktes.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Point {

    private int nummer;

    private String vorher;

    private int x;

    private int y;

    /**
     * Konstruktor für einen Punkt, der aus einer x-Koordinate, einer y-Koordinate und einer Nummer
     * besteht.
     *
     * @param x x-Koordinate des Punktes
     * @param y y-Koordinate des Punktes
     * @param nummer Nummer des Punktes
     */
    public Point(int x, int y, int nummer) {
        this.x = x;
        this.y = y;
        this.nummer = nummer;
        vorher = ""; //$NON-NLS-1$
    }

    /**
     * Konstruktor für einen Punkt, der aus einer x-Koordinate, einer y-Koordinate und einer Nummer
     * besteht.
     *
     * @param x x-Koordinate des Punktes
     * @param y y-Koordinate des Punktes
     * @param nummer Nummer des Punktes
     */
    public Point(int x, int y, int nummer, int vorher) {
        this.x = x;
        this.y = y;
        this.nummer = nummer;
        this.vorher = "(" + (vorher + 1) + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Methode zum Erhalten der Nummer, die der Knoten zu diesem Punkt hatte, bevor er transformiert
     * wurde
     *
     * @return Nummer des zugehörigen Knotens, bevor er transformiert wurde
     */
    public String getAlt() {
        return vorher;
    }

    /**
     * Methode zum Erhalten der Nummer des Punktes
     *
     * @return Nummer des Punktes
     */
    public int getNummer() {
        return nummer;
    }

    /**
     * Methode zum Erhalten der x-Koordinate des Punktes
     *
     * @return x-Koordinate des Punktes
     */
    public int getXCoord() {
        return x;
    }

    /**
     * Methode zum Erhalten der y-Koordinate des Punktes
     *
     * @return y-Koordinate des Punktes
     */
    public int getYCoord() {
        return y;
    }

    /**
     * rotiert den Punkt um angle in der radient-Darstellung
     *
     * @param angle radient-Wert des Winkels, um den gedreht werden soll
     */
    public void rotate(double angle) {
        int x_new = (int) (x * Math.cos(angle) - y * Math.sin(angle));
        int y_new = (int) (x * Math.sin(angle) + y * Math.cos(angle));
        x = x_new;
        y = y_new;
    }

    /**
     * Methode zum Translatieren der Linie
     *
     * @param x_trans Wert, um den die x-Koordinate verschoben werden soll
     * @param y_trans Wert, um den die y-Koordinate verschoben werden soll
     */
    public void translate(int x_trans, int y_trans) {
        x += x_trans;
        y += y_trans;
    }
}
