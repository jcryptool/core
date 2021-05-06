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
package org.jcryptool.crypto.flexiprovider.operations.ui.handlers;

import org.eclipse.osgi.util.NLS;

/**
 * @author Anatoli Barski
 *
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.crypto.flexiprovider.operations.ui.handlers.messages"; //$NON-NLS-1$
    public static String SelectKeyHandler_WrongKey;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
