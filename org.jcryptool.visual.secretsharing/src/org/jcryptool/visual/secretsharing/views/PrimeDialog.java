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

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class PrimeDialog extends TitleAreaDialog implements Constants {

	private Button generateNextPrimeButton;
	private Button okButton;
	private Button verifyInputButton;
	private StyledText primeText;
	private BigInteger number;
	private StyleRange style;
	private String[] result;

	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public PrimeDialog(Shell parentShell, BigInteger number, String[] result) {
		super(parentShell);
		this.number = number;
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
		modulLabel.setText(MESSAGE_PRIME_MODUL_LABEL);

		primeText = new StyledText(container, SWT.BORDER);
		primeText.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {
				result[0] = primeText.getText();
			}
		});

		final GridData gd_primeText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		primeText.setLayoutData(gd_primeText);
		primeText.setText(number.toString());
		style = new StyleRange();
		style.start = 0;
		style.length = primeText.getText().length();
		style.foreground = RED;
		style.fontStyle = SWT.BOLD;
		primeText.setStyleRange(style);

		generateNextPrimeButton = new Button(container, SWT.NONE);
		generateNextPrimeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				BigInteger tmpValue = new BigInteger(primeText.getText());
				boolean isPrime = false;

				while (!isPrime) {
					tmpValue = tmpValue.add(BigInteger.ONE);
					isPrime = tmpValue.isProbablePrime(2000000);
				}
				primeText.setText(tmpValue.toString());

				style = new StyleRange();
				style.start = 0;
				style.length = primeText.getText().length();
				style.foreground = GREEN;
				style.fontStyle = SWT.BOLD;
				primeText.setStyleRange(style);

				verifyInputButton.setEnabled(false);
				okButton.setEnabled(true);
			}
		});
		generateNextPrimeButton.setText(MESSAGE_NEXT_PRIME);

		verifyInputButton = new Button(container, SWT.NONE);
		verifyInputButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String text = primeText.getText();

				style = new StyleRange();
				style.start = 0;
				style.length = primeText.getText().length();

				if (text.matches("[0-9]")) {

					BigInteger tmpValue = new BigInteger(primeText.getText());
					if (tmpValue.isProbablePrime(2000000)) {
						style.foreground = GREEN;
						okButton.setEnabled(true);
						verifyInputButton.setEnabled(false);
					} else {
						style.foreground = RED;
						verifyInputButton.setEnabled(true);
					}
					style.fontStyle = SWT.BOLD;
					primeText.setStyleRange(style);
				} else {
					style.foreground = RED;
					style.fontStyle = SWT.BOLD;
					primeText.setStyleRange(style);
				}
			}
		});

		primeText.addExtendedModifyListener(new ExtendedModifyListener() {
			public void modifyText(final ExtendedModifyEvent event) {
				style = new StyleRange();
				style.start = 0;
				style.length = primeText.getText().length();
				style.foreground = BLACK;
				style.fontStyle = SWT.BOLD;
				primeText.setStyleRange(style);

				verifyInputButton.setEnabled(true);
				if (okButton != null) {
					okButton.setEnabled(false);
				}
			}
		});

		verifyInputButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		verifyInputButton.setText(MESSAGE_VERIFY_INPUT);
		setMessage(MESSAGE_PRIME_DIALOG);
		setTitle(MESSAGE_PRIME_HEAD);
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
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

			}
		});
		okButton.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(428, 250);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(MESSAGE_PRIME_TITLE);
	}

}
