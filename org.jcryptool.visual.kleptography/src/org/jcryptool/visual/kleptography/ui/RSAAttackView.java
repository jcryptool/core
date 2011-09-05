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
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.kleptography.ui.KleptoView.PrimeGenSetting;

public class RSAAttackView extends Composite {

	private KleptoView kleptoView;

	private Composite cAttackFixed;
	private Composite cFieldsFixed;
	private Composite cButtonsFixed;
	private Group gPublicKeysFixed;
	private Group gCalculationsFixed;
	private Group gCipherTextFixed;
	private Group gDecryptionsFixed;
	private Text tCipherFixed1;
	private Text tTextFixed1;
	private Text tCipherFixed2;
	private Text tTextFixed2;
	private StyledText tNFixed1;
	private StyledText tNFixed2;
	private StyledText tEFixed1;
	private StyledText tEFixed2;
	private StyledText tPFixed;
	private StyledText tQFixed1;
	private StyledText tQFixed2;
	private StyledText tDFixed1;
	private StyledText tDFixed2;
	private Label lCipherFixed1;
	private Label lTextFixed1;
	private Label lCipherFixed2;
	private Label lTextFixed2;
	private Label lNFixed1;
	private Label lNFixed2;
	private Label lEFixed1;
	private Label lEFixed2;
	private Label lPFixed;
	private Label lQFixed1;
	private Label lQFixed2;
	private Label lDFixed1;
	private Label lDFixed2;
	private Button bCalcGCD;
	private Button bCalcPrivateKeysFixed;
	private Button bDecryptTextsFixed;
	private Composite cPFixedPlaceHolder;
	private Button bDecryptP;
	private Label lNSETUP;
	private StyledText tNSETUP;
	private Label lESETUP;
	private StyledText tESETUP;
	private Composite cPFixed;
	private Label lPSETUP1;
	private Label lPSETUP2;
	private StyledText tPSETUP1;
	private StyledText tPSETUP2;
	private Composite cAttackStack;
	private Composite cFieldsSETUP;
	private Group gPublicKeysSETUP;
	private Group gCalculationsSETUP;
	private Group gCipherTextSETUP;
	private Group gDecryptionsSETUP;
	private Text tCipherSETUP;
	private Label lTextSETUP1;
	private Label lTextSETUP2;
	private Text tTextSETUP1;
	private Text tTextSETUP2;
	private Composite cAttackSETUP;
	private Composite cButtonsSETUP;
	private Button bCalcPrivateKeysSETUP;
	private Button bDecryptTextsSETUP;
	private Label lQSETUP1;
	private Label lQSETUP2;
	private StyledText tQSETUP1;
	private StyledText tQSETUP2;
	private Label lDSETUP1;
	private Label lDSETUP2;
	private StyledText tDSETUP1;
	private StyledText tDSETUP2;
	private Label lPublicKeysFixed;
	private Label lCalculationsFixed;
	private Label lCipherTextFixed;
	private Label lDecryptionsFixed;
	private Label lPublicKeysSETUP;
	private Label lCalculationsSETUP;
	private Label lCipherTextSETUP;
	private Label lDecryptionsSETUP;
	private Composite cDescriptionFixed;
	private Label lDescTitleFixed;
	private StyledText stSpecDescFixed;
	private Composite cDescriptionSETUP;
	private Label lDescTitleSETUP;
	private StyledText stSpecDescSETUP;
	private Composite cHeader;
	private Label lTitle;
	private StyledText stGeneralDescription;
	private Button bBackFixed;
	private Button bBackSETUP;
	private Group gAdditionalData;
	private Label lAdditionalData;
	private Label lAttackersD;
	private StyledText tAttackersD;
	private Label lEncryptedP;
	private StyledText tEncryptedP;

	/**
	 * Constructor for the attack view. Sets up the layout of the tab.
	 * @param parent The parent composite (the Attack tab).
	 * @param kleptoView A reference to the kleptoView driver class.
	 */
	public RSAAttackView(final Composite parent, final int style, KleptoView kleptoView) {
		// Set up the basic appearance.
		super(parent, style);
		setLayout(new GridLayout(1, true));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Save a reference to the driver class.
		this.kleptoView = kleptoView;

		// Set up the controls.
		setUpHeader(this);
		setUpAttackStack(this);
		setUpAttackListeners();

		// Set up the visibility, editability, and enabled states.
		setUpEditability();
		updateEnabled();
//		setVisibility();

		this.setTabList(new Control[]{cAttackStack});
	}

