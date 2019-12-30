//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets.composite;

import java.util.LinkedList;
import java.util.List;

/**
 * Responsible for building String representations of AtomAlphabet objects, and parsing them back.
 *
 * @author Simon L
 *
 */
public class StringAlphabetFactory {

	public static final String TO_STRING_LEVEL_DOWN = "((";
	public static final String TO_STRING_LEVEL_UP = "))";
	public static final String TO_STRING_SEPARATOR = "||";
	public static final String TO_SHORT_STRING_LEVEL_DOWN = "(";
	public static final String TO_SHORT_STRING_LEVEL_UP = ")";
	public static final String TO_SHORT_STRING_SEPARATOR = "|";
	public static final String ALPHABET_TYPE_SEPARATOR = "=";

	public static final String ID_ATOM_ALPHA = "AtomAlphabet";
	public static final String ID_EXCLUDE_CHAR_ALPHA_ID = "ExcludeCharAlphabet";
	public static final String ID_COMPOSITE_ALPHA_ID = "CompositeAlphabet";

	public static String buildStringRepresentation(AtomAlphabet alphabet) {
		if(alphabet instanceof CompositeAlphabet) {
			CompositeAlphabet compositeAlphabet = (CompositeAlphabet) alphabet;
			return buildStringRepresentationCompositeAlphabet(compositeAlphabet);
		} else if (alphabet instanceof ExcludeCharAlphabet) {
			ExcludeCharAlphabet excludeCharAlphabet = (ExcludeCharAlphabet) alphabet;
			return buildStringRepresentationExcludeCharAlphabet(excludeCharAlphabet);
		}

		//TODO: !provisory char[] -> List<Character> "stringToList"
		return ID_ATOM_ALPHA + StringAlphabetFactory.ALPHABET_TYPE_SEPARATOR + listToString(stringToList(alphabet.getCharacterSet()));
	}

	private static String buildStringRepresentationExcludeCharAlphabet(
			ExcludeCharAlphabet excludeCharAlphabet) {
		return buildAlphabetRepresentationByArguments(ID_EXCLUDE_CHAR_ALPHA_ID,
				buildStringRepresentation(excludeCharAlphabet),
				excludeCharAlphabet.getExcludedChar().toString());
	}

	private static String buildStringRepresentationCompositeAlphabet(
			CompositeAlphabet compositeAlphabet) {
		//arguments: residualAlphabets string representation
		List<AtomAlphabet> residualAlphabets = compositeAlphabet.getResidualAlphabets();
		String[] argList = new String[residualAlphabets.size()];

		for (int i = 0; i < residualAlphabets.size(); i++) {
			argList[i] = buildStringRepresentation(residualAlphabets.get(i));
		}

		return buildAlphabetRepresentationByArguments(ID_COMPOSITE_ALPHA_ID, argList);
	}

	/**
	 * Returns the position of a specified string on the zero level in a String beginning at a specific index.
	 *
	 * @param string the string to search in
	 * @param beginIndex the first index to search for separators
	 * @param levelDownOp
	 * @return the index of the first character of the first encountered separator, or -1, if there is
	 * no separator found. (only separators on level zero are "found"). If the position is greater than
	 * or equal to the length of the String, -1 is returned.
	 */
	private static int findStringPositionOnLevelZero(String searchField, String searchSubject, String levelDownOp, String levelUpOp, int beginIndex) {
		int index = beginIndex;
		int level = 0;

		while(index < searchField.length()) {
			int nextSep = searchField.indexOf(searchSubject, index);
			if(nextSep == -1) return -1;

			int nextLvlDown = searchField.indexOf(levelDownOp, index);
			int nextLvlUp = searchField.indexOf(levelUpOp, index);

			if(level <= 0 && (nextLvlDown == -1 || nextSep < nextLvlDown)) {
				return nextSep;
			}

			if(nextLvlDown != -1 && ( nextLvlDown < nextLvlUp || nextLvlUp == -1) ) {
				index = nextLvlDown+1;
				level++;
			} else if(nextLvlUp != -1) {
				index = nextLvlUp+1;
				if(level > 0) level--;
			} else {
				return -1;
			}
		}
		return -1;
	}

	/**
	 * see {@link #findStringPositionOnLevelZero(searchField, TO_STRING_SEPARATOR, TO_STRING_LEVEL_DOWN, TO_STRING_LEVEL_UP, beginIndex)}
	 */
	private static int findSeparatorOnLvlZero(String searchField, int beginIndex) {
		return findStringPositionOnLevelZero(searchField, TO_STRING_SEPARATOR, TO_STRING_LEVEL_DOWN, TO_STRING_LEVEL_UP, beginIndex);
	}

	private static String buildAlphabetRepresentationByArguments(
			String alphaID, String... argList) {
		StringBuilder result = new StringBuilder();

		result.append(alphaID + ALPHABET_TYPE_SEPARATOR);
		/*
		 * argument format examples (assuming that the separator is "||"):
		 *  - arg1						assuming arg1 has no "||" in it
		 *  - arg1||arg2				assuming each arg has no "||" in it
		 *  - ((arg1))||arg2			assuming only arg1 has a "||" in it
		 * ...
		 */

		String argWithoutSeparatorsMask = "%s";
		String argWithSeparatorsMask = TO_STRING_LEVEL_DOWN + "%s" + TO_STRING_LEVEL_UP;

		for(int i=0; i<argList.length; i++) {
			if(i > 0) result.append(StringAlphabetFactory.TO_STRING_SEPARATOR);
			if(findSeparatorOnLvlZero(argList[i], 0) != -1) {
				result.append(String.format(argWithSeparatorsMask, argList[i]));
			} else {
				result.append(String.format(argWithoutSeparatorsMask, argList[i]));
			}
		}

		return result.toString();
	}

	protected static List<Character> stringToList(char[] characters) {
		List<Character> list = new LinkedList<Character>();
		for(char c: characters) list.add(c);
		return list;
	}

	protected static List<Character> stringToList(String characters) {
		return stringToList(characters.toCharArray());
	}

	protected static String listToString(List<Character> input) {
		StringBuffer result = new StringBuffer();
		for(Character c: input) result.append(c);
		return result.toString();
	}

	protected static char[] toCharArray(List<Character> input) {
		char[] result = new char[input.size()];
		for(int i=0; i<input.size(); i++) result[i] = input.get(i);
		return result;
	}


}
