//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

/**
 * An Alphabet for the classic algorithms.
 * 
 * @author Robin Welsch
 * @author t-kern (refactoring to new architecture)
 * 
 */
public class Alphabet extends AbstractAlphabet {

	private String name = "";

	private char substituteCharacter = ' ';

	private int displayMissingCharacters;

	private char[] characterSet;

	private boolean defaultAlphabet = false;

	private boolean basic = false;

	private String shortName = "";
	
	/**
	 * Constructor using a String for initialization
	 * 
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 * @param set
	 *            Alphabet String
	 * @param name
	 *            name of the alphabet
	 * @param display
	 *            the way of characters not included in alphabet are displayed
	 */
	public Alphabet(char[] set, String name, int display) {
		characterSet = set;
		displayMissingCharacters = display;
		this.name = name;
		this.shortName = name;
	}

	public Alphabet(char[] set, String name, int display, boolean basic) {
		characterSet = set;
		displayMissingCharacters = display;
		this.name = name;
		this.shortName = name;
		this.basic = basic;
	}

	public Alphabet(char[] set, String name, String shortName, int display,
			boolean basic) {
		this(set,name,display,basic);
		this.shortName = shortName;
	}

	@Override
	public boolean isDefaultAlphabet() {
		return defaultAlphabet;
	}

	/**
	 * returns all characters included by the alphabet
	 * 
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 */

	@Override
	public char[] getCharacterSet() {
		// preventing unwanted changes to the charset
		return characterSet.clone();
	}

	/**
	 * returns method missing characters are displayed by the alphabet
	 * 
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 */
	@Override
	public int getDisplayMissingCharacters() {
		return displayMissingCharacters;
	}

	/**
	 * returns the name of the alphabet
	 * 
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * returns the character which is show as placeholder for characters which
	 * are not in the alphabet
	 * 
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 */

	@Override
	public char getSubstituteCharacter() {
		return substituteCharacter;
	}

	@Override
	public void setDefaultAlphabet(boolean b) {
		defaultAlphabet = b;
	}

	@Override
	public void setCharacterSet(char[] characterSet) {
		this.characterSet = characterSet;
	}

	@Override
	public boolean isBasic() {
		return basic;
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
	public String getShortName() {
		return shortName;
	}

	@Override
	public void setBasic(boolean basic) {
		this.basic = basic;
	}

	@Override
	public boolean contains(char e) {
		for (int i = 0; i < characterSet.length; i++) {
			if (characterSet[i] == e) {
				return true;
			}
		}
		return false;
	}

}
