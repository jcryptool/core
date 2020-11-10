//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.ui.wizards;

//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.transpositionanalysis.TranspositionAnalysisPlugin;
import org.jcryptool.analysis.transpositionanalysis.ui.ReadDirectionChooser;
import org.jcryptool.analysis.transpositionanalysis.ui.TranspositionTableComposite;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.inputs.TextonlyInput;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.crypto.ui.textloader.ui.wizard.loadtext.UIInputTextWithSource;
import org.jcryptool.crypto.ui.textmodify.wizard.ModifyWizard;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;
import org.jcryptool.crypto.ui.textsource.TextSourceType;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
/**
 * @author SLeischnig A Wizard page to set a text for the transposition analysis
 */
public class TranspTextWizardPage extends WizardPage {

	private static final int auto_max_length = 30000;

	public static class PageConfiguration {
		TextInputWithSource text;
		Integer columnCount;
		Boolean crop;
		Integer cropLength;
		Boolean readInDirection;
		int[] columnOrder;

		public PageConfiguration(TextInputWithSource text, Integer columnCount, Boolean crop, Integer cropLength,
				Boolean readInDirection, int[] columnOrder) {
			super();
			this.text = text;
			this.columnCount = columnCount;
			this.crop = crop;
			this.cropLength = cropLength;
			this.readInDirection = readInDirection;
			if (columnOrder != null) {
				this.columnOrder = columnOrder;
			} else {
				this.columnOrder = makeStdColumnOrder(columnCount);
			}
		}

		public TextInputWithSource getText() {
			return text;
		}

		public Integer getColumnCount() {
			return columnCount;
		}

		public Boolean isCrop() {
			return crop;
		}

		public Integer getCropLength() {
			return cropLength;
		}

		public Boolean getReadInDirection() {
			return readInDirection;
		}

		public boolean equalsNeglectingTextsource(PageConfiguration c) {
			return getColumnCount().equals(c.getColumnCount()) && getCropLength().equals(c.getCropLength())
					&& getText().getText().equals(c.getText().getText()) && isCrop().equals(c.isCrop())
					&& getReadInDirection().equals(c.getReadInDirection());
			// TODO: add check for column order equality
		}

		public boolean equals(PageConfiguration c) {
			return getColumnCount().equals(c.getColumnCount()) && getCropLength().equals(c.getCropLength())
					&& getText().equals(c.getText()) && isCrop().equals(c.isCrop())
					&& getReadInDirection().equals(c.getReadInDirection());
			// TODO: add check for column order equality
		}

		public void setReadInMode(Boolean content) {
			this.readInDirection = content;
		}

		public int[] getColumnOrder() {
			return columnOrder;
		}

		public void setColumnOrder(int[] columnOrder) {
			this.columnOrder = columnOrder;
		}
	}

	public static int[] makeStdColumnOrder(Integer columnCount) {
		int[] columnOrder = new int[columnCount];
		for (int i = 0; i < columnCount; i++) {
			columnOrder[i] = i;
		}
		return columnOrder;
	}

	private Group group1;
	private Spinner blocklengthSpinner;
	private Label label1;
	private ReadDirectionChooser directionChooserIn;
	private Label labelReadIn;
	private Group groupReadInDirection;
	private Group previewGroup;
	private Composite textfieldComp;
	private TranspositionTableComposite transpositionTable1;
	private Text txtInputText;
	private Group grpTextinputCtrls;
	private Group parttextGroup;
	private Button parttextCheck;
	private Spinner parttextCount;
	private boolean isPageBuilt = false;

	private int init_blocklength = 8;
	private boolean init_croptext = false;
	private int init_croplength = 1000;
	private int[] init_columnOrder = makeStdColumnOrder(init_blocklength);
	private boolean doAutoCheckbox = true;
	private List<IEditorReference> editorRefs;

