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
package org.jcryptool.visual.viterbi.views;

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
	private static final String BUNDLE_NAME = "org.jcryptool.visual.viterbi.views.messages"; //$NON-NLS-1$

	public static String XORComposite_tab_title;
	public static String XORComposite_description;
	public static String XORComposite_algo_header;

	public static String XORComposite_loadFile;
	public static String XORComposite_Plain1;
	public static String XORComposite_Plain1DefaultText;
	public static String XORComposite_combination_header;

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

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
