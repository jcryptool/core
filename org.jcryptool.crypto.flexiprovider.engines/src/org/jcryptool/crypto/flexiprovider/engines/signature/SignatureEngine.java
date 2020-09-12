// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.engines.signature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.ProviderMismatchException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.providers.ProviderManager2;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEngine;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEnginesPlugin;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.Signature;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.exceptions.SignatureException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.api.keys.PrivateKey;
import de.flexiprovider.api.keys.PublicKey;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

public class SignatureEngine extends FlexiProviderEngine {
    private Signature signature;

    @Override
    public KeyObject init(IFlexiProviderOperation operation) {
        LogUtil.logInfo("initializing signature engine"); //$NON-NLS-1$
        this.operation = operation;
        char[] password = null;
        KeyObject usedKey = null;
        try {
        	// this tries to 
//             ProviderManager2.getInstance().setProviders__flexiPromoted();
            signature = Registry.getSignature(operation.getAlgorithmDescriptor().getAlgorithmName());
            AlgorithmParameterSpec spec = operation.getAlgorithmDescriptor().getAlgorithmParameterSpec();
            if (spec != null) {
                signature.setParameters(spec);
            }
            if (operation.getOperation().equals(OperationType.SIGN)) {
                if (operation.getPassword() != null) {
                    password = operation.getPassword();
                } else {
                    password = promptPassword();
                }

                if (password == null) {
                    return null;
                }

                Key privateKey = (Key) KeyStoreManager.getInstance().getPrivateKey(operation.getKeyStoreAlias(),
                        password);
                signature.initSign((PrivateKey) privateKey, FlexiProviderEnginesPlugin.getSecureRandom());

                usedKey = new KeyObject(privateKey, password);
                operation.setPassword(password); // save in the operation if no exception occurred
            } else {
                Certificate certificate = KeyStoreManager.getInstance().getCertificate(operation.getKeyStoreAlias());

                Key publicKey = (Key) certificate.getPublicKey();

                signature.initVerify((PublicKey) publicKey);
                usedKey = new KeyObject(publicKey, password);
            }
            initialized = true;
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "NoSuchAlgorithmException while initializing a signature", e, true); //$NON-NLS-1$
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "InvalidAlgorithmParameterException while initializing a signature", e, true); //$NON-NLS-1$
            return null;
        } catch (InvalidKeyException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, Messages.SignatureEngine_5, e, true);
            return null;
        } catch (UnrecoverableEntryException e) {
            JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, FlexiProviderEnginesPlugin.PLUGIN_ID,
                    Messages.ExAccessKeystorePassword, e));
            return null;
        } catch (Exception e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "Exception while initializing a signature", e, true); //$NON-NLS-1$
            return null;
        } finally {
//         	ProviderManager2.getInstance().setProviders__sunPromoted();
        }
        return usedKey;
    }

    @Override
    public void perform(KeyObject usedKey) {
        if (initialized) {
            LogUtil.logInfo("performing a signature"); //$NON-NLS-1$
            InputStream inputStream = initInput(operation.getInput());
            try {
                int i;
                while ((i = inputStream.read()) != -1) {
                    signature.update((byte) i);
                }
                inputStream.close();
                if (operation.getOperation().equals(OperationType.SIGN)) {
                    OutputStream outputStream = initOutput(operation.getSignature());
                    byte[] signatureBytes = signature.sign();
                    outputStream.write(signatureBytes);
                    outputStream.close();

                    try {
                        KeyStoreManager.getInstance().updateKeyPair((PrivateKey) usedKey.getKey(),
                                usedKey.getPassword(), (KeyStoreAlias) operation.getKeyStoreAlias());
                    } catch (UnrecoverableEntryException e) {
                        LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, e);
                    } catch (java.security.NoSuchAlgorithmException e) {
                        LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, e);
                    }
                    // }

                } else {
                    InputStream signatureInputStream = initInput(operation.getSignature());
                    // rotten hack...
                    List<Byte> signatureByteList = new ArrayList<Byte>();
                    int s;
                    while ((s = signatureInputStream.read()) != -1) {
                        signatureByteList.add((byte) s);
                    }
                    signatureInputStream.close();
                    byte[] signatureBytes = new byte[signatureByteList.size()];
                    for (int b = 0; b < signatureBytes.length; b++) {
                        signatureBytes[b] = signatureByteList.get(b);
                    }
                    boolean valid = signature.verify(signatureBytes);

                    if (valid) {
                        MessageDialog.openInformation(shell, Messages.SignatureEngine_0, Messages.SignatureEngine_1);
                    } else {
                        MessageDialog.openWarning(shell, Messages.SignatureEngine_0, Messages.SignatureEngine_2);
                    }
                }
            } catch (SignatureException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "SignatureException while performing a signature", e, true); //$NON-NLS-1$
            } catch (IOException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "IOException while performing a signature", e, //$NON-NLS-1$
                        false);
            }
        }
    }
}
