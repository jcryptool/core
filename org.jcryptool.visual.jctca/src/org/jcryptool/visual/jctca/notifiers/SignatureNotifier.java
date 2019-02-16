//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.notifiers;

import java.util.Date;

import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.Signature;
import org.jcryptool.visual.sig.algorithm.Input;
import org.jcryptool.visual.sig.listener.SignatureEvent;
import org.jcryptool.visual.sig.listener.SignatureListener;

/**
 * used by the sigvis plugin to notify this plugin if a signature was done successfully
 * 
 * @author mmacala
 * 
 */
public class SignatureNotifier implements SignatureListener {
    @Override
    public void signaturePerformed(SignatureEvent e) {
        Signature signature = new Signature(e.getSignature(), e.getPath(), e.getText(), new Date(
                System.currentTimeMillis()), e.getPrivAlias(), e.getPubAlias(), e.getHashAlgorithm());
        CertificateCSRR.getInstance().addSignature(signature);
        Input.privateKeyJCTCA = null;
        Input.publicKey = null;
    }
}
