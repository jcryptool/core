// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.engines.cipher;

import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEnginesPlugin;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.Key;

public class AsymmetricHybridCipherEngine extends CipherEngine {

    @Override
    public KeyObject init(IFlexiProviderOperation operation) {
        LogUtil.logInfo("initializing asymmetric hybrid cipher engine"); //$NON-NLS-1$
        this.operation = operation;
        char[] password = null;
        Key key = null;
        try {
            cipher = Registry.getAsymmetricHybridCipher(operation.getAlgorithmDescriptor().getAlgorithmName());
            if (operation.getOperation().equals(OperationType.DECRYPT)) {

                // password may be contained in the ActionItem, otherwise prompt
                if (operation.getPassword() != null) {
                    password = operation.getPassword();
                } else {
                    password = promptPassword();
                }

                if (password == null) {
                    return null;
                }

                key = (Key) KeyStoreManager.getInstance().getPrivateKey(operation.getKeyStoreAlias(), password);
                cipher.initDecrypt(key, operation.getAlgorithmDescriptor().getAlgorithmParameterSpec());
            } else {
                Certificate certificate = KeyStoreManager.getInstance().getCertificate(operation.getKeyStoreAlias());
                key = (Key) certificate.getPublicKey();
                cipher.initEncrypt(key, operation.getAlgorithmDescriptor().getAlgorithmParameterSpec(),
                        FlexiProviderEnginesPlugin.getSecureRandom());
            }
            operation.setPassword(password); // save in the operation if no exception occurred
            initialized = true;
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "NoSuchAlgorithmException while initializing a cipher engine", e, true); //$NON-NLS-1$
            return null;
        } catch (InvalidKeyException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, Messages.AsymmetricHybridCipherEngine_1, e, true);
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "InvalidAlgorithmParameterException while initializing a cipher engine", e, true); //$NON-NLS-1$
            return null;
        } catch (UnrecoverableEntryException e) {
            JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, FlexiProviderEnginesPlugin.PLUGIN_ID,
                    Messages.ExAccessKeystorePassword, e));
            return null;
        } catch (UnsupportedOperationException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "UnsupportedOperationException while initializing a cipher engine", e, true); //$NON-NLS-1$
            return null;
        } catch (Exception e) {
            LogUtil.logError(e);
            return null;
        }

        return new KeyObject(key, password);
    }
}
