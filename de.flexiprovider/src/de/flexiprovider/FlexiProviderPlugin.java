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
package de.flexiprovider;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Plugin;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FlexiProviderPlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.flexiprovider";

	// The shared instance
	private static FlexiProviderPlugin plugin;

	/**
	 * The constructor
	 */
	public FlexiProviderPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static FlexiProviderPlugin getDefault() {
		return plugin;
	}

	/**
	 * Reads the algorithms.xml file and creates a document object.
	 * 
	 * @return	A jdom document
	 */
	public static Document getAlgorithmsXML() {
		URL xmlFile = FlexiProviderPlugin.getDefault().getBundle().getEntry("/xml/algorithms.xml");
		SAXBuilder builder = new SAXBuilder();
		try {
			return builder.build(xmlFile);
		} catch (JDOMException e) {
            LogUtil.logError(PLUGIN_ID, e);
		} catch (IOException e) {
            LogUtil.logError(PLUGIN_ID, e);
		}
		return null;
	}

}
