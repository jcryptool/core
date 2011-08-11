/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.encrypt;

import org.eclipse.osgi.util.NLS;

/**
 * <p>Externalized strings for the org.eclipse.wst.xml.security.ui.encrypt package.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public final class Messages extends NLS {
    /** The bundle name. */
    private static final String BUNDLE_NAME = "org.eclipse.wst.xml.security.ui.encrypt.messages";

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
    public static String encryptionWizard;
    /** PageResource externalized strings. */
    public static String resourceDescription, resource, basicSecurityProfile,
            document, selection, xpath, browse, keepRootElementPlain, bspCompliant, enterXPath,
            xpathNoElement, xpathMultipleElements, xpathAttribute, xpathPopup, encryptionType,
            encryptionEnveloping, encryptionDetached, detachedFile, verifyDetachedFile, openKey,
            createKey, createKeystoreAndKey, key, keystoreAndKey, documentInvalid;
    /** PageOpenKey externalized strings. */
    public static String openKeyDescription, open, selectKeystoreFile,
            enterKeyName, echoPassword, verifyKeyPassword, enterKeyPassword,
            enterKeystorePassword, verifyKeyName, verifyAll, password,
            name, keystoreNotFound;
    /** PageCreateKey externalized strings. */
    public static String createKeyDescription, keystore, generateButton,
            keystoreAlreadyExists, selectKeyAlgorithm, selectKeyAlgorithmSize, keyGenerationFailed,
            keyGenerated, keyAlgorithm, keyAlgorithmSize, enterNewKeystorePassword, existingKeyName,
            enterNewKeyName, selectKeystoreFileToExtend, enterNewKeyPassword, keyInsertionFailed;
    /** PageCreateKeystore externalized strings. */
    public static String createKeystoreDescription, keystoreGenerated,
            keystoreGenerationFailed, enterNewKeystoreName;
    /** PageAlgorithms externalized strings. */
    public static String encryptionTitle, algorithmsDescription, algorithms, encryptionId, select,
            selectEncryptionAlgorithm, selectKeyWrapAlgorithm, signatureWizard,
            startSignatureWizard, ambiguousEncryptionId, properties;
}
