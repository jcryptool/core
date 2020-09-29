//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.numbershark.strategies;

/*
 * ============================================================================ Berechnung der optimalen Punktzahlen
 * beim Zahlenhai-Spiel ============================================================================ Copyright 2010 by
 * Volker van Nek, volkervannek at googlemail dot com This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License, http://www.gnu.org/copyleft/gpl.html. This software has NO
 * WARRANTY, not even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * ============================================================================ Der folgende Algorithmus ist geeignet,
 * die optimale Punktzahl beim Taxman-Game bzw. Zahlenhai mit g Zahlen zu bestimmen. Schritt 1: Solange es Zahlen
 * groesser g/2 gibt, die genau einen aktiven echten Teiler besitzen, nimm von diesen die groesste. Schritt 2: Solange
 * es ueberhaupt noch Zahlen gibt, fertige fuer jede dieser Zahlen eine Kopie der aktiven Zahlenmenge an, nimm die Zahl
 * aus der jeweiligen Kopie und gehe mit der Kopie zurueck zu Schritt 1.
 * ============================================================================ Die Klasse T01 implementiert diesen
 * Algorithmus etwas effizienter: 1. Die durch Schritt 2 erzeugte Anzahl an Verzweigungen des Suchbaums wird auf 5
 * verschiedene Weisen reduziert: a) Wurde an einer anderen Stelle der Suche die noch zu betrachtende Menge an aktiven
 * Zahlen bereits einmal untersucht, wird die Suche in diesem Zweig abgebrochen. b) Es kann fuer jeden Zweig
 * abgeschaetzt werden, wieviel der Hai hier mindestens erhaelt. Liegt dieser Wert bereits ueber einer oberen Schranke,
 * wird die Suche in diesem Zweig abgebrochen. c) Hat eine Zahl noch mehr als zwei aktive echte Teiler, so wird diese
 * Zahl an diesem Punkt noch nicht ausgewaehlt, wenn unter diesen echten Teilern einer ist, der einen der anderen teilt.
 * d) Hat eine Zahl noch mehr als einen aktiven echten Teiler, so wird statt dieser Zahl der kleinste aktive Teiler
 * ausgewaehlt. Dieser Teiler wird dabei jedoch nicht in die Zug-Sequenz uebernommen. e) Erreicht ein Zweig eine
 * Punktzahl, die um g hoeher liegt als die optimale Punktzahl fuer g-1, so wird die Suche abgebrochen. 2. Statt Kopien
 * der aktiven Zahlenmenge anzufertigen, werden mit Hilfe einer Rueckverfolgung die jeweiligen Veraenderungen an der
 * Zahlenmenge wieder rueckgaengig gemacht. ============================================================================
 * Start des Programms: Zwei Kommandozeilenparameter werden als Intervallgrenzen von und bis verwendet. Beispiel: java
 * ZahlenhaiBestwerte 1 159 Laufzeit des Programms: Die Laufzeiten sind sowohl von der Anzahl der Zahlen als auch von
 * der Anzahl der Verzweigungen des Suchbaums und von den Permutationsmoeglichkeiten in der Zug-Sequenz zur optimalen
 * Punktzahl abhaengig. Die Laufzeiten steigen nicht kontinuierlich an. Abhaengig sind die Zeiten natuerlich auch davon,
 * ob auf die Berechnung des Vorgaengers zurueck gegriffen werden kann oder ob die Suche nach Erreichen der maximal
 * moeglichen Punktzahl abgebrochen werden kann. Ab 224 sollten die Rechnungen nicht mehr mit den Standardeinstellungen
 * durchgefuehrt werden, da Java nach Erreichen der maximalen Heap-Kapazitaet recht viel Zeit zur Speicherbereinigung
 * benoetigt und das Programm eventuell sogar mit einer Fehlermeldung abbricht. Die GC-Option -XX:+UseConcMarkSweepGC
 * hat sich bei Rechnungen mit hoher RAM-Beanspruchung als guenstig erwiesen. Mit Hilfe dieser Option benoetigt z.B. die
 * 330 nur 530 MB Speicher, ohne jedoch 1.1 GB. Allerdings ist Java mit dem Standard-Kollektor i.A. deutlich schneller.
 * Durch Anwendung von Multithreading erzielt das Programm, von den ersten 223 abgesehen, wesentlich geringere
 * Laufzeiten. Die hoehere Auslastung des Rechners wirkt sich hier positiv aus. Die Reihenfolge der den einzelnen
 * Threads zugewiesenen Rechenzeiten scheint bei jeder Rechnung stets ein klein wenig unterschiedlich zu sein. Der Baum
 * wird daher nicht immer gleich durchlaufen, die Datenbank ist unterschiedlich gross und die Rechenzeiten weichen etwas
 * voneinander ab. Ueber den globalen Parameter G_MT laesst sich das Multithreading abstellen bzw. definieren, ab
 * welcher Zahl Multithreading verwendet werden soll. Es hat sich gezeigt, dass dieses Programm auf der 64-Bit-VM
 * deutlich schneller laeuft und auch, dass die Java-Version 7 gegenueber der Version 6 deutliche Zeitvorteile erzielt.
 * (Zeitwerte in Klammern: Zeit bis zum Erreichen einer Eingabesequenz zum Bestwert.) Testrechner 1: Ubuntu 10.04, 64
 * Bit VM 1.7.0, 4 GB RAM, 2 x 2.4 GHz VM-Optionen (wie 224 falls nicht anders notiert) 1 - 159 in 1 ( 1) s keine 160 -
 * 223 in 3 ( 1) s keine 224 - 259 in 472 ( 92) s -Xms1g -Xmx2g -Xss6m 260 in 271 ( 103) s 261 - 329 in 343 ( 85) s 330
 * u 331 in 415 ( 3) s 332 - 339 in 101 ( 28) s 340 u 341 in 529 ( 378) s 342 in 1443 (1042) s -Xms2g -Xmx3g -Xss12m 343
 * in 91 ( 91) s 344 in 4283 (2278) s -XX:+UseConcMarkSweepGC -Xms2g -Xmx3840m -Xss12m 345 - 347 in 156 ( 156) s 348 u
 * 349 in 6099 (2436) s wie 344 350 in ---- (----) s 351 in ---- (----) s 352 u 353 in ---- (----) s 354 - 356 in 1414
 * (1414) s 357 in 3637 ( 576) s wie 344 358 - 359 in 54 ( 54) s Testrechner 2: Ubuntu 10.04, 64 Bit VM 1.6.0_18, 34 GB
 * RAM, 4 x 2.6 GHz 350 in 20745 s -XX:+UseConcMarkSweepGC -Xms28g -Xmx32g -Xss64m 351 in 17638 s wie 350 352 u 353 in
 * 27218 s wie 350 Die Suchbaeume: Der groesste auf dem Testrechner 1 berechnete Baum gehoert zur 348. Das Programm
 * musste 9.3 Mrd. Knoten besuchen, um den optimalen Punktwert zu beweisen, was einer Verarbeitung von 1.4 Mio. Knoten/s
 * entspricht. (Aufgrund der recht grossen Anzahl an Knoten verwendet das Programm pro Baum bzw. Thread nur ein
 * Arbeitsobjekt (Objekt-Typ D). Der Baum existiert lediglich in der Idee des Programms.) Die Datenbank enthielt 80 Mio.
 * Eintraege, wobei bei der 348 die meisten Eintraege (Objekte vom Typ R) inkl. der Metadaten ein Volumen von jeweils 56
 * Byte beanspruchen. Die 352 benoetigte auf dem Testrechner 2 ein Volumen von ueber 26 GB.
 */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.IProgressMonitor;

public class ZahlenhaiBestwerte {

    private static String[][] output = null;
    private static int stoppedAt;
    static final boolean isCancelled = false;
    private static int von = 2;
    private static int bis = 0;

    /*
     * VOLLSTAENDIG bestimmt, ob der Suchbaum vollstaendig durchlaufen werden soll (true) oder ob nur bis zum Erreichen
     * einer Eingabesequenz zum Bestwert gerechnet werden soll (false):
     */
    static final boolean VOLLSTAENDIG = true;

    /*
     * Je nach Hardware kann mit G_MT die Grenze definiert werden, ab der mit Multithreading gerechnet werden soll. Ab
     * der ersten Verzweigung laufen dann die einzelnen Zweige jeweils in einem eigenen Thread. Es werden zwei
     * Moeglichkeiten des Multithreadings angeboten: Eine, bei der alle Verzweigungen parallel abgearbeitet werden, und
     * eine, bei der nur AZ_KERNE viele Threads gleichzeiteig laufen. Auf letztere Weise wird bei nur wenigen
     * Prozessor-Kernen eine fast sequentielle Reihenfolge eingehalten. Die Zahlen in G_SEMI_SEQUENTIELL sind die, bei
     * denen sich diese Art des Durchlaufs auf dem Testrechner 1 als guenstiger erwies.
     */
    static final int G_MT = 220, // auf den Testrechner 1 zugeschnitten
            AZ_KERNE = Runtime.getRuntime().availableProcessors(); // Anzahl Kerne

    static final short[] G_SEMI_SEQUENTIELL = { // opt. fuer Testrechner 1
    220, 222, 224, 225, 228, 232, 234, 261, 264, 266, 268, 270, 272, 273, 275, 276, 279, 280, 282, 284, 285, 286, 288,
            290, 292, 294, 296, 297, 300, 304, 306, 308, 312, 315, 320, 322, 324, 328, 333, 338, 344, 348, 357 };

    // Teste, ob g zu denen gehoert, die semisequentiell abgearbeitet werden:
    static boolean semiseq(int g) {
        for (short n : G_SEMI_SEQUENTIELL)
            if (g == n)
                return true;
        return false;
    }

    public static void main(final int von1, final int bis1, IProgressMonitor monitor) throws InterruptedException {
        folge2 = new int[0];
        azR2 = new int[2];
        rest0 = new int[0];
        int schlaf, t;

        try {
            von = von1;
            bis = bis1;
        } catch (Exception e) {
            System.out.println("Das Programm benoetigt zwei ganzzahlige "
                    + "Intervallgrenzen. Beispiel:\njava ZahlenhaiBestwerte 10 20");
        }
        if (von < 1) {
            if (bis < 1)
                return;
            von = 1;
        }
        if (von == 1 && bis > 0) {
            System.out.println("1:1:0:[1]:0ms");
            von++;
        }
        output = new String[bis + 1 - von][5];
        for (g = von; g <= bis; g++) {
            if (!monitor.isCanceled()) {
                start = System.currentTimeMillis();
                berechne();
                if (g >= G_MT) {
                    schlaf = (g * g * g) >> 16; // Pause von g abhaengig
                    while (azVZW > 0)
                        Thread.sleep(schlaf);
                    xs.shutdown();
                }
                stop = System.currentTimeMillis();
                t = (int) (stop - start);
                String zeit = t < 1000 ? t + "ms" : (t / 1000) + "s";
                output[g - von][4] = zeit;
                stoppedAt = g;
            }
        }
    }

    static int azVZW; // Zaehler fuer Anzahl an Thread-Instanzen
    static int azDB;

    static int g, // groesste Zahl
            g2, // g/2
            g64, // (effektiv + 16 + 63)/64
            haiMax, // obere Schranke fuer hai
            max, // gesuchter Maximalwert
            bw, // der aktuelle Bestwert
            az0, // Anzahl Zahlen in letzter Eingabe-Sequenz
            pts1g, // Punkte nach schritt1B
            pts1g0; // Punkte nach schritt1B im letzten Durchlauf

    static L folge1; // Eingabe-Sequenz nach schritt1B
    static int[] folge2, // Eingabe-Sequenz ab Schritt 2
            rest0, // speichert Rest nach schritt1B fuer naechstes g
            azR2;

    static R[] db; // Datenbank fuer Reste bzw inaktive Zahlen, Index = ds.aktiv

    static boolean abbruch, // Cut im Fall T(g) = T(g-1) + g
            semiseq;

    static long start, stop; // fuer die Zeitmessung

    static L pp; // pp = pseudoprimes Futter bei g
    static int azP, azPP, // Anzahl der Primzahlen bzw. PP groesser g/2
            effektiv, // Summe der effektiv waehlbaren Zahlen nach Schritt 1B
            effektiv2, // Index fuer Teilung der Datenbank
            effektiv2plus79; // dto.

    static ExecutorService xs;
    static L vzw1;

    // s.u.: static final short[] PRIM; // Primzahlen
    // s.u.: static final int[][] U_SCHRANKE; // untere Schranken fuer Punkte

    // eine nuetzliche Abkuerzung:
    static class L extends LinkedList<Integer> {
        private static final long serialVersionUID = -2507500531472253381L;
    }

    /*
     * Nach der Auswahl der groessten Primzahl in Schritt 1A werden saemtliche restliche Primzahlen groesser g/2 und
     * alle Pseudoprimzahlen aus dem Spiel genommen. Wesentlich ist hierfuer die vorherige Markierung durch die Methode
     * markierePP(T[]) (ebenfalls in Schritt 1A). Die Suche reduziert sich dann nach Schritt 1B auf die im Hinblick auf
     * die optimale Punktzahl effektiv waehlbaren Zahlen.
     */
    /*
     * pseudoprim() berechnet saemtliche Pseudoprimzahlen (nicht prime Zahlen, die in keiner optimalen Zugfolge
     * enthalten sein koennen). Berechnung: Hat g die Primfaktorzerlegung g = p * q mit p,q > 2 und bezeichne p' bzw. q'
     * die jeweils naechst kleinere Primzahl und sei f = p' * q' > g/2. Dann ist f pseudoprim bzgl. g. Weiter ist f
     * pseudoprim bzgl. nachfolgender g solange f > g/2. (Zur Ueberpruefung der Konsistenz der Folge opt(g) der
     * optimalen Punktzahlen kann die folgende Beziehung verwendet werden: Ist g die kleinste Zahl bzgl. derer f eine
     * Pseudoprimzahl ist, so gilt opt(g) = opt(g-1) + g - p' * q' S. Kommentare unten in int[][] U_SCHRANKE.)
     */
    static L pseudoprim() {
        L[] pp = new L[g + 1];
        pp[0] = new L();
        for (int i = 1; i <= g; i++) {
            pp[i] = (L) pp[i - 1].clone();
            if (i % 2 == 0)
                pp[i].remove(i / 2);
            L t = tmOhne1uN(i); // Teilermenge von N ohne 1 und ohne N
            if (t.size() == 1) {
                int p = t.get(0);
                if (p > 2) {
                    int q = primDavor(p), f = q * q;
                    if (f > i / 2)
                        pp[i].add(f);
                }
            } else if (t.size() == 2) {
                int p1 = t.get(0), p2 = t.get(1);
                if (p1 > 2 && istPrim(p2)) {
                    int q1 = primDavor(p1), q2 = primDavor(p2), f = q1 * q2;
                    if (f > i / 2)
                        pp[i].add(f);
                }
            }
        }
        return pp[g];
    }

    // letzte Primzahl vor N:
    static int primDavor(final int N) { // Voraussetzung: N > 2
        int i = 0;
        for (int p : PRIM)
            if (p >= N)
                break;
            else
                i++;
        return PRIM[i - 1];
    }

    // (selbstredend):
    static boolean istPrim(final int N) {
        for (int p : PRIM)
            if (p == N)
                return true;
            else if (p > N)
                return false;
        return false; // Compiler-Dummy
    }

    // Anzahl der Primzahlen groesser g/2:
    static int prim2() {
        int az = 0;
        for (int n = g; n > g / 2; n--)
            if (istPrim(n))
                az++;
        return az;
    }

    /*
     * Die Primzahlen groesser g/2 und alle Pseudoprimzahlen werden markiert und in Schritt 1B (zu diesem Zeitpunkt ist
     * die groesste Primzahl bereits nicht mehr dabei) nach entsprechender Gutschrift fuer den Hai aus dem Spiel
     * entfernt:
     */
    static void markierePP(T[] tab) {
        for (int i = g; i > g2; i--)
            if (istPrim(i) || pp.contains(i)) {
                tab[i].az = -1;
                tab[i].aktiv = null;
                tab[i].tm = null;
                tab[i].indizes = null;
            }
    }

    /*
     * Um die Anzahl erweiterte Teilermenge (Menge ECHTER aktiver Teiler ungleich 1): Um eine effiziente Verwaltung der
     * Teilermengen zu ermoeglichen, wird noch ein Array hinzu gefuegt, das sich merkt, ob der entsprechende Teiler
     * aktiv ist, und eins, das den Array-Index der Teiler speichert.
     */
    static class T {
        int az, // Anzahl INKL. der Zahl selbst
                az1; // zu Beginn und OHNE
        int[] tm; // Teilermenge OHNE die Zahl selbst
        byte[] aktiv; // Zahl aktiv=1 oder nicht=0
        short[] indizes; // die Indizes der Zahlen in tm

        T(int[] tm) { // Konstruktor fuer den Start der Suche
            az1 = tm.length;
            az = az1 + 1;
            this.tm = tm;
            aktiv = new byte[az1];
            if (az1 > 0) {
                int gT = tm[az1 - 1];
                indizes = new short[gT + 1];
            }
            for (int i = 0; i < az1; i++) {
                aktiv[i] = 1;
                indizes[tm[i]] = (short) i;
            }
        }

        T(int az, int az1, int[] tm, byte[] aktiv, short[] indizes) {
            this.az = az;
            this.az1 = az1;
            this.tm = tm;
            this.aktiv = aktiv;
            this.indizes = indizes;
        }

        T kopie() {
            return new T(az, az1, tm == null ? null : Arrays.copyOf(tm, tm.length), aktiv == null ? null
                    : Arrays.copyOf(aktiv, aktiv.length), indizes == null ? null : Arrays.copyOf(indizes,
                    indizes.length));
        }

        void einfg(final int N) {
            aktiv[indizes[N]] = 1;
            az++;
        }

        void entf(final int N) {
            aktiv[indizes[N]] = 0;
            az--;
        }

        int kleinste() { // nicht alle Eintraege 0
            int i = 0;
            for (;; i++)
                if (aktiv[i] > 0)
                    break;
            return tm[i];
        }

        int groesste() { // nicht alle Eintraege 0
            int i = az1 - 1;
            for (;; i--)
                if (aktiv[i] > 0)
                    break;
            return tm[i];
        }

    } // Ende class T

    // der gute alte Gauss (fuer berechne()):
    static int summe(final int K) {
        return K * (K + 1) / 2;
    }

    // Start der Rechnung:
    static void berechne() {
        azDB = 0;
        max = U_SCHRANKE[g][0]; // hier: bester bisher bekannter Wert
        haiMax = g * (g + 1) / 2 - max; // obere Grenze fuer den Hai; Summenformel
        az0 = U_SCHRANKE[g - 1][1];
        g2 = g / 2;
        pp = pseudoprim();
        azPP = pp.size();
        azP = prim2();
        bw = 0;
        semiseq = semiseq(g);
        folge1 = new L(); // Zahlen in den Schritten 1A und 1B
        schritt1A();
    }

    /*
     * In Schritt 1A: Erzeuge das Array mit den Teilermengen aller Zahlen. Der Array-Index entspricht dabei der
     * jeweiligen Zahl.
     */
    static T[] tabelle() {
        T[] tab = new T[g + 1];
        for (int n = 1; n <= g; n++) {
            tab[n] = new T(tm(n));
        }
        return tab;
    }

    // Teilermenge von N ohne 1 und ohne N als Array:
    static int[] tm(final int N) {
        L tm = tmOhne1uN(N);
        final int S = tm.size();
        int[] a = new int[S];
        if (S == 0)
            return a;
        int i = 0;
        for (int z : tm)
            a[i++] = z;
        return a;
    }

    // Teilermenge von N ohne 1 und ohne N als Liste:
    static L tmOhne1uN(final int N) {
        L tm = new L();
        for (int i = N / 2; i > 1; i--)
            if (N % i == 0)
                tm.push(i);
        return tm;
    }

    // Schritte 1A und 1B sind der erste Durchlauf von Schritt 1:
    static void schritt1A() {
        abbruch = false;
        T[] tab = tabelle();
        int pts, hai = 1, i = g;

        // Sonderbehandlung der groessten Primzahl:
        while (tab[i].az != 1)
            i--; // 1 bereits weg
        pts = i; // groesste Primzahl
        folge1.add(i);

        markierePP(tab); // setzt u.a. tab[i].az = -1;
        tab[i].az = -2; // Korrektur

        (new D(hai, pts, null, tab, 1, g - 2)).schritt1B();
    }

    /*
     * entf(int,T[]) und reaktiviere(int,T[],int) werden zur Aenderung und Rueckaenderung des Arbeitsobjekts D verwendet
     * (Methoden schritt1B(), setze(int) und schritt1()).
     */
    // entferne n aus saemtlichen Teilermengen:
    static void entf(final int N, T[] tab) {
        if (tab[N].az > 0)
            tab[N].az = 0; // d.h. if az == 1
        // (N wird aus tab[N] als letztes entfernt)
        for (int i = N << 1; i <= g; i += N)
            if (tab[i].az > 0)
                tab[i].entf(N);
    }

    /*
     * Der Teiler R wird in der gesamten Tabelle wieder reaktiviert, nur nicht in tab[N] (dort Sonderbehandlung):
     */
    static void reaktiviere(final int R, T[] tab, final int N) {
        if (tab[R].az == 0 && R != N)
            tab[R].az = 1;
        for (int i = R << 1; i <= g; i += R)
            if (tab[i].az > 0 && i != N)
                tab[i].einfg(R);
    }

    /*
     * D ist das Arbeitsobjekt, dass entweder selbst oder als Kopie in einem eigenstaendigen Thread per Backtracking
     * durch den Suchbaum geschickt wird. Im Wesentlichen beschreiben seine Methoden den Suchvorgang.
     */
    /*
     * Datenstruktur D, die es erlaubt, in Schritt 1 schnell die noch aktiven Zahlen der Teilermenge einer Zahl i und
     * deren Anzahl zu bestimmen: Wesentlich ist hierfuer T[] tab. tab[i] haelt diese Information bereit.
     */
    static class D {
        int hai, pts, // Hai-Punkte, Spieler-Punkte
                az, aktiv; // Anzahl bisher gewaehlter Zahlen bzw. aktiver Zahlen
        int[] seq2; // Eingabe-Sequenz ab Schritt 2; seq2[0] speichert Anzahl
        T[] tab; // Tabelle mit der Teilermenge jeder Zahl

        D(int hai, int pts, int[] seq2, T[] tab, int az, int aktiv) {
            this.hai = hai;
            this.pts = pts;
            this.seq2 = seq2;
            this.tab = tab;
            this.az = az;
            this.aktiv = aktiv;
        }

        D kopie() {
            T[] tabKopie = new T[tab.length];
            for (int i = 0; i < tab.length; i++)
                tabKopie[i] = tab[i] == null ? null : tab[i].kopie();
            return new D(hai, pts, Arrays.copyOf(seq2, seq2.length), tabKopie, az, aktiv);
        }

        void schritt1B() {
            T tabN, tabT;
            for (int n = g, t; n > g2; n--) {
                tabN = tab[n];
                if (tabN.az == -1) {
                    tabN.az = -2;
                    hai += n;
                    aktiv--;
                } else if (tabN.az == 2) {
                    t = tabN.kleinste();
                    tabT = tab[t];
                    hai += t;
                    pts += n;
                    tabN.az = -2;
                    folge1.add(n);
                    az++;
                    entf(t, tab);
                    tabT.az = -2;
                    tabT.aktiv = null;
                    tabT.tm = null;
                    tabT.indizes = null;
                    // n > g/2 muss nur in ds.tab[n] geloescht werden:
                    tabN.aktiv = null;
                    tabN.tm = null;
                    tabN.indizes = null;
                    aktiv -= 2;
                    n = g + 1;
                }
            }
            /*
             * Nach dem ersten Durchlauf des Schritts 1 gibt es 3 Moeglichkeiten: 1. Die Suche ist bereits fertig. 2.
             * g-1 hatte an dieser Stelle den gleichen 'Rest'. 3. Die Suche geht weiter.
             */
            int[] rest = new int[aktiv];
            int k = 0;
            for (int i = 2; i <= g; i++)
                if (tab[i].az > 0)
                    rest[k++] = i;
            // hatte g-1 den gleichen Rest? :
            int eq = 0;
            if (java.util.Arrays.equals(rest, rest0))
                eq = 1;
            rest0 = rest; // fuer g+1
            pts1g0 = pts1g;
            pts1g = pts; // dto.

            // 1. Die Suche ist bereits fertig:
            if (aktiv == 0) {
                max = pts;
                folge2 = new int[0];
                haiMax = g * (g + 1) / 2 - max; // falls max anfaenglich nicht stimmte
                ausgabe("");
                return;
            }
            // 2. g-1 hatte an dieser Stelle den gleichen Rest:
            else if (eq == 1) {
                max = pts;
                max += U_SCHRANKE[g - 1][0] - pts1g0;
                U_SCHRANKE[g][0] = max; // fuer g+1
                U_SCHRANKE[g][1] = az + (folge2.length == 0 ? 0 : folge2[0]);
                haiMax = g * (g + 1) / 2 - max;
                ausgabe(":rest");
                return;
            }
            // 3. Die Suche geht weiter:
            seq2 = new int[1 + aktiv / 2]; // seq2[0] speichert Anzahl
            effektiv = aktiv; // effektiv waehlbar ab der ersten Verzweigung
            effektiv2 = effektiv / 2;
            effektiv2plus79 = effektiv2 + 79; // 79 = 16 + 63
            db = new R[aktiv]; // Datenbank
            g64 = (effektiv + 79) / 64; // 79 = 16 + 63
            azR2[0] = (effektiv2 + 16) / 64;
            azR2[1] = (effektiv2 + 16) % 64;

            if (g < G_MT)
                schritt2(); // kein Multithreading
            else if (semiseq)
                schritt2SemiSequentiell();
            else
                schritt2Parallel();
        }

        /*
         * Die erste Verzweigung des Suchbaums: Jeder Zweig wird in einem eigenen Thread abgearbeitet.
         */
        void schritt2SemiSequentiell() {
            short[] vzw = verzweigungen(this);
            vzw1 = new L();
            for (short i : vzw) {
                if (i == 0)
                    break;
                azVZW++;
                vzw1.add((int) i);
            }
            xs = Executors.newFixedThreadPool(AZ_KERNE);
            for (int k = 0, n; k < AZ_KERNE; k++) {
                n = vzw1Pop();
                if (n > 0)
                    xs.execute(new VZW(n, kopie()));
            }
        }

        void schritt2Parallel() {
            short[] vzw = verzweigungen(this);
            for (short i : vzw) {
                if (i == 0)
                    break;
                azVZW++;
            }
            xs = Executors.newFixedThreadPool(azVZW);
            for (short i : vzw) {
                if (i == 0)
                    break;
                xs.execute(new VZW(i, kopie()));
            }
        }

        // Die weiteren Verzweigungen des Suchbaums:
        void schritt2() {
            if (abbruch)
                return;
            for (short i : verzweigungen(this)) {
                if (i == 0)
                    break;
                setze(i);
            }
        }

        // n wird ausgewaehlt, die Datenstruktur ds entsprechend angepasst:
        void setze(final int N) {
            if (abbruch)
                return;
            T tabN = tab[N];
            final int AZ = tabN.az; // Merker fuer Ruecksetzung; az ist 1 oder 2
            int t = 0; // unten im Fall az = 2 der echte Teiler von N
            tabN.az = 0; // verhindert das Loeschen in tabN.tm
            if (AZ > 1) {
                t = tabN.kleinste();
                hai += t;
                entf(t, tab);
                pts += N;
                seq2[++seq2[0]] = N;
                az++;
            } else
                hai += N;
            entf(N, tab); // N <= g/2 muss auch geloescht werden
            aktiv -= AZ;
            schritt1();
            // Backtracking: die Aenderungen werden wieder rueckgaengig gemacht:
            aktiv += AZ;
            reaktiviere(N, tab, N);
            if (AZ > 1) {
                pts -= N;
                seq2[seq2[0]--] = 0;
                az--;
                hai -= t;
                reaktiviere(t, tab, N);
                tabN.einfg(t);
            } else
                hai -= N;
            tabN.az = AZ;
        }

        /*
         * Der Teil, der dem Algorithmus seine hohe Geschwindigkeit verleiht: Die Suche ohne Verzweigung:
         */
        void schritt1() {
            boolean ende = true;
            T tabN;
            for (int n = g, t; n > g2; n--) {
                tabN = tab[n];
                if (tabN.az < 1)
                    continue;
                // Haifutter darf es (bei opt. Punktzahlen) hier nicht mehr geben:
                else if (tabN.az == 1)
                    return;
                else if (tabN.az == 2) {
                    t = tabN.kleinste();
                    tabN.az = 0;
                    hai += t;
                    entf(t, tab); // n > g2 muss nur in dsTabN geloescht werden
                    pts += n;
                    seq2[++seq2[0]] = n;
                    az++;
                    aktiv -= 2;
                    ende = false;
                    schritt1();
                    // Backtracking:
                    tabN.einfg(t);
                    tabN.az = 2;
                    hai -= t;
                    pts -= n;
                    seq2[seq2[0]--] = 0;
                    az--;
                    reaktiviere(t, tab, n);
                    aktiv += 2;
                    break;
                }
            }
            if (ende) {
                if (pts > bw)
                    bestwerte(); // evtl. Ausgabe; if (pts > bw) kostet
                // .. hier weniger Zeit als in synchronized bestwerte()
                // Fertig?
                if (aktiv == 0)
                    return;
                // Soll dieser Zweig weiter verfolgt werden?
                if (!moeglich(this))
                    return;
                long[] rest;
                // effektiv : die nach Schritt 1B noch effektiv waehlbaren Zahlen
                if (aktiv < effektiv2) { // weniger AKTIVE Zahlen als effektiv/2
                    int s = (effektiv2plus79 + (aktiv << 1)) >> 6, m;
                    if (s >= g64)
                        rest = restAktiv1(this);
                    else if ((m = 1 + (aktiv + 1) / 7) > s) // + 1 = - 5 + 6
                        rest = restAktiv2(s, this);
                    else
                        rest = restAktiv9(m, this);
                } else { // weniger INAKTIVE (ausgewaehlte) Zahlen
                    int r = effektiv - aktiv, s = (effektiv2plus79 + (r << 1)) >> 6, m;
                    if (s >= g64)
                        rest = restInaktiv1(this);
                    else if ((m = 1 + (r + 1) / 7) > s)
                        rest = restInaktiv2(s, this);
                    else
                        rest = restInaktiv9(m, this);
                }
                if (dbEinfuegen(rest, aktiv))
                    schritt2();
            }
        }

        /*
         * Hier wird der maximale Wert (Thread-geschuetzt) hochgeschraubt und die zugehoerige Zugfolge ausgegeben, ggf.
         * auch die weitere Suche abgebrochen:
         */
        synchronized void bestwerte() {
            if (pts > bw) { // Thread-geschuetzte Wiederholung dieser Frage
                bw = pts;
                if (pts >= max) {
                    haiMax -= pts - max; // falls max zu niedrig angesetzt wurde
                    max = pts;
                    U_SCHRANKE[g][0] = max; // Vorbereitung fuer naechstes g
                    U_SCHRANKE[g][1] = az; // dto.
                    folge2 = seq2.clone();
                    if (!VOLLSTAENDIG || max == U_SCHRANKE[g - 1][0] + g) {
                        ausgabe(":cut");
                        abbruch = true;
                    } else
                        ausgabe("");
                    // else {
                    // stop = System.currentTimeMillis();
                    // ausgabe(":"+((stop-start)/1000)+"s");
                    // }
                }
            }
        }

    } // Ende class D

    /*
     * Es wird berechnet, was der Hai in diesem Zweig mindestens bekommt. Liegt dieser Wert bereits zu hoch, wird false
     * zurueck gegeben. Zusaetzlich wird beurteilt, ob ueberhaupt noch genuegend Zahlen zur Auswahl stehen.
     */
    static boolean moeglich(final D DS) { // haiMax-Version
        int hai = DS.hai, s = DS.aktiv, s2 = s / 2, // mehr Zahlen kann der Spieler nicht bekommen
        az1 = U_SCHRANKE[g - 1][1] - DS.az; // braucht er mindestens noch
        if (s2 < az1)
            return false;
        if (s2 == az1)
            s -= s2;
        else
            s -= ++az1; // diesmal evtl. eine Zahl mehr
        final T[] TAB = DS.tab;
        for (int i = 2, n = 0; n < s; i++)
            if (TAB[i].az > 0) {
                hai += i;
                n++;
            }
        if (hai > haiMax)
            return false;
        return true;
    }

    /*
     * Die folgenden restXYZ-Methoden bilden die Spielsituation isomorph und platzsparend in ein long[] ab. Betrachtet
     * wird dabei nur der effektive Rest (nach Schritt 1B). ds.pts wird aus Speicherplatzgruenden (Java verwaltet
     * Objekte in 8-Byte-Schritten) mit in dieses Array gesteckt.
     */
    /*
     * Hier wird geprueft, ob die jeweilige Zahl aktiv ist (aktiv = 1, nicht aktiv = 0) und das entsprechende Bit
     * festgehalten:
     */
    static long[] restAktiv1(final D DS) {
        long[] rest = new long[g64];
        final int G = g;
        final T[] TAB = DS.tab;
        int i = 2, r = 0, r1 = 16; // 16 Bit ganz rechts fuer ds.pts
        for (; i <= G; i++) {
            if (TAB[i].az < 0)
                continue; // i.A. in der Ueberzahl
            if (TAB[i].az > 0)
                rest[r] |= 1; // aktiv
            if (r1 < 63) {
                rest[r] <<= 1;
                r1++;
            } else {
                r1 = 0;
                r++;
            }
        }
        rest[0] <<= 16;
        rest[0] |= DS.pts; // Punkte
        return rest;
    }

    /*
     * Hier wird geprueft, ob die jeweilige Zahl nicht aktiv ist (aktiv = 0, nicht aktiv = 1) und das entsprechende Bit
     * festgehalten:
     */
    static long[] restInaktiv1(final D DS) {
        long[] rest = new long[g64];
        final int G = g;
        final T[] TAB = DS.tab;
        int i = 2, r = 0, r1 = 16;
        for (; i <= G; i++) {
            if (TAB[i].az < 0)
                continue;
            if (TAB[i].az == 0)
                rest[r] |= 1;
            if (r1 < 63) {
                rest[r] <<= 1;
                r1++;
            } else {
                r1 = 0;
                r++;
            }
        }
        rest[0] <<= 16;
        rest[0] |= DS.pts; // Punkte
        return rest;
    }

    /*
     * Sind NUR NOCH WENIGE Zahlen AKTIV, so ist die Wahrscheinlichkeit hoch, dass zwei aufeinander folgende (effektive)
     * Zahlen nicht aktiv sind. In diesem Fall wird im vorderen Teil des Arrays (hinter ds.pts) eine 1 festgehalten.
     * Andernfalls wird hier eine Null eingetragen und beide Zahlen werden im hinteren Teil wie in der Methode
     * restAktiv1 als 0 oder 1 entsprechend notiert. D.h. hier werden INaktive Zahlen zusammengefasst und aktive
     * differenziert dargestellt.
     */
    static long[] restAktiv2(final int DIM, final D DS) {
        long[] rest = new long[DIM];
        rest[0] = DS.pts;
        final int G = g;
        final T[] TAB = DS.tab;
        int i = 2, x = 0, y = 0, m, r = azR2[0], r1 = azR2[1], s = 0, s1 = 16;
        aussen: while (i <= G) {
            while (TAB[i].az < 0)
                if (i < G)
                    i++;
                else
                    break aussen;
            if (i == G)
                break;
            m = 1;
            x = y = 0;
            if (TAB[i++].az > 0) {
                m = 0;
                x = 1;
            }
            while (TAB[i].az < 0)
                if (i < G)
                    i++;
                else
                    break aussen;
            if (TAB[i++].az > 0) {
                m = 0;
                y = 1;
            }
            if (m == 0) {
                if (x > 0)
                    rest[r] |= (1L << r1);
                if (r1 < 63)
                    r1++;
                else {
                    r1 = 0;
                    r++;
                }
                if (y > 0)
                    rest[r] |= (1L << r1);
                if (r1 < 63)
                    r1++;
                else {
                    r1 = 0;
                    r++;
                }
            } else
                rest[s] |= (1L << s1);
            if (s1 < 63)
                s1++;
            else {
                s1 = 0;
                s++;
            }
        }
        if (i <= g && TAB[i].az > 0)
            rest[r] |= (1L << r1);
        return rest;
    }

    /*
     * Sind noch viele (effektiv waehlbare) Zahlen aktiv (WENIGE INAKTIV), so ist die Wahrscheinlichkeit hoch, dass zwei
     * aufeinander folgende (effektive) Zahlen aktiv sind. ... (s.o.) D.h. hier werden aktive Zahlen zusammengefasst und
     * INaktive differenziert dargestellt.
     */
    static long[] restInaktiv2(final int DIM, final D DS) {
        long[] rest = new long[DIM];
        rest[0] = DS.pts;
        final int G = g;
        final T[] TAB = DS.tab;
        int i = 2, x = 0, y = 0, m, r = azR2[0], r1 = azR2[1], s = 0, s1 = 16;
        aussen: while (i <= G) {
            while (TAB[i].az < 0)
                if (i < G)
                    i++;
                else
                    break aussen;
            if (i == G)
                break;
            m = 1;
            x = y = 0;
            if (TAB[i++].az == 0) {
                m = 0;
                x = 1;
            }
            while (TAB[i].az < 0)
                if (i < G)
                    i++;
                else
                    break aussen;
            if (TAB[i++].az == 0) {
                m = 0;
                y = 1;
            }
            if (m == 0) {
                if (x > 0)
                    rest[r] |= (1L << r1);
                if (r1 < 63)
                    r1++;
                else {
                    r1 = 0;
                    r++;
                }
                if (y > 0)
                    rest[r] |= (1L << r1);
                if (r1 < 63)
                    r1++;
                else {
                    r1 = 0;
                    r++;
                }
            } else
                rest[s] |= (1L << s1);
            if (s1 < 63)
                s1++;
            else {
                s1 = 0;
                s++;
            }
        }
        if (i <= g && TAB[i].az == 0)
            rest[r] |= (1L << r1);
        return rest;
    }

    /*
     * Hier werden alle aktiven Zahlen als 9-Bit-Zahlen festgehalten (Methode fuer nur wenige aktive Zahlen):
     */
    static long[] restAktiv9(final int DIM, final D DS) {
        long[] rest = new long[DIM];
        final int G = g;
        final T[] TAB = DS.tab;
        int i = 2, r = 0, r1 = 2;
        for (; i <= G; i++) {
            if (TAB[i].az > 0) { // aktiv
                rest[r] |= i;
                if (r1 < 6) {
                    rest[r] <<= 9;
                    r1++;
                } else {
                    r1 = 0;
                    r++;
                }
            }
        }
        rest[0] <<= 16;
        rest[0] |= DS.pts;
        return rest;
    }

    /*
     * Hier werden alle nicht aktiven Zahlen als 9-Bit-Zahlen festgehalten (Methode fuer nur wenige nicht aktive
     * Zahlen):
     */
    static long[] restInaktiv9(final int DIM, final D DS) {
        long[] rest = new long[DIM];
        final int G = g;
        final T[] TAB = DS.tab;
        int i = 2, r = 0, r1 = 2;
        for (; i <= G; i++) {
            if (TAB[i].az == 0) { // nicht aktiv
                rest[r] |= i;
                if (r1 < 6) {
                    rest[r] <<= 9;
                    r1++;
                } else {
                    r1 = 0;
                    r++;
                }
            }
        }
        rest[0] <<= 16;
        rest[0] |= DS.pts;
        return rest;
    }

    /*
     * Aus den Zahlen, die Schritt 1(B) uebrig laesst, werden hier die Zahlen zur Verzweigung des Suchbaums bestimmt:
     * Dieser Test unterscheidet grundsaetzlich zwischen Zahlen mit genau einem aktiven echten Teiler und Zahlen mit
     * mehr als einem aktiven echten Teiler. Bei mehr als einem Teiler kann man statt der Zahl den kleinsten aktiven
     * Teiler auswaehlen. So lassen sich mehrere Zweige zusammenfassen. Der Fall einer Zahl mit genau zwei aktiven
     * echten Teilern wird zuerst behandelt und dabei der kleinste und auch der groesste (echte aktive) Teiler
     * festgehalten. Im anschliessenden Fall der Zahlen mit genau einem aktiven echten Teiler duerfen Zahl und Teiler
     * nicht mit einem zuvor notierten groessten und kleinsten Teiler uebereinstimmen.
     */
    static short[] verzweigungen(final D DS) {
        short[] res = new short[DS.aktiv];
        int[] ket = new int[DS.aktiv >> 1], // kleine echte Teiler
        nres = new int[DS.aktiv >> 2]; // was nicht in res soll
        final T[] TAB = DS.tab;
//        int ir = 0, ik = 0, inr = 0, az, t1, t2;
        int ir = 0, ik = 0, inr = 0, t1, t2;
        for (int i = g; i > 3; i--)
            if (TAB[i].az == 3) {
                t2 = TAB[i].groesste();
                if (TAB[t2].az > 1)
                    nres[inr++] = t2; // d.h. t2 % t1 = 0
                t1 = TAB[i].kleinste();
                if (enthaelt(ket, t1, ik) || enthaelt(ket, t2, ik))
                    continue;
                ket[ik++] = t1;
            }
        for (int i = g2; i > 3; i--)
            // die 2er der unteren Haelfte
//            if ((az = TAB[i].az) == 2 && !enthaelt(nres, i, inr))
        	if ((TAB[i].az) == 2 && !enthaelt(nres, i, inr))
                res[ir++] = (short) i;
        for (int i = g; i > 3; i--)
//            if ((az = TAB[i].az) > 3 && kandidat(i, TAB)) {
        	if ((TAB[i].az) > 3 && kandidat(i, TAB)) {
                t1 = TAB[i].kleinste();
                if (!enthaelt(ket, t1, ik))
                    ket[ik++] = t1;
            }
        // erst die Zahlen, dann die kleinen Teiler:
        for (int k = 0, r = ir; k < ik; k++)
            res[r++] = (short) ket[k];
        return res;
    }

    // ist N in A ?
    static boolean enthaelt(final int[] A, final int N, final int ENDE) {
        for (int i = 0; i < ENDE; i++)
            if (A[i] == N)
                return true;
        return false;
    }

    /*
     * Eine Zahl mit mehr als zwei aktiven echten Teilern, die unter diesen einen hat, der einen anderen teilt, muss
     * diesmal nicht in die Auswahl genommen werden. kandidat gibt dann false zurueck. Dieser Test nutzt die Tatsache,
     * dass die Teilermenge einer Zahl die Teilermengen ihrer Teiler vollstaendig enhaelt.
     */
    static boolean kandidat(final int K, final T[] TAB) {
        for (int i : TAB[K].tm)
            if (i != 0 && TAB[i].az > 1)
                return false;
        return true;
    }

    static class VZW extends D implements Runnable {
        int vzw;

        VZW(int vzw, D ds) {
            super(ds.hai, ds.pts, ds.seq2, ds.tab, ds.az, ds.aktiv);
            this.vzw = vzw;
        }

        @Override
		public void run() {
            // stop = System.currentTimeMillis();
            // System.out.print(vzw+":"+((stop-start)/1000)+"s:");
            setze(vzw);
            runterzaehlen();
            if (semiseq) {
                int n = vzw1Pop();
                if (n > 0)
                    xs.execute(new VZW(n, kopie()));
            }
        }

    } // Ende class VZW

    // Thread-geschuetztes Herunterzaehlen des globalen Instanzen-Zaehlers:
    static synchronized void runterzaehlen() {
        azVZW--;
    }

    // Thread-geschuetzter Zugriff auf die globale Liste vzw1:
    static synchronized int vzw1Pop() {
        if (vzw1.isEmpty())
            return -1;
        return vzw1.remove();
    }

    /*
     * Es wird versucht, den Rest in die Datenbank einzufuegen. Dies gelingt nur, wenn der Rest noch nicht vorhanden ist
     * oder wenn der Zwischenstand mehr Punkte aufweist. In beiden Faellen wird true zurueck gegeben:
     * (Thread-geschuetzter Zugriff auf die Datenbank)
     */
    static synchronized boolean dbEinfuegen(final long[] REST, final int AKTIV) {
        if (db[AKTIV] == null) {
            db[AKTIV] = new R(REST); // Wurzel anlegen
            return true;
        }
        return db[AKTIV].dbEinfuegen(REST);
    }

    /*
     * Die verbleibenden noch aktiven Zahlen ('Reste') werden um die bisher erreichten Punkte und um rekursive
     * Referenzen erweitert. R[] db implementiert eine Datenbank fuer Reste, so dass eine identische Aufgabe nicht ein
     * zweites Mal gerechnet werden muss. Der Index des Arrays entspricht dabei der Laenge des Rest-Arrays. db[i]
     * erhaelt die Struktur eines Binaerbaums und die Methoden dbEinfuegen(..) greifen - da diese Baeume aufgrund der
     * praktisch zufaelligen Reihenfolge an Zahlen recht gut ausbalanciert sind - mit einer Laufzeit vom Typ O(log(N))
     * auf db[i] zu. (Die Verwendung eines AVL- oder Rot-Schwarz-Baums bringt hier keinen Vorteil.)
     */
    static class R { // erweiterter Rest
        long[] rest; // die uebrig gebliebenen Zahlen, rest[0] = Punkte
        R links, rechts;

        R(long[] rest) {
            this.rest = rest;
            links = rechts = null;
        }

        // Einfuegen in Baum mit existierender Wurzel:
        boolean dbEinfuegen(final long[] REST) {
            long v = vergleiche(rest, REST);
            if (v > 0) {
                if (rechts == null)
                    rechts = new R(REST);
                else
                    return rechts.dbEinfuegen(REST);
            } else if (v < 0) {
                if (links == null)
                    links = new R(REST);
                else
                    return links.dbEinfuegen(REST);
            } else if ((rest[0] & 65535L) < (REST[0] & 65535L)) {
                rest[0] &= -65536L; // 16 Bits rechts nullen
                rest[0] |= (REST[0] & 65535L);
            } // (0x000000000000ffffL = 65535, 0xffffffffffff0000L = -65536L)
            else
                return false;
            return true;
        }

    }

    // 'compareTo' fuer zwei Reste:
    static long vergleiche(final long[] A, final long[] B) {
        if ((A[0] & -65536L) - (B[0] & -65536L) != 0) // 16 Bits rechts ..
            return (A[0] & -65536L) - (B[0] & -65536L); // .. = ds.pts
        for (int i = 1; i < B.length; i++)
            if (A[i] - B[i] != 0)
                return A[i] - B[i];
        return 0;
    }

    /*
     * Der senkrechte Strich dient in der Ausgabe als Information ueber das Ende der ersten Ausfuehrung des Schritts 1:
     */
    static void ausgabe(final String ARG) { // ggf. den Beduerfnissen anpassen
        String s = "";
        for (int i : folge1)
            s += i + ",";/*
                          * if (folge2.length == 0) s += "]"+ARG;
                          */
        if (folge2.length != 0) {
            int i = 1; // folge2[0] speichert Anzahl
            for (; i < folge2[0]; i++)
                s += folge2[i] + ",";
            s += folge2[i] + ",";
        }

        s = s.substring(0, s.length() - 1);
        output[g - von][0] = "" + g;
        output[g - von][1] = "" + max;
        output[g - von][2] = "" + haiMax;
        output[g - von][3] = "" + s;
    }

    static final short[] PRIM = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79,
            83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193,
            197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313,
            317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443,
            449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509 };

    /*
     * Werte, die die moeglich-Methode verwendet: Je naeher die Werte den tatsaechlichen Bestwerten sind, um so
     * schneller kann die Methode moeglich() alle unbrauchbare Zweige abschneiden. (Die hier aufgefuehrten stimmen
     * natuerlich mit den Bestwerten ueberein.)
     */
    static final int[][] U_SCHRANKE = {
    /* 0,0 */{ 0, 0 }, // /* g, haiMax */ {max, seq-Laenge}
            /* 1,1 */{ 0, 1 },
            /* 2,1 */{ 2, 1 }, // prim
            /* 3,3 */{ 3, 1 }, // prim
            /* 4,3 */{ 7, 2 }, // 2*2
            /* 5,6 */{ 9, 2 }, // prim
            /* 6,6 */{ 15, 3 }, // 2*3
            /* 7,11 */{ 17, 3 }, // prim
            /* 8,15 */{ 21, 3 },
            /* 9,15 */{ 30, 4 },
            /* 10,15 */{ 40, 5 }, // 2*5
            /* 11,22 */{ 44, 5 }, // prim
            /* 12,28 */{ 50, 5 },
            /* 13,39 */{ 52, 5 }, // prim
            /* 14,39 */{ 66, 6 }, // 2*7
            /* 15,39 */{ 81, 7 },
            /* 16,47 */{ 89, 7 },
            /* 17,60 */{ 93, 7 }, // prim
            /* 18,60 */{ 111, 8 },
            /* 19,77 */{ 113, 8 }, // prim
            /* 20,86 */{ 124, 8 },
            /* 21,87 */{ 144, 9 },
            /* 22,87 */{ 166, 10 }, // 2*11
            /* 23,106 */{ 170, 10 }, // prim
            /* 24,118 */{ 182, 10 },
            /* 25,127 */{ 198, 10 },
            /* 26,127 */{ 224, 11 }, // 2*13
            /* 27,127 */{ 251, 12 },
            /* 28,127 */{ 279, 13 },
            /* 29,150 */{ 285, 13 }, // prim
            /* 30,164 */{ 301, 13 },
            /* 31,193 */{ 303, 13 }, // prim
            /* 32,209 */{ 319, 13 },
            /* 33,209 */{ 352, 14 },
            /* 34,209 */{ 386, 15 }, // 2*17
            /* 35,212 */{ 418, 16 },
            /* 36,224 */{ 442, 16 },
            /* 37,255 */{ 448, 16 }, // prim
            /* 38,255 */{ 486, 17 }, // 2*19
            /* 39,277 */{ 503, 17 },
            /* 40,295 */{ 525, 17 },
            /* 41,332 */{ 529, 17 }, // prim
            /* 42,332 */{ 571, 18 },
            /* 43,373 */{ 573, 18 }, // prim
            /* 44,373 */{ 617, 19 }, // 2*22 (22: PP bis 43)
            /* 45,375 */{ 660, 20 },
            /* 46,375 */{ 706, 21 }, // 2*23
            /* 47,418 */{ 710, 21 }, // prim
            /* 48,442 */{ 734, 21 },
            /* 49,467 */{ 758, 21 },
            /* 50,467 */{ 808, 22 }, // 2*25 (25: PP bis 49)
            /* 51,493 */{ 833, 22 },
            /* 52,493 */{ 885, 23 }, // 2*26 (26: PP bis 51)
            /* 53,540 */{ 891, 23 }, // prim
            /* 54,545 */{ 940, 24 },
            /* 55,559 */{ 981, 24 },
            /* 56,579 */{ 1017, 24 },
            /* 57,613 */{ 1040, 24 },
            /* 58,613 */{ 1098, 25 }, // 2*29
            /* 59,666 */{ 1104, 25 }, // prim
            /* 60,693 */{ 1137, 25 },
            /* 61,752 */{ 1139, 25 }, // prim
            /* 62,752 */{ 1201, 26 }, // 2*31
            /* 63,752 */{ 1264, 27 },
            /* 64,784 */{ 1296, 27 },
            /* 65,817 */{ 1328, 27 },
            /* 66,817 */{ 1394, 28 }, // 2*33 (33: PP bis 65)
            /* 67,878 */{ 1400, 28 }, // prim
            /* 68,878 */{ 1468, 29 }, // 2*34 (34: PP bis 67)
            /* 69,916 */{ 1499, 29 },
            /* 70,919 */{ 1566, 30 },
            /* 71,986 */{ 1570, 30 }, // prim
            /* 72,986 */{ 1642, 31 },
            /* 73,105 */{ 1644, 31 }, // prim
            /* 74,105 */{ 1718, 32 }, // 2*37
            /* 75,105 */{ 1793, 33 },
            /* 76,105 */{ 1869, 34 }, // 2*38 (38: PP bis 75)
            /* 77,1089 */{ 1914, 34 },
            /* 78,1090 */{ 1991, 35 },
            /* 79,1163 */{ 1997, 35 }, // prim
            /* 80,1199 */{ 2041, 35 },
            /* 81,1216 */{ 2105, 36 },
            /* 82,1216 */{ 2187, 37 }, // 2*41
            /* 83,1295 */{ 2191, 37 }, // prim
            /* 84,1307 */{ 2263, 37 },
            /* 85,1346 */{ 2309, 37 },
            /* 86,1346 */{ 2395, 38 }, // 2*43
            /* 87,1392 */{ 2436, 38 },
            /* 88,1420 */{ 2496, 38 },
            /* 89,1503 */{ 2502, 38 }, // prim
            /* 90,1543 */{ 2552, 38 },
            /* 91,1598 */{ 2588, 38 },
            /* 92,1598 */{ 2680, 39 }, // 2*46 (46: PP bis 91)
            /* 93,1656 */{ 2715, 39 },
            /* 94,1656 */{ 2809, 40 }, // 2*47
            /* 95,1707 */{ 2853, 40 },
            /* 96,1755 */{ 2901, 40 },
            /* 97,1844 */{ 2909, 40 }, // prim
            /* 98,1844 */{ 3007, 41 },
            /* 99,1844 */{ 3106, 42 },
            /* 100,188 */{ 3164, 42 },
            /* 101,198 */{ 3168, 42 }, // prim
            /* 102,198 */{ 3270, 43 }, // 2*51 (51: PP bis 101)
            /* 103,208 */{ 3272, 43 }, // prim
            /* 104,212 */{ 3332, 43 },
            /* 105,213 */{ 3434, 44 },
            /* 106,213 */{ 3540, 45 }, // 2*53
            /* 107,223 */{ 3544, 45 }, // prim
            /* 108,223 */{ 3652, 46 },
            /* 109,234 */{ 3654, 46 }, // prim
            /* 110,234 */{ 3764, 47 }, // 2*55 (55: PP bis 109)
            /* 111,240 */{ 3813, 47 },
            /* 112,240 */{ 3925, 48 },
            /* 113,251 */{ 3929, 48 }, // prim
            /* 114,251 */{ 4043, 49 },
            /* 115,256 */{ 4101, 49 },
            /* 116,256 */{ 4217, 50 }, // 2*58 (58: PP bis 115)
            /* 117,256 */{ 4334, 51 },
            /* 118,256 */{ 4452, 52 }, // 2*59
            /* 119,263 */{ 4506, 52 },
            /* 120,266 */{ 4593, 53 },
            /* 121,269 */{ 4689, 53 },
            /* 122,269 */{ 4811, 54 }, // 2*61
            /* 123,276 */{ 4860, 54 },
            /* 124,276 */{ 4984, 55 }, // 2*62 (62: PP bis 123)
            /* 125,276 */{ 5109, 56 },
            /* 126,281 */{ 5191, 56 },
            /* 127,292 */{ 5205, 56 }, // prim
            /* 128,295 */{ 5301, 57 },
            /* 129,303 */{ 5348, 57 },
            /* 130,303 */{ 5478, 58 }, // 2*65 (65: PP bis 129)
            /* 131,316 */{ 5482, 58 }, // prim
            /* 132,3206 */{ 5572, 58 },
            /* 133,3291 */{ 5620, 58 },
            /* 134,3291 */{ 5754, 59 }, // 2*67
            /* 135,3336 */{ 5844, 59 },
            /* 136,3388 */{ 5928, 59 },
            /* 137,3519 */{ 5934, 59 }, // prim
            /* 138,3519 */{ 6072, 60 },
            /* 139,3656 */{ 6074, 60 }, // prim
            /* 140,3706 */{ 6164, 60 },
            /* 141,3792 */{ 6219, 60 },
            /* 142,3792 */{ 6361, 61 }, // 2*71
            /* 143,3869 */{ 6427, 61 },
            /* 144,3917 */{ 6523, 61 },
            /* 145,3986 */{ 6599, 61 },
            /* 146,3986 */{ 6745, 62 }, // 2*73
            /* 147,3986 */{ 6892, 63 },
            /* 148,3986 */{ 7040, 64 }, // 2*74 (74: PP bis 147)
            /* 149,4125 */{ 7050, 64 }, // prim
            /* 150,4188 */{ 7137, 64 },
            /* 151,4337 */{ 7139, 64 }, // prim
            /* 152,4405 */{ 7223, 64 },
            /* 153,4405 */{ 7376, 65 },
            /* 154,4405 */{ 7530, 66 }, // 2*77 (77: PP bis 153)
            /* 155,4492 */{ 7598, 66 },
            /* 156,4558 */{ 7688, 66 },
            /* 157,4709 */{ 7694, 66 }, // prim
            /* 158,4709 */{ 7852, 67 }, // 2*79
            /* 159,4803 */{ 7917, 67 },
            /* 160,4875 */{ 8005, 67 },
            /* 161,4970 */{ 8071, 67 },
            /* 162,4970 */{ 8233, 68 },
            /* 163,5127 */{ 8239, 68 }, // prim
            /* 164,5127 */{ 8403, 69 },
            /* 165,5127 */{ 8568, 70 },
            /* 166,5127 */{ 8734, 71 }, // 2*83
            /* 167,5290 */{ 8738, 71 }, // prim
            /* 168,5290 */{ 8906, 72 },
            /* 169,5411 */{ 8954, 72 },
            /* 170,5411 */{ 9124, 73 },
            /* 171,5411 */{ 9295, 74 },
            /* 172,5411 */{ 9467, 75 },
            /* 173,5578 */{ 9473, 75 }, // prim
            /* 174,5578 */{ 9647, 76 },
            /* 175,5578 */{ 9822, 77 },
            /* 176,5591 */{ 9985, 78 },
            /* 177,5697 */{ 10056, 78 },
            /* 178,5697 */{ 10234, 79 }, // 2*89
            /* 179,5870 */{ 10240, 79 }, // prim
            /* 180,5889 */{ 10401, 80 },
            /* 181,6068 */{ 10403, 80 }, // prim
            /* 182,6110 */{ 10543, 80 },
            /* 183,6228 */{ 10608, 80 },
            /* 184,6276 */{ 10744, 81 },
            /* 185,6369 */{ 10836, 81 },
            /* 186,6369 */{ 11022, 82 },
            /* 187,6446 */{ 11132, 82 },
            /* 188,6446 */{ 11320, 83 },
            /* 189,6508 */{ 11447, 83 },
            /* 190,6508 */{ 11637, 84 },
            /* 191,6689 */{ 11647, 84 }, // prim
            /* 192,6741 */{ 11787, 85 },
            /* 193,6932 */{ 11789, 85 }, // prim
            /* 194,6932 */{ 11983, 86 }, // 2*97
            /* 195,6946 */{ 12164, 87 },
            /* 196,6974 */{ 12332, 87 },
            /* 197,7167 */{ 12336, 87 }, // prim
            /* 198,7230 */{ 12471, 87 },
            /* 199,7427 */{ 12473, 87 }, // prim
            /* 200,7452 */{ 12648, 88 },
            /* 201,7574 */{ 12727, 88 }, // rest
            /* 202,7574 */{ 12929, 89 }, // rest 2*101
            /* 203,7689 */{ 13017, 89 }, // rest
            /* 204,7764 */{ 13146, 89 },
            /* 205,7875 */{ 13240, 89 }, // rest
            /* 206,7875 */{ 13446, 90 }, // rest 2*103
            /* 207,7875 */{ 13653, 91 },
            /* 208,7904 */{ 13832, 92 },
            /* 209,8023 */{ 13922, 92 }, // rest
            /* 210,8089 */{ 14066, 92 },
            /* 211,8288 */{ 14078, 92 }, // rest prim
            /* 212,8288 */{ 14290, 93 }, // cut 2*106 (106=2*53 PP bis 211)
            /* 213,8422 */{ 14369, 93 }, // rest
            /* 214,8422 */{ 14583, 94 }, // rest
            /* 215,8545 */{ 14675, 94 }, // rest
            /* 216,8602 */{ 14834, 94 },
            /* 217,8747 */{ 14906, 94 }, // rest
            /* 218,8747 */{ 15124, 95 }, // rest 2*109
            /* 219,8889 */{ 15201, 95 }, // rest
            /* 220,8964 */{ 15346, 95 },
            /* 221,9107 */{ 15424, 95 }, // rest 13*17-11*13:+78
            /* 222,9107 */{ 15646, 96 }, // cut 2*111 (111=3*37 PP bis 221)
            /* 223,9318 */{ 15658, 96 }, // rest prim:+12
            /* 224,9373 */{ 15827, 96 },
            /* 225,9473 */{ 15952, 96 },
            /* 226,9473 */{ 16178, 97 }, // rest 2*113 +226
            /* 227,9696 */{ 16182, 97 }, // rest prim:+4
            /* 228,9798 */{ 16308, 97 },
            /* 229,10025 */{ 16310, 97 }, // rest prim:+2
            /* 230,10025 */{ 16540, 98 }, // cut
            /* 231,10025 */{ 16771, 99 }, // cut
            /* 232,10117 */{ 16911, 99 },
            /* 233,10346 */{ 16915, 99 }, // rest prim:+4
            /* 234,10427 */{ 17068, 99 },
            /* 235,10556 */{ 17174, 99 }, // rest 5*47-3*43=+106
            /* 236,10556 */{ 17410, 100 }, // cut 2*118 (PP bis 235) +236
            /* 237,10702 */{ 17501, 100 }, // rest 3*79-2*73:+91
            /* 238,10702 */{ 17739, 101 }, // cut 2*119 (PP bis 237) +238
            /* 239,10935 */{ 17745, 101 }, // rest prim:+6
            /* 240,11043 */{ 17877, 101 },
            /* 241,11283 */{ 17879, 101 }, // rest prim:+2
            /* 242,11282 */{ 18121, 102 }, // cut 2*121:+242
            /* 243,11282 */{ 18364, 103 }, // cut
            /* 244,11282 */{ 18608, 104 }, // cut 2*122:+244
            /* 245,11282 */{ 18853, 105 }, // cut
            /* 246,11282 */{ 19099, 106 }, // cut 2*123:+246
            /* 247,11469 */{ 19159, 106 }, // rest 13*19-11*17:+60
            /* 248,11585 */{ 19291, 106 },
            /* 249,11743 */{ 19382, 106 }, // rest 3*83-2*79:+91
            /* 250,11842 */{ 19533, 106 },
            /* 251,12083 */{ 19543, 106 }, // prim:+10
            /* 252,12083 */{ 19795, 107 }, // cut:+252
            /* 253,12216 */{ 19915, 107 }, // rest 11*23-7*19:+120
            /* 254,12216 */{ 20169, 108 }, // 2*127:+254
            /* 255,12216 */{ 20424, 109 }, // cut:+255
            /* 256,12344 */{ 20552, 109 }, // +128
            /* 257,12595 */{ 20558, 109 }, // prim:+6
            /* 258,12595 */{ 20816, 110 }, // cut 2*129:+258
            /* 259,12750 */{ 20920, 110 }, // rest 7*37-5*31:+104
            /* 260,12855 */{ 21075, 110 },
            /* 261,12855 */{ 21336, 111 }, // cut
            /* 262,12855 */{ 21598, 112 }, // rest 2*131:+262
            /* 263,13112 */{ 21604, 112 }, // prim:+6
            /* 264,13121 */{ 21859, 113 },
            /* 265,13262 */{ 21983, 113 }, // rest
            /* 266,13262 */{ 22249, 114 }, // cut:+266
            /* 267,13428 */{ 22350, 114 }, // rest
            /* 268,13428 */{ 22618, 115 }, // cut:+268
            /* 269,13691 */{ 22624, 115 }, // prim:+6
            /* 270,13691 */{ 22894, 116 }, // cut:+270
            /* 271,13960 */{ 22896, 116 }, // prim:+2
            /* 272,13986 */{ 23142, 117 },
            /* 273,13997 */{ 23404, 118 },
            /* 274,13997 */{ 23678, 119 }, // rest:2*137:+274
            /* 275,13997 */{ 23953, 120 }, // cut:+275
            /* 276,14111 */{ 24115, 120 },
            /* 277,14382 */{ 24121, 120 }, // prim:+6,
            /* 278,14382 */{ 24399, 121 }, // rest:2*139:+278
            /* 279,14382 */{ 24678, 122 }, // cut
            /* 280,14406 */{ 24934, 123 },
            /* 281,14683 */{ 24938, 123 }, // prim:+4,
            /* 282,14683 */{ 25220, 124 }, // cut
            /* 283,14964 */{ 25222, 124 }, // prim:+2,
            /* 284,14964 */{ 25506, 125 }, // cut
            /* 285,14964 */{ 25791, 126 }, // cut
            /* 286,14964 */{ 26077, 127 }, // cut
            /* 287,15149 */{ 26179, 127 },
            /* 288,15221 */{ 26395, 127 },
            /* 289,15390 */{ 26515, 127 },
            /* 290,15390 */{ 26805, 128 },
            /* 291,15568 */{ 26918, 128 },
            /* 292,15568 */{ 27210, 129 },
            /* 293,15851 */{ 27220, 129 }, // prim:+10
            /* 294,15851 */{ 27514, 130 },
            /* 295,16010 */{ 27650, 130 },
            /* 296,16134 */{ 27822, 130 },
            /* 297,16212 */{ 28041, 130 },
            /* 298,16212 */{ 28339, 131 },
            /* 299,16421 */{ 28429, 131 },
            /* 300,16459 */{ 28691, 132 },
            /* 301,16664 */{ 28787, 132 },
            /* 302,16664 */{ 29089, 133 },
            /* 303,16858 */{ 29198, 133 },
            /* 304,16930 */{ 29430, 134 },
            /* 305,17107 */{ 29558, 134 },
            /* 306,17175 */{ 29796, 134 },
            /* 307,17468 */{ 29810, 134 }, // prim:+14
            /* 308,17555 */{ 30031, 134 },
            /* 309,17757 */{ 30138, 134 },
            /* 310,17757 */{ 30448, 135 },
            /* 311,18064 */{ 30452, 135 }, // prim:+4
            /* 312,18109 */{ 30719, 136 },
            /* 313,18420 */{ 30721, 136 }, // prim:+2
            /* 314,18420 */{ 31035, 137 }, // rest
            /* 315,18522 */{ 31248, 137 },
            /* 316,18522 */{ 31564, 138 }, // cut
            /* 317,18835 */{ 31568, 138 }, // prim:+4
            /* 318,18835 */{ 31886, 139 }, // cut
            /* 319,18996 */{ 32044, 139 }, // rest
            /* 320,19071 */{ 32289, 140 },
            /* 321,19277 */{ 32404, 140 }, // rest
            /* 322,19277 */{ 32726, 141 }, // cut
            /* 323,19498 */{ 32828, 141 }, // rest:+102 (221 neue PP: 323-221=102)
            /* 324,19576 */{ 33074, 141 },
            /* 325,19579 */{ 33396, 142 },
            /* 326,19579 */{ 33722, 143 }, // cut:+326 (2*163;prim)
            /* 327,19793 */{ 33835, 143 }, // 214 neue PP
            /* 328,19941 */{ 34015, 143 },
            /* 329,20156 */{ 34129, 143 }, // rest:+114 (215 neue PP: 329-215=114)
            /* 330,20261 */{ 34354, 143 },
            /* 331,20578 */{ 34368, 143 }, // prim:+14
            /* 332,20578 */{ 34700, 144 }, // cut:+332 (2*166 PP seit 267)
            /* 333,20578 */{ 35033, 145 }, // cut:+333
            /* 334,20578 */{ 35367, 146 }, // rest/cut:+334 (334=2*167;prim)
            /* 335,20761 */{ 35519, 146 }, // rest:+152 (183 neue PP: 335-183=152)
            /* 336,20842 */{ 35774, 146 },
            /* 337,21173 */{ 35780, 146 }, // prim:+6
            /* 338,21173 */{ 36118, 147 }, // cut 2*13*13:+338 PP (17*17=289)
            /* 339,21391 */{ 36239, 147 }, // rest:+121 (218 neue PP: 339-218=121)
            /* 340,21521 */{ 36449, 147 },
            /* 341,21724 */{ 36587, 147 }, // rest:+138 (203 ist neue PP: 341-203=138)
            /* 342,21826 */{ 36827, 147 },
            /* 343,21826 */{ 37170, 148 }, // cut: +343
            /* 344,21990 */{ 37350, 148 },
            /* 345,21990 */{ 37695, 149 }, // cut: +345
            /* 346,21990 */{ 38041, 150 }, // rest/cut:+346:(2*173; prim)
            /* 347,22327 */{ 38051, 150 }, // rest, prim:+10
            /* 348,22465 */{ 38261, 150 },
            /* 349,22812 */{ 38263, 150 }, // rest, prim:+2
            /* 350,22962 */{ 38463, 150 },
            /* 351,23061 */{ 38715, 150 },
            /* 352,23173 */{ 38955, 150 },
            /* 353,23522 */{ 38959, 150 }, // rest, prim:+4
            /* 354,23522 */{ 39313, 151 }, // cut: +354 (2*177; PP bis 353)
            /* 355,23723 */{ 39467, 151 }, // rest:+154 (201 neue PP: 355-201=154)
            /* 356,23723 */{ 39823, 152 }, // cut: +356 (2*178; PP bis 355)
            /* 357,23736 */{ 40167, 153 },
            /* 358,23736 */{ 40525, 154 }, // rest/cut: +358 (2*179; prim)
            /* 359,24089 */{ 40531, 154 }, // rest, prim:+6

            /*
             * --------------------------------------------------------------------------- Alle folgenden Punktzahlen
             * wurden mit Hilfe einer unzulaessigen Vereinfachung berechnet u. sind daher nicht 100 % sicher.
             */
            /* 360,24249 */{ 40731, 154 },
            /* 361,24538 */{ 40803, 154 }, // rest:+72 (289 neue PP: 361-289=72)
            /* 362,24538 */{ 41165, 155 }, // rest/cut: +362 (2*181; prim)
            /* 363,24538 */{ 41528, 156 }, // cut: +363
            /* 364,24661 */{ 41769, 156 },
            /* 365,24874 */{ 41921, 156 }, // rest:+152 (213 neue PP: 365-213=152)
            /* 366,24874 */{ 42287, 157 }, // cut: +366 (2*183; PP bis 365)
            /* 367,25233 */{ 42295, 157 }, // rest, prim:+8
            /* 368,25271 */{ 42625, 158 },
            /* 369,25271 */{ 42994, 159 }, // cut: +369
            /* 370,25271 */{ 43364, 160 }, // cut: +370 (2*185; PP bis 369)
            /* 371,25506 */{ 43500, 160 }, // rest:+136 (235 ist neue PP: 371-235=136)
            /* 372,25680 */{ 43698, 160 },
            /* 373,26047 */{ 43704, 160 }, // rest, prim:+6
            /* 374,26047 */{ 44078, 161 }, // cut: +378 (2*187; PP bis 373)
            /* 375,26172 */{ 44328, 161 },
            /* 376,26344 */{ 44532, 161 },
            /* 377,26597 */{ 44656, 161 }, // rest:+124 (253 ist neue PP: 377-253=124)
            /* 378,26597 */{ 45034, 162 }, // cut: +378
            /* 379,26970 */{ 45040, 162 } // rest, prim:+6
    };

    /*
     * Die vorliegende Version des Programms ist auf optimale Punkte bis 65535 beschraenkt.
     */

    public static int getStoppedAt() {
        return stoppedAt;
    }

    public static String[][] getOutput() {
        return output;
    }

    public static void setOutput(String[][] output) {
        ZahlenhaiBestwerte.output = output;
    }
}
