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

public class GenericAlphabet extends AbstractAlphabet {

    private String name;
    private String shortName;
    private char[] content;
    private boolean isBasic;

    public GenericAlphabet(String name, String shortName, char[] content, boolean isBasic) {
        this.name = name;
        this.shortName = shortName;
        this.content = content;
        this.isBasic = isBasic;
    }

    @Override
    public boolean isDefaultAlphabet() {
        return false;
    }

    @Override
    public char[] getCharacterSet() {
        return content;
    }

    @Override
    public int getDisplayMissingCharacters() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public char getSubstituteCharacter() {
        return 0;
    }

    @Override
    public void setDefaultAlphabet(boolean b) {
    }

    @Override
    public void setCharacterSet(char[] characterSet) {
        content = characterSet;
    }

    @Override
    public boolean isBasic() {
        return isBasic;
    }

    @Override
    public void setBasic(boolean basic) {
        isBasic = basic;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public boolean contains(char e) {
        for (char c : content) {
            if (e == c)
                return true;
        }
        return false;
    }

}
