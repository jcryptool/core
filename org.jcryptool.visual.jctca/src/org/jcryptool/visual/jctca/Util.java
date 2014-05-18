//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Date;

import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.visual.jctca.CertificateClasses.CRLEntry;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;

@SuppressWarnings("deprecation")
public class Util {

    public static X509Certificate certificateForKeyPair(CSR csr, BigInteger serialNumber, X509Certificate caCert,
            Date expiryDate, Date startDate, PrivateKey caKey) {
        KeyStoreManager mng = KeyStoreManager.getInstance();
        PublicKey pub = null;
        try {
            pub = mng.getCertificate(csr.getPubAlias()).getPublicKey();
        } catch (UnrecoverableEntryException e) {
            LogUtil.logError(e);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(e);
        }
        PrivateKey priv;
        try {
            priv = mng.getPrivateKey(csr.getPrivAlias(), KeyStoreManager.KEY_PASSWORD);
            return Util.certificateForKeyPair(csr.getFirst() + " " + csr.getLast(), csr.getCountry(),//$NON-NLS-1$
                    csr.getStreet(), csr.getZip(), csr.getTown(), "", "", csr.getMail(), pub,//$NON-NLS-1$ //$NON-NLS-2$
                    priv, serialNumber, caCert, expiryDate, startDate, caKey);
        } catch (Exception e) {
            LogUtil.logError(e);
        }
        return null;
    }

