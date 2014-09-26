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

import org.jcryptool.games.divide.views.Messages;

public class ComputerPlayer implements IPlayer {

    // instance vars
    private String name;
    private IStrategy strategy;
    private boolean isHuman;

    // constructor
    public ComputerPlayer(IStrategy strategy) {
        super();
        isHuman = false;
        this.name = Messages.DivideView_7;
        this.strategy = strategy;
    }

    // methods
    @Override
    public boolean isHuman() {
        return isHuman;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IStrategy getStrategy() {
        return strategy;
    }
}
