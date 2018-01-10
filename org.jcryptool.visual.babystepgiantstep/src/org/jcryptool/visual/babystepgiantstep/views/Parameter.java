//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.babystepgiantstep.views;

import java.math.BigInteger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Miray Inel
 * 
 */
public class Parameter extends TitleAreaDialog {

	private Text textGroup;
	private Text textGenerator;
	private Text textGroupElement;

	private String groupValue;
	private String generatorValue;
	private String groupElementValue;

	private VerifyListener vl_numbers = new VerifyListener() {

		@Override
		public void verifyText(VerifyEvent e) {
			Text textField = null;
			e.doit = true;

			if (e.getSource() instanceof Text) {
				textField = (Text) e.getSource();
			}

			if (textField == null
					|| ((textField.getText().length() == 0 && e.text.compareTo("0") == 0) || (textField.getSelection().x == 0 && e.keyCode == 48))) { //$NON-NLS-1$
				e.doit = false;
				return;
			}

			String text = e.text;
			char[] chars = text.toCharArray();

			for (int i = 0; i < chars.length; i++) {
				if (chars.length > 1 && i == 0 && chars[i] == '0') {
					e.doit = false;
					break;
				}

				if (!Character.isDigit(chars[i])) {
					e.doit = false;
					break;
				}
			}
		}
	};

	private ModifyListener primeModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			Text tmpText = null;

			if (e.getSource() instanceof Text) {
				tmpText = (Text) e.getSource();
			}

			if (!tmpText.getText().isEmpty()) {
				BigInteger tmpTextValue = new BigInteger(tmpText.getText());
				if (tmpTextValue.compareTo(Constants.LIMIT) >= 0 || !tmpTextValue.isProbablePrime(10000)) {
					tmpText.setBackground(Constants.RED);
					if (tmpTextValue.compareTo(Constants.LIMIT) >= 0) {
						btnNextPrime.setEnabled(false);
					} else {
						btnNextPrime.setEnabled(true);
					}
				} else {
					tmpText.setBackground(Constants.WHITE);
					btnNextPrime.setEnabled(false);
				}
			} else {
				tmpText.setBackground(Constants.RED);
			}
			tmpText.setSelection(tmpText.getText().length());

