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

public class HumanPlayer implements IPlayer {

    // instance vars
    private String name;
    private boolean isHuman;

    // constructor
    public HumanPlayer(String name) {
        super();
        this.name = name;
        isHuman = true;
    }

    // methods
    @Override
    public int chooseNumber(List<Integer> n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isHuman() {
        return isHuman;
    }
}
