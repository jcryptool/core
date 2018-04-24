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
import org.jcryptool.visual.he.algo.Functions;
import org.jcryptool.visual.he.algo.GHData;
import org.jcryptool.visual.library.Lib;

/**
 * Page to enter the initial number in the Gentry & Halevi fully homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class GHChooseModulusPage extends WizardPage{
    
    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "Choose GH modulus Page"; //$NON-NLS-1$
    
    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = Messages.GHChooseModulusPage_Title;
    
    /** holds the entered text */
    private Text text;
    
    /** hols the data */
    private GHData data;
    
    public GHChooseModulusPage(GHData data) {
    	super(PAGENAME, TITLE, null);
    	this.data = data;
    	this.setDescription(Messages.GHChooseModulusPage_Description);
    	this.setPageComplete(false);
    }
    
    public final void createControl(final Composite parent) {
    	final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.GHChooseModulusPage_Textfield);
        text = new Text(composite, SWT.BORDER | SWT.WRAP);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.setTextLimit(9);
        
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.GHChooseModulusPage_UsedText);
       
        final Text usedText = new Text(composite, SWT.BORDER | SWT.WRAP);
        GridData gd_usedText = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_usedText.widthHint = 300;
        usedText.setLayoutData(gd_usedText);
        usedText.setEditable(false);
        
        text.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
            	if (((Text) e.widget).getText().equals("")) {
            		setPageComplete(false);
            	} else {
	                setPageComplete(true); //$NON-NLS-1$
	                int mod = Functions.nextPowerOfTwo(Integer.parseInt(((Text) e.widget).getText()));
	                data.setModulus(mod);
	                usedText.setText(Integer.toString((int)Math.pow(2, mod)));
            	}
            }
        });
        text.addVerifyListener(Lib.getVerifyListener(Lib.DIGIT));
        
        setControl(composite);
    }
    
    public static String getPagename() {
        return PAGENAME;
    }

}