	/**
	 * state 0 ~ pure loaded file <br />
	 * state 1 ~ file + manual input <br />
	 * state 2 ~ pure manual input (either click delete all button or delete text in
	 * one chunk manually) <br />
	 */
	private int manualInputState = 0;
	private boolean iniDirection = false;
	private Group grpText;
	private Composite compTextInputMode;
	private Composite composite;
	private Button btnJcteditor;
	private Button btnDatei;
	private Button btnZwischenablageeigeneEingabe;
	private Combo comboEditorInputSelector;
	private Composite pageComposite;
	/**
	 * The file that was last using the file selection wizard on press of the
	 * "select text from file" radiobutton
	 */
	protected File fileTextInput;
	private UIInputTextWithSource textInput;
	private TextInputWithSource initTextObject;
	private Composite compFileInputDetails;
	private Label lblFilenametxt;
	private Link linkChangeFile;
	private PageConfiguration lastPreviewedPageConfig;
	private TextonlyInput textOnlyInput;
	private Label lblIfTheText;
	private Label lblCharacters;
	private Label lblyouCanChange;
	private Button buttonTransformText;
	
	private GridData text1lData;
	private Composite compTextshortened;
	private String lastDisplayedText;
	private String lastDisplayedFullText;
	private Composite container;

	private TransformData lastTransform = null;
	private TransformData transformation = null;

	/**
	 * Creates a new instance of TranspTextWizardPage.
	 *
	 **/
	public TranspTextWizardPage() {
		super(Messages.TranspTextWizardPage_textwizardtitle, "", null); //$NON-NLS-1$
		setTitle(Messages.TranspTextWizardPage_pagetitle);
		setMessage(Messages.TranspTextWizardPage_pagedescription);

		// setting initial values for the settings
		setCroplength(init_croplength);
		setCroptext(init_croptext);
		setBlocklength(init_blocklength);
		if (init_columnOrder != null)
			setColumnOrder(init_columnOrder);
		// all others have default values in their corresponding input objects
		// (text, readInDirection)
		// -------
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public final void createControl(final Composite parent) {
		GridLayout pageCompositeLayout = new GridLayout();
		pageCompositeLayout.marginHeight = 0;
		pageCompositeLayout.marginWidth = 0;
		pageComposite = new Composite(parent, SWT.NULL);
		pageComposite.setLayout(pageCompositeLayout);
		pageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		grpText = new Group(pageComposite, SWT.NONE);
		grpText.setText(Messages.TranspTextWizardPage_grpText_text);
		grpText.setLayout(new GridLayout(2, false));
		grpText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		compTextInputMode = new Composite(grpText, SWT.NONE);
		compTextInputMode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		compTextInputMode.setLayout(new GridLayout(1, false));

		Label lblLadenDesTextes = new Label(compTextInputMode, SWT.NONE);
		lblLadenDesTextes.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		lblLadenDesTextes.setText(Messages.TranspTextWizardPage_lblLadenDesTextes_text);

		composite = new Composite(compTextInputMode, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));

		btnJcteditor = new Button(composite, SWT.RADIO);
		btnJcteditor.setText(Messages.TranspTextWizardPage_btnJcteditor_text);

		comboEditorInputSelector = new Combo(composite, SWT.NONE) {
			@Override
			protected void checkSubclass() {
			};

			@Override
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point result = super.computeSize(wHint, hHint, changed);
				return new Point(getAppropriateXValue(result.x, 160), result.y);
			};

			private int getAppropriateXValue(int superXCalc, int maxSize) {
				return Math.min(superXCalc, maxSize);
			}
		};
		GridData gd_comboEditorInputSelector = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboEditorInputSelector.horizontalIndent = 15;
		comboEditorInputSelector.setLayoutData(gd_comboEditorInputSelector);

		btnDatei = new Button(composite, SWT.RADIO);
		btnDatei.setText(Messages.TranspTextWizardPage_btnDatei_text);

		compFileInputDetails = new Composite(composite, SWT.NONE);
		GridData gd_compFileInputDetails = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_compFileInputDetails.horizontalIndent = 15;
		compFileInputDetails.setLayoutData(gd_compFileInputDetails);
		GridLayout gl_compFileInputDetails = new GridLayout(2, false);
		gl_compFileInputDetails.marginWidth = 0;
		gl_compFileInputDetails.marginHeight = 0;
		compFileInputDetails.setLayout(gl_compFileInputDetails);

