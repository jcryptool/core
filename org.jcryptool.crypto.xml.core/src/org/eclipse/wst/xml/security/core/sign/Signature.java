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
package org.eclipse.wst.xml.security.core.sign;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.wst.xml.security.core.cryptography.Keystore;
import org.w3c.dom.Document;

/**
 * <p>Model for the <i>XML Signature Wizard</i>.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class Signature {
    /** Resource to sign. */
    private String resource;
    /** Document to sign. */
    private Document doc;
    /** XPath, if selected. */
    private String xpath;
    /** Signature Type. */
    private String signatureType;
    /** Detached File. */
    private File detachedFile;
    /** Basic Security Profile. */
    private boolean bsp;
    /** Keystore. */
    private Keystore keystore;
    /** Keystore Password. */
    private char[] keystorePassword;
    /** Key algorithm. */
    private String keyAlgorithm;
    /** Key name. */
    private String keyName;
    /** Key password. */
    private char[] keyPassword;
    /** Message Digest Algorithm. */
    private String messageDigestAlgorithm;
    /** Signature Algorithm. */
    private String signatureAlgorithm;
    /** Canonicalization Algorithm. */
    private String canonicalizationAlgorithm;
    /** Transformation Algorithm. */
    private String transformationAlgorithm;
    /** Signature ID. */
    private String signatureId;
    /** Launch Encryption Wizard after signing. */
    private boolean launchEncryptionWizard;
    /** Additional signature properties for the signature. */
    private ArrayList<DigitalSignatureProperty> signatureProperties;
    private String[] ids;

    /**
     * Sets the XML document (fragment) to sign.
     *
     * @param resource The resource to sign
     */
    public void setResource(final String resource) {
        this.resource = resource;
    }

    /**
     * Sets the file to sign.
     *
     * @param doc The selected file to sign
     */
    public void setDocument(final Document doc) {
        this.doc = doc;
    }

    /**
     * Sets the selected XPath.
     *
     * @param xpath The XPath expression
     */
    public void setXpath(final String xpath) {
        this.xpath = xpath;
    }

    /**
     * Sets the signature type.
     *
     * @param signatureType Signature type
     */
    public void setSignatureType(final String signatureType) {
        this.signatureType = signatureType;
    }

    /**
     * Sets the detached file for a detached signature.
     *
     * @param detachedFile The selected file to detach
     */
    public void setDetachedFile(final File detachedFile) {
        this.detachedFile = detachedFile;
    }

    /**
     * Sets the Basic Security Profile compliant signature selection.
     *
     * @param bsp True or false
     */
    public void setBsp(final boolean bsp) {
        this.bsp = bsp;
    }

    /**
     * Sets the Java Keystore file.
     *
     * @param keystore The Keystore file.
     */
    public void setKeystore(final Keystore keystore) {
        this.keystore = keystore;
    }

    /**
     * Sets the password for the Java Keystore.
     *
     * @param keystorePassword The keystore password
     */
    public void setKeystorePassword(final char[] keystorePassword) {
        this.keystorePassword = keystorePassword.clone();
    }

    /**
     * Sets the key algorithm.
     *
     * @param keyAlgorithm The key algorithm
     */
    public void setKeyAlgorithm(final String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }

    /**
     * Sets the key password.
     *
     * @param keyPassword The key password
     */
    public void setKeyPassword(final char[] keyPassword) {
        this.keyPassword = keyPassword.clone();
    }

    /**
     * Sets the key name.
     *
     * @param keyName The name of the key
     */
    public void setKeyName(final String keyName) {
        this.keyName = keyName;
    }

    /**
     * Sets the Message Digest Algorithm.
     *
     * @param messageDigestAlgorithm The Message Digest Algorithm
     */
    public void setMessageDigestAlgorithm(final String messageDigestAlgorithm) {
        this.messageDigestAlgorithm = messageDigestAlgorithm;
    }

    /**
     * Sets the Signature Algorithm.
     *
     * @param signatureAlgorithm The Signature Algorithm
     */
    public void setSignatureAlgorithm(final String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * Sets the Canonicalization Algorithm.
     *
     * @param canonicalizationAlgorithm The Canonicalization Algorithm
     */
    public void setCanonicalizationAlgorithm(final String canonicalizationAlgorithm) {
        this.canonicalizationAlgorithm = canonicalizationAlgorithm;
    }

    /**
     * Sets the Transformation Algorithm.
     *
     * @param transformationAlgorithm The Transformation Algorithm
     */
    public void setTransformationAlgorithm(final String transformationAlgorithm) {
        this.transformationAlgorithm = transformationAlgorithm;
    }

    /**
     * Sets the signature id.
     *
     * @param signatureId The signature id
     */
    public void setSignatureId(final String signatureId) {
        this.signatureId = signatureId;
    }

    /**
     * Call the Encryption Wizard after signing the resource.
     *
     * @param launchEncryptionWizard Call Encryption Wizard afterwards
     */
    public void setLaunchEncryptionWizard(final boolean launchEncryptionWizard) {
        this.launchEncryptionWizard = launchEncryptionWizard;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    /**
     * Sets the signature properties (maybe null).
     *
     * @param signatureProperties The signature properties
     */
    public void setSignatureProperties(final ArrayList<DigitalSignatureProperty> signatureProperties) {
        this.signatureProperties = signatureProperties;
    }

    /**
     * Returns the resource to sign.
     *
     * @return The resource to sign
     */
    public String getResource() {
        return resource;
    }

    /**
     * Returns the path for the file to sign.
     *
     * @return The path of the file to sign
     */
    public Document getDocument() {
        return doc;
    }

    /**
     * Returns the XPath expression for the document fragment to sign.
     *
     * @return The XPath expression
     */
    public String getXpath() {
        return xpath;
    }

    /**
     * Returns the signature type.
     *
     * @return The signature type
     */
    public String getSignatureType() {
        return signatureType;
    }

    /**
     * Returns the file to be detached.
     *
     * @return The detached file
     */
    public File getDetachedFile() {
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
     * Returns the selected Java Keystore.
     *
     * @return The selected Java Keystore
     */
    public Keystore getKeystore() {
        return keystore;
    }

    /**
     * Returns the Java Keystore password.
     *
     * @return The keystore password
     */
    public char[] getKeystorePassword() {
        return keystorePassword.clone();
    }

    /**
     * Returns the key algorithm.
     *
     * @return The key algorithm
     */
    public String getKeyAlgorithm() {
        return keyAlgorithm;
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
     * Returns the key name.
     *
     * @return The key name
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * Returns the Message Digest Algorithm.
     *
     * @return The Message Digest Algorithm
     */
    public String getMessageDigestAlgorithm() {
        return messageDigestAlgorithm;
    }

    /**
     * Returns the Signature Algorithm.
     *
     * @return The Signature Algorithm
     */
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * Returns the Canonicalization Algorithm.
     *
     * @return The Canonicalization Algorithm
     */
    public String getCanonicalizationAlgorithm() {
        return canonicalizationAlgorithm;
    }

    /**
     * Returns the Transformation Algorithm.
     *
     * @return The Transformation Algorithm
     */
    public String getTransformationAlgorithm() {
        return transformationAlgorithm;
    }

    /**
     * Returns the signature ID.
     *
     * @return Returns the signature ID
     */
    public String getSignatureId() {
        return signatureId;
    }

    /**
     * Returns whether or not to launch the Encryption Wizard after signing the resource.
     *
     * @return Launch Encryption Wizard afterwards
     */
    public boolean getLaunchEncryptionWizard() {
        return launchEncryptionWizard;
    }

    /**
     * Returns the signature properties (maybe null).
     *
     * @return Returns the signature properties
     */
    public ArrayList<DigitalSignatureProperty> getSignatureProperties() {
        return signatureProperties;
    }

    public String[] getIds() {
        return ids;
    }
}
