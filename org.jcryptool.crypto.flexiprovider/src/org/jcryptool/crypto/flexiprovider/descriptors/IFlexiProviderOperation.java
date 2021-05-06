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
package org.jcryptool.crypto.flexiprovider.descriptors;

import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.types.OperationType;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;

public interface IFlexiProviderOperation {

    public AlgorithmDescriptor getAlgorithmDescriptor();

    public String getEntryName();

    public String getInput();

    /**
     * Returns whether a custom key should be used instead a key from a key store.
     * The custom key can be received by calling getKeyBytes(); 
     */
    public boolean useCustomKey(); 
    
    public IKeyStoreAlias getKeyStoreAlias();
    
    /**
     * If useCustomKey is true, this method returns the bytes which should be used for the operation
     *
     */
    public byte[] getKeyBytes();

    public OperationType getOperation();

    public String getOutput();

    public RegistryType getRegistryType();

    public String getSignature();

    public long getTimestamp();

    public void setEntryName(String entryName);

    public void setInput(String input);

    public void setKeyStoreAlias(IKeyStoreAlias alias);

    public void setOperation(OperationType type);

    public void setOutput(String output);

    public void setSignature(String signature);

    public void setPassword(char[] password);

    public char[] getPassword();
}
