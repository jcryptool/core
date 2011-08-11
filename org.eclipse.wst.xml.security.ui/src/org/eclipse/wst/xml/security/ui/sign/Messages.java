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
package org.eclipse.wst.xml.security.ui.sign;

import org.eclipse.osgi.util.NLS;

/**
 * <p>Externalized strings for the org.eclipse.wst.xml.security.ui.sign package.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public final class Messages extends NLS {
    /** The bundle name. */
    private static final String BUNDLE_NAME = "org.eclipse.wst.xml.security.ui.sign.messages";

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
    public static String signatureWizard;
    /** PageResource externalized strings. */
    public static String basicSecurityProfile, browse, bspCompliant, createKey, createKeystoreAndKey, detached,
            detachedFile, document, enterXPath, enveloped, enveloping, key, resource, resourceDescription,
            signatureTitle, select, selection, signatureType, useKey, verifyDetachedFile, xpath, xpathAttribute,
            xpathMultipleElements, xpathNoElement, xpathPopup, documentInvalid, keystoreAndKey;
    /** PageOpenKey externalized strings. */
    public static String enterKeyName, enterKeyPassword, enterKeystorePassword, open, password, selectKeyFile,
            useKeyDescription, verifyAll, verifyKeyName, verifyKeyPassword, verifyKeystore,
            verifyKeystorePassword, wrongKeyAlgorithm, keystoreNotFound;
    /** PageCreateKey externalized strings. */
    public static String commonName, country, createKeyDescription, certificate, generate,
            enterCommonName, enterNewKeyName, enterNewKeyPassword, enterNewKeystoreName, enterNewKeystorePassword,
            keyAlgorithm, keyExistsInKeystore, keyGenerated, keyGenerationFailed, keystore, keystoreAlreadyExists,
            location, organization, organizationalUnit, selectKeyAlgorithm, state, selectKeystoreForInsert;
    /** PageCreateKeystore externalized strings. */
    public static String createKeystoreDescription, keystoreGenerated,
            keystoreGenerationFailed, name;
    /** PageAlgorithms externalized strings. */
    public static String algorithmsDescription, ambiguousSignatureId, buttonAddProperty,
            buttonRemoveProperty, canonicalizationTransformation, encryptionWizard, messageDigestSignature,
            selectCanonicalization, selectMessageDigest, selectSignature, selectTransformation, signatureId,
            properties, signaturePropertyContent, signaturePropertyContentToolTip, signaturePropertyId,
            signaturePropertyIdToolTip, signaturePropertyTarget, signaturePropertyTargetToolTip, startEncryptionWizard;
}
