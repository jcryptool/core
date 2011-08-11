// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.magischetuer;

import org.jcryptool.visual.zeroknowledge.algorithm.Funcs;

/**
 * Klasse, die die Funktionalität bereit stellt, das Magische-Tuer-Protokoll mehrmals am Stück ohne
 * Parameterüberwachung durchzuführen.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class MFuncs implements Funcs {

    private MAlice alice;

    private MBob bob;

    private MPerson carol;

    private MDoor door;

    private boolean secretKnown;

    /**
     * Konstruktor, der die Tür mit dem Code erhält
     * 
     * @param m Tür, deren Code Alice kennen soll
     */
    public MFuncs(MDoor m) {
        door = m;
        secretKnown = true;
        alice = new MAlice();
        carol = new MPerson();
        bob = new MBob();
        alice.setCode(m.getCode());
    }

    /**
     * Führt value-mal das Protokoll der Magischen Tür durch. Liefert das Ergebnis zurück
     * 
     * @param value Anzahl, wie oft das Protokoll ausgeführt werden soll
     * @return Anzahl, wie oft erfolgreich verifiziert wurde
     * @see Funcs#protokoll(int)
     */
    public int protokoll(int value) {
        MPerson p;
        if (secretKnown) // Person setzen
            p = alice;
        else
            p = carol;
        int correct = 0; // Zaehler, wie oft erfolgreich verifiziert wurde
        for (int i = 1; i <= value; i++) {
            p.chooseRaum();
            bob.chooseRaum();
            if (p.getRaum() != bob.getRaumwahl()) {
                p.changeRaum(door);
            }
            if (p.getRaum() == bob.getRaumwahl()) {
                correct++;
            }
        }
        return correct; // Wert zurückgeben
    }

    /**
     * Setzt, ob das Geheimnis bekannt ist oder nicht
     * 
     * @param isAlice true, wenn das Geheimnis als bekannt eingestuft werden soll
     * @see Funcs#setSecretKnown(boolean)
     */
    public void setSecretKnown(boolean isAlice) {
        this.secretKnown = isAlice;
    }
}