			checkValues();
			checkBackgroundValues();
		}
	};

	private ModifyListener integerModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			Text tmpText = null;

			if (e.getSource() instanceof Text) {
				tmpText = (Text) e.getSource();
			}

			if (!tmpText.getText().isEmpty()) {
				BigInteger tmpTextValue = new BigInteger(tmpText.getText());
				if (tmpTextValue.compareTo(Constants.LIMIT) >= 0 || tmpTextValue.compareTo(BigInteger.ONE) == 0) {
					tmpText.setBackground(Constants.RED);
				} else {
					tmpText.setBackground(Constants.WHITE);
				}
			} else {
				tmpText.setBackground(Constants.RED);
			}
			tmpText.setSelection(tmpText.getText().length());
			
			checkBackgroundValues();
		}
	};

	private ModifyListener groupElementModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			Text tmpText = null;

			if (e.getSource() instanceof Text) {
				tmpText = (Text) e.getSource();
			}

			if (!tmpText.getText().isEmpty()) {
				BigInteger tmpTextValue = new BigInteger(tmpText.getText());
				if (tmpTextValue.compareTo(Constants.LIMIT) >= 0 || tmpTextValue.compareTo(BigInteger.ONE) == 0) {
					tmpText.setBackground(Constants.RED);
				} else {
					tmpText.setBackground(Constants.WHITE);
				}
			} else {
				tmpText.setBackground(Constants.RED);
			}
			tmpText.setSelection(tmpText.getText().length());

			checkValues();
			checkBackgroundValues();
		}
	};

	private Button btnNextPrime;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public Parameter(Shell parentShell, String group, String generator, String groupElement) {
		super(parentShell);

		this.groupValue = group;
		this.generatorValue = generator;
		this.groupElementValue = groupElement;
	}

	protected void checkValues() {
		if (textGroup.getText().isEmpty() && textGroupElement.getText().isEmpty()) {
			textGroup.setBackground(Constants.RED);
			textGroupElement.setBackground(Constants.RED);
			btnNextPrime.setEnabled(false);
			return;
		}

		if (textGroup.getText().isEmpty() && !textGroupElement.getText().isEmpty()) {
			textGroup.setBackground(Constants.RED);
			if (new BigInteger(textGroupElement.getText()).compareTo(Constants.LIMIT) >= 0
					|| new BigInteger(textGroupElement.getText()).compareTo(BigInteger.ONE) == 0) {
				textGroupElement.setBackground(Constants.RED);
			} else {
				textGroupElement.setBackground(Constants.WHITE);
			}
			btnNextPrime.setEnabled(false);
			return;
		}

		if (!textGroup.getText().isEmpty() && textGroupElement.getText().isEmpty()) {
			textGroupElement.setBackground(Constants.RED);
			if (new BigInteger(textGroup.getText()).compareTo(Constants.LIMIT) >= 0
					|| !(new BigInteger(textGroup.getText()).isProbablePrime(10000))) {
				textGroup.setBackground(Constants.RED);
				btnNextPrime.setEnabled(true);
			} else {
				textGroup.setBackground(Constants.WHITE);
				btnNextPrime.setEnabled(false);
			}
			return;
		}

		if (new BigInteger(textGroup.getText()).compareTo(Constants.LIMIT) >= 0
				|| !(new BigInteger(textGroup.getText()).isProbablePrime(10000))
				|| new BigInteger(textGroup.getText()).compareTo(BigInteger.ONE) == 0) {
			textGroup.setBackground(Constants.RED);
			if (new BigInteger(textGroup.getText()).compareTo(Constants.LIMIT) >= 0) {
				btnNextPrime.setEnabled(false);
			} else {
				btnNextPrime.setEnabled(true);
			}
		} else {
			textGroup.setBackground(Constants.WHITE);
		}

		if (new BigInteger(textGroupElement.getText()).compareTo(Constants.LIMIT) >= 0
				|| new BigInteger(textGroupElement.getText()).compareTo(BigInteger.ONE) == 0) {
			textGroupElement.setBackground(Constants.RED);
		} else {
			textGroupElement.setBackground(Constants.WHITE);
		}

		if (new BigInteger(textGroupElement.getText()).mod(new BigInteger(textGroup.getText())).compareTo(BigInteger.ZERO) == 0) {
			textGroup.setBackground(Constants.RED);
			textGroupElement.setBackground(Constants.RED);
			btnNextPrime.setEnabled(true);
		}

	}

	protected void checkBackgroundValues() {
		if (this.getButton(IDialogConstants.OK_ID) != null) {
			if (textGroup.getBackground().toString().compareTo(Constants.WHITE.toString()) == 0
					&& textGenerator.getBackground().toString().compareTo(Constants.WHITE.toString()) == 0
					&& textGroupElement.getBackground().toString().compareTo(Constants.WHITE.toString()) == 0
					&& !textGroup.getText().isEmpty()
					&& !textGroupElement.getText().isEmpty()
					&& !textGenerator.getText().isEmpty()
					&& textGroup.getText().compareTo("1") != 0 && textGenerator.getText().compareTo("1") != 0 && textGroupElement.getText().compareTo("1") != 0) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this.getButton(IDialogConstants.OK_ID).setEnabled(true);
			} else {
				this.getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
		}
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage(Messages.Parameter_0);
		setTitle(Messages.Parameter_1);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		

		Label lblGroup = new Label(container, SWT.NONE);
		lblGroup.setText(Messages.BabystepGiantstepView_4);

		textGroup = new Text(container, SWT.BORDER);
		textGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textGroup.addVerifyListener(vl_numbers);
		textGroup.addModifyListener(primeModifyListener);

		btnNextPrime = new Button(container, SWT.NONE);
		btnNextPrime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!textGroup.getText().isEmpty()) {
					BigInteger tmp = new BigInteger(textGroup.getText());
					if (!tmp.isProbablePrime(10000)) {
						tmp = tmp.nextProbablePrime();
					} else {
						tmp = tmp.add(BigInteger.ONE).nextProbablePrime();
					}

					textGroup.setText(tmp.toString());
					btnNextPrime.setEnabled(false);

					checkValues();
				}
			}
		});
		btnNextPrime.setText(Messages.Parameter_3);

		Label lblGenerator = new Label(container, SWT.NONE);
		lblGenerator.setText(Messages.BabystepGiantstepView_5);

		textGenerator = new Text(container, SWT.BORDER);
		textGenerator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		textGenerator.addVerifyListener(vl_numbers);
		textGenerator.addModifyListener(integerModifyListener);

		Label lblGroupElement = new Label(container, SWT.NONE);
		lblGroupElement.setText(Messages.BabystepGiantstepView_6);

		textGroupElement = new Text(container, SWT.BORDER);
		textGroupElement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		textGroupElement.addVerifyListener(vl_numbers);
		textGroupElement.addModifyListener(groupElementModifyListener);

		textGroup.setText(groupValue);
		BigInteger a = new BigInteger(groupValue);
		if (a.compareTo(Constants.LIMIT) > 0) {
			textGroup.setBackground(Constants.RED);
		}

		textGenerator.setText(generatorValue);
		BigInteger b = new BigInteger(generatorValue);
		if (b.compareTo(Constants.LIMIT) > 0) {
			textGenerator.setBackground(Constants.RED);
		}

		textGroupElement.setText(groupElementValue);
		BigInteger c = new BigInteger(groupElementValue);
		if (c.compareTo(Constants.LIMIT) > 0) {
			textGroupElement.setBackground(Constants.RED);
		}

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.Parameter_2, false);

		if (textGroup.getBackground().toString().compareTo(Constants.RED.toString()) == 0
				|| textGenerator.getBackground().toString().compareTo(Constants.RED.toString()) == 0
				|| textGroupElement.getBackground().toString().compareTo(Constants.RED.toString()) == 0
				|| textGroup.getText().compareTo("1") == 0 || textGenerator.getText().compareTo("1") == 0 //$NON-NLS-1$ //$NON-NLS-2$
				|| textGroupElement.getText().compareTo("1") == 0) { //$NON-NLS-1$
			this.getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}

	@Override
	protected void okPressed() {
		saveInput();

		super.okPressed();
	}

	private void saveInput() {
		groupValue = textGroup.getText();
		generatorValue = textGenerator.getText();
		groupElementValue = textGroupElement.getText();

	}

	public String getCyclicGroupValue() {
		return groupValue;
	}

	public String getGeneratorValue() {
		return generatorValue;
	}

	public String getGroupElementValue() {
		return groupElementValue;
	}

//	/**
//	 * Return the initial size of the dialog.
//	 */
//	@Override
//	protected Point getInitialSize() {
//		return new Point(480, 250);
//	}
}
