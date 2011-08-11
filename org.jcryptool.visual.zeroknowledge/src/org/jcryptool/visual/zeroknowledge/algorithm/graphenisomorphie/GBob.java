// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie;

import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Graph;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Isomorphismus;

/**
 * Klasse zum Speichern der Parameter, die Bob kennt
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class GBob extends GPerson {

    private int b;

    /**
     * Konstruktor, der eine Person mit Namen Bob
     */
    public GBob() {
        super("Bob"); //$NON-NLS-1$
        b = -1;
    }

    /**
     * Generiert ein Bit b, das angibt, von welchem Graphen ein Isomorphismus zu h angegeben werden
     * soll
     */
    public void generateB() {
        b = rand.nextInt(2);
        notifyChanged();
    }

    /**
     * Methode zum Erhalten von b
     * 
     * @return Wert von b
     */
    public int getB() {
        return b;
    }

    /**
     * Methode zum Zurücksetzen von b
     * 
     * @see GPerson#reset()
     */
    public void reset() {
        b = -1;
        super.reset();
    }

    /**
     * Methode zum Zuruecksetzen von b
     * 
     * @see GPerson#resetNotSecret()
     */
    public void resetNotSecret() {
        b = -1;
        super.resetNotSecret();
    }

    /**
     * setzt einen der Graphen, deren Isomorphis bewiesen werden soll
     * 
     * @param g0 Graph G_0
     */
    public void setG0(Graph g0) {
        this.g0 = g0;
        notifyChanged();
    }

    /**
     * setzt einen der Graphen, deren Isomorphis bewiesen werden soll
     * 
     * @param g1 Graph G_1
     */
    public void setG1(Graph g1) {
        this.g1 = g1;
        notifyChanged();
    }

    /**
     * Methode zum Setzen von H
     * 
     * @param h Graph, zu dem später ein Isomorphismus angegeben werden soll
     */
    public void setH(Graph h) {
        this.graphH = h;
    }

    /**
     * Methode zum Setzen des Isomorphismus von G<sub>b</sub> zu H
     * 
     * @param iso Isopmrphismus mit iso(G<sub>b</sub>) = H
     */
    public void setIsomorphismus(Isomorphismus iso) {
        h = iso;
    }

    /**
     * Methode zum Verifizieren der Antwort. Es wird überprüft, ob gilt: h(G<sub>b</sub>) = H
     * 
     * @return true, wenn die Gleichung erfüllt ist, sonst false
     */
    public boolean verify() {
        if (b == 0) {
            return g0.change(h).equals(graphH);
        } else {
            return g1.change(h).equals(graphH);
        }
    }
}