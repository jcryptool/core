// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.engines.cipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import de.flexiprovider.api.exceptions.BadPaddingException;
import de.flexiprovider.api.exceptions.IllegalBlockSizeException;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.Key;

public class AsymmetricBlockCipherEngine extends CipherEngine {

    @Override
    public KeyObject init(IFlexiProviderOperation operation) {
        LogUtil.logInfo("initializing asymmetric block cipher engine"); //$NON-NLS-1$
        this.operation = operation;
        char[] password = null;
        Key key = null;
        try {
            cipher = Registry.getAsymmetricBlockCipher(operation.getAlgorithmDescriptor().getAlgorithmName());
            if (operation.getOperation().equals(OperationType.ENCRYPT)) {
                Certificate certificate = KeyStoreManager.getInstance().getCertificate(operation.getKeyStoreAlias());
                key = (Key) certificate.getPublicKey();
                cipher.initEncrypt(key, operation.getAlgorithmDescriptor().getAlgorithmParameterSpec(),
                        FlexiProviderEnginesPlugin.getSecureRandom());
            } else {
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
            }
            operation.setPassword(password); // save in the operation if no exception occurred
            initialized = true;
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "NoSuchAlgorithmException while initializing an asymmetric block cipher engine", //$NON-NLS-1$
                    e, true);
            return null;
        } catch (InvalidKeyException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, Messages.AsymmetricBlockCipherEngine_1, e, true);
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                    "InvalidAlgorithmParameterException while initializing an asymmetric block cipher engine", //$NON-NLS-1$
                    e, true);
            return null;
        } catch (UnrecoverableEntryException e) {
            JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, FlexiProviderEnginesPlugin.PLUGIN_ID,
                    Messages.ExAccessKeystorePassword, e));
            return null;
        } catch (Exception ex) {
            LogUtil.logError(ex);
            return null;
        }
        return new KeyObject(key, password);
    }
    
    @Override
    protected void performCipher(InputStream inputStream,
    		OutputStream outputStream) throws IOException,
    		IllegalBlockSizeException, BadPaddingException {
    	
    		byte[] outputBuffer;
    		byte[] currentBlock = new byte[cipher.getBlockSize()];
    		
    		int length;
    		while ((length = inputStream.read(currentBlock)) != -1) {
    		    outputBuffer = cipher.update(currentBlock, 0, length);
    		    outputBuffer = cipher.doFinal();
    		    outputStream.write(outputBuffer);
    		}
    }

}
