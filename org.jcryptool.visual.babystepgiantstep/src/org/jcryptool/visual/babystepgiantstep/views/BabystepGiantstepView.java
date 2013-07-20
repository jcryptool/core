package org.jcryptool.visual.babystepgiantstep.views;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
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
	public static final String ID = "org.jcryptool.visual.babystepgiantstep.views.BabystepGiantstepView";
	private Text textGroup;
	private Text textGenerator;
	private Text textGroupElement;
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
	private Button btnClearTheTables;
	private Button btnResult;
	private TableColumn tblclmnCommentBS;
	private TableColumn tblclmnQ;
	private TableColumn tblclmnGiantSteps;
	private Composite compositeDescription;
	private StyledText styledText;

	private VerifyListener vl_numbers = new VerifyListener() {

		@Override
		public void verifyText(VerifyEvent e) {
			e.doit = true;

			String text = e.text;
			char[] chars = text.toCharArray();

			for (int i = 0; i < chars.length; i++) {
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
	private Label lblInv;
	private Text textInv;

	/**
	 * The constructor.
	 */
	public BabystepGiantstepView() {
		setPartName("test");

	}

	@Override
	public void createPartControl(Composite parent) {

		Group grpBabyStepGiant = new Group(parent, SWT.NONE);
		grpBabyStepGiant.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		grpBabyStepGiant.setText("Shanks Babystep Giantstep Algorithm");
		grpBabyStepGiant.setLayout(new GridLayout(1, false));

		compositeDescription = new Composite(grpBabyStepGiant, SWT.NONE);
		compositeDescription.setBackground(SWTResourceManager.getColor(240, 240, 240));
		compositeDescription.setLayout(new GridLayout(1, false));
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		styledText = new StyledText(compositeDescription, SWT.NONE);
		styledText.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		styledText.setText(Constants.msgIntro);
		styledText.setBackground(SWTResourceManager.getColor(240, 240, 240));
		styledText.setEditable(false);
		GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_styledText.heightHint = 51;
		styledText.setLayoutData(gd_styledText);

		Group grpEnterTheParameters = new Group(grpBabyStepGiant, SWT.NONE);
		grpEnterTheParameters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpEnterTheParameters.setText(Constants.msgStep1);
		grpEnterTheParameters.setLayout(new GridLayout(4, false));

		Label lblEnterACyclicGroup = new Label(grpEnterTheParameters, SWT.NONE);
		lblEnterACyclicGroup.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblEnterACyclicGroup.setText(Constants.msgEnterACyclicGroup);

		Label lblEnterAGenerator = new Label(grpEnterTheParameters, SWT.NONE);
		lblEnterAGenerator.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblEnterAGenerator.setText(Constants.msgEnterAGenerator);

		Label lblEnterEGroupelement = new Label(grpEnterTheParameters, SWT.NONE);
		lblEnterEGroupelement.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblEnterEGroupelement.setText(Constants.msgEnterEGroupelement);
		new Label(grpEnterTheParameters, SWT.NONE);

		textGroup = new Text(grpEnterTheParameters, SWT.BORDER);
		textGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textGroup.addVerifyListener(vl_numbers);

		textGenerator = new Text(grpEnterTheParameters, SWT.BORDER);
		textGenerator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textGenerator.addVerifyListener(vl_numbers);

		textGroupElement = new Text(grpEnterTheParameters, SWT.BORDER);
		textGroupElement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textGroupElement.addVerifyListener(vl_numbers);

		btnContinueToStep2 = new Button(grpEnterTheParameters, SWT.NONE);
		btnContinueToStep2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!textGroup.getText().isEmpty() && !textGenerator.getText().isEmpty() && !textGroupElement.getText().isEmpty()) {
					BigInteger textGroupValue = new BigInteger(textGroup.getText());
					BigInteger textGeneratorValue = new BigInteger(textGenerator.getText());
					BigInteger textGroupElementValue = new BigInteger(textGroupElement.getText());

					if (textGroupValue.compareTo(Constants.MAX_INTEGER_BI) <= 0 && textGeneratorValue.compareTo(Constants.MAX_INTEGER_BI) <= 0 && textGroupElementValue.compareTo(Constants.MAX_INTEGER_BI) <= 0) {

						compute();

					} else {
						Parameter parameter = new Parameter(e.display.getActiveShell(), textGroupValue.toString(), textGeneratorValue.toString(), textGroupElementValue.toString());

						int rc = parameter.open();

						if (rc == 0) {
							textGroup.setText(parameter.getCyclicGroupValue());
							textGenerator.setText(parameter.getGeneratorValue());
							textGroupElement.setText(parameter.getGroupElementValue());

							compute();
						}
					}
				}
			}

			public void compute() {
				BigInteger tmp = new BigInteger(textGroup.getText());
				if (!tmp.isProbablePrime(10000)) {
					tmp = tmp.nextProbablePrime();
					textGroup.setText(tmp.toString());
				}

				BigInteger group = new BigInteger(textGroup.getText());
				BigInteger generator = new BigInteger(textGenerator.getText());
				BigInteger groupElement = new BigInteger(textGroupElement.getText());

				babyStepGiantStep = new BabystepGiantstep(group, generator, groupElement);
				textOrder.setText(babyStepGiantStep.getN().toString());
				textM.setText(babyStepGiantStep.getM().toString());
				textInv.setText(babyStepGiantStep.getMultInv().toString());

				textGroup.setEnabled(false);
				textGenerator.setEnabled(false);
				textGroupElement.setEnabled(false);
				btnContinueToStep2.setEnabled(false);
				btnContinueToStep3.setEnabled(true);
				
				styledText.setText(Constants.msgButton1);
			}

		});
		GridData gd_btnContinueToStep2 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnContinueToStep2.widthHint = 120;
		btnContinueToStep2.setLayoutData(gd_btnContinueToStep2);
		btnContinueToStep2.setText(Constants.btnStep2);

		Group grpCalculateTheGroupoder = new Group(grpBabyStepGiant, SWT.NONE);
		grpCalculateTheGroupoder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpCalculateTheGroupoder.setSize(790, 96);
		grpCalculateTheGroupoder.setText(Constants.msgStep2);
		grpCalculateTheGroupoder.setLayout(new GridLayout(7, false));

		lblCalculateTheGrouporder = new Label(grpCalculateTheGroupoder, SWT.NONE);
		lblCalculateTheGrouporder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblCalculateTheGrouporder.setText(Constants.msgCalculateTheGrouporder);

		lblCalculateTheCeiling = new Label(grpCalculateTheGroupoder, SWT.NONE);
		lblCalculateTheCeiling.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblCalculateTheCeiling.setText(Constants.msgCalculateTheCeiling);

		lblCalculateTheInverse = new Label(grpCalculateTheGroupoder, SWT.NONE);
		lblCalculateTheInverse.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblCalculateTheInverse.setText(Constants.msgCalculateTheInverse);
		new Label(grpCalculateTheGroupoder, SWT.NONE);

		Label lblOrdg = new Label(grpCalculateTheGroupoder, SWT.NONE);
		lblOrdg.setText("Ord(G) = ");

		textOrder = new Text(grpCalculateTheGroupoder, SWT.BORDER | SWT.READ_ONLY);
		textOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textOrder.addVerifyListener(vl_numbers);

		Label lblM = new Label(grpCalculateTheGroupoder, SWT.NONE);
		lblM.setText("m =");

		textM = new Text(grpCalculateTheGroupoder, SWT.BORDER | SWT.READ_ONLY);
		textM.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblInv = new Label(grpCalculateTheGroupoder, SWT.NONE);
		lblInv.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInv.setText("Inv =");

		textInv = new Text(grpCalculateTheGroupoder, SWT.BORDER | SWT.READ_ONLY);
		textInv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnContinueToStep3 = new Button(grpCalculateTheGroupoder, SWT.NONE);
		btnContinueToStep3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				babyStepGiantStep.computeBabySteps();

				HashMap<BigInteger, BigInteger> babyStepMenge = babyStepGiantStep.getBabystepMenge();

				Set<Entry<BigInteger, BigInteger>> set = babyStepMenge.entrySet();

				BigInteger i = BigInteger.ZERO;
				BigInteger limit = new BigInteger(textM.getText());

				while (i.compareTo(limit) < 0) {
					for (Entry<BigInteger, BigInteger> entry : set) {
						if (entry.getValue().equals(new BigInteger(String.valueOf(i)))) {
							TableItem tableItem = new TableItem(tableBS, SWT.NONE);
							tableItem.setText(0, entry.getValue().toString());
							tableItem.setText(1, entry.getKey().toString());

							if (i.equals(BigInteger.ZERO)) {
								StringBuilder sb = new StringBuilder("r = " + i + " => ");
								BigInteger tmp = new BigInteger(textGroupElement.getText());
								sb.append(tmp + " ");
								sb.append(Constants.uCongruence + " " + entry.getKey().toString() + " mod " + textGroup.getText());

								tableItem.setText(2, sb.toString());
							} else {
								StringBuilder sb = new StringBuilder("r = " + i + " => ");
								sb.append(babyStepGiantStep.getMultInv() + " * " + tableBS.getItem(i.intValue() - 1).getText(1) + " = ");
								BigInteger tmp = babyStepGiantStep.getMultInv().multiply(new BigInteger(tableBS.getItem(i.intValue() - 1).getText(1)));
								sb.append(tmp + " ");
								sb.append(Constants.uCongruence + " " + entry.getKey().toString() + " mod " + textGroup.getText());

								tableItem.setText(2, sb.toString());
							}
							break;
						}
					}
					i = i.add(BigInteger.ONE);
				}

				tableBS.setSelection(tableBS.getItemCount() - 1);

				btnContinueToStep3.setEnabled(false);
				if (babyStepGiantStep.getX() == null) {
					btnContinueToStep4.setEnabled(true);
					styledText.setText(Constants.msgButton2False);
				} else {
					btnContinueToStep4.setEnabled(false);
					btnResult.setEnabled(true);
					styledText.setText(Constants.msgButton2True);
				}
			}
		});
		btnContinueToStep3.setEnabled(false);
		GridData gd_btnContinueToStep3 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnContinueToStep3.widthHint = 120;
		btnContinueToStep3.setLayoutData(gd_btnContinueToStep3);
		btnContinueToStep3.setText(Constants.btnStep3);

		Group grpCalculateTheBaby = new Group(grpBabyStepGiant, SWT.NONE);
		GridData gd_grpCalculateTheBaby = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpCalculateTheBaby.minimumHeight = 150;
		grpCalculateTheBaby.setLayoutData(gd_grpCalculateTheBaby);
		grpCalculateTheBaby.setText(Constants.msgStep3);
		grpCalculateTheBaby.setLayout(new GridLayout(3, false));

		tableBS = new Table(grpCalculateTheBaby, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tableBS = new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1);
		gd_tableBS.minimumHeight = 80;
		tableBS.setLayoutData(gd_tableBS);
		tableBS.setHeaderVisible(true);
		tableBS.setLinesVisible(true);

		tblclmnR = new TableColumn(tableBS, SWT.NONE);
		tblclmnR.setWidth(120);
		tblclmnR.setText("Rest (r)");

		tblclmnBabySteps = new TableColumn(tableBS, SWT.NONE);
		tblclmnBabySteps.setWidth(120);
		tblclmnBabySteps.setText("Babysteps");

		tblclmnCommentBS = new TableColumn(tableBS, SWT.NONE);
		tblclmnCommentBS.setWidth(400);
		tblclmnCommentBS.setText("Comment");
		new Label(grpCalculateTheBaby, SWT.NONE);
		new Label(grpCalculateTheBaby, SWT.NONE);

		btnContinueToStep4 = new Button(grpCalculateTheBaby, SWT.NONE);
		btnContinueToStep4.setEnabled(false);
		GridData gd_btnContinueToStep4 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnContinueToStep4.widthHint = 120;
		btnContinueToStep4.setLayoutData(gd_btnContinueToStep4);
		btnContinueToStep4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (babyStepGiantStep.getX() == null) {
					babyStepGiantStep.computeGiantSteps();
					ArrayList<BigInteger> giantStepMenge = babyStepGiantStep.getGiantstepMenge();
					BigInteger q = BigInteger.ONE;
					for (BigInteger elem : giantStepMenge) {
						TableItem tableItem = new TableItem(tableGS, SWT.NONE);
						tableItem.setText(0, q.toString());
						tableItem.setText(1, elem.toString());

						if (tableGS.getItemCount() == 1) {
							StringBuilder sb = new StringBuilder("q = " + q + " => ");
							sb.append(textM.getText() + "^" + q + " " + Constants.uCongruence + " ");
							sb.append(elem.toString() + " mod " + textGroup.getText());
							tableItem.setText(2, sb.toString());
						} else {
							StringBuilder sb = new StringBuilder("q = " + q + " => ");
							sb.append(textM.getText() + "^" + q + " = ");
							sb.append(textM.getText() + " * " + tableGS.getItem(tableGS.getItemCount() - 2).getText(1) + " = ");

							BigInteger tmp = new BigInteger(textM.getText());
							tmp = tmp.multiply(new BigInteger(tableGS.getItem(tableGS.getItemCount() - 2).getText(1)));
							sb.append(tmp + " " + Constants.uCongruence + " " + elem.toString() + " mod " + textGroup.getText());

							tableItem.setText(2, sb.toString());
						}

						q = q.add(BigInteger.ONE);
					}
					tableGS.setSelection(tableGS.getItemCount() - 1);
				} else {
					textResult.setText(Constants.msgResultNoGiantsteps);
				}
				btnContinueToStep4.setEnabled(false);
				btnResult.setEnabled(true);

				styledText.setText(Constants.msgButton3);

			}
		});
		btnContinueToStep4.setText(Constants.btnStep4);

		Group grpCalculateTheGiant = new Group(grpBabyStepGiant, SWT.NONE);
		GridData gd_grpCalculateTheGiant = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpCalculateTheGiant.minimumHeight = 150;
		grpCalculateTheGiant.setLayoutData(gd_grpCalculateTheGiant);
		grpCalculateTheGiant.setText(Constants.msgStep4);
		grpCalculateTheGiant.setLayout(new GridLayout(4, false));

		tableGS = new Table(grpCalculateTheGiant, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tableGS = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gd_tableGS.minimumHeight = 80;
		tableGS.setLayoutData(gd_tableGS);
		tableGS.setHeaderVisible(true);
		tableGS.setLinesVisible(true);

		tblclmnQ = new TableColumn(tableGS, SWT.NONE);
		tblclmnQ.setWidth(120);
		tblclmnQ.setText("Quotient (q)");

		tblclmnGiantSteps = new TableColumn(tableGS, SWT.NONE);
		tblclmnGiantSteps.setWidth(120);
		tblclmnGiantSteps.setText("Giantsteps");

		tblclmnCommentGS = new TableColumn(tableGS, SWT.NONE);
		tblclmnCommentGS.setWidth(400);
		tblclmnCommentGS.setText("Comment");
		new Label(grpCalculateTheGiant, SWT.NONE);
		new Label(grpCalculateTheGiant, SWT.NONE);

		btnResult = new Button(grpCalculateTheGiant, SWT.NONE);
		btnResult.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (babyStepGiantStep.getX() != null) {
					if (babyStepGiantStep.getR() != null) {
						tableBS.setSelection(babyStepGiantStep.getR().intValue());
						tableBS.getItem(babyStepGiantStep.getR().intValue()).setForeground(0, Constants.GREEN);
						tableBS.getItem(babyStepGiantStep.getR().intValue()).setForeground(1, Constants.GREEN);
						tableBS.getItem(babyStepGiantStep.getR().intValue()).setForeground(2, Constants.GREEN);
						tableGS.setSelection(babyStepGiantStep.getQ().intValue() - 1);
						tableGS.getItem(babyStepGiantStep.getQ().intValue() - 1).setForeground(0, Constants.BLUE);
						tableGS.getItem(babyStepGiantStep.getQ().intValue() - 1).setForeground(1, Constants.BLUE);
						tableGS.getItem(babyStepGiantStep.getQ().intValue() - 1).setForeground(2, Constants.BLUE);
						StringBuilder result = new StringBuilder("x = q * m + r = " + babyStepGiantStep.getQ() + " * " + textM.getText() + " + " + babyStepGiantStep.getR() + " = "
								+ babyStepGiantStep.getX().toString() + " mod " + textGroup.getText());
						textResult.setText(result.toString());
						StyleRange parameterA = new StyleRange();
						parameterA.start = 16;
						parameterA.length = babyStepGiantStep.getQ().toString().length();
						parameterA.foreground = Constants.BLUE;
						parameterA.fontStyle = SWT.BOLD;
						textResult.setStyleRange(parameterA);
						StyleRange parameterB = new StyleRange();
						parameterB.start = 16 + babyStepGiantStep.getQ().toString().length() + 3 + textM.getText().length() + 3;
						parameterB.length = babyStepGiantStep.getR().toString().length();
						parameterB.foreground = Constants.GREEN;
						parameterB.fontStyle = SWT.BOLD;
						textResult.setStyleRange(parameterB);
						StyleRange parameterC = new StyleRange();
						parameterC.start = 16 + babyStepGiantStep.getQ().toString().length() + 3 + textM.getText().length() + 3 + babyStepGiantStep.getR().toString().length() + 3;
						parameterC.length = babyStepGiantStep.getX().toString().length();
						parameterC.foreground = Constants.RED;
						parameterC.fontStyle = SWT.BOLD;
						textResult.setStyleRange(parameterC);
						styledText.setText(Constants.msgButton4True);
					} else {
						tableBS.setSelection(babyStepGiantStep.getX().intValue());
						tableBS.getItem(babyStepGiantStep.getX().intValue()).setForeground(0, Constants.GREEN);
						tableBS.getItem(babyStepGiantStep.getX().intValue()).setForeground(1, Constants.GREEN);
						tableBS.getItem(babyStepGiantStep.getX().intValue()).setForeground(2, Constants.GREEN);

						StringBuilder result = new StringBuilder("x = r = " + babyStepGiantStep.getX() + " = " + " mod " + textGroup.getText());
						textResult.setText(result.toString());
						styledText.setText(Constants.msgButton4OnlyBs);
					}

				} else {
					textResult.setText(Constants.msgResultNoSolution);
					StyleRange parameterA = new StyleRange();
					parameterA.start = 0;
					parameterA.length = textResult.getText().length();
					parameterA.foreground = Constants.RED;
					parameterA.fontStyle = SWT.BOLD;
					textResult.setStyleRange(parameterA);

					styledText.setText(Constants.msgButton4False);
				}

				btnResult.setEnabled(false);

			}
		});
		GridData gd_btnResult = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnResult.widthHint = 120;
		btnResult.setLayoutData(gd_btnResult);
		btnResult.setEnabled(false);
		btnResult.setText(Constants.btnResult);

		btnClearTheTables = new Button(grpCalculateTheGiant, SWT.NONE);
		btnClearTheTables.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textResult.setText("");
				textResult.setBackground(Constants.LIGHTGREY);

				textGenerator.setText("");
				textGroup.setText("");
				textGroupElement.setText("");
				textM.setText("");
				textOrder.setText("");
				textInv.setText("");

				tableBS.removeAll();
				tableGS.removeAll();

				btnContinueToStep2.setEnabled(true);
				btnContinueToStep3.setEnabled(false);
				btnContinueToStep4.setEnabled(false);
				btnResult.setEnabled(false);

				textGenerator.setEnabled(true);
				textGroup.setEnabled(true);
				textGroupElement.setEnabled(true);

				styledText.setText(Constants.msgIntro);

			}
		});
		GridData gd_btnClearTheTables = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnClearTheTables.widthHint = 120;
		btnClearTheTables.setLayoutData(gd_btnClearTheTables);
		btnClearTheTables.setText(Constants.btnClear);

		Group grpDescription = new Group(grpBabyStepGiant, SWT.NONE);
		grpDescription.setLayout(new GridLayout(1, false));
		grpDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpDescription.setText("Result");

		textResult = new StyledText(grpDescription, SWT.READ_ONLY | SWT.WRAP);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_textDescription.heightHint = 30;
		textResult.setBackground(Constants.LIGHTGREY);
		textResult.setLayoutData(gd_textDescription);

	}

	@Override
	public void setFocus() {
		textGroup.setFocus();

	}
}