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
package org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jcryptool.analysis.transpositionanalysis.calc.PolledPositiveInteger;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.PolledTranspositionKey;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisOutput;

public class TranspositionAnalysisDataobject implements TranspositionAnalysisOutput {
	
	public Map<Integer, PolledTranspositionKey> keyPolls;
	public PolledPositiveInteger keylengthPoll;
	
	public TranspositionAnalysisInitialization iniAnalysis;
	public TranspositionAnalysisCipherlengthDividers dividerAnalysis;
	public TranspositionAnalysisPadding paddingAnalysis;
	public TranspositionAnalysisResumee resumeeAnalysis;
	
	public Map<Integer, PolledTranspositionKey> getKeyPolls() {
		return keyPolls;
	}

	public PolledPositiveInteger getKeylengthPoll() {
		return keylengthPoll;
	}

	public void setKeyPolls(Map<Integer, PolledTranspositionKey> keys) {
		this.keyPolls = keys; 
	}

	public void setKeylengthPoll(PolledPositiveInteger keylength) {
		this.keylengthPoll = keylength;
	}
	
	/**
	 * @return all analyses in the data object as list
	 */
	public List<TranspositionAnalysis> getListOfAnalyses() {
		List<TranspositionAnalysis> result = new LinkedList<TranspositionAnalysis>();
		result.add(iniAnalysis);
		result.add(dividerAnalysis);
		result.add(paddingAnalysis);
		result.add(resumeeAnalysis);
		
		return result;
	}
	
	public TranspositionAnalysisDataobject() {
		keylengthPoll = new PolledPositiveInteger();
		keyPolls = new HashMap<Integer, PolledTranspositionKey>();
		
		iniAnalysis = new TranspositionAnalysisInitialization();
		iniAnalysis.setOutput(this);
		
		dividerAnalysis = new TranspositionAnalysisCipherlengthDividers();
		dividerAnalysis.setOutput(this);
		
		paddingAnalysis = new TranspositionAnalysisPadding();
		paddingAnalysis.setOutput(this);
		
		resumeeAnalysis = new TranspositionAnalysisResumee();
		resumeeAnalysis.setOutput(this);
	}
}
