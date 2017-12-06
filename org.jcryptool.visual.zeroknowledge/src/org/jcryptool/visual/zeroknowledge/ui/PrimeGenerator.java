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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.zeroknowledge.ModNCalculator;

/**
 * Graphische Benutzeroberfläche zur Erstellung der Primzahlen p und q. Man kann p und q direkt
 * eingeben oder vom Programm generieren lassen.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class PrimeGenerator {

    private Label error;
    private Button gen;
    private Group group;
    private GeneralParams n;
    private Text pPrime;
    private Text qPrime;
    private Button take;
    private Button secret;

    /**
     * Konstruktor für die Group, die für die Erstellung von zwei Primzahlen zuständig ist
     *
     * @param shamir Objekt, das das Modell zu dieser View enthält
     * @param parent Parent zu der graphische n Oberfläche
     */
    public PrimeGenerator(final ModNCalculator shamir, Composite parent) {

    	group = new Group(parent, SWT.NONE);
        group.setText(Messages.PrimeGenerator_0);
        group.setLayout(new GridLayout(4, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        group.setVisible(true);

        // Eingabefeld für die Primzahl p
        Label labelP = new Label(group, SWT.NONE);
        labelP.setText(Messages.PrimeGenerator_1);
        labelP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        pPrime = new Text(group, SWT.BORDER);
        pPrime.setTextLimit(10);
        pPrime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Warum ist die Group nur ca. 1,5 Zeilen hoch. Sie müsste genauso hoch sein wie 
        // die Textfelder für p und q.
        n = new GeneralParams(group);
        n.getGroup().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));

        // Button zum Generieren der Parameter
        gen = new Button(group, SWT.PUSH);
        gen.setText(Messages.PrimeGenerator_2);
        gen.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "Generieren"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * öffnet einen Dialog zum Generieren der Primzahlen p und q
             */
            public void widgetSelected(SelectionEvent arg0) {
                new Generator(shamir, group.getShell());
                secret.setFocus();
            }
        });
        gen.setToolTipText(Messages.PrimeGenerator_3);
        gen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Eingabefeld fuer die Primzahl q
        Label labelQ = new Label(group, SWT.NONE);
        labelQ.setText(Messages.PrimeGenerator_4);
        labelQ.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        qPrime = new Text(group, SWT.BORDER);
        qPrime.setTextLimit(10);
        qPrime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        secret = new Button(group, SWT.PUSH);
        secret.setText(Messages.PrimeGenerator_5);
        secret.setEnabled(false);
        secret.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        secret.addSelectionListener(
        /**
         * Selection-Listener, der auf Events vom "Geheimnis erstellen"-Button achtet
         */
        new SelectionAdapter() {
            /**
             * Erstellt im Alice das Geheimnis und reicht v an Bob und Carol weiter. Ermöglicht den
             * ersten Schritt des Protokolls
             */
            public void widgetSelected(SelectionEvent arg0) {
                if (!shamir.getN().equals(BigInteger.ZERO)) {
                    shamir.setSecret();
                    ((Button) arg0.getSource()).setEnabled(false);
                }
            }
        });

        // Dummy Label for Layout
        new Label(group, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));;

        // Button zum Uebernehmen der Parameter
        take = new Button(group, SWT.PUSH);
        take.setText(Messages.PrimeGenerator_6);
        SelectionListener listener = new PrimeGeneratorListener(shamir, this);
        take.addSelectionListener(listener);
        take.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        take.setToolTipText(Messages.PrimeGenerator_7);

        // Label zum Anzeigen von Fehlern
        error = new Label(group, SWT.NONE);
        error.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        error.setText(Messages.PrimeGenerator_8);
        error.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        error.setVisible(false);

    }

    /**
     * Methode zum Erhalten des Group-Objektes, das die graphischen Komponenten enthält
     *
     * @return Group-Objekt einer Instanz dieser Klasse
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Methode zum Erhalten des formatierten Textfeldes, das die Primzahl p enthält
     *
     * @return das formatierte Textfeld, das die Primzahl p enthält
     */
    public Text getP() {
        return pPrime;
    }

    public Button getSecret() {
        return secret;
    }

    /**
     * Methode zum Erhalten des formatierten Textfeldes, das die Primzahl q enthält
     *
     * @return das formatierte Textfeld, das die Primzahl q enthält
     */
    public Text getQ() {
        return qPrime;
    }

    /**
     * "löscht" die Nachricht
     */
    public void removeException() {
        error.setVisible(false);
    }

    /**
     * Zeigt eine Nachricht an, tendenziell eine Fehlermeldung bei falscher Benutzereingabe
     *
     * @param message
     */
    public void setException(String message) {
        error.setText(message);
        error.setVisible(true);
    }

    /**
     * Methode zum Updaten des Panels
     *
     * @param shamir Object, das die Modelle enthält
     */
    public void update(ModNCalculator shamir) {
        BigInteger tmp;
        if (!(tmp = shamir.getModell().getP()).equals(BigInteger.ZERO)) {
            pPrime.setText(tmp.toString());
        } else {
            pPrime.setText(""); //$NON-NLS-1$
            shamir.getSecret().setEnabled(false);
        }
        if (!(tmp = shamir.getModell().getQ()).equals(BigInteger.ZERO)) {
            qPrime.setText(tmp.toString());
        } else {
            qPrime.setText(""); //$NON-NLS-1$
            shamir.getSecret().setEnabled(false);
        }
        n.update(shamir);
    }
}
