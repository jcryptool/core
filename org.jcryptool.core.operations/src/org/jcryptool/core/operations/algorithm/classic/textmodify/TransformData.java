// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm.classic.textmodify;

import java.util.regex.Pattern;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.alphabets.GenericAlphabet;

/**
 * saving the transformation wizard settings / configuring the transformation class
 * 
 * @author SLeischnig
 * @version 0.6.0
 */
public class TransformData {
    private static final String NONSTORE_ALPHABET_STARTMARKER = "Alphabet:"; //$NON-NLS-1$
    private static final String STORE_ALPHABET_STARTMARKER = "StoreAlphabet:"; //$NON-NLS-1$
    private static final String INTER_ALPHA_SEPARATOR = "{inter-alpha separator}"; //$NON-NLS-1$
    private static final String SEPARATOR_REPLACEMENT = "{separator replacement}"; //$NON-NLS-1$
    private AbstractAlphabet selectedAlphabet; //$NON-NLS-1$
    private boolean doUppercase = true;
    private boolean uppercaseTransformationOn = true;
    private boolean alphabetTransformationON = false;
    private boolean umlautTransformationON = true;
    private boolean leerTransformationON = true;

    public static final String UPPERLOWERCASE_LABEL = "upper/lowercase"; //$NON-NLS-1$
    public static final String ALPHABET_LABEL = "filterByAlphabet"; //$NON-NLS-1$
    public static final String UMLAUTS_LABEL = "filterUmlauts"; //$NON-NLS-1$
    public static final String BLANKS_LABEL = "filterBlanks"; //$NON-NLS-1$
    private static final String UPPERLOWERCASE_LABEL_R = Messages.TransformData_0;
    private static final String ALPHABET_LABEL_R = Messages.TransformData_1;
    private static final String UMLAUTS_LABEL_R = Messages.TransformData_2;
    private static final String BLANKS_LABEL_R = Messages.TransformData_3;
    private static final String SEPARATOR = ", "; //$NON-NLS-1$

    /**
     * Creates the class, setting all variables such, that a Transformation with these parameters would leave the Text
     * unmodified.
     */
    public TransformData() {
        setUnmodified();
    }

    /**
     * @param pSelectedAlphabet the filtering alphabet
     * @param pDoUppercase true: UPPERCASE. false: lowercase.
     * @param pUppercaseTransformationON whether upper/lowercase transformation will be applied.
     * @param pLeerTransformationON whether blanks removal transformation will be applied.
     * @param pAlphabetTransformationON whether filter-characters-by-alphabet transformation will be applied.
     * @param pUmlautTransformationON whether umlauts-replacing transformation will be applied.
     */
    public TransformData(final AbstractAlphabet pSelectedAlphabet, final boolean pDoUppercase,
            final boolean pUppercaseTransformationON, final boolean pLeerTransformationON,
            final boolean pAlphabetTransformationON, final boolean pUmlautTransformationON) {
        setSelectedAlphabet(pSelectedAlphabet);
        setDoUppercase(pDoUppercase);
        setUppercaseTransformationOn(pUppercaseTransformationON);
        setAlphabetTransformationON(pAlphabetTransformationON);
        setUmlautTransformationON(pUmlautTransformationON);
        setLeerTransformationON(pLeerTransformationON);
    }

    public final void setSelectedAlphabet(final AbstractAlphabet selectedAlphabet) {
        this.selectedAlphabet = selectedAlphabet;
    }

    public final AbstractAlphabet getSelectedAlphabet() {
        return selectedAlphabet;
    }

    public final void setDoUppercase(final boolean doUppercase) {
        this.doUppercase = doUppercase;
    }

    public final boolean isDoUppercase() {
        return doUppercase;
    }

    public final void setUppercaseTransformationOn(final boolean uppercaseTransformationOn) {
        this.uppercaseTransformationOn = uppercaseTransformationOn;
    }

    public final boolean isUppercaseTransformationOn() {
        return uppercaseTransformationOn;
    }

    public final void setAlphabetTransformationON(final boolean alphabetTransformationON) {
        this.alphabetTransformationON = alphabetTransformationON;
    }

    public final boolean isAlphabetTransformationON() {
        return alphabetTransformationON;
    }

    public final void setUmlautTransformationON(final boolean umlautTransformationON) {
        this.umlautTransformationON = umlautTransformationON;
    }

    public final boolean isUmlautTransformationON() {
        return umlautTransformationON;
    }

    public final void setLeerTransformationON(final boolean leerTransformationON) {
        this.leerTransformationON = leerTransformationON;
    }

    public final boolean isLeerTransformationON() {
        return leerTransformationON;
    }

