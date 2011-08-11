// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3.skein.algorithm;

import java.util.Arrays;

public class Skein512 { // block function constants
    private static final int R00 = 46, R01 = 36, R02 = 19, R03 = 37;
    private static final int R10 = 33, R11 = 27, R12 = 14, R13 = 42;
    private static final int R20 = 17, R21 = 49, R22 = 36, R23 = 39;
    private static final int R30 = 44, R31 = 9, R32 = 54, R33 = 56;
    private static final int R40 = 39, R41 = 30, R42 = 34, R43 = 24;
    private static final int R50 = 13, R51 = 50, R52 = 10, R53 = 17;
    private static final int R60 = 25, R61 = 29, R62 = 39, R63 = 43;
    private static final int R70 = 8, R71 = 35, R72 = 56, R73 = 22;
    // version 1, id-string "SHA3"
    private static final long SCHEMA_VERSION = 0x133414853L;
    private static final long T1_FLAG_FINAL = 1L << 63;
    private static final long T1_FLAG_FIRST = 1L << 62;
    private static final long T1_FLAG_BIT_PAD = 1L << 55;
    private static final long T1_POS_TYPE = 56;
    private static final long TYPE_CONFIG = 4L << T1_POS_TYPE;
    private static final long TYPE_MESSAGE = 48L << T1_POS_TYPE;
    private static final long TYPE_OUT = 63L << T1_POS_TYPE;
    private static final int WORDS = 8, BYTES = 8 * WORDS;
    private static final int ROUNDS = 72;
    private static final long KS_PARITY = 0x5555555555555555L;
    private static final int[] MOD3 = new int[ROUNDS];
    private static final int[] MOD9 = new int[ROUNDS];
    static {
        for (int i = 0; i < MOD3.length; i++) {
            MOD3[i] = i % 3;
            MOD9[i] = i % 9;
        }
    }
    private static Skein512 INITIALIZED = new Skein512(512);
    // size of hash result, in bits
    private int hashBitCount;
    // current byte count in the buffer
    private int byteCount;
    // tweak words: tweak0=byte count, tweak1=flags
    private long tweak0, tweak1;
    // chaining variables
    private long[] x = new long[WORDS];
    // partial block buffer (8-byte aligned)
    private byte[] buffer = new byte[BYTES];
    // key schedule: tweak
    private long[] tweakSchedule = new long[5];
    // key schedule: chaining variables
    private long[] keySchedule = new long[17];

    // build/process the configuration block (only done once)
    private Skein512(int hashBitCount) {
        this.hashBitCount = hashBitCount;
        startNewType(TYPE_CONFIG | T1_FLAG_FINAL);
        // set the schema, version
        long[] w = new long[] {SCHEMA_VERSION, hashBitCount};
        // compute the initial chaining values from the configuration block
        setBytes(buffer, w, 2 * 8);
        processBlock(buffer, 0, 1, 4 * WORDS);
        // the chaining vars (x) are now initialized for the given hashBitLen.
        // set up to process the data message portion of the hash (default)
        // buffer starts out empty
        startNewType(TYPE_MESSAGE);
    }

    private Skein512() {
        hashBitCount = INITIALIZED.hashBitCount;
        tweak0 = INITIALIZED.tweak0;
        tweak1 = INITIALIZED.tweak1;
        System.arraycopy(INITIALIZED.x, 0, x, 0, x.length);
    }

    /**
     * Calculate the hash code of the given message. Each bit in the message is processed.
     *
     * @param msg the message
     * @param digest the resulting hash code
     */
    public static void hash(byte[] msg, byte[] digest) {
        hash(msg, msg.length * 8, digest);
    }

    /**
     * Calculate the hash code of the given message.
     *
     * @param msg the message
     * @param bitCount the number of bits to process
     * @param digest the resulting hash code
     */
    public static void hash(byte[] msg, int bitCount, byte[] digest) {
        int byteCount = bitCount >>> 3;
        if ((bitCount & 7) != 0) {
            int mask = 1 << (7 - (bitCount & 7));
            msg[byteCount] = (byte) ((msg[byteCount] & (-mask)) | mask);
            byteCount++;
        }
        Skein512 instance = new Skein512();
        instance.update(msg, byteCount);
        if ((bitCount & 7) != 0) {
            instance.tweak1 |= T1_FLAG_BIT_PAD;
        }
        instance.finalize(digest);
    }

    // process the input bytes
    private void update(byte[] msg, int len) {
        int pos = 0;
        // process full blocks, if any
        if (len + byteCount > BYTES) {
            // finish up any buffered message data
            if (byteCount != 0) {
                // # bytes free in buffer
                int n = BYTES - byteCount;
                if (n != 0) {
                    System.arraycopy(msg, 0, buffer, byteCount, n);
                    len -= n;
                    pos += n;
                    byteCount += n;
                }
                processBlock(buffer, 0, 1, BYTES);
                byteCount = 0;
            }
            // now process any remaining full blocks,
            // directly from input message data
            if (len > BYTES) {
                // number of full blocks to process
                int n = (len - 1) / BYTES;
                processBlock(msg, pos, n, BYTES);
                len -= n * BYTES;
                pos += n * BYTES;
            }
        }
        // copy any remaining source message data bytes into the buffer
        if (len != 0) {
            System.arraycopy(msg, pos, buffer, byteCount, len);
            byteCount += len;
        }
    }

    // finalize the hash computation and output the result
    private void finalize(byte[] hash) {
        // tag as the final block
        tweak1 |= T1_FLAG_FINAL;
        // zero pad if necessary
        if (byteCount < BYTES) {
            Arrays.fill(buffer, byteCount, BYTES, (byte) 0);
        }
        // process the final block
        processBlock(buffer, 0, 1, byteCount);
        // now output the result
        // zero out the buffer, so it can hold the counter
        Arrays.fill(buffer, (byte) 0);
        // up to 512 bits are supported
        // build the counter block
        startNewType(TYPE_OUT | T1_FLAG_FINAL);
        // run "counter mode"
        processBlock(buffer, 0, 1, 8);
        // "output" the counter mode bytes
        setBytes(hash, x, (hashBitCount + 7) >> 3);
    }

    // set up for starting with a new type
    private void startNewType(long type) {
        tweak0 = 0;
        tweak1 = T1_FLAG_FIRST | type;
    }

    private void processBlock(byte[] block, int off, int blocks, int bytes) {
        while (blocks-- > 0) {
            // this implementation supports 2**64 input bytes (no carry out here)
            // update processed length
            long[] ts = tweakSchedule;
            tweak0 += bytes;
            int[] mod3 = MOD3;
            int[] mod9 = MOD9;
            ts[3] = ts[0] = tweak0;
            ts[4] = ts[1] = tweak1;
            ts[2] = tweak0 ^ tweak1;
            long[] c = x;
            long[] ks = keySchedule;
            // pre-compute the key schedule for this block
            System.arraycopy(c, 0, ks, 0, 8);
            System.arraycopy(c, 0, ks, 9, 8);
            ks[8] = KS_PARITY ^ c[7] ^ c[0] ^ c[1] ^ c[2] ^ c[3] ^ c[4] ^ c[5] ^ c[6];
            // do the first full key injection
            long x0 = (c[0] = getLong(block, off)) + ks[0];
            long x1 = (c[1] = getLong(block, off + 8)) + ks[1];
            long x2 = (c[2] = getLong(block, off + 16)) + ks[2];
            long x3 = (c[3] = getLong(block, off + 24)) + ks[3];
            long x4 = (c[4] = getLong(block, off + 32)) + ks[4];
            long x5 = (c[5] = getLong(block, off + 40)) + ks[5] + tweak0;
            long x6 = (c[6] = getLong(block, off + 48)) + ks[6] + tweak1;
            long x7 = (c[7] = getLong(block, off + 56)) + ks[7];
            // unroll 8 rounds
            for (int r = 1; r <= ROUNDS / 4; r += 2) {
                int rm9 = mod9[r], rm3 = mod3[r];
                x1 = rotlXor(x1, R00, x0 += x1);
                x3 = rotlXor(x3, R01, x2 += x3);
                x5 = rotlXor(x5, R02, x4 += x5);
                x7 = rotlXor(x7, R03, x6 += x7);
                x1 = rotlXor(x1, R10, x2 += x1);
                x7 = rotlXor(x7, R11, x4 += x7);
                x5 = rotlXor(x5, R12, x6 += x5);
                x3 = rotlXor(x3, R13, x0 += x3);
                x1 = rotlXor(x1, R20, x4 += x1);
                x3 = rotlXor(x3, R21, x6 += x3);
                x5 = rotlXor(x5, R22, x0 += x5);
                x7 = rotlXor(x7, R23, x2 += x7);
                x1 = rotlXor(x1, R30, x6 += x1) + ks[rm9 + 1];
                x7 = rotlXor(x7, R31, x0 += x7) + ks[rm9 + 7] + r;
                x5 = rotlXor(x5, R32, x2 += x5) + ks[rm9 + 5] + ts[rm3];
                x3 = rotlXor(x3, R33, x4 += x3) + ks[rm9 + 3];
                x1 = rotlXor(x1, R40, x0 += x1 + ks[rm9]);
                x3 = rotlXor(x3, R41, x2 += x3 + ks[rm9 + 2]);
                x5 = rotlXor(x5, R42, x4 += x5 + ks[rm9 + 4]);
                x7 = rotlXor(x7, R43, x6 += x7 + ks[rm9 + 6] + ts[rm3 + 1]);
                x1 = rotlXor(x1, R50, x2 += x1);
                x7 = rotlXor(x7, R51, x4 += x7);
                x5 = rotlXor(x5, R52, x6 += x5);
                x3 = rotlXor(x3, R53, x0 += x3);
                x1 = rotlXor(x1, R60, x4 += x1);
                x3 = rotlXor(x3, R61, x6 += x3);
                x5 = rotlXor(x5, R62, x0 += x5);
                x7 = rotlXor(x7, R63, x2 += x7);
                x1 = rotlXor(x1, R70, x6 += x1) + ks[rm9 + 2];
                x7 = rotlXor(x7, R71, x0 += x7) + ks[rm9 + 8] + r + 1;
                x5 = rotlXor(x5, R72, x2 += x5) + ks[rm9 + 6] + ts[rm3 + 1];
                x3 = rotlXor(x3, R73, x4 += x3) + ks[rm9 + 4];
                x0 += ks[rm9 + 1];
                x2 += ks[rm9 + 3];
                x4 += ks[rm9 + 5];
                x6 += ks[rm9 + 7] + ts[rm3 + 2];
            }
            // do the final "feed forward" xor, update context chaining vars
            c[6] ^= x6;
            c[4] ^= x4;
            c[0] ^= x0;
            c[1] ^= x1;
            c[2] ^= x2;
            c[3] ^= x3;
            c[5] ^= x5;
            c[7] ^= x7;
            // clear the start bit
            tweak1 &= ~T1_FLAG_FIRST;
            off += BYTES;
        }
    }

    private long rotlXor(long x, int n, long xor) {
        return ((x << n) | (x >>> (64 - n))) ^ xor;
    }

    private void setBytes(byte[] dst, long[] src, int byteCount) {
        for (int n = 0, i = 0; n < byteCount; n += 8, i++) {
            long x = src[i];
            dst[n] = (byte) x;
            dst[n + 1] = (byte) (x >> 8);
            dst[n + 2] = (byte) (x >> 16);
            dst[n + 3] = (byte) (x >> 24);
            dst[n + 4] = (byte) (x >> 32);
            dst[n + 5] = (byte) (x >> 40);
            dst[n + 6] = (byte) (x >> 48);
            dst[n + 7] = (byte) (x >> 56);
        }
    }

    private long getLong(byte[] b, int i) {
        if (i >= b.length + 8) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (((b[i] & 255) + ((b[i + 1] & 255) << 8) + ((b[i + 2] & 255) << 16) + ((b[i + 3] & 255) << 24)) & 0xffffffffL)
                + (((b[i + 4] & 255) + ((b[i + 5] & 255) << 8) + ((b[i + 6] & 255) << 16) + ((b[i + 7] & 255L) << 24)) << 32);
    }
}
