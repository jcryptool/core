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
package org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model;

import java.util.List;


/**
 * Represents an informal conclusion of Transposition analysis. this can be for single steps, or the whole as well.
 * <br /> It consists of a conclusion text, in which "links" are embedded that represent single,
 * usable results of the analysis. A placeholder, defined as the constant {@link #PLACEHOLDER}, must be placed in the 
 * conclusion text, to represent the sequential insertion places of the links, which are stored in a list separately.
 * 
 * @author Simon L
 *
 */
public class TranspositionAnalysisConclusion {
	public static final String PLACEHOLDER = "%%";
	
	private String conclusion = "";
	private List<TranspositionAnalysisResultAtom> resultAtoms;
	
	public TranspositionAnalysisConclusion(String conclusion,
			List<TranspositionAnalysisResultAtom> resultAtoms) {
		super();
		this.conclusion = conclusion;
		this.resultAtoms = resultAtoms;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public List<TranspositionAnalysisResultAtom> getResultAtoms() {
		return resultAtoms;
	}

	public void setResultAtoms(List<TranspositionAnalysisResultAtom> resultAtoms) {
		this.resultAtoms = resultAtoms;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String[] splitConclusion = conclusion.split(PLACEHOLDER);
		for(int i=0; i<Math.min(splitConclusion.length-1, resultAtoms.size()); i++) {
			builder.append(splitConclusion[i]);
			builder.append(resultAtoms.get(i).toString());
		}
		builder.append(splitConclusion[Math.min(splitConclusion.length-1, resultAtoms.size())]);
		
		return builder.toString();
	}
	
}
