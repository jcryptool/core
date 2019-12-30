// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sphincsplus.algorithm;

import java.security.MessageDigest;

/**
 * mask generator function, as described in PKCS1v2
 * 
 * @author Lukas Stelzer
 *
 */
class MGF1 {
    private MessageDigest digest;

    /**
     * Create a version of MGF1 for the given digest.
     * 
     * @param MessageDigest digest to use as the basis of the function.
     */
    public MGF1(MessageDigest digest) {
        this.digest = digest;
    }

    /**
     * int to octet string
     * 
     * @param int i
     * @param byte[]sp
     */
    private void ItoOSP(int i, byte[] sp) {
        sp[0] = (byte) (i >>> 24);
        sp[1] = (byte) (i >>> 16);
        sp[2] = (byte) (i >>> 8);
        sp[3] = (byte) (i >>> 0);
    }

    /**
     * Generate the mask.
     * 
     * @param seed source of input bytes for initial digest state
     * @param len length of mask to generate
     * 
     * @return a byte array containing a MGF1 generated mask
     */
    public byte[] generateMask(byte[] seed, int len) {
        byte[] mask = new byte[len];
        byte[] c = new byte[4];
        int counter = 0;
        int hLen = digest.getDigestLength();

        while (counter < (len / hLen)) {
            ItoOSP(counter, c);
            digest.update(seed);
            digest.update(c);
            System.arraycopy(digest.digest(), 0, mask, counter * hLen, hLen);
            counter++;
        }
        if ((counter * hLen) < len) {
            ItoOSP(counter, c);
            digest.update(seed);
            digest.update(c);
            System.arraycopy(digest.digest(), 0, mask, counter * hLen, mask.length - (counter * hLen));
        }
        return mask;
    }
}
