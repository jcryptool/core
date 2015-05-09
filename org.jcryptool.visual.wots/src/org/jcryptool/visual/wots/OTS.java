package org.jcryptool.visual.wots;

/**
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 */
public interface OTS {

    public void generateKeyPair(byte[] seed);

    public void generatePrivateKey(byte[] seed);

    public void generatePublicKey();

    public byte[][] getPrivateKey();

    public byte[][] getPublicKey();

    public byte[] sign(byte[] message);

    public boolean verify(byte[] message, byte[] signature);
}
