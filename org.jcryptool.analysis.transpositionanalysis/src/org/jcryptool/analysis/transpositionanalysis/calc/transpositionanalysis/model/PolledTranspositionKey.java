//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jcryptool.analysis.transpositionanalysis.calc.PolledPositiveInteger;
import org.jcryptool.analysis.transpositionanalysis.calc.PolledValue;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;

/**
 * A Transposition key (with set length), which positions can be polled.
 * 
 * @author Simon L
 * 
 */
public class PolledTranspositionKey {
	/**
	 * Creates a standard entry for a key position with given length
	 * 
	 * @param positionCount the number of positions in the key ("key length")
	 * @return a PolledPositiveInteger of the specified length and standard values
	 */
	private static final PolledPositiveInteger NOVALUE(int positionCount) {
		PolledPositiveInteger result = new PolledPositiveInteger();
		for (int i = 0; i < positionCount; i++)
			result.addChoice(i);
		return result;
	}

	private HashMap<Integer, PolledPositiveInteger> internalRepresentation;

	/**
	 * Creates an empty PolledTranspositionKey
	 */
	public PolledTranspositionKey() {
		internalRepresentation = new HashMap<Integer, PolledPositiveInteger>(0);
	}

	/**
	 * Creates a PolledTranspositionKey from an array. Format: see
	 * {@link #fromArray(double[][])}
	 * 
	 * @param arrayRepresentation the array
	 */
	public PolledTranspositionKey(double[][] arrayRepresentation) {
		this();
		this.fromArray(arrayRepresentation);
	}

	/**
	 * Clears all content.
	 */
	public void clear() {
		internalRepresentation.clear();
	}

	/**
	 * Specifies the possibility for a value at the given position of the key.
	 * <br />
	 * Basically, if a value a is on the position b in the key, that means, that in
	 * every column of a plaintext, the character at position b will be placed in
	 * position a in the ciphertext. Specifying a high possibility for a value at a
	 * given position means, that it is very likely that the polled key will have
	 * this value on the said position in the end. <br />
	 * <br />
	 * If one of the parameters (position, or value) have a higher value than the
	 * actual size of the key, the key will be resized to it.
	 * 
	 * @param position    the position in the key
	 * @param value       the value that has to stand at this position.
	 * @param possibility the possibility vot the value to be actually in the given
	 *                    position (use constants from {@link PolledValue} for
	 *                    example).
	 */
	public void setPossibility(int position, int value, double possibility) {
		setLength(Math.max(Math.max(position + 1, value + 1), getLength()));

		internalRepresentation.get(position).addChoice(value, possibility);
	}

	/**
	 * Combines the possibility for a value at the given position of the key with a
	 * given one. <br />
	 * See also: {@link #setPossibility(int, int, double)}<br />
	 * If one of the parameters (position, or value) have a higher value than the
	 * actual size of the key, the key will be resized to it.
	 * 
	 * @param position    the position in the key
	 * @param value       the value that has to stand at this position.
	 * @param possibility the possibility the existing possibility will be combined
	 *                    with (you can use constants from {@link PolledValue} for
	 *                    example).
	 */
	public void combinePossibility(int position, int value, double possibility) {
		setLength(Math.max(Math.max(position + 1, value + 1), getLength()));

		internalRepresentation.get(position).combinePossibility(value, possibility);
	}

	/**
	 * Sets the key to a specified length. All values in the key that are bigger
	 * than this new length, will be removed. In the end, there will be n polls for
	 * key positions, when n is the new length, and the method {@link #toArray()}
	 * will return a n x n array.
	 * 
	 * @param length the new length
	 */
	public void setLength(int length) {

		// remove all entries with key greater than length
		Set<Integer> removelist = new HashSet<Integer>(0);
		for (Integer k : internalRepresentation.keySet()) {
			if (k >= length) {
				removelist.add(k);
			}
		}
		for (Integer i : removelist) {
			internalRepresentation.remove(i);
		}

		for (int i = 0; i < length; i++) {
			// make sure that every key from 0 to length is contained once.
			if (!internalRepresentation.containsKey(i)) {
				internalRepresentation.put(i, NOVALUE(length));
			}

			// remove all entries from a poll for a position, that are greater
			// than the length
			removelist.clear();
			for (Integer choice : internalRepresentation.get(i).getPollSubjects()) {
				if (choice >= length)
					removelist.add(choice);
			}
			for (Integer removeChoice : removelist) {
				internalRepresentation.get(i).removeChoice(removeChoice);
			}

			// make sure, that in all Polls for positions are only the values
			// between 0 and length
			for (int k = 0; k < length; k++) {
				if (!internalRepresentation.get(i).hasChoice(k)) {
					internalRepresentation.get(i).addChoice(k);
				}
			}

		}

	}

	private double valueOfChoice(int[] choice, double[][] pollArray) {
		double[] choiceArray = new double[choice.length];
		for (int i = 0; i < choiceArray.length; i++) {
			choiceArray[i] = pollArray[i][choice[i]];
		}

		return valueOfChoiceAsDoublerow(choiceArray);
	}

	private double valueOfChoiceAsDoublerow(double[] choiceValues) {
		double result = PolledValue.POSSIBILITY_DEFAULT;
		for (int i = 0; i < choiceValues.length; i++) {
			result *= choiceValues[i];
		}

		return result;
	}

	private int[] combineRecursiveChoices(int thisDepthChoice, int[] nextDepthChoice) {
		int[] result = new int[nextDepthChoice.length + 1];
		result[0] = thisDepthChoice;
		for (int i = 1; i < nextDepthChoice.length + 1; i++) {
			result[i] = nextDepthChoice[i - 1] >= thisDepthChoice ? nextDepthChoice[i - 1] + 1 : nextDepthChoice[i - 1];
		}

		return result;
	}

	private double[][] makeSubPollArray(int choiceIndexOfRowZero, double[][] pollArray) {
		if (!(pollArray.length > 1 && pollArray[0].length > 1))
			throw new IllegalArgumentException("Can't crate sub - Poll-array from 1-element-array.");
		if (choiceIndexOfRowZero >= pollArray.length)
			throw new IllegalArgumentException(
					"choiceIndex is greater than poll array dimensions. cannot create sub - poll-array.");
		double[][] result = new double[pollArray.length - 1][pollArray[0].length - 1];

		for (int i = 1; i < pollArray.length; i++) {
			for (int k = 0; k < pollArray[i].length - 1; k++) {
				result[i - 1][k] = pollArray[i][(k >= choiceIndexOfRowZero) ? k + 1 : k];
			}
		}

		return result;
	}

	private int[] getBestChoice(double[][] pollArray) {
		if (pollArray.length < 1)
			throw new IllegalArgumentException("there is no best choice for a void array");
		if (pollArray.length == 1)
			return new int[] { 0 }; // take the remaining
									// piece
		// not-trivial cases
		int[] bestChoice = null;
		double bestChoiceValue = Double.MIN_VALUE;
		for (int i = 0; i < pollArray.length; i++) {
			double[][] subArray = makeSubPollArray(i, pollArray);
			// heuristic cost is the biggest cost that could possibly reached
			// with choice i (estimated)
			double heuristicCost = calcHeuristicCostForChoiceAndFollowingSubarray(pollArray[0][i], subArray);
			if (heuristicCost > bestChoiceValue) {
				int[] currentChoice = combineRecursiveChoices(i, getBestChoice(subArray));
				double subValue = valueOfChoice(currentChoice, pollArray);
				if (subValue > bestChoiceValue) {
					bestChoiceValue = subValue;
					bestChoice = currentChoice;
				}
			}
		}

		return bestChoice;
	}

	private double calcHeuristicCostForChoiceAndFollowingSubarray(double choiceValue, double[][] subArray) {
		double[] valueArray = new double[subArray.length + 1];
		double[] subArrayOverestimation = calcBestValuePerPosList(subArray);
		valueArray[0] = choiceValue;
		for (int i = 1; i < subArray.length + 1; i++) {
			valueArray[i] = subArrayOverestimation[i - 1];
		}

		return valueOfChoiceAsDoublerow(valueArray);
	}

	/**
	 * Calculates the best TranspositionKey that would match the given possibility
	 * data.
	 * 
	 * @return a TranspositionKey which is the best match for this
	 *         PolledTranspositionKey.
	 */
	public TranspositionKey getBestChoice() {
		double[][] array = toArray();
		if (!(array.length > 0 && array[0].length > 0))
			throw new IllegalArgumentException("polled key has no content - cannot calculate best choice");

		int[] bestChoice = getBestChoice(array);

		return new TranspositionKey(bestChoice);
	}

	private double[] calcBestValuePerPosList(double[][] array) {
		List<Double> maxValues = new LinkedList<Double>();
		for (int i = 0; i < array.length; i++) {
			double minVal = Double.MIN_VALUE;
			for (int k = 0; k < array[i].length; k++) {
				minVal = Math.max(minVal, array[i][k]);
			}
			maxValues.add(minVal);
		}

		double[] valueListAsArray = new double[maxValues.size()];
		for (int i = 0; i < valueListAsArray.length; i++)
			valueListAsArray[i] = maxValues.get(i);
		return valueListAsArray;
	}

	/**
	 * Read an array of integer values into the polled key, as follows:
	 * <code>arrayRepresentation[2][4] = 6</code> means, that in the key on position
	 * 2 (zero-relative), the value "4" has a possibility that is one relatively
	 * high one*. Such, a polled key of length n is always represented by a double
	 * array with dimensions n x n.<br />
	 * <br />
	 * 
	 * * because a possibility of 1 means 50%, and 6 is normally a value that
	 * specifies "Very high possibility". see possibility constants in
	 * {@link PolledValue}, e. g. {@link PolledValue#POSSIBILITY_VERY_LIKELY}
	 * 
	 * @param arrayRepresentation a representation of the polled key that shall be
	 *                            loaded, as described above (length x length -
	 *                            array)
	 */
	public void fromArray(double[][] arrayRepresentation) {
		this.clear();

		int length = arrayRepresentation.length;
		this.clear();
		this.setLength(length);

		for (int i = 0; i < length; i++) {
			for (int k = 0; k < length; k++) {
				this.setPossibility(i, k, arrayRepresentation[i][k]);
			}
		}
	}

	/**
	 * See {@link #fromArray()} (inverse-compatible method)
	 * 
	 * @return an length x length-array of possibilities.
	 */
	public double[][] toArray() {
		TreeSet<Integer> sortedKeySet = new TreeSet<Integer>(internalRepresentation.keySet());
		Integer[] sortedKeyArray = new Integer[] {};
		sortedKeyArray = sortedKeySet.toArray(sortedKeyArray);

		double[][] result = new double[sortedKeyArray.length][sortedKeyArray.length];

		for (int i = 0; i < sortedKeyArray.length; i++) {
			PolledPositiveInteger pollAtPos = internalRepresentation.get(i);
			for (int k = 0; k < pollAtPos.getChoiceCount(); k++) {
				result[i][k] = pollAtPos.getPossibility(k);
			}
		}

		return result;
	}

	@Override
	public String toString() {
		String result = "";
		double[][] array = this.toArray();
		for (int i = 0; i < array.length; i++) {
			result += (i == 0 ? "" : "\n") + "Position " + i + ": ";
			for (int k = 0; k < array[i].length; k++) {
				result += (k == 0 ? "" : "  ") + array[i][k];
			}
		}
		return result;
	}

	/**
	 * Returns the length of the key (speaking: the number of positions in the key).
	 * 
	 * @return the length
	 */
	public int getLength() {
		return internalRepresentation.keySet().size();
	}

}