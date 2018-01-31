/**
 * This Class is used to create a View of SPA, which introduces the basic principle of SPA and visualizes the process
 * and countermeasures of SPA attack.
 *
 * @author Biqiang Jiang
 * @version 1.0, 01/09/09
 * @since JDK1.5.7
 */

package org.jcryptool.visual.sidechannelattack.spa.views;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.algorithm.SquareandMultiply;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.sidechannelattack.SPAPlugIn;

public class SPAView extends ViewPart implements Constants {
	
	public SPAView() {
		
	}

	private Table recordTable;
	private Text mod;
	private Combo pSelectCombo;
	private Combo qSelectCombo;
	private Text result;
	private Combo exponent;
	private Text basis;
	private int counter = 1;
	private int exp_selected = 0;
	// this is the ....
	private int q_selected = 0;
	private int p_selected = 0;
	private StyledText rsaProcessText;

	private Combo modeSelection;

	// declare a object of SquareandMultiply, which is used to process the
	// "square and multiply" algorithm
	SquareandMultiply squareandMultiply = new SquareandMultiply();

	// the rang of prime number can be selected as exponent
	final int[] primeDataExponent = { 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179,
			181, 191, 193, 197, 199 };

	private Composite mainContent;
	private Composite parameterOfRSAGroup;
	private Composite informationGroup;

	private Composite visualizedGroup;
	private Composite powerTraceVisualizationGroup;
	private Text exponentBinary;
	private Composite parent;
	private Composite descriptionComposite;
	private Text description;
	private Composite content;

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;
		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
				SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		content = new Composite(scrolledComposite, SWT.NONE);
		content.setLayout(new GridLayout());

		descriptionComposite = new Composite(content, SWT.NONE);
		descriptionComposite.setLayout(new GridLayout());
		descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		descriptionComposite.setBackground(ColorService.WHITE);

		Label lblHeader = new Label(descriptionComposite, SWT.NONE);
		lblHeader.setFont(FontService.getHeaderFont());
		lblHeader.setBackground(ColorService.WHITE);
		lblHeader.setText(MAIN_GROUP_TITLE);

		description = new Text(descriptionComposite, SWT.WRAP);
		description.setText(
				Messages.SPAView_description);
		GridData gd_description = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_description.widthHint = 800;
		description.setLayoutData(gd_description);
		description.setBackground(ColorService.WHITE);
		description.setEditable(false);

