// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crt.views;

import java.math.BigInteger;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
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
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.core.util.ui.auto.SmoothScroller;
import org.jcryptool.visual.crt.algorithm.ChineseRemainderTheorem;
import org.jcryptool.visual.crt.xeuclid.XEuclid;

public class CRTGroup extends Composite implements Constants {

	public static boolean execute;

	private Composite content;
	private Composite verifyGroup;
	private Group scrolledVerify;
	private Group scrolledEquation;
	private Group scrolledInverse;
	private Composite equationGroup;
	private Composite inverseGroup;
	private Button previousButton;
	private Button nextButton;
	private Button reset;
	private Button step4nextButton;
	private Button step3nextButton;
	private Button step2nextButton;
	private Button step1nextButton;
	private Group resultGroup;
	private Group step4Group;
	private Group step3Group;
	private Group step2Group;
	private Group step1Group;
	private Text resultMoreText;
	private Text resultText;
	private Text resultValueText;
	private Text resultXText;
	private Text step4Text;
	private Text step3Text;
	private Text step2Text;

	private Text step1Text;

	private ChineseRemainderTheorem crt;
	private Equations equations;
	private int numberOfEquations;
	private boolean showDialog;
	private BigInteger result;

	private ChineseRemainderTheoremView view;
	private Vector<Control> inverseEquationSet;

	private Vector<Control> verifyEquationSet;
	private ScrolledComposite scrolledComposite_1;
	private ScrolledComposite scrolledComposite_2;

