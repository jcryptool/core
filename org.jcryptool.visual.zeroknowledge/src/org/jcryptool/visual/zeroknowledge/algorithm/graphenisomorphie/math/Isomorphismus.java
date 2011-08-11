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

import java.util.Random;
import java.util.Vector;

/**
 * Diese Klasse repräsentiert einen Permutations-Isomorphismus.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Isomorphismus {

    /**
     * Isomorphismus der Länge 0
     */
    public static final Isomorphismus NULL = new Isomorphismus(0);

    private int[][] tausch;

    /**
     * Default-Konstruktor
     */
    public Isomorphismus() {
    }

    /**
     * Konstruktor, der einen Isomorphismus erstellt, der amount Stellen permutiert. Auf welche Zahl
     * welche abgebildet wird, wird zufällig ausgewählt.
     * 
     * @param amount Anzahl der Zahlen, die permutiert werden
     */
    public Isomorphismus(int amount) {
        Random r = new Random();
        tausch = new int[amount][2];
        Vector<Integer> tmp = new Vector<Integer>();
        for (int i = 0; i < amount; i++) {
            tausch[i][0] = i;
            tmp.add(i);
        }
        int i = 0;
        while (tmp.size() > 0) {
            tausch[i++][1] = tmp.remove(r.nextInt(tmp.size()));
        }
    }

    /**
     * Komponiert diesen Isomorphismus mit dem anderen. Berechnet und zurückgegeben wird this * iso
     * 
     * @param iso Isomorphismus, der mit diesem konkatiniert wird
     * @return this * iso
     * @throws IllegalArgumentException wird geworfen, wenn die Isomorphismen unterschiedliche
     *         Längen haben
     */
    public Isomorphismus concat(Isomorphismus iso) {
        if (iso.tausch.length != tausch.length)
            throw new IllegalArgumentException(Messages.Isomorphismus_0);
        Isomorphismus back = new Isomorphismus();
        int[][] perm = new int[tausch.length][2];
        for (int i = 0; i < perm.length; i++) {
            perm[i][0] = i;
            perm[i][1] = tausch[iso.tausch[i][1]][1];
        }
        back.setPerm(perm);
        return back;
    }

    /**
     * invertiert den aktuellen Isomorphismus und liefert das Ergebnis als neuen Isomorphismus
     * zurück.
     * 
     * @return den inversen Isomorphismus von dem aktuellen
     */
    public Isomorphismus inverse() {
        Isomorphismus back = new Isomorphismus();
        int[][] perm = new int[tausch.length][2];
        for (int i = 0; i < tausch.length; i++) {
            perm[i][0] = i;
            perm[tausch[i][1]][1] = i;
        }
        back.setPerm(perm);
        return back;
    }

    /**
     * gibt an, auf welche Zahl i abgebildet wird.
     * 
     * @param i die Zahl, die abgebildet werden soll
     * @return die Zahl, auf die i abgebildet wird
     * @throws RuntimeException wird geworfen, wenn i außerhalb des Wertebereiches liegt
     */
    public int of(int i) {
        if (i < 0 || i >= tausch.length)
            throw new RuntimeException(Messages.Isomorphismus_1);
        return tausch[i][1];
    }

    /**
     * setzt die Permutationsmatrix
     * 
     * @param permutation Permutationsmatrix, die gesetzt werden soll
     * @throws RuntimeException wird geworfen, wenn die Permutationsmatrix nicht die Tiefe 2 hat
     */
    public void setPerm(int[][] permutation) {
        if (permutation.length != 0 && permutation[0].length != 2)
            throw new RuntimeException(Messages.Isomorphismus_2);
        this.tausch = permutation;
    }

    /**
     * Liefert die String-Repräsentation des Graphen: in der oberen Zeile sind die Zahlen von 0 bis
     * n-1, in der unteren die, auf die der jeweilige Wert abgebildet wird.
     * 
     * @see Object#toString()
     */
    public String toString() {
        String back = "|"; //$NON-NLS-1$
        for (int i = 0; i < tausch.length; i++) {
            back += (tausch[i][0] + 1) + " "; //$NON-NLS-1$
        }
        back += "|\n|"; //$NON-NLS-1$
        for (int i = 0; i < tausch.length; i++) {
            back += (tausch[i][1] + 1) + " "; //$NON-NLS-1$
        }
        back += "|"; //$NON-NLS-1$
        return back;
    }
}
