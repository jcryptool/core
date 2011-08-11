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
package org.jcryptool.games.numbershark.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jcryptool.games.numbershark.NumberSharkPlugin;

/**
 * @author Markus Schu
 * @author markus_schu@web.de
 * @version 0.4.3, 24.02.2009
 * 
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = NumberSharkPlugin.getDefault().getPreferenceStore();
		store.setDefault("Number Shark.SBI", true);
		store.setDefault("Number Shark.NSGM", "0");
		store.setDefault("Number Shark.NSPM", "2");
		store.setDefault("Number Shark.MAX", "1024");
	}
}
