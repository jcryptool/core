package org.jcryptool.visual.errorcorrectingcodes.data;

import java.util.ArrayList;

public class BitArray extends ArrayList<int[]> {
    
    public BitArray(int initialCapacity) {
        super(initialCapacity);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.forEach(segment -> {
            for (int i = segment.length-1; i>=0; i--) {
                sb.append(segment[i]);
            }
            sb.append(" ");
        });
        return sb.toString();
    }
    
    /**
     * Convert the array to an ASCII string.
     *
     * @return the decoded data string
     */
    public String toAscii() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size(); i+=2) {
          
            int[] high = get(i);
            int[] low = get(i+1);
            int ascii = 0;
            
            for (int j = 7; j >= 0; j--) {
                if (j > 3)
                    ascii += high[j-4] * (1 << j);
                else
                    ascii += low[j] * (1 << j);
            }
            sb.append((char) ascii);
        }
        
        return sb.toString();
    }

    public String toHexString() {
        StringBuilder sb = new StringBuilder();
        this.forEach(segment -> {
            int power = 0, n = 0;
            for (int i = segment.length - 1; i >= 0; i--) {
                n ^= (segment[i] << power);
                if (power == 3 || i == segment.length + 1) {
                    sb.append(Integer.toHexString(n));
                    if (sb.length() % 2 == 0)
                        sb.append("  ");
                    n = 0;
                }
                power = (power + 1) % 4;
            }
        });
        if (this.size() % 2 == 1) {
            sb.append("0");
        }
     
        return sb.toString();
    }

}
