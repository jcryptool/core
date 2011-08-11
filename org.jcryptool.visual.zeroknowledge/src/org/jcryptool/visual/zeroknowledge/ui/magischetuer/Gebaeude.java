// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.magischetuer;

import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MBob;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MDoor;

/**
 * Klasse zum Darstellen der verschiedenen Fälle, die beim Protokoll der magischen Tür auftreten
 * können.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Gebaeude {

    private Composite comp;

    private ShowGeb gA1;

    private ShowGeb gA11;

    private ShowGeb gA12;

    private ShowGeb gA13;

    private ShowGeb gA14;

    private ShowGeb gA21;

    private ShowGeb gA22;

    private ShowGeb gA23;

    private ShowGeb gA24;

    private ShowGeb gC1;

    private ShowGeb gC11;

    private ShowGeb gC12;

    private ShowGeb gC13;

    private ShowGeb gC14;

    private ShowGeb gC21;

    private ShowGeb gC22;

    private ShowGeb gC23;

    private ShowGeb gC24;

    private boolean secretKnown;

    /**
     * Konstruktor für ein Gebäude, das aus mehreren ShowGebs besteht, die je nach Fall sichtbar
     * werden.
     * 
     * @param parent Parent für die graphischen Komponenten
     * @param b M_Bob-Objekt, das an die einzelnen M_Bob-Objekte weitergegeben wird
     */
    public Gebaeude(Composite parent, MBob b, MDoor door) {
        comp = new Composite(parent, 0);
        comp.setSize(200, 190);
        comp.setLayout(null);

        gA1 = new ShowGeb(0, comp, b, door); // beide stehen davor
        gA11 = new ShowGeb(1, comp, b, door); // Alice geht in Raum 1
        gA12 = new ShowGeb(2, comp, b, door); // Alice in 1, Bob in 0
        gA13 = new ShowGeb(3, comp, b, door); // Alice geht von 1 in 2
        gA14 = new ShowGeb(4, comp, b, door); // Alice kommt aus 1
        gA21 = new ShowGeb(5, comp, b, door); // Alice geht in Raum 2
        gA22 = new ShowGeb(6, comp, b, door); // Alice in 2, Bob in 0
        gA23 = new ShowGeb(7, comp, b, door); // Alice geht von 2 in 1
        gA24 = new ShowGeb(8, comp, b, door); // Alice kommt aus 2

        gC1 = new ShowGeb(16, comp, b, door); // beide stehen davor
        gC11 = new ShowGeb(17, comp, b, door); // Carol geht in Raum 1
        gC12 = new ShowGeb(18, comp, b, door); // Carol in 1, Bob in 0
        gC13 = new ShowGeb(18, comp, b, door); // Carol geht von 1 in 2
        gC14 = new ShowGeb(19, comp, b, door); // Carol kommt aus 1
        gC21 = new ShowGeb(20, comp, b, door); // Carol geht in Raum 2
        gC22 = new ShowGeb(21, comp, b, door); // Carol in 2, Bob in 0
        gC23 = new ShowGeb(21, comp, b, door); // Carol geht von 2 in 1
        gC24 = new ShowGeb(22, comp, b, door); // Carol kommt aus 2
        secretKnown = true;
        setStep(0, 0, false);
    }

    /**
     * Methode zum Erhalten der Graphischen Komponente
     * 
     * @return graphische Komponente
     */
    public Composite getComp() {
        return comp;
    }

    /**
     * setzt, ob die Person, die antwortet, das Geheimnis kennt
     * 
     * @param secretKnown true, falls Alice richtig antwortet
     */
    public void setSecretKnown(boolean secretKnown) {
        this.secretKnown = secretKnown;
    }

    /**
     * setzt den Schritt, in dem sich das Protokoll befindet
     * 
     * @param step Schritt, in dem sich das Protokoll befindet
     * @param raumwahl Raumwahl des jeweiligen Beweisenden
     * @param change gibt an, ob die Person im jeweils dritten Schritt den Raum wechselt
     */
    public void setStep(int step, int raumwahl, boolean change) {
        setAllInvisible();
        switch (step) {
            case 0:
                if (secretKnown) {
                    gA1.setVisible(true);
                } else {
                    gC1.setVisible(true);
                }
                break;
            case 1:
                if (secretKnown && raumwahl == 1) {
                    gA11.setVisible(true);
                } else if (secretKnown && raumwahl == 2) {
                    gA21.setVisible(true);
                } else if (!secretKnown && raumwahl == 1) {
                    gC11.setVisible(true);
                } else {
                    gC21.setVisible(true);
                }
                break;
            case 2:
                if (secretKnown && raumwahl == 1) {
                    gA12.setVisible(true);
                } else if (secretKnown && raumwahl == 2) {
                    gA22.setVisible(true);
                } else if (!secretKnown && raumwahl == 1) {
                    gC12.setVisible(true);
                } else {
                    gC22.setVisible(true);
                }
                break;
            case 3:
                if (raumwahl == 1) {
                    if (secretKnown && change) {
                        gA13.setVisible(true);
                    } else if (secretKnown && !change) {
                        gA12.setVisible(true);
                    } else if (!secretKnown) {
                        gC12.setVisible(true);
                    }
                } else if (raumwahl == 2) {
                    if (secretKnown && change) {
                        gA23.setVisible(true);
                    } else if (secretKnown && !change) {
                        gA22.setVisible(true);
                    } else if (!secretKnown) {
                        gC22.setVisible(true);
                    }
                }
                break;
            case 4:
                if (secretKnown && raumwahl == 1) {
                    gA14.setVisible(true);
                } else if (secretKnown && raumwahl == 2) {
                    gA24.setVisible(true);
                } else if (!secretKnown && raumwahl == 1) {
                    gC14.setVisible(true);
                } else {
                    gC24.setVisible(true);
                }
                break;
        }
    }

    /**
     * macht alle ShowGebs unsichtbar
     */
    private void setAllInvisible() {
        gA1.setVisible(false);
        gA11.setVisible(false);
        gA12.setVisible(false);
        gA13.setVisible(false);
        gA14.setVisible(false);
        gA21.setVisible(false);
        gA22.setVisible(false);
        gA23.setVisible(false);
        gA24.setVisible(false);
        gC1.setVisible(false);
        gC11.setVisible(false);
        gC12.setVisible(false);
        gC13.setVisible(false);
        gC14.setVisible(false);
        gC21.setVisible(false);
        gC22.setVisible(false);
        gC23.setVisible(false);
        gC24.setVisible(false);
    }
}
