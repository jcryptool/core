// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.algorithm;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.Vector;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;

/**
 * A Transposition key
 *
 * @author Simon L
 *
 */
public class TranspositionKey {
	private static final Integer NOVALUE = -1;
	private static final String SEPARATOR = "|"; //$NON-NLS-1$
	private static final String SEPARATOR_REGEX = "\\|"; //$NON-NLS-1$

	private HashMap<Integer, Integer> internalRepresentation;

	public TranspositionKey() {
		internalRepresentation = new HashMap<Integer, Integer>(0);
	}

	public TranspositionKey(int[] arrayRepresentation) {
		this();
		this.fromArray(arrayRepresentation);
	}

	/**
	 * TranspositionKey from formatted string (numbers+delimiters)
	 *
	 * @param stringRepresentation
	 */
	public TranspositionKey(String stringRepresentation) {
		this();
		this.fromString(stringRepresentation);
	}

	/**
	 * TranspositionKey from unformatted string (with currentAlphabet as sorter)
	 *
	 * @param stringRepresentation
	 */
	public TranspositionKey(String stringRepresentation, char[] alphabet) {
		this();
		this.fromString(stringRepresentation, alphabet);
	}


	/**
	 * Clears all content.
	 */
	public void clear() {
		internalRepresentation.clear();
	}

	/**
	 * Specifies a value at the given position of the key. <br />
	 * Basically, if a value a is on the position b in the key, that
	 * means, that in every column of a plaintext, the character at
	 * position b will be placed in position a in the ciphertext.
	 * <br /> <br />
	 * If there is already the value "value" in this key, but on another
	 * position, the value on this "other position" will be replaced with
	 * the default value for "no value".
	 * <br /><br />
	 * If one of the parameters (position, or value) have a higher value than
	 * the actual size of the key, the key will be resized to it.
	 *
	 * @param position the position in the key
	 * @param value the value that has to stand at this position.
	 */
	public void set(int position, int value) {
		setLength(Math.max(Math.max(position+1, value+1), getLength()));
		for(Entry<Integer, Integer> entry: internalRepresentation.entrySet()) {
			//Doublet
			if(entry.getValue() == value) {
				internalRepresentation.put(entry.getKey(), NOVALUE);
			}
		}
		internalRepresentation.put(position, value);
	}

	/**
	 * Sets the key to a specified length. All values in the key that
	 * are bigger than this new length, will be replaced with "no value".
	 *
	 * @param length the new length
	 */
	public void setLength(int length) {
		Vector<Integer> removelist = new Vector<Integer>(0);
		for(Integer k: internalRepresentation.keySet()) {
			if(k >= length) {
				removelist.add(k);
			}
		}
		for(Integer i: removelist) {
			internalRepresentation.remove(i);
		}

		for(int i=0; i<length; i++) {
			// make sure that every key from 0 to length is contained once.
			if(! internalRepresentation.containsKey(i)) {
				internalRepresentation.put(i, NOVALUE);
			}

			// make sure, that no value is contained that is greater than the
			// length
			if(internalRepresentation.get(i) >= length) {
				internalRepresentation.put(i, NOVALUE);
			}
		}

	}

	/**
	 * Reads a unformatted String into the key. The key is interpreted
	 * by using the given currentAlphabet (as char-array) <br /><br />
	 *
	 * @param argString The String to be read into the key.
	 */
	public void fromString(String argString, char[] alphabet) {
		String alphaString = String.valueOf(alphabet);
		char[] inputArray = argString.toCharArray();
		int[] numberArray = new int[inputArray.length];
		for(int i=0; i<inputArray.length; i++) {
			numberArray[i] = alphaString.indexOf(inputArray[i]);
		}

		int[] resultArray = new int[numberArray.length];
		for(int i=0; i<numberArray.length; i++) {
			int howMuchAreSmaller = 0;
			for(int k=0; k<numberArray.length; k++) {
				if(numberArray[k] < numberArray[i]) {
					howMuchAreSmaller++;
				} else if(numberArray[k] == numberArray[i] && i>k) {
					howMuchAreSmaller++;
				}
			}

			resultArray[i] = howMuchAreSmaller;
		}

		this.fromArray(resultArray);
	}

