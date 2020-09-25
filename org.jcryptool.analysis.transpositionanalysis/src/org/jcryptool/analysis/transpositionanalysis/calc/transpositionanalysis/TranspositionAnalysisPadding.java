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

import java.util.LinkedList;

import org.jcryptool.analysis.transpositionanalysis.calc.PolledPositiveInteger;
import org.jcryptool.analysis.transpositionanalysis.calc.PolledValue;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisConclusion;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisResultAtom;

public class TranspositionAnalysisPadding extends TranspositionAnalysis {

	private TranspositionAnalysisPaddingInput in;
	private int resultMinBlocklength = 0;
	private TranspositionAnalysisConclusion conclusion;
	private static final double POSSIBILITY_LESS_THAN_MINLENGTH = PolledValue.POSSIBILITY_VERY_UNLIKELY
			* PolledValue.POSSIBILITY_SLIGHTLY_UNDER_DEFAULT;

	@Override
	public void analyze() {
		resultMinBlocklength = in.getSelectedPaddingLengthFromEnd();
		calcConclusion();
	}

	private void calcConclusion() {
		if (resultMinBlocklength > 1) {
			conclusion = new TranspositionAnalysisConclusion(
					"Following the user evaluation of the padding at the end of "
							+ "the ciphertext, keylengths less than " + resultMinBlocklength
							+ " are considered not very likely.",
					new LinkedList<TranspositionAnalysisResultAtom>());
		} else {
			conclusion = new TranspositionAnalysisConclusion(
					"Because no big enough padding was detected, this analysis was not able "
							+ "to make meaningful statements about the block length.",
					new LinkedList<TranspositionAnalysisResultAtom>());
		}

	}

	@Override
	public void combineResultsWithOutput() {
		PolledPositiveInteger lengthPoll = out.getKeylengthPoll();
		for (int length : lengthPoll.getPollSubjects()) {
			if (length < resultMinBlocklength)
				lengthPoll.combinePossibility(length, POSSIBILITY_LESS_THAN_MINLENGTH);
		}
	}

	@Override
	public TranspositionAnalysisConclusion getConclusion() {
		return conclusion;
	}

	public void setInput(TranspositionAnalysisPaddingInput in) {
		this.in = in;
	}

	/**
	 * Recognition of very simple paddings - Returns the distance of the first
	 * recognized padding symbol to the end of the sample text.<br />
	 * This only a rough estimation, and should not be taken directly into an
	 * autmatic analysis.
	 * 
	 * @param sampletext a sample text where a sampletext occurs. Should be made as
	 *                   short as possible.
	 * @return
	 */
	public static int guessPaddingDistanceFromEndOfSampletext(String sampletext) {
		PolledValue<Character> paddingSymbol = new PolledValue<Character>();
		for (char c : sampletext.toCharArray()) {
			paddingSymbol.addChoice(c);
		}

		// Make special starting possibilities for frequent padding characters
		// like '0', 'X', ...
		PolledValue<Character> specialPaddingSymbols = paddingSymbol.cloneWithDefaultPossibilities();
		specialPaddingSymbols.addChoice('0', PolledValue.POSSIBILITY_HIGHLY_LIKELY);
		specialPaddingSymbols.addChoice('X', PolledValue.POSSIBILITY_HIGHLY_LIKELY);
		specialPaddingSymbols.addChoice('x', PolledValue.POSSIBILITY_HIGHLY_LIKELY);
		specialPaddingSymbols.addChoice('#', PolledValue.POSSIBILITY_HIGHLY_LIKELY);
		specialPaddingSymbols.addChoice(' ', PolledValue.POSSIBILITY_HIGHLY_LIKELY);
		specialPaddingSymbols.addChoice('*', PolledValue.POSSIBILITY_HIGHLY_LIKELY);
		specialPaddingSymbols.addChoice('+', PolledValue.POSSIBILITY_HIGHLY_LIKELY);
		specialPaddingSymbols.addChoice('~', PolledValue.POSSIBILITY_HIGHLY_LIKELY);
		specialPaddingSymbols.addChoice('?', PolledValue.POSSIBILITY_HIGHLY_LIKELY);

		// With every occurence, each character gets more likely
		for (char c : sampletext.toCharArray()) {
			paddingSymbol.combinePossibility(c, PolledValue.POSSIBILITY_LIKELY);
		}
		paddingSymbol.combineWith(specialPaddingSymbols);

		// Return the distance from the first padding symbol to the end.
		Character finalPaddingChar = paddingSymbol.getBestValue();
		if (finalPaddingChar != null)
			return sampletext.length() - sampletext.indexOf(finalPaddingChar);
		return 0;
	}

	/**
	 * Function for pre-computing a piece from the ciphertext where the padding can
	 * be found (usually this comes from the very end of the text and is 1-40
	 * letters long) <br />
	 * Should have set the output set before for autmatic detection of the possible
	 * keylengths.
	 * 
	 * @return a piece of the ciphertext
	 */
	public static String getPaddingExcerpt(Integer blocklength, String ciphertext) {
		final int paddingExcerptLength = (blocklength == null || blocklength < 2) ? 20 : blocklength;

		return ciphertext.substring(Math.max(0, ciphertext.length() - paddingExcerptLength));
	}

	@Override
	public String getAnalysisDescription() {
		return "Searches for Padding characters at the end of the ciphertext and draws conclusions from it about the blocklength (later: key positions, too).";
	}

	@Override
	public String getAnalysisName() {
		return "Padding analysis";
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
