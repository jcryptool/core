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

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.jcryptool.visual.zeroknowledge.ModNCalculator;

/**
 * SelectionListener für den PrimeGenerator. Wenn der Button "Primzahlen übernehmen" betätigt wurde,
 * werden die Eingaben von p und q eingelesen. Wenn p und q eingegeben wurden, wird n in den
 * Modellen gesetzt. Ansonsten wird eine Fehlermeldung ausgegeben.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class PrimeGeneratorListener extends SelectionAdapter {

    private PrimeGenerator gen;

    private ModNCalculator shamir;

    /**
     * Konstruktor für einen Listener, der die Attribute setzt.
     *
     * @param fiatShamir Fiat-Shamir-Object, das die Modelle enthält
     * @param gen PrimeGenerator, der die Eingabefelder enthält, die verarbeitet werden sollen
     */
    public PrimeGeneratorListener(ModNCalculator fiatShamir, PrimeGenerator gen) {
        this.shamir = fiatShamir;
        this.gen = gen;
    }

    /**
     * Methode, die ausgeführt wird, wenn der Button "Primzahlen übernehmen" betätigt wird. Die
     * Eingaben in den Textfeldern werden eingelesen und an das FiatShamir-Modell weitergeleitet.
     * Wenn eine Eingabe fehlt oder keine Zahl eingegeben worden ist, wird eine Nachricht dazu
     * ausgegeben.
     *
     * @see SelectionAdapter#widgetSelected(SelectionEvent)
     */
    public void widgetSelected(SelectionEvent arg0) {
        String p = gen.getP().getText().trim();
        String q = gen.getQ().getText().trim();
        shamir.removeVerifingItem();
        if (p.length() == 0 && q.length() == 0) {
            gen.setException(Messages.PrimeGeneratorListener_0);
            return;
        } else if (p.length() == 0) {
            gen.setException(Messages.PrimeGeneratorListener_1);
            return;
        } else if (q.length() == 0) {
            gen.setException(Messages.PrimeGeneratorListener_2);
            return;
        }
        try {
            Integer.parseInt(p);
        } catch (NumberFormatException e) {
            gen.setException(Messages.PrimeGeneratorListener_3);
            return;
        }
        try {
            Integer.parseInt(q);
        } catch (NumberFormatException e) {
            gen.setException(Messages.PrimeGeneratorListener_4);
            return;
        }
        BigInteger pBI = new BigInteger(p);
        BigInteger qBI = new BigInteger(q);
        if (!pBI.isProbablePrime(5)) {
            gen.setException(Messages.PrimeGeneratorListener_5);
            return;
        }
        if (!qBI.isProbablePrime(5)) {
            gen.setException(Messages.PrimeGeneratorListener_6);
            return;
        }
        if (pBI.compareTo(BigInteger.ZERO) < 0) {
            gen.setException(Messages.PrimeGeneratorListener_7);
            return;
        } else if (qBI.compareTo(BigInteger.ZERO) < 0) {
            gen.setException(Messages.PrimeGeneratorListener_8);
            return;
        }
        if (shamir.setP(p) && shamir.setQ(q)) {
            gen.removeException();
            shamir.getSecret().setEnabled(true);
            shamir.getSecret().setFocus();
            shamir.disableAllInFlow();
        }
    }
}