// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3.jh;

/**
 * The main JH algorithm.
 *
 * @author Michael Rupp
 */
public class JHAlgorithm {
    protected byte[] bitSequence;
    protected long dataLength;
    protected byte[] hashval;

    public JHAlgorithm(byte[] data) {
        this.bitSequence = data;
        this.dataLength = data.length * 8;
    }

    /**
     * The constant for the Round 0 of E8.
     */
    final static byte roundconstant_zero[] = {0x006, 0x00a, 0x000, 0x009, 0x00e, 0x006, 0x006, 0x007, 0x00f, 0x003,
            0x00b, 0x00c, 0x00c, 0x009, 0x000, 0x008, 0x00b, 0x002, 0x00f, 0x00b, 0x001, 0x003, 0x006, 0x006, 0x00e,
            0x00a, 0x009, 0x005, 0x007, 0x00d, 0x003, 0x00e, 0x003, 0x00a, 0x00d, 0x00e, 0x00c, 0x001, 0x007, 0x005,
            0x001, 0x002, 0x007, 0x007, 0x005, 0x000, 0x009, 0x009, 0x00d, 0x00a, 0x002, 0x00f, 0x005, 0x009, 0x000,
            0x00b, 0x000, 0x006, 0x006, 0x007, 0x003, 0x002, 0x002, 0x00a};

    /**
     * The two Sboxes S0 and S1.
     */
    byte S[][] = { {9, 0, 4, 11, 13, 12, 3, 15, 1, 10, 2, 6, 7, 5, 8, 14},
            {3, 12, 6, 13, 5, 7, 1, 9, 15, 2, 0, 4, 11, 10, 14, 8}};

    /**
     * round function
     */
    void R8(JHHashState state) {
        int i;
        byte tem[] = new byte[256], t;
        /**
         * The round constant expanded into 256 1-bit element.
         */
        byte roundconstant_expanded[] = new byte[256];

        /**
         * Expands the round constant into 256 one-bit element.
         */
        for (i = 0; i < 256; i++)
            roundconstant_expanded[i] = (byte) ((state.roundconstant[i >> 2] >> (3 - (i & 3))) & 1);

        /**
         * S box layer, each constant bit selects one Sbox from S0 and S1.
         */
        for (i = 0; i < 256; i++) {
            /**
             * Constant bits are used to determine which Sbox to use.
             */
            tem[i] = S[roundconstant_expanded[i]][state.A[i]];
        }

        /**
         * L Layer
         */
        for (i = 0; i < 256; i = i + 2) {
            tem[i + 1] ^= (((tem[i] << 1) & 0x00FF) ^ (tem[i] >> 3) ^ ((tem[i] >> 2) & 2)) & 0x00f;
            tem[i] ^= (((tem[i + 1] << 1) & 0x00FF) ^ (tem[i + 1] >> 3) ^ ((tem[i + 1] >> 2) & 2)) & 0x00f;
        }

        /*
         * The following is the permuation layer P_8$ initial swap Pi_8
         */
        for (i = 0; i < 256; i = i + 4) {
            t = tem[i + 2];
            tem[i + 2] = tem[i + 3];
            tem[i + 3] = t;
        }

        /**
         * permutation P'_8
         */
        for (i = 0; i < 128; i = i + 1) {
            state.A[i] = tem[i << 1];
            state.A[i + 128] = tem[(i << 1) + 1];
        }
        /**
         * final swap Phi_8
         */
        for (i = 128; i < 256; i = i + 2) {
            t = state.A[i];
            state.A[i] = state.A[i + 1];
            state.A[i + 1] = t;
        }
    }

    /**
     * Last half round of R8, only Sbox layer in this last half round.
     */
    void last_half_round_R8(JHHashState state) {
        int i;
        /**
         * The round constant expanded into 256 1-bit element.
         */
        byte roundconstant_expanded[] = new byte[256];
        /**
         * Expands the round constant into one-bit element.
         */
        for (i = 0; i < 256; i++) {
            roundconstant_expanded[i] = (byte) ((state.roundconstant[i >> 2] >> (3 - (i & 3))) & 1);
        }

        /**
         * S box layer
         */
        for (i = 0; i < 256; i++) {
            /**
             * Constant bits are used to determine which Sbox to use.
             */
            state.A[i] = S[roundconstant_expanded[i]][state.A[i]];
        }

    }

