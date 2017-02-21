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
package org.jcryptool.visual.arc4.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.arc4.ARC4Con;
import org.jcryptool.visual.arc4.Messages;

/**
 * The wizard page that takes the user input
 * 
 * @author Luca Rupp
 */
public class ARC4WizardPage extends WizardPage {

    // the background for the page
    private Composite page;

    // the input field
    private Text input;

    // an area to display messages e.g. if the user tries to input invalid data
    private Label messages, notification;

    // how many characters have already been taken
    private int len = 0;
    
    // the org.jcryptool.visual.library.Lib class provides a similar constant, 
    // but it also allows whitespace, which is undesirable here
    private String HEXDIGIT = "[0-9a-fA-F]*";
    
    private String NOTHEXDIGIT = "[^0-9a-fA-F]*";

    /**
     * The constructor fir the ARC4WizardPage
     * 
     * @param pageName the name of the page
     * @param description the description of the page
     */
    protected ARC4WizardPage(String pageName, String description) {
        super(pageName);
        this.setTitle(pageName);
        this.setDescription(description);
        setPageComplete(false);
    }

    /**
     * Initialize the page
     */
    public void createControl(final Composite parent) {

        // create the background
        page = new Composite(parent, SWT.NONE);
        page.setLayout(new GridLayout(1, true));
        page.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        setControl(page);

        input = new Text(page, SWT.NONE);
        input.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        input.setTextLimit(ARC4Con.HEXLEN);
        input.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                e.doit = true;
                // delete all whitespace characters
                e.text = e.text.replaceAll("\\s+", "");
                // find invalid chars
                if (!e.text.matches(HEXDIGIT)) {
                    messages.setText("'" + e.character + "' " + Messages.WizardPageInvalidHexDigit);
                    // error message is printed either way, but unless the user 
                    // only typed one character it is immediately overwritten
                    if (e.text.length() == 1) {
                        e.doit = false;
                        return;
                    }
                }
                
                e.text = e.text.replaceAll(NOTHEXDIGIT, "");
                
                e.text = e.text.toLowerCase();      
                
                // get amount of text that is about to be inserted or deleted
                String olds = input.getText();
                
                String news = olds.substring(0, e.start) + e.text + olds.substring(e.end);
                
                int difference = -(olds.length() - news.length());
                
                // recalculate length
                len += difference;
                
                // the above code allows length to become bigger than 32, 
                // even if the 32 character limit of the widget itself prohibits
                // the widget from showing the characters -> limit len to 32
                len = (len > ARC4Con.HEXLEN) ? ARC4Con.HEXLEN : len;
                
                // print message, how many characters are left
                messages.setText(Messages.WizardPageInputLimitPart1 + " " 
                        + (ARC4Con.HEXLEN - len) + " " + Messages.WizardPageInputLimitPart2);
                
                // weather the page is complete
                if (len == ARC4Con.HEXLEN) {
                    setPageComplete(true);
                } else {
                    setPageComplete(false);
                }
            }
        });

        messages = new Label(page, SWT.NONE);
        messages.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        // the initial message, before any user input has taken place
        messages.setText(Messages.WizardPageInputLimitPart1 + " " + (ARC4Con.HEXLEN - len) + " "
                + Messages.WizardPageInputLimitPart2);
        
        notification = new Label(page, SWT.NONE);
        notification.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        notification.setText(Messages.WizardPageHexNotification);
    }

    /**
     * return the input from the wizard
     * 
     * @return returns an array of integers that represent the input from the user
     */
    public int[] getInput() {
        String in = this.input.getText();
        // two hex-chars for one byte
        int[] ret = new int[in.length() / 2];
        for (int a = 0; a < ret.length; a += 1) {
            // the prefix string "0x" is needed for the Integer.decode function to recognize the
            // number as hexadecimal
            ret[a] = Integer.decode("0x" + in.charAt(a * 2) + in.charAt(a * 2 + 1));
        }
        return ret;
    }

}