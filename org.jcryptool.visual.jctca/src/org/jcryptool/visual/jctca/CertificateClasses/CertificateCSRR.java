// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.jctca.CertificateClasses;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.visual.jctca.Util;

/**
 * Class containing everything regarding CSRs that have been sent to the CA, Revocation Requests, the CRL,
 * CA-Certificates+Keys and the CRL.
 * 
 * Accessible from everywhere via .getInstance()
 * 
 * @author mmacala
 * 
 */
@SuppressWarnings("deprecation")
public class CertificateCSRR {
    /**
     * the object instance of this class
     */
    private static CertificateCSRR instance = null;

    /**
     * the CSRs that have been approved by the RA
     */
    private ArrayList<CSR> approved_csrs;

    /**
     * revocation requests sent from users
     */
    private ArrayList<RR> revRequests;

    /**
     * the keys from the CA to sign certificates
     */
    private ArrayList<AsymmetricCipherKeyPair> caKeys;

    /**
     * the certificates from the CA
     */
    private ArrayList<X509Certificate> certs;

    /**
     * the CRL containing all revoked Certificates
     */
    private ArrayList<CRLEntry> crl;

    /**
     * contains all generated signatures
     */
    private ArrayList<Signature> sigList;

    /**
     * private constructor, use getInstance()
     */
    private CertificateCSRR() {
        approved_csrs = new ArrayList<CSR>();
        revRequests = new ArrayList<RR>();
        caKeys = new ArrayList<AsymmetricCipherKeyPair>();
        certs = new ArrayList<X509Certificate>();
        crl = new ArrayList<CRLEntry>();
        sigList = new ArrayList<Signature>();
        checkCertificatesAndCRL();
    }

    /**
     * Get the Instance of this class
     * 
     * @return Instance of CertificateCSRR
     */
    public static CertificateCSRR getInstance() {
        if (instance == null) {
            instance = new CertificateCSRR();
        }
        return instance;
    }

    /**
     * Checks if there are root certificates in the keystore. if not, creates and adds them to the keystore. Also loads
     * the revoked requests from the keystore and adds them to the CRL.
     */
    public void checkCertificatesAndCRL() {
        boolean certsExist = false;
        KeyStoreManager mng = KeyStoreManager.getInstance();
        // iterate through all public key aliases
        for (IKeyStoreAlias pubAlias : mng.getAllPublicKeys()) {
            if (pubAlias.getContactName().contains("JCT-PKI Root Certificates")) {//$NON-NLS-1$
                // root certificates have been found, do not create new ones
                certsExist = true;
                java.security.cert.Certificate c = null;
                try {
                    c = mng.getCertificate(pubAlias);
                } catch (UnrecoverableEntryException e) {
                    LogUtil.logError(e);
                } catch (NoSuchAlgorithmException e) {
                    LogUtil.logError(e);
                }
                if (c instanceof X509Certificate) {
                    certs.add((X509Certificate) c);
                }
            } else if (pubAlias.getContactName().contains("JCT-PKI Certificate Revocation List")) {//$NON-NLS-1$
                // revoked certificates have been found. add them to the CRL-ArrayList
                java.security.cert.Certificate c = null;
                try {
                    c = mng.getCertificate(pubAlias);
                } catch (UnrecoverableEntryException e) {
                    LogUtil.logError(e);
                } catch (NoSuchAlgorithmException e) {
                    LogUtil.logError(e);
                }
                if (c instanceof X509Certificate) {
                    long time = Long.parseLong(pubAlias.getOperation());
                    X509Certificate cert = (X509Certificate) c;
                    crl.add(new CRLEntry(cert.getSerialNumber(), new Date(time)));
                }
            }
        }
        if (!certsExist) {
            // no certificates exist, create new ones

            // GENERATE THE PUBLIC/PRIVATE RSA KEY PAIR
            RSAKeyPairGenerator gen = new RSAKeyPairGenerator();
            SecureRandom sr = new SecureRandom();
            gen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(3), sr, 1024, 80));

