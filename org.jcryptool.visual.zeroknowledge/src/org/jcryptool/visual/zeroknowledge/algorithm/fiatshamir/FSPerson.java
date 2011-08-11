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
import java.util.Observable;
import java.util.Random;

/**
 * Superklasse für Alice und Carol, die ihre Identität beweisen sollen. Sie haben eine Zahl v
 * bekannt gegeben. Sie sollen beweisen, dass sie eine Quadratwurzel s von v mod n kennen. Diese
 * Klasse speichert den Modul n, das Geheimnis, das Quadrat des Geheimnisses, die generierte
 * Zufallszahl und die Antwort auf die Eingabe von b.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public abstract class FSPerson extends Observable {

    private String name;

    /**
     * Modul
     */
    protected BigInteger n = BigInteger.ZERO;

    /**
     * Zufallszahl
     */
    protected BigInteger r = BigInteger.ZERO;

    /**
     * Random-Objekt zur Generierung von Zufallszahlen
     */
    protected Random rand = new Random();

    /**
     * Geheimnis
     */
    protected BigInteger secret;

    /**
     * Geheimnis
     */
    protected BigInteger v = BigInteger.ZERO;

    /**
     * Zufallszahl
     */
    protected BigInteger x = BigInteger.ZERO;

    /**
     * Antwort
     */
    protected BigInteger y = BigInteger.ZERO;

    /**
     * Konstruktor, der den Namen setzt
     *
     * @param name Name der Person
     */
    public FSPerson(String name) {
        this.name = name;
    }

    /**
     * Methode zum Berechnen der Antwort
     *
     * @param b gibt das Bit an, das Bob geschickt hat
     */
    public abstract void calculateY(int b);

    /**
     * Methode zum Generieren der Zufallszahl.
     */
    public void generateR() {
        if (n.equals(BigInteger.ZERO)) {
            return;
        }
        do {
            r = new BigInteger(n.bitCount() + 5, rand);
            r.abs();
            r.mod(n);
        } while (r.equals(BigInteger.ZERO));
        x = r.pow(2).mod(n);
        setChanged();
        notifyObservers();
    }

    /**
     * Methode zum Erzeugen des Geheimnisses von Alice
     */
    public abstract void generateSecret();

    /**
     * gibt das Modul zurück
     *
     * @return Modul
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * gibt den Namen zurück
     *
     * @return Name der Person
     */
    public String getName() {
        return name;
    }

    /**
     * gibt die Zufallszahl zurück
     *
     * @return die generierte Zufallszahl
     */
    public BigInteger getR() {
        return r;
    }

    /**
     * gibt das Geheimnis zurück
     *
     * @return das Geheimnis
     */
    public BigInteger getSecret() {
        return secret;
    }

    /**
     * gibt die Quadrat des Geheimnisses zurück
     *
     * @return Quadrat des Geheimnisses
     */
    public BigInteger getV() {
        return v;
    }

    /**
     * berechnet r^2 mod n und liefert das Ergebnis
     *
     * @return r^2 mod n
     */
    public BigInteger getX() {
        return x;
    }

    /**
     * gibt die berechnete Antwort zurück
     *
     * @return die berechnete Antwort
     */
    public BigInteger getY() {
        return y;
    }

    /**
     * setzt alle Daten der Person zurück
     */
    public void reset() {
        n = BigInteger.ZERO;
        secret = BigInteger.ZERO;
        x = BigInteger.ZERO;
        v = BigInteger.ZERO;
        r = BigInteger.ZERO;
        y = BigInteger.ZERO;
        setChanged();
        notifyObservers();
    }

    /**
     * setzt r und y zurück
     */
    public void resetNotSecret() {
        r = BigInteger.ZERO;
        y = BigInteger.ZERO;
        x = BigInteger.ZERO;
        setChanged();
        notifyObservers();
    }

    /**
     * setzt n = p * q.
     *
     * @param p erste Primzahl
     * @param q zweite Primzahl
     */
    public void setN(BigInteger p, BigInteger q) {
        reset();
        n = p.multiply(q);
        setChanged();
        notifyObservers();
    }
}
