//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.keystore.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaLength;
import org.jcryptool.crypto.flexiprovider.keystore.FlexiProviderKeystorePlugin;
import org.jcryptool.crypto.flexiprovider.keystore.wizards.NewSymmetricKeyWizard;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;
import org.jcryptool.crypto.keystore.descriptors.NewSecretKeyDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewKeyWizard;
import org.jcryptool.crypto.keystore.ui.actions.AbstractNewKeyStoreEntryHandler;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.SecretKey;
import de.flexiprovider.api.keys.SecretKeyGenerator;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

/**
 * @author tkern
 * @author Holger Friedrich (support for Commands, additional class based on NewSymmetricKeyAction)
 *
 */
public class NewSymmetricKeyHandler extends AbstractNewKeyStoreEntryHandler {
	/** The shell is required for the wizard */
	private Shell shell;

	/** The wizard dialog containing the wizards */
	private WizardDialog dialog;

	/**
	 * Creates a new instance of NewSymmetricKeyHandler.
	 */
	public NewSymmetricKeyHandler() {
		// this.setText(Messages.NewSymmetricKeyAction_0);
		// this.setToolTipText(Messages.NewSymmetricKeyAction_1);
		// this.setImageDescriptor(KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_key1.png")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public Object execute(ExecutionEvent event) {
		LogUtil.logInfo("NewSymmetricKeyAction"); //$NON-NLS-1$
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Wizard wizard = new NewSymmetricKeyWizard();
		dialog = new WizardDialog(shell, wizard);
		dialog.setMinimumPageSize(300, 350);

		int result = dialog.open();
		if (result == Window.OK) {
			if (wizard instanceof INewKeyWizard) {
				INewEntryDescriptor nkd = ((INewKeyWizard)wizard).getNewEntryDescriptor();
				Integer[] argument = new Integer[1];
				argument[0] = nkd.getKeyLength();
				Integer keyLen = argument[0];
				LogUtil.logInfo("key strength: " + argument[0]); //$NON-NLS-1$
				try {
					IMetaKeyGenerator gen = AlgorithmsXMLManager.getInstance().getSecretKeyGenerator(nkd.getAlgorithmName());
					IMetaLength validKeyLengths = gen.getLengths();

					//Check if entered key length is valid
					boolean isValidKeyLength = true;
					if(validKeyLengths != null) {
						isValidKeyLength = (validKeyLengths.getDefaultLength() == keyLen)
							|| (keyLen >= validKeyLengths.getLowerBound() && keyLen <= validKeyLengths.getUpperBound())
							|| (validKeyLengths.getLengths() != null && validKeyLengths.getLengths().contains(keyLen));
					}
					if(!isValidKeyLength) {
						throw new InvalidAlgorithmParameterException("illegal key length");
					}

					AlgorithmParameterSpec spec = null;
					if (gen.getParameterSpecClassName() != null) {
						spec = Reflector.getInstance().instantiateParameterSpec(gen.getParameterSpecClassName(), argument);
					}

					SecretKeyGenerator generator = Registry.getSecretKeyGenerator(nkd.getAlgorithmName());
					if (spec != null) {
						LogUtil.logInfo("initializing generator with spec"); //$NON-NLS-1$
						generator.init(spec, FlexiProviderKeystorePlugin.getSecureRandom());
					} else {
						generator.init(FlexiProviderKeystorePlugin.getSecureRandom());
					}
					SecretKey key = generator.generateKey();

					performNewKeyAction(new NewSecretKeyDescriptor(nkd, key));
				} catch (SecurityException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "SecurityException while generating a secret key", e, true);
				} catch (IllegalArgumentException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "IllegalArgumentException while generating a secret key", e, true);
				} catch (ClassNotFoundException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "ClassNotFoundException while generating a secret key", e, true);
				} catch (NoSuchMethodException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "NoSuchMethodException while generating a secret key", e, true);
				} catch (InstantiationException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "InstantiationException while generating a secret key", e, true);
				} catch (IllegalAccessException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "IllegalAccessException while generating a secret key", e, true);
				} catch (InvocationTargetException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "InvocationTargetException while generating a secret key", e, true);
				} catch (NoSuchAlgorithmException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while generating a secret key", e, true);
				} catch (InvalidAlgorithmParameterException e) {
					LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                            "InvalidAlgorithmParameterException while generating a secret key", e, true);
				}
			}
		}
		return(null);
	}

}
