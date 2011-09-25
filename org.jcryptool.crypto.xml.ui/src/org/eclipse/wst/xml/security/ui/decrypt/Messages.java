/*******************************************************************************
 * Copyright (c) 2010 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.decrypt;

import org.eclipse.osgi.util.NLS;

/**
 * <p>
 * Externalized strings for the org.eclipse.wst.xml.security.ui.decrypt package.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public final class Messages extends NLS {
    /** The bundle name. */
    private static final String BUNDLE_NAME = "org.eclipse.wst.xml.security.ui.decrypt.messages";

    /**
     * Private Constructor to avoid instantiation.
     */
    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /** Wizard launcher externalized strings. */
    public static String decryptionWizard;
    public static String PageKeystore_keystoreDescription;
    /** PageResource externalized strings. */
    public static String decryptionTitle, resourceDescription, encryptionType, encryptionEnveloping,
            encryptionDetached;
    /** PageKeystore externalized strings. */
    public static String keystore, selectButton, missingKeystore, encryptionId, missingEncryptionId, keystoreNotFound,
            password, echoPassword, name, key, missingKeyName, missingKeystorePassword, missingKeyPassword,
            verifyKeyName, verifyKeyPassword, verifyAll;
}
