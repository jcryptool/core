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
package org.jcryptool.visual.sig.algorithm;

import java.security.MessageDigest;
// import javax.crypto.*;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sig.SigPlugin;

/**
 * Hashes the given input with the selected hash methods.
 * 
 * @author Grebe
 */
public class Hash {
    /**
     * This method hashes an input stored in Input.java with a given hash method
     * 
     * @throws Exception
     * @param hashmethod The name of the method (a string)
     * @return the hash of the input as byte array
     */
    public static byte[] hashInput(String hashmethod, byte[] input) throws Exception {
        byte[] md = null;
        
        try {
            // Get an MD5 message digest object and compute the plaintext digest
            MessageDigest messageDigest = MessageDigest.getInstance(hashmethod); // Argument is a string!
            messageDigest.update(input);
            // Output:
            md = messageDigest.digest();

            Input.hash = md; // Store the generated hash
            Input.hashHex = Input.bytesToHex(md); // Hex
        } catch (Exception ex) {
            LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
        }
        
        return md;
    }

}
