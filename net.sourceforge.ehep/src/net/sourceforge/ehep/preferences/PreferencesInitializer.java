/*==========================================================================
 * 
 * PreferencesInitializer.java
 * 
 * $Author: randallco $
 * $Revision: 1.1 $
 * $Date: 2004/10/16 21:23:22 $
 * $Name:  $
 * 
 * Created on 9-Jul-2004
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import net.sourceforge.ehep.EhepPlugin;

/**
 * @author Marcel Palko
 * @author randallco@users.sourceforge.net
 */
public class PreferencesInitializer extends AbstractPreferenceInitializer {

	/**
	 * Default constructor
	 */
	public PreferencesInitializer() {
		super();
	}

	/**
	 * Initializes the default preferences settings for this plug-in.
	 * <p>
	 * This method is called sometime after the preference store for this
	 * plug-in is created. Default values are never stored in preference
	 * stores; they must be filled in each time. This method provides the
	 * opportunity to initialize the default values.
	 * </p>
	 * <p>
	 * The default implementation of this method does nothing. A subclass that needs
	 * to set default values for its preferences must reimplement this method.
	 * Default values set at a later point will override any default override
	 * settings supplied from outside the plug-in (product configuration or
	 * platform start up).
	 * </p>
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		EhepPlugin plugin = EhepPlugin.getDefault();
		if (plugin == null) return;
		IPreferenceStore store = plugin.getPreferenceStore();
		PreferencesPage.initializeFontDefaults(store);
		PreferencesPage.initializeColorDefaults(store);
	}
}
