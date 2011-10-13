package org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks;

import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;

public class BlockAlphabet extends AtomAlphabet {

	private String blockName;

	public BlockAlphabet(String characters, String blockName) {
		super(characters);
		this.blockName = blockName;
	}
	
	public BlockAlphabet(String characters) {
		this(characters, characters);
	}

	public String getBlockName() {
		return blockName;
	}
	
	@Override
	public String toString() {
		return blockName;
	}

}
