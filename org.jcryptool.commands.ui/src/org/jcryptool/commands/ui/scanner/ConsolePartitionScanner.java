// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.commands.ui.scanner;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class ConsolePartitionScanner extends RuleBasedPartitionScanner {
	public final static String CONSOLE_DEFAULT = "__console_default"; //$NON-NLS-1$
	public final static String CONSOLE_COMMAND = "__console_command"; //$NON-NLS-1$

	public ConsolePartitionScanner() {

		IToken consoleCommand = new Token(CONSOLE_COMMAND);

		IPredicateRule[] rules = new IPredicateRule[1];

		rules[0] = new EndOfLineRule("JCrypTool=> ", consoleCommand); //$NON-NLS-1$

		setPredicateRules(rules);
	}
}
