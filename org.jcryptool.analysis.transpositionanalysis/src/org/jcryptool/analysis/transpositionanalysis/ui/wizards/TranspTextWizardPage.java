//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
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
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.InputVerificationResult.MessageType;
import org.jcryptool.core.util.input.TextfieldInput;

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

	public static class PageConfiguration {
		TextInputWithSource text;
		Integer columnCount;
		Boolean crop;
		Integer cropLength;
		Boolean readInDirection;

		public PageConfiguration(TextInputWithSource text, Integer columnCount, Boolean crop, Integer cropLength,
			Boolean readInDirection) {
			super();
			this.text = text;
			this.columnCount = columnCount;
			this.crop = crop;
			this.cropLength = cropLength;
			this.readInDirection = readInDirection;
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
		}

		public boolean equals(PageConfiguration c) {
			return getColumnCount().equals(c.getColumnCount()) && getCropLength().equals(c.getCropLength())
				&& getText().equals(c.getText()) && isCrop().equals(c.isCrop())
				&& getReadInDirection().equals(c.getReadInDirection());
		}
	}

	private static final String NOBALLOON_RESULTTYPE = "NOBALLOON";
	private Group group1;
	private Spinner blocklengthSpinner;
	private Label label1;
	private ReadDirectionChooser directionChooserIn;
	private Label labelReadIn;
	private Group groupReadInDirection;
	private Composite fillcomposite;
	private Group previewGroup;
	private Label label3;
	private Composite textfieldComp;
	private TranspositionTableComposite transpositionTable1;
	private Text txtInputText;
	private Group grpTextinputCtrls;
	private Label label2;
	private Composite composite1;
	private Group parttextGroup;
	private Composite parttextCompRow;
	private Button parttextCheck;
	private Spinner parttextCount;
	private Label parttextLabel2;
	private boolean isPageBuilt = false;

	private int init_blocklength = 0;
	private boolean init_croptext = true;
	private int init_croplength = 40;
	private boolean doAutoCheckbox = true;
	private List<IEditorReference> editorRefs;

	/**
	 * state 0 ~ pure loaded file <br />
	 * state 1 ~ file + manual input <br />
	 * state 2 ~ pure manual input (either click delete all button or delete
	 * text in one chunk manually) <br />
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
	private AbstractUIInput<TextInputWithSource> textInput;
	private TextfieldInput<String> textOnlyInput;
	private TextInputWithSource initTextObject;
	private Composite compFileInputDetails;
	private Label lblFilenametxt;
	private Link linkChangeFile;
	private PageConfiguration lastPreviewedPageConfig;

	/**
	 * Creates a new instance of TranspTextWizardPage.
	 * 
	 **/
	public TranspTextWizardPage() {
		super(Messages.TranspTextWizardPage_textwizardtitle, "", null); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
		setTitle(Messages.TranspTextWizardPage_pagetitle);
		setMessage(Messages.TranspTextWizardPage_pagedescription);

		// setting initial values for the settings
		setCroplength(80);
		setCroptext(true);
		setBlocklength(0);
		// all others have default values in their corresponding input objects
		// (text, readInDirection)
		// -------
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public final void createControl(final Composite parent) {
		GridData pageCompositeLayoutData = new GridData();
		GridLayout pageCompositeLayout = new GridLayout();
		pageCompositeLayoutData.grabExcessHorizontalSpace = true;
		pageCompositeLayoutData.grabExcessVerticalSpace = true;
		pageCompositeLayoutData.horizontalAlignment = SWT.FILL;
		pageCompositeLayoutData.verticalAlignment = SWT.FILL;
		pageComposite = new Composite(parent, SWT.NULL);
		pageComposite.setLayout(pageCompositeLayout);
		pageComposite.setLayoutData(pageCompositeLayoutData);

		{
			grpText = new Group(pageComposite, SWT.NONE);
			grpText.setText(Messages.TranspTextWizardPage_grpText_text);
			grpText.setLayout(new GridLayout(2, false));
			grpText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			{
				compTextInputMode = new Composite(grpText, SWT.NONE);
				compTextInputMode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
				compTextInputMode.setLayout(new GridLayout(1, false));

				Label lblLadenDesTextes = new Label(compTextInputMode, SWT.NONE);
				lblLadenDesTextes.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
				lblLadenDesTextes.setBounds(0, 0, 55, 15);
				lblLadenDesTextes.setText(Messages.TranspTextWizardPage_lblLadenDesTextes_text);
				{
					composite = new Composite(compTextInputMode, SWT.NONE);
					composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					composite.setLayout(new GridLayout(1, false));
					{
						btnJcteditor = new Button(composite, SWT.RADIO);
						btnJcteditor.setText(Messages.TranspTextWizardPage_btnJcteditor_text);
					}
					{
						comboEditorInputSelector = new Combo(composite, SWT.NONE);
						GridData gd_comboEditorInputSelector = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
						gd_comboEditorInputSelector.horizontalIndent = 15;
						comboEditorInputSelector.setLayoutData(gd_comboEditorInputSelector);
					}
					{
						btnDatei = new Button(composite, SWT.RADIO);
						btnDatei.setText(Messages.TranspTextWizardPage_btnDatei_text);
					}
					{
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
					}
					{
						btnZwischenablageeigeneEingabe = new Button(composite, SWT.RADIO);
						btnZwischenablageeigeneEingabe
							.setText(Messages.TranspTextWizardPage_btnZwischenablageeigeneEingabe_text);
					}
				}
			}
			{
				grpTextinputCtrls = new Group(grpText, SWT.NONE);
				GridLayout group2Layout = new GridLayout();
				grpTextinputCtrls.setLayout(group2Layout);
				GridData group2LData = new GridData();
				group2LData.grabExcessHorizontalSpace = true;
				group2LData.horizontalAlignment = GridData.FILL;
				grpTextinputCtrls.setLayoutData(group2LData);
				grpTextinputCtrls.setText(Messages.TranspTextWizardPage_text);
				{
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
					{
						txtInputText = new Text(textfieldComp, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
						GridData text1LData = new GridData();
						text1LData.grabExcessHorizontalSpace = true;
						text1LData.horizontalAlignment = GridData.FILL;

						GC temp = new GC(txtInputText);
						int lines = 6;
						int charHeight = temp.getFontMetrics().getAscent() + 2 * temp.getFontMetrics().getLeading();
						int height = lines * charHeight;
						temp.dispose();
						text1LData.widthHint = 200;
						text1LData.heightHint = height;

						txtInputText.setLayoutData(text1LData);

					}
				}
			}
		}
		{
			parttextGroup = new Group(pageComposite, SWT.NONE);
			GridLayout composite4Layout = new GridLayout();
			composite4Layout.verticalSpacing = 0;
			GridData parttextGroupLData = new GridData();
			parttextGroupLData.grabExcessHorizontalSpace = true;
			parttextGroupLData.horizontalAlignment = GridData.FILL;
			parttextGroupLData.verticalIndent = 5;
			parttextGroup.setLayoutData(parttextGroupLData);
			parttextGroup.setLayout(composite4Layout);
			parttextGroup.setText(Messages.TranspTextWizardPage_shortenthetext);
			{
				parttextCompRow = new Composite(parttextGroup, SWT.NONE);
				RowLayout composite5Layout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
				GridData composite5LData = new GridData();
				composite5LData.grabExcessHorizontalSpace = true;
				composite5LData.horizontalAlignment = GridData.FILL;
				parttextCompRow.setLayoutData(composite5LData);
				parttextCompRow.setLayout(composite5Layout);
				{
					parttextCheck = new Button(parttextCompRow, SWT.CHECK | SWT.LEFT);
					parttextCheck.setText(Messages.TranspTextWizardPage_takeonlyapartofthetext);
					parttextCheck.setSelection(init_croptext);

					parttextCheck.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							parttextCheckWidgetSelected(evt);
						}
					});

				}
				{
					parttextCount = new Spinner(parttextCompRow, SWT.NONE);
					parttextCount.setLayoutData(new RowData());
					parttextCount.setSelection(init_croplength);

					parttextCount.setEnabled(parttextCheck.getSelection());
					parttextCount.setMinimum(1);
					parttextCount.setMaximum(200);
					parttextCount.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							parttextCountWidgetSelected(evt);
						}
					});

					parttextCount.setSelection(init_croplength);
				}
				{
					parttextLabel2 = new Label(parttextCompRow, SWT.NONE);
					parttextLabel2.setText(""); //$NON-NLS-1$
				}
			}
			{
				// parttextLabel1 = new Label(parttextGroup, SWT.NONE);
				// parttextLabel1.setText("(This option is activated by default, if you chose not to arrange the text into columns)");
			}
		}
		{
			fillcomposite = new Composite(pageComposite, SWT.NONE);
			GridLayout composite2Layout = new GridLayout();
			composite2Layout.makeColumnsEqualWidth = true;
			GridData composite2LData = new GridData();
			composite2LData.grabExcessHorizontalSpace = true;
			composite2LData.horizontalAlignment = GridData.FILL;
			composite2LData.heightHint = 15;
			fillcomposite.setLayoutData(composite2LData);
			fillcomposite.setLayout(composite2Layout);
		}
		{
			group1 = new Group(pageComposite, SWT.NONE);
			GridLayout group1Layout = new GridLayout();
			group1Layout.makeColumnsEqualWidth = true;
			group1.setLayout(group1Layout);
			GridData group1LData = new GridData();
			group1LData.grabExcessHorizontalSpace = true;
			group1LData.horizontalAlignment = GridData.FILL;
			group1.setLayoutData(group1LData);
			group1.setText(Messages.TranspTextWizardPage_columns);
			{
				composite1 = new Composite(group1, SWT.NONE);
				GridLayout composite1Layout = new GridLayout();
				composite1Layout.numColumns = 2;
				composite1Layout.horizontalSpacing = 10;
				GridData composite1LData = new GridData();
				composite1LData.grabExcessHorizontalSpace = true;
				composite1LData.horizontalAlignment = GridData.FILL;
				composite1.setLayoutData(composite1LData);
				composite1.setLayout(composite1Layout);
				{
					label1 = new Label(composite1, SWT.NONE);
					label1.setText(Messages.TranspTextWizardPage_setnumberofcolumns);
					GridData label1LData = new GridData();
					label1LData.heightHint = 15;
					label1LData.horizontalAlignment = GridData.FILL;
					label1.setLayoutData(label1LData);
				}
				{
					GridData spinner1LData = new GridData();
					spinner1LData.grabExcessHorizontalSpace = true;
					spinner1LData.grabExcessVerticalSpace = true;
					spinner1LData.verticalSpan = 2;
					blocklengthSpinner = new Spinner(composite1, SWT.NONE);
					blocklengthSpinner.setLayoutData(spinner1LData);
					blocklengthSpinner.setMinimum(0);
					blocklengthSpinner.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							blocklengthSpinnerWidgetSelected(evt);
						}
					});

					blocklengthSpinner.setSelection(init_blocklength);
				}
				{
					label3 = new Label(composite1, SWT.NONE);
					GridData label3LData = new GridData();
					label3LData.horizontalAlignment = GridData.FILL;
					label3LData.verticalAlignment = GridData.BEGINNING;
					label3.setLayoutData(label3LData);
					label3.setText(Messages.TranspTextWizardPage_valuezerodescription);
				}
			}
			{
				label2 = new Label(group1, SWT.NONE | SWT.WRAP);
				GridData label2LData = new GridData();
				label2LData.verticalIndent = 10;
				label2LData.grabExcessHorizontalSpace = true;
				label2LData.horizontalAlignment = SWT.FILL;
				label2LData.widthHint = 300;
				label2.setLayoutData(label2LData);
				label2.setText(Messages.TranspTextWizardPage_whatiscolumncount);
			}
		}
		{
			groupReadInDirection = new Group(pageComposite, SWT.NONE);
			GridLayout groupReadInDirectionLayout = new GridLayout();
			groupReadInDirectionLayout.numColumns = 2;
			groupReadInDirection.setLayout(groupReadInDirectionLayout);
			GridData groupReadInDirectionLData = new GridData();
			groupReadInDirectionLData.grabExcessHorizontalSpace = true;
			groupReadInDirectionLData.horizontalAlignment = GridData.FILL;
			groupReadInDirection.setLayoutData(groupReadInDirectionLData);
			groupReadInDirection.setText(Messages.TranspTextWizardPage_readinmode);
			{
				labelReadIn = new Label(groupReadInDirection, SWT.NONE);
				labelReadIn.setText(Messages.TranspTextWizardPage_readinmode_description);
			}
			{
				directionChooserIn = new ReadDirectionChooser(groupReadInDirection, true);
				directionChooserIn.setDirection(iniDirection);

				directionChooserIn.getInput().addObserver(new Observer() {
					public void update(Observable o, Object arg) {
						if (arg == null) preview();
					}
				});
			}
		}

		{
			previewGroup = new Group(pageComposite, SWT.NONE);
			GridLayout previewGroupLayout = new GridLayout();
			previewGroupLayout.makeColumnsEqualWidth = true;
			previewGroup.setLayout(previewGroupLayout);
			GridData previewGroupLData = new GridData();
			previewGroupLData.verticalIndent = 10;
			previewGroupLData.horizontalAlignment = GridData.FILL;
			previewGroupLData.grabExcessHorizontalSpace = true;
			previewGroupLData.verticalAlignment = GridData.FILL;
			previewGroupLData.grabExcessVerticalSpace = true;
			previewGroup.setLayoutData(previewGroupLData);
			previewGroup.setText(Messages.TranspTextWizardPage_preview);
			{
				GridLayout transpTableLayout = new GridLayout();
				transpTableLayout.makeColumnsEqualWidth = true;
				GridData transpositionTable1LData = new GridData();
				transpositionTable1LData.heightHint = 120;
				transpositionTable1LData.widthHint = 250;
				transpositionTable1LData.grabExcessHorizontalSpace = true;
				transpositionTable1LData.horizontalAlignment = GridData.FILL;
				transpositionTable1LData.grabExcessVerticalSpace = true;
				transpositionTable1LData.verticalAlignment = GridData.FILL;
				transpositionTable1 = new TranspositionTableComposite(previewGroup, SWT.NONE);
				transpositionTable1.setLayout(transpTableLayout);
				transpositionTable1.setLayoutData(transpositionTable1LData);

			}
		}

		setControl(pageComposite);
		PlatformUI.getWorkbench().getHelpSystem()
			.setHelp(getControl(), TranspositionAnalysisPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$

		initializeTextInput();

		setPageComplete(true);

		isPageBuilt = true;
		preview();
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
			loadEditorsAndFillEditorChooser();
		}

		textOnlyInput = new TextfieldInput<String>() {
			@Override
			protected Text getTextfield() {
				return txtInputText;
			}

			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}

			@Override
			public String readContent() {
				return txtInputText.getText();
			}

			@Override
			protected String getDefaultContent() {
				return "";
			}

			@Override
			public String getName() {
				return "Text";
			}
		};

		textInput = new AbstractUIInput<TextInputWithSource>() {

			@Override
			protected InputVerificationResult verifyUserChange() {
				if (btnDatei.getSelection() && fileTextInput == null) {
					// msg not meant to be displayed
					return InputVerificationResult.generateIVR(false, "no file was selected", MessageType.INFORMATION,
						false, NOBALLOON_RESULTTYPE);
				}
				if (btnDatei.getSelection() && !fileTextInput.exists()) {
					// msg not meant to be displayed
					return InputVerificationResult.generateIVR(false, "input file does not exist",
						MessageType.INFORMATION, false);
				}
				if (btnJcteditor.getSelection() && getEditorsNotNecessarilyFresh().size() == 0) {
					// msg not meant to be displayed
					return InputVerificationResult.generateIVR(false, "no editors are available",
						MessageType.INFORMATION, false, NOBALLOON_RESULTTYPE);
				}
				if (btnJcteditor.getSelection() && comboEditorInputSelector.getSelectionIndex() < 0) {
					// should never appear
					return InputVerificationResult.generateIVR(true, "no editor selected", MessageType.INFORMATION,
						false, NOBALLOON_RESULTTYPE);
				}
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}

			@Override
			public TextInputWithSource readContent() {
				if (btnZwischenablageeigeneEingabe.getSelection()) {
					String text = textOnlyInput.getContent();
					return new TextInputWithSource(text);
				} else if (btnDatei.getSelection()) {
					try {
						return new TextInputWithSource(getTextFromFile(fileTextInput), fileTextInput);
					} catch (FileNotFoundException e) {
						// should not happen since existance of file has been
						// tested
						e.printStackTrace();
						return null;
					}
				} else if (btnJcteditor.getSelection()) {
					if (comboEditorInputSelector.getSelectionIndex() < 0) {
						IEditorReference bestEditor = getBestEditorReference();
						return new TextInputWithSource(retrieveTextForEditor(bestEditor), bestEditor);
					} else {
						IEditorReference currentlySelectedEditor = getCurrentlySelectedEditor();
						return new TextInputWithSource(retrieveTextForEditor(currentlySelectedEditor),
							currentlySelectedEditor);
					}
				} else {
					throw new RuntimeException("Not all input method cases covered at reading input text!");
				}
			}

			@Override
			public void writeContent(TextInputWithSource content) {
				if (content.getSourceType().equals(TextSourceType.FILE)) {
					setTextInputUIState(content, true);
				} else if (content.getSourceType().equals(TextSourceType.JCTEDITOR)) {
					setTextInputUIState(content, true);
				} else if (content.getSourceType().equals(TextSourceType.USERINPUT)) {
					setTextInputUIState(content, true);
				} else {
					throw new RuntimeException("not all cases covered in writeContent");
				}
			}

			@Override
			protected TextInputWithSource getDefaultContent() {
				if (initTextObject != null) { // preset from out of the wizard
												// exists
					if (!isEditorAvailable()) {
						if (initTextObject.getSourceType().equals(TextSourceType.JCTEDITOR)) {
							// mh... has to load text from editor, but it is not
							// available. what to do?
							// just default.
							return new TextInputWithSource(initTextObject.getText());
						} else {
							return initTextObject;
						}
					} else {
						return initTextObject;
					}
				} else { // no preset from out of the wizard
					if (!isEditorAvailable()) {
						return new TextInputWithSource("");
					} else {
						IEditorReference bestEditor = getBestEditorReference();
						return new TextInputWithSource(retrieveTextForEditor(bestEditor), bestEditor);
					}
				}
			}

			@Override
			public String getName() {
				return "Text";
			}
		};

		btnJcteditor.setEnabled(isEditorAvailable());

		btnDatei.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnDatei.getSelection()) {
					if (fileTextInput == null) fileTextInput = openFileSelectionDialogue();
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

		textOnlyInput.addObserver(textInput);
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
		fd.setText("Open input file");
		fd.setFilterPath(null);
		String[] filterExt = { "*.*" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		if (selected == null) return null;
		return new File(selected);
	}

	protected void setTextInputUIState(TextInputWithSource text, boolean writeText) {
		TextSourceType sourceType = text.getSourceType();
		String textString = text.getText();
		btnDatei.setSelection(sourceType.equals(TextSourceType.FILE));
		btnJcteditor.setSelection(sourceType.equals(TextSourceType.JCTEDITOR));
		btnZwischenablageeigeneEingabe.setSelection(sourceType.equals(TextSourceType.USERINPUT));
		if (writeText) {
			textOnlyInput.writeContent(textString);
			textOnlyInput.synchronizeWithUserSide();
		}

		showLoadFromEditorComponents(sourceType.equals(TextSourceType.JCTEDITOR));
		if (sourceType.equals(TextSourceType.JCTEDITOR)) {
			comboEditorInputSelector.select(editorRefs.indexOf(text.editorReference));
		}
		if (sourceType.equals(TextSourceType.FILE)) {
			showLoadFromFileComponents(sourceType.equals(TextSourceType.FILE), text.file.getName());
			// fileTextInput is somewhat the representation of the file
			// selection wizard UI, so this fits here
			fileTextInput = text.file;
		} else {
			showLoadFromFileComponents(sourceType.equals(TextSourceType.FILE), "");
		}

		txtInputText.setEditable(sourceType.equals(TextSourceType.USERINPUT));
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

	protected String getTextFromFile(File file) throws FileNotFoundException {
		FileInputStream is = new FileInputStream(file);
		return inputStreamToString(is);
	}

	private String retrieveTextForEditor(IEditorReference bestEditor) {
		InputStream is = EditorsManager.getInstance().getContentInputStream(bestEditor.getEditor(false));
		return inputStreamToString(is);
	}

	protected List<IEditorReference> getEditorsNotNecessarilyFresh() {
		return editorRefs;
	}

	private static IEditorReference getBestEditorReference() {
		List<IEditorReference> editorReferences = EditorsManager.getInstance().getEditorReferences();
		if (editorReferences.size() > 0) {
			IEditorReference activeEditor = EditorsManager.getInstance().getActiveEditorReference();
			if (activeEditor != null) {
				return activeEditor;
			} else {
				return editorReferences.get(0);
			}
		} else {
			return null;
		}
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

	private boolean isEditorAvailable() {
		return editorRefs.size() > 0;
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
		transpositionTable1.setText(pageConfig.getText().getText(), pageConfig.getColumnCount(), !pageConfig.isCrop(),
			pageConfig.getCropLength());
		lastPreviewedPageConfig = pageConfig;
	}

	/**
	 * reads the current value from an input stream
	 * 
	 * @param in
	 *            the input stream
	 */
	private String inputStreamToString(InputStream in) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
		} catch (UnsupportedEncodingException e1) {
			LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e1);
		}

		StringBuffer myStrBuf = new StringBuffer();
		int charOut = 0;
		String output = ""; //$NON-NLS-1$
		try {
			while ((charOut = reader.read()) != -1) {
				myStrBuf.append(String.valueOf((char) charOut));
			}
		} catch (IOException e) {
			LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e);
		}
		output = myStrBuf.toString();
		return output;
	}

	private void setReadInDirection(boolean dir) {
		if (directionChooserIn != null) {
			directionChooserIn.setDirection(dir);
		} else {
			this.iniDirection = dir;
		}
	}

	/**
	 * @param initBlocklength
	 *            the init_blocklength to set
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
	 * @param initCroptext
	 *            the init_croptext to set
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
	 * @param initCroplength
	 *            the init_croplength to set
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
	}

	private void parttextCountWidgetSelected(SelectionEvent evt) {
		preview();
	}

	private void parttextCheckWidgetSelected(SelectionEvent evt) {
		parttextCount.setEnabled(parttextCheck.getSelection());
		if (blocklengthSpinner.getSelection() > 0 && parttextCheck.getSelection()) doAutoCheckbox = false;
		if (evt != null) preview();
	}

	private void blocklengthSpinnerWidgetSelected(SelectionEvent evt) {
		if (doAutoCheckbox) parttextCheck.setSelection(blocklengthSpinner.getSelection() == 0);
		parttextCheckWidgetSelected(null);
		preview();
	}

	public boolean isPageBuilt() {
		return isPageBuilt;
	}

	public PageConfiguration getPageConfiguration() {
		return new PageConfiguration(textInput.getContent(), blocklengthSpinner.getSelection(),
			parttextCheck.getSelection(), parttextCount.getSelection(), directionChooserIn.getInput().getContent());
	}

}
