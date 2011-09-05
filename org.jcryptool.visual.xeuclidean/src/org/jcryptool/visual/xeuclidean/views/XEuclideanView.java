// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.xeuclidean.views;

import java.math.BigInteger;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.xeuclidean.XEuclideanPlugin;
import org.jcryptool.visual.xeuclidean.algorithm.XEuclid;
import org.jcryptool.visual.xeuclidean.export.FileExporter;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class XEuclideanView extends ViewPart {
	private Action exportToLatexAction;
	private Action exportToCSVAction;
	private Action exportToPdfAction;

	private Composite parent;

	private static final String MESSAGE_X_VISUAL = "X: "; //$NON-NLS-1$
	private static final String MESSAGE_Y_VISUAL = "Y: "; //$NON-NLS-1$

	private static final Color BLACK_COLOR = Display.getCurrent()
			.getSystemColor(SWT.COLOR_BLACK);
	private static final Color RED_COLOR = Display.getCurrent().getSystemColor(
			SWT.COLOR_RED);
	private static final Color GREEN_COLOR = Display.getCurrent()
			.getSystemColor(SWT.COLOR_GREEN);
	private static final Color BLUE_COLOR = Display.getCurrent()
			.getSystemColor(SWT.COLOR_BLUE);
	private static final Color VIOLET_COLOR = Display.getCurrent()
			.getSystemColor(SWT.COLOR_MAGENTA);

	private Text resultText;
	private StyledText visualizeStyledText;

	private Label resultLabel;

	private Table table;
	private Group info;
	private Text pText;
	private Text qText;
	private Group action;
	private Composite main;

	private VerifyListener pTextOnlyNumbers;
	private VerifyListener qTextOnlyNumbers;

	private int stepwiseCounter;

	private TableItem tableItem0;
	private TableItem tableItem1;
	private TableItem tableItemTmp;

	private String pValue;
	private String qValue;

	private enum visualMode {
		INIT, QUOTIENT, REMAINDER, X_EVAL, Y_EVAL, RESULT
	}

	private visualMode nextState;
	private visualMode previousState;

	private BigInteger value;
	private XEuclid xeuclid;
	private Vector<BigInteger> xTmp = null;
	private Vector<BigInteger> yTmp = null;
	private Vector<BigInteger> rTmp = null;
	private Vector<BigInteger> qTmp = null;

	@Override
	public void dispose() {
		// RESULT_FONT.dispose();

		super.dispose();
	}

	/**
	 * Create contents of the view part
	 *
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		xeuclid = new XEuclid();

		nextState = visualMode.INIT;
		previousState = null;

		stepwiseCounter = 0;

		pValue = ""; //$NON-NLS-1$
		qValue = ""; //$NON-NLS-1$

		value = BigInteger.ZERO;

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.heightHint = 185;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = true;

		// Create scrollable composite and composite within it
		ScrolledComposite scroll = new ScrolledComposite(parent, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);
		scroll.setLayoutData(gridData);

		// gridlayout for elements
		Composite pageComposite = new Composite(scroll, SWT.NONE);
		scroll.setContent(pageComposite);
		pageComposite.setLayout(gridLayout);
		pageComposite.setLayoutData(gridData);

		main = pageComposite;

		GridLayout gridLayoutAction = new GridLayout();
		gridLayoutAction.numColumns = 6;
		gridLayoutAction.makeColumnsEqualWidth = false;
		action = new Group(main, SWT.None);
		action.setText(Messages.XEuclideanView_ExtendedEuclidean);
		action.setLayout(gridLayoutAction);
		action.setLayoutData(gridData);

		final Label pLabel = new Label(action, SWT.NONE);
		final GridData gd_pLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
				true, 2, 1);
		gd_pLabel.widthHint = 206;
		pLabel.setLayoutData(gd_pLabel);
		pLabel.setText(Messages.XEuclideanView_Message_P);
		new Label(action, SWT.NONE);
		new Label(action, SWT.NONE);
		new Label(action, SWT.NONE);
		new Label(action, SWT.NONE);

		pText = new Text(action, SWT.BORDER);
		pTextOnlyNumbers = new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				/*
				 * keyCode == 8 is BACKSPACE and keyCode == 48 and keyCode ==
				 * 128 is DEL
				 */
				if (e.text.matches("[0-9]*") || e.keyCode == SWT.BS || e.keyCode == SWT.DEL) { //$NON-NLS-1$
					if (pText.getText().length() == 0
							&& e.text.compareTo("0") == 0) { //$NON-NLS-1$
						e.doit = false;
					} else if (pText.getSelection().x == 0 && e.keyCode == 48) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}
		};
		pText.addVerifyListener(pTextOnlyNumbers);

		final GridData gd_pText = new GridData(SWT.FILL, SWT.CENTER, false,
				true, 6, 1);
		gd_pText.widthHint = 445;
		pText.setLayoutData(gd_pText);

		final Label qLabel = new Label(action, SWT.NONE);
		qLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 2,
				1));
		qLabel.setText(Messages.XEuclideanView_Message_Q);
		new Label(action, SWT.NONE);
		new Label(action, SWT.NONE);
		new Label(action, SWT.NONE);
		new Label(action, SWT.NONE);

		qText = new Text(action, SWT.BORDER);
		qTextOnlyNumbers = pTextOnlyNumbers;
		qText.addVerifyListener(qTextOnlyNumbers);
		final GridData gd_qText = new GridData(SWT.FILL, SWT.CENTER, false,
				true, 6, 1);
		gd_qText.widthHint = 465;
		qText.setLayoutData(gd_qText);

		final Button clearButton = new Button(action, SWT.NONE);
		final GridData gd_clearButton = new GridData(SWT.RIGHT, SWT.CENTER,
				false, true);
		gd_clearButton.widthHint = 125;
		clearButton.setLayoutData(gd_clearButton);
		clearButton.setText(Messages.XEuclideanView_Clear_Button);

		final Button resetTableButton = new Button(action, SWT.NONE);
		resetTableButton.setEnabled(false);
		final GridData gd_resetTableButton = new GridData(SWT.LEFT, SWT.CENTER,
				false, true);
		gd_resetTableButton.widthHint = 125;
		resetTableButton.setLayoutData(gd_resetTableButton);
		resetTableButton.setText(Messages.XEuclideanView_ResetTable_Button);

		final Button computeButton = new Button(action, SWT.NONE);
		final GridData gd_computeButton = new GridData(SWT.LEFT, SWT.CENTER,
				false, true);
		gd_computeButton.widthHint = 125;
		computeButton.setLayoutData(gd_computeButton);
		computeButton.setText(Messages.XEuclideanView_Compute_Button);

		final Button stepwiseButton = new Button(action, SWT.NONE);
		final Button backStepwiseButton = new Button(action, SWT.NONE);

		resetTableButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(final MouseEvent e) {
				/*
				 * clear first the table
				 */
				table.clearAll();
				table.setItemCount(0);

				/*
				 * enable or disable the control buttons
				 */
				computeButton.setEnabled(true);
				stepwiseButton.setEnabled(true);
				resetTableButton.setEnabled(false);
				backStepwiseButton.setEnabled(false);
				exportToPdfAction.setEnabled(false);
				exportToLatexAction.setEnabled(false);
				exportToCSVAction.setEnabled(false);

				stepwiseCounter = 0;

				nextState = visualMode.INIT;

				visualizeStyledText.setText(""); //$NON-NLS-1$
				resultLabel.setText(""); //$NON-NLS-1$
				resultText.setText(""); //$NON-NLS-1$

				pValue = ""; //$NON-NLS-1$
				qValue = ""; //$NON-NLS-1$

			}
		});

		computeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(final MouseEvent e) {
				clearAll();

				pValue = pText.getText();
				qValue = qText.getText();

				if (pValue.compareTo("") != 0 && qValue.compareTo("") != 0) { //$NON-NLS-1$ //$NON-NLS-2$
					BigInteger p = new BigInteger(pText.getText());
					BigInteger q = new BigInteger(qText.getText());
					value = xeuclid.xeuclid(p, q);
					xTmp = xeuclid.getX();
					yTmp = xeuclid.getY();
					rTmp = xeuclid.getR();
					qTmp = xeuclid.getQ();

					/*
					 * get the values and write it into the table
					 */
					for (int i = 0; i < qTmp.size(); i++) {
						TableItem tableItem = new TableItem(table, SWT.BORDER);
						tableItem.setText(0, String.valueOf(i));
						if (i == 0 || i == qTmp.size() - 1) {
							tableItem.setText(1, ""); //$NON-NLS-1$
						} else {
							tableItem.setText(1, qTmp.get(i).toString());
						}
						tableItem.setText(2, rTmp.get(i).toString());
						tableItem.setText(3, xTmp.get(i).toString());
						tableItem.setText(4, yTmp.get(i).toString());

						/*
						 * autoscroll the table. the second line is for
						 * deselection the TableItem.
						 */
						table.setSelection(table.getItems().length - 1);
						table.setSelection(table.getItems().length);

					}
					String yTmpLastElement = yTmp.lastElement().toString();
					String xTmpLastElement = xTmp.lastElement().toString();
					/*
					 * if the value for x or y is negative, then the sign have
					 * to be in parenthesis
					 */
					if (xTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
						xTmpLastElement = "( " + xTmpLastElement + " )"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					if (yTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
						yTmpLastElement = "( " + yTmpLastElement + " )"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					String tmpValue = " = " + rTmp.firstElement() + " * " + yTmpLastElement + " + " + rTmp.get(1) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ " * " + xTmpLastElement; //$NON-NLS-1$

					/*
					 * check which parameter is bigger, then change the order
					 */
					if (p.compareTo(q) < 0) {
						resultLabel.setText("gcd(q,p) = q * x + p * y"); //$NON-NLS-1$
					} else {
						resultLabel.setText("gcd(p,q) = p * x + q * y"); //$NON-NLS-1$
					}

					resultText.setText(value.toString() + tmpValue);

					/*
					 * visualization block
					 */
					String result = Messages.XEuclideanView_GCD_Text
							+ " " + value.toString(); //$NON-NLS-1$

					visualizeStyledText.setText(result);
					StyleRange parameterA = new StyleRange();
					parameterA.start = 0;
					parameterA.length = Messages.XEuclideanView_GCD_Text
							.length();
					parameterA.foreground = BLACK_COLOR;
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					StyleRange parameterB = new StyleRange();
					parameterB.start = Messages.XEuclideanView_GCD_Text
							.length() + 1;
					parameterB.length = value.toString().length();
					parameterB.foreground = BLUE_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					/*
					 * set the color of the result in table to blue
					 */
					table.getItem(qTmp.size() - 2).setForeground(2, BLUE_COLOR);

					stepwiseCounter = qTmp.size() - 1;
					previousState = visualMode.Y_EVAL;
					/*
					 * enable or disable the control buttons
					 */
					backStepwiseButton.setEnabled(true);
					computeButton.setEnabled(false);
					stepwiseButton.setEnabled(false);
					resetTableButton.setEnabled(true);
					clearButton.setEnabled(true);
					exportToPdfAction.setEnabled(true);
					exportToLatexAction.setEnabled(true);
					exportToCSVAction.setEnabled(true);

				}

			}

		});

		// final Button stepwiseButton = new Button(action, SWT.NONE);
		stepwiseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(final MouseEvent e) {

				StyleRange parameterM = new StyleRange();
				StyleRange parameterA = new StyleRange();
				StyleRange parameterB = new StyleRange();
				StyleRange parameterC = new StyleRange();
				StyleRange parameterD = new StyleRange();

				String tmpA;
				String tmpB;
				String tmpC;
				String tmpD;

				/*
				 * have to be initialized
				 */
				Vector<BigInteger> xTmp = xeuclid.getX();
				Vector<BigInteger> yTmp = xeuclid.getY();
				Vector<BigInteger> rTmp = xeuclid.getR();
				Vector<BigInteger> qTmp = xeuclid.getQ();

				switch (nextState) {
				/*
				 * the initial state, where xeuclid is called
				 */
				case INIT:
					clearAll();
					pValue = pText.getText();
					qValue = qText.getText();

					if (pValue.compareTo("") != 0 && qValue.compareTo("") != 0) { //$NON-NLS-1$ //$NON-NLS-2$
						BigInteger p = new BigInteger(pValue);
						BigInteger q = new BigInteger(qValue);

						// executeButton.setEnabled(false);
						resetTableButton.setEnabled(true);

						value = xeuclid.xeuclid(p, q);
						xTmp = xeuclid.getX();
						yTmp = xeuclid.getY();
						rTmp = xeuclid.getR();
						qTmp = xeuclid.getQ();

						tableItemTmp = new TableItem(table, SWT.BORDER);
						tableItemTmp.setText(0, String.valueOf(stepwiseCounter));
						tableItemTmp.setText(1, ""); //$NON-NLS-1$
						tableItemTmp.setText(2, rTmp.get(stepwiseCounter)
								.toString());
						tableItemTmp.setText(3, xTmp.get(stepwiseCounter)
								.toString());
						tableItemTmp.setText(4, yTmp.get(stepwiseCounter)
								.toString());
						stepwiseCounter++;

						tableItem0 = new TableItem(table, SWT.BORDER);
						tableItem0.setText(0, String.valueOf(stepwiseCounter));
						tableItem0.setText(2, rTmp.get(stepwiseCounter)
								.toString());
						tableItem0.setText(3, xTmp.get(stepwiseCounter)
								.toString());
						tableItem0.setText(4, yTmp.get(stepwiseCounter)
								.toString());
						stepwiseCounter++;

						tableItem1 = null;

						/*
						 * visualization block
						 */
						visualizeStyledText
								.setText(Messages.XEuclideanView_Initialization);
						StyleRange styleInit = new StyleRange();
						styleInit.foreground = BLACK_COLOR;
						styleInit.start = 0;
						styleInit.length = Messages.XEuclideanView_Initialization
								.length();
						styleInit.fontStyle = SWT.BOLD;
						visualizeStyledText.setStyleRange(styleInit);

						nextState = visualMode.QUOTIENT;
						previousState = null;
					}

					break;
				case QUOTIENT:
					/*
					 * tableItemTmp is the first row tableItem0 is the second
					 * row tableItam1 is the third row
					 */
					backStepwiseButton.setEnabled(true);

					clearTableItem(tableItemTmp);
					clearTableItem(tableItem0);
					if (stepwiseCounter < 3 && stepwiseCounter != 2) {
						clearTableItem(tableItem1);
					} else {
						stepwiseCounter = table.getItemCount();
					}

					tableItemTmp = table.getItem(stepwiseCounter - 2);
					tableItem0 = table.getItem(stepwiseCounter - 1);

					clearTableItem(tableItem0);

					tableItemTmp.setForeground(2, RED_COLOR);
					tableItem0.setForeground(2, GREEN_COLOR);

					tableItem0.setText(1, qTmp.get(stepwiseCounter - 1)
							.toString());
					tableItem0.setForeground(1, BLUE_COLOR);

					/*
					 * visualization block
					 */
					tmpA = tableItemTmp.getText(2);
					tmpB = tableItem0.getText(2);
					tmpC = qTmp.get(stepwiseCounter - 1).toString();

					String result = Messages.XEuclideanView_Quotient + tmpA
							+ " / " + tmpB + " = " + tmpC; //$NON-NLS-1$ //$NON-NLS-2$

					visualizeStyledText.setText(result);
					parameterM.start = 0;
					parameterM.length = Messages.XEuclideanView_Quotient
							.length();
					parameterM.foreground = BLACK_COLOR;
					parameterM.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterM);

					parameterA.start = Messages.XEuclideanView_Quotient
							.length();
					parameterA.length = tmpA.length();
					parameterA.foreground = RED_COLOR;
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					parameterB.start = Messages.XEuclideanView_Quotient
							.length() + tmpA.length() + 3;
					parameterB.length = tmpB.length();
					parameterB.foreground = GREEN_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					parameterC.start = Messages.XEuclideanView_Quotient
							.length() + tmpA.length() + 3 + tmpB.length() + 3;
					parameterC.length = tmpC.length();
					parameterC.foreground = BLUE_COLOR;
					parameterC.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterC);

					nextState = visualMode.REMAINDER;
					if (previousState != null) {
						previousState = visualMode.Y_EVAL;
					} else {
						previousState = visualMode.INIT;
					}

					break;

				case REMAINDER:
					tableItem1 = new TableItem(table, SWT.BORDER);

					tableItem1.setText(0, String.valueOf(stepwiseCounter));
					tmpA = rTmp.get(stepwiseCounter).toString();
					tableItem1.setText(2, tmpA);
					tableItem1.setForeground(2, BLUE_COLOR);

					tableItem0.setForeground(1, BLACK_COLOR);

					/*
					 * visualization block
					 */
					result = Messages.XEuclideanView_Remainder + tmpA;

					visualizeStyledText.setText(result);
					parameterA = new StyleRange();
					parameterA.start = 0;
					parameterA.length = Messages.XEuclideanView_Remainder
							.length();
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					parameterB = new StyleRange();
					parameterB.start = Messages.XEuclideanView_Remainder
							.length();
					parameterB.length = tmpA.length();
					parameterB.foreground = BLUE_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					nextState = visualMode.X_EVAL;
					previousState = visualMode.QUOTIENT;

					/*
					 * autoscroll the table. the second line is for deselection
					 * the TableItem.
					 */
					table.setSelection(table.getItems().length - 1);
					table.setSelection(table.getItems().length);

					break;

				case X_EVAL:
					/*
					 * clear second column and set the color to black
					 */
					tableItemTmp.setForeground(2, BLACK_COLOR);
					tableItem0.setForeground(2, BLACK_COLOR);
					tableItem1.setForeground(2, BLACK_COLOR);

					tableItem0.setForeground(1, VIOLET_COLOR);
					tableItemTmp.setForeground(3, RED_COLOR);
					tableItem0.setForeground(3, GREEN_COLOR);

					tmpA = xTmp.get(stepwiseCounter).toString();
					tableItem1.setText(3, tmpA);
					tableItem1.setForeground(3, BLUE_COLOR);

					/*
					 * visualization block
					 */
					tmpA = qTmp.get(stepwiseCounter - 1).toString();
					tmpB = xTmp.get(stepwiseCounter - 1).toString();
					tmpC = xTmp.get(stepwiseCounter - 2).toString();
					tmpD = xTmp.get(stepwiseCounter).toString();

					result = MESSAGE_X_VISUAL + tmpA
							+ " * " + tmpB + " + " + tmpC + " = " + tmpD; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

					visualizeStyledText.setText(result);
					parameterM = new StyleRange();
					parameterM.start = 0;
					parameterM.length = MESSAGE_X_VISUAL.length();
					parameterM.foreground = BLACK_COLOR;
					parameterM.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterM);

					parameterA = new StyleRange();
					parameterA.start = MESSAGE_X_VISUAL.length();
					parameterA.length = tmpA.length();
					parameterA.foreground = VIOLET_COLOR;
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					parameterB = new StyleRange();
					parameterB.start = MESSAGE_X_VISUAL.length()
							+ tmpA.length() + 3;
					parameterB.length = tmpB.length();
					parameterB.foreground = GREEN_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					parameterC = new StyleRange();
					parameterC.start = MESSAGE_X_VISUAL.length()
							+ tmpA.length() + 3 + tmpB.length() + 3;
					parameterC.length = tmpC.length();
					parameterC.foreground = RED_COLOR;
					parameterC.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterC);

					parameterD = new StyleRange();
					parameterD.start = MESSAGE_X_VISUAL.length()
							+ tmpA.length() + 3 + tmpB.length() + 3
							+ tmpC.length() + 3;
					parameterD.length = tmpD.length();
					parameterD.foreground = BLUE_COLOR;
					parameterD.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterD);

					nextState = visualMode.Y_EVAL;
					previousState = visualMode.REMAINDER;

					break;

				case Y_EVAL:
					/*
					 * clear third column and set the color to black
					 */
					tableItemTmp.setForeground(3, BLACK_COLOR);
					tableItem0.setForeground(3, BLACK_COLOR);
					tableItem1.setForeground(3, BLACK_COLOR);

					tableItemTmp.setForeground(4, RED_COLOR);
					tableItem0.setForeground(4, GREEN_COLOR);

					tmpA = yTmp.get(stepwiseCounter).toString();
					tableItem1.setText(4, tmpA);
					tableItem1.setForeground(4, BLUE_COLOR);

					/*
					 * visualization block
					 */
					tmpA = qTmp.get(stepwiseCounter - 1).toString();
					tmpB = yTmp.get(stepwiseCounter - 1).toString();
					tmpC = yTmp.get(stepwiseCounter - 2).toString();
					tmpD = yTmp.get(stepwiseCounter).toString();

					result = MESSAGE_Y_VISUAL + tmpA
							+ " * " + tmpB + " + " + tmpC + " = " + tmpD; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

					visualizeStyledText.setText(result);
					parameterM = new StyleRange();
					parameterM.start = 0;
					parameterM.length = MESSAGE_Y_VISUAL.length();
					parameterM.foreground = BLACK_COLOR;
					parameterM.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterM);

					parameterA = new StyleRange();
					parameterA.start = MESSAGE_Y_VISUAL.length();
					parameterA.length = tmpA.length();
					parameterA.foreground = VIOLET_COLOR;
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					parameterB = new StyleRange();
					parameterB.start = MESSAGE_Y_VISUAL.length()
							+ tmpA.length() + 3;
					parameterB.length = tmpB.length();
					parameterB.foreground = GREEN_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					parameterC = new StyleRange();
					parameterC.start = MESSAGE_Y_VISUAL.length()
							+ tmpA.length() + 3 + tmpB.length() + 3;
					parameterC.length = tmpC.length();
					parameterC.foreground = RED_COLOR;
					parameterC.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterC);

					parameterD = new StyleRange();
					parameterD.start = MESSAGE_Y_VISUAL.length()
							+ tmpA.length() + 3 + tmpB.length() + 3
							+ tmpC.length() + 3;
					parameterD.length = tmpD.length();
					parameterD.foreground = BLUE_COLOR;
					parameterD.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterD);

					if (stepwiseCounter >= qTmp.size() - 1) {
						nextState = visualMode.RESULT;
					} else {
						nextState = visualMode.QUOTIENT;
						stepwiseCounter++;
					}
					previousState = visualMode.X_EVAL;

					break;

				case RESULT:
					/*
					 * clear the three rows and set the color of the result
					 * value to blue
					 */
					clearTableItem(tableItemTmp);
					clearTableItem(tableItem0);
					clearTableItem(tableItem1);

					tableItem0.setForeground(2, BLUE_COLOR);

					String yTmpLastElement = yTmp.lastElement().toString();
					String xTmpLastElement = xTmp.lastElement().toString();
					if (xTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
						xTmpLastElement = "( " + xTmpLastElement + " )"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					if (yTmp.lastElement().compareTo(BigInteger.ZERO) < 0) {
						yTmpLastElement = "( " + yTmpLastElement + " )"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					String tmpValue = " = " + rTmp.firstElement() + " * " + yTmpLastElement + " + " + rTmp.get(1) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ " * " + xTmpLastElement; //$NON-NLS-1$
					resultText.setText(value.toString() + tmpValue);

					if (new BigInteger(pValue)
							.compareTo(new BigInteger(qValue)) < 0) {
						resultLabel.setText("gcd(q,p) = q * x + p * y"); //$NON-NLS-1$
					} else {
						resultLabel.setText("gcd(p,q) = p * x + q * y"); //$NON-NLS-1$
					}

					/*
					 * visualization block
					 */
					result = Messages.XEuclideanView_GCD_Text
							+ " " + value.toString(); //$NON-NLS-1$

					visualizeStyledText.setText(result);
					parameterA = new StyleRange();
					parameterA.start = 0;
					parameterA.length = Messages.XEuclideanView_GCD_Text
							.length();
					parameterA.foreground = BLACK_COLOR;
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					parameterB = new StyleRange();
					parameterB.start = Messages.XEuclideanView_GCD_Text
							.length() + 1;
					parameterB.length = value.toString().length();
					parameterB.foreground = BLUE_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					stepwiseButton.setEnabled(false);
					computeButton.setEnabled(false);
					exportToPdfAction.setEnabled(true);
					exportToLatexAction.setEnabled(true);
					exportToCSVAction.setEnabled(true);

					nextState = null;
					previousState = visualMode.Y_EVAL;

					break;
				}
			}
		});
		final GridData gd_stepwiseButton = new GridData(SWT.LEFT, SWT.CENTER,
				false, true);
		gd_stepwiseButton.widthHint = 125;
		stepwiseButton.setLayoutData(gd_stepwiseButton);
		stepwiseButton.setText(Messages.XEuclideanView_Stepwise_Button);

		backStepwiseButton.setEnabled(false);
		backStepwiseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(final MouseEvent e) {

				stepwiseButton.setEnabled(true);

				StyleRange parameterM = new StyleRange();
				StyleRange parameterA = new StyleRange();
				StyleRange parameterB = new StyleRange();
				StyleRange parameterC = new StyleRange();
				StyleRange parameterD = new StyleRange();

				String tmpA;
				String tmpB;
				String tmpC;
				String tmpD;

				Vector<BigInteger> xTmp = xeuclid.getX();
				Vector<BigInteger> yTmp = xeuclid.getY();
				Vector<BigInteger> rTmp = xeuclid.getR();
				Vector<BigInteger> qTmp = xeuclid.getQ();

				String result;

				switch (previousState) {

				case INIT:
					clearTableItem(tableItemTmp);
					clearTableItem(tableItem0);

					tableItem0.setText(1, ""); //$NON-NLS-1$

					backStepwiseButton.setEnabled(false);

					nextState = visualMode.QUOTIENT;
					previousState = null;

					/*
					 * visualization block
					 */
					visualizeStyledText
							.setText(Messages.XEuclideanView_Initialization);
					StyleRange styleInit = new StyleRange();
					styleInit.foreground = BLACK_COLOR;
					styleInit.start = 0;
					styleInit.length = Messages.XEuclideanView_Initialization
							.length();
					styleInit.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(styleInit);

					break;

				case QUOTIENT:
					tableItem0.setForeground(1, BLUE_COLOR);

					/*
					 * visualization block
					 */
					tmpA = tableItemTmp.getText(2);
					tmpB = tableItem0.getText(2);
					tmpC = qTmp.get(stepwiseCounter - 1).toString();

					result = Messages.XEuclideanView_Quotient + tmpA
							+ " / " + tmpB + " = " + tmpC; //$NON-NLS-1$ //$NON-NLS-2$

					visualizeStyledText.setText(result);
					parameterM.start = 0;
					parameterM.length = Messages.XEuclideanView_Quotient
							.length();
					parameterM.foreground = BLACK_COLOR;
					parameterM.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterM);

					parameterA.start = Messages.XEuclideanView_Quotient
							.length();
					parameterA.length = tmpA.length();
					parameterA.foreground = RED_COLOR;
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					parameterB.start = Messages.XEuclideanView_Quotient
							.length() + tmpA.length() + 3;
					parameterB.length = tmpB.length();
					parameterB.foreground = GREEN_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					parameterC.start = Messages.XEuclideanView_Quotient
							.length() + tmpA.length() + 3 + tmpB.length() + 3;
					parameterC.length = tmpC.length();
					parameterC.foreground = BLUE_COLOR;
					parameterC.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterC);

					table.remove(stepwiseCounter);

					// currentState = visualMode.QUOTIENT;
					nextState = visualMode.REMAINDER;
					if (previousState != null) {
						previousState = visualMode.Y_EVAL;
						stepwiseCounter = table.getItemCount();
					}

					if (stepwiseCounter == 2) {
						previousState = visualMode.INIT;
					}

					break;

				case REMAINDER:
					tableItemTmp.setForeground(3, BLACK_COLOR);
					tableItem0.setForeground(3, BLACK_COLOR);
					tableItem1.setForeground(3, BLACK_COLOR);
					tableItem0.setForeground(1, BLACK_COLOR);

					tableItem1.setForeground(2, BLUE_COLOR);
					tableItem0.setForeground(2, GREEN_COLOR);
					tableItemTmp.setForeground(2, RED_COLOR);

					tableItem1.setText(3, ""); //$NON-NLS-1$

					/*
					 * visualization block
					 */
					tmpA = rTmp.get(stepwiseCounter).toString();

					result = Messages.XEuclideanView_Remainder + tmpA;

					visualizeStyledText.setText(result);
					parameterA = new StyleRange();
					parameterA.start = 0;
					parameterA.length = Messages.XEuclideanView_Remainder
							.length();
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					parameterB = new StyleRange();
					parameterB.start = Messages.XEuclideanView_Remainder
							.length();
					parameterB.length = tmpA.length();
					parameterB.foreground = BLUE_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					nextState = visualMode.X_EVAL;
					previousState = visualMode.QUOTIENT;

					break;

				case X_EVAL:
					stepwiseCounter = table.getItemCount() - 1;

					tableItemTmp.setForeground(4, BLACK_COLOR);
					tableItem0.setForeground(4, BLACK_COLOR);
					tableItem1.setForeground(4, BLACK_COLOR);

					tableItemTmp.setForeground(3, RED_COLOR);
					tableItem0.setForeground(3, GREEN_COLOR);
					tableItem1.setForeground(3, BLUE_COLOR);

					tableItem1.setText(4, ""); //$NON-NLS-1$

					/*
					 * visualization block
					 */
					tmpA = qTmp.get(stepwiseCounter - 1).toString();
					tmpB = xTmp.get(stepwiseCounter - 1).toString();
					tmpC = xTmp.get(stepwiseCounter - 2).toString();
					tmpD = xTmp.get(stepwiseCounter).toString();

					result = MESSAGE_X_VISUAL + tmpA
							+ " * " + tmpB + " + " + tmpC + " = " + tmpD; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

					visualizeStyledText.setText(result);
					parameterM = new StyleRange();
					parameterM.start = 0;
					parameterM.length = MESSAGE_X_VISUAL.length();
					parameterM.foreground = BLACK_COLOR;
					parameterM.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterM);

					parameterA = new StyleRange();
					parameterA.start = MESSAGE_X_VISUAL.length();
					parameterA.length = tmpA.length();
					parameterA.foreground = VIOLET_COLOR;
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					parameterB = new StyleRange();
					parameterB.start = MESSAGE_X_VISUAL.length()
							+ tmpA.length() + 3;
					parameterB.length = tmpB.length();
					parameterB.foreground = GREEN_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					parameterC = new StyleRange();
					parameterC.start = MESSAGE_X_VISUAL.length()
							+ tmpA.length() + 3 + tmpB.length() + 3;
					parameterC.length = tmpC.length();
					parameterC.foreground = RED_COLOR;
					parameterC.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterC);

					parameterD = new StyleRange();
					parameterD.start = MESSAGE_X_VISUAL.length()
							+ tmpA.length() + 3 + tmpB.length() + 3
							+ tmpC.length() + 3;
					parameterD.length = tmpD.length();
					parameterD.foreground = BLUE_COLOR;
					parameterD.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterD);

					nextState = visualMode.Y_EVAL;
					previousState = visualMode.REMAINDER;

					break;

				case Y_EVAL:
					stepwiseCounter = table.getItemCount() - 1;

					tableItemTmp = table.getItem(stepwiseCounter - 2);
					tableItem0 = table.getItem(stepwiseCounter - 1);
					tableItem1 = table.getItem(stepwiseCounter);

					clearTableItem(tableItemTmp);
					clearTableItem(tableItem0);
					clearTableItem(tableItem1);

					tableItem0.setForeground(1, VIOLET_COLOR);

					tableItemTmp.setForeground(4, RED_COLOR);
					tableItem0.setForeground(4, GREEN_COLOR);
					tableItem1.setForeground(4, BLUE_COLOR);

					tableItem1.setText(1, ""); //$NON-NLS-1$

					/*
					 * visualization block
					 */
					tmpA = qTmp.get(stepwiseCounter - 1).toString();
					tmpB = yTmp.get(stepwiseCounter - 1).toString();
					tmpC = yTmp.get(stepwiseCounter - 2).toString();
					tmpD = yTmp.get(stepwiseCounter).toString();

					resultLabel.setText(""); //$NON-NLS-1$
					resultText.setText(""); //$NON-NLS-1$

					result = MESSAGE_Y_VISUAL + tmpA
							+ " * " + tmpB + " + " + tmpC + " = " + tmpD; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

					visualizeStyledText.setText(result);
					parameterM = new StyleRange();
					parameterM.start = 0;
					parameterM.length = MESSAGE_Y_VISUAL.length();
					parameterM.foreground = BLACK_COLOR;
					parameterM.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterM);

					parameterA = new StyleRange();
					parameterA.start = MESSAGE_Y_VISUAL.length();
					parameterA.length = tmpA.length();
					parameterA.foreground = VIOLET_COLOR;
					parameterA.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterA);

					parameterB = new StyleRange();
					parameterB.start = MESSAGE_Y_VISUAL.length()
							+ tmpA.length() + 3;
					parameterB.length = tmpB.length();
					parameterB.foreground = GREEN_COLOR;
					parameterB.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterB);

					parameterC = new StyleRange();
					parameterC.start = MESSAGE_Y_VISUAL.length()
							+ tmpA.length() + 3 + tmpB.length() + 3;
					parameterC.length = tmpC.length();
					parameterC.foreground = RED_COLOR;
					parameterC.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterC);

					parameterD = new StyleRange();
					parameterD.start = MESSAGE_Y_VISUAL.length()
							+ tmpA.length() + 3 + tmpB.length() + 3
							+ tmpC.length() + 3;
					parameterD.length = tmpD.length();
					parameterD.foreground = BLUE_COLOR;
					parameterD.fontStyle = SWT.BOLD;
					visualizeStyledText.setStyleRange(parameterD);

					if (stepwiseCounter >= qTmp.size() - 1) {
						nextState = visualMode.RESULT;
						computeButton.setEnabled(true);
						exportToPdfAction.setEnabled(false);
						exportToLatexAction.setEnabled(false);
						exportToCSVAction.setEnabled(false);
					} else {
						nextState = visualMode.QUOTIENT;
					}
					previousState = visualMode.X_EVAL;

					break;
				}

			}
		});
		final GridData gd_backStepwiseButton = new GridData(SWT.LEFT,
				SWT.CENTER, false, true);
		gd_backStepwiseButton.widthHint = 125;
		backStepwiseButton.setLayoutData(gd_backStepwiseButton);
		backStepwiseButton.setText(Messages.XEuclideanView_BackStepwise_Button);

		info = new Group(main, SWT.NONE);
		final GridData gridDataI = new GridData(SWT.FILL, SWT.FILL, true, false);

		gridDataI.heightHint = 333;
		info.setLayoutData(gridDataI);
		final GridLayout gridLayoutInfo = new GridLayout();
		gridLayoutInfo.numColumns = 2;
		info.setLayout(gridLayoutInfo);
		info.setText(Messages.XEuclideanView_Visualization);

		table = new Table(info, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		final GridData gd_table = new GridData(SWT.FILL, SWT.CENTER, true,
				true, 2, 1);
		gd_table.widthHint = 466;
		gd_table.heightHint = 198;
		table.setLayoutData(gd_table);

		final TableColumn indexTableColumn = new TableColumn(table, SWT.NONE);
		indexTableColumn.setResizable(false);
		indexTableColumn.setWidth(50);
		indexTableColumn.setText("Index"); //$NON-NLS-1$

		final TableColumn quotientTableColumn = new TableColumn(table, SWT.NONE);
		quotientTableColumn.setWidth(100);
		quotientTableColumn.setText("Quotient"); //$NON-NLS-1$

		final TableColumn remainderrTableColumn = new TableColumn(table,
				SWT.NONE);
		remainderrTableColumn.setWidth(100);
		remainderrTableColumn.setText(Messages.XEuclideanView_Remainder_Table);

		final TableColumn xTableColumn = new TableColumn(table, SWT.NONE);
		xTableColumn.setWidth(100);
		xTableColumn.setText("X"); //$NON-NLS-1$

		final TableColumn yTableColumn = new TableColumn(table, SWT.NONE);
		yTableColumn.setWidth(100);
		yTableColumn.setText("Y"); //$NON-NLS-1$

		final Label gcdLabel = new Label(info, SWT.NONE);
		final GridData gd_gcdLabel = new GridData(SWT.FILL, SWT.FILL, false,
				true);
		gd_gcdLabel.heightHint = 24;
		gd_gcdLabel.widthHint = 154;
		gd_gcdLabel.verticalIndent = 15;
		gcdLabel.setLayoutData(gd_gcdLabel);
		gcdLabel.setText(Messages.XEuclideanView_GCD_Text);

		resultLabel = new Label(info, SWT.NONE);
		final GridData gd_resultLabel = new GridData(SWT.FILL, SWT.FILL, false,
				true);
		gd_resultLabel.widthHint = 396;
		gd_resultLabel.verticalIndent = 15;
		resultLabel.setLayoutData(gd_resultLabel);
		resultLabel.setFont(FontService.getNormalFont());
		resultLabel.setForeground(BLUE_COLOR);

		resultText = new Text(info, SWT.H_SCROLL | SWT.BORDER);
		resultText.setEditable(false);
		final GridData gd_resultText = new GridData(SWT.FILL, SWT.FILL, true,
				false, 2, 1);
		resultText.setLayoutData(gd_resultText);

		clearButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(final MouseEvent e) {
				clearAll();
				stepwiseCounter = 0;

				backStepwiseButton.setEnabled(false);
				computeButton.setEnabled(true);
				stepwiseButton.setEnabled(true);
				resetTableButton.setEnabled(false);
				exportToPdfAction.setEnabled(false);
				exportToLatexAction.setEnabled(false);
				exportToCSVAction.setEnabled(false);

				/*
				 * we have to remove the verifyListener first to display the
				 * changed value for the parameters, then we added again
				 */
				pText.removeVerifyListener(pTextOnlyNumbers);
				qText.removeVerifyListener(qTextOnlyNumbers);

				pText.setText(""); //$NON-NLS-1$
				qText.setText(""); //$NON-NLS-1$
				visualizeStyledText.setText(""); //$NON-NLS-1$

				pText.addVerifyListener(pTextOnlyNumbers);
				qText.addVerifyListener(qTextOnlyNumbers);

				resultLabel.setText(""); //$NON-NLS-1$
				resultText.setText(""); //$NON-NLS-1$

				nextState = visualMode.INIT;

			}
		});

		final Label dummyLabel = new Label(action, SWT.NONE);
		final GridData gd_dummyLabel = new GridData(SWT.LEFT, SWT.CENTER, true,
				false);
		dummyLabel.setLayoutData(gd_dummyLabel);
		dummyLabel.setText("dummy"); //$NON-NLS-1$
		dummyLabel.setVisible(false);

		visualizeStyledText = new StyledText(action, SWT.SINGLE | SWT.READ_ONLY
				| SWT.BORDER);
		visualizeStyledText.setEnabled(false);
		visualizeStyledText.setEditable(false);
		visualizeStyledText.setAlignment(SWT.CENTER);
		final GridData gd_visualizeStyledText = new GridData(SWT.FILL,
				SWT.CENTER, false, false, 6, 1);
		gd_visualizeStyledText.widthHint = 400;
		visualizeStyledText.setLayoutData(gd_visualizeStyledText);

		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(parent.getShell(),
						XEuclideanPlugin.PLUGIN_ID + ".viewer");
		createActions();
		initializeMenu();
	}

	/**
	 * Create the actions
	 */
	private void createActions() {

		exportToPdfAction = new Action(Messages.XEuclideanView_ExportPDF_Menu) {
			@Override
			public void run() {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
				dialog.setFilterPath(DirectoryService.getUserHomeDir());
				dialog.setFilterExtensions(new String[] { IConstants.PDF_FILTER_EXTENSION });
				dialog.setFilterNames(new String[] { IConstants.PDF_FILTER_NAME });
				dialog.setOverwrite(true);

				String filename = dialog.open();

				if (filename != null) {
					FileExporter pdfExport = new FileExporter(xeuclid, filename);
					pdfExport.exportToPDF();
				}
			}
		};
		exportToPdfAction.setEnabled(false);

		exportToCSVAction = new Action(Messages.XEuclideanView_ExportCSV_Menu) {
			@Override
			public void run() {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
				dialog.setFilterPath(DirectoryService.getUserHomeDir());
				dialog.setFilterExtensions(new String[] { IConstants.CSV_FILTER_EXTENSION });
				dialog.setFilterNames(new String[] { IConstants.CSV_FILTER_NAME });
				dialog.setOverwrite(true);

				String filename = dialog.open();

				if (filename != null) {
					FileExporter csvExport = new FileExporter(xeuclid, filename);
					csvExport.exportToCSV();
				}
			}
		};
		exportToCSVAction.setEnabled(false);

		exportToLatexAction = new Action(
				Messages.XEuclideanView_ExportLatex_Menu) {
			@Override
			public void run() {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
				dialog.setFilterPath(DirectoryService.getUserHomeDir());
				dialog.setFilterExtensions(new String[] { IConstants.TEX_FILTER_EXTENSION });
				dialog.setFilterNames(new String[] { IConstants.TEX_FILTER_NAME });
				dialog.setOverwrite(true);

				String filename = dialog.open();

				if (filename != null) {
					FileExporter latexExport = new FileExporter(xeuclid,
							filename);
					latexExport.exportToLatex();
				}

			}
		};
		exportToLatexAction.setEnabled(false);
		// Create the actions
	}

	/**
	 * Initialize the menu
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();

		menuManager.add(exportToPdfAction);

		menuManager.add(exportToLatexAction);

		menuManager.add(exportToCSVAction);
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}

	/**
	 * Clears the table and set all values back
	 */
	private void clearAll() {
		table.clearAll();
		table.setItemCount(0);
		xeuclid.clear();
	}

	/**
	 * Set the Color of the tableItem back to black
	 */
	private void clearTableItem(TableItem tableItem) {
		if (tableItem != null) {
			tableItem.setForeground(0, BLACK_COLOR);
			tableItem.setForeground(1, BLACK_COLOR);
			tableItem.setForeground(2, BLACK_COLOR);
			tableItem.setForeground(3, BLACK_COLOR);
			tableItem.setForeground(4, BLACK_COLOR);
		}
	}
}
