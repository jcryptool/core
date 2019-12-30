//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2014, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.zudoku.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.games.zudoku.views.messages"; //$NON-NLS-1$
	
    // Messages for VerificationPanel
	public static String VP_NEW_SUDOKU;
	public static String VP_EXPOSE_CARDS;
	public static String VP_CHEAT;
	public static String VP_CHEAT_INFO;
	public static String VP_CHEAT_INFO_01;
	public static String VP_CHEAT_INFO_02;
	public static String VP_CHEAT_INFO_03;
	public static String VP_CHEAT_INFO_04;
	public static String VP_CHEAT_INFO_05;
	public static String VP_CHEAT_INFO_06;
	public static String VP_CHEAT_INFO_07;
	public static String VP_CHEAT_INFO_08;
	public static String VP_CHEAT_INFO_09;
	public static String VP_CHEAT_INFO_10;
	public static String VP_CHEAT_INFO_11;
	public static String VP_CHEAT_INFO_12;
	public static String VP_CHEAT_INFO_LIMIT;
	public static String VP_COVER_CARDS;
	public static String VP_CHALLENGE;
	public static String VP_PICK_ROW_COLUMN_OR_BLOCK;
	public static String VP_VERIFICATION_TAKE_CARDS;
	public static String VP_VERIFICATION_SHUFFLE_CARDS;
	public static String VP_VERIFICATION_CHECK;
	public static String VP_VERIFICATION_CHECK_OK;
	public static String VP_VERIFICATION_CHECK_FAIL;
	public static String VP_VERIFICATION_COVER;
	public static String VP_VERIFICATION_RETURN_CARDS;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}