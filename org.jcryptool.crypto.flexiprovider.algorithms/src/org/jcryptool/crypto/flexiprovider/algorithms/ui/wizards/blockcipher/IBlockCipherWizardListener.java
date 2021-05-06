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
package org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.blockcipher;

import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaMode;

import de.flexiprovider.api.parameters.AlgorithmParameterSpec;
import de.flexiprovider.common.mode.ModeParameterSpec;

public interface IBlockCipherWizardListener {

	public void setMode(IMetaMode mode);
	public void setPadding(String padding);
	public void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameters);
	public void setModeParameterSpec(ModeParameterSpec modeParameters);
	
}
