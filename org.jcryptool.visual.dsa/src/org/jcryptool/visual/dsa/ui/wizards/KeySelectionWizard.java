// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.dsa.ui.wizards;

import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.interfaces.DSAParams;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.certificates.CertFact;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.exceptions.NoKeyStoreFileException;
import org.jcryptool.visual.dsa.Action;
import org.jcryptool.visual.dsa.DSAData;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.ChooseKeytypePage;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.DecryptSignPage;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.EncryptVerifyPage;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.LoadKeypairPage;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.LoadPublicKeyPage;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.NewKeypairPage;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.NewPublicKeyPage;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.SaveKeypairPage;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.SavePublicKeyPage;

import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.core.dsa.DSAKeyFactory;
import de.flexiprovider.core.dsa.DSAPrivateKey;
import de.flexiprovider.core.dsa.DSAPrivateKeySpec;
import de.flexiprovider.core.dsa.DSAPublicKey;
import de.flexiprovider.core.dsa.DSAPublicKeySpec;

/**
 * wizard for key selection and creation.
 * @author Michael Gaber
 */
public class KeySelectionWizard extends Wizard {

	/** title of this wizard, displayed in the titlebar. */
	private static final String TITLE = "Schlüsselauswahl";

	/** action, whether it's encrypt, decrypt, verify or sign. */
	private final Action action;

	/** shared data object for exchanging data. */
	private final DSAData data;

	/** whether this Wizard is called as standalone or withing the algorithm. */
	private final boolean standalone;

	/**
	 * Constructor, setting title, action and data.
	 * @param action the cryptographic action
	 * @param data the data object
	 * @param standalone selects whether this wizard is stand-alone. If it is there is no setting of any variables.
	 */
	public KeySelectionWizard(final Action action, final DSAData data, final boolean standalone) {
		this.action = action;
		if (standalone) {
			this.data = new DSAData();
			this.data.setStandalone(standalone);
		} else {
			this.data = data;
		}
		this.setWindowTitle(TITLE);
		this.standalone = standalone;
	}

	@Override
	public final void addPages() {
		if (standalone) {
			addPage(new ChooseKeytypePage());
			addPage(new NewKeypairPage(data));
			addPage(new SaveKeypairPage(data));
			addPage(new NewPublicKeyPage(data));
			addPage(new SavePublicKeyPage(data));
		} else {
			switch (action) {
			case SignAction:
				addPage(new DecryptSignPage());
				addPage(new LoadKeypairPage(data));
				addPage(new NewKeypairPage(data));
				addPage(new SaveKeypairPage(data));
				break;
			case VerifyAction:
				addPage(new EncryptVerifyPage());
				addPage(new LoadPublicKeyPage(data));
				addPage(new NewPublicKeyPage(data));
				addPage(new SavePublicKeyPage(data));
				break;
			default:
				break;
			}
		}
	}

	@Override
	public final boolean canFinish() {
		boolean rv = true;
		if (standalone) {
			rv = (((ChooseKeytypePage) getPage(ChooseKeytypePage.getPagename())).keypair() && getPage(
					SaveKeypairPage.getPagename()).isPageComplete())
					|| (!((ChooseKeytypePage) getPage(ChooseKeytypePage.getPagename())).keypair() && getPage(
							SavePublicKeyPage.getPagename()).isPageComplete());
		} else {
			IWizardPage page;
			switch (action) {
			case SignAction:
				page = getPage(DecryptSignPage.getPagename());
				rv &= page.isPageComplete();
				if (((DecryptSignPage) page).wantNewKey()) {
					page = getPage(NewKeypairPage.getPagename());
					rv &= page.isPageComplete();
					if (((NewKeypairPage) page).wantSave()) {
						rv &= getPage(SaveKeypairPage.getPagename()).isPageComplete();
					}
				} else {
					rv &= getPage(LoadKeypairPage.getPagename()).isPageComplete();
				}
				break;
			case VerifyAction:
				page = getPage(EncryptVerifyPage.getPagename());
				rv &= page.isPageComplete();
				if (((EncryptVerifyPage) page).wantNewKey()) {
					page = getPage(NewPublicKeyPage.getPagename());
					rv &= page.isPageComplete();
					if (((NewPublicKeyPage) page).wantSave()) {
						rv &= getPage(SavePublicKeyPage.getPagename()).isPageComplete();
					}
				} else {
					rv &= getPage(LoadPublicKeyPage.getPagename()).isPageComplete();
				}
				break;
			default:
				rv = false;
			}
		}
		return rv;
	}

	@Override
	public final boolean performFinish() {
		if (action == null) {
			save(((ChooseKeytypePage) getPage(ChooseKeytypePage.getPagename())).keypair());
			return true;
		}
		try {
			switch (action) {
			case SignAction:
				if (((DecryptSignPage) getPage(DecryptSignPage.getPagename())).wantNewKey()) {
					if (((NewKeypairPage) getPage(NewKeypairPage.getPagename())).wantSave()) {
						save(true);
					}
				} else {
					final KeyStoreManager ksm = KeyStoreManager.getInstance();
					final KeyStoreAlias privAlias = data.getPrivateAlias();
					final String password = data.getPassword();
					final PrivateKey key = ksm.getPrivateKey(privAlias, password.toCharArray());
					final DSAPrivateKey privkey = (DSAPrivateKey) key;
					final DSAParams params = privkey.getParams();

					data.setP(params.getP());
					data.setQ(params.getQ());
					data.setGenerator(params.getG());
					data.setY(params.getG().modPow(privkey.getX(), params.getP()));
					data.setX(privkey.getX());
				}
				break;
			case VerifyAction:
				if (((EncryptVerifyPage) getPage(EncryptVerifyPage.getPagename())).wantNewKey()) {
					if (((NewPublicKeyPage) getPage(NewPublicKeyPage.getPagename())).wantSave()) {
						save(false);
					}
				} else {
					final KeyStoreManager ksm = KeyStoreManager.getInstance();
					final KeyStoreAlias publicAlias = data.getPublicAlias();
					final DSAPublicKey pubkey = (DSAPublicKey) ksm.getPublicKey(publicAlias).getPublicKey();
					final DSAParams params = pubkey.getParams();
					data.setP(params.getP());
					data.setQ(params.getQ());
					data.setGenerator(params.getG());
					data.setY(pubkey.getY());
				}
				break;
			default:
				save(((ChooseKeytypePage) getPage(ChooseKeytypePage.getPagename())).keypair());
			}
			return true;
		} catch (final KeyStoreException e) {
			// TODO: handle exception
		    LogUtil.logError(e);
		} catch (final Exception e) {
		    LogUtil.logError(e);
		}
		return false;
	}

	/**
	 * Saves the keypair or private key this wizard constructs to the platform keystore.
	 * @param keypair <code>true</code> if the key to save is a keypair or <code>false</code> if it's only a public key.
	 */
	private void save(final boolean keypair) {
		final KeyStoreManager ksm = KeyStoreManager.getInstance();
		try {
			ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());

			final DSAKeyFactory factory = new DSAKeyFactory();
			final DSAPublicKey pubkey = (DSAPublicKey) factory.generatePublic(new DSAPublicKeySpec(data.getY(), data
					.getP(), data.getQ(), data.getGenerator()));
			final KeyStoreAlias publicAlias = new KeyStoreAlias(data.getContactName(), KeyType.KEYPAIR_PUBLIC_KEY, "",
					data.getP().bitLength(), data.getContactName().concat(data.getP().toString()).hashCode() + "",
					pubkey.getClass().getName());

			if (keypair) {
				final DSAPrivateKey privkey = (DSAPrivateKey) factory.generatePrivate(new DSAPrivateKeySpec(
						data.getX(), data.getP(), data.getQ(), data.getGenerator()));
				final KeyStoreAlias privateAlias = new KeyStoreAlias(data.getContactName(),
						KeyType.KEYPAIR_PRIVATE_KEY, "", data.getP().bitLength(), data.getContactName().concat(
								data.getP().toString()).hashCode()
								+ "", privkey.getClass().getName());
				ksm.addKeyPair(privkey, CertFact.getDummyCertificate(pubkey), data.getPassword(), privateAlias,
						publicAlias);
			} else {
				ksm.addCertificate(CertFact.getDummyCertificate(pubkey), publicAlias);
			}
		} catch (final NoKeyStoreFileException e) {
		    LogUtil.logError(e);
		} catch (final InvalidKeySpecException e) {
		    LogUtil.logError(e);
		}
	}
}
