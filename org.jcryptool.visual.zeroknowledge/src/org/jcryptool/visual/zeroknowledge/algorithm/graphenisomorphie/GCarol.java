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
 * Klasse, die einen Beweiser repräsentiert, der das Geheimnis nicht kennt.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class GCarol extends GBeweiser {

    /**
     * Konstruktor, der ein Carol-Objekt mit Namen Carol erstellt
     */
    public GCarol(int knoten) {
        super("Carol"); //$NON-NLS-1$
        generateSecret(knoten);
    }

    /**
     * Methode zum Generieren von a, g und H
     * 
     * @see GBeweiser#generateA()
     */
    public void generateA() {
        a = rand.nextInt(2);
        g = new Isomorphismus(g0.getKnoten().size());
        if (a == 0) {
            graphH = g0.change(g);
        } else {
            graphH = g1.change(g);
        }
        notifyChanged();
    }

    /**
     * Methode zum Berechnen von h
     * 
     * @param b h wird in Abhängigkeit von b und a berechnet
     * @see GBeweiser#genH(int)
     */
    public void genH(int b) {
        h = g;
        notifyChanged();
    }

    /**
     * Methode zum Erhalten von f
     * 
     * @return graphenisomorphie.math.Isomorphismus.NULL
     * @see GBeweiser#getF()
     */
    public Isomorphismus getF() {
        return Isomorphismus.NULL;
    }

    /**
     * Methode zum Generieren des Geheimnisses
     * 
     * @param knoten Anzahl der Knoten, die die Graphen haben sollen
     * @see GBeweiser#generateSecret(int)
     */
    public void generateSecret(int knoten) {
        int kanten = 9 + rand.nextInt(3);
        g0 = new Graph(knoten, kanten);
        f = Isomorphismus.NULL;
        g1 = g0.change(new Isomorphismus(g0.getKnoten().size()));
        notifyChanged();
    }
}