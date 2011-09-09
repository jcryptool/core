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
package org.jcryptool.crypto.classic.alphabets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Vector;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.crypto.classic.alphabets.tools.AlphabetPersistence;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * The activator class controls the plug-in life cycle
 */
public class AlphabetsPlugin extends Plugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "org.jcryptool.crypto.classic.alphabets";

	// The shared instance
	private static AlphabetsPlugin plugin;

	/** The preferences store */
	private IEclipsePreferences preferences;

	private static final String ALPHABET_DEFAULTS = "alphabet-defaults";
	private static final String FILTER_CHARS = "filter-chars";

	private boolean filterChars;

	public void setFilterChars(boolean value) {
		filterChars = value;
	}

	public boolean getFilterChars() {
		loadPreferences();
		return filterChars;
	}

	public boolean getFilterCharsDefault() {
		return true;
	}

	public void savePreferences() {
		preferences = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
		Preferences alphabets = preferences.node(ALPHABET_DEFAULTS);
		alphabets.putBoolean(FILTER_CHARS, filterChars);
		try {
			alphabets.flush();
		} catch (BackingStoreException ex) {
            LogUtil.logError(ex);
		}
	}

	public void loadPreferences() {
		preferences = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
		Preferences alphabets = preferences.node(ALPHABET_DEFAULTS);
		filterChars = alphabets.getBoolean(FILTER_CHARS, true);
	}

	public void initializeAlphabetDefaults() {
		preferences = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
		Preferences alphabets = preferences.node(ALPHABET_DEFAULTS);
		alphabets.putBoolean(FILTER_CHARS, true);
		try {
			alphabets.flush();
		} catch (BackingStoreException ex) {
            LogUtil.logError(ex);
		}
	}

	/**
	 * The constructor
	 */
	public AlphabetsPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static AlphabetsPlugin getDefault() {
		return plugin;
	}

	public void performStoreAlphabets(Vector<Alphabet> alphabets, String path) throws IOException {
		AlphabetPersistence.saveAlphabetsToXML(alphabets, new OutputStreamWriter(new FileOutputStream(path),
		        Charset.forName(IConstants.UTF8_ENCODING)));
	}

	public static String getFolderPath(){
		return getDefault().getBundle().getLocation();
	}

}
