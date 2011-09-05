// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.transposition.ui.messages"; //$NON-NLS-1$
	public static String TranspositionKeyInputComposite_columnwise;
	public static String TranspositionKeyInputComposite_inputname_read_in_order;
	public static String TranspositionKeyInputComposite_inputname_read_out_order;
	public static String TranspositionKeyInputComposite_key_instructions;
	public static String TranspositionKeyInputComposite_previewkey;
	public static String TranspositionKeyInputComposite_rowwise;
	public static String TranspositionKeyInputComposite_transpositionsteps_first_readin;
	public static String TranspositionKeyInputComposite_transpositionsteps_second_transposition;
	public static String TranspositionKeyInputComposite_transpositionsteps_third_readout;
	public static String TranspositionWizard_classic;
	public static String TranspositionWizardPage_transposition;
	public static String TranspositionWizardPage_enterkey1;
	public static String TranspositionWizardPage_firstkey_grouptitle;
	public static String TranspositionWizardPage_firstnotalter;
	public static String TranspositionWizardPage_grouptext_keys;
	public static String TranspositionWizardPage_inputname_firstkey;
	public static String TranspositionWizardPage_inputname_secondkey;
	public static String TranspositionWizardPage_secondkey_grouptitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
