//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.viterbi;

import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * The activator class controls the plug-in life cycle
 */
public class ViterbiPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jcryptool.visual.viterbi";

    /**
     * The shared instance
     */
    private static ViterbiPlugin plugin;

    /**
     * The constructor
     */
    public ViterbiPlugin() {
        plugin = this;
    }


    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static ViterbiPlugin getDefault() {
        return plugin;
    }
}