    /**
     * The following function generates the next round constant from the current round constant; R6 is used for
     * generating round constants for E8, and the round constants of R6 is set as 0;
     */
    void update_roundconstant(JHHashState state) {
        int i;
        byte tem[] = new byte[64], t;

        /**
         * Sbox layer
         */
        for (i = 0; i < 64; i++)
            tem[i] = S[0][state.roundconstant[i]];

        /**
         * Linear transformation layer L
         */
        for (i = 0; i < 64; i = i + 2) {
            tem[i + 1] ^= (((tem[i] << 1) & 0x00FF) ^ (tem[i] >> 3) ^ ((tem[i] >> 2) & 2)) & 0x00f;
            tem[i] ^= (((tem[i + 1] << 1) & 0x00FF) ^ (tem[i + 1] >> 3) ^ ((tem[i + 1] >> 2) & 2)) & 0x00f;
        }

        /**
         * The following is the permutation layer P_6
         */

        /**
         * initial swap Pi_6
         */
        for (i = 0; i < 64; i = i + 4) {
            t = tem[i + 2];
            tem[i + 2] = tem[i + 3];
            tem[i + 3] = t;
        }

        /**
         * permutation P'_6
         */
        for (i = 0; i < 32; i = i + 1) {
            state.roundconstant[i] = tem[i << 1];
            state.roundconstant[i + 32] = tem[(i << 1) + 1];
        }

        /**
         * final swap Phi_6
         */
        for (i = 32; i < 64; i = i + 2) {
            t = state.roundconstant[i];
            state.roundconstant[i] = state.roundconstant[i + 1];
            state.roundconstant[i + 1] = t;
        }
    }

    /**
     * Bijective function E
     */
    void E8(JHHashState state) {
        int i;
        byte t0, t1, t2, t3;
        byte tem[] = new byte[256];

        /**
         * initial group at the begining of E_8: group the H value into 4-bit elements and store them in A
         *
         * t0 is the i-th bit of H t1 is the (i+2^d)-th bit of H t2 is the (i+2*(2^d))-th bit of H t3 is the
         * (i+3*(2^d))-th bit of H
         * */
        for (i = 0; i < 256; i++) {
            t0 = (byte) ((state.H[i >> 3] >> (7 - (i & 7))) & 1);
            t1 = (byte) ((state.H[(i + 256) >> 3] >> (7 - (i & 7))) & 1);
            t2 = (byte) ((state.H[(i + 512) >> 3] >> (7 - (i & 7))) & 1);
            t3 = (byte) ((state.H[(i + 768) >> 3] >> (7 - (i & 7))) & 1);
            tem[i] = (byte) ((((t0 << 3) & 0x00FF) | ((t1 << 2) & 0x00FF) | ((t2 << 1) & 0x00FF) | ((t3 << 0) & 0x00FF)) & 0x00FF);
        }

        for (i = 0; i < 128; i++) {
            state.A[i << 1] = tem[i];
            state.A[(i << 1) + 1] = tem[i + 128];
        }

        /**
         * 35 rounds
         */
        for (i = 0; i < 35; i++) {
            R8(state);
            update_roundconstant(state);
        }

        /**
         * The final half round with only Sbox layer.
         */
        last_half_round_R8(state);

        /**
         * de-group at the end of E_8: decompose the 4-bit elements of A into the 1024-bit H
         */
        for (i = 0; i < 128; i++)
            state.H[i] = 0;

        for (i = 0; i < 128; i++) {
            tem[i] = state.A[i << 1];
            tem[i + 128] = state.A[(i << 1) + 1];
        }

        for (i = 0; i < 256; i++) {
            t0 = (byte) ((tem[i] >> 3) & 1);
            t1 = (byte) ((tem[i] >> 2) & 1);
            t2 = (byte) ((tem[i] >> 1) & 1);
            t3 = (byte) ((tem[i] >> 0) & 1);

            state.H[i >> 3] |= t0 << (7 - (i & 7));
            state.H[(i + 256) >> 3] |= t1 << (7 - (i & 7));
            state.H[(i + 512) >> 3] |= t2 << (7 - (i & 7));
            state.H[(i + 768) >> 3] |= t3 << (7 - (i & 7));
        }

    }

    /**
     * compression function F8
     */
    void F8(JHHashState state) {
        int i;

        /**
         * initialize the round constant
         */
        if (true)

            for (i = 0; i < 64; i++)
                state.roundconstant[i] = roundconstant_zero[i];
        for (i = 0; i < 64; i++)
            state.H[i] ^= state.buffer[i];

        /**
         * Bijective function E8
         */
        E8(state);
        /**
         * Xor the message with the last half of H.
         */
        for (i = 0; i < 64; i++)
            state.H[i + 64] ^= state.buffer[i];
    }

    public void init(JHHashState state, int hashbitlen) {
        int i;

        state.hashbitlen = hashbitlen;

        for (i = 0; i < 64; i++)
            state.buffer[i] = 0;
        for (i = 0; i < 128; i++)
            state.H[i] = 0;

        /**
         * initialize the initial hash value of JH
         */
        state.H[1] = (byte) (hashbitlen & 0x00ff);
        state.H[0] = (byte) ((hashbitlen >> 8) & 0x00ff);

        F8(state);
    }

