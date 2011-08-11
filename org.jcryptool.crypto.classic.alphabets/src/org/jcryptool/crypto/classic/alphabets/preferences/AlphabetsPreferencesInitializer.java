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
package org.jcryptool.crypto.classic.alphabets.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.jcryptool.crypto.classic.alphabets.AlphabetsPlugin;

public class AlphabetsPreferencesInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		AlphabetsPlugin plugin = AlphabetsPlugin.getDefault();
		if (plugin == null) return;
		plugin.initializeAlphabetDefaults();
	}

}
