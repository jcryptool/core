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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.babystepgiantstep.algorithm.BabystepGiantstep;

/**
 * 
 * @author Miray Inel
 * 
 */
public class BabystepGiantstepView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jcryptool.visual.babystepgiantstep.views.BabystepGiantstepView"; //$NON-NLS-1$
	private Combo comboGroup;
	private Combo comboGenerator;
	private Combo comboGroupElement;
	private StyledText textResult;
	private Table tableBS;
	private Table tableGS;
	private Text textOrder;
	private Text textM;
	private BabystepGiantstep babyStepGiantStep;
	private TableColumn tblclmnR;
	private TableColumn tblclmnBabySteps;
	private Button btnContinueToStep3;
	private Button btnContinueToStep2;
	private Button btnContinueToStep4;
	private Button btnReset;
	private Button btnResult;
	private TableColumn tblclmnCommentBS;
	private TableColumn tblclmnQ;
	private TableColumn tblclmnGiantSteps;
	private Composite compositeDescription;
	private Text styledText;

	private VerifyListener vl_numbers = new VerifyListener() {

		@Override
		public void verifyText(VerifyEvent e) {
			Combo comboField = null;
			e.doit = true;

			if (e.getSource() instanceof Combo) {
				comboField = (Combo) e.getSource();
			}

			if (comboField == null
					|| ((comboField.getText().length() == 0 && e.text.compareTo("0") == 0) || (comboField //$NON-NLS-1$
							.getSelection().x == 0 && e.keyCode == 48))) {
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

	private Label lblCalculateTheGrouporder;
	private Label lblCalculateTheCeiling;
	private TableColumn tblclmnCommentGS;
	private Label lblCalculateTheInverse;
	private Text textInv;
	private ScrolledComposite scrolledComposite;
	private Composite compositeStep1;
	private Composite compositeStep2;
	private Composite compositeStep3;
	private Composite compositeStep4;
	private Composite compositeStep1Btn;
	private Composite compositeStep2Btn;
	private Composite compositeStep3Btn;
	private Composite compositeStep4Btn;

	private Composite parent;
	
	private GridData gd_compositeStep1Btn;
	private GridData gd_compositeStep2Btn;
	private GridData gd_compositeStep3Btn;
	private GridData gd_compositeStep4Btn;

	/**
	 * The constructor.
	 */
	public BabystepGiantstepView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		parent.setLayout(new GridLayout(1, false));

		scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Group grpBabyStepGiant = new Group(scrolledComposite, SWT.NONE);
		grpBabyStepGiant.setFont(FontService.getNormalBoldFont());
		grpBabyStepGiant.setText(Messages.BabystepGiantstepView_0);
		grpBabyStepGiant.setLayout(new GridLayout(1, false));

		compositeDescription = new Composite(grpBabyStepGiant, SWT.NONE);
		compositeDescription.setBackground(Constants.LIGHTGREY);
		compositeDescription.setLayout(new GridLayout(1, false));
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		styledText = new Text(compositeDescription, SWT.WRAP | SWT.MULTI);
		styledText.setFont(FontService.getNormalFont());
		styledText.setText(Messages.BabystepGiantstepView_2);
		styledText.setBackground(Constants.LIGHTGREY);
		styledText.setEditable(false);
		GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_styledText.widthHint = 400;
		styledText.setLayoutData(gd_styledText);

		Group grpEnterTheParameters = new Group(grpBabyStepGiant, SWT.NONE);
		grpEnterTheParameters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpEnterTheParameters.setText(Messages.BabystepGiantstepView_3);
		grpEnterTheParameters.setLayout(new GridLayout(2, false));

		compositeStep1 = new Composite(grpEnterTheParameters, SWT.NONE);
		compositeStep1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeStep1.setLayout(new GridLayout(3, true));

		Label lblEnterACyclicGroup = new Label(compositeStep1, SWT.NONE);
		lblEnterACyclicGroup.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblEnterACyclicGroup.setText(Messages.BabystepGiantstepView_4);

		Label lblEnterAGenerator = new Label(compositeStep1, SWT.NONE);
		lblEnterAGenerator.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblEnterAGenerator.setText(Messages.BabystepGiantstepView_5);

		Label lblEnterEGroupelement = new Label(compositeStep1, SWT.NONE);
		lblEnterEGroupelement.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblEnterEGroupelement.setText(Messages.BabystepGiantstepView_6);

		comboGroup = new Combo(compositeStep1, SWT.BORDER);
		comboGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboGroup.add("1823"); //$NON-NLS-1$
		comboGroup.add("2017"); //$NON-NLS-1$
		comboGroup.select(comboGroup.getItemCount() - 1);

		comboGenerator = new Combo(compositeStep1, SWT.BORDER);
		comboGenerator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboGenerator.add("3"); //$NON-NLS-1$
		comboGenerator.add("5"); //$NON-NLS-1$
		comboGenerator.select(comboGenerator.getItemCount() - 1);

		comboGroupElement = new Combo(compositeStep1, SWT.BORDER);
		comboGroupElement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboGroupElement.add("454"); //$NON-NLS-1$
		comboGroupElement.add("525"); //$NON-NLS-1$
		comboGroupElement.select(comboGroupElement.getItemCount() - 1);
		comboGroupElement.addVerifyListener(vl_numbers);
		comboGenerator.addVerifyListener(vl_numbers);
		comboGroup.addVerifyListener(vl_numbers);

		compositeStep1Btn = new Composite(grpEnterTheParameters, SWT.NONE);
		gd_compositeStep1Btn = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		compositeStep1Btn.setLayoutData(gd_compositeStep1Btn);
		compositeStep1Btn.setLayout(new GridLayout(1, false));

		btnContinueToStep2 = new Button(compositeStep1Btn, SWT.NONE);
		btnContinueToStep2.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
		btnContinueToStep2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!comboGroup.getText().isEmpty() && !comboGenerator.getText().isEmpty()
						&& !comboGroupElement.getText().isEmpty()) {
					BigInteger textGroupValue = new BigInteger(comboGroup.getText());
					BigInteger textGeneratorValue = new BigInteger(comboGenerator.getText());
					BigInteger textGroupElementValue = new BigInteger(comboGroupElement.getText());

					/*
					 * if values are not bigger than an integer and the group
					 * element is not a multiple of the cyclic group then
					 * compute the discrete logarithm
					 */
					if (textGroupValue.compareTo(Constants.LIMIT) <= 0
							&& textGeneratorValue.compareTo(Constants.LIMIT) <= 0
							&& textGroupElementValue.compareTo(Constants.LIMIT) <= 0
							&& textGroupValue.compareTo(BigInteger.ONE) != 0
							&& textGeneratorValue.compareTo(BigInteger.ONE) != 0
							&& textGroupElementValue.compareTo(BigInteger.ONE) != 0
							&& textGroupElementValue.mod(textGroupValue).compareTo(BigInteger.ZERO) != 0
							&& textGroupValue.mod(textGroupElementValue).compareTo(BigInteger.ZERO) != 0
							&& textGroupValue.isProbablePrime(10000)) {

						compute();

					} else {
						/*
						 * the entered values are bigger than an integer or the
						 * group element is a multiple of the cyclic group
						 */
						Parameter parameter = new Parameter(e.display.getActiveShell(), textGroupValue.toString(),
								textGeneratorValue.toString(), textGroupElementValue.toString());
						parameter.setHelpAvailable(false);

						int rc = parameter.open();

						if (rc == 0) {
							comboGroup.setText(parameter.getCyclicGroupValue());
							comboGenerator.setText(parameter.getGeneratorValue());
							comboGroupElement.setText(parameter.getGroupElementValue());

							compute();
						}
					}
				}
			}

			public void compute() {
				BigInteger group = new BigInteger(comboGroup.getText());
				BigInteger generator = new BigInteger(comboGenerator.getText());
				BigInteger groupElement = new BigInteger(comboGroupElement.getText());

				try {
					babyStepGiantStep = new BabystepGiantstep(group, generator, groupElement);
				} catch (ArithmeticException e) {
					textResult.setText(Messages.BabystepGiantstepView_95
							+ comboGenerator.getText()
							+ " " + Messages.BabystepGiantstepView_96 + comboGroup.getText() + Messages.BabystepGiantstepView_97); //$NON-NLS-1$
					LogUtil.logError(e.toString());
					StyleRange parameterA = new StyleRange();
					parameterA.start = 0;
					parameterA.length = textResult.getText().length();
					parameterA.foreground = Constants.RED;
					parameterA.fontStyle = SWT.BOLD;
					textResult.setStyleRange(parameterA);
					btnContinueToStep2.setEnabled(false);
					return;
				}
				textOrder.setText(babyStepGiantStep.getN().toString());
				textM.setText(babyStepGiantStep.getM().toString());
				textInv.setText(babyStepGiantStep.getMultInv().toString());

				comboGroup.setEnabled(false);
				comboGenerator.setEnabled(false);
				comboGroupElement.setEnabled(false);
				btnContinueToStep2.setEnabled(false);
				btnContinueToStep3.setEnabled(true);

				styledText.setText(Messages.BabystepGiantstepView_25);
			}

		});
		btnContinueToStep2.setText(Messages.BabystepGiantstepView_26);

		Group grpCalculateTheGroupoder = new Group(grpBabyStepGiant, SWT.NONE);
		grpCalculateTheGroupoder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpCalculateTheGroupoder.setText(Messages.BabystepGiantstepView_27);
		grpCalculateTheGroupoder.setLayout(new GridLayout(2, false));

		compositeStep2 = new Composite(grpCalculateTheGroupoder, SWT.NONE);
		compositeStep2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		compositeStep2.setLayout(new GridLayout(3, true));

		lblCalculateTheGrouporder = new Label(compositeStep2, SWT.NONE);
		lblCalculateTheGrouporder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblCalculateTheGrouporder.setText(Messages.BabystepGiantstepView_28);

		lblCalculateTheCeiling = new Label(compositeStep2, SWT.NONE);
		lblCalculateTheCeiling.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblCalculateTheCeiling.setText(Messages.BabystepGiantstepView_29);

		lblCalculateTheInverse = new Label(compositeStep2, SWT.NONE);
		lblCalculateTheInverse.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblCalculateTheInverse.setText(Messages.BabystepGiantstepView_30);

		textOrder = new Text(compositeStep2, SWT.BORDER | SWT.READ_ONLY);
		textOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		textM = new Text(compositeStep2, SWT.BORDER | SWT.READ_ONLY);
		textM.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		textInv = new Text(compositeStep2, SWT.BORDER | SWT.READ_ONLY);
		textInv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		compositeStep2Btn = new Composite(grpCalculateTheGroupoder, SWT.NONE);
		gd_compositeStep2Btn = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		compositeStep2Btn.setLayoutData(gd_compositeStep2Btn);
		compositeStep2Btn.setLayout(new GridLayout(1, false));

		btnContinueToStep3 = new Button(compositeStep2Btn, SWT.NONE);
		btnContinueToStep3.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
		btnContinueToStep3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				babyStepGiantStep.computeBabySteps();

				HashMap<BigInteger, BigInteger> babyStepMenge = babyStepGiantStep.getBabystepMenge();

				Set<Entry<BigInteger, BigInteger>> set = babyStepMenge.entrySet();

				BigInteger i = BigInteger.ZERO;
				BigInteger limit = new BigInteger(textM.getText());

				if (babyStepGiantStep.getZyklischeGruppe().compareTo(BigInteger.valueOf((Integer.MAX_VALUE))) <= 0) {
					while (i.compareTo(limit) < 0) {
						for (Entry<BigInteger, BigInteger> entry : set) {
							if (entry.getValue().equals(new BigInteger(String.valueOf(i)))) {
								TableItem tableItem = new TableItem(tableBS, SWT.NONE);
								tableItem.setText(0, entry.getValue().toString());
								tableItem.setText(1, entry.getKey().toString());

								if (i.equals(BigInteger.ZERO)) {
									StringBuilder sb = new StringBuilder("r = " + i + " => "); //$NON-NLS-1$ //$NON-NLS-2$
									BigInteger tmp = new BigInteger(comboGroupElement.getText());
									sb.append(tmp + " "); //$NON-NLS-1$
									sb.append(Constants.uCongruence
											+ " " + entry.getKey().toString() + " mod " + comboGroup.getText()); //$NON-NLS-1$ //$NON-NLS-2$

									tableItem.setText(2, sb.toString());
								} else {
									StringBuilder sb = new StringBuilder("r = " + i + " => "); //$NON-NLS-1$ //$NON-NLS-2$
									sb.append(babyStepGiantStep.getMultInv()
											+ " * " + tableBS.getItem(i.intValue() - 1).getText(1) + " = "); //$NON-NLS-1$ //$NON-NLS-2$
									BigInteger tmp = babyStepGiantStep.getMultInv().multiply(
											new BigInteger(tableBS.getItem(i.intValue() - 1).getText(1)));
									sb.append(tmp + " "); //$NON-NLS-1$
									sb.append(Constants.uCongruence
											+ " " + entry.getKey().toString() + " mod " + comboGroup.getText()); //$NON-NLS-1$ //$NON-NLS-2$

									tableItem.setText(2, sb.toString());
								}
								break;
							}
						}
						i = i.add(BigInteger.ONE);
					}

					tableBS.setSelection(tableBS.getItemCount() - 1);
				} else {
					babyStepGiantStep.computeGiantSteps();
					MessageDialog.openInformation(null, "Information", //$NON-NLS-1$
							Messages.BabystepGiantstepView_7);

					tableBS.setEnabled(false);
					tableBS.setRedraw(false);
					while (tableBS.getColumnCount() > 0) {
						tableBS.getColumns()[0].dispose();
					}
					tableBS.setRedraw(true);
					
					tableGS.setEnabled(false);
					tableGS.setRedraw(false);
					while (tableGS.getColumnCount() > 0) {
						tableGS.getColumns()[0].dispose();
					}
					tableGS.setRedraw(true);
					
					
					btnContinueToStep3.setEnabled(false);
					btnContinueToStep4.setEnabled(false);
					btnResult.setEnabled(true);
					btnResult.setFocus();					
					return;
				}

				btnContinueToStep3.setEnabled(false);
				if (babyStepGiantStep.getX() == null) {
					btnContinueToStep4.setEnabled(true);
					styledText.setText(Messages.BabystepGiantstepView_41);
				} else {
					btnContinueToStep4.setEnabled(false);
					btnResult.setEnabled(true);
					styledText.setText(Messages.BabystepGiantstepView_42);
				}
			}
		});
		btnContinueToStep3.setEnabled(false);
		btnContinueToStep3.setText(Messages.BabystepGiantstepView_43);

		Group grpCalculateTheBaby = new Group(grpBabyStepGiant, SWT.NONE);
		GridData gd_grpCalculateTheBaby = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpCalculateTheBaby.minimumHeight = 150;
		grpCalculateTheBaby.setLayoutData(gd_grpCalculateTheBaby);
		grpCalculateTheBaby.setText(Messages.BabystepGiantstepView_44);
		grpCalculateTheBaby.setLayout(new GridLayout(2, false));

		compositeStep3 = new Composite(grpCalculateTheBaby, SWT.NONE);
		compositeStep3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeStep3.setLayout(new GridLayout(1, false));

		tableBS = new Table(compositeStep3, SWT.BORDER | SWT.FULL_SELECTION);
		tableBS.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableBS.setHeaderVisible(true);
		tableBS.setLinesVisible(true);

		tblclmnR = new TableColumn(tableBS, SWT.NONE);
		tblclmnR.setWidth(120);
		tblclmnR.setText("Rest (r)"); //$NON-NLS-1$

		tblclmnBabySteps = new TableColumn(tableBS, SWT.NONE);
		tblclmnBabySteps.setWidth(120);
		tblclmnBabySteps.setText("Babysteps"); //$NON-NLS-1$

		tblclmnCommentBS = new TableColumn(tableBS, SWT.NONE);
		tblclmnCommentBS.setWidth(525);
		tblclmnCommentBS.setText(Messages.BabystepGiantstepView_47);

		compositeStep3Btn = new Composite(grpCalculateTheBaby, SWT.NONE);
		gd_compositeStep3Btn = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		compositeStep3Btn.setLayoutData(gd_compositeStep3Btn);
		compositeStep3Btn.setLayout(new GridLayout(1, false));

		btnContinueToStep4 = new Button(compositeStep3Btn, SWT.NONE);
		btnContinueToStep4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		btnContinueToStep4.setEnabled(false);
		btnContinueToStep4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				babyStepGiantStep.computeGiantSteps();
				ArrayList<BigInteger> giantStepMenge = babyStepGiantStep.getGiantstepMenge();
				if (babyStepGiantStep.getZyklischeGruppe().compareTo(BigInteger.valueOf((Integer.MAX_VALUE))) <= 0) {
					BigInteger q = BigInteger.ONE;
					for (BigInteger elem : giantStepMenge) {
						TableItem tableItem = new TableItem(tableGS, SWT.NONE);
						tableItem.setText(0, q.toString());
						tableItem.setText(1, elem.toString());

						if (tableGS.getItemCount() == 1) {
							StringBuilder sb = new StringBuilder("q = " + q + " => "); //$NON-NLS-1$ //$NON-NLS-2$
							sb.append(textM.getText() + "^" + q + " " + Constants.uCongruence + " "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							sb.append(elem.toString() + " mod " + comboGroup.getText()); //$NON-NLS-1$
							tableItem.setText(2, sb.toString());
						} else {
							StringBuilder sb = new StringBuilder("q = " + q + " => "); //$NON-NLS-1$ //$NON-NLS-2$
							sb.append(textM.getText() + "^" + q + " = "); //$NON-NLS-1$ //$NON-NLS-2$
							sb.append(textM.getText()
									+ " * " + tableGS.getItem(tableGS.getItemCount() - 2).getText(1) + " = "); //$NON-NLS-1$ //$NON-NLS-2$

							BigInteger tmp = new BigInteger(textM.getText());
							tmp = tmp.multiply(new BigInteger(tableGS.getItem(tableGS.getItemCount() - 2).getText(1)));
							sb.append(tmp
									+ " " + Constants.uCongruence + " " + elem.toString() + " mod " + comboGroup.getText()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

							tableItem.setText(2, sb.toString());
						}
						q = q.add(BigInteger.ONE);
					}
					tableGS.setSelection(tableGS.getItemCount() - 1);
				} else {
					MessageDialog.openInformation(null, "Information", //$NON-NLS-1$
							Messages.BabystepGiantstepView_7);

					tableGS.setEnabled(false);
					tableGS.setRedraw(false);
					while (tableGS.getColumnCount() > 0) {
						tableGS.getColumns()[0].dispose();
					}
					tableGS.setRedraw(true);
				}

				btnContinueToStep4.setEnabled(false);
				btnResult.setEnabled(true);

				styledText.setText(Messages.BabystepGiantstepView_64);

			}
		});
		btnContinueToStep4.setText(Messages.BabystepGiantstepView_65);

		Group grpCalculateTheGiant = new Group(grpBabyStepGiant, SWT.NONE);
		GridData gd_grpCalculateTheGiant = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpCalculateTheGiant.minimumHeight = 150;
		grpCalculateTheGiant.setLayoutData(gd_grpCalculateTheGiant);
		grpCalculateTheGiant.setText(Messages.BabystepGiantstepView_66);
		grpCalculateTheGiant.setLayout(new GridLayout(2, false));

		compositeStep4 = new Composite(grpCalculateTheGiant, SWT.NONE);
		compositeStep4.setLayout(new GridLayout(1, false));
		compositeStep4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

		tableGS = new Table(compositeStep4, SWT.BORDER | SWT.FULL_SELECTION);
		tableGS.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableGS.setHeaderVisible(true);
		tableGS.setLinesVisible(true);

		tblclmnQ = new TableColumn(tableGS, SWT.NONE);
		tblclmnQ.setWidth(120);
		tblclmnQ.setText("Quotient (q)"); //$NON-NLS-1$

		tblclmnGiantSteps = new TableColumn(tableGS, SWT.NONE);
		tblclmnGiantSteps.setWidth(120);
		tblclmnGiantSteps.setText("Giantsteps"); //$NON-NLS-1$

		tblclmnCommentGS = new TableColumn(tableGS, SWT.NONE);
		tblclmnCommentGS.setWidth(525);
		tblclmnCommentGS.setText(Messages.BabystepGiantstepView_69);

		compositeStep4Btn = new Composite(grpCalculateTheGiant, SWT.NONE);
		gd_compositeStep4Btn = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2);
		compositeStep4Btn.setLayoutData(gd_compositeStep4Btn);
		compositeStep4Btn.setLayout(new GridLayout(1, false));

		btnResult = new Button(compositeStep4Btn, SWT.NONE);
		btnResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		btnResult.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (babyStepGiantStep.getX() != null) {
					if (babyStepGiantStep.getZyklischeGruppe().compareTo(BigInteger.valueOf((Integer.MAX_VALUE))) > 0) {
						
						StringBuilder result = new StringBuilder(
								"r = x = " + babyStepGiantStep.getX() + " mod " + comboGroup.getText()); //$NON-NLS-1$ //$NON-NLS-2$ 
						textResult.setText(Messages.BabystepGiantstepView_75
								+ result.toString()
								+ ". " + Messages.BabystepGiantstepView_76 + comboGroupElement.getText() + " = " + comboGenerator.getText() + " ^ " + babyStepGiantStep.getX() + " mod " + comboGroup.getText() + "."); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

						StyleRange parameterA = new StyleRange();
						parameterA.start = Messages.BabystepGiantstepView_75.length() + 8;
						parameterA.length = babyStepGiantStep.getX().toString().length();
						parameterA.foreground = Constants.GREEN;
						parameterA.fontStyle = SWT.BOLD;
						textResult.setStyleRange(parameterA);
					} else {
						if (babyStepGiantStep.getR() != null) {
							tableBS.setSelection(babyStepGiantStep.getR().intValue());
							tableBS.getItem(babyStepGiantStep.getR().intValue()).setForeground(0, Constants.MAGENTA);
							tableBS.getItem(babyStepGiantStep.getR().intValue()).setForeground(1, Constants.MAGENTA);
							tableBS.getItem(babyStepGiantStep.getR().intValue()).setForeground(2, Constants.MAGENTA);
							tableGS.setSelection(babyStepGiantStep.getQ().intValue() - 1);
							tableGS.getItem(babyStepGiantStep.getQ().intValue() - 1).setForeground(0, Constants.BLUE);
							tableGS.getItem(babyStepGiantStep.getQ().intValue() - 1).setForeground(1, Constants.BLUE);
							tableGS.getItem(babyStepGiantStep.getQ().intValue() - 1).setForeground(2, Constants.BLUE);
							StringBuilder result = new StringBuilder(
									"x = q * m + r = " + babyStepGiantStep.getQ() + " * " + textM.getText() + " + " + babyStepGiantStep.getR() //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
											+ " = " + babyStepGiantStep.getX().toString() + " mod " + comboGroup.getText() + ". "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							textResult.setText(Messages.BabystepGiantstepView_75
									+ result.toString()
									+ Messages.BabystepGiantstepView_76
									+ comboGroupElement.getText()
									+ " = " + comboGenerator.getText() + " ^ " + babyStepGiantStep.getX().intValue() + " mod " + comboGroup.getText() + "."); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
							StyleRange parameterA = new StyleRange();
							parameterA.start = Messages.BabystepGiantstepView_75.length() + 16;
							parameterA.length = babyStepGiantStep.getQ().toString().length();
							parameterA.foreground = Constants.BLUE;
							parameterA.fontStyle = SWT.BOLD;
							textResult.setStyleRange(parameterA);
							StyleRange parameterB = new StyleRange();
							parameterB.start = Messages.BabystepGiantstepView_75.length() + 16
									+ babyStepGiantStep.getQ().toString().length() + 3 + textM.getText().length() + 3;
							parameterB.length = babyStepGiantStep.getR().toString().length();
							parameterB.foreground = Constants.MAGENTA;
							parameterB.fontStyle = SWT.BOLD;
							textResult.setStyleRange(parameterB);
							StyleRange parameterC = new StyleRange();
							parameterC.start = Messages.BabystepGiantstepView_75.length() + 16
									+ babyStepGiantStep.getQ().toString().length() + 3 + textM.getText().length() + 3
									+ babyStepGiantStep.getR().toString().length() + 3;
							parameterC.length = babyStepGiantStep.getX().toString().length();
							parameterC.foreground = Constants.GREEN;
							parameterC.fontStyle = SWT.BOLD;
							textResult.setStyleRange(parameterC);
						} else {
							if (tableBS.getItemCount() == 1) {
								tableBS.setSelection(babyStepGiantStep.getX().intValue());
								tableBS.getItem(babyStepGiantStep.getX().intValue() - 1).setForeground(0,
										Constants.GREEN);
								tableBS.getItem(babyStepGiantStep.getX().intValue() - 1).setForeground(1,
										Constants.GREEN);
								tableBS.getItem(babyStepGiantStep.getX().intValue() - 1).setForeground(2,
										Constants.GREEN);
							} else {
								tableBS.setSelection(babyStepGiantStep.getX().intValue());
								tableBS.getItem(babyStepGiantStep.getX().intValue()).setForeground(0, Constants.GREEN);
								tableBS.getItem(babyStepGiantStep.getX().intValue()).setForeground(1, Constants.GREEN);
								tableBS.getItem(babyStepGiantStep.getX().intValue()).setForeground(2, Constants.GREEN);
							}

							StringBuilder result = new StringBuilder(
									"r = x = " + babyStepGiantStep.getX() + " mod " + comboGroup.getText()); //$NON-NLS-1$ //$NON-NLS-2$ 
							textResult.setText(Messages.BabystepGiantstepView_75
									+ result.toString()
									+ ". " + Messages.BabystepGiantstepView_76 + comboGroupElement.getText() + " = " + comboGenerator.getText() + " ^ " + babyStepGiantStep.getX() + " mod " + comboGroup.getText() + "."); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

							StyleRange parameterA = new StyleRange();
							parameterA.start = Messages.BabystepGiantstepView_75.length() + 8;
							parameterA.length = babyStepGiantStep.getX().toString().length();
							parameterA.foreground = Constants.GREEN;
							parameterA.fontStyle = SWT.BOLD;
							textResult.setStyleRange(parameterA);
						}
					}
				} else {
					textResult.setText(Messages.BabystepGiantstepView_88);
					StyleRange parameterA = new StyleRange();
					parameterA.start = 0;
					parameterA.length = textResult.getText().length();
					parameterA.foreground = Constants.RED;
					parameterA.fontStyle = SWT.BOLD;
					textResult.setStyleRange(parameterA);
				}
				btnResult.setEnabled(false);
			}
		});
		btnResult.setEnabled(false);
		btnResult.setText(Messages.BabystepGiantstepView_90);

		btnReset = new Button(compositeStep4Btn, SWT.NONE);
		btnReset.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textResult.setText(""); //$NON-NLS-1$
				textResult.setBackground(Constants.LIGHTGREY);

