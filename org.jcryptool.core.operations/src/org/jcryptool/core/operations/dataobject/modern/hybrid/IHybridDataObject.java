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
import java.security.cert.X509Certificate;

import org.jcryptool.core.operations.dataobject.modern.asymmetric.IAsymmetricDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject;

/**
 * Provides a top level frame for the data objects used for hybrid algorithms.
 * 
 * @author t-kern
 * 
 */
public interface IHybridDataObject extends IAsymmetricDataObject, ISymmetricDataObject {

    /**
     * Returns the name of the asymmetric algorithm.
     * 
     * @return The name of the asymmetric algorithm
     */
    public String getAsymmetricAlgorithmName();

    /**
     * Returns the asymmetric padding scheme.
     * 
     * @return The asymmetric padding scheme
     */
    public String getAsymmetricPaddingName();

    /**
     * Returns the recipient's DER encoded X.509 certificates issuer.
     * 
     * @return The recipient's DER encoded X.509 certificates issuer
     */
    public byte[] getIssuerDN();

    /**
     * Returns the recipient's X.509 certificate
     * 
     * @return The recipient's X.509 certificate
     */
    public X509Certificate getRecipientCertificate();

    /**
     * Returns the recipient's X.509 certificates serial number.
     * 
     * @return The recipient's X.509 certificates serial number
     */
    public BigInteger getSerialNumber();

    /**
     * Returns the name of the symmetric algorithm.
     * 
     * @return The name of the symmetric algorithm
     */
    public String getSymmetricAlgorithmName();

    /**
     * Sets the asymmetric algorithm name.
     * 
     * @param asymmetricAlgorithmName
     */
    public void setAsymmetricAlgorithmName(String asymmetricAlgorithmName);

    /**
     * Sets the asymmetric padding scheme.
     * 
     * @param paddingName The asymmetric padding scheme
     */
    public void setAsymmetricPaddingName(String paddingName);

    /**
     * Sets the recipients DER encoded X.509 certificates issuer.
     * 
     * @param issuer The recipients DER encoded X.509 certificates issuer
     */
    public void setIssuerDN(byte[] issuer);

    /**
     * Set's the recipients X.509 certificate.
     * 
     * @param certificate
     */
    public void setRecipientCertificate(X509Certificate certificate);

    /**
     * Set's the recipients X.509 certificates serial number.
     * 
     * @param serialNumber The recipients X.509 certificates serial number
     */
    public void setSerialNumber(BigInteger serialNumber);

    /**
     * Sets the symmetric algorithm name.
     * 
     * @param symmetricAlgorithmName
     */
    public void setSymmetricAlgorithmName(String symmetricAlgorithmName);

}
