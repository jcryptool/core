// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.engines.cipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEngine;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEnginesPlugin;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

import de.flexiprovider.api.AsymmetricHybridCipher;
import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.BadPaddingException;
import de.flexiprovider.api.exceptions.IllegalBlockSizeException;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.common.util.ByteUtils;

public class AsymmetricHybridCipherEngine extends FlexiProviderEngine {
    private AsymmetricHybridCipher cipher;

    @Override
    public KeyObject init(IFlexiProviderOperation operation) {
        LogUtil.logInfo("initializing asymmetric hybrid cipher engine"); //$NON-NLS-1$
        this.operation = operation;
        char[] password = null;
        Key key = null;
        try {
            cipher = Registry.getAsymmetricHybridCipher(operation.getAlgorithmDescriptor().getAlgorithmName());
            if (operation.getOperation().equals(OperationType.ENCRYPT)) {

                // password may be contained in the ActionItem, otherwise prompt
                if (operation.getPassword() != null) {
                    password = operation.getPassword();
                } else {
                    password = promptPassword();
                }

                key = (Key) KeyStoreManager.getInstance().getPrivateKey(operation.getKeyStoreAlias(), password);
                cipher.initEncrypt(key, operation.getAlgorithmDescriptor().getAlgorithmParameterSpec(),
                        FlexiProviderEnginesPlugin.getSecureRandom());
            } else {
                Certificate certificate = KeyStoreManager.getInstance().getPublicKey(operation.getKeyStoreAlias());
                key = (Key) certificate.getPublicKey();
                cipher.initDecrypt(key, operation.getAlgorithmDescriptor().getAlgorithmParameterSpec());
            }
            operation.setPassword(password); // save in the operation if no exception occurred
            initialized = true;
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "NoSuchAlgorithmException while initializing a cipher engine", e, true); //$NON-NLS-1$
        } catch (InvalidKeyException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, Messages.AsymmetricHybridCipherEngine_1, e, true);
        } catch (InvalidAlgorithmParameterException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "InvalidAlgorithmParameterException while initializing a cipher engine", e, true); //$NON-NLS-1$
        } catch (UnrecoverableEntryException e) {
            JCTMessageDialog.showInfoDialog(new Status(Status.INFO, FlexiProviderEnginesPlugin.PLUGIN_ID,
                    Messages.ExAccessKeystorePassword, e));
        } catch (Exception e) {
            LogUtil.logError(e);
        }

        return new KeyObject(key, password);
    }

    public void perform(KeyObject usedKey) {
        if (initialized) {
            LogUtil.logInfo("perfoming cipher"); //$NON-NLS-1$
            InputStream inputStream = initInput(operation.getInput());
            OutputStream outputStream = initOutput(operation.getOutput());
            byte[] currentBlock = new byte[128];
            int i;
            byte[] outputBuffer;
            try {
                while ((i = inputStream.read(currentBlock)) != -1) {
                    outputBuffer = cipher.update(currentBlock, 0, i);
                    outputStream.write(outputBuffer);
                }
                outputBuffer = cipher.doFinal();
                LogUtil.logInfo("dofinal: " + ByteUtils.toHexString(outputBuffer)); //$NON-NLS-1$
                outputStream.write(outputBuffer);
                inputStream.close();
                outputStream.close();
                if (operation.getOutput().equals("<Editor>")) { //$NON-NLS-1$
                    EditorsManager.getInstance().openNewHexEditor(
                            AbstractEditorService.createOutputFile(new FileInputStream(new File(getOutputURI()))));
                }
            } catch (IOException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "IOException while performing a cipher", e, false); //$NON-NLS-1$
            } catch (IllegalBlockSizeException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "IllegalBlockSizeException while performing a cipher", e, true); //$NON-NLS-1$
            } catch (BadPaddingException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "BadPaddingException while performing a cipher", e, true); //$NON-NLS-1$
            } catch (PartInitException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "Failed to open the Hexeditor", e, true); //$NON-NLS-1$
            }
        }
    }

}
