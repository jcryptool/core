// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie;

import java.util.Observable;
import java.util.Random;

import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Graph;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.math.Isomorphismus;

/**
 * Superklasse für Alice, Bob und Carol. Diese Klasse speichert die beiden Graphen, deren Isomorphie
 * bewiesen werden sollen, den Graphen H und den Isomorphismus h, die während der Authentifizierung
 * erzeugt werden.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public abstract class GPerson extends Observable {

    private String name;

    /**
     * einer der beiden Graphen, deren Isomorphie bewiesen werden soll
     */
    protected Graph g0;

    /**
     * der andere Graph, deren Isomorphie bewiesen werden soll
     */
    protected Graph g1;

    /**
     * Graph, der bei jedem Durchlauf durch das Protokoll neu erstellt wird
     */
    protected Graph graphH;

    /**
     * Isomorphismus h
     */
    protected Isomorphismus h;

    /**
     * Random-Objekt zur Generierung von Zufallszahlen
     */
    protected Random rand = new Random();

    /**
     * Konstruktor, der den Namen setzt
     * 
     * @param name Name der Person
     */
    public GPerson(String name) {
        this.name = name;
    }

    /**
     * liefert einen der beiden Graphen, deren Isomorphie bewiesen werden soll
     * 
     * @return Graphen G_0
     */
    public Graph getG0() {
        return g0;
    }

    /**
     * liefert einen der beiden Graphen, deren Isomorphie bewiesen werden soll
     * 
     * @return Graphen G_1
     */
    public Graph getG1() {
        return g1;
    }

    /**
     * liefert den Graphen H
     * 
     * @return Graphen H
     */
    public Graph getGraphH() {
        return graphH;
    }

    /**
     * liefert den Isomorphismus h
     * 
     * @return Isomorphismus h
     */
    public Isomorphismus getH() {
        return h;
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
     * Methode, die die Graphen g0, g1, graphH sowie die den Isomorphismus h zurücksetzt
     */
    public void reset() {
        this.g0 = null;
        this.g1 = null;
        this.h = null;
        this.graphH = null;
        notifyChanged();
    }

    /**
     * setzt der Graphen H und den Isomorphismus h zurück
     */
    public void resetNotSecret() {
        graphH = null;
        h = null;
        notifyChanged();
    }

    /**
     * Methode, die setChanged() und notifyObservers() zusammenfasst
     */
    protected void notifyChanged() {
        setChanged();
        notifyObservers();
    }
}
