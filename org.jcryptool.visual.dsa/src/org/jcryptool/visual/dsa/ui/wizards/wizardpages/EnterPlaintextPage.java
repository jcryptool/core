// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.dsa.ui.wizards.wizardpages;

import static org.jcryptool.visual.library.Lib.CHARACTERS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.dsa.Action;
import org.jcryptool.visual.dsa.DSAData;
import org.jcryptool.visual.library.Lib;

/**
 * Page for entering a plaintext to sign or encrypt.
 * @author Michael Gaber
 */
public class EnterPlaintextPage extends TextWizardPage {

	private static final int TEXTLIMIT = 150;

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "Enter Plaintext Page";

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = "Klartext eingeben.";

	/** the action of this run, decides whether to display the hash-method description. */
	private final Action action;

	/** common data object to store the entries. */
	private final DSAData data;

	/**
	 * Constructor setting the data and the action.
	 * @param action the cryptographic action
	 * @param data the shared data object
	 */
	public EnterPlaintextPage(final Action action, final DSAData data) {
		super(PAGENAME, TITLE, null);
		this
				.setDescription("Bitte geben Sie den zu verschlüsselnden bzw. zu signierenden Text in das vorgesehene Feld ein.");
		this.setPageComplete(false);
		this.action = action;
		this.data = data;
	}

	/**
	 * sets up all the UI stuff.
	 * @param parent the parent composite
	 */
	public final void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		// do stuff like layout et al
		composite.setLayout(new GridLayout());
		Label label;
		if (action == Action.SignAction) {
			label = new Label(composite, SWT.WRAP);
			label
					.setText("Das hier angewendete Hashverfahren ist ein stark vereinfachtes Verfahren:\nDie Buchstaben der Nachricht werden zunächst in Ihre ASCII-Werte übersetzt und anschließend modulo N aufaddiert.\nDas Ergebnis ist ein Hashwert der Nachricht, der anschließend für die Siganturerstellung genutzt wird.\nFür reale Anwendungen wird statt dieses einfachen Verfahrens ein Hashverfahren wie SHA-1 verwendet, das bessere kryptographische Sicherheit bietet.");

			// separator
			new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false));
		}
		label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		label.setText("Texteingabe");
		text = new Text(composite, SWT.BORDER | SWT.WRAP);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setTextLimit(TEXTLIMIT);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				setPageComplete(!((Text) e.widget).getText().equals(""));
			}
		});
		text.addVerifyListener(Lib.getVerifyListener(CHARACTERS));
		if (action == Action.SignAction) {
			final Button SHA1Checkbox = new Button(composite, SWT.CHECK);
			SHA1Checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
			SHA1Checkbox.setText("SHA-1 verwenden");
			SHA1Checkbox
					.setToolTipText("Wählen Sie dieses Feld aus, wenn Sie statt des einfachen Hashverfahrens SHA-1 verwenden möchten");
			SHA1Checkbox.addSelectionListener(new SelectionListener() {

				public void widgetSelected(final SelectionEvent e) {
					data.setSimpleHash(!SHA1Checkbox.getSelection());
				}

				public void widgetDefaultSelected(final SelectionEvent e) {
					// won't be called
				}
			});
			SHA1Checkbox.setSelection(!data.getSimpleHash());
		}
		// fill in old data
		text.setText(data.getPlainText());

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
