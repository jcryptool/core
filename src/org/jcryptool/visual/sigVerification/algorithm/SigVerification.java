package org.jcryptool.visual.sigVerification.algorithm;

import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.PrivateKey;
import java.security.PublicKey;
//import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
//import java.util.Date;

/*import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.sigVerification.listener.SignatureEvent;
import org.jcryptool.visual.sigVerification.listener.SignatureListener;
import org.jcryptool.visual.sigVerification.listener.SignatureListenerAdder;*/

/**
 * Verifies a signature for the input with the selected signature methods.
 * 
 * @author Wilfing
 */
public class SigVerification {
    

    /**
     * 
     */
    public SigVerification(String signaturemethod, byte[] signature, byte[] pubKey, byte[] data) throws Exception {
        
        verifyInput(signaturemethod, signature, pubKey, data);
        
    }
    public static boolean verifyInput(String signaturemethod, byte[] signature, byte[] pubKey, byte[] data){
        try {
            KeyFactory generator = KeyFactory.getInstance(signaturemethod);
            EncodedKeySpec pK = new X509EncodedKeySpec(pubKey);
            PublicKey publicKey = generator.generatePublic(pK);
            Signature s = Signature.getInstance(signaturemethod);
            s.initVerify(publicKey);
            s.update(data);
            return s.verify(signature);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create Public Key from provided encoded keys", e);
        }                
    }
    
}
