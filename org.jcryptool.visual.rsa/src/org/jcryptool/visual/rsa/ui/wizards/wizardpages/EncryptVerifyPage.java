// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.rsa.ui.wizards.wizardpages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;

/**
 * page to choose whether to use a new key or enter the parameters manually.
 * @author Michael Gaber
 */
public class EncryptVerifyPage extends WizardPage {

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "Encrypt/Verify Page"; //$NON-NLS-1$

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = Messages.DecryptSignPage_choose_action;

	/** Buttons for creating, loading a key or keypair. */
	private Button newKeypairButton;
	private Button existingKeypairButton;
	private Button newPubkeyButton;
	private Button existingPubkeyButton;

	/** Determine which action we got */

	
	/** selection listener that updates the buttons. */
	private final SelectionListener sl = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			getContainer().updateButtons();
		}
	};
	
	
	/**
	 * Constructor, setting name, title and description.
	 */
	public EncryptVerifyPage(RSAData data) {
		super(PAGENAME, TITLE, null);

	    switch (data.getAction()) {
         case EncryptAction:
    		this.setDescription(Messages.EncryptVerifyPage_choose_action_text_enc);
            break;
        case VerifyAction:
    		this.setDescription(Messages.EncryptVerifyPage_choose_action_text_ver);
            break;
        default:
            break;
	    }
	}

	/**
	 * sets up all the UI stuff.
	 * @param parent the parent composite
	 */
	@Override
	public final void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		
		// add enter Pubkey button
		newPubkeyButton = new Button(composite, SWT.RADIO);
		newPubkeyButton.setText(Messages.EncryptVerifyPage_manual_entry);
		newPubkeyButton.setToolTipText(Messages.EncryptVerifyPage_manual_entry_popup);
		newPubkeyButton.setSelection(true);
		newPubkeyButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		newPubkeyButton.addSelectionListener(sl);
		
		// add existing Pubkey button
		existingPubkeyButton = new Button(composite, SWT.RADIO);
		existingPubkeyButton.setText(Messages.EncryptVerifyPage_existing_key);
		existingPubkeyButton.setToolTipText(Messages.EncryptVerifyPage_existing_key_popup);
		existingPubkeyButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		existingPubkeyButton.addSelectionListener(sl);
		
		// add new Keypair button
		newKeypairButton = new Button(composite, SWT.RADIO);
		newKeypairButton.setText(Messages.ChooseKeytypePage_new_keypair);
		newKeypairButton.setToolTipText(Messages.ChooseKeytypePage_new_keypair_popup);
		newKeypairButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		newKeypairButton.addSelectionListener(sl);
		
		// add existing Keypair button
		existingKeypairButton = new Button(composite, SWT.RADIO);
		existingKeypairButton.setText(Messages.DecryptSignPage_existing_keypair);
		existingKeypairButton.setToolTipText(Messages.DecryptSignPage_existing_keypair_popup);
		existingKeypairButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		existingKeypairButton.addSelectionListener(sl);
		
        //Separator label
		Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_separator = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_separator.verticalIndent = 20;
		separator.setLayoutData(gd_separator);
		
		// Add Note
		Label selectdtext = new Label(composite, SWT.WRAP);
        selectdtext.setText(Messages.EncryptVerifyPage_note);
        GridData gd_selectdtext = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_selectdtext.widthHint = 600;
        gd_selectdtext.verticalIndent = 20;
        selectdtext.setLayoutData(gd_selectdtext);
		
		// finally set control something
		setControl(composite);
	}

	@Override
	public final IWizardPage getNextPage() {
		if (newPubkeyButton.getSelection()) {
			//return this.getWizard().getPage(NewPublicKeyPage.getPagename());
			return getWizard().getPage(NewPublicKeyPage.getPagename());
		} else if (newKeypairButton.getSelection()) {
			return getWizard().getPage(NewKeypairPage.getPagename());
		} else if (existingKeypairButton.getSelection()) {
			return getWizard().getPage(LoadKeypairPage.getPagename());
		} else {
			//return this.getWizard().getPage(LoadPublicKeyPage.getPagename());
			return getWizard().getPage(LoadPublicKeyPage.getPagename());
		}
	}
	
	/**
	 * getter for the pagename.
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

	/**
	 * convenience method for checking whether key creation is enabled or not.
	 * @return selection status of the corresponding checkbox
	 */
	public final boolean wantNewKey() {
		return (newPubkeyButton.getSelection()||newKeypairButton.getSelection());
	}
}