    /**
     * Sets all parameters such, that a transformation with them would leave a text unmodified.
     */
    public final void setUnmodified() {
        selectedAlphabet = getDefaultFilterAlphabet(); //$NON-NLS-1$
        doUppercase = true;
        uppercaseTransformationOn = false;
        alphabetTransformationON = false;
        umlautTransformationON = false;
        leerTransformationON = false;
    }

    private static AbstractAlphabet getDefaultFilterAlphabet() {
        if (OperationsPlugin.getDefault() != null) {
            AlphabetsManager instance = AlphabetsManager.getInstance();
            return instance.getDefaultAlphabet();
        }
        return null;
    }

    public boolean isUnmodified() {
        if (!doUppercase) {
            return false;
        }
        if (uppercaseTransformationOn) {
            return false;
        }
        if (alphabetTransformationON) {
            return false;
        }
        if (umlautTransformationON) {
            return false;
        }
        if (leerTransformationON) {
            return false;
        }
        return true;
    }

    /**
     * parses a Transformdata object from a String that was created using TransformData.toString();
     * 
     * @return a TransformData instance
     */
    public static TransformData fromString(String data) {
        TransformData result = new TransformData();
        try {
            String[] split = data.split(SEPARATOR);
            for (int i = 0; i < split.length; i++) {
                if (split[i].contains(UPPERLOWERCASE_LABEL)) {
                    result.setUppercaseTransformationOn(true);
                    String value = split[i].substring(split[i].indexOf("=") + 1); //$NON-NLS-1$

                    result.setDoUppercase(true);
                    if (value.equals("lowercase")) { //$NON-NLS-1$
                        result.setDoUppercase(false);
                    }
                }

                if (split[i].contains(ALPHABET_LABEL)) {
                    result.setAlphabetTransformationON(true);
                    String value = split[i].substring(split[i].indexOf("=") + 1); //$NON-NLS-1$

                    value = value.replaceAll(Pattern.quote(SEPARATOR_REPLACEMENT), SEPARATOR);
                    AbstractAlphabet alpha;
                    if (isAlphaStringStoreReference(value)) {
                        alpha = alphaStoreReferenceStringToAlpha(value);
                    } else {
                        alpha = stringToAlpha(value);
                    }

                    result.setSelectedAlphabet(alpha);
                }

                if (split[i].contains(BLANKS_LABEL)) {
                    result.setLeerTransformationON(true);
                }

                if (split[i].contains(UMLAUTS_LABEL)) {
                    result.setUmlautTransformationON(true);
                }
            }
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Error when parsing TransformData from String"); //$NON-NLS-1$
        }

        return result;
    }

    public String toReadableString() {
        StringBuilder result = new StringBuilder(""); //$NON-NLS-1$

        if (uppercaseTransformationOn) {
            String value = Messages.TransformData_4;
            String separator = SEPARATOR;
            if (doUppercase)
                value = Messages.TransformData_5;
            if (result.toString().equals("")) //$NON-NLS-1$
                separator = ""; //$NON-NLS-1$

            result.append(separator + UPPERLOWERCASE_LABEL_R + "" + value); //$NON-NLS-1$
        }

        if (alphabetTransformationON) {
            String alphaAsString;
            // reference alphabets by name from the alphabets
            if (isAlphaInAlphabetStore(selectedAlphabet)) {
                alphaAsString = alphaToAlphaStoreReferenceString(selectedAlphabet);
            } else {
                alphaAsString = alphaToString(selectedAlphabet);
            }

            String value = alphaAsString;
            String separator = SEPARATOR;
            if (result.toString().equals("")) //$NON-NLS-1$
                separator = ""; //$NON-NLS-1$

            result.append(separator + ALPHABET_LABEL_R + ": " + value); //$NON-NLS-1$
        }

        if (leerTransformationON) {
            String separator = SEPARATOR;
            if (result.toString().equals("")) //$NON-NLS-1$
                separator = ""; //$NON-NLS-1$

            result.append(separator + BLANKS_LABEL_R); //$NON-NLS-1$
        }

        if (umlautTransformationON) {
            String separator = SEPARATOR;
            if (result.toString().equals("")) //$NON-NLS-1$
                separator = ""; //$NON-NLS-1$

            result.append(separator + UMLAUTS_LABEL_R); //$NON-NLS-1$
        }

        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(""); //$NON-NLS-1$

        if (uppercaseTransformationOn) {
            String value = "lowercase"; //$NON-NLS-1$
            String separator = SEPARATOR;
            if (doUppercase)
                value = "uppercase"; //$NON-NLS-1$
            if (result.toString().equals("")) //$NON-NLS-1$
                separator = ""; //$NON-NLS-1$

            result.append(separator + UPPERLOWERCASE_LABEL + "=" + value); //$NON-NLS-1$
        }

        if (alphabetTransformationON) {
            String alphaAsString;
            if (isAlphaInAlphabetStore(selectedAlphabet)) {
                alphaAsString = alphaToAlphaStoreReferenceString(selectedAlphabet);
            } else {
                alphaAsString = alphaToString(selectedAlphabet);
            }

            String value = alphaAsString.replaceAll(Pattern.quote(SEPARATOR), SEPARATOR_REPLACEMENT);
            String separator = SEPARATOR;
            if (result.toString().equals("")) //$NON-NLS-1$
                separator = ""; //$NON-NLS-1$

            result.append(separator + ALPHABET_LABEL + "=" + value); //$NON-NLS-1$
        }

        if (leerTransformationON) {
            String value = "on"; //$NON-NLS-1$
            String separator = SEPARATOR;
            if (result.toString().equals("")) //$NON-NLS-1$
                separator = ""; //$NON-NLS-1$

            result.append(separator + BLANKS_LABEL + "=" + value); //$NON-NLS-1$
        }

        if (umlautTransformationON) {
            String value = "on"; //$NON-NLS-1$
            String separator = SEPARATOR;
            if (result.toString().equals("")) //$NON-NLS-1$
                separator = ""; //$NON-NLS-1$

            result.append(separator + UMLAUTS_LABEL + "=" + value); //$NON-NLS-1$
        }

        return result.toString();
    }

