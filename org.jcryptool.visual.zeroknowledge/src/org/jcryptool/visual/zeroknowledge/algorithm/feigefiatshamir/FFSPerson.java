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
 * Superklasse für Alice und Carol, die ihre Identität beweisen sollen. Sie haben einen Vektor v.
 * Sie sollen beweisen, dass sie einen Vektor s kennen mit s<sub>i</sub><sup>2</sup> = v<sub>i</sub>
 * mod n kennen. Diese Klasse speichert den Modul n, das Geheimnis, das Quadrat des Geheimnisses,
 * die generierte Zufallszahl und die Antwort auf die Eingabe von b.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public abstract class FFSPerson extends Observable {

    private String name;

    /**
     * Anzahl der Einträge im Vektor
     */
    protected int anzahl;

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
    protected BigInteger[] secret;

    /**
     * Geheimnis
     */
    protected BigInteger[] v;

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
     * @param anzahl Anzahl der Einträge in den Vektoren
     */
    public FFSPerson(String name, int anzahl) {
        this.name = name;
        this.anzahl = anzahl;
        secret = new BigInteger[anzahl];
        v = new BigInteger[anzahl];
        for (int i = 0; i < anzahl; i++) {
            secret[i] = BigInteger.ZERO;
            v[i] = BigInteger.ZERO;
        }
    }

    /**
     * Methode zum Berechnen der Antwort
     *
     * @param b gibt das Bit an, das Bob geschickt hat
     */
    public abstract void calculateY(int[] b);

    /**
     * Methode zum Generieren der Zufallszahl. Gleichzeitig wird auch x = r^2 mod n.
     */
    public void generateR() {
        // wenn der Modul noch nicht gesetzt ist: Abbruch
        if (n.equals(BigInteger.ZERO)) {
            return;
        }

        // Erstellen einer Zufallszahl
        do {
            r = new BigInteger(n.bitCount(), rand).abs().mod(n);
        } while (r.equals(BigInteger.ZERO) || r.equals(BigInteger.ONE));

        // Berechnen von x
        x = r.pow(2).mod(n);

        // Änderungen den Observer mitteilen
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
    public BigInteger[] getSecret() {
        return secret;
    }

    /**
     * gibt die Quadrat des Geheimnisses zurück
     *
     * @return Quadrat des Geheimnisses
     */
    public BigInteger[] getV() {
        return v;
    }

    /**
     * berechnet r<sup>2</sup> mod n und liefert das Ergebnis
     *
     * @return r<sup>2</sup> mod n
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
     * setzt alle Attribute der Person zurück
     */
    public void reset() {
        n = BigInteger.ZERO;
        x = BigInteger.ZERO;
        secret = new BigInteger[anzahl];
        v = new BigInteger[anzahl];
        for (int i = 0; i < anzahl; i++) {
            v[i] = secret[i] = BigInteger.ZERO;
        }
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
