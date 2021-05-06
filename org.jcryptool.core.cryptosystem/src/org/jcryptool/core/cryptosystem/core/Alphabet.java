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
package org.jcryptool.core.cryptosystem.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Models alphabets with a finite number of elements which are maintained in a specific order (stored as a list).
 * 
 * @param <C> the boundary generic type for elements contained in this alphabet
 * 
 * @author Simon L
 */
public class Alphabet<C> {

    private List<C> alphabetElements;

    /**
     * Creates an alphabet by specifying its elements. The order in which the iterator of the collection returns the
     * elements will be maintained.
     * 
     * @param alphabetElements the alphabet elements.
     * @throws IllegalArgumentException if the collection is null or if the collection is empty
     */
    public Alphabet(Collection<? extends C> alphabetContent) {
        if (alphabetContent != null) {
            if (alphabetContent.size() > 0) {
                this.alphabetElements = new LinkedList<C>(alphabetContent);
            } else {
                throw new IllegalArgumentException("The alphabet collection may not be empty"); //$NON-NLS-1$
            }
        } else {
            throw new IllegalArgumentException("The alphabet collection may not be null"); //$NON-NLS-1$
        }
    }

    /**
     * @see #Alphabet(Collection)
     */
    public Alphabet(C[] alphabetContent) {
        this(Arrays.asList(alphabetContent));
    }

    /**
     * @return the elements of the alphabet
     */
    public List<C> getContent() {
        return alphabetElements;
    }

    /**
     * Filters non-alphabet content from a list (alters the given list!).
     * 
     * @param toFilter the list to filter
     */
    public void filterByAlphabet(List<C> toFilter) {
        for (Iterator<C> iterator = toFilter.iterator(); iterator.hasNext();) {
            C element = iterator.next();
            if (!getContent().contains(element)) {
                iterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (C element : alphabetElements) {
            String prefix = (alphabetElements.indexOf(element) > 0) ? ((element instanceof Character) ? "" : ",") : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            builder.append(prefix.concat(element.toString()));
        }

        return builder.toString();
    }

    /**
     * returns true, if every element that is contained in this alphabet is contained in the other alphabet, too.
     * 
     * @param otherAlpha the other alphabet
     * @return whether the set of elements of this alphabet is a subset of the other alphabet's element set.
     */
    public boolean isSubsetOf(Alphabet<? extends C> otherAlpha) {
        for (C thisAlphaElem : getContent()) {
            if (otherAlpha.getContent().contains(thisAlphaElem)) {
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * returns true, if this and the other alphabet have exactly the same elements sotred. (alphabets can be permuted,
     * still, thus not be equal)
     * 
     * @param otherAlpha the other alphabet
     */
    public boolean isSetEqualTo(Alphabet<? extends C> otherAlpha) {
        return isSubsetOf(otherAlpha) && otherAlpha.getContent().size() == this.getContent().size();
    }

    private static <EType> List<EType> removeDoubles(Collection<EType> coll) {
        List<EType> found = new ArrayList<EType>();
        for (Iterator<EType> iterator = coll.iterator(); iterator.hasNext();) {
            EType collElem = iterator.next();
            if (!found.contains(collElem)) {
                found.add(collElem);
            }
        }

        return found;
    }

    public static <EType> List<EType> createFilledAlphabetlistFromKeyword(List<EType> keyword,
            Alphabet<? extends EType> plainTextAlphabet) {
        // remove doublets
        List<EType> result = removeDoubles(keyword);
        // fill subsequently with alphabet elements
        for (EType alphaElem : plainTextAlphabet.getContent()) {
            if (!result.contains(alphaElem)) {
                result.add(alphaElem);
            }
        }

        return result;
    }

    public static <EType> Alphabet<EType> createFilledAlphabetFromKeyword(List<EType> keyword,
            Alphabet<? extends EType> plainTextAlphabet) {
        return new Alphabet<EType>(createFilledAlphabetlistFromKeyword(keyword, plainTextAlphabet));
    }

}
