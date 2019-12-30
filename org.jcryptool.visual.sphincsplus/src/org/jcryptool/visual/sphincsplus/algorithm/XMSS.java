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

import java.util.Stack;

import org.jcryptool.visual.sphincsplus.EnvironmentParameters;
import org.jcryptool.visual.sphincsplus.interfaces.IHashFunction;
import org.jcryptool.visual.sphincsplus.interfaces.IWOTSplus;
import org.jcryptool.visual.sphincsplus.interfaces.IXMSS;

public class XMSS implements IXMSS {
    private IHashFunction hash;
    private int hXMSS;
    private IWOTSplus wots;

    public XMSS() {
        this.hash = HashFunction.getInstance(EnvironmentParameters.function);
        this.hXMSS = EnvironmentParameters.h / EnvironmentParameters.d;
        this.wots = new WOTSplus();
    }

    @Override
    public byte[] treehash(byte[] skseed, int s, int z, byte[] pkseed, ADRS adrs) {
        int numberOfLeaves = (int) Math.pow(2, z);
        Stack<byte[]> stack = new Stack<byte[]>();
        int offset = 0;
        int index = 0;
        int[] heights = new int[z + 1];
        byte[] node = null;

        if (s % (1 << z) != 0) { // ?algo nimmt immer diesen weg, au�er wenn s = 0?
            return new byte[0];
        }
        for (int i = 0; i < numberOfLeaves; i++) {
            offset++;
            heights[offset - 1] = 0;

            adrs.setType(ADRSTypes.WOTS_HASH);
            index = s + i;
            adrs.setKeyPairAddress(Utils.intToByteArray(index));
            node = this.wots.wots_PKgen(skseed, pkseed, adrs);
            adrs.setType(ADRSTypes.TREE);
            adrs.setTreeIndex(index);
            while (offset >= 2 && heights[offset - 1] == heights[offset - 2]) {
                adrs.setTreeIndex((adrs.getTreeIndex() - 1) / 2);
                node = hash.H(pkseed, adrs, stack.pop(), node);
                adrs.setTreeHeight(adrs.getTreeHeigh() + 1);
                offset--;
                heights[offset - 1]++;
            }
            stack.push(node);
        }
        return stack.pop();
    }

    @Override
    public byte[] xmss_PKgen(byte[] skseed, byte[] pkseed, ADRS adrs) {
        byte[] pk = treehash(skseed, 0, this.hXMSS, pkseed, adrs);
        //Test 
        //System.out.println(Utils.bytesToHex(pk));
        return pk;
    }

    @Override
    public SIG_XMSS xmss_sign(byte[] message, byte[] skseed, int idx, byte[] pkseed, ADRS adrs) {
        AUTH auth = new AUTH("xmss");
        int k;
        int twoSquareJ;
        byte[][] sig = {};
        for (int j = 0; j < this.hXMSS; j++) {
            twoSquareJ = (int) Math.pow(2, j);
            k = (int) Math.floor(idx / twoSquareJ) ^ 1;
            auth.setNode(treehash(skseed, k * twoSquareJ, j, pkseed, adrs), j);
            //System.out.println("sign["+j+"]:"+Utils.bytesToHex(auth.getNode(j)));
        }
        adrs.setType(ADRSTypes.WOTS_HASH);
        adrs.setKeyPairAddress(Utils.intToByteArray(idx));
        sig = this.wots.wots_sign(message, skseed, pkseed, adrs);
        SIG_XMSS sig_xmss = new SIG_XMSS(sig, auth);
        return sig_xmss;
    }

    @Override
    public byte[] xmss_pkFromSig(int idx, SIG_XMSS sig_xmss, byte[] message, byte[] pkseed, ADRS adrs) {
        byte[][] node = new byte[2][];
        int twoSquareK = 0;
        // compute WOTS+ pk from WOTS+ signature
        adrs.setType(ADRSTypes.WOTS_HASH);
        adrs.setKeyPairAddress(Utils.intToByteArray(idx));
        byte[][] sig = sig_xmss.getWOTSSig();
        AUTH auth = sig_xmss.getXMSSAUTH();
        node[0] = this.wots.wots_pkFromSig(sig, message, pkseed, adrs);

        // compute root from WOTS+ pk and AUTH
        adrs.setType(ADRSTypes.TREE);
        adrs.setTreeIndex(idx);
        for (int k = 0; k < this.hXMSS; k++) {
            twoSquareK = (int) Math.pow(2, k);
            adrs.setTreeHeight(k + 1);
            //System.out.println("fromSIG["+k+"]:"+Utils.bytesToHex(auth.getNode(k)));
            if ((Math.floor(idx / twoSquareK) % 2) == 0) {
                adrs.setTreeIndex(adrs.getTreeIndex() / 2);
                node[1] = hash.H(pkseed, adrs, node[0], auth.getNode(k));
            } else {
                adrs.setTreeIndex((adrs.getTreeIndex() - 1) / 2);
                node[1] = hash.H(pkseed, adrs, auth.getNode(k), node[0]);
            }
            node[0] = node[1];
        }
        byte[]pk = node[0];
        //System.out.println(adrs.getLayerAddress());
        //System.out.println(Utils.bytesToHex(pk));
        return pk;
    }

}
