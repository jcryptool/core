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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.xml.security.core.XmlSecurityPlugin;

/**
 * <p>Manages the Java KeyStores (jks files). The generated Java KeyStores as well as the public and
 * private keys are standard compliant and can be used with other tools too. Newly generated
 * public keys are stored together with their certificate (chains). The default <b>JKS</b> keystore
 * can only store private key and certificate entries. A <b>JCEKS</b> keystore can also store secret
 * key entries.</p>
 *
 * <p>The keystore file and, optionally (but recommended), their included entries, are password
 * protected.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class Keystore {
    /** Keystore file name. */
    private String file = ""; //$NON-NLS-1$
    /** Keystore password. */
    private char[] password = null;
    /** The Java KeyStore. */
    private KeyStore keystore = null;

    /**
     * Loads the given keystore identified by an absolute path and the corresponding keystore
     * password. Keystore type is normally JCEKS, which is handled by the SunJCE provider. Other
     * keystore types should be possible too but may require a special provider.
     *
     * @param file The keystore file (absolute path and filename)
     * @param password The corresponding keystore password
     * @param type The keystore type
     * @throws Exception Exception during loading the given keystore
     */
    public Keystore(String file, String password, String type) throws Exception {
        this.file = file;
        this.password = password.toCharArray();

        keystore = KeyStore.getInstance(type);

        keystore.load(null, this.password);
    }

    /**
     * Stores a newly created keystore. Remember to call <code>load()</code>
     * again to get access to the extended keystore.
     *
     * @throws Exception Exception during storing the new keystore
     */
    public void store() throws Exception {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);

            keystore.store(fos, password);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * Loads the keystore.
     *
     * @throws Exception Exception during closing the FileInputStream
     */
    public boolean load() throws Exception {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            keystore.load(fis, password);

            return true;
        } catch (IOException ex) {
            return false;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * Inserts the given certificate with the given name and stores it in the
     * selected Java KeyStore.
     *
     * @param alias The certificate key alias (must be unique in the keystore)
     * @param cert The certificate
     * @return Certificate generation result
     * @throws Exception General exception during certificate generation
     */
    public boolean insertCertificate(String alias, Certificate cert) throws Exception {
        int sizeBefore = keystore.size();

        if (!keystore.containsAlias(alias)) {
            keystore.setCertificateEntry(alias, cert);

            int sizeAfter = keystore.size();

            if (sizeAfter > sizeBefore) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Generates a secret key with the given algorithm name and a matching algorithm size. A
     * <code>NoSuchAlgorithmException</code> will be thrown if the algorithm or the algorithm
     * size are not supported.
     *
     * @param algorithm The algorithm name
     * @param size The algorithm size
     * @return The generated secret key
     * @throws NoSuchAlgorithmException If the algorithm or the algorithm size are not supported
     */
    public SecretKey generateSecretKey(String algorithm, int size) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        kg.init(size);

        return kg.generateKey();
    }

    /**
     * Inserts the given secret key into the loaded keystore by using the given alias and the
     * given password.
     *
     * @param alias The alias name of the secret key
     * @param password The password to protect the secret key
     * @param key The secret key to insert
     * @return Secret key insertion result
     */
    public boolean insertSecretKey(String alias, char[] password, SecretKey key) {
        try {
            if (!keystore.containsAlias(alias)) {
                int sizeBefore = keystore.size();

                KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);
                keystore.setEntry(alias, skEntry, new PasswordProtection(password));

                int sizeAfter = keystore.size();

                if (sizeAfter > sizeBefore) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (KeyStoreException ex) {
            IStatus status = new Status(IStatus.ERROR, XmlSecurityPlugin.getDefault().getBundle().getSymbolicName(),
                    0, "Failed to insert the private key into the keystore", ex); //$NON-NLS-1$
            XmlSecurityPlugin.getDefault().getLog().log(status);

            return false;
        }
    }

    /**
     * Returns the secret key identified by the key alias and the key password.
     *
     * @param alias The key alias
     * @param password The key password
     * @return The secret key, null if it does not exist in the keystore
     */
    public SecretKey getSecretKey(String alias, char[] password) {
        try {
            if (keystore.containsAlias(alias)) {
                SecretKeyEntry ske = (SecretKeyEntry) keystore.getEntry(alias, new PasswordProtection(password));

                return ske.getSecretKey();
            } else {
                return null;
            }
        } catch (UnrecoverableEntryException ex) {
            return null;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (KeyStoreException ex) {
            return null;
        }
    }

    /**
     * Generates a key pair (a public and a private key) with the given algorithm name and a
     * matching algorithm size. A <code>NoSuchAlgorithmException</code> will be thrown if the
     * algorithm or the algorithm size are not supported.
     *
     * @param algorithm The algorithm name
     * @param size The algorithm size
     * @return The generated key pair
     * @throws NoSuchAlgorithmException If the algorithm or the algorithm size are not supported
     */
    public KeyPair generateKeyPair(String algorithm, int size) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
        kpg.initialize(size);

        return kpg.generateKeyPair();
    }

    /**
     * Inserts the given private key into the loaded keystore by using the given alias and the
     * given password.
     *
     * @param alias The alias name of the private key, may not be null
     * @param password The password to protect the private key
     * @param key The private key to insert, may not be null
     * @param chain The certificate chain, may not be null
     * @return Private key insertion result
     */
    public boolean insertPrivateKey(String alias, char[] password, PrivateKey key, Certificate[] chain) {
        try {
            if (!keystore.containsAlias(alias)) {
                int sizeBefore = keystore.size();

                KeyStore.PrivateKeyEntry pkEntry = new KeyStore.PrivateKeyEntry(key, chain);
                keystore.setEntry(alias, pkEntry, new PasswordProtection(password));

                int sizeAfter = keystore.size();

                if (sizeAfter > sizeBefore) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (KeyStoreException ex) {
            IStatus status = new Status(IStatus.ERROR, XmlSecurityPlugin.getDefault().getBundle().getSymbolicName(),
                    0, "Failed to insert the private key into the keystore", ex); //$NON-NLS-1$
            XmlSecurityPlugin.getDefault().getLog().log(status);

            return false;
        }
    }

    /**
     * Returns the private key identified by the key alias and the key password.
     *
     * @param alias The key alias
     * @param password The key password
     * @return The private key, null if it does not exist in the keystore
     * @throws Exception Any exception during loading the private key
     */
    public PrivateKey getPrivateKey(String alias, char[] password) {
        try {
            if (keystore.containsAlias(alias)) {
                PrivateKeyEntry ske = (PrivateKeyEntry) keystore.getEntry(alias, new PasswordProtection(password));

                return ske.getPrivateKey();
            }

            return null;
        } catch (Exception ex) {
            // ignore

            return null;
        }
    }

    /**
     * Checks whether the given key alias is contained in the current keystore.
     *
     * @param alias The key alias to look for
     * @return Key is contained in current keystore
     */
    public boolean containsKey(String alias) {
        try {
            return keystore.containsAlias(alias);
        } catch (KeyStoreException ex) {
            IStatus status = new Status(IStatus.ERROR, XmlSecurityPlugin.getDefault().getBundle().getSymbolicName(),
                    0, "Failed to retrieve the private key from the keystore", ex); //$NON-NLS-1$
            XmlSecurityPlugin.getDefault().getLog().log(status);

            return false;
        }
    }

    /**
     * Returns the certificate identified by the given alias name.
     *
     * @param alias The certificate alias
     * @return The certificate, null if it does not exist in the keystore
     */
    public Certificate getCertificate(String alias) {
        try {
            if (keystore.containsAlias(alias)) {
                return keystore.getCertificate(alias);
            } else {
                return null;
            }
        } catch (KeyStoreException ex) {
            IStatus status = new Status(IStatus.ERROR, XmlSecurityPlugin.getDefault().getBundle().getSymbolicName(),
                    0, "Failed to retrieve the certificate from the keystore", ex); //$NON-NLS-1$
            XmlSecurityPlugin.getDefault().getLog().log(status);

            return null;
        }
    }
}
