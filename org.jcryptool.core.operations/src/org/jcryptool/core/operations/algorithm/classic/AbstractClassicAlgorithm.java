// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm.classic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithm;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphaConverter;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.core.util.constants.IConstants;

/**
 * The base class for a classic algorithm.
 * 
 * @author Amro
 * @author Dominik Schadow
 * @version 0.9.2
 */
public abstract class AbstractClassicAlgorithm extends AbstractAlgorithm {
    public class CipherTextMask {

    	private Map<Integer, Character> maskMap;
    	private char[] nonmasked;
		private boolean dontReconstruct = false;
    	
    	public CipherTextMask(char[] charInput, AbstractAlphabet alphabet) {
			this.maskMap = new LinkedHashMap<>();
			List<Character> passed = new ArrayList<>();
			for (int i = 0; i < charInput.length; i++) {
				Character c = charInput[i];
				if(! alphabet.contains(c)) {
					this.maskMap.put(i, c);
				} else {
					passed.add(c);
				}
			}
			nonmasked = new char[passed.size()];
			for (int i = 0; i < passed.size(); i++) {
				Character c = passed.get(i);
				nonmasked[i] = c;
			}
		}

		public char[] readIntoMask(char[] fillIn) {
			StringBuilder b = new StringBuilder();
			LinkedHashMap<Integer, Character> mask = this.dontReconstruct ? new LinkedHashMap<>() : new LinkedHashMap<>(maskMap);
			
			int filledInCounter = 0;
			for (int i = 0; i < fillIn.length; i++) {
				char c = fillIn[i];
				int fillInLookup = i+filledInCounter;
				if(mask.containsKey(fillInLookup)) {
					b.append(mask.get(fillInLookup));
					mask.remove(fillInLookup);

					filledInCounter++;
					i--;
				} else {
					b.append(c);
				}
				
			}
			
			for(Character rest: mask.values()) {
				b.append(rest);
			}
			
			return b.toString().toCharArray();
		}

		public char[] getNonmasked() {
			return this.nonmasked;
		}

		public void setToUnchangedReconstruction(boolean dontReconstruct) {
			this.dontReconstruct = dontReconstruct;
		}

	}

	/** Operation's engine. */
    protected IClassicAlgorithmEngine engine;
    /** Operation's key. */
    protected int[] key;
    /** Operation's key. */
    protected char[] keyChars;
    /** Operation's alpha converter. */
    protected AlphaConverter alphaConv;
    /** Filter for non alpha chars. */
    protected boolean filter = true;
    /** Data object implementation must be the classic implementation. */
    protected ClassicDataObject dataObject;
    /** The InputStream. */
    protected BufferedInputStream is;
    /** The OutputStream. */
    protected BufferedOutputStream os;

    /**
     * Initializes the Algorithm with its key parameters
     * 
     * @param opmode encrypt or decrypt (mode)
     * @param input input text
     * @param alphabet filter alphabet
     * @param key the operation's key
     * @param transformData the transformation data used to transform the text before the real operation, may be
     *            <code>null</code> in case no transformation should be applied
     */
    public void init(int opmode, String input, AbstractAlphabet alphabet, char[] key, TransformData transformData) {
        this.dataObject = new ClassicDataObject();
        this.dataObject.setTransformData(transformData);
        this.dataObject.setPlain(filterTextByTransformData(input, transformData));
        this.dataObject.setAlphabet(alphabet);
        this.dataObject.setKey(key);
        this.dataObject.setOpmode(opmode);
    }

    /**
     * Initializes the Algorithm with its key parameters
     * 
     * @param opmode encrypt or decrypt (mode)
     * @param input input text
     * @param alphabet filter alphabet
     * @param key the operation's key
     * @param key2 the second key
     * @param transformData the transformation data used to transform the text before the real operation, may be
     *            <code>null</code> in case no transformation should be applied
     */
    public void init(int opmode, String input, AbstractAlphabet alphabet, char[] key, char[] key2,
            TransformData transformData) {
        this.dataObject = new ClassicDataObject();
        this.dataObject.setTransformData(transformData);
        this.dataObject.setPlain(filterTextByTransformData(input, transformData));
        
        this.dataObject.setAlphabet(alphabet);
        this.dataObject.setKey(key);
        this.dataObject.setOpmode(opmode);
        this.dataObject.setKey2(key2);
    }

