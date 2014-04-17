// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool team and contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.sourceProviders;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class SaveStateSourceProvider extends AbstractSourceProvider {

    public static final String SAVE_COMMAND_STATE = "org.jcryptool.games.divide.save";

    // instance vars
    private static final String ENABLED = "enabled";
    private static final String DISABLED = "disabled";
    private boolean isEnabled;

    // constructor
    public SaveStateSourceProvider() {
        super();
        isEnabled = false;
    }

    // methods
    @Override
    public void dispose() {

    }

    @Override
    public Map<String, String> getCurrentState() {
        Map<String, String> currentStateMap = new HashMap<String, String>(1);
        String currentState = isEnabled ? ENABLED : DISABLED;
        currentStateMap.put(SAVE_COMMAND_STATE, currentState);

        return currentStateMap;
    }

    @Override
    public String[] getProvidedSourceNames() {
        return new String[] { SAVE_COMMAND_STATE };
    }

    public void setState(boolean isEnabled) {
        this.isEnabled = isEnabled;
        String currentState = isEnabled ? ENABLED : DISABLED;
        fireSourceChanged(ISources.WORKBENCH, SAVE_COMMAND_STATE, currentState);
    }
}
