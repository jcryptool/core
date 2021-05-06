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
package org.jcryptool.crypto.flexiprovider.integrator;

import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;

/**
 * operation object to contain the FlexiProvider parameters
 *
 * @author mwalthart
 *
 */
public class IntegratorOperation implements IFlexiProviderOperation {

    private String signature;
    private String output;
    private String input;
    private String entryName;
    private IKeyStoreAlias alias;
    private OperationType type;
    private AlgorithmDescriptor descriptor;
    private char[] password;
    private boolean useCustomKey;
    private byte[] customKey;

    /**
     * constructor
     *
     * @param descriptor the algorithms descriptor
     */
    public IntegratorOperation(AlgorithmDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * getter for the descriptor
     */
    public AlgorithmDescriptor getAlgorithmDescriptor() {
        return descriptor;
    }

    /**
     * getter for the entry name
     */
    public String getEntryName() {
        return entryName;
    }

    /**
     * getter for the input
     */
    public String getInput() {
        return input;
    }

    /**
     * getter for the keystore alias
     */
    public IKeyStoreAlias getKeyStoreAlias() {
        return alias;
    }

    /**
     * getter for the operation
     */
    public OperationType getOperation() {
        return type;
    }

    /**
     * getter for the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * getter for the registry type
     */
    public RegistryType getRegistryType() {
        return descriptor.getType();
    }

    /**
     * getter for the signature file
     */
    public String getSignature() {
        return signature;
    }

    /**
     * getter for the timestamp
     */
    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * setter for the entry name
     */
    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    /**
     * setter for the input
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * setter for the keystore alias
     */
    public void setKeyStoreAlias(IKeyStoreAlias alias) {
        this.alias = alias;
    }

    /**
     * setter for the operation
     */
    public void setOperation(OperationType type) {
        this.type = type;
    }

    /**
     * setter for the output
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * setter for the signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

//	@Override
	public boolean useCustomKey() {
		return useCustomKey;
	}
	
	public void setUseCustomKey(boolean value) {
		this.useCustomKey = value;
	}
	

//	@Override
	public byte[] getKeyBytes() {
		return customKey;
	}
	
	public void setKeyBytes(byte[] value) {
		this.customKey = value;
	}
	
	
	
}