//				comboGenerator.setText(""); //$NON-NLS-1$
//				comboGroup.setText(""); //$NON-NLS-1$
//				comboGroupElement.setText(""); //$NON-NLS-1$
				textM.setText(""); //$NON-NLS-1$
				textOrder.setText(""); //$NON-NLS-1$
				textInv.setText(""); //$NON-NLS-1$

				tableBS.removeAll();
				tableGS.removeAll();

				btnContinueToStep2.setEnabled(true);
				btnContinueToStep3.setEnabled(false);
				btnContinueToStep4.setEnabled(false);
				btnResult.setEnabled(false);

				comboGenerator.setEnabled(true);
//				comboGenerator.select(comboGenerator.getItemCount() - 1);
				comboGroup.setEnabled(true);
//				comboGroup.select(comboGroup.getItemCount() - 1);
				comboGroupElement.setEnabled(true);
//				comboGroupElement.select(comboGroupElement.getItemCount() - 1);

				styledText.setText(Messages.BabystepGiantstepView_2);

				if (tableBS.getColumnCount() == 0) {
					tblclmnR = new TableColumn(tableBS, SWT.NONE);
					tblclmnR.setWidth(120);
					tblclmnR.setText("Rest (r)"); //$NON-NLS-1$
					tblclmnBabySteps = new TableColumn(tableBS, SWT.NONE);
					tblclmnBabySteps.setWidth(120);
					tblclmnBabySteps.setText("Babysteps"); //$NON-NLS-1$
					tblclmnCommentBS = new TableColumn(tableBS, SWT.NONE);
					tblclmnCommentBS.setWidth(525);
					tblclmnCommentBS.setText(Messages.BabystepGiantstepView_47);
					tableBS.setEnabled(true);
				}

				if (tableGS.getColumnCount() == 0) {
					tblclmnQ = new TableColumn(tableGS, SWT.NONE);
					tblclmnQ.setWidth(120);
					tblclmnQ.setText("Quotient (q)"); //$NON-NLS-1$
					tblclmnGiantSteps = new TableColumn(tableGS, SWT.NONE);
					tblclmnGiantSteps.setWidth(120);
					tblclmnGiantSteps.setText("Giantsteps"); //$NON-NLS-1$
					tblclmnCommentGS = new TableColumn(tableGS, SWT.NONE);
					tblclmnCommentGS.setWidth(525);
					tblclmnCommentGS.setText(Messages.BabystepGiantstepView_69);
					tableGS.setEnabled(true);
				}

				comboGroup.setFocus();

			}
		});
		btnReset.setText(Messages.BabystepGiantstepView_93);

		Group grpDescription = new Group(grpBabyStepGiant, SWT.NONE);
		grpDescription.setLayout(new GridLayout(1, false));
		grpDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpDescription.setText(Messages.BabystepGiantstepView_94);

		textResult = new StyledText(grpDescription, SWT.READ_ONLY | SWT.WRAP);
		textResult.setFont(FontService.getNormalFont());
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_textDescription.widthHint = 400;
		textResult.setBackground(Constants.LIGHTGREY);
		textResult.setLayoutData(gd_textDescription);
		
		textResult.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				//Autoadjust the size of the textfield.
				parent.layout(new Control[] {textResult});
			}
		});
		
		scrolledComposite.setMinSize(grpBabyStepGiant.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setContent(grpBabyStepGiant);
		
		this.parent.addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent e) {
				resizeButtons();
			}
			
			@Override
			public void controlMoved(ControlEvent e) {
				// Just do nothing
				
			}
		});

	}
	
	private void resizeButtons() {
		//At first, the composite should use their preferred size (the minimum size).
		compositeStep1Btn.pack();
		compositeStep2Btn.pack();
		compositeStep3Btn.pack();
		compositeStep4Btn.pack();
		
		//Then figure out, which composite is the biggest.
		int width = 0;
		if (compositeStep1Btn.getBounds().width > width) {
			width = compositeStep1Btn.getBounds().width;
		}
		if (compositeStep2Btn.getBounds().width > width) {
			width = compositeStep2Btn.getBounds().width;
		}
		if (compositeStep3Btn.getBounds().width > width) {
			width = compositeStep3Btn.getBounds().width;
		}
		if (compositeStep4Btn.getBounds().width > width) {
			width = compositeStep4Btn.getBounds().width;
		}
		
		//Set all composites to the size of the biggest.
		gd_compositeStep1Btn.widthHint = width;
		gd_compositeStep2Btn.widthHint = width;
		gd_compositeStep3Btn.widthHint = width;
		gd_compositeStep4Btn.widthHint = width;
	}

	@Override
	public void setFocus() {
		comboGroup.setFocus();
	}

	public void resetView() {
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
		
		resizeButtons();
		parent.layout();
		
	}
}