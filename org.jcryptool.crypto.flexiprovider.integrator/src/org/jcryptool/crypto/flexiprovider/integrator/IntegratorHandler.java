// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.integrator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmHandler;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.BlockCipherDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.SecureRandomDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.operations.engines.PerformOperationManager;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;

import de.flexiprovider.api.MessageDigest;
import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;

/**
 * This class provides the actions for FlexiProvider algorithms.
 *
 * @author mwalthart
 * @author Holger Friedrich (support for Commands, based on IntegratorAction)
 * @version 0.9.1
 */
public abstract class IntegratorHandler extends AbstractAlgorithmHandler {
    private final String FLEXIPROVIDER_ALGORITHM_NAME = getFlexiProviderAlgorithmName();
    private final String READABLE_ALGORITHM_NAME = getReadableAlgorithmName();
    private String HEADER_DESCRIPTION;
    private boolean SHOW_OPERATION_GROUP = true;
    private boolean SHOW_PADDING_GROUP = false;
    private final String SHOW_KEY = getShowKey();
    private boolean SHOW_SIGNATURE_GROUP = false;
    private boolean SHOW_RANDOM_GROUP = false;
    private int SHOW_MESSAGE_DIGEST_GROUP = 0;
    private boolean SHOW_KEY_SOURCE_GROUP = false;

    private IntegratorWizard wizard;

    private int algorithmType;
    static final int TYPE_ASYMMETRIC_BLOCK = 0x01;
    static final int TYPE_ASYMMETRIC_HYBRID = 0x02;
    static final int TYPE_CIPHER_BLOCK = 0x03;
    static final int TYPE_CIPHER = 0x04;
    static final int TYPE_MESSAGE_DIGEST = 0x05;
    static final int TYPE_MESSAGE_AUTHTIFICATION_CODE = 0x06;
    static final int TYPE_SIGNATURE = 0x07;
    static final int TYPE_RANDOM_NUMBER_GENERATOR = 0x08;

    /**
     * default constructor
     */
    public IntegratorHandler() {
        super();
    }

    /**
     * returns the name of the algorithms as the FlexiProvider refers to it
     *
     * @return the name of the algorithms as the FlexiProvider refers to it
     */
    protected abstract String getFlexiProviderAlgorithmName();

    /**
     * returns the human readable name of the algorithm
     *
     * @return the human readable name of the algorithm
     */
    protected abstract String getReadableAlgorithmName();

    /**
     * returns the key id needed for this algorithms
     *
     * @return the key id needed for this algorithms
     */
    protected abstract String getShowKey();

    private String getHeaderDescription() {
        String mask = null;

        switch (algorithmType) {
        case TYPE_RANDOM_NUMBER_GENERATOR:
            mask = Messages.getString("IntegratorAction.titlemask-PRNG"); //$NON-NLS-1$
            break;
        case TYPE_SIGNATURE:
            mask = Messages.getString("IntegratorAction.titlemask-signature"); //$NON-NLS-1$
            break;
        case TYPE_MESSAGE_DIGEST:
            mask = Messages.getString("IntegratorAction.titlemask-digest"); //$NON-NLS-1$
            break;
        case TYPE_MESSAGE_AUTHTIFICATION_CODE:
            mask = Messages.getString("IntegratorAction.titlemask-mac"); //$NON-NLS-1$
            break;
        default:
            if (algorithmType == TYPE_ASYMMETRIC_BLOCK || algorithmType == TYPE_ASYMMETRIC_HYBRID) {
                mask = Messages.getString("IntegratorAction.titlemask-cryptosystem"); //$NON-NLS-1$
            } else if ((algorithmType == TYPE_CIPHER_BLOCK || algorithmType == TYPE_CIPHER) && SHOW_PADDING_GROUP) {
                mask = Messages.getString("IntegratorAction.titlemask-cryptosystem-padding"); //$NON-NLS-1$
            }
        }

        if (mask != null) {
            return String.format(mask, getReadableAlgorithmName());
        }
        return Messages.getString("DummyAction.2") + getReadableAlgorithmName() + Messages.getString("DummyAction.3") //$NON-NLS-1$ //$NON-NLS-2$
                + "";// + (algorithmType != TYPE_MESSAGE_DIGEST && algorithmType != TYPE_RANDOM_NUMBER_GENERATOR ? Messages.getString("DummyWizardPage.1") : ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private AlgorithmDescriptor searchList(String name, List<IMetaAlgorithm> list) {
        for (IMetaAlgorithm algorithm : list) {
            for (String algorithm_name : algorithm.getNames()) {
                if (algorithm_name.equals(name)) {
                    return new AlgorithmDescriptor(algorithm.getName(), algorithm.getType(), null);
                }
            }
        }
        return null;
    }

