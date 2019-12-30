//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Listener for selecting file in Tab "User" - Sign files or text
 * 
 * @param lbl_selected_file label for shwowing the name of the selected file
 * @param txt_text text area for typing in text to sign
 * @param deselect button for deselect selected file
 **/
public class SelectFileListener implements SelectionListener {
    private Label lbl_selected_file;

    public SelectFileListener(Label lbl_selected_file, Text txt_text) {
        this.lbl_selected_file = lbl_selected_file;
    }

    /**
     * Function if select file button is clicked if a file is selected, disable text area and show button to deselect
     * file if a file is deselected enable text area and set button for delesection visible(false)
     * 
     * @param e triggered event
     **/
    @Override
    public void widgetSelected(SelectionEvent e) {
        FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        String path = ""; //$NON-NLS-1$
        path = fd.open();
        lbl_selected_file.setText(path);
        lbl_selected_file.getParent().layout();
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }
}
