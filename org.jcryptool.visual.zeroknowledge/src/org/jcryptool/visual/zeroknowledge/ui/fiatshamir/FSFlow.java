// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.fiatshamir;

import java.math.BigInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSBob;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSCarol;
import org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.FSPerson;
import org.jcryptool.visual.zeroknowledge.views.FiatShamirView;

/**
 * Diese Klasse repräsentiert die graphische Oberfläche für den Ablauf des Fiat-Shamir-Protokolls.
 * In der Mitte findet man Buttons, die den Ablauf Schritt für Schritt ermöglichen. Rechts sind
 * Labels angeordnet, die angeben, an welchen Schritten Bob aktiv beteiligt ist, links steht, bei
 * welchen Schritten Alice bzw. Carol agiert.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FSFlow {

    private FSAlice alice;

    private FSBob bob;

    private Button calcY;

    private FSCarol carol;

    private Label erklaerung01;

    private Label erklaerung02;

    private Label erklaerung03;

    private Label erklaerung04;

    private Button genB;

    private Button genRandom;

    private Composite group;

    private boolean secretKnown;

    private Button verify;

    /**
     * Konstruktor, der die graphische Oberflaeche erstellt.
     *
     * @param shamir FiatShamir-Objekt, das die Modelle enthaelt
     * @param parent Parent-Objekt für die Oberflaeche
     */
    public FSFlow(final FiatShamirView shamir, Composite parent) {
    	
        // Attribute werden gesetzt
        alice = shamir.getAlice();
        bob = shamir.getBob();
        carol = shamir.getCarol();
        secretKnown = true;

        // allgemeine Angaben
        group = new Composite(parent, SWT.None);
        GridLayout gl_group = new GridLayout(4, false);
        gl_group.horizontalSpacing = 50;
        gl_group.verticalSpacing = 20;
        group.setLayout(gl_group);
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        // Generieren der einzelnen Komponenten des Panels
        // genLabels();
        generateButtons(shamir, group);
        setFont();
        setFirstCase(true);
    }

    /**
     * schaltet nur den ersten Button frei, deaktiviert alle folgenden
     */
    public void disableAll() {
        genRandom.setEnabled(false);
        genB.setEnabled(false);
        calcY.setEnabled(false);
        verify.setEnabled(false);
        if (erklaerung03.isVisible())
            erklaerung04.setVisible(true);
    }

    /**
     * schaltet den genRandom Button frei, deaktiviert alle folgenden
     */
    public void enableFirst() {
        genRandom.setEnabled(true);
        genB.setEnabled(false);
        calcY.setEnabled(false);
        verify.setEnabled(false);
        genRandom.setFocus();
    }

    /**
     * schaltet den vierten Button frei
     */
    public void enableForth() {
        verify.setEnabled(true);
        erklaerung03.setVisible(true);
        verify.setFocus();
    }

    /**
     * schaltet den dritten Button frei, deaktiviert alle folgenden
     */
    public void enableSecond() {
        genB.setEnabled(true);
        calcY.setEnabled(false);
        verify.setEnabled(false);
        erklaerung01.setVisible(true);
        genB.setFocus();
    }

    /**
     * schaltet den calcY Button frei, deaktiviert alle folgenden
     */
    public void enableThird() {
        calcY.setEnabled(true);
        verify.setEnabled(false);
        erklaerung02.setVisible(true);
        calcY.setFocus();
    }

    /**
     * Methode zum Erhalten des Group-Objektes
     *
     * @return Group-Objekt mit den grphischen Komponenten des Flows
     */
    public Composite getGroup() {
        return group;
    }

    /**
     * setzt den Text in Abhängigkeit von dem Fall, der behandelt wird.
     *
     * @param b true, wenn der erste Fall betrachtet wird, ansonsten false
     */
    public void setFirstCase(boolean b) {
        secretKnown = b;
        if (b) {
            erklaerung01.setText(Messages.FS_Flow_0);
            erklaerung02.setText(Messages.FS_Flow_1);
            erklaerung03.setText(Messages.FS_Flow_2);
            erklaerung04.setText(Messages.FS_Flow_3);
        } else {
            erklaerung01.setText(Messages.FS_Flow_4);
            erklaerung02.setText(Messages.FS_Flow_5);
            erklaerung03.setText(Messages.FS_Flow_6);
            erklaerung04.setText(Messages.FS_Flow_7);
        }
        genRandom.setFocus();
        erklaerung01.setVisible(false);
        erklaerung02.setVisible(false);
        erklaerung03.setVisible(false);
        erklaerung04.setVisible(false);
        group.layout();
        group.getParent().layout();
        setFont();
    }

    /**
     * Highlighting der erläuternden Textteile
     *
     * @param step Schritt im Zero-Knowledge-Protokoll
     */
    public void setStep(int step) {
        setFont();
        switch (step) {
            case 0:
                erklaerung01.setVisible(false);
                erklaerung02.setVisible(false);
                erklaerung03.setVisible(false);
                erklaerung04.setVisible(false);
                break;
            case 1:
                erklaerung01.setFont(FontService.getNormalBoldFont());
                erklaerung01.getParent().layout();
                break;
            case 2:
                erklaerung02.setFont(FontService.getNormalBoldFont());
                erklaerung02.getParent().layout();
                break;
            case 3:
                erklaerung03.setFont(FontService.getNormalBoldFont());
                erklaerung03.getParent().layout();
                break;
            case 4:
                erklaerung04.setFont(FontService.getNormalBoldFont());
                erklaerung04.getParent().layout();
                break;
        }
    }

    /**
     * Methode, die die Buttons anlegt
     *
     * @param fiatshamir FiatShamir-Objekt, das die Modells und die restlichen graphischen
     *        Komponenten enthält.
     *        
     * @param parent The parent Composite in which the buttons should be into.
     *        
     */
    private void generateButtons(final FiatShamirView fiatshamir, Composite parent) {
      
    	new Label(parent, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        // Button, um die Zufallszahl zu erstellen
        genRandom = new Button(parent, SWT.PUSH);
        genRandom.setText(Messages.FS_Flow_8);
        genRandom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        genRandom.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "Zufallszahl erstellen"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * Wenn n gesetzt wurde, werden r und x berechnet und das Protokoll einen Schritt weiter
             * gesetzt
             */
            public void widgetSelected(SelectionEvent arg0) {
                if (!alice.getN().equals(BigInteger.ZERO)) {
                    if (secretKnown) {
                        bob.setV(alice.getV());
                        alice.generateR();
                        bob.receiveX(alice.getX());
                    } else {
                        bob.setV(carol.getV());
                        carol.generateR();
                        bob.receiveX(carol.getX());
                    }
                    fiatshamir.getFlow().setStep(1);
                    enableSecond();
                }
            }
        });
        genRandom.setEnabled(false);
        
        erklaerung01 = new Label(parent, SWT.WRAP);
        erklaerung01.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        new Label(parent, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        // Button, um das Zufallsbit zu erstellen
        genB = new Button(parent, SWT.PUSH);
        genB.setText(Messages.FS_Flow_9);
        genB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        genB.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "b generieren"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * Bob generiert b, das Protokoll wird einen Schritt weitergesetzt.
             */
            public void widgetSelected(SelectionEvent arg0) {
                bob.generateB();
                enableThird();
                fiatshamir.getFlow().setStep(2);
            }
        });
        genB.setEnabled(false);
        
        erklaerung02 = new Label(parent, SWT.WRAP);
        erklaerung02.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        new Label(parent, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        // Button, um die Antwort von Alice oder Carol zu errechnen
        calcY = new Button(parent, SWT.PUSH);
        calcY.setText(Messages.FS_Flow_10);
        calcY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        calcY.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "Antwort berechnen"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * die aktive Person berechnet y und sendet y an Bob. Das Protokoll wird einen Schritt
             * weiter gesetzt.
             */
            public void widgetSelected(SelectionEvent arg0) {
                FSPerson p;
                if (secretKnown)
                    p = alice;
                else
                    p = carol;
                p.calculateY(bob.getB());
                bob.setToVerify(p.getY());
                enableForth();
                fiatshamir.getFlow().setStep(3);
            }
        });
        calcY.setEnabled(false);
        
        erklaerung03 = new Label(parent, SWT.WRAP);
        erklaerung03.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        new Label(parent, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        // Button, um die Antwort von Alice oder Carol zu verifizieren
        verify = new Button(parent, SWT.PUSH);
        verify.setText(Messages.FS_Flow_11);
        verify.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        verify.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "Verifizieren"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * Bob verifiziert die Antwort. Das Ergebnis wird öffentlich bekannt gegeben. Das
             * Protokoll ist abgeschlossen.
             */
            public void widgetSelected(SelectionEvent arg0) {
                fiatshamir.getParamsBob().setVerifiziert(bob.verify());
                fiatshamir.getFlow().setStep(4);
                fiatshamir.getButtons().enableOK(true);
                fiatshamir.getParamsBob().verifizieren(true);
                disableAll();
            }
        });
        verify.setEnabled(false);
        
        erklaerung04 = new Label(parent, SWT.WRAP);
        erklaerung04.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2,1));

    }

    /**
     * Methode, die den Font setzt
     */
    private void setFont() {
        erklaerung01.setFont(FontService.getNormalFont());
        erklaerung02.setFont(FontService.getNormalFont());
        erklaerung03.setFont(FontService.getNormalFont());
        erklaerung04.setFont(FontService.getNormalFont());
    }
}
