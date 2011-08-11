// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.magischetuer;

import java.util.Observable;
import java.util.Random;

/**
 * Superklasse für Alice und Carol. Hier werden der Name, der Raum, in dem sich die Person befindet,
 * der Code und die Raumwahl gespeichert.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class MPerson extends Observable {

    private String name;

    /**
     * Raum, in dem man sich befindet
     */
    protected int akt_raum = -1;

    /**
     * Geheimnis
     */
    protected int[] code = new int[0];

    /**
     * Random-Objekt zur Generierung von Zufallszahlen
     */
    protected Random rand = new Random();

    /**
     * Raum, der gewählt wurde
     */
    protected int raumwahl = -1;

    /**
     * Konstruktor, der eine Person mit Namen Carol erstellt
     */
    public MPerson() {
        this("Carol"); //$NON-NLS-1$
    }

    /**
     * Konstruktor, der den Namen setzt
     * 
     * @param name Name der Person
     */
    public MPerson(String name) {
        this.name = name;
    }

    /**
     * Methode, um den Raum zu wechseln, soweit möglich
     * 
     * @param d Door, durch die man gehen möchte
     */
    public void changeRaum(MDoor d) {
        int other = d.getOtherRoom(akt_raum);
        if ((d.isSecret() && d.verifyCode(code)) || !d.isSecret()) {
            akt_raum = other;
        }
        notifyChanged();
    }

    /**
     * Methode, um einen Raum zu wählen
     */
    public void chooseRaum() {
        raumwahl = 1 + Math.abs(rand.nextInt(2));
        akt_raum = raumwahl;
        notifyChanged();
    }

    /**
     * Methode, um an den Code zu kommen
     * 
     * @return Code, den diese Person kennt
     */
    public int[] getCode() {
        return code;
    }

    /**
     * Gibt den Namen zurück
     * 
     * @return Name der Person
     */
    public String getName() {
        return name;
    }

    /**
     * Methode zum Erhalten des Raumes, in dem sich die Person befindet
     * 
     * @return Raum, in dem sich die Person befindet
     */
    public int getRaum() {
        return akt_raum;
    }

    /**
     * Gibt die Raumwahl zurück
     * 
     * @return die generierte Raumwahl
     */
    public int getRaumwahl() {
        return raumwahl;
    }

    /**
     * Setzt die Daten der Person zurück
     */
    public void reset() {
        raumwahl = -1;
        akt_raum = -1;
        code = new int[0];
        notifyChanged();
    }

    /**
     * Setzt die Raumwahl und den Raum zurück
     */
    public void resetNotSecret() {
        raumwahl = -1;
        akt_raum = -1;
        notifyChanged();
    }

    /**
     * ruft setChanged() und notifyObservers() auf
     */
    protected void notifyChanged() {
        setChanged();
        notifyObservers();
    }
}
