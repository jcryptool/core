package org.jcryptool.visual.sigVerification.algorithm;

import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;

import de.flexiprovider.core.dsa.DSAPrivateKey;
import de.flexiprovider.core.rsa.RSAPrivateCrtKey;
import de.flexiprovider.ec.keys.ECPrivateKey;

/**
 * Verifies the signature of the input with the selected signature method.
 *
 * @author Wilfing
 */
public class SigVerification {
    boolean result = false; // Contains the result of the verification.
    private PublicKey publicKey = null;
    public KeyStoreAlias alias = null;

    /**
     * Chooses the correct function to verify the signature for the input with the selected
     * signature method.
     *
     * @param input A instance of Input
     * @param hash A instance of Hash
     */
    public void verifySignature(Input input, Hash hash) {
        if (input.signaturemethod == "RSA" || input.signaturemethod == "DSA" || input.signaturemethod == "RSA and MGF1" || input.signaturemethod == "ECDSA") {
            if (this.publicKey != null) {
                verifySig(input, hash);
            } else {
                setPublicKey(input);
                verifySig(input, hash);
            }
        }
    }

    /**
     * Sets the RSA and DSA public key. Loads public key from JCT keystore.
     *
     * @param input A instance of Input (contains the signaturemethod)
     */
    public void setPublicKey(Input input) {
        try {
            KeyStoreManager ksm = KeyStoreManager.getInstance();
            Enumeration<String> aliases = ksm.getAliases();
            while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());
                alias.getAliasString();
                if (input.signaturemethod == "RSA" || input.signaturemethod == "RSA and MGF1") { // RSA
                    if (alias.getClassName().equals(RSAPrivateCrtKey.class.getName())) {
                        Certificate cert = ksm.getCertificate(alias);
                        // input.signatureSize = alias.getKeyLength();
                        this.publicKey = cert.getPublicKey();
                    }
                } else if (input.signaturemethod == "DSA") { // DSA
                    if (alias.getClassName().equals(DSAPrivateKey.class.getName())) {
                        // Fill in keys
                        Certificate cert = ksm.getCertificate(alias);
                        this.publicKey = cert.getPublicKey();
                    } // end if
                } else if (input.signaturemethod == "ECDSA") { // ECDSA
                    if (alias.getClassName().equals(ECPrivateKey.class.getName())) {
                        // Fill in keys
                        Certificate cert = ksm.getCertificate(alias);
                        this.publicKey = cert.getPublicKey();
                    } // end if
                }
            }
        } catch (Exception ex) {
            LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
    }

    /**
     * Verifies RSA, DSA (ECDSA) and RSA with MGF1 signatures. Sets the variable result (boolean)
     * TRUE if the signature is correct.
     *
     * @param input A instance of Input (contains the signature, the plaintext, and the
     *            signaturemethod)
     * @param hash A instance of Hash (contains the hashmethod)
     */
    public void verifySig(Input input, Hash hash) {
        try {
            Signature signature;
            if (input.signaturemethod == "RSA and MGF1") {
                signature = Signature.getInstance(hash.hashmethod + "WithRSA/PSS", "BC");
            } else if (input.signaturemethod == "ECDSA") {
                signature = Signature.getInstance(hash.hashmethod + "with" + input.signaturemethod, "FlexiEC");
            } else {
                signature = Signature.getInstance(hash.hashmethod + "with" + input.signaturemethod, "FlexiCore");
            }
            signature.initVerify(this.publicKey);

            // Signatur updaten
            signature.update(input.plain);

            // Signatur ausgeben
            this.result = signature.verify(input.signature);
        } catch (Exception ex) {
            LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
    }

    /**
     * !!! YET NOT IMPLEMENTED !!!
     * Selects the right function to convert the input key from byte array to PublicKey (RSA, DSA, ECDSA).
     *
     * @param pubKeyBytes
     * @param input A instance of Input (contains signaturemethod)
     */
    /*public void publicKeyFile(byte[] pubKeyBytes, Input input) {
        if (input.signaturemethod == "RSA" || input.signaturemethod == "DSA") {
            setDsaRsaPublicKeyFile(pubKeyBytes, input);
        } else {
            ;// ECDSA noch keine Methode zum Einlesen von ECDSA keys gefunden
        }
    }*/

    /**
     * !!! NOT YET IMPLEMENTED!!!
     * Converts the imported key (byte array) in a DSA/RSA public key.
     *
     * @param pubKeyBytes A byte array
     * @param input A instance of Input
     *//*
    public void setDsaRsaPublicKeyFile(byte[] pubKeyBytes, Input input) {

    }*/

    /**
     * Returns the result (boolean).
     *
     * @return result A boolean
     */
    public boolean getResult() {
        return this.result;
    }

    /**
     * Sets the public Key.
     *
     * @param pubKey PublicKey
     */
    public void setPublicKey(PublicKey pubKey) {
        this.publicKey = pubKey;
    }

    /**
     * Resets this instance.
     */
    public void reset() {
        this.result = false;
        this.publicKey = null;
    }
}
