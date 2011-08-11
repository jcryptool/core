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

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class DefaultScanner extends RuleBasedScanner {
    public static final RGB defaultRGB = new RGB(75, 75, 75);
    private Color defaultColor;

    public DefaultScanner() {
        IRule[] rules = new IRule[0];

        setRules(rules);
    }

    public Color getDefaultColor() {
        if (defaultColor == null) {
            defaultColor = new Color(Display.getCurrent(), defaultRGB);
        }
        return defaultColor;
    }
}