	/**
	 * Reads a formatted String into the key. Values in the String have
	 * to be separated by TranspositionKey.SEPARATOR. <br /><br />
	 *
	 * @param argString The String to be read into the key.
	 */
	public void fromString(String argString) {
		String[] valueStrings = argString.split(SEPARATOR_REGEX);
		int[] values = new int[valueStrings.length];
		for(int i=0; i<valueStrings.length; i++) {
			values[i] = Integer.parseInt(valueStrings[i]);
		}

		this.fromArray(values);
	}

	/**
	 * Read an array of integer values into the key. This content
	 * replaces the current one.
	 *
	 *
	 * @param argArray
	 */
	public void fromArray(int[] argArray) {
		this.clear();

		for(int i=0; i<argArray.length; i++) {
			this.set(i, argArray[i]);
		}
	}

	/**
	 * Converts the actual key into an int array, and adds an offset to every value, except
	 * the NOVALUE constant.<br /><br />
	 * The values are all zero-relative.
	 *
	 * @return an integer array representing the positions of the key.
	 */
	public int[] toArray() {
		return toArray(0);
	}

	/**
	 * Converts the actual key into an int array, and adds an offset to every value, except
	 * the NOVALUE constant.<br /><br />
	 * The values are all relative to the value "1" ("1" would refer to a first position).
	 *
	 * @return an integer array representing the positions of the key.
	 */
	public int[] toArrayOneRelative() {
		return toArray(1);
	}

	private int[] toArray(int offset) {
		TreeSet<Integer> sortedKeySet = new TreeSet<Integer>(internalRepresentation.keySet());
		Integer[] sortedKeyArray = new Integer[]{};
		sortedKeyArray = sortedKeySet.toArray(sortedKeyArray);

		int[] result = new int[sortedKeyArray.length];

		for(int i=0; i<sortedKeyArray.length; i++) {
			int value = internalRepresentation.get(sortedKeyArray[i]);
			if(value != NOVALUE) value+=offset;

			result[i] = value;
		}

		return result;
	}

	public String toString() {
		return toString(0);
	}

	/**
	 * Unlike the normal toString function, this function returns
	 * all values "1"-relative, not zero-relative.
	 *
	 * @return
	 */
	public String toStringOneRelative() {
		return toString(1);
	}

	/**@see TranspositionKey#getKeyInChars(AbstractAlphabet, TranspositionKey)
	 *
	 * @param alpha the currentAlphabet
	 * @return a String
	 */
	public String toUnformattedChars(AbstractAlphabet alpha) {
		return getKeyInChars(alpha, this);
	}

	/**@see TranspositionKey#getKeyInChars(char[], TranspositionKey)
	 *
	 * @param alpha the Alphabet
	 * @return a String
	 */
	public String toUnformattedChars(char[] alpha) {
		return getKeyInChars(alpha, this);
	}

	private String toString(int offset) {
		String result = ""; //$NON-NLS-1$
		int[] sortedKeyArray = this.toArray(offset);

		for(int i=0; i<sortedKeyArray.length; i++) {
			Integer value = sortedKeyArray[i];

			if(i<sortedKeyArray.length-1)
				result = result.concat(
						value + SEPARATOR);
			else
				result = result.concat(
						value.toString());
		}

		return result;
	}

