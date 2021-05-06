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
package org.jcryptool.fileexplorer.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.fileexplorer.FileExplorerPlugin;

/**
 * Preference initializer for <b>File Explorer</b> view preferences.
 *
 * @author Dominik Schadow
 * @version 0.5
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = FileExplorerPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_OPEN_SOURCE, true);
        store.setDefault(PreferenceConstants.P_OPEN_TARGET, true);
        store.setDefault(PreferenceConstants.P_START_DIRECTORY, DirectoryService.getUserHomeDir());
    }
}
