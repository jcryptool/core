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
 * Klasse für eine Person, die das Geheimnis nicht kennt.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FFSCarol extends FFSPerson {

    private int[] c;

    /**
     * erstellt ein Object mit dem Namen "Carol"
     * 
     * @param anzahl Anzahl der Einträge in den Vektoren
     */
    public FFSCarol(int anzahl) {
        super("Carol", anzahl); //$NON-NLS-1$
        c = new int[anzahl];
        for (int i = 0; i < anzahl; i++) {
            c[i] = -1;
        }
    }

    /**
     * berechnet y in Abhängigkeit von b. Es gilt hierbei immer y = r.
     * 
     * @see FFSPerson#calculateY(int[])
     */
    public void calculateY(int[] b) {
        y = r;
        setChanged();
        notifyObservers();
    }

    /**
     * generiert r und setzt x
     * 
     * @see FFSPerson#generateR()
     */
    public void generateR() {
        super.generateR();
        for (int i = 0; i < c.length; i++) {
            c[i] = rand.nextInt(2);
            if (c[i] == 1) {
                x = (x.multiply(v[i].modInverse(n))).mod(n);
            }
        }
        setChanged();
        notifyObservers();
    }

    /**
     * generiert das Geheimnis. In diesem Fall gilt, das das Geheimnis -1 ist, da das Geheimnis
     * nicht bekannt ist.
     * 
     * @see FFSPerson#generateSecret()
     */
    public void generateSecret() {
        BigInteger tmp;
        secret = new BigInteger[anzahl];
        v = new BigInteger[anzahl];
        for (int i = 0; i < anzahl; i++) {
            secret[i] = new BigInteger("-1"); //$NON-NLS-1$
            do {
                tmp = new BigInteger(n.bitCount() * n.bitLength(), rand).mod(n);
            } while (!tmp.gcd(n).equals(BigInteger.ONE) && tmp.equals(BigInteger.ONE)
                    && tmp.equals(BigInteger.ZERO));
            v[i] = tmp.pow(2).mod(n);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Methode, um den Wert von c zu erhalten
     * 
     * @return Wert von c
     */
    public int[] getC() {
        return c;
    }

    /**
     * setzt alle Attribute von Carol zurück
     * 
     * @see FFSPerson#reset()
     */
    public void reset() {
        c = new int[anzahl];
        for (int i = 0; i < anzahl; i++)
            c[i] = -1;
        super.reset();
    }

    /**
     * setzt c, r und y zurück overrides {@link FFSPerson#resetNotSecret()}
     * 
     * @see FFSPerson#resetNotSecret()
     */
    public void resetNotSecret() {
        c = new int[anzahl];
        for (int i = 0; i < anzahl; i++)
            c[i] = -1;
        super.resetNotSecret();
    }
}