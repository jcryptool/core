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
package org.jcryptool.analysis.viterbi.views;

import org.eclipse.osgi.util.NLS;

/**
 *
 * This class defines variables, which are used in the GUI. The values of this
 * variables are defined in the messges.properties and messages_de.properties.
 * This is an easy way to change text printed in the GUI, without doing anything
 * in the actual code.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.analysis.viterbi.views.messages"; //$NON-NLS-1$

	public static String DetailsComposite_0;

	public static String DetailsComposite_1;

	public static String DetailsComposite_11;

	public static String DetailsComposite_12;

	public static String DetailsComposite_13;

	public static String DetailsComposite_14;

	public static String DetailsComposite_15;

	public static String DetailsComposite_16;

	public static String DetailsComposite_17;

	public static String DetailsComposite_18;

	public static String DetailsComposite_2;

	public static String DetailsComposite_21;

	public static String DetailsComposite_22;

	public static String DetailsComposite_26;

	public static String DetailsComposite_3;

	public static String DetailsComposite_4;

	public static String DetailsComposite_5;

	public static String DetailsComposite_6;

	public static String XORComposite_tab_title;
	public static String XORComposite_description;
	public static String XORComposite_algo_header;

	public static String XORComposite_loadFile;
	public static String XORComposite_Plain1;
	public static String XORComposite_Plain1DefaultText;
	public static String XORComposite_combination_header;
	public static String XORComposite_combo_default;

	public static String XORComposite_Combination_RadioXOR;
	public static String XORComposite_Combination_RadioMOD;

	public static String XORComposite_Plain2;
	public static String XORComposite_Plain2DefaultText;
	public static String XORComposite_calculate;

	public static String XORComposite_EncodingMode_RadioHEX;
	public static String XORComposite_EncodingMode_RadioUNI;

	public static String XORComposite_next;
	public static String XORComposite_encodingmod_header;
	public static String XORComposite_cipher;
	
	public static String XORComposite_warning;
	public static String XORComposite_warning_text;

	public static String ViterbiComposite_00ShowAnalysis;

	public static String ViterbiComposite_tab_title;

	public static String ViterbiComposite_description;

	public static String ViterbiComposite_input_header;
	public static String ViterbiComposite_calculation_header;
	public static String ViterbiComposite_language_header;

	public static String ViterbiComposite_language_german;
	public static String ViterbiComposite_language_english;

	public static String ViterbiComposite_nGramLabel;
	public static String ViterbiComposite_pathLabel;
	public static String ViterbiComposite_startButton;
	public static String ViterbiComposite_result_header;

	public static String ViterbiComposite_solution1;
	public static String ViterbiComposite_solution2;

	public static String ViterbiComposite_exportButton;
	public static String ViterbiComposite_cancelButton;

	public static String ViterbiView_0;

	public static String options1tooltip;
	public static String options2tooltip;
	public static String DetailsComposite_tblclmnStep_text;
	public static String DetailsComposite_tblclmnRank_1_text;

	public static String DetailsComposite_xnew7;
	public static String DetailsComposite_lblViterbiAnalysisDetails_text;
	public static String DetailsComposite_lblCandidatePathsOf_text;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
