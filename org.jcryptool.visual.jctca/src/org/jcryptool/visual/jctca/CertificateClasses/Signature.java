//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.CertificateClasses;

import java.util.Date;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * This class represents a succsessfully created signature.
 * 
 * @author mmacala
 * 
 */
public class Signature {

    /**
     * the signature from the sigvis plugin
     */
    private byte[] signature;

    /**
     * path to the file that has been signed
     */
    private String path;

    /**
     * text that has been signed
     */
    private String text;

    /**
     * time when the signature was done
     */
    private Date time;

    /**
     * private key that has been used for the signature
     */
    private KeyStoreAlias privAlias;

    /**
     * public key
     */
    private KeyStoreAlias pubAlias;

    private String hashalgorithm;

    /**
     * constructor for a signature
     * 
     * @param signature
     * @param path - path to the file that has been signed (text has to be null)
     * @param text - text that has been signed (path has to be null)
     * @param time - time when the signature was created
     * @param privAlias - alias for the private key that has been used for signing
     * @param pubAlias - alias for the corresponsing public key
     */
    public Signature(byte[] signature, String path, String text, Date time, KeyStoreAlias privAlias,
            KeyStoreAlias pubAlias, String hashalgorithm) {
        this.signature = signature;
        this.path = path;
        this.text = text;
        this.time = time;
        this.privAlias = privAlias;
        this.pubAlias = pubAlias;
        this.hashalgorithm = hashalgorithm;
    }

    public String getHashAlgorithm() {
        return hashalgorithm;
    }

    /**
     * @return the signature
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    /**
     * @return the path
     */
    public String getPath() {
        if (path == null) {
            return "";
        }
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return the alias
     */
    public KeyStoreAlias getPrivAlias() {
        return privAlias;
    }

    /**
     * @param privAlias the Alias to set
     */
    public void setPrivAlias(KeyStoreAlias privAlias) {
        this.privAlias = privAlias;
    }

    /**
     * @return the alias
     */
    public KeyStoreAlias getPubAlias() {
        return pubAlias;
    }

    /**
     * @param pubAlias the Alias to set
     */
    public void setPubAlias(KeyStoreAlias pubAlias) {
        this.pubAlias = pubAlias;
    }
}
