//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.cryptosystem.core.text;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;

/**
 * The most basic form of a cryptographic alphabet based on characters.
 * 
 * @author Simon L
 */
public class CharAlphabet extends TextCompatibleAlphabet<Character> {

    /**
     * Creates an alphabet by specifying its characters. The order in which the iterator of the collection returns the
     * characters will be maintained.
     * 
     * @param alphabetCharacters the alphabet characters.
     * @throws IllegalArgumentException if the collection is null or if the collection is empty
     */
    public CharAlphabet(Collection<Character> alphabetCharacters) {
        super(alphabetCharacters);
    }

    /**
     * @see #CharacterAlphabet(Collection)
     */
    public CharAlphabet(Character[] alphabetCharacters) {
        super(alphabetCharacters);
    }

    /**
     * @see #CharacterAlphabet(Collection)
     */
    public CharAlphabet(char[] alphabetCharacters) {
        this(charArrayToCharacterList(alphabetCharacters));
    }

    /**
     * @see #CharacterAlphabet(Collection)
     */
    public CharAlphabet(String alphabetString) {
        this(alphabetString.toCharArray());
    }

    /**
     * Converts a array of char into a List of Character.
     * 
     * @param chars
     * @return
     */
    private static List<Character> charArrayToCharacterList(char[] chars) {
        List<Character> list = new LinkedList<Character>();
        for (char c : chars)
            list.add(c);

        return list;
    }

    @Override
    public List<Character> parseString(String string) throws ElementNotInAlphabetException {
        List<Character> charList = new LinkedList<Character>();
        for (Character c : string.toCharArray()) {
            if (!getContent().contains(c)) {
                throw new ElementNotInAlphabetException();
            }
            charList.add(c);
        }
        return charList;
    }

}
