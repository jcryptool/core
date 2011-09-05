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
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.secretsharing.algorithm.Point;
import org.jcryptool.visual.secretsharing.algorithm.ShamirsSecretSharing;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class ShamirsCompositeNumerical extends Composite implements Constants {

	private Text infoText;
	private StyledText stInfo;
	private Label reconstructSumLabel;
	private Label reconstructEquivalenzLabel1;
	private Label reconstructEquivalenzLabel2;
	private Label reconstructPxLabel;

	private Composite compositeReconstruction;
	private ScrolledComposite scrolledReconstruction;
	private Button numericalButton;
	private Label coefficentLabel;
	private BigInteger[] coefficients;
	private Composite compositeIntro;
	private Composite compositeShares;
	private Button computeSharesButton;

	private Composite content;
	private Button deselectAllButton;
	private Group groupCurve;
	private Group groupParameter;
	private Group groupReconstruction;
	private Group groupSettings;
	private Group groupShares;
	private Group groupModus;
	private BigInteger modul;
	private Label modulLabel;

	private Text modulText;
	private Label numberForReconstructionLabel;
	private Label numberOfConcernedLabel;
	private VerifyListener numberOnlyVerifyListenerModul;
	private Label polynomLabel;
	private Label pxLabel;
	private Button reconstructButton;
	private String result[];
	private ScrolledComposite scrolledShares;
	private BigInteger secret;
	private Label secretLabel;
	private Text secretText;
	private Button selectAllButton;
	private Button selectButton;
	private ShamirsSecretSharing shamirsSecretSharing;
	private Vector<BigInteger[]> subpolynomial;
	private Point[] shares;
	private Button[] sharesUseCheckButtonSet;

	private Text sharesYCoordinateText;

	private Button graphicalButton;
	private Spinner spnrN;
	private Spinner spnrT;
	private StyledText stDescription;
	private StyledText stPolynom;

	private StyledText stValue;
	private SecretSharingView view;
	private VerifyListener numberOnlyVerifyListenerSecret;

	/**
	 * Create the composite
	 * @param parent
	 * @param style
	 */
	public ShamirsCompositeNumerical(Composite parent, int style, SecretSharingView view) {
		super(parent, style);
		this.view = view;
		setLayout(new FillLayout());

		ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		content = new Composite(scrolledComposite, SWT.NONE);

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 2;
		content.setLayout(gridLayout);

		createCompositeIntro();
		createGroupSecretSharing();
		createGroupSettings();

		scrolledComposite.setContent(content);
		scrolledComposite.setMinSize(content.computeSize(862, 585));

		//
	}

	/**
	 * Reset all elements for the next calculation
	 */
	public void adjustButtons() {
		spnrN.setEnabled(true);
		spnrN.setSelection(2);
		spnrN.setMinimum(2);
		spnrN.setMaximum(500);
		spnrT.setEnabled(true);
		spnrT.setSelection(2);
		spnrT.setMinimum(2);
		spnrT.setMaximum(2);
		numericalButton.setSelection(true);
		graphicalButton.setSelection(false);
		computeSharesButton.setEnabled(false);
		secret = null;
		modul = null;
		secretText.removeVerifyListener(numberOnlyVerifyListenerSecret);
		secretText.setText("");
		secretText.setEnabled(true);
		secretText.addVerifyListener(numberOnlyVerifyListenerSecret);
		modulText.removeVerifyListener(numberOnlyVerifyListenerModul);
		modulText.setText("");
		modulText.setEnabled(true);
		modulText.addVerifyListener(numberOnlyVerifyListenerModul);
		stPolynom.setText("");
		stPolynom.setEnabled(true);
		selectButton.setEnabled(true);
		computeSharesButton.setEnabled(false);
		reconstructButton.setEnabled(false);
		selectAllButton.setEnabled(false);
		deselectAllButton.setEnabled(false);

		Control[] tmpWidgets = compositeShares.getChildren();
		for (int i = 0; i < tmpWidgets.length; i++) {
			tmpWidgets[i].dispose();
		}
		compositeShares.pack();

		tmpWidgets = compositeReconstruction.getChildren();
		for (int i = 0; i < tmpWidgets.length; i++) {
			tmpWidgets[i].dispose();
		}
		compositeReconstruction.pack();

		stValue.setText("");
		stValue.setEnabled(false);
		stInfo.setText("");

		stInfo.setBackground(WHITE);

		reconstructPxLabel.setEnabled(false);
		reconstructSumLabel.setEnabled(false);
		reconstructEquivalenzLabel1.setEnabled(false);
		reconstructEquivalenzLabel2.setEnabled(false);
		infoText.setEnabled(false);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Convert a number to a superscript index
	 * @param id is the number to be converted
	 * @return a string which contains only superscript
	 */
	private String convertToSuperscript(int id) {
		char[] data = String.valueOf(id).toCharArray();
		String result = "";

		if (id == 0 || id == 1)
			return result;

		for (int i = 0; i < data.length; i++) {
			if (data[i] == '0')
				result += sZero;

			if (data[i] == '1')
				result += sOne;

			if (data[i] == '2')
				result += sTwo;

			if (data[i] == '3')
				result += sThree;

			if (data[i] == '4')
				result += sFour;

			if (data[i] == '5')
				result += sFive;

			if (data[i] == '6')
				result += sSix;

			if (data[i] == '7')
				result += sSeven;

			if (data[i] == '8')
				result += sEight;

			if (data[i] == '9')
				result += sNine;
		}

		return result;
	}

	/**
	 * This method initializes compositeIntro
	 */
	private void createCompositeIntro() {
		compositeIntro = new Composite(content, SWT.NONE);
		compositeIntro.setBackground(WHITE);
		compositeIntro.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		compositeIntro.setLayout(new GridLayout(1, false));

		Label label = new Label(compositeIntro, SWT.NONE);
		label.setFont(FontService.getHeaderFont());
		label.setBackground(WHITE);
		label.setText(MESSAGE_TITLE);

		stDescription = new StyledText(compositeIntro, SWT.READ_ONLY);
		stDescription.setText(MESSAGE_DESCRIPTION + " " + MESSAGE_LAGRAGE + " " + MESSAGE_FORMULAR);
		stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	}

	/**
	 * This method initializes groupCurve
	 */
	private void createGroupSecretSharing() {
		GridData gridData8 = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData8.heightHint = 483;
		groupCurve = new Group(content, SWT.NONE);
		groupCurve.setLayout(new GridLayout());
		groupCurve.setLayoutData(gridData8);
		groupCurve.setText(MESSAGE_TITLE);

		createGroupShares();

		createGroupReconstruction();
	}

	/**
	 * Creates the info header
	 */
	private void createGroupInfo() {
		Group infoGroup = new Group(groupSettings, SWT.NONE);
		infoGroup.setEnabled(false);
		infoGroup.setText(MESSAGE_INFO_GROUP);
		final GridData gd_infoGroup = new GridData(SWT.FILL, SWT.FILL, true, true);
		infoGroup.setLayoutData(gd_infoGroup);
		infoGroup.setLayout(new GridLayout());

		infoText = new Text(infoGroup, SWT.WRAP | SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		infoText.setEnabled(false);
		final GridData gd_infoText = new GridData(SWT.FILL, SWT.FILL, true, true);
		infoText.setLayoutData(gd_infoText);
		infoText.setText(MESSAGE_INFO + "\n" + MESSAGE_FORMULAR + "\n" + MESSAGE_FORMULAR_RANGE);

		stInfo = new StyledText(infoGroup, SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
		stInfo.setEditable(false);
		stInfo.setEnabled(false);
		final GridData gd_stInfo = new GridData(SWT.FILL, SWT.FILL, true, true);
		stInfo.setLayoutData(gd_stInfo);
	}

	/**
	 * This method creates the parameter group
	 */
	private void createGroupParameter() {
		groupParameter = new Group(groupSettings, SWT.NONE);
		final GridData gd_groupParameter = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_groupParameter.heightHint = 210;
		groupParameter.setLayoutData(gd_groupParameter);
		groupParameter.setText(MESSAGE_PARAMETER);

		numberOfConcernedLabel = new Label(groupParameter, SWT.NONE);
		numberOfConcernedLabel.setBounds(10, 20, 174, 15);
		numberOfConcernedLabel.setText(MESSAGE_CONCERNED_PERSONS);

		spnrN = new Spinner(groupParameter, SWT.BORDER);
		spnrN.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				spnrT.setMaximum(spnrN.getSelection());
				spnrT.setSelection(spnrT.getSelection());
			}
		});
		spnrN.setBounds(225, 17, 55, 20);
		spnrN.setMinimum(2);
		spnrN.setMaximum(500);

		numberForReconstructionLabel = new Label(groupParameter, SWT.NONE);
		numberForReconstructionLabel.setBounds(10, 46, 210, 15);
		numberForReconstructionLabel.setText(MESSAGE_RECONSTRUCT_PERSONS);

		spnrT = new Spinner(groupParameter, SWT.BORDER);
		spnrT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (spnrT.getSelection() >= spnrN.getSelection()) {
					spnrT.setMaximum(spnrT.getSelection() + 1);
					spnrN.setSelection(spnrT.getSelection());
				}
			}
		});
		spnrT.setBounds(225, 43, 55, 20);
		spnrT.setMinimum(2);
		spnrT.setMaximum(2);

		modulLabel = new Label(groupParameter, SWT.NONE);
		modulLabel.setBounds(10, 72, 152, 15);
		modulLabel.setText(MESSAGE_MODUL);

		modulText = new Text(groupParameter, SWT.BORDER);
		modulText.setBounds(168, 69, 113, 20);

		numberOnlyVerifyListenerModul = new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				/*
				 * keyCode == 8 is BACKSPACE and keyCode == 48 is ZERO and keyCode == 127 is DEL
				 */
				if (e.text.matches("[0-9]") || e.keyCode == 8 || e.keyCode == 127) {
					if (modulText.getText().length() == 0 && e.text.compareTo("0") == 0) {
						e.doit = false;
					} else if (modulText.getSelection().x == 0 && e.keyCode == 48) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}
		};
		modulText.addVerifyListener(numberOnlyVerifyListenerModul);

		secretLabel = new Label(groupParameter, SWT.NONE);
		secretLabel.setText(MESSAGE_SECRET);
		secretLabel.setBounds(10, 101, 105, 15);

		secretText = new Text(groupParameter, SWT.BORDER);
		secretText.setBounds(121, 98, 159, 20);

		numberOnlyVerifyListenerSecret = new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				/*
				 * keyCode == 8 is BACKSPACE and keyCode == 48 is ZERO and keyCode == 127 is DEL
				 */
				if (e.text.matches("[0-9]") || e.keyCode == 8 || e.keyCode == 127) {
					if (secretText.getText().length() == 0 && e.text.compareTo("0") == 0) {
						e.doit = false;
					} else if (secretText.getSelection().x == 0 && e.keyCode == 48) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}
		};
		secretText.addVerifyListener(numberOnlyVerifyListenerSecret);

		coefficentLabel = new Label(groupParameter, SWT.NONE);
		coefficentLabel.setText(MESSAGE_COEFFICIENT);
		coefficentLabel.setBounds(10, 127, 58, 15);

		selectButton = new Button(groupParameter, SWT.NONE);
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				// spnrN.setEnabled(false);
				// spnrT.setEnabled(false);

				int statusPrime = 0;
				int statusSecret = 0;

				result = new String[2];
				result[0] = modulText.getText();
				result[1] = secretText.getText();

				coefficients = new BigInteger[spnrT.getSelection()];

				String tmpModul = modulText.getText();
				String tmpSecret = secretText.getText();

				/*
				 * check if the input(modul and secret) is empty
				 */
				if (tmpModul.length() > 0 && tmpSecret.length() > 0) {
					boolean isPrime = false;
					modul = new BigInteger(tmpModul);
					secret = new BigInteger(tmpSecret);
					isPrime = modul.isProbablePrime(2000000);

					if (modul.compareTo(new BigInteger(spnrN.getText())) > 0) {
						/*
						 * check if the modul is prime
						 */
						if (!isPrime) {
							PrimeDialog primeDialog = new PrimeDialog(getDisplay().getActiveShell(), modul, result);
							statusPrime = primeDialog.open();

							if (statusPrime == 0) {
								modulText.removeVerifyListener(numberOnlyVerifyListenerModul);
								modulText.setText(result[0]);
								modul = new BigInteger(modulText.getText());
								modulText.addVerifyListener(numberOnlyVerifyListenerModul);
							}
						}
						/*
						 * check if the secret is smaller than the modul
						 */
						if (secret.compareTo(modul) >= 0 && statusPrime != 1) {

							SecretDialog secretDialog = new SecretDialog(getDisplay().getActiveShell(), secret, result);
							statusSecret = secretDialog.open();
							if (statusSecret == 0) {
								secretText.removeVerifyListener(numberOnlyVerifyListenerSecret);
								secretText.setText(result[1]);
								secret = new BigInteger(secretText.getText());
								secretText.addVerifyListener(numberOnlyVerifyListenerSecret);
							}
						}
						/*
						 * if the precondition is correct and the input is not empty than select the coefficients
						 */
						if (statusPrime == 0 && statusSecret == 0) {
							String polynomialString = "";
							CoefficientDialog cdialog = new CoefficientDialog(getDisplay().getActiveShell(), spnrT
									.getSelection(), secret, coefficients, modul);
							int statusCoefficient = cdialog.open();
							if (statusCoefficient == 0) {
								/*
								 * make a polynomial string
								 */
								polynomialString = createPolynomialString(coefficients);

								StyleRange stPolynomStyle = new StyleRange();
								stPolynomStyle.start = 0;
								stPolynomStyle.length = polynomialString.length();
								stPolynomStyle.fontStyle = SWT.BOLD;
								stPolynomStyle.foreground = BLUE;
								stPolynom.setText(polynomialString);
								stPolynom.setStyleRange(stPolynomStyle);

								if (polynomialString.contains("-") || polynomialString.contains("+")) {
									computeSharesButton.setEnabled(true);

								} else {
									computeSharesButton.setEnabled(false);
								}
								modulText.setEnabled(false);
								secretText.setEnabled(false);
								spnrN.setEnabled(false);
								spnrT.setEnabled(false);
								spnrN.setEnabled(false);
								spnrT.setEnabled(false);
								selectButton.setEnabled(false);
							}
						}
					}
				}
			}
		});
		selectButton.setText(MESSAGE_SELECT);
		selectButton.setBounds(107, 124, 173, 20);

		polynomLabel = new Label(groupParameter, SWT.NONE);
		polynomLabel.setText(MESSAGE_POLYNOM);
		polynomLabel.setBounds(10, 148, 270, 15);

		stPolynom = new StyledText(groupParameter, SWT.READ_ONLY | SWT.BORDER);
		stPolynom.setBounds(44, 169, 236, 20);

		pxLabel = new Label(groupParameter, SWT.NONE);
		pxLabel.setText(MESSAGE_P);
		pxLabel.setBounds(10, 169, 28, 15);

		computeSharesButton = new Button(groupParameter, SWT.NONE);
		computeSharesButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				BigInteger n = new BigInteger(String.valueOf(spnrN.getSelection()));
				BigInteger t = new BigInteger(String.valueOf(spnrT.getSelection()));
				modul = new BigInteger(modulText.getText());
				shares = new Point[n.intValue()];
				for (int j = 0; j < shares.length; j++) {
					shares[j] = new Point(new BigInteger(String.valueOf(j + 1)));
				}

				shamirsSecretSharing = new ShamirsSecretSharing(t, n, modul, Mode.NUMERICAL);
				shamirsSecretSharing.setCoefficient(coefficients);

				shares = shamirsSecretSharing.computeShares(shares);
				createShares(shares.length);

				computeSharesButton.setEnabled(false);
				selectAllButton.setEnabled(true);
				deselectAllButton.setEnabled(true);
			}
		});
		computeSharesButton.setEnabled(false);
		computeSharesButton.setText(MESSAGE_COMPUTE_SHARES);
		computeSharesButton.setBounds(10, 195, 270, 20);

		reconstructPxLabel.setEnabled(true);
		reconstructSumLabel.setEnabled(true);
		reconstructEquivalenzLabel1.setEnabled(true);
	}

	/**
	 * This method initializes groupReconstruction
	 */
	private void createGroupReconstruction() {
		groupReconstruction = new Group(groupCurve, SWT.NONE);
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 5;
		GridData gridData11 = new GridData(SWT.FILL, SWT.FILL, true, true);

		groupReconstruction.setLayoutData(gridData11);
		groupReconstruction.setLayout(gridLayout5);
		groupReconstruction.setText(MESSAGE_RECONSTRUT);

		scrolledReconstruction = new ScrolledComposite(groupReconstruction, SWT.V_SCROLL | SWT.BORDER);
		scrolledReconstruction.setExpandHorizontal(true);
		final GridData gd_scrolledReconstruction = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
		scrolledReconstruction.setLayoutData(gd_scrolledReconstruction);

		compositeReconstruction = new Composite(scrolledReconstruction, SWT.NONE);
		compositeReconstruction.setLocation(0, 0);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		compositeReconstruction.setLayout(gridLayout);

		reconstructPxLabel = new Label(groupReconstruction, SWT.NONE);
		reconstructPxLabel.setEnabled(false);
		reconstructPxLabel.setText(MESSAGE_P_);

		reconstructEquivalenzLabel1 = new Label(groupReconstruction, SWT.NONE);
		reconstructEquivalenzLabel1.setEnabled(false);
		reconstructEquivalenzLabel1.setText(MESSAGE_EQUAL);

		reconstructSumLabel = new Label(groupReconstruction, SWT.NONE);
		reconstructSumLabel.setEnabled(false);
		reconstructSumLabel.setText(uSum + " w" + uI);

		reconstructEquivalenzLabel2 = new Label(groupReconstruction, SWT.NONE);
		reconstructEquivalenzLabel2.setEnabled(false);
		reconstructEquivalenzLabel2.setText(MESSAGE_EQUAL);

		stValue = new StyledText(groupReconstruction, SWT.READ_ONLY | SWT.BORDER);
		stValue.setEnabled(false);

		final GridData gd_stValue = new GridData(SWT.FILL, SWT.CENTER, false, false);
		stValue.setLayoutData(gd_stValue);

		compositeReconstruction.pack();
		scrolledReconstruction.setContent(compositeReconstruction);
	}

	/**
	 * This method initializes groupSettings
	 */
	private void createGroupSettings() {
		GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, false, false);
		gridData2.heightHint = 413;
		gridData2.widthHint = 300;
		groupSettings = new Group(content, SWT.NONE);
		groupSettings.setLayout(new GridLayout());
		groupSettings.setText(MESSAGE_SETTINGS);
		groupSettings.setLayoutData(gridData2);

		createGroupModus();
		createGroupParameter();
		createGroupInfo();
	}

	/**
	 * This method initializes groupShares
	 */
	private void createGroupShares() {
		groupShares = new Group(groupCurve, SWT.NONE);
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 3;
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		groupShares.setLayoutData(gridData11);
		groupShares.setLayout(gridLayout5);
		groupShares.setText(MESSAGE_SHARES);

		scrolledShares = new ScrolledComposite(groupShares, SWT.V_SCROLL | SWT.BORDER);
		scrolledShares.setExpandHorizontal(true);
		final GridData gd_scrolledShares = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
		gd_scrolledShares.widthHint = 492;
		gd_scrolledShares.heightHint = 213;
		scrolledShares.setLayoutData(gd_scrolledShares);

		compositeShares = new Composite(scrolledShares, SWT.NONE);
		compositeShares.setLocation(0, 0);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 8;
		compositeShares.setLayout(gridLayout);

		/*
		 * BEGIN dummy for design only
		 */
		// Label sharesPLabel = new Label(compositeShares, SWT.NONE);
		// sharesPLabel.setText("Share 1");
		//
		// Label sharesEquivalentLabel = new Label(compositeShares, SWT.NONE);
		// sharesEquivalentLabel.setText("=");
		//
		// Label sharesOpenBracetLabel = new Label(compositeShares, SWT.NONE);
		// sharesOpenBracetLabel.setText("(");
		//
		// Label sharesXCoordinateLabel = new Label(compositeShares, SWT.NONE);
		// sharesXCoordinateLabel.setText(String.valueOf("1234"));
		//
		// Label sharesSeperatorLabel = new Label(compositeShares, SWT.NONE);
		// sharesSeperatorLabel.setText("|");
		//
		// sharesYCoordinateText = new Text(compositeShares, SWT.READ_ONLY | SWT.BORDER);
		// sharesYCoordinateText.setText("5678");
		// GridData gd_sharesYCoordinateText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		// sharesYCoordinateText.setLayoutData(gd_sharesYCoordinateText);
		//
		// Label sharesCloseBarcetLabel = new Label(compositeShares, SWT.NONE);
		// sharesCloseBarcetLabel.setText(")");
		// compositeShares.setSize(517, 210);
		//
		// Button sharesUseCheckButton = new Button(compositeShares, SWT.CHECK);
		/*
		 * END dummy
		 */

		compositeShares.pack();

		reconstructButton = new Button(groupShares, SWT.NONE);
		reconstructButton.setEnabled(false);
		reconstructButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				/*
				 * clear the composite for the next visualization
				 */
				Control[] tmpWidgets = compositeReconstruction.getChildren();
				for (int i = 0; i < tmpWidgets.length; i++) {
					tmpWidgets[i].dispose();
				}
				compositeReconstruction.pack();

				Vector<Point> tmpPointSet = new Vector<Point>();

				for (int i = 0; i < sharesUseCheckButtonSet.length; i++) {
					if (sharesUseCheckButtonSet[i].getSelection()) {
						tmpPointSet.add(shares[i]);
					}
				}
				Point[] pointSet = new Point[tmpPointSet.size()];
				for (int i = 0; i < tmpPointSet.size(); i++) {
					pointSet[i] = tmpPointSet.get(i);
				}

				BigInteger[] resultPolynomial = shamirsSecretSharing.interpolatePoints(pointSet, modul);
				subpolynomial = shamirsSecretSharing.getSubPolynomialNumerical();

				createReconstruction(subpolynomial.size());

				stValue.setText(createPolynomialString(resultPolynomial));

				StyleRange styleValue = new StyleRange();
				StyleRange styleInfo = new StyleRange();
				styleValue.start = 0;
				styleInfo.start = 0;
				styleValue.length = stValue.getText().length();

				if (comparePolynomial(resultPolynomial, coefficients)) {
					styleValue.foreground = GREEN;

					stInfo.setText(MESSAGE_RECONSTRUCTION_TRUE);
					stInfo.setBackground(GREEN);
				} else {
					styleValue.foreground = RED;

					stInfo.setText(MESSAGE_RECONSTRUCTION_FALSE);
					stInfo.setBackground(RED);
				}
				styleInfo.length = stInfo.getText().length();
				styleInfo.fontStyle = SWT.BOLD;
				styleValue.fontStyle = SWT.BOLD;
				stInfo.setStyleRange(styleInfo);
				stValue.setStyleRange(styleValue);

				infoText.setEnabled(true);
				reconstructPxLabel.setEnabled(true);
				reconstructSumLabel.setEnabled(true);
				reconstructEquivalenzLabel1.setEnabled(true);
				reconstructEquivalenzLabel2.setEnabled(true);
				stValue.setEnabled(true);

			}
		});
		reconstructButton.setText(MESSAGE_RECONSTRUCT);

		selectAllButton = new Button(groupShares, SWT.NONE);
		selectAllButton.setEnabled(false);
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				for (int i = 0; i < sharesUseCheckButtonSet.length; i++) {
					sharesUseCheckButtonSet[i].setSelection(true);
				}
				reconstructButton.setEnabled(true);
			}
		});
		selectAllButton.setText(MESSAGE_SELECT_ALL);

		deselectAllButton = new Button(groupShares, SWT.NONE);
		deselectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				for (int i = 0; i < sharesUseCheckButtonSet.length; i++) {
					sharesUseCheckButtonSet[i].setSelection(false);
				}
				reconstructButton.setEnabled(false);
			}
		});
		deselectAllButton.setEnabled(false);
		deselectAllButton.setText(MESSAGE_DESELECT_ALL);
		scrolledShares.setContent(compositeShares);
	}

	private void createGroupModus() {
        groupModus = new Group(groupSettings, SWT.NONE);
        groupModus.setText(MESSAGE_MODUS);
        groupModus.setLayout(new GridLayout(2, true));
        groupModus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        graphicalButton = new Button(groupModus, SWT.RADIO);
        graphicalButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                view.showGraphical();
            }
        });
        graphicalButton.setText(MESSAGE_GRAPHICAL);
        graphicalButton.setSelection(true);

        numericalButton = new Button(groupModus, SWT.RADIO);
        numericalButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                view.showNumerical();
            }
        });
        numericalButton.setText(MESSAGE_NUMERICAL);
    }

	/**
	 * creates the shares
	 * @param n number of share rows
	 */
	private void createShares(int n) {
		sharesUseCheckButtonSet = new Button[n];
		for (int i = 0; i < n; i++) {
			Label sharesPLabel = new Label(compositeShares, SWT.NONE);
			sharesPLabel.setText(MESSAGE_SHARE + (i + 1));

			Label sharesEquivalentLabel = new Label(compositeShares, SWT.NONE);
			sharesEquivalentLabel.setText(MESSAGE_EQUAL);

			Label sharesOpenBracetLabel = new Label(compositeShares, SWT.NONE);
			sharesOpenBracetLabel.setText(MESSAGE_LEFT_PARENTHESIS);

			Label sharesXCoordinateLabel = new Label(compositeShares, SWT.NONE);
			sharesXCoordinateLabel.setText(String.valueOf(i + 1));

			Label sharesSeperatorLabel = new Label(compositeShares, SWT.NONE);
			sharesSeperatorLabel.setText(MESSAGE_SEPERATOR);

			sharesYCoordinateText = new Text(compositeShares, SWT.READ_ONLY | SWT.BORDER);
			sharesYCoordinateText.setText(shares[i].getY().toString());
			GridData gd_sharesYCoordinateText = new GridData(SWT.FILL, SWT.CENTER, true, false);
			sharesYCoordinateText.setLayoutData(gd_sharesYCoordinateText);

			Label sharesCloseBarcetLabel = new Label(compositeShares, SWT.NONE);
			sharesCloseBarcetLabel.setText(MESSAGE_RIGHT_PARENTHESIS);

			Button sharesUseCheckButton = new Button(compositeShares, SWT.CHECK);
			sharesUseCheckButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					int checkButtonCounter = 0;
					for (int j = 0; j < sharesUseCheckButtonSet.length; j++) {
						if (sharesUseCheckButtonSet[j].getSelection()) {
							checkButtonCounter++;
						}
					}

					if (checkButtonCounter >= 2) {
						reconstructButton.setEnabled(true);
					} else {
						reconstructButton.setEnabled(false);
					}
				}
			});
			sharesUseCheckButtonSet[i] = sharesUseCheckButton;

		}
		compositeShares.pack();
	}

	private void createReconstruction(int n) {
		for (int i = 0; i < n; i++) {
			Label reconstructionLabel = new Label(compositeReconstruction, SWT.NONE);
			reconstructionLabel.setText(MESSAGE_W + convertToSubset(i));

			Label reconstructEquivalentLabel = new Label(compositeReconstruction, SWT.NONE);
			reconstructEquivalentLabel.setText(MESSAGE_EQUAL);

			Text reconstructPolynomial = new Text(compositeReconstruction, SWT.READ_ONLY | SWT.BORDER);
			reconstructPolynomial.setText(createPolynomialString(subpolynomial.get(i)));
			GridData gd_reconstructPolynomial = new GridData(SWT.FILL, SWT.CENTER, true, false);
			reconstructPolynomial.setLayoutData(gd_reconstructPolynomial);
		}
		compositeReconstruction.pack();
	}

	/**
	 * Converts the id value to subscript
	 * @param id
	 * @return a converted string
	 */
	private String convertToSubset(int id) {
		char[] data = String.valueOf(id).toCharArray();
		String result = "";

		for (int i = 0; i < data.length; i++) {
			if (data[i] == '0')
				result += uZero;

			if (data[i] == '1')
				result += uOne;

			if (data[i] == '2')
				result += uTwo;

			if (data[i] == '3')
				result += uThree;

			if (data[i] == '4')
				result += uFour;

			if (data[i] == '5')
				result += uFive;

			if (data[i] == '6')
				result += uSix;

			if (data[i] == '7')
				result += uSeven;

			if (data[i] == '8')
				result += uEight;

			if (data[i] == '9')
				result += uNine;
		}
		return result;
	}

	/**
	 * converts an array containing coefficients to a polynomial string
	 * @param coefficients
	 * @return a string representation of a polynomial
	 */
	private String createPolynomialString(BigInteger[] coefficients) {
		String result = "";

		for (int i = 0; i < coefficients.length; i++) {
			if (i == 0) {
				result = coefficients[i].toString() + " ";
			} else {
				BigInteger bi = coefficients[i];
				if (bi.compareTo(BigInteger.ZERO) != 0) {
					if (bi.compareTo(BigInteger.ZERO) < 0) {
						if (bi.compareTo(MINUS_ONE) == 0) {
							result += "-x" + convertToSuperscript(i) + " ";
						} else {
							result += coefficients[i] + "x" + convertToSuperscript(i) + " ";
						}
					} else {
						if (bi.compareTo(BigInteger.ONE) == 0) {
							result += "+ " + "x" + convertToSuperscript(i) + " ";
						} else {
							result += "+ " + coefficients[i] + "x" + convertToSuperscript(i) + " ";
						}
					}
				}
			}
		}
		result = result.trim();

		return result;
	}

	/**
	 * compares two BigInterger array equality
	 * @param a a BigInteger array
	 * @param b a BigInteger array
	 * @return true if the arrays are equal otherwise false
	 */
	private boolean comparePolynomial(BigInteger[] a, BigInteger[] b) {
		boolean result = true;
		int n;
		if (a.length < b.length) {
			n = a.length;
		} else {
			n = b.length;
		}

		for (int i = 0; i < n; i++) {
			if (!a[i].equals(b[i])) {
				result = false;
			}
		}
		return result;
	}

}
