// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations.alphabets;

/**
 * Reference interface for management of alphabets in the alphabet manager.
 * 
 * @author Simon L
 */
public class AlphabetReference {

    public String getAlphabetName() {
        return alphabetName;
    }

    public void setAlphabetName(String alphabetName) {
        this.alphabetName = alphabetName;
    }

    public String getShortAlphabetName() {
        return shortAlphabetName;
    }

    public void setShortAlphabetName(String shortAlphabetName) {
        this.shortAlphabetName = shortAlphabetName;
    }

    public boolean isDefaultAlphabet() {
        return defaultAlphabet;
    }

    public void setDefaultAlphabet(boolean defaultAlphabet) {
        this.defaultAlphabet = defaultAlphabet;
    }

    public boolean isIntegral() {
        return integral;
    }

    public void setIntegral(boolean integral) {
        this.integral = integral;
    }

    public AbstractAlphabetStore2 getOriginStore() {
        return originStore;
    }

    String alphabetName;
    String shortAlphabetName;
    boolean defaultAlphabet;
    boolean integral;
    AbstractAlphabetStore2 originStore;

    public AlphabetReference(String alphabetName, String shortAlphabetName, boolean defaultAlphabet, boolean integral,
            AbstractAlphabetStore2 originStore) {
        super();
        this.alphabetName = alphabetName;
        this.shortAlphabetName = shortAlphabetName;
        this.defaultAlphabet = defaultAlphabet;
        this.integral = integral;
        this.originStore = originStore;
    }

}
