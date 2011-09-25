/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.cryptography;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

/**
 * <p>Generates a new X.509 certificate. This certificate may be stored in a Java KeyStore.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class XmlSecurityCertificate extends X509Certificate {
    private PublicKey publicKey = null;
    private X500Principal subjectDN = null;

    public XmlSecurityCertificate(PublicKey publicKey, X500Principal subjectDN) {
        this.publicKey = publicKey;
        this.subjectDN = subjectDN;
    }

    @Override
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
    }

    @Override
    public void checkValidity(Date date) throws CertificateExpiredException,
            CertificateNotYetValidException {
    }

    @Override
    public int getBasicConstraints() {
        return 0;
    }

    @Override
    public Principal getIssuerDN() {
        return subjectDN;
    }

    @Override
    public boolean[] getIssuerUniqueID() {
        return null;
    }

    @Override
    public boolean[] getKeyUsage() {
        return null;
    }

    @Override
    public Date getNotAfter() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2099, Calendar.DECEMBER, 31);

        return calendar.getTime();
    }

    @Override
    public Date getNotBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);

        return calendar.getTime();
    }

    @Override
    public BigInteger getSerialNumber() {
        return null;
    }

    @Override
    public String getSigAlgName() {
        return publicKey.getAlgorithm();
    }

    @Override
    public String getSigAlgOID() {
        return null;
    }

    @Override
    public byte[] getSigAlgParams() {
        return null;
    }

    @Override
    public byte[] getSignature() {
        return null;
    }

    @Override
    public Principal getSubjectDN() {
        return subjectDN;
    }

    @Override
    public boolean[] getSubjectUniqueID() {
        return null;
    }

    @Override
    public byte[] getTBSCertificate() throws CertificateEncodingException {
        return subjectDN.getEncoded();
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public byte[] getEncoded() throws CertificateEncodingException {
        return toString().getBytes();
    }

    @Override
    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException,
            InvalidKeyException, NoSuchProviderException, SignatureException {
    }

    @Override
    public void verify(PublicKey key, String sigProvider) throws CertificateException,
            NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException,
            SignatureException {
    }

    public Set<String> getCriticalExtensionOIDs() {
        return null;
    }

    public byte[] getExtensionValue(String oid) {
        return null;
    }

    public Set<String> getNonCriticalExtensionOIDs() {
        return null;
    }

    public boolean hasUnsupportedCriticalExtension() {
        return false;
    }
}
