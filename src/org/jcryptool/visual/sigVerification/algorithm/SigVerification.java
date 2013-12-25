package org.jcryptool.visual.sigVerification.algorithm;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Verifies a signature for the input with the selected signature methods.
 * 
 * @author Wilfing
 */
public class SigVerification {
    

    /**
     * 
     */
    public SigVerification(String signaturemethod, byte[] signature, byte[] pubKey, byte[] hashNew) throws Exception {
        
        verifyInput(signaturemethod, signature, pubKey, hashNew);
        
    }
    public static boolean verifyInput(String signaturemethod, byte[] signature, byte[] pubKey, byte[] hashNew){
        try {
            KeyFactory generator = KeyFactory.getInstance(signaturemethod);
            EncodedKeySpec pK = new X509EncodedKeySpec(pubKey);
            PublicKey publicKey = generator.generatePublic(pK);
            Signature s = Signature.getInstance(signaturemethod);
            s.initVerify(publicKey);
            s.update(hashNew);
            return s.verify(signature);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create Public Key from provided encoded keys", e);
        }                
    }
    
}
