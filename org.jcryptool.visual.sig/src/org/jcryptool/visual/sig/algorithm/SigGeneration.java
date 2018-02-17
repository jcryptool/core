// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sig.algorithm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Arrays;
import java.util.Date;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.sig.listener.SignatureEvent;
import org.jcryptool.visual.sig.listener.SignatureListener;
import org.jcryptool.visual.sig.listener.SignatureListenerAdder;

/**
 * Creates a signature for the input with the selected signature methods.
 * 
 * @author Grebe
 */
public class SigGeneration {
    private static PrivateKey k = null;

    /**
     * Old version of SignInput, calls new version of the method
     */
    public static byte[] signInput(String signaturemethod, byte[] input) throws Exception {
        return signInput(signaturemethod, input, null);
    }

    /**
     * This method signs the data stored in Input.java with the signature method selected by the user. It either uses
     * the user selected key or the key given by jctca (stored in Input.java).
     * 
     * @param signaturemethod Chosen signature method to sign the hash.
     * @param input The Data the user selected
     * @return The signature (byte array)
     * @throws Exception
     */
    public static byte[] signInput(String signaturemethod, byte[] input, KeyStoreAlias alias) throws Exception {
        byte[] signature = null; // Stores the signature

        Input.chosenHash = signaturemethod.replace("withRSA", ""); //$NON-NLS-1$ //$NON-NLS-2$

        if (signaturemethod.contains("ECDSA")) { // Generate a key because there are no ECDSA Keys in the keystore //$NON-NLS-1$
            // Generate a key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC"); //$NON-NLS-1$
            keyGen.initialize(256, SecureRandom.getInstance("SHA1PRNG")); //$NON-NLS-1$

            KeyPair pair = keyGen.generateKeyPair();
            k = pair.getPrivate();
        } else {
            KeyStoreManager ksm = KeyStoreManager.getInstance();

            // Check if called by JCT-CA
            if (Input.privateKey != null) { // Use their key
                Input.privateKey.getAliasString();
                k = ksm.getPrivateKey(Input.privateKey, KeyStoreManager.KEY_PASSWORD);
            } else { // Use own Key from given alias
                k = ksm.getPrivateKey(alias, KeyStoreManager.KEY_PASSWORD);
            }
        }

        // Get a signature object using the specified combo and sign the data with the private key
        Signature sig = Signature.getInstance(signaturemethod);
        sig.initSign(k);
        sig.update(input);
        
        signature = sig.sign();

        if (Input.privateKey != null) {
            String p = null;
            String t = null;
            if (Input.data != null) {
                t = new String(Input.data);
            } else {
                p = Input.path;
            }

            for (SignatureListener lst : SignatureListenerAdder.getListeners()) {
                lst.signaturePerformed(new SignatureEvent(signature, // byte array
                        p, // path
                        t, // direct input
                        new Date(System.currentTimeMillis()), // date time
                        alias, // private key
                        Input.publicKey, // public key
                        Input.chosenHash)); // hash method string
            }
        }

        // Store the generated signature
        Input.signature = signature; // Store the generated original signature
        Input.signatureHex = Input.bytesToHex(signature); // Hex String
        Input.signatureOct = Input.toOctalString(signature, ""); //$NON-NLS-1$
        
        //The first 1024 bytes of data get converted to hex representation
        int maxHexLength = 1024; //1kB
        if (Input.data.length > maxHexLength)
        	Input.dataHex = Input.bytesToHex(Arrays.copyOfRange(Input.data, 0, maxHexLength));
        else
        	Input.dataHex = Input.bytesToHex(Input.data);

        return signature;
    }
}
