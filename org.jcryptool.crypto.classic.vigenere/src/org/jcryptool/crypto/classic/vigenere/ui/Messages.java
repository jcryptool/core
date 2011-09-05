// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.vigenere.ui;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized strings for the plug-ins wizard.
 *
 * @author Dominik Schadow
 * @version 0.4.0
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.vigenere.ui.messages"; //$NON-NLS-1$
    public static String VigenereWizard_title;
    public static String VigenereWizardPage_description;
    public static String VigenereWizardPage_title;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
