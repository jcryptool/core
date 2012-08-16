package org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks;

import java.util.List;

import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;
import org.jcryptool.crypto.classic.alphabets.composite.ExcludeCharAlphabet;

public class ExcludeCharBlock extends BlockAlphabet implements HasOriginalBlockAlpha {

	private BlockAlphabet origAlpha;

	public ExcludeCharBlock(BlockAlphabet origAlpha, char leaveOutChar) {
		this(origAlpha, leaveOutChar, generateLeaveOutName(origAlpha, leaveOutChar));
		this.origAlpha = origAlpha;
	}

	private ExcludeCharBlock(BlockAlphabet origAlpha, char leaveOutChar, String blockname) {
		super(excludedAlphaContent(origAlpha, leaveOutChar), blockname);
		this.origAlpha = origAlpha;
	}

	private static String excludedAlphaContent(BlockAlphabet origAlpha, char leaveOutChar) {
		List<Character> chars = ExcludeCharAlphabet.createExclusionCharset(origAlpha, leaveOutChar);
		char[] charsArray = new char[chars.size()];
		for (int i = 0; i < chars.size(); i++) {
			charsArray[i] = chars.get(i);
		}
		return String.valueOf(charsArray);
	}

	private static String generateLeaveOutName(BlockAlphabet origAlpha, char leaveOutChar) {
		if(origAlpha.getBlockName().equals(AtomAlphabet.alphabetContentAsString(origAlpha.getCharacterSet()))) {
			List<Character> chars = ExcludeCharAlphabet.createExclusionCharset(origAlpha, leaveOutChar);
			char[] charsArray = new char[chars.size()];
			for (int i = 0; i < chars.size(); i++) {
				charsArray[i] = chars.get(i);
			}
			return AtomAlphabet.alphabetContentAsString(charsArray);
		} else if(origAlpha instanceof ExcludeCharBlock) {
			return origAlpha.getBlockName()+", "+ AtomAlphabet.getPrintableCharRepresentation(leaveOutChar);
		} else {
			return origAlpha.getBlockName() + " without " + AtomAlphabet.getPrintableCharRepresentation(leaveOutChar);
		}
	}

	@Override
	public BlockAlphabet getOrigAlphabet() {
		return origAlpha;
	}


}
