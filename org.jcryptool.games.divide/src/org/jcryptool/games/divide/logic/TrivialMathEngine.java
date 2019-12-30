// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.logic;

import java.util.ArrayList;
import java.util.List;

public class TrivialMathEngine implements IMathEngine {

    // instance vars

    // constructor
    public TrivialMathEngine() {
        super();
    }

    // methods
    @Override
    public List<Integer> getDivider(int x) {
        List<Integer> divider = new ArrayList<Integer>();
        for (int i = 1; i <= x; i++) {
            if (x % i == 0) {
                divider.add(i);
            }
        }
        return divider;
    }

    @Override
    public List<Integer> dropMultiples(List<Integer> oldList, int x) {
        List<Integer> updatedList = new ArrayList<Integer>();
        for (int i = 0; i < oldList.size(); i++) {
            if (oldList.get(i) % x != 0) {
                updatedList.add(oldList.get(i));
            }
        }
        return updatedList;
    }
}