	/**
	 * Sets up the header with a brief description of the attack.
	 * @param localParent The parent control of the header (the Key tab).
	 */
	private void setUpHeader(Composite localParent) {
		cHeader = new Composite(localParent, SWT.NONE);
		cHeader.setBackground(KleptoView.WHITE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd2.minimumWidth = 300;
		gd2.widthHint = 300;
		cHeader.setLayoutData(gd2);
		cHeader.setLayout(new GridLayout());

		lTitle = new Label(cHeader, SWT.NONE);
		lTitle.setFont(FontService.getHeaderFont());
		lTitle.setBackground(KleptoView.WHITE);
		lTitle.setText(Messages.RSAAttackView_Title);

		stGeneralDescription = new StyledText(cHeader, SWT.READ_ONLY | SWT.WRAP);
		stGeneralDescription.setText(Messages.RSAAttackView_Gen_Desc);
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		stGeneralDescription.setLayoutData(gd);
	}
	/**
	 * Sets up the StackLayout used to switch between different attack pages.
	 * Actual attacks are only available for Fixed P and SETUP and thus we
	 * essentially just switch between the two possible pages. They are designed to appear similarly.
	 * @param localParent The parent control of the StackLayout (the Attack tab).
	 */
	private void setUpAttackStack(Composite localParent) {
		// Create the StackLayout.
		cAttackStack = new Composite(localParent, SWT.NONE);
		StackLayout sl = new StackLayout();
		cAttackStack.setLayout(sl);
		// Grabbing extra vertical space here is key - this stretches the attack tab
		// to be as long as the keygen tab, thus allowing the text fields to grow a bit.
		cAttackStack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// First item: Fixed P attack
		cAttackFixed = new Composite(cAttackStack, SWT.NONE);
		cAttackFixed.setLayout(new GridLayout(4, false));
		setUpAttackFixed(cAttackFixed);

		// Second item: SETUP attack
		cAttackSETUP = new Composite(cAttackStack, SWT.NONE);
		cAttackSETUP.setLayout(new GridLayout(4, false));
		setUpAttackSETUP(cAttackSETUP);
	}

	/**
	 * Basic outline of the Fixed P attack.
	 * @param localParent The Fixed P composite contained in the StackLayout.
	 */
	private void setUpAttackFixed(Composite localParent) {
		cButtonsFixed = new Composite(localParent, SWT.BORDER);
		cButtonsFixed.setLayout(new GridLayout(1, false));
		cButtonsFixed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

		setUpButtonsFixed(cButtonsFixed);

		cFieldsFixed = new Composite(localParent, SWT.BORDER);
		cFieldsFixed.setLayout(new GridLayout(1, false));
		cFieldsFixed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		setUpFieldsFixed(cFieldsFixed);
	}

	/**
	 * Sets up the left-hand buttons of the Fixed P attack.
	 * @param localParent The parent control of the Fixed P buttons.
	 */
	private void setUpButtonsFixed(Composite localParent) {
		bCalcGCD = new Button(localParent, SWT.PUSH);
		bCalcGCD.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		bCalcGCD.setText(Messages.RSAAttackView_Fixed_Find_GCD);

		bCalcPrivateKeysFixed = new Button(localParent, SWT.PUSH);
		bCalcPrivateKeysFixed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		bCalcPrivateKeysFixed.setText(Messages.RSAAttackView_Fixed_Calc_Private);

		bDecryptTextsFixed = new Button(localParent, SWT.PUSH);
		bDecryptTextsFixed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		bDecryptTextsFixed.setText(Messages.RSAAttackView_Fixed_Decrypt_Cipher);

		bBackFixed = new Button(localParent, SWT.PUSH);
		bBackFixed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		bBackFixed.setText(Messages.RSAAttackView_Fixed_Back);

		// Add a placeholder after the numbers and then set up the key gen descriptor at the bottom.
		Composite cPlaceHolder = new Composite(localParent, SWT.NONE);
		cPlaceHolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 0;
		gd.heightHint = 0;
		cPlaceHolder.setLayoutData(gd);

		setUpDescriptionFixed(localParent);

		localParent.setTabList(new Control[]{bCalcGCD, bCalcPrivateKeysFixed, bDecryptTextsFixed, bBackFixed});
	}

	/**
	 * Sets up the description text field used for the attack sequence steps.
	 * @param localParent The parent control of the Fixed P description.
	 */
	private void setUpDescriptionFixed(Composite localParent) {
		cDescriptionFixed = new Composite(localParent, SWT.NONE);
		cDescriptionFixed.setBackground(KleptoView.WHITE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 250;
		gd.heightHint = 300;
//		gd.minimumWidth = 300;
//		gd.widthHint = 300; //800
		cDescriptionFixed.setLayoutData(gd);
		cDescriptionFixed.setLayout(new GridLayout());

		lDescTitleFixed = new Label(cDescriptionFixed, SWT.NONE);
		lDescTitleFixed.setFont(FontService.getHeaderFont());
		lDescTitleFixed.setBackground(KleptoView.WHITE);
		lDescTitleFixed.setText(Messages.RSAAttackView_Fixed_Title);
		lDescTitleFixed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		// The text here depends on the current step - hence initialize it to be blank.
		stSpecDescFixed = new StyledText(cDescriptionFixed, SWT.READ_ONLY | SWT.WRAP);
		stSpecDescFixed.setText(""); //$NON-NLS-1$
		stSpecDescFixed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	/**
	 * Sets up the numeric and text fields of the Fixed P attack.
	 * This is divided into four subsections in one wide column.
	 * @param localParent The parent control of the Fixed P fields.
	 */
	private void setUpFieldsFixed(Composite localParent) {
		gPublicKeysFixed = new Group(localParent, SWT.NONE);
		gPublicKeysFixed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		gPublicKeysFixed.setLayout(new GridLayout(2, true));
		gPublicKeysFixed.setText(Messages.RSAAttackView_Fixed_Public_Keys);
		setUpPublicKeyFixed(gPublicKeysFixed);

		gCalculationsFixed = new Group(localParent, SWT.NONE);
		gCalculationsFixed.setLayout(new GridLayout(2, true));
		gCalculationsFixed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		gCalculationsFixed.setText(Messages.RSAAttackView_Fixed_Calculations);
		setUpPrivateKeyDataFixed(gCalculationsFixed);

		gCipherTextFixed = new Group(localParent, SWT.NONE);
		gCipherTextFixed.setLayout(new GridLayout(2, true));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 40;
		gd.heightHint = 40;
		gCipherTextFixed.setLayoutData(gd);
//		gCipherTextFixed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		gCipherTextFixed.setText(Messages.RSAAttackView_Fixed_Ciphertexts);
		setUpCipherTextsFixed(gCipherTextFixed);

		gDecryptionsFixed = new Group(localParent, SWT.NONE);
		gDecryptionsFixed.setLayout(new GridLayout(2, true));
		gDecryptionsFixed.setLayoutData(gd);
//		gDecryptionsFixed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		gDecryptionsFixed.setText(Messages.RSAAttackView_Fixed_Decrypted);
		setUpDecryptionsFixed(gDecryptionsFixed);
	}

	/**
	 * Sets up the public key fields of the Fixed P attack.
	 * @param localParent The parent control of the Fixed P public key fields.
	 */
	private void setUpPublicKeyFixed(Composite localParent) {
		Composite cText = new Composite(localParent, SWT.NONE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd2.minimumWidth = 100;
		gd2.widthHint = 300;
		cText.setLayoutData(gd2);
		cText.setLayout(new GridLayout(1, true));

		lPublicKeysFixed = new Label(cText, SWT.WRAP);
		lPublicKeysFixed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lPublicKeysFixed.setText(Messages.RSAAttackView_Fixed_Public_Desc);

		lNFixed1 = new Label(localParent, SWT.None);
		lNFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lNFixed1.setText(Messages.RSAAttackView_Fixed_N1);

		lNFixed2 = new Label(localParent, SWT.None);
		lNFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lNFixed2.setText(Messages.RSAAttackView_Fixed_N2);

		tNFixed1 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tNFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tNFixed1.setMargins(4, 0, 4, 0);

		tNFixed2 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tNFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tNFixed2.setMargins(4, 0, 4, 0);

		lEFixed1 = new Label(localParent, SWT.None);
		lEFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lEFixed1.setText(Messages.RSAAttackView_Fixed_E1);

		lEFixed2 = new Label(localParent, SWT.None);
		lEFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lEFixed2.setText(Messages.RSAAttackView_Fixed_E2);

		tEFixed1 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tEFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tEFixed1.setMargins(4, 0, 4, 0);

		tEFixed2 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tEFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tEFixed2.setMargins(4, 0, 4, 0);
	}

	/**
	 * Sets up the private key fields of the Fixed P attack.
	 * @param localParent The parent control of the Fixed P private key fields.
	 */
	private void setUpPrivateKeyDataFixed(Composite localParent) {
		Composite cText = new Composite(localParent, SWT.NONE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd2.minimumWidth = 100;
		gd2.widthHint = 300;
		cText.setLayoutData(gd2);
		cText.setLayout(new GridLayout(1, true));

		lCalculationsFixed = new Label(cText, SWT.WRAP);
		lCalculationsFixed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lCalculationsFixed.setText(Messages.RSAAttackView_Fixed_Calculations_Desc);

		cPFixed = new Composite(localParent, SWT.NONE);
		cPFixed.setLayout(new GridLayout(4, true));
		cPFixed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		setUpPFixed(cPFixed);

		lQFixed1 = new Label(localParent, SWT.None);
		lQFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lQFixed1.setText(Messages.RSAAttackView_Fixed_Q1);

		lQFixed2 = new Label(localParent, SWT.None);
		lQFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lQFixed2.setText(Messages.RSAAttackView_Fixed_Q2);

		tQFixed1 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tQFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tQFixed1.setMargins(4, 0, 4, 0);

		tQFixed2 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tQFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tQFixed2.setMargins(4, 0, 4, 0);

		lDFixed1 = new Label(localParent, SWT.None);
		lDFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lDFixed1.setText(Messages.RSAAttackView_Fixed_D1);

		lDFixed2 = new Label(localParent, SWT.None);
		lDFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lDFixed2.setText(Messages.RSAAttackView_Fixed_D2);

		tDFixed1 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tDFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tDFixed1.setMargins(4, 0, 4, 0);

		tDFixed2 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tDFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tDFixed2.setMargins(4, 0, 4, 0);
	}

	/**
	 * Sets up the P field of the Fixed P attack.
	 * @param localParent The parent control of the Fixed P P field.
	 */
	private void setUpPFixed(Composite localParent) {
		// Center the P field in the parent composite by using a placeholder
		// to fill the first of the four columns.
		cPFixedPlaceHolder = new Composite(localParent, SWT.None);
		cPFixedPlaceHolder.setLayout(new GridLayout(1, true));
		cPFixedPlaceHolder.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));

		lPFixed = new Label(localParent, SWT.None);
		lPFixed.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lPFixed.setText(Messages.RSAAttackView_Fixed_FactoredP);

		tPFixed = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tPFixed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		tPFixed.setMargins(4, 0, 4, 0);
	}

	/**
	 * Sets up the ciphertext fields of the Fixed P attack.
	 * @param localParent The parent control of the Fixed P ciphertext fields.
	 */
	private void setUpCipherTextsFixed(Composite localParent) {
		Composite cText = new Composite(localParent, SWT.NONE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd2.minimumWidth = 100;
		gd2.widthHint = 300;
		cText.setLayoutData(gd2);
		cText.setLayout(new GridLayout(1, true));

		lCipherTextFixed = new Label(cText, SWT.WRAP);
		lCipherTextFixed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		lCipherTextFixed.setText(Messages.RSAAttackView_Fixed_Ciphertexts_Desc);

		lCipherFixed1 = new Label(localParent, SWT.None);
		lCipherFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lCipherFixed1.setText(Messages.RSAAttackView_Fixed_Ciphertext1);

		lCipherFixed2 = new Label(localParent, SWT.None);
		lCipherFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lCipherFixed2.setText(Messages.RSAAttackView_Fixed_Ciphertext2);

		tCipherFixed1 = new Text(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 10;
		gd.heightHint = 10;
		tCipherFixed1.setLayoutData(gd);
//		tCipherFixed1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tCipherFixed2 = new Text(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tCipherFixed2.setLayoutData(gd);
//		tCipherFixed2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	/**
	 * Sets up the decryption fields of the Fixed P attack.
	 * @param localParent The parent control of the Fixed P decryption fields.
	 */
	private void setUpDecryptionsFixed(Composite localParent) {
		Composite cText = new Composite(localParent, SWT.NONE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd2.minimumWidth = 100;
		gd2.widthHint = 300;
		cText.setLayoutData(gd2);
		cText.setLayout(new GridLayout(1, true));

		lDecryptionsFixed = new Label(cText, SWT.WRAP);
		lDecryptionsFixed.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1));
//		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
//		gd2.minimumWidth = 0;
//		gd2.widthHint = 100;
//		lDecryptionsFixed.setLayoutData(gd2);
		lDecryptionsFixed.setText(Messages.RSAAttackView_Fixed_Decrypted_Desc);

		lTextFixed1 = new Label(localParent, SWT.None);
		lTextFixed1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lTextFixed1.setText(Messages.RSAAttackView_Fixed_Decrypted1);

		lTextFixed2 = new Label(localParent, SWT.None);
		lTextFixed2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lTextFixed2.setText(Messages.RSAAttackView_Fixed_Decrypted2);

		tTextFixed1 = new Text(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 10;
		gd.heightHint = 10;
		tTextFixed1.setLayoutData(gd);
//		tTextFixed1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tTextFixed2 = new Text(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tTextFixed2.setLayoutData(gd);
//		tTextFixed2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	/**
	 * Sets up the numeric and text fields of the SETUP attack.
	 * This is divided into four subsections in one wide column.
	 * @param localParent The parent control of the SETUP fields.
	 */
	private void setUpAttackSETUP(Composite localParent) {
		cButtonsSETUP = new Composite(localParent, SWT.BORDER);
		cButtonsSETUP.setLayout(new GridLayout(1, false));
		cButtonsSETUP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

		setUpButtonsSETUP(cButtonsSETUP);

		cFieldsSETUP = new Composite(localParent, SWT.BORDER);
		cFieldsSETUP.setLayout(new GridLayout(1, false));
		cFieldsSETUP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		setUpFieldsSETUP(cFieldsSETUP);
	}

	/**
	 * Sets up the buttons on the left hand of the SETUP attack.
	 * @param localParent The parent control of the SETUP attack buttons.
	 */
	private void setUpButtonsSETUP(Composite localParent) {
		bDecryptP = new Button(localParent, SWT.PUSH);
		bDecryptP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		bDecryptP.setText(Messages.RSAAttackView_SETUP_DecryptP);

		bCalcPrivateKeysSETUP = new Button(localParent, SWT.PUSH);
		bCalcPrivateKeysSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		bCalcPrivateKeysSETUP.setText(Messages.RSAAttackView_SETUP_Calc_Private);

		bDecryptTextsSETUP = new Button(localParent, SWT.PUSH);
		bDecryptTextsSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		bDecryptTextsSETUP.setText(Messages.RSAAttackView_SETUP_Decrypt_Cipher);

		bBackSETUP = new Button(localParent, SWT.PUSH);
		bBackSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		bBackSETUP.setText(Messages.RSAAttackView_SETUP_Back);

		// Add a placeholder after the numbers and then set up the key gen descriptor at the bottom.
		Composite cPlaceHolder = new Composite(localParent, SWT.NONE);
		cPlaceHolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 0;
		gd.heightHint = 0;
		cPlaceHolder.setLayoutData(gd);

		setUpDescriptionSETUP(localParent);

		localParent.setTabList(new Control[]{bDecryptP, bCalcPrivateKeysSETUP, bDecryptTextsSETUP, bBackSETUP});
	}

	/**
	 * Sets up the description text field used for the attack sequence steps.
	 * @param localParent The parent control of the SETUP description.
	 */
	private void setUpDescriptionSETUP(Composite localParent) {
		cDescriptionSETUP = new Composite(localParent, SWT.NONE);
		cDescriptionSETUP.setBackground(KleptoView.WHITE);
		cDescriptionSETUP.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 250;
		gd.heightHint = 300;
		gd.minimumWidth = 150;
		gd.widthHint = 150;
		cDescriptionSETUP.setLayoutData(gd);

		// The text here depends on the algorithm chosen - hence initialize it to be blank.
		lDescTitleSETUP = new Label(cDescriptionSETUP, SWT.NONE);
		lDescTitleSETUP.setFont(FontService.getHeaderFont());
		lDescTitleSETUP.setBackground(KleptoView.WHITE);
		lDescTitleSETUP.setText(Messages.RSAAttackView_SETUP_Title);
		lDescTitleSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		// The text here depends on the current step - hence initialize it to be blank.
		stSpecDescSETUP = new StyledText(cDescriptionSETUP, SWT.READ_ONLY | SWT.WRAP);
		stSpecDescSETUP.setText(""); //$NON-NLS-1$
		stSpecDescSETUP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	/**
	 * Sets up the numeric and text fields of the SETUP attack.
	 * @param localParent The parent control of the SETUP fields.
	 */
	private void setUpFieldsSETUP(Composite localParent) {
		gPublicKeysSETUP = new Group(localParent, SWT.NONE);
		gPublicKeysSETUP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		gPublicKeysSETUP.setLayout(new GridLayout(2, true));
		gPublicKeysSETUP.setText(Messages.RSAAttackView_SETUP_Public_Keys);
		setUpPublicKeySETUP(gPublicKeysSETUP);

		gAdditionalData = new Group(localParent, SWT.NONE);
		gAdditionalData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		gAdditionalData.setLayout(new GridLayout(2, true));
		gAdditionalData.setText(Messages.RSAAttackView_SETUP_Additional_Data);
		setUpAdditionalData(gAdditionalData);

		gCalculationsSETUP = new Group(localParent, SWT.NONE);
		gCalculationsSETUP.setLayout(new GridLayout(2, true));
		gCalculationsSETUP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		gCalculationsSETUP.setText(Messages.RSAAttackView_SETUP_Calculations);
		setUpPrivateKeyDataSETUP(gCalculationsSETUP);

		gCipherTextSETUP = new Group(localParent, SWT.NONE);
		gCipherTextSETUP.setLayout(new GridLayout(4, true));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 45;
		gd.heightHint = 45;
		gCipherTextSETUP.setLayoutData(gd);
//		gCipherTextSETUP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		gCipherTextSETUP.setText(Messages.RSAAttackView_SETUP_Ciphertext_Group);
		setUpCipherTextsSETUP(gCipherTextSETUP);

		gDecryptionsSETUP = new Group(localParent, SWT.NONE);
		gDecryptionsSETUP.setLayout(new GridLayout(2, true));
		gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 60;
		gd.heightHint = 60;
		gDecryptionsSETUP.setLayoutData(gd);
//		gDecryptionsSETUP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		gDecryptionsSETUP.setText(Messages.RSAAttackView_SETUP_Decryptions);
		setUpDecryptionsSETUP(gDecryptionsSETUP);
	}

	/**
	 * Sets up the public key fields of the SETUP attack.
	 * @param localParent The parent control of the SETUP public key fields.
	 */
	private void setUpPublicKeySETUP(Composite localParent) {
		Composite cText = new Composite(localParent, SWT.NONE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd2.minimumWidth = 100;
		gd2.widthHint = 300;
		cText.setLayoutData(gd2);
		cText.setLayout(new GridLayout(1, true));

		lPublicKeysSETUP = new Label(cText, SWT.WRAP);
		lPublicKeysSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lPublicKeysSETUP.setText(Messages.RSAAttackView_SETUP_Public_Desc);

		lNSETUP = new Label(localParent, SWT.None);
		lNSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lNSETUP.setText(Messages.RSAAttackView_SETUP_N);

		lESETUP = new Label(localParent, SWT.None);
		lESETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lESETUP.setText(Messages.RSAAttackView_SETUP_E);

		tNSETUP = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tNSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tNSETUP.setMargins(4, 0, 4, 0);

		tESETUP = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tESETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tESETUP.setMargins(4, 0, 4, 0);
	}

	/**
	 * Sets up the public key fields of the SETUP attack.
	 * @param localParent The parent control of the SETUP public key fields.
	 */
	private void setUpAdditionalData(Composite localParent) {
		Composite cText = new Composite(localParent, SWT.NONE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd2.minimumWidth = 100;
		gd2.widthHint = 300;
		cText.setLayoutData(gd2);
		cText.setLayout(new GridLayout(1, true));

		lAdditionalData = new Label(cText, SWT.WRAP);
		lAdditionalData.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lAdditionalData.setText(Messages.RSAAttackView_SETUP_Additional_Desc);

		lEncryptedP = new Label(localParent, SWT.None);
		lEncryptedP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lEncryptedP.setText(Messages.RSAAttackView_SETUP_EncryptedP);

		lAttackersD = new Label(localParent, SWT.None);
		lAttackersD.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lAttackersD.setText(Messages.RSAAttackView_SETUP_AttackersD);

		tEncryptedP = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tEncryptedP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tEncryptedP.setMargins(4, 0, 4, 0);

		tAttackersD = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tAttackersD.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tAttackersD.setMargins(4, 0, 4, 0);
	}

	/**
	 * Sets up the private key fields of the SETUP attack.
	 * @param localParent The parent control of the SETUP private key fields.
	 */
	private void setUpPrivateKeyDataSETUP(Composite localParent) {
		Composite cText = new Composite(localParent, SWT.NONE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd2.minimumWidth = 100;
		gd2.widthHint = 300;
		cText.setLayoutData(gd2);
		cText.setLayout(new GridLayout(1, true));

		lCalculationsSETUP = new Label(cText, SWT.WRAP);
		lCalculationsSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lCalculationsSETUP.setText(Messages.RSAAttackView_SETUP_Calculations_Desc);

		lPSETUP1 = new Label(localParent, SWT.None);
		lPSETUP1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lPSETUP1.setText(Messages.RSAAttackView_SETUP_P1);

		lPSETUP2 = new Label(localParent, SWT.None);
		lPSETUP2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lPSETUP2.setText(Messages.RSAAttackView_SETUP_P2);

		tPSETUP1 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tPSETUP1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tPSETUP1.setMargins(4, 0, 4, 0);

		tPSETUP2 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tPSETUP2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tPSETUP2.setMargins(4, 0, 4, 0);

		lQSETUP1 = new Label(localParent, SWT.None);
		lQSETUP1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lQSETUP1.setText(Messages.RSAAttackView_SETUP_Q1);

		lQSETUP2 = new Label(localParent, SWT.None);
		lQSETUP2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lQSETUP2.setText(Messages.RSAAttackView_SETUP_Q2);

		tQSETUP1 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tQSETUP1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tQSETUP1.setMargins(4, 0, 4, 0);

		tQSETUP2 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tQSETUP2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tQSETUP2.setMargins(4, 0, 4, 0);

		lDSETUP1 = new Label(localParent, SWT.None);
		lDSETUP1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lDSETUP1.setText(Messages.RSAAttackView_SETUP_D1);

		lDSETUP2 = new Label(localParent, SWT.None);
		lDSETUP2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lDSETUP2.setText(Messages.RSAAttackView_SETUP_D2);

		tDSETUP1 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tDSETUP1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tDSETUP1.setMargins(4, 0, 4, 0);

		tDSETUP2 = new StyledText(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tDSETUP2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		tDSETUP2.setMargins(4, 0, 4, 0);
	}

	/**
	 * Sets up the ciphertext field of the SETUP attack.
	 * @param localParent The parent control of the SETUP ciphertext field.
	 */
	private void setUpCipherTextsSETUP(Composite localParent) {
		Composite cText = new Composite(localParent, SWT.NONE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
		gd2.minimumWidth = 100;
		gd2.widthHint = 300;
		cText.setLayoutData(gd2);
		cText.setLayout(new GridLayout(1, true));

		lCipherTextSETUP = new Label(cText, SWT.WRAP);
		lCipherTextSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		lCipherTextSETUP.setText(Messages.RSAAttackView_SETUP_Ciphertext_Desc);

		// Textless labels are used throughout as placeholders to center the visible
		// fields. The placeholders must mimic the real fields, but take up only one
		// column instead of 2 (of the four possible).
		Label lPlaceHolder3 = new Label(localParent, SWT.None);
		lPlaceHolder3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		tCipherSETUP = new Text(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd.minimumHeight = 40;
		gd.heightHint = 40;
		tCipherSETUP.setLayoutData(gd);
//		tCipherSETUP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		Label lPlaceHolder4 = new Label(localParent, SWT.None);
		lPlaceHolder4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	}

	/**
	 * Sets up the decryption fields of the SETUP attack.
	 * @param localParent The parent control of the SETUP decryption fields.
	 */
	private void setUpDecryptionsSETUP(Composite localParent) {
		Composite cText = new Composite(localParent, SWT.NONE);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd2.minimumWidth = 100;
		gd2.widthHint = 300;
		cText.setLayoutData(gd2);
		cText.setLayout(new GridLayout(1, true));

		lDecryptionsSETUP = new Label(cText, SWT.WRAP);
		lDecryptionsSETUP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		lDecryptionsSETUP.setText(Messages.RSAAttackView_SETUP_Decryption_Desc);

		lTextSETUP1 = new Label(localParent, SWT.None);
		lTextSETUP1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lTextSETUP1.setText(Messages.RSAAttackView_SETUP_Decrypted1);

		lTextSETUP2 = new Label(localParent, SWT.None);
		lTextSETUP2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lTextSETUP2.setText(Messages.RSAAttackView_SETUP_Decrypted2);

		tTextSETUP1 = new Text(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = 40;
		gd.heightHint = 40;
		tTextSETUP1.setLayoutData(gd);
//		tTextSETUP1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tTextSETUP2 = new Text(localParent, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		tTextSETUP2.setLayoutData(gd);
//		tTextSETUP2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	/**
	 * Copies the current Fixed P ciphertext in the encryption tab to the attack tab.
	 * Two must be save to complete the attack. The first two saves can default to the
	 * first and second save locations, respectively, but additional saves must ask the
	 * user as to which should be overwritten.
	 */
	public void saveCipherTextFixed() {
		// Simple: save the first text to the first location and increment the saved-texts count.
		if(kleptoView.klepto.attack.getFixedTextsSaved() <= 0) {
			saveCipher1();
			kleptoView.klepto.attack.incrementFixedTextsSaved();
		}
		// Almost as simple: save the second text to the second location, and
		// then change the tab view to the attack tab. Presumably after saving two
		// ciphertexts the user will want to try attacking them!
		// Don't forget to increment the saved-texts count.
		else if(kleptoView.klepto.attack.getFixedTextsSaved() == 1) {
			saveCipher2();
			kleptoView.setTabSelection(1);
			kleptoView.klepto.attack.incrementFixedTextsSaved();
		}
		// Trickier: after two have been saved, ask which of the two to overwrite.
		else {
			// First, set the tab view to the attack tab so the user can see the saved texts.
			kleptoView.setTabSelection(1);
			// Then open a dialog to get the user's choice.
			OverwriteDialog temp = new OverwriteDialog(Display.getDefault().getActiveShell());
			int result = temp.open();
			// Overwrite the appropriate choice (unless the user canceled).
			if(result == 1) {
				saveCipher1();
			}
			else if(result == 2) {
				saveCipher2();
			}
			// If the user canceled, don't save and go back to the encryption tab.
			else {
				kleptoView.setTabSelection(0);
			}
		}
	}

	/**
	 * Does the actual work of copying the ciphertext and public key from the
	 * encryption tab to the first Fixed P attack locations.
	 */
	private void saveCipher1() {
		kleptoView.klepto.attack.setE1(kleptoView.klepto.rsa.getE());
		kleptoView.klepto.attack.setN1(kleptoView.klepto.rsa.getN());
		kleptoView.klepto.attack.setSavedCipherBytes1(kleptoView.klepto.rsa.getCipherBytes());
		kleptoView.klepto.attack.setSavedCipherHex1(kleptoView.klepto.rsa.getCipherHex());
		tEFixed1.setText(kleptoView.klepto.attack.getE1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tNFixed1.setText(kleptoView.klepto.attack.getN1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tCipherFixed1.setText(kleptoView.klepto.attack.getSavedCipherHex1().toString());
	}

	/**
	 * Does the actual work of copying the ciphertext and public key from the
	 * encryption tab to the second Fixed P attack locations.
	 */
	private void saveCipher2() {
		kleptoView.klepto.attack.setE2(kleptoView.klepto.rsa.getE());
		kleptoView.klepto.attack.setN2(kleptoView.klepto.rsa.getN());
		kleptoView.klepto.attack.setSavedCipherBytes2(kleptoView.klepto.rsa.getCipherBytes());
		kleptoView.klepto.attack.setSavedCipherHex2(kleptoView.klepto.rsa.getCipherHex());
		tEFixed2.setText(kleptoView.klepto.attack.getE2().
				toString(kleptoView.keyView.getDisplayRadix()));
		tNFixed2.setText(kleptoView.klepto.attack.getN2().
				toString(kleptoView.keyView.getDisplayRadix()));
		tCipherFixed2.setText(kleptoView.klepto.attack.getSavedCipherHex2().toString());
	}

	/**
	 * Copies the current SETUP ciphertext and public key from the encryption tab to the attack tab.
	 * This attack just needs to copy one set over to work.
	 */
	public void saveCipherTextSETUP() {
		kleptoView.klepto.attack.setE1(kleptoView.klepto.rsa.getE());
		kleptoView.klepto.attack.setN1(kleptoView.klepto.rsa.getN());
		kleptoView.klepto.attack.setSavedCipherBytes1(kleptoView.klepto.rsa.getCipherBytes());
		kleptoView.klepto.attack.setSavedCipherHex1(kleptoView.klepto.rsa.getCipherHex());
		tESETUP.setText(kleptoView.klepto.attack.getE1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tNSETUP.setText(kleptoView.klepto.attack.getN1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tCipherSETUP.setText(kleptoView.klepto.attack.getSavedCipherHex1().toString());
		// Take the upper half of the bits from the composite N. If we instead just
		// use the encrypted P, it could be off by one.
		tEncryptedP.setText(kleptoView.klepto.rsa.getN().
				shiftRight(kleptoView.klepto.functions.getBitCount() / 2).
				toString(kleptoView.keyView.getDisplayRadix()));
		tAttackersD.setText(kleptoView.klepto.rsa.getAttackerD().
				toString(kleptoView.keyView.getDisplayRadix()));
		kleptoView.setTabSelection(1);
	}

	/**
	 * Set up the listeners for the buttons to accept user input.
	 * In each case, the enabled and visible buttons and text fields are updated
	 * appropriately, calculations are done in other classes, and the results are displayed.
	 */
	private void setUpAttackListeners() {
		bCalcGCD.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				// If the user used the same N to generate both saved ciphertexts,
				// the attack won't work. Let the user know and don't bother trying.
				if(kleptoView.klepto.attack.getN1().equals(kleptoView.klepto.attack.getN2())) {
					MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
					mb.setText(Messages.RSAAttackView_Identical_N_Title);
					mb.setMessage(Messages.RSAAttackView_Identical_N_Text);
					mb.open();
				}
				else {
					kleptoView.currentStep = 17;
					kleptoView.klepto.attack.calculateCompositeGCD();
					tPFixed.setText(kleptoView.klepto.attack.getFactoredP().
							toString(kleptoView.keyView.getDisplayRadix()));
					updateEnabled();
					updateDescriptionFixed();
				}
			}
		});

		bCalcPrivateKeysFixed.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				kleptoView.currentStep = 18;
				kleptoView.klepto.attack.calculatePrivateKeysFixed();
				tQFixed1.setText(kleptoView.klepto.attack.getQ1().
						toString(kleptoView.keyView.getDisplayRadix()));
				tDFixed1.setText(kleptoView.klepto.attack.getD1().
						toString(kleptoView.keyView.getDisplayRadix()));
				tQFixed2.setText(kleptoView.klepto.attack.getQ2().
						toString(kleptoView.keyView.getDisplayRadix()));
				tDFixed2.setText(kleptoView.klepto.attack.getD2().
						toString(kleptoView.keyView.getDisplayRadix()));
				updateEnabled();
				updateDescriptionFixed();
			}
		});

		bDecryptTextsFixed.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				kleptoView.currentStep = 19;
				kleptoView.klepto.attack.decryptFixed();
				tTextFixed1.setText(kleptoView.klepto.attack.getSavedDeciphered1().toString());
				tTextFixed2.setText(kleptoView.klepto.attack.getSavedDeciphered2().toString());
				updateEnabled();
			}
		});

		bDecryptP.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				kleptoView.currentStep = 10;
				kleptoView.klepto.attack.getPFromN(
						kleptoView.klepto.rsa.getAttackerD(), kleptoView.klepto.rsa.getAttackerN());
				tPSETUP1.setText(kleptoView.klepto.attack.getFactoredP().
						toString(kleptoView.keyView.getDisplayRadix()));
				tPSETUP2.setText(kleptoView.klepto.attack.getP2().
						toString(kleptoView.keyView.getDisplayRadix()));
				updateEnabled();
				updateDescriptionSETUP();
			}
		});

		bCalcPrivateKeysSETUP.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				kleptoView.currentStep = 11;
				kleptoView.klepto.attack.calculatePrivateKeysSETUP();
				tQSETUP1.setText(kleptoView.klepto.attack.getQ1().
						toString(kleptoView.keyView.getDisplayRadix()));
				tDSETUP1.setText(kleptoView.klepto.attack.getD1().
						toString(kleptoView.keyView.getDisplayRadix()));
				tQSETUP2.setText(kleptoView.klepto.attack.getQ2().
						toString(kleptoView.keyView.getDisplayRadix()));
				tDSETUP2.setText(kleptoView.klepto.attack.getD2().
						toString(kleptoView.keyView.getDisplayRadix()));
				updateEnabled();
				updateDescriptionSETUP();
			}
		});

		bDecryptTextsSETUP.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				kleptoView.currentStep = 12;
				kleptoView.klepto.attack.decryptSETUP();
				tTextSETUP1.setText(kleptoView.klepto.attack.getSavedDeciphered1().toString());
				tTextSETUP2.setText(kleptoView.klepto.attack.getSavedDeciphered2().toString());
				updateEnabled();
			}
		});

		bBackFixed.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				kleptoView.setTabSelection(0);
			}
		});