		mainContent = new Composite(content, SWT.NONE);
		mainContent.setLayout(new GridLayout(2, false));
		mainContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		parameterOfRSAGroup = new Composite(mainContent, SWT.NONE);
		parameterOfRSAGroup.setLayout(new GridLayout(2, false));
		parameterOfRSAGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));

		visualizedGroup = new Composite(mainContent, SWT.NONE);
		visualizedGroup.setLayout(new GridLayout());
		visualizedGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		informationGroup = new Composite(mainContent, SWT.NONE);
		informationGroup.setLayout(new GridLayout());
		informationGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		AddParameterOfRsaGroupContent();
		AddVisualizedGroupContent();
		AddRsaAlgorithmGroupContent();
		
		// mainContent.setSize(mainContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// content.setSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setContent(content);
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(),
				"org.jcryptool.visual.sidechannelattack.spa.spaview"); //$NON-NLS-1$
	}

	/**
	 * Creates the text with the description on the bottom of the view.
	 */
	private void AddRsaAlgorithmGroupContent() {
		rsaProcessText = new StyledText(informationGroup, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData gd_rsaProcessText = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_rsaProcessText.widthHint = 760;
		gd_rsaProcessText.heightHint = 200;
		rsaProcessText.setLayoutData(gd_rsaProcessText);
		rsaProcessText.setEditable(false);
		rsaProcessText.setText(INFORMATION_SAM_TEXT);
	}

	/**
	 * Creates the left composite with the input, paramters, modulus and result.
	 */
	private void AddParameterOfRsaGroupContent() {

		CLabel lblParameterOfRSAGroup = new CLabel(parameterOfRSAGroup, SWT.NONE);
		lblParameterOfRSAGroup.setText(Constants.INPUT);
		lblParameterOfRSAGroup.setFont(FontService.getSmallBoldFont());
		lblParameterOfRSAGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		final Label enterTheBasisLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		enterTheBasisLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		enterTheBasisLabel.setText(BASIS_LABEL);

		// basis is used to save the ciphertext c in RSA: R = c^d mod n
		basis = new Text(parameterOfRSAGroup, SWT.BORDER);
		basis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		basis.setTextLimit(9);
		basis.setToolTipText(TOOL_TIP_TEXT_BASIS);

		// the verifylistener on basis is used to verify the correctness of
		// input
		basis.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				// Pattern pattern = Pattern.compile("[0-9]\\d*");
				Pattern pattern = Pattern.compile("^[0-9]{1,9}$"); //$NON-NLS-1$
				Matcher matcher = pattern.matcher(e.text);

				if (matcher.matches())
					e.doit = true;
				else if (e.text.length() > 0)
					e.doit = false;
				else
					e.doit = true;
			}
		});

		// it's a cue label.
		final Label exponentLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		exponentLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		exponentLabel.setText(EXPONENT_LABEL);

		// basis is used to save the exponent d in RSA: R = c^d mod n
		exponent = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		exponent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		exponent.setToolTipText(TOOL_TIP_TEXT_EXPONENT);

		Label exponentBinaryLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		exponentBinaryLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		exponentBinaryLabel.setText("="); //$NON-NLS-1$

		exponentBinary = new Text(parameterOfRSAGroup, SWT.BORDER);
		exponentBinary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		exponentBinary.setToolTipText(Constants.TOOL_TIP_TEXT_EXPONENT_BINARY);
		exponentBinary.setEditable(false);

		CLabel parameterLabel = new CLabel(parameterOfRSAGroup, SWT.NONE);
		GridData gd_parameterLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_parameterLabel.verticalIndent = 30;
		parameterLabel.setLayoutData(gd_parameterLabel);
		parameterLabel.setText(Constants.PARAMETER);
		parameterLabel.setFont(FontService.getSmallBoldFont());

		// two cue labels
		final Label chooseQLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		chooseQLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		chooseQLabel.setText(CHOOSE_Q_LABEL);

		qSelectCombo = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		qSelectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		qSelectCombo.setToolTipText(TOOL_TIP_TEXT_Q_SELECTION);
		// the selectionlistener is used to determine which prime number has
		// been chosen
		qSelectCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				q_selected = Integer.parseInt(qSelectCombo.getItem(qSelectCombo.getSelectionIndex()));

				for (int i = 0; i < pSelectCombo.getItemCount(); i++) {
					if (pSelectCombo.getItem(i).equals(String.valueOf(q_selected))) {

						pSelectCombo.remove(i);
						break;
					}
				}

				if (p_selected != 0 && q_selected != 0) {
					mod.setText(String.valueOf((p_selected) * (q_selected)));
				}
			}
		});

		final Label choosePLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		choosePLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		choosePLabel.setText(CHOOSE_P_LABEL);

		// Q and P can be selected in qselectcombo and pselectcombo
		pSelectCombo = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		pSelectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		pSelectCombo.setToolTipText(TOOL_TIP_TEXT_P_SELECTION);

		// the selectionlistener is used to determine which prime number has
		// been chosen
		pSelectCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				p_selected = Integer.parseInt(pSelectCombo.getItem(pSelectCombo.getSelectionIndex()));
				for (int i = 0; i < qSelectCombo.getItemCount(); i++) {

					if (qSelectCombo.getItem(i).equals(String.valueOf(p_selected))) {
						qSelectCombo.remove(i);
						break;
					}
				}

				if (p_selected != 0 && q_selected != 0) {
					mod.setText(String.valueOf((p_selected) * (q_selected)));
				}
			}
		});

		final Label moduleLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		moduleLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		moduleLabel.setText(MODULE_LABEL_TEXT);

		// mod is used here to save the module n in R = c^d mod n
		mod = new Text(parameterOfRSAGroup, SWT.BORDER);
		mod.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		mod.setToolTipText(TOOL_TIP_TEXT_MODULE_N);
		mod.setEditable(false);

		CLabel selectModeLabel = new CLabel(parameterOfRSAGroup, SWT.NONE);
		GridData gd_selectModeLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_selectModeLabel.verticalIndent = 30;
		selectModeLabel.setLayoutData(gd_selectModeLabel);
		selectModeLabel.setText(Constants.MODE);
		selectModeLabel.setFont(FontService.getSmallBoldFont());

		modeSelection = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		modeSelection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		modeSelection.add(SAM_MODE);
		modeSelection.add(SAMA_MODE);
		modeSelection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rsaProcessText
						.setText(modeSelection.getSelectionIndex() == 0 ? INFORMATION_SAM_TEXT : INFORMATION_SAMA_TEXT);
				super.widgetSelected(e);
			}
		});
		modeSelection.select(0);

		// the executeButton is used here to start the process of
		// "Square and Multiply" Algorithm
		final Button executeButton = new Button(parameterOfRSAGroup, SWT.NONE);
		executeButton.setEnabled(false);
		executeButton.setText(EXECUTION_BUTTON_TEXT);

		// executeButton is used to start the process of "square and multiply"
		// algorithm
		executeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				Execute();
			}
		});

		mod.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {

				if (!basis.getText().equals("") && (exp_selected != 0) && (!mod.getText().equals(""))) { //$NON-NLS-1$ //$NON-NLS-2$

					executeButton.setEnabled(true);
				}
			}
		});

		basis.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {

				if (!basis.getText().equals("") && (exp_selected != 0) && (!mod.getText().equals(""))) { //$NON-NLS-1$ //$NON-NLS-2$
					executeButton.setEnabled(true);
				}
			}
		});

		// the selectionlistener here is used to determine which prime number
		// has been chosen as exponent
		exponent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				exp_selected = primeDataExponent[exponent.getSelectionIndex()];
				exponentBinary.setText(Integer.toBinaryString(Integer.parseInt(exponent.getText())));

				if (!basis.getText().equals("") && (exp_selected != 0) && (!mod.getText().equals(""))) { //$NON-NLS-1$ //$NON-NLS-2$
					executeButton.setEnabled(true);
				}

			}
		});

		CLabel resultLabel = new CLabel(parameterOfRSAGroup, SWT.NONE);
		GridData gd_resultLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_resultLabel.verticalIndent = 30;
		resultLabel.setLayoutData(gd_resultLabel);
		resultLabel.setText(Constants.RESULT);
		resultLabel.setFont(FontService.getSmallBoldFont());

		// it's a cue label
		final Label resultDescLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		resultDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		resultDescLabel.setText(TOOL_TIP_TEXT_RESULT_LABEL);

		// result is used to save the result R in R = c^d mod n
		result = new Text(parameterOfRSAGroup, SWT.BORDER);
		result.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		result.setToolTipText(TOOL_TIP_TEXT_RESULT);
		result.setEditable(false);

		// clearButton is used to clear the table
		final Button clearButton = new Button(parameterOfRSAGroup, SWT.NONE);
		GridData gd_clearButton = new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 2, 1);
		gd_clearButton.verticalIndent = 30;
		clearButton.setLayoutData(gd_clearButton);
		clearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				recordTable.removeAll();
				exponentBinary.setText(""); //$NON-NLS-1$
				basis.setText(""); //$NON-NLS-1$
				exponent.setText(""); //$NON-NLS-1$
				mod.setText(""); //$NON-NLS-1$
				exp_selected = 0;
				result.setText(""); //$NON-NLS-1$
				exponent.deselectAll();
				pSelectCombo.deselectAll();
				qSelectCombo.deselectAll();
				p_selected = 0;
				q_selected = 0;
				modeSelection.select(0);
				executeButton.setEnabled(false);
				basis.setFocus();
				for (Control ctrl : powerTraceVisualizationGroup.getChildren())
					ctrl.dispose();
				rsaProcessText.setText(INFORMATION_SAM_TEXT);
			}
		});
		clearButton.setToolTipText(TOOL_TIP_TEXT_CLEARBUTTON);
		clearButton.setText(CLEAR_BUTTON_TEXT);

		int dataLengthExponent = primeDataExponent.length;
		int dataElementIndexExponent = 0;
		while (dataLengthExponent > 0) {
			exponent.add(String.valueOf(primeDataExponent[dataElementIndexExponent]));
			dataLengthExponent--;
			dataElementIndexExponent++;
		}

		// the range in which p and q can be selected
		final int[] primeData = { 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181,
				191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293 };

		int data_length = primeData.length;
		int data_element_index = 0;
		while (data_length > 0) {

			qSelectCombo.add(String.valueOf(primeData[data_element_index]));
			data_length--;
			data_element_index++;

		}

		data_length = primeData.length;
		data_element_index = 0;
		while (data_length > 0) {

			pSelectCombo.add(String.valueOf(primeData[data_element_index]));
			data_length--;
			data_element_index++;

		}
	}

	/**
	 * Fill the table with values and print the pictures below the table.
	 */
	protected void Execute() {

		for (Control ctrl : powerTraceVisualizationGroup.getChildren())
			ctrl.dispose();
		recordTable.removeAll();

		counter = 1;

		boolean isSaMmode = modeSelection.getSelectionIndex() == 0;

		int[] tempresult = new int[Integer.toBinaryString(Integer.parseInt(exponent.getText())).length()];

		String exp_in_binary = exponentBinary.getText();
		int exp_in_binar_length = exp_in_binary.length();

		int count = 0;
		long res = 1;

		// declare some initial parameters of
		// "square and multiply always" in table
		final TableItem initialTableItemBasis = new TableItem(recordTable, SWT.BORDER);

		initialTableItemBasis.setText(0, INPUT_BASIS_ENG);

		String text = RES_AFTER_SQUARE_ENG;
		initialTableItemBasis.setText(1, text);
		text = isSaMmode ? RES_AFTER_MUL_BIT_1_ENG : RES_AFTER_MUL;
		initialTableItemBasis.setText(2, text);

		if (isSaMmode) {
			final TableItem initialTableItemBasis2 = new TableItem(recordTable, SWT.BORDER);
			initialTableItemBasis2.setText(2, RES_AFTER_MUL_BIT_0_ENG);
		}

		final TableItem initialTableItemProcess = new TableItem(recordTable, SWT.BORDER);
		initialTableItemProcess.setText(0, INITIAL_ITEM_TEXT_3_IN_TABLE);

		int achse_x = 0;

		final Composite powerTraceComposite = new Composite(powerTraceVisualizationGroup, SWT.NONE);

		final Label achseY_squareLabel = new Label(powerTraceComposite, SWT.NONE);
		achseY_squareLabel.setBounds(achse_x, 10, 85, 110);
		achseY_squareLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADRESSE_Y_ACHSE_ENG).createImage());

		achse_x = achse_x + 85;

		while (exp_in_binar_length > 0) {
			final TableItem tempTableItems = new TableItem(recordTable, SWT.BORDER);
			tempTableItems.setText(0, "  " + counter + HIGHEST_BIT_ENG + exp_in_binary.charAt(count)); //$NON-NLS-1$

			long tempres_byExp = res;

			text = NLS.bind(S_RES, INDICES[counter]);

			res = (long) Math.pow(res, 2) % Integer.parseInt(mod.getText());
			tempTableItems.setText(1, text + tempres_byExp + HOCH_2_MOD_ENG + mod.getText() + " = " + res); //$NON-NLS-1$

			long tempres = res;

			text = NLS.bind(R_RES, INDICES[counter + 1]);

			if (exp_in_binary.charAt(count) == '1') {
				long tempres_byMul = res;
				res = (res * Integer.parseInt(basis.getText()) % Integer.parseInt(mod.getText()));
				tempTableItems.setText(2,
						text + tempres_byMul + " * " + basis.getText() + " mod " + mod.getText() + " = " + res); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				final Label powertrace_sqMulLabel = new Label(powerTraceComposite, SWT.NONE);
				powertrace_sqMulLabel.setBounds(achse_x, 10, 85, 110);
				powertrace_sqMulLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADDRESSE_SQMUL_ENG).createImage());
				achse_x = achse_x + 85;
			} else {
				if (!isSaMmode) {
					long tempres_byMul = res;
					res = (res * Integer.parseInt(basis.getText()) % Integer.parseInt(mod.getText()));
					tempTableItems.setText(2,
							text + tempres_byMul + " * " + basis.getText() + " mod " + mod.getText() + " = " + res); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					res = tempres;
				}

				final Label powertrace_squareLabel = new Label(powerTraceComposite, SWT.PUSH);

				text = isSaMmode ? IMGADDRESSE_SQ_ENG : IMGADDRESSE_SQMUL_0_ENG;

				powertrace_squareLabel.setBounds(achse_x, 10, (isSaMmode ? 42 : 85), 110);
				powertrace_squareLabel.setImage(SPAPlugIn.getImageDescriptor(text).createImage());
				achse_x = achse_x + (isSaMmode ? 42 : 85);
			}
			count++;
			counter++;
			exp_in_binar_length--;
		}

		final Label achseX_squareLabel = new Label(powerTraceComposite, SWT.NONE);
		achseX_squareLabel.setBounds(achse_x, 10, 85, 110);
		achseX_squareLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADDRESSE_X_ACHSE_ENG).createImage());

		final TableItem outPutTableItems = new TableItem(recordTable, SWT.BORDER);
		outPutTableItems.setText(0, OUTPUT_TABLE_ITEM_TEXT);

		final TableItem finalTableItems = new TableItem(recordTable, SWT.BORDER);

		finalTableItems.setText(0, "  " + FINAL_RESULT_ENG + res); //$NON-NLS-1$
		result.setText("" + res); //$NON-NLS-1$

		res = squareandMultiply.sqmulExcution(Integer.parseInt(basis.getText()), Integer.parseInt(exponent.getText()),
				Integer.parseInt(mod.getText()));

		String str = "(" + tempresult[--count]; //$NON-NLS-1$
		while (count != 0) {
			str = str + "*" + tempresult[--count]; //$NON-NLS-1$
		}

		for (int i = 0, n = recordTable.getColumnCount(); i < n; i++) {
			recordTable.getColumn(i).pack();
		}
		parent.layout(new Control[] {powerTraceComposite});
	}

	/**
	 * Creates the area with the table.
	 */
	private void AddVisualizedGroupContent() {

		recordTable = new Table(visualizedGroup, SWT.BORDER);
		GridData gd_recordTable = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_recordTable.minimumHeight = 200;
		gd_recordTable.minimumWidth= 760;
		recordTable.setLayoutData(gd_recordTable);
		recordTable.setLinesVisible(true);
		recordTable.setHeaderVisible(true);

		// roundCol is the first low of recorder table in which some initial
		// information will be saved
		final TableColumn roundCol = new TableColumn(recordTable, SWT.NONE);
		roundCol.setWidth(180);
		roundCol.setText(FIRST_COLUMN_IN_TABLE);

		// resSquareCol is the second low of recorder table in which the result
		// after squaring will be saved
		final TableColumn resSquareCol = new TableColumn(recordTable, SWT.NONE);
		resSquareCol.setWidth(210);
		resSquareCol.setText(SECOND_COLUMN_IN_TABLE);

		// resMultiCol is the last low in which the result after multiplication
		// will be saved
		final TableColumn resMultiCol = new TableColumn(recordTable, SWT.NONE);
		resMultiCol.setWidth(255);
		resMultiCol.setText(THIRD_COLUMN_IN_TABLE);

		new TableCursor(recordTable, SWT.NONE);
		
		powerTraceVisualizationGroup = new Composite(visualizedGroup, SWT.NONE);
		GridData gd_powerTraceVisualizationGroup = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_powerTraceVisualizationGroup.heightHint = 120;
		powerTraceVisualizationGroup.setLayoutData(gd_powerTraceVisualizationGroup);
		powerTraceVisualizationGroup.setLayout(new FormLayout());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		content.setFocus();
	}

	public void reset() {
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
		parent.layout();

	}
	
}
