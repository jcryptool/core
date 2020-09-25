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

import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisInput;

public interface TranspositionAnalysisPaddingInput extends TranspositionAnalysisInput {

	/**
	 * A recognition system (e. g. the user) recognizes a padding at the end of a
	 * given sample text (usually this sample text should be retrieved by using
	 * {@link TranspositionAnalysisPadding#getPaddingExcerpt()}).
	 * 
	 * @return the distance from the beginning of the recognized padding, to the end
	 *         of the sample text.
	 */
	public int getSelectedPaddingLengthFromEnd();

}
