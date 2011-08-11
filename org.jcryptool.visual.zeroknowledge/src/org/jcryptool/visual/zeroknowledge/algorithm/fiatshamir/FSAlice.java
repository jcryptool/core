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

import java.math.BigInteger;

/**
 * Klasse für eine Person, die das Geheimnis kennt.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FSAlice extends FSPerson {

    private BigInteger p;

    private BigInteger q;

    /**
     * erstellt ein Object mit dem Namen "Alice"
     */
    public FSAlice() {
        super("Alice"); //$NON-NLS-1$
        secret = BigInteger.ZERO;
    }

    /**
     * Berechnet die Antwort in Abhängigkeit von b: y = r * s<sub>b</sub> mod n.
     *
     * @throws IllegalArgumentException wird geworfen, wenn b einen Wert außer 0 und 1 annimmt
     * @see FSPerson#calculateY(int)
     */
    public void calculateY(int b) {
        if (b == 0) {
            y = r;
        } else if (b == 1) {
            y = r.multiply(secret).mod(n);
        } else
            throw new IllegalArgumentException(Messages.FS_Alice_0);
        setChanged();
        notifyObservers();
    }

    /**
     * Methode zum Generieren der Zufallszahl.
     *
     * @see FSPerson#generateR()
     */
    public void generateR() {
        if (n.equals(BigInteger.ZERO)) {
            return;
        }
        do {
            r = new BigInteger(n.bitCount() + 5, rand);
            r.abs();
            r.mod(n);
        } while (r.equals(BigInteger.ZERO) || r.equals(secret));
        x = r.pow(2).mod(n);
        setChanged();
        notifyObservers();
    }

    /**
     * generiert das Geheimnis, das teilerfremd zum Modul ist
     *
     * @see FSPerson#generateSecret()
     */
    public void generateSecret() {
        if (n.equals(BigInteger.ZERO)) {
            return;
        }
        do {
            secret = new BigInteger(n.bitCount() + 5, rand).abs().mod(n);
        } while (secret.equals(BigInteger.ZERO) || secret.equals(BigInteger.ONE)
                || secret.mod(p).equals(BigInteger.ZERO) || secret.mod(q).equals(BigInteger.ZERO));
        v = secret.pow(2).mod(n);
        setChanged();
        notifyObservers();
    }

    /**
     * setzt p, q und den Modul n = p * q
     *
     * @param p erste Primzahl
     * @param q erste Primzahl
     * @see FSPerson#setN(BigInteger, BigInteger)
     */
    public void setN(BigInteger p, BigInteger q) {
        super.setN(p, q);
        this.p = p;
        this.q = q;
    }
}