    public void update(JHHashState state, byte[] data, long databitlen) {
        int i;

        state.databitlen = databitlen;

        /**
         * Compresses the message blocks except the last partial block.
         */
        for (i = 0; (i + 512) <= databitlen; i = i + 512) {
            arrayCopy(state.getBuffer(), (i >> 3), 64, data);
            F8(state);
        }

        /**
         * Storing the partial block into buffer.
         */
        if ((databitlen & 0x1ff) > 0) {
            for (i = 0; i < 64; i++)
                state.buffer[i] = 0;
            if ((databitlen & 7) == 0)
                arrayCopy(state.getBuffer(), (int) ((databitlen >> 9) << 6), (int) (databitlen & 0x1ff) >> 3, data);
            else
                arrayCopy(state.getBuffer(), (int) ((databitlen >> 9) << 6), (int) ((databitlen & 0x1ff) >> 3) + 1,
                        data);
        }
    }

    /**
     * Padding the message, truncate the hash value H and obtain the message digest.
     */
    public void finalize(JHHashState state, byte hashval[]) {
        int i;

        if ((state.databitlen & 0x1ff) == 0) {
            /**
             * Pad the message when databitlen is multiple of 512 bits, then process the padded block.
             */
            for (i = 0; i < 64; i++)
                state.buffer[i] = 0;
            state.buffer[0] = (byte) 0x0080;
            state.buffer[63] = (byte) (state.databitlen & 0x00ff);
            state.buffer[62] = (byte) ((state.databitlen >> 8) & 0x00ff);
            state.buffer[61] = (byte) ((state.databitlen >> 16) & 0x00ff);
            state.buffer[60] = (byte) ((state.databitlen >> 24) & 0x00ff);
            state.buffer[59] = (byte) ((state.databitlen >> 32) & 0x00ff);
            state.buffer[58] = (byte) ((state.databitlen >> 40) & 0x00ff);
            state.buffer[57] = (byte) ((state.databitlen >> 48) & 0x00ff);
            state.buffer[56] = (byte) ((state.databitlen >> 56) & 0x00ff);
            F8(state);
        } else {
            /**
             * Pad and processes the partial block.
             */
            state.buffer[(int) ((state.databitlen & 0x1ff) >> 3)] |= 1 << (7 - (state.databitlen & 7));
            F8(state);
            for (i = 0; i < 64; i++)
                state.buffer[i] = 0;
            /**
             * Precesses the last padded block.
             */
            state.buffer[63] = (byte) (state.databitlen & 0x00ff);
            state.buffer[62] = (byte) ((state.databitlen >> 8) & 0x00ff);
            state.buffer[61] = (byte) ((state.databitlen >> 16) & 0x00ff);
            state.buffer[60] = (byte) ((state.databitlen >> 24) & 0x00ff);
            state.buffer[59] = (byte) ((state.databitlen >> 32) & 0x00ff);
            state.buffer[58] = (byte) ((state.databitlen >> 40) & 0x00ff);
            state.buffer[57] = (byte) ((state.databitlen >> 48) & 0x00ff);
            state.buffer[56] = (byte) ((state.databitlen >> 56) & 0x00ff);
            F8(state);
        }

        /**
         * Trunacting the final hash value to generate the message digest.
         */
        if (state.getHashBitlen() == 224)
            arrayCopy(hashval, 100, 28, state.getH());

        if (state.getHashBitlen() == 256)
            arrayCopy(hashval, 96, 32, state.getH());

        if (state.getHashBitlen() == 384)
            arrayCopy(hashval, 80, 48, state.getH());

        if (state.getHashBitlen() == 512)
            arrayCopy(hashval, 64, 64, state.getH());
    }

    /**
     * hash the message
     */
    public byte[] hash(int hashbitlen) {
        this.hashval = new byte[hashbitlen];
        JHHashState state = new JHHashState(hashbitlen, this.dataLength);
        if (hashbitlen == 224 || hashbitlen == 256 || hashbitlen == 384 || hashbitlen == 512) {
            init(state, hashbitlen);
            update(state, bitSequence, dataLength);
            finalize(state, hashval);
            state.setHash(hashval);
        }
        return state.getHash();
    }

    /**
     * copies an array
     *
     * @return
     */
    public byte[] arrayCopy(byte[] array, int index, int bytes, byte[] data) {
        if (array.length >= bytes && data.length >= (index + bytes)) {
            for (int i = 0; i < bytes; i++) {
                array[i] = data[index + i];
            }
        }
        return array;
    }
}
