//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.CertificateClasses;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * Class representing a Revocation Request that is sent to the CA
 * 
 * @author mmacala
 */
public class RR {

    /**
     * the public key alias of the keypair that is going to be revoked
     */
    private KeyStoreAlias pubAlias;

    /**
     * the reason why the certificate should be revoked
     */
    private String reason;

    /**
     * public constructor
     * 
     * @param ksAlias - alias of the certificate that should be revoked
     * @param reason - the reason why the certificate should be revoked
     */
    public RR(KeyStoreAlias ksAlias, String reason) {
        pubAlias = ksAlias;
        this.reason = reason;
    }

    /**
     * gets the alias of the certificate that should be revoked
     * 
     * @return the public key alias
     */
    public KeyStoreAlias getAlias() {
        return pubAlias;
    }

    /**
     * gets the reason why the certificate should be revoked
     * 
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

}
