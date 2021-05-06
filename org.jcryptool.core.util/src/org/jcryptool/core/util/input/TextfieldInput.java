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

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;

/**
 * This AbstractUIInput derivate already includes resetting the cursor and text in Text fields after resetting its
 * content. Also, it sets the listeners already in the constructor.
 * 
 * @see {@link AbstractUIInput}
 * 
 * @author Simon L
 */
public abstract class TextfieldInput<Content> extends AbstractUIInput<Content> {
    protected Point selectionForReset;
    protected String textForReset;
    protected long lastTimeKeyPressed;
    private boolean overrideModifyListener;

    /**
     * Creates a AbstractUIInput which listens (for now) to a given textfield, reacts when the textfield changes, and
     * resets the cursor properly when the content is reset.
     * 
     * @param textfield the text field.
     */
    public TextfieldInput() {
        super();
        addListeners();
    }

    private void addListeners() {
        getTextfield().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                synchronizeWithUserSide();
            }
        });
        getTextfield().addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent evt) {
                if (!overrideModifyListener) {
                    synchronizeWithUserSide();
                }
            }
        });
    }

    /**
     * Method that should be used to set the text of the text field instead of the textfield.setText method. This is
     * ugly, but necessary, because the modifyText-Event responds to Text.setText(..), too.
     * 
     * @param newText the text to be set without notification of the modifyText event
     */
    public void setTextfieldTextExternal(String newText) {
        overrideModifyListener = true;
        Point cursorpos = getTextfield().getSelection();
        getTextfield().setText(newText);
        getTextfield().setSelection(cursorpos);
        overrideModifyListener = false;
    }

    public void writeContent(Content content) {
        setTextfieldTextExternal(content.toString());
    };

    @Override
    protected void resetUserInput() {
        super.resetUserInput();
        setTextfieldTextExternal(textForReset);
        getTextfield().setSelection(selectionForReset);
    }

    @Override
    protected void saveRawUserInput() {
        this.textForReset = getTextfield().getText();
        this.selectionForReset = getTextfield().getSelection();
    }

    /**
     * @return the textfield this TextfieldInput is about.
     */
    public abstract org.eclipse.swt.widgets.Text getTextfield();

}
