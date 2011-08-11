// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3.echo;

/**
 * The action class of ECHO.
 *
 * @author Michael Starzer
 *
 */
public class ECHOAction {

    public byte[] run(int hashlength, String str) {
        ECHOAlgorithm e = new ECHOAlgorithm(hashlength, str.getBytes());
        return e.getHash();
    }

    public byte[] run(int hashlength, String str, String salt) {
        ECHOAlgorithm e = new ECHOAlgorithm(hashlength, str.getBytes(), salt);
        return e.getHash();
    }
}
