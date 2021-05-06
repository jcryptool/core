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
package org.jcryptool.crypto.flexiprovider.descriptors.algorithms;

import org.jcryptool.crypto.flexiprovider.types.RegistryType;

import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

public class AlgorithmDescriptor {

	private String algorithmName;
	private AlgorithmParameterSpec spec;
	private RegistryType type;
	
	public AlgorithmDescriptor(String algorithmName, RegistryType type, AlgorithmParameterSpec spec) {
		this.algorithmName = algorithmName;
		this.spec = spec;
		this.type = type;
	}
	
	public String getAlgorithmName() {
		return algorithmName;
	}
	
	public AlgorithmParameterSpec getAlgorithmParameterSpec() {
		return spec;
	}
	
	public RegistryType getType() {
		return type;
	}
	
}
