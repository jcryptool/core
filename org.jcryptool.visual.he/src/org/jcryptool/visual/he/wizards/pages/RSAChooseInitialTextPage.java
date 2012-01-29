// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards.pages;

import java.math.BigInteger;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.RSAData;
import org.jcryptool.visual.library.Lib;

/**
 * Wizard to enter an initial number for RSA homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class RSAChooseInitialTextPage extends WizardPage{
    
    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "Choose initial text Page";
    
    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = Messages.GHChooseIntialTextPage_Title + " (RSA)";
    
    /** holds the entered text */
    private Text text, usedText;
    
    /** hols the data */
    private RSAData data;
    
    public RSAChooseInitialTextPage(RSAData data) {
    	super(PAGENAME, TITLE, null);
    	this.data = data;
    	this.setDescription(Messages.GHChooseIntialTextPage_Description);
    	this.setPageComplete(false);
    }
    
    public final void createControl(final Composite parent) {
    	final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new RowLayout(SWT.VERTICAL));
        Label label;
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new RowData(450,18));
        label.setText(Messages.GHChooseIntialTextPage_Textfield + " N " + Messages.GHChooseIntialTextPage_Textfield2);
        Composite subComposite = new Composite(composite, SWT.NONE);
        subComposite.setLayout(new RowLayout());
        label = new Label(subComposite, SWT.NONE);
        label.setLayoutData(new RowData(30,18));
        label.setText("N:");
        Text modulus = new Text(subComposite, SWT.BORDER | SWT.H_SCROLL);
        modulus.setLayoutData(new RowData(413,18));
        modulus.setText(data.getN().toString());
        modulus.setEditable(false);
        text = new Text(composite, SWT.BORDER | SWT.WRAP);
        text.setLayoutData(new RowData(450,200));
        
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new RowData(450,18));
        label.setText(Messages.GHChooseOperationTextPage_UsedText);
        usedText = new Text(composite, SWT.BORDER | SWT.H_SCROLL);
        usedText.setLayoutData(new RowData(450,18));
        usedText.setEditable(false);
        setControl(composite);
        text.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                setPageComplete(!((Text) e.widget).getText().equals("")); //$NON-NLS-1$
                data.setPlainText(((Text) e.widget).getText());
                usedText.setText((new BigInteger(data.getPlainText())).mod(data.getN()).toString());
            }
        });
        text.addVerifyListener(Lib.getVerifyListener(Lib.DIGIT));
    }
    
    public static String getPagename() {
        return PAGENAME;
    }

}
