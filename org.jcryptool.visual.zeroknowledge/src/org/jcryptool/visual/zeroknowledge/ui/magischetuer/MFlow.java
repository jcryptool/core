// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.magischetuer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MAlice;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MBob;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MDoor;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MPerson;
import org.jcryptool.visual.zeroknowledge.views.MagicDoorView;

/**
 * Group, die die Erklärung zum behandelten Fall enthält.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class MFlow {

    private MAlice alice;

    private MBob bob;

    private MPerson carol;

    private Button changeRoom;

    private Button chooseRoom;

    private MDoor door;

    private Button enterRoom;

    private Label erklaerung01;

    private Label erklaerung02;

    private Label erklaerung03;

    private Label erklaerung04;

    private Composite group;

    private Button leave;

    private MagicDoorView magisch;

    private boolean secretKnown;

//    private GridData gridData1 = new GridData();

    /**
     * Konstruktor, der die Group erstellt
     */
    public MFlow(MagicDoorView magical, Composite parent) {
        // Attribute werden gesetzt
        magisch = magical;
        alice = magisch.getAlice();
        bob = magisch.getBob();
        carol = magisch.getCarol();
        door = magisch.getDoor();
        secretKnown = true;

        group = new Composite(parent, SWT.NONE);
        GridLayout gl_group = new GridLayout(4, false);
        gl_group.horizontalSpacing = 50;
        gl_group.verticalSpacing = 20;
        group.setLayout(gl_group);
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        // Generieren der einzelnen Komponenten des Panels
        generateButtons(group);
        setFont();
        setFirstCase(true);
    }

    /**
     * schaltet nur den ersten Button frei, deaktiviert alle folgenden
     */
    public void disableAll() {
        enterRoom.setEnabled(false);
        chooseRoom.setEnabled(false);
        changeRoom.setEnabled(false);
        leave.setEnabled(false);
        erklaerung04.setVisible(true);
    }

    /**
     * schaltet den genRandom Button frei, deaktiviert alle folgenden
     */
    public void enableFirst() {
        enterRoom.setEnabled(true);
        enterRoom.setFocus();
        chooseRoom.setEnabled(false);
        changeRoom.setEnabled(false);
        leave.setEnabled(false);
        erklaerung01.setVisible(false);
        erklaerung02.setVisible(false);
        erklaerung03.setVisible(false);
        erklaerung04.setVisible(false);
    }

    /**
     * schaltet den vierten Button frei
     */
    public void enableForth() {
        leave.setEnabled(true);
        leave.setFocus();
        erklaerung03.setVisible(true);
    }

    /**
     * schaltet den dritten Button frei, deaktiviert alle folgenden
     */
    public void enableSecond() {
        chooseRoom.setEnabled(true);
        chooseRoom.setFocus();
        changeRoom.setEnabled(false);
        leave.setEnabled(false);
        erklaerung01.setVisible(true);
    }

    /**
     * schaltet den calcY Button frei, deaktiviert alle folgenden
     */
    public void enableThird() {
        changeRoom.setEnabled(true);
        changeRoom.setFocus();
        leave.setEnabled(false);
        erklaerung02.setVisible(true);
    }

    /**
     * Methode, die die Buttons anlegt
     * @param parent parent Composite in which the Buttons and 
     * explanatory notes should be displayed
     */
    private void generateButtons(Composite parent) {
    
    	//Spacer, that helps to center the texts and buttons in the middle of the available space
    	new Label(parent, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    
        // Button, um die Zufallszahl zu erstellen
    	enterRoom = new Button(group, SWT.PUSH);
    	enterRoom.setText(Messages.MFlow_8);
    	enterRoom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        enterRoom.addSelectionListener(
        /**
         * SelectionAdapter, der darauf achtet, ob der Button "Raum betreten" betätigt wurde
         */
        new SelectionAdapter() {
            /**
             * Alice bzw. Carol sucht sich einen Raum aus und betritt ihn
             */
            @Override
			public void widgetSelected(SelectionEvent arg0) {
                alice.chooseRaum();
                carol.chooseRaum();
                if (secretKnown) {
                    magisch.getGebaeude().setStep(1, alice.getRaum(), false);
                    if (alice.getRaumwahl() == 1)
                        magisch.setText(0);
                    else {
                        magisch.setText(8);
                    }
                } else {
                    magisch.getGebaeude().setStep(1, carol.getRaum(), false);
                    if (carol.getRaumwahl() == 1)
                        magisch.setText(1);
                    else {
                        magisch.setText(9);
                    }
                }
                setStep(1);
                enableSecond();
            }
        });
        enterRoom.setEnabled(false);
        
        // Label für Button
        erklaerung01 = new Label(parent, SWT.WRAP);
        erklaerung01.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
    	//Spacer, that helps to center the texts and buttons in the middle of the available space
        new Label(parent, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
               
        // Button, um das Zufallsbit zu erstellen
        chooseRoom = new Button(group, SWT.PUSH);
        chooseRoom.setText(Messages.MFlow_9);
        chooseRoom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        chooseRoom.addSelectionListener(
        /**
         * SelectionAdapter, der darauf achtet, ob der Button "Raum aussuchen" betätigt wurde
         */
        new SelectionAdapter() {
            /**
             * Bob sucht sich einen Raum aus
             */
            @Override
			public void widgetSelected(SelectionEvent arg0) {
                bob.chooseRaum();
                if (secretKnown) {
                    magisch.getGebaeude().setStep(2, alice.getRaum(), false);
                } else {
                    magisch.getGebaeude().setStep(2, carol.getRaum(), false);
                }
                if (bob.getRaumwahl() == 1)
                    magisch.setText(1024);
                else {
                    magisch.setText(1025);
                }
                enableThird();
                setStep(2);
            }
        });
        chooseRoom.setEnabled(false);
        
        // Label für Button
        erklaerung02 = new Label(parent, SWT.WRAP);
        erklaerung02.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
    	//Spacer, that helps to center the texts and buttons in the middle of the available space
        new Label(parent, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        // Button, um die Antwort von Alice oder Carol zu errechnen
        changeRoom = new Button(group, SWT.PUSH);
        changeRoom.setText(Messages.MFlow_10);
        changeRoom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        changeRoom.addSelectionListener(
        /**
         * SelectionAdapter, der darauf achtet, ob der Button "evtl. Raum wechseln" betätigt wurde
         */
        new SelectionAdapter() {
            /**
             * Alice wechselt ggf. den Raum, Carol bleibt in dem Raum
             */
            @Override
			public void widgetSelected(SelectionEvent arg0) {
                MPerson p;
                int nummer = 0;
                if (secretKnown) {
                    p = alice;
                } else {
                    p = carol;
                    nummer |= 1;
                }
                boolean change = false;
                if (bob.getRaumwahl() != p.getRaumwahl()) {
                    p.changeRaum(door);
                    change = true;
                }
                if (change && p.getRaumwahl() == 1) {
                    nummer |= 2;
                } else if (change) {
                    nummer |= 16;
                } else {
                    nummer |= 512;
                }
                magisch.getGebaeude().setStep(3, p.getRaumwahl(), change);
                magisch.setText(nummer);
                enableForth();
                setStep(3);
            }
        });
        changeRoom.setEnabled(false);

        erklaerung03 = new Label(parent, SWT.WRAP);
        erklaerung03.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

    	//Spacer, that helps to center the texts and buttons in the middle of the available space
        new Label(parent, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        // Button, um die Antwort von Alice oder Carol zu verifizieren
        leave = new Button(group, SWT.PUSH);
        leave.setText(Messages.MFlow_11);
        leave.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        leave.addSelectionListener(
        /**
         * SelectionAdapter, der darauf achtet, ob der Button "Raum verlassen" betätigt wurde
         */
        new SelectionAdapter() {
            /**
             * Alice bzw. Carol verlässt den Raum, in dem sie sich befindet.
             */
            @Override
			public void widgetSelected(SelectionEvent arg0) {
                MPerson p;
                int nr = 0;
                if (secretKnown)
                    p = alice;
                else {
                    p = carol;
                    nr |= 1;
                }
                bob.setToVerify(p.getRaum());
                boolean ver = bob.verify();
                magisch.getParamsBob().setVerifiziert(ver);
                if (ver & p.getRaum() == 1) {
                    nr |= 128;
                } else if (ver) {
                    nr |= 32;
                } else if (p.getRaum() == 1) {
                    nr |= 256;
                } else {
                    nr |= 64;
                }
                setStep(4);
                magisch.setText(nr);
                magisch.getGebaeude().setStep(4, p.getRaum(), false);
                magisch.getButtons().enableOK(true);
                magisch.getParamsBob().verifizieren(true);
                disableAll();
            }
        });
        leave.setEnabled(false);
        
        erklaerung04 = new Label(parent, SWT.WRAP);
        erklaerung04.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2,1));
    
    }

    /**
     * Gibt das Group-Objekt zurück, auf dem die graphischen Komponenten enthalten sind.
     *
     * @return Group-Objekt, auf den die graphischen Komponenten liegen
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
            erklaerung01.setText(Messages.MFlow_0);
            erklaerung02.setText(Messages.MFlow_1);
            erklaerung03.setText(Messages.MFlow_2);
            erklaerung04.setText(Messages.MFlow_3);
        } else {
            erklaerung01.setText(Messages.MFlow_4);
            erklaerung02.setText(Messages.MFlow_5);
            erklaerung03.setText(Messages.MFlow_6);
            erklaerung04.setText(Messages.MFlow_7);
        }
        erklaerung01.setVisible(false);
        erklaerung02.setVisible(false);
        erklaerung03.setVisible(false);
        erklaerung04.setVisible(false);
        group.layout();
        group.getParent().layout();
        setFont();
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
}
