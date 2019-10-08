/*
 * Copyright 2014 Amund Elstad. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.aelstad.keccakj.core;

import java.security.MessageDigest;

import com.github.aelstad.keccakj.io.BitInputStream;
import com.github.aelstad.keccakj.io.BitOutputStream;

public abstract class AbstractKeccackMessageDigest extends MessageDigest {

    KeccackSponge keccackSponge;
    BitOutputStream absorbStream;
    int digestLength;

    /**
     * Security level in bits is min(capacity/2,digestLength*8).
     * 
     * @param algorithm Algorithm name
     * @param capacity Keccack capacity in bits. Must be a multiple of 8.
     * @param digestLength Length of digest in bytes
     * @param domainPadding
     * @param domainPaddingBits
     */
    public AbstractKeccackMessageDigest(String algorithm, int capacityInBits, int digestLength, byte domainPadding,
            int domainPaddingBitLength) {
        super(algorithm);
        this.keccackSponge = new KeccackSponge(capacityInBits, domainPadding, domainPaddingBitLength);

        this.absorbStream = keccackSponge.getAbsorbStream();
        this.digestLength = digestLength;
    }

    @Override
    protected byte[] engineDigest() {
        absorbStream.close();

        byte[] rv = new byte[digestLength];
        BitInputStream bis = keccackSponge.getSqueezeStream();
        bis.read(rv);
        bis.close();

        return rv;
    }

    @Override
    protected void engineReset() {
        keccackSponge.reset();
    }

    public void engineUpdateBits(byte[] bits, long bitOff, long bitLength) {
        absorbStream.writeBits(bits, bitOff, bitLength);
    }

    @Override
    protected void engineUpdate(byte input) {
        absorbStream.write(((int) input));
    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        engineUpdateBits(input, ((long) offset) << 3, ((long) len) << 3);
    }

    public byte[] getRateBits(int boff, int len) {
        return keccackSponge.getRateBits(boff, len);
    }

    public int getRateBits() {
        return keccackSponge.getRateBits();
    }

    @Override
    protected int engineGetDigestLength() {
        return digestLength;
    }

    public KeccackSponge getSponge() {
        return keccackSponge;
    }
}
