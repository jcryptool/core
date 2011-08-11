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

/**
 * Klasse zum Speichern der Parameter, die Bob kennt
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class MBob extends MPerson {

    private int toVerify;

    /**
     * setzt alle Werte von Bob zurück
     * 
     * @see MPerson#reset()
     */
    public void reset() {
        toVerify = -1;
        super.reset();
    }

    /**
     * setzt die Zahl, die verifiziert werden soll
     * 
     * @param y Zahl, die verifiziert werden soll
     */
    public void setToVerify(int y) {
        toVerify = y;
    }

    /**
     * überprüft, ob die empfangenen Zahlen zusammen passen
     * 
     * @return true, wenn die empfangenen Zahlen zusammen passen
     */
    public boolean verify() {
        return toVerify == raumwahl;
    }
}