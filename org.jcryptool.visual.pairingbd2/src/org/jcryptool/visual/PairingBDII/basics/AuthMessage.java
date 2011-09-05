//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.PairingBDII.basics;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;

import org.jcryptool.core.logging.utils.LogUtil;

public class AuthMessage {
    private final String message;

    public AuthMessage(String message) {
        this.message = message;
    }

    public SignedObject sign(PrivateKey key) {
        SignedObject so = null;
        try {
            final Serializable o = message;
            final Signature sig = Signature.getInstance("SHA1withRSA"); //$NON-NLS-1$
            so = new SignedObject(o, key, sig);
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(e);
        } catch (final SignatureException e) {
            LogUtil.logError(e);
        } catch (final InvalidKeyException e) {
            LogUtil.logError(e);
        } catch (final IOException e) {
            LogUtil.logError(e);
        }

        return so;

    }

    public boolean verify(PublicKey pkey, SignedObject so) {
        boolean b = false;
        try {
            final Signature sig = Signature.getInstance("SHA1withRSA"); //$NON-NLS-1$
            b = so.verify(pkey, sig);

        } catch (final SignatureException e) {
            LogUtil.logError(e);
        } catch (final InvalidKeyException e) {
            LogUtil.logError(e);
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(e);
        }

        return b;

    }
}
