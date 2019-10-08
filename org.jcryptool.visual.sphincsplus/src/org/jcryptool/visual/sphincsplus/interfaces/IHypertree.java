package org.jcryptool.visual.sphincsplus.interfaces;

import org.jcryptool.visual.sphincsplus.algorithm.SIG_HT;

/**
 * See NIST paper 4.2
 * 
 * The SPHINCS+ HT(Hypertree) is a variant of XMSS multi-tree. The trees on top and intermediate
 * layers are used to sign the public keys of the XMSS trees on the respective next layer below.
 * Trees on the lowest layer are used to sign the actual messages, which are the FORS public keys in
 * SPHINCS+. The SPHINCS+ HT consists of total h, that has d layers of XMSS trees of height h' =
 * h/d.
 * 
 * @author Lukas Stelzer
 *
 */
public interface IHypertree {
    /**
     * See NIST paper 4.2.2
     * 
     * The HT public key is the public key (root node) of the single XMSS tree on the top layer.
     * 
     * @param secret seed skseed
     * @param public seed pkseed
     * @return Hypertree public key PK_HT
     */
    public byte[] ht_PKgen(byte[] skseed, byte[] pkseed);

    /**
     * See NIST paper 4.2.4
     * 
     * Generating an HT signature
     * 
     * @param message
     * @param secret seed skseed
     * @param public seed pkseed
     * @param tree index idx_tree
     * @param leaf index idx_leaf
     * @return Hypertree signature sig_ht (SIG_XMSS[] with d layers)
     */
    public SIG_HT ht_sign(byte[] message, byte[] skseed, byte[] pkseed, int idx_tree, int idx_leaf);

    /**
     * See NIST paper 4.2.5
     * 
     * Verifying a HT signature SIG_HT on a message using HT public key pk_HT
     * 
     * @param message
     * @param signature sig_HT
     * @param public seed pkseed
     * @param tree index idx_tree
     * @param leaf index idx_leaf
     * @param Hypertree public key pk_HT
     * @return result of verification as Boolean (true => signature is valid)
     */
    public boolean ht_verify(byte[] message, SIG_HT sig_HT, byte[] pkseed, int idx_tree, int idx_leaf, byte[] pk_HT, byte[] check);

}
