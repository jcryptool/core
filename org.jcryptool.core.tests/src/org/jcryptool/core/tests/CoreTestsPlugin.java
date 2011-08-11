// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.tests;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * <p>The activator class of the JCrypTool Core Test plug-in. To set up this
 * test plug-in launch it as <i>JUnit Test Plug-in</i>. Open the created run
 * menu entry, switch to the <i>Main</i> tab and activate the <i>Run a product</i>
 * option. Select the <i>org.jcryptool.core.product</i> in the drop down menu and launch
 * again. Make sure the JUnit workspace is cleared automatically before every run.</p>
 *
 * <p>Additionally this test suite requires to launch an English JCrypTool (-nl en),
 * otherwise all language dependent tests (like view titles) will fail.</p>
 *
 * @author Dominik Schadow
 * @version 0.9.2
 */
public class CoreTestsPlugin extends AbstractUIPlugin {
    // The plug-in ID
    public static final String PLUGIN_ID = "org.jcryptool.core.tests";

    // The shared instance
    private static CoreTestsPlugin plugin;

    /**
     * The constructor
     */
    public CoreTestsPlugin() {
        plugin = this;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
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
    public static CoreTestsPlugin getDefault() {
        return plugin;
    }
}
