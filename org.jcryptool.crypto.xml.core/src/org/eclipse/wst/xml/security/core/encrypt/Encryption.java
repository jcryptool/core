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
package org.eclipse.wst.xml.security.core.encrypt;

import java.io.File;

import org.eclipse.wst.xml.security.core.cryptography.Keystore;
import org.w3c.dom.Document;

/**
 * <p>Model for the <i>XML Encryption Wizard</i>.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class Encryption {
    /** The resource to encrypt. */
    private String resource;
    /** The encryption type. */
    private String encryptionType;
    /** Detached File. */
    private File detachedFile;
    /** File to encrypt. */
    private Document doc;
    /** XPath, if selected. */
    private String xpath;
    /** Basic Security Profile. */
    private boolean bsp;
    /** Encryption content. */
    private boolean content;
    /** Encryption algorithm. */
    private String encryptionAlgorithm;
    /** Key cipher algorithm. */
    private String keyWrapAlgorithm;
    /** Keystore. */
    private Keystore keystore;
    /** Keystore Password. */
    private char[] keystorePassword;
    /** Key name. */
    private String keyName;
    /** Key password. */
	private char[] keyPassword;
    /** Encryption ID. */
    private String encryptionId;
    /** Launch XML Signature Wizard after encryption. */
    private boolean launchSignatureWizard;
    private String[] ids;

    /**
     * Sets the document (fragment) to encrypt.
     *
     * @param resource Selection what to encrypt
     */
    public void setResource(final String resource) {
        this.resource = resource;
    }

    /**
     * The file to encrypt.
     *
     * @param doc The selected file to encrypt
     */
    public void setDocument(final Document doc) {
        this.doc = doc;
    }

    /**
     * Sets the selected XPath.
     *
     * @param wXPath XPath selection
     */
    public void setXpath(final String wXPath) {
        xpath = wXPath;
    }

    /**
     * The encryption type.
     *
     * @param encryptionType Encryption type
     */
    public void setEncryptionType(final String encryptionType) {
        this.encryptionType = encryptionType;
    }

    /**
     * The detached file for a detached encryption.
     *
     * @param detachedFile The selected file to detach
     */
    public void setDetachedFile(final File detachedFile) {
        this.detachedFile = detachedFile;
    }

    /**
     * Basic Security Profil compliant encryption or not.
     *
     * @param bsp True or false
     */
    public void setBsp(final boolean bsp) {
        this.bsp = bsp;
    }

    /**
     * Encrypt only element content or complete element.
     *
     * @param content True or false
     */
    public void setContent(final boolean content) {
        this.content = content;
    }

    /**
     * Sets the encryption algorithm.
     *
     * @param enncryptionAlgorithm Encryption algorithm
     */
    public void setEncryptionAlgorithm(final String enncryptionAlgorithm) {
        this.encryptionAlgorithm = enncryptionAlgorithm;
    }

    /**
     * Sets the key wrap algorithm.
     *
     * @param keyWrapAlgorithm key wrap algorithm
     */
    public void setKeyWrapAlgorithm(final String keyWrapAlgorithm) {
        this.keyWrapAlgorithm = keyWrapAlgorithm;
    }

    /**
     * Sets the key file to store the generated key.
     *
     * @param keystore File to store the generated key
     */
    public void setKeystore(final Keystore keystore) {
        this.keystore = keystore;
    }

    /**
     * Sets the password for the Java keystore.
     *
     * @param keystorePassword The keystore password
     */
    public void setKeystorePassword(final char[] keystorePassword) {
        this.keystorePassword = keystorePassword.clone();
    }

    /**
     * Sets the name for the key.
     *
     * @param keyName The key name
     */
    public void setKeyName(final String keyName) {
        this.keyName = keyName;
    }

    /**
     * Sets the password for the key.
     *
     * @param keyPassword The key password
     */
    public void setKeyPassword(char[] keyPassword) {
		this.keyPassword = keyPassword.clone();
	}

    /**
     * The encryption ID.
     *
     * @param encryptionId The encryption Id to set
     */
    public void setEncryptionId(final String encryptionId) {
        this.encryptionId = encryptionId;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    /**
     * Call the XML Signature Wizard after encrypting the resource.
     *
     * @param launchSignatureWizard Call XML Signature Wizard afterwards
     */
    public void setLaunchSignatureWizard(final boolean launchSignatureWizard) {
        this.launchSignatureWizard = launchSignatureWizard;
    }

    /**
     * Returns the resource to encrypt.
     *
     * @return The resource to encrypt
     */
    public String getResource() {
        return resource;
    }

    /**
     * Returns the file to encrypt.
     *
     * @return The file to encrypt
     */
    public Document getDocument() {
        return doc;
    }

    /**
     * Returns the XPath selection to encrypt.
     *
     * @return The XPath to encrypt
     */
    public String getXpath() {
        return xpath;
    }

    /**
     * Returns the encryption type.
     *
     * @return The encryption type
     */
    public String getEncryptionType() {
        return encryptionType;
    }

    /**
     * Returns the file to be detached.
     *
     * @return The file to be detached
     */
    public File getFileDetached() {
        return detachedFile;
    }

    /**
     * Returns the Basic Security Profile selection.
     *
     * @return True or false
     */
    public boolean getBsp() {
        return bsp;
    }

    /**
     * Returns the part to encrypt.
     *
     * @return True or false
     */
    public boolean getContent() {
        return content;
    }

    /**
     * Returns the encryption algorithm.
     *
     * @return The selected encryption algorithm
     */
    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    /**
     * Returns the key wrap algorithm.
     *
     * @return The selected key wrap algorithm
     */
    public String getKeyWrapAlogrithm() {
        return keyWrapAlgorithm;
    }

    /**
     * Returns the key file with the generated key.
     *
     * @return The file with the generated key
     */
    public Keystore getKeystore() {
        return keystore;
    }

    /**
     * Returns the Java Keystore password.
     *
     * @return The Keystore password
     */
    public char[] getKeystorePassword() {
        return keystorePassword.clone();
    }

    /**
     * Returns the key name.
     *
     * @return The name key
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

    /**
     * Returns the encryption ID.
     *
     * @return The encryption ID
     */
    public String getEncryptionId() {
        return encryptionId;
    }

    public String[] getIds() {
        return ids;
    }

    /**
     * Returns whether or not to call the XML Signature Wizard after encrypting the resource.
     *
     * @return Call XML Signature Wizard afterwards
     */
    public boolean getLaunchSignatureWizard() {
        return launchSignatureWizard;
    }
}