		lblFilenametxt = new Label(compFileInputDetails, SWT.NONE);
		lblFilenametxt.setBounds(0, 0, 55, 15);
		lblFilenametxt.setText(Messages.TranspTextWizardPage_lblFilenametxt_text);

		linkChangeFile = new Link(compFileInputDetails, SWT.NONE);
		linkChangeFile.setText(Messages.TranspTextWizardPage_link_text);

		btnZwischenablageeigeneEingabe = new Button(composite, SWT.RADIO);
		btnZwischenablageeigeneEingabe.setText(Messages.TranspTextWizardPage_btnZwischenablageeigeneEingabe_text);
		btnZwischenablageeigeneEingabe.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnZwischenablageeigeneEingabe.getSelection()) {
					doTxfieldActionPreservingVisibleLines(new Runnable() {
						@Override
						public void run() {
							txtInputText.setSelection(txtInputText.getText().length(), 0);
							txtInputText.forceFocus();
						}
					}, txtInputText);
				}
			}
		});

		grpTextinputCtrls = new Group(grpText, SWT.NONE);
		GridLayout group2Layout = new GridLayout();
		grpTextinputCtrls.setLayout(group2Layout);
		GridData group2LData = new GridData();
		group2LData.verticalAlignment = SWT.FILL;
		group2LData.grabExcessHorizontalSpace = true;
		group2LData.horizontalAlignment = GridData.FILL;
		grpTextinputCtrls.setLayoutData(group2LData);
		grpTextinputCtrls.setText(Messages.TranspTextWizardPage_text);

		textfieldComp = new Composite(grpTextinputCtrls, SWT.NONE);
		GridLayout composite2Layout = new GridLayout();
		composite2Layout.makeColumnsEqualWidth = true;
		composite2Layout.marginHeight = 0;
		composite2Layout.marginWidth = 0;
		GridData composite2LData = new GridData();
		composite2LData.grabExcessHorizontalSpace = true;
		composite2LData.horizontalAlignment = GridData.FILL;
		composite2LData.grabExcessVerticalSpace = true;
		composite2LData.verticalAlignment = GridData.FILL;
		textfieldComp.setLayoutData(composite2LData);
		textfieldComp.setLayout(composite2Layout);

		txtInputText = new Text(textfieldComp, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		//text1lData = new GridData(SWT.FILL, SWT.FILL, true, true);
		text1lData = new GridData();
		text1lData.grabExcessVerticalSpace = true;
		text1lData.verticalAlignment = SWT.FILL;
		text1lData.grabExcessHorizontalSpace = true;
		text1lData.horizontalAlignment = GridData.FILL;
		
		compTextshortened = new Composite(textfieldComp, SWT.NONE);
		compTextshortened.setLayout(new GridLayout());
		compTextshortened.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Link lblTextshortened = new Link(compTextshortened, SWT.NONE);
		lblTextshortened.setText(Messages.TranspTextWizardPage_0);
		lblTextshortened.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				displayText(lastDisplayedFullText, true);
			}
		});

		buttonTransformText = new Button(grpText, SWT.CHECK);
		buttonTransformText.setText("Filter text..."); //$NON-NLS-1$
		buttonTransformText.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ModifyWizard transformSelectionWizard = new ModifyWizard();
				TransformData preTfData = new TransformData();

				TransformData newTransform = null;
				if (buttonTransformText.getSelection()) {
					if (lastTransform != null) {
						preTfData = lastTransform;
					}
					transformSelectionWizard.setPredefinedData(preTfData);
					WizardDialog dialog = new WizardDialog(
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), transformSelectionWizard);
					int result = dialog.open();

					if (result == 0) {

						newTransform = transformSelectionWizard.getWizardData();
						lastTransform = newTransform;
						transformation = newTransform;
						textInput.synchronizeWithUserSide();

					} else {
						transformation = null;
						buttonTransformText.setSelection(false);
						textInput.synchronizeWithUserSide();
					}
				} else {
					newTransform = null;
					transformation = newTransform;
					textInput.synchronizeWithUserSide();
				}
			}
		});

		GC temp = new GC(txtInputText);
		int lines = 4;
		int charHeight = temp.getFontMetrics().getAscent() + 2 * temp.getFontMetrics().getLeading();
		int height = lines * charHeight;
		temp.dispose();
		text1lData.widthHint = 200;
		text1lData.heightHint = height;

		txtInputText.setLayoutData(text1lData);

		parttextGroup = new Group(pageComposite, SWT.NONE);
		GridLayout composite4Layout = new GridLayout(3, false);
		composite4Layout.horizontalSpacing = 0;
		composite4Layout.verticalSpacing = 0;
		GridData parttextGroupLData = new GridData();
		parttextGroupLData.grabExcessHorizontalSpace = true;
		parttextGroupLData.horizontalAlignment = GridData.FILL;
		parttextGroupLData.verticalIndent = 5;
		parttextGroup.setLayoutData(parttextGroupLData);
		parttextGroup.setLayout(composite4Layout);
		parttextGroup.setText(Messages.TranspTextWizardPage_shortenthetext);

		parttextCheck = new Button(parttextGroup, SWT.CHECK | SWT.LEFT);
		parttextCheck.setText(Messages.TranspTextWizardPage_takeonlyapartofthetext);
		parttextCheck.setSelection(init_croptext);
		
		//set checkbox default to true
		parttextCheck.setSelection(true);

		parttextCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				parttextCheckWidgetSelected(evt);
			}
		});

		parttextCount = new Spinner(parttextGroup, SWT.NONE);
		parttextCount.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		parttextCount.setSelection(init_croplength);

		parttextCount.setEnabled(parttextCheck.getSelection());
		parttextCount.setMinimum(1);
		parttextCount.setMaximum(400);
		parttextCount.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				parttextCountWidgetSelected(evt);
			}
		});

		parttextCount.setSelection(init_croplength);

		lblCharacters = new Label(parttextGroup, SWT.NONE);
		lblCharacters.setText(Messages.TranspTextWizardPage_lblCharacters_text);

		lblIfTheText = new Label(parttextGroup, SWT.WRAP);
		lblIfTheText.setText(Messages.TranspTextWizardPage_lblIfTheText_text);
		GridData lblIfthetextLData = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		lblIfthetextLData.horizontalIndent = 20;
		lblIfthetextLData.verticalIndent = 4;
		lblIfthetextLData.widthHint = 200;
		lblIfTheText.setLayoutData(lblIfthetextLData);
		FontData fontdata = lblIfTheText.getFont().getFontData()[0];
		fontdata.height = ((float) 0.9) * (fontdata.height);
		lblIfTheText.setFont(new Font(Display.getCurrent(), fontdata));

		// parttextLabel1 = new Label(parttextGroup, SWT.NONE);
		// parttextLabel1.setText("(This option is activated by default, if you chose
		// not to arrange the text into columns)");

		group1 = new Group(pageComposite, SWT.NONE);
		GridLayout group1Layout = new GridLayout(3, false);
		group1.setLayout(group1Layout);
		GridData group1LData = new GridData();
		group1LData.grabExcessHorizontalSpace = true;
		group1LData.horizontalAlignment = GridData.FILL;
		group1.setLayoutData(group1LData);
		group1.setText(Messages.TranspTextWizardPage_columns);

		label1 = new Label(group1, SWT.NONE);
		label1.setText(Messages.TranspTextWizardPage_setnumberofcolumns);
		GridData label1LData = new GridData();
		label1LData.horizontalAlignment = GridData.FILL;
		label1.setLayoutData(label1LData);

		blocklengthSpinner = new Spinner(group1, SWT.NONE);

		GridData gd_blocklengthSpinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		blocklengthSpinner.setLayoutData(gd_blocklengthSpinner);

		blocklengthSpinner.setMinimum(1);
		blocklengthSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				blocklengthSpinnerWidgetSelected(evt);
			}
		});

		blocklengthSpinner.setSelection(init_blocklength);

		lblyouCanChange = new Label(group1, SWT.WRAP);
		lblyouCanChange.setText(Messages.TranspTextWizardPage_lblyouCanChange_text);
		GridData layoutData2 = new GridData(SWT.FILL, SWT.CENTER, true, false);
		layoutData2.widthHint = 200;
		lblyouCanChange.setLayoutData(layoutData2);

		groupReadInDirection = new Group(pageComposite, SWT.NONE);
		groupReadInDirection.setLayout(new GridLayout(2, false));
		GridData groupReadInDirectionLData = new GridData();
		groupReadInDirectionLData.grabExcessHorizontalSpace = true;
		groupReadInDirectionLData.horizontalAlignment = GridData.FILL;
		groupReadInDirection.setLayoutData(groupReadInDirectionLData);
		groupReadInDirection.setText(Messages.TranspTextWizardPage_readinmode);

		labelReadIn = new Label(groupReadInDirection, SWT.NONE);
		labelReadIn.setText(Messages.TranspTextWizardPage_readinmode_description);

		directionChooserIn = new ReadDirectionChooser(groupReadInDirection);
		directionChooserIn.setDirection(iniDirection);

		directionChooserIn.getInput().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if (arg == null)
					preview();
			}
		});

		previewGroup = new Group(pageComposite, SWT.NONE);
		GridLayout previewGroupLayout = new GridLayout();
		previewGroupLayout.makeColumnsEqualWidth = true;
		previewGroup.setLayout(previewGroupLayout);
		GridData previewGroupLData = new GridData(SWT.FILL, SWT.FILL, true, true);
		previewGroupLData.verticalIndent = 10;
		previewGroup.setLayoutData(previewGroupLData);
		previewGroup.setText(Messages.TranspTextWizardPage_preview);

		GridLayout transpTableLayout = new GridLayout();
		transpTableLayout.makeColumnsEqualWidth = true;
		GridData transpositionTable1LData = new GridData(SWT.FILL, SWT.FILL, true, true);
		transpositionTable1LData.heightHint = 120;
		transpositionTable1LData.widthHint = 250;
		transpositionTable1 = new TranspositionTableComposite(previewGroup, SWT.NONE);
		transpositionTable1.setLayout(transpTableLayout);
		transpositionTable1.setLayoutData(transpositionTable1LData);

		setControl(pageComposite);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
				TranspositionAnalysisPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$

		initializeTextInput();

		setPageComplete(true);

		preview();
		isPageBuilt = true;
	}

	/**
	 * Runs a runnable, which executes some code. The top visible line number of the
	 * text field is remembered, and after the runnable has finished, the top
	 * visible line is restored is set to the remembered number.
	 *
	 * @param runnable
	 */
	protected void doTxfieldActionPreservingVisibleLines(final Runnable runnable, Text textfield) {
		final Display display = textfield.getDisplay();
		final int topIndex = textfield.getTopIndex();
		runnable.run();

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					LogUtil.logError(e);
				}
				Runnable r = new Runnable() {
					@Override
					public void run() {
						txtInputText.setTopIndex(topIndex);
					}
				};

				display.syncExec(r);
			};
		}.start();
	}

	private void loadEditorsAndFillEditorChooser() {
		IEditorReference activeReference = EditorsManager.getInstance().getActiveEditorReference();
		for (int i = 0; i < editorRefs.size(); i++) {
			comboEditorInputSelector.add(editorRefs.get(i).getTitle());
			if (activeReference != null && activeReference.equals(editorRefs.get(i))) {
				comboEditorInputSelector.setText(editorRefs.get(i).getTitle());
				comboEditorInputSelector.select(i);
			}
		}
		if (activeReference == null) {
			if (editorRefs.size() > 0) {
				comboEditorInputSelector.select(0);
			}
		}
	}

	private void initializeTextInput() {

		editorRefs = EditorsManager.getInstance().getEditorReferences();
		if (editorRefs.size() > 0) {
			// TODO: rename method (no loading here!)
			loadEditorsAndFillEditorChooser();
		}

		textOnlyInput = new TextonlyInput() {
			@Override
			public Text getTextfield() {
				return txtInputText;
			}
		};

		textInput = new UIInputTextWithSource(editorRefs) {
			@Override
			protected Button getFileRadioButton() {
				return btnDatei;
			}

			@Override
			protected Button getBtnJctEditorOption() {
				return btnJcteditor;
			}

			@Override
			protected Button getBtnOwninput() {
				return btnZwischenablageeigeneEingabe;
			}

			@Override
			protected File getSelectedFile() {
				return fileTextInput;
			}

			@Override
			protected Combo getComboEditors() {
				return comboEditorInputSelector;
			}

			@Override
			protected Text getTextFieldForTextInput() {
				return txtInputText;
			}

			@Override
			protected IEditorReference getSelectedEditor() {
				return getCurrentlySelectedEditor();
			}

			@Override
			protected void setUIState(TextInputWithSource content, boolean b) {
				setTextInputUIState(content, b);
			}

			@Override
			protected TextInputWithSource getInitialTextObject() {
				return initTextObject;
			}

			@Override
			protected List<IEditorReference> getEditorsNotNecessarilyFresh() {
				return editorRefs;
			}

			@Override
			protected AbstractUIInput<String> getTextOnlyInput() {
				return textOnlyInput;
			}

			@Override
			protected TransformData getTransformData() {
				return transformation;
			}

			@Override
			protected Button getBtnTransformation() {
				return buttonTransformText;
			}
		};

		btnJcteditor.setEnabled(editorRefs.size() > 0);
		btnDatei.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnDatei.getSelection()) {
					if (fileTextInput == null)
						fileTextInput = openFileSelectionDialogue();
					textInput.synchronizeWithUserSide();
					setTextInputUIState(textInput.getContent(), true);
				}
			}
		});
		btnJcteditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnJcteditor.getSelection()) {
					textInput.synchronizeWithUserSide();
					setTextInputUIState(textInput.getContent(), true);
				}
			}
		});
		btnZwischenablageeigeneEingabe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnZwischenablageeigeneEingabe.getSelection()) {
					textInput.synchronizeWithUserSide();
					setTextInputUIState(textInput.getContent(), true);
				}
			}
		});
		comboEditorInputSelector.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textInput.synchronizeWithUserSide();
				setTextInputUIState(textInput.getContent(), true);
			}
		});
		linkChangeFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileTextInput = openFileSelectionDialogue();
				textInput.synchronizeWithUserSide();
				setTextInputUIState(textInput.getContent(), true);
			}
		});
		textInput.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				preview();
			}
		});
	}

	/**
	 * Opens a modal dialogue to select a file from the file system. If the file
	 * selection is cancelled, null is returned.
	 *
	 * @return the file, or null at dialogue cancel
	 */
	protected File openFileSelectionDialogue() {
		FileDialog fd = new FileDialog(btnDatei.getShell(), SWT.OPEN);
		fd.setText(Messages.TranspTextWizardPage_windowsfiledialogtitle);
		fd.setFilterPath(null);
		String[] filterExt = { "*.*" }; //$NON-NLS-1$
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		if (selected == null)
			return null;
		return new File(selected);
	}

	protected void setTextInputUIState(TextInputWithSource text, boolean writeText) {
		TextSourceType sourceType = text.getSourceType();
		String textString = text.getText();
		btnDatei.setSelection(sourceType.equals(TextSourceType.FILE));
		btnJcteditor.setSelection(sourceType.equals(TextSourceType.JCTEDITOR));
		btnZwischenablageeigeneEingabe.setSelection(sourceType.equals(TextSourceType.USERINPUT));
		if (writeText) {
			if (textInput != null) {
				//textOnlyInput.writeContent(textString);
				displayText(textString);
				textOnlyInput.synchronizeWithUserSide();
			} else {
				//txtInputText.setText(textString);
				displayText(textString);
			}
		}

		showLoadFromEditorComponents(sourceType.equals(TextSourceType.JCTEDITOR));
		if (sourceType.equals(TextSourceType.JCTEDITOR)) {
			// TODO:
			comboEditorInputSelector.select(editorRefs.indexOf(text.editorReference));
		}
		if (sourceType.equals(TextSourceType.FILE)) {
			showLoadFromFileComponents(sourceType.equals(TextSourceType.FILE), text.file.getName());
			// fileTextInput is somewhat the representation of the file
			// selection wizard UI, so this fits here
			fileTextInput = text.file;
		} else {
			showLoadFromFileComponents(sourceType.equals(TextSourceType.FILE), ""); //$NON-NLS-1$
		}

		txtInputText.setEditable(sourceType.equals(TextSourceType.USERINPUT));
	}
	
	private void displayText(String textString, boolean shortenIfNecessary) {
		// TODO Auto-generated method stub
		boolean makePreview = shortenIfNecessary && textString.length() > auto_max_length;
		String previewText = textString;
		if (makePreview) {
			previewText = textString.subSequence(0, Math.min(textString.length(), auto_max_length)).toString();
		}
		lastDisplayedText = previewText;
		lastDisplayedFullText = textString;
		txtInputText.setText(previewText);
		toggleTextshortenedDisplay(lastDisplayedText.length() < lastDisplayedFullText.length(), textString);
	}
	
	private String makeShortenedTextForTablePreview(String textString, boolean shortenIfNecessary) {
		// TODO Auto-generated method stub
		boolean makePreview = shortenIfNecessary && textString.length() > auto_max_length;
		String previewText = textString;
		if (makePreview) {
			previewText = textString.subSequence(0, Math.min(textString.length(), auto_max_length)).toString();
			return previewText;
		}
		//lastDisplayedText = previewText;
		//lastDisplayedFullText = textString;
		txtInputText.setText(previewText);
		//toggleTextshortenedDisplay(lastDisplayedText.length() < lastDisplayedFullText.length(), textString);
		return previewText;
	}

	private void displayText(String textString) {
		displayText(textString, true);
	}


	private void toggleTextshortenedDisplay(boolean b, String textString) {
			GridData ldata = (GridData) compTextshortened.getLayoutData();
			ldata.exclude = !b;
			compFileInputDetails.setVisible(b);
			pageComposite.layout(new Control[] { compTextshortened });
	}

	/**
	 * Assumes that the selection index is not "-1" -> something is actually
	 * selected
	 *
	 * @return
	 */
	protected IEditorReference getCurrentlySelectedEditor() {
		int index = comboEditorInputSelector.getSelectionIndex();
		return editorRefs.get(index);
	}

	protected List<IEditorReference> getEditorsNotNecessarilyFresh() {
		return editorRefs;
	}
	

	private void showLoadFromFileComponents(boolean b, String fileName) {
		if (b) {
			GridData ldata = (GridData) compFileInputDetails.getLayoutData();
			ldata.exclude = !b;
			compFileInputDetails.setVisible(b);
			lblFilenametxt.setText(fileName);
			pageComposite.layout(new Control[] { lblFilenametxt, linkChangeFile, compFileInputDetails });
		} else {
			GridData ldata = (GridData) compFileInputDetails.getLayoutData();
			ldata.exclude = !b;
			compFileInputDetails.setVisible(b);
			pageComposite.layout(new Control[] { lblFilenametxt, linkChangeFile, compFileInputDetails });
		}
	}

	private void showLoadFromEditorComponents(boolean b) {
		if (b) {
			GridData ldata = (GridData) comboEditorInputSelector.getLayoutData();
			ldata.exclude = !b;
			comboEditorInputSelector.setVisible(b);
			pageComposite.layout(new Control[] { comboEditorInputSelector });
		} else {
			GridData ldata = (GridData) comboEditorInputSelector.getLayoutData();
			ldata.exclude = !b;
			comboEditorInputSelector.setVisible(b);
			pageComposite.layout(new Control[] { comboEditorInputSelector });
		}
	}

	protected void textFieldChanged(String previousText, String actualText) {
		if (manualInputState != 2) {
			manualInputState = 1;
		}
		if (actualText.length() < 1) {
			manualInputState = 2;
		}
	}

	private void preview() {
		// if(textInput != null) {
		// setMessage("Origin: " +
		// (textInput.getContent().userInputOrigin==null?"null":textInput.getContent().userInputOrigin.toString()));
		// }
		if (lastPreviewedPageConfig != null) {
			if (!getPageConfiguration().equalsNeglectingTextsource(lastPreviewedPageConfig)) {
				// if there is 'real' change in the previewed data, preview..
				forcePreview();
			} else {
				// do nothing -- no preview to do, cuz nothing changed!
			}
		} else {
			// first preview always necessary
			forcePreview();
		}
	}

	private void forcePreview() {
		PageConfiguration pageConfig = getPageConfiguration();
		transpositionTable1.setReadInOrder(pageConfig.getReadInDirection());
		String text = pageConfig.getText().getText();
		transpositionTable1.setText(makeShortenedTextForTablePreview(text,true), pageConfig.getColumnCount(), !pageConfig.isCrop(),
				pageConfig.getCropLength(), pageConfig.getColumnOrder(), true);
	
		txtInputText.setText(makeShortenedTextForTablePreview(text,true));
		//pageConfig.getText().setText(makeShortenedTextForTablePreview(text, true));
		
		lastPreviewedPageConfig = pageConfig;
	}

	private void setReadInDirection(boolean dir) {
		if (directionChooserIn != null) {
			directionChooserIn.setDirection(dir);
		} else {
			this.iniDirection = dir;
		}
	}

	/**
	 * @param initBlocklength the init_blocklength to set
	 */
	private void setBlocklength(int length) {
		if ((blocklengthSpinner != null) && (!blocklengthSpinner.isDisposed())) {
			blocklengthSpinner.setSelection(length);
			parttextCountWidgetSelected(null);
		} else {
			init_blocklength = length;
		}
	}

	/**
	 * @param initCroptext the init_croptext to set
	 */
	private void setCroptext(boolean initCroptext) {
		if ((parttextCheck != null) && (!parttextCheck.isDisposed())) {
			parttextCheck.setSelection(initCroptext);
			parttextCheckWidgetSelected(null);
		} else {
			this.init_croptext = initCroptext;
		}
	}

	/**
	 * @param initCroplength the init_croplength to set
	 */
	private void setCroplength(int initCroplength) {
		if ((parttextCount != null) && (!parttextCount.isDisposed())) {
			parttextCount.setSelection(initCroplength);
			parttextCountWidgetSelected(null);
		} else {
			this.init_croplength = initCroplength;
		}
	}

	private void setText(TextInputWithSource initFirstTabText) {
		if (isPageBuilt()) {
			textInput.writeContent(initFirstTabText);
			textInput.synchronizeWithUserSide();
		} else {
			initTextObject = initFirstTabText;
		}
	}

	public void setPageConfiguration(PageConfiguration config) {
		setText(config.getText());
		setReadInDirection(config.getReadInDirection());
		setBlocklength(config.getColumnCount());
		setCroptext(config.isCrop());
		setCroplength(config.getCropLength());
		setColumnOrder(config.getColumnOrder());
	}

	private void setColumnOrder(int[] columnOrder) {
		if (transpositionTable1 != null && !transpositionTable1.isDisposed()) {
			transpositionTable1.setColumnOrder(columnOrder);
		} else {
			init_columnOrder = columnOrder;
		}
	}

	private void parttextCountWidgetSelected(SelectionEvent evt) {
		preview();
	}

	private void parttextCheckWidgetSelected(SelectionEvent evt) {
		parttextCount.setEnabled(parttextCheck.getSelection());
		if (blocklengthSpinner.getSelection() > 0 && parttextCheck.getSelection())
			doAutoCheckbox = false;
		if (evt != null)
			preview();
	}

	private void blocklengthSpinnerWidgetSelected(SelectionEvent evt) {
		if (doAutoCheckbox)
			parttextCheck.setSelection(blocklengthSpinner.getSelection() == 0);
		parttextCheckWidgetSelected(null);
		preview();
	}

	public boolean isPageBuilt() {
		return isPageBuilt;
	}

	public PageConfiguration getPageConfiguration() {
		return new PageConfiguration(textInput.getContent(), blocklengthSpinner.getSelection(),
				parttextCheck.getSelection(), parttextCount.getSelection(), directionChooserIn.getInput().getContent(),
				isPageBuilt ? transpositionTable1.getColumnOrder() : init_columnOrder);
	}
	
	

}
