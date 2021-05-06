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
package org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes;

import java.util.Date;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;
import org.jcryptool.crypto.flexiprovider.operations.OperationsManager;
import org.jcryptool.crypto.flexiprovider.operations.engines.CheckOperationManager;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.IOperationChangedListener;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.algorithms.AlgorithmNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.IONode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.InputOutputNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.OutputNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.SignatureIONode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.keys.KeyNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.keys.KeyPairNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.keys.SecretKeyNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.ops.OperationsNode;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;

public class EntryNode extends TreeNode implements IFlexiProviderOperation {

    private String entryName;
    private long timestamp;
    private AlgorithmNode algorithmNode;
    private KeyNode keyNode;
    private OperationsNode operationNode;
    private IONode ioNode;
    private RegistryType type;
    private AlgorithmDescriptor descriptor;
    private char[] password;

    public EntryNode(String entryName, long timestamp, AlgorithmDescriptor descriptor) {
        super(getName(entryName, timestamp, descriptor));
        this.entryName = getName(entryName, timestamp, descriptor);
        this.timestamp = timestamp;
        this.type = descriptor.getType();
        this.descriptor = descriptor;

        algorithmNode = new AlgorithmNode(descriptor);
        this.addChild(algorithmNode);

        // Message digests and secure randoms do not require keys
        if (!descriptor.getType().equals(RegistryType.MESSAGE_DIGEST)
                && !descriptor.getType().equals(RegistryType.SECURE_RANDOM)) {
            // Only block ciphers, ciphers and macs require secret keys
            if (descriptor.getType().equals(RegistryType.BLOCK_CIPHER)
                    || descriptor.getType().equals(RegistryType.CIPHER)
                    || descriptor.getType().equals(RegistryType.MAC)) {
                keyNode = new SecretKeyNode();
            } else {
                keyNode = new KeyPairNode();
            }
            this.addChild(keyNode);
        }

        // Macs, message digests and secure randoms do not require a specific operation
        if (!descriptor.getType().equals(RegistryType.MAC)
                && !descriptor.getType().equals(RegistryType.MESSAGE_DIGEST)
                && !descriptor.getType().equals(RegistryType.SECURE_RANDOM)) {
            operationNode = new OperationsNode();
            this.addChild(operationNode);
        }

        if (descriptor.getType().equals(RegistryType.SECURE_RANDOM)) {
            ioNode = new OutputNode();
            this.addChild(ioNode);
        } else if (descriptor.getType().equals(RegistryType.SIGNATURE)) {
            // signatures
            ioNode = new SignatureIONode();
            this.addChild(ioNode);
        } else {
            // rest, standard input output
            ioNode = new InputOutputNode();
            this.addChild(ioNode);
        }

    }

    private static String getName(String entryName, long timestamp, AlgorithmDescriptor descriptor) {
        if (entryName.equals("no name")) { //$NON-NLS-1$
            return NLS.bind(Messages.EntryNode_0, new Object[] {descriptor.getAlgorithmName(), entryName, new Date(timestamp).toString()});
        } else {
            return entryName;
        }
    }

    public RegistryType getRegistryType() {
        return type;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
        super.setName(entryName);
        Iterator<IOperationChangedListener> it = OperationsManager.getInstance()
                .getOperationChangedListeners();
        while (it.hasNext()) {
            it.next().update(this);
        }
    }

    public String getEntryName() {
        return entryName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setOperation(OperationType type) {
        if (operationNode != null) {
            operationNode.setOperation(type);
            Iterator<IOperationChangedListener> it = OperationsManager.getInstance()
                    .getOperationChangedListeners();
            while (it.hasNext()) {
                it.next().update(operationNode);
            }
        }
    }

    public OperationType getOperation() {
        if (operationNode != null) {
            return operationNode.getOperation();
        } else
            return null;
    }

    public void setKeyStoreAlias(IKeyStoreAlias alias) {
        if (keyNode != null) {
            if (alias != null) {
                setPassword(null);
                keyNode.setAlias(alias);
                if (type.equals(RegistryType.SIGNATURE)) {
                    if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
                        // set sign
                        setOperation(OperationType.SIGN);
                    } else {
                        // set verify
                        setOperation(OperationType.VERIFY);
                    }
                } else if (type.equals(RegistryType.ASYMMETRIC_BLOCK_CIPHER)
                        || type.equals(RegistryType.ASYMMETRIC_HYBRID_CIPHER)) {
                    if (alias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
                        // set decrypt
                        setOperation(OperationType.DECRYPT);
                    } else {
                        // set encrypt
                        setOperation(OperationType.ENCRYPT);
                    }
                }
                if(!CheckOperationManager.getInstance().fireCheckOperation(this))
                	setKeyStoreAlias(null);
            } else {
                keyNode.setAlias(null);
                setOperation(OperationType.UNKNOWN);
            }
        }
    }

    public IKeyStoreAlias getKeyStoreAlias() {
        if (keyNode != null) {
            return keyNode.getAlias();
        } else
            return null;
    }

    public ImageDescriptor getImageDescriptor() {
        return ImageService.getImageDescriptor(FlexiProviderOperationsPlugin.PLUGIN_ID, "icons/16x16/klipper_dock.png");
    }

    public String getSignature() {
        if (ioNode != null && ioNode instanceof SignatureIONode) {
            return ((SignatureIONode) ioNode).getSignature();
        } else
            return null;
    }

    public String getInput() {
        if (ioNode != null) {
            if (ioNode instanceof SignatureIONode) {
                return ((SignatureIONode) ioNode).getInput();
            } else if (ioNode instanceof InputOutputNode) {
                return ((InputOutputNode) ioNode).getInput();
            }
        }
        return null;
    }

    public String getOutput() {
        if (ioNode != null) {
            if (ioNode instanceof OutputNode) {
                return ((OutputNode) ioNode).getOutput();
            } else if (ioNode instanceof InputOutputNode) {
                return ((InputOutputNode) ioNode).getOutput();
            }
        }
        return null;
    }

    public void setInput(String input) {
        if (ioNode != null) {
            if (ioNode instanceof InputOutputNode) {
                ((InputOutputNode) ioNode).setInput(input);
            } else if (ioNode instanceof SignatureIONode) {
                ((SignatureIONode) ioNode).setInput(input);
            }
        }
    }

    public void setOutput(String output) {
        if (ioNode != null) {
            if (ioNode instanceof OutputNode) {
                ((OutputNode) ioNode).setOutput(output);
            } else if (ioNode instanceof InputOutputNode) {
                ((InputOutputNode) ioNode).setOutput(output);
            }
        }
    }

    public void setSignature(String signature) {
        if (ioNode != null && ioNode instanceof SignatureIONode) {
            ((SignatureIONode) ioNode).setSignature(signature);
        }
    }

    public AlgorithmDescriptor getAlgorithmDescriptor() {
        return descriptor;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

//	@Override
	public boolean useCustomKey() {
		return false;
	}

//	@Override
	public byte[] getKeyBytes() {
		return null;
	}

}
