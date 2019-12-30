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

import java.math.BigInteger;
import java.util.Stack;
import org.jcryptool.visual.sphincsplus.EnvironmentParameters;
import org.jcryptool.visual.sphincsplus.interfaces.IFORS;
import org.jcryptool.visual.sphincsplus.interfaces.IHashFunction;

public class FORS implements IFORS {
    private SIG_FORS signature = new SIG_FORS();
    private byte[] sk;
    private IHashFunction hash;

    public FORS() {
        this.hash = HashFunction.getInstance(EnvironmentParameters.function);
    }

    @Override
    public byte[] fors_SKgen(byte[] skseed, ADRS adrs, int idx) {
        adrs.setTreeHeight(0);
        adrs.setTreeIndex(idx);
        this.sk = hash.PRF(skseed, adrs);
        return this.sk;
    }

    /**
     * s = start index z = target node height
     */
    @Override
    public byte[] fors_treehash(byte[] skseed, int s, int z, byte[] pkseed, ADRS adrs) {
        int numberOfLeaves = (int) Math.pow(2, z);
        Stack<byte[]> stack = new Stack<byte[]>();
        int offset = 0;
        int[] heights = new int[z + 1];
        byte[] node = null;

        if (s % (1 << z) != 0) { // ?algo nimmt immer diesen weg, au�er wenn s = 0? return new
            return new byte[EnvironmentParameters.n];
        }

        for (int i = 0; i < numberOfLeaves; i++) {
            offset++;
            heights[offset - 1] = 0;

            adrs.setTreeHeight(0);
            adrs.setTreeIndex(s + i);
            this.sk = hash.PRF(skseed, adrs);
            node = hash.F(pkseed, adrs, this.sk);
            adrs.setTreeHeight(1);
            adrs.setTreeIndex(s + i);
            while (offset >= 2 && heights[offset - 1] == heights[offset - 2]) {
                adrs.setTreeIndex((adrs.getTreeIndex() - 1) / 2);
                node = hash.H(pkseed, adrs, stack.pop(), node);
                adrs.setTreeHeight(adrs.getTreeHeigh() + 1);
                offset--;
                heights[offset - 1]++;
            }
            stack.push(node);
        }
        byte[] result = stack.pop();
        return result;
    }

    /*
     * @Override public byte[] fors_PKgen(byte[] skseed, byte[] pkseed, ADRS adrs) { byte[] pk; ADRS
     * forspkADRS = adrs; int k = EnvironmentParameters.k; int a = EnvironmentParameters.a; byte[][]
     * root = new byte[k][]; for (int i = 0; i < k; i++) { root[i] = fors_treehash(skseed, i * k, a,
     * pkseed, adrs); } forspkADRS.setType(ADRSTypes.FORS_ROOTS);
     * forspkADRS.setKeyPairAddress(adrs.getKeyPairAddress()); // TODO: evtl T_k() == F() nicht
     * korrekt pk = hash.F(pkseed, forspkADRS, Utils.concatenateByteArrays(root)); // TEST //
     * System.out.println("\n\nForsPKgen: " + Utils.bytesToHex(pk)); return pk; }
     */

    @Override
    public SIG_FORS fors_sign(byte[] message, byte[] skseed, byte[] pkseed, ADRS adrs) {
        int k = EnvironmentParameters.k;
        int a = EnvironmentParameters.a;
        int t = (int) Math.pow(2, a);
        int twoSquareJ = 0;
        int s = 0;

        for (int i = 0; i < k; i++) {
            AUTH auth = new AUTH("fors");
            // setIndices(idx, message); //like C-Reference-Code
            int idx = calculate_idx(message, i * a, a);

            adrs.setTreeHeight(0);
            // adrs.setTreeIndex(i * t + idx[i]);
            adrs.setTreeIndex(i * t + idx);
            signature.setSK(hash.PRF(skseed, adrs), i);

            // like NIST-PAPER
            for (int j = 0; j < a; j++) {
                twoSquareJ = (1 << j);
                s = (int) Math.floor(idx / twoSquareJ) ^ 1;
                auth.setNode(fors_treehash(skseed, i * t + s * twoSquareJ, j, pkseed, adrs), j);
            }

            signature.setAUTH(auth, i);
        }
        return signature;
    }

