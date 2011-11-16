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
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 * @deprecated soon to be replaced by List<Character> getCharacterSet() 
	 */
	public abstract char[] getCharacterSet();
	
	/**
	 * returns method missing characters are displayed by the alphabet
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 * @deprecated functionality to be moved to the AlphabetManager
	 */
	public abstract int getDisplayMissingCharacters();

	/**
	 * returns the name of the alphabet
	 * @author Robin Welsch
	 * @version 0.01
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
	 * @author Robin Welsch
	 * @version 0.01
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
	
}
