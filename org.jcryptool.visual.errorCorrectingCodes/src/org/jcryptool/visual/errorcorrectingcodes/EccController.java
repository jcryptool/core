package org.jcryptool.visual.errorcorrectingcodes;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.stream.IntStream;

import org.jcryptool.core.logging.utils.LogUtil;

public class EccController {

    EccData data;

    public EccController(EccData data) {
        super();
        this.data = data;
    }

    public void textAsBinary(String s) {
        data.setBinary(new ArrayList<BitSet>());
        data.setOriginalString(s);
        StringBuilder codeString = new StringBuilder();

        for (byte b : s.getBytes()) {
            if (codeString.length() > 0)
                codeString.append(" ");

            int high = (b & 0xf0) >> 4;
            int low = b & 0xf;
            BitSet bitsHigh = BitSet.valueOf(new byte[] { (byte) high });
            BitSet bitsLow = BitSet.valueOf(new byte[] { (byte) low });
            codeString.append(bitSetToString(bitsHigh)).append(bitSetToString(bitsLow));
            data.getBinary().add(bitsHigh);
            data.getBinary().add(bitsLow);
        }

        data.setBinaryAsString(codeString.toString());
    }

    public void encodeBits() {
        data.setBinaryCode(new ArrayList<BitSet>());
        StringBuilder codeString = new StringBuilder();

        data.getBinary().forEach(b -> {
            if (codeString.length() > 0)
                codeString.append(" ");

            BitSet code = getHamming(b);
            data.getBinaryCode().add(code);
            codeString.append(bitSetToString(code));
        });

        data.setCodeAsString(codeString.toString());
    }

    private String bitSetToString(BitSet bits) {
        String bitString = IntStream.range(0, bits.length()).mapToObj(i -> bits.get(i) ? '1' : '0')
                .collect(() -> new StringBuilder(bits.length()), (buf, character) -> buf.append(character),
                        StringBuilder::append)
                .reverse().toString();

        return bitString;
    }
    
    private BitSet getHamming(BitSet b) {
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
}