	/** Returns, whether this transposition key will really alter a text, or leave it the same.
	 * @return
	 */
	public boolean isReallyEncrypting() {
		for(Entry<Integer, Integer> e: internalRepresentation.entrySet()) {
			if(e.getKey() != e.getValue()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns the value on the specified position in the key
	 *
	 * @param position the position
	 * @return
	 */
	public int get(Integer position) {
		return internalRepresentation.get(position);
	}

	/**
	 * Returns, where the specified value (zero relativa as always)
	 * stands in the key
	 *
	 * @param positionValue the value
	 * @return the position of the value
	 */
	public int getIndexOf(int positionValue) {
		for(Entry<Integer, Integer> e: internalRepresentation.entrySet()) {
			if(e.getValue().equals(positionValue)) return e.getKey();
		}
		return -1;
	}

	public int getLength() {
		return internalRepresentation.keySet().size();
	}

	public static TranspositionKey getReverseKey(TranspositionKey key) {
		if(key != null) {
			int[] thiskey = key.toArray();
			int[] reversekey = new int[thiskey.length];
			for(int i=0; i<reversekey.length; i++) reversekey[i] = -1;
			for(int i=0; i<thiskey.length; i++) {
				int val = thiskey[i];
				reversekey[val] = i;
			}

			return new TranspositionKey(reversekey);
		}

		return null;
	}

	public TranspositionKey getReverseKey() {
		return TranspositionKey.getReverseKey(this);
	}

	/**
	 * Retrieves a key String without delimiters for a specific currentAlphabet.
	 * if the currentAlphabet is null, the standard Alphabet will be used. <br>
	 * This Method tries to retrieve some "nice" String. If the length of
	 * the key is equal or less 10, and the provided Alphabet supports
	 * Digits, the String will consist of a digit stream. Otherwise,
	 * it will consist of the first fitting characters that can be found
	 * in the currentAlphabet.
	 *
	 * @param a the Alphabet
	 * @param k the Key
	 * @return
	 */
	public static String getKeyInChars(char[] a, TranspositionKey k) {
		char[] alphaSet = a;

		// 0..10?
		int digitsContained = 0;
		for(char c: alphaSet) if(48 <= (byte)c && (byte) c <= 58) digitsContained++;

		boolean containsDigits;
		if(digitsContained != 10) containsDigits = false;
		else containsDigits = true;

		boolean inDigits = (containsDigits && k.getLength()<=10);

		if(inDigits) {
			char[] digitSet = new char[10];
			int counter = 0;
			for(char c: alphaSet) {
				if(48 <= (byte)c && (byte) c <= 58) {
					digitSet[counter] = c;
					counter++;
				}
			}

			alphaSet = digitSet;
		}

		char[] output = new char[Math.min(alphaSet.length, k.getLength())];
		int[] key = k.toArray();

		for(int i=0; i<output.length; i++) {
			output[i] = alphaSet[key[i]];
		}

		if(alphaSet.length>=key.length) return String.valueOf(output);
		return null;

	}

	/**
	 * Retrieves a key String without delimiters for a specific currentAlphabet.
	 * if the currentAlphabet is null, the standard Alphabet will be used. <br>
	 * This Method tries to retrieve some "nice" String. If the length of
	 * the key is equal or less 10, and the provided Alphabet supports
	 * Digits, the String will consist of a digit stream. Otherwise,
	 * it will consist of the first fitting characters that can be found
	 * in the currentAlphabet.
	 *
	 * @param a the Alphabet
	 * @param k the Key
	 * @return
	 */
	public static String getKeyInChars(AbstractAlphabet a, TranspositionKey k) {
		AbstractAlphabet alpha = AlphabetsManager.getInstance().getDefaultAlphabet();
		if(a != null) alpha = a;

		char[] alphaSet = alpha.getCharacterSet();

		return TranspositionKey.getKeyInChars(alphaSet, k);
	}

	public static TranspositionKey getDefaultUnchangingKey(int length) {
		TranspositionKey result = new TranspositionKey();
		result.setLength(length);

		for(int i=0; i<length; i++) result.set(i, i);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)) return true;
		if(! (obj instanceof TranspositionKey)) return false;
		TranspositionKey other = (TranspositionKey) obj;

		int[] thisArray = this.toArray();
		int[] otherArray = other.toArray();

		if(thisArray.length != otherArray.length) return false;
		for(int i=0; i<thisArray.length; i++) {
			if(thisArray[i] != otherArray[i]) return false;
		}

		return true;

	}


}