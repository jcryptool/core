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
package org.jcryptool.commands.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.commands.core.messages"; //$NON-NLS-1$
    public static String HelpCommand_1;
	public static String HelpCommand_2;
    public static String HelpCommand_aliasesArePrefix;
	public static String HelpCommand_argdescriptionsLabel;
	public static String HelpCommand_chooseOnlyOneOptionHint;
	public static String HelpCommand_detailedHelpRefstring;
	public static String HelpCommand_helpfile;
	public static String HelpCommand_listcommandsLabel;
	public static String HelpCommand_noExampleSupport;
	public static String HelpCommand_refstring1BadCommand;
	public static String HelpCommand_refstringExamples;
	public static String HelpCommand_show_examples_label;
	public static String HelpCommand_syntaxLabel;
	public static String HelpCommand_tooManyArgs;
	public static String HelpCommand_tooFewArgs;
	public static String HelpCommand_tooManyArgsSyntaxhelpRef;
	static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