    public static X509Certificate certificateForKeyPair(String principal, String country, String street, String zip,
            String city, String unit, String organisation, String mail, PublicKey pub, PrivateKey priv,
            BigInteger serialNumber, X509Certificate caCert, Date expiryDate, Date startDate, PrivateKey caKey) {
        try {
            KeyPair keyPair = new KeyPair(pub, priv); // public/private key pair
                                                      // that we are creating
                                                      // certificate for

            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            X509Name subjectName = new X509Name("CN=" + principal + ", " + //$NON-NLS-1$ //$NON-NLS-2$
                    "ST=" + street + ", " + //$NON-NLS-1$ //$NON-NLS-2$
                    "L=" + zip + " " + city + ", " + "C=" + country + ", " + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    "OU=" + unit + ", " + //$NON-NLS-1$ //$NON-NLS-2$
                    "O=" + organisation + ", " + //$NON-NLS-1$ //$NON-NLS-2$
                    "E=" + mail); //$NON-NLS-1$

            certGen.setSerialNumber(serialNumber);
            if (caCert != null) {
                certGen.setIssuerDN(caCert.getSubjectX500Principal());
                certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(
                        caCert));
                certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifierStructure(
                        keyPair.getPublic()));
            } else {
                certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
                certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature
                        | KeyUsage.keyEncipherment));
                certGen.addExtension(X509Extensions.ExtendedKeyUsage, true, new ExtendedKeyUsage(
                        KeyPurposeId.id_kp_serverAuth));
            }
            certGen.setNotBefore(startDate);
            certGen.setNotAfter(expiryDate);
            certGen.setSubjectDN(subjectName);
            certGen.setPublicKey(keyPair.getPublic());
            certGen.setSignatureAlgorithm("SHA512withRSA");//$NON-NLS-1$

            X509Certificate cert;

            cert = certGen.generate(caKey, "BC");//$NON-NLS-1$
            return cert;
        } catch (CertificateEncodingException e) {
            LogUtil.logError(e);
        } catch (InvalidKeyException e) {
            LogUtil.logError(e);
        } catch (IllegalStateException e) {
            LogUtil.logError(e);
        } catch (NoSuchProviderException e) {
            LogUtil.logError(e);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(e);
        } catch (SignatureException e) {
            LogUtil.logError(e);
        } catch (CertificateParsingException e) {
            LogUtil.logError(e);
        }

        return null;

        // note: private key of CA
    }

    public static KeyPair asymmetricKeyPairToNormalKeyPair(AsymmetricCipherKeyPair keypair) {
        RSAKeyParameters publicKey = (RSAKeyParameters) keypair.getPublic();
        RSAPrivateCrtKeyParameters privateKey = (RSAPrivateCrtKeyParameters) keypair.getPrivate();
        @SuppressWarnings("unused")
        RSAPublicKey pkStruct = new RSAPublicKey(publicKey.getModulus(), publicKey.getExponent());
        // JCE format needed for the certificate - because
        // getEncoded() is necessary...
        PublicKey pubKey;
        try {
            pubKey = KeyFactory.getInstance("RSA").generatePublic(//$NON-NLS-1$
                    new RSAPublicKeySpec(publicKey.getModulus(), publicKey.getExponent()));
            PrivateKey privKey = KeyFactory.getInstance("RSA").generatePrivate(//$NON-NLS-1$
                    new RSAPrivateCrtKeySpec(publicKey.getModulus(), publicKey.getExponent(), privateKey.getExponent(),
                            privateKey.getP(), privateKey.getQ(), privateKey.getDP(), privateKey.getDQ(), privateKey
                                    .getQInv()));

            return new KeyPair(pubKey, privKey);
        } catch (InvalidKeySpecException e) {
            LogUtil.logError(e);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(e);
        }
        // and this one for the KeyStore
        return null;
    }

    public static void createCARootNodes(Tree tree) {
        TreeItem tree_item_csr = new TreeItem(tree, SWT.NONE);
        tree_item_csr.setText(Messages.Util_CSR_Tree_Head);
        TreeItem tree_item_crl = new TreeItem(tree, SWT.NONE);
        tree_item_crl.setText(Messages.Util_RR_Tree_Head);

        tree.getItems()[0].setExpanded(true);
        tree.getItems()[1].setExpanded(true);
    }

    public static void create2ndUserRootNodes(Tree tree) {
        TreeItem tree_item_csr = new TreeItem(tree, SWT.NONE);
        tree_item_csr.setText(Messages.Util_signed_texts);
        TreeItem tree_item_crl = new TreeItem(tree, SWT.NONE);
        tree_item_crl.setText(Messages.Util_signed_files);

        tree.getItems()[0].setExpanded(true);
        tree.getItems()[1].setExpanded(true);
    }

    /**
     * Find all RSA public keys in a given keystore ksm and return them in an array of KeyStoreAlias. omits JCT-CA Root
     * Certificates
     * 
     * @param ksm - KeyStoreManager from where to get the keys
     * @return ArrayList of all KeyStoreAlias containing either RSA or DSA, excluding JCT-CA Root Certificates public
     *         key pairs
     */
    public static ArrayList<KeyStoreAlias> getAllRSAPublicKeys(KeyStoreManager ksm) {
        ArrayList<KeyStoreAlias> RSAAndDSAPublicKeys = new ArrayList<KeyStoreAlias>();
        for (IKeyStoreAlias ksAlias : ksm.getAllPublicKeys()) {
            if (ksAlias.getOperation().contains("RSA")//$NON-NLS-1$
                    && (ksAlias.getKeyStoreEntryType() == KeyType.KEYPAIR_PUBLIC_KEY)
                    && !(ksAlias.getContactName().contains("JCT-PKI Root Certificates"))) {//$NON-NLS-1$
                if (ksAlias instanceof KeyStoreAlias) {
                    RSAAndDSAPublicKeys.add((KeyStoreAlias) ksAlias);
                }
            }
        }
        return RSAAndDSAPublicKeys;
    }

    public static void showMessageBox(String title, String text, int type) {
        MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), type);
        box.setText(title);
        box.setMessage(text);
        box.open();
    }

    public static boolean isSignedByJCTCA(KeyStoreAlias ksAlias) {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        X509Certificate pubKey = null;
        try {
            pubKey = (X509Certificate) ksm.getCertificate(ksAlias);
        } catch (UnrecoverableEntryException e) {
            LogUtil.logError(e);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(e);
        }
        // create X500Name from the X509 certificate Subjects distinguished name
        X500Name x500name = new X500Name(pubKey.getIssuerDN().toString());
        RDN rdn = x500name.getRDNs(BCStyle.OU)[0];
        if (rdn.getFirst().getValue().toString().equals("JCT-CA Visual")) {//$NON-NLS-1$
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCertificateRevoked(BigInteger serialNumber) {
        ArrayList<CRLEntry> crl = CertificateCSRR.getInstance().getRevoked();
        for (CRLEntry crle : crl) {
            if (serialNumber.compareTo(crle.GetSerial()) == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDateBeforeRevocation(BigInteger serialNumber, Date signDate) {
        ArrayList<CRLEntry> crl = CertificateCSRR.getInstance().getRevoked();
        for (CRLEntry crle : crl) {
            if (serialNumber.compareTo(crle.GetSerial()) == 0) {
                Date revokeDate = crle.getRevokeTime();
                if (signDate.before(revokeDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static KeyStoreAlias getAliasForHash(String hash) {
        KeyStoreManager mng = KeyStoreManager.getInstance();
        for (IKeyStoreAlias al : mng.getAllPublicKeys()) {
            if (al.getHashValue().compareTo(hash) == 0) {
                if (al instanceof KeyStoreAlias) {
                    return (KeyStoreAlias) al;
                }
            }
        }
        return null;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String formatHash(String hash) {
        char[] help = hash.toCharArray();
        String formatted = "";
        int i = 0;
        for (char c : help) {
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                formatted += c;
                i++;
                if (i % 2 == 0) {
                    formatted += "-";
                }
            }
        }
        if (formatted.charAt(formatted.length() - 1) == '-') {
            formatted = formatted.substring(0, formatted.length() - 1);
        }
        if (formatted.charAt(0) == '-') {
            formatted = formatted.substring(1);
        }
        return formatted;
    }

}