		bBackSETUP.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				kleptoView.setTabSelection(0);
			}
		});
	}

	/**
	 * Sets the descriptions to their appropriate state (presumably clear, since
	 * this routine should only be called from the keygen tab before the attack is underway)
	 * and calls another routine to disable all the controls.
	 */
	public void clearAttackDisplay() {
		updateDescriptionFixed();
		updateDescriptionSETUP();
		updateEnabled();
	}

	/**
	 * Changes the text of the Fixed P description to reflect the current step.
	 * See RSAKeyView.updateDescription() for a more thorough description.
	 */
	public void updateDescriptionFixed() {
		if(kleptoView.currentStep <= 15 || kleptoView.currentSetting != PrimeGenSetting.FIXED)
			stSpecDescFixed.setText(""); //$NON-NLS-1$
		else {
	    	// Figure out where the first colon appears. This will determine how much of each
	    	// line should be in bold. (In English, it's the first seven ("Step 1:") and
	    	// in German the first ten ("Schritt 1:").)
			int boldlength = Messages.RSAKeyView_Honest_Step1.indexOf(":") + 1;

			// The first 15 steps can be ignored; those occur on the first tab.
			// An extra one is needed for highlighting, hence minus 14.
			int styleCount = kleptoView.currentStep - 14;

	        // Set up each of the style ranges for the step numerators
	        // and add them all to an array.
			StyleRange[] styles = new StyleRange[styleCount];
			for(int i = 0; i < styles.length - 1; i++) {
				StyleRange sr = new StyleRange();
		        // The first one will have a length of seven for English or ten for German.
				// Any after that need one more unit (because the step numbers have two digits).
				sr.length = (i == 0 ? boldlength : boldlength + 1);
				sr.fontStyle = SWT.BOLD;
				styles[i] = sr;
			}

	        // After this, boldlength will be used to determine how much space in each
	        // line should be ignored to do the highlighting correctly. Specifically,
	        // boldlength will be subtracted from the line's length to get the highlighted
	        // length. (The space after the colon should be ignored, hence adding one.)
	        // highlightoffset marks where to start the highlighting and is three greater
	        // to account for the line breaks and the second digit of the step number.
	        boldlength++;
	        int highlightoffset = boldlength + 3;

	        // Start the description and set the current styleRange count to zero.
			StringBuilder sbDescription = new StringBuilder(100);
			int srCount = 0;
	        // Create the highlighted styleRange and save it as the last entry of the array.
			StyleRange sr = new StyleRange();
			sr.start = boldlength;
			sr.background = KleptoView.YELLOW;
			styles[styles.length - 1] = sr;

			if(kleptoView.currentStep >= 16) {
				styles[srCount].start = 0;
				styles[styles.length - 1].length = Messages.RSAAttackView_Fixed_Step9.length() - boldlength;
				sbDescription.append(Messages.RSAAttackView_Fixed_Step9);
			}
			if(kleptoView.currentStep >= 17) {
				// Add one here to account for the second digit of the step count.
				boldlength++;

				styles[++srCount].start = sbDescription.length() + 2;
				styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
				styles[styles.length - 1].length = Messages.RSAAttackView_Fixed_Step10.length() - boldlength;
				sbDescription.append("\n\n" + Messages.RSAAttackView_Fixed_Step10); //$NON-NLS-1$
			}
			if(kleptoView.currentStep >= 18) {
				styles[++srCount].start = sbDescription.length() + 2;
				styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
				styles[styles.length - 1].length = Messages.RSAAttackView_Fixed_Step11.length() - boldlength;
				sbDescription.append("\n\n" + Messages.RSAAttackView_Fixed_Step11); //$NON-NLS-1$
			}
			stSpecDescFixed.setText(sbDescription.toString());
			stSpecDescFixed.setStyleRanges(styles);
		}
	}

	/**
	 * Changes the text of the SETUP description to reflect the current step.
	 */
	public void updateDescriptionSETUP() {
		if(kleptoView.currentStep <= 8 || kleptoView.currentSetting != PrimeGenSetting.SETUP)
			stSpecDescSETUP.setText(""); //$NON-NLS-1$
		else {
	    	// Figure out where the first colon appears. This will determine how much of each
	    	// line should be in bold. (In English, it's the first seven ("Step 1:") and
	    	// in German the first ten ("Schritt 1:").)
			int boldlength = Messages.RSAKeyView_Honest_Step1.indexOf(":") + 1;

			// The first 8 steps can be ignored; those occur on the first tab.
			// An extra one is needed for highlighting, hence minus 7.
			int styleCount = kleptoView.currentStep - 7;

	        // Set up each of the style ranges for the step numerators
	        // and add them all to an array.
	        // Each must be bold and have a length of seven for English or ten for German.
			StyleRange[] styles = new StyleRange[styleCount];
			for(int i = 0; i < styles.length - 1; i++) {
				StyleRange sr = new StyleRange();
				sr.length = boldlength;
				sr.fontStyle = SWT.BOLD;
				styles[i] = sr;
			}

	        // After this, boldlength will be used to determine how much space in each
	        // line should be ignored to do the highlighting correctly. Specifically,
	        // boldlength will be subtracted from the line's length to get the highlighted
	        // length. (The space after the colon should be ignored, hence adding one.)
	        // highlightoffset marks where to start the highlighting and is two greater
	        // to account for the line breaks.
	        boldlength++;
	        int highlightoffset = boldlength + 2;

	        // Start the description and set the current styleRange count to zero.
			StringBuilder sbDescription = new StringBuilder(100);
			int srCount = 0;
	        // Create the highlighted styleRange and save it as the last entry of the array.
			StyleRange sr = new StyleRange();
			sr.start = boldlength;
			sr.background = KleptoView.YELLOW;
			styles[styles.length - 1] = sr;

			if(kleptoView.currentStep >= 9) {
				styles[srCount].start = 0;
				styles[styles.length - 1].length = Messages.RSAAttackView_SETUP_Step7.length() - boldlength;
				sbDescription.append(Messages.RSAAttackView_SETUP_Step7);
			}
			if(kleptoView.currentStep >= 10) {
				styles[++srCount].start = sbDescription.length() + 2;
				styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
				styles[styles.length - 1].length = Messages.RSAAttackView_SETUP_Step8.length() - boldlength;
				sbDescription.append("\n\n" + Messages.RSAAttackView_SETUP_Step8); //$NON-NLS-1$
			}
			if(kleptoView.currentStep >= 11) {
				styles[++srCount].start = sbDescription.length() + 2;
				styles[styles.length - 1].start = sbDescription.length() + highlightoffset;
				styles[styles.length - 1].length = Messages.RSAAttackView_SETUP_Step9.length() - boldlength;
				sbDescription.append("\n\n" + Messages.RSAAttackView_SETUP_Step9); //$NON-NLS-1$
			}
			stSpecDescSETUP.setText(sbDescription.toString());
			stSpecDescSETUP.setStyleRanges(styles);
		}
	}

	/**
	 * Activates or deactivates highlighting of the encrypted P and the copy of it stored in N.
	 * @param highlight Whether or not the numbers should be highlighted.
	 */
	public void highlightEncryptedP(boolean highlight) {
		if(tEncryptedP.getEnabled() && highlight) {
			StyleRange styleRange = new StyleRange();
			styleRange.start = 0;
			styleRange.length = tEncryptedP.getCharCount();
			styleRange.background = KleptoView.YELLOW;
			tEncryptedP.setStyleRange(styleRange);
			tNSETUP.setStyleRange(styleRange);
		}
		else {
			StyleRange styleRange = new StyleRange();
			styleRange.start = 0;
			styleRange.length = tEncryptedP.getCharCount();
			styleRange.background = KleptoView.BACKGROUND_GRAY;
			tEncryptedP.setStyleRange(styleRange);
			if(tNSETUP.getCharCount() > tEncryptedP.getCharCount())
				tNSETUP.setStyleRange(styleRange);
		}
	}

	/**
	 * Display all numbers in the current display radix.
	 */
	public void updateNumbers() {
		tEFixed1.setText(kleptoView.klepto.attack.getE1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tNFixed1.setText(kleptoView.klepto.attack.getN1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tEFixed2.setText(kleptoView.klepto.attack.getE2().
				toString(kleptoView.keyView.getDisplayRadix()));
		tNFixed2.setText(kleptoView.klepto.attack.getN2().
				toString(kleptoView.keyView.getDisplayRadix()));
		tPFixed.setText(kleptoView.klepto.attack.getFactoredP().
				toString(kleptoView.keyView.getDisplayRadix()));
		tQFixed1.setText(kleptoView.klepto.attack.getQ1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tDFixed1.setText(kleptoView.klepto.attack.getD1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tQFixed2.setText(kleptoView.klepto.attack.getQ2().
				toString(kleptoView.keyView.getDisplayRadix()));
		tDFixed2.setText(kleptoView.klepto.attack.getD2().
				toString(kleptoView.keyView.getDisplayRadix()));
		tESETUP.setText(kleptoView.klepto.attack.getE1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tNSETUP.setText(kleptoView.klepto.attack.getN1().
				toString(kleptoView.keyView.getDisplayRadix()));
		// A bit funny, but note that the following two actually come from the RSA class,
		// and note that the encrypted P is a bit trickier, since it has to be read
		// straight from the upper bits of N. (The actual encrypted P could be off by 1).
		tEncryptedP.setText(kleptoView.klepto.rsa.getN().
				shiftRight(kleptoView.klepto.functions.getBitCount() / 2).
				toString(kleptoView.keyView.getDisplayRadix()));
		tAttackersD.setText(kleptoView.klepto.rsa.getAttackerD().
				toString(kleptoView.keyView.getDisplayRadix()));
		tPSETUP1.setText(kleptoView.klepto.attack.getFactoredP().
				toString(kleptoView.keyView.getDisplayRadix()));
		tPSETUP2.setText(kleptoView.klepto.attack.getP2().
				toString(kleptoView.keyView.getDisplayRadix()));
		tQSETUP1.setText(kleptoView.klepto.attack.getQ1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tDSETUP1.setText(kleptoView.klepto.attack.getD1().
				toString(kleptoView.keyView.getDisplayRadix()));
		tQSETUP2.setText(kleptoView.klepto.attack.getQ2().
				toString(kleptoView.keyView.getDisplayRadix()));
		tDSETUP2.setText(kleptoView.klepto.attack.getD2().
				toString(kleptoView.keyView.getDisplayRadix()));
	}

	/**
	 * Disable the editability of all the text fields (since they all will be displaying
	 * read-only numeric data or texts).
	 */
	private void setUpEditability() {
		tNFixed1.setEditable(false);
		tNFixed1.setBackground(KleptoView.BACKGROUND_GRAY);
		tNFixed2.setEditable(false);
		tNFixed2.setBackground(KleptoView.BACKGROUND_GRAY);
		tEFixed1.setEditable(false);
		tEFixed1.setBackground(KleptoView.BACKGROUND_GRAY);
		tEFixed2.setEditable(false);
		tEFixed2.setBackground(KleptoView.BACKGROUND_GRAY);
		tPFixed.setEditable(false);
		tPFixed.setBackground(KleptoView.BACKGROUND_GRAY);
		tQFixed1.setEditable(false);
		tQFixed1.setBackground(KleptoView.BACKGROUND_GRAY);
		tQFixed2.setEditable(false);
		tQFixed2.setBackground(KleptoView.BACKGROUND_GRAY);
		tDFixed1.setEditable(false);
		tDFixed1.setBackground(KleptoView.BACKGROUND_GRAY);
		tDFixed2.setEditable(false);
		tDFixed2.setBackground(KleptoView.BACKGROUND_GRAY);
		tCipherFixed1.setEditable(false);
		tCipherFixed1.setBackground(KleptoView.BACKGROUND_GRAY);
		tCipherFixed2.setEditable(false);
		tCipherFixed2.setBackground(KleptoView.BACKGROUND_GRAY);
		tTextFixed1.setEditable(false);
		tTextFixed1.setBackground(KleptoView.BACKGROUND_GRAY);
		tTextFixed2.setEditable(false);
		tTextFixed2.setBackground(KleptoView.BACKGROUND_GRAY);
		tNSETUP.setEditable(false);
		tNSETUP.setBackground(KleptoView.BACKGROUND_GRAY);
		tESETUP.setEditable(false);
		tESETUP.setBackground(KleptoView.BACKGROUND_GRAY);
		tEncryptedP.setEditable(false);
		tEncryptedP.setBackground(KleptoView.BACKGROUND_GRAY);
		tAttackersD.setEditable(false);
		tAttackersD.setBackground(KleptoView.BACKGROUND_GRAY);
		tPSETUP1.setEditable(false);
		tPSETUP1.setBackground(KleptoView.BACKGROUND_GRAY);
		tPSETUP2.setEditable(false);
		tPSETUP2.setBackground(KleptoView.BACKGROUND_GRAY);
		tQSETUP1.setEditable(false);
		tQSETUP1.setBackground(KleptoView.BACKGROUND_GRAY);
		tQSETUP2.setEditable(false);
		tQSETUP2.setBackground(KleptoView.BACKGROUND_GRAY);
		tDSETUP1.setEditable(false);
		tDSETUP1.setBackground(KleptoView.BACKGROUND_GRAY);
		tDSETUP2.setEditable(false);
		tDSETUP2.setBackground(KleptoView.BACKGROUND_GRAY);
		tCipherSETUP.setEditable(false);
		tCipherSETUP.setBackground(KleptoView.BACKGROUND_GRAY);
		tTextSETUP1.setEditable(false);
		tTextSETUP1.setBackground(KleptoView.BACKGROUND_GRAY);
		tTextSETUP2.setEditable(false);
		tTextSETUP2.setBackground(KleptoView.BACKGROUND_GRAY);
	}

	/**
	 * Set up what controls are enabled based on the current setting and step.
	 * There are two linear paths to follow based on which attack is selected, and
	 * it also grows complicated by the multiple required texts for the Fixed P attack.
	 * Unlike RSAKeyView.setEnabled(), this doesn't use a set of booleans; the complexity
	 * and extensiveness of the controls here makes it easier to just work with the global settings.
	 */
	public void updateEnabled() {
		if(kleptoView.currentSetting == PrimeGenSetting.FIXED) {
			// Fixed P attack, first key saved. Must wait for second to be saved to enable the
			// calcGCD button.
			if(kleptoView.klepto.attack.getFixedTextsSaved() >= 1) {
				tNFixed1.setForeground(KleptoView.BLACK);
				tEFixed1.setForeground(KleptoView.BLACK);
				tNFixed1.setEnabled(true);
				tEFixed1.setEnabled(true);
				tCipherFixed1.setEnabled(true);
			}
			else {
				tNFixed1.setForeground(KleptoView.FOREGROUND_GRAY);
				tEFixed1.setForeground(KleptoView.FOREGROUND_GRAY);
				tNFixed1.setEnabled(false);
				tEFixed1.setEnabled(false);
				tCipherFixed1.setEnabled(false);
			}

			// Fixed P, second key saved.
			if(kleptoView.klepto.attack.getFixedTextsSaved() >= 2) {
				tNFixed2.setForeground(KleptoView.BLACK);
				tEFixed2.setForeground(KleptoView.BLACK);
				tNFixed2.setEnabled(true);
				tEFixed2.setEnabled(true);
				tCipherFixed2.setEnabled(true);
				bCalcGCD.setEnabled(true);
			}
			else {
				tNFixed2.setForeground(KleptoView.FOREGROUND_GRAY);
				tEFixed2.setForeground(KleptoView.FOREGROUND_GRAY);
				tNFixed2.setEnabled(false);
				tEFixed2.setEnabled(false);
				tCipherFixed2.setEnabled(false);
				bCalcGCD.setEnabled(false);
			}

			// Fixed P, P calculated via GCD.
			if(kleptoView.currentStep >= 17) {
				tPFixed.setForeground(KleptoView.BLACK);
				tPFixed.setEnabled(true);
				bCalcPrivateKeysFixed.setEnabled(true);
			}
			else {
				tPFixed.setForeground(KleptoView.FOREGROUND_GRAY);
				tPFixed.setEnabled(false);
				bCalcPrivateKeysFixed.setEnabled(false);
			}

			// Fixed P, private key calculated.
			if(kleptoView.currentStep >= 18) {
				tQFixed1.setForeground(KleptoView.BLACK);
				tQFixed2.setForeground(KleptoView.BLACK);
				tDFixed1.setForeground(KleptoView.BLACK);
				tDFixed2.setForeground(KleptoView.BLACK);
				tQFixed1.setEnabled(true);
				tQFixed2.setEnabled(true);
				tDFixed1.setEnabled(true);
				tDFixed2.setEnabled(true);
				bDecryptTextsFixed.setEnabled(true);
			}
			else {
				tQFixed1.setForeground(KleptoView.FOREGROUND_GRAY);
				tQFixed2.setForeground(KleptoView.FOREGROUND_GRAY);
				tDFixed1.setForeground(KleptoView.FOREGROUND_GRAY);
				tDFixed2.setForeground(KleptoView.FOREGROUND_GRAY);
				tQFixed1.setEnabled(false);
				tQFixed2.setEnabled(false);
				tDFixed1.setEnabled(false);
				tDFixed2.setEnabled(false);
				bDecryptTextsFixed.setEnabled(false);
			}

			// Fixed P, ciphertexts decrypted.
			if(kleptoView.currentStep >= 19) {
				tTextFixed1.setEnabled(true);
				tTextFixed2.setEnabled(true);
			}
			else {
				tTextFixed1.setEnabled(false);
				tTextFixed2.setEnabled(false);
			}
		}
		else if(kleptoView.currentSetting == PrimeGenSetting.SETUP) {
			// SETUP attack, ciphertext saved.
			if(kleptoView.currentStep >= 9) {
				tNSETUP.setForeground(KleptoView.BLACK);
				tESETUP.setForeground(KleptoView.BLACK);
				tEncryptedP.setForeground(KleptoView.BLACK);
				tAttackersD.setForeground(KleptoView.BLACK);
				tNSETUP.setEnabled(true);
				tESETUP.setEnabled(true);
				tEncryptedP.setEnabled(true);
				tAttackersD.setEnabled(true);
				tCipherSETUP.setEnabled(true);
				bDecryptP.setEnabled(true);
			}
			else {
				tNSETUP.setForeground(KleptoView.FOREGROUND_GRAY);
				tESETUP.setForeground(KleptoView.FOREGROUND_GRAY);
				tEncryptedP.setForeground(KleptoView.FOREGROUND_GRAY);
				tAttackersD.setForeground(KleptoView.FOREGROUND_GRAY);
				tNSETUP.setEnabled(false);
				tESETUP.setEnabled(false);
				tEncryptedP.setEnabled(false);
				tAttackersD.setEnabled(false);
				tCipherSETUP.setEnabled(false);
				bDecryptP.setEnabled(false);
			}

			// SETUP, P decrypted.
			if(kleptoView.currentStep >= 10) {
				tPSETUP1.setForeground(KleptoView.BLACK);
				tPSETUP2.setForeground(KleptoView.BLACK);
				tPSETUP1.setEnabled(true);
				tPSETUP2.setEnabled(true);
				bCalcPrivateKeysSETUP.setEnabled(true);
			}
			else {
				tPSETUP1.setForeground(KleptoView.FOREGROUND_GRAY);
				tPSETUP2.setForeground(KleptoView.FOREGROUND_GRAY);
				tPSETUP1.setEnabled(false);
				tPSETUP2.setEnabled(false);
				bCalcPrivateKeysSETUP.setEnabled(false);
			}

			// SETUP, private key calculated.
			if(kleptoView.currentStep >= 11) {
				tQSETUP1.setForeground(KleptoView.BLACK);
				tQSETUP2.setForeground(KleptoView.BLACK);
				tDSETUP1.setForeground(KleptoView.BLACK);
				tDSETUP2.setForeground(KleptoView.BLACK);
				tQSETUP1.setEnabled(true);
				tQSETUP2.setEnabled(true);
				tDSETUP1.setEnabled(true);
				tDSETUP2.setEnabled(true);
				bDecryptTextsSETUP.setEnabled(true);
			}
			else {
				tQSETUP1.setForeground(KleptoView.FOREGROUND_GRAY);
				tQSETUP2.setForeground(KleptoView.FOREGROUND_GRAY);
				tDSETUP1.setForeground(KleptoView.FOREGROUND_GRAY);
				tDSETUP2.setForeground(KleptoView.FOREGROUND_GRAY);
				tQSETUP1.setEnabled(false);
				tQSETUP2.setEnabled(false);
				tDSETUP1.setEnabled(false);
				tDSETUP2.setEnabled(false);
				bDecryptTextsSETUP.setEnabled(false);
			}

			// SETUP, ciphertext decrypted.
			if(kleptoView.currentStep >= 12) {
				tTextSETUP1.setEnabled(true);
				tTextSETUP2.setEnabled(true);
			}
			else {
				tTextSETUP1.setEnabled(false);
				tTextSETUP2.setEnabled(false);
			}
		}
	}

	/**
	 * Controls which composite appears on top of the StackLayout (depending
	 * on the currently selected method/attack).
	 * @param fixedP Set to true if using the Fixed P attack.
	 * @param setup Set to true if using the SETUP attack.
	 */
	public void updateVisibility() {
		if(kleptoView.currentSetting == PrimeGenSetting.FIXED) {
			((StackLayout) cAttackStack.getLayout()).topControl = cAttackFixed;
		}
		else if(kleptoView.currentSetting == PrimeGenSetting.SETUP) {
			((StackLayout) cAttackStack.getLayout()).topControl = cAttackSETUP;
		}
		// Refresh the StackLayout.
		cAttackStack.layout();
	}
} // End RSAAttackView