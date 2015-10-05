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
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;

/**
 * A Transposition key
 *
 * @author Simon L
 *
 */
public class TranspositionKey {
	private static final String[] POSSIBLE_PARSE_SEPARATORS = new String[]{",", ";", "|", " "};
	private static final Integer NOVALUE = -1;
	private static final String SEPARATOR = "|"; //$NON-NLS-1$

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

//	/**
//	 * Returns the characters that are allowed
//	 *
//	 * @param baseAlphabet
//	 * @return
//	 */
//	public static char[] generateCharsetForAcceptableKeyInputCharacters(char[] baseAlphabet) {
//		String additionalCharacters = "0123456789";
//		for(String s: POSSIBLE_PARSE_SEPARATORS) {
//			additionalCharacters += s;
//		}
//		String result = String.valueOf(baseAlphabet);
//
//		for(char c: additionalCharacters.toCharArray()) {
//			if(String.valueOf(baseAlphabet).indexOf(c)==-1) {
//				result += String.valueOf(c);
//			}
//		}
//
//		return result.toCharArray();
//	}

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

	public static class KeyFromStringParseResult {
		public enum Mode {
			LIMITER, NOTINALPHA, DEFAULT;

			public boolean isSuccessful() {
				if(this != NOTINALPHA) {
					return true;
				}
				return false;
			}
		}

		public Mode mode;
		public String limiter;
		public Character notinalphaChar;
		public String newkey;
	}

	public static KeyFromStringParseResult generateKeyFromStringMode(String argString, char[] alphabet) {
		KeyFromStringParseResult result = new KeyFromStringParseResult();
		KeyFromStringParseResult.Mode mode = KeyFromStringParseResult.Mode.DEFAULT;
		String limiter = null;

		String[] possibleLimiterStrings = POSSIBLE_PARSE_SEPARATORS;

		limiter = keyStringIsWithLimiters(argString, possibleLimiterStrings);
		if(limiter != null) {
			mode = KeyFromStringParseResult.Mode.LIMITER;
		}
		if(argString.matches("(\\d)*")) {
			String digitStringToLimiterString = "";
			for(int i=0; i<argString.toCharArray().length; i++) {
				if(i!=argString.toCharArray().length-1) {
					digitStringToLimiterString += (argString.toCharArray()[i]+",");
				} else {
					digitStringToLimiterString += (argString.toCharArray()[i]);
				}
			}
			result.newkey = digitStringToLimiterString;
			limiter = ",";
			mode = KeyFromStringParseResult.Mode.LIMITER;
		}



		// check for not-in-alpha characters
		if(mode==KeyFromStringParseResult.Mode.DEFAULT) {
			Character lastFoundCharOutOfAlpha = null;
			for(char c: argString.toCharArray()) {
				if(String.valueOf(alphabet).indexOf(c) == -1) {
					lastFoundCharOutOfAlpha = c;
					String inputWithoutChar = argString.replaceAll(Pattern.quote(String.valueOf(c)), "");
					if(generateKeyFromStringMode(inputWithoutChar, alphabet).mode.isSuccessful()) {
						result.mode = KeyFromStringParseResult.Mode.NOTINALPHA;
						result.notinalphaChar = c;
						return result;
					}

				}
			}
			if(lastFoundCharOutOfAlpha != null) {
				result.mode = KeyFromStringParseResult.Mode.NOTINALPHA;
				result.notinalphaChar = lastFoundCharOutOfAlpha;
				return result;
			}
		}

		result.mode = mode;
		result.limiter = limiter;
		return result;
	}

	/**
	 * Reads a unformatted String into the key. The key is interpreted
	 * by using the given currentAlphabet (as char-array) <br /><br />
	 * Special input forms that even use characters out of the alphabet are allowed:
	 * pure numbers and pure numbers limited by limiters specified in {@link #POSSIBLE_PARSE_SEPARATORS}.
	 *
	 * @param argString The String to be read into the key.
	 */
	public void fromString(String argString, char[] alphabet) {
		KeyFromStringParseResult modeObj = generateKeyFromStringMode(argString, alphabet);
		if(modeObj.newkey != null) argString = modeObj.newkey;
		KeyFromStringParseResult.Mode mode = modeObj.mode;
		String limiter = modeObj.limiter;

		if(! mode.isSuccessful()) {
			throw new RuntimeException("Can't parse this string to a transposition key because it does not comply with any special form and there is a character out of the specified alphabet: " + modeObj.notinalphaChar);
		}

		if(mode == KeyFromStringParseResult.Mode.DEFAULT) {
			String alphaString = String.valueOf(alphabet);
			char[] inputArray = argString.toCharArray();
			int[] numberArray = new int[inputArray.length];
			for(int i=0; i<inputArray.length; i++) {
				int indexOf = alphaString.indexOf(inputArray[i]);
				if(indexOf == -1) throw new RuntimeException("character not in alphabet when trying to generate transposition key");
				numberArray[i] = indexOf;
			}

			int[] resultArray = generateKeyArrayFromNumberArray(numberArray);
			this.fromArray(resultArray);
		} else if(mode.equals(KeyFromStringParseResult.Mode.LIMITER)) {
			argString = replaceAllNoRegex(argString, " ", "");
			String[] split = replaceAllNoRegex(argString, " ", "").split(Pattern.quote(limiter));
			List<Integer> numbers = new LinkedList<Integer>();
			for(String splitPart: split) {
				if(! splitPart.equals("")) {
					try {
						Integer part = Integer.parseInt(splitPart);
						numbers.add(part);
					} catch(NumberFormatException e) {
			            LogUtil.logError(e);
					}
				}
			}
			int[] key = new int[numbers.size()];
			for (int i = 0; i < key.length; i++) {
				key[i] = numbers.get(i);
			}

			int[] resultArray = generateKeyArrayFromNumberArray(key);

			this.fromArray(resultArray);
		}
	}

	/**
	 * Reads a formatted String into the key. Values of this string can be separated by one of the separators in {@link #POSSIBLE_PARSE_SEPARATORS} (read further about separators here: {@link #keyStringIsWithLimiter(String, String)}). If they are not separated in such a way, the alphabet used to parse this string is the standard alphabet as specified in {@link TranspositionAlgorithmSpecification}.
	 *
	 * @param argString The String to be read into the key.
	 */
	public void fromString(String argString) {
		this.fromString(argString, TranspositionAlgorithm.specification.getDefaultPlainTextAlphabet().getCharacterSet());
	}

	private int[] generateKeyArrayFromNumberArray(int[] key) {
		int[] resultArray = new int[key.length];
		for(int i=0; i<key.length; i++) {
			int howMuchAreSmaller = 0;
			for(int k=0; k<key.length; k++) {
				if(key[k] < key[i]) {
					howMuchAreSmaller++;
				} else if(key[k] == key[i] && i>k) {
					howMuchAreSmaller++;
				}
			}

			resultArray[i] = howMuchAreSmaller;
		}
		return resultArray;
	}

	/**
	 * Checks, if a transposition key string uses one of the given strings as exclusive limiter for numbers.
	 * for example, the method would return "," for the following key string: "1,2,3,4" but not for "1,,2,3,4".
	 * if only blanks after a delimiter would prevent the limiter from being accepted, they will be ignored.
	 *
	 * @param keyString
	 * @param possibleLimiterStrings
	 * @return
	 */
	private static String keyStringIsWithLimiters(String keyString, String[] possibleLimiterStrings) {
		for(String limiter: possibleLimiterStrings) {
			if(keyStringIsWithLimiter(keyString, limiter)) {
				return limiter;
			}
		}
		return null;
	}

	private static String replaceAllNoRegex(String str, String target, String replacement) {
		return str.replaceAll(Pattern.quote(target), replacement);
	}

	/**
	 * Checks, if a transposition key string uses the given string as exclusive limiter for numbers.
	 * for example, the method would return "," for the following key string: "1,2,3,4" but not for "1,,2,3,4". (neither would "1,a,2,3" be accepted)
	 * if only blanks after a delimiter would prevent the limiter from being accepted, they will be ignored.
	 *
	 * @param keyString
	 * @param possibleLimiterString
	 * @return
	 */
	private static boolean keyStringIsWithLimiter(String keyString, String possibleLimiterString) {
		if(!keyString.contains(possibleLimiterString)) return false;
		String[] digits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		for(String digit: digits) {
			keyString = replaceAllNoRegex(keyString, digit, "");
		}

		keyString = replaceAllNoRegex(keyString, possibleLimiterString, "");
		keyString = replaceAllNoRegex(keyString, " ", "");
		if(keyString.length() == 0) return true;
		return false;
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

	@Override
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
		for(char c: alphaSet) if(48 <= (byte)c && (byte) c < 58) digitsContained++;

		boolean containsDigits;
		if(digitsContained != 10) containsDigits = false;
		else containsDigits = true;

		boolean inDigits = (containsDigits && k.getLength()<=10);

		if(inDigits) {
			char[] digitSet = new char[10];
			int counter = 0;
			for(char c: alphaSet) {
				if(48 <= (byte)c && (byte) c < 58) {
					digitSet[counter] = c;
					counter++;
				}
			}

			boolean couldSpareTheZero = digitSet[0] == '0' && k.getLength()<10;
			if(couldSpareTheZero) {
				alphaSet = new char[9];
				for(int i=0; i<digitSet.length-1; i++) {
					alphaSet[i] = digitSet[i+1];
				}
			} else {
				alphaSet = digitSet;
			}

		}

		//would delete all non-digits and non-A-Z characters suffice?
		if(!inDigits) {
			String alphaString = String.valueOf(a);
			String nonNecessaryCharacters = "[^0-9A-Za-z]";
			alphaString = alphaString.replaceAll(nonNecessaryCharacters, "");

			if(k.getLength() <= alphaString.length()) {
				alphaSet = alphaString.toCharArray();
			}
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
		//TODO: efficiency
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