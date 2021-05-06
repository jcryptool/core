// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

/**
 * This class provides a text box designed for hexadecimal input.
 * 
 * Only digits and the characters a to f can be entered.
 * 
 * @author Nils Reimers
 * 
 */
public class HexTextbox extends Text {
    private int numBytes;
    private char fillValue;

    private char[] text;
    private boolean showToolTipOnError = true;
    private ToolTip errorToolTip;

    public HexTextbox(Composite parent, int style, int numBytes, char defaultValue) {
        super(parent, style);

        this.numBytes = numBytes;
        this.fillValue = defaultValue;
        this.text = new char[2 * numBytes];
        for (int i = 0; i < text.length; i += 2)
            text[i] = text[i + 1] = defaultValue;

        errorToolTip = new ToolTip(parent.getShell(), SWT.BALLOON | SWT.ERROR);
        errorToolTip.setText(Messages.HexTextbox_0);
        errorToolTip.setMessage(Messages.HexTextbox_1);
        errorToolTip.setAutoHide(true);

        // textBox = new Text(parent, style | SWT.READ_ONLY);
        super.setTextLimit(3 * numBytes - 1);

        showText();
        super.addKeyListener(new HexTextboxKeyListener());
    }

    /*
     * Overridden methods from parent class. Disable their functionality, so that no one can mixes the correct behaviour
     * up
     */
    @Override
    protected void checkSubclass() {
    }

    @Override
    public void paste() {
        final Clipboard cb = new Clipboard(this.getDisplay());
        TextTransfer transfer = TextTransfer.getInstance();
        String data = (String) cb.getContents(transfer);

        for (int i = 0; i < data.length(); i++)
            enteredCharacter(data.charAt(i));
    }

    @Override
    public void setTextLimit(int limit) {
        throw new RuntimeException("setTextLimit is disabled for HexTextbox. Use setNumBytes instead"); //$NON-NLS-1$
    }

    @Override
    public void setText(String text) {
        throw new RuntimeException("setText is disabled for HexTextbox"); //$NON-NLS-1$
    }

    /********************************************************************/

    /**
     * Updates the textBox to show display the text from the char-array 'text'
     */
    public void showText() {
        int caretPos = super.getCaretPosition();
        StringBuilder builder = new StringBuilder(text.length + numBytes - 1);

        for (int i = 0; i < text.length; i += 2) {
            builder.append(text[i]);
            builder.append(text[i + 1]);
            if ((i + 2) < text.length)
                builder.append(' ');
        }

        super.setText(builder.toString());
        if (caretPos < builder.length() && caretPos > 0)
            super.setSelection(caretPos, caretPos);
    }

    /**
     * Allows to change the length of the displayed hex string
     */
    public void setNumBytes(int numBytes) {
        this.numBytes = numBytes;
        char[] tmp = text;
        text = new char[numBytes * 2];

        for (int i = 0; i < text.length; i++) {
            if (i < tmp.length)
                text[i] = tmp[i];
            else
                text[i] = fillValue;
        }

        super.setTextLimit(3 * numBytes - 1);
        showText();
    }

    /**
     * Returns the byte representation entered in the textbox
     * 
     * @return the byte value entered in the textbox
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[numBytes];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) ((Character.digit(text[2 * i], 16) << 4) + Character.digit(text[2 * i + 1], 16));

        return bytes;
    }

    public boolean getShowTooltipOnError() {
        return showToolTipOnError;
    }

    public void setShowTooltipOnError(boolean value) {
        showToolTipOnError = value;
    }

    /*
     * Handle key pressed
     */
    /**
     * Method is called when a hexadecimal character is entered
     */
    private void enteredCharacter(char character) {
        // Only hexdecimal characters are valid
        if (!(character >= 'A' && character <= 'F') && !(character >= 'a' && character <= 'f')
                && !(character >= '0' && character <= '9'))
            return;

        int caretPos = super.getSelection().x;
        if (caretPos % 3 == 2) // Caret is in front of a space
            caretPos++;

        character = Character.toUpperCase(character);
        int textPos = getTextPosition();
        if (textPos >= text.length)
            return;

        text[textPos] = character;
        showText();

        setCaretPosition(caretPos + 1, true, true);
    }

    /**
     * Returns the position in the text char array, calculated from the caret position in the textbox.
     * 
     */
    private int getTextPosition() {
        int caretPos = super.getSelection().x; // getCaretPosition();
        int spacesInBetween = caretPos / 3;

        return caretPos - spacesInBetween;
    }

    private void delete() {
        int caretPos = super.getCaretPosition();
        int textPos = getTextPosition();
        for (; textPos < text.length - 1; textPos++)
            text[textPos] = text[textPos + 1];
        text[text.length - 1] = fillValue;
        showText();
        setCaretPosition(caretPos, false, false);
    }

    private void backSpace() {
        int textPos = getTextPosition() - 1;
        if (textPos < 0) {
            return;
        }

        for (int i = textPos; i < text.length - 1; i++) {
            text[i] = text[i + 1];
        }
        text[text.length - 1] = fillValue;
        showText();
        setCaretPosition(textPos + textPos / 2, false, false);
    }

    /**
     * Set the caret to a specific position. If skipSpace is true, it skips the space if the caret is in front of a
     * space. moveRight defines the direction for skipping.
     */
    private void setCaretPosition(int newPos, boolean skipSpace, boolean moveRight) {
        newPos = Math.max(0, newPos);
        newPos = Math.min(super.getTextLimit(), newPos);

        if (skipSpace && (newPos % 3) == 2) {
            if (moveRight) {
                newPos++;
            } else {
                newPos--;
            }
        }
        super.setSelection(newPos, newPos);
    }

    /**
     * Displays the error tool tip (invalid input).
     */
    private void showErrorToolTip() {
        if (showToolTipOnError) {
            Point p = super.toDisplay(super.getSize().x - 2, 2);
            errorToolTip.setLocation(p);
            errorToolTip.setVisible(true);
        }
    }

    private class HexTextboxKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            e.doit = false;
            int caretPos = getCaretPosition();

            errorToolTip.setVisible(false);
            if (e.keyCode == SWT.ARROW_LEFT)
                setCaretPosition(caretPos - 1, false, false);
            else if (e.keyCode == SWT.ARROW_RIGHT)
                setCaretPosition(caretPos + 1, false, true);
            else if (e.character == SWT.DEL)
                delete();
            else if (e.character == SWT.BS)
                backSpace();
            else if ((e.character >= 'A' && e.character <= 'F') || (e.character >= 'a' && e.character <= 'f')
                    || (e.character >= '0' && e.character <= '9'))
                enteredCharacter(e.character);
            else if (e.character == ' ')
                setCaretPosition(getCaretPosition() + 1, false, false);
            else if (Character.isLetterOrDigit(e.character) || (e.character >= 32 && e.character <= 126))
                showErrorToolTip();
        }

        public void keyReleased(KeyEvent e) {
        }
    }
}
