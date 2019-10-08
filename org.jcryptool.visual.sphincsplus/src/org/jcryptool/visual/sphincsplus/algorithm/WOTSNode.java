package org.jcryptool.visual.sphincsplus.algorithm;

/***
 * This class is a wrapper for the WOTS+-Node returned by WOTSplus.PK_gen
 * 
 * @author akro
 */
public class WOTSNode {

    private byte[] PKseed;
    private byte[][] tmp;
    private ADRS wotspkADRS;

    public WOTSNode(byte[] PKseed, ADRS wotspkADRS, byte[][] tmp) {
        // TODO Auto-generated constructor stub
    }

    public byte[] getPKseed() {
        return PKseed;
    }

    public byte[][] getTmp() {
        return tmp;
    }

    public ADRS getWotspkADRS() {
        return wotspkADRS;
    }

}
