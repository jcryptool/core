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
import java.util.Observable;
import java.util.Random;

/**
 * Klasse zum Speichern der Parameter, die Bob kennt
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FFSBob extends Observable {

    private int[] b;

    private BigInteger n = BigInteger.ZERO;

    private Random rand = new Random();

    private BigInteger receivedX = BigInteger.ZERO;

    private BigInteger toVerify = BigInteger.ZERO;

    private BigInteger[] v;

    /**
     * Konstruktor für ein Bob Objekt. Hier werden v und b initialisiert
     *
     * @param anzahl Anzahl der Einträge in den Vektoren
     */
    public FFSBob(int anzahl) {
        v = new BigInteger[anzahl];
        b = new int[anzahl];
        for (int i = 0; i < v.length; i++) {
            v[i] = BigInteger.ZERO;
            b[i] = -1;
        }
    }

    /**
     * generiert ein Bit b aus der Menge {0,1}
     */
    public void generateB() {
        for (int i = 0; i < b.length; i++)
            b[i] = Math.abs(rand.nextInt()) % 2;
        setChanged();
        notifyObservers();
    }

    /**
     * gibt b zurück
     *
     * @return b
     */
    public int[] getB() {
        return b;
    }

    /**
     * gibt den Modul n zurück
     *
     * @return Modul n
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * gibt das empfangene X zurück
     *
     * @return das empfangene X
     */
    public BigInteger getReceivedX() {
        return receivedX;
    }

    /**
     * gibt den Wert zurück, der verifiziert werden soll (y-Wert)
     *
     * @return empfangener y-Wert
     */
    public BigInteger getToVerify() {
        return toVerify;
    }

    /**
     * gibt den v-Wert zurück
     *
     * @return v-Wert
     */
    public BigInteger[] getV() {
        return v;
    }

    /**
     * Methode zum Empfangen von x.
     *
     * @param x das Quadrat der Zufallszahl
     */
    public void receiveX(BigInteger x) {
        this.receivedX = x;
    }

    /**
     * setzt alle Attribute von Bob zurück
     */
    public void reset() {
        setN(BigInteger.ZERO, BigInteger.ZERO);
    }

    /**
     * setzt b auf den Default-Wert
     */
    public void resetNotSecret() {
        for (int i = 0; i < b.length; i++)
            b[i] = -1;
        receivedX = toVerify = BigInteger.ZERO;
        setChanged();
        notifyObservers();
    }

    /**
     * setzt n = p * q
     *
     * @param p erste Primzahl
     * @param q zweite Primzahl
     */
    public void setN(BigInteger p, BigInteger q) {
        n = p.multiply(q);
        for (int i = 0; i < b.length; i++) {
            b[i] = -1;
            v[i] = BigInteger.ZERO;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * setzt die Zahl, die verifiziert werden soll
     *
     * @param y Zahl, die verifiziert werden soll
     */
    public void setToVerify(BigInteger y) {
        toVerify = y;
    }

    /**
     * setzt v neu
     *
     * @param v Wert für v
     */
    public void setV(BigInteger[] v) {
        this.v = v;
    }

    /**
     * überprüft, ob die empfangenen Zahlen zusammen passen
     *
     * @return true, wenn die empfangenen Zahlen zusammen passen
     */
    public boolean verify() {
        BigInteger tmp = receivedX;
        for (int i = 0; i < b.length; i++) {
            if (b[i] == 1) {
                tmp = tmp.multiply(v[i]);
            }
        }
        return tmp.mod(n).equals(toVerify.pow(2).mod(n));
    }
}