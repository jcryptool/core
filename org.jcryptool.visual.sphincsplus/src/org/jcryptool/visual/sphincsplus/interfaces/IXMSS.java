package org.jcryptool.visual.sphincsplus.interfaces;

import org.jcryptool.visual.sphincsplus.algorithm.ADRS;
import org.jcryptool.visual.sphincsplus.algorithm.SIG_XMSS;

/**
 * See Chapter 4.1 in the whitepaper
 */
public interface IXMSS {
    /**
     * See NIST paper 4.1.3
     * 
     * @param secret seed skseed
     * @param start index s
     * @param traget node height z
     * @param public seed pkseed
     * @param address adrs
     * @return n-byte root node - top node on Stack
     */
    public byte[] treehash(byte[] skseed, int s, int z, byte[] pkseed, ADRS adrs);

    /**
     * See NIST paper 4.1.4
     * 
     * Generating an XMSS public key.
     * 
     * @param secret seed skseed
     * @param public seed pkseed
     * @param address adrs
     * @return XMSS public key PK
     */
    public byte[] xmss_PKgen(byte[] skseed, byte[] pkseed, ADRS adrs);

    /**
     * See NIST paper 4.1.6
     * 
     * Generating an XMSS signature.
     * 
     * @param n-byte message
     * @param secret seed skseed
     * @param index idx
     * @param public seed pkseed
     * @param address adrs
     * @return XMSS signature SIG_XMSS = (sig || AUTH)
     */
    public SIG_XMSS xmss_sign(byte[] message, byte[] skseed, int idx, byte[] pkseed, ADRS adrs);

    /**
     * See NIST paper 4.1.7
     * 
     * Computing an XMSS public key from an XMSS signature.
     * 
     * @param index idx
     * @param XMSS signature sig_xmss = (sig || AUTH)
     * @param n-byte message
     * @param public seed pkseed
     * @param address adrs
     * @return n-byte root value node [0]
     */
    public byte[] xmss_pkFromSig(int idx, SIG_XMSS sig_xmss, byte[] message, byte[] pkseed, ADRS adrs);

}
