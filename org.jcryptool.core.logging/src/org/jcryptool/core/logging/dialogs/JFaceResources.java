// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.logging.dialogs;

import org.eclipse.osgi.util.NLS;

public class JFaceResources extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.core.logging.dialogs.messages"; //$NON-NLS-1$

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, JFaceResources.class);
    }

    private JFaceResources() {
    }
}
