package org.jcryptool.visual.wots.files;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 */
public abstract class OLD_AESPRF extends PseudorandomFunction {

    private final Cipher cipher;

    /**
     * Creates a new AES PRF.
     *
     * @param oid Object identifier
     */
    protected OLD_AESPRF(String oid) {
	super(oid);

	try {
	    cipher = Cipher.getInstance("AES/ECB/NoPadding");
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public byte[] apply(byte[] input, byte[] key) {
	try {
	    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

	    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

	    return cipher.doFinal(input);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public int getLength() {
	return cipher.getBlockSize();
    }

    /**
     * AES 128 pseudorandom function.
     */
    public static final class AES128 extends AESPRF {

	/**
	 * Creates a new AES 128 PRF.
	 */
	public AES128() {
	    super(XMSS_AES128_AES128);
	}
    }
}
