// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.algorithm;

import java.security.PublicKey;
import java.security.Signature;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;

/**
 * Verifies the signature of the input with the selected signature method.
 *
 * @author Wilfing
 */
public class SigVerification {
    boolean result = false; // Contains the result of the verification.

    /**
     * Chooses the correct function to verify the signature for the input with the selected
     * signature method.
     *
     * @param input A instance of Input
     * @param hash A instance of Hash
     */
    public void verifySignature(Input input, Hash hash) {
        if (input.signaturemethod == "RSA" || input.signaturemethod == "DSA" || input.signaturemethod == "RSA and MGF1" || input.signaturemethod == "ECDSA") {
            if (input.publicKeyAlias != null) {
                verifySig(input, hash);
            }
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
//                signature = Signature.getInstance(hash.hashmethod + "WithRSA/PSS", "BC");
            	signature = Signature.getInstance(hash.hashmethod + "WithRSA/PSS");
            } else if (input.signaturemethod == "ECDSA") {
//                signature = Signature.getInstance(hash.hashmethod + "with" + input.signaturemethod, "FlexiEC");
            	signature = Signature.getInstance(hash.hashmethod + "with" + input.signaturemethod);
                
            } else {
//                signature = Signature.getInstance(hash.hashmethod + "with" + input.signaturemethod, "FlexiCore");
                signature = Signature.getInstance(hash.hashmethod + "with" + input.signaturemethod);
            }
            PublicKey key = KeyStoreManager.getInstance().getCertificate(input.publicKeyAlias).getPublicKey();
            signature.initVerify(key);

            // Signatur updaten
            signature.update(input.plain);

            // Signatur ausgeben
            this.result = signature.verify(input.signature);
        } catch (Exception ex) {
            LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
    }

    /**
     * Returns the result (boolean).
     *
     * @return result A boolean
     */
    public boolean getResult() {
        return this.result;
    }

    /**
     * Resets this instance.
     */
    public void reset() {
        this.result = false;
    }
}
