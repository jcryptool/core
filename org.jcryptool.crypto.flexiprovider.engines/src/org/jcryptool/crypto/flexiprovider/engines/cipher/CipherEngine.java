//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.engines.cipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.UnrecoverableEntryException;

import org.eclipse.core.runtime.IStatus;
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

import de.flexiprovider.api.Cipher;
import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.BadPaddingException;
import de.flexiprovider.api.exceptions.IllegalBlockSizeException;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.common.util.ByteUtils;

public class CipherEngine extends FlexiProviderEngine {
    protected Cipher cipher;

    @Override
    public KeyObject init(IFlexiProviderOperation operation) {
        LogUtil.logInfo("initializing cipher engine"); //$NON-NLS-1$
        this.operation = operation;

        char[] password = null;
        Key key = null;

        // password may be contained in the ActionItem, otherwise prompt
        if (operation.getPassword() != null) {
            password = operation.getPassword();
        } else {
            password = promptPassword();
        }


        if (password != null) {
            try {
                key = (Key) KeyStoreManager.getInstance().getSecretKey(
                        operation.getKeyStoreAlias(), password);
                operation.setPassword(password); // save in the operation if no exception occurred
            } catch (UnrecoverableEntryException e) {
                JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, FlexiProviderEnginesPlugin.PLUGIN_ID,
                        Messages.ExAccessKeystorePassword, e));
                return null;
            } catch (Exception e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "Exception while accessing a secret key", e, true); //$NON-NLS-1$
                return null;
            }
        }

        if (key != null) {
            try {
                cipher = Registry.getCipher(operation.getAlgorithmDescriptor().getAlgorithmName());
                if (operation.getOperation().equals(OperationType.ENCRYPT)) {
                    cipher.initEncrypt(key, operation.getAlgorithmDescriptor()
                            .getAlgorithmParameterSpec(), FlexiProviderEnginesPlugin
                            .getSecureRandom());
                } else {
                    cipher.initDecrypt(key, operation.getAlgorithmDescriptor()
                            .getAlgorithmParameterSpec());
                }
                initialized = true;
            } catch (NoSuchAlgorithmException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "NoSuchAlgorithmException while initializing a cipher engine", e, true); //$NON-NLS-1$
                return null;
            } catch (InvalidKeyException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        Messages.CipherEngine_2, e, true);
                return null;
            } catch (InvalidAlgorithmParameterException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "InvalidAlgorithmParameterException while initializing a cipher engine", e, true); //$NON-NLS-1$
                return null;
            }
        }
        return new KeyObject(key, password);
    }

    public void perform(KeyObject usedKey) {
        if (initialized) {
            LogUtil.logInfo("perfoming cipher"); //$NON-NLS-1$
            InputStream inputStream = initInput(operation.getInput());
            OutputStream outputStream = initOutput(operation.getOutput());
            try {
            	performCipher(inputStream, outputStream);
                
                inputStream.close();
                outputStream.close();
                if (operation.getOutput().equals("<Editor>")) { //$NON-NLS-1$
                    EditorsManager.getInstance().openNewHexEditor(
                            AbstractEditorService.createOutputFile(new FileInputStream(new File(
                                    getOutputURI()))));
                }
            } catch (IOException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "IOException while performing a cipher", e, false); //$NON-NLS-1$
            } catch (IllegalBlockSizeException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "IllegalBlockSizeException while performing a cipher", e, true); //$NON-NLS-1$
            } catch (BadPaddingException e) {
//                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
//                        "BadPaddingException while performing a cipher", e, true); //$NON-NLS-1$
                JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, FlexiProviderEnginesPlugin.PLUGIN_ID,
                        Messages.ExBadPadding, e));
            } catch (PartInitException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "Failed to open the Hexeditor", e, true); //$NON-NLS-1$
            }
        }
    }

	protected void performCipher(InputStream inputStream,
			OutputStream outputStream) throws IOException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] outputBuffer;
		byte[] currentBlock = new byte[cipher.getBlockSize()];
		
		int length;
		while ((length = inputStream.read(currentBlock)) != -1) {
		    outputBuffer = cipher.update(currentBlock, 0, length);
		    outputStream.write(outputBuffer);
		}
		outputBuffer = cipher.doFinal();
		
		LogUtil.logInfo("dofinal: " + ByteUtils.toHexString(outputBuffer)); //$NON-NLS-1$
        outputStream.write(outputBuffer);
	}

}
