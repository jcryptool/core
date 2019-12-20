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
  
    public int getNumLayers() {
        return privateKey.getVi().length;
    }

    public int[] getVi() {
        return privateKey.getVi();
    }
    
    public RainbowPrivateKeyParameters getPrivateKeyParams() {
        return privateKey;
    }

    public RainbowPublicKeyParameters getPublicKey() {
        return publicKey;
    }
    
    
}
