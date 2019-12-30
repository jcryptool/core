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

public class RangeBlockAlphabet extends BlockAlphabet {

	private Character startCharacter;
	private Character endCharacter;


	public RangeBlockAlphabet(Character startCharacter, Character endCharacter) {
		super(generateCharRange(startCharacter, endCharacter), generateRangeName(startCharacter, endCharacter));
		this.startCharacter = startCharacter;
		this.endCharacter = endCharacter;
	}

	public Character getStartCharacter() {
		return startCharacter;
	}
	
	public Character getEndCharacter() {
		return endCharacter;
	}
	
	protected static String generateRangeName(Character startCharacter,
			Character endCharacter) {
		return startCharacter+"-"+endCharacter;
	}


	public static String generateCharRange(Character startCharacter,
			Character endCharacter) {
		int start = startCharacter;
		int end = endCharacter;
		int step = (end-start)/Math.abs(end-start);
		
		StringBuilder builder = new StringBuilder();
		
		for(int i=start; i != end+step; i+=step) {
			builder.append((char) i);
		}
		
		return builder.toString();
	}

}
