package org.jcryptool.visual.sphincsplus.algorithm;

import org.jcryptool.visual.sphincsplus.HashFunctionType;
import org.jcryptool.visual.sphincsplus.interfaces.IHashFunction;

public class HashFunction implements IHashFunction {
    private static HashFunction instance;
    private IHashFunction hashalgorithm = null;

    /*
     * public HashFunction(HashFunctionType type) { //TODO build to algorithm switch (type) { case
     * SHA_256: this.hashalgorithm = new SHA256(); break; case SHAKE_256: this.hashalgorithm = new
     * SHAKE256(); break; default: break; } }
     */
    @Override
    public byte[] F(byte[] Pkseed, ADRS adrs, byte[] m1) {
        return hashalgorithm.F(Pkseed, adrs, m1);
    }

    @Override
    public byte[] H(byte[] Pkseed, ADRS adrs, byte[] m1, byte[] m2) {
        return hashalgorithm.H(Pkseed, adrs, m1, m2);
    }

    @Override
    public byte[] Hmsg(byte[] R, byte[] Pkseed, byte[] Pkroot, byte[] m) {
        return hashalgorithm.Hmsg(R, Pkseed, Pkroot, m);
    }

    @Override
    public byte[] PRF(byte[] seed, ADRS adrs) {
        return hashalgorithm.PRF(seed, adrs);
    }

    @Override
    public byte[] PRFmsg(byte[] Skprf, byte[] optRand, byte[] m) {
        return hashalgorithm.PRFmsg(Skprf, optRand, m);
    }

    public static HashFunction getInstance(HashFunctionType type) {
        if (instance == null) {
            instance = new HashFunction(type);
        }
        return instance;
    }
    
    private HashFunction(HashFunctionType type) {
        switch (type) {
        case SHA_256:
            this.hashalgorithm = new SHA256();
            break;
        case SHAKE_256:
            this.hashalgorithm = new SHAKE256();
            break;
        default:
            break;
        }
    }
}
