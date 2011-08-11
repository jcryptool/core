// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math;

/**
 * Repräsentiert eine ungerichtete Kante innerhalb eines Graphen. Eine Kante besteht aus zwei
 * Knoten, die durch diese Kante verbunden werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Kante implements Comparable<Kante> {

    private Knoten one;

    private Knoten two;

    /**
     * Konstruktor für eine Kante zwischen den Knoten eins und zwei
     *
     * @param eins einer der Knoten, die durch die Kante verbunden werden
     * @param zwei der andere Knoten, die durch die Kante verbunden werden
     */
    public Kante(Knoten eins, Knoten zwei) {
        if (eins.compareTo(zwei) <= 0) {
            one = eins;
            two = zwei;
        } else {
            one = zwei;
            two = eins;
        }
    }

    /**
     * Methode zum Vergleichen dieser Kante mit einer anderen
     *
     * @param k Kante, die mit dieser verglichen werden sollen
     * @return 0, wenn die Kanten gleich sind; 1, wenn diese Kante "größer" ist als die übergebene;
     *         -1, wenn diese Kante "kleiner" ist als die übergebene
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Kante k) {
        if (one.getNummer() < k.one.getNummer())
            return -1;
        if (one.getNummer() > k.one.getNummer())
            return 1;
        if (two.getNummer() < k.two.getNummer())
            return -1;
        if (two.getNummer() > k.two.getNummer())
            return 1;
        return 0;
    }

    /**
     * Methode zum Vergleichen dieser Kante mit dem uebergebenen Objekt
     *
     * @param o Objekt, mit dem diese Kante verglichen werden soll
     * @return true, wenn die beiden Objekt gleich sind, sonst false
     * @see Object#equals(Object)
     */
    public boolean equals(Object o) {
        if (!(o instanceof Kante))
            return false;
        if (o == this)
            return true;
        return compareTo((Kante) o) == 0;
    }

    /**
     * gibt den Knoten "am anderen Ende der Kante" zurück
     *
     * @param einer ein Ende des Knoten
     * @return Knoten, der am anderen Ende der Kante liegt
     */
    public Knoten getAnderenKnoten(Knoten einer) {
        if (einer.equals(one))
            return two;
        else if (einer.equals(two))
            return one;
        return null;
    }

    /**
     * gibt ein Array mit den Knoten der Kante zurück
     *
     * @return Array mit den Knoten der Kante
     */
    public Knoten[] getKnoten() {
        Knoten[] back = new Knoten[2];
        back[0] = one;
        back[1] = two;
        return back;
    }
}