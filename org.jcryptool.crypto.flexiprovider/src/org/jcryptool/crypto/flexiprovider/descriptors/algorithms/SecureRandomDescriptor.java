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


public class SecureRandomDescriptor extends AlgorithmDescriptor {

	private int length;
	private byte[][] alphabet;

	public SecureRandomDescriptor(String algorithm, int length) {
		super(algorithm, RegistryType.SECURE_RANDOM, null);
		this.length = length;
	}
	
	public SecureRandomDescriptor(String algorithm, int length, byte[][] alphabet) {
		super(algorithm, RegistryType.SECURE_RANDOM, null);
		this.length = length;
		this.alphabet = alphabet;
	}


	public int getLength() {
		return length;
	}

	public byte[][] getAlphabet(){
		return alphabet;
	}
}
