package org.jcryptool.visual.wots.files;

/**
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 */
public class MathUtils {

    /**
     * Calculates the log base 2 of x.
     *
     * @param x Value x
     * @return Log base 2 of x
     */
    public static double log2(int x) {
	return (Math.log10(x) / Math.log10(2));
    }
}