    private AlgorithmDescriptor getAlgorithmDescriptor(String name) {
        AlgorithmDescriptor descriptor = null;
        descriptor = searchList(name, AlgorithmsXMLManager.getInstance().getAsymmetricBlockCiphers());
        if (descriptor != null) {
            algorithmType = TYPE_ASYMMETRIC_BLOCK;
        }

        if (descriptor == null) {
            descriptor = searchList(name, AlgorithmsXMLManager.getInstance().getAsymmetricHybridCiphers());
            if (descriptor != null) {
                algorithmType = TYPE_ASYMMETRIC_HYBRID;
            }
        }
        if (descriptor == null) {
            descriptor = searchList(name, AlgorithmsXMLManager.getInstance().getBlockCiphers());
            if (descriptor != null) {
                algorithmType = TYPE_CIPHER_BLOCK;
                SHOW_PADDING_GROUP = true;
            }
        }
        if (descriptor == null) {
            descriptor = searchList(name, AlgorithmsXMLManager.getInstance().getCiphers());
            if (descriptor != null) {
                algorithmType = TYPE_CIPHER;
            }
        }
        if (descriptor == null) {
            descriptor = searchList(name, AlgorithmsXMLManager.getInstance().getMacs());
            if (descriptor != null) {
                algorithmType = TYPE_MESSAGE_AUTHTIFICATION_CODE;
                SHOW_OPERATION_GROUP = false;
            }
        }
        if (descriptor == null) {
            descriptor = searchList(name, AlgorithmsXMLManager.getInstance().getMessageDigests());
            if (descriptor != null) {
                algorithmType = TYPE_MESSAGE_DIGEST;
                try {
                    SHOW_MESSAGE_DIGEST_GROUP = java.security.MessageDigest.getInstance(name).getDigestLength();
                    SHOW_OPERATION_GROUP = false;
                } catch (java.security.NoSuchAlgorithmException e) {
                    return null;
                }
            }
        }
        if (descriptor == null) {
            descriptor = searchList(name, AlgorithmsXMLManager.getInstance().getSecureRandoms());
            if (descriptor != null) {
                algorithmType = TYPE_RANDOM_NUMBER_GENERATOR;
                SHOW_OPERATION_GROUP = false;
                SHOW_RANDOM_GROUP = true;
            }
        }
        if (descriptor == null) {
            descriptor = searchList(name, AlgorithmsXMLManager.getInstance().getSignatures());
            if (descriptor != null) {
                algorithmType = TYPE_SIGNATURE;
                SHOW_SIGNATURE_GROUP = true;
            }
        }

        SHOW_KEY_SOURCE_GROUP = (algorithmType == TYPE_CIPHER_BLOCK || algorithmType == TYPE_MESSAGE_AUTHTIFICATION_CODE);
        HEADER_DESCRIPTION = getHeaderDescription();
        return descriptor;
    }

