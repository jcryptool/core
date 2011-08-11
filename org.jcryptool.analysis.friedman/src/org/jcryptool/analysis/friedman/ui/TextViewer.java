//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.friedman.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.friedman.FriedmanPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * @author SLeischnig views a simple text in a little shell.
 */
public class TextViewer extends org.eclipse.swt.widgets.Dialog {

    private Shell dialogShell;
    private Text text1;
    private String title, text;
    private boolean visible = false;

    /**
     * creates the textviewer.
     */
    public TextViewer(final Shell parent) {
        super(parent, SWT.NONE);
    }

    /**
     * creates the text viewer, setting title and displayed text.
     *
     * @param parent the parent
     * @param pTitle title of the shell
     * @param pText displayed text
     */
    public TextViewer(final Shell parent, final String pTitle, final String pText) {
        super(parent, SWT.NONE);
        text = pText;
        title = pTitle;
    }

    /**
     * sets the title
     */
    public final void setTitle(final String pTitle) {
        title = pTitle;
    }

    /**
     * sets the displayed text
     */
    public final void setDisplayedText(final String pText) {
        text = pText;
    }

    /**
     * opens the TextViewer.
     */
    public final void open() {
        try {
            Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM);

            dialogShell.setLayout(new FormLayout());
            dialogShell.setText(title);
            dialogShell.layout();
            dialogShell.pack();
            dialogShell.setSize(99, 255);
            text1 = new Text(dialogShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
            FormData text1LData = new FormData();
            text1LData.width = 116;
            text1LData.height = 211;
            text1LData.left = new FormAttachment(0, 1000, 5);
            text1LData.top = new FormAttachment(0, 1000, 5);
            text1LData.bottom = new FormAttachment(1000, 1000, -5);
            text1LData.right = new FormAttachment(1000, 1000, -5);
            text1.setLayoutData(text1LData);
            text1.setText(text);
            text1.setEditable(false);
            dialogShell.setLocation(getParent().toDisplay(parent.getSize().x/2-dialogShell.getSize().x/2, parent.getSize().y/2-dialogShell.getSize().y/2
//                    (int) Math.round(Math.random() * 50),
//                    (int) Math.round(Math.random() * 4) + 200
                    ));
            dialogShell.open();
            visible = true;
            
            Display display = dialogShell.getDisplay();
            while (!dialogShell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
            visible = false;
        } catch (Exception e) {
            LogUtil.logError(FriedmanPlugin.PLUGIN_ID, e);
        }
    }
    
    public boolean isVisible() {
        return visible;
    }

}
