package org.jcryptool.visual.wots.files;

/**
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 */
public abstract class OLD_Function implements XMSSObjectIdentifer {

    private final String oid;

    /**
     * Creates a new Function.
     *
     * @param oid Object identifier
     */
    protected OLD_Function(String oid) {
	this.oid = oid;
    }

    /**
     * Returns the object identifier.
     *
     * @return Object identifier.
     */
    public String getOID() {
	return oid;
    }

    /**
     * Returns the length of the function.
     *
     * @return Length
     */
    public abstract int getLength();
}
