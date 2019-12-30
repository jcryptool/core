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


public class RevertBlock extends BlockAlphabet implements HasOriginalBlockAlpha {

	private BlockAlphabet origAlphabet;

	protected String charArrayToLabelCompatibleString(char[] string) {
		StringBuilder builder = new StringBuilder();
		for(char c: string) {
			if(c == '\r') {
				builder.append("\\r");
			} else if(c == '\n') {
				builder.append("\\n");
			} else if (c<32) {
				builder.append("{"+((int) c)+"}");
			} else {
				builder.append(String.valueOf(c));
			}
		}
		
		return builder.toString();
	}
	
	public RevertBlock(BlockAlphabet alpha) {
		super(reverseString(String.valueOf(alpha.getCharacterSet())), generateReverseName(alpha));
		this.origAlphabet = alpha;
	}
	
	private static String generateReverseName(BlockAlphabet alpha) {
		if(alpha instanceof RangeBlockAlphabet) {
			return RangeBlockAlphabet.generateRangeName(((RangeBlockAlphabet) alpha).getEndCharacter(), ((RangeBlockAlphabet) alpha).getStartCharacter());
		} else if(alpha.getBlockName().equals(AbstractAlphabet.alphabetContentAsString(alpha.getCharacterSet()))) {
			return AbstractAlphabet.alphabetContentAsString(reverseString(String.valueOf(alpha.getCharacterSet())).toCharArray());
		} else {
			return "Reverse( "+alpha.getName()+" )";
		}
	}

	@Override
	public BlockAlphabet getOrigAlphabet() {
		return origAlphabet;
	}

	private static String reverseString(String string) {
		char[] newSet = new char[string.length()];
		for(int i=string.length()-1; i>=0; i--) {
			newSet[string.length()-1-i] = string.charAt(i);
		}
		return String.valueOf(newSet);
	}
	
}
