// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.backend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.crypto.SecretKey;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.providers.AbstractProviderController;
import org.jcryptool.core.operations.providers.ProviderManager2;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;

import de.flexiprovider.FlexiProviderController;
import de.flexiprovider.api.keys.Key;

/**
 * <p>
 * This class represents the JCrypTool keystore. It is implemented as a singleton and can be used by any plug-in by
 * calling the <code>getInstance</code> method. Most methods require a <code>KeyStoreAlias</code> as method parameter to
 * access the keystores content.
 * </p>
 * 
 * <p>
 * The default password for all protected entries is <b>1234</b>. In case your plug-in offers access to a protected
 * entry, it is recommended to show this password somewhere in your GUI.
 * </p>
 * 
 * <p>
 * The default keystore password is <b>jcryptool</b>. Users do not require this password.
 * </p>
 * 
 * @see org.jcryptool.crypto.keystore.backend.KeyStoreAlias
 * 
 * @author Tobias Kern, Dominik Schadow
 */
public class KeyStoreManager {
    /** Hard-coded default password for the platform keystore. */
    private static final char[] KEYSTORE_PASSWORD = { 'j', 'c', 'r', 'y', 'p', 't', 'o', 'o', 'l' };
    /** Hard-coded default password for the keys. */
    public static final char[] KEY_PASSWORD = { '1', '2', '3', '4' };
    /** JCrypTool keystore name, value is {@value} . */
    public static final String KEYSTORE_NAME = Messages.KeyStoreManager_11;
    /** FlexiProvider workspace settings folder, value is {@value} . */
    private static final String FLEXIPROVIDER_FOLDER = "flexiprovider"; //$NON-NLS-1$
    /** JCrypTool keystore file name, value is {@value} . */
    private static final String KEYSTORE_FILE = DirectoryService.getWorkspaceDir()
            + System.getProperty("file.separator") + FLEXIPROVIDER_FOLDER //$NON-NLS-1$
            + System.getProperty("file.separator") + "jctKeystore.ksf"; //$NON-NLS-1$ //$NON-NLS-2$
    /** The JCrypTool keystore of type JCEKS. */
    private KeyStore keyStore;
    /** The IFileStore representing the JCrypTool keystore. */
    private IFileStore platformKeystore;
    /** The JCrypTool keystore instance, only one instance exists. */
    private static KeyStoreManager instance;

    /**
     * The JCrypTool keystore is implemented as a singleton, therefore the constructor is private. Use
     * <code>getInstance()</code> to retrieve the active instance instead.
     * 
     * @see org.jcryptool.crypto.keystore.backend.KeyStoreManager#getInstance()
     */
    private KeyStoreManager() {
        ProviderManager2.getInstance();
        try {
//             keyStore = KeyStore.getInstance("JCEKS"); //$NON-NLS-1$
        	ProviderManager2.getInstance().pushFlexiProviderPromotion();
            keyStore = KeyStore.getInstance("JCEKS"); //$NON-NLS-1$
        } catch (KeyStoreException ex) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, Messages.KeyStoreManager_0, ex, true);
        } finally {
        	ProviderManager2.getInstance().popCryptoProviderPromotion();
        }

        platformKeystore = EFS.getLocalFileSystem().fromLocalFile(new File(KEYSTORE_FILE));

        if (keystoreExists()) {
            loadKeystore();
        } else {
            createDefaultKeystore();
        }
    }

    /**
     * <p>
     * Returns the fully initialized instance of the JCrypTool keystore. The returned instance is never null and always
     * fully initialized.
     * </p>
     * 
     * <p>
     * Use <code>KeyStoreManager ksm = KeyStoreManager.getInstance()</code> to retrieve the instance.
     * </p>
     * 
     * @return The JCrypTool keystore instance
     */
    public static synchronized KeyStoreManager getInstance() {
        if (instance == null) {
            instance = new KeyStoreManager();
        }

        return instance;
    }

    private boolean keystoreExists() {
        return (new File(KEYSTORE_FILE)).exists();
    }

    /**
     * Creates the JCrypTool default keystore.
     */
    private void createDefaultKeystore() {
        BufferedInputStream is = null;

        try {
        	ProviderManager2.getInstance().pushFlexiProviderPromotion();
            File flexiProvider = new File(DirectoryService.getWorkspaceDir(), FLEXIPROVIDER_FOLDER);
            if (!flexiProvider.exists()) {
                flexiProvider.mkdir();
            }

            String url = FileLocator.toFileURL(
                    FileLocator.find(Platform.getBundle(KeyStorePlugin.PLUGIN_ID),
                            new Path("keystore/jctKeystore.ksf"), null)).getPath(); //$NON-NLS-1$
            IFileStore originalKeystore = EFS.getLocalFileSystem().fromLocalFile(new File(url));
            originalKeystore.copy(platformKeystore, 0, null);

            is = new BufferedInputStream(platformKeystore.openInputStream(EFS.NONE, null));
            keyStore.load(is, KEYSTORE_PASSWORD);
        } catch (Exception ex) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, ex);
        } finally {
        	ProviderManager2.getInstance().popCryptoProviderPromotion();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
                }
            }
        }
    }

    /**
     * Loads the JCrypTool platform keystore file from the workspace directory. Creates the default keystore in case
     * this operation should fail.
     */
    private void loadKeystore() {
        InputStream is = null;
        try {
        	ProviderManager2.getInstance().pushFlexiProviderPromotion();

            is = new BufferedInputStream(platformKeystore.openInputStream(EFS.NONE, null));
            keyStore.load(is, KEYSTORE_PASSWORD);
        } catch (Exception e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
            createDefaultKeystore();
        } finally {
        	ProviderManager2.getInstance().popCryptoProviderPromotion();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
                }
            }
        }
    }

    /**
     * Saves the complete JCrypTool keystore in the workspace directory. Called after any operation that manipulates the
     * keystores content.
     */
    private void saveKeystore() {
        OutputStream os = null;
        try {
        	ProviderManager2.getInstance().pushFlexiProviderPromotion();

            os = new BufferedOutputStream(platformKeystore.openOutputStream(EFS.NONE, null));
            keyStore.store(os, KEYSTORE_PASSWORD);
        } catch (Exception ex) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, Messages.KeyStoreManager_1, ex, true);
        } finally {
        	ProviderManager2.getInstance().popCryptoProviderPromotion();
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
                }
            }
        }
    }
    
    public void backupKeystore(String pathToFile) {
        try {
        	ProviderManager2.getInstance().pushFlexiProviderPromotion();
            File backupFile = new File(pathToFile);
            URI uri = backupFile.toURI();
            IFileStore backupKeystore = EFS.getLocalFileSystem().getStore(uri);
            try {
            	platformKeystore.copy(backupKeystore, 0, null);
            } catch(CoreException ex) {
            	MessageBox mbox = new MessageBox(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
            		SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            	mbox.setMessage(NLS.bind(Messages.KeyStoreManager_9, pathToFile));
            	if(mbox.open() == SWT.YES) {
            		platformKeystore.copy(backupKeystore, EFS.OVERWRITE, null);
            	}
            }
        } catch (Exception ex) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, ex);
        } finally {
        	ProviderManager2.getInstance().popCryptoProviderPromotion();
        }
    }
    
    public void restoreKeystore(String pathToFile) {
        try {
        	ProviderManager2.getInstance().pushFlexiProviderPromotion();
            File flexiProvider = new File(DirectoryService.getWorkspaceDir(), FLEXIPROVIDER_FOLDER);
            if (!flexiProvider.exists()) {
                flexiProvider.mkdir();
            }

            File autoBackupFile = new File(flexiProvider, "autobackup.ksf"); //$NON-NLS-1$
            backupKeystore(autoBackupFile.getAbsolutePath());
            
            File backupFile = new File(pathToFile);
            URI uri = backupFile.toURI();
            IFileStore backupKeystore = EFS.getLocalFileSystem().getStore(uri);
            System.out.println(PlatformUI.getWorkbench().getDisplay().getShells());
            Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
            if (shell == null) {
            	shell = PlatformUI.getWorkbench().getDisplay().getShells()[0];
			}
        	MessageBox mbox = new MessageBox(shell,
            	SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            mbox.setMessage(Messages.KeyStoreManager_10);
            if(mbox.open() == SWT.YES) {
            	backupKeystore.copy(platformKeystore, EFS.OVERWRITE, null);
            	loadKeystore();
            }
        } catch (Exception ex) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, ex);
        } finally {
        	ProviderManager2.getInstance().popCryptoProviderPromotion();
        }
    }

    /**
     * Returns the keystore entry matching the given alias. Returned key may be of type secret key, keypair private key,
     * keypair public key or public key. Returned value may be null in case no entry is found or its type is not
     * supported.
     * 
     * @param alias The alias to look up
     * @param password The password of the key
     * @return The key retrieved from the keystore or null in case no matching entry was found
     * @throws NoSuchAlgorithmException In case the requested algorithm is not supported
     * @throws UnrecoverableEntryException In case the entered password does not match the given entry
     */
    public Key getKey(IKeyStoreAlias alias, char[] password) throws UnrecoverableEntryException,
            NoSuchAlgorithmException {
    	try {
    		ProviderManager2.getInstance().pushFlexiProviderPromotion();
			switch (alias.getKeyStoreEntryType()) {
			case SECRETKEY:
				return (Key) getSecretKey(alias, password);
			case KEYPAIR_PRIVATE_KEY:
				return (Key) getPrivateKey(alias, password);
			case KEYPAIR_PUBLIC_KEY:
				Certificate cert = getCertificate(alias);
				if (cert == null) {
					return null;
				}
				return (Key) cert.getPublicKey();
			case PUBLICKEY:
				cert = getCertificate(alias);
				if (cert == null) {
					return null;
				}
				return (Key) cert.getPublicKey();
			default:
				LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
						NLS.bind(Messages.KeyStoreManager_2, alias.getKeyStoreEntryType()), null, true);
				return null;
			}
    	} finally {
    		ProviderManager2.getInstance().popCryptoProviderPromotion();
    	}
    }

    /**
     * Returns the certificate for the given keystore alias.
     * 
     * @param alias The alias to look up
     * @return The retrieved certificate or null in case no matching entry was found
     * @throws NoSuchAlgorithmException In case the requested algorithm is not supported
     * @throws UnrecoverableEntryException In case the entry cannot be recovered
     */
    public Certificate getCertificate(IKeyStoreAlias alias) throws UnrecoverableEntryException,
            NoSuchAlgorithmException {
        try {
        	ProviderManager2.getInstance().pushFlexiProviderPromotion();
            return keyStore.getCertificate(alias.getAliasString());
        } catch (KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
        } finally {
        	ProviderManager2.getInstance().popCryptoProviderPromotion();	
        }

        return null;
    }

    /**
     * Returns the certificate chain for the given keystore alias.
     * 
     * @param alias The alias to look up
     * @return The retrieved certificate chain or null in case no matching entry was found
     * @throws NoSuchAlgorithmException In case the requested algorithm is not supported
     * @throws UnrecoverableEntryException In case the entered password does not match the selected entry
     */
    public Certificate[] getCertificateChain(IKeyStoreAlias alias, char[] password) throws UnrecoverableEntryException,
            NoSuchAlgorithmException {
        try {
        	try {
				ProviderManager2.getInstance().pushFlexiProviderPromotion();
				KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias.getAliasString(),
						new KeyStore.PasswordProtection(password));
				return entry.getCertificateChain();
			} finally {
				ProviderManager2.getInstance().popCryptoProviderPromotion();
			}
        } catch (KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
        }

        return null;
    }

    public static class PasswordDialog extends Dialog {
        private Text passwordField;
        private String passwordString;

        public PasswordDialog(Shell parentShell) {
            super(parentShell);
        }

        @Override
        protected void configureShell(Shell newShell)
        {
            super.configureShell(newShell);
            newShell.setText(Messages.KeyStoreManager_12);
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            Composite comp = (Composite) super.createDialogArea(parent);

            GridLayout layout = (GridLayout) comp.getLayout();
            layout.numColumns = 2;

            Label passwordLabel = new Label(comp, SWT.RIGHT);
            passwordLabel.setText(Messages.KeyStoreManager_13);
            passwordField = new Text(comp, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);

            GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            passwordField.setLayoutData(data);

            return comp;
        }

        @Override
        protected void okPressed()
        {
            passwordString = passwordField.getText();
            super.okPressed();
        }

        @Override
        protected void cancelPressed()
        {
            passwordField.setText(""); //$NON-NLS-1$
            super.cancelPressed();
        }

        public String getPassword()
        {
            return passwordString;
        }
    } 
    
    /**
     * Returns the private key for the given keystore alias.
     * 
     * @param alias The alias to look up
     * @param password The password of the selected entry
     * @return The retrieved private key or null in case no matching entry was found
     * @throws NoSuchAlgorithmException In case the requested algorithm is not supported
     * @throws UnrecoverableEntryException In case the entered password does not match the selected entry
     */
    public PrivateKey getPrivateKey(IKeyStoreAlias alias, char[] password) throws UnrecoverableEntryException,
            NoSuchAlgorithmException {
        try {
        	try {
				ProviderManager2.getInstance().pushFlexiProviderPromotion();
				KeyStore.PrivateKeyEntry entry;
				try {
					entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias.getAliasString(),
							new KeyStore.PasswordProtection(password));
				} catch (UnrecoverableEntryException e) {
					PasswordDialog dialog = new PasswordDialog(Display.getCurrent().getActiveShell());
					int result = dialog.open();
					if (result == Dialog.OK) {
						char[] pwchararr = dialog.getPassword().toCharArray();
						entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias.getAliasString(),
								new KeyStore.PasswordProtection(pwchararr));
					} else {
						throw e;
					}
				}
				return entry.getPrivateKey();
			} finally {
				ProviderManager2.getInstance().popCryptoProviderPromotion();
			}
        } catch (KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
        }

        return null;
    }

    /**
     * Returns the secret key for the given keystore alias.
     * 
     * @param alias The alias to look up
     * @param password The password of the selected entry
     * @return The retrieved secret key or null in case no matching entry was found
     * @throws NoSuchAlgorithmException In case the requested algorithm is not supported
     * @throws UnrecoverableEntryException In case the entered password does not match the selected entry
     */
    public SecretKey getSecretKey(IKeyStoreAlias alias, char[] password) throws UnrecoverableEntryException,
            NoSuchAlgorithmException {
        try {
        	try {
				ProviderManager2.getInstance().pushFlexiProviderPromotion();
				KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias.getAliasString(),
						new KeyStore.PasswordProtection(password));
				return entry.getSecretKey();
			} finally {
				ProviderManager2.getInstance().popCryptoProviderPromotion();
			}
        } catch (KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
        }

        return null;
    }

    /**
     * Returns the public key entry for the given private key. The returned value may be null in case no public key was
     * found.
     * 
     * @param privateAlias The private keystore alias
     * @return The public keystore alias
     */
    public KeyStoreAlias getPublicForPrivate(IKeyStoreAlias privateAlias) {
    	try {
			ProviderManager2.getInstance().pushFlexiProviderPromotion();
			if (privateAlias == null) {
				return null;
			}

			Enumeration<String> aliases = getAliases();

			while (aliases != null && aliases.hasMoreElements()) {
				KeyStoreAlias alias = new KeyStoreAlias(aliases.nextElement());

				if (alias.getKeyStoreEntryType() == KeyType.KEYPAIR_PUBLIC_KEY
						&& privateAlias.getHashValue().equalsIgnoreCase(alias.getHashValue())) {
					return alias;
				}
			}

			return null;
			
		} finally {
			ProviderManager2.getInstance().popCryptoProviderPromotion();
		}
    }

    /**
     * Returns the private key entry for the given public key. The returned value may be null in case no private key was
     * found.
     * 
     * @param publicAlias The public keystore alias
     * @return The private keystore alias
     */
    public KeyStoreAlias getPrivateForPublic(IKeyStoreAlias publicAlias) {
    	try {
			ProviderManager2.getInstance().pushFlexiProviderPromotion();
			if (publicAlias == null) {
				return null;
			}

			Enumeration<String> aliases = getAliases();

			while (aliases != null && aliases.hasMoreElements()) {
				KeyStoreAlias alias = new KeyStoreAlias(aliases.nextElement());

				if (alias.getKeyStoreEntryType() == KeyType.KEYPAIR_PRIVATE_KEY
						&& alias.getHashValue().equalsIgnoreCase(publicAlias.getHashValue())) {
					return alias;
				}
			}

			return null;
			
		} finally {
			ProviderManager2.getInstance().popCryptoProviderPromotion();
		}
    }

    /**
     * Returns all public keys available in the JCrypTool keystore.
     * 
     * @return All public keys available in the JCrypTool keystore.
     */
    public ArrayList<IKeyStoreAlias> getAllPublicKeys() {
    	try {
			ProviderManager2.getInstance().pushFlexiProviderPromotion();
			ArrayList<IKeyStoreAlias> publicKeys = new ArrayList<IKeyStoreAlias>();

			try {
				Enumeration<String> aliases = keyStore.aliases();

				while (aliases.hasMoreElements()) {
					KeyStoreAlias alias = new KeyStoreAlias(aliases.nextElement());
					if (alias.getKeyStoreEntryType().getType().contains(KeyType.KEYPAIR.getType())) {
						if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
							publicKeys.add(alias);
						}
					}
				}
			} catch (KeyStoreException e) {
				LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
			}

			return publicKeys;
			
		} finally {
			ProviderManager2.getInstance().popCryptoProviderPromotion();
		}
    }

    /**
     * Returns all private keys available in the JCrypTool keystore.
     * 
     * @return All private keys available in the JCrypTool keystore.
     */
    public ArrayList<IKeyStoreAlias> getAllPrivateKeys() {
    	try {
			ProviderManager2.getInstance().pushFlexiProviderPromotion();
			ArrayList<IKeyStoreAlias> privateKeys = new ArrayList<IKeyStoreAlias>();

			try {
				Enumeration<String> aliases = keyStore.aliases();

				while (aliases.hasMoreElements()) {
					KeyStoreAlias alias = new KeyStoreAlias(aliases.nextElement());
					if (alias.getKeyStoreEntryType().getType().contains(KeyType.KEYPAIR.getType())) {
						if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
							privateKeys.add(alias);
						}
					}
				}
			} catch (KeyStoreException e) {
				LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
			}

			return privateKeys;
			
		} finally {
			ProviderManager2.getInstance().popCryptoProviderPromotion();
		}
    }

    /**
     * Lists all the alias names of the JCrypTool keystore.
     * 
     * @return Enumeration of the alias names
     */
    public Enumeration<String> getAliases() {
        try {
            return keyStore.aliases();
        } catch (KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, e);
        }

        return null;
    }

    /**
     * Deletes all keystore entries for the given contact and removes the contact afterwards.
     * 
     * @param contactName The contact name to delete
     */
    public void deleteAllEntriesForContact(String contactName) {
        Enumeration<String> aliases = getAliases();

        while (aliases != null && aliases.hasMoreElements()) {
            KeyStoreAlias alias = new KeyStoreAlias(aliases.nextElement());
            if (alias.getContactName().equals(contactName)) {
                try {
                    keyStore.deleteEntry(alias.getAliasString());
                } catch (KeyStoreException e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
                            NLS.bind(Messages.KeyStoreManager_3, alias.getAliasString()), e, true);
                }
            }
        }

        saveKeystore();
        ContactManager.getInstance().removeContact(contactName);
    }

    /**
     * Deletes the selected keystore entry. A selected public key automatically deletes the corresponding private key
     * and vice versa.
     * 
     * @param alias The keystore entry to delete
     */
    public void deleteEntry(IKeyStoreAlias alias) {
        try {
            if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
                KeyStoreAlias pub = getPublicForPrivate(alias);
                if (pub != null) {
                    keyStore.deleteEntry(pub.getAliasString());
                }
            } else if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
                KeyStoreAlias priv = getPrivateForPublic(alias);
                if (priv != null) {
                    keyStore.deleteEntry(priv.getAliasString());
                }
            }

            keyStore.deleteEntry(alias.getAliasString());
            saveKeystore();
            ContactManager.getInstance().removeEntry(alias);
        } catch (KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, NLS.bind(Messages.KeyStoreManager_3, alias.getAliasString()), e,
                    true);
        }
    }

    /**
     * Adds the given certificate using the information provided as alias to the JCrypTool keystore.
     * 
     * @param certificate The certificate to add
     * @param alias The certificate metadata
     */
    public void addCertificate(Certificate certificate, IKeyStoreAlias alias) {
    	try {
			ProviderManager2.getInstance().pushFlexiProviderPromotion();
			try {
				keyStore.setEntry(alias.getAliasString(), new KeyStore.TrustedCertificateEntry(certificate), null);
				saveKeystore();
				ContactManager.getInstance().addCertificate(alias);
			} catch (KeyStoreException e) {
				LogUtil.logError(KeyStorePlugin.PLUGIN_ID, NLS.bind(Messages.KeyStoreManager_4, alias.getAliasString()), e,
						true);
			}
			
		} finally {
			ProviderManager2.getInstance().popCryptoProviderPromotion();
		}
    }

    /**
     * Adds the given secret key using the information provided as alias to the JCrypTool keystore.
     * 
     * @param key The secret key to add
     * @param password The secret key password
     * @param alias The certificate metadata
     */
    public void addSecretKey(SecretKey key, char[] password, IKeyStoreAlias alias) {
        try {
            keyStore.setEntry(alias.getAliasString(), new KeyStore.SecretKeyEntry(key),
                    new KeyStore.PasswordProtection(password));
            saveKeystore();
            ContactManager.getInstance().addSecretKey(alias);
        } catch (KeyStoreException e) {
            LogUtil.logError(KeyStorePlugin.PLUGIN_ID, NLS.bind(Messages.KeyStoreManager_5, alias.getAliasString()), e,
                    true);
        }
    }

    /**
     * Adds the given key pair using the information provided as alias to the JCrypTool keystore.
     * 
     * @param privateKey The private key to add
     * @param publicKey The public key to add
     * @param password The private key password
     * @param publicAlias The public key metadata
     * @param privateAlias The private key metadata
     */
    public void addKeyPair(PrivateKey privateKey, Certificate publicKey, char[] password, IKeyStoreAlias privateAlias,
            IKeyStoreAlias publicAlias) {
    	try {
			ProviderManager2.getInstance().pushFlexiProviderPromotion();
			Certificate[] certs = new Certificate[1];
			certs[0] = publicKey;
			try {
				keyStore.setEntry(privateAlias.getAliasString(), new KeyStore.PrivateKeyEntry(privateKey, certs),
						new KeyStore.PasswordProtection(password));
				keyStore.setEntry(publicAlias.getAliasString(), new KeyStore.TrustedCertificateEntry(publicKey), null);
				saveKeystore();
				ContactManager.getInstance().addKeyPair(privateAlias, publicAlias);
			} catch (KeyStoreException e) {
				LogUtil.logError(KeyStorePlugin.PLUGIN_ID,
						NLS.bind(Messages.KeyStoreManager_6, publicAlias.getAliasString()), e, true);
			}
			
		} finally {
			ProviderManager2.getInstance().popCryptoProviderPromotion();
		}
    }

    /**
     * Updates the private key in a key pair. Before updating this method ensures that the private key is available in
     * the keystore and that the password isn't changed.
     * 
     * @param privateKey The private key to update
     * @param password The private key password
     * @param alias The private key metadata
     * @throws UnrecoverableEntryException In case the entered password does not match the selected entry
     * @throws NoSuchAlgorithmException In case the requested algorithm is not supported
     */
    public void updateKeyPair(PrivateKey privateKey, char[] password, IKeyStoreAlias alias)
            throws UnrecoverableEntryException, NoSuchAlgorithmException {
    	try {
			ProviderManager2.getInstance().pushFlexiProviderPromotion();
			try {
				getPrivateKey(alias, password);
			} catch (Exception e) {
				LogUtil.logError(KeyStorePlugin.PLUGIN_ID, NLS.bind(Messages.KeyStoreManager_8, alias.getAliasString()), e,
						true);
				return;
			}

			try {
				KeyStoreAlias publicAlias = getPublicForPrivate(alias);
				Certificate publicKey = getCertificate(publicAlias);
				Certificate[] certs = new Certificate[1];
				certs[0] = publicKey;

				keyStore.setEntry(alias.getAliasString(), new KeyStore.PrivateKeyEntry(privateKey, certs),
						new KeyStore.PasswordProtection(password));
				saveKeystore();
			} catch (KeyStoreException e) {
				LogUtil.logError(KeyStorePlugin.PLUGIN_ID, NLS.bind(Messages.KeyStoreManager_7, alias.getAliasString()), e,
						true);
			}
			
		} finally {
			ProviderManager2.getInstance().popCryptoProviderPromotion();
		}
    }
}