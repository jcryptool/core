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

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.jcryptool.visual.sphincsplus.EnvironmentParameters;
import org.jcryptool.visual.sphincsplus.interfaces.IHashFunction;

/**
 * See NIST paper 7.2.2
 * 
 * Implementations of the several SHA256 Hash-functions.
 * 
 * @author Lukas Stelzer
 *
 */
class SHA256 implements IHashFunction {
    @Override
    public byte[] F(byte[] Pkseed, ADRS adrs, byte[] m1) {
        byte[][] tempArrayMaks = { Pkseed, adrs.toByteArray() };
        byte[] mask = mask(Utils.concatenateByteArrays(tempArrayMaks), m1.length);
        byte[] m1X = generateMessageXorMask(m1, mask, m1.length);
        byte[][] tempArray = { Pkseed, adrs.toByteArray(), m1X };
        byte[] result = hash(Utils.concatenateByteArrays(tempArray));
        if (EnvironmentParameters.n < 32) {
            return getFirstNBytes(result);
        }
        return result;
    }

    @Override
    public byte[] H(byte[] Pkseed, ADRS adrs, byte[] m1, byte[] m2) {
        byte[][] tempArrayMaks = { Pkseed, adrs.toByteArray() };
        byte[] mask = mask(Utils.concatenateByteArrays(tempArrayMaks), m1.length);
        byte[] m1X = generateMessageXorMask(m1, mask, m1.length);
        byte[] m2X = generateMessageXorMask(m2, mask, m2.length);
        byte[][] tempArray = { Pkseed, adrs.toByteArray(), m1X, m2X };
        byte[] result = hash(Utils.concatenateByteArrays(tempArray));
        if (EnvironmentParameters.n < 32) {
            return getFirstNBytes(result);
        }
        return result;
    }

    @Override
    public byte[] Hmsg(byte[] R, byte[] Pkseed, byte[] Pkroot, byte[] m) {
        int k = EnvironmentParameters.k;
        int a = EnvironmentParameters.a;
        int h = EnvironmentParameters.h;
        int d = EnvironmentParameters.d;
        byte[][] tempArray = { R, Pkseed, Pkroot, m };
        byte[] tempHash = hash(Utils.concatenateByteArrays(tempArray));
        int len = (int) Math.ceil((k * a + 7) / 8) + (int) Math.ceil((h - h / d + 7) / 8)
                + (int) Math.ceil((h / d + 7) / 8);
        return mask(tempHash, len);
    }

    @Override
    public byte[] PRF(byte[] seed, ADRS adrs) {
        byte[][] tempArray = { seed, adrs.toByteArray() };
        byte[] rand = hash(Utils.concatenateByteArrays(tempArray));
        if (EnvironmentParameters.n < 32) {
            return getFirstNBytes(rand);
        }
        return rand;
    }

    @Override
    public byte[] PRFmsg(byte[] Skprf, byte[] optRand, byte[] m) {
        byte[][] tempArray = { optRand, m };
        byte[] hmac = hmacSHA256(Skprf, Utils.concatenateByteArrays(tempArray));
        if (EnvironmentParameters.n < 32) {
            return getFirstNBytes(hmac);
        }
        return hmac;
    }

    private byte[] hash(byte[] message) {
        byte[] hash = null;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(message);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    private byte[] mask(byte[] seed, int length) {
        MGF1 mgf;
        try {
            mgf = new MGF1(MessageDigest.getInstance("SHA-256"));
            return mgf.generateMask(seed, length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] generateMessageXorMask(byte[] message, byte[] mask, int lenght) {
        return Utils.xorTwoByteArrays(message, mask, lenght);
    }

    private byte[] getFirstNBytes(byte[] bytesInput) {
        byte[] firstNBytes = new byte[EnvironmentParameters.n];
        System.arraycopy(bytesInput, 0, firstNBytes, 0, EnvironmentParameters.n);
        return firstNBytes;
    }

    private byte[] hmacSHA256(byte[] sk, byte[] msg) {
        String algo = "HmacSHA256";
        byte[] hash = null;
        SecretKeySpec key;
        try {
            key = new SecretKeySpec(sk, algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);
            hash = mac.doFinal(msg);
        } catch (NoSuchAlgorithmException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return hash;
    }

}
