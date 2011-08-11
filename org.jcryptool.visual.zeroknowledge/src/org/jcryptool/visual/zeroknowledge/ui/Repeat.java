// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.visual.zeroknowledge.algorithm.Funcs;

/**
 * Diese Klasse ist ein Dialog, der ein Protokoll mehrmals durchgeführt werden kann. Dazu muss das
 * Interface Funcs implementiert sein
 *
 * @author Mareike Paul
 *
 */
public class Repeat extends Dialog {
    private Button aliceButton;
    private Label aliceInfo;
    private Scale amount;
    private Label amountAnzeige;
    private Button carolButton;
    private Label carolInfo;
    private Label carolPercent;
    private Label ergebnis;
    private Funcs funcs;
    private Label info;
    private Button start;
    // This is the percentage how likely it is for carol to guess the right answer
    // in one single round
    private Double algoPercentage;

    /**
     * Konstruktor für die graphische Oberfläche
     *
     * @param parent Shell, zu dem dieser Dialog gehört
     * @param funcs Funcs-Implementation, die ein Protokoll durchführen soll
     * @param string String, der ggf. dem String zum Finden des richtigen Strings aus den
     *        MessagesBundle*.properties
     */
    public Repeat(Shell parent, Funcs funcs, String string) {
        super(parent, 0);
        this.funcs = funcs;
        setText(Messages.Repeat_0);
        // Sets Carols chance to guess right for the different functions
        // All except FiatFeigeShamir have a probability of 50%
        if (funcs.getClass().getSimpleName().equals("FFS_Funcs")) { //$NON-NLS-1$
            algoPercentage = 0.0625;
        } else {
            algoPercentage = 0.5;
        }

        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(getText());
        createGui(shell, string);
        shell.setSize(450, 300);
        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }

    private void createGui(Shell s, String string) {
        amount = new Scale(s, 0);
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
            public void widgetSelected(SelectionEvent arg0) {
                amountAnzeige.setText(amount.getSelection() + ""); //$NON-NLS-1$
                // If Carol:
                // Calculate how likely it is for the specified amount of iterations
                // to deceive Bob
                Double chance = getChance(algoPercentage, Double.valueOf(amount.getSelection()));
                if (chance < 0.01) {
                    carolPercent.setText("(<0.01" + Messages.Repeat_1); //$NON-NLS-1$
                } else {
                    carolPercent.setText("(" + Double.toString(chance) + Messages.Repeat_1); //$NON-NLS-1$
                }
            }
        });

        amount.setBounds(90, 160, 150, 40);
        amountAnzeige = new Label(s, 0);
        amountAnzeige.setText(amount.getSelection() + ""); //$NON-NLS-1$
        amountAnzeige.setBounds(50, 160, 30, 20);

        // Create the labels which specify for Carol the chance to deceive Bob
        carolPercent = new Label(s, 0);
        // Set initial chance to deceive Bob when "Carol"
        String tmp =
                Double.toString(getChance(algoPercentage, Double.valueOf(amount.getSelection())));
        if (tmp.equals("0.0"))tmp = "<0.01"; //$NON-NLS-1$ //$NON-NLS-2$
        carolPercent.setText("(" + tmp + Messages.Repeat_1); //$NON-NLS-1$
        carolPercent.setBounds(200, 130, 250, 20);
        carolPercent.setVisible(false);

        aliceButton = new Button(s, SWT.RADIO);
        aliceButton.addSelectionListener(

        new SelectionAdapter() {
            /**
             * Wenn der RadioButton betätigt wurde, wird beim Funcs-Objekt gesetzt, das das
             * Geheimnis bekannt ist. Zusätzlich wird der RadioButton für Carol auf
             * "nicht ausgewählt" gesetzt
             */
            public void widgetSelected(SelectionEvent e) {
                aliceButton.setSelection(true);
                carolButton.setSelection(false);
                carolPercent.setVisible(false);
                funcs.setSecretKnown(true);
            }
        });
        aliceButton.setSelection(true);
        aliceButton.setBounds(20, 30, 20, 15);

        aliceInfo = new Label(s, 0);
        aliceInfo.setText(Messages.Repeat_2);
        aliceInfo.setBounds(50, 20, 300, 40);

        carolButton = new Button(s, SWT.RADIO);
        carolButton.addSelectionListener(

        new SelectionAdapter() {
            /**
             * Wenn der RadioButton betätigt wurde, wird beim Funcs-Objekt gesetzt, das das
             * Geheimnis nicht bekannt ist. Zusätzlich wird der RadioButton für Alice auf
             * "nicht ausgewählt" gesetzt
             */
            public void widgetSelected(SelectionEvent e) {
                aliceButton.setSelection(false);
                carolButton.setSelection(true);
                carolPercent.setVisible(true);
                funcs.setSecretKnown(false);
            }
        });
        carolButton.setBounds(20, 80, 20, 20);

        carolInfo = new Label(s, 0);
        carolInfo.setText(Messages.Repeat_3);
        carolInfo.setBounds(50, 70, 360, 50);

        info = new Label(s, 0);
        info.setText(Messages.Repeat_4);
        info.setBounds(50, 130, 150, 20);

        start = new Button(s, SWT.PUSH | SWT.CENTER);
        start.setText(Messages.Repeat_9);
        start.setBounds(250, 160, 100, 25);
        start.addSelectionListener(

        new SelectionAdapter() {
            /**
             * Methode, die aufgerufen wird, wenn der Start-Button betätigt wurde. Liest den Wert
             * des Sliders aus, lässt das Protokoll dementsprechend oft laufen und gibt das Ergebnis
             * aus.
             */
            public void widgetSelected(SelectionEvent e) {
                int amount_int = amount.getSelection();
                int result = funcs.protokoll(amount_int);
                String s = result + Messages.Repeat_5;
                String s1 = new Double(result * 100.0 / amount_int).toString();
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

        ergebnis = new Label(s, 0);
        ergebnis.setBounds(40, 210, 300, 20);
    }

    // Computes (percentage^runs) in percentage (xx.yy%)
    private Double getChance(Double percentage, Double runs) {
        Double tmp = 10000 * Math.pow(percentage, runs);
        return Double.valueOf(Long.toString(Math.round(tmp))) / 100;
    }
}
