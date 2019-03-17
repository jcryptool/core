//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sphincs.ui;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.sphincs.SphincsDescriptions;
import org.jcryptool.visual.sphincs.algorithm.*;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * Class for the Composite of Tab "Sign and Verify" It provides a GUI interface
 * to sign messages and see the signature result and to verify the signature.
 * 
 * @author Philipp Guggenberger
 * 
 * 
 * @author Klaus Kaiser
 *
 */
public class SphincsSignVerifyView extends Composite {

	private Signature signature = null;

	private Button btnSign;
	private Button btnVerify;
	private Button btnColorAuthpath;
	private Button btnColorIndex;
	private Button btnColorSignature;

	private StyledText inputText;
	private StyledText txtSignature;
	private Label txtStatus;

	private Label lblColorLabel;

	private Group inputMessageGroup;
	private Group signatureGroup;
	private GridData inputTextLayout;
	private GridData txtStatusLayout;

	boolean authFlag = false, indeFlag = false, signFlag = false, verified = false;

	private String authPath;
	private String index;
	private String horstSignature;

	private Color buttonDefault = new Color(getDisplay(), 240, 240, 240, 255);
	private Color greenish = new Color(getDisplay(), 27, 158, 119, 255);
	private Color orange = new Color(getDisplay(), 217, 95, 2, 255);
	private Color purple = new Color(getDisplay(), 116, 112, 179, 255);

	private Color statusRed = new Color(getDisplay(), 235, 59, 36, 255);
	private Color statusGreen = new Color(getDisplay(), 90, 180, 90, 255);

