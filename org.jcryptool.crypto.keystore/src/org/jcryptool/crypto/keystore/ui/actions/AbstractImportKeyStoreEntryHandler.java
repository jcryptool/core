// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.crypto.SecretKey;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.descriptors.ImportDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IImportDescriptor;
import org.jcryptool.crypto.keystore.keys.KeyType;

import codec.asn1.ASN1Exception;
import codec.pkcs12.CertBag;
import codec.pkcs12.PFX;
import codec.pkcs12.PKCS8ShroudedKeyBag;
import codec.pkcs12.SafeBag;
import de.flexiprovider.core.dsa.interfaces.DSAPublicKey;
import de.flexiprovider.core.rsa.interfaces.RSAPublicKey;

public abstract class AbstractImportKeyStoreEntryHandler extends AbstractKeyStoreHandler {
    protected void performImportAction(IImportDescriptor descriptor, Object importedObject)
            throws IllegalArgumentException {
        if (descriptor.getKeyStoreEntryType().equals(KeyType.SECRETKEY)) {
            if (importedObject instanceof SecretKey) {
                LogUtil.logInfo("importing secret key"); //$NON-NLS-1$
                addSecretKey(descriptor, (SecretKey) importedObject);
            } else {
                throw new IllegalArgumentException("Parameter is not as expected an instance of SecretKey");
            }
        } else if (descriptor.getKeyStoreEntryType().equals(KeyType.KEYPAIR)) {
            if (importedObject instanceof PFX) {
                LogUtil.logInfo("importing pfx"); //$NON-NLS-1$
                PFX pfx = (PFX) importedObject;
                try {
                    char[] password = promptPassword();
                    if (password == null)
                        return;

                    SafeBag safeBag = pfx.getAuthSafe().getSafeContents(0).getSafeBag(0);
                    PKCS8ShroudedKeyBag kBag = (PKCS8ShroudedKeyBag) safeBag.getBagValue();
                    PrivateKey privKey = kBag.getPrivateKey(password);

                    SafeBag certBag = pfx.getAuthSafe().getSafeContents(1, password).getSafeBag(0);
                    CertBag cBag = (CertBag) certBag.getBagValue();
                    PublicKey pubKey = cBag.getCertificate().getPublicKey();

                    int keySize = -1;
                    if (pubKey instanceof RSAPublicKey)
                        keySize = ((RSAPublicKey) pubKey).getN().bitLength();
                    else if (pubKey instanceof DSAPublicKey)
                        keySize = ((DSAPublicKey) pubKey).getParameters().getP().bitLength();
                    // TODO: Add keySize calculation for the remaining
                    // algorithms.

                    ImportDescriptor newDescriptor = new ImportDescriptor(descriptor.getContactName(),
                            privKey.getAlgorithm(), KeyType.KEYPAIR, descriptor.getFileName(),
                            descriptor.getPassword(), descriptor.getProvider(), keySize);

                    addKeyPair(newDescriptor, privKey, pubKey);
                } catch (ASN1Exception e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "error while importing key pair", e, true);
                } catch (IOException e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "error while importing key pair", e, false);
                } catch (GeneralSecurityException e) {
                    LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "error while importing key pair", e, true);
                }
            } else {
                throw new IllegalArgumentException("Parameter is not an instance of PFX, as expected");
            }
        } else if (descriptor.getKeyStoreEntryType().equals(KeyType.PUBLICKEY)) {
            if (importedObject instanceof Certificate) {
                LogUtil.logInfo("importing certificate"); //$NON-NLS-1$
                addCertificate(descriptor, (Certificate) importedObject);
            } else {
                throw new IllegalArgumentException("Parameter is not an instance of Certificate, as expected");
            }
        }
    }

    protected char[] promptPassword() {
        InputDialog dialog = new InputDialog(null, Messages.getString("PasswordPromt.Title"), //$NON-NLS-1$
                Messages.getString("PasswordPromt.Message"), //$NON-NLS-1$
                "", null) { //$NON-NLS-1$

            protected Control createDialogArea(Composite parent) {
                Control control = super.createDialogArea(parent);
                getText().setEchoChar('*');
                return control;
            }
        };
        int result = dialog.open();
        if (result == Window.OK) {
            return dialog.getValue().toCharArray();
        } else {
            return null;
        }
    }

}
