// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.GHData;
import org.jcryptool.visual.library.Lib;

/**
 * Page to enter an operation number for Gentry & Halevi fully homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class GHChooseOperationTextPage extends WizardPage{

    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "Choose GH operation text Page"; //$NON-NLS-1$

    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = Messages.GHChooseOperationTextPage_Title;

    /** holds the entered text */
    private Text text;

    /** 2-log of modulus */
    private int logMod;

    /** holds the data */
    private GHData data;

    public GHChooseOperationTextPage(int logMod, GHData data) {
    	super(PAGENAME, TITLE, null);
    	this.logMod = logMod;
    	this.data = data;
    	this.setDescription(Messages.GHChooseOperationTextPage_Description);
    	this.setPageComplete(false);
    }

    public final void createControl(final Composite parent) {
    	final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        Label label;
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.GHChooseOperationTextPage_Textfield + Integer.toString((int)Math.pow(2, logMod)) + " " + Messages.GHChooseOperationTextPage_Textfield2);
        text = new Text(composite, SWT.BORDER | SWT.WRAP);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.setTextLimit(9);

        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.GHChooseOperationTextPage_UsedText + Integer.toString((int)Math.pow(2, logMod)) + Messages.GHChooseOperationTextPage_UsedText2);

        final Text usedText = new Text(composite, SWT.BORDER | SWT.WRAP);
        usedText.setEditable(false);

        text.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                setPageComplete(!((Text) e.widget).getText().equals("")); //$NON-NLS-1$
                data.setData(Integer.parseInt(((Text) e.widget).getText()));
                usedText.setText(Integer.toString(data.getNumber()%(int)Math.pow(2, logMod)));
            }
        });
        text.addVerifyListener(Lib.getVerifyListener(Lib.DIGIT));

        setControl(composite);
    }

    public static String getPagename() {
        return PAGENAME;
    }

}
