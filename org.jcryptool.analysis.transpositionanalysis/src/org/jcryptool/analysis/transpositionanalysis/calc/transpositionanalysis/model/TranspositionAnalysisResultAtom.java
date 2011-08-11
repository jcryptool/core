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

import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;

/**
 * An atom of a transposition analysis result, meaning one possible configuration ready to be displayed. As some analysis only calculate the column count, the atom has a flag for using only the length of the stored transposition key.
 * 
 * @author Simon L
 *
 */
public class TranspositionAnalysisResultAtom {
	TranspositionKey result;
	boolean onlyAboutLength;
	
	public TranspositionAnalysisResultAtom(TranspositionKey result,
			boolean onlyAboutLength) {
		super();
		this.result = result;
		this.onlyAboutLength = onlyAboutLength;
	}

	public TranspositionKey getResult() {
		return result;
	}

	public void setResult(TranspositionKey result) {
		this.result = result;
	}

	public boolean isOnlyAboutLength() {
		return onlyAboutLength;
	}

	public void setOnlyAboutLength(boolean onlyAboutLength) {
		this.onlyAboutLength = onlyAboutLength;
	}
	
	public String toString() {
		if(onlyAboutLength) {
			return String.valueOf(result.getLength());
		} else {
			return result.toStringOneRelative();
		}
	}
	
}
