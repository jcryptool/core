// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.logging;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcryptool.core.logging.utils.LogUtil;
import org.osgi.framework.BundleContext;

/**
 * <p>
 * JCrypTool logging plug-in. The logging is based on Eclipse logging, this plug-in is only added for convenience. There
 * is no need to instantiate this plug-in directly, the <code>LogUtil</code> class provides only static methods for easy
 * access.
 * </p>
 * 
 * <p>
 * The log messages are stored in the workbench log file (Eclipse takes care of that). User access is possible via the
 * <b>Error View</b> included in the JCrypTool platform.
 * </p>
 * 
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class LoggingPlugin extends AbstractUIPlugin {
    /** The plug-in ID. */
    public static final String PLUGIN_ID = "org.jcryptool.core.logging"; //$NON-NLS-1$
    /** The shared instance. */
    private static LoggingPlugin plugin;


    /*
     * (non-Javadoc)
     * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        
        int loglevel = getPreferenceStore().getInt(LogUtil.LOGGER_LOG_LEVEL);
        if (loglevel != 0) {
            LogUtil.setLogLevel(loglevel);
        }
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
     * Returns the shared instance.
     * 
     * @return the shared instance
     */
    public static LoggingPlugin getDefault() {
        return plugin;
    }
}
