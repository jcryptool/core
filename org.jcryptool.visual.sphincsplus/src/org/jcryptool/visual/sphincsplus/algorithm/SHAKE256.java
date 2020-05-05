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

import org.jcryptool.visual.sphincsplus.EnvironmentParameters;
import org.jcryptool.visual.sphincsplus.interfaces.IHashFunction;

import com.github.aelstad.keccakj.fips202.Shake256;

/**
 * See NIST paper 7.2.1
 * 
 * Implementations of the several SHAKE256 Hash-functions.
 * 
 * @author Lukas Stelzer
 *
 */
public class SHAKE256 implements IHashFunction {

    @Override
    public byte[] F(byte[] Pkseed, ADRS adrs, byte[] m1) {
        byte[][] tempArray1 = { Pkseed, adrs.toByteArray() };
        byte[] m1X = generateMessageXorMask(m1, Utils.concatenateByteArrays(tempArray1), m1.length);
        byte[][] tempArray2 = { Pkseed, adrs.toByteArray(), m1X };
        return hash(Utils.concatenateByteArrays(tempArray2), EnvironmentParameters.n);
    }

    @Override
    public byte[] H(byte[] Pkseed, ADRS adrs, byte[] m1, byte[] m2) {
        byte[][] tempArray1 = { Pkseed, adrs.toByteArray() };
        byte[] m1X = generateMessageXorMask(m1, Utils.concatenateByteArrays(tempArray1), m1.length);
        byte[] m2X = generateMessageXorMask(m2, Utils.concatenateByteArrays(tempArray1), m2.length);
        byte[][] tempArray2 = { Pkseed, adrs.toByteArray(), m1X, m2X };
        return hash(Utils.concatenateByteArrays(tempArray2), EnvironmentParameters.n);
    }

    @Override
    public byte[] Hmsg(byte[] R, byte[] Pkseed, byte[] Pkroot, byte[] m) {
        int k = EnvironmentParameters.k;
        int d = EnvironmentParameters.d;
        int a = EnvironmentParameters.a;
        int h = EnvironmentParameters.h;
        byte[][] tempArray = { R, Pkseed, Pkroot, m };
        int len = (int) Math.ceil((k * a + 7) / 8) + (int) Math.ceil((h - h / d + 7) / 8)
                + (int) Math.ceil((h / d + 7) / 8);
        return hash(Utils.concatenateByteArrays(tempArray), len);
    }

    @Override
    public byte[] PRF(byte[] seed, ADRS adrs) {
        byte[][] tempArray = { seed, adrs.toByteArray() };
        return hash(Utils.concatenateByteArrays(tempArray), EnvironmentParameters.n);
    }

    @Override
    public byte[] PRFmsg(byte[] Skprf, byte[] optRand, byte[] m) {
        byte[][] tempArray = { Skprf, optRand, m };
        return hash(Utils.concatenateByteArrays(tempArray), EnvironmentParameters.n);
    }

    /**
     * @param message
     * @param lenght in Bytes
     * @return SHAKE256 hash of the message
     */
    private byte[] hash(byte[] message, int lenght) {
        Shake256 shakeInstance = new Shake256();
        shakeInstance.getAbsorbStream().write(message);
        byte[] digest = new byte[lenght];
        shakeInstance.getSqueezeStream().read(digest);
        shakeInstance.reset();
        return digest;
    }

    private byte[] generateMessageXorMask(byte[] message, byte[] seed, int lenght) {
        byte[] tempHash = hash(seed, message.length);
        return Utils.xorTwoByteArrays(message, tempHash, message.length);
    }
}
