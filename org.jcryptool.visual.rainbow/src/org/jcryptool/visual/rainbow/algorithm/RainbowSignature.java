package org.jcryptool.visual.rainbow.algorithm;

import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.pqc.crypto.rainbow.*;

public class RainbowSignature {
    
    private AsymmetricCipherKeyPair keyPair;
    private RainbowSigner signer;
    private RainbowSigner verifier;
    private byte[] signature;

    public void init() {
        SecureRandom random = new SecureRandom();
        random.nextBytes(new byte[20]);
        RainbowParameters params = new RainbowParameters();
        RainbowKeyGenerationParameters keyGenParam = new RainbowKeyGenerationParameters(random, params);
        
        RainbowKeyPairGenerator keygen = new RainbowKeyPairGenerator();
        keygen.init(keyGenParam);
        
        keyPair = keygen.generateKeyPair();
        signer = new RainbowSigner();
        signer.init(true, keyPair.getPrivate());
        verifier = new RainbowSigner();
        verifier.init(false, keyPair.getPublic());
    }
    
    
    public void sign(byte[] message) {
        signature = signer.generateSignature(message);
    }
    
    public boolean verify(byte[] message) {
        return verifier.verifySignature(message, signature);
    }
    
}
