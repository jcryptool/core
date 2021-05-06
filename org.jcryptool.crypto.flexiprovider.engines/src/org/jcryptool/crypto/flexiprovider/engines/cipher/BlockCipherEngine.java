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

import java.security.UnrecoverableEntryException;

import javax.crypto.SecretKeyFactory;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.BlockCipherDescriptor;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEnginesPlugin;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

import de.flexiprovider.api.BlockCipher;
import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.exceptions.NoSuchPaddingException;
import de.flexiprovider.api.keys.Key;
import de.flexiprovider.api.keys.SecretKeySpec;

public class BlockCipherEngine extends CipherEngine {

	@Override
	public KeyObject init(IFlexiProviderOperation operation) {
		LogUtil.logInfo("initializing block cipher engine"); //$NON-NLS-1$
		this.operation = operation;

		char[] password = new char[4];
		Key key = null;

		if (operation.useCustomKey()) {
			try {
				SecretKeyFactory secretKeyFactory = SecretKeyFactory
						.getInstance(operation.getAlgorithmDescriptor()
								.getAlgorithmName(), "FlexiCore"); //$NON-NLS-1$

				SecretKeySpec keySpec = new SecretKeySpec(
						operation.getKeyBytes(), operation
								.getAlgorithmDescriptor().getAlgorithmName());
				key = (Key) secretKeyFactory.generateSecret(keySpec);
			} catch (Exception e) {
				LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID,
						"Exception while creating key", e, true); //$NON-NLS-1$
				return null;
			}
		} else {
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
		}

		if (key != null) {
			try {
				String fullCipherName = operation.getAlgorithmDescriptor().getAlgorithmName() // Name of algorithm
						+ "/" //$NON-NLS-1$
						+ ((BlockCipherDescriptor) operation.getAlgorithmDescriptor()).getMode() // Name of mode
						+ "/" //$NON-NLS-1$
						+ ((BlockCipherDescriptor) operation.getAlgorithmDescriptor()).getPadding(); // Name of padding

				cipher = Registry.getBlockCipher(fullCipherName);
				if (operation.getOperation().equals(OperationType.ENCRYPT)) {
					((BlockCipher)cipher).initEncrypt(key, ((BlockCipherDescriptor) operation
							.getAlgorithmDescriptor()).getModeParameters(),
							operation.getAlgorithmDescriptor()
									.getAlgorithmParameterSpec(),
							FlexiProviderEnginesPlugin.getSecureRandom());
				} else {
					((BlockCipher)cipher).initDecrypt(key, ((BlockCipherDescriptor) operation
							.getAlgorithmDescriptor()).getModeParameters(),
							operation.getAlgorithmDescriptor()
									.getAlgorithmParameterSpec());
				}
				initialized = true;
			} catch (NoSuchAlgorithmException e) {
				LogUtil.logError(
						FlexiProviderEnginesPlugin.PLUGIN_ID,
						"NoSuchAlgorithmException while initializing a block cipher engine", e, true); //$NON-NLS-1$
				return null;
			} catch (NoSuchPaddingException e) {
				LogUtil.logError(
						FlexiProviderEnginesPlugin.PLUGIN_ID,
						"NoSuchPaddingException while initializing a block cipher engine", e, true); //$NON-NLS-1$
				return null;
			} catch (InvalidKeyException e) {
				LogUtil.logError(
						FlexiProviderEnginesPlugin.PLUGIN_ID,
						Messages.BlockCipherEngine_5, e, true);
				return null;
			} catch (InvalidAlgorithmParameterException e) {
				LogUtil.logError(
						FlexiProviderEnginesPlugin.PLUGIN_ID,
						"InvalidAlgorithmParameterException while initializing a block cipher engine", e, true); //$NON-NLS-1$
				return null;
			}
		}
		return new KeyObject(key, password);
	}
}
