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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
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
	final int[] primeDataExponent = { 101, 103, 107, 109, 113, 127, 131, 137,
			139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199 };

	private Composite mainContent;
	private Composite parameterOfRSAGroup;
	private Composite informationGroup;

	private Composite visualizedGroup;
	private Composite powerTraceVisualizationGroup;
	private Text exponentBinary;

	public void createPartControl(final Composite parent) {

		final ScrolledComposite scrolledComposite = new ScrolledComposite(
				parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);

		Composite content = new Composite(scrolledComposite, SWT.NONE);
		content.setLayout(new RowLayout(SWT.VERTICAL));

		Label lblHeader = new Label(content, SWT.NONE);
		lblHeader.setFont(FontService.getHeaderFont());
		lblHeader.setText(MAIN_GROUP_TITLE);

		mainContent = new Composite(content, SWT.NONE);
		mainContent.setLayout(new GridLayout(2, false));

		parameterOfRSAGroup = new Composite(mainContent, SWT.NONE);
		parameterOfRSAGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 2));

		visualizedGroup = new Composite(mainContent, SWT.NONE);
		visualizedGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		
		informationGroup = new Composite(mainContent, SWT.NONE);
		informationGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		
		
		AddParameterOfRsaGroupContent();
		AddRsaAlgorithmGroupContent(parent);
		AddVisualizedGroupContent();
		mainContent.setSize(mainContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		content.setSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setContent(content);
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(parent.getShell(),
						"org.jcryptool.visual.sidechannelattack.spa.spaview");
	}

	private void AddRsaAlgorithmGroupContent(final Composite parent) {
		
		informationGroup.setLayout(new RowLayout());
		
//		CLabel resultLabel = new CLabel(informationGroup,
//				SWT.NONE);
//		resultLabel.setText(Constants.INFORMATION);
//		resultLabel.setFont(FontService.getSmallBoldFont());
		
		rsaProcessText = new StyledText(informationGroup, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		rsaProcessText.setSize(600, 200);

		rsaProcessText.setEditable(false);
		rsaProcessText.setText(INFORMATION_SAM_TEXT);
	}

	private void AddParameterOfRsaGroupContent() {

		FormLayout layout = new FormLayout();
		layout.spacing = 25;
		layout.marginRight = 50;
		parameterOfRSAGroup.setLayout(layout);

		CLabel lblParameterOfRSAGroup = new CLabel(parameterOfRSAGroup,
				SWT.NONE);
		lblParameterOfRSAGroup.setText(Constants.INPUT);
		lblParameterOfRSAGroup.setFont(FontService.getSmallBoldFont());

		final Label enterTheBasisLabel = new Label(parameterOfRSAGroup,
				SWT.NONE);
		FormData formData = new FormData();
		formData.top = new FormAttachment(lblParameterOfRSAGroup);
		enterTheBasisLabel.setLayoutData(formData);
		enterTheBasisLabel.setText(BASIS_LABEL);

		// basis is used to save the ciphertext c in RSA: R = c^d mod n
		basis = new Text(parameterOfRSAGroup, SWT.BORDER);
		formData = new FormData();
		formData.top = new FormAttachment(enterTheBasisLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(enterTheBasisLabel, 0, SWT.RIGHT);
		basis.setSize(50, 25);
		basis.setTextLimit(9);
		basis.setToolTipText(TOOL_TIP_TEXT_BASIS);
		basis.setLayoutData(formData);

		// the verifylistener on basis is used to verify the correctness of
		// input
		basis.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				// Pattern pattern = Pattern.compile("[0-9]\\d*");
				Pattern pattern = Pattern.compile("^[0-9]{1,9}$");
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
		final Label exponentLabel = new Label(parameterOfRSAGroup,
				SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(enterTheBasisLabel);
		exponentLabel.setLayoutData(formData);
		exponentLabel.setText(EXPONENT_LABEL);

		// basis is used to save the exponent d in RSA: R = c^d mod n
		exponent = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		formData = new FormData();
		formData.top = new FormAttachment(exponentLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(basis, 0, SWT.LEFT);
		formData.right = new FormAttachment(basis, 0, SWT.RIGHT);
		exponent.setLayoutData(formData);
		exponent.setToolTipText(TOOL_TIP_TEXT_EXPONENT);
		
		Label exponentBinaryLabel = new Label(parameterOfRSAGroup,
				SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(exponentLabel);
		formData.right = new FormAttachment(exponentLabel, 0, SWT.RIGHT);
		exponentBinaryLabel.setLayoutData(formData);
		exponentBinaryLabel.setText("=");
		
		exponentBinary = new Text(parameterOfRSAGroup, SWT.BORDER);
		formData = new FormData();
		formData.top = new FormAttachment(exponentBinaryLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(exponent, 0, SWT.LEFT);
		formData.right = new FormAttachment(exponent, 0, SWT.RIGHT);
		exponentBinary.setLayoutData(formData);

		exponentBinary.setToolTipText(Constants.TOOL_TIP_TEXT_EXPONENT_BINARY);
		exponentBinary.setEditable(false);

		CLabel parameterLabel = new CLabel(parameterOfRSAGroup,
				SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(exponentBinaryLabel);
		parameterLabel.setLayoutData(formData);
		parameterLabel.setText(Constants.PARAMETER);
		parameterLabel.setFont(FontService.getSmallBoldFont());
		
		// two cue labels
		final Label chooseQLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(parameterLabel);
		chooseQLabel.setLayoutData(formData);
		chooseQLabel.setText(CHOOSE_Q_LABEL);

		qSelectCombo = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		formData = new FormData();
		formData.top = new FormAttachment(chooseQLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(chooseQLabel, 30, SWT.RIGHT);
		qSelectCombo.setLayoutData(formData);
		qSelectCombo.setToolTipText(TOOL_TIP_TEXT_Q_SELECTION);
		// the selectionlistener is used to determine which prime number has
		// been chosen
		qSelectCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				q_selected = Integer.parseInt(qSelectCombo.getItem(qSelectCombo
						.getSelectionIndex()));

				for (int i = 0; i < pSelectCombo.getItemCount(); i++) {

					if (pSelectCombo.getItem(i).equals(
							String.valueOf(q_selected))) {

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
		formData = new FormData();
		formData.top = new FormAttachment(chooseQLabel);
		choosePLabel.setLayoutData(formData);
		choosePLabel.setText(CHOOSE_P_LABEL);

		// Q and P can be selected in qselectcombo and pselectcombo
		pSelectCombo = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		formData = new FormData();
		formData.top = new FormAttachment(choosePLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(qSelectCombo, 0, SWT.LEFT);
		formData.right = new FormAttachment(qSelectCombo, 0, SWT.RIGHT);
		pSelectCombo.setLayoutData(formData);
		pSelectCombo.setToolTipText(TOOL_TIP_TEXT_P_SELECTION);

		// the selectionlistener is used to determine which prime number has
		// been chosen
		pSelectCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				p_selected = Integer.parseInt(pSelectCombo.getItem(pSelectCombo
						.getSelectionIndex()));

				for (int i = 0; i < qSelectCombo.getItemCount(); i++) {

					if (qSelectCombo.getItem(i).equals(
							String.valueOf(p_selected))) {

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
		formData = new FormData();
		formData.top = new FormAttachment(choosePLabel);
		moduleLabel.setLayoutData(formData);
		moduleLabel.setText(MODULE_LABEL_TEXT);

		// mod is used here to save the module n in R = c^d mod n
		mod = new Text(parameterOfRSAGroup, SWT.BORDER);
		formData = new FormData();
		formData.top = new FormAttachment(moduleLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(qSelectCombo, 0, SWT.LEFT);
		formData.right = new FormAttachment(qSelectCombo, 0, SWT.RIGHT);
		mod.setLayoutData(formData);

		mod.setToolTipText(TOOL_TIP_TEXT_MODULE_N);
		mod.setEditable(false);
		
		CLabel selectModeLabel = new CLabel(parameterOfRSAGroup,
				SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(mod);
		selectModeLabel.setLayoutData(formData);
		selectModeLabel.setText(Constants.MODE);
		selectModeLabel.setFont(FontService.getSmallBoldFont());
		
		modeSelection = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		modeSelection.add(SAM_MODE);
		modeSelection.add(SAMA_MODE);
		modeSelection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rsaProcessText.setText(modeSelection.getSelectionIndex() == 0 ? INFORMATION_SAM_TEXT : INFORMATION_SAMA_TEXT);
				super.widgetSelected(e);
			}
		});
		modeSelection.select(0);
		formData = new FormData();
		formData.top = new FormAttachment(selectModeLabel);
		formData.right = new FormAttachment(basis, 0, SWT.RIGHT);
		formData.left = new FormAttachment(selectModeLabel, 0, SWT.LEFT);
		modeSelection.setLayoutData(formData);

		// the executeButton is used here to start the process of
		// "Square and Multiply" Algorithm
		final Button executeButton = new Button(parameterOfRSAGroup, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(modeSelection, 20, SWT.BOTTOM);
		formData.left = new FormAttachment(modeSelection, 0, SWT.LEFT);
		formData.right = new FormAttachment(modeSelection, 0, SWT.RIGHT);
		executeButton.setLayoutData(formData);
		executeButton.setEnabled(false);
		executeButton.setText(EXECUTION_BUTTON_TEXT);

		// executeButton is used to start the process of "square and multiply"
		// algorithm
		executeButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				Execute();
			}
		});

		basis.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {

				if (!basis.getText().equals("") && (exp_selected != 0)
						&& (!mod.getText().equals(""))) {
					executeButton.setEnabled(true);
				}
			}
		});

		// the selectionlistener here is used to determine which prime number
		// has been chosen as exponent
		exponent.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				exp_selected = primeDataExponent[exponent.getSelectionIndex()];
				
			    exponentBinary.setText(Integer.toBinaryString(Integer.parseInt(exponent.getText())));

				if (!basis.getText().equals("") && (exp_selected != 0)
						&& (!mod.getText().equals(""))) {
					executeButton.setEnabled(true);
				}

			}
		});
		
		CLabel resultLabel = new CLabel(parameterOfRSAGroup,
				SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(executeButton);
		resultLabel.setLayoutData(formData);
		resultLabel.setText(Constants.RESULT);
		resultLabel.setFont(FontService.getSmallBoldFont());
		
		// it's a cue label
		final Label resultDescLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(resultLabel);
		resultDescLabel.setLayoutData(formData);
		resultDescLabel.setText(TOOL_TIP_TEXT_RESULT_LABEL);

		// result is used to save the result R in R = c^d mod n
		result = new Text(parameterOfRSAGroup, SWT.BORDER);
		formData = new FormData();
		formData.top = new FormAttachment(resultDescLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(resultDescLabel, 0, SWT.RIGHT);
		result.setLayoutData(formData);
		result.setToolTipText(TOOL_TIP_TEXT_RESULT);
		result.setEditable(false);

		// clearButton is used to clear the table
		final Button clearButton = new Button(parameterOfRSAGroup, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(resultDescLabel, 20, SWT.BOTTOM);
		formData.left = new FormAttachment(executeButton, 0, SWT.LEFT);
		formData.right = new FormAttachment(executeButton, 0, SWT.RIGHT);
		clearButton.setLayoutData(formData);
		clearButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				recordTable.removeAll();
				basis.setText("");
				exponent.setText("");
				mod.setText("");
				exp_selected = 0;
				result.setText("");
				exponent.deselectAll();
				pSelectCombo.deselectAll();
				qSelectCombo.deselectAll();
				p_selected = 0;
				q_selected = 0;
				modeSelection.select(0);
				executeButton.setEnabled(false);
				basis.setFocus();
				for(Control ctrl : powerTraceVisualizationGroup.getChildren())
					ctrl.dispose();
				rsaProcessText.setText(INFORMATION_SAM_TEXT);
			}
		});
		clearButton.setToolTipText(TOOL_TIP_TEXT_CLEARBUTTON);
		clearButton.setText(CLEAR_BUTTON_TEXT);

		mod.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {

				if (!basis.getText().equals("") && (exp_selected != 0)
						&& (!mod.getText().equals(""))) {

					executeButton.setEnabled(true);
				}

			}
		});

		int dataLengthExponent = primeDataExponent.length;
		int dataElementIndexExponent = 0;
		while (dataLengthExponent > 0) {

			exponent.add(String
					.valueOf(primeDataExponent[dataElementIndexExponent]));
			dataLengthExponent--;
			dataElementIndexExponent++;
		}

		// the range in which p and q can be selected
		final int[] primeData = { 101, 103, 107, 109, 113, 127, 131, 137, 139,
				149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199,
				211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271,
				277, 281, 283, 293 };

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

	protected void Execute() {
		
		for(Control ctrl : powerTraceVisualizationGroup.getChildren())
			ctrl.dispose();
		recordTable.removeAll();

		counter = 1;
		
		boolean isSaMmode = modeSelection.getSelectionIndex() == 0;
		
		int[] tempresult = new int[Integer.toBinaryString(
				Integer.parseInt(exponent.getText())).length()];
		
		String exp_in_binary = exponentBinary.getText(); 
		int exp_in_binar_length = exp_in_binary.length();
		
		int count = 0;
		long res = 1;

		// declare some initial parameters of
		// "square and multiply always" in table
		final TableItem initialTableItemBasis = new TableItem(recordTable,
				SWT.BORDER);
		
		initialTableItemBasis.setText(0, INPUT_BASIS_ENG);
		
		String text = RES_AFTER_SQUARE_ENG;
		initialTableItemBasis.setText(1, text);
		text = isSaMmode ? RES_AFTER_MUL_BIT_1_ENG : RES_AFTER_MUL;
		initialTableItemBasis.setText(2, text);

		if(isSaMmode)
		{
			final TableItem initialTableItemBasis2 = new TableItem(recordTable,
					SWT.BORDER);
			initialTableItemBasis2.setText(2, RES_AFTER_MUL_BIT_0_ENG);
		}
		
		final TableItem initialTableItemProcess = new TableItem(recordTable,
				SWT.BORDER);
		initialTableItemProcess.setText(0, INITIAL_ITEM_TEXT_3_IN_TABLE);
		
		int achse_x = 0;

		final Composite powerTraceComposite = new Composite(
				powerTraceVisualizationGroup, SWT.NONE);

		final Label achseY_squareLabel = new Label(powerTraceComposite,
				SWT.NONE);
		achseY_squareLabel.setBounds(achse_x, 10, 85, 110);
		achseY_squareLabel.setImage(SPAPlugIn.getImageDescriptor(
				IMGADRESSE_Y_ACHSE_ENG).createImage());
		
		achse_x = achse_x + 85;

		while (exp_in_binar_length > 0) {
			final TableItem tempTableItems = new TableItem(recordTable,
					SWT.BORDER);
			tempTableItems.setText(0, "  " + counter + HIGHEST_BIT_ENG
					+ exp_in_binary.charAt(count));

			long tempres_byExp = res;
			
			text = NLS.bind(S_RES, INDICES[counter]);

			res = (long) Math.pow(res, 2) % Integer.parseInt(mod.getText());
			tempTableItems.setText(1, text + tempres_byExp
					+ HOCH_2_MOD_ENG + mod.getText() + " = " + res);

			long tempres = res;
			
			text = NLS.bind(R_RES, INDICES[counter+1]);
			
			if (exp_in_binary.charAt(count) == '1') {
				long tempres_byMul = res;
				res = (res * Integer.parseInt(basis.getText()) % Integer
						.parseInt(mod.getText()));
				tempTableItems.setText(2, text + tempres_byMul + " * "
						+ basis.getText() + " mod " + mod.getText() + " = "
						+ res);

				final Label powertrace_sqMulLabel = new Label(
						powerTraceComposite, SWT.NONE);
				powertrace_sqMulLabel.setBounds(achse_x, 10, 85, 110);
				powertrace_sqMulLabel.setImage(SPAPlugIn.getImageDescriptor(
						IMGADDRESSE_SQMUL_ENG).createImage());
				achse_x = achse_x + 85;
			} else {
				if(!isSaMmode)
				{
					long tempres_byMul = res;
	                res = (res * Integer.parseInt(basis.getText()) % Integer.parseInt(mod.getText()));
	                tempTableItems.setText(2, text + tempres_byMul + " * "
	                        + basis.getText() + " mod " + mod.getText() + " = " + res);
	                res = tempres;
				}
                
				final Label powertrace_squareLabel = new Label(
						powerTraceComposite, SWT.PUSH);
				
				text = isSaMmode ? IMGADDRESSE_SQ_ENG : IMGADDRESSE_SQMUL_0_ENG;

				powertrace_squareLabel.setBounds(achse_x, 10, (isSaMmode ? 42 : 85), 110);
				powertrace_squareLabel.setImage(SPAPlugIn.getImageDescriptor(text).createImage());
				achse_x = achse_x + (isSaMmode ? 42 : 85);
			}
			count++;
			counter++;
			exp_in_binar_length--;
		}

		final Label achseX_squareLabel = new Label(powerTraceComposite,
				SWT.NONE);
		achseX_squareLabel.setBounds(achse_x, 10, 85, 110);
		achseX_squareLabel.setImage(SPAPlugIn.getImageDescriptor(
				IMGADDRESSE_X_ACHSE_ENG).createImage());

		final TableItem outPutTableItems = new TableItem(recordTable,
				SWT.BORDER);
		outPutTableItems.setText(0, OUTPUT_TABLE_ITEM_TEXT);

		final TableItem finalTableItems = new TableItem(recordTable, SWT.BORDER);

		finalTableItems.setText(0, "  " + FINAL_RESULT_ENG + res);
		result.setText("" + res);

		res = squareandMultiply.sqmulExcution(
				Integer.parseInt(basis.getText()),
				Integer.parseInt(exponent.getText()),
				Integer.parseInt(mod.getText()));

		String str = "(" + tempresult[--count];
		while (count != 0) {
			str = str + "*" + tempresult[--count];
		}
		
		powerTraceComposite.setSize(powerTraceComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		powerTraceVisualizationGroup.setSize(powerTraceVisualizationGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		mainContent.layout(false);
	}

	

	private void AddVisualizedGroupContent() {

		RowLayout layout = new RowLayout(SWT.VERTICAL);
		visualizedGroup.setLayout(layout);

		Composite calculateTableGroup = new Composite(visualizedGroup, SWT.NONE);
		
		powerTraceVisualizationGroup = new Composite(visualizedGroup,
				SWT.NONE);
		
		recordTable = new Table(calculateTableGroup, SWT.BORDER);
		recordTable.setBounds(0, 0, 650, 320);
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
		
		calculateTableGroup.setSize(calculateTableGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}

}