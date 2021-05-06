// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.certificates;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;

import codec.asn1.ASN1BitString;
import codec.asn1.ASN1ObjectIdentifier;
import codec.x509.X509Certificate;
import codec.x509.X509Extension;
import codec.x509.X509TBSCertificate;

public class CertificateFactory {
    public static X509Certificate createJCrypToolCertificate(PublicKey publicKey) {
        X509TBSCertificate certificate = new X509TBSCertificate();
        try {
            certificate.setSubjectPublicKey(publicKey);

            Calendar calendar = Calendar.getInstance();

            // not before 1.1.2000
            calendar.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
            certificate.setNotBefore(calendar);
            // not after 31.12.2049
            calendar.clear();
            calendar.set(2049, Calendar.DECEMBER, 31, 0, 0, 0);
            certificate.setNotAfter(calendar);

            X500Principal subjectDN = new X500Principal(
                    "CN=JCrypTool, C=Germany, ST=Hessen, L=Frankfurt, O=JCrypTool.org, OU=JCrypTool Core"); //$NON-NLS-1$
            certificate.setSubjectDN(subjectDN);
            certificate.setIssuerDN(subjectDN);

            boolean[] keyUsages = { false, false, true, true, false, false, false, false, false };
            setKeyUsages(certificate, keyUsages);

            return new X509Certificate(certificate);
        } catch (InvalidKeyException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                    "InvalidKeyException while setting up the JCrypTool certificate", e, true); //$NON-NLS-1$
        } catch (Exception e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "Exception while setting up the JCrypTool certificate", e, true); //$NON-NLS-1$
        }
        return null;
    }

    private static void setKeyUsages(X509TBSCertificate certificate, boolean[] keyUsages) throws Exception {
        certificate.addExtension(new X509Extension(new ASN1ObjectIdentifier("2.5.29.15"), true, new ASN1BitString( //$NON-NLS-1$
                keyUsages)));
    }
}
