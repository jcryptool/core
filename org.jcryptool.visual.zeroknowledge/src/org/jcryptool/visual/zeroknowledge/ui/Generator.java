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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.visual.zeroknowledge.ModNCalculator;
import org.jcryptool.visual.zeroknowledge.algorithm.Primzahlen;

/**
 * Dialog zum Generieren von Primzahlen. Man kann zwischen den Algorithmen wählen und bestimmen, ob
 * die Wertebereiche der beiden Primzahlen gleich sein sollen. Man kann Primzahlen generieren und
 * über einen Button angeben, dass die Zahlen übernommen werden sollen. Außerdem kann die Aktion
 * abgebrochen werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Generator extends Dialog {

    private Button abbrechen;

    private Shell akt;

    private boolean both;

    private Label exception;

    private Button generieren;

    private Button gleich;

    private Button nehmen;

    private PrimePanel p;

    private PrimePanel q;

    private ModNCalculator shamir;

    private Button unabhaengig;

    private Group wertebereich;
    
    private Composite main;
    
    private Composite compositeButtons;

    /**
     * Konstruktor für einen Dialog, der zum Erstellen von Primzahlen gedacht ist.
     *
     * @param fiatShamir Objekt, in dem die Modelle gespeichert sind.
     * @param s Parent der graphischen Komponente
     */
    public Generator(ModNCalculator fiatShamir, Shell s) {
        super(s);
        shamir = fiatShamir;
        open();
    }

    /**
     * gibt an, ob der Wertebereiche gleich sein sollen
     *
     * @return true, wenn die Wertebereiche gleich sein sollen
     */
    public boolean isBoth() {
        return both;
    }

    /**
     * Methode, um das Label, das Fehlermeldungen ausgibt, nicht sichtbar zu machen
     */
    public void removeExceptionLabel() {
        exception.setVisible(false);
    }

    /**
     * Methode, um das Label mit der Fehlermeldung sichtbar zu machen und den Text zu aktualisieren
     *
     * @param text Text, der auf dem Label erscheinen soll
     */
    public void setExceptionLabel(String text) {
        final MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_ERROR
                | SWT.OK);
        messageBox.setText(text);
        messageBox.setMessage(text);
        messageBox.open();
    }

    /**
     * Methode, um die GUI zu erstellen.
     *
     * @param s Shell, auf der die GUI zusammengebastelt werden soll
     */
    private void createGui(Shell s) {

    	main = new Composite(s, SWT.NONE);
    	GridLayout gd_main = new GridLayout(2, true);
    	gd_main.marginWidth = 30;
    	main.setLayout(gd_main);
    	
        // allgemeine Ueberschrift
    	Label description = new Label(main, SWT.FILL);
        description.setText(Messages.Generator_0);
        description.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1));

        // Gruppe mit dem Wertebereich anlegen, Ueberschrift setzen
        wertebereich = new Group(main, SWT.NONE);
        wertebereich.setText(Messages.Generator_1);
        wertebereich.setLayout(new GridLayout(2, true));
        GridData gd_wertebereich = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        gd_wertebereich.verticalIndent = 20;
        wertebereich.setLayoutData(gd_wertebereich);
        
        
        gleich = new Button(wertebereich, SWT.RADIO);
        gleich.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        gleich.setText(Messages.Generator_3);
        gleich.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "beide gleich (nur einer einzugeben)"-Button
         * achtet
         */
        new SelectionAdapter() {
            /**
             * Methode, die aufgerufen wird, wenn der "gleich"-RadioButton ausgewählt wurde. Setzt
             * den RadioButton "unabhängig" auf nicht gewahlt und vermerkt, dass die Wertebereich
             * gleich sein sollen
             */
            public void widgetSelected(SelectionEvent arg0) {
                unabhaengig.setSelection(false);
                gleich.setSelection(true);
                both = true;
                q.setEnabled(false);
            }
        });

        // Erstellung der RadioButtons zur Festlegung des Verhaeltnisses der
        // Wertebereiche
        unabhaengig = new Button(wertebereich, SWT.RADIO);
        unabhaengig.setText(Messages.Generator_2);
        unabhaengig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        unabhaengig.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "unabhängig voneinander"-Button achtet
         */
        new SelectionAdapter() {

            /**
             * Methode, die aufgerufen wird, wenn der "unabhängig"-RadioButton ausgewählt wurde.
             * Setzt den RadioButton "gleich" auf nicht gewahlt und vermerkt, dass die Wertebereich
             * nicht gleich sein muessen
             */
            public void widgetSelected(SelectionEvent arg0) {
                gleich.setSelection(false);
                unabhaengig.setSelection(true);
                both = false;
                q.setEnabled(true);
            }
        });
        unabhaengig.setSelection(true);

        // PrimePanels p und q einfuegen
        p = new PrimePanel("p", main);
        q = new PrimePanel("q", main);
        
        compositeButtons = new Composite(main, SWT.NONE);
        compositeButtons.setLayout(new GridLayout(3, true));
        GridData gd_compositeButtons = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        gd_compositeButtons.verticalIndent = 20;
        compositeButtons.setLayoutData(gd_compositeButtons);
        
        
        // Button zum Generieren der Primzahlen
        generieren = new Button(compositeButtons, SWT.PUSH);
        generieren.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        generieren.setText(Messages.Generator_6);
        generieren.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "Primzahlen generieren"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * Methode, die aufgerufen wird, wenn der "generieren"-Button geklickt wurde. Liest die
             * Ober- und Untergrenzen ein und erzeugt zwei Primzahlen in diesen Bereichen.
             */
            public void widgetSelected(SelectionEvent arg0) {
                nehmen.setEnabled(true);
                removeExceptionLabel();

                int unten_p, oben_p, unten_q, oben_q;
                String p_unten = p.getUnten(), p_oben = p.getOben();

                // bei der Untergrenze nur Leerzeichen eingegeben
                if (p_unten.trim().length() == 0) {
                    setExceptionLabel(Messages.Generator_7);
                    return;
                }
                // bei der Obergrenze nur Leerzeichen eingegeben
                if (p_oben.trim().length() == 0) {
                    setExceptionLabel(Messages.Generator_8);
                    return;
                }
                // keine Zahl bei der Untergrenze eingegeben
                try {
                    unten_p = Integer.parseInt(p_unten);
                } catch (NumberFormatException e) {
                    setExceptionLabel(Messages.Generator_9);
                    return;
                }
                // keine Zahl bei der Obergrenze eingegeben
                try {
                    oben_p = Integer.parseInt(p_oben);
                } catch (NumberFormatException e) {
                    setExceptionLabel(Messages.Generator_10);
                    return;
                }
                // Primzahl p generieren
                try {
                    p.setErgebnis(Primzahlen.primzahl(unten_p, oben_p));
                } catch (IllegalArgumentException e) {
                    setExceptionLabel(e.getMessage());
                    return;
                }
                // wenn die beiden Primzahlen den gleichen Wertebereich haben sollen:
                if (both) {
                    // Ueberpruefung, ob das Ergebnis klein genug ist
                    if (2 * oben_p > 29) {
                        setExceptionLabel(Messages.Generator_11);
                        return;
                    }
                    // Ober- und Untergrenze von q setzen
                    q.setObergrenze(oben_q = oben_p);
                    q.setUntergrenze(unten_q = unten_p);
                } else {
                    String q_unten = q.getUnten().trim(), q_oben = q.getOben().trim();
                    // wenn bei der Untergrenze nur Leerzeichen eingegeben wurden
                    if (q_unten.trim().length() == 0) {
                        setExceptionLabel(Messages.Generator_12);
                        return;
                    }
                    // wenn bei der Obergrenze nur Leerzeichen eingegeben wurden
                    if (q_oben.trim().length() == 0) {
                        setExceptionLabel(Messages.Generator_13);
                        return;
                    }
                    // wenn keine Zahl bei der Untergrenze eingegeben wurde
                    try {
                        unten_q = Integer.parseInt(q_unten);
                    } catch (NumberFormatException e) {
                        setExceptionLabel(Messages.Generator_14);
                        return;
                    }
                    // wenn keine Zahl bei der Obergrenze eingegeben wurde
                    try {
                        oben_q = Integer.parseInt(q_oben);
                    } catch (NumberFormatException e) {
                        setExceptionLabel(Messages.Generator_15);
                        return;
                    }
                }
                if (oben_p + oben_q > 29) {
                    setExceptionLabel(Messages.Generator_16);
                    return;
                }
                // q generieren
                try {
                    q.setErgebnis(Primzahlen.primzahl(unten_q, oben_q));
                    nehmen.setFocus();
                } catch (IllegalArgumentException e) {
                    setExceptionLabel(e.getMessage());
                }
            }
        });
        generieren.setToolTipText(Messages.Generator_17);

        // Button zum Uebernehmen der Primzahlen
        nehmen = new Button(compositeButtons, SWT.PUSH);
        nehmen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        nehmen.setText(Messages.Generator_18);
        nehmen.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "Primzahlen übernehmen"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * Methode, die aufgerufen wird, wenn der "Nehmen"-Button betätigt wurde. Setzt p und q
             * im Modell und schaltet den Button "Geheimnis erstellen" an.
             */
            public void widgetSelected(SelectionEvent arg0) {
                removeExceptionLabel();
                if (shamir.setP(p.getErgebnis()) && shamir.setQ(q.getErgebnis())) {
                    shamir.removeException();// getPrimeGen().removeException();
                }
                shamir.getSecret().setEnabled(true);
                akt.dispose();
            }
        });
        nehmen.setToolTipText(Messages.Generator_19);
        nehmen.setEnabled(false);

        // Button zum Abbrechen der Aktion
        abbrechen = new Button(compositeButtons, SWT.PUSH);
        abbrechen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        abbrechen.setText(Messages.Generator_20);
        abbrechen.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "Abbrechen"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * Methode, die aufgerufen wird, wenn der "Abbrechen"-Button betätigt wurde. Schließt
             * den Generator.
             */
            public void widgetSelected(SelectionEvent arg0) {
                akt.dispose();
                shamir.removeException();
            }
        });
        abbrechen.setToolTipText(Messages.Generator_21);

        exception = new Label(main, SWT.BOLD);
        exception.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        GridData gd_exception = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_exception.verticalIndent = 20;
        exception.setLayoutData(gd_exception);
        exception.setVisible(false);
        
        main.pack();
        
    }

    /**
     * private Methode, um den Dialog zum Laufen zu bekommen
     */
    private void open() {
        Shell parent = getParent();
        akt = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        akt.setText("Primzahlen generieren");
        createGui(akt);
        akt.pack();
        akt.open();
        Display display = parent.getDisplay();
        while (!akt.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
}