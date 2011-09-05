// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui.wizards;

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
import org.jcryptool.visual.elGamal.Action;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.ChooseKeytypePage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.DecryptSignPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.EncryptVerifyPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.LoadKeypairPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.LoadPublicKeyPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.NewKeypairPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.NewPublicKeyPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.SaveKeypairPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.SavePublicKeyPage;

import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.core.elgamal.ElGamalKeyFactory;
import de.flexiprovider.core.elgamal.ElGamalPrivateKey;
import de.flexiprovider.core.elgamal.ElGamalPrivateKeySpec;
import de.flexiprovider.core.elgamal.ElGamalPublicKey;
import de.flexiprovider.core.elgamal.ElGamalPublicKeySpec;

/**
 * wizard for key selection and creation.
 *
 * @author Michael Gaber
 */
public class KeySelectionWizard extends Wizard {

    /** title of this wizard, displayed in the titlebar. */
    private static final String TITLE = Messages.KeySelectionWizard_keyselection;

    /** action, whether it's encrypt, decrypt, verify or sign. */
    private final Action action;

    /** shared data object for exchanging data. */
    private final ElGamalData data;

    /** whether this Wizard is called as standalone or withing the algorithm. */
    private final boolean standalone;

    /**
     * Constructor, setting title, action and data.
     *
     * @param action the cryptographic action
     * @param data the data object
     * @param standalone selects whether this wizard is stand-alone. If it is there is no setting of any variables.
     */
    public KeySelectionWizard(final Action action, final ElGamalData data, final boolean standalone) {
        this.action = action;
        if (standalone) {
            this.data = new ElGamalData(null);
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
            switch (action) {
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
        if (action == null) {
            save(((ChooseKeytypePage) getPage(ChooseKeytypePage.getPagename())).keypair());
            return true;
        }
        try {
            switch (action) {
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
                        final ElGamalPrivateKey privkey = (ElGamalPrivateKey) key;
                        data.setModulus(privkey.getModulus().bigInt);
                        data.setGenerator(privkey.getGenerator().bigInt);
                        data.setPublicA(privkey.getPublicA().bigInt);
                        data.setA(privkey.getA().bigInt);
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
                        final ElGamalPublicKey pubkey = (ElGamalPublicKey) ksm.getPublicKey(publicAlias).getPublicKey();
                        data.setModulus(pubkey.getModulus().bigInt);
                        data.setGenerator(pubkey.getGenerator().bigInt);
                        data.setPublicA(pubkey.getPublicA().bigInt);
                    }
                    break;
                default:
                    save(((ChooseKeytypePage) getPage(ChooseKeytypePage.getPagename())).keypair());
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
     * Saves the key pair or private key this wizard constructs to the platform keystore.
     *
     * @param keypair <code>true</code> if the key to save is a key pair or <code>false</code> if it's only a public
     *        key.
     */
    private void save(final boolean keypair) {
        final KeyStoreManager ksm = KeyStoreManager.getInstance();
        try {
            ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
            final FlexiBigInt modulus = new FlexiBigInt(data.getModulus()), generator = new FlexiBigInt(
                    data.getGenerator()), publicA = new FlexiBigInt(data.getPublicA());
            final ElGamalKeyFactory factory = new ElGamalKeyFactory();
            final ElGamalPublicKey pubkey = (ElGamalPublicKey) factory.generatePublic(new ElGamalPublicKeySpec(modulus,
                    generator, publicA));

            final KeyStoreAlias publicAlias = new KeyStoreAlias(
                    data.getContactName(),
                    KeyType.KEYPAIR_PUBLIC_KEY,
                    "", data.getModulus().bitLength(), (data //$NON-NLS-1$
                            .getContactName().concat(data.getModulus().toString())).hashCode() + "", pubkey.getClass().getName()); //$NON-NLS-1$
            if (keypair) {
                final ElGamalPrivateKey privkey = (ElGamalPrivateKey) factory
                        .generatePrivate(new ElGamalPrivateKeySpec(modulus, generator, publicA, new FlexiBigInt(data
                                .getA())));
                final KeyStoreAlias privateAlias = new KeyStoreAlias(
                        data.getContactName(),
                        KeyType.KEYPAIR_PRIVATE_KEY,
                        "", data.getModulus().bitLength(), (data //$NON-NLS-1$
                                .getContactName().concat(data.getModulus().toString())).hashCode() + "", privkey.getClass().getName()); //$NON-NLS-1$
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