    /**
     * Initializes the Algorithm with its key parameters
     * 
     * @param opmode encrypt or decrypt (mode)
     * @param input input text
     * @param alphabet filter alphabet
     * @param key the operation's key
     * @param nullchar the char (in some algorithms) that is the one to replace a missing character in the
     *            26-char-alphabet
     * @param transformData the transformation data used to transform the text before the real operation, may be
     *            <code>null</code> in case no transformation should be applied
     */
    public void init(int opmode, String input, AbstractAlphabet alphabet, char[] key, char nullchar,
            TransformData transformData) {
        this.dataObject = new ClassicDataObject();
        this.dataObject.setTransformData(transformData);
        this.dataObject.setPlain(filterTextByTransformData(input, transformData));
        this.dataObject.setAlphabet(alphabet);
        this.dataObject.setKey(key);
        this.dataObject.setOpmode(opmode);
        this.dataObject.setNullchar(nullchar);
    }

    /**
     * Initializes the Algorithm with its key parameters
     * 
     * @param opmode encrypt or decrypt (mode)
     * @param input input text
     * @param alphabet filter alphabet
     * @param key the operation's key
     * @param transformData the transformation data used to transform the text before the real operation, may be
     *            <code>null</code> in case no transformation should be applied
     */
    public void init(int opmode, InputStream input, AbstractAlphabet alphabet, char[] key, TransformData transformData) {
        this.dataObject = new ClassicDataObject();
        this.dataObject.setTransformData(transformData);

        try {
            this.dataObject.setInputStream(filterStreamByTransformData(input, transformData));
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
            this.dataObject.setInputStream(input);
        }
        this.dataObject.setAlphabet(alphabet);
        this.dataObject.setKey(key);
        this.dataObject.setOpmode(opmode);
    }

    /**
     * Initializes the Algorithm with its key parameters
     * 
     * @param opmode encrypt or decrypt (mode)
     * @param input input text
     * @param alphabet filter alphabet
     * @param key the operation's key
     * @param key2 the second key
     * @param transformData the transformation data used to transform the text before the real operation, may be
     *            <code>null</code> in case no transformation should be applied
     */
    public void init(int opmode, InputStream input, AbstractAlphabet alphabet, char[] key, char[] key2,
            TransformData transformData) {
        this.dataObject = new ClassicDataObject();
        this.dataObject.setTransformData(transformData);
        try {
            this.dataObject.setInputStream(filterStreamByTransformData(input, transformData));
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
            this.dataObject.setInputStream(input);
        }
        this.dataObject.setAlphabet(alphabet);
        this.dataObject.setKey(key);
        this.dataObject.setKey2(key2);
        this.dataObject.setOpmode(opmode);
    }

    /**
     * Initializes the Algorithm with its key parameters
     * 
     * @param opmode encrypt or decrypt (mode)
     * @param input input text
     * @param alphabet filter alphabet
     * @param key the operation's key
     * @param nullchar the char (in some algorithms) that is the one to replace a missing character in the
     *            26-char-alphabet
     * @param transformData the transformation data used to transform the text before the real operation, may be
     *            <code>null</code> in case no transformation should be applied
     */
    public void init(int opmode, InputStream input, AbstractAlphabet alphabet, char[] key, char nullchar,
            TransformData transformData) {
        this.dataObject = new ClassicDataObject();
        this.dataObject.setTransformData(transformData);
        try {
            this.dataObject.setInputStream(filterStreamByTransformData(input, transformData));
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
            this.dataObject.setInputStream(input);
        }
        this.dataObject.setAlphabet(alphabet);
        this.dataObject.setKey(key);
        this.dataObject.setOpmode(opmode);
        this.dataObject.setNullchar(nullchar);
    }

    public void init(ClassicDataObject dataobject) {
        this.dataObject = dataobject;
        this.setFilter(dataobject.isFilterNonAlphaChars());
        try {
            this.dataObject.setInputStream(filterStreamByTransformData(dataobject.getInputStream(),
                    dataobject.getTransformData()));
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
            // this.dataObject.setInputStream(input);
        }
    }
    
    /**
     * filters a string by given criteria
     * 
     * @param in the input text
     * @param filter the filter Data
     * @return the filtered Text
     */
    private String filterTextByTransformData(String in, TransformData filter) {
        try {
            return Transform.transformText(in, filter);
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
        }
        return in;
    }

