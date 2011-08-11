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

public class Skein512Small {
    public static void hash(byte[] m, byte[] h) {
        long[] c = new long[9], t = new long[] {32, 0xc4L << 56, 0};
        byte[] b = new byte[64], d = "ND3EAJ.;1QDGLXV)G>B8-1*R9=GK(6XC".getBytes();
        System.arraycopy("SHA3\1\0\0\0\0\2".getBytes(), 0, b, 0, 10);
        block(c, t, b, 0, d);
        t[0] = 0;
        t[1] = 0x70L << 56;
        for (; t[0] < m.length - 64; t[1] = 0x30L << 56)
            block(c, t, m, (int) (t[0] += 64) - 64, d);
        System.arraycopy(m, (int) t[0], b = new byte[64], 0, m.length - (int) t[0]);
        t[0] = m.length;
        t[1] |= 0x80L << 56;
        block(c, t, b, 0, d);
        t[0] = 8;
        t[1] = 0xffL << 56;
        block(c, t, new byte[64], 0, d);
        for (int i = 0; i < 64; i++)
            h[i] = (byte) (c[i / 8] >> (i & 7) * 8);
    }

    private static void block(long[] c, long[] t, byte[] b, int o, byte[] d) {
        long[] x = new long[8], k = new long[8];
        c[8] = 0x5555555555555555L;
        for (int i = 0; i < 8; x[i] = k[i] + c[i], c[8] ^= c[i], i++)
            for (int j = 7; j >= 0; j--)
                k[i] = (k[i] << 8) + (b[o + i * 8 + j] & 255);
        x[5] += t[0];
        x[6] += t[1];
        t[2] = t[0] ^ t[1];
        for (int r = 1, p = 0; r <= 18; r++, p ^= 16) {
            for (int i = 0; i < 16; i++) {
                int m = 2 * ((i + (1 + i + i) * (i / 4)) & 3);
                int n = (1 + i + i) & 7, s = d[p + i] - 32;
                x[n] = ((x[n] << s) | (x[n] >>> (64 - s))) ^ (x[m] += x[n]);
            }
            for (int i = 0; i < 8; i++)
                x[i] += c[(r + i) % 9];
            x[5] += t[r % 3];
            x[6] += t[(r + 1) % 3];
            x[7] += r;
        }
        for (int i = 0; i < 8; i++)
            c[i] = k[i] ^ x[i];
    }
}
