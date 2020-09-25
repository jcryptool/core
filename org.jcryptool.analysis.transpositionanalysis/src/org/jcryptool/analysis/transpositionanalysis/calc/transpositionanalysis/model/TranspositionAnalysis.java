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

import org.jcryptool.analysis.transpositionanalysis.calc.Analysis;

/**
 * Transposition Analysis, which performs analysis steps. The output of this
 * analysis is combined with the already existing content of a given output
 * object, which should be set previous to calling the analyze-function, or it
 * will be newly created.
 * 
 * 
 * @author Simon L
 * 
 */
public abstract class TranspositionAnalysis extends Analysis {

	/**
	 * @return a textual description of the analzsis (usually 1-2 sentences).
	 */
	public abstract String getAnalysisDescription();

	protected TranspositionAnalysisOutput out;

	/**
	 * Analyzes the (previously set) input. Does not alter the output object.
	 */
	public abstract void analyze();

	/**
	 * Set the Transposition analysis output (e. g. for combining multiple analysis
	 * methods)
	 * 
	 * @param out the output object
	 */
	public void setOutput(TranspositionAnalysisOutput out) {
		this.out = out;
	}

	/**
	 * Combines the analysis results with the set output, and returns it (also, the
	 * output can be retrieved by {@link #getOutput()}).
	 */
	public abstract void combineResultsWithOutput();

	/**
	 * @return the output object that was set for this analysis.
	 */
	public TranspositionAnalysisOutput getOutput() {
		return out;
	}

	/**
	 * @return a conclusion object, which contains a concise descriptive conclusion
	 *         of the single analysis step, normally inclusive the best candidates
	 *         for the solution.
	 */
	public abstract TranspositionAnalysisConclusion getConclusion();

	public abstract void resetConclusion();

	/**
	 * Returns, whether this analysis is prepared to process a user estimated
	 * analysis rating
	 * 
	 * @return
	 */
	public boolean allowUserEstimatedRating() {
		return true;
	}

	/**
	 * @return whether this analysis must be executed, or not.
	 */
	public abstract boolean isObligatory();

}
