// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math;

import java.util.Random;
import java.util.Vector;

/**
 * Klasse zur Repräsentation eines Graphen im mathematischen Sinne
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Graph {

    private Vector<Kante> kanten;

    private Vector<Knoten> knoten;

    /**
     * Konstruktor, der einen leeren Graphen erstellt
     */
    public Graph() {
        knoten = new Vector<Knoten>();
        kanten = new Vector<Kante>();
    }

    /**
     * Konstruktor, der einen Graphen mit einer spezifierten Anzahl an Knoten und Kanten erstellt.
     * Die Kanten werden willkürlich erstellt
     *
     * @param knotenanzahl Anzahl der Knoten im Graphen
     * @param kantenanzahl Anzahl der Kanten im Graphen
     */
    public Graph(int knotenanzahl, int kantenanzahl) {
        if (knotenanzahl < 0)
            throw new IllegalArgumentException(Messages.Graph_0);
        if (kantenanzahl >= ((knotenanzahl * (knotenanzahl - 1)) / 2))
            throw new RuntimeException(
                    Messages.Graph_1);
        Random r = new Random();
        knoten = new Vector<Knoten>();
        kanten = new Vector<Kante>();

        for (int i = 0; i < knotenanzahl; i++) {
            knoten.add(new Knoten(i, i));
        }

        for (int i = 0; i < kantenanzahl; i++) {
            int knoteneins = r.nextInt(knotenanzahl);
            int knotenzwei;
            do {
                knotenzwei = r.nextInt(knotenanzahl);
            } while (knoteneins == knotenzwei);
            Kante toAdd = new Kante(knoten.get(knoteneins), knoten.get(knotenzwei));
            if (kanten.contains(toAdd)) {
                i--;
            } else
                kanten.add(toAdd);
        }
    }

    /**
     * Methode zum Hinzufügen einer Kante zum Graphen. Die Kante wird eingefügt, wenn, sie noch
     * nicht im Graphen existiert
     *
     * @param k Kante, die eingefuegt werden soll
     */
    public void addKante(Kante k) {
        if (kanten.contains(k))
            return;
        kanten.add(k);
    }

    /**
     * Fügt dem Graphen einen Knoten hinzu, falls dieser nicht schon in dem Graphen existiert.
     *
     * @param k Knoten, der hinzugefügt werden soll
     */
    public void addKnoten(Knoten k) {
        if (knoten == null) {
            knoten = new Vector<Knoten>();
        }
        if (knoten.contains(k)) {
            return;
        }
        knoten.add(k);
    }

    /**
     * Methode zum Erstellen eines neuen Graphen. Die Knoten des aktuellen Graphen werden durch den
     * Isomorphismus permutiert und in einen neuen Graphen eingefuegt.
     *
     * @param iso Isomorphismus, nach dem die Knoten permutiert werden sollen
     * @return einen Graphen, der das Ergebnis eines Isomorphismus darstellt
     */
    public Graph change(Isomorphismus iso) {
        Graph back = new Graph();
        for (Knoten k : knoten) {
            back.addKnoten(new Knoten(iso.of(k.getNummer()), k.getNummer()));
        }
        for (Kante k : kanten) {
            Knoten a = k.getKnoten()[0];
            Knoten b = k.getKnoten()[1];
            int eins = iso.of(a.getNummer());
            int zwei = iso.of(b.getNummer());
            a = new Knoten(eins, a.getNummer());
            b = new Knoten(zwei, b.getNummer());
            Kante tmp = new Kante(a, b);
            back.addKante(tmp);
        }
        return back;
    }

    /**
     * Methode, die angibt, ob der aktuelle Graph mit dem übergebenen Objekt übereinstimmt
     *
     * @param o Objekt, das mit dem aktuellen Graphen verglichen werden soll
     * @return true, wenn die beiden Objekte gleich sind, sonst false
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (!(o instanceof Graph))
            return false;
        if (o == this)
            return true;
        Graph g = (Graph) o;
        if (knoten.size() != g.getKnoten().size())
            return false;
        for (Knoten k : knoten)
            if (!g.getKnoten().contains(k))
                return false;
        if (kanten.size() != g.getKanten().size())
            return false;
        for (Kante k : kanten)
            if (!g.getKanten().contains(k))
                return false;
        return true;
    }

    /**
     * Methode zum Erhalten der Kanten des Graphen
     *
     * @return einen Vector, der alle Kanten enthält
     */
    public Vector<Kante> getKanten() {
        return kanten;
    }

    /**
     * Methode zum Erhalten der Knoten des Graphen
     *
     * @return einen Vector, der alle Knoten enthält
     */
    public Vector<Knoten> getKnoten() {
        return knoten;
    }
}
