// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
/**
 * 
 */
package org.jcryptool.core.operations.providers.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * Initializes the default preferences in the preferences store.
 * 
 * @author t-kern
 * 
 */
public class ProvidersPreferencesInitializer extends AbstractPreferenceInitializer {

    /**
     * No-args consturctor.
     */
    public ProvidersPreferencesInitializer() {
        super();
    }

    /**
     * Initialize with default settings.<br>
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        OperationsPlugin plugin = OperationsPlugin.getDefault();
        if (plugin == null)
            return;
        // plugin.initializeDefaultProvider();
    }

}
