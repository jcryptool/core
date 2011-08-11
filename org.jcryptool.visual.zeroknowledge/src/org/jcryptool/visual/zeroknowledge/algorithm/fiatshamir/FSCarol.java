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
 * Klasse für eine Person, die das Geheimnis nicht kennt.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FSCarol extends FSPerson {

    private int c = -1;

    private final BigInteger minus1 = new BigInteger("-1"); //$NON-NLS-1$

    /**
     * erstellt ein Object mit dem Namen "Carol"
     */
    public FSCarol() {
        super("Carol"); //$NON-NLS-1$
        secret = minus1;
    }

    /**
     * berechnet y in Abhängigkeit von b. Es gilt hierbei immer y = r.
     *
     * @throws IllegalArgumentException wird geworfen, wenn b nich 0 oder 1 ist
     * @see FSPerson#calculateY(int)
     */
    public void calculateY(int b) {
        if (b == 0 || b == 1) {
            y = r;
        } else {
            throw new IllegalArgumentException(Messages.FS_Carol_0);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * generiert r und setzt x
     *
     * @see FSPerson#generateR()
     */
    public void generateR() {
        super.generateR();
        c = Math.abs(rand.nextInt()) % 2;
        if (c == 1)
            x = x.multiply(v.modInverse(n)).mod(n);
        setChanged();
        notifyObservers();
    }

    /**
     * generiert das Geheimnis. In diesem Fall gilt, das das Geheimnis -1 ist, da das Geheimnis
     * nicht bekannt ist.
     *
     * @see FSPerson#generateSecret()
     */
    public void generateSecret() {
        do {
            secret = new BigInteger(n.bitCount() * n.bitLength() + 3, rand).mod(n);
        } while (!secret.gcd(n).equals(BigInteger.ONE) && secret.equals(BigInteger.ZERO)
                && secret.equals(BigInteger.ONE));
        this.v = secret.pow(2).mod(n);
        secret = minus1;
        setChanged();
        notifyObservers();
    }

    /**
     * Methode, um den Wert von c zu erhalten
     *
     * @return Wert von c
     */
    public int getC() {
        return c;
    }

    /**
     * setzt alle Attribute von Carol zurück
     *
     * @see FSPerson#reset()
     */
    public void reset() {
        c = -1;
        super.reset();
    }

    /**
     * setzt c, r und x zurück
     *
     * @see FSPerson#resetNotSecret()
     */
    public void resetNotSecret() {
        c = -1;
        super.resetNotSecret();
    }
}