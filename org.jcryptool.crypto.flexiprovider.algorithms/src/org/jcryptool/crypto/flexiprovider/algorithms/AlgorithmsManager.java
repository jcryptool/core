// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.algorithms;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.listeners.NewOperationManager;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.AlgorithmWizard;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.blockcipher.BlockCipherWizard;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.blockcipher.BlockCipherWizardDialog;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.securerandom.SecureRandomWizard;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.signature.SignatureWizard;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;

import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

/**
 * @author tkern
 * 
 */
public class AlgorithmsManager {
    private static AlgorithmsManager instance;

    private static Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

    private static AlgorithmWizard algorithmWizard;
    private static BlockCipherWizard blockCipherWizard;
    private static SignatureWizard signatureWizard;

    private static WizardDialog dialog;

    private AlgorithmsManager() {
    }

    public synchronized static AlgorithmsManager getInstance() {
        if (instance == null)
            instance = new AlgorithmsManager();
        return instance;
    }

    @SuppressWarnings("incomplete-switch")
    public void algorithmCalled(IMetaAlgorithm metaAlgorithm) {
        LogUtil.logInfo("Algorithm called! (Type: " + metaAlgorithm.getType() + ", Algorithm: " + metaAlgorithm.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        switch (metaAlgorithm.getType()) {
            case ASYMMETRIC_BLOCK_CIPHER:
                performAsymmetricBlockCipherCalled(metaAlgorithm);
                break;
            case ASYMMETRIC_HYBRID_CIPHER:
                performAsymmetricHybridCipherCalled(metaAlgorithm);
                break;
            case BLOCK_CIPHER:
                performBlockCipherCalled(metaAlgorithm);
                break;
            case CIPHER:
                performCipherCalled(metaAlgorithm);
                break;
            case MAC:
                performMacCalled(metaAlgorithm);
                break;
            case MESSAGE_DIGEST:
                performMessageDigestCalled(metaAlgorithm);
                break;
            case SECURE_RANDOM:
                performSecureRandomCalled(metaAlgorithm);
                break;
            case SIGNATURE:
                performSignatureCalled(metaAlgorithm);
                break;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Message Authentication Code erzeugt werden soll.
     * @param algorithm
     */
    private static void performMacCalled(IMetaAlgorithm algorithm) {
        if (algorithm.getBlockCipherName() != null) {
            IMetaAlgorithm bc = null;
            LogUtil.logInfo("BC name: " + algorithm.getBlockCipherName()); //$NON-NLS-1$
            if (algorithm.getBlockCipherOID() != null) {
                LogUtil.logInfo("BC oid: " + algorithm.getBlockCipherOID()); //$NON-NLS-1$
                bc = AlgorithmsXMLManager.getInstance().getBlockCipher(algorithm.getBlockCipherOID());
            } else {
            	LogUtil.logInfo("BC name: " + algorithm.getBlockCipherName()); //$NON-NLS-1$
            	AlgorithmsXMLManager manager = AlgorithmsXMLManager.getInstance();
                bc = manager.getBlockCipher(algorithm.getBlockCipherName());
            }
            
            LogUtil.logInfo("BC mode: " + algorithm.getBlockCipherMode()); //$NON-NLS-1$
            if (bc != null) {
                blockCipherWizard = new BlockCipherWizard(bc, algorithm.getBlockCipherMode());
                dialog = new BlockCipherWizardDialog(shell, blockCipherWizard);
                dialog.setMinimumPageSize(300, 175);
                int result = dialog.open();
                if (result == Window.OK) {
                    AlgorithmDescriptor blockCipherDescriptor = blockCipherWizard.getDescriptor();
                    AlgorithmDescriptor macDescriptor =
                            new AlgorithmDescriptor(algorithm.getName(), RegistryType.MAC,
                                    blockCipherDescriptor.getAlgorithmParameterSpec());
                    NewOperationManager.getInstance().fireNewOperation(macDescriptor);
                    return;
                }
            } else {
            	LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "Algorithm not found! " + algorithm.getBlockCipherName());
            }
        }

        if (algorithm.getParameterSpecClassName() != null && !algorithm.isParameterSpecDisabled()) {
            algorithmWizard = new AlgorithmWizard(algorithm);
            dialog = new WizardDialog(shell, algorithmWizard);
            dialog.setMinimumPageSize(300, 100);
            int result = dialog.open();
            if (result == Window.OK) {
                LogUtil.logInfo("adding mac"); //$NON-NLS-1$
                NewOperationManager.getInstance().fireNewOperation(algorithmWizard.getDescriptor());
            } else {
                LogUtil.logInfo("adding mac w/o parameter spec"); //$NON-NLS-1$
                LogUtil.logInfo("has param generator: " + (algorithm.getParameterGeneratorClassName() != null)); //$NON-NLS-1$
                if (algorithm.getParameterGeneratorClassName() != null) {
                    AlgorithmParameterSpec generatedSpec =
                            Reflector.getInstance().generateDefaultParameterSpec(algorithm);
                    NewOperationManager.getInstance().fireNewOperation(
                            new AlgorithmDescriptor(algorithm.getName(), RegistryType.MAC, generatedSpec));
                } else {
                    NewOperationManager.getInstance().fireNewOperation(
                            new AlgorithmDescriptor(algorithm.getName(), RegistryType.MAC, null));
                }
            }
        } else {
        	LogUtil.logInfo("adding cipher w/o parameter spec"); //$NON-NLS-1$
        	 NewOperationManager.getInstance().fireNewOperation(
        			 new AlgorithmDescriptor(algorithm.getName(), RegistryType.MAC, null));
        }
    }

    private static void performCipherCalled(IMetaAlgorithm algorithm) {
        if (algorithm.getParameterSpecClassName() != null) {
            algorithmWizard = new AlgorithmWizard(algorithm);
            dialog = new WizardDialog(shell, algorithmWizard);
            dialog.setMinimumPageSize(300, 100);
            int result = dialog.open();
            if (result == Window.OK) {
                LogUtil.logInfo("adding cipher"); //$NON-NLS-1$
                NewOperationManager.getInstance().fireNewOperation(algorithmWizard.getDescriptor());
            }
        } else {
            LogUtil.logInfo("adding cipher w/o parameter spec"); //$NON-NLS-1$
            NewOperationManager.getInstance().fireNewOperation(
                    new AlgorithmDescriptor(algorithm.getName(), RegistryType.ASYMMETRIC_BLOCK_CIPHER, null));
        }
    }

    private static void performAsymmetricHybridCipherCalled(IMetaAlgorithm algorithm) {
        if (algorithm.getParameterSpecClassName() != null && !algorithm.isParameterSpecDisabled()) {
            algorithmWizard = new AlgorithmWizard(algorithm);
            dialog = new WizardDialog(shell, algorithmWizard);
            dialog.setMinimumPageSize(300, 100);
            int result = dialog.open();
            if (result == Window.OK) {
                LogUtil.logInfo("adding asymmetric hybrid cipher"); //$NON-NLS-1$
                NewOperationManager.getInstance().fireNewOperation(algorithmWizard.getDescriptor());
            }
        } else {
            LogUtil.logInfo("adding asymmetric hybrid cipher w/o parameter spec"); //$NON-NLS-1$
            NewOperationManager.getInstance().fireNewOperation(
                    new AlgorithmDescriptor(algorithm.getName(), RegistryType.ASYMMETRIC_HYBRID_CIPHER, null));
        }
    }

    private static void performAsymmetricBlockCipherCalled(IMetaAlgorithm algorithm) {
        if (algorithm.getParameterSpecClassName() != null && !algorithm.isParameterSpecDisabled()) {
            algorithmWizard = new AlgorithmWizard(algorithm);
            dialog = new WizardDialog(shell, algorithmWizard);
            dialog.setMinimumPageSize(300, 100);
            int result = dialog.open();
            if (result == Window.OK) {
                LogUtil.logInfo("adding asymmetric block cipher"); //$NON-NLS-1$
                NewOperationManager.getInstance().fireNewOperation(algorithmWizard.getDescriptor());

            }
        } else {
            LogUtil.logInfo("adding asymmetric block cipher w/o parameter spec"); //$NON-NLS-1$
            NewOperationManager.getInstance().fireNewOperation(
                    new AlgorithmDescriptor(algorithm.getName(), RegistryType.ASYMMETRIC_BLOCK_CIPHER, null));
        }
    }

    private static void performBlockCipherCalled(IMetaAlgorithm algorithm) {
        blockCipherWizard = new BlockCipherWizard(algorithm);
        dialog = new BlockCipherWizardDialog(shell, blockCipherWizard);
        dialog.setMinimumPageSize(300, 175);
        int result = dialog.open();
        if (result == Window.OK) {
            NewOperationManager.getInstance().fireNewOperation(blockCipherWizard.getDescriptor());
        }
    }

    private static void performMessageDigestCalled(IMetaAlgorithm algorithm) {
        LogUtil.logInfo("has param generator: " + (algorithm.getParameterGeneratorClassName() != null)); //$NON-NLS-1$
        if (algorithm.getParameterGeneratorClassName() != null && !algorithm.isParameterSpecDisabled()) {
            AlgorithmParameterSpec generatedSpec = Reflector.getInstance().generateDefaultParameterSpec(algorithm);
            NewOperationManager.getInstance().fireNewOperation(
                    new AlgorithmDescriptor(algorithm.getName(), RegistryType.MESSAGE_DIGEST, generatedSpec));
        } else {
            NewOperationManager.getInstance().fireNewOperation(
                    new AlgorithmDescriptor(algorithm.getName(), RegistryType.MESSAGE_DIGEST, null));
        }

    }

    private static void performSecureRandomCalled(IMetaAlgorithm algorithm) {
        algorithmWizard = new SecureRandomWizard(algorithm);
        dialog = new WizardDialog(shell, algorithmWizard);
        dialog.setMinimumPageSize(300, 175);

        int result = dialog.open();
        if (result == Window.OK) {
            NewOperationManager.getInstance().fireNewOperation(algorithmWizard.getDescriptor());
        }
    }

    private static void performSignatureCalled(IMetaAlgorithm algorithm) {
        if (algorithm.getParameterSpecClassName() != null /*
                                                           * && !algorithm. isParameterSpecDisabled ()
                                                           */) {
            LogUtil.logInfo("has paramspec: " + algorithm.getParameterSpecClassName()); //$NON-NLS-1$
            // open wizard
            signatureWizard = new SignatureWizard(algorithm);
            dialog = new WizardDialog(shell, signatureWizard);
            dialog.setMinimumPageSize(300, 175);
            int result = dialog.open();
            if (result == Window.OK) {
                NewOperationManager.getInstance().fireNewOperation(signatureWizard.getDescriptor());
            }
        } else {
            LogUtil.logInfo("has no paramspec"); //$NON-NLS-1$
            NewOperationManager.getInstance().fireNewOperation(
                    new AlgorithmDescriptor(algorithm.getName(), RegistryType.SIGNATURE, null));
        }
    }

}
