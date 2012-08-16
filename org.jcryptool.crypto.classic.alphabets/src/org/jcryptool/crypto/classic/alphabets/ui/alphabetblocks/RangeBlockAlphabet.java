package org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks;

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
		int start = (int) startCharacter;
		int end = (int) endCharacter;
		int step = (end-start)/Math.abs(end-start);
		
		StringBuilder builder = new StringBuilder();
		
		for(int i=start; i != end+step; i+=step) {
			builder.append((char) i);
		}
		
		return builder.toString();
	}

}
