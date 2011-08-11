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

import org.jcryptool.visual.zeroknowledge.algorithm.Funcs;

/**
 * Klasse, die die Funktionalität bereit stellt, das Graphenisomorphie- Protokoll mehrmals am Stück
 * ohne Parameterüberwachung durchzuführen.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class GFuncs implements Funcs {

    private boolean secretKnown = true;

    /**
     * Führt value-mal das Graphenisomorphie-Protokoll durch. Liefert das Ergebnis zurück
     * 
     * @param value Anzahl, wie oft das Protokoll ausgeführt werden soll
     * @return Anzahl, wie oft erfolgreich verifiziert wurde
     * @see Funcs#protokoll(int)
     */
    public int protokoll(int value) {
        GAlice alice = new GAlice(6);
        GCarol carol = new GCarol(6);
        GBob bob = new GBob();
        GBeweiser b;
        if (secretKnown) // Person setzen
            b = alice;
        else
            b = carol;
        bob.setG0(b.getG0());
        bob.setG1(b.getG1());
        int correct = 0; // Zaehler, wie oft erfolgreich verifiziert wurde
        for (int i = 1; i <= value; i++) {
            b.generateA();
            bob.setH(b.getGraphH());
            bob.generateB();
            b.genH(bob.getB());
            bob.setIsomorphismus(b.getH());
            if (bob.verify())
                correct++;
        }
        return correct; // Wert zurückgeben
    }

    /**
     * setzt, ob das Geheimnis bekannt ist
     * 
     * @param b true, wenn das Geheimnis bekannt ist, sonst false
     * @see Funcs#setSecretKnown(boolean)
     */
    public void setSecretKnown(boolean b) {
        secretKnown = b;
    }
}
