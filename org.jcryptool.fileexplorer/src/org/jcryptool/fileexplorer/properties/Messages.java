// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.properties;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized strings for the <code>org.jcryptool.fileexplorer.properties</code> package.
 * 
 * @author Anatoli Barski
 * @version 1.0.0
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.fileexplorer.properties.messages"; //$NON-NLS-1$
    public static String FileExplorerProperties_lastModified;
    public static String FileExplorerProperties_size;
    public static String FileExplorerProperties_path;
    public static String FileExplorerProperties_type;
    public static String FileExplorerProperties_attributes;
    public static String FileExplorerProperties_initFailed;
    public static String FileExplorerProperties_readonly;
    public static String FileExplorerProperties_hidden;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
