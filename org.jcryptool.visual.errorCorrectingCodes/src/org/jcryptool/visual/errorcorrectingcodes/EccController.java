package org.jcryptool.visual.errorcorrectingcodes;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;

import org.jcryptool.core.logging.utils.LogUtil;

public class EccController {

    EccData data;

    public EccController(EccData data) {
        super();
        this.data = data;
    }

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

    public void flipBits() {
        data.setErrorCode(new ArrayList<BitSet>());
        StringBuilder codeString = new StringBuilder();

        data.getEncoded().forEach(b -> {
            BitSet e = (BitSet) b.clone();
            e.flip(3);
            data.getErrorCode().add(e);
            codeString.append(bitSetToString(e, 7));
        });
        data.setCodeStringWithErrors(codeString.toString());
    }

    public List<BitSet> getBitErrors() {
        List<BitSet> bits = new ArrayList<>();
        List<BitSet> error = data.getErrorCode();
        for (int i = 0; i < error.size(); i++) {
            bits.add((BitSet) data.getEncoded().get(i).clone());
            bits.get(i).xor(error.get(i));
        }
        return bits;
    }

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

    public void correctErrors() {
        StringBuilder sb = new StringBuilder();
        ArrayList<BitSet> corrected = new ArrayList<>();
        data.getErrorCode().forEach(e -> {
            BitSet c = (BitSet) e.clone();
            BitSet error = new BitSet(3);
            error.set(0,c.get(0) ^ c.get(2) ^ c.get(4) ^ c.get(6));
            error.set(1,c.get(1) ^ c.get(2) ^ c.get(5) ^ c.get(6));
            error.set(2,c.get(3) ^ c.get(4) ^ c.get(5) ^ c.get(6));
            
            if (!error.isEmpty()) {
                c.flip((int) convert(error)-1);
            }
            corrected.add(c);
            sb.append(bitSetToString(c, 7));
        });
        data.setCorrectedString(sb.toString());
    }

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

    public EccData getData() {
        return data;
    }

    public void setData(EccData data) {
        this.data = data;
    }

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

    private static long convert(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return value;
    }
}
