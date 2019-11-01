package org.jcryptool.visual.errorcorrectingcodes.algorithm;

import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mceliece.*;

public class McElieceCrypto {

    private McElieceCipher encryptionCipher;
    private McElieceCipher decryptionCipher;
    private McElieceKeyPairGenerator generator;
    private AsymmetricCipherKeyPair keyPair;

    private McElieceParameters keyParams;
    private McElieceKeyGenerationParameters keyGenParam;
    private byte[] encrypted;
    private byte[] decrypted;

    
    public McElieceCrypto() {
        this(10, 50);
    }

    public McElieceCrypto(int degree, int errors) {
        generator = new McElieceKeyPairGenerator();
        encryptionCipher = new McElieceCipher();
        decryptionCipher = new McElieceCipher();
        setKeyParams(degree, errors);
        init();
    }
    
    public void init() {
        SecureRandom rng = new SecureRandom();
        rng.nextBytes(new byte[20]);
        keyGenParam = new McElieceKeyGenerationParameters(rng, keyParams);
        generator.init(keyGenParam);
        keyPair = generator.generateKeyPair();
        encryptionCipher.init(true, keyPair.getPublic());
        decryptionCipher.init(false, keyPair.getPrivate());
    }
    
    public byte[] encrypt(byte[] message) {
       this.encrypted = encryptionCipher.messageEncrypt(message);
       return this.encrypted;
    }
    
    public byte[] decrypt() throws InvalidCipherTextException {
        if (this.encrypted == null)
            return null;
        this.decrypted = decryptionCipher.messageDecrypt(this.encrypted);
        return this.decrypted;
    }
    
    public void setKeyParams(int m, int t) {
        keyParams = new McElieceCCA2Parameters(m, t);
    }
    
    public int getPoly() {
        return keyParams.getFieldPoly();
    }
    
    public int getCodeLength() {
        return keyParams.getN();
    }
    
    public double getStrength() {
        return Math.pow(2, keyParams.getM());
    }

    public int getPublicKeySize() {
        if (keyPair == null)
            return 0;
        
        return encryptionCipher.getKeySize((McEliecePublicKeyParameters) keyPair.getPublic());
    }
    
    public int getPrivateKeySize() {
        if (keyPair == null)
            return 0;
        
        return encryptionCipher.getKeySize((McEliecePrivateKeyParameters) keyPair.getPrivate());
    }
}