    /**
     * runs the action: setup the algorithm and executed the specified operation
     */
    @Override
    public Object execute(ExecutionEvent event) {
        AlgorithmDescriptor descriptor = getAlgorithmDescriptor(FLEXIPROVIDER_ALGORITHM_NAME);
        if (descriptor == null) {
            MessageBox messageBox = new MessageBox(getActiveWorkbenchWindow().getShell());
            messageBox.setText(Messages.getString("DummyAction.error")); //$NON-NLS-1$
            messageBox.setMessage(Messages.getString("DummyAction.8")); //$NON-NLS-1$
            messageBox.open();
            return(null);
        }

        String readableNameExtension;
        switch (algorithmType) {
        case TYPE_RANDOM_NUMBER_GENERATOR:
            readableNameExtension = Messages.getString("DummyAction.random_number_generator"); //$NON-NLS-1$
            break;
        case TYPE_SIGNATURE:
            readableNameExtension = Messages.getString("DummyAction.signature"); //$NON-NLS-1$
            break;
        case TYPE_MESSAGE_DIGEST:
            readableNameExtension = Messages.getString("DummyAction.message_digest"); //$NON-NLS-1$
            break;
        case TYPE_MESSAGE_AUTHTIFICATION_CODE:
            readableNameExtension = Messages.getString("DummyAction.message_authentification_code"); //$NON-NLS-1$
            break;
        default:
            readableNameExtension = Messages.getString("DummyAction.encryption"); //$NON-NLS-1$
        }

        // Get which key lengths are valid for this algorithm
        int[] validKeyLengths = null;
        IMetaKeyGenerator keyGen = AlgorithmsXMLManager.getInstance().getSecretKeyGenerator(
                FLEXIPROVIDER_ALGORITHM_NAME);
        if (keyGen != null) {
            if (keyGen.getLengths().getLengths() != null) {
                validKeyLengths = new int[keyGen.getLengths().getLengths().size()];
                for (int i = 0; i < validKeyLengths.length; i++)
                    validKeyLengths[i] = keyGen.getLengths().getLengths().get(i);
            } else {
                validKeyLengths = new int[1];
                validKeyLengths[0] = keyGen.getLengths().getDefaultLength();
            }
        }

        wizard = new IntegratorWizard(READABLE_ALGORITHM_NAME + readableNameExtension, READABLE_ALGORITHM_NAME,
                HEADER_DESCRIPTION, SHOW_OPERATION_GROUP, SHOW_PADDING_GROUP, SHOW_KEY, SHOW_KEY_SOURCE_GROUP,
                validKeyLengths, SHOW_SIGNATURE_GROUP, SHOW_RANDOM_GROUP, SHOW_MESSAGE_DIGEST_GROUP, algorithmType);
        WizardDialog dialog = new WizardDialog(getActiveWorkbenchWindow().getShell(), wizard);
        dialog.setHelpAvailable(true);

        switch (dialog.open()) {
        case Window.OK:
            if (algorithmType == TYPE_CIPHER_BLOCK) {
                descriptor = new BlockCipherDescriptor(descriptor.getAlgorithmName(), AlgorithmsXMLManager
                        .getInstance().getMode(wizard.getMode().getDescription()).getID(), AlgorithmsXMLManager
                        .getInstance().getPaddingScheme(wizard.getPadding().getPaddingSchemeName()).getID(), null,
                        descriptor.getAlgorithmParameterSpec());
            }
            if (algorithmType == TYPE_RANDOM_NUMBER_GENERATOR) {
                if (wizard.doFilter())
                    descriptor = new SecureRandomDescriptor(descriptor.getAlgorithmName(), wizard.getRandomSize(),
                            wizard.getFilter());
                else
                    descriptor = new SecureRandomDescriptor(descriptor.getAlgorithmName(), wizard.getRandomSize());
            }

            IntegratorOperation operation = new IntegratorOperation(descriptor);

            operation.setEntryName(""); //$NON-NLS-1$
            operation.setInput(Messages.getString("InputType")); //$NON-NLS-1$
            operation.setOutput("<Editor>"); //$NON-NLS-1$
            operation.setSignature(wizard.signature());
            operation.setUseCustomKey(wizard.useCustomKey());
            if (wizard.useCustomKey())
                operation.setKeyBytes(wizard.getCustomKey());

            try {
                if (SHOW_KEY != null && !SHOW_KEY.equals("") && !wizard.useCustomKey()) //$NON-NLS-1$
                    operation.setKeyStoreAlias(wizard.getKey());
                if (wizard.encrypt()) { // explicit encrypt
                    if (descriptor.getType() == RegistryType.SIGNATURE) {
                        operation.setOperation(OperationType.VERIFY);
                    } else {
                        operation.setOperation(OperationType.ENCRYPT);
                    }
                } else { // implicit decrypt
                    if (descriptor.getType() == RegistryType.SIGNATURE) {
                        operation.setOperation(OperationType.SIGN);
                    } else {
                        operation.setOperation(OperationType.DECRYPT);
                    }
                }

                if (SHOW_MESSAGE_DIGEST_GROUP > 0 && !wizard.encrypt()) {
                    try {
                        MessageDigest digest = Registry.getMessageDigest(operation.getAlgorithmDescriptor()
                                .getAlgorithmName());
                        InputStream inputStream = EditorsManager.getInstance().getActiveEditorContentInputStream();
                        int i;
                        while ((i = inputStream.read()) != -1) {
                            digest.update((byte) i);
                        }
                        byte[] checksumAsBytes = digest.digest();
                        String checksum = ""; //$NON-NLS-1$
                        for (byte b : checksumAsBytes) {
                            String temp = Integer.toHexString((int) b);
                            checksum += (temp.length() == 1 ? "0" : "") + temp.substring(Math.max(0, temp.length() - 2)); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                        String expectedChecksum = wizard.getExpectedChecksum();
                        if (checksum.equalsIgnoreCase(expectedChecksum)) {
                            MessageBox messageBox = new MessageBox(getActiveWorkbenchWindow().getShell(),
                                    SWT.ICON_WORKING);
                            messageBox.setText(Messages.getString("IntegratorAction.0")); //$NON-NLS-1$
                            messageBox.setMessage(Messages.getString("IntegratorAction.1")); //$NON-NLS-1$
                            messageBox.open();
                        } else {
                            MessageBox messageBox = new MessageBox(getActiveWorkbenchWindow().getShell(),
                                    SWT.ICON_ERROR);
                            messageBox.setText(Messages.getString("IntegratorAction.2")); //$NON-NLS-1$
                            messageBox.setMessage(NLS.bind(Messages.getString("IntegratorAction.3"), //$NON-NLS-1$
                                    new Object[] { checksum.toLowerCase(), expectedChecksum.toLowerCase() }));
                            messageBox.open();
                        }

						if (operation.getOperation() == OperationType.SIGN) {
							MessageBox messageBox = new MessageBox(getActiveWorkbenchWindow().getShell(), SWT.NONE);
							messageBox.setText(Messages.getString("DummyAction.13")); //$NON-NLS-1$
							messageBox.setMessage(Messages.getString("DummyAction.14") + wizard.signature()); //$NON-NLS-1$
							messageBox.open();
						}
                    } catch (NoSuchAlgorithmException e) {
                        LogUtil.logError(IntegratorPlugin.PLUGIN_ID,
                                "NoSuchAlgorithmException while initializing a message digest", e, true); //$NON-NLS-1$
                    }
                } else {
                    PerformOperationManager.getInstance().firePerformOperation(operation);
					if (operation.getOperation() == OperationType.SIGN) {
						MessageBox messageBox = new MessageBox(getActiveWorkbenchWindow().getShell(), SWT.NONE);
						messageBox.setText(Messages.getString("DummyAction.13")); //$NON-NLS-1$
						messageBox.setMessage(Messages.getString("DummyAction.14") + wizard.signature()); //$NON-NLS-1$
						messageBox.open();
					}
                }

            } catch (IOException ex) {
                LogUtil.logError(ex);
            }
            break;
        case Window.CANCEL:
            break;
        }
        return(null);
    }

    @Override
    public void run(IDataObject o) {
    }
}