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

import java.util.List;

public class GameMachineEvent {

    // instance vars
    private GameMachineNotifyEvent eventType;
    private List<GameState> state;

    // constructor
    public GameMachineEvent(GameMachineNotifyEvent eventType, List<GameState> state) {
        super();
        this.eventType = eventType;
        this.state = state;
    }

    // methods
    public GameMachineNotifyEvent getEventType() {
        return eventType;
    }

    public List<GameState> getStateList() {
        return state;
    }
}
