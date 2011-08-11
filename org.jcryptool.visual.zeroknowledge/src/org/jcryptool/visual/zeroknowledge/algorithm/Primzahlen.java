// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm;

import java.math.BigInteger;
import java.util.Random;

/**
 * Klasse, die eine statische Methode zum Erstellen einer Primzahl bereitstellt
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Primzahlen {

    /**
     * Methode zum Generieren einer Primzahl im Bereich ]2^obergrenze, 2^untergrenze[
     * 
     * @param untergrenze dualer Logarithmus der unteren Intervallgrenze
     * @param obergrenze dualer Logarithmus der oberen Intervallgrenze
     * @return BigInteger, dessen Wert eine Primzahl im Bereich ]2^obergrenze, 2^untergrenze[ ist
     */
    public static BigInteger primzahl(int untergrenze, int obergrenze) {
        Random r = new Random();

        // Fehlerabfrage
        if (untergrenze >= obergrenze) {
            throw new IllegalArgumentException("Error.UPPER_LOWER"); //$NON-NLS-1$
        }

        BigInteger tmp;
        // Anzahl der Bits, die die Primzahl haben soll
        int anzahl = untergrenze + 1 + new Random().nextInt(obergrenze - untergrenze);

        // Primzahlgenerierung
        tmp = BigInteger.probablePrime(anzahl, r);
        return tmp;
    }
}
