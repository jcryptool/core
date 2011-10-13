package org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks;

import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;

public class RevertBlock extends BlockAlphabet {

	private BlockAlphabet origAlphabet;

	protected String charArrayToLabelCompatibleString(char[] string) {
		StringBuilder builder = new StringBuilder();
		for(char c: string) {
			if(c == '\r') {
				builder.append("\\r");
			} else if(c == '\n') {
				builder.append("\\n");
			} else if ((int) c<32) {
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
		if(alpha instanceof RangeBlockAlphabet 
				|| alpha.getBlockName().equals(String.valueOf(alpha.getCharacterSet()))
				|| alpha.getBlockName().equals("'"+String.valueOf(alpha.getCharacterSet())+"'")) {
			return reverseString(alpha.getBlockName());
		} else {
			return "invert("+alpha.getName()+")";
		}
	}

	public AtomAlphabet getOrigAlphabet() {
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
