// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets.preferences;

import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;

/**
 * @author Simon Preference Set to convert a single TransformationData object to a saveable
 *         Preference Format
 */
public class TransformationPreferenceSet {
    // Will be the subnode's name
    private String preferenceName = "";

    private MyPreference selectedAlphabetName = new MyPreference(ID_ALPHA_NAME, "");
    private MyPreference doUppercase = new MyPreference(ID_DO_UPPERCASE, "true");
    private MyPreference uppercaseTransformationOn = new MyPreference(ID_UPPC_ON, "false");
    private MyPreference alphabetTransformationON = new MyPreference(ID_ALPH_ON, "false");
    private MyPreference umlautTransformationON = new MyPreference(ID_UML_ON, "false");
    private MyPreference leerTransformationON = new MyPreference(ID_LEER_ON, "false");

    public static final String ID_ALPHA_NAME = "selectedAlphabetName";
    public static final String ID_DO_UPPERCASE = "doUppercase";
    public static final String ID_UPPC_ON = "uppercaseTransformationOn";
    public static final String ID_ALPH_ON = "alphabetTransformationON";
    public static final String ID_UML_ON = "umlautTransformationON";
    public static final String ID_LEER_ON = "leerTransformationON";

    public static final int PREFERENCE_COUNT = 6;

    private String bool2String(boolean in) {
        if (in)
            return "true";
        else
            return "false";
    }

    private boolean String2Bool(String in) {
        if (in.equals("true"))
            return true;
        else
            return false;
    }

    /**
     * Creates a PrefernceSet from a TransformationData object.
     *
     * @param input The TransformationData to be converted into-
     * @param alphabetName the alphabet which standard transformation input is
     */
    public TransformationPreferenceSet(TransformData input, String alphabetName) {
        selectedAlphabetName = new MyPreference(ID_ALPHA_NAME, input.getSelectedAlphabetName());
        doUppercase = new MyPreference(ID_DO_UPPERCASE, bool2String(input.isDoUppercase()));
        uppercaseTransformationOn = new MyPreference(ID_UPPC_ON, bool2String(input
                .isUppercaseTransformationOn()));
        alphabetTransformationON = new MyPreference(ID_ALPH_ON, bool2String(input
                .isAlphabetTransformationON()));
        umlautTransformationON = new MyPreference(ID_UML_ON, bool2String(input
                .isUmlautTransformationON()));
        leerTransformationON = new MyPreference(ID_LEER_ON, bool2String(input
                .isLeerTransformationON()));

        this.setPreferenceName(alphabetName);
    }

    /**
     * @param id the preference ID (see class constants)
     * @param value the value to set
     */
    public void setPreference(String id, String value) {
        if (ID_ALPHA_NAME.equals(id))
            selectedAlphabetName.setVal(value);
        else if (ID_DO_UPPERCASE.equals(id))
            doUppercase.setVal(value);
        else if (ID_UPPC_ON.equals(id))
            uppercaseTransformationOn.setVal(value);
        else if (ID_ALPH_ON.equals(id))
            alphabetTransformationON.setVal(value);
        else if (ID_UML_ON.equals(id))
            umlautTransformationON.setVal(value);
        else if (ID_LEER_ON.equals(id))
            leerTransformationON.setVal(value);
    }

    /**
     * Returns the PreferenceSet as easy-saveable ID/Value pairs.
     *
     * @return a String[6][2]-object. The String[x][0]-value is the ID, the String[x][1]-value is
     *         the preference value.
     */
    public String[][] toStringArray() {
        String[][] output = new String[PREFERENCE_COUNT][2];
        output[0][0] = ID_ALPHA_NAME;
        output[1][0] = ID_DO_UPPERCASE;
        output[2][0] = ID_UPPC_ON;
        output[3][0] = ID_ALPH_ON;
        output[4][0] = ID_UML_ON;
        output[5][0] = ID_LEER_ON;

        output[0][1] = selectedAlphabetName.getVal();
        output[1][1] = doUppercase.getVal();
        output[2][1] = uppercaseTransformationOn.getVal();
        output[3][1] = alphabetTransformationON.getVal();
        output[4][1] = umlautTransformationON.getVal();
        output[5][1] = leerTransformationON.getVal();

        return output;
    }

    /**
     * converts a String[6][2]-Array into a TransformationPreferenceSet
     *
     * @param input a String[6][2]-object. The String[x][0]-value is the ID, the String[x][1]-value
     *        is the preference value.
     * @return The converted PreferenceSet
     */
    public static TransformationPreferenceSet fromStringArray(String[][] input) {
        TransformationPreferenceSet output = new TransformationPreferenceSet(new TransformData(),
                "");
        for (int i = 0; i < input.length; i++) {
            output.setPreference(input[i][0], input[i][1]);
        }

        return output;
    }

    /**
     * Returns the PreferenceSet as TransformData object.
     *
     * @return a String[6][2]-object. The String[x][0]-value is the ID, the String[x][1]-value is
     *         the preference value.
     */
    public TransformData toTransformData() {
        String pSelectedAlphabetName = this.selectedAlphabetName.getVal();
        boolean pDoUppercase = String2Bool(this.doUppercase.getVal());
        boolean pUppercaseTransformationOn = String2Bool(this.uppercaseTransformationOn.getVal());
        boolean pLeerTransformationON = String2Bool(this.leerTransformationON.getVal());
        boolean pAlphabetTransformationON = String2Bool(this.alphabetTransformationON.getVal());
        boolean pUmlautTransformationON = String2Bool(this.umlautTransformationON.getVal());
        return new TransformData(pSelectedAlphabetName, pDoUppercase, pUppercaseTransformationOn,
                pLeerTransformationON, pAlphabetTransformationON, pUmlautTransformationON);
    }

    public static TransformData getDefaultSetting(String alphabetName) {
        String[][] standards = new String[8][2];
        standards[0][0] = "Printable ASCII";
        standards[0][1] = "Printable ASCII|true|false|false|true|false";
        standards[1][0] = "Upper and lower Latin (A-Z,a-z)";
        standards[1][1] = "Upper and lower Latin (A-Z,a-z)|true|false|false|true|true";
        standards[2][0] = "Upper Latin (A-Z)";
        standards[2][1] = "Upper Latin (A-Z)|true|true|false|true|true";
        standards[3][0] = "Lower Latin (a-z)";
        standards[3][1] = "Lower Latin (a-z)|false|true|false|true|true";
        standards[4][0] = "Playfair/alike alphabet (25chars, w/o \"J\")";
        standards[4][1] = "Playfair/alike alphabet (25chars, w/o \"J\")|true|true|false|true|true";
        standards[5][0] = "ADFGVX Alphabet";
        standards[5][1] = "ADFGVX Alphabet|true|true|true|true|true";
        standards[6][0] = "Xor Alphabet with 32 characters";
        standards[6][1] = "Xor Alphabet with 32 characters|true|true|false|true|true";
        standards[7][0] = "Xor Alphabet with 64 characters	";
        standards[7][1] = "Xor Alphabet with 64 characters|true|false|false|true|true";

        for (int i = 0; i < standards.length; i++) {
            if (alphabetName.equals(standards[i][0])) {
                TransformationPreferenceSet mySet = TransformationPreferenceSet
                        .fromString(standards[i][1]);
                return mySet.toTransformData();
            }
        }

        return new TransformData();
    }

    /**
     * Returns the PreferenceSet with no values, but IDs.
     *
     * @return a String[6][2]-object. The String[x][0]-value is the ID, the String[x][1]-value is
     *         the preference value.
     */
    public static String[][] getTemplate() {
        String[][] output = new String[PREFERENCE_COUNT][2];
        output[0][0] = ID_ALPHA_NAME;
        output[1][0] = ID_DO_UPPERCASE;
        output[2][0] = ID_UPPC_ON;
        output[3][0] = ID_ALPH_ON;
        output[4][0] = ID_UML_ON;
        output[5][0] = ID_LEER_ON;

        output[0][1] = "";
        output[1][1] = "";
        output[2][1] = "";
        output[3][1] = "";
        output[4][1] = "";
        output[5][1] = "";

        return output;
    }

    public String toString() {
        String[][] test = this.toStringArray();
        return test[0][1] + "|" + test[1][1] + "|" + test[2][1] + "|" + test[3][1] + "|"
                + test[4][1] + "|" + test[5][1];
    }

    public static TransformationPreferenceSet fromString(String input) {
        String[] myInput = input.split("\\|");
        String[][] myTemplate = TransformationPreferenceSet.getTemplate();
        for (int i = 0; i < TransformationPreferenceSet.PREFERENCE_COUNT; i++) {
            myTemplate[i][1] = myInput[i];
        }
        return TransformationPreferenceSet.fromStringArray(myTemplate);
    }

    // String[] myKeyString = key.split("\\|");
    public static String implode(String[] ary, String delim) {
        StringBuffer out = new StringBuffer("");
        for (int i = 0; i < ary.length; i++) {
            if (i != 0) {
                out.append(delim);
            }
            out.append(ary[i]);
        }
        return out.toString();
    }

    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    public String getPreferenceName() {
        return preferenceName;
    }

}
