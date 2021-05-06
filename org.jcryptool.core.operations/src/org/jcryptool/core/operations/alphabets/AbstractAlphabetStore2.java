// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
/**
 * 
 */
package org.jcryptool.core.operations.alphabets;

import java.util.List;

import org.jcryptool.core.cryptosystem.core.Alphabet;

/**
 * Abstract superclass for the AlphabetStore.
 * 
 * @author t-kern, sleischnig
 * 
 */
public abstract class AbstractAlphabetStore2 {

    public abstract void init();

    public abstract List<Alphabet<Character>> getAlphabets();

    /**
     * @return the first alphabet in this store of the given name
     */
    public abstract Alphabet<Character> getAlphabetByName(String name);

    /**
     * @return the first alphabet in this store of the given short name
     */
    public abstract Alphabet<Character> getAlphabetByShortName(String name);

    /**
     * Adds an alphabet to the store
     * 
     * @param alphabet the alphabet to add
     * @return the reference object which is now referencing the alphabet in this store
     */
    public abstract AlphabetReference addAlphabet(Alphabet<Character> alphabet, String name, String shortName,
            boolean isDefault, boolean isIntegral);

    /**
     * removes an alphabet from the store
     * 
     * @param alphabetReference the reference object for the alphabet
     * @return whether the removal was successful or not
     */
    public abstract boolean removeAlphabet(AlphabetReference alphabetReference);

    public abstract void updateAlphabet(AlphabetReference alphabetReference, Alphabet<Character> alphabet);

    public abstract void setAlphabets(List<Alphabet<Character>> alphas);

    public abstract void storeAlphabets();

    public abstract Alphabet<Character> getAlphabet(AlphabetReference alphabetReference);

}
