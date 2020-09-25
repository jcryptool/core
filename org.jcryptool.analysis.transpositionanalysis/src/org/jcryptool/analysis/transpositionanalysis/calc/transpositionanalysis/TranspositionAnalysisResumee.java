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
import java.util.List;

import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisConclusion;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisResultAtom;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;

public class TranspositionAnalysisResumee extends TranspositionAnalysis {

	private TranspositionAnalysisConclusion conclusion;

	@Override
	public void analyze() {
		calcConclusion();
	}

	private void calcConclusion() {
		StringBuffer buffer = new StringBuffer();
		List<TranspositionAnalysisResultAtom> atoms = new LinkedList<TranspositionAnalysisResultAtom>();

		int bestLength = out.getKeylengthPoll().getBestValue();
		TranspositionKey bestKey = out.getKeyPolls().get(bestLength).getBestChoice();

		buffer.append("Based on all executed analysis methods, the first key to try would be: "
				+ TranspositionAnalysisConclusion.PLACEHOLDER + ".");
		atoms.add(new TranspositionAnalysisResultAtom(bestKey, false));

		conclusion = new TranspositionAnalysisConclusion(buffer.toString(), atoms);
	}

	@Override
	public void combineResultsWithOutput() {

	}

	@Override
	public TranspositionAnalysisConclusion getConclusion() {
		return conclusion;
	}

	@Override
	public String getAnalysisDescription() {
		return "Combines the resuls of all executed analysis to one conclusion (not very fine-tuned, to-date, though).";
	}

	@Override
	public String getAnalysisName() {
		return "Conclusion - combined results of all executed analysis";
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
