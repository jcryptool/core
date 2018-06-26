//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.numbershark.util;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;
import org.jcryptool.games.numbershark.util.CommandState.State;
import org.jcryptool.games.numbershark.util.CommandState.Variable;

/**
 * The class enables and disable commands
 * 
 * @author Daniel Loeffler
 * @version 0.9.5
 */
public class CommandStateChanger {

    public CommandStateChanger() {
    }

    public void chageCommandState(Variable stateVariabe, State state) {

        ISourceProviderService sourceProviderService = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getService(ISourceProviderService.class);

        CommandState commandStateService = (CommandState) sourceProviderService.getSourceProvider(CommandState
                .variableVal(stateVariabe));

        if (state.equals(State.UNDO_ENABLED)) {
            commandStateService.setUndoEnabled();
        } else if (state.equals(State.REDO_ENABLED)) {
            commandStateService.setRedoEnabled();
        } else if (state.equals(State.UNDO_DISABLED)) {
            commandStateService.setUndoDisabled();
        } else if (state.equals(State.REDO_DISABLED)) {
            commandStateService.setRedoDisabled();
        } else if (state.equals(State.ENABLED)) {
            commandStateService.setUndoEnabled();
            commandStateService.setRedoEnabled();
        } else if (state.equals(State.DISABLED)) {
            commandStateService.setUndoDisabled();
            commandStateService.setRedoDisabled();
        } else if (state.equals(State.SHARKMEAL_DISABLED)) {
            commandStateService.setSharkMealDisabled();
        } else if (state.equals(State.SHARKMEAL_ENABLED)) {
            commandStateService.setSharkMealEnabled();
        } else if (state.equals(State.HINT_DISABLED)) {
            commandStateService.setHintDisabled();
        } else if (state.equals(State.HINT_ENABLED)) {
            commandStateService.setHintEnabled();
        }
    }
}
