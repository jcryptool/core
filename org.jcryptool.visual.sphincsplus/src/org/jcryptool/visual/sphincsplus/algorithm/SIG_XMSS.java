package org.jcryptool.visual.sphincsplus.algorithm;

import org.jcryptool.visual.sphincsplus.EnvironmentParameters;

public class SIG_XMSS {
    private byte[][] sig;
    private AUTH auth;
    public int length = 0;

    public SIG_XMSS(byte[][] sig, AUTH auth) {
        this.sig = sig;
        this.auth = auth;
        for (int i = 0; i < sig.length; i++) {
            this.length += sig[i].length;
        }
        this.length += auth.length;
    }

    public SIG_XMSS(byte[] array) {
        int w = EnvironmentParameters.w;
        int n = EnvironmentParameters.n;
        this.length = array.length;
        double ldW = Utils.ld(w);
        int hXMSS = EnvironmentParameters.h / EnvironmentParameters.d;
        int len1 = (int) Math.ceil(EnvironmentParameters.n / ldW);
        int len2 = (int) Math.floor(Utils.ld(len1 * (w - 1)) / ldW) + 1;
        int len = len1 + len2;
        int offset = 0;
        sig = new byte[len][n];
        for (int i = 0; i < len; i++) {
            System.arraycopy(array, offset, sig[i], 0, n);
            offset += n;
        }
        byte[] path = new byte[hXMSS * n];
        System.arraycopy(array, offset, path, 0, hXMSS * n);
        this.auth = new AUTH(path, "xmss");
    }

    public byte[] toByteArray() {
        byte[][] tempArray = { Utils.concatenateByteArrays(this.sig), auth.getAUTH() };
        return Utils.concatenateByteArrays(tempArray);
    }

    public byte[][] getWOTSSig() {
        return this.sig;
    }

    public AUTH getXMSSAUTH() {
        return this.auth;
    }
}