            AsymmetricCipherKeyPair keypair = null;
            for (int i = 0; i < 2; i++) {
                // generates 5 new self signed certificates and adds them to the keystore
                keypair = gen.generateKeyPair();
                KeyPair kp = Util.asymmetricKeyPairToNormalKeyPair(keypair);
                // yesterday
                Date validityBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
                // in 10 years
                Date validityEndDate = new Date(System.currentTimeMillis() + 10 * 365 * 24 * 60 * 60 * 1000);
                X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
                X509Name dnName = new X509Name("CN=JCrypTool, O=JCrypTool, OU=JCT-CA Visual");//$NON-NLS-1$

                certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
                certGen.setSubjectDN(dnName);
                certGen.setIssuerDN(dnName); // use the same
                certGen.setNotBefore(validityBeginDate);
                certGen.setNotAfter(validityEndDate);
                certGen.setPublicKey(kp.getPublic());
                certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");//$NON-NLS-1$

                X509Certificate cert = null;
                try {
                    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
                    cert = certGen.generate(kp.getPrivate(), "BC");//$NON-NLS-1$
                } catch (Exception e) {

                }
                caKeys.add(keypair);
                certs.add(cert);
                KeyStoreAlias pubAlias = new KeyStoreAlias(
                        "JCT-PKI Root Certificates", KeyType.KEYPAIR_PUBLIC_KEY, "RSA", 1024, kp.getPrivate().hashCode() + "", kp.getPublic().getClass().toString());//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                KeyStoreAlias privAlias = new KeyStoreAlias(
                        "JCT-PKI Root Certificates", KeyType.KEYPAIR_PRIVATE_KEY, "RSA", 1024, kp.getPrivate().hashCode() + "", kp.getPrivate().getClass().toString());//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                mng.addKeyPair(kp.getPrivate(), cert, KeyStoreManager.KEY_PASSWORD, privAlias, pubAlias);
            }
        }
    }

    /**
     * adds a CSR to the list, will then be shown in the CA view as an approved CSR
     * 
     * @param c - the CSR to be added
     */
    public void addCSR(CSR c) {
        approved_csrs.add(c);
    }

    /**
     * arraylist containing the approved
     * 
     * @return arraylist with the CSRs
     */
    public ArrayList<CSR> getApproved() {
        return approved_csrs;
    }

    /**
     * private ca-key for the index
     * 
     * @param i - the index
     * @return private key
     */
    public AsymmetricCipherKeyPair getCAKey(int i) {
        return caKeys.get(i);
    }

    /**
     * public ca-key for the index
     * 
     * @param i - the index
     * @return public key
     */
    public X509Certificate getCACert(int i) {
        return certs.get(i);
    }

    /**
     * list containing the revocation requests
     * 
     * @return the revocation requests
     */
    public ArrayList<RR> getRevocations() {
        return revRequests;
    }

    /**
     * list containing all private ca-keys
     * 
     * @return the private ca-keys
     */
    public ArrayList<AsymmetricCipherKeyPair> getCAKeys() {
        return caKeys;
    }

    /**
     * removes a csr from the approved CSR-List
     * 
     * @param i - the index
     */
    public void removeCSR(int i) {
        this.approved_csrs.remove(i);
    }

    /**
     * removes a csr from the approved CSR-List
     * 
     * @param c - the csr object from the list, that should be removed
     */
    public void removeCSR(CSR c) {
        this.approved_csrs.remove(c);
    }

    /**
     * removes a rr from the RR-List
     * 
     * @param r - the revocation request object from the list, that should be removed
     */
    public void removeRR(RR r) {
        this.revRequests.remove(r);
    }

    /**
     * gets the current CRL
     * 
     * @return the CRL
     */
    public ArrayList<CRLEntry> getCRL() {
        return crl;
    }

    /**
     * adds the given rr to the rr-List
     * 
     * @param rr - the rr to be added
     */
    public void addRR(RR rr) {
        this.revRequests.add(rr);
    }

    /**
     * adds the given crl-entry to the crl-list
     * 
     * @param crle - the crl-entry to be added
     */
    public void addCRLEntry(CRLEntry crle) {
        this.crl.add(crle);
    }

    /**
     * gets the list containing the revoked certificates
     * 
     * @return the revoked certificates
     */
    public ArrayList<CRLEntry> getRevoked() {
        return crl;
    }

    /**
     * gets the list containing the generated signatures
     */
    public ArrayList<Signature> getSignatures() {
        return sigList;
    }

    /**
     * adds a signature to the list of signatures
     * 
     * @param sig - the signature to be added
     */
    public void addSignature(Signature sig) {
        this.sigList.add(sig);
    }

    /**
     * removes a signature from the list of signatures
     * 
     * @param sig - the signature to be deleted form the list
     */
    public void removeSignature(Signature sig) {
        this.sigList.remove(sig);
    }
}
