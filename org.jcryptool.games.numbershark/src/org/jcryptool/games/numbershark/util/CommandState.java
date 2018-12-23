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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**
 * 
 * @author Daniel Löffler
 * @version 0.9.5
 */
public class CommandState extends AbstractSourceProvider {

    public static String stateVal(State state) {
        String str = "";
        switch (state) {
        case UNDO_ENABLED:
            str = "UNDO_ENABLED";
            break;
        case UNDO_DISABLED:
            str = "UNDO_DISABLED";
            break;
        case REDO_ENABLED:
            str = "REDO_ENABLED";
            break;
        case REDO_DISABLED:
            str = "REDO_DISABLED";
            break;
        case SHARKMEAL_ENABLED:
            str = "SHARKMEAL_ENABLED";
            break;
        case SHARKMEAL_DISABLED:
            str = "SHARKMEAL_DISABLED";
            break;
        case HINT_ENABLED:
            str = "HINT_ENABLED";
            break;
        case HINT_DISABLED:
            str = "HINT_DISABLED";
            break;
        case DISABLED:
            str = "DISABLED";
            break;
        case ENABLED:
            str = "ENABLED";
            break;
        }
        return str;
    }

    public static String variableVal(Variable variable) {
        String str = "";
        switch (variable) {
        case UNDO_STATE:
            str = "org.jcryptool.games.numbershark.commands.undoState";
            break;
        case REDO_STATE:
            str = "org.jcryptool.games.numbershark.commands.redoState";
            break;
        case SHARKMEAL_STATE:
            str = "org.jcryptool.games.numbershark.commands.sharkMealState";
            break;
        case HINT_STATE:
            str = "org.jcryptool.games.numbershark.commands.hintState";
            break;
        }
        return str;
    }

    public enum Variable {
        UNDO_STATE, REDO_STATE, SHARKMEAL_STATE, HINT_STATE
    }

    public enum State {
        UNDO_ENABLED, UNDO_DISABLED, REDO_ENABLED, REDO_DISABLED, SHARKMEAL_ENABLED, SHARKMEAL_DISABLED, HINT_ENABLED, HINT_DISABLED, DISABLED, ENABLED
    }

    private State curState = State.DISABLED;

    @Override
	public void dispose() {

    }

    @Override
    public Map<String, String> getCurrentState() {

        Map<String, String> map = new HashMap<String, String>(1);
        if (curState == State.UNDO_ENABLED)
            map.put(variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_ENABLED));
        else if (curState == State.UNDO_DISABLED)
            map.put(variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_DISABLED));
        else if (curState == State.REDO_ENABLED)
            map.put(variableVal(Variable.REDO_STATE), stateVal(State.REDO_ENABLED));
        else if (curState == State.REDO_DISABLED)
            map.put(variableVal(Variable.REDO_STATE), stateVal(State.REDO_DISABLED));
        else if (curState == State.SHARKMEAL_ENABLED)
            map.put(variableVal(Variable.SHARKMEAL_STATE), stateVal(State.SHARKMEAL_ENABLED));
        else if (curState == State.SHARKMEAL_DISABLED)
            map.put(variableVal(Variable.SHARKMEAL_STATE), stateVal(State.SHARKMEAL_DISABLED));
        else if (curState == State.HINT_ENABLED)
            map.put(variableVal(Variable.HINT_STATE), stateVal(State.HINT_ENABLED));
        else if (curState == State.HINT_DISABLED)
            map.put(variableVal(Variable.HINT_STATE), stateVal(State.HINT_DISABLED));
        else if (curState == State.DISABLED) {
            map.put(variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_DISABLED));
            map.put(variableVal(Variable.REDO_STATE), stateVal(State.REDO_DISABLED));
        } else if (curState == State.ENABLED) {
            map.put(variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_ENABLED));
            map.put(variableVal(Variable.REDO_STATE), stateVal(State.REDO_ENABLED));
        }
        return map;
    }

    @Override
    public String[] getProvidedSourceNames() {
        return new String[] { variableVal(Variable.UNDO_STATE), variableVal(Variable.REDO_STATE),
                variableVal(Variable.SHARKMEAL_STATE), variableVal(Variable.HINT_STATE) };
    }

    public void setUndoEnabled() {
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_ENABLED));
    }

    public void setUndoDisabled() {
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.UNDO_STATE), stateVal(State.UNDO_DISABLED));
    }

    public void setRedoEnabled() {
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.REDO_STATE), stateVal(State.REDO_ENABLED));
    }

    public void setRedoDisabled() {
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.REDO_STATE), stateVal(State.REDO_DISABLED));
    }

    public void setSharkMealEnabled() {
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.SHARKMEAL_STATE), stateVal(State.SHARKMEAL_ENABLED));
    }

    public void setSharkMealDisabled() {
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.SHARKMEAL_STATE), stateVal(State.SHARKMEAL_DISABLED));
    }

    public void setHintEnabled() {
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.HINT_STATE), stateVal(State.HINT_ENABLED));
    }

    public void setHintDisabled() {
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.HINT_STATE), stateVal(State.HINT_DISABLED));
    }

    public void setDisabled() {
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.UNDO_STATE), stateVal(State.DISABLED));
        fireSourceChanged(ISources.WORKBENCH, variableVal(Variable.REDO_STATE), stateVal(State.DISABLED));
    }

}
