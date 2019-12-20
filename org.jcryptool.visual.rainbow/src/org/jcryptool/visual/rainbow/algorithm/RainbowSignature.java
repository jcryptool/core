/*
 * @author Daniel Hofmann
 */
package org.jcryptool.visual.rainbow.algorithm;

import java.security.SecureRandom;
import java.util.Arrays;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.pqc.crypto.DigestingMessageSigner;
import org.bouncycastle.pqc.crypto.rainbow.*;

public class RainbowSignature {

    private AsymmetricCipherKeyPair keyPair;
    private DigestingMessageSigner signer;
    private DigestingMessageSigner verifier;
    private RainbowPrivateKeyParameters privateKey;
    private RainbowPublicKeyParameters publicKey;

    
    /**
     * Instantiates a new rainbow signature with default parameters.
     */
    public RainbowSignature() {
        init(new RainbowParameters());
    }
    
    /**
     * Instantiates a new rainbow signature with a given number of Vinegar variables.
     * The length of the array is the number of layers while the integer values define the number of coefficients per layer, e.g. vi = int[]{6, 12, 17, 22, 33}
     */
    public RainbowSignature(int[] vi) {
        init(new RainbowParameters(vi));
    }

    private void init(RainbowParameters params) {
        SecureRandom random = new SecureRandom();
        random.nextBytes(new byte[20]);
        RainbowKeyGenerationParameters keyGenParam = new RainbowKeyGenerationParameters(random, params);
        
        RainbowKeyPairGenerator keygen = new RainbowKeyPairGenerator();
        keygen.init(keyGenParam);

        keyPair = keygen.generateKeyPair();
        privateKey = (RainbowPrivateKeyParameters) keyPair.getPrivate();
        publicKey = (RainbowPublicKeyParameters) keyPair.getPublic();
        signer = new DigestingMessageSigner(new RainbowSigner(), new SHA224Digest());
        signer.init(true, keyPair.getPrivate());

        verifier = new DigestingMessageSigner(new RainbowSigner(), new SHA224Digest());
        verifier.init(false, keyPair.getPublic());
    }
    
    
    /**
     * Compute and return the signature for a message.
     *
     * @param message the message string as byte array
     * @return the signature byte array
     */
    public byte[] sign(byte[] message) {
        signer.update(message, 0, message.length);
        return signer.generateSignature();
    }

    /**
     * Verify a message with the previously generated signature.
     *
     * @param message the message
     * @param signature the signature
     * @return true, if successful; false if either array is null
     */
    public boolean verify(byte[] message, byte[] signature) {
        if (message == null || signature == null) return false;
        
        verifier.update(message, 0, message.length);
        return verifier.verifySignature(signature);
    }

    public String getVars() {
        StringBuilder sb = new StringBuilder();
        sb.append("----Private Key----\n")
            .append("Doc length: ").append(privateKey.getDocLength()).append("\n")
            .append("Number of Layers: ").append(privateKey.getVi().length).append("\n")
            .append("Vi per Layer: ").append(arrayToString(privateKey.getVi())).append("\n")
            .append("B1: ").append(arrayToString(privateKey.getB1())).append("\n")
            .append("B2: ").append(arrayToString(privateKey.getB2())).append("\n")
            .append("InvA1: ").append(arrayToString(privateKey.getInvA1())).append("\n")
            .append("InvA2: ").append(arrayToString(privateKey.getInvA2())).append("\n");

        sb.append("\n----Public Key----\n")
            .append("Doc length: ").append(publicKey.getDocLength()).append("\n")
            .append("Coeff Quadratic: ").append(arrayToString(publicKey.getCoeffQuadratic())).append("\n")
            .append("Coeff Scalar: ").append(arrayToString(publicKey.getCoeffScalar())).append("\n")
            .append("Coeff Singlar: ").append(arrayToString(publicKey.getCoeffSingular()));

        return sb.toString();
    }

    public String arrayToString(short[][] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dimension: [").append(arr.length).append("][").append(arr[0].length).append("]\n");
        int count = 0;
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[r].length; c++) {
                sb.append(arr[r][c]).append(" ");
                count++;
                if (count > 10)
                  break;
            }
            
        }
        sb.append("...");
        return sb.toString();
    }

    public String arrayToString(short[] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dimension: [").append(arr.length).append("]\n");
        for (int r = 0; r < arr.length; r++) {
            sb.append(arr[r]).append(" ");
        }
        return sb.toString();
    }

    public String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < arr.length; r++) {
            sb.append(arr[r]).append(" ");
        }
        return sb.toString();
    }

    public int getNumLayers() {
        return privateKey.getVi().length;
    }

    public int[] getVi() {
        return privateKey.getVi();
    }
    
    public RainbowPrivateKeyParameters getPrivateKeyParams() {
        return privateKey;
    }
}