    @Override
    public byte[] fors_pkFromSig(SIG_FORS sig_fors, byte[] message, byte[] pkseed, ADRS adrs) {
        int a = EnvironmentParameters.a;
        int t = (int) Math.pow(2, a);
        int idx = 0;
        byte[] pk;
        byte[][] node = new byte[2][];
        byte[][] root = new byte[EnvironmentParameters.k][];
        byte[] sk;
        AUTH auth;
        for (int i = 0; i < EnvironmentParameters.k; i++) {
            // get next index
            // setIndices(this.idx, message); old
            idx = calculate_idx(message, i * a, a);

            // compute leaf
            sk = sig_fors.getSK(i);
            adrs.setTreeHeight(0);
            adrs.setTreeIndex(i * t + idx);
            node[0] = hash.F(pkseed, adrs, sk);

            // compute root from leaf and AUTH
            auth = sig_fors.getAUTH(i);
            adrs.setTreeIndex(idx);
            for (int j = 0; j < EnvironmentParameters.a; j++) {
                adrs.setTreeHeight(j + 1);
                if ((Math.floor(idx / Math.pow(2, j)) % 2) == 0) {
                    adrs.setTreeIndex(adrs.getTreeIndex() / 2);
                    node[1] = hash.H(pkseed, adrs, node[0], auth.getNode(j));
                } else {
                    adrs.setTreeIndex((adrs.getTreeIndex() - 1) / 2);
                    node[1] = hash.H(pkseed, adrs, auth.getNode(j), node[0]);
                }
                node[0] = node[1];
            }
            root[i] = node[0];
        }
        ADRS forspkADRS = adrs;
        forspkADRS.setType(ADRSTypes.FORS_ROOTS);
        forspkADRS.setKeyPairAddress(adrs.getKeyPairAddress());
        // TODO: evtl T_K() == F() nicht korrekt
        pk = hash.F(pkseed, forspkADRS, Utils.concatenateByteArrays(root));
        // TEST
        // System.out.println("\n\nForsPKfromSIG: " + Utils.bytesToHex(pk));
        return pk;
    }

    /***
     * This function takes a byte array to and calculates and integer value <br>
     * of the range defined by the length relativ to bitstart
     * 
     * @param input a byte array, must no be null
     * @param bitstart bit-index, the start of the bitstring
     * @param length the length of the bitstring. MUST NOT be bigger than EnvironmentParameters.a !
     * @return an integer calculated from the defined bit-range
     */
    private int calculate_idx(byte[] input, int bitstart, int length) {
        if (length == 0) {
            return 0;
        }
        if ((bitstart / 8) > input.length) {
            return 0;
        }

        byte[] tmp = new byte[length / 8 + 1];
        int rem = length % 8;
        // bitmask to get the first/last bits of the input-fields
        // the first 0xFF-AND is to trim the int-value to byte-limits
        int firstBits = 0xFF & (0xFF << (8 - rem));
        int lastBits = 0xFF & (0xFF >> (rem));

        // to right-align the fields in the tmp-array, we have to take the first (8-rem)-bits of the
        // current input-field and the last rem-bits of the next input-field
        int input_i = (bitstart + length) / 8;
        for (int tmp_i = tmp.length - 1; tmp_i >= 0; tmp_i--) {
            // take the first bits of the right field, move them to the right
            tmp[tmp_i] = (byte) ((input[input_i] & firstBits) >> (8 - rem));
            if (tmp_i > 0) {
                // take the last bits of the left field, move them to the left
                tmp[tmp_i] ^= (input[input_i - 1] & lastBits) << rem;
            }
            input_i--;
        }
        tmp[0] &= (1 << 8) - 1; // remove first bit

        // calculate t
        BigInteger i = new BigInteger(tmp);
        // System.out.println(i.toString());
        if (i.intValue() > (1 << EnvironmentParameters.a)) {
            // i is bigger than t: this behaviour is undefined
            return -1;
        }
        return i.intValue();
    }

}
