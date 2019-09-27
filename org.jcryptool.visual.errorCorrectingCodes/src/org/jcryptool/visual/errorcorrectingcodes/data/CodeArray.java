package org.jcryptool.visual.errorcorrectingcodes.data;

import java.util.ArrayList;

public class CodeArray extends ArrayList<int[]> {

    public CodeArray(int initialCapacity) {
        super(initialCapacity);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.forEach(segment -> {
            //add leading 0's to make strings of equal length
            for (int pad = 0; pad < segment.length % 4; pad++)
                sb.append("0");
                
            for (int i = 0; i < segment.length; i++) {
                sb.append(segment[i]);
            }
            sb.append(" ");
        });
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
