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
package org.eclipse.wst.xml.security.ui.preferences;

/**
 * <p>This utility class contains constant values and names for the
 * preference pages of the XML Security Tools.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public final class PreferenceConstants {
    /**
     * Private Constructor to avoid instantiation.
     */
    private PreferenceConstants() { }
    
    /** Number of characters for pixel width conversion. */
    public static final int CHARS_TO_PIXEL = 30;
    /** Canonicalization type. */
    public static final String CANON_TYPE = "canonType";
    /** Canonicalization document. */
    public static final String CANON_TARGET = "canonTarget";
    /** Resource to encrypt. */
    public static final String ENCRYPT_RESOURCE = "encryptionResource";
    /** XPath to encrypt. */
    public static final String ENCRYPT_XPATH = "encryptionXPath";
    /** Encryption type. */
    public static final String ENCRYPT_TYPE = "encryptionType";
    /** Encryption Algorithm. */
    public static final String ENCRYPT_ENCRYPTION = "encryptionEncryption";
    /** Key Wrap Algorithm. */
    public static final String ENCRYPT_KEY_WRAP = "encryptionKeyWrap";
    /** Encryption ID. */
    public static final String ENCRYPT_ID = "encryptionID";
    /** Encryption Keystore. */
    public static final String ENCRYPT_KEY_STORE = "encryptionKeyStore";
    /** Encryption key name. */
	public static final String ENCRYPT_KEY_NAME = "encryptionKeyName";
    /** Resource to sign. */
    public static final String SIGN_RESOURCE = "signatureResource";
    /** XPath to sign. */
    public static final String SIGN_XPATH = "signatureXPath";
    /** Signature type. */
    public static final String SIGN_TYPE = "signatureType";
    /** Signature ID. */
    public static final String SIGN_ID = "signatureID";
    /** Signature Canonicalization Algorithm. */
    public static final String SIGN_CANON = "signatureCanonicalization";
    /** Signature Transformation Algorithm. */
    public static final String SIGN_TRANS = "signatureTransformation";
    /** Signature Message Digest Algorithm. */
    public static final String SIGN_MDA = "signatureMDA";
    /** Signature Algorithm. */
    public static final String SIGN_SA = "signatureAlgorithm";
    /** Keystore file. */
    public static final String SIGN_KEYSTORE_FILE = "signatureKeystoreFile";
    /** Key name. */
    public static final String SIGN_KEY_NAME = "signatureKeyName";
    /** Layout textfield length. */
    public static final int TEXTFIELD = 20;
    /** Layout margin. */
    public static final int MARGIN = 2;
    /** Layout number of columns. */
    public static final int COLUMNS = 2;
    /** Layout single group. */
    public static final int SINGLE_GROUP = 1;
    /** Layout small group. */
    public static final int SMALL_GROUP = 2;
    /** Layout large group. */
    public static final int LARGE_GROUP = 3;
}
