// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.elGamal.Action;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;

public class NewChooseKeyTypePage extends WizardPage {
	
	private ElGamalData data;
	
	//Control elements
	private Composite content;
	private Button createPublicKey;
	private Button loadPublicKey;
	private Button createPrivateKey;
	private Button loadPrivateKey;

	public NewChooseKeyTypePage(ElGamalData data) {
		super("NewChooseKeyTypePage"); //$NON-NLS-1$
		this.data = data;
		
		setTitle(Messages.NewChooseKeyTypePage_keySelection);
		setDescription(Messages.NewChooseKeyTypePage_choseKey);
	}

	@Override
	public void createControl(Composite parent) {
		
		content = new Composite(parent, SWT.NONE);
		GridLayout gl_content = new GridLayout();
		gl_content.marginWidth = 50;
		content.setLayout(gl_content);
		
		//Button for creating a new public key
		createPublicKey = new Button(content, SWT.RADIO);
		createPublicKey.setText(Messages.NewChooseKeyTypePage_createPublicKey);
		
		//Button for loading a public key
		loadPublicKey = new Button(content, SWT.RADIO);
		loadPublicKey.setText(Messages.NewChooseKeyTypePage_loadPublicKey);
		
		//Button for creating a new keypair (public key + private key)
		createPrivateKey = new Button(content, SWT.RADIO);
		createPrivateKey.setText(Messages.NewChooseKeyTypePage_createKeypair);
		
		//Button for loading a private key
		loadPrivateKey = new Button(content, SWT.RADIO);
		loadPrivateKey.setText(Messages.NewChooseKeyTypePage_loadKeypair);
		
		//Disable the public key buttons when a keypair is required
		if (data.getAction() == Action.DecryptAction || data.getAction() == Action.SignAction) {
			createPublicKey.setEnabled(false);
			loadPublicKey.setEnabled(false);
			createPrivateKey.setEnabled(true);
			loadPrivateKey.setEnabled(true);
		}
		if (data.isStandalone()) {
			createPublicKey.setEnabled(true);
			loadPublicKey.setEnabled(false);
			createPrivateKey.setEnabled(true);
			loadPrivateKey.setEnabled(false);
		}
		
		setControl(content);
	}
	
	/**
	 * Select the page that will be displayed when pressing next dependent on which radio button has been selected.
	 */
	@Override
	public IWizardPage getNextPage() {
		if (createPublicKey.getSelection())
			return getWizard().getPage("New Public Key Page"); //$NON-NLS-1$
		if (loadPublicKey.getSelection())
			return getWizard().getPage("Load Public Key Page"); //$NON-NLS-1$
		if (createPrivateKey.getSelection())
			return getWizard().getPage("New Keypair Page"); //$NON-NLS-1$
		if (loadPrivateKey.getSelection()) 
			return getWizard().getPage("Load Keypair Page"); //$NON-NLS-1$
		//for default return the new keypair page
		return getWizard().getPage("New Keypair Page"); //$NON-NLS-1$
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return true;
	}
	
	/**
	 * Which Radiobutton is selected.
	 * @return 0: if creating a new public key is selected. 
	 * 1: if loading a public key is selcted. 
	 * 2: if creating a new keypair is selected. 
	 * 3: if loading a keypair is selected.
	 */
	public int getSelection() {
		if (createPublicKey.getSelection())
			return 0;
		if (loadPublicKey.getSelection())
			return 1;
		if (createPrivateKey.getSelection())
			return 2;
		if (loadPrivateKey.getSelection()) 
			return 3;
		return 0;
	}

}
