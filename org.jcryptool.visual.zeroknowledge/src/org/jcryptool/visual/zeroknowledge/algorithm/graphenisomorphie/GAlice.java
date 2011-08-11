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
 * Klasse für eine Person, die das Geheimnis kennt.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class GAlice extends GBeweiser {

    /**
     * Erstellt ein Objekt mit dem Namen "Alice"
     */
    public GAlice(int knoten) {
        super("Alice"); //$NON-NLS-1$
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
        } else if (a == 1) {
            graphH = g1.change(g);
        }
        notifyChanged();
    }

    /**
     * Methode zum Generieren des Geheimnisses
     * 
     * @see GBeweiser#generateSecret(int)
     */
    public void generateSecret(int knoten) {
        int kanten = 9 + rand.nextInt(3);
        g0 = new Graph(knoten, kanten);
        f = new Isomorphismus(g0.getKnoten().size());
        g1 = g0.change(f);
        notifyChanged();
    }

    /**
     * Methode zum Berechnen von h
     * 
     * @param b h wird in Abhängigkeit von b und a berechnet: h = g * f<sub>a - b</sub>
     * @see GBeweiser#genH(int)
     */
    public void genH(int b) {
        if (a == b)
            h = g;
        else if (a == 1 && b == 0) {
            h = g.concat(f);
        } else {
            h = g.concat(f.inverse());
        }
        notifyChanged();
    }
}
