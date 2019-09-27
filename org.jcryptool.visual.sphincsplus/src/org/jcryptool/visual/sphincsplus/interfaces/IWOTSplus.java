package org.jcryptool.visual.sphincsplus.interfaces;

import org.jcryptool.visual.sphincsplus.algorithm.ADRS;

public interface IWOTSplus {
    public byte[] chain(byte[] X, int i, int s, byte[] PKseed, ADRS adrs);

    public byte[][] wots_SKgen(byte[] SKseed, ADRS adrs);

    public byte[] wots_PKgen(byte[] SKseed, byte[] PKseed, ADRS adrs);

    public byte[][] wots_sign(byte[] message, byte[] SKseed, byte[] PKseed, ADRS adrs);

    public byte[] wots_pkFromSig(byte[][] sig, byte[] message, byte[] PKseed, ADRS adrs);

}
