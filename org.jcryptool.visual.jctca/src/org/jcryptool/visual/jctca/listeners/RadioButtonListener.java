//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * listener on the radiobuttons in the user view where you can sign stuff :P
 * 
 * @author mmacala
 * 
 */
public class RadioButtonListener implements SelectionListener {
    private Combo cmb_select;
    private Label lbl_selected_file;
    private Text txt_text;
    private Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
    private Color dark_gray = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
    private Button btn_select_file;
    private Boolean enabled;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Label selected_file;

    public RadioButtonListener(Combo cmb_select) {
        this.cmb_select = cmb_select;
    }

    public RadioButtonListener(Label lbl_selected_file, Text txt_text, Button btn_select_file) {
        this.lbl_selected_file = lbl_selected_file;
        this.txt_text = txt_text;
        this.btn_select_file = btn_select_file;

    }

    public RadioButtonListener(boolean enabled, Button btn1, Button btn2, Button btn3, Label selected_file,
            Text txt_text, Button btn_selected_file) {
        this.enabled = enabled;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.selected_file = selected_file;
        this.txt_text = txt_text;
        this.btn_select_file = btn_selected_file;

    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        Button btn_selected = (Button) e.getSource();
        if (btn_selected.getData().equals("generate")) { //$NON-NLS-1$
            cmb_select.setEnabled(false);
        } else if (btn_selected.getData().equals("select")) { //$NON-NLS-1$
            cmb_select.setEnabled(true);
        } else if (btn_selected.getData().equals("file")) { //$NON-NLS-1$
            lbl_selected_file.setEnabled(true);
            txt_text.setEnabled(false);
            txt_text.setForeground(dark_gray);
            btn_select_file.setEnabled(true);
        } else if (btn_selected.getData().equals("text")) { //$NON-NLS-1$
            lbl_selected_file.setEnabled(false);
            txt_text.setEnabled(true);
            // set fontcolor to black - looks like enabled
            txt_text.setForeground(black);
            btn_select_file.setEnabled(false);
        } else if (btn_selected.getData().equals("detail")) { //$NON-NLS-1$
            btn1.setEnabled(enabled);
            btn2.setEnabled(enabled);
            selected_file.setEnabled(enabled);
            // none of the two radio buttons are selected
            if (!(btn1.getSelection() || btn2.getSelection())) {
                btn2.setSelection(enabled);
                txt_text.setEnabled(enabled);
                txt_text.setForeground(black);
            }
            if (enabled == false) {
                btn3.setEnabled(enabled);
                txt_text.setForeground(dark_gray);
                txt_text.setEnabled(enabled);
            } else {
                if (btn2.getSelection()) {
                    txt_text.setForeground(black);
                    txt_text.setEnabled(enabled);
                } else if (btn1.getSelection()) {
                    selected_file.setEnabled(true);
                    btn_select_file.setEnabled(true);
                }

            }
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }
}