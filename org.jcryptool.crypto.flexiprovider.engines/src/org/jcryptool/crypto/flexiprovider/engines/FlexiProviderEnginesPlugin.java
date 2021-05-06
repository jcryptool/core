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
package org.jcryptool.crypto.flexiprovider.engines;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.SecureRandom;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;

/**
 * The activator class controls the plug-in life cycle
 */
public class FlexiProviderEnginesPlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jcryptool.crypto.flexiprovider.engines"; //$NON-NLS-1$
	
	// The shared instance
	private static FlexiProviderEnginesPlugin plugin;
	
	/**
	 * The constructor
	 */
	public FlexiProviderEnginesPlugin() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
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
	public static FlexiProviderEnginesPlugin getDefault() {
		return plugin;
	}
	
	private static SecureRandom sha1prng;

	public static SecureRandom getSecureRandom() {
		try {
			if (sha1prng == null) sha1prng = Registry.getSecureRandom("SHA1PRNG"); //$NON-NLS-1$
			return sha1prng;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
