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
package org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces;

import java.util.List;

import org.jcryptool.crypto.flexiprovider.types.RegistryType;


public interface IMetaAlgorithm extends IMetaEntry, Comparable<IMetaAlgorithm> {

	public void addName(String name);

	public void addStandardParams(String params);

	public String getBlockCipherMode();

	public String getBlockCipherName();

	public IMetaOID getBlockCipherOID();

	public List<Integer> getBlockLengths();

	public int getDefaultBlockLength();

	public String getKeyGeneratorClassName();

	public String getName();

	public int getNameCount();

	public List<String> getStandardParams();

	public RegistryType getType();

	public void merge(IMetaAlgorithm algorithm);

	public void setBlockCipherMode(String mode);

	public void setBlockCipherName(String name);

	public void setBlockCipherOID(IMetaOID oid);

	public void setBlockLengths(List<Integer> blockLengths);

	public void setDefaultBlockLength(int defaultBlockLength);

	public void setKeyGeneratorClassName(String keyGenClassName);

	public void setName(String name);

}
