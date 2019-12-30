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
package org.jcryptool.crypto.ui.alphabets.composite;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

//TODO: change method names to sth more suitable (toStringShort....)
/**
 * Basic alphabet which may be created solely from its characters
 * 
 * @author Simon L
 *
 */
public class AtomAlphabet extends AbstractAlphabet {
	
	public static final String ATOM_ALPHABET_NAME = "AtomAlphabet";
	
	public static final boolean IS_STANDARD_ALPHABET_STD = false;
	public static final boolean IS_BASIC_STD = false;
	public static final String NAME_STD = null;
	public static final String SHORTNAME_STD = null;
	
	private List<Character> content;
	private boolean isBasic = IS_BASIC_STD;
	private boolean isDefault = IS_STANDARD_ALPHABET_STD;
	/**
	 * null => generated name is returned in getter
	 */
	private String name;
	/**
	 * null => generated short name is returned in getter
	 */
	private String shortName;
	
	/**
	 * @param characters
	 * @param isBasic
	 * @param isDefault
	 * @param name
	 * @param shortName
	 * 
	 * @deprecated the additional parameters will soon be managed in the alphabets manager
	 */
	@Deprecated
	public AtomAlphabet(List<Character> characters, boolean isBasic, boolean isDefault, String name, String shortName) {
		this.content = filterDoublets(characters);
		setName(name);
		setShortName(shortName);
		setBasic(isBasic);
		setDefaultAlphabet(isDefault);
	}

	/**
	 * creates an alphabet solely from its containing characters, defaulting the other parameters
	 * (see public static standard fields like {@value AtomAlphabet#IS_STANDARD_ALPHABET_STD}.
	 * <br />
	 * Double characters will be ignored.<br />
	 * 
	 * @param characters the characters of the alphabet
	 */
	public AtomAlphabet(List<Character> characters) {
		this(characters, false, false, null, null);
	}
	

	/**
	 * creates an alphabet solely from its containing characters, defaulting the other parameters
	 * (see public static standard fields like {@value AtomAlphabet#IS_STANDARD_ALPHABET_STD}.
	 * <br />
	 * by default, the names are generated from the alphabet content.
	 * 
	 * @param characters the characters of the alphabet
	 */
	public AtomAlphabet(String characters) {
		this(StringAlphabetFactory.stringToList(characters));
	}
	
	/**
	 * creates an alphabet solely from its containing characters, defaulting the other parameters
	 * (see public static standard fields like {@value AtomAlphabet#IS_STANDARD_ALPHABET_STD}.
	 * <br />
	 * by default, the names are generated from the alphabet content.
	 * 
	 * @param characters the characters of the alphabet
	 */
	public AtomAlphabet(char[] characters) {
		this(StringAlphabetFactory.stringToList(characters));
	}

	private static List<Character> filterDoublets(List<Character> characters) {
		List<Character> ref = new LinkedList<Character>();
		for(Character c: characters) {
			if(! ref.contains(c)) {
				ref.add(c);
			}
		}
		
		return ref;
	}

	@Override
	public char[] getCharacterSet() {
		return StringAlphabetFactory.toCharArray(content);
	}
	@Override
	public boolean contains(char e) {
		return content.contains(e);
	}
	
	@Override
	public String toString() {
		return StringAlphabetFactory.buildStringRepresentation(this);
	}

	@Override
	public boolean isDefaultAlphabet() {
		return isDefault;
	}

	@Override
	public int getDisplayMissingCharacters() {
		// wont be available for much longer
		return -1;
	}

	@Override
	public char getSubstituteCharacter() {
		// wont be available for much longer
		return 'X';
	}

	@Override
	public void setDefaultAlphabet(boolean b) {
		this.isDefault = b;
	}

	@Override
	public void setCharacterSet(char[] characterSet) {
		this.content = StringAlphabetFactory.stringToList(characterSet);
	}

	@Override
	public boolean isBasic() {
		return isBasic;
	}

	@Override
	public void setBasic(boolean basic) {
		this.isBasic = basic;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public String getShortName() {return shortName != null ? shortName : generateShortName();} 

	@Override
	public String getName() {return name != null ? name : generateName();} 

	/**
	 * Method for creating a generated short name for the alphabet if no short name is explicitely set
	 * 
	 * @return a generated name
	 * @deprecated like {@link #getShortName()}
	 */
	@Deprecated
	private String generateShortName() {
		return generateName();
	}

	/**
	 * Method for creating a generated name for the alphabet if no short name is explicitely set
	 * 
	 * @return a generated name
	 * @deprecated like {@link #getName()}
	 */
	@Deprecated
	private String generateName() {
		return alphabetContentAsString(getCharacterSet());
	}

}
