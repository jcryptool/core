// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.backend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.SecretKey;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.providers.ProviderManager2;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.exceptions.NoKeyStoreFileException;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;
import org.jcryptool.util.ui.PasswordPrompt;

import de.flexiprovider.api.keys.Key;

/**
 * @author t-kern
 * 
 */
public class KeyStoreManager {
    /** Hard-coded standard password for the platform keystore */
    private static final char[] KEYSTORE_PASSWORD = {'j', 'c', 'r', 'y', 'p', 't', 'o', 'o', 'l'};

    /** Hard-coded standard password for the keys */
    private static final char[] KEY_PASSWORD = {'1', '2', '3', '4'};

    /** The current keystore (single keystore design!) */
    private KeyStore keyStore = null;

    private IFileStore keyStoreFileStore = null;

    /** Singleton instance */
    private static KeyStoreManager instance;

    private KeyStoreManager() {
        ProviderManager2.getInstance();
        try {
            this.keyStore = KeyStore.getInstance("JCEKS"); //$NON-NLS-1$
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                    "KeyStoreException while accessing an instance of JCEKS keystore", e, true);
        }
        KeyStorePlugin.loadPreferences();
        try {
            loadKeyStore(KeyStorePlugin.getCurrentKeyStoreURI());
        } catch (NoKeyStoreFileException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "Unable to load keystore", e, true);
        }
    }

    public synchronized static KeyStoreManager getInstance() {

        if (instance == null) {
            instance = new KeyStoreManager();
        }
        return instance;
    }

    public void createNewKeyStore(final URI uri) {
        try {
            final KeyStore newKeyStore = KeyStore.getInstance("JCEKS"); //$NON-NLS-1$
            final IFileStore store = EFS.getStore(uri);
            newKeyStore.load(null, KEYSTORE_PASSWORD);
            newKeyStore.store(store.openOutputStream(EFS.APPEND, null), KEYSTORE_PASSWORD);
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while creating a new keystore", e, true);
        } catch (final CoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException while creating a new keystore", e, true);
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while creating a new keystore", e,
                    true);
        } catch (final CertificateException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CertificateException while creating a new keystore", e, true);
        } catch (final IOException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "IOException while creating a new keystore", e, true);
        }
    }

    public void deleteContact(final String contactName) {
        Enumeration<String> en;
        try {
            en = this.getAliases();
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while accessing the aliases", e, true);
            return;
        }
        KeyStoreAlias current = null;
        while (en != null && en.hasMoreElements()) {
            current = new KeyStoreAlias(en.nextElement());
            if (current.getContactName().equals(contactName)) {
                try {
                    this.keyStore.deleteEntry(current.getAliasString());
                } catch (final KeyStoreException e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while trying to delete an entry", e,
                            true);
                }
            }
        }
        this.storeKeyStore();
        ContactManager.getInstance().removeContact(contactName);
    }

    public void delete(final KeyStoreAlias alias) {
        try {
            if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
                // if it is a private key, make sure that the corresponding
                // public key gets deleted,
                // as well
                KeyStoreAlias pub = this.getPublicForPrivate(alias);
                if (pub != null) {
                    this.keyStore.deleteEntry(pub.getAliasString());
                }
            } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
                KeyStoreAlias priv = this.getPrivateForPublic(alias);
                if (priv != null) {
                    this.keyStore.deleteEntry(priv.getAliasString());
                }
            }
            this.keyStore.deleteEntry(alias.getAliasString());
            this.storeKeyStore();
            ContactManager.getInstance().removeEntry(alias);
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while deleting an entry", e, true);
        }
    }

    private KeyStoreAlias getPublicForPrivate(final KeyStoreAlias privateAlias) {
        try {
            final Enumeration<String> en = this.getAliases();
            KeyStoreAlias tmp;
            while (en != null && en.hasMoreElements()) {
                tmp = new KeyStoreAlias(en.nextElement());
                if (privateAlias.getHashValue().toLowerCase().equals(tmp.getHashValue().toLowerCase())
                        && tmp.getKeyStoreEntryType() == KeyType.KEYPAIR_PUBLIC_KEY) {
                    return tmp;
                }
            }
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while accessing the aliases", e, true);
        }
        return null;
    }

    /**
     * extracts the private alias matching the known {@link #publicAlias}
     * 
     * @return the private alias or <code>null</code> if none is found or there was a problem accessing the keystore
     */
    public KeyStoreAlias getPrivateForPublic(final KeyStoreAlias publicAlias) {
        if (publicAlias == null)
            return null;

        Enumeration<String> aliases;
        try {
            aliases = this.getAliases();
        } catch (final KeyStoreException e) {
            LogUtil.logError(e);
            return null;
        }
        KeyStoreAlias alias;
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());
            if (alias.getHashValue().equalsIgnoreCase(publicAlias.getHashValue()) && !alias.equals(publicAlias)
                    && alias.getKeyStoreEntryType() == KeyType.KEYPAIR_PRIVATE_KEY) {
                return alias;
            }
        }
        return null;
    }

    public void addCertificate(final Certificate certificate, final KeyStoreAlias alias) {
        try {
            this.keyStore.setEntry(alias.getAliasString(), new KeyStore.TrustedCertificateEntry(certificate), null);
            this.storeKeyStore();
            ContactManager.getInstance().addCertificate(alias);
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while adding a certificate", e, true);
        }
    }

    public void addSecretKey(final SecretKey key, final String password, final KeyStoreAlias alias) {
        try {
            this.keyStore.setEntry(alias.getAliasString(), new KeyStore.SecretKeyEntry(key),
                    new KeyStore.PasswordProtection(password.toCharArray()));
            this.storeKeyStore();
            ContactManager.getInstance().addSecretKey(alias);
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while adding a secret key", e, true);
        }
    }

    public void addKeyPair(final PrivateKey privateKey, final Certificate publicKey, final String password,
            final KeyStoreAlias privateAlias, final KeyStoreAlias publicAlias) {
        final Certificate[] certs = new Certificate[1];
        certs[0] = publicKey;
        try {
            this.keyStore.setEntry(privateAlias.getAliasString(), new KeyStore.PrivateKeyEntry(privateKey, certs),
                    new KeyStore.PasswordProtection(password.toCharArray()));
            this.keyStore.setEntry(publicAlias.getAliasString(), new KeyStore.TrustedCertificateEntry(publicKey), null);
            this.storeKeyStore();
            ContactManager.getInstance().addKeyPair(privateAlias, publicAlias);
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while adding a key pair", e, true);
        }
    }

    /**
     * Updates the private key in a key pair. Necessary for some one-time signature algorithms
     */
    public void updateKeyPair(PrivateKey privateKey, char[] password, KeyStoreAlias privateAlias) {
        // Check that private key is in the keystore and that we don't change
        // the
        // password
        try {
            getPrivateKey(privateAlias, password);
        } catch (Exception e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while updating a private key", e, true);
        }

        try {
            KeyStoreAlias publicAlias = getPublicForPrivate(privateAlias);
            Certificate publicKey = getPublicKey(publicAlias);
            Certificate[] certs = new Certificate[1];
            certs[0] = publicKey;

            keyStore.setEntry(privateAlias.getAliasString(), new KeyStore.PrivateKeyEntry(privateKey, certs),
                    new KeyStore.PasswordProtection(password));
            storeKeyStore();
        } catch (KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while updating a priavte key", e, true);
        }
    }

    public Certificate getPublicKey(final IKeyStoreAlias alias) {
        try {
            final KeyStore.TrustedCertificateEntry entry = (KeyStore.TrustedCertificateEntry) this.keyStore.getEntry(
                    alias.getAliasString(), null);
            return entry.getTrustedCertificate();
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while accessing a public key", e, true);
        } catch (final UnrecoverableEntryException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "UnrecoverableEntryException while accessing a public key", e,
                    false);
            MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    Messages.getString("Label.ProblemOccured"), //$NON-NLS-1$
                    Messages.getString("Label.KeyNotAccessable")); //$NON-NLS-1$
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while accessing a public key", e, true);
        }
        return null;
    }

    public PrivateKey getPrivateKey(final IKeyStoreAlias alias, final char[] password) throws Exception {
        try {
            final KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) this.keyStore.getEntry(
                    alias.getAliasString(), new KeyStore.PasswordProtection(password));
            return entry.getPrivateKey();
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while accessing a private key", e,
                    true);
        } catch (final UnrecoverableEntryException e) {
            throw e;
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while accessing a private key", e, true);
        }

        return null;
    }
    
    /**
     * tries to retrieve the key from keystore using the default password if the operation succeeds, the default
     * password will be updated, if it fails, the user have to enter a password into a prompt window
     * 
     * @param alias
     * @return
     */
    public Key getKey(IKeyStoreAlias alias) {

        try {
            Key key = KeyStoreManager.getInstance().getKey(alias, KEY_PASSWORD);
            return key;
        } catch (Exception e) {
            // prompt for password and try again
            char[] prompt_password = PasswordPrompt.prompt(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getShell());
            if (prompt_password == null) {
                JCTMessageDialog.showInfoDialog(new Status(Status.INFO, KeyStorePlugin.PLUGIN_ID,
                        Messages.getString("ExAccessKeystorePassword"), e)); //$NON-NLS-1$
                return null;
            }
            try {
                Key key = KeyStoreManager.getInstance().getKey(alias, prompt_password);
                return key;
            } catch (UnrecoverableEntryException ex) {
                JCTMessageDialog.showInfoDialog(new Status(Status.INFO, KeyStorePlugin.PLUGIN_ID,
                        Messages.getString("ExAccessKeystorePassword"), e)); //$NON-NLS-1$
                return null;
            } catch (Exception ex) {
                LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                        Messages.getString("ExAccessKeystorePassword"), e, true);
                return null;
            }
        }
    }

    /**
     * let the function decide which key type the alias is associated with
     * @param alias
     * @param password
     * @return key from the keystore
     * @throws Exception
     */
    public Key getKey(final IKeyStoreAlias alias, final char[] password) throws Exception {
        Key key = null;
        switch (alias.getKeyStoreEntryType()) {
            case SECRETKEY:
                key = (Key) KeyStoreManager.getInstance().getSecretKey(alias, password);
                break;
            case KEYPAIR_PRIVATE_KEY:
                key = (Key) KeyStoreManager.getInstance().getPrivateKey(alias, password);
                break;
            case KEYPAIR_PUBLIC_KEY:
                Certificate cert = KeyStoreManager.getInstance().getPublicKey(alias);
                if(cert == null) return null;
                key = (Key) cert.getPublicKey();
                break;
            case PUBLICKEY:
                Certificate certpub = KeyStoreManager.getInstance().getPublicKey(alias);
                if(certpub == null) return null;
                key = (Key) certpub.getPublicKey();
                break;
            default:
                LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                        Messages.getString("ExKeyTypeUnsupported") + alias.getKeyStoreEntryType(), null, true);
                break;
        }
        return key;
    }

    public Certificate[] getCertificateChain(final KeyStoreAlias alias, final char[] password) throws Exception {
        try {
            final KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) this.keyStore.getEntry(
                    alias.getAliasString(), new KeyStore.PasswordProtection(password));
            return entry.getCertificateChain();
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while accessing a private key", e,
                    true);
        } catch (final UnrecoverableEntryException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "UnrecoverableEntryException while accessing a private key", e,
                    true);
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while accessing a private key", e, true);
        }

        return null;
    }

    public SecretKey getSecretKey(final IKeyStoreAlias alias, final char[] password) throws Exception {
        try {
            final KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) this.keyStore.getEntry(
                    alias.getAliasString(), new KeyStore.PasswordProtection(password));
            return entry.getSecretKey();
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while accessing a secret key", e, true);
        } catch (final UnrecoverableEntryException e) {
            throw e;
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while accessing a secret key", e, true);
        }
        return null;
    }

    public Enumeration<String> getAliases() throws KeyStoreException {
        return this.keyStore.aliases();
    }

    public void loadKeyStore(URI currentKeyStoreURI) throws NoKeyStoreFileException {
        if (currentKeyStoreURI != null) {
            try {
                if (currentKeyStoreURI.toString().endsWith(";")) {
                    String temp = currentKeyStoreURI.toString();
                    try {
                        currentKeyStoreURI = new URI(temp.substring(0, temp.length() - 1));
                    } catch (URISyntaxException ex) {
                        LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                                "The keystore URI contains a trailing ;, but cut off failed", ex, false);
                    }
                }
                this.keyStoreFileStore = EFS.getStore(currentKeyStoreURI);
                this.load(this.keyStoreFileStore, KEYSTORE_PASSWORD);
            } catch (final CoreException e) {
                LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException while accessing the keystore file store.", e,
                        false);
                throw new NoKeyStoreFileException(new Status(IStatus.WARNING,
                        "org.jcryptool.crypto.keystore", "No keystore file with the given name exists!")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        } else {
            // load and establish default keystore
            LogUtil.logInfo("uri does not exist."); //$NON-NLS-1$
            try {
                this.keyStoreFileStore = EFS.getStore(KeyStorePlugin.getPlatformKeyStoreURI());
                LogUtil.logInfo("PlatformKS: " + KeyStorePlugin.getPlatformKeyStore()); //$NON-NLS-1$
                KeyStorePlugin.setCurrentKeyStore(KeyStorePlugin.getPlatformKeyStoreName());
                if (KeyStorePlugin.getAvailableKeyStores().size() == 0) {
                    final List<String> newList = new ArrayList<String>(1);
                    newList.add(KeyStorePlugin.getPlatformKeyStore());
                    KeyStorePlugin.setAvailableKeyStores(newList);
                }
                KeyStorePlugin.savePreferences();
                this.createNewPlatformKeyStore();
            } catch (final CoreException e) {
                LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException", e, true);
            }
        }
    }

    /**
     * Loads a keystore from the filesystem.
     * 
     * @param fullyQualifiedName The full name of the keystore file, including path information
     * @param password The password with which the keystore is protected
     */
    private void load(final IFileStore keyStoreFileStore, final char[] password) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(keyStoreFileStore.openInputStream(EFS.NONE, null));
            this.keyStore.load(is, password);
        } catch (final CoreException e) {
            if (URIUtil.equals(keyStoreFileStore.toURI(), KeyStorePlugin.getPlatformKeyStoreURI())) {
                this.createNewPlatformKeyStore();
            } else {
                LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                        "CoreException while opening an input stream on a file store", e, true);
            }
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NoSuchAlgorihtmException while loading a keystore", e, true);
        } catch (final CertificateException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CertificateException while loading a keystore", e, true);
        } catch (final IOException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "IOException while loading a keystore", e, true);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "Failed to close BufferdInputStream", e, true);
                }
            }
        }
    }

    private void createNewPlatformKeyStore() {
        try {
            this.keyStore.load(null, KEYSTORE_PASSWORD);
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NoSuchAlgorithmException", e, true);
        } catch (final CertificateException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CertificateException", e, true);
        } catch (final IOException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "IOException", e, true);
        }

        BufferedInputStream is = null;

        try {
            final File flexiProvider = new File(DirectoryService.getWorkspaceDir(),
                    KeyStorePlugin.getFlexiProviderFolder());

            if (!flexiProvider.exists()) {
                flexiProvider.mkdir();
            }

            final URL url = KeyStorePlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
            final File file = new File(FileLocator.toFileURL(url).getFile() + "keystore" + File.separatorChar //$NON-NLS-1$
                    + "jctKeystore.ksf"); //$NON-NLS-1$
            final IFileStore jctKeystore = EFS.getStore(file.toURI());
            jctKeystore.copy(EFS.getStore(KeyStorePlugin.getPlatformKeyStoreURI()), EFS.NONE, null);

            is = new BufferedInputStream(this.keyStoreFileStore.openInputStream(EFS.NONE, null));
            this.keyStore.load(is, KEYSTORE_PASSWORD);
        } catch (final Exception e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "Exception", e, false);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "Failed to close BufferdInputStream", e, false);
                }
            }
        }
    }

    public void storeKeyStore() {
        OutputStream os;
        try {
            os = new BufferedOutputStream(this.keyStoreFileStore.openOutputStream(EFS.NONE, null));
            this.keyStore.store(os, KEYSTORE_PASSWORD);
            os.close();
        } catch (final CoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CoreException while storing a keystore", e, true);
        } catch (final IOException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "IOException while storing a keystore", e, false);
        } catch (final KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "KeyStoreException while storing a keystore", e, true);
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while storing a keystore", e, true);
        } catch (final CertificateException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "CertificateException while storing a keystore", e, true);
        }
    }

    public static char[] getDefaultKeyPassword() {
        return KEY_PASSWORD;
    }
}
