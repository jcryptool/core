// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge;

import java.math.BigInteger;

import org.eclipse.swt.widgets.Button;
import org.jcryptool.visual.zeroknowledge.algorithm.Modell;
import org.jcryptool.visual.zeroknowledge.ui.Buttons;

/**
 * Interface, das alle Klassen implementieren, die modulo n rechnen. n ist das Produkt zweier großer
 * Primzahlen P und Q. Das Interface wird benötigt, um P und Q zu setzen, eventuelle Fehlermeldungen
 * anzuzeigen und auszublenden etc.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public interface ModNCalculator extends Protocol {

    /**
     * macht das Betätigen der Buttons im Flow-Objekt unmöglich
     */
    public void disableAllInFlow();

    /**
     * gibt das Modell zurück
     * 
     * @return Modell, das die Primzahlen p und q enthält
     */
    public Modell getModell();

    /**
     * gibt den generierten Wert für n zurück
     * 
     * @return Wert von n
     */
    public BigInteger getN();

    /**
     * Methode, die den Button zurückgibt, der das Geheimnis erstellt.
     * 
     * @return Button, der das Geheimnis erstellt.
     */
    public Button getSecret();

    /**
     * Entfernt das Label, das eventuelle Fehlermeldungen ausgibt.
     */
    public void removeException();

    /**
     * Returns the buttons object
     */
    public Buttons getButtons();

    /**
     * Generates the secret b
     */
    public void setSecret();

    /**
     * macht das "Verifiziert"-Feld unsichtbar
     */
    public void removeVerifingItem();

    /**
     * Methode, die P setzen soll. Der String soll in ein int oder ein BigInteger umgewandelt
     * werden. Tritt dabei ein Fehler auf (p ist keine Zahl oder keine Primzahl), wird false
     * zurückgegeben
     * 
     * @param p String, der in ein int oder ein BigInteger umgewandelt werden soll
     * @return false, wenn ein Fehler auftritt, true bei erfolgreicher Umwandlung
     */
    public boolean setP(String p);

    /**
     * Methode, die Q setzen soll. Der String soll in ein int oder ein BigInteger umgewandelt
     * werden. Tritt dabei ein Fehler auf (q ist keine Zahl oder keine Primzahl), wird false
     * zurückgegeben
     * 
     * @param q String, der in ein int oder ein BigInteger umgewandelt werden soll
     * @return false, wenn ein Fehler auftritt, true bei erfolgreicher Umwandlung
     */
    public boolean setQ(String q);
}
