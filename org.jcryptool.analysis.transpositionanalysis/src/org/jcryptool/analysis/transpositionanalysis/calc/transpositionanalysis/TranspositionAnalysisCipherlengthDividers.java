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
package org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jcryptool.analysis.transpositionanalysis.calc.PolledPositiveInteger;
import org.jcryptool.analysis.transpositionanalysis.calc.PolledValue;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisConclusion;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisInput;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisOutput;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisResultAtom;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;

public class TranspositionAnalysisCipherlengthDividers extends TranspositionAnalysis {

	TranspositionAnalysisInput in;

	public TranspositionAnalysisCipherlengthDividers() {
		super();
	}

	private static final double POSSIBILITY_SINGLE_OCCURENCE = PolledValue.POSSIBILITY_HIGHLY_LIKELY;
	private PolledPositiveInteger pureResult;
	private TranspositionAnalysisConclusion conclusion;

	public void analyze(TranspositionAnalysisInput in, TranspositionAnalysisOutput out) {
		this.in = in;
		this.out = out;
		analyze();
	}

	private double smallValueFunction(double x) {
		double a = (PolledValue.POSSIBILITY_DEFAULT - PolledValue.POSSIBILITY_HIGHLY_UNLIKELY) / (4 - 1);
		double b = PolledValue.POSSIBILITY_HIGHLY_UNLIKELY - a;

		return Math.min(1, a * x + b);
	}

	private double possibilityFunction(double x, int numberOfHits, double bestChoiceWouldNormallyBe) {
		final double STANDARD_POSSILITY = PolledValue.POSSIBILITY_DEFAULT;
		final double STRETCH_FUNCTION_MULTIPLICATOR = 20;

		double oneToZeroDist = 1
				/ (Math.abs(x - bestChoiceWouldNormallyBe) / STRETCH_FUNCTION_MULTIPLICATOR + STANDARD_POSSILITY);// *((POSSIBILITY_SINGLE_OCCURENCE
																													// -
																													// STANDARD_POSSILITY)/numberOfHits)
																													// -
																													// STANDARD_POSSILITY;
		double basicFunction = STANDARD_POSSILITY
				+ ((POSSIBILITY_SINGLE_OCCURENCE - STANDARD_POSSILITY) / numberOfHits) * oneToZeroDist;

		return smallValueFunction(x) * basicFunction;
	}

	private double calcPeakPossibilityLength(List<Integer> dividers) {
		int counter = 0;
		double sum = 0;
		for (Integer divider : dividers) {
			sum += divider;
			counter++;
			if (counter >= 4)
				break;
		}

		return sum / counter;
	}

	private PolledPositiveInteger calculatePossibilitiesIntoOutputFromLengthAndDividers(List<Integer> dividers,
			int length, TranspositionAnalysisOutput out, Set<Integer> possibleLengths) {
		final double POSSIBILITY_NO_DIVIDER = PolledValue.POSSIBILITY_UNLIKELY;
		double peakPossibilityLength = calcPeakPossibilityLength(dividers);
		PolledPositiveInteger dividerPoll = new PolledPositiveInteger();

		for (Integer i : dividers) {
			dividerPoll.addChoice(i, possibilityFunction(i, dividers.size(), peakPossibilityLength));
		}
		for (int i : possibleLengths) {
			if (!dividers.contains(i))
				dividerPoll.addChoice(i, POSSIBILITY_NO_DIVIDER);
		}

		return dividerPoll;
	}

