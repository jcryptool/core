package org.jcryptool.visual.sphincsplus.algorithm;

import org.jcryptool.visual.sphincsplus.EnvironmentParameters;
import org.jcryptool.visual.sphincsplus.interfaces.IHashFunction;
import org.jcryptool.visual.sphincsplus.interfaces.IWOTSplus;

/***
 * WOTS+ implemented as stated in NIST paper chapter 3.
 * 
 * @author akro
 *
 */

public class WOTSplus implements IWOTSplus {

    /***
     * The class Lengths manages the len-parameters defined in NIST paper 3.1.
     * 
     * @author akro
     *
     */
    private static class Lengths {
        private static int len_1, len_2;

        /***
         * see NIST paper 3.1 WOTS+ Parameters
         * 
         * @return len, defined as len_1 + len_2 = ceil(n/log(w)) + floor(log(len_1*(w-1))/log(w))+1
         */
        static int getLen() {
            if (len_1 == 0)
                getLen_1();
            if (len_2 == 0)
                getLen_2();
            return len_1 + len_2;
        }

        /***
         * see NIST paper 3.1 WOTS+ Parameters
         * 
         * @return len_1, defined as ceil(n/log(w))
         */
        static int getLen_1() {
            if (len_1 == 0) {
                len_1 = (int) Math.ceil(EnvironmentParameters.n / Utils.ld(EnvironmentParameters.w));

            }
            return len_1;
        }

        /***
         * see NIST paper 3.1 WOTS+ Parameters
         * 
         * @return len_2, defined as floor(log(len_1*(w-1))/log(w))+1
         */
        static int getLen_2() {
            if (len_2 == 0) {
                len_2 = (int) Math
                        .floor(Utils.ld(getLen_1() * (EnvironmentParameters.w - 1)) / Utils.ld(EnvironmentParameters.w))
                        + 1;
            }
            return len_2;
        }
    }

    private IHashFunction f;

    public WOTSplus() {
        // TODO: change to ENUM instead of String argument. Wait until HashFunction-Class is fixed.
        f = HashFunction.getInstance(EnvironmentParameters.function);
    }

    /***
     * computes an iteration of hash-function F on an n-byte input using a WOTS+ hash address and a
     * public seed.
     * 
     * @param X Input string
     * @param i start index
     * @param s number of steps
     * @param PKseed public key seed
     * @param adrs WOTS+ hash address
     * @return value of F iterated s times on X
     */
    @Override
    public byte[] chain(byte[] X, int i, int s, byte[] PKseed, ADRS adrs) {
        if (s == 0) {
            return X;
        }

        if ((i + s) > (EnvironmentParameters.w - 1)) {
            return null;
        }

        byte[] tmp = chain(X, i, s - 1, PKseed, adrs);
        adrs.setHashAddress(i + s - 1);
        tmp = f.F(PKseed, adrs, tmp);
        return tmp;
    }

    /***
     * @param msg must not be null
     * @param msg_2 must not be null
     * @return concatenation of msg and msg_2
     */
    private int[] concatIntArrays(int[] msg, int[] msg_2) {
        int[] completeMsg = new int[msg.length + msg_2.length];
        System.arraycopy(msg, 0, completeMsg, 0, msg.length);
        System.arraycopy(msg_2, 0, completeMsg, msg.length, msg_2.length);
        return completeMsg;
    }

    /***
     * See NIST paper 3.6<br>
     * In order to verify a WOTS+ signature sig on a message M, the veriﬁer computes a WOTS+ public
     * key value from the signature.
     * 
     * @param sig WOTS+ signatures, len count, n length
     * @param message message
     * @param adrs WOTS+ hash address
     * @return WOTS+ public key pkg_sig derived from sig
     */
    @Override
    public byte[] wots_pkFromSig(byte[][] sig, byte[] message, byte[] PKseed, ADRS adrs) {
        long csum = 0;
        int w = EnvironmentParameters.w;
        byte[] tmp = new byte[Lengths.getLen() * w];
        ADRS wotspkADRS = adrs.deepCopy();
        // convert message to base w
        int[] msg = base_w(message, w, Lengths.getLen_1());
        // compute checksum
        for (int i = 0; i < Lengths.getLen_1(); i++) {
            csum = csum + w - 1 - msg[i];
        }
        // convert csum to base w
        csum = csum << (int) (8 - ((Lengths.getLen_2() * Utils.ld(w)) % 8));
        int len_2_bytes = (int) Math.ceil((Lengths.getLen_2() * Utils.ld(w)) / 8);
        msg = concatIntArrays(msg, base_w(Utils.toByte(csum, len_2_bytes), w, Lengths.getLen_2()));

        for (int i = 0; i < Lengths.getLen(); i++) {
            adrs.setChainAddress(i);
            byte[] chain = chain(sig[i], msg[i], w - 1 - msg[i], PKseed, adrs);
            for (int j = 0; j < w; j++) {
                tmp[i * w + j] = chain[j];
            }
        }

        wotspkADRS.setType(ADRSTypes.WOTS_PK);
        wotspkADRS.setKeyPairAddress(adrs.getKeyPairAddress());
        // TODO: T_LEN(PKseed, wotspkadrs, tmp);
        return f.F(PKseed, wotspkADRS, tmp);
        // return new WOTSNode(PKseed, wotspkADRS, tmp);
    }

