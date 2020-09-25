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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jcryptool.analysis.transpositionanalysis.calc.PolledPositiveInteger;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.PolledTranspositionKey;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisConclusion;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisResultAtom;

public class TranspositionAnalysisInitialization extends TranspositionAnalysis {

	private TranspositionAnalysisInitializationInput in;
	private int beginLength;
	private int endLength;
	private TranspositionAnalysisConclusion conclusion;

	private static final int MAX_LENGTH = 20;

	@Override
	public void analyze() {
		beginLength = 1;
		int setLength = in.getMaxKeylength() == -1 ? MAX_LENGTH : in.getMaxKeylength();
		endLength = Math.min(setLength, in.getCiphertext().length());
		calcConclusion();
	}

	private void calcConclusion() {
		conclusion = new TranspositionAnalysisConclusion(
				"The analysis was initialized with the search range for keys with" + "a length between " + beginLength
						+ " and " + endLength + ".",
				new LinkedList<TranspositionAnalysisResultAtom>());
	}

	@Override
	public void combineResultsWithOutput() {
		PolledPositiveInteger lengthPoll = new PolledPositiveInteger();
		Map<Integer, PolledTranspositionKey> keys = new HashMap<Integer, PolledTranspositionKey>();
		for (int i = beginLength; i <= endLength; i++) {
			lengthPoll.addChoice(i);
			PolledTranspositionKey key = new PolledTranspositionKey();
			key.setLength(i);
			keys.put(i, key);
		}

		out.setKeylengthPoll(lengthPoll);
		out.setKeyPolls(keys);
	}

	@Override
	public TranspositionAnalysisConclusion getConclusion() {
		return conclusion;
	}

	public void setInput(TranspositionAnalysisInitializationInput in) {
		this.in = in;
	}

	@Override
	public boolean allowUserEstimatedRating() {
		return false;
	}

	@Override
	public String getAnalysisDescription() {
		return "Preparation of the analysis with a few important parameters.";
	}

	@Override
	public String getAnalysisName() {
		return "Initialization";
	}

	@Override
	public boolean isObligatory() {
		return false;
	}

	@Override
	public void resetConclusion() {
		conclusion = null;
	}

}
