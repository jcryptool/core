package org.jcryptool.crypto.uitools.alphabets.composite;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

/**
 * Represents an alphabet which is created by concatenating multiple alphabets
 * 
 * @author Simon L
 *
 */
public class CompositeAlphabet extends AtomAlphabet {
	
	public static final String COMPOSITE_ALPHABET_NAME = "CompositeAlphabet";
	
	private List<AtomAlphabet> residualAlphabets;
	private List<AtomAlphabet> originalAlphabets;

	/**
	 * create an alphabet by concatenating the character sets from one or more
	 * alphabets. Characters which occur a second time will be ignored; the parts of the alphabets
	 * that will be effectively kept after the doublet-neglection ("residual alphabets") are available via.
	 * #getResidualAlphabets() in the same order as put into this constructor.
	 * 
	 * @param alphabets the alphabets to create a composite from
	 */
	public CompositeAlphabet(List<? extends AtomAlphabet> alphabets) {
		super(generateCompositeAlphabet(alphabets));
		this.residualAlphabets = generateResidualAlphabets(alphabets);
		this.originalAlphabets = new LinkedList<AtomAlphabet>(alphabets);
	}
	
	/**
	 * see {@link #CompositeAlphabet(List)}
	 */
	public CompositeAlphabet(AtomAlphabet... alphabets) {
		this(alphaArrayToList(alphabets));
	}

	private static List<AtomAlphabet> alphaArrayToList(AtomAlphabet[] alphabets) {
		List<AtomAlphabet> alphaList = new LinkedList<AtomAlphabet>();
		for(AtomAlphabet a: alphabets) alphaList.add(a);
		return alphaList;
	}

	private static List<AtomAlphabet> generateResidualAlphabets(
			List<? extends AtomAlphabet> alphabets) {
		List<AtomAlphabet> residualAlphabets = new LinkedList<AtomAlphabet>();
		List<Character> characters = new LinkedList<Character>();
		for(AtomAlphabet alphabet: alphabets) {
			boolean wholeAlphabet = true;
			List<Character> localCharacters = new LinkedList<Character>();
			for(Character c: alphabet.getCharacterSet()) {
				if(! characters.contains(c)) {
					characters.add(c);
					localCharacters.add(c);
				} else {
					wholeAlphabet = false;
				}
			}
			
			if(wholeAlphabet) {
				residualAlphabets.add(alphabet);
			} else {
				residualAlphabets.add(new AtomAlphabet(localCharacters));
			}
		}
		
		return residualAlphabets;
	}

	private static List<Character> generateCompositeAlphabet(
			List<? extends AtomAlphabet> alphabets) {
		if(alphabets.size() < 1) throw new IllegalArgumentException("minimum of 1 alphabets required to create composite alphabet");
		List<Character> characters = new LinkedList<Character>();
		for(AbstractAlphabet alphabet: alphabets) {
			for(Character c: alphabet.getCharacterSet()) {
				if(! characters.contains(c)) {
					characters.add(c);
				}
			}
		}
			
		return characters;
	}
	
	/**
	 * @return the alphabets which were passed to the constructor at creating this composite alphabet
	 */
	public List<AtomAlphabet> getOriginalAlphabets() {
		return originalAlphabets;
	}
	
	/**
	 * @return the alphabets of which this composite alphabet exists 
	 * (doublets were deleted from the alphabets where they had common characters). 
	 * <br />Alphabets which were disjunct to the composite alphabet at their introduction are present 
	 * in this list as the original objects. Alphabets which didn't meet that requirement appear as 
	 * automatically created AtomAlphabets (new objects) with the doublet characters removed. 
	 */
	public List<AtomAlphabet> getResidualAlphabets() {
		return residualAlphabets;
	}
	
	
}
