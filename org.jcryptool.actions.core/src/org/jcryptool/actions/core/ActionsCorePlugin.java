// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.core;

import org.eclipse.core.runtime.Plugin;

/**
 *
 * @author Dominik Schadow
 * @version 0.9.3
 */
public class ActionsCorePlugin extends Plugin {
    /**
     * The shared instance.
     */
    private static ActionsCorePlugin plugin;

    /**
     * The constructor.
     */
    public ActionsCorePlugin() {
        plugin = this;
    }

    /**
     * Returns the shared instance.
     *
     * @return the shared instance
     */
    public static ActionsCorePlugin getDefault() {
        return plugin;
    }
}
