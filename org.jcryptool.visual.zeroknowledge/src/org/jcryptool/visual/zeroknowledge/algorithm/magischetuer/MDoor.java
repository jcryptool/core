// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.algorithm.magischetuer;

import java.util.Random;

/**
 * Methode zur Repräsentation der Tür, die durch einen Code gesichert ist.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class MDoor {

    private int[] code;

    private Random rand = new Random();

    private int raum1;

    private int raum2;

    private boolean secret;

    /**
     * erstellt eine Tür zwischen den Räumen one und two
     * 
     * @param one einer der Räume, die an die Tür münden
     * @param two der andere Raum, der an die Tür mündet
     */
    public MDoor(int one, int two) {
        this(one, two, false, 0);
    }

    /**
     * Erstellt eine Tuer zwischen den Räumen one und two. Wenn die Tür durch einen Code gesichert
     * sein soll, wird dieser Code mit der übregebenen Länge generiert
     * 
     * @param one einer der Räume, die an die Tuer münden
     * @param two der andere Raum, der an die Tuer mündet
     * @param secret gibt an, ob die Tür gesichert sein soll
     * @param length gibt die Länge des Codes an
     */
    public MDoor(int one, int two, boolean secret, int length) {
        raum1 = one;
        raum2 = two;
        this.secret = secret;
        if (!secret)
            length = 0;
        code = new int[length];
        createCode();
    }

    /**
     * kreiert einen neuen Code für die Tuer
     */
    public void createCode() {
        for (int i = 0; i < code.length; i++) {
            code[i] = rand.nextInt(10);
        }
    }

    /**
     * Methode zum Erhalten des Codes
     * 
     * @return Code der Tür
     */
    public int[] getCode() {
        return code;
    }

    /**
     * Methode zum Erhalten des Raumes auf der anderen Seite des übergebenen Raumes
     * 
     * @param now Raum, in dem man sich jetzt befindet
     * @return Raum auf der anderen Seite der Tuer
     */
    public int getOtherRoom(int now) {
        return raum1 == now ? raum2 : raum1;
    }

    /**
     * Methode, die zurückgibt, ob die Tuer gesichert ist
     * 
     * @return true, wenn die Tür gesichert ist
     */
    public boolean isSecret() {
        return secret;
    }

    /**
     * überprüft, ob der übergebene Code mit dem Code der Tür übereinstimmt
     * 
     * @param givenCode Code, der mit dem Code der Tür verglichen werden soll
     * @return true, wenn die Codes gleich sind, sonst falsch
     */
    public boolean verifyCode(int[] givenCode) {
        if (givenCode == code)
            return true;
        if (givenCode.length != code.length)
            return false;
        for (int i = 0; i < code.length; i++) {
            if (code[i] != givenCode[i])
                return false;
        }
        return true;
    }
}
