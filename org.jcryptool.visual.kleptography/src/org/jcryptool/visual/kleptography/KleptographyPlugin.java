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
package org.jcryptool.visual.kleptography;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Patrick Vacek
 * @version 0.1
 */
public class KleptographyPlugin extends AbstractUIPlugin {

	/**
	 * The plug-in ID
	 */
	public static final String PLUGIN_ID = "org.jcryptool.visual.kleptography"; //$NON-NLS-1$

	/**
	 * The shared instance
	 */
	private static KleptographyPlugin plugin;
	
	/**
	 * The constructor
	 */
	public KleptographyPlugin() {
		plugin = this;
	}

	/**
	 * The method uses the superclass method which refreshes the plug-in actions.
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
//		plugin = this;
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
	 * Returns the shared instance
	 * @return The shared instance
	 */
	public static KleptographyPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 * @param path The path
	 * @return The image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
