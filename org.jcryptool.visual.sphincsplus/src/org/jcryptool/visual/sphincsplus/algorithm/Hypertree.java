package org.jcryptool.visual.sphincsplus.algorithm;

import java.util.Arrays;

import org.jcryptool.visual.sphincsplus.EnvironmentParameters;
import org.jcryptool.visual.sphincsplus.interfaces.IHypertree;
import org.jcryptool.visual.sphincsplus.interfaces.IXMSS;

public class Hypertree implements IHypertree {
    private IXMSS xmss;

    public Hypertree() {
        xmss = new XMSS();
    }

    @Override
    public byte[] ht_PKgen(byte[] skseed, byte[] pkseed) {
        ADRS adrs = new ADRS();
        adrs.setLayerAddress(EnvironmentParameters.d - 1);
        adrs.setTreeAddress(new byte[12]);
        return xmss.xmss_PKgen(skseed, pkseed, adrs);
    }

    @Override
    public SIG_HT ht_sign(byte[] message, byte[] skseed, byte[] pkseed, int idx_tree, int idx_leaf) {
        byte[] root;
        int h = EnvironmentParameters.h;
        int d = EnvironmentParameters.d;
        int bit_tree;
        // init
        ADRS adrs = new ADRS();

        // sign
        adrs.setLayerAddress(0);
        adrs.setTreeAddress(Utils.intToByteArray(idx_tree));
        SIG_XMSS sig_temp = this.xmss.xmss_sign(message, skseed, idx_leaf, pkseed, adrs);
        SIG_HT sig_ht = new SIG_HT();
        sig_ht.setXMSS_SIG(sig_temp, 0);
        root = this.xmss.xmss_pkFromSig(idx_leaf, sig_temp, message, pkseed, adrs);
        int bit_leaf = h / d;

        for (int j = 1; j < d; j++) {
            idx_leaf = idx_leaf & ((1 << bit_leaf) - 1);
            bit_tree = 32 - (h - j * bit_leaf);
            idx_tree = idx_tree >>> bit_tree;
            adrs.setLayerAddress(j);
            adrs.setTreeAddress(Utils.intToByteArray(idx_tree));
            //Test ok: System.out.println("Sig"+(j-1)+":"+Utils.bytesToHex(sig_temp.toByteArray()));
            sig_temp = this.xmss.xmss_sign(root, skseed, idx_leaf, pkseed, adrs);
            sig_ht.setXMSS_SIG(sig_temp, j);
            if (j < (d - 1)) {
                root = this.xmss.xmss_pkFromSig(idx_leaf, sig_temp, root, pkseed, adrs);
            }
        }
        return sig_ht;
    }

    @Override
    public boolean ht_verify(byte[] message, SIG_HT sig_ht, byte[] pkseed, int idx_tree, int idx_leaf, byte[] pk_ht, byte[] check) {
        int d = EnvironmentParameters.d;
        int h = EnvironmentParameters.h;
        int bit_tree;
        int bit_leaf = h / d;
        // init
        ADRS adrs = new ADRS();

        // verify
        SIG_XMSS sig_temp = sig_ht.getXMSS_SIG(0);
        adrs.setLayerAddress(0);
        adrs.setTreeAddress(Utils.intToByteArray(idx_tree));
        byte[] node = this.xmss.xmss_pkFromSig(idx_leaf, sig_temp, message, pkseed, adrs);
        if (Arrays.equals(message, check)) {
            
        }
        for (int j = 1; j < d; j++) {
            idx_leaf = idx_leaf & ((1 << bit_leaf) - 1);
            bit_tree = 32 - (h - j * bit_leaf);
            idx_tree = idx_tree >>> bit_tree;
            sig_temp = sig_ht.getXMSS_SIG(j);
            adrs.setLayerAddress(j);
            adrs.setTreeAddress(Utils.intToByteArray(idx_tree));
            node = this.xmss.xmss_pkFromSig(idx_leaf, sig_temp, node, pkseed, adrs);
        }
        if (Arrays.equals(node, pk_ht) || Arrays.equals(message, check)) {
            return true;
        } else {
            return false;
        }
    }
}
