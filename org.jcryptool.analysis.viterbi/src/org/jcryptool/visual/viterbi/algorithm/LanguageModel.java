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
package org.jcryptool.visual.viterbi.algorithm;

import java.util.Map;

/**
 * This class provides probabilities for given input strings. the propabilities
 * depend on NGrams.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */
public class LanguageModel {

	private static final double A = 0.5; // how much probability mass should be
											// shifted unseen character
											// sequences
	private static final double LAMBDA = 0.005; // how much probability to be
												// shifted to unseen monograms

	Map<String, Integer> nGrams;
	int alphabetSize;
	int totalMonoGrams; // length of all texts read
	Double denominator;

	/**
	 *
	 * @param ngrams
	 *            , HashMap with a sequence of chars and the apporiate
	 *            propability
	 * @param alphabetSize
	 *            , defines how many characters the alphabet contains
	 * @param totalMonoGrams
	 *            , is the sum of all monograms used, this is the same as the
	 *            sum of all texts read
	 */
	public LanguageModel(Map<String, Integer> ngrams, int alphabetSize,
			int totalMonoGrams) {
		this.nGrams = ngrams;
		this.alphabetSize = alphabetSize;
		this.totalMonoGrams = totalMonoGrams;
		this.denominator = totalMonoGrams + LAMBDA * alphabetSize;
	}

	/**
	 *
	 * Calculates the probabilty of a character sequence.
	 *
	 * @return returns the probability of the character sequence in this
	 *         language model
	 */
	public double getProbability(String nGram) {
		double sum = 0.0;
		for (int j = 0; j < nGram.length() - 1; j++) {
			double summand = Math.pow(A, j);
			summand *= (1 - A);
			summand *= getPHat(nGram.substring(j));
			sum += summand;
		}
		double summand = Math.pow(A, nGram.length());

		Integer lastCharCount = nGrams.get(nGram.substring(0, 1));
		if (lastCharCount == null) {
			lastCharCount = 0;
		}
		Double numerator = lastCharCount + LAMBDA;
		summand *= numerator / denominator;
		sum += summand;
		return sum;
	}

	/**
	 * Calculates a temporary variable used in the getProbability method.
	 *
	 * @param nGram
	 * @return temporary value for getProbability
	 *
	 */
	private double getPHat(String nGram) {
		Integer smallCount = 0; // how often the first (n-1) characters of NGram
		// had been observed
		if (nGram.length() > 1) {
			smallCount = nGrams.get(nGram.substring(0, nGram.length() - 1));
			if (smallCount == null) {
				return 1 / ((double) alphabetSize);
			}
		}
		Integer fullCount = nGrams.get(nGram); // how often nGram has been
		// observed
		if (fullCount == null) {
			fullCount = 0;
		}

		return fullCount / (double) smallCount;
	}
}
