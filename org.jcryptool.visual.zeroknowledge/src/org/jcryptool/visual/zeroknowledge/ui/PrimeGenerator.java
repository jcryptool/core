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
    private Text p;
    private Text q;
    private Button take;
    private Button secret;

    /**
     * Konstruktor für die Group, die für die Erstellung von zwei Primzahlen zuständig ist
     *
     * @param shamir Objekt, das das Modell zu dieser View enthält
     * @param parent Parent zu der graphische n Oberfläche
     */
    public PrimeGenerator(final ModNCalculator shamir, Composite parent) {
        group = new Group(parent, 0);
        group.setText(Messages.PrimeGenerator_0);

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        gridLayout.makeColumnsEqualWidth = false;
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = SWT.FILL;

        group.setLayout(gridLayout);
        group.setLayoutData(gridData);
        group.setVisible(true);

        // Eingabefeld für die Primzahl p
        Label l = new Label(group, 0);
        l.setText(Messages.PrimeGenerator_1);
        l.setBounds(10, 20, 20, 20);
        p = new Text(group, SWT.SINGLE | SWT.BORDER | SWT.LEFT);
        p.setBounds(30, 20, 150, 20);
        p.setTextLimit(10);
        p.setLayoutData(gridData);

        GridData gridData2 = new GridData();
        gridData2.verticalSpan = 2;
        gridData2.verticalAlignment = SWT.FILL;
        gridData2.horizontalAlignment = SWT.FILL;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.grabExcessVerticalSpace = true;

        n = new GeneralParams(group);
        n.getGroup().setLayoutData(gridData2);

        // Button zum Generieren der Parameter
        gen = new Button(group, SWT.PUSH | SWT.CENTER);
        gen.setText(Messages.PrimeGenerator_2);
        gen.setBounds(400, 20, 170, 20);
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
        gen.setLayoutData(gridData);

        // Eingabefeld fuer die Primzahl q
        l = new Label(group, 0);
        l.setText(Messages.PrimeGenerator_4);
        l.setBounds(10, 45, 20, 20);
        q = new Text(group, SWT.SINGLE | SWT.BORDER | SWT.LEFT);
        q.setBounds(30, 45, 150, 20);
        q.setTextLimit(10);
        q.setLayoutData(gridData);

        secret = new Button(group, SWT.PUSH | SWT.CENTER);
        secret.setText(Messages.PrimeGenerator_5);
        secret.setEnabled(false);
        secret.setLayoutData(gridData);
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
        new Label(group, 0);

        // Button zum Uebernehmen der Parameter
        take = new Button(group, SWT.PUSH | SWT.CENTER);
        take.setText(Messages.PrimeGenerator_6);
        SelectionListener listener = new PrimeGeneratorListener(shamir, this);
        take.addSelectionListener(listener);
        take.setLayoutData(gridData);
        take.setToolTipText(Messages.PrimeGenerator_7);

        // Label zum Anzeigen von Fehlern
        error = new Label(group, 0);
        error.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        error.setText(Messages.PrimeGenerator_8);
        error.setLayoutData(gridData);
        error.setVisible(false);

        // Dummy Label for Layout
        new Label(group, 0);

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
        return p;
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
        return q;
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
            p.setText(tmp.toString());
        } else {
            p.setText(""); //$NON-NLS-1$
            shamir.getSecret().setEnabled(false);
        }
        if (!(tmp = shamir.getModell().getQ()).equals(BigInteger.ZERO)) {
            q.setText(tmp.toString());
        } else {
            q.setText(""); //$NON-NLS-1$
            shamir.getSecret().setEnabled(false);
        }
        n.update(shamir);
    }
}
