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

/**
 * Interface für alle Klassen, die vom Repeat-Dialog aus aufgerufen ein Protokoll mehrmals
 * durchführen sollen
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public interface Funcs {

    /**
     * Methode, die das jeweilige Protokoll amount-mal durchführt und die Anzahl der richtigen
     * Versuche zurückgibt
     * 
     * @param amount Anzahl der gewünschten Wiederholungen des Protokolls
     * @return Anzahl der richtig beanworteten Anfragen
     */
    public int protokoll(int amount);

    /**
     * Methode zum Setzen, ob die Person, die antwortet, das Geheimnis kennt oder nicht.
     * 
     * @param secretKnown bestimmt, ob das Geheimnis bekannt ist oder nicht
     */
    public void setSecretKnown(boolean secretKnown);
}
