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
package org.jcryptool.crypto.classic.playfair;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The Activator class controls the plug-in life cycle
 *
 * @author SLeischnig
 * @version 0.1
 */
public class PlayfairPlugin extends AbstractUIPlugin {
	/**
	 * The plug-in ID
	 */
	public static final String PLUGIN_ID = "org.jcryptool.crypto.classic.playfair";

	/**
	 * The shared instance
	 */
	private static PlayfairPlugin plugin;

	/**
	 * The constructor
	 */
	public PlayfairPlugin() {
		plugin = this;
	}

	/**
	 * Returns this plug-in's log manager.
	 *
	 * @return	This plug-in's log manager
	 */

	/**
	 * The method uses the superclass method which refreshes the plug-in actions.
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * The method uses the superclass method which saves this plug-in's
	 * preference and dialog stores and shuts down
     * its image registry (if they are in use).
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance.
	 */
	public static PlayfairPlugin getDefault() {
		return plugin;
	}

}
