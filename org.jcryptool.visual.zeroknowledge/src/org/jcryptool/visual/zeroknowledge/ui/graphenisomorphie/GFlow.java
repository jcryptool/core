// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.graphenisomorphie;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GBeweiser;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GBob;
import org.jcryptool.visual.zeroknowledge.algorithm.graphenisomorphie.GCarol;
import org.jcryptool.visual.zeroknowledge.views.GraphenisomorphieView;

/**
 * Repräsentiert eine Klasse, die ein Group-Objekt erstellt, das die Erklärung zum behandelten Fall
 * enthält.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class GFlow {
    private GAlice alice;
    private GBob bob;
    private GCarol carol;
    private Label erklaerung01;
    private Label erklaerung02;
    private Label erklaerung03;
    private Label erklaerung04;
    private Button generateB;
    private Button generateH;
    private GraphenisomorphieView graphiso;
    private Composite group;
    private boolean secretKnown;
    private Button sendSigma;
    private Button verify;

    /**
     * Konstruktor, der das Group-Objekt erstellt
     *
     * @param graphiso Graphenisomorphie-Objekt, das die Modelle enthält
     * @param parent Parent für die graphische Oberfläche
     */
    public GFlow(GraphenisomorphieView graphiso, Composite parent) {
        this.graphiso = graphiso;
        alice = graphiso.getAlice();
        bob = graphiso.getBob();
        carol = graphiso.getCarol();
        secretKnown = true;
        group = new Composite(parent, SWT.NONE);

        generateButtons();
        setFont();
        setFirstCase(true);
    }

    /**
     * schaltet nur den ersten Button frei, deaktiviert alle folgenden
     */
    public void disableAll() {
        generateH.setEnabled(false);
        generateB.setEnabled(false);
        sendSigma.setEnabled(false);
        verify.setEnabled(false);
        erklaerung04.setVisible(true);
    }

    /**
     * schaltet den genRandom Button frei, deaktiviert alle folgenden
     */
    public void enableFirst() {
        generateH.setEnabled(true);
        generateH.setFocus();
        generateB.setEnabled(false);
        sendSigma.setEnabled(false);
        verify.setEnabled(false);
        erklaerung01.setVisible(false);
        erklaerung02.setVisible(false);
        erklaerung03.setVisible(false);
        erklaerung04.setVisible(false);
    }

    /**
     * schaltet den vierten Button frei
     */
    public void enableForth() {
        verify.setEnabled(true);
        verify.setFocus();
        erklaerung03.setVisible(true);
    }

    /**
     * schaltet den dritten Button frei, deaktiviert alle folgenden
     */
    public void enableSecond() {
        generateB.setEnabled(true);
        generateB.setFocus();
        sendSigma.setEnabled(false);
        verify.setEnabled(false);
        erklaerung01.setVisible(true);
    }

    /**
     * schaltet den calcY Button frei, deaktiviert alle folgenden
     */
    public void enableThird() {
        sendSigma.setEnabled(true);
        sendSigma.setFocus();
        verify.setEnabled(false);
        erklaerung02.setVisible(true);
    }

    /**
     * Methode zum Erhalten des Group-Objektes, auf dem die Buttons und Labels liegen
     *
     * @return Group-Objekt, auf dem die graphische Oberfläche des Flows liegt.
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
            erklaerung01.setText(Messages.GFlow_0);
            erklaerung02.setText(Messages.GFlow_1);
            erklaerung03.setText(Messages.GFlow_2);
            erklaerung04.setText(Messages.GFlow_3);
        } else {
            erklaerung01.setText(Messages.GFlow_4);
            erklaerung02.setText(Messages.GFlow_5);
            erklaerung03.setText(Messages.GFlow_6);
            erklaerung04.setText(Messages.GFlow_7);
        }
        erklaerung01.setVisible(false);
        erklaerung02.setVisible(false);
        erklaerung03.setVisible(false);
        erklaerung04.setVisible(false);
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
            case 1:
                erklaerung01.setFont(FontService.getNormalBoldFont());
                erklaerung01.setSize(erklaerung01.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                break;
            case 2:
                erklaerung02.setFont(FontService.getNormalBoldFont());
                erklaerung02.setSize(erklaerung02.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                break;
            case 3:
                erklaerung03.setFont(FontService.getNormalBoldFont());
                erklaerung03.setSize(erklaerung03.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                break;
            case 4:
                erklaerung04.setFont(FontService.getNormalBoldFont());
                erklaerung04.setSize(erklaerung04.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                break;
        }
    }

    /**
     * aktualisiert die Attribute des Flows
     */
    public void update() {
        this.alice = graphiso.getAlice();
        this.bob = graphiso.getBob();
        this.carol = graphiso.getCarol();
    }

    /**
     * Methode, die die Buttons anlegt
     */
    private void generateButtons() {
        int heigth = 45;
        int width = 150;

        GridData gridData1 = new GridData();
        gridData1.heightHint = heigth;
        gridData1.widthHint = width;
        gridData1.verticalAlignment = GridData.CENTER;

        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.horizontalSpacing = 5;
        gridLayout1.makeColumnsEqualWidth = false;
        gridLayout1.numColumns = 2;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        group.setLayout(gridLayout1);
        group.setLayoutData(gridData);

        // Button, um die Zufallszahl zu erstellen
        generateH = new Button(group, SWT.PUSH | SWT.CENTER);
        generateH.setText(Messages.GFlow_8);
        generateH.addSelectionListener(
        /**
         * SelectionAdapter, der darauf achtet, ob der Button "Graph H erstellen" betätigt wurde
         */
        new SelectionAdapter() {
            /**
             * Die Person, die gerade betrachtet wird, erstellt einen Graphen H und reicht diesen an
             * Bob. Der erläuternde Text wird fett gedruckt, der zweite Schritt des Protokolls wird
             * ermöglicht.
             */
            public void widgetSelected(SelectionEvent arg0) {
                GBeweiser p;
                if (secretKnown) {
                    p = alice;
                } else {
                    p = carol;
                }
                bob.setG0(p.getG0());
                bob.setG1(p.getG1());
                p.generateA();
                bob.setH(p.getGraphH());
                graphiso.setH(p.getGraphH());
                setStep(1);
                enableSecond();
            }
        });
        generateH.setEnabled(false);
        generateH.setLayoutData(gridData1);
        erklaerung01 = new Label(group, SWT.None);
        // erklaerung01.setLayoutData(gridData2);
        erklaerung01.setVisible(false);

        // Button, um das Zufallsbit zu erstellen
        generateB = new Button(group, SWT.PUSH | SWT.CENTER);
        generateB.setText(Messages.GFlow_9);
        generateB.addSelectionListener(
        /**
         * SelectionAdapter, der darauf achtet, ob der Button "b wählen" betätigt wurde
         */
        new SelectionAdapter() {
            /**
             * Bob generiert b. Der erläuternde Text wird fett gedruckt, der dritte Schritt des
             * Protokolls wird ermöglicht.
             */
            public void widgetSelected(SelectionEvent arg0) {
                bob.generateB();
                enableThird();
                setStep(2);
            }
        });
        // generateB.setSize(width, height);
        // generateB.setLocation(220, 70);
        generateB.setEnabled(false);
        generateB.setLayoutData(gridData1);
        erklaerung02 = new Label(group, SWT.None);
        // erklaerung02.setLayoutData(gridData2);
        erklaerung02.setVisible(false);

        // Button, um die Antwort von Alice oder Carol zu errechnen
        sendSigma = new Button(group, SWT.PUSH | SWT.CENTER);
        sendSigma.setText(Messages.GFlow_10);
        sendSigma.addSelectionListener(
        /**
         * SelectionAdapter, der darauf achtet, ob der Button "Isomorphismus berechnen" betätigt
         * wurde
         */
        new SelectionAdapter() {
            /**
             * Die Person, die gerade betrachtet wird, berechnet den Isomorphismus h. Der
             * erläuternde Text wird fett gedruckt, der vierte Schritt des Protokolls wird
             * ermöglicht.
             */
            public void widgetSelected(SelectionEvent arg0) {
                GBeweiser p;
                if (secretKnown)
                    p = alice;
                else
                    p = carol;
                p.genH(bob.getB());
                bob.setIsomorphismus(p.getH());
                enableForth();
                setStep(3);
            }
        });
        // sendSigma.setSize(width, height);
        // sendSigma.setLocation(220, 130);
        sendSigma.setEnabled(false);
        sendSigma.setLayoutData(gridData1);
        erklaerung03 = new Label(group, SWT.None);
        // erklaerung03.setLayoutData(gridData2);
        erklaerung03.setVisible(false);

        // Button, um die Antwort von Alice oder Carol zu verifizieren
        verify = new Button(group, SWT.PUSH | SWT.CENTER);
        verify.setText(Messages.GFlow_11);
        verify.addSelectionListener(
        /**
         * SelectionAdapter, der darauf achtet, ob der Button "verifizieren" betätigt wurde
         */
        new SelectionAdapter() {
            /**
             * Bob erhält den Isomorphismus h und überprüft ihn. Das Ergebnis wird bekannt gegeben,
             * ebenso der von Bob berechnete Graph h(G<sub>b</sub>). Der erläuternde Text wird fett
             * gedruckt, der Durchlauf durch das Protokoll ist beendet.
             */
            public void widgetSelected(SelectionEvent arg0) {
                setStep(4);
                graphiso.getButtons().enableOK(true);
                graphiso.getParamsBob().verifizieren(true);
                graphiso.getParamsBob().setVerifiziert(bob.verify());
                if (bob.getB() == 0) {
                    graphiso.setH_G_b(bob.getG0().change(bob.getH()));
                } else {
                    graphiso.setH_G_b(bob.getG1().change(bob.getH()));
                }
                disableAll();
            }
        });
        verify.setEnabled(false);
        verify.setLayoutData(gridData1);
        erklaerung04 = new Label(group, SWT.None);
        // erklaerung04.setLayoutData(gridData2);
        erklaerung04.setVisible(false);
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
