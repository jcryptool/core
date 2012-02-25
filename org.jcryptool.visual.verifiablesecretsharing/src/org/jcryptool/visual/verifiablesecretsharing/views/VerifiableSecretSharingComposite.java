// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.verifiablesecretsharing.views;

import org.jcryptool.visual.verifiablesecretsharing.algorithm.Polynomial;
import org.jcryptool.visual.verifiablesecretsharing.algorithm.VerifiableSecretSharing;

import java.math.BigInteger;
import java.util.Random;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.util.fonts.FontService;

public class VerifiableSecretSharingComposite extends Composite {

	/* colors for backgrounds. */
	private static final Color WHITE = Display.getDefault().getSystemColor(
			SWT.COLOR_WHITE);
	private static final Color GREEN = Display.getDefault().getSystemColor(
			SWT.COLOR_GREEN);
	private static final Color RED = Display.getDefault().getSystemColor(
			SWT.COLOR_RED);

	/* number of players for reconstruction t */
	private static int playersRecon;
	private static int players;

	/* int array coefficients */
	private static int[] coefficientsInt;

	/* instance for calculating shares */
	private static VerifiableSecretSharing vss = new VerifiableSecretSharing();

	private static int[] playerID;

	/*
	 * list of safe primes, array-index is bit-length. value is biggest safe
	 * prime for this bit-length if value is -1, there is no safe prime for this
	 * bit-length
	 */
	private static int[] safePrimes = new int[] { -1, -1, 5, 7, 11, 23, 59,
			107, 227, 503, 1019, 2039, 4079, 8147, 16223, 32603, 65267, 130787,
			262127, 524243, 1048343, 2097143, 4194287 };

	/* if true, commit-Button got clicked */
	private static boolean commitmentsChecked = false;

	StyledText stDescription;
	private Composite inputBody;
	private GridLayout inputBodyLayout;
	private Group parametersGroup;
	private Label playerLabel;
	private Spinner playerSpinner;
	private Label reconstructorLabel;
	private Spinner reconstructorSpinner;
	private Label secretLabel;
	private Text secretText;
	private Label moduleLabel;
	private Text moduleText;
	private Label primitiveRootLabel;
	private Text primitiveRootText;
	private Label spaceLabel;
	private Label nextStepButtonParameters;
	private Button determineCoefficients;
	private RowLayout coefficientsGroupLayout;
	private GridLayout coefficientsPolynomNextStepLayout;
	private Group coefficientsGroup;
	private ScrolledComposite scrolledCoefficientsGroup;
	private Composite scrolledCoefficientsGroupContent;
	private Label[] coefficientsLabelsCoefficients;
	private Spinner[] coefficientsSpinnersCoefficients;
	private GridLayout commitGenerateButtonLayout;
	private Composite commitGenerateButtonComposite;
	private Button commitCoefficientsButton;
	private Button generateCoefficientsButton;
	private Composite polynomContent;
	private Label polynomLabel;
	private Text polynomText;
	private Composite nextStepContent;
	private Label nextStepButtonCoefficients;
	private Button calculateShares;
	private RowLayout commitmentsGroupLayout;
	private GridLayout commitmentsGroupGridLayout;
	private Group commitmentsGroup;
	private Label coefficientLabel;
	private Label commitmentLabel;
	private GridData seperatorData;
	private Label horizontalSeperator;
	private ScrolledComposite scrolledCommitmentsGroup;
	private Composite scrolledCommitmentsGroupContent;
	private Label[] coefficientsLabelsCommitment;
	private Text[] coefficientsTextCommitment;
	private Group sharesGroup;
	private Group reconstructionGroup;
	private Group descriptionGroup;
	private RowLayout sharesGroupLayout;
	private GridLayout sharesGroupGridLayout;
	private ScrolledComposite scrolledSharesGroup;
	private Composite scrolledSharesGroupContent;
	private Label indexLabel;
	private Label shareNLabel;
	private Label[] playerLabelShares;
	private Composite[] shareNCompositeShares;
	private Text[] shareNTextShares;
	private Label[] isModShares;
	private Text[] shareModNTextShares;
	private Button[] checkButtonShares;
	private RowLayout reconstructionGroupLayout;
	private GridLayout reconstructionGroupGridLayout;
	private ScrolledComposite scrolledReconstructionGroup;
	private Composite scrolledReconstructionGroupContent;
	private Label[] playerLabelReconstructions;
	private Button[] playerCheckboxReconstructions;
	private RowLayout shareModNRowLayout;
	private GridData nextStepSpanData;
	private GridLayout nextStepSpanLayout;
	private Composite nextStepParametersComposite;
	private Button reconstructButton;
	private Label descriptionLeft;
	private Label descriptionRight;
	private Label primeFactorLabel;
	private Text primeFactorText;

	public VerifiableSecretSharingComposite(final Composite parent,
			final int style,
			VerifiableSecretSharingView verifiableSecretSharingView) {
		super(parent, style);

		// default-values
		playersRecon = 2;
		players = 2;
		commitmentsChecked = false;

		setLayout(new GridLayout());
		createHead();
		createBody();
	}

	/**
	 * Generates the head of the tab. The head has a title and a description.
	 */
	private void createHead() {
		final Composite head = new Composite(this, SWT.NONE);
		head.setBackground(WHITE);
		head.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		head.setLayout(new GridLayout());

		final Label label = new Label(head, SWT.NONE);
		label.setFont(FontService.getHeaderFont());
		label.setBackground(WHITE);
		label.setText(Messages.VerifiableSecretSharingComposite_tab_title);
		stDescription = new StyledText(head, SWT.READ_ONLY | SWT.MULTI
				| SWT.WRAP);
		stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		stDescription
				.setText(Messages.VerifiableSecretSharingComposite_description);
	}

	private void createBody() {
		final Composite body = new Composite(this, SWT.NONE);
		body.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		body.setLayout(new GridLayout());

		createInputBody(body);
		createDescriptionGroup(body);
		createFocusHandlers();
	}

