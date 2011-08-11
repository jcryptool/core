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

	public abstract boolean isDefaultAlphabet();

	/**
	 * returns all characters included by the alphabet
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 */
	public abstract char[] getCharacterSet();

	/**
	 * returns method missing characters are displayed by the alphabet
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 */
	public abstract int getDisplayMissingCharacters();

	/**
	 * returns the name of the alphabet
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 */
	public abstract String getName();
	
	public abstract String getShortName();

	/**
	 * returns the character which is show as placeholder for characters which
	 * are not in the alphabet
	 * @author Robin Welsch
	 * @version 0.01
	 * @since 0.01
	 */
	public abstract char getSubstituteCharacter();

	public abstract void setDefaultAlphabet(boolean b);

	public abstract void setCharacterSet(char[] characterSet);

	public abstract boolean isBasic();

	public abstract void setBasic(boolean basic);
	
	public abstract void setName(String name);
	
	public abstract void setShortName(String shortName);
	
	public abstract boolean contains(char e);
	
}
