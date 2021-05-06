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

import org.jcryptool.core.cryptosystem.core.Cryptosystem;
import org.jcryptool.core.cryptosystem.core.Key;
import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;

/**
 * Wrapper to convert a normal {@link Cryptosystem} into a {@link TextCompatibleCryptosystem} by providing text
 * converters to handle string parsing.
 * 
 * @see Cryptosystem
 * @see TextCompatibleCryptosystem
 * 
 * @author Simon L
 */
public class TextCompatibleCryptosystemWrapper<P, C, K extends Key> extends TextCompatibleCryptosystem<P, C, K> {

    private Cryptosystem<P, C, K> systemToWrap;

    /**
     * Wraps the given {@link Cryptosystem} into a {@link TextCompatibleCryptosystem} by providing string parsing
     * techniques through {@link TextConverter}
     * 
     * @param systemToWrap the system to wrap/convert
     * @param plainTextConverter the plainTextConverter
     * @param cipherTextConverter the cipherTextConverter
     */
    public TextCompatibleCryptosystemWrapper(Cryptosystem<P, C, K> systemToWrap, TextConverter<P> plainTextConverter,
            TextConverter<C> cipherTextConverter) {
        super(new TextCompatibleAlphabetWrapper<P>(systemToWrap.getPlainTextAlphabet(), plainTextConverter),
                new TextCompatibleAlphabetWrapper<C>(systemToWrap.getCipherTextAlphabet(), cipherTextConverter));
        this.systemToWrap = systemToWrap;
    }

    @Override
    public List<C> encrypt(List<? extends P> plainText, K key) throws ElementNotInAlphabetException {
        return systemToWrap.encrypt(plainText, key);
    }

    @Override
    public List<P> decrypt(List<? extends C> cipherText, K key) throws ElementNotInAlphabetException {
        return systemToWrap.decrypt(cipherText, key);
    }

}
