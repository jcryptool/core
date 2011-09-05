// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.rsa.ui.wizards;

import java.math.BigInteger;
import java.security.KeyStoreException;
import java.security.PrivateKey;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.certificates.CertFact;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.exceptions.NoKeyStoreFileException;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.ChooseKeytypePage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.DecryptSignPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EncryptVerifyPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.LoadKeypairPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.LoadPublicKeyPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.NewKeypairPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.NewPublicKeyPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.SaveKeypairPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.SavePublicKeyPage;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.core.rsa.RSAPrivateCrtKey;
import de.flexiprovider.core.rsa.RSAPublicKey;

/**
 * wizard for key selection and creation.
 *
 * @author Michael Gaber
 */
public class KeySelectionWizard extends Wizard {
    /** shared data object for exchanging data. */
    private final RSAData data;

    /** whether this Wizard is called as standalone or within the algorithm. */
    private final boolean standalone;

    /**
     * Constructor, setting title, action and data.
     *
     * @param action the cryptographic action
     * @param data the data object
     * @param standalone selects whether this wizard is stand-alone. If it is there is no setting of any variables.
     */
    public KeySelectionWizard(final RSAData data, final boolean standalone) {
        if (standalone) {
            this.data = new RSAData(null);
            this.data.setStandalone(standalone);
        } else {
            this.data = data;
        }
        this.setWindowTitle(Messages.KeySelectionWizard_key_selection);
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
            switch (data.getAction()) {
                case DecryptAction:
                case SignAction:
                    addPage(new DecryptSignPage());
                    addPage(new LoadKeypairPage(data));
                    addPage(new NewKeypairPage(data));
                    addPage(new SaveKeypairPage(data));
                    break;
                case EncryptAction:
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
            switch (data.getAction()) {
                case DecryptAction:
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
                case EncryptAction:
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
        try {
            if (data.getAction() == null) {
                save(((ChooseKeytypePage) getPage(ChooseKeytypePage.getPagename())).keypair());
                return true;
            } else {
                switch (data.getAction()) {
                    case DecryptAction:
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
                            final RSAPrivateCrtKey privkey = (RSAPrivateCrtKey) key;
                            data.setN(privkey.getModulus());
                            data.setD(privkey.getD().bigInt);
                            data.setP(privkey.getP().bigInt);
                            data.setQ(privkey.getQ().bigInt);
                            data.setE(privkey.getPublicExponent());
                        }
                        break;
                    case EncryptAction:
                    case VerifyAction:
                        if (((EncryptVerifyPage) getPage(EncryptVerifyPage.getPagename())).wantNewKey()) {
                            if (((NewPublicKeyPage) getPage(NewPublicKeyPage.getPagename())).wantSave()) {
                                save(false);
                            }
                        } else {
                            final KeyStoreManager ksm = KeyStoreManager.getInstance();
                            final KeyStoreAlias publicAlias = data.getPublicAlias();
                            final RSAPublicKey pubkey = (RSAPublicKey) ksm.getPublicKey(publicAlias).getPublicKey();
                            data.setN(pubkey.getModulus());
                            data.setE(pubkey.getPublicExponent());
                        }
                        break;
                    default:

                }
            }
            return true;
        } catch (final KeyStoreException e) {
            LogUtil.logError(e);
        } catch (final Exception e) {
            LogUtil.logError(e);
        }
        return false;
    }

    /**
     * Saves the keypair or private key this wizard constructs to the platform keystore.
     *
     * @param keypair <code>true</code> if the key to save is a keypair or <code>false</code> if it's only a public key.
     */
    private void save(final boolean keypair) {
        final KeyStoreManager ksm = KeyStoreManager.getInstance();
        try {
            ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
        } catch (final NoKeyStoreFileException e) {
            LogUtil.logError(e);
        }
        final FlexiBigInt n = new FlexiBigInt(data.getN()), e = new FlexiBigInt(data.getE());
        final RSAPublicKey pubkey = new RSAPublicKey(n, e);

        final KeyStoreAlias publicAlias = new KeyStoreAlias(data.getContactName(), KeyType.KEYPAIR_PUBLIC_KEY,
                "", new BigInteger(data.getN().toString()).bitLength(), //$NON-NLS-1$
                (data.getContactName().concat(data.getN().toString())).hashCode() + "", pubkey //$NON-NLS-1$
                        .getClass().getName());
        data.setPublicAlias(publicAlias);
        if (keypair) {
            final RSAPrivateCrtKey privkey = new RSAPrivateCrtKey(n, e, new FlexiBigInt(data.getD()), new FlexiBigInt(
                    data.getP()), new FlexiBigInt(data.getQ()), FlexiBigInt.ZERO, FlexiBigInt.ZERO, FlexiBigInt.ZERO);
            final KeyStoreAlias privateAlias = new KeyStoreAlias(data.getContactName(), KeyType.KEYPAIR_PRIVATE_KEY,
                    "", new BigInteger(data.getN().toString()).bitLength(), (data.getContactName().concat(data.getN() //$NON-NLS-1$
                            .toString())).hashCode() + "", privkey.getClass().getName()); //$NON-NLS-1$
            data.setPrivateAlias(privateAlias);
            ksm.addKeyPair(privkey, CertFact.getDummyCertificate(pubkey), data.getPassword(), privateAlias, publicAlias);
        } else {
            ksm.addCertificate(CertFact.getDummyCertificate(pubkey), publicAlias);
        }
    }
}