	private TranspositionAnalysisConclusion calculateConclusion(PolledPositiveInteger result, int cipherlength) {
		/**
		 * Fälle: * -1- Primzahl * 0 - keine Ergebnisse * 4 - nur 1 Ergebnis! * 1 -
		 * klares Ergebnis: nur 2-3 Ergebnisse * 2 - eher unklares Ergebnis: 4-oo
		 * Ergebnisse
		 *
		 *
		 * Text: * Eigentlich alle Längen gleich wahrscheinlich, jedoch: hohe
		 * Schlüssellängen unsicher, niedrige Schlüssellängen ebenfalls. Längen von 1-10
		 * immer aufzählen. Grundsätzlich möglich sind ebenfalls: <alle
		 * in @alsoStarring>
		 *
		 */

		PolledPositiveInteger resultOnlyLikely = result.clone();
		StringBuilder text = new StringBuilder();
		List<TranspositionAnalysisResultAtom> atoms = new LinkedList<TranspositionAnalysisResultAtom>();

		Set<Integer> removeSet = new HashSet<Integer>();
		// remove lengths that have "unlikely" possibility
		for (Integer i : resultOnlyLikely.getPollSubjects()) {
			if (resultOnlyLikely.getPossibility(i) < PolledValue.POSSIBILITY_DEFAULT)
				removeSet.add(i);
		}
		for (Integer i : removeSet)
			resultOnlyLikely.removeChoice(i);

		int conclusionCase = -1;

		if (isPrime(cipherlength))
			conclusionCase = -1;
		else if (resultOnlyLikely.getPollSubjects().size() < 1)
			conclusionCase = 0;
		else if (resultOnlyLikely.getPollSubjects().size() == 1)
			conclusionCase = 4;
		else if (resultOnlyLikely.getPollSubjects().size() < 4)
			conclusionCase = 1;
		else if (true)
			conclusionCase = 2;

		if (conclusionCase == -1) { // case -1
			text.append(
					"The length of the ciphertext is a prime number. This means in general, that the encipherment included changes of the text length which make this analysis futile.");
		} else if (conclusionCase == 0) { // case 0
			text.append(
					"No dividers of the ciphertext length were found in the given range. Try to widen the search range the next time.");
		} else if (conclusionCase == 4) { // case 0
			text.append(
					"Only one block length comes into question: " + TranspositionAnalysisConclusion.PLACEHOLDER + ".");
			atoms.add(new TranspositionAnalysisResultAtom(
					new TranspositionKey(new int[resultOnlyLikely.getBestValue()]), true));
		} else if (conclusionCase == 1) { // case 1
			text.append(
					"Only a few dividers were found, " + "making the pool of block lengths in question not too big. "
							+ "The length to try first would be ");
			text.append(TranspositionAnalysisConclusion.PLACEHOLDER);
			// put the first best value's atom
			atoms.add(new TranspositionAnalysisResultAtom(
					new TranspositionKey(new int[resultOnlyLikely.getBestValue()]), true));
			text.append(".");
			// if there are just 2 choices, don't make much wind around it.
			if (resultOnlyLikely.getPollSubjects().size() == 2) {
				text.append(" The next best length to try would be ");
				text.append(TranspositionAnalysisConclusion.PLACEHOLDER);
				atoms.add(new TranspositionAnalysisResultAtom(
						new TranspositionKey(new int[resultOnlyLikely.getValuesSortedByPossibility().get(1)]), true));
				text.append(".");
			} else { // else count them up.
				text.append(" The next best lengths to try would be:");
				boolean first = true;
				for (Integer length : resultOnlyLikely.getValuesSortedByPossibility()) {
					if (length != resultOnlyLikely.getBestValue()) {
						atoms.add(new TranspositionAnalysisResultAtom(new TranspositionKey(new int[length]), true));
						text.append((first ? " " : ", ") + TranspositionAnalysisConclusion.PLACEHOLDER);
						first = false;
					}
				}
				text.append(".");
			}
		} else if (true) { // case 2
			// constants for dividing the result into different parts
			final double MAX_DIFF_NEAR_EQUAL_BEST = 0.15;
			final int MAX_ELEMENTS_NEAR_EQUAL_BEST = 3;

			final double MAX_DIFF_ALSO_STARRING = 0.3;
			final int MAX_ELEMENTS_ALSO_STARRING = 4;

			List<Integer> nearEqualBest = new LinkedList<Integer>();
			List<Integer> alsoStarring = new LinkedList<Integer>();

			List<Integer> sortedLengths = resultOnlyLikely.getValuesSortedByPossibility();
			double highestNearEqualBest = resultOnlyLikely.getPossibility(resultOnlyLikely.getBestValue());
			double highestAlsoStarring = Double.MIN_VALUE;

			// putting elements from the result into the different output
			// groups, following the specified parameters
			int counter = 0;
			for (Integer length : sortedLengths) {
				if (counter < MAX_ELEMENTS_NEAR_EQUAL_BEST && (resultOnlyLikely.getPossibility(length)
						/ highestNearEqualBest) > (1 - MAX_DIFF_NEAR_EQUAL_BEST)) {
					nearEqualBest.add(length);
				} else if (counter < MAX_ELEMENTS_NEAR_EQUAL_BEST + MAX_ELEMENTS_ALSO_STARRING
						&& (resultOnlyLikely.getPossibility(length) / highestAlsoStarring) > (1
								- MAX_DIFF_ALSO_STARRING)) {
					if (highestAlsoStarring == Double.MIN_VALUE)
						highestAlsoStarring = resultOnlyLikely.getPossibility(length);
					alsoStarring.add(length);
				}

				counter++;
			}

			boolean first = true;
			text.append("Many possibilities for the blocklength were found. " + "The following dividers of "
					+ "the text length should be considered as blocklengths first:");
			for (Integer length : nearEqualBest) {
				atoms.add(new TranspositionAnalysisResultAtom(new TranspositionKey(new int[length]), true));
				text.append((first ? " " : ", ") + TranspositionAnalysisConclusion.PLACEHOLDER);
				first = false;
			}
			text.append(". \n");
			if (!alsoStarring.isEmpty()) {
				text.append("Also possible:");
				first = true;
				for (Integer length : alsoStarring) {
					atoms.add(new TranspositionAnalysisResultAtom(new TranspositionKey(new int[length]), true));
					text.append((first ? " " : ", ") + TranspositionAnalysisConclusion.PLACEHOLDER);
					first = false;
				}
				text.append(".");
			}
		}

		conclusion = new TranspositionAnalysisConclusion(text.toString(), atoms);
		return conclusion;

	}

	public boolean isPrime(int number) {
		for (int i = 2; i < number; i++) {
			if ((number % i) == 0) {
				return false;
			}
		}
		return true;
	}

	public void setInput(TranspositionAnalysisInput in) {
		this.in = in;
	}

	@Override
	public void analyze() {
		Set<Integer> possibleLengths = out.getKeylengthPoll().getPollSubjects();

		// calculate dividers
		List<Integer> dividers = new LinkedList<Integer>();
		for (int i : possibleLengths) {
			if (in.getCiphertext().length() % i == 0 && out.getKeylengthPoll().hasChoice(i))
				dividers.add(i);
		}

		pureResult = calculatePossibilitiesIntoOutputFromLengthAndDividers(dividers, in.getCiphertext().length(), out,
				possibleLengths);
		calculateConclusion(pureResult, in.getCiphertext().length());
	}

	@Override
	public void combineResultsWithOutput() {
		out.getKeylengthPoll().combineWith(pureResult);
	}

	@Override
	public TranspositionAnalysisConclusion getConclusion() {
		return conclusion;
	}

	@Override
	public String getAnalysisDescription() {
		return "This analysis finds dividers of the ciphertext length, which are possible blocklengths.";
	}

	@Override
	public String getAnalysisName() {
		return "Cipherlength divider analysis";
	}

	@Override
	public boolean isObligatory() {
		return true;
	}

	@Override
	public void resetConclusion() {
		conclusion = null;
	}

}
