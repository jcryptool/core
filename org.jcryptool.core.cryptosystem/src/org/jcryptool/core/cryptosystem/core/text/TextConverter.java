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

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.cryptosystem.core.Alphabet;
import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;

/**
 * Simple shell for a String parsing mechanism.
 * 
 * @param <C> the generic type of objects to convert
 * @author Simon L
 */
public abstract class TextConverter<C> {
    public final static TextConverter<Character> CHAR_TEXT_CONVERTER = new TextConverter<Character>() {
        @Override
        public List<Character> parseString(String s, Alphabet<Character> alphabet) throws ElementNotInAlphabetException {
            List<Character> result = new LinkedList<Character>();
            for (Character c : s.toCharArray()) {
                if (!alphabet.getContent().contains(c)) {
                    throw new ElementNotInAlphabetException();
                }
                result.add(c);
            }
            return result;
        }
    };

    /**
     * parses the given String into a sequence of Alphabet elements. Must throw {@link ElementNotInAlphabetException}
     * when encountering content that does not belong to the given alphabet or utterly unexpected content.
     * 
     * @param s the string to parse
     * @param alphabet the alphabet which provides the possible list elements
     * @return a list of alphabetic elements
     * @throws ElementNotInAlphabetException when unexpected content occurs (e. g. elements which are not parseable
     *             within the alphabet)
     */
    public abstract List<C> parseString(String s, Alphabet<C> alphabet) throws ElementNotInAlphabetException;
}