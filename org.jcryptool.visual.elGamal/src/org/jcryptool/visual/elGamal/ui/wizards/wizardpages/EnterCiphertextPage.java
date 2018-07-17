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

import java.math.BigInteger;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.library.Lib;

/**
 * page for entering a ciphertext to decrypt.
 * @author Michael Gaber
 * @author Thorben Groos
 */
public class EnterCiphertextPage extends TextWizardPage {

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "Enter Ciphertext Page"; //$NON-NLS-1$

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = Messages.EnterCiphertextPage_enter_ciphertext;

	/** shared data object to save the entries made on this page. */
	private final ElGamalData data;

	/**
	 * Constructor setting up the data object and description.
	 * @param data the data object
	 */
	public EnterCiphertextPage(final ElGamalData data) {
		super(PAGENAME, TITLE, null);
		this.data = data;
		setDescription(Messages.EnterCiphertextPage_ciphertext_text);
		setPageComplete(false);
	}

	/**
	 * sets up all the UI stuff.
	 * @param parent the parent composite
	 */
	@Override
	public final void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		// do stuff like layout et al
		GridLayout gl_composite = new GridLayout();
		gl_composite.marginWidth = 50;
		composite.setLayout(gl_composite);
		final Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		label.setText(Messages.EnterCiphertextPage_textentry);
		
		text = new Text(composite, SWT.BORDER | SWT.WRAP);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				final String trimmed = ((Text) e.widget).getText().replaceAll(Lib.WHITESPACE, ""); //$NON-NLS-1$
				final boolean leer = trimmed.equals(""); //$NON-NLS-1$
				if (!leer) {
					for (String s : text.getText().trim().split(" ")) { //$NON-NLS-1$
						if (new BigInteger(s).compareTo(data.getModulus()) >= 0) {
							setErrorMessage(Messages.EnterCiphertextPage_error_param_gt_mod);
							setPageComplete(false);
							return;
						}
					}
					setErrorMessage(null);
				}
				setPageComplete(!leer);
			}
		});
		text.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				// Hopefully all 123 or 12 1234 342 or 1
				// at least one digit and then a space and another digit and so on.
				if (e.text.matches("^(\\d*\\s+)*\\d*$")) {
					e.doit = true;
				} else {
					e.doit = false;
				}
			}
		});

		// fill in old data
		String oldValues  = "";
		for (Iterator<Integer> it = data.getCipherTextAsNumbers().iterator(); it.hasNext();) {
			oldValues += it.next() + " ";
		}
		text.setText(oldValues);

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
}
