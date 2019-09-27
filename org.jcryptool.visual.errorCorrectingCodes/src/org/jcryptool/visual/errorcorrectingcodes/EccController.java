/*
 * @author Daniel Hofmann
 */
package org.jcryptool.visual.errorcorrectingcodes;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.errorcorrectingcodes.data.EccData;

/**
 * The Class EccController.
 */

//TODO replace BitSet with matrix calc
public class EccController {

    EccData data;

    /**
     * Instantiates a new ecc controller.
     *
     * @param data the data
     */
    public EccController(EccData data) {
        super();
        this.data = data;
    }

    /**
     * Convert the original string to a list of 'BitSet's and create a string representation of the
     * bits.
     */

    public void textAsBinary() {
        String s = data.getOriginalString();
        StringBuilder codeString = new StringBuilder();

        if (s != null) {
            data.setBinary(new ArrayList<BitSet>());
            for (byte b : s.getBytes()) {
                if (codeString.length() > 0)
                    codeString.append(" ");

                int high = (b & 0xf0) >> 4;
                int low = b & 0xf;
                BitSet bitsHigh = BitSet.valueOf(new byte[] { (byte) high });
                BitSet bitsLow = BitSet.valueOf(new byte[] { (byte) low });
                codeString.append(bitSetToString(bitsHigh, 4)).append(bitSetToString(bitsLow, 4));
                data.getBinary().add(bitsHigh);
                data.getBinary().add(bitsLow);
            }
        }
        data.setBinaryAsString(codeString.toString());
    }

    /**
     * Encode the original bitsets and store it in the according BitSet list. Also creates the
     * string representation of the encoded bits.
     */
    public void encodeBits() {
        data.setEncoded(new ArrayList<BitSet>());
        StringBuilder codeString = new StringBuilder();

        data.getBinary().forEach(b -> {

            BitSet code = calcHamming(b);
            data.getEncoded().add(code);
            codeString.append(bitSetToString(code, 7));
        });

        data.setCodeAsString(codeString.toString());
    }

    /**
     * Flip random bits in the encoded BitSets and store it in the according list.
     */
    public void flipBits() {
        SecureRandom rand = new SecureRandom();
        data.setErrorCode(new ArrayList<BitSet>());
        StringBuilder codeString = new StringBuilder();

        data.getEncoded().forEach(b -> {
            int error = rand.nextInt(7);

            BitSet e = (BitSet) b.clone();
            e.flip(error);
            data.getErrorCode().add(e);
            codeString.append(bitSetToString(e, 7));
        });
        data.setCodeStringWithErrors(codeString.toString());
    }

    /**
     * Gets the difference between the encoded and falsified BitSets.
     *
     * @return the errors as a bitset
     */
    public List<BitSet> getBitErrors() {
        List<BitSet> bits = new ArrayList<>();
        List<BitSet> error = data.getErrorCode();
        for (int i = 0; i < error.size(); i++) {
            bits.add((BitSet) data.getEncoded().get(i).clone());
            bits.get(i).xor(error.get(i));
        }
        return bits;
    }

    /**
     * Convert a BitSet object to string value.
     *
     * @param bits the bits
     * @param offset the offset
     * @return the string
     */
    private String bitSetToString(BitSet bits, int offset) {
        StringBuilder bitString = IntStream.range(0, bits.length()).mapToObj(i -> bits.get(i) ? '1' : '0').collect(
                () -> new StringBuilder(bits.length()), (buf, character) -> buf.append(character),
                StringBuilder::append);

        // add trail 0's
        if (offset != 0)
            for (int j = bitString.length(); j < offset; j++) {
                bitString.append("0");
            }

        return bitString.reverse().toString();
    }

    /**
     * Correct errors by parity check and set the corrected BitSet and string.
     */
    public void correctErrors() {
        StringBuilder sb = new StringBuilder();
        ArrayList<BitSet> corrected = new ArrayList<>();
        data.getErrorCode().forEach(e -> {
            BitSet c = (BitSet) e.clone();
            BitSet error = new BitSet(3);
            error.set(0, c.get(0) ^ c.get(2) ^ c.get(4) ^ c.get(6));
            error.set(1, c.get(1) ^ c.get(2) ^ c.get(5) ^ c.get(6));
            error.set(2, c.get(3) ^ c.get(4) ^ c.get(5) ^ c.get(6));

            if (!error.isEmpty()) {
                c.flip((int) convert(error) - 1);
            }
            corrected.add(c);
            sb.append(bitSetToString(c, 7));
        });
        data.setCorrectedString(sb.toString());
        // TODO actually decode bit string
        data.setDecodedString(data.getBinaryAsString());
    }

    /**
     * Encode a bitset by adding parity bits according to Hamming(7,4) code.
     *
     * @param b the input
     * @return the encoded bitset
     */
    private BitSet calcHamming(BitSet b) {
        BitSet encoded = new BitSet(7);
        encoded.set(0, b.get(0) ^ b.get(1) ^ b.get(3));
        encoded.set(1, b.get(0) ^ b.get(2) ^ b.get(3));
        encoded.set(2, b.get(0));
        encoded.set(3, b.get(1) ^ b.get(2) ^ b.get(3));
        encoded.set(4, b.get(1));
        encoded.set(5, b.get(2));
        encoded.set(6, b.get(3));

        return encoded;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public EccData getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data the new data
     */
    public void setData(EccData data) {
        this.data = data;
    }

    /**
     * Convert long to BitSet.
     *
     * @param value the long value
     * @return the bitset
     */
    private static BitSet convert(long value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    /**
     * Convert bitset to long.
     *
     * @param bits the bitset object
     * @return the long value
     */
    private static long convert(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return value;
    }

}
