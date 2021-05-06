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
import java.util.List;

import org.jcryptool.core.cryptosystem.core.Alphabet;
import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;

/**
 * Cryptographic alphabet which provides a function to convert a string into a list of elements of this alphabet. The
 * other way of the conversion (alphabet elements to String) is automatically provided by the toString()- method of the
 * elements.
 * 
 * @param <C> Boundary generic type for the stored alphabet elements
 * @author Simon L
 */
public abstract class TextCompatibleAlphabet<C> extends Alphabet<C> {

    /**
     * @see Alphabet#Alphabet(Object[])
     */
    public TextCompatibleAlphabet(C[] alphabetContent) {
        super(alphabetContent);
    }

    /**
     * @see Alphabet#Alphabet(Collection)
     */
    public TextCompatibleAlphabet(Collection<? extends C> alphabetContent) {
        super(alphabetContent);
    }

    /**
     * Parses a list of elements from this alphabet from a string. strings should be well-formatted, such, that there
     * will be no unexpected content. Throws ElementNotInAlphabetException, should unparseable content be encountered.
     * 
     * @param s the String
     * @return a list of Elements from this alphabet
     * @throws ElementNotInAlphabetException when unexpected content occurs (e. g. elements which are not parseable
     *             within this alphabet)
     */
    public abstract List<C> parseString(String s) throws ElementNotInAlphabetException;

}
