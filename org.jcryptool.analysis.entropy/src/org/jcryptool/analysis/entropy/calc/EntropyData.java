// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.entropy.calc;

import java.util.Enumeration;
import java.util.Hashtable;

import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;

/**
 * @author Matthias Mensch
 */
public class EntropyData {

    /**
     * enthaelt den uebergebenen Quelltext als String
     */
    private String sourceText;

    private String filteredText;

    private String alphabet;

    /**
     * Anzahl der unterschiedlichen Zeichen im Alphabet:
     */
    private int lengthAlphabet;

    /**
     * enthaelt die absolute Haeufigkeit aller Zeichen des gefilterten Text Schluessel =
     * Zeichentupel (String), Wert = Anzahl des Zeichen (int)
     */
    private Hashtable<String, Integer> absFreq;

    /**
     * enthaelt die relative Haeufigkeit aller Zeichen des normalisierten Text Schluessel =
     * Zeichentupel (String), Wert = relative Haeufigkeit (double)
     */
    private Hashtable<String, Double> relFreq;

    /**
     * Die zum EntropyData gehoerige Instanz der Klasse EntropyTupelFreq. Genaue Dokumentation siehe
     * EntropyTupelFreq.java
     */
    private EntropyTupelFreq letterFreqTable;

    public EntropyData(String text, TransformData modifySettings) {
        sourceText = text;
        filteredText = Transform.transformText(text, modifySettings);
        alphabet = reduceToDifferentChars(filteredText);
        lengthAlphabet = alphabet.length();
        relFreq = new Hashtable<String, Double>();
        absFreq = new Hashtable<String, Integer>();
    }

    /**
     * Get-Methode, liefert den urspruenglichen Text.
     *
     * @return sourceText
     */
    public String getSourceText() {
        return sourceText;
    }

    /**
     * Get-Methode, liefert den gefilterten Text.
     *
     * @return filteredText
     */
    public String getFilteredText() {
        return filteredText;
    }

    /**
     * Get-Methode, liefert eine Hashtable mit den absoluten Haeufigkeiten von n-tupeln
     *
     * @return Hashtable absFreq
     */
    public Hashtable<String, Integer> getAbsoluteFrequency() {
        return absFreq;
    }

    /**
     * get-Methode, liefert eine Hashtable mit den relativen Haeufigkeiten von n-Tupeln.
     *
     * @return Hashtable relFreq
     */
    public Hashtable<String, Double> getRelativeFrequency() {
        return relFreq;
    }

