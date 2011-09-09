// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.adfgvx.algorithm;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AlphaConverter;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.crypto.classic.adfgvx.AdfgvxPlugin;

public class AdfgvxAlgorithm extends AbstractClassicAlgorithm {

    public static final AdfgvxAlgorithmSpecification specification = new AdfgvxAlgorithmSpecification();

    public AdfgvxAlgorithm() {
        engine = new AdfgvxEngine();
    }

    @Override
    protected int[] generateKey(char[] keyData) {
        for (int i = 0; i < keyData.length; i++) {
            if (Character.isLetter(keyData[i]))
                keyData[i] = Character.toUpperCase(keyData[i]);
        }

        try {
            return (alphaConv.charArrayToIntArray(alphaConv.filterNonAlphaChars(keyData)));
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while filtering an currentAlphabet", e, true); //$NON-NLS-1$
        }
        return null;

    }

    private InputStream filterStreamByTransformData(InputStream in, TransformData filter) throws Exception {
        if (filter == null) {
            return in;
        }

        String filterString = InputStreamToString(in);
        String filteredString = Transform.transformText(filterString, filter);

        return StringToInputStream(filteredString);
    }

    private InputStream StringToInputStream(String in) {
        byte[] bytes = null;
        try {
            bytes = in.getBytes(IConstants.UTF8_ENCODING);
        } catch (UnsupportedEncodingException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
        }
        return new ByteArrayInputStream(bytes);
    }

    private String InputStreamToString(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e1) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e1);
        }

        StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }

    public IDataObject execute() {
        // 1st prepare currentAlphabet table, every char is associated has an int value
        this.alphaConv = new AlphaConverter(this.dataObject.getAlphabet().getCharacterSet());
        // 2nd key
        this.key = generateKey(this.dataObject.getKey());
        this.keyChars = this.dataObject.getKey();

        try {
            // temporary only -- see tracker artifact #3071244
            this.is =
                    new BufferedInputStream(filterStreamByTransformData(this.dataObject.getInputStream(),
                            new TransformData("ADFGVX Alphabet", true, true, true, true, true)));
        } catch (Exception e) {
            LogUtil.logError(e);
        }

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        this.dataObject.setOutputStream(bout);

        int read;
        StringBuilder sb;
        try {
            sb = new StringBuilder(is.available());
            while ((read = is.read()) != -1) {
                sb.append((char) read);
            }
        } catch (IOException e) {
            LogUtil.logError(e);
            return null;
        }

        try {
            if (dataObject.getOpmode() == ENCRYPT_MODE) {
                bout.write(toByteArray(encrypt(sb.toString().toCharArray())));
            } else {
                bout.write(toByteArray(decrypt(sb.toString().toCharArray())));
            }
        } catch (IOException e) {
            LogUtil.logError(e);
        }
        return dataObject;
    }

    /**
     * Encryption method
     *
     * @param cipherInput the to be processed input
     * @return the cipher output as a char array
     */
    private char[] encrypt(char[] cipherInput) {

        // convert cipher input to an int array by using the currentAlphabet
        int[] input = alphaConv.charArrayToIntArray(cipherInput);

        // get length of currentAlphabet
        int alphaLength = alphaConv.getAlphaLength();

        // currentAlphabet as int array
        int[] alphabet = alphaConv.charArrayToIntArray(alphaConv.getAlpha());
        // Character used as null value
        char nullchar = dataObject.getNullchar();
        // currentAlphabet as char array
        char[] alphaChars = null;
        // second key, used by some algorithms, e.g. adfgvx
        char[] key2 = dataObject.getKey2();
        try {
            alphaChars = alphaConv.filterNonAlphaChars(dataObject.getAlphabet().getCharacterSet());
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while filtering an currentAlphabet", e, false); //$NON-NLS-1$
        }
        char[] inputNoNonAlphaChar = null;
        try {
            inputNoNonAlphaChar = alphaConv.filterNonAlphaChars(cipherInput);
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while filtering an currentAlphabet", e, false); //$NON-NLS-1$
        }
        this.keyChars = alphaConv.intArrayToCharArray(key);
        // process encryption
        int[] cipher =
                engine.doEncryption(input, key, alphaLength, alphabet, nullchar, alphaChars, keyChars,
                        inputNoNonAlphaChar, this.alphaConv, key2, 0);

        // convert cipher int to to char and return
        return alphaConv.intArrayToCharArray(cipher);

    }

    /**
     * Decryption method
     *
     * @param cipherInput the to be processed input
     * @return the cipher output as a char array
     */
    private char[] decrypt(char[] cipherInput) {

        // convert cipher input to an int array by using the currentAlphabet
        int[] input = alphaConv.charArrayToIntArray(cipherInput);

        // get length of currentAlphabet
        int alphaLength = alphaConv.getAlphaLength();

        // currentAlphabet as int array
        int[] alphabet = alphaConv.charArrayToIntArray(alphaConv.getAlpha());
        // Character used as null value
        char nullchar = dataObject.getNullchar();
        // second key, used by some algorithms, e.g. adfgvx
        char[] key2 = dataObject.getKey2();
        // currentAlphabet as char array
        char[] alphaChars = null;
        try {
            alphaChars = alphaConv.filterNonAlphaChars(dataObject.getAlphabet().getCharacterSet());
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while filtering an currentAlphabet", e, true); //$NON-NLS-1$
        }
        this.keyChars = alphaConv.intArrayToCharArray(key);
        char[] inputNoNonAlphaChar = null;
        try {
            inputNoNonAlphaChar = alphaConv.filterNonAlphaChars(cipherInput);
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while filtering an currentAlphabet", e, false); //$NON-NLS-1$
        }
        // process encryption
        int[] cipher =
                engine.doDecryption(input, key, alphaLength, alphabet, nullchar, alphaChars, keyChars,
                        inputNoNonAlphaChar, this.alphaConv, key2, 0);

        // convert cipher int to to char and return
        return alphaConv.intArrayToCharArray(cipher);
    }

    @Override
    public String getAlgorithmName() {
        return "ADFGVX"; //$NON-NLS-1$
    }

}
