// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.backend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.SecretKey;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;

import codec.asn1.ASN1Exception;
import codec.asn1.DEREncoder;
import codec.pkcs12.PFX;

public class ImportExportManager {
    private static ImportExportManager instance;
    private Provider lastUsed;

    private ImportExportManager() {
    }

    public static synchronized ImportExportManager getInstance() {
        if (instance == null) {
            instance = new ImportExportManager();
        }
        return instance;
    }

    private X509Certificate[] convert(Certificate[] chain) {
        if (chain != null) {
            X509Certificate[] x509 = new X509Certificate[chain.length];
            for (int i = 0; i < x509.length; i++) {
                if (chain[i] instanceof X509Certificate) {
                    x509[i] = (X509Certificate) chain[i];
                } else {
                    return null;
                }
            }
            return x509;
        } else {
            return null;
        }
    }

    public void exportKeyPair(IPath path, PrivateKey key, Certificate[] chain, char[] password) {
        PFX pfx;
        X509Certificate[] x509Chain = convert(chain);
        try {
            if (x509Chain.length > 1) {
                X509Certificate[] shortChain = new X509Certificate[x509Chain.length - 1];
                for (int i = 1; i < chain.length; i++) {
                    shortChain[i - 1] = x509Chain[i];
                }
                pfx = new PFX(key, x509Chain[0], shortChain, password, null, null);
            } else {
                pfx = new PFX(key, x509Chain[0], null, password, null, null);
            }
            IFileStore fileStore = EFS.getStore(URIUtil.toURI(path));
            OutputStream os = new BufferedOutputStream(fileStore.openOutputStream(EFS.APPEND, null));
            DEREncoder encoder = new DEREncoder(os);
            pfx.encode(encoder);
            encoder.close();
            os.close();
        } catch (CertificateEncodingException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CertificateEncodingException while creating a PFX", e, true);
        } catch (GeneralSecurityException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "GeneralSecurityException while creating a PFX", e, true);
        } catch (ASN1Exception e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "ASN1Exception while creating a PFX", e, true);
        } catch (IOException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "IOException while creating a PFX", e, true);
        } catch (CoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException while creating a PFX", e, true);
        }
    }

    public void exportSecretKey(IPath path, SecretKey key) {
        ObjectOutputStream oos;
        try {
            IFileStore fileStore = EFS.getStore(URIUtil.toURI(path));
            oos = new ObjectOutputStream(new BufferedOutputStream(fileStore.openOutputStream(EFS.APPEND, null)));
            oos.writeObject(key);
            oos.close();
        } catch (CoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException while accessing a file store", e, true);
        } catch (IOException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "IOException while initializing the object output stream", e,
                    true);
        }
    }

    public void exportCertificate(IPath path, Certificate publicKey) {
        LogUtil.logInfo("Exporting a certficate to " + path.toString()); //$NON-NLS-1$
        LogUtil.logInfo("Certificate:" + publicKey.getType()); //$NON-NLS-1$
        BufferedOutputStream bos;
        try {
            IFileStore fileStore = EFS.getStore(URIUtil.toURI(path));
            bos = new BufferedOutputStream(fileStore.openOutputStream(EFS.APPEND, null));
            bos.write(publicKey.getEncoded());
            bos.close();
        } catch (CoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException while accessing a file store", e, true);
        } catch (CertificateEncodingException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CertificateEncodingException while accessing a certificate", e,
                    true);
        } catch (IOException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "IOException while initializing the output stream", e, false);
        }
    }

    public SecretKey importSecretKey(IPath path) {
        ObjectInputStream ois;
        try {
            IFileStore fileStore = EFS.getStore(URIUtil.toURI(path));
            ois = new ObjectInputStream(new BufferedInputStream(fileStore.openInputStream(EFS.NONE, null)));
            SecretKey key = (SecretKey) ois.readObject();
            LogUtil.logInfo("is instanceof " + key.getClass().getName()); //$NON-NLS-1$
            ois.close();
            return key;
        } catch (CoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException while accessing a file store", e, true);
        } catch (IOException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "IOException while initializing the object input stream", e,
                    false);
        } catch (ClassNotFoundException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                    "ClassNotFoundException while initializing the object input stream", e, true);
        }
        return null;
    }

    public Certificate importCertificate(IPath path) {
        BufferedInputStream is;
        try {
            IFileStore fileStore = EFS.getStore(URIUtil.toURI(path));
            is = new BufferedInputStream(fileStore.openInputStream(EFS.NONE, null));
            CertificateFactory factory = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$
            lastUsed = factory.getProvider();
            return factory.generateCertificate(is);
        } catch (CoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException while accessing a file store", e, true);
        } catch (CertificateException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CertificateException while importing a certificate", e, true);
        }
        return null;
    }

    public Provider getLastUsedProvider() {
        return lastUsed;
    }

}
