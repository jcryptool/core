/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.interfaces;

public class FriedmanData {
    private int index;
    private double value;

    public FriedmanData(final int index, final double value) {
        this.index = index;
        this.value = value;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }
}
