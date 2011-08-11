/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorReference;

public abstract class ContentDelegator extends Composite {

    protected ContentDelegator(final Composite parent, final int style) {
        super(parent, style);
    }

    protected abstract void toFriedman(IEditorReference selection);

    protected abstract void toFrequency(int length);

    protected abstract void toDecrypt(String chiff, String plain,
            String password, int passlength);

    protected abstract void toQuick(IEditorReference selection);

    protected abstract void backSummary();

    protected abstract void backFriedman(int length);

    protected abstract void backFrequency();

    protected abstract void backFrequency(String chiff, int length,
            String phrase);
}
