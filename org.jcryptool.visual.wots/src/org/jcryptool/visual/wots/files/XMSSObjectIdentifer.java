package org.jcryptool.visual.wots.files;

/**
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 */
public interface XMSSObjectIdentifer {

    /** XMMS object identifier. OID: 1.3.6.1.4.1.8301.3.1 */
    public static final String XMSS = "1.3.6.1.4.1.8301.3.1";
    /** AES 128 as hash and pseudorandom function. OID: 1.3.6.1.4.1.8301.3.1.42 */
    public static final String XMSS_AES128_AES128 = XMSS + ".42";
    /** SHA 256 as hash and pseudorandom function. OID: 1.3.6.1.4.1.8301.3.1.43 */
    public static final String XMSS_SHA256_SHA256 = XMSS + ".43";
}
