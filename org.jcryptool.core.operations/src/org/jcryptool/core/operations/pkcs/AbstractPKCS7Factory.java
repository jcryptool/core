// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.pkcs;

import java.security.cert.X509Certificate;

import org.jcryptool.core.operations.dataobject.modern.hybrid.HybridDataObject;

/**
 * Plug-in interface for a PKCS#7 factory.
 * 
 * @author t-kern
 * 
 */
public abstract class AbstractPKCS7Factory implements IPKCSFactory {

    /**
     * Builds a HybridDataObject with the data retrieved from a PKCS#7 container.
     * 
     * @param encoded The DER encoded PKCS#7 container
     * @return A HybridDataObject filled with the data from the PKCS#7 container
     */
    public abstract HybridDataObject disectRSAwithAES(byte[] encoded);

    /**
     * Checks whether the given byte[] contains a valid PKCS#7 container.<br>
     * (A <code>ContentInfo</code> element with an <i>EnvelopedData OID</i>, to be precise)
     * 
     * @param encoded The content that will be validated
     * @return <code>true</code>, if the given content is a valid PKCS#7 ContentInfo object; <code>false</code>
     *         otherwise.
     */
    public abstract boolean isPKCS7(byte[] encoded);

    /**
     * Builds a PKCS#7 container with RSA as the asymmetric cipher and AES as the symmetric cipher.<br>
     * For more information on PKCS#7, please refer to <href>http://www.rsa.com/rsalabs/node.asp?id=2129</href>
     * 
     * @param recipient The recipient's X.509 certificate
     * @param encryptedMessage The encrypted message
     * @param encryptedKey The encrypted symmetric key
     * @param iv The initialization vector
     * @param cipherMode The cipher mode
     * @param symmetricKeyLength The length of the AES key
     * @return The DER encoded PKCS#7 container
     */
    public abstract byte[] rsaWithAES(X509Certificate recipient, byte[] encryptedMessage, byte[] encryptedKey,
            byte[] iv, String cipherMode, int symmetricKeyLength);

}