	private void createInputBody(Composite parent) {
		inputBody = new Composite(parent, SWT.NONE);
		inputBodyLayout = new GridLayout(5, false);
		inputBodyLayout.marginWidth = 0;
		inputBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		inputBody.setLayout(inputBodyLayout);

		createParametersGroup(inputBody);
		createCoefficientsGroup(inputBody);
		enableCoefficientsGroup(false, 1);
		createCommitmentsGroup(inputBody);
		enableCommitmentsGroup(false, 2);
		createSharesGroup(inputBody);
		enableSharesGroup(false, 2);
		createReconstructionGroup(inputBody);
		enableReconstructionGroup(false, 2);
	}

	private void createParametersGroup(Composite parent) {
		parametersGroup = new Group(parent, SWT.NONE);
		parametersGroup.setLayout(new GridLayout(2, false));
		parametersGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		parametersGroup
				.setText(Messages.VerifiableSecretSharingComposite_parameters_title);

		playerLabel = new Label(parametersGroup, SWT.NONE);
		playerLabel
				.setText(Messages.VerifiableSecretSharingComposite_parameters_players);

		playerSpinner = new Spinner(parametersGroup, SWT.BORDER);
		playerSpinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		playerSpinner.setMinimum(2);
		playerSpinner.setMaximum(100);

		reconstructorLabel = new Label(parametersGroup, SWT.NONE);
		reconstructorLabel
				.setText(Messages.VerifiableSecretSharingComposite_parameters_reconstructors);

		reconstructorSpinner = new Spinner(parametersGroup, SWT.BORDER);
		reconstructorSpinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));
		reconstructorSpinner.setMinimum(2);

		secretLabel = new Label(parametersGroup, SWT.NONE);
		secretLabel
				.setText(Messages.VerifiableSecretSharingComposite_parameters_secret);

		secretText = new Text(parametersGroup, SWT.BORDER);
		secretText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		secretText.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;

				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}

		});
		secretText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event e) {
				int nextPrime;
				String text = secretText.getText();
				BigInteger secret;
				int bitlength = 0;
				if (text != "") {
					if (Integer.parseInt(text) > 2000000) {
						secretText.setText("2000000");
						text = "2000000";
					} else if (Integer.parseInt(text) == 0) {
						Random randomGenerator = new Random();
						String newSecret = String.valueOf(randomGenerator
								.nextInt(2000000));
						secretText.setText(newSecret);
						text = newSecret;
					}
					secret = new BigInteger(text);
					bitlength = secret.bitLength();
					if (bitlength >= 3 && bitlength <= 21) {
						nextPrime = safePrimes[bitlength];
						if (nextPrime <= Integer.parseInt(text)
								|| (nextPrime - 1) / 2 <= Integer
										.parseInt(text)) {
							nextPrime = safePrimes[bitlength + 1];
							if ((nextPrime - 1) / 2 <= Integer.parseInt(text)) {
								nextPrime = safePrimes[bitlength + 2];
							}
						}
						moduleText.setText(nextPrime + "");
					} else {
						moduleText.setText("");
					}
				} else {
					moduleText.setText(text);
				}
			}
		});

		moduleLabel = new Label(parametersGroup, SWT.NONE);
		moduleLabel
				.setText(Messages.VerifiableSecretSharingComposite_parameters_primeMod);

		moduleText = new Text(parametersGroup, SWT.BORDER);
		moduleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		moduleText.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event ev) {
				String string = ev.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						ev.doit = false;
						return;
					}
				}
			}

		});
		moduleText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				int primitiveRoot;
				if (moduleText.getText().compareTo("") != 0
						&& Integer.parseInt(moduleText.getText()) > safePrimes[safePrimes.length - 1]) {
					moduleText.setText(safePrimes[safePrimes.length - 1] + "");
				}
				if (moduleText.getText().compareTo("") != 0
						&& new BigInteger(moduleText.getText())
								.isProbablePrime(3)
						&& new BigInteger(moduleText.getText())
								.subtract(BigInteger.ONE)
								.divide(new BigInteger("" + 2))
								.isProbablePrime(3)) {
					primeFactorText.setText(new BigInteger(moduleText.getText())
							.subtract(BigInteger.ONE)
							.divide(new BigInteger("" + 2)).toString());
					primitiveRoot = generatePrimitiveRoot(moduleText.getText());
					if (primitiveRoot != -1) {
						primitiveRootText.setText(primitiveRoot + "");
					} else {
						primeFactorText.setText("");
						primitiveRootText.setText("");
					}
				} else {
					primeFactorText.setText("");
					primitiveRootText.setText("");
				}
			}

			private int generatePrimitiveRoot(String p) {
				int pInt = Integer.parseInt(p);
				int qInt = (pInt - 1) / 2;
				for (int i = 2; i < pInt; i++) {
					int j = i, o = 1;
					do {
						o++;
						j = j * i % pInt;
					} while (j != 1);
					if (o == qInt) {
						return i;
					}
				}
				return -1;
			}
		});

		primeFactorLabel = new Label(parametersGroup, SWT.NONE);
		primeFactorLabel
				.setText(Messages.VerifiableSecretSharingComposite_parameters_primeFactorMod);

		primeFactorText = new Text(parametersGroup, SWT.BORDER | SWT.READ_ONLY);
		primeFactorText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		primeFactorText.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;

				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});

		primitiveRootLabel = new Label(parametersGroup, SWT.NONE);
		primitiveRootLabel
				.setText(Messages.VerifiableSecretSharingComposite_parameters_primitiveRoot);

		primitiveRootText = new Text(parametersGroup, SWT.BORDER
				| SWT.READ_ONLY);
		primitiveRootText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		primitiveRootText.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;

				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		primitiveRootText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				if (primitiveRootText.getText().compareTo("") != 0
						&& Integer.parseInt(primitiveRootText.getText()) > 2000000) {
					primitiveRootText.setText("2000000");
				}
			}
		});

		spaceLabel = new Label(parametersGroup, SWT.NONE);
		spaceLabel = new Label(parametersGroup, SWT.NONE);

		nextStepSpanData = new GridData(SWT.FILL, SWT.FILL, true, false);
		nextStepSpanData.horizontalSpan = 2;

		nextStepSpanLayout = new GridLayout(2, false);
		nextStepSpanLayout.marginWidth = 0;
		nextStepSpanLayout.marginHeight = 0;

		nextStepParametersComposite = new Composite(parametersGroup, SWT.NONE);
		nextStepParametersComposite.setLayoutData(nextStepSpanData);
		nextStepParametersComposite.setLayout(nextStepSpanLayout);

		nextStepButtonParameters = new Label(nextStepParametersComposite,
				SWT.NONE);
		nextStepButtonParameters
				.setText(Messages.VerifiableSecretSharingComposite_nextStep_button);
		determineCoefficients = new Button(nextStepParametersComposite,
				SWT.NONE);
		determineCoefficients
				.setText(Messages.VerifiableSecretSharingComposite_parameters_determineCoefficients);
		determineCoefficients.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));
		determineCoefficients.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				boolean everythingCorrect = true;
				String errorText = Messages.VerifiableSecretSharingComposite_error_start;
				BigInteger moduleTextBI;
				int i = 0;
				boolean warning = false;
				if (secretText.getText().compareTo("") == 0
						|| moduleText.getText().compareTo("") == 0
						|| primeFactorText.getText().compareTo("") == 0
						|| primitiveRootText.getText().compareTo("") == 0) {

					if (moduleText.getText().compareTo("") != 0
							&& primeFactorText.getText().compareTo("") == 0) {
						errorText += "\n\r"
								+ Messages.VerifiableSecretSharingComposite_param_p_not_safe_prime;
						while (i < safePrimes.length) {
							if (Integer.parseInt(moduleText.getText()) < safePrimes[i]) {
								moduleText.setText(safePrimes[i] + "");
								i = safePrimes.length;
							}
							i++;
						}
						everythingCorrect = false;
					} else {
						warning = true;
					}

				} else {
					if (isSubgroup(primitiveRootText.getText(),
							moduleText.getText()) == false) {
						errorText += "\n\r"
								+ Messages.VerifiableSecretSharingComposite_param_primitive_g;
						everythingCorrect = false;
					}
				}
				if (Integer.parseInt(playerSpinner.getText()) < Integer
						.parseInt(reconstructorSpinner.getText())) {
					errorText += "\n\r"
							+ Messages.VerifiableSecretSharingComposite_param_player_t_smaller_n;
					everythingCorrect = false;
				}

				if (moduleText.getText().compareTo("") != 0
						&& Integer.parseInt(moduleText.getText()) < Integer
								.parseInt(secretText.getText())) {
					errorText += "\n\r"
							+ Messages.VerifiableSecretSharingComposite_param_module_p_bigger_s;
					everythingCorrect = false;
				} else {
					if (moduleText.getText().compareTo("") != 0) {
						moduleTextBI = new BigInteger(moduleText.getText());
						if (moduleTextBI.isProbablePrime(3) == false) {
							errorText += "\n\r"
									+ Messages.VerifiableSecretSharingComposite_param_module_p_isPrime;
							everythingCorrect = false;
						}
					}
				}
				if (everythingCorrect) {
					if (warning) {
						MessageDialog
								.openWarning(
										getShell(),
										"",
										Messages.VerifiableSecretSharingComposite_param_set_all);
						if (secretText.getText().compareTo("") == 0) {
							do {
								secretText.setText(new Random()
										.nextInt(2000000) + "");
							} while (Integer.parseInt(secretText.getText()) <= 3);
						}
					}
					playersRecon = Integer.parseInt(reconstructorSpinner
							.getText());
					/* initiate array and set value for secret */
					coefficientsInt = new int[playersRecon];
					coefficientsInt[0] = Integer.parseInt(secretText.getText());
					/* ************************ */
					players = Integer.parseInt(playerSpinner.getText());
					enableCoefficientsGroup(true, (playersRecon - 1));
					enableParametersGroupWithoutDispose(false);
				} else {
					if (secretText.getText().compareTo("") == 0) {
						do {
							secretText.setText(new Random().nextInt(2000000)
									+ "");
						} while (Integer.parseInt(secretText.getText()) <= 3);
					}
					MessageDialog.openError(getShell(), "Error", errorText);
				}
			}
		});
	}

	private void enableParametersGroupWithoutDispose(boolean enableGroup) {

		for (Control control : parametersGroup.getChildren()) {
			control.setEnabled(enableGroup);
		}

		for (Control control : nextStepParametersComposite.getChildren()) {
			control.setEnabled(enableGroup);
		}
	}

	private void createCoefficientsGroup(Composite parent) {
		coefficientsGroupLayout = new RowLayout();
		coefficientsGroupLayout.type = SWT.VERTICAL;
		coefficientsGroupLayout.marginWidth = 0;
		coefficientsGroupLayout.marginHeight = 0;

		coefficientsPolynomNextStepLayout = new GridLayout(2, false);
		coefficientsPolynomNextStepLayout.marginWidth = 0;
		coefficientsPolynomNextStepLayout.marginHeight = 0;

		coefficientsGroup = new Group(parent, SWT.NONE);
		coefficientsGroup.setLayout(coefficientsGroupLayout);
		coefficientsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		coefficientsGroup
				.setText(Messages.VerifiableSecretSharingComposite_coefficients_title);

		scrolledCoefficientsGroup = new ScrolledComposite(coefficientsGroup,
				SWT.V_SCROLL);
		scrolledCoefficientsGroup.setExpandHorizontal(true);
		scrolledCoefficientsGroup.setLayoutData(new RowData(200, 104));
		scrolledCoefficientsGroup.setBackground(WHITE);

		scrolledCoefficientsGroupContent = new Composite(
				scrolledCoefficientsGroup, SWT.NONE);
		scrolledCoefficientsGroupContent
				.setLayout(coefficientsPolynomNextStepLayout);
		scrolledCoefficientsGroupContent.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, false));
		scrolledCoefficientsGroupContent.setBackground(WHITE);

		commitGenerateButtonLayout = new GridLayout(2, true);
		commitGenerateButtonLayout.marginWidth = 0;
		commitGenerateButtonLayout.marginHeight = 0;

		commitGenerateButtonComposite = new Composite(coefficientsGroup,
				SWT.NONE);
		commitGenerateButtonComposite.setLayout(commitGenerateButtonLayout);
		commitGenerateButtonComposite.setLayoutData(new RowData(220, -1));

		generateCoefficientsButton = new Button(commitGenerateButtonComposite,
				SWT.PUSH);
		generateCoefficientsButton
				.setText(Messages.VerifiableSecretSharingComposite_coefficients_generate_button);
		generateCoefficientsButton.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, false));
		generateCoefficientsButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Random randomGenerator = new Random();
				for (int i = 1; i < playersRecon; i++) {
					coefficientsSpinnersCoefficients[i]
							.setSelection(randomGenerator.nextInt(Integer
									.parseInt(primeFactorText.getText())));
				}
				for (int i = 0; i < playersRecon; i++) {
					coefficientsInt[i] = Integer
							.parseInt(coefficientsSpinnersCoefficients[i]
									.getText());
				}
				generatePolynom();
			}
		});

		commitCoefficientsButton = new Button(commitGenerateButtonComposite,
				SWT.NONE);
		commitCoefficientsButton
				.setText(Messages.VerifiableSecretSharingComposite_coefficients_commit_button);
		commitCoefficientsButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));

		commitCoefficientsButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				enableCommitmentsGroup(true, (playersRecon));
				vss.commitment(Integer.parseInt(primitiveRootText.getText()),
						coefficientsInt, Integer.parseInt(moduleText.getText()));
				for (int i = 0; i < coefficientsSpinnersCoefficients.length; i++) {
					coefficientsTextCommitment[i].setText(String.valueOf(vss
							.getCommitments()[i]));
				}
				commitmentsChecked = true;
			}
		});

		polynomContent = new Composite(coefficientsGroup, SWT.NONE);
		polynomContent.setLayout(coefficientsPolynomNextStepLayout);
		polynomContent.setLayoutData(new RowData(220, -1));

		polynomLabel = new Label(polynomContent, SWT.NONE);
		polynomLabel.setText("P(x)    ");

		polynomText = new Text(polynomContent, SWT.BORDER | SWT.READ_ONLY);
		polynomText
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		nextStepContent = new Composite(coefficientsGroup, SWT.NONE);
		nextStepContent.setLayout(coefficientsPolynomNextStepLayout);
		nextStepContent.setLayoutData(new RowData(220, -1));

		spaceLabel = new Label(nextStepContent, SWT.NONE);
		spaceLabel = new Label(nextStepContent, SWT.NONE);

		nextStepButtonCoefficients = new Label(nextStepContent, SWT.NONE);
		nextStepButtonCoefficients
				.setText(Messages.VerifiableSecretSharingComposite_nextStep_button);
		calculateShares = new Button(nextStepContent, SWT.NONE);
		calculateShares
				.setText(Messages.VerifiableSecretSharingComposite_coefficients_calculateShares_button);
		calculateShares.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		calculateShares.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				enableSharesGroup(true, players);
				vss.calculateShares(coefficientsInt,
						Integer.parseInt(primeFactorText.getText()), players);
				for (int i = 0; i < players; i++) {
					shareModNTextShares[i].setText(String.valueOf(vss
							.getSharesModQ()[i]));
					shareNTextShares[i].setText(vss.getSharesBig()[i]
							.toString());
				}
				enableReconstructionGroup(true, players);
				enableCoefficientsGroupWithoutDispose(false);
			}
		});
	}

	private void enableCoefficientsGroup(boolean enableGroup, int coefficients) {
		for (Control control : scrolledCoefficientsGroupContent.getChildren()) {
			control.dispose();
		}
		coefficientsGroup.setEnabled(enableGroup);

		coefficientsLabelsCoefficients = new Label[coefficients + 1];
		coefficientsSpinnersCoefficients = new Spinner[coefficients + 1];
		coefficientsLabelsCoefficients[0] = new Label(
				scrolledCoefficientsGroupContent, SWT.NONE);
		coefficientsLabelsCoefficients[0].setBackground(WHITE);
		coefficientsLabelsCoefficients[0].setText("a"
				+ convertIntegerToSubscript(0) + " = s");
		coefficientsSpinnersCoefficients[0] = new Spinner(
				scrolledCoefficientsGroupContent, SWT.BORDER);
		coefficientsSpinnersCoefficients[0].setLayoutData(new GridData(
				SWT.FILL, SWT.FILL, true, false));
		if (enableGroup) {
			coefficientsInt[0] = Integer.parseInt(secretText.getText());
			coefficientsSpinnersCoefficients[0].setMaximum(Integer
					.parseInt(primeFactorText.getText()) - 1);
			coefficientsSpinnersCoefficients[0].setSelection(Integer
					.parseInt(secretText.getText()));
		}
		for (int i = 1; i <= coefficients; i++) {
			if (enableGroup) {
				coefficientsInt[i] = 1;
			}

			coefficientsLabelsCoefficients[i] = new Label(
					scrolledCoefficientsGroupContent, SWT.NONE);
			coefficientsLabelsCoefficients[i].setText("a"
					+ convertIntegerToSubscript(i));
			coefficientsLabelsCoefficients[i].setBackground(WHITE);

			coefficientsSpinnersCoefficients[i] = new Spinner(
					scrolledCoefficientsGroupContent, SWT.BORDER);
			coefficientsSpinnersCoefficients[i].setLayoutData(new GridData(
					SWT.FILL, SWT.FILL, true, false));
			coefficientsSpinnersCoefficients[i].setMinimum(1);
			if (enableGroup) {
				coefficientsSpinnersCoefficients[i].setMaximum(Integer
						.parseInt(primeFactorText.getText()) - 1);
			}
			coefficientsSpinnersCoefficients[i].addListener(SWT.Modify,
					new Listener() {
						@Override
						public void handleEvent(Event event) {
							generatePolynom();
							for (int i = 0; i < coefficientsSpinnersCoefficients.length; i++) {
								coefficientsInt[i] = Integer
										.parseInt(coefficientsSpinnersCoefficients[i]
												.getText());
							}
						}
					});
		}
		generatePolynom();
		scrolledCoefficientsGroup.setContent(scrolledCoefficientsGroupContent);
		scrolledCoefficientsGroupContent.pack();
		for (Control control : scrolledCoefficientsGroupContent.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(2);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}

		for (Control control : scrolledCoefficientsGroupContent.getChildren()) {
			control.setEnabled(enableGroup);
		}
		for (Control control : commitGenerateButtonComposite.getChildren()) {
			control.setEnabled(enableGroup);
		}
		for (Control control : polynomContent.getChildren()) {
			control.setEnabled(enableGroup);
		}
		for (Control control : nextStepContent.getChildren()) {
			control.setEnabled(enableGroup);
		}
		coefficientsSpinnersCoefficients[0].setEnabled(false);
	}

	private void enableCoefficientsGroupWithoutDispose(boolean enableGroup) {
		for (Control control : scrolledCoefficientsGroupContent.getChildren()) {
			control.setEnabled(enableGroup);
		}
		for (Control control : commitGenerateButtonComposite.getChildren()) {
			control.setEnabled(enableGroup);
		}
		for (Control control : polynomContent.getChildren()) {
			control.setEnabled(enableGroup);
		}
		for (Control control : nextStepContent.getChildren()) {
			control.setEnabled(enableGroup);
		}
	}

	private void createCommitmentsGroup(Composite parent) {
		commitmentsGroupLayout = new RowLayout();
		commitmentsGroupLayout.type = SWT.VERTICAL;

		commitmentsGroupGridLayout = new GridLayout(2, false);
		commitmentsGroupGridLayout.marginWidth = 0;
		commitmentsGroupGridLayout.marginHeight = 0;

		commitmentsGroup = new Group(parent, SWT.NONE);
		commitmentsGroup.setLayout(commitmentsGroupLayout);
		commitmentsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		commitmentsGroup
				.setText(Messages.VerifiableSecretSharingComposite_commitments_title);
		commitmentsGroup.setData(false);

		scrolledCommitmentsGroup = new ScrolledComposite(commitmentsGroup,
				SWT.V_SCROLL);
		scrolledCommitmentsGroup.setExpandHorizontal(true);
		scrolledCommitmentsGroup.setLayoutData(new RowData(150, 205));
		scrolledCommitmentsGroup.setBackground(WHITE);

		scrolledCommitmentsGroupContent = new Composite(
				scrolledCommitmentsGroup, SWT.NONE);
		scrolledCommitmentsGroupContent.setLayout(commitmentsGroupGridLayout);
		scrolledCommitmentsGroupContent.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, false));
		scrolledCommitmentsGroupContent.setBackground(WHITE);

		coefficientLabel = new Label(scrolledCommitmentsGroupContent, SWT.NONE);
		coefficientLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL,
				false, true));
		coefficientLabel
				.setText(Messages.VerifiableSecretSharingComposite_commitments_coefficient_subtitle);
		coefficientLabel.setData(false);
		coefficientLabel.setBackground(WHITE);

		commitmentLabel = new Label(scrolledCommitmentsGroupContent, SWT.NONE);
		commitmentLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false,
				true));
		commitmentLabel
				.setText(Messages.VerifiableSecretSharingComposite_commitments_commitment_subtitle);
		commitmentLabel.setData(false);
		commitmentLabel.setBackground(WHITE);

		seperatorData = new GridData(SWT.FILL, SWT.FILL, true, false);
		seperatorData.horizontalSpan = 2;

		horizontalSeperator = new Label(scrolledCommitmentsGroupContent,
				SWT.SEPARATOR | SWT.HORIZONTAL);
		horizontalSeperator.setLayoutData(seperatorData);
		horizontalSeperator.setData(false);

	}

	private void enableCommitmentsGroup(boolean enableGroup, int commitments) {
		commitmentsGroup.setEnabled(enableGroup);
		for (Control control : scrolledCommitmentsGroupContent.getChildren()) {

			if (control.getData() == null) {
				control.dispose();
			}
		}

		coefficientsLabelsCommitment = new Label[commitments];
		coefficientsTextCommitment = new Text[commitments];
		for (int i = 0; i < commitments; i++) {
			coefficientsLabelsCommitment[i] = new Label(
					scrolledCommitmentsGroupContent, SWT.NONE);
			coefficientsLabelsCommitment[i].setText("a"
					+ convertIntegerToSubscript(i));
			coefficientsLabelsCommitment[i].setLayoutData(new GridData(
					SWT.CENTER, SWT.FILL, true, true));
			coefficientsLabelsCommitment[i].setBackground(WHITE);

			coefficientsTextCommitment[i] = new Text(
					scrolledCommitmentsGroupContent, SWT.BORDER | SWT.READ_ONLY);
			coefficientsTextCommitment[i].setLayoutData(new GridData(SWT.FILL,
					SWT.FILL, true, false));
		}

		scrolledCommitmentsGroup.setContent(scrolledCommitmentsGroupContent);
		scrolledCommitmentsGroupContent.pack();
		for (Control control : scrolledCommitmentsGroupContent.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(3);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
		for (Control control : scrolledCommitmentsGroupContent.getChildren()) {
			control.setEnabled(enableGroup);
		}
	}

	private void createSharesGroup(Composite parent) {
		sharesGroupLayout = new RowLayout();
		sharesGroupLayout.type = SWT.VERTICAL;

		sharesGroupGridLayout = new GridLayout(3, false);
		sharesGroupGridLayout.marginWidth = 0;
		sharesGroupGridLayout.marginHeight = 0;

		sharesGroup = new Group(parent, SWT.NONE);
		sharesGroup.setLayout(sharesGroupLayout);
		sharesGroup
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		sharesGroup
				.setText(Messages.VerifiableSecretSharingComposite_shares_title);

		scrolledSharesGroup = new ScrolledComposite(sharesGroup, SWT.V_SCROLL);
		scrolledSharesGroup.setExpandHorizontal(true);
		scrolledSharesGroup.setLayoutData(new RowData(250, 205));
		scrolledSharesGroup.setBackground(WHITE);

		scrolledSharesGroupContent = new Composite(scrolledSharesGroup,
				SWT.NONE);
		scrolledSharesGroupContent.setLayout(sharesGroupGridLayout);
		scrolledSharesGroupContent.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, false));
		scrolledSharesGroupContent.setBackground(WHITE);

		indexLabel = new Label(scrolledSharesGroupContent, SWT.NONE);
		indexLabel
				.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true));
		indexLabel.setText(Messages.VerifiableSecretSharingComposite_playerX
				+ " i");
		indexLabel.setData(false);
		indexLabel.setBackground(WHITE);

		shareNLabel = new Label(scrolledSharesGroupContent, SWT.NONE);
		shareNLabel
				.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
		shareNLabel
				.setText(Messages.VerifiableSecretSharingComposite_shares_shareNModP_subtitle);
		shareNLabel.setData(false);
		shareNLabel.setBackground(WHITE);

		spaceLabel = new Label(scrolledSharesGroupContent, SWT.NONE);
		spaceLabel.setData(false);

		seperatorData = new GridData(SWT.FILL, SWT.FILL, true, false);
		seperatorData.horizontalSpan = 4;

		horizontalSeperator = new Label(scrolledSharesGroupContent,
				SWT.SEPARATOR | SWT.HORIZONTAL);
		horizontalSeperator.setLayoutData(seperatorData);
		horizontalSeperator.setData(false);

	}

	private void enableSharesGroup(boolean enableGroup, int shares) {
		sharesGroup.setEnabled(enableGroup);
		for (Control control : scrolledSharesGroupContent.getChildren()) {
			if (control.getData() == null
					|| (control.getData() != null && (control.getData() instanceof Boolean) == false)) {
				control.dispose();
			}
		}
		playerLabelShares = new Label[shares];
		shareNCompositeShares = new Composite[shares];
		shareNTextShares = new Text[shares];
		isModShares = new Label[shares];
		shareModNTextShares = new Text[shares];
		checkButtonShares = new Button[shares];
		playerID = new int[shares];

		shareModNRowLayout = new RowLayout();
		shareModNRowLayout.type = SWT.HORIZONTAL;
		shareModNRowLayout.wrap = false;

		for (int i = 0; i < shares; i++) {
			playerID[i] = i + 1;
			playerLabelShares[i] = new Label(scrolledSharesGroupContent,
					SWT.NONE);
			playerLabelShares[i]
					.setText(Messages.VerifiableSecretSharingComposite_playerX
							+ " " + (i + 1));
			playerLabelShares[i].setLayoutData(new GridData(SWT.CENTER,
					SWT.FILL, true, true));
			playerLabelShares[i].setBackground(WHITE);

			shareNCompositeShares[i] = new Composite(
					scrolledSharesGroupContent, SWT.NONE);
			shareNCompositeShares[i].setLayoutData(new GridData(SWT.CENTER,
					SWT.FILL, false, false));
			shareNCompositeShares[i].setLayout(shareModNRowLayout);
			shareNCompositeShares[i].setBackground(WHITE);

			shareNTextShares[i] = new Text(shareNCompositeShares[i], SWT.BORDER
					| SWT.READ_ONLY);
			shareNTextShares[i].setLayoutData(new RowData(43, -1));

			isModShares[i] = new Label(shareNCompositeShares[i], SWT.NONE);
			isModShares[i].setText("\u2261");
			isModShares[i].setBackground(WHITE);

			shareModNTextShares[i] = new Text(shareNCompositeShares[i],
					SWT.BORDER);
			shareModNTextShares[i].setLayoutData(new RowData(43, -1));
			shareModNTextShares[i].setData(i);

			shareModNTextShares[i].addListener(SWT.Verify, new Listener() {
				public void handleEvent(Event e) {
					String string = e.text;

					char[] chars = new char[string.length()];
					string.getChars(0, chars.length, chars, 0);
					for (int i = 0; i < chars.length; i++) {
						if (!('0' <= chars[i] && chars[i] <= '9')) {
							e.doit = false;
							return;
						}
					}
				}

			});

			shareModNTextShares[i].addListener(SWT.Modify, new Listener() {
				public void handleEvent(Event e) {
					int newShareModP;
					int i = (Integer) e.widget.getData();
					if (shareModNTextShares[i].getText().compareTo("") != 0) {
						newShareModP = Integer.parseInt(shareModNTextShares[i]
								.getText());
						vss.setSharesModQ(i, newShareModP);
					}

				}
			});

			checkButtonShares[i] = new Button(scrolledSharesGroupContent,
					SWT.NONE);
			checkButtonShares[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					true, true));
			checkButtonShares[i]
					.setText(Messages.VerifiableSecretSharingComposite_shares_check_button);
			checkButtonShares[i].setData(i);
			checkButtonShares[i].addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (commitmentsChecked == true) {
						enableCoefficientsGroupWithoutDispose(false);
						if (vss.check(
								Integer.parseInt(primitiveRootText.getText()),
								Integer.parseInt(moduleText.getText()),
								playerID[(Integer) e.widget.getData()]) == true) {
							shareModNTextShares[(Integer) e.widget.getData()]
									.setBackground(GREEN);
						} else {
							shareModNTextShares[(Integer) e.widget.getData()]
									.setBackground(RED);
						}
					} else {
						String errorText = Messages.VerifiableSecretSharingComposite_commitment_not_calculated;
						MessageDialog.openError(getShell(), "Error", errorText);
						enableCoefficientsGroupWithoutDispose(true);
						enableSharesGroup(false, players);
						enableReconstructionGroup(false, players);

						;
					}
				}
			});

			for (Control control : shareNCompositeShares[i].getChildren()) {
				control.setEnabled(enableGroup);
			}
		}

		scrolledSharesGroup.setContent(scrolledSharesGroupContent);
		scrolledSharesGroupContent.pack();
		for (Control control : scrolledSharesGroupContent.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(4);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
		for (Composite composite : shareNCompositeShares) {
			for (Control control : composite.getChildren()) {
				control.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent e) {
						showDescription(4);
					}

					@Override
					public void focusLost(FocusEvent e) {
					}
				});
			}
		}
		for (Control control : scrolledSharesGroupContent.getChildren()) {
			control.setEnabled(enableGroup);
		}
	}

	private void createReconstructionGroup(Composite parent) {
		reconstructionGroupLayout = new RowLayout();
		reconstructionGroupLayout.type = SWT.VERTICAL;

		reconstructionGroupGridLayout = new GridLayout(2, false);
		reconstructionGroupGridLayout.marginWidth = 0;
		reconstructionGroupGridLayout.marginHeight = 0;

		reconstructionGroup = new Group(parent, SWT.NONE);
		reconstructionGroup.setLayout(reconstructionGroupLayout);
		reconstructionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false));
		reconstructionGroup
				.setText(Messages.VerifiableSecretSharingComposite_reconstruction_title);

		scrolledReconstructionGroup = new ScrolledComposite(
				reconstructionGroup, SWT.V_SCROLL);
		scrolledReconstructionGroup.setExpandHorizontal(true);
		scrolledReconstructionGroup.setLayoutData(new RowData(100, 170));
		scrolledReconstructionGroup.setBackground(WHITE);

		scrolledReconstructionGroupContent = new Composite(
				scrolledReconstructionGroup, SWT.NONE);
		scrolledReconstructionGroupContent
				.setLayout(reconstructionGroupGridLayout);
		scrolledReconstructionGroupContent.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, false));
		scrolledReconstructionGroupContent.setBackground(WHITE);

		reconstructButton = new Button(reconstructionGroup, SWT.NONE);
		reconstructButton
				.setText(Messages.VerifiableSecretSharingComposite_reconstruction_reconstruct_button);
		reconstructButton.setLayoutData(new RowData(120, -1));
		reconstructButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReconstructionChartComposite rcc;
				Polynomial reconstruction;
				int[] playerIdsHelp = new int[Integer
						.parseInt(reconstructorSpinner.getText())];
				BigInteger[] sharesHelp = new BigInteger[Integer
						.parseInt(reconstructorSpinner.getText())];
				int[] playerIds;
				BigInteger[] shares;
				int i = 0;
				try {
					IViewReference[] platformParts = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getViewReferences();
					for (IViewReference platformPart : platformParts) {
						if (platformPart.getPartName().compareTo(
								"Verifiable Secret Sharing") == 0) {

							for (Control control : scrolledReconstructionGroupContent
									.getChildren()) {
								if (i == playerIdsHelp.length) {
									break;
								}
								if (control.getData() != null
										&& ((Button) control).getSelection()) {
									playerIdsHelp[i] = Integer.parseInt(control
											.getData().toString());
									// sharesHelp[i] =
									// Integer.parseInt(shareNTextShares[playerIdsHelp[i]-1].getText());
									sharesHelp[i] = new BigInteger(
											shareNTextShares[playerIdsHelp[i] - 1]
													.getText());
									i++;
								}
							}
							playerIds = new int[i];
							shares = new BigInteger[i];
							if (playerIds.length == 0) {
								String errorText = Messages.VerifiableSecretSharingComposite_reconstruct_no_players;
								MessageDialog.openError(getShell(), "Error",
										errorText);
								break;
							} else {
								for (int j = 0; j < playerIds.length; j++) {
									playerIds[j] = playerIdsHelp[j];
									shares[j] = sharesHelp[j];
								}
								reconstruction = vss.reconstruct(playerIds,
										Integer.parseInt(primeFactorText
												.getText()));

								if (polynomText.getText().compareTo(
										reconstruction.toString()) == 0) {
									MessageDialog
											.openInformation(
													getShell(),
													Messages.VerifiableSecretSharingComposite_reconstruction_reconstruct_dialog_title,
													Messages.ChartComposite_reconstruct_success
															+ Messages.VerifiableSecretSharingComposite_reconstruction_reconstruct_dialog_text
															+ reconstruction
																	.getCoef()[0]
																	.toString()
															+ ".");
								} else {
									MessageDialog
											.openInformation(
													getShell(),
													Messages.VerifiableSecretSharingComposite_reconstruction_reconstruct_dialog_title,
													Messages.ChartComposite_reconstruct_failure
															+ Messages.VerifiableSecretSharingComposite_reconstruction_reconstruct_dialog_text
															+ reconstruction
																	.getCoef()[0]
																	.toString()
															+ ".");
								}
								//MessageDialog.openError(getShell(), "Error",errorText);
								rcc = ((VerifiableSecretSharingView) platformPart
										.getView(false))
										.getReconstructionChartComposite();
								rcc.setReconstructedPolynom(reconstruction);
								rcc.setPlayerID(playerIds);
								rcc.setShares(shares);
								rcc.setPolynom(polynomText.getText());
								rcc.setSecret(Integer.valueOf(secretText
										.getText()));
								rcc.redrawChart();
								((VerifiableSecretSharingView) platformPart
										.getView(false))
										.setFocusOnReconstructionTab(true);
							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	private void enableReconstructionGroup(boolean enableGroup, int player) {
		reconstructionGroup.setEnabled(enableGroup);
		for (Control control : scrolledReconstructionGroupContent.getChildren()) {
			control.dispose();
		}
		playerLabelReconstructions = new Label[player];
		playerCheckboxReconstructions = new Button[player];

		for (int i = 0; i < player; i++) {
			playerLabelReconstructions[i] = new Label(
					scrolledReconstructionGroupContent, SWT.NONE);
			playerLabelReconstructions[i]
					.setText(Messages.VerifiableSecretSharingComposite_playerX
							+ " " + (i + 1));
			playerLabelReconstructions[i].setLayoutData(new GridData(SWT.LEFT,
					SWT.FILL, true, true));
			playerLabelReconstructions[i].setBackground(WHITE);

			playerCheckboxReconstructions[i] = new Button(
					scrolledReconstructionGroupContent, SWT.CHECK);
			playerCheckboxReconstructions[i].setLayoutData(new GridData(
					SWT.CENTER, SWT.FILL, true, false));
			playerCheckboxReconstructions[i].setBackground(WHITE);
			playerCheckboxReconstructions[i].setData(i + 1 + "");
		}

		scrolledReconstructionGroup
				.setContent(scrolledReconstructionGroupContent);
		scrolledReconstructionGroupContent.pack();
		for (Control control : scrolledReconstructionGroupContent.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(5);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
		for (Control control : scrolledReconstructionGroupContent.getChildren()) {
			control.setEnabled(enableGroup);
		}
		for (Control control : reconstructionGroup.getChildren()) {
			control.setEnabled(enableGroup);
		}
	}

	private void createDescriptionGroup(Composite body) {
		descriptionGroup = new Group(body, SWT.NONE);
		descriptionGroup.setLayout(new GridLayout(2, true));
		descriptionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		descriptionGroup
				.setText(Messages.VerifiableSecretSharingComposite_description_title);
		descriptionGroup
				.setToolTipText(Messages.VerifiableSecretSharingComposite_description_tooltip);
	}

	/**
	 * 
	 * @param group
	 *            Description for this group 1: parameters-group 2:
	 *            coefficients-group 3: commitments-group 4: shares-group 5:
	 *            reconstruction-group
	 */
	private void showDescription(int group) {
		Image imageCheck = new Image(getDisplay(),
				VerifiableSecretSharingComposite.class
						.getResourceAsStream("check.png"));
		Image imageReconstruct = new Image(getDisplay(),
				VerifiableSecretSharingComposite.class
						.getResourceAsStream("reconstruction.png"));
		Label image;
		Label descPart2;
		for (Control control : descriptionGroup.getChildren()) {
			control.dispose();
		}
		descriptionLeft = new Label(descriptionGroup, SWT.NONE);
		descriptionRight = new Label(descriptionGroup, SWT.NONE);
		switch (group) {
		case 1:
			descriptionLeft
					.setText(Messages.VerifiableSecretSharingComposite_description_parameters_left);
			descriptionRight
					.setText(Messages.VerifiableSecretSharingComposite_description_parameters_right);
			break;
		case 2:
			descriptionLeft
					.setText(Messages.VerifiableSecretSharingComposite_description_coefficients_left);
			descriptionRight
					.setText(Messages.VerifiableSecretSharingComposite_description_coefficients_right);
			break;
		case 3:
			descriptionLeft
					.setText(Messages.VerifiableSecretSharingComposite_description_commitments_left);
			descriptionRight
					.setText(Messages.VerifiableSecretSharingComposite_description_commitments_right);
			break;
		case 4:
			descriptionLeft
					.setText(Messages.VerifiableSecretSharingComposite_description_shares_left);
			descriptionRight
					.setText(Messages.VerifiableSecretSharingComposite_description_shares_right);
			spaceLabel = new Label(descriptionGroup, SWT.NONE);
			image = new Label(descriptionGroup, SWT.NONE);
			image.setImage(imageCheck);
			break;
		case 5:
			descriptionLeft
					.setText(Messages.VerifiableSecretSharingComposite_description_reconstruction_left);
			descriptionRight
					.setText(Messages.VerifiableSecretSharingComposite_description_reconstruction_right);
			image = new Label(descriptionGroup, SWT.NONE);
			image.setImage(imageReconstruct);
			descPart2 = new Label(descriptionGroup, SWT.NONE);
			descPart2
					.setText(Messages.VerifiableSecretSharingComposite_description_reconstruction_right_part2);
			break;
		default:

		}
		descriptionGroup.layout();
	}

	private void createFocusHandlers() {
		for (Control control : parametersGroup.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(1);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
		for (Control control : coefficientsGroup.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(2);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
		for (Control control : commitGenerateButtonComposite.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(2);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
		for (Control control : polynomContent.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(2);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
		for (Control control : nextStepContent.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(2);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
		for (Control control : commitmentsGroup.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(3);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}

		for (Control control : sharesGroup.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(4);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
		for (Control control : reconstructionGroup.getChildren()) {
			control.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					showDescription(5);
				}

				@Override
				public void focusLost(FocusEvent e) {
				}
			});
		}
	}

	private String convertIntegerToSubscript(int number) {
		String result = "";
		String numberString = number + "";
		for (int i = 0; i < numberString.length(); i++) {
			switch (numberString.charAt(i)) {
			case '0':
				result += "\u2080";
				break;
			case '1':
				result += "\u2081";
				break;
			case '2':
				result += "\u2082";
				break;
			case '3':
				result += "\u2083";
				break;
			case '4':
				result += "\u2084";
				break;
			case '5':
				result += "\u2085";
				break;
			case '6':
				result += "\u2086";
				break;
			case '7':
				result += "\u2087";
				break;
			case '8':
				result += "\u2088";
				break;
			case '9':
				result += "\u2089";
				break;
			default:
				result += "";
			}
		}
		return result;
	}

	private String convertIntegerToSuperscript(int number) {
		String result = "";
		String numberString = number + "";
		for (int i = 0; i < numberString.length(); i++) {
			switch (numberString.charAt(i)) {
			case '0':
				result += "\u2070";
				break;
			case '1':
				result += "\u00B9";
				break;
			case '2':
				result += "\u00B2";
				break;
			case '3':
				result += "\u00B3";
				break;
			case '4':
				result += "\u2074";
				break;
			case '5':
				result += "\u2075";
				break;
			case '6':
				result += "\u2076";
				break;
			case '7':
				result += "\u2077";
				break;
			case '8':
				result += "\u2078";
				break;
			case '9':
				result += "\u2079";
				break;
			default:
				result += "";
			}
		}
		return result;
	}

	private boolean isSubgroup(String g, String p) {
		BigInteger pBigInt = new BigInteger(p);
		BigInteger gBigInt = new BigInteger(g);
		BigInteger j;
		BigInteger o;
		for (int i = 2; new BigInteger(i + "").compareTo(pBigInt) < 0; i++) {
			j = new BigInteger(i + "");
			o = BigInteger.ONE;
			do {
				o = o.add(BigInteger.ONE);
				j = j.multiply(new BigInteger(i + "")).mod(pBigInt);
			} while (j.compareTo(BigInteger.ONE) != 0);
			if (o.compareTo(pBigInt.subtract(BigInteger.ONE).divide(
					new BigInteger("2"))) == 0
					&& gBigInt.compareTo(new BigInteger(i + "")) == 0) {
				return true;
			}
		}
		return false;
	}

	private void generatePolynom() {
		String polynom = coefficientsSpinnersCoefficients[0].getText() + " + "
				+ coefficientsSpinnersCoefficients[1].getText() + "x + ";

		for (int i = 2; i < playersRecon; i++) {
			polynom += coefficientsSpinnersCoefficients[i].getText() + "x"
					+ convertIntegerToSuperscript(i) + " + ";
		}

		polynomText.setText(polynom.substring(0, polynom.length() - 3));

	}

}
