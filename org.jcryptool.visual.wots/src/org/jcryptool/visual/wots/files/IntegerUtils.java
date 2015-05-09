package org.jcryptool.visual.wots.files;

import java.math.BigInteger;

/**
 * A set of utility functions for integers.
 *
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 */
public class IntegerUtils {

    /**
     * Convert an integer to a byte array.
     *
     * @param value integer to be converted
     * @return byte[]
     */
    public static byte[] toByteArray(int value) {
	return ByteUtils.cutLeadingNullBytes(BigInteger.valueOf(value).toByteArray());
    }
}