	public SphincsSignVerifyView(Composite parent, int style, aSPHINCS256 sphincs) {
		super(parent, style);
		this.setLayout(new GridLayout(6, true));

		// ***********************************
		// Beginning of GUI elements
		// ***********************************

		inputMessageGroup = new Group(this, SWT.NONE);
		inputMessageGroup.setLayout(new GridLayout(6, true));
		inputMessageGroup.setFont(FontService.getNormalBoldFont());
		inputMessageGroup.setText(SphincsDescriptions.SphincsSign_Group_0);

		inputMessageGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));

		new Label(this, SWT.NONE); // spacing label

		btnSign = new Button(this, SWT.NONE);
		btnSign.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnSign.setText(SphincsDescriptions.SphincsSign_Button_0);

		// sign message
		btnSign.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String message = inputText.getText();

				try {
					signature = sphincs.sign(message.getBytes());

					if (signature != null) {
						restoreForNewSign();

						authPath = signature.getAuthPath();
						horstSignature = signature.getHorstSignature();
						index = signature.getIndex();

						txtSignature.setText(authPath + "\n" + horstSignature + "\n" + index);

						if (verified == false) {
							txtStatus.setBackground(ColorConstants.white);
							txtStatus.setText("");
						}

						authFlag = false;
						indeFlag = false;
						signFlag = false;

						txtStatus.setBackground(ColorConstants.lightGray);
						txtStatus.setText(SphincsDescriptions.SphincsVerify_Generated);
						verified = false;
						setSphincsSignature(signature);
					}
				} catch (NullPointerException ex) {
					MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
					messageBoxx.setMessage(SphincsDescriptions.NoKeyText);
					messageBoxx.setText(SphincsDescriptions.Info);
					messageBoxx.open();
				}
			}
		});

		btnVerify = new Button(this, SWT.NONE);
		btnVerify.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnVerify.setText(SphincsDescriptions.SphincsVerify_Button_0);
		btnVerify.setEnabled(false);

		// verify signature
		btnVerify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (signature.getHorstSignature().equals(sphincs.sign(inputText.getText()).getHorstSignature())) {
					txtStatus.setBackground(statusGreen);
					txtStatus.setForeground(ColorConstants.black);
					txtStatus.setText(SphincsDescriptions.SphincsVerify_Success);
					verified = true;
				} else {
					txtStatus.setBackground(statusRed);
					txtStatus.setForeground(ColorConstants.white);
					txtStatus.setText(SphincsDescriptions.SphincsVerify_Fail);
					verified = false;
				}

			}
		});

		txtStatus = new Label(this, SWT.BORDER | SWT.CENTER);
		txtStatusLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		txtStatusLayout.heightHint = 25;
		txtStatus.setLayoutData(txtStatusLayout);
		txtStatus.setFont(FontService.getLargeFont());

		signatureGroup = new Group(this, SWT.NONE);
		signatureGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, SphincsConstant.H_SPAN_MAIN, 1));
		signatureGroup.setLayout(new GridLayout(8, true));
		signatureGroup.setText(SphincsDescriptions.SphincsSign_Group_1);
		signatureGroup.setFont(FontService.getNormalBoldFont());

		inputText = new StyledText(inputMessageGroup, SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI | SWT.WRAP);

		inputTextLayout = new GridData(SWT.FILL, SWT.TOP, true, true, 8, 1);
		inputTextLayout.minimumHeight = 150;
		inputTextLayout.heightHint = 235;
		inputText.setLayoutData(inputTextLayout);
		inputText.setText(SphincsDescriptions.SphincsSign_Text_0);

		lblColorLabel = new Label(signatureGroup, SWT.NONE);
		lblColorLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		lblColorLabel.setText(SphincsDescriptions.SphincsSign_Label_0);

		btnColorAuthpath = new Button(signatureGroup, SWT.NONE);
		btnColorAuthpath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int indexAuthPath = txtSignature.getText().indexOf(authPath); // get index of the authentication path

				if (indeFlag == false) {
					txtSignature.setStyleRange(
							new StyleRange(indexAuthPath, authPath.length(), greenish, txtSignature.getBackground()));
					btnColorAuthpath.setBackground(greenish);
					btnColorAuthpath.setForeground(ColorConstants.white);
					txtSignature.setFocus();
					txtSignature.setTopIndex(txtSignature.getLineAtOffset(indexAuthPath));
					indeFlag = true;
				} else {
					txtSignature.setStyleRange(new StyleRange(indexAuthPath, authPath.length(), ColorConstants.black,
							txtSignature.getBackground()));
					btnColorAuthpath.setBackground(buttonDefault);
					btnColorAuthpath.setForeground(ColorConstants.black);
					txtSignature.setFocus();
					indeFlag = false;
				}

			}
		});
		btnColorAuthpath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		btnColorAuthpath.setText(SphincsDescriptions.SphincsSign_Button_1);
		btnColorAuthpath.setEnabled(false);

		btnColorSignature = new Button(signatureGroup, SWT.NONE);
		btnColorSignature.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btnColorSignature.setText(SphincsDescriptions.SphincsSign_Button_3);
		btnColorSignature.setEnabled(false);
		btnColorSignature.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// contains the begin index of the HORST signature
				int indexHorstSignature = txtSignature.getText().indexOf(horstSignature); // contains the begin index of

				if (signFlag == false) {
					txtSignature.setStyleRange(new StyleRange(indexHorstSignature, horstSignature.length(), purple,
							txtSignature.getBackground()));
					btnColorSignature.setBackground(purple);
					btnColorSignature.setForeground(ColorConstants.white);
					txtSignature.setFocus();
					txtSignature.setTopIndex(txtSignature.getLineAtOffset(indexHorstSignature));
					signFlag = true;
				} else {
					txtSignature.setStyleRange(new StyleRange(indexHorstSignature, horstSignature.length(),
							ColorConstants.black, txtSignature.getBackground()));
					btnColorSignature.setBackground(buttonDefault);
					btnColorSignature.setForeground(ColorConstants.black);
					txtSignature.setFocus();
					signFlag = false;
				}
			}
		});

		btnColorIndex = new Button(signatureGroup, SWT.NONE);
		btnColorIndex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// get (text) index of the signature leaf (also called index)
				int indexLeafIndex = txtSignature.getText().indexOf(index);

				if (authFlag == false) {
					txtSignature.setStyleRange(
							new StyleRange(indexLeafIndex, index.length(), orange, txtSignature.getBackground()));
					btnColorIndex.setBackground(orange);
					btnColorIndex.setForeground(ColorConstants.white);
					txtSignature.setFocus();
					txtSignature.setTopIndex(txtSignature.getLineAtOffset(indexLeafIndex));
					authFlag = true;
				} else {
					txtSignature.setStyleRange(new StyleRange(indexLeafIndex, index.length(), ColorConstants.black,
							txtSignature.getBackground()));
					btnColorIndex.setBackground(buttonDefault);
					btnColorIndex.setForeground(ColorConstants.black);
					txtSignature.setFocus();

					authFlag = false;
				}

			}
		});

		btnColorIndex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btnColorIndex.setText(SphincsDescriptions.SphincsSign_Button_2);
		btnColorIndex.setEnabled(false);

		txtSignature = new StyledText(signatureGroup, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		txtSignature.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		// Disables the sign button if no message is given in the text field
		inputText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (inputText.getText().length() > 0) {
					btnSign.setEnabled(true);
				} else {
					btnSign.setEnabled(false);
				}

				verified = false;
			}
		});

		// Enables the "color" buttons if signature is given in the text field
		txtSignature.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (txtSignature.getText().length() > 0) {
					btnColorSignature.setEnabled(true);
					btnColorIndex.setEnabled(true);
					btnColorAuthpath.setEnabled(true);
					btnVerify.setEnabled(true);
				} else {
					btnColorSignature.setEnabled(false);
					btnColorIndex.setEnabled(false);
					btnColorAuthpath.setEnabled(false);
					btnVerify.setEnabled(false);
				}

			}
		});

		// ***********************************
		// End of GUI elements
		// ***********************************
	}

	public Signature getSphincsSignatur() {
		return this.signature;
	}

	public void setSphincsSignature(Signature signature) {
		this.signature = signature;
	}

	/**
	 * Set back UI elements of page when new signature is created
	 * 
	 * - clear output - reset colors of buttons (foreground and background)
	 */
	private void restoreForNewSign() {
		txtStatus.setBackground(ColorConstants.white);
		txtStatus.setText("");

		txtSignature.setText("");

		btnColorSignature.setBackground(buttonDefault);
		btnColorIndex.setBackground(buttonDefault);
		btnColorAuthpath.setBackground(buttonDefault);

		btnColorSignature.setForeground(ColorConstants.black);
		btnColorIndex.setForeground(ColorConstants.black);
		btnColorAuthpath.setForeground(ColorConstants.black);
	}

}
