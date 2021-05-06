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

import java.util.List;

import org.jcryptool.core.cryptosystem.core.Alphabet;
import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;

/**
 * Wraps a normal alphabet into a {@link TextCompatibleAlphabet} by providing the string parsing mechanism (by passing a
 * {@link TextConverter} in the constructor).
 * 
 * @see TextCompatibleAlphabet
 * @see Alphabet
 * @author Simon L
 */
public class TextCompatibleAlphabetWrapper<C> extends TextCompatibleAlphabet<C> {

    private TextConverter<C> converter;

    /**
     * The given alphabet is converted into a {@link TextCompatibleAlphabet}, providing its String conversion mechanism
     * by passing a {@link TextConverter}.
     * 
     * @param alphabetToWrap the alphabet to convert
     * @param converter the TextConverter to parse Strings into alphabet elements
     */
    public TextCompatibleAlphabetWrapper(Alphabet<C> alphabetToWrap, TextConverter<C> converter) {
        super(alphabetToWrap.getContent());
        this.converter = converter;
    }

    @Override
    public List<C> parseString(String s) throws ElementNotInAlphabetException {
        return converter.parseString(s, this);
    }

}
