//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.actions.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jcryptool.actions.ui.ActionsUIPlugin;

/**
 * Preference initializer for <b>Actions view</b> preferences.
 *
 * @author Dominik Schadow
 * @version 0.5
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = ActionsUIPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_STORE_PASSWORDS, true);
        store.setDefault(PreferenceConstants.P_SHOW_FILENAMES, true);
    }
}
