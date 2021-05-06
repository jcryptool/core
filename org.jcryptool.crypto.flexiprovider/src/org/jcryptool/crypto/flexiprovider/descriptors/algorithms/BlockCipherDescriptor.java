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
import de.flexiprovider.common.mode.ModeParameterSpec;


public class BlockCipherDescriptor extends AlgorithmDescriptor {

	private String mode;
	private String padding;
	private ModeParameterSpec modeParameters;
	
	public  BlockCipherDescriptor(String algorithmName, String mode, String padding, ModeParameterSpec modeParameters, AlgorithmParameterSpec algorithmParameters) {
		super(algorithmName, RegistryType.BLOCK_CIPHER, algorithmParameters);
		this.mode = mode;
		this.padding = padding;
		this.modeParameters = modeParameters;
	}
		
	public String getMode() {
		return mode;
	}

	public ModeParameterSpec getModeParameters() {
		return modeParameters;
	}

	public String getPadding() {
		return padding;
	}

	public String toString() {
		return getAlgorithmName() + "/" + mode + "/" + padding; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
