// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir;

import org.jcryptool.visual.zeroknowledge.algorithm.Funcs;
import org.jcryptool.visual.zeroknowledge.algorithm.Modell;

/**
 * Klasse, die die Funktionalität bereit stellt, das Fiat-Shamir-Protokoll mehrmals am Stück ohne
 * Parameterüberwachung durchzuführen.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FSFuncs implements Funcs {

    private FSAlice alice;

    private FSBob bob;

    private FSCarol carol;

    private boolean secretKnown;

    /**
     * Konstruktor, der ein Alice-Objekt und ein Modell erhält
     * 
     * @param m Modell-Objekt mit den Werten der beiden Primzahlen p und q
     */
    public FSFuncs(Modell m) {
        secretKnown = true;
        alice = new FSAlice();
        alice.setN(m.getP(), m.getQ());
        alice.generateSecret();

        carol = new FSCarol();
        carol.setN(m.getP(), m.getQ());
        carol.generateSecret();

        bob = new FSBob();
        bob.setN(m.getP(), m.getQ());
    }

    /**
     * Fuehrt value-mal das Fiat-Shamir-Protokoll durch. Liefert als Ergebnis die Anzahl der
     * geglückten Antworten zurück
     * 
     * @param value Anzahl, wie oft das Protokoll ausgeführt werden soll
     * @return Anzahl, wie oft erfolgreich verifiziert wurde
     * @see Funcs#protokoll(int)
     */
    public int protokoll(int value) {
        FSPerson p;
        if (secretKnown) // Person setzen
            p = alice;
        else
            p = carol;
        int correct = 0; // Zaehler, wie oft erfolgreich verifiziert wurde
        bob.setV(p.getV()); // das Geheimnis bei Bob setzen
        for (int i = 1; i <= value; i++) {
            p.generateR(); // Zufallszahl r erstellen
            bob.receiveX(p.getX()); // r an Bob schicken
            bob.generateB(); // Bob generiert das Zufallsbit
            p.calculateY(bob.getB()); // Person empfaengt b und berechnet y
            bob.setToVerify(p.getY()); // Bob erhaelt y
            if (bob.verify()) { // wenn Bob erfolgreich verifizieren konnte
                correct++; // Zaehler eins hochzaehlen
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
