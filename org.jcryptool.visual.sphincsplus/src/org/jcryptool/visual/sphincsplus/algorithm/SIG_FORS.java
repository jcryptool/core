package org.jcryptool.visual.sphincsplus.algorithm;

import org.jcryptool.visual.sphincsplus.EnvironmentParameters;

public class SIG_FORS {
    private byte[][] sk;
    private AUTH[] auth;
    public int length = 0;

    public SIG_FORS(byte[] sigfors) {
        int k = EnvironmentParameters.k;
        int n = EnvironmentParameters.n;
        int a = EnvironmentParameters.a;
        int offset = 0;
        this.length = sigfors.length;
        this.sk = new byte[k][n];
        this.auth = new AUTH[k];
        byte[] temp = new byte[a * n];
        for (int i = 0; i < k; i++) {
            System.arraycopy(sigfors, offset, sk[i], 0, n);
            offset += n;
            System.arraycopy(sigfors, offset, temp, 0, a * n);
            auth[i] = new AUTH(temp, "fors");
            offset += a * n;
        }
    }

    public SIG_FORS() {
        this.sk = new byte[EnvironmentParameters.k][];
        this.auth = new AUTH[EnvironmentParameters.k];
    }

    public void setSK(byte[] sk, int index) {
        this.sk[index] = sk;
        this.length += sk.length;
    }

    public void setAUTH(AUTH auth, int index) {
        this.auth[index] = auth;
        this.length += auth.length;
    }

    public byte[] getSK(int index) {
        return this.sk[index];
    }

    public byte[][] getSK() {
        return this.sk;
    }

    public AUTH[] getAUTH() {
        return this.auth;
    }

    public AUTH getAUTH(int index) {
        return this.auth[index];
    }

    public byte[] toByteArray() {
        byte[] result = {};
        for (int i = 0; i < EnvironmentParameters.k; i++) {
            byte[][] tempArray = { result, sk[i], auth[i].getAUTH() };
            result = Utils.concatenateByteArrays(tempArray);
        }
        return result;
    }
}
