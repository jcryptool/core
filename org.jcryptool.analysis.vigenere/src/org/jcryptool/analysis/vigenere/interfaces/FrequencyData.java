/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.interfaces;

public class FrequencyData {
    private int ctally;
    private int cchar;

    public FrequencyData(final int character, final int count) {
        this.ctally = count;
        this.cchar = character;
    }

    /**
     * @return the ctally
     */
    public int getCount() {
        return ctally;
    }

    /**
     * @return the cchar
     */
    public int getCharacter() {
        return cchar;
    }

    public String getCharacterString() {
        return String.valueOf((char) cchar);
    }
}
