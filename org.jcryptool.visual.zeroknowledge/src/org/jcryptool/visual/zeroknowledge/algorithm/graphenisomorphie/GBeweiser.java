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

import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Isomorphismus;

/**
 * Abstrakte Klasse zur Repräsentation eines Beweisers. Ein Beweiser möchte Bob beweisen, dass er
 * Alice ist.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public abstract class GBeweiser extends GPerson {

    /**
     * bestimmt, von welchem Graphen aus H berechnet werden soll
     */
    protected int a;

    /**
     * Isomorphismus von G<sub>0</sub> zu G<sub>1</sub>
     */
    protected Isomorphismus f;

    /**
     * Isomorphismus von G<sub>a</sub> zu H
     */
    protected Isomorphismus g;

    /**
     * Konstruktor, der den Namen setzt
     * 
     * @param name Name des Beweisers
     */
    public GBeweiser(String name) {
        super(name);
        a = -1;
    }

    /**
     * Methode zum Generieren von a, g und H
     */
    public abstract void generateA();

    /**
     * Methode zum Generieren des Geheimnisses
     * 
     * @param knoten Anzahl der Knoten, die die Graphen haben sollen
     */
    public abstract void generateSecret(int knoten);

    /**
     * Methode zum Berechnen von h
     * 
     * @param b h wird in Abhängigkeit von b und a berechnet
     */
    public abstract void genH(int b);

    /**
     * Methode zum Erhalten des Wertes von a
     * 
     * @return Wert von a
     */
    public int getA() {
        return a;
    }

    /**
     * Methode zum Erhalten von f
     * 
     * @return den Isomorphismus f
     */
    public Isomorphismus getF() {
        return f;
    }

    /**
     * Methode zum Erhalten von g
     * 
     * @return den Isomorphismus g
     */
    public Isomorphismus getG() {
        return g;
    }

    /**
     * Methode zum Zurücksetzen von a, f, g und allem, was durch Person zurückgesetzt wird
     */
    public void reset() {
        a = -1;
        f = null;
        g = null;
        super.reset();
    }

    /**
     * Methode zum Zurücksetzen von a, g und allem, was durch Person zurückgesetzt wird
     */
    public void resetNotSecret() {
        a = -1;
        g = null;
        super.resetNotSecret();
        notifyChanged();
    }
}
