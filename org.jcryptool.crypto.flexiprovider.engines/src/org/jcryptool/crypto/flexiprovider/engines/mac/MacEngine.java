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
package org.jcryptool.crypto.flexiprovider.engines.mac;

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
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

import de.flexiprovider.api.Mac;
import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.api.keys.SecretKey;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;
import de.flexiprovider.core.mac.HMacKey;

public class MacEngine extends FlexiProviderEngine {
    private Mac mac;

    @Override
    public KeyObject init(IFlexiProviderOperation operation) {
        LogUtil.logInfo("initializing mac engine"); //$NON-NLS-1$
        this.operation = operation;

        char[] password = null;
        Key key = null;

        // password may be contained in the ActionItem, otherwise prompt
        if (operation.getPassword() != null) {
            password = operation.getPassword();
        } else if (!operation.useCustomKey()) { // farndt - prompt only if custom key is not to be used
            password = promptPassword();
        }
        
        if (password != null && !operation.useCustomKey()) { 
            try {
                key = (Key) KeyStoreManager.getInstance().getSecretKey(operation.getKeyStoreAlias(),
                        password);
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
        } else { // farndt - use custom key
        	key = new CustomKey(operation.getKeyBytes());
        }

        if (key != null) {
            try {
                mac = Registry.getMAC(operation.getAlgorithmDescriptor().getAlgorithmName());
                AlgorithmParameterSpec spec = operation.getAlgorithmDescriptor()
                        .getAlgorithmParameterSpec();
                if (spec != null) {
                    mac.init((SecretKey) key, spec);
                } else {
                    mac.init((SecretKey) key);
                }
                initialized = true;
            } catch (NoSuchAlgorithmException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "NoSuchAlgorithmException while initializing a mac", e, true); //$NON-NLS-1$
                return null;
            } catch (InvalidKeyException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        Messages.MacEngine_2, e, true);
                return null;
            } catch (InvalidAlgorithmParameterException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "InvalidAlgorithmParameterException while initializing a mac", e, true); //$NON-NLS-1$
                return null;
            }
        }
        return new KeyObject(key, password);
    }

    @Override
    public void perform(KeyObject usedKey) {
        if (initialized) {
            InputStream inputStream = initInput(operation.getInput());
            OutputStream outputStream = initOutput(operation.getOutput());
            try {
                int i;
                while ((i = inputStream.read()) != -1) {
                    mac.update((byte) i);
                }
                outputStream.write(mac.doFinal());
                inputStream.close();
                outputStream.close();
                if (operation.getOutput().equals("<Editor>")) { //$NON-NLS-1$
                    EditorsManager.getInstance().openNewHexEditor(
                            AbstractEditorService.createOutputFile(new FileInputStream(new File(
                                    getOutputURI()))));
                }
            } catch (IOException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
                        "IOException while performing a mac", e, false); //$NON-NLS-1$
            } catch (PartInitException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "Failed to open the Hexeditor", e, true); //$NON-NLS-1$
            }
        }
    }

    /**
     * Introduced to somehow turn the custom key bytes into a proper <code>Key</code>
     * @author farndt
     */
    private class CustomKey extends HMacKey {
        private static final long serialVersionUID = -5931470639830190744L;

        protected CustomKey(byte[] keyBytes) {
			super(keyBytes);
		}    	
    }
    
}
