package org.jcryptool.visual.wots.files;

import org.jcryptool.visual.wots.files.Function;

/**
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 */
public abstract class OLD_PseudorandomFunction extends Function {

    /**
     * Creates a new PseudorandomFunction.
     *
     * @param oid Object identifier
     */
    protected OLD_PseudorandomFunction(String oid) {
	super(oid);
    }

    /**
     * Applies the function.
     *
     * @param input Input
     * @param key Key
     * @return
     */
    public abstract byte[] apply(byte[] input, byte[] key);
}
