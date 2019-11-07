package org.jcryptool.visual.errorcorrectingcodes.algorithm;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mceliece.*;
import org.bouncycastle.util.Arrays;

public class McElieceCrypto {

    private McElieceCipher encryptionCipher;
    private McElieceCipher decryptionCipher;
    private McElieceKeyPairGenerator generator;
    private AsymmetricCipherKeyPair keyPair;

    private McElieceParameters keyParams;
    private McElieceKeyGenerationParameters keyGenParam;
    private ArrayList<byte[]> encrypted;
    private ArrayList<byte[]> decrypted;

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

    public void encrypt(byte[] message) {
        encrypted = new ArrayList<byte[]>();
        if (message.length <= encryptionCipher.maxPlainTextSize)
            this.encrypted.add(encryptionCipher.messageEncrypt(message));
        else {
            int segments = message.length / encryptionCipher.maxPlainTextSize;
            int remainder = (message.length % encryptionCipher.maxPlainTextSize);
            int lower = 0, upper = 0;

            for (int i = 0; i < segments; i++) {
                upper += encryptionCipher.maxPlainTextSize;
                encrypted.add(encryptionCipher.messageEncrypt(Arrays.copyOfRange(message, lower, upper)));
                lower = upper;
            }

            if (remainder != 0) {
                encrypted.add(encryptionCipher.messageEncrypt(Arrays.copyOfRange(message, lower, lower + remainder)));
            }

        }
    }

    public void decrypt() throws InvalidCipherTextException {
        if (this.encrypted == null)
            return;
        
        decrypted = new ArrayList<>();

        for (byte[] cipher : encrypted) {
            byte[] clear = decryptionCipher.messageDecrypt(cipher);
            decrypted.add(clear);
        }
    }

    public void setKeyParams(int m, int t) {
        // avoid recreating with same parameters
        if (keyParams == null || m != keyParams.getM() || t != keyParams.getT())
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

    public int getT() {
        return keyParams.getT();
    }

    public int getM() {
        return keyParams.getM();
    }
    
    public int getMaxMessageSize() {
        return encryptionCipher.maxPlainTextSize;
    }

    public String getEncryptedHex() {
        StringBuilder sb = new StringBuilder();
        encrypted.forEach(cipher -> sb.append(javax.xml.bind.DatatypeConverter.printHexBinary(cipher)));
        return sb.toString();
    }

    public String getClearText() {
        StringBuilder sb = new StringBuilder();
        decrypted.forEach(clear -> sb.append(new String(clear, StandardCharsets.UTF_8)));
        return sb.toString();
    }
    
}
