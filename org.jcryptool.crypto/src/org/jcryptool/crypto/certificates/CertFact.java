// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.certificates;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.CryptoPlugin;

import codec.asn1.ASN1BitString;
import codec.asn1.ASN1ObjectIdentifier;
import codec.x509.X509Certificate;
import codec.x509.X509Extension;
import codec.x509.X509TBSCertificate;

public class CertFact {

    public static X509Certificate getDummyCertificate(PublicKey publicKey) {
        X509TBSCertificate certificate = new X509TBSCertificate();
        try {
            certificate.setSubjectPublicKey(publicKey);

            Calendar calendar = Calendar.getInstance();

            // not before 1.1.2000
            calendar.set(2000, Calendar.JANUARY, 1);
            certificate.setNotBefore(calendar);
            // not after 31.12.2099
            calendar.clear();
            calendar.set(2199, Calendar.DECEMBER, 31);
            certificate.setNotAfter(calendar);

            X500Principal subjectDN = new X500Principal("CN=Dummy, C=Dummy, ST=Dummy, L=Dummy, O=Dummy, OU=Dummy"); //$NON-NLS-1$
            certificate.setSubjectDN(subjectDN);
            certificate.setIssuerDN(subjectDN);

            boolean[] keyUsages = { false, false, true, true, false, false, false, false, false };
            setKeyUsages(certificate, keyUsages);

            return new X509Certificate(certificate);

        } catch (InvalidKeyException e) {
            LogUtil.logError(CryptoPlugin.PLUGIN_ID, "InvalidKeyException while setting up a dummy certificate", e, false); //$NON-NLS-1$
        } catch (Exception e) {
            LogUtil.logError(CryptoPlugin.PLUGIN_ID, e);
        }
        return null;
    }

    private static void setKeyUsages(X509TBSCertificate certificate, boolean[] keyUsages) throws Exception {
        certificate.addExtension(new X509Extension(new ASN1ObjectIdentifier("2.5.29.15"), true, new ASN1BitString( //$NON-NLS-1$
                keyUsages)));
    }

}
