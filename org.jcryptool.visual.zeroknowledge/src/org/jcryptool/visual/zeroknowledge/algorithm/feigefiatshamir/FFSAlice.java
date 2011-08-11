// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir;

import java.math.BigInteger;

/**
 * Klasse für eine Person, die das Geheimnis kennt.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FFSAlice extends FFSPerson {

    private BigInteger p;

    private BigInteger q;

    /**
     * erstellt ein Object mit dem Namen "Alice"
     * 
     * @param anzahl Anzahl der Einträge in den Vektoren
     */
    public FFSAlice(int anzahl) {
        super("Alice", anzahl); //$NON-NLS-1$
    }

    /**
     * berechnet die Antwort in Abhängigkeit von b. Im Fall b = 0 lautet die Antwort y = r und im
     * Fall b = 1 lautet die Antwort y = (r * secret) mod n.
     * 
     * @throws IllegalArgumentException wird geworfen, wenn b einen Wert außer 0 und 1 annimmt
     * @see FFSPerson#calculateY(int[])
     */
    public void calculateY(int[] b) {
        y = r;
        for (int i = 0; i < b.length; i++) {
            if (b[i] == 1) {
                y = y.multiply(secret[i]);
            }
        }
        y = y.mod(n);
        setChanged();
        notifyObservers();
    }

    /**
     * generiert das Geheimnis, das teilerfremd zum Modul ist
     * 
     * @see FFSPerson#generateSecret()
     */
    public void generateSecret() {
        if (n.equals(BigInteger.ZERO)) {
            return;
        }
        for (int i = 0; i < secret.length; i++) {
            do {
                secret[i] = new BigInteger(n.bitCount(), rand).abs().mod(n);
            } while (secret[i].equals(BigInteger.ZERO) || secret[i].equals(BigInteger.ONE)
                    || secret[i].mod(p).equals(BigInteger.ZERO)
                    || secret[i].mod(q).equals(BigInteger.ZERO));
            v[i] = secret[i].pow(2).mod(n);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * setzt p, q und den Modul n = p * q
     * 
     * @param p erste Primzahl
     * @param q erste Primzahl
     * @see FFSPerson#setN(BigInteger, BigInteger)
     */
    public void setN(BigInteger p, BigInteger q) {
        super.setN(p, q);
        this.p = p;
        this.q = q;
    }
}
