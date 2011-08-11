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
 * Repräsentiert einen Knoten innerhalb eines Graphen. Ein Knoten hat in dieser Implementation eines
 * Graphen eine Nummer und eine Nummer, die er früher einmal hatte.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Knoten implements Comparable<Knoten> {

    private int nummer;

    private int vorher;

    /**
     * erstellt einen neuen Knoten mir der angegeben Nummer
     * 
     * @param nr
     */
    public Knoten(int nr, int alt) {
        nummer = nr;
        vorher = alt;
    }

    /**
     * Vergleicht zwei Knoten miteinander
     * 
     * @param kn Knoten, mit dem dieser Knoten verglichen werden soll
     * @return 0, wenn die Nummern gleich sind; 1, wenn die Nummer dieses Knoten größer ist; -1,
     *         wenn die Nummer dieses Knoten kleiner ist;
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Knoten kn) {
        if (nummer < kn.nummer)
            return -1;
        else if (nummer > kn.nummer)
            return 1;
        return 0;
    }

    /**
     * Methode, die angibt, ob das übergebene Objekt mit diesem Knoten übereinstimmt
     * 
     * @param o Objekt, mit dem dieser Knoten verglichen werden soll
     * @return true, wenn die das Objekt mit diesem Knoten übereinstimmt
     * @see Object#equals(Object)
     */
    public boolean equals(Object o) {
        if (!(o instanceof Knoten))
            return false;
        Knoten k = (Knoten) o;
        return k.nummer == nummer;
    }

    /**
     * Gibt die Nummer des Knoten zurück
     * 
     * @return Nummer des Knoten
     */
    public int getNummer() {
        return nummer;
    }

    /**
     * Methode zum Erhalten der Nummer, die der Knoten hatte, bevor er durch einen Isomorphismus
     * transformiert wurde.
     * 
     * @return Nummer des Knotens, bevor er transformiert wurde.
     */
    public int getVorher() {
        return vorher;
    }
}
