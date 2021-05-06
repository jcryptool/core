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
package org.jcryptool.fileexplorer.views;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized strings for the <code>org.jcryptool.fileexplorer.views</code>
 * package.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.fileexplorer.views.messages"; //$NON-NLS-1$
    public static String FileExplorerView_2;
    public static String FileExplorerView_3;
    public static String FileExplorerView_4;
    public static String FileExplorerView_error;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
