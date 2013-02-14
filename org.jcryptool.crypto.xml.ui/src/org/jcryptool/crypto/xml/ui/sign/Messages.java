/*******************************************************************************
 * Copyright (c) 2013 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.jcryptool.crypto.xml.ui.sign;

import org.eclipse.osgi.util.NLS;

/**
 * <p>
 * Externalized strings for the org.eclipse.wst.xml.security.ui.sign package.
 * </p>
 * 
 * @author Dominik Schadow
 * @version 1.0.0
 */
public final class Messages extends NLS {
    /** The bundle name. */
    private static final String BUNDLE_NAME = "org.jcryptool.crypto.xml.ui.sign.messages";

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
    public static String basicSecurityProfile, browse, bspCompliant, detached, detachedFile, document, enterXPath,
            enveloped, enveloping, key, resource, resourceDescription, signatureTitle, select, selection,
            signatureType, verifyDetachedFile, xpath, xpathAttribute, xpathMultipleElements, xpathNoElement,
            xpathPopup, documentInvalid;
    /** PageKey externalized strings. */
    public static String name, selectKey, enterKeyPassword, password, useKeyDescription;
    /** PageAlgorithms externalized strings. */
    public static String algorithmsDescription, ambiguousSignatureId, buttonAddProperty, buttonRemoveProperty,
            canonicalizationTransformation, encryptionWizard, messageDigestSignature, selectCanonicalization,
            selectMessageDigest, selectSignature, selectTransformation, signatureId, properties,
            signaturePropertyContent, signaturePropertyContentToolTip, signaturePropertyId, signaturePropertyIdToolTip,
            signaturePropertyTarget, signaturePropertyTargetToolTip, startEncryptionWizard;
}