    /**
     * reads the current value from an input stream
     * 
     * @param in the input stream
     */
    protected String InputStreamToString(InputStream in) {
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
                myStrBuf.append(String.valueOf(Character.valueOf((char) charOut)));
            }
        } catch (IOException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
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

    /**
     * Filters a InputStream by given criteria.
     * 
     * @param in the input stream
     * @param filter the filter data, may be null, in this case the original input stream is returned
     * @return the filtered Text
     * @throws Exception
     */
    private InputStream filterStreamByTransformData(InputStream in, TransformData filter) throws Exception {
        if (filter == null) {
            return in;
        }

        String filterString = InputStreamToString(in);
        String filteredString = Transform.transformText(filterString, filter);

        return StringToInputStream(filteredString);
    }

    public IDataObject execute() {
        // 1st prepare alphabet table, every char is associated has an int value
        this.alphaConv = new AlphaConverter(this.dataObject.getAlphabet().getCharacterSet());
        // 2nd key
        this.key = generateKey(this.dataObject.getKey());
        this.keyChars = this.dataObject.getKey();
        if (this.dataObject.getInputStream() instanceof BufferedInputStream) {
            this.is = (BufferedInputStream) this.dataObject.getInputStream();
        } else {
            this.is = new BufferedInputStream(this.dataObject.getInputStream());
        }

        // read from inputstream and call decryt/encryt methods
        String inputString = null;
        char[] charInput;
        char[] cipherInput = null;
        char[] cipherOutput = null;

        inputString = InputStreamToString(is);

        charInput = inputString.toCharArray();

        // process en-/decryption cipher as char representation remove non-alpha chars
        CipherTextMask mask = new CipherTextMask(charInput, this.dataObject.getAlphabet());
        try {
            cipherInput = mask.getNonmasked();
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while setting up the cipher input", e, true); //$NON-NLS-1$
        }

        mask.setToUnchangedReconstruction(filter);
        // encrypt
        if (dataObject.getOpmode() == 0) {
            cipherOutput = mask.readIntoMask(encrypt(cipherInput, 0));
        } else if (dataObject.getOpmode() == 1) {
            cipherOutput = mask.readIntoMask(decrypt(cipherInput, 0));
        }

        char[] out2 = new char[cipherOutput.length];
        for (int i = 0; i < cipherOutput.length; i++) {
            out2[i] = cipherOutput[i];
        }

        filter = true; 
        this.dataObject.setOutput(cipherOutput);

        return dataObject;
    }

    /**
     * Subclasses must provide a method which generates the algorithms key
     * 
     * @param keyData the data the key will be generated from
     * @return the generated key as an int array
     */
    protected abstract int[] generateKey(char[] keyData);

    /**
     * Encryption method
     * 
     * @param cipherInput the to be processed input
     * @return the cipher output as a char array
     */
    protected char[] encrypt(char[] cipherInput, int cipherCount) {
        // convert cipher input to an int array by using the alphabet
        int[] input = alphaConv.charArrayToIntArray(cipherInput);

        // get length of alphabet
        int alphaLength = alphaConv.getAlphaLength();

        // alphabet as int array
        int[] alphabet = alphaConv.charArrayToIntArray(alphaConv.getAlpha());
        // Character used as null value
        char nullchar = dataObject.getNullchar();
        // alphabet as char array
        char[] alphaChars = null;
        // second key, used by some algorithms, e.g. adfgvx
        char[] key2 = dataObject.getKey2();
        try {
            alphaChars = alphaConv.filterNonAlphaChars(dataObject.getAlphabet().getCharacterSet());
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while filtering an alphabet", e, true); //$NON-NLS-1$
        }
        char[] inputNoNonAlphaChar = null;
        try {
            inputNoNonAlphaChar = alphaConv.filterNonAlphaChars(cipherInput);
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while filtering an alphabet", e, true); //$NON-NLS-1$
        }
        this.keyChars = alphaConv.intArrayToCharArray(key);
        // process encryption
        int[] cipher = engine.doEncryption(input, key, alphaLength, alphabet, nullchar, alphaChars, keyChars,
                inputNoNonAlphaChar, this.alphaConv, key2, cipherCount);

        // convert cipher int to to char and return
        return alphaConv.intArrayToCharArray(cipher);
    }

    /**
     * Decryption method
     * 
     * @param cipherInput the to be processed input
     * @return the cipher output as a char array
     */
    protected char[] decrypt(char[] cipherInput, int cipherCount) {
        // convert cipher input to an int array by using the alphabet
        int[] input = alphaConv.charArrayToIntArray(cipherInput);

        // get length of alphabet
        int alphaLength = alphaConv.getAlphaLength();

        // alphabet as int array
        int[] alphabet = alphaConv.charArrayToIntArray(alphaConv.getAlpha());
        // Character used as null value
        char nullchar = dataObject.getNullchar();
        // second key, used by some algorithms, e.g. adfgvx
        char[] key2 = dataObject.getKey2();
        // alphabet as char array
        char[] alphaChars = null;
        try {
            alphaChars = alphaConv.filterNonAlphaChars(dataObject.getAlphabet().getCharacterSet());
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while filtering an alphabet", e, true); //$NON-NLS-1$
        }
        this.keyChars = alphaConv.intArrayToCharArray(key);
        char[] inputNoNonAlphaChar = null;
        try {
            inputNoNonAlphaChar = alphaConv.filterNonAlphaChars(cipherInput);
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while filtering an alphabet", e, true); //$NON-NLS-1$
        }
        // process encryption
        int[] cipher = engine.doDecryption(input, key, alphaLength, alphabet, nullchar, alphaChars, keyChars,
                inputNoNonAlphaChar, this.alphaConv, key2, cipherCount);

        // convert cipher int to to char and return
        return alphaConv.intArrayToCharArray(cipher);
    }

    /**
     * This method is used to merge the cipherOutput and the non-alpha chars in the plain text.
     * 
     * @param plain the plain text
     * @param cipherOutput the cipher output
     * @return the merged final output
     */
    protected char[] mergeToFinalOutput(char[] plain, char[] cipherOutput) {
        char[] finalOutput = new char[plain.length];
        boolean[] nonAlphaIndicator = new boolean[finalOutput.length];

        // i is the general and the j counter for non alpha chars
        for (int i = 0, j = 0; i < finalOutput.length; i++) {
            if (alphaConv.containsLetter(plain[i])) { // alpha letter
                // copy from cipher output
                finalOutput[i] = cipherOutput[j++];
            } else { // non alpha letter
                // copy from plain
                finalOutput[i] = plain[i];

                // non alpha char, therefore true
                nonAlphaIndicator[i] = true;
            }
        }

        return finalOutput;
    }

    /**
     * Setter for the non alpha
     * 
     * @param filterNonAlphaFlag if <i>true</i> and if <i>false</i>
     */
    public void setFilter(boolean filterNonAlphaFlag) {
        this.filter = filterNonAlphaFlag;
    }

    /**
     * Getter for the filter flag.
     * 
     * @return filter
     */
    public boolean isFilter() {
        return this.filter;
    }

    /**
     * Getter for the data object
     * 
     * @return IDataObject the data object
     */
    public IDataObject getDataObject() {
        return dataObject;
    }

    /**
     * Rewrite a byte array as a char array method taken from class ByteArrayUtils
     * 
     * @param input - the byte array
     * @return char array
     */
    protected static char[] toCharArray(byte[] input) {
        char[] result = new char[input.length];

        for (int i = 0; i < input.length; i++) {
            result[i] = (char) input[i];
        }

        return result;
    }

    /**
     * Rewrite a int array
     * 
     * @param input - the byte array
     * @return char array
     */
    protected static char[] toCharArray(int[] input) {
        char[] result = new char[input.length];

        for (int i = 0; i < input.length; i++) {
            result[i] = (char) input[i];
        }

        return result;
    }

    /**
     * Rewrite a char array as a byte array
     * 
     * @param input - the char array
     * @return byte array
     */
    protected static byte[] toByteArray(char[] input) {
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) input[i];
        }

        return result;
    }

    // /**
    // * @return standard available Alphabets for algorithms
    // */
    // public static List<AbstractAlphabet> getAvailableAlphabets() {
    // ArrayList<AbstractAlphabet> result = new ArrayList<AbstractAlphabet>();
    // for(AbstractAlphabet a: AlphabetsManager.getInstance().getAlphabets()) {
    // result.add(a);
    // }
    //
    // return result;
    // }
}