	/**
	 * Create the composite
	 *
	 * @param parent
	 * @param style
	 * @param equations
	 * @param chineseRemainderView
	 */
	public CRTGroup(Composite parent, int style, final ChineseRemainderTheoremView view) {
		super(parent, style);
		this.view = view;
		setLayout(new FillLayout());

		equations = new Equations();
		inverseEquationSet = new Vector<Control>();
		verifyEquationSet = new Vector<Control>();

		ScrolledComposite scrolledGroup = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledGroup.setExpandHorizontal(true);
		scrolledGroup.setExpandVertical(true);

		content = new Composite(scrolledGroup, SWT.NONE);
		content.setLayout(new GridLayout(2, false));
		
		// This creates the header and the description
		TitleAndDescriptionComposite titleAndDescription = new TitleAndDescriptionComposite(content);
		titleAndDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		titleAndDescription.setTitle(Messages.CRTGroup_Header);
		titleAndDescription.setDescription(Messages.CRTGroup_HeaderInfoText);

		step1Group = new Group(content, SWT.NONE);
		step1Group.setLayout(new GridLayout(2, false));
		step1Group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		step1Group.setText(MESSAGE_STEP_1_GROUP);

		step1Text = new Text(step1Group, SWT.MULTI);
		step1Text.setEditable(false);
		step1Text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		step1Text.setText(MESSAGE_STEP1);
		step1Text.setFont(FontService.getSmallBoldFont());

		step1nextButton = new Button(step1Group, SWT.NONE);
		step1nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true));
		step1nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				/*
				 * checks whether all fields are filled
				 */
				Vector<Equation> data = equations.getEquationSet();
				inverseEquationSet.clear();
				verifyEquationSet.clear();
				numberOfEquations = data.size();

				String[] tmpATextfield = new String[numberOfEquations];
				String[] tmpMTextfield = new String[numberOfEquations];

				/*
				 * the modul always have to be bigger than the parameter a
				 */
				BigInteger tmpA = BigInteger.ZERO, tmpM = BigInteger.ONE;
				try {
					for (int i = 0; i < numberOfEquations; i++) {
						tmpA = new BigInteger(data.get(i).getTextfieldA());
						tmpM = new BigInteger(data.get(i).getTextfieldM());
						tmpA = tmpA.mod(tmpM);
					}
				} catch (NumberFormatException e1) {
					MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
					mb.setText(Messages.CRTGroup_error_no_equations_title);
					mb.setMessage(Messages.CRTGroup_error_no_equations_text);
					mb.open();
					return;
				}

				/*
				 * in the array "marking" we store the id of the not paired coprime equations
				 */
				int[] marking = new int[numberOfEquations];
				for (int i = 0; i < marking.length; i++) {
					marking[i] = 0;
				}
				boolean notFilled = false;
				for (int i = 0; i < numberOfEquations; i++) {
					tmpATextfield[i] = data.get(i).getTextfieldA();
					tmpMTextfield[i] = data.get(i).getTextfieldM();
					if (data.get(i).getTextfieldA().length() == 0 || data.get(i).getTextfieldM().length() == 0)
						notFilled = true;

				}
				if (notFilled)
					return;

				/*
				 * it is paired compared, whether the "moduli" are paired coprime. if this is
				 * not the case, then the position of the equation is marked.
				 */
				for (int i = 0; i < tmpMTextfield.length; i++) {
					BigInteger a = new BigInteger(tmpMTextfield[i]);
					for (int j = i + 1; j < tmpMTextfield.length; j++) {
						XEuclid gcd = new XEuclid();
						BigInteger tmpValue = gcd.xeuclid(a, new BigInteger(tmpMTextfield[j]));
						if (tmpValue.compareTo(BigInteger.ONE) != 0) {
							marking[j] = -1;
						}

					}
				}

				/*
				 * if no equation is marked, then the VerifyDialog not displayed. Otherwise, the
				 * "CheckingEquationDialog" is displayed for corrections.
				 */
				for (int i = 0; i < marking.length; i++) {
					if (marking[i] == -1) {
						showDialog = true;
					}
				}
				int dialogStatus = 0;

				/*
				 * open the Dialog to correct the input
				 */
				if (showDialog) {
					CheckingEquationDialog ced = new CheckingEquationDialog(getShell(), equations.getEquationSet(),
							marking);
					ced.setHelpAvailable(false);
					dialogStatus = ced.open();
				}

				/*
				 * if you closes the "CheckingEquationDialog" by the cancel button, then the
				 * ChineseReaminder is not computed.
				 */
				if (dialogStatus == 0) {
					/*
					 * GUI functionality. Disable or enable widgets
					 */
					reset.setEnabled(true);
					step1Text.clearSelection();
					step1Text.setFont(FontService.getSmallFont());
					step2Text.setFont(FontService.getSmallBoldFont());
					execute = true;
					step1Group.setEnabled(false);
					step1nextButton.setEnabled(false);
					step2Group.setEnabled(true);
					step2Text.setEnabled(true);
					step2nextButton.setEnabled(true);
					crt = new ChineseRemainderTheorem();
					BigInteger[] a = new BigInteger[numberOfEquations];
					BigInteger[] moduli = new BigInteger[numberOfEquations];
					for (int i = 0; i < numberOfEquations; i++) {
						a[i] = new BigInteger(data.get(i).getTextfieldA());
						moduli[i] = new BigInteger(data.get(i).getTextfieldM());
					}
					/*
					 * create the equations
					 */
					for (Equation equation : equations.getEquationSet()) {
						equation.setEquationEnable(false);
					}

					/*
					 * store the result
					 */
					result = crt.crt(moduli, a);
				}
			}
		});
		step1nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		step1nextButton.setText(MESSAGE_NEXT_STEP);

		scrolledEquation = new Group(content, SWT.NONE);
		scrolledEquation.setLayout(new GridLayout());
		scrolledEquation.setText(MESSAGE_GROUP_EQUATION);
		GridData gd_scrolledEquation = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_scrolledEquation.heightHint = 100;
		scrolledEquation.setLayoutData(gd_scrolledEquation);

		ScrolledComposite scrolledComposite = new ScrolledComposite(scrolledEquation, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		equationGroup = new Composite(scrolledComposite, SWT.NONE);
		equationGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		equationGroup.setLayout(new GridLayout(7, false));

		reset = new Button(equationGroup, SWT.PUSH);
		reset.setEnabled(false);
		reset.setText(MESSAGE_CHANGE_VALUES);
		reset.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 7, 1));
		reset.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				reset();
			}
		});

		equations.createEquation(0, equationGroup, this);
		equations.createEquation(1, equationGroup, this);

		scrolledComposite.setContent(equationGroup);

		step2Group = new Group(content, SWT.NONE);
		step2Group.setEnabled(false);
		step2Group.setLayout(new GridLayout(2, false));
		step2Group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		step2Group.setText(MESSAGE_STEP_2_GROUP);

		step2Text = new Text(step2Group, SWT.MULTI);
		step2Text.setEnabled(false);
		step2Text.setEditable(false);
		step2Text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		step2Text.setText(MESSAGE_STEP2);

		step2nextButton = new Button(step2Group, SWT.NONE);
		step2nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true));
		step2nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				/*
				 * GUI functionality disable or enable widgets
				 */
				step2Text.clearSelection();
				step2Text.setFont(FontService.getSmallFont());
				step3Text.setFont(FontService.getSmallBoldFont());
				step2nextButton.setEnabled(false);
				step2Group.setEnabled(false);
				step3Group.setEnabled(true);
				step3Text.setEnabled(true);
				step3nextButton.setEnabled(true);

				/*
				 * create the inverse-group
				 */
				BigInteger[] bigM = crt.getBigM();

				for (int i = -1; i < bigM.length; i++) {
					if (i == -1) {
						Label mLabel = new Label(inverseGroup, SWT.NONE);
						mLabel.setText("m"); //$NON-NLS-1$
						mLabel.setVisible(false);

						Label equivalenzLabel = new Label(inverseGroup, SWT.NONE);
						equivalenzLabel.setText("="); //$NON-NLS-1$
						equivalenzLabel.setVisible(false);

						Text mText = new Text(inverseGroup, SWT.READ_ONLY | SWT.BORDER);
						mText.setText(crt.getModulus().toString());
						GridData gd_mText = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
						gd_mText.widthHint = 267;
						mText.setLayoutData(gd_mText);
						mText.setVisible(false);

						inverseEquationSet.add(mLabel);
						inverseEquationSet.add(equivalenzLabel);
						inverseEquationSet.add(mText);

					} else {

						Label mLabel = new Label(inverseGroup, SWT.NONE);
						mLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
						mLabel.setText("M" + convertToSubset(i)); //$NON-NLS-1$
						mLabel.setVisible(false);

						Label equivalenzLabelFront = new Label(inverseGroup, SWT.NONE);
						equivalenzLabelFront.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
						equivalenzLabelFront.setText("="); //$NON-NLS-1$
						equivalenzLabelFront.setVisible(false);

						Text mText = new Text(inverseGroup, SWT.READ_ONLY | SWT.BORDER);
						mText.setEditable(false);
						GridData gd_mText = new GridData(SWT.FILL, SWT.CENTER, false, false);
						gd_mText.widthHint = 180;
						mText.setLayoutData(gd_mText);
						mText.setText(crt.getBigM()[i].toString());
						mText.setVisible(false);

						Label yLabel = new Label(inverseGroup, SWT.NONE);
						yLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
						yLabel.setText("y" + convertToSubset(i)); //$NON-NLS-1$
						yLabel.setVisible(false);

						Label equivalenzLabelBack = new Label(inverseGroup, SWT.NONE);
						equivalenzLabelBack.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
						equivalenzLabelBack.setText("="); //$NON-NLS-1$
						equivalenzLabelBack.setVisible(false);

						Text yText = new Text(inverseGroup, SWT.READ_ONLY | SWT.BORDER);
						yText.setEditable(false);
						yText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
						yText.setText(crt.getInverse()[i].toString());
						yText.setVisible(false);

						inverseEquationSet.add(mLabel);
						inverseEquationSet.add(equivalenzLabelFront);
						inverseEquationSet.add(mText);
						inverseEquationSet.add(yLabel);
						inverseEquationSet.add(equivalenzLabelBack);
						inverseEquationSet.add(yText);

					}
				}
				/*
				 * expand the scolledComposite for better look
				 */
				inverseGroup.layout();
				scrolledComposite_1.setMinSize(inverseGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));

				/*
				 * make the widgets visible. the first three widgets always have to be visible
				 */
				for (int i = 0; i < inverseEquationSet.size(); i++) {
					/*
					 * make only the left side visible
					 */
					if (i == 0 || i == 1 || i == 2 || i % 6 == 3 || i % 6 == 4 || i % 6 == 5) {
						inverseEquationSet.get(i).setVisible(true);
					}
				}
			}
		});
		step2nextButton.setEnabled(false);
		step2nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		step2nextButton.setText(MESSAGE_NEXT_STEP);

		step3Group = new Group(content, SWT.NONE);
		step3Group.setEnabled(false);
		step3Group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		step3Group.setText(MESSAGE_STEP_3_GROUP);
		step3Group.setLayout(new GridLayout(2, false));

		step3Text = new Text(step3Group, SWT.MULTI);
		step3Text.setEnabled(false);
		step3Text.setEditable(false);
		step3Text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		step3Text.setText(MESSAGE_STEP3);

		step3nextButton = new Button(step3Group, SWT.NONE);
		step3nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true));
		step3nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				/*
				 * GUI functionality. Disable or enable widgets
				 */
				step3Text.clearSelection();
				step3Text.setFont(FontService.getSmallFont());
				step4Text.setFont(FontService.getSmallBoldFont());
				step3nextButton.setEnabled(false);
				step3Group.setEnabled(false);
				step4Group.setEnabled(true);
				step4Text.setEnabled(true);
				step4nextButton.setEnabled(true);
				nextButton.setVisible(true);

				/*
				 * make the widgets visible.
				 */
				for (int i = 3; i < inverseEquationSet.size(); i++) {
					/*
					 * make only the left side visible
					 */
					if (i % 6 == 0 || i % 6 == 1 || i % 6 == 2) {
						inverseEquationSet.get(i).setVisible(true);
					}
				}
			}
		});
		step3nextButton.setEnabled(false);
		step3nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		step3nextButton.setText(MESSAGE_NEXT_STEP);

		scrolledInverse = new Group(content, SWT.V_SCROLL);
		scrolledInverse.setLayout(new GridLayout(1, false));
		scrolledInverse.setText("Inverse"); //$NON-NLS-1$
		GridData gd_scrolledInverse = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_scrolledInverse.verticalIndent = 20;
		scrolledInverse.setLayoutData(gd_scrolledInverse);

		scrolledComposite_1 = new ScrolledComposite(scrolledInverse, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);

		inverseGroup = new Composite(scrolledComposite_1, SWT.NONE);
		inverseGroup.setLayout(new GridLayout(6, false));
		scrolledComposite_1.setContent(inverseGroup);

		step4Group = new Group(content, SWT.NONE);
		step4Group.setEnabled(false);
		step4Group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		step4Group.setText(MESSAGE_STEP_4_GROUP);
		step4Group.setLayout(new GridLayout(2, false));

		step4Text = new Text(step4Group, SWT.MULTI);
		step4Text.setEnabled(false);
		step4Text.setEditable(false);
		step4Text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		step4Text.setText(MESSAGE_STEP4);

		step4nextButton = new Button(step4Group, SWT.NONE);
		step4nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true));
		step4nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				/*
				 * GUI functionality. Disable or enable widgets
				 */
				step4Text.clearSelection();
				step4Text.setFont(FontService.getSmallFont());
				resultXText.setFont(FontService.getSmallBoldFont());
				resultText.setFont(FontService.getSmallBoldFont());
				resultValueText.setFont(FontService.getSmallBoldFont());
				resultMoreText.setFont(FontService.getSmallBoldFont());
				step4nextButton.setEnabled(false);
				step4Group.setEnabled(false);
				resultGroup.setEnabled(true);
				resultXText.setEnabled(true);
				resultText.setEnabled(true);
				resultValueText.setEnabled(true);
				resultMoreText.setEnabled(true);
				nextButton.setEnabled(true);
				previousButton.setEnabled(false);

				/*
				 * create the verify-group
				 */
				resultValueText.setText(result.toString());

				Vector<Equation> equationSet = equations.getEquationSet();
				for (Equation equation : equationSet) {
					Text resultText = new Text(verifyGroup, SWT.READ_ONLY | SWT.BORDER);
					GridData gd_resultText = new GridData(SWT.FILL, SWT.CENTER, true, false);
					gd_resultText.widthHint = 121;
					resultText.setLayoutData(gd_resultText);
					resultText.setText(result.toString());

					Label cLabel = new Label(verifyGroup, SWT.NONE);
					cLabel.setText(uCongruence);

					Text aText = new Text(verifyGroup, SWT.READ_ONLY | SWT.BORDER);
					GridData gd_aText = new GridData(SWT.FILL, SWT.CENTER, false, false);
					gd_aText.widthHint = 51;
					aText.setLayoutData(gd_aText);
					aText.setText(equation.getTextfieldA());

					Label modLabel = new Label(verifyGroup, SWT.NONE);
					modLabel.setText("mod"); //$NON-NLS-1$

					Text mText = new Text(verifyGroup, SWT.READ_ONLY | SWT.BORDER);
					GridData gd_mText = new GridData(SWT.FILL, SWT.CENTER, true, false);
					gd_mText.widthHint = 51;
					mText.setLayoutData(gd_mText);
					mText.setText(equation.getTextfieldM());

					verifyEquationSet.add(resultText);
					verifyEquationSet.add(cLabel);
					verifyEquationSet.add(aText);
					verifyEquationSet.add(modLabel);
					verifyEquationSet.add(mText);

				}

				scrolledComposite_2.setMinSize(verifyGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				verifyGroup.pack();

				/*
				 * enable the export menu
				 */
				view.enableMenu(true);

			}
		});
		step4nextButton.setEnabled(false);
		step4nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		step4nextButton.setText(MESSAGE_RESULT_GROUP);

		resultGroup = new Group(content, SWT.NONE);
		resultGroup.setEnabled(false);
		resultGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		resultGroup.setText(MESSAGE_RESULT_GROUP);
		resultGroup.setLayout(new GridLayout(4, false));

		resultText = new Text(resultGroup, SWT.NONE);
		resultText.setEnabled(false);
		resultText.setEditable(false);
		resultText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		resultText.setText(MESSAGE_RESULT);
		resultText.setFont(FontService.getSmallFont());

		resultXText = new Text(resultGroup, SWT.READ_ONLY);
		resultXText.setEditable(false);
		resultXText.setEnabled(false);
		resultXText.setText(Messages.CRTGroup_0);
		resultText.setFont(FontService.getSmallFont());
		resultXText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true));

		resultValueText = new Text(resultGroup, SWT.READ_ONLY | SWT.BORDER);
		resultValueText.setEditable(false);
		resultValueText.setEnabled(false);
		resultValueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1));

		resultMoreText = new Text(resultGroup, SWT.MULTI);
		resultMoreText.setEnabled(false);
		resultMoreText.setEditable(false);
		resultMoreText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));
		resultMoreText.setText(MESSAGE_MORE_SOLUTION);
		resultMoreText.setFont(FontService.getSmallFont());

		previousButton = new Button(resultGroup, SWT.NONE);
		previousButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				/*
				 * change the result value in the verify-group
				 */
				BigInteger tmpValue = new BigInteger(resultValueText.getText());
				tmpValue = tmpValue.subtract(crt.getModulus());

				if (tmpValue.subtract(crt.getModulus()).compareTo(BigInteger.ZERO) < 0) {
					previousButton.setEnabled(false);
				}

				resultValueText.setText(tmpValue.toString());

				for (int i = 0; i < verifyEquationSet.size(); i++) {
					if (i % 5 == 0) {
						Text tmpText = (Text) verifyEquationSet.get(i);
						tmpText.setText(tmpValue.toString());
					}
				}
			}
		});
		previousButton.setEnabled(false);
		previousButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		previousButton.setText(MESSAGE_PREVIOUS);

		nextButton = new Button(resultGroup, SWT.NONE);
		nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				/*
				 * change the result value in the verify-group
				 */
				BigInteger tmpValue = new BigInteger(resultValueText.getText());
				tmpValue = tmpValue.add(crt.getModulus());
				resultValueText.setText(tmpValue.toString());

				for (int i = 0; i < verifyEquationSet.size(); i++) {
					if (i % 5 == 0) {
						Text tmpText = (Text) verifyEquationSet.get(i);
						tmpText.setText(tmpValue.toString());
					}
				}
				previousButton.setEnabled(true);
			}
		});
		nextButton.setEnabled(false);
		nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		nextButton.setText(MESSAGE_NEXT);

		scrolledVerify = new Group(content, SWT.NONE);
		scrolledVerify.setText(MESSAGE_VERIFY_GROUP);
		scrolledVerify.setLayout(new GridLayout(1, false));
		GridData gd_scrolledVerify = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_scrolledVerify.heightHint = 120;
		gd_scrolledVerify.verticalIndent = 20;
		scrolledVerify.setLayoutData(gd_scrolledVerify);

		scrolledComposite_2 = new ScrolledComposite(scrolledVerify, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite_2.setExpandHorizontal(true);
		scrolledComposite_2.setExpandVertical(true);

		verifyGroup = new Composite(scrolledComposite_2, SWT.NONE);
		verifyGroup.setLayout(new GridLayout(5, false));
		scrolledComposite_2.setContent(verifyGroup);

		step2Text.setFont(FontService.getSmallFont());
		step3Text.setFont(FontService.getSmallFont());
		step4Text.setFont(FontService.getSmallFont());
		new Label(content, SWT.NONE);

		scrolledGroup.setContent(content);
		scrolledGroup.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		// This makes the ScrolledComposite scrolling, when the mouse 
		// is on a Text with one or more of the following tags: SWT.READ_ONLY,
		// SWT.V_SCROLL or SWT-H_SCROLL.
		SmoothScroller.scrollSmooth(scrolledGroup);

		setDefaultValues();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Convert a number to a subscript index
	 *
	 * @param id
	 *            is the number to be converted
	 * @return a string which contains only subscript
	 */
	private String convertToSubset(int id) {
		char[] data = String.valueOf(id).toCharArray();
		String result = ""; //$NON-NLS-1$

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
	 * get the crt algorithm
	 *
	 * @return the crt object which contains the algorithm
	 */
	public ChineseRemainderTheorem getCrt() {
		return crt;
	}

	/**
	 * reset the algorithm. Executed when pressed the plus or minus button
	 */
	public void reset() {
		view.enableMenu(false);
		showDialog = false;
		execute = false;

		equationGroup.setEnabled(true);
		for (Equation equation : equations.getEquationSet()) {
			equation.setEquationEnable(true);
		}

		step1Text.setFont(FontService.getSmallBoldFont());
		step2Text.setFont(FontService.getSmallFont());
		step3Text.setFont(FontService.getSmallFont());
		step4Text.setFont(FontService.getSmallFont());
		resultText.setFont(FontService.getSmallFont());
		resultValueText.setFont(FontService.getSmallFont());
		resultMoreText.setFont(FontService.getSmallFont());
		resultXText.setFont(FontService.getSmallFont());

		step1Group.setEnabled(true);
		step2Group.setEnabled(false);
		step3Group.setEnabled(false);
		step4Group.setEnabled(false);
		resultGroup.setEnabled(false);

		step1Text.setEnabled(true);
		step2Text.setEnabled(false);
		step3Text.setEnabled(false);
		step4Text.setEnabled(false);

		step1nextButton.setEnabled(true);
		step2nextButton.setEnabled(false);
		step3nextButton.setEnabled(false);
		step4nextButton.setEnabled(false);

		resultText.setEnabled(false);
		resultMoreText.setEnabled(false);
		resultValueText.setEnabled(false);
		resultXText.setEnabled(false);
		resultValueText.setText(""); //$NON-NLS-1$

		nextButton.setEnabled(false);
		previousButton.setEnabled(false);

		/*
		 * dispose the inverse and verify group for the next turn of calculating
		 */
		for (Control elem : inverseGroup.getChildren()) {
			elem.dispose();
		}
		inverseGroup.pack();

		for (Control elem : verifyGroup.getChildren()) {
			elem.dispose();
		}
		verifyGroup.pack();

	}

	private void setDefaultValues() {
		equations.getEquationSet().elementAt(0).setTextfieldA("2"); //$NON-NLS-1$
		equations.getEquationSet().elementAt(0).setTextfieldM("5"); //$NON-NLS-1$

		equations.getEquationSet().elementAt(1).setTextfieldA("3"); //$NON-NLS-1$
		equations.getEquationSet().elementAt(1).setTextfieldM("7"); //$NON-NLS-1$
	}
	
	/**
	 * If available, returns the result.</br>
	 * Used for creating a default filename.
	 * @return The result of the CRT.
	 */
	public BigInteger getResult() {
		return result;
	}
}
