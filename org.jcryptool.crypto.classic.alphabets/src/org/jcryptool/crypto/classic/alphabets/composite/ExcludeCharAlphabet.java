package org.jcryptool.crypto.classic.alphabets.composite;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

/**
 * Alphabet which represents a target alphabet with all of the characters of this target but one 
 * 
 * @author Simon L
 *
 */
public class ExcludeCharAlphabet extends AtomAlphabet {

	public static final String ATOM_ALPHABET_NAME = "ExcludeCharAlphabet";
	
	private AtomAlphabet baseAlphabet;
	private Character excludedChar;

	/**
	 * @param baseAlphabet
	 * @param excludedChar
	 * @param isBasic
	 * @param isDefault
	 * @param name
	 * @param shortName
	 * 
	 * @deprecated the additional parameters will soon be managed in the alphabets manager
	 */
	public ExcludeCharAlphabet(AtomAlphabet baseAlphabet, Character excludedChar, boolean isBasic, boolean isDefault, String name, String shortName) {
		super(createExclusionCharset(baseAlphabet, excludedChar), isBasic, isDefault, name, shortName);
		this.baseAlphabet = baseAlphabet;
		this.excludedChar = excludedChar;
	}
	
	public ExcludeCharAlphabet(AtomAlphabet alphabet, Character excludedChar) {
		this(alphabet, excludedChar, IS_BASIC_STD, IS_STANDARD_ALPHABET_STD, NAME_STD, SHORTNAME_STD);
	}
	
	private static List<Character> createExclusionCharset(AbstractAlphabet alphabet, Character excludedChar) {
		List<Character> charset = new LinkedList<Character>();
		for(Character c: alphabet.getCharacterSet()) if(! c.equals(excludedChar)) {
			charset.add(c);
		}
		return charset;
	}

	public AbstractAlphabet getBaseAlphabet() {
		return baseAlphabet;
	}

	public Character getExcludedChar() {
		return excludedChar;
	}

}
