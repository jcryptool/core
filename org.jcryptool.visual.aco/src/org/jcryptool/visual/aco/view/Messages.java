// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.view;

import org.eclipse.osgi.util.NLS;

public class Messages {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.aco.view.messages"; //$NON-NLS-1$

	public static String Func_allSteps;
	public static String Func_alpha;
	public static String Func_analyseCreated;
	public static String Func_analyseGiven;
	public static String Func_analysis;
	public static String Func_animation;
	public static String Func_antAnalysis;
	public static String Func_beta;
	public static String Func_cycle;
	public static String Func_doWithNewAnt;
	public static String Func_encryption;
	public static String Func_error;
	public static String Func_evap;
	public static String Func_initial_plaintext;
	public static String Func_keyLength;
	public static String Func_newAnt;
	public static String Func_permutation;
	public static String Func_plaintext;
	public static String Func_ciphertext;
	public static String Func_general;
	public static String Func_keytext;
	public static String Func_oneStep;
	public static String Func_proceed;
	public static String Func_proceedToAnalysis;
	public static String Func_reset;
	public static String Func_analyseConfiguration;
	public static String Func_analyseGroupLabel;
	public static String Func_step;
	public static String Info_description1;
	public static String Info_description2;
	public static String Info_description3;
	public static String Info_description4;
	public static String Func_possibleInputs;
	public static String Func_textLanguage;
	public static String Func_analysisType;
	public static String Func_settings;
	public static String Show_decryptedBestKnown;
	public static String Show_decryptedByAnt1;
	public static String Show_decryptedByAnt2;
	public static String Show_encryptedText;
	public static String Show_encryptionKey;
	public static String Show_permutationMatrix;
	public static String Show_pheromoneMatrix;
	public static String Show_wrongInputTextSize;
	public static String Description_title;
	public static String Description_tooltip;
	public static String Func_radioCompleteRound;
	public static String Control_language1;
	public static String Control_language2;
	public static String Control_language1_short;
	public static String Control_language2_short;
	public static String Analysis_multipleCycles;

	private Messages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
