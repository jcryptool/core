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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;

/**
 * page for saving a public key.
 * @author Michael Gaber
 */
public class SavePublicKeyPage extends SaveWizardPage {

	/** margin width. */
	private static final int MARGIN = 50;

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "Save Public Key Page"; //$NON-NLS-1$

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = Messages.SavePublicKeyPage_save_pubkey;

	/** shared data object. */
	private final RSAData data;

	/**
	 * constructor, sets this page incomplete and the description.
	 * @param data shared Data object
	 */
	public SavePublicKeyPage(final RSAData data) {
		super(PAGENAME, TITLE, null);
		setPageComplete(false);
		this.setDescription(Messages.SavePublicKeyPage_enter_params);
		this.data = data;
	}

	/**
	 * Set up UI stuff.
	 * @param parent the parent composite
	 */
	@Override
	public final void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		// do stuff like layout et al
		GridLayout gl_composite = new GridLayout();
		gl_composite.marginWidth = MARGIN;
		composite.setLayout(gl_composite);
		
		Label own = new Label(composite, SWT.NONE);
		own.setText(Messages.SavePublicKeyPage_enter_name);
		own.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		owner = new Text(composite, SWT.BORDER | SWT.SINGLE);
		owner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		owner.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				setPageComplete(!owner.getText().equals("")); //$NON-NLS-1$
				data.setContactName(owner.getText());
			}
		});

		// finish
		setControl(composite);
	}

	/**
	 * getter for the pagename.
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

	@Override
	public IWizardPage getNextPage() {
		return null;
	}
}
