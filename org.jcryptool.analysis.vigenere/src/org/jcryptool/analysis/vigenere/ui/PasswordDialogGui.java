/* *****************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class PasswordDialogGui extends Dialog {
	private FrequencyGui parent;
	private String passphrase;
	private Text tpass;

	/**
	 * Creates the Dialog in which you can set the Password.
	 * 
	 * @param container
	 * @param pass
	 *            The suggested password.
	 */
	public PasswordDialogGui(final FrequencyGui container, final String pass) {
		super(container.getShell());
		this.parent = container;
		this.passphrase = pass;
	}

	@Override
	protected void cancelPressed() {
		super.cancelPressed();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.PassDialogGui_dialog_title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite compositeContain = new Composite(parent, SWT.NONE);
		compositeContain.setLayout(new GridLayout());

		Composite compositeContent = new Composite(compositeContain, SWT.NONE);
		compositeContent.setLayout(new GridLayout(2, true));
		compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label lpass = new Label(compositeContent, SWT.NONE);
		lpass.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		lpass.setText(Messages.PassDialogGui_label_password);

		tpass = new Text(compositeContent, SWT.BORDER);
		tpass.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		tpass.setText(passphrase);
		tpass.setFocus();
		tpass.setTextLimit(100);
		tpass.selectAll();
		
		Label separator = new Label(compositeContent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_separator = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_separator.verticalIndent = 10;
		separator.setLayoutData(gd_separator);

		compositeContain.setSize(compositeContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return compositeContain;
	}

	@Override
	protected void okPressed() {
		String in = tpass.getText();
		parent.showCompletePass(in);
		super.okPressed();
	}
}
