package org.jcryptool.visual.sigVerification.algorithm;

import java.security.MessageDigest;
// import javax.crypto.*;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;

/**
 * Hashes the given input with the selected hash methods.
 * 
 * @author Grebe
 */
public class Hash {
    /**
     * This method hashes an input stored in Input.java with a given hash method
     * 
     * @throws Exception
     * @param hashmethod The name of the method (a string)
     * @return the hash of the input as byte array
     */
    public static byte[] hashInput(String hashmethod, byte[] input) throws Exception {
        byte[] md = null;
        
        try {
            // Get an MD5 message digest object and compute the plaintext digest
            MessageDigest messageDigest = MessageDigest.getInstance(hashmethod); // Argument is a string!
            messageDigest.update(input);
            // Output:
            md = messageDigest.digest();

            Input.hashNew = md; // Store the generated hash
            Input.hashHexNew = Input.bytesToHex(md); // Hex
        } catch (Exception ex) {
            LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
        
        return md;
    }

}