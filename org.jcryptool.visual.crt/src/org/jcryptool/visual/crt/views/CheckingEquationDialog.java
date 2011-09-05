// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crt.views;

import java.math.BigInteger;
import java.util.Vector;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.xeuclidean.algorithm.XEuclid;

public class CheckingEquationDialog extends TitleAreaDialog implements Constants{

	private int[] marking;
	private Button okButton;
	private Composite area;
	private ScrolledComposite scrolledComposite;
	private Group dialogGroup;
	private Vector<Equation> equationSet;
	private Label xLabel;
	private Label congruenceLabel;
	private Text textfieldA;
	private Label modLabel;
	private Text textfieldM;
	private Button suggestionButton;
	private Button verifyButton;
	private Text[] textfieldASet;
	private Text[] textfieldMSet;
	private Button[] verifyButtonSet;
	private VerifyListener[] mTextVerifyListenerSet;
	private Vector<Button> suggestionButtonSet;
	private VerifyListener mTextfieldVerifyListener;
	private BigInteger suggestionValue;

	/**
	 * Create the dialog
	 * @param parentShell
	 * @param marking
	 */
	public CheckingEquationDialog(Shell parentShell, Vector<Equation> equationSet, int[] marking) {
		super(parentShell);
		this.equationSet = equationSet;
		this.marking = marking;
		textfieldASet = new Text[equationSet.size()];
		textfieldMSet = new Text[equationSet.size()];
		verifyButtonSet = new Button[equationSet.size()];
		mTextVerifyListenerSet = new VerifyListener[equationSet.size()];
		suggestionButtonSet = new Vector<Button>();
		suggestionValue = new BigInteger("1");

	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		area = (Composite) super.createDialogArea(parent);

		scrolledComposite = new ScrolledComposite(area, SWT.V_SCROLL | SWT.BORDER);
		final GridData gd_scrolledComposite = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_scrolledComposite.heightHint = 357;
		gd_scrolledComposite.widthHint = 543;
		scrolledComposite.setLayoutData(gd_scrolledComposite);

		dialogGroup = new Group(scrolledComposite, SWT.NONE);
		dialogGroup.setText(MESSAGE_GROUP_EQUATION);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 7;
		dialogGroup.setLayout(gridLayout);
		dialogGroup.setSize(484, 220);
		scrolledComposite.setContent(dialogGroup);

		setTitle(MESSAGE_DIALOG_TITLE);
		setMessage(MESSAGE_DIALOG_INFO);
		int i = 0;
		for (Equation e : equationSet) {
			xLabel = new Label(dialogGroup, SWT.NONE);
			xLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			xLabel.setText("x");

			congruenceLabel = new Label(dialogGroup, SWT.NONE);
			congruenceLabel.setLayoutData(new GridData());
			congruenceLabel.setText("\u2261");

			textfieldA = new Text(dialogGroup, SWT.BORDER);
			final GridData gd_textfieldA = new GridData(SWT.FILL, SWT.CENTER, false, false);
			gd_textfieldA.widthHint = 90;
			textfieldA.setLayoutData(gd_textfieldA);
			textfieldA.setEnabled(false);
			textfieldA.setText(e.getTextfieldA());
			textfieldASet[i] = textfieldA;

			modLabel = new Label(dialogGroup, SWT.NONE);
			modLabel.setText("mod");

			textfieldM = new Text(dialogGroup, SWT.BORDER);
			final GridData gd_textfieldM = new GridData(SWT.FILL, SWT.CENTER, false, false);
			gd_textfieldM.widthHint = 110;
			textfieldM.setLayoutData(gd_textfieldM);
			textfieldM.setText(e.getTextfieldM());
			mTextfieldVerifyListener = new VerifyListener() {
				public void verifyText(VerifyEvent e) {
					/*
					 * keyCode == 8 is BACKSPACE and keyCode == 48 is ZERO and keyCode == 127 is DEL
					 */
					if (e.text.matches("[0-9]") || e.keyCode == 8 || e.keyCode == 127){
						if (textfieldM.getText().length() == 0 && e.text.compareTo("0") == 0){
							e.doit = false;
						}else if(textfieldM.getSelection().x == 0 && e.keyCode == 48){
							e.doit = false;
						}else{
							e.doit = true;
						}
					}else{
						e.doit = false;
					}
				}
			};
			textfieldM.addVerifyListener(mTextfieldVerifyListener);
			mTextVerifyListenerSet[i] = mTextfieldVerifyListener;
			textfieldMSet[i] = textfieldM;

			suggestionButton = new Button(dialogGroup, SWT.NONE);
			final GridData gd_suggestionButton = new GridData(SWT.FILL, SWT.FILL, false, false);
			gd_suggestionButton.heightHint = 20;
			gd_suggestionButton.widthHint = 80;
			suggestionButton.setLayoutData(gd_suggestionButton);
			suggestionButton.setText(MESSAGE_DIALOG_SUGGESTION);
			suggestionButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(final MouseEvent e) {
					int i = suggestionButtonSet.indexOf(e.widget);
					for (int j = 0; j < verifyButtonSet.length; j++) {
						if (verifyButtonSet[j].isEnabled() == true) {
							okButton.setEnabled(false);
							break;
						} else {
							okButton.setEnabled(true);
						}
					}

					textfieldMSet[i].removeVerifyListener(mTextVerifyListenerSet[i]);
					suggestionValue = new BigInteger(textfieldMSet[i].getText());
					suggestionValue = suggestionValue.add(BigInteger.ONE);
					XEuclid gcd = new XEuclid();
					boolean search = true;
					while (search) {
						for (int k = 0; k < textfieldMSet.length; k++) {
							BigInteger tmpGcd = gcd
									.xeuclid(new BigInteger(textfieldMSet[k].getText()), suggestionValue);
							if (new BigInteger(textfieldMSet[k].getText()).compareTo(suggestionValue) == 0
									|| tmpGcd.compareTo(BigInteger.ONE) != 0) {

								suggestionValue = suggestionValue.add(BigInteger.ONE);
								search = true;
								break;
							} else {
								search = false;
							}
						}
					}

					textfieldMSet[i].setText(suggestionValue.toString());

					textfieldMSet[i].addVerifyListener(mTextVerifyListenerSet[i]);
				}
			});
			suggestionButtonSet.add(suggestionButton);

			verifyButton = new Button(dialogGroup, SWT.NONE);
			final GridData gd_verifyButton = new GridData(SWT.FILL, SWT.FILL, false, false);
			gd_verifyButton.heightHint = 20;
			gd_verifyButton.widthHint = 80;
			verifyButton.setLayoutData(gd_suggestionButton);
			verifyButton.setText(MESSAGE_DIALOG_VERIFY);
			verifyButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(final MouseEvent e) {
					int verifyButtonIndex = -1;
					for (int i = 0; i < verifyButtonSet.length; i++) {
						if (verifyButtonSet[i] == e.widget) {
							verifyButtonIndex = i;
						}
					}
					for (int i = 0; i < marking.length; i++) {
						marking[i] = 0;
					}
					for (int i = 0; i < textfieldMSet.length; i++) {
						BigInteger a = new BigInteger(textfieldMSet[i].getText());
						for (int j = i + 1; j < textfieldMSet.length; j++) {
							XEuclid gcd = new XEuclid();
							BigInteger tmpValue = gcd.xeuclid(a, new BigInteger(textfieldMSet[j].getText()));
							if (tmpValue.compareTo(BigInteger.ONE) != 0) {
								marking[j] = -1;
								if (a.compareTo(new BigInteger(textfieldMSet[j].getText())) == 0) {
									marking[i] = -1;
								}
							}

						}
					}
					if (marking[verifyButtonIndex] != -1) {
						textfieldMSet[verifyButtonIndex].setEnabled(false);
						verifyButtonSet[verifyButtonIndex].setEnabled(false);
						suggestionButtonSet.get(verifyButtonIndex).setEnabled(false);
					}

					if (checkVerifyButtons()) {
						okButton.setEnabled(true);
					} else {
						okButton.setEnabled(false);
					}
				}

			});
			verifyButtonSet[i] = verifyButton;

			if (marking[i] != -1) {
				textfieldM.setEnabled(false);
				suggestionButton.setEnabled(false);
				verifyButton.setEnabled(false);
			}

			i++;
		}
		dialogGroup.pack();
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
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(final MouseEvent e) {
				for (int i = 0; i < textfieldASet.length; i++) {
					equationSet.get(i).removetextfieldMVerifyListener();
					if (equationSet.get(i).getTextfieldA().compareTo(textfieldASet[i].getText()) == 0) {
						equationSet.get(i).setTextfieldM(textfieldMSet[i].getText());
					}
					equationSet.get(i).addTextfieldMVerifyListener();
				}
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(487, 375);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(MESSAGE_DIALOG_VERIFY_INPUT);
	}

	/**
	 * checks if all verifyButtons are disabled
	 * @return true if all verifyButtons are disabled
	 */
	private boolean checkVerifyButtons() {
		boolean value = true;
		for (int j = 0; j < verifyButtonSet.length; j++) {
			if (verifyButtonSet[j].isEnabled() == true) {
				value = false;
				break;
			}
		}
		return value;
	}
}
