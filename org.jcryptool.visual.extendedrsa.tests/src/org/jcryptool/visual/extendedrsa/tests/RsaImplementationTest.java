// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.tests;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.jcryptool.visual.extendedrsa.RsaImplementation;
import org.junit.Test;

/**
 * test for the class "RsaImplementation.java"
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class RsaImplementationTest {

    @Test
    public void testEncrypt() {
        RsaImplementation impl = new RsaImplementation();
        String encryption = impl.encrypt("abcd", new BigInteger("65537"), new BigInteger("9998000099"));
        assertEquals("61689ed3 11f597f64 db1a4084 2a370bf6 ", encryption);
    }

    @Test
    public void testDecrypt() {
        RsaImplementation impl = new RsaImplementation();
        BigInteger p = new BigInteger("99991");
        BigInteger q = new BigInteger("99989");
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger d = new BigInteger("65537").modInverse(phi);
        String dec = impl.decrypt("61689ed3 11f597f64 db1a4084 2a370bf6 ", d, new BigInteger("9998000099"));
        assertEquals("abcd", dec);
    }

    @Test
    public void testCombined() {
        RsaImplementation impl = new RsaImplementation();
        String encryption = impl.encrypt("abcd", new BigInteger("65537"), new BigInteger("9998000099"));

        BigInteger p = new BigInteger("99991");
        BigInteger q = new BigInteger("99989");
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger d = new BigInteger("65537").modInverse(phi);
        String dec = impl.decrypt(encryption, d, new BigInteger("9998000099"));
        assertEquals("abcd", dec);
    }
}
