// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.input;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

/**
 * This input is for getting booleans from SWT buttons. registers itself as listener to the button.
 * 
 * @author Simon L
 */
public abstract class ButtonInput extends AbstractUIInput<Boolean> {

    public ButtonInput() {
        super();
        addListeners();
    }

    /**
     * Sets the listeners which signal the ButtonInput that content has to be synchronized with the user side
     */
    protected void addListeners() {
        getButton().addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                synchronizeWithUserSide();
            }
        });
    }

    /**
     * @return the button this ButtonInput is about.
     */
    public abstract Button getButton();

    @Override
    public Boolean readContent() {
        return this.getButton().getSelection();
    }

    @Override
    public void writeContent(Boolean content) {
        this.getButton().setSelection(content);
    }

}
