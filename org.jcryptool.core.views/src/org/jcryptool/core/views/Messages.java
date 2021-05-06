//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.core.views.messages"; //$NON-NLS-1$
	public static String AlgorithmView_0;
    public static String AlgorithmView_menu_text_algorithms;
	public static String AlgorithmView_menu_text_analysis;
	public static String AlgorithmView_menu_text_games;
	public static String AlgorithmView_menu_text_visuals;
	public static String AlgorithmView_search_message;
	public static String AlgorithmView_showPalette;
	public static String AlgorithmView_showTree;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
