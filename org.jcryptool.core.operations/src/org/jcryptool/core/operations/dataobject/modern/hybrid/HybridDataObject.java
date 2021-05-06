// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.dataobject.modern.hybrid;

import java.math.BigInteger;
import java.security.Key;
import java.security.cert.X509Certificate;

import org.jcryptool.core.operations.dataobject.modern.ModernDataObject;

/**
 * Data object for hybrid ciphers.
 * 
 * @author t-kern
 * 
 */
public class HybridDataObject extends ModernDataObject implements IHybridDataObject {

    /** The initialization vector */
    private byte[] iv;

    /** The shared symmetric key */
    private byte[] symmetricKey;

    /** The cipher mode (i.e. CBC) */
    private String cipherMode;

    /** The asymmetric key */
    private Key asymmetricKey;

    /** The name of the asymmetric algorithm */
    private String asymmetricAlgorithmName;

    /** The name of the symmetric algorithm */
    private String symmetricAlgorithmName;

    /** The recipients X.509 certificate */
    private X509Certificate recipientCertificate;

    /** The issuer DN of the recipients X.509 certificate */
    private byte[] issuer;

    /** The serial number of the recipients X.509 certificate */
    private BigInteger serialNumber;

    /** The name of the asymmetric padding scheme */
    private String asymmetricPaddingName;

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#getAsymmetricAlgorithmName()
     */
    public String getAsymmetricAlgorithmName() {
        return asymmetricAlgorithmName;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.asymmetric.IAsymmetricDataObject#getAsymmetricKey()
     */
    public Key getAsymmetricKey() {
        return asymmetricKey;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#getCipherMode()
     */
    public String getCipherMode() {
        return cipherMode;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#getIssuerDN()
     */
    public byte[] getIssuerDN() {
        return issuer;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#getIV()
     */
    public byte[] getIV() {
        return iv;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#getRecipientCertificate()
     */
    public X509Certificate getRecipientCertificate() {
        return recipientCertificate;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#getSerialNumber()
     */
    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#getSymmetricAlgorithmName()
     */
    public String getSymmetricAlgorithmName() {
        return symmetricAlgorithmName;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#getSymmetricKey()
     */
    public byte[] getSymmetricKey() {
        return symmetricKey;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#setAsymmetricAlgorithmName(java.lang.String)
     */
    public void setAsymmetricAlgorithmName(String asymmetricAlgorithmName) {
        this.asymmetricAlgorithmName = asymmetricAlgorithmName;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.asymmetric.IAsymmetricDataObject#setAsymmetricKey(java.security.Key)
     */
    public void setAsymmetricKey(Key asymmetricKey) {
        this.asymmetricKey = asymmetricKey;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#setCipherMode(java.lang.String)
     */
    public void setCipherMode(String cipherMode) {
        this.cipherMode = cipherMode;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#setIssuerDN(byte[])
     */
    public void setIssuerDN(byte[] issuer) {
        this.issuer = issuer;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#setIV(byte[])
     */
    public void setIV(byte[] iv) {
        this.iv = iv;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#setRecipientCertificate(java.security.cert.X509Certificate)
     */
    public void setRecipientCertificate(X509Certificate certificate) {
        this.recipientCertificate = certificate;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#setSerialNumber(java.math.BigInteger)
     */
    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#setSymmetricAlgorithmName(java.lang.String)
     */
    public void setSymmetricAlgorithmName(String symmetricAlgorithmName) {
        this.symmetricAlgorithmName = symmetricAlgorithmName;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#setSymmetricKey(byte[])
     */
    public void setSymmetricKey(byte[] symmetricKey) {
        this.symmetricKey = symmetricKey;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#getAsymmetricPaddingName()
     */
    public String getAsymmetricPaddingName() {
        return asymmetricPaddingName;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.hybrid.IHybridDataObject#setAsymmetricPaddingName(java.lang.String)
     */
    public void setAsymmetricPaddingName(String paddingName) {
        this.asymmetricPaddingName = paddingName;
    }

}
