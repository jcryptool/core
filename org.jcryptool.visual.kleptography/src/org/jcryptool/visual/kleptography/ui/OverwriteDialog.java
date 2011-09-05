// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.kleptography.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Lets the user choose which of two saved public keys and ciphertexts to overwrite.
 */
public class OverwriteDialog extends Dialog {

	private int selection;

	public int getSelection() {
		return selection;
	}
	public void setSelection(int selection) {
		this.selection = selection;
	}

	/**
	 * Constructor. Nothing special.
	 * @param parent The active shell from which this dialog should be sourced.
	 * @param style The SWT style bits of the control.
	 */
	public OverwriteDialog (Shell parent, int style) {
		super (parent, style);
		setText(Messages.OverwriteDialog_Overwrite_Saved_Data);
	}

	/**
	 * Constructor with default style.
	 * @param parent The active shell from which this dialog should be sourced.
	 */
	public OverwriteDialog (Shell parent) {
		this (parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	/**
	 * Summons up the dialog and displays it.
	 * @return
	 */
	public int open() {
		Shell parent = getParent();
		Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(getText());
		createContents(shell);
		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		return getSelection();
	}

	/**
	 * Creates the dialog's contents: explanatory text
	 * and the appropriate three buttons.
	 * @param shell The dialog window.
	 */
	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(3, true));

		Label lText = new Label(shell, SWT.NONE);
		lText.setText(Messages.OverwriteDialog_Already_Saved_Two);
		GridData data = new GridData();
		data.horizontalSpan = 3;
		lText.setLayoutData(data);

		Button bFirst = new Button(shell, SWT.PUSH);
		bFirst.setText(Messages.OverwriteDialog_First_Set);
		data = new GridData(GridData.FILL_HORIZONTAL);
		bFirst.setLayoutData(data);

		bFirst.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setSelection(1);
				shell.close();
			}
		});

		Button bSecond = new Button(shell, SWT.PUSH);
		bSecond.setText(Messages.OverwriteDialog_Second_Set);
		data = new GridData(GridData.FILL_HORIZONTAL);
		bSecond.setLayoutData(data);

		bSecond.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setSelection(2);
				shell.close();
			}
		});

		Button bCancel = new Button(shell, SWT.PUSH);
		bCancel.setText(Messages.OverwriteDialog_Cancel);
		data = new GridData(GridData.FILL_HORIZONTAL);
		bCancel.setLayoutData(data);

		bCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setSelection(-1);
				shell.close();
			}
		});

		shell.setDefaultButton(bCancel);
	}
}