    private static AbstractAlphabet stringToAlpha(String value) {
        String contentString;
        String nameString;
        String shortnameString;
        String isbasicString;
        GenericAlphabet result = null;

        try {
            String[] split = value.substring(NONSTORE_ALPHABET_STARTMARKER.length()).split(
                    Pattern.quote(INTER_ALPHA_SEPARATOR));
            contentString = split[0];
            nameString = split[1];
            shortnameString = split[2];
            isbasicString = split[3];

            result = new GenericAlphabet(nameString, shortnameString,
                    AbstractAlphabet.parseAlphaContentFromString(contentString), Boolean.parseBoolean(isbasicString));
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID,
                    "Error when trying to parse alphabet from string for transformData: " + value); //$NON-NLS-1$
            return null;
        }

        return result;
    }

    /**
     * Returns an alphabet from the store which is referenced in a string by "STORE_ALPHABET_STARTMARKER:[name]"
     * 
     * @param value the reference string
     * @return null if not found in the store, else the store alphabet
     */
    private static AbstractAlphabet alphaStoreReferenceStringToAlpha(String value) {
        String name = value.substring(STORE_ALPHABET_STARTMARKER.length());
        AbstractAlphabet alpha = AlphabetsManager.getInstance().getAlphabetByName(name);
        if (alpha == null) {
            LogUtil.logWarning(
                    OperationsPlugin.PLUGIN_ID,
                    "could not load alphabet by name " + name + " for transformData. Using the default transformation instead."); //$NON-NLS-1$ //$NON-NLS-2$
            return AlphabetsManager.getInstance().getDefaultAlphabet();
        }
        return alpha;
    }

    private static boolean isAlphaStringStoreReference(String value) {
        return value.startsWith(STORE_ALPHABET_STARTMARKER);
    }

    private static String alphaToString(AbstractAlphabet alpha) {
        StringBuilder b = new StringBuilder();
        b.append(NONSTORE_ALPHABET_STARTMARKER);
        b.append(AbstractAlphabet.alphabetContentAsString(alpha.getCharacterSet()));
        b.append(INTER_ALPHA_SEPARATOR);
        b.append(alpha.getName());
        b.append(INTER_ALPHA_SEPARATOR);
        b.append(alpha.getShortName());
        b.append(INTER_ALPHA_SEPARATOR);
        b.append(Boolean.valueOf(alpha.isBasic()).toString());
        return b.toString();
    }

    private static String alphaToAlphaStoreReferenceString(AbstractAlphabet alpha) {
        return STORE_ALPHABET_STARTMARKER + alpha.getName();
    }

    private static boolean isAlphaInAlphabetStore(AbstractAlphabet alpha) {
        AbstractAlphabet[] alphas = AlphabetsManager.getInstance().getAlphabets();
        for (AbstractAlphabet a : alphas) {
            if (a != null && (a == alpha || a.equals(alpha))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected TransformData clone() throws CloneNotSupportedException {
        TransformData clone = new TransformData();
        clone.selectedAlphabet = selectedAlphabet;
        clone.doUppercase = doUppercase;
        clone.uppercaseTransformationOn = uppercaseTransformationOn;
        clone.alphabetTransformationON = alphabetTransformationON;
        clone.umlautTransformationON = umlautTransformationON;
        clone.leerTransformationON = leerTransformationON;
        return clone;
    }

}