    /**
     * Get-Methode, liefert eine Instanz der Klasse EntropyTupelFreq
     *
     * @return EntropyTupelFreq
     */
    public EntropyTupelFreq getFreqTable() {
        return letterFreqTable;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public int getLengthAlphabet() {
        return lengthAlphabet;
    }

    public int getLengthFilteredText() {
        return filteredText.length();
    }

    /**
     * berechnet die bedingte und absolute Entropie und nutzt dabei 'filteredText' als Nachricht.
     * Daraus resultierende Informationen werden berechnet und in einem double-Arraysiehe
     * gespeichert, siehe return Parameter.
     *
     * @param n zu beruecksichtigenden Tupellaengen der bedingten Entropie
     * @return double[] Ergebnis [0]:F_n, [1]:G_n, [2]:Anzahl unterschiedlicher n-tupel, [3]:Anzahl
     *         moeglicher n-tupel, [4]:maximale Entropie, [5]:relative Entropie bezueglich F_n,
     *         [6]:absolute Entropie geteilt durch n, [7]:Redundanz bezgl. F_n, [8]:Redundanz bezgl.
     *         G_n, [9]:Zuwachsfaktor von G_n-1 auf G_n
     */
    public double[] calcEntropy(int n) {

        // Hilfsvariable deklarieren:

        String ntupel; // das aktuell zu bearbeitende n-Tupel wird hier zwischengespeichert.

        // hier wird die Auftrittswahrscheinlichkeit des gefundenen n-Tupel gespeichert:
        double rf; // = relative frequency

        // bedingte Wahrscheinlichkeit des n-ten Buchstabens eines n-tupels
        // wird hier zwischengespeichert werden:
        double crf; // = conditional relative frequency

        // Zaehler zum ermitteln Anzahl unterschiedlcher vorkommender n-tupel:
        double numDifTupels = 0.0; // = number of different tupels

        // Rueckgabearray deklarieren und mit 0 initalisieren:
        double[] result = new double[EntropyCalc.MATRIXCOLUMS];
        for (int j = 0; j < result.length; j++) {
            result[j] = 0.0;
        }

        // Hashtable relFreq und absFreq "leeren" (alle evtl. vorhandenen Werte loeschen).
        // Zweck: bei inkrementeller Rechnung wird die eventuell sehr hohe Anzahl an
        // n-tupeln, die nicht mehr benoetigt werden, geloescht.
        relFreq.clear();
        absFreq.clear();

        // Bestimme absolute Haeufigkeit aller n-Tupel und (n-1)-Tupel sowie deren
        // Auftrittswahrscheinlichkeit:
        if (n > 1) {
            countAbsFreq(n - 1);
        }
        countAbsFreq(n);
        if (n > 1) {
            countRelFreq(n - 1, this.filteredText.length());
        }
        countRelFreq(n, this.filteredText.length());

        // berechne Entropiewerte:
        Enumeration<String> e = relFreq.keys();
        while (e.hasMoreElements()) {
            // waehle das naechste Element:
            ntupel = e.nextElement().toString();
            // beachte nur Tupel der Laenge n:
            if (ntupel.length() == n) {
                rf = Double.parseDouble(relFreq.get(ntupel).toString());
                // crf = bedingte Wahrscheinlichkeit des n-ten Buchstabens eines n-tupels
                crf = calcCondProb(ntupel);
                // absolute und bedingte Wahrscheinlichkeiten ausgeben:

                // bedingte Entropie unter Beachtung von n-Tupel berechnen:
                result[0] += (rf * EntropyData.log2(crf));
                // absolute Entropie ueber n-tupel berechnen:
                result[1] += (rf * EntropyData.log2(rf));

                numDifTupels += (double) 1;
            }
        }

        if (n == 1) {
            letterFreqTable = new EntropyTupelFreq(this.lengthAlphabet, 1, relFreq);
        }


        // Anzahl unterschiedlicher Tupel der Laenge n speichern:
        result[2] = numDifTupels;
        // Anzahl moeglicher Tupel der Laenge n berechnen und speichern:
        result[3] = Math.pow((double) this.lengthAlphabet, (double) n);
        // berechne max Entropie per Definition:
        result[4] = EntropyData.log2((double) this.lengthAlphabet);

        // Betrag der bedingten, absoluten u max Entropiewert setzen: (Grund: siehe Studienarbeit)
        result[0] = Math.abs(result[0]);
        result[4] = Math.abs(result[4]);

        result[1] = Math.abs(result[1]);

        // relative Entropie bezueglich F_n, sowie G_n/n berechnen:
        result[5] = result[0] / result[4]; // rel-F_n
        result[6] = (result[1] / n); // (G_n)/n

        // Redundanzen berechnen:
        result[7] = 1.0 - result[5];
        result[8] = 1.0 - ((result[1] / n) / result[4]);

        // prozentuale Zunahme von G_(n-1) auf G_n berechnen:
        if (n == 1) {
            result[9] = 0.0;
        } else {
            result[9] = (result[1] - (result[1] - result[0])) / (result[1] - result[0]);
            // = ( G_n - G_(n-1) ) / G_(n-1)
        }

        return result;
    }

    private String reduceToDifferentChars(String text) {
        String t = text;
        for (int i = 0; i < t.length(); i++) {
            t = t.substring(0, i + 1).concat(removeChar(t.substring(i + 1), t.charAt(i)));
        }
        return t;
    }

    private static String removeChar(String text, char c) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != c)
                result.append(text.charAt(i));
        }
        return result.toString();
    }

    /**
     * gibt die relative Haeufigkeit des ntupels zurueck, sofern diese in der Hashtable relFreq
     * hinterlegt ist. Sonst wird -1.0 zurueck gegeben.
     *
     * @param ntupel String, dessen relative Haeufigkeit zurueck gegeben werden soll
     * @return double Wert, realtive haeufigkeit des angefragten n-Tupels.
     */
    public double getRelFrequency(String ntupel) {
        double result;
        if (relFreq.containsKey(ntupel)) {
            result = Double.parseDouble(relFreq.get(ntupel).toString());
        } else {
            result = -1.0;
        }
        return result;
    }

    /**
     * berechnet die absoluten Haeufigkeiten der gefundenen n-Tupeln. Diese werden in der Hashtable
     * absFreq gespeichert.
     *
     * @param n Laenge der zu betrachtenden n-Tupel
     */
    private void countAbsFreq(int n) {

        // Hilfsvariable zum Zwischenspeichern des aktuellen n-Tupels:
        String ntupel;
        // Schleife laeuft wie ein sliding-window ueber den Text
        for (int i = 0; i <= (filteredText.length() - n); i++) {
            ntupel = filteredText.substring(i, (i + n));
            insertInAbsFreq(ntupel);
        }

        // Hier muessen noch tupel die ueber die Grenze laufen gezaehlt werden
        if (n > 1) {
            for (int i = 0; i <= (n - 2); i++) {
                ntupel = filteredText.substring(filteredText.length() - (n - (i + 1)))
                        + filteredText.substring(0, (1 + i));
                insertInAbsFreq(ntupel);
            }
        }

    }

    /**
     * Hilfsmethode zum einfuegen eines Tupels in die Hashtable absFreq. Ist das Tupel bereits
     * vorhanden, wird der zugehoerige Zaehler inkrementiert, ansonsten wird das Tupel neu
     * eingefuegt.
     *
     * @param ntupel einzufuegendes bzw zu inkrementierendes Tupel
     */
    private void insertInAbsFreq(String ntupel) {
        // zaehlt die Anzahl der gefunden n-tupel:
        int counter = 0;
        // wenn das an der Stelle i beginnende n-Tupel noch nicht in der Hashtable ist ...
        if (!(absFreq.containsKey(ntupel))) {
            // ... dannn fuege es hinzu mit der Anzahl 1
            absFreq.put(ntupel, (int) 1);
        } else {
            // wenn schon in der Hashtable, dann inkrementiere den zugehoerigen Zaehler.
            counter = ((Integer) absFreq.get(ntupel)).intValue();
            counter++;
            absFreq.put(ntupel, counter);
        }
    }

    /**
     * berechnet die relativen Haeufigkeiten von n-Tupeln. Diese werden in Hashtable relFreq
     * gespeichert.
     *
     * @param n Laenge der zu betrachtenden n-Tupel
     */
    private void countRelFreq(int n, double length) {
        // Hilfsvariable zum Zwischenspeichern des aktuellen n-Tupels:
        String ntupel;

        // Hilfsvariable zum Zwischenspeichern der
        // relativen Haeufigkeit des aktuellen n-Tupels:
        double rf;

        Enumeration<String> e = absFreq.keys();
        // Schleife durchlaeuft die Hashtable der absoluten Haeufigkeiten:
        while (e.hasMoreElements()) {
            ntupel = e.nextElement().toString();
            // betrachte nur tupel der Laenge n
            if (ntupel.length() == n) {
                int anzahl = Integer.parseInt(absFreq.get(ntupel).toString());
                rf = ((double) anzahl) / (double) length;
                relFreq.put(ntupel, rf);
            }
        }

    }

    /**
     * berechnet die bedingte Wahrscheinlichkeit (conditional probability) des n-ten Zeichens eines
     * n-tupels, indem es dem vorhergehenden (n-1)-Tupels die Auftrittswahrscheinlichkeit des
     * Nachfolgebuchstabens zuordnet. Bei n=1 wird die Auftrittswahrscheinlichkeit dieses
     * Buchstabens zurueck gegeben.
     *
     * @param ntupel zu betrachtendes n-tupel, von dem die Auftrittswahrscheinlichkeit des n-ten
     *        Buchstabens berechnet werden soll.
     * @return Aufftrittswahrscheinlichkeit des n-ten Zeichens als double Wert.
     */
    private double calcCondProb(String ntupel) {
        // Falls das n-tupel die Laenge 1 hat wird als Wahrscheinlichkeit die
        // relative Haeufigkeit dieses Buchstabens zurueck gegeben:
        if (ntupel.length() == 1)
            return Double.parseDouble(relFreq.get(ntupel).toString());

        double res = 0.0;

        // Laenge des n-tupels > 1:

        // ktupel = die esrten (n-1) Zeichen des n-tupels:
        String ktupel = ntupel.substring(0, ntupel.length() - 1);

        // Auftrittswahrscheinlichkeit des n-tupels:
        double rfntupel = Double.parseDouble(relFreq.get(ntupel).toString());
        // Auftrittswahrscheinlichkeit des (n-1)-tupels:
        double rfktupel = Double.parseDouble(relFreq.get(ktupel).toString());

        // bedingte Wahrscheinlichkeit des n-ten Zeichens eines n-tupels
        // errechnet sich aus dem Quotient der Auftrittswahrscheinlichkeit
        // des n-tupels und der Auftrittswahrscheinlichkeit des (n-1)-tupels:
        res = (rfntupel / rfktupel);
        return res;
    }

    /**
     * gibt den Logarithmus vom Argument a zur Basis 2 zurueck
     *
     * @param a Argument der Logarithmusfunktion
     * @return double Wert, der Logarithmus von a zur Basis 2
     */
    public static double log2(double a) {
        return (Math.log(a) / Math.log((double) 2));
    }

    /**
     * rundet den Wert d auf l-viele Nachkommastellen
     *
     * @param d zu rundender double Wert,
     * @param l Anzahl der Nachkommastellen
     * @return double Wert, auf l Nachkomaastellen gerundeter Wert von d
     */
    public static double roundDouble(double d, int l) {
        double result = d * Math.pow(10.0, (double) l);
        result = Math.rint(result);
        result = result / Math.pow(10.0, (double) l);
        return result;
    }

    /**
     * liefert das Ergebnis von x hoch p als Integer-Wert.
     *
     * @param x Basis
     * @param p Exponent
     * @return Integer Wert von x hoch p.
     */
    public static int intPower(int x, int p) {
        int result = x;
        for (int i = 1; i < p; i++) {
            result *= x;
        }
        return result;
    }
}
