// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.secretsharing.views;

import java.math.BigInteger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Oryal Inel
 * @version
 */
public class SecretDialog extends TitleAreaDialog implements Constants {

	private StyledText secretText;
	private Text modulText;
	private String[] result;
	private StyleRange style;
	private Button okButton;

	/**
	 * Create the dialog
	 * @param parentShell
	 * @param result
	 * @param secret
	 */
	public SecretDialog(Shell parentShell, BigInteger secret, String[] result) {
		super(parentShell);
		this.result = result;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Label modulLabel = new Label(container, SWT.NONE);
		modulLabel.setLayoutData(new GridData(112, SWT.DEFAULT));
		modulLabel.setText(MESSAGE_PRIME_MODUL_LABEL);

		modulText = new Text(container, SWT.BORDER);
		final GridData gd_modulText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		modulText.setLayoutData(gd_modulText);
		modulText.setText(result[0]);
		modulText.setEnabled(false);

		final Label secretLabel = new Label(container, SWT.NONE);
		secretLabel.setText(MESSAGE_SECRET_LABEL);

		secretText = new StyledText(container, SWT.BORDER);
		secretText.addExtendedModifyListener(new ExtendedModifyListener() {
			public void modifyText(final ExtendedModifyEvent event) {
				style = new StyleRange();
				style.start = 0;
				style.length = secretText.getText().length();
				style.foreground = BLACK;
				style.fontStyle = SWT.BOLD;
				secretText.setStyleRange(style);

				if (okButton != null) {
					okButton.setEnabled(false);
				}
			}
		});
		secretText.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {
				result[1] = secretText.getText();
			}
		});

		final GridData gd_secretText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		secretText.setLayoutData(gd_secretText);
		secretText.setText(result[1]);

		style = new StyleRange();
		style.start = 0;
		style.length = secretText.getText().length();
		style.foreground = RED;
		style.fontStyle = SWT.BOLD;
		secretText.setStyleRange(style);

		final Button checkInputButton = new Button(container, SWT.NONE);
		checkInputButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String text = secretText.getText();
				style = new StyleRange();
				style.start = 0;
				style.length = secretText.getText().length();

				if (text.matches("[0-9]*")) {

					if (new BigInteger(secretText.getText()).compareTo(new BigInteger(modulText.getText())) < 0) {
						style = new StyleRange();
						style.start = 0;
						style.length = secretText.getText().length();
						style.foreground = GREEN;
						style.fontStyle = SWT.BOLD;
						secretText.setStyleRange(style);
						okButton.setEnabled(true);
					} else {
						style = new StyleRange();
						style.start = 0;
						style.length = secretText.getText().length();
						style.foreground = RED;
						style.fontStyle = SWT.BOLD;
						secretText.setStyleRange(style);
						okButton.setEnabled(false);
					}
				} else {
					style.foreground = RED;
					style.fontStyle = SWT.BOLD;
					secretText.setStyleRange(style);
				}
			}
		});
		final GridData gd_checkInputButton = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
		checkInputButton.setLayoutData(gd_checkInputButton);
		checkInputButton.setText(MESSAGE_VERIFY_INPUT);

		setMessage(MESSAGE_SECRET_DIALOG);
		setTitle(MESSAGE_SECRET_HEADER);
		//
		return area;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		okButton.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(335, 250);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(MESSAGE_SECRET_TITLE);
	}

}
