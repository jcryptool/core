// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.PaillierData;
import org.jcryptool.visual.library.Lib;

/**
 * Wizard to enter an initial number for Paillier homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class PaillierChooseInitialTextPage extends WizardPage{
    
    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "Choose paillier initial text Page";
    
    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = Messages.GHChooseIntialTextPage_Title + " (Paillier)";
    
    /** holds the entered text */
    private Text text, usedText;
    
    /** holds the data */
    private PaillierData data;
    
    public PaillierChooseInitialTextPage(PaillierData data) {
    	super(PAGENAME, TITLE, null);
    	this.data = data;
    	this.setDescription(Messages.GHChooseIntialTextPage_Description);
    	this.setPageComplete(false);
    }
    
    public final void createControl(final Composite parent) {
    	final Composite composite = new Composite(parent, SWT.NONE);
    	GridLayout gl_composite = new GridLayout(2, false);
    	composite.setLayout(gl_composite);
    	
        Label lblN = new Label(composite, SWT.NONE);
        lblN.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        lblN.setText("N:");
        
        Text modulus = new Text(composite, SWT.BORDER | SWT.H_SCROLL);
        GridData gd_modulus = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_modulus.widthHint = 250;
        modulus.setLayoutData(gd_modulus);
        modulus.setText(data.getPubKey()[0].toString());
        modulus.setEditable(false);
        
        Label lblFirstOperand = new Label(composite, SWT.NONE);
        GridData gd_lblFirstOperand = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        lblFirstOperand.setLayoutData(gd_lblFirstOperand);
        lblFirstOperand.setText(Messages.GHChooseIntialTextPage_Textfield + " N" + Messages.GHChooseIntialTextPage_Textfield2);
        
        text = new Text(composite, SWT.BORDER | SWT.WRAP);
        GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd_text.widthHint = 250;
        gd_text.heightHint = 200;
        text.setLayoutData(gd_text);
        text.setFocus();
        
        Label lblUsedText = new Label(composite, SWT.NONE);
        lblUsedText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1)	);
        lblUsedText.setText(Messages.GHChooseOperationTextPage_UsedText);
        
        usedText = new Text(composite, SWT.BORDER | SWT.H_SCROLL);
        GridData gd_usedText = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd_usedText.widthHint = 250;
        usedText.setLayoutData(gd_usedText);
        usedText.setEditable(false);
        
        text.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                setPageComplete(!((Text) e.widget).getText().equals("")); //$NON-NLS-1$
                data.setPlain(new BigInteger(((Text) e.widget).getText()));
                usedText.setText(data.getPlain().mod(data.getPubKey()[0]).toString());
            }
        });
        text.addVerifyListener(Lib.getVerifyListener(Lib.DIGIT));
        
        setControl(composite);
    }
    
    public static String getPagename() {
        return PAGENAME;
    }

}
