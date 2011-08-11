//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
/**
 *
 */
package org.jcryptool.editor.text.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jcryptool.editor.text.JCTTextEditorPlugin;

/**
 * Preference initializer for <b>JCrypTool Texteditor</b> preferences.
 *
 * @author Dominik Schadow
 * @version 0.9.2
 */
public class PreferencesInitializer extends AbstractPreferenceInitializer {
	@Override
	public void initializeDefaultPreferences() {
	    IPreferenceStore store = JCTTextEditorPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.FONT_FAMILY, "");
        store.setDefault(PreferenceConstants.FONT_SIZE, 10);
	}
}
