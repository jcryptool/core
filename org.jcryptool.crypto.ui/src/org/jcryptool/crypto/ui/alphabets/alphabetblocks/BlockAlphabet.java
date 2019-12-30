//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.alphabets.alphabetblocks;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.ui.alphabets.composite.AtomAlphabet;


public class BlockAlphabet extends AtomAlphabet {

	private String blockName;

	public BlockAlphabet(String characters, String blockName) {
		super(characters);
		this.blockName = blockName;
	}
	
	public BlockAlphabet(String characters) {
		this(characters, AbstractAlphabet.alphabetContentAsString(characters.toCharArray()));
	}

	public String getBlockName() {
		return blockName;
	}
	
	@Override
	public String toString() {
		return blockName;
	}

}
