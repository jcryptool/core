// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool team and contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.logic;

import java.util.List;

import org.jcryptool.games.divide.views.Messages;

public class LowestStrategy implements IStrategy {

    // instance vars
    private String name;

    // constructor
    public LowestStrategy() {
        super();
        this.name = Messages.DivideView_26;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int chooseNumber(List<Integer> n) {
        if (n.size() > 1) {
            return n.get(1);
        } else {
            return n.get(0);
        }
    }
}
