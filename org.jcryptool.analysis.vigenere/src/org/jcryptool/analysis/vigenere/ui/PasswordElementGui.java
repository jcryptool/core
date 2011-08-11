/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import javax.swing.UIManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.vigenere.views.VigenereBreakerView;

public class PasswordElementGui {
    private static final int LIMIT = 10;
    private static final int WIDTH = 40;
    private static final int HEIGHT = 20;
    private static final int BORDER = 10;
    private boolean found = false;
    private Composite comp;
    private Text text;
    private int charnumber;
    private PasswordContainer contain;

    public PasswordElementGui(final Composite parent, final int number,
            PasswordContainer container) {
        this.setNumber(number);
        this.contain = container;
        init(parent);
    }

    // calculates the position of the text field with current
    // character number. LIMIT shows number of possible elements.
    // Modulo represents position; if modulo 0, then it's last
    // in line.
    private void init(final Composite parent) {
        // calculation for positioning of text fields:

        // save modulo of current character number.
        int mod = charnumber % LIMIT;
        // save value of division with integer (always rounds towards
        // lesser values)
        int rd = charnumber / LIMIT;
        // determine absolute position of element. modulo 0 indicates
        // current element is multiple of LIMIT and should be positioned
        // as last element of current line; otherwise position equals
        // modulo.
        int pos = 0 == mod ? LIMIT : mod;
        // determine line number of this element. modulo 0 indicates
        // current element is multiple of LIMIT. therefore the result
        // of integer division is exact (no rounding towards lesser
        // integer value). in this case increasing the result by 1
        // is not necessary.
        int line = 0 == mod ? rd : rd + 1;

        // 4 examples with LIMIT = 5:
        // (1) character number: 3
        // mod: 3 % 5 = 3 (due to mod != 0) --> 3rd position
        // rd : 3 / 5 = 0 (due to mod != 0: 0 + 1) --> 1st line
        // (2) character number: 5
        // mod: 5 % 5 = 0 (due to mod == 0) --> position of limit; 5th
        // rd : 5 / 5 = 1 (due to mod == 0: 1 + 0) --> 1st line
        // (3) character number: 9
        // mod: 9 % 5 = 4 (due to mod != 0) --> 4th position
        // rd : 9 / 5 = 1 (due to mod != 0: 1 + 1) --> 2nd line
        // (4) character number: 15
        // (1) mod: 15 % 5 = 0 (due to mod == 0) --> position of limit; 5th
        // (2) rd : 15 / 5 = 3 (due to mod == 0: 3 + 0) --> 3rd line

        // calculate x and y for positioning
        int x = pos * BORDER + (pos - 1) * WIDTH;
        int y = line * BORDER + (line - 1) * HEIGHT;

        {
            comp = new Composite(parent, SWT.NONE);
            FormLayout compositeLayout = new FormLayout();
            FormData compositeLData = new FormData();
            compositeLData.left = new FormAttachment(0, 1000, x);
            compositeLData.top = new FormAttachment(0, 1000, y);
            compositeLData.width = WIDTH;
            compositeLData.height = HEIGHT;
            comp.setLayoutData(compositeLData);
            comp.setLayout(compositeLayout);
            comp.setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_HAND));
            comp.addMouseListener(new MouseAdapter() {
                public void mouseDown(MouseEvent event) {
                    focus();
                }
            });
            {
                text = new Text(comp, SWT.CENTER | SWT.BORDER);
                FormData textLData = new FormData();
                textLData.left = new FormAttachment(0, 1000, 0);
                textLData.top = new FormAttachment(0, 1000, 0);
                textLData.width = 28;

                String s = UIManager.getSystemLookAndFeelClassName();

                // change height of test field when gtk+ is used.
                if (s.endsWith(VigenereBreakerView.GTK)) {
                    textLData.height = 11;
                } else {
                    textLData.height = 14;
                }

                text.setLayoutData(textLData);
                text.setText(String.valueOf(charnumber));
                text.setBackground(new Color(parent.getDisplay(), 250, 250, 250));
                text.setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_HAND));
                text.setEditable(false);
                text.setEnabled(false);
            }
        }
    }

    private void focus() {
        contain.showAnalysis(charnumber);
    }

    /**
     * @param character
     *            the character to set
     */
    private void setNumber(int character) {
        this.charnumber = character;
    }

    /**
     * @return the character
     */
    public int getNumber() {
        return charnumber;
    }

    protected void select() {
        // old color.
        // text.setBackground(SWTResourceManager.getColor(180, 250, 255));
        text.setBackground(new Color(null, 55, 155, 255));
    }

    protected void unselect() {
        if (found) {
            text.setBackground(new Color(null, 196, 255, 196));
        } else {
            text.setBackground(new Color(null, 250, 250, 250));
        }
    }

    protected void hightlight(final String character) {
        found = true;
        text.setText(character);
        text.setBackground(new Color(null, 196, 255, 196));
    }

    public boolean isFound() {
        return found;
    }

    protected String getCharacter() {
        return text.getText();
    }
}
