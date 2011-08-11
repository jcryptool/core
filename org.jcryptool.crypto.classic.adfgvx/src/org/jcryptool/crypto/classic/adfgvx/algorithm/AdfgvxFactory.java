//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.adfgvx.algorithm;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

public class AdfgvxFactory {
	public AbstractAlphabet alpha;
	private char[] cipherAlphabet;
	private AdfgvxEngine algo = new AdfgvxEngine();
	
	public AdfgvxFactory(AbstractAlphabet alpha, char[] key){
		setCipherAlphabet (alpha, key);
	}
	
	public AdfgvxFactory(){
		
	}

	
	public char[] getAlphabet(){
		return alpha.getCharacterSet();
	}
	
	public void setCipherAlphabet (AbstractAlphabet alphabet, char[] key){
		this.alpha = alphabet;//AlphabetStore.getAlphabetByName(alphaName);
		this.cipherAlphabet = algo.getCipherAlphabet(key, alpha);
	}
	public char[] getCipherAlphabet(AbstractAlphabet alphabet, char[] key){
		setCipherAlphabet (alphabet, key);
		return cipherAlphabet;
	}
}
