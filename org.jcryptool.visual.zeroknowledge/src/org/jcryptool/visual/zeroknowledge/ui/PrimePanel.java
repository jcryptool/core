// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui;

import java.math.BigInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Klasse zum Darstellen und für die Eingabe von Wertebereichen für eine Primzahl p oder q. Die
 * Group besteht aus einem Titel (p oder q), jeweils einem Label und einem formatierten TextField
 * für die Untergrenze, die Obergrenze und zwei Labeln zur Darstellung des Ergebnisses (eins ist für
 * die Beschreibung, eins für den Inhalt).
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class PrimePanel {

    private Label ergebnis;

    private Group group;

    private Label input_ergebnis;

    private Text input_obergrenze;

    private Text input_untergrenze;

    private Label obergrenze;

    private Label untergrenze;

    /**
     * Konstruktor für ein Panel, das aus den in der Klassenbeschreibung erwähnten graphischen
     * Elementen besteht.
     *
     * @param POrQ Titel für das Panel
     */
    public PrimePanel(String POrQ, Composite parent) {
    	
    	group = new Group(parent, SWT.NONE);
    	group.setText(Messages.PrimePanel_0 + POrQ);
    	group.setLayout(new GridLayout(2, false));
    	GridData gd_group = new GridData(SWT.FILL, SWT.FILL, true, false);
    	gd_group.verticalIndent = 20;
    	group.setLayoutData(gd_group);
    	
    	untergrenze = new Label(group, SWT.NONE);
        untergrenze.setText(Messages.PrimePanel_1);
        untergrenze.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        input_untergrenze = new Text(group, SWT.NONE);
        input_untergrenze.setText(Messages.PrimePanel_2);
        input_untergrenze.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    	
        obergrenze = new Label(group, SWT.NONE);
        obergrenze.setText(Messages.PrimePanel_3);
        obergrenze.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        input_obergrenze = new Text(group, SWT.NONE);
        input_obergrenze.setText(Messages.PrimePanel_4);
        input_obergrenze.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        ergebnis = new Label(group, SWT.NONE);
        ergebnis.setText(Messages.PrimePanel_5);
        ergebnis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        input_ergebnis = new Label(group, SWT.FILL);
        input_ergebnis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
    }

    /**
     * gibt das Ergebnis der Berechnung zurück
     *
     * @return Wert des Ergebnisses
     */
    public String getErgebnis() {
        return input_ergebnis.getText();
    }

    /**
     * Methode zum Erhalten der Group
     *
     * @return Group, die die einzelnen graphischen Komponenten enthält
     */
    public Group getGroup() {
        return group;
    }

    /**
     * gibt den Wert der Obergrenze des Wertebereichs zurück
     *
     * @return Wert der Obergrenze des Wertebereichs
     */
    public String getOben() {
        return input_obergrenze.getText();
    }

    /**
     * gibt den Wert der Untergrenze des Wertebereichs zurück
     *
     * @return Wert der Untergrenze des Wertebereichs
     */
    public String getUnten() {
        return input_untergrenze.getText();
    }

    /**
     * sorgt dafür, das auf den Eingabefeldern (nicht) gearbeitet werden kann
     */
    public void setEnabled(boolean enabled) {
        group.setEnabled(enabled);
        input_untergrenze.setEnabled(enabled);
        input_obergrenze.setEnabled(enabled);
    }

    /**
     * setzt das Ergebnis der Berechnung
     *
     * @param ergebnis Ergebnis der Berechnung
     */
    public void setErgebnis(BigInteger ergebnis) {
        input_ergebnis.setText(ergebnis.toString());
    }

    /**
     * setzt den Text der Obergrenze neu
     *
     * @param oben Zahl, die in das Text-Feld eintragen wird
     */
    public void setObergrenze(int oben) {
        obergrenze.setText(oben + ""); //$NON-NLS-1$
    }

    /**
     * setzt den Text der Untergrenze neu
     *
     * @param unten Zahl, die in das Text-Feld eingetragen wird
     */
    public void setUntergrenze(int unten) {
        untergrenze.setText(unten + ""); //$NON-NLS-1$
    }
}
