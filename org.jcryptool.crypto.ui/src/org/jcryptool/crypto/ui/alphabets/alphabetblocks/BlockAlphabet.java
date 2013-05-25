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
