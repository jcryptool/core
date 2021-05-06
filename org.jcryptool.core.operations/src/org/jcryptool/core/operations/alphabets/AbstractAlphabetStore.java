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

import java.io.IOException;

/**
 * Abstract superclass for the AlphabetStore.
 * 
 * @author t-kern
 * 
 */
public abstract class AbstractAlphabetStore {

    public abstract void init();

    public abstract AbstractAlphabet[] getAlphabets();

    public abstract AbstractAlphabet getAlphabetByName(String name);

    public abstract void addAlphabet(AbstractAlphabet alphabet);

    public abstract void removeAlphabet(AbstractAlphabet alphabet);

    public abstract void updateAlphabet(String alphabetTitle, char[] newCharacterSet);

    public abstract String[] getSelfCreatedAlphaList();

    public abstract void setAlphabets(AbstractAlphabet[] alphas);

    public abstract void storeAlphabets() throws IOException;

    /**
     * 
     * @return the size of various alphabets stored
     */
    public abstract int getSize();

    public abstract AbstractAlphabet getAlphabetByShortName(String name);

}
