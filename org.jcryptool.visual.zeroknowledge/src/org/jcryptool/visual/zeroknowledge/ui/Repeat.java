// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui;

import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.visual.zeroknowledge.algorithm.Funcs;
import org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir.FFSFuncs;

/**
 * Diese Klasse ist ein Dialog, der ein Protokoll mehrmals durchgeführt werden kann. Dazu muss das
 * Interface Funcs implementiert sein
 *
 * @author Mareike Paul
 *
 */
public class Repeat extends Dialog {
    private Button aliceButton;
//    private Label aliceInfo;
    private Scale amount;
    private Label amountAnzeige;
    private Button carolButton;
//    private Label carolInfo;
    private Label carolPercent;
    private Label ergebnis;
    private Funcs funcs;
    private Label info;
    private Button start;
    private Composite main;
    private HashMap<Integer, Double> attackChances;

    /**
     * Konstruktor für die graphische Oberfläche
     *
     * @param parent Shell, zu dem dieser Dialog gehört
     * @param funcs Funcs-Implementation, die ein Protokoll durchführen soll
     * @param string String, der ggf. dem String zum Finden des richtigen Strings aus den
     *        MessagesBundle*.properties
     */
    public Repeat(Shell parent, Funcs funcs, String string) {
    	super(parent, SWT.NONE);
        this.funcs = funcs;
        // Sets Carols chance to guess right for the different functions
        // All except FiatFeigeShamir have a probability of 50%
        if (string == "FFS.") { 
            attackChances = calculateAttackChances(((FFSFuncs) funcs).getVectorLength());
        } else {
            attackChances = calculateAttackChances(1);
        }

        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(Messages.Repeat_0);
        createGui(shell);
        shell.pack();
        shell.open();
    }

    private void createGui(Shell s) {

        main = new Composite(s, SWT.NONE);
        GridLayout gl_main = new GridLayout(3, false);
        gl_main.marginWidth = 50;
        gl_main.marginHeight = 20;
        main.setLayout(gl_main);

        aliceButton = new Button(main, SWT.RADIO | SWT.WRAP);
        aliceButton.setText(Messages.Repeat_2);
        aliceButton.addSelectionListener(

        new SelectionAdapter() {
            /**
             * Wenn der RadioButton betätigt wurde, wird beim Funcs-Objekt gesetzt, das das
             * Geheimnis bekannt ist. Zusätzlich wird der RadioButton für Carol auf
             * "nicht ausgewählt" gesetzt
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                aliceButton.setSelection(true);
                carolButton.setSelection(false);
                carolPercent.setVisible(false);
                funcs.setSecretKnown(true);
            }
        });
        aliceButton.setSelection(true);
        GridData gd_aliceButton = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_aliceButton.heightHint = 50;
        aliceButton.setLayoutData(gd_aliceButton);
                
        carolButton = new Button(main, SWT.RADIO | SWT.WRAP);
        carolButton.setText(Messages.Repeat_3);
        carolButton.addSelectionListener(
        new SelectionAdapter() {
            /**
             * Wenn der RadioButton betätigt wurde, wird beim Funcs-Objekt gesetzt, das das
             * Geheimnis nicht bekannt ist. Zusätzlich wird der RadioButton für Alice auf
             * "nicht ausgewählt" gesetzt
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                aliceButton.setSelection(false);
                carolButton.setSelection(true);
                carolPercent.setVisible(true);
                funcs.setSecretKnown(false);
            }
        });
        GridData gd_carolButton = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        carolButton.setLayoutData(gd_carolButton);

        info = new Label(main, SWT.NONE);
        info.setText(Messages.Repeat_4);
        GridData gd_info = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
        gd_info.verticalIndent = 20;
        info.setLayoutData(gd_info);
        
        // Create the labels which specify for Carol the chance to deceive Bob
        carolPercent = new Label(main, SWT.NONE);
        carolPercent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        carolPercent.setVisible(false);
        
        amountAnzeige = new Label(main, SWT.NONE);
        amountAnzeige.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

        amount = new Scale(main, SWT.NONE);
        amount.setPageIncrement(2);
        amount.setMinimum(1);
        amount.setMaximum(20);
        amount.setSelection(10);
        amount.addSelectionListener(

        new SelectionAdapter() {
            /**
             * Methode, die aufgerufen wird, wenn der Scale bewegt wurde. Aktualisiert den Wert im
             * Label, das die Anzahl der Durchläufe angibt
             */
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                var selectedRounds = amount.getSelection();
                amountAnzeige.setText(Integer.toString(selectedRounds)); //$NON-NLS-1$
                // If Carol:
                // Calculate how likely it is for the specified amount of iterations
                // to deceive Bob
                var chance = attackChances.get(selectedRounds);
                if (chance < 0.01) {
                    carolPercent.setText(String.format(" %6.2e", chance) + Messages.Repeat_1);
                } else {
                    carolPercent.setText(" " + Double.toString(chance) + Messages.Repeat_1);
                }
            }
        });
        amount.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        start = new Button(main, SWT.PUSH);
        start.setText(Messages.Repeat_9);
        start.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        start.addSelectionListener(

        new SelectionAdapter() {
            /**
             * Methode, die aufgerufen wird, wenn der Start-Button betätigt wurde. Liest den Wert
             * des Sliders aus, lässt das Protokoll dementsprechend oft laufen und gibt das Ergebnis
             * aus.
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                int amount_int = amount.getSelection();
                int result = funcs.protokoll(amount_int);
                String s = result + Messages.Repeat_5;
                String s1 = String.valueOf(((double) result * 100) / amount_int);
                int length = Math.min(s1.indexOf(".") + 3, s1.length()); //$NON-NLS-1$
                s1 = s1.substring(0, length).concat("%"); //$NON-NLS-1$
                ergebnis.setText(s + " " + s1); //$NON-NLS-1$
                // Only when all iterations can be guessed right from Carol, Bob is deceived
                if (carolButton.getSelection()) {
                    if (s1.equals("100.0%")) { //$NON-NLS-1$
                        ergebnis.setText(ergebnis.getText().concat(Messages.Repeat_6));
                    } else {
                        ergebnis.setText(ergebnis.getText().concat(Messages.Repeat_7));
                    }
                }
            }
        });
        start.setToolTipText(Messages.Repeat_8);

        ergebnis = new Label(main, SWT.NONE);
        ergebnis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));

        var defaultScalerSelect = amount.getSelection();
        amountAnzeige.setText(Integer.toString(defaultScalerSelect));

        // Set initial chance to deceive Bob when "Carol"
        var chance = attackChances.get(defaultScalerSelect);

        if (chance < 0.01) {
            carolPercent.setText(String.format(" %6.2e", chance) + Messages.Repeat_1);
        } else {
            carolPercent.setText(" " + Double.toString(chance) + Messages.Repeat_1);
        } 
        
        main.pack();
    }

    /**
     * Calculate a map of (round: chance of successful attack) for 20 rounds.
     * 
     * The attack chance for Feige-Fiat-Shamir is 0.5^(k*t), where k is the used
     * secret vector length and t is the number of rounds in the protocol. 
     * 
     * @param vectorSize Length of secret vector used in the protocol variant.
     * @return A HashMap with key=round, value=chance
     * 
     */
    private HashMap<Integer, Double> calculateAttackChances(int vectorSize) {
        var map = new HashMap<Integer, Double>();
        for (int i = 1; i <= 21; ++i) {
            map.put(i, Math.pow(0.5, i * vectorSize));
        }
        return map;
    }
}
