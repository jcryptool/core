// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.keystore;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaLength;
import org.jcryptool.crypto.flexiprovider.keystore.wizards.NewKeyPairWizard;
import org.jcryptool.crypto.flexiprovider.keystore.wizards.NewSymmetricKeyWizard;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.descriptors.NewKeyPairDescriptor;
import org.jcryptool.crypto.keystore.descriptors.NewSecretKeyDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewKeyWizard;
import org.jcryptool.crypto.keystore.ui.actions.AbstractKeyStoreHandler;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.KeyPair;
import de.flexiprovider.api.keys.KeyPairGenerator;
import de.flexiprovider.api.keys.PrivateKey;
import de.flexiprovider.api.keys.PublicKey;
import de.flexiprovider.api.keys.SecretKey;
import de.flexiprovider.api.keys.SecretKeyGenerator;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

public class KeyStoreHelper {

	public static final String KEYSTOREHELPER_FAMILY = "keystorehelper-family";

	public static class KeyStoreAliasNotifier extends Observable {
		private KeyStoreAlias toNotifyAlias;
		private boolean makeNotificationAtFirstObserver = false;

		@Override
		public synchronized void addObserver(Observer o) {
			super.addObserver(o);
			if(makeNotificationAtFirstObserver) {
				notifyAboutAlias(toNotifyAlias);
			}
		}

		public void notifyAboutAlias(KeyStoreAlias alias) {
			toNotifyAlias = alias;
			if(this.countObservers() > 0) {
				makeNotificationAtFirstObserver = false;
				this.setChanged();
				this.notifyObservers(alias);
			} else {
				makeNotificationAtFirstObserver = true;
			}
		}
	}

	public static KeyStoreAliasNotifier makeKeyPairByWizard(String keyType) {
		LogUtil.logInfo("NewKeyPairAction"); //$NON-NLS-1$
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Wizard wizard = new NewKeyPairWizard(keyType);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.setHelpAvailable(false);
		dialog.setMinimumPageSize(300, 350);

		final KeyStoreAliasNotifier resultAlias = new KeyStoreAliasNotifier();

		int result = dialog.open();
		if (result == Window.OK) {
			if (wizard instanceof INewKeyWizard) {
				final INewEntryDescriptor nkd = ((INewKeyWizard)wizard).getNewEntryDescriptor();
				final Integer[] argument = new Integer[1];
				final Integer arg = nkd.getKeyLength();
				argument[0] = arg;
				final Integer keyLen = argument[0];
				LogUtil.logInfo("nkd.getKeyLength: " + argument[0]);				 //$NON-NLS-1$
				Job job = new Job("New Key Pair Job") { //$NON-NLS-1$
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("New KeyPair Task", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
						try {
							IMetaKeyGenerator gen = AlgorithmsXMLManager.getInstance().getKeyPairGenerator(nkd.getAlgorithmName());
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
							if (arg != -1) {
								if (gen.getParameterSpecClassName() != null) {
									spec = Reflector.getInstance().instantiateParameterSpec(gen.getParameterSpecClassName(), argument);
								}
							}
							KeyPairGenerator generator = Registry.getKeyPairGenerator(nkd.getAlgorithmName());
							if (spec != null) {
								generator.initialize(spec, FlexiProviderKeystorePlugin.getSecureRandom());
							} else if (arg != -1) {
								generator.initialize(arg, FlexiProviderKeystorePlugin.getSecureRandom());
							}
							KeyPair keyPair = generator.genKeyPair();
							PrivateKey priv = keyPair.getPrivate();
							PublicKey pub = keyPair.getPublic();
							NewKeyPairDescriptor descriptor = new NewKeyPairDescriptor(nkd, priv, pub);
							resultAlias.notifyAboutAlias(AbstractKeyStoreHandler.addKeyPairStatic(descriptor, ((NewKeyPairDescriptor)descriptor).getPrivateKey(), ((NewKeyPairDescriptor)descriptor).getPublicKey()));
						} catch (NoSuchAlgorithmException e) {
							LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "NoSuchAlgorithmException while generating a key pair", e, true);
						} catch (InvalidAlgorithmParameterException e) {
							LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "InvalidAlgorithmParameterException while generating a key pair", e, true);
						} catch (SecurityException e) {
							LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "SecurityException while generating a key pair", e, true);
						} catch (IllegalArgumentException e) {
							LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "IllegalArgumentException while generating a key pair", e, true);
						} catch (ClassNotFoundException e) {
							LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "ClassNotFoundException while generating a key pair", e, true);
						} catch (NoSuchMethodException e) {
							LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "NoSuchMethodException while generating a key pair", e, true);
						} catch (InstantiationException e) {
							LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "InstantiationException while generating a key pair", e, true);
						} catch (IllegalAccessException e) {
							LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "IllegalAccessException while generating a key pair", e, true);
						} catch (InvocationTargetException e) {
							LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "InvocationTargetException while generating a key pair", e, true);
						} finally {
							monitor.done();
						}
						return Status.OK_STATUS;
					}

					@Override
					public boolean belongsTo(Object family) {
						return family == KEYSTOREHELPER_FAMILY;
					}
				};
				job.setPriority(Job.LONG);
				job.setUser(true);
				job.schedule();
			}
		} else {
			resultAlias.notifyAboutAlias(null);
		}

		return resultAlias;
	}

	public static KeyStoreAliasNotifier makeSymmetricKeyByWizard(String keyType) {
		LogUtil.logInfo("NewSymmetricKeyAction"); //$NON-NLS-1$
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Wizard wizard = new NewSymmetricKeyWizard(keyType);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.setHelpAvailable(false);
		dialog.setMinimumPageSize(300, 350);

		final KeyStoreAliasNotifier resultAlias = new KeyStoreAliasNotifier();

		int result = dialog.open();
		if (result == Window.OK) {
			if (wizard instanceof INewKeyWizard) {
				final INewEntryDescriptor nkd = ((INewKeyWizard)wizard).getNewEntryDescriptor();
				final Integer[] argument = new Integer[1];
				argument[0] = nkd.getKeyLength();
				final Integer keyLen = argument[0];
				LogUtil.logInfo("key strength: " + argument[0]); //$NON-NLS-1$
				Job job = new Job("New SecretKey Job") { //$NON-NLS-1$
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("New SecretKey Task", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
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

							INewEntryDescriptor descriptor = new NewSecretKeyDescriptor(nkd, key);
							resultAlias.notifyAboutAlias(AbstractKeyStoreHandler.addSecretKeyStatic(descriptor , ((NewSecretKeyDescriptor)descriptor).getSecretKey()));
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
						return Status.OK_STATUS;
					}

					@Override
					public boolean belongsTo(Object family) {
						return family == KEYSTOREHELPER_FAMILY;
					}
				};
				job.setPriority(Job.LONG);
				job.setUser(true);
				job.schedule();
			}
		} else {
			resultAlias.notifyAboutAlias(null);
		}

		return resultAlias;
	}

	public static void makeKeyPairByWizard() {
		makeKeyPairByWizard(KeyStoreAlias.EVERYTHING_MATCHER);
	}

	public static void makeSymmetricKeyByWizard() {
		makeSymmetricKeyByWizard(KeyStoreAlias.EVERYTHING_MATCHER);
	}
}