    /***
     * See NIST paper 3.4
     * 
     * @param SKseed secret key seed
     * @param PKseed public key seed
     * @param adrs the WOTS+ hash address
     * @return WOTS+ public key pk
     */
    @Override
    public byte[] wots_PKgen(byte[] SKseed, byte[] PKseed, ADRS adrs) {
        // see whitepaper 3.4
        ADRS wotspkADRS = adrs.deepCopy();
        int len = Lengths.getLen();
        int w = EnvironmentParameters.w;
        byte[][] sk = new byte[len][];
        byte[] tmp = new byte[len * w];

        for (int i = 0; i < len; i++) {
            adrs.setChainAddress(i);
            sk[i] = f.PRF(SKseed, adrs);
            byte[] chain = chain(sk[i], 0, w - 1, PKseed, adrs);
            for (int j = 0; j < w; j++) {
                tmp[i * w + j] = chain[j];
            }
        }
        wotspkADRS.setType(ADRSTypes.WOTS_PK);
        wotspkADRS.setKeyPairAddress(adrs.getKeyPairAddress());
        // TODO: T_LEN(PKseed, wotspkadrs, tmp);
        return f.F(PKseed, wotspkADRS, tmp);
    }

    /***
     * see NIST paper 3.5
     * 
     * @param message Message
     * @param SKseed secret key seed
     * @param PKseed public key seed
     * @param adrs WOTS+ hash address
     * @return WOTS+ signatures, len signatures of n length
     */
    @Override
    public byte[][] wots_sign(byte[] message, byte[] SKseed, byte[] PKseed, ADRS adrs) {
        // one signature has n bytes (16-32 bytes)
        int w = EnvironmentParameters.w;

        byte[][] sig = new byte[Lengths.getLen()][];
        long csum = 0;
        // convert message to base w
        int[] msg = base_w(message, w, Lengths.getLen() - 1);
        // compute checksum
        for (int i = 0; i < Lengths.getLen_1(); i++) {
            csum = csum + w - 1 - msg[i];
        }
        // convert csum to base w
        csum = csum << (8 - ((int) ((Lengths.getLen_2() * Utils.ld(w)) % 8)));
        int len_2_bytes = (int) Math.ceil((Lengths.getLen_2() * Utils.ld(w)) / 8);
        // concat 2 int-arrays
        msg = concatIntArrays(msg, base_w(Utils.toByte(csum, len_2_bytes), w, Lengths.getLen_2()));

        for (int i = 0; i < Lengths.getLen(); i++) {
            adrs.setChainAddress(i);
            byte[] tmpBytes = chain(f.PRF(SKseed, adrs), 0, msg[i], PKseed, adrs);
            sig[i] = tmpBytes;
        }
        return sig;
    }

    /***
     * See NIST paper 3.3
     * 
     * @param SKseed secret key seed
     * @param adrs the WOTS+ hash address
     * @return a byte[][], len*SK-byte[]
     */
    @Override
    public byte[][] wots_SKgen(byte[] SKseed, ADRS adrs) {
        // see whitepaper 3.3
        int len = Lengths.getLen();
        byte[][] sk = new byte[len][];
        for (int i = 0; i < len; i++) {
            adrs.setChainAddress(i);
            sk[i] = f.PRF(SKseed, adrs);
        }
        return sk;
    }

    /***
     * Defined NIST paper 2.5<br>
     * "For example, if X is the (big-endian) byte string 0x1234, then base_w(X,16,4) returns the
     * array a = {1,2,3,4}."
     * 
     * @param X byte string
     * @param w must be {4, 16, 256}
     * @param out_len number of elements in the return parameter. A small parameter cuts of the last
     *            elements in the array!
     * @return array of out_len integers between 0 and w-1
     */
    public int[] base_w(byte[] X, int w, int out_len) {
        int in = 0;
        int out = 0;
        int total = 0;
        int bits = 0;
        int[] basew = new int[out_len];
        double ld_w = Utils.ld(w);
        for (int consumed = 0; consumed < out_len; consumed++) {
            if (bits == 0) {
                total = X[in];
                in++;
                bits += 8;
            }
            bits -= ld_w;
            basew[out] = (total >> bits) & (w - 1);
            out++;
        }
        return basew;
    }
}
