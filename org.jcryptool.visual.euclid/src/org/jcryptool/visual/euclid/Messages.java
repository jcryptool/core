// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.euclid;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.euclid.messages"; //$NON-NLS-1$
	public static String Euclid_Input;
    public static String Euclid_Computation;
    public static String Euclid_P;
    public static String Euclid_Q;
    public static String Euclid_NextStep_Button;
    public static String Euclid_Compute_Button;
    public static String Euclid_Reset_Button;
    public static String Euclid_Long_Line;
    public static String Euclid_Short_Line;
    public static String Euclid_GCD;
    public static String Euclid_Euclidean;
    public static String Euclid_XEuclidean;
    public static String Euclid_Quotient;
    public static String Euclid_Remainder;
    public static String Euclid_ResetTable_Button;
    public static String Euclid_PrevStep_Button;
    public static String Euclid_Description_1;
    public static String Euclid_Description_2;
    public static String Euclid_Export_CSV;
    public static String Euclid_Export_Tex;
    public static String Euclid_Restart;
    public static String Euclid_ResetCanvas_Button;
    public static String Euclid_SaveDialog;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
