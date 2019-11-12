/*
 * @author Daniel Hofmann
 */
package org.jcryptool.visual.errorcorrectingcodes.algorithm;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.RuntimeCryptoException;
import org.bouncycastle.pqc.crypto.mceliece.*;
import org.bouncycastle.util.Arrays;
import org.jcryptool.core.logging.utils.LogUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class McElieceCrypto.
 */
public class McElieceCrypto {

    /** The Constant MAX_M. Complexity increases exponentially with larger M */
    private static final int MAX_M = 13;
    
    /** The encryption cipher. */
    private McElieceCipher encryptionCipher;
    
    /** The decryption cipher. */
    private McElieceCipher decryptionCipher;
    
    /** The generator. */
    private McElieceKeyPairGenerator generator;
    
    /** The key pair. */
    private AsymmetricCipherKeyPair keyPair;

    /** The key params. */
    private McElieceParameters keyParams;
    
    /** The key gen param. */
    private McElieceKeyGenerationParameters keyGenParam;
    
    /** The encrypted. */
    private ArrayList<byte[]> encrypted;
    
    /** The decrypted. */
    private ArrayList<byte[]> decrypted;

    /**
     * Instantiates a new mc eliece crypto.
     */
    public McElieceCrypto() {
        this(8, 1);
    }

    /**
     * Instantiates a new mc eliece crypto.
     *
     * @param degree the degree
     * @param errors the errors
     */
    public McElieceCrypto(int degree, int errors) {
        generator = new McElieceKeyPairGenerator();
        encryptionCipher = new McElieceCipher();
        decryptionCipher = new McElieceCipher();
        setKeyParams(degree, errors);
        init();
    }

    /**
     * Initialize the key pair and cipher system.
     */
    public void init() {
        SecureRandom rng = new SecureRandom();
        rng.nextBytes(new byte[20]);
        
        keyGenParam = new McElieceKeyGenerationParameters(rng, keyParams);
        generator.init(keyGenParam);
        keyPair = generator.generateKeyPair();
        encryptionCipher.init(true, keyPair.getPublic());
        decryptionCipher.init(false, keyPair.getPrivate());
        
        if (encryptionCipher.maxPlainTextSize == 0)
            throw new RuntimeCryptoException("Maximum plaintext size is 0.");
    }

    /**
     * Encrypt.
     *
     * @param message the message
     */
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

    /**
     * Decrypt.
     *
     * @throws InvalidCipherTextException the invalid cipher text exception
     */
    public void decrypt() throws InvalidCipherTextException {
        if (this.encrypted == null)
            return;
        
        decrypted = new ArrayList<>();

        for (byte[] cipher : encrypted) {
            byte[] clear = decryptionCipher.messageDecrypt(cipher);
            decrypted.add(clear);
        }
    }
    
    public void sign(byte[] message) {
    }

    /**
     * Sets the key params.
     *
     * @param m the m
     * @param t the t
     */
    public void setKeyParams(int m, int t) {
        // avoid recreating with same parameters
        if (keyParams == null || m != keyParams.getM() || t != keyParams.getT()) {
            keyParams = new McElieceCCA2Parameters(m, t);
            init();
        }
    }

    /**
     * Gets the poly.
     *
     * @return the poly
     */
    public int getPoly() {
        return keyParams.getFieldPoly();
    }

    /**
     * Gets the code length.
     *
     * @return the code length
     */
    public int getCodeLength() {
        return keyParams.getN();
    }

    /**
     * Gets the strength.
     *
     * @return the strength
     */
    public double getStrength() {
        return Math.pow(2, keyParams.getM());
    }

    /**
     * Gets the public key size.
     *
     * @return the public key size
     */
    public int getPublicKeySize() {
        if (keyPair == null)
            return 0;
        
        McEliecePublicKeyParameters pubKey = (McEliecePublicKeyParameters) keyPair.getPublic();
        return encryptionCipher.getKeySize(pubKey);
    }

    /**
     * Gets the private key size.
     *
     * @return the private key size
     */
    public int getPrivateKeySize() {
        if (keyPair == null)
            return 0;
        return encryptionCipher.getKeySize((McEliecePrivateKeyParameters) keyPair.getPrivate());
    }

    /**
     * Gets the t.
     *
     * @return the t
     */
    public int getT() {
        return keyParams.getT();
    }

    /**
     * Gets the m.
     *
     * @return the m
     */
    public int getM() {
        return keyParams.getM();
    }
    
    /**
     * Gets the max message size.
     *
     * @return the max message size
     */
    public int getMaxMessageSize() {
        return encryptionCipher.maxPlainTextSize;
    }

    /**
     * Gets the encrypted hex.
     *
     * @return the encrypted hex
     */
    public String getEncryptedHex() {
        StringBuilder sb = new StringBuilder();
        encrypted.forEach(cipher -> sb.append(javax.xml.bind.DatatypeConverter.printHexBinary(cipher)));
        return sb.toString();
    }

    /**
     * Gets the clear text.
     *
     * @return the clear text
     */
    public String getClearText() {
        StringBuilder sb = new StringBuilder();
        decrypted.forEach(clear -> sb.append(new String(clear, StandardCharsets.UTF_8)));
        return sb.toString();
    }
    
    
    /**
     * Gets the valid values for M.
     *
     * @return string array with values from 6 to M
     */
    public String[] getValidMValues() {
        String[] mValues =  new String[MAX_M-5];
        for (int i = 0; i < mValues.length; i++) {
            mValues[i] = String.valueOf(i+6);
        }
        return mValues;
    }
    
}
