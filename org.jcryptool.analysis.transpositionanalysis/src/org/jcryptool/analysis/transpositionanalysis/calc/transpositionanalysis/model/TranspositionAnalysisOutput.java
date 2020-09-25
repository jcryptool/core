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

import java.util.Map;

import org.jcryptool.analysis.transpositionanalysis.calc.AnalysisOutput;
import org.jcryptool.analysis.transpositionanalysis.calc.PolledPositiveInteger;

/**
 * This class is used for making the result of the TranspositionAnalysis global
 * for all partial analysis methods. It contains <br />
 * - a Map, which maps a keylengths to PolledTranspositionKeys of this length
 * (To decide about the key itself). <br />
 * - a PolledPositiveInteger, which is used to decide about the key length.
 * 
 * @author Simon L
 * 
 */
public interface TranspositionAnalysisOutput extends AnalysisOutput {

	/**
	 * @return the map containing all keylengths->polled key - mappings.
	 */
	public Map<Integer, PolledTranspositionKey> getKeyPolls();

	/**
	 * Set the map of keylengths->polled keys.
	 * 
	 * @param keys A map, that maps possible keylengths to PolledTranspositionKeys
	 *             (of that length each!)
	 */
	public void setKeyPolls(Map<Integer, PolledTranspositionKey> keys);

	/**
	 * @return the Poll about the keylength
	 */
	public PolledPositiveInteger getKeylengthPoll();

	/**
	 * @param keylength sets the poll about the keylength
	 */
	public void setKeylengthPoll(PolledPositiveInteger keylength);

}
