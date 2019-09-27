package org.jcryptool.visual.sphincsplus;

/***
 * Parameters are set from GUI-selection, read from algorithm
 */
public class EnvironmentParameters {
    /***
     * See NIST paper 3.1<br>
     * n is the security parameter of WOTS+.
     */
    public static int n = 16;
    /***
     * See NIST paper 3.1<br>
     * w is the Winternitz parameter. Defined as a element of {4, 16, 256}
     */
    public static int w = 16;
    public static int h = 64;
    public static int d = 8;
    public static int k = 10;
    public static int a = 15; // ld(t)=a
    /***
     * See NIST Paper 6.4<br>
     * This global var is used to add a random factor to SPHINCS+, otherwise SPHINCS+ is always
     * deterministic.
     */
    public static boolean RANDOMIZE = false;

    public static HashFunctionType function = HashFunctionType.SHA_256;

}
