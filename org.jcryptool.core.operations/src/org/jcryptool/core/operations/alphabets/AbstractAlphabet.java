// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.alphabets;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract superclass for an Alphabet.
 *
 * @author t-kern
 *
 */
public abstract class AbstractAlphabet {

    public static final int NO_DISPLAY = 0;

    public static final int DISPLAY = 1;

    public static final int SUBSTITUTE = 2;

    public static final int HIGHLIGHT = 3;

    public List<Character> asList() {
        return alphaToList(this);
    }

    /**
     * @deprecated functionality to be moved to the AlphabetManager
     */
    public abstract boolean isDefaultAlphabet();

    /**
     * returns all characters included by the alphabet
     *
     * @since 0.01
     * @deprecated soon to be replaced by List<Character> getCharacterSet()
     */
    public abstract char[] getCharacterSet();

    /**
     * returns method missing characters are displayed by the alphabet
     *
     * @since 0.01
     * @deprecated functionality to be moved to the AlphabetManager
     */
    public abstract int getDisplayMissingCharacters();

    /**
     * returns the name of the alphabet
     *
     * @since 0.01
     * @deprecated functionality to be moved to the AlphabetManager
     */
    public abstract String getName();

    /**
     * @return the short name
     * @deprecated functionality to be moved to the AlphabetManager
     */
    public abstract String getShortName();

    /**
     * returns the character which is show as placeholder for characters which are not in the alphabet
     *
     * @since 0.01
     * @deprecated functionality to be removed
     */
    public abstract char getSubstituteCharacter();

    /**
     * @deprecated functionality to be moved to the AlphabetManager
     */
    public abstract void setDefaultAlphabet(boolean b);

    /**
     * @deprecated alphabets will be immutable
     */
    public abstract void setCharacterSet(char[] characterSet);

    /**
     * @deprecated functionality to be moved to the AlphabetManager
     */
    public abstract boolean isBasic();

    /**
     * @deprecated functionality to be moved to the AlphabetManager
     */
    public abstract void setBasic(boolean basic);

    /**
     * @deprecated functionality to be moved to the AlphabetManager
     */
    public abstract void setName(String name);

    /**
     * @deprecated functionality to be moved to the AlphabetManager
     */
    public abstract void setShortName(String shortName);

    public abstract boolean contains(char e);

    private static Map<Character, String> specialCharactersForPrinting = new HashMap<Character, String>();
    static {
        specialCharactersForPrinting.put('\n', "\\n"); //$NON-NLS-1$
        specialCharactersForPrinting.put('\r', "\\r"); //$NON-NLS-1$
        specialCharactersForPrinting.put('\t', "\\t"); //$NON-NLS-1$
    }

    /**
     * converts an alphabet's content to a String. This makes sure that linebreaks etc. are shown as "\n" etc.<br />
     *
     *
     * @param alpha the alphabet
     * @return a string containing a representation of every character in the alphabet
     */
    public static String alphabetContentAsString(char[] alphaContent) {
        StringBuilder sb = new StringBuilder();

        for (char c : alphaContent) {
            String charRep = getPrintableCharRepresentation(c);
            sb.append(charRep);
        }

        return sb.toString();
    }

    public static String getPrintableCharRepresentation(char c) {
        if (specialCharactersForPrinting.containsKey(Character.valueOf(c))) {
            return specialCharactersForPrinting.get(Character.valueOf(c));
            // } else if(!isCharacterOnKeyboard(c)) {
        } else if ((int) c < 32) {
            return "{" + String.valueOf((int) c) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            return String.valueOf(c);
        }
    }

    /**
     * pendant to {@link #alphabetContentAsString(AbstractAlphabet)}
     */
    public static char[] parseAlphaContentFromString(String alpha) {
        Pattern nonprintables;
        nonprintables = Pattern.compile("\\{\\d+}", Pattern.DOTALL); //$NON-NLS-1$
        String newAlpha = alpha;

        Matcher m = nonprintables.matcher(alpha);

        while (m.find()) {
            int number = Integer.valueOf(newAlpha.substring(m.start() + 1, m.end() - 1));
            newAlpha = newAlpha.replace(m.group(), String.valueOf((char) number));
            m = nonprintables.matcher(newAlpha);
        }

        for (Entry<Character, String> replacement : specialCharactersForPrinting.entrySet()) {
            newAlpha = newAlpha.replace(replacement.getValue(), String.valueOf(replacement.getKey()));
        }

        // TODO!: delete doublets

        return newAlpha.toCharArray();
    }

    public static List<Character> alphaToList(AbstractAlphabet alpha) {
        List<Character> result = new LinkedList<Character>();
        for (char c : alpha.getCharacterSet())
            result.add(c);
        return result;
    }

    public static AbstractAlphabet getMostSimilarAlphaTo(AbstractAlphabet alpha, List<AbstractAlphabet> compareBase) {
        LinkedList<List<Character>> alphas = new LinkedList<List<Character>>();
        for (AbstractAlphabet a : compareBase)
            alphas.add(alphaToList(a));
        List<Character> best = getMostSimilarAlphaTo(alphaToList(alpha), alphas);
        if (best != null) {
            int index = alphas.indexOf(best);
            return compareBase.get(index);
        } else {
            return null;
        }
    }

    public static List<Character> getMostSimilarAlphaTo(List<Character> alpha, List<List<Character>> compareBase) {
        double bestScore = Double.MIN_VALUE;
        List<Character> bestScoredAlpha = null;
        for (List<Character> compareBaseElement : compareBase) {
            double score = compareTwoAlphabets(alpha, compareBaseElement);
            if (score > bestScore) {
                bestScore = score;
                bestScoredAlpha = compareBaseElement;
            }
        }
        return bestScoredAlpha;
    }

    public static double compareTwoAlphabets(AbstractAlphabet a1, AbstractAlphabet a2) {
        return compareTwoAlphabets(alphaToList(a1), alphaToList(a2));
    }

    public static double compareTwoAlphabets(Collection<? extends Character> remoteCharacters,
            Collection<? extends Character> thisCharacters) {
        Set<Character> disjunction = calcAlphaDisjunction(remoteCharacters, thisCharacters);
        Set<Character> conjunction = calcAlphaConjunction(remoteCharacters, thisCharacters);

        if (disjunction.size() == 0)
            return 0;
        return (double) (conjunction.size()) / (double) (disjunction.size());
    }

    public static Set<Character> calcAlphaConjunction(Collection<? extends Character> remoteCharacters,
            Collection<? extends Character> thisCharacters) {
        Set<Character> conjunction = new LinkedHashSet<Character>();
        for (Character c : thisCharacters) {
            if (remoteCharacters.contains(c)) {
                conjunction.add(c);
            }
        }
        return conjunction;
    }

    public static Set<Character> calcAlphaDisjunction(Collection<? extends Character> remoteCharacters,
            Collection<? extends Character> thisCharacters) {
        Set<Character> disjunction = new LinkedHashSet<Character>();
        disjunction.addAll(thisCharacters);
        disjunction.addAll(remoteCharacters);
        return disjunction;
    }

}
