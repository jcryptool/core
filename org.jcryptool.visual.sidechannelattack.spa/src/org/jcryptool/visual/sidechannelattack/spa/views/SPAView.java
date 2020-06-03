// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2009, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
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
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.core.util.ui.auto.SmoothScroller;
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
	// this is the ....
	private int q_selected = 0;
	private int p_selected = 0;
	private StyledText rsaProcessText;

	private Combo modeSelection;
	private Button executeButton;
	private Button clearButton;

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
		
		// THis creates the top area of the plugin. 
		// It shows the  title and the description of the plugin.
		TitleAndDescriptionComposite titleAndDescription = new TitleAndDescriptionComposite(content);
		titleAndDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		titleAndDescription.setTitle(Messages.Constants_114);
		titleAndDescription.setDescription(Messages.SPAView_description);

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
		
		scrolledComposite.setContent(content);
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		// This makes the ScrolledComposite scrolling, when the mouse 
		// is on a Text with one or more of the following tags: SWT.READ_ONLY,
		// SWT.V_SCROLL or SWT.H_SCROLL.
		SmoothScroller.scrollSmooth(scrolledComposite);
		
		// This registers the context help files to this view.
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				"org.jcryptool.visual.sidechannelattack.spa.view"); //$NON-NLS-1$
	}

	/**
	 * Creates the text with the description on the bottom of the view.
	 */
	private void AddRsaAlgorithmGroupContent() {
		rsaProcessText = new StyledText(informationGroup, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData gd_rsaProcessText = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_rsaProcessText.widthHint = 760;
		gd_rsaProcessText.minimumHeight = 150;
		rsaProcessText.setLayoutData(gd_rsaProcessText);
		rsaProcessText.setEditable(false);
		rsaProcessText.setText(Messages.Constants_125);
	}

	/**
	 * Creates the left composite with the input, paramters, modulus and result.
	 */
	private void AddParameterOfRsaGroupContent() {

		CLabel lblParameterOfRSAGroup = new CLabel(parameterOfRSAGroup, SWT.NONE);
		lblParameterOfRSAGroup.setText(Messages.Constants_100);
		lblParameterOfRSAGroup.setFont(FontService.getNormalBoldFont());
		lblParameterOfRSAGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		final Label enterTheBasisLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		enterTheBasisLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		enterTheBasisLabel.setText(Messages.Constants_115);

		// basis is used to save the ciphertext c in RSA: R = c^d mod n
		basis = new Text(parameterOfRSAGroup, SWT.BORDER);
		basis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		basis.setTextLimit(9);
		basis.setToolTipText(Messages.Constants_135);

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
				
				String currentText = ((Text) e.widget).getText();
				String newName = (currentText.substring(0, e.start) + e.text + currentText.substring(e.end));
				if (newName.isEmpty()) {
					executeButton.setEnabled(false);
				} else {
					checkInputFields();
				}
				
			}
		});
		

		// it's a cue label.
		final Label exponentLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		exponentLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		exponentLabel.setText(Messages.Constants_116_1);

		// basis is used to save the exponent d in RSA: R = c^d mod n
		exponent = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		exponent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		exponent.setToolTipText(Messages.Constants_134);

		Label exponentBinaryLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		exponentBinaryLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		exponentBinaryLabel.setText(Messages.Constants_116_2);

		exponentBinary = new Text(parameterOfRSAGroup, SWT.BORDER);
		exponentBinary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		exponentBinary.setToolTipText(Messages.Constants_144);
		exponentBinary.setEditable(false);
		
		CLabel parameterLabel = new CLabel(parameterOfRSAGroup, SWT.NONE);
		GridData gd_parameterLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_parameterLabel.verticalIndent = 30;
		parameterLabel.setLayoutData(gd_parameterLabel);
		parameterLabel.setText(Messages.Constants_143);
		parameterLabel.setFont(FontService.getNormalBoldFont());

		// two cue labels
		final Label chooseQLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		chooseQLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		chooseQLabel.setText(Messages.Constants_117);

		qSelectCombo = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		qSelectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		qSelectCombo.setToolTipText(Messages.Constants_139);
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
				
				checkInputFields();
			}
		});

		final Label choosePLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		choosePLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		choosePLabel.setText(Messages.Constants_118);

		// Q and P can be selected in qselectcombo and pselectcombo
		pSelectCombo = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		pSelectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		pSelectCombo.setToolTipText(Messages.Constants_14);

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
				
				checkInputFields();
			}
		});

		final Label moduleLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		moduleLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		moduleLabel.setText(Messages.Constants_128);

		// mod is used here to save the module n in R = c^d mod n
		mod = new Text(parameterOfRSAGroup, SWT.BORDER);
		mod.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		mod.setToolTipText(Messages.Constants_140);
		mod.setEditable(false);

		CLabel selectModeLabel = new CLabel(parameterOfRSAGroup, SWT.NONE);
		GridData gd_selectModeLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_selectModeLabel.verticalIndent = 30;
		selectModeLabel.setLayoutData(gd_selectModeLabel);
		selectModeLabel.setText(Messages.Constants_10);
		selectModeLabel.setFont(FontService.getNormalBoldFont());

		modeSelection = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
		modeSelection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		modeSelection.add(Messages.Constants_141);
		modeSelection.add(Messages.Constants_142);
		modeSelection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rsaProcessText
						.setText(modeSelection.getSelectionIndex() == 0 ? Messages.Constants_125 : Messages.Constants_132);
				super.widgetSelected(e);
				
				checkInputFields();
			}
		});
		modeSelection.select(0);

		// the executeButton is used here to start the process of
		// "Square and Multiply" Algorithm
		executeButton = new Button(parameterOfRSAGroup, SWT.NONE);
		executeButton.setText(Messages.Constants_126);

		// executeButton is used to start the process of "square and multiply"
		// algorithm
		executeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				Execute();
				clearButton.setFocus();
			}
		});

		// the selectionlistener here is used to determine which prime number
		// has been chosen as exponent
		exponent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				exponentBinary.setText(Integer.toBinaryString(Integer.parseInt(exponent.getText())));

				checkInputFields();

			}
		});

		CLabel resultLabel = new CLabel(parameterOfRSAGroup, SWT.NONE);
		GridData gd_resultLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_resultLabel.verticalIndent = 30;
		resultLabel.setLayoutData(gd_resultLabel);
		resultLabel.setText(Messages.Constants_1);
		resultLabel.setFont(FontService.getNormalBoldFont());

		// it's a cue label
		final Label resultDescLabel = new Label(parameterOfRSAGroup, SWT.NONE);
		resultDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		resultDescLabel.setText(Messages.Constants_137);

		// result is used to save the result R in R = c^d mod n
		result = new Text(parameterOfRSAGroup, SWT.BORDER);
		result.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		result.setToolTipText(Messages.Constants_138);
		result.setEditable(false);

		// clearButton is used to clear the table
		clearButton = new Button(parameterOfRSAGroup, SWT.NONE);
		GridData gd_clearButton = new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 2, 1);
		gd_clearButton.verticalIndent = 30;
		clearButton.setLayoutData(gd_clearButton);
		clearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				// Reset the table.
				recordTable.removeAll();
				
				modeSelection.select(0);
				basis.setFocus();
				
				// Remove the image.
				for (Control ctrl : powerTraceVisualizationGroup.getChildren()) {
					ctrl.dispose();
				}
				
				rsaProcessText.setText(Messages.Constants_125);
				
				// Set the default values to the GUI.
				setStartValues();
				
				checkInputFields();
			}
		});
		clearButton.setToolTipText(Messages.Constants_136);
		clearButton.setText(Messages.Constants_127);

		// Fill private key d combo
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

		// Fill "choose Q" combo box.
		int data_length = primeData.length;
		int data_element_index = 0;
		while (data_length > 0) {
			qSelectCombo.add(String.valueOf(primeData[data_element_index]));
			data_length--;
			data_element_index++;
		}
		

		// Fill "choose P" combo box
		data_length = primeData.length;
		data_element_index = 0;
		while (data_length > 0) {
			pSelectCombo.add(String.valueOf(primeData[data_element_index]));
			data_length--;
			data_element_index++;
		}
		
		// Fill the GUI with default values.
		setStartValues();
		
		checkInputFields();
		
	}
	
	/**
	 * This method checks, if all entry field contain
	 * values. If this is the case the "execute" button is
	 * enabled. If no value is entered in an input field the
	 * "execute" button is disabled.
	 */
	private void checkInputFields() {
		boolean inputCorrect = false;
		// user entered a ciphertext
		if (!basis.getText().isBlank()) {
			// user selected a private key.
			if (!exponent.getText().isBlank()) {
				// user entered a prime q.
				if (!qSelectCombo.getText().isBlank()) {
					// user entered a prime p
					if (!pSelectCombo.getText().isBlank()) {
						// Enable the execute button
						inputCorrect = true;
					}
				}
			}
		}
		
		executeButton.setEnabled(inputCorrect);
		
	}

	/**
	 * This method sets default values to all
	 * entry field
	 */
	private void setStartValues() {
		basis.setText("5454");
		exponent.setText("101");
		exponentBinary.setText("1100101");
		mod.setText("21877");
		qSelectCombo.setText("131");
		q_selected = 131;
		pSelectCombo.setText("167");
		p_selected = 167;
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

		initialTableItemBasis.setText(0, Messages.Constants_106);

		String text = Messages.Constants_108;
		initialTableItemBasis.setText(1, text);
		text = isSaMmode ? Messages.Constants_109 : Messages.Constants_145;
		initialTableItemBasis.setText(2, text);

		if (isSaMmode) {
			final TableItem initialTableItemBasis2 = new TableItem(recordTable, SWT.BORDER);
			initialTableItemBasis2.setText(2, Messages.Constants_131);
		}

		final TableItem initialTableItemProcess = new TableItem(recordTable, SWT.BORDER);
		initialTableItemProcess.setText(0, Messages.Constants_123);

		int achse_x = 0;

		final Composite powerTraceComposite = new Composite(powerTraceVisualizationGroup, SWT.NONE);

		final Label achseY_squareLabel = new Label(powerTraceComposite, SWT.NONE);
		achseY_squareLabel.setBounds(achse_x, 10, 85, 110);
		achseY_squareLabel.setImage(ImageService.getImage(SPAPlugIn.PLUGIN_ID, IMGADRESSE_Y_ACHSE_ENG));

		achse_x = achse_x + 85;

		while (exp_in_binar_length > 0) {
			final TableItem tempTableItems = new TableItem(recordTable, SWT.BORDER);
			tempTableItems.setText(0, "  " + counter + Messages.Constants_104 + exp_in_binary.charAt(count)); //$NON-NLS-1$

			long tempres_byExp = res;

			text = NLS.bind(Messages.Constants_103, INDICES[counter]);

			res = (long) Math.pow(res, 2) % Integer.parseInt(mod.getText());
			tempTableItems.setText(1, text + tempres_byExp + Messages.Constants_112 + mod.getText() + " = " + res); //$NON-NLS-1$

			long tempres = res;

			text = NLS.bind(Messages.Constants_111, INDICES[counter + 1]);

			if (exp_in_binary.charAt(count) == '1') {
				long tempres_byMul = res;
				res = (res * Integer.parseInt(basis.getText()) % Integer.parseInt(mod.getText()));
				tempTableItems.setText(2,
						text + tempres_byMul + " * " + basis.getText() + " mod " + mod.getText() + " = " + res); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				final Label powertrace_sqMulLabel = new Label(powerTraceComposite, SWT.NONE);
				powertrace_sqMulLabel.setBounds(achse_x, 10, 85, 110);
				powertrace_sqMulLabel.setImage(ImageService.getImage(SPAPlugIn.PLUGIN_ID, IMGADDRESSE_SQMUL_ENG));
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
				powertrace_squareLabel.setImage(ImageService.getImage(SPAPlugIn.PLUGIN_ID, text));
				achse_x = achse_x + (isSaMmode ? 42 : 85);
			}
			count++;
			counter++;
			exp_in_binar_length--;
		}

		final Label achseX_squareLabel = new Label(powerTraceComposite, SWT.NONE);
		achseX_squareLabel.setBounds(achse_x, 10, 85, 110);
		achseX_squareLabel.setImage(ImageService.getImage(SPAPlugIn.PLUGIN_ID, IMGADDRESSE_X_ACHSE_ENG));

		final TableItem outPutTableItems = new TableItem(recordTable, SWT.BORDER);
		outPutTableItems.setText(0, Messages.Constants_124);

		final TableItem finalTableItems = new TableItem(recordTable, SWT.BORDER);

		finalTableItems.setText(0, "  " + Messages.Constants_105 + res); //$NON-NLS-1$
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
		roundCol.setText(Messages.Constants_119);

		// resSquareCol is the second low of recorder table in which the result
		// after squaring will be saved
		final TableColumn resSquareCol = new TableColumn(recordTable, SWT.NONE);
		resSquareCol.setWidth(210);
		resSquareCol.setText(Messages.Constants_12);

		// resMultiCol is the last low in which the result after multiplication
		// will be saved
		final TableColumn resMultiCol = new TableColumn(recordTable, SWT.NONE);
		resMultiCol.setWidth(255);
		resMultiCol.setText(Messages.Constants_120);

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
