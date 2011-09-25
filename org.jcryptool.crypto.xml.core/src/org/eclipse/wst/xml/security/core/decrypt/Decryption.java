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
package org.eclipse.wst.xml.security.core.decrypt;

import org.eclipse.wst.xml.security.core.cryptography.Keystore;

/**
 * <p>Model for the <i>XML Decryption Wizard</i>.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class Decryption {
    /** XML document to decrypt. */
    private String file;
    /** The Keystore. */
    private Keystore keystore;
    /** The Keystore password. */
    private char[] keystorePassword;
    /** Encryption ID. */
    private String encryptionId;
    /** The key name. */
	private String keyName;
	/** The key password. */
	private char[] keyPassword;
	/** The used encryption type (enveloping, detached). */
    private String encryptionType;
    /** The detached file, null with an enveloping encryption. */
    private String detachedFile;

    /**
     * The XML document to decrypt.
     *
     * @param file The selected XML document to decrypt
     */
    public void setFile(final String file) {
        this.file = file;
    }

    /**
     * Sets the Keystore in which the secret key was stored.
     *
     * @param keystore Keystore containing the secret key
     */
    public void setKeystore(final Keystore keystore) {
        this.keystore = keystore;
    }

    /**
     * Sets the Keystore password.
     *
     * @param keystorePassword Keystore password
     */
    public void setKeystorePassword(final char[] keystorePassword) {
        this.keystorePassword = keystorePassword.clone();
    }

    /**
     * Sets the encryption ID.
     *
     * @param encryptionId The encryption ID
     */
    public void setEncryptionId(final String encryptionId) {
        this.encryptionId = encryptionId;
    }

    public void setEncryptionType(final String encryptionType) {
        this.encryptionType = encryptionType;
    }

    public void setDetachedFile(final String detachedFile) {
        this.detachedFile = detachedFile;
    }

    /**
     * Sets the key name.
     *
     * @param keyName The key name
     */
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	/**
	 * Sets the key password.
	 *
	 * @param keyPassword The key password
	 */
	public void setKeyPassword(char[] keyPassword) {
		this.keyPassword = keyPassword.clone();
	}

    /**
     * Returns the XML document to decrypt.
     *
     * @return The XML document to decrypt
     */
    public String getFile() {
        return file;
    }

    /**
     * Returns the keystore.
     *
     * @return The keystore
     */
    public Keystore getKeystore() {
        return keystore;
    }

    /**
     * Returns the keystore password.
     *
     * @return The keystore password
     */
    public char[] getKeystorePassword() {
        return keystorePassword.clone();
    }

    /**
     * Returns the encryption ID.
     *
     * @return Encryption ID.
     */
    public String getEncryptionId() {
        return encryptionId;
    }

    /**
     * Returns the key name.
     *
     * @return The key name
     */
	public String getKeyName() {
		return keyName;
	}

	/**
	 * Returns the key password.
	 *
	 * @return The key password
	 */
	public char[] getKeyPassword() {
		return keyPassword.clone();
	}

	public String getEncryptionType() {
	    return encryptionType;
	}

	public String getDetachedFile() {
	    return detachedFile;
	}
}
