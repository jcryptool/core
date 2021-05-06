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
package org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.blockcipher;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.FlexiProviderAlgorithmsPlugin;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;

import de.flexiprovider.api.BlockCipher;
import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.exceptions.NoSuchPaddingException;
import de.flexiprovider.api.keys.SecretKey;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

public class BlockCipherWizardDialog extends WizardDialog {
	private BlockCipherWizard wizard;
	private IMetaAlgorithm algorithm;
	private SecretKey dummyKey;
	private int modeBlockSize = -1;

	public BlockCipherWizardDialog(Shell parentShell, BlockCipherWizard wizard) {
		super(parentShell, wizard);
		this.wizard = wizard;
		algorithm = wizard.getAlgorithm();
	}

	@Override
	public void nextPressed() {
		LogUtil.logInfo("next pressed"); //$NON-NLS-1$
		if (wizard.hasAlgorithmParameterSpecPage() && getCurrentPage().getName().equals("AlgorithmParameterWizardPage")) { //$NON-NLS-1$
			Object[] values = wizard.getAlgorithmParameterValues();
			for (Object value : values) {
				LogUtil.logInfo("Value: " + value +" of type: " + value.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			try {
				dummyKey = wizard.getDummyKey();
				BlockCipher cipher = Registry.getBlockCipher(algorithm.getName());
				AlgorithmParameterSpec spec = Reflector.getInstance().instantiateParameterSpec(algorithm.getParameterSpecClassName(), values);
				cipher.initEncrypt(dummyKey, null, spec, FlexiProviderAlgorithmsPlugin.getSecureRandom());
				modeBlockSize = cipher.getBlockSize();
				wizard.setModeBlockSize(modeBlockSize);
			} catch (NoSuchAlgorithmException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "NoSuchAlgorithmException while initializing a block cipher", e, true); //$NON-NLS-1$
			} catch (NoSuchPaddingException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "NoSuchPaddingException while initializing a block cipher", e, true); //$NON-NLS-1$
			} catch (InvalidKeyException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, Messages.BlockCipherWizardDialog_2, e, true);
			} catch (InvalidAlgorithmParameterException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "InvalidAlgorithmParameterException while initializing a block cipher", e, true); //$NON-NLS-1$
			} catch (SecurityException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "SecurityException while initializing a block cipher", e, true); //$NON-NLS-1$
			} catch (IllegalArgumentException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "IllegalArgumentException while initializing a block cipher", e, true); //$NON-NLS-1$
			} catch (ClassNotFoundException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "ClassNotFoundException while initializing a block cipher", e, true); //$NON-NLS-1$
			} catch (NoSuchMethodException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "NoSuchMethodException while initializing a block cipher", e, true); //$NON-NLS-1$
			} catch (InstantiationException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "InstantiationException while initializing a block cipher", e, true); //$NON-NLS-1$
			} catch (IllegalAccessException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "IllegalAccessException while initializing a block cipher", e, true); //$NON-NLS-1$
			} catch (InvocationTargetException e) {
				LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "InvocationTargetException while initializing a block cipher", e, true); //$NON-NLS-1$
			}
		}
		super.nextPressed();
	}

}
