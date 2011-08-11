//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.commands.ui.scanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ConsoleCommandScanner extends RuleBasedScanner {
    public static final RGB commandPromptRGB = new RGB(100, 100, 200);
    private Color commandPromptColor;

    public static final RGB defaultRGB = new RGB(100, 200, 100);
    private Color defaultColor;

    public ConsoleCommandScanner() {
        TextAttribute textAttribute = new TextAttribute(getCommandPromptColor(), null, SWT.BOLD);
        IToken commandPrompt = new Token(textAttribute);

        PatternRule[] rules = new PatternRule[1];

        rules[0] = new SingleLineRule("JCrypTool=>", " ", commandPrompt); //$NON-NLS-1$ //$NON-NLS-2$
        rules[0].setColumnConstraint(0);

        setRules(rules);
    }

    private Color getCommandPromptColor() {
        if (commandPromptColor == null) {
            commandPromptColor = new Color(Display.getCurrent(), commandPromptRGB);
        }
        return commandPromptColor;
    }

    public Color getDefaultColor() {
        if (defaultColor == null) {
            defaultColor = new Color(Display.getCurrent(), defaultRGB);
        }
        return defaultColor;
    }
}
