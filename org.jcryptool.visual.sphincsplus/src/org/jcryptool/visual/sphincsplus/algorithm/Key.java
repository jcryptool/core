package org.jcryptool.visual.sphincsplus.algorithm;

public class Key {
    private static Key instance;
    private byte[] skseed;
    private byte[] skprf;
    private byte[] pkroot;
    private byte[] pkseed;

    public static Key getInstance() {
        if (instance == null) {
            instance = new Key();
        }
        return instance;
    }

    /***
     * THIS METHOD MUST NOT BE USED OUTSIDE OF A UNIT-TEST!!!
     * 
     * @param k
     */
    public void setKeyInstance(Key k) {
        Key.instance = k;
    }

    public byte[] getSkseed() {
        return skseed;
    }

    public void setSkseed(byte[] skseed) {
        this.skseed = skseed;
    }

    public byte[] getSkprf() {
        return skprf;
    }

    public void setSkprf(byte[] skprf) {
        this.skprf = skprf;
    }

    public byte[] getPkroot() {
        return pkroot;
    }

    public void setPkroot(byte[] pkroot) {
        this.pkroot = pkroot;
    }

    public byte[] getPkseed() {
        return pkseed;
    }

    public void setPkseed(byte[] pkseed) {
        this.pkseed = pkseed;
    }

    private Key() {
    }

}