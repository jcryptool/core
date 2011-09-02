//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm.classic.textmodify;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphaConverter;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;

/**
 * Transformation calculation class - Executes all the transformations based upon the Wizard setting
 * class. Can be used to execute single transformations or custom transformation sets.
 *
 * @author SLeischnig
 * @version 0.9.2
 */
public class Transform {
    private TransformData myTransform;

    /**
     * Transforms a text based upon the <b>already set</b> Wizard transformation Data.
     *
     * @param text the text to be transformed
     * @return the transformed text
     * @throws Exception
     */
    public static String transformText(String text, TransformData myTransformationData) {
        String myText = text;
        if (myTransformationData.isLeerTransformationON())
            myText = leerTransformation(myText);
        if (myTransformationData.isUmlautTransformationON())
            myText = umlautTransformation(myText);
        if (myTransformationData.isUppercaseTransformationOn())
            myText = uppercaseTransformation(myText, myTransformationData.isDoUppercase());
        if (myTransformationData.isAlphabetTransformationON())
            try {
                myText = alphabetTransformation(myText, myTransformationData.getSelectedAlphabetName());
            } catch (Exception e) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
            }

        return myText;
    }

    /**
     * Transforms a text based upon a custom Transformation set.
     *
     * @param text the text to be transformed
     * @param myTransformationData the custom transformation data
     * @return the transformed text
     */
    public String transformText(String text) {
        String myText = ""; //$NON-NLS-1$
        try {
            myText = transformText(text, myTransform);
        } catch (Exception ex) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, ex);
        }

        return myText;
    }

    /**
     * Single filter-by-alphabet-transformation
     *
     * @param text the text to be transformed
     * @param alphabetName the alphabet's name
     * @return the transformed text
     * @throws Exception
     */
    public static String alphabetTransformation(String text, String alphabetName) throws Exception {
        AbstractAlphabet myAlphabet;
        AlphaConverter myAC;
        AbstractAlphabet[] alphas = AlphabetsManager.getInstance().getAlphabets();
        alphas = AlphabetsManager.getInstance().getAlphabets();
        myAlphabet = AlphabetsManager.getInstance().getDefaultAlphabet();
        for (int i = 0; i < alphas.length; i++) {
            if (alphas[i].getName().equals(alphabetName)) {
                myAlphabet = alphas[i];
            }
        }

        myAC = new AlphaConverter(myAlphabet.getCharacterSet());

        char[] myCharArray = text.toCharArray();
        return String.valueOf(myAC.filterNonAlphaChars(myCharArray));
    }

    /**
     * Single transform-to-UPPER/lowercase-transformation
     *
     * @param text the text to be transformed
     * @param uppercase true: to UPPERCASE, false: to lowercase
     * @return the transformed text
     */
    public static String uppercaseTransformation(String text, boolean uppercase) {
        if (uppercase)
            return text.toUpperCase();
        else
            return text.toLowerCase();
    }

    /**
     * Single remove-blanks-transformation
     *
     * @param text the text to be transformed
     * @return the transformed text
     */
    public static String leerTransformation(String text) {
        return text.replaceAll("\\s", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Single remove-umlauts-transformation.
     *
     * @param text the text to be transformed
     * @return the transformed text
     */
    public static String umlautTransformation(String text) {
        String text2;
        String replaceThis = "Ä"; //$NON-NLS-1$
        String withThis = "AE"; //$NON-NLS-1$
        text2 = text.replaceAll(replaceThis, withThis); //
        replaceThis = "ä"; //$NON-NLS-1$
        withThis = "ae"; //$NON-NLS-1$
        text2 = text2.replace(replaceThis, withThis); //
        replaceThis = "Ö"; //$NON-NLS-1$
        withThis = "OE"; //$NON-NLS-1$
        text2 = text2.replace(replaceThis, withThis); //
        replaceThis = "ö"; //$NON-NLS-1$
        withThis = "oe"; //$NON-NLS-1$
        text2 = text2.replace(replaceThis, withThis); //
        replaceThis = "Ü"; //$NON-NLS-1$
        withThis = "UE"; //$NON-NLS-1$
        text2 = text2.replace(replaceThis, withThis); //
        replaceThis = "ü"; //$NON-NLS-1$
        withThis = "ue"; //$NON-NLS-1$
        text2 = text2.replace(replaceThis, withThis); //
        if (text.toUpperCase().equals(text)) { // For not writing two small caps 's' into a text full of capitals
            replaceThis = "ß"; //$NON-NLS-1$
            withThis = "SS"; //$NON-NLS-1$
            text2 = text2.replace(replaceThis, withThis); //
        } else if (text2.toUpperCase().equals(text2)) {
            replaceThis = "ß"; //$NON-NLS-1$
            withThis = "ss"; //$NON-NLS-1$
            text2 = text2.replace(replaceThis, withThis); //
        }

        return text2;
    }

    /**
     * Sets the transformation-describing record
     *
     * @param myTransform
     */
    public void setMyTransform(TransformData myTransform) {
        this.myTransform = myTransform;
    }

    /**
     * @return the transformation-describing record
     */
    public TransformData getMyTransform() {
        return myTransform;
    }
}
