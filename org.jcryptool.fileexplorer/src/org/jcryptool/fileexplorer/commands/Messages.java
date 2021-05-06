// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.commands;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized strings for the commands package.
 *
 * @author Dominik Schadow
 * @version 0.9.3
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.fileexplorer.commands.messages"; //$NON-NLS-1$
    public static String DeleteHandler_0;
    public static String DeleteHandler_1;
    public static String DeleteHandler_2;
    public static String DeleteHandler_4;
    public static String DeleteHandler_5;
    public static String DeleteHandler_6;
	public static String PasteHandler_0;
    public static String PasteHandler_1;
    public static String RenameHandler_0;
    public static String RenameHandler_1;
    public static String RenameHandler_4;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
