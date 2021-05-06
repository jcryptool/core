// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.keystore.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionEvent;
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
import org.jcryptool.crypto.flexiprovider.keystore.FlexiProviderKeystorePlugin;
import org.jcryptool.crypto.flexiprovider.keystore.wizards.NewKeyPairWizard;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;
import org.jcryptool.crypto.keystore.descriptors.NewKeyPairDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewKeyWizard;
import org.jcryptool.crypto.keystore.ui.actions.AbstractNewKeyStoreEntryHandler;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.keys.KeyPair;
import de.flexiprovider.api.keys.KeyPairGenerator;
import de.flexiprovider.api.keys.PrivateKey;
import de.flexiprovider.api.keys.PublicKey;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

/**
 * @author t-kern
 * @author Holger Friedrich (support for Commands, additional class based on NewKeyPairAction)
 * 
 */
public class NewKeyPairHandler extends AbstractNewKeyStoreEntryHandler {
    private Shell shell;
    private WizardDialog dialog;

    /**
     * Creates a new instance of NewKeyPairHandler.
     */
    public NewKeyPairHandler() {
        // this.setText(Messages.NewKeyPairAction_0);
        // this.setToolTipText(Messages.NewKeyPairAction_1);
        // this.setImageDescriptor(KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_key2.png")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public Object execute(ExecutionEvent event) {
        LogUtil.logInfo("NewKeyPairAction"); //$NON-NLS-1$
        shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        Wizard wizard = new NewKeyPairWizard();
        dialog = new WizardDialog(shell, wizard);
        dialog.setMinimumPageSize(300, 350);

        int result = dialog.open();
        if (result == Window.OK) {
            if (wizard instanceof INewKeyWizard) {
                final INewEntryDescriptor nkd = ((INewKeyWizard) wizard).getNewEntryDescriptor();
                final Integer[] argument = new Integer[1];
                final Integer arg = nkd.getKeyLength();
                argument[0] = arg;
                final Integer keyLen = argument[0];
                LogUtil.logInfo("nkd.getKeyLength: " + argument[0]); //$NON-NLS-1$
                Job job = new Job(Messages.NewKeyPairHandler_2) {
                    @Override
                    protected IStatus run(IProgressMonitor monitor) {
                        monitor.beginTask(Messages.NewKeyPairHandler_3, IProgressMonitor.UNKNOWN);
                        try {
                            IMetaKeyGenerator gen = AlgorithmsXMLManager.getInstance().getKeyPairGenerator(
                                    nkd.getAlgorithmName());
                            IMetaLength validKeyLengths = gen.getLengths();

                            // Check if entered key length is valid
                            boolean isValidKeyLength = true;
                            if (validKeyLengths != null) {
                                isValidKeyLength = (validKeyLengths.getDefaultLength() == keyLen)
                                        || (keyLen >= validKeyLengths.getLowerBound() && keyLen <= validKeyLengths
                                                .getUpperBound())
                                        || (validKeyLengths.getLengths() != null && validKeyLengths.getLengths()
                                                .contains(keyLen));
                            }
                            if (!isValidKeyLength) {
                                throw new InvalidAlgorithmParameterException("illegal key length"); //$NON-NLS-1$
                            }

                            AlgorithmParameterSpec spec = null;
                            if (arg != -1) {
                                if (gen.getParameterSpecClassName() != null) {
                                    spec = Reflector.getInstance().instantiateParameterSpec(
                                            gen.getParameterSpecClassName(), argument);
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
                            performNewKeyAction(new NewKeyPairDescriptor(nkd, priv, pub));
                        } catch (NoSuchAlgorithmException e) {
                            LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "NoSuchAlgorithmException while generating a key pair", e, true); //$NON-NLS-1$
                        } catch (InvalidAlgorithmParameterException e) {
                            LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "InvalidAlgorithmParameterException while generating a key pair", e, true); //$NON-NLS-1$
                        } catch (SecurityException e) {
                            LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "SecurityException while generating a key pair", e, true); //$NON-NLS-1$
                        } catch (IllegalArgumentException e) {
                            LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "IllegalArgumentException while generating a key pair", e, true); //$NON-NLS-1$
                        } catch (ClassNotFoundException e) {
                            LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "ClassNotFoundException while generating a key pair", e, true); //$NON-NLS-1$
                        } catch (NoSuchMethodException e) {
                            LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "NoSuchMethodException while generating a key pair", e, true); //$NON-NLS-1$
                        } catch (InstantiationException e) {
                            LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "InstantiationException while generating a key pair", e, true); //$NON-NLS-1$
                        } catch (IllegalAccessException e) {
                            LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "IllegalAccessException while generating a key pair", e, true); //$NON-NLS-1$
                        } catch (InvocationTargetException e) {
                            LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID,
                                    "InvocationTargetException while generating a key pair", e, true); //$NON-NLS-1$
                        } finally {
                            monitor.done();
                        }
                        return Status.OK_STATUS;
                    }
                };
                job.setPriority(Job.LONG);
                // job.setUser(true);
                job.schedule();
            }
        }
        return(null);
    }

}
