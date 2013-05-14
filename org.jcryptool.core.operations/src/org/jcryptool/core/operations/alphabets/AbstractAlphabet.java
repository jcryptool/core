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
package org.jcryptool.core.operations.alphabets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Abstract superclass for an Alphabet.
 *
 * @author t-kern
 *
 */
public abstract class AbstractAlphabet {

	public static final int NO_DISPLAY = 0;

	public static final int DISPLAY = 1;

	public static final int SUBSTITUTE = 2;

	public static final int HIGHLIGHT = 3;

	/**
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract boolean isDefaultAlphabet();

	/**
	 * returns all characters included by the alphabet
	 * @since 0.01
	 * @deprecated soon to be replaced by List<Character> getCharacterSet()
	 */
	public abstract char[] getCharacterSet();

	/**
	 * returns method missing characters are displayed by the alphabet
	 * @since 0.01
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract int getDisplayMissingCharacters();

	/**
	 * returns the name of the alphabet
	 * @since 0.01
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract String getName();

	/**
	 * @return the short name
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract String getShortName();

	/**
	 * returns the character which is show as placeholder for characters which
	 * are not in the alphabet
	 * @since 0.01
	 * @deprecated functionality to be removed
	 */
	public abstract char getSubstituteCharacter();

	/**
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract void setDefaultAlphabet(boolean b);

	/**
	 * @deprecated alphabets will be immutable
	 */
	public abstract void setCharacterSet(char[] characterSet);

	/**
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract boolean isBasic();

	/**
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract void setBasic(boolean basic);

	/**
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract void setName(String name);

	/**
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract void setShortName(String shortName);

	public abstract boolean contains(char e);

	
	
	private static Map<Character, String> specialCharactersForPrinting = new HashMap<Character, String>();
	static {
		specialCharactersForPrinting.put('\n', "\\n"); //$NON-NLS-1$
		specialCharactersForPrinting.put('\r', "\\r"); //$NON-NLS-1$
		specialCharactersForPrinting.put('\t', "\\t"); //$NON-NLS-1$
	}
	
	/**
	 * converts an alphabet's content to a String. This makes sure that linebreaks etc. are
	 * shown as "\n" etc.<br />
	 * 
	 * 
	 * @param alpha the alphabet
	 * @return a string containing a representation of every character in the alphabet
	 */
	public static String alphabetContentAsString(char[] alphaContent) {
		StringBuilder sb = new StringBuilder();
		
		for(char c: alphaContent) {
			String charRep = getPrintableCharRepresentation(c);
			sb.append(charRep);
		}
		
		return sb.toString();
	}

	public static String getPrintableCharRepresentation(char c) {
		if(specialCharactersForPrinting.containsKey(Character.valueOf(c))) {
			return specialCharactersForPrinting.get(Character.valueOf(c));
//		} else if(!isCharacterOnKeyboard(c)) {
		} else if((int)c < 32) {
			return "{" + String.valueOf((int) c) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return String.valueOf(c);
		}
	}

	private static boolean isCharacterOnKeyboard(char c) {
		boolean isControlChar = Character.isISOControl(c);
		boolean isNonANSIChar = (int)c > 125;
		boolean isGermanUmlaut = 
				Character.valueOf(c).equals('ä') ||
				Character.valueOf(c).equals('ö') ||
				Character.valueOf(c).equals('ü') ||
				Character.valueOf(c).equals('Ä') ||
				Character.valueOf(c).equals('Ö') ||
				Character.valueOf(c).equals('Ü') ||
				Character.valueOf(c).equals('ß')
				;
		
		return (!isControlChar && !isNonANSIChar) || isGermanUmlaut;
	}

	/**
	 * pendant to {@link #alphabetContentAsString(AbstractAlphabet)}
	 */
	public static char[] parseAlphaContentFromString(String alpha) {
		Pattern nonprintables;
		nonprintables = Pattern.compile("\\{\\d+}", Pattern.DOTALL); //$NON-NLS-1$
		String newAlpha = alpha;
		
		Matcher m = nonprintables.matcher(alpha);
		
		while(m.find()) {
			int number = Integer.valueOf(newAlpha.substring(m.start()+1, m.end()-1));
			newAlpha = newAlpha.replace(m.group(), String.valueOf((char)number));
			m = nonprintables.matcher(newAlpha);
		}
		
		for(Entry<Character, String> replacement: specialCharactersForPrinting.entrySet()) {
			newAlpha = newAlpha.replace(replacement.getValue(), String.valueOf(replacement.getKey()));
		}
		
		//TODO!: delete doublets
		
		return newAlpha.toCharArray();
	}
	
}
