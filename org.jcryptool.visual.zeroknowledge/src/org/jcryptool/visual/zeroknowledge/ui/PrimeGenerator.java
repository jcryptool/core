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

import java.math.BigInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
 * @version 1.0.0
 */
public class PrimeGenerator {
	
	private PrimeGenerator object;
    private Label error;
    private Button gen;
    private Group group;
    private Text pPrime;
    private Text qPrime;
	private Text textN;

    /**
     * Konstruktor für die Group, die für die Erstellung von zwei Primzahlen zuständig ist
     *
     * @param shamir Objekt, das das Modell zu dieser View enthält
     * @param parent Parent zu der graphische n Oberfläche
     */
    public PrimeGenerator(final ModNCalculator shamir, Composite parent) {
    	
    	object = this;

    	group = new Group(parent, SWT.NONE);
        group.setText(Messages.PrimeGenerator_0);
        group.setLayout(new GridLayout(3, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        group.setVisible(true);

        // Eingabefeld für die Primzahl p
        Label labelP = new Label(group, SWT.NONE);
        labelP.setText(Messages.PrimeGenerator_1);
        labelP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        pPrime = new Text(group, SWT.BORDER);
        pPrime.setTextLimit(10);
        pPrime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        pPrime.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// Reset the plugin if the user changes a prime.
				shamir.resetNotSecret();
				
				// Test if input is a number, a prime and positive.
				try {
					BigInteger input = new BigInteger(pPrime.getText());
					if (input.isProbablePrime(100)) {
						if (input.compareTo(BigInteger.ZERO) == 1) {
							shamir.setP(input);
							removeException();
							return;
						} else {
							setException(Messages.PrimeGeneratorListener_7);
						}
					} else {
						setException(Messages.PrimeGeneratorListener_5);
					}
				} catch (NumberFormatException ex) {
					setException(Messages.PrimeGeneratorListener_3);
				}
				
				// If the user entered shit set N to "-"
				textN.setText("-");
				shamir.setP(BigInteger.ZERO);
			}
		});

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
            @Override
			public void widgetSelected(SelectionEvent arg0) {
                new Generator(object, shamir, group.getShell());
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
        qPrime.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// Reset the plugin if the user changes a prime.
				shamir.resetNotSecret();
				
				// Test if input is a number, a prime and positive.
				try {
					BigInteger input = new BigInteger(qPrime.getText());
					if (input.isProbablePrime(100)) {
						if (input.compareTo(BigInteger.ZERO) == 1) {
							shamir.setQ(input);
							removeException();
							return;
						} else {
							setException(Messages.PrimeGeneratorListener_8);
						}
					} else {
						setException(Messages.PrimeGeneratorListener_6);
					}
				} catch (NumberFormatException ex) {
					setException(Messages.PrimeGeneratorListener_4);
				}
				
				// If the user entered shit set N to "-"
				textN.setText("-");
				shamir.setQ(BigInteger.ZERO);
			}
		});


        // Label zum Anzeigen von Fehlern
        error = new Label(group, SWT.NONE);
        error.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        error.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        error.setVisible(false);
        
        Label labelN = new Label(group, SWT.NONE);
        labelN.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        labelN.setText("n :");
        
        textN = new Text(group, SWT.BORDER);
        textN.setEditable(false);
        textN.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textN.setText("-");

        // Dummy Label for Layout
        new Label(group, SWT.NONE);

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
    
    public void setN(String n) {
    	textN.setText(n);
    }
    
    public void setP(String p) {
    	pPrime.setText(p);
    }
    
    public void setQ(String q) {
    	qPrime.setText(q);
    }

}
