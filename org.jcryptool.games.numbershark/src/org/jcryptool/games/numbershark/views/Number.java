// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.views;

import java.util.ArrayList;

public class Number {
    private int value;
    private ArrayList<Integer> divisors = new ArrayList<Integer>();
    private boolean isEnabled = true;

    public Number(int n) {
        value = n;
        calcDiv();
    }

    public int getValue() {
        return value;
    }

    private void calcDiv() {
        for (int i = value - 1; i > 0; i--) {
            if (value % i == 0) {
                divisors.add(i);
            }
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public ArrayList<Integer> getDivisors() {
        return divisors;
    }
}
