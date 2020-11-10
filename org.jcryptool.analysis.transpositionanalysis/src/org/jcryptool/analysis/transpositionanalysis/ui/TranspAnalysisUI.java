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
package org.jcryptool.analysis.transpositionanalysis.ui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizard;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage.PageConfiguration;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.core.util.input.ButtonInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.core.util.ui.auto.LayoutAdvisor;
import org.jcryptool.core.util.ui.auto.SmoothScroller;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionTable;
import org.jcryptool.crypto.classic.transposition.ui.TranspositionKeyInputWizard;
import org.jcryptool.crypto.classic.transposition.ui.TranspositionKeyInputWizardPage;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;
import org.jcryptool.crypto.ui.textsource.TextInputWithSourceDisplayer;
import org.jcryptool.crypto.ui.textsource.TextInputWithSourceDisplayer.Style;
import org.jcryptool.editor.text.JCTTextEditorPlugin;

public class TranspAnalysisUI extends Composite implements Observer {

	private TranspositionTableComposite transpTable;

	private Group instrGroup;
	private TextInputWithSource text = null;
	private Composite composite7;
	private Label labelReadDir;
	private Link linkExport;
	private Composite composite8;
	private Button btnDecipher;
	private Label textpreviewDescription;
	private Group previewGroup;
	private Text previewText;
	private Composite compResults;
	private Group compTable;
	private Text labelKeypreview;
	private Composite composite4;
	private Label keyfound;
	private boolean crop = true;
	private int croplength = 80;
	private int blocklength = 0;

	private boolean doAutoCrop = true;

	private TranspTextWizard textWizard;
	private WizardDialog dialog;
	private TranspositionKey keyUsedToEncrypt;
	private ReadDirectionChooser readoutDirChooser;
	private boolean readInMode = false;
	private PageConfiguration textPageConfiguration;
	private Composite compLoadTextBtn;
	private Button btnOpenTextWizard;
	private Composite compInstructions;
	private Label lblTryTo;
	private Label lblTryToRecover;
	private Label lblHoweverTheColumn;
	private Label lblColumnCount;
	private Spinner spinner;
	private Composite compTextSource;
	private Label label;
	private Label lblYouHaveSelected;
	private TextInputWithSourceDisplayer sourceDisplayer;

	private Link linkChooseText;
	private Link linkSetKey;
	private Label label_1;

	private String ownKeyInputString;
	private Label lblLoadedTextwith;

	private String lastPreviewedText;
	private Label lblParameters;

	private Composite compReadDir;

	private ReadDirectionChooser readinDirChooser;

	private Composite compApplyTransform;
	private Button btnCheckButton;

	private ButtonInput applyTransformationInput;
	private Label lblTheseTextSettings;

	private Composite compSolvableWarning;

	// composites to enable scrolling
	private ScrolledComposite scrolledComposite;
	private Composite content;

	private Label lblLoadA;

	/**
	 * @param text the text to set
	 */
	public void setText(TextInputWithSource text) {
		setText(text, true);
	}

	private void setText(TextInputWithSource text, boolean refresh) {
		this.text = text;
		if (refresh)
			transpTable.setText(calcText());
		checkIsSolvableWarningNeeded();
	}

	/**
	 * @param crop the crop to set
	 */
	public void setCrop(boolean crop) {
		setCrop(crop, true);
	}

	public void setCrop(boolean crop, boolean refresh) {
		this.crop = crop;
		if (refresh)
			transpTable.setText(calcText(), blocklength, !crop, croplength);
	}

	/**
	 * @param croplength the croplength to set
	 */
	public void setCroplength(int croplength) {
		setCroplength(croplength, true);
	}

	public void setCroplength(int croplength, boolean refresh) {
		this.croplength = croplength;
		if (refresh)
			transpTable.setText(calcText(), blocklength, !crop, croplength);
	}

	public void setColumnOrder(int[] order) {
		transpTable.setColumnOrder(order);
		columnsReordered(transpTable.getColumnOrder());
	}

	/**
	 * @param blocklength the blocklength to set
	 */
	public void setBlocklength(int blocklength) {
		setBlocklength(blocklength, true);
	}

	public void setBlocklength(int blocklength, boolean refresh) {
		this.blocklength = blocklength;
		spinner.setSelection(blocklength);

		if (refresh) {
			transpTable.setColumnCount(blocklength);
			columnsReordered(transpTable.getColumnOrder());
		}
	}

	protected void setReadInMode(boolean readInDirection, boolean refresh) {
		this.readInMode = readInDirection;
		if (textPageConfiguration != null)
			textPageConfiguration.setReadInMode(readInDirection);
		checkIsSolvableWarningNeeded();
		if (refresh) {
			transpTable.setReadInOrder(readInMode);
			previewPlaintext();
		}
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */

	public TranspAnalysisUI(Composite parent, int style) {
		super(parent, style);

		initGUI();
	}

	private void initGUI() {
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		this.setLayout(gridLayout);

		scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		content = new Composite(scrolledComposite, SWT.NONE);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,1));
		content.setLayout(new GridLayout(1, true));
		
		TitleAndDescriptionComposite td = new TitleAndDescriptionComposite(content);
		td.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		td.setTitle(Messages.TranspAnalysisUI_view_title);
		td.setDescription(Messages.TranspAnalysisUI_view_description);

		instrGroup = new Group(content, SWT.NONE);
		instrGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		instrGroup.setLayout(new GridLayout(1, false));

		compLoadTextBtn = new Composite(instrGroup, SWT.NONE);
		compLoadTextBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		GridLayout compLoadTextBtnLayout = new GridLayout(2, false);
		compLoadTextBtnLayout.marginHeight = 3;
		compLoadTextBtn.setLayout(compLoadTextBtnLayout);

		lblLoadA = new Label(compLoadTextBtn, SWT.PUSH);
		lblLoadA.setText("1)"); //$NON-NLS-1$

		btnOpenTextWizard = new Button(compLoadTextBtn, SWT.PUSH);
		btnOpenTextWizard.setLayoutData(new GridData(SWT.PUSH, SWT.CENTER, false, false, 1, 1));
		btnOpenTextWizard.setText(Messages.TranspAnalysisUI_btnOpenTextWizard_text);
		btnOpenTextWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mainButton();
			}
		});

		// This is the alternative composite that is shown when a text was loaded
		compTextSource = new Composite(instrGroup, SWT.NONE);
		compTextSource.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		GridLayout compTextSourceLayout = new GridLayout(3, false);
		compTextSourceLayout.verticalSpacing = 2;
		compTextSourceLayout.marginHeight = 3;
		compTextSource.setLayout(compTextSourceLayout);

		label = new Label(compTextSource, SWT.NONE);
		label.setText("1)"); //$NON-NLS-1$

		lblYouHaveSelected = new Label(compTextSource, SWT.PUSH);
		lblYouHaveSelected.setText(Messages.TranspAnalysisUI_lblYouHaveSelected_text);

		sourceDisplayer = new TextInputWithSourceDisplayer(compTextSource, this, new TextInputWithSource("abcdef"), //$NON-NLS-1$
				new Style(true)); // $hide$
		GridData sourceDisplayerLData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		sourceDisplayerLData.horizontalIndent = 3;
		sourceDisplayer.setLayoutData(sourceDisplayerLData); // $hide$

		new Label(compTextSource, SWT.None);

		linkChooseText = new Link(compTextSource, SWT.PUSH);
		linkChooseText.setText(Messages.TranspAnalysisUI_lblChooseAnotherText);
		linkChooseText.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 2, 1));
		linkChooseText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mainButton();
			}
		});

		compInstructions = new Composite(instrGroup, SWT.NONE);
		compInstructions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		GridLayout compInstructionsLayout = new GridLayout(2, false);
		compInstructionsLayout.marginHeight = 3;
		compInstructions.setLayout(compInstructionsLayout);

		lblTryTo = new Label(compInstructions, SWT.NONE);
		lblTryTo.setBounds(0, 0, 55, 15);
		lblTryTo.setText("2)"); //$NON-NLS-1$

		lblTryToRecover = new Label(compInstructions, SWT.NONE);
		lblTryToRecover.setText(Messages.TranspAnalysisUI_lblTryToRecover_text);

		new Label(compInstructions, SWT.NONE);

		lblHoweverTheColumn = new Label(compInstructions, SWT.WRAP);

		GridData gd_lblHoweverTheColumn = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lblHoweverTheColumn.widthHint = 600;
		lblHoweverTheColumn.setLayoutData(gd_lblHoweverTheColumn);

		lblHoweverTheColumn.setText(Messages.TranspAnalysisUI_lblHoweverTheColumn_text);

		compTable = new Group(content, SWT.NONE);
		GridLayout gl_compTable = new GridLayout();
		gl_compTable.makeColumnsEqualWidth = true;
		compTable.setText(Messages.TranspAnalysisUI_grpEditText);
		compTable.setFont(FontService.getLargeFont());
		compTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		compTable.setLayout(gl_compTable);

		compApplyTransform = new Composite(compTable, SWT.NONE);
		GridLayout compApplyTransformLayout = new GridLayout(1, false);
		compApplyTransformLayout.marginWidth = 0;
		compApplyTransformLayout.marginHeight = 0;
		compApplyTransform.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		compApplyTransform.setLayout(compApplyTransformLayout);

		btnCheckButton = new Button(compApplyTransform, SWT.CHECK | SWT.WRAP);
		btnCheckButton.setText(String.format(Messages.TranspAnalysisUI_btnCheckButton_text, "")); //$NON-NLS-1$
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		layoutData.widthHint = 100;
		btnCheckButton.setLayoutData(layoutData);

		applyTransformationInput = new ButtonInput() {

			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}

			@Override
			public String getName() {
				return "ApplyTransformation"; //$NON-NLS-1$
			}

			@Override
			protected Boolean getDefaultContent() {
				return false;
			}

			@Override
			public Button getButton() {
				return btnCheckButton;
			}
		};

		applyTransformationInput.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				refreshTable();
				previewPlaintext();
			}
		});

		compReadDir = new Composite(compTable, SWT.NONE);
		GridLayout compReadDirLayout = new GridLayout(2, false);
		compReadDirLayout.marginWidth = 0;
		compReadDirLayout.marginHeight = 0;
		compReadDir.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		compReadDir.setLayout(compReadDirLayout);

		labelReadDir = new Label(compReadDir, SWT.NONE);
		labelReadDir.setText(Messages.TranspAnalysisUI_lblReadinmode);

		readinDirChooser = new ReadDirectionChooser(compReadDir);
		readinDirChooser.setDirection(TranspositionTable.DIR_ROWWISE);
		readinDirChooser.setEnabled(false);
		readinDirChooser.getInput().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if (arg == null) {
					setReadInMode(readinDirChooser.getInput().getContent(), true);
				}
			}
		});

		labelReadDir = new Label(compReadDir, SWT.NONE);
		labelReadDir.setText(Messages.TranspAnalysisUI_read_out_mode_label);

		readoutDirChooser = new ReadDirectionChooser(compReadDir);
		readoutDirChooser.setDirection(TranspositionTable.DIR_ROWWISE);
		readoutDirChooser.setEnabled(false);
		readoutDirChooser.getInput().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if (arg == null) {
					previewPlaintext();
					checkIsSolvableWarningNeeded();
				}
			}
		});

		lblColumnCount = new Label(compReadDir, SWT.NONE);
		lblColumnCount.setText(Messages.TranspAnalysisUI_lblColumnCount_text_1);

		spinner = new Spinner(compReadDir, SWT.BORDER);
		spinner.setMaximum(1000);
		spinner.setMinimum(1);
		spinner.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 1));
		spinner.setEnabled(false);
		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinnerSelected();
			}
		});

		compSolvableWarning = new Composite(compTable, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;

		compSolvableWarning.setLayout(layout);
		compSolvableWarning.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Label lblSolvableIcon = new Label(compSolvableWarning, SWT.NONE);
		lblSolvableIcon.setImage(ImageService.ICON_WARNING);

		lblTheseTextSettings = new Label(compSolvableWarning, SWT.WRAP);
		lblTheseTextSettings.setText(Messages.TranspAnalysisUI_lblTheseTextSettings_text);
		layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		layoutData.widthHint = compSolvableWarning.getClientArea().x;
		lblTheseTextSettings.setLayoutData(layoutData);

		lblLoadedTextwith = new Label(compTable, SWT.NONE);
		lblLoadedTextwith.setText(Messages.TranspAnalysisUI_lblLoadedTextwith_text);

		GridData transpTableLData = new GridData(SWT.FILL, SWT.FILL, true, true);
		transpTableLData.grabExcessHorizontalSpace = true;
		transpTableLData.minimumHeight = 150;
		transpTableLData.heightHint = 150;
		transpTable = new TranspositionTableComposite(compTable, SWT.NONE);
		transpTable.setLayoutData(transpTableLData);
		transpTable.setColReorderObserver(this);

		compResults = new Composite(content, SWT.NONE);
		GridLayout gl_compResults = new GridLayout();
		gl_compResults.marginHeight = 0;
		gl_compResults.marginWidth = 0;
		compResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		compResults.setLayout(gl_compResults);

		previewGroup = new Group(compResults, SWT.NONE);
		previewGroup.setLayout(new GridLayout());
		previewGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		previewGroup.setText(Messages.TranspAnalysisUI_Results);
		previewGroup.setFont(FontService.getLargeFont());

		composite4 = new Composite(previewGroup, SWT.NONE);
		GridLayout composite4Layout = new GridLayout();
		composite4Layout.marginWidth = 0;
		composite4Layout.marginHeight = 0;
		composite4.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		composite4.setLayout(composite4Layout);

		keyfound = new Label(composite4, SWT.NONE);
		GridData label2LData = new GridData();
		label2LData.grabExcessHorizontalSpace = true;
		keyfound.setLayoutData(label2LData);
		keyfound.setText(Messages.TranspAnalysisUI_keyfound);

		composite8 = new Composite(composite4, SWT.NONE);
		GridLayout composite8Layout = new GridLayout();
		composite8Layout.numColumns = 4;
		composite8Layout.marginWidth = 0;
		composite8Layout.marginHeight = 0;

		GridData composite8LData = new GridData();
		composite8LData.grabExcessHorizontalSpace = true;
		composite8LData.horizontalAlignment = GridData.FILL;
		composite8.setLayoutData(composite8LData);
		composite8.setLayout(composite8Layout);

		labelKeypreview = new Text(composite8, SWT.BORDER);
		GridData label3LData = new GridData();
		label3LData.grabExcessHorizontalSpace = true;
		label3LData.horizontalAlignment = GridData.FILL;
		labelKeypreview.setLayoutData(label3LData);
		labelKeypreview.setText(""); //$NON-NLS-1$
		labelKeypreview.setEditable(false);

		linkSetKey = new Link(composite8, SWT.NONE);
		linkSetKey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TranspositionKeyInputWizard wiz = new TranspositionKeyInputWizard();
				dialog = new WizardDialog(TranspAnalysisUI.this.getShell(), wiz);

				TranspositionKey oldKey = getKeyUsedToEncrypt();
				wiz.setPageConfig(new TranspositionKeyInputWizardPage.PageConfiguration(oldKey, ownKeyInputString));

				int dialogResult = dialog.open();

				if (dialogResult == Window.OK) {
					TranspositionKey key = wiz.getPageConfig().getKey();
					spinner.setSelection(key.getLength());
					spinnerSelected();
					transpTable.setColumnOrder(key.toArray());
					columnsReordered(transpTable.getColumnOrder());
					ownKeyInputString = wiz.getPageConfig().getInputString();
				}
			}
		});
		linkSetKey.setText(Messages.TranspAnalysisUI_link_text);
		linkSetKey.setEnabled(false);

		label_1 = new Label(composite8, SWT.NONE);
		label_1.setText("|"); //$NON-NLS-1$

		linkExport = new Link(composite8, SWT.NONE);
		linkExport.setText("<a>" + Messages.TranspAnalysisUI_Export + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		linkExport.setEnabled(false);
		linkExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				if (keyUsedToEncrypt != null) {
					KeyViewer myKeyViewer = new KeyViewer(getShell(), Messages.TranspAnalysisUI_keyviewer,
							keyUsedToEncrypt);
					myKeyViewer.open();
				}
			}
		});

		lblParameters = new Label(composite4, SWT.WRAP);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		data.widthHint = composite4.getClientArea().x;
		lblParameters.setLayoutData(data);

		composite7 = new Composite(composite4, SWT.NONE);
		GridLayout composite7Layout = new GridLayout();
		composite7Layout.marginHeight = 0;
		composite7Layout.marginWidth = 0;

		GridData composite7LData = new GridData(SWT.FILL, SWT.FILL, true, false);
		composite7LData.verticalIndent = 12;
		composite7.setLayoutData(composite7LData);
		composite7.setLayout(composite7Layout);

		textpreviewDescription = new Label(composite7, SWT.NONE);
		textpreviewDescription.setText(Messages.TranspAnalysisUI_preview);

		previewText = new Text(previewGroup, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		GridData previewTextLData = new GridData(SWT.FILL, SWT.FILL, true, true);
		previewTextLData.minimumHeight = 100;
		//previewTextLData.heightHint = 220;
		previewTextLData.widthHint = 600;
		previewText.setLayoutData(previewTextLData);
		previewText.setEditable(false);

		btnDecipher = new Button(previewGroup, SWT.PUSH | SWT.CENTER);
		GridData btnDecipherLData = new GridData(SWT.CENTER, SWT.FILL, false, false);
		btnDecipher.setLayoutData(btnDecipherLData);
		btnDecipher.setText(Messages.TranspAnalysisUI_decipher);
		btnDecipher.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				decipherIntoEditor();
			}
		});
		btnDecipher.setEnabled(false);
		btnDecipher.setImage(
				JCTTextEditorPlugin.getDefault().getImageRegistry().get(JCTTextEditorPlugin.JCT_TEXT_EDITOR_ICON));

		// hide source display
		displaySolvableWarningLabel(false, false);
		displayTextTransformBtn(false, false, new TransformData());
		displayTextSource(null, false, false);

		scrolledComposite.setContent(content);
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        SmoothScroller.scrollSmooth(scrolledComposite);
        LayoutAdvisor.addPreLayoutRootComposite(content);
		
	}

	protected TranspositionKey getKeyUsedToEncrypt() {
		return keyUsedToEncrypt;
	}

	private void spinnerSelected() {
		ownKeyInputString = null;

		if (doAutoCrop) {
			if (spinner.getSelection() > 0) {
				// if blocklength is greater than zero, make the text now
				// uncropped

				setCrop(false, false);
				setBlocklength(spinner.getSelection(), false);
				refreshTable();
			} else {
				// if blocklength is zero, make the text now cropped

				setCrop(true, false);
				setBlocklength(spinner.getSelection(), false);
				refreshTable();
			}
		} else {
			setBlocklength(spinner.getSelection(), true);
		}

		if (transpTable != null && !transpTable.isDisposed()) {
			columnsReordered(transpTable.getColumnOrder());
		}

		// Recalculate the size of the scrolled composite after the warning is
		// displayed.
		// This avoids text being cut of at the bottom of the plugin.
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		content.layout();

	}

	private void mainButton() {
		setTableTextWithWizard();
	}

	private void setTableTextWithWizard() {
		textWizard = new TranspTextWizard();
		dialog = new WizardDialog(this.getShell(), textWizard);

		if (textPageConfiguration != null) { // if not initial state (already
												// have loaded a text)
			textWizard.setTextPageConfig(textPageConfiguration);
		}
		int result = dialog.open();

		if (result == Window.OK) {
			
			textPageConfiguration = textWizard.getTextPageConfig();	
			textPageConfiguration.getText().setText(makeShortenedTextForTablePreview(textPageConfiguration.getText().getText(), true));
			setText(textPageConfiguration.getText(), false);
			setReadInMode(textPageConfiguration.getReadInDirection(), false);
			readinDirChooser.setDirection(textPageConfiguration.getReadInDirection());
			setCrop(textPageConfiguration.isCrop(), false);
			setCroplength(textPageConfiguration.getCropLength(), false);
			setBlocklength(textPageConfiguration.getColumnCount(), false);
			
			displayTextTransformBtn(false, true, new TransformData());
			applyTransformationInput.writeContent(false);
			
			applyTransformationInput.synchronizeWithUserSide();

			doAutoCrop = (blocklength > 0 && !crop || blocklength == 0 && crop);

			transpTable.setReadInOrder(readInMode, false);
			transpTable.setText(calcText(), blocklength, !crop, croplength);

			setColumnOrder(textPageConfiguration.getColumnOrder());
			columnsReordered(transpTable.getColumnOrder());

			displayTextSource(textPageConfiguration.getText(), true, true);

			spinner.setEnabled(true);
			linkSetKey.setEnabled(true);
			linkExport.setEnabled(true);
			readoutDirChooser.setEnabled(true);
			readinDirChooser.setEnabled(true);

			// Recalc size after new line added
			
			
			//scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

			// This makes the ScrolledComposite scrolling, when the mouse
			// is on a Text with one or more of the following tags: SWT.READ_ONLY,
			// SWT.V_SCROLL or SWT-H_SCROLL.
			// SmoothScroller.scrollSmooth(scrolledComposite);
			// LayoutAdvisor.addPreLayoutRootComposite(scrolledComposite);
			scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			content.layout();

		}
	}
	
	private String makeShortenedTextForTablePreview(String textString, boolean shortenIfNecessary) {
		// TODO Auto-generated method stub
		boolean makePreview = shortenIfNecessary && textString.length() > 30000;
		String previewText = textString;
		if (makePreview) {
			previewText = textString.subSequence(0, Math.min(textString.length(), 30000)).toString();
			return previewText;
		}
		return previewText;
	}
	

	/**
	 * switch load text / show text source composites.
	 *
	 * @param text2  if the source composite is displayed: what should be displayed
	 *               there. (else this parameter is mute)
	 * @param show   whether to show the source composite (true) or the load Text
	 *               button (false)
	 * @param layout whether to layout after this operation (recommended if another
	 *               layout of master composite does not follow directly anyway)
	 */
	private void displayTextSource(TextInputWithSource text, boolean show, boolean layout) {

		((GridData) compTextSource.getLayoutData()).exclude = !show;
		((GridData) compLoadTextBtn.getLayoutData()).exclude = show;

		compTextSource.setVisible(show);
		compLoadTextBtn.setVisible(!show);

		if (show) {
			sourceDisplayer.setText(text);
		}

		if (layout) {
			this.layout(new Control[] { compTextSource, compLoadTextBtn });
		}
	}

	private void displayTextTransformBtn(boolean b, boolean layout, TransformData transformData) {
		((GridData) compApplyTransform.getLayoutData()).exclude = !b;
		compApplyTransform.setVisible(b);

		if (b) {
			btnCheckButton.setText(
					String.format(Messages.TranspAnalysisUI_btnCheckButton_text, transformData.toReadableString()));
		}

		if (layout) {
			this.layout(new Control[] { compApplyTransform });
		}
	}

	private void displaySolvableWarningLabel(boolean b, boolean layout) {
		((GridData) compSolvableWarning.getLayoutData()).exclude = !b;
		compSolvableWarning.setVisible(b);

		if (layout) {
			this.layout(new Control[] { compApplyTransform });
		}
	}

	private String calcText() {
		return Transform.transformText(text.getText(),
				// applyTransformationInput.getContent() ? textTransformData :
				new TransformData());
	}

	private void refreshTable() {
		if (text != null) {
			transpTable.setReadInOrder(readinDirChooser.getInput().getContent(), false);
			transpTable.setText(calcText(), blocklength, !crop, croplength);
		}

		checkIsSolvableWarningNeeded();
	}

	private void columnsReordered(int[] cols) {
		ownKeyInputString = null;
		int[] order;
		if (cols.length != this.croplength)
			order = cols.clone();
		else
			order = null;
		setKeyUsedToEncrypt(order);

		if (transpTable != null) {
			previewPlaintext();
		}

		checkIsSolvableWarningNeeded();
	}

	private void checkIsSolvableWarningNeeded() {
		boolean needed = false;
		if (isCipherParametersComplete()) {
			if (/*
				 * readoutDirChooser.getInput().getContent() ==
				 * TranspositionTable.DIR_COLUMNWISE ||
				 */
			readinDirChooser.getInput().getContent() == TranspositionTable.DIR_COLUMNWISE) {

				if (text.getText().length() % getKeyUsedToEncrypt().getLength() != 0) {
					needed = true;
				}
			}
		}

		displaySolvableWarningLabel(needed, true);
	}

	private void previewPlaintext() {
		if (transpTable != null) {

			if (isCipherParametersComplete()) {
				TranspositionTable table = new TranspositionTable(transpTable.getColumnCountDisplayed());

				table.fillCharsIntoTable(calcText().toCharArray(), readinDirChooser.getInput().getContent());
				table.transposeColumns(getKeyUsedToEncrypt().getReverseKey());
				lastPreviewedText = String.valueOf(table.readOutContent(readoutDirChooser.getInput().getContent()));
				previewText.setText(lastPreviewedText);
				btnDecipher.setEnabled(true);
				
				String maskParams = Messages.TranspAnalysisUI_lbl_params_mask;
				String paramsLblText = Messages.TranspAnalysisUI_lblNewLabel_1_text + String.format(maskParams,
						TranspositionTable.readDirectionToString(readoutDirChooser.getInput().getContent()),
						TranspositionTable.readDirectionToString(readinDirChooser.getInput().getContent()));
				lblParameters.setText(paramsLblText);
				lblParameters.setToolTipText(Messages.TranspAnalysisUI_tooltipParams);
				this.layout(new Control[] { lblParameters });
			}
		} else {
			btnDecipher.setEnabled(false);
		}
	}

	private boolean isCipherParametersComplete() {
		return text != null && readoutDirChooser != null && readinDirChooser != null && keyUsedToEncrypt != null
				&& keyUsedToEncrypt.getLength() > 0;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof int[]) {
			columnsReordered((int[]) arg1);
		}
	}

	/**
	 * @param actualKey the actualKey to set
	 */
	public void setKeyUsedToEncrypt(int[] key) {
		int actualKeyLength;
		if (key != null) {
			this.keyUsedToEncrypt = new TranspositionKey(key);
			labelKeypreview.setText(" " + keyUsedToEncrypt.toStringOneRelative() + "|"); //$NON-NLS-1$ //$NON-NLS-2$

//			btnDecipher.setEnabled(true);
			actualKeyLength = keyUsedToEncrypt.getLength();
			labelKeypreview.setEnabled(true);
		} else {
			this.keyUsedToEncrypt = null;
//			btnDecipher.setEnabled(false);
			labelKeypreview.setText(Messages.TranspAnalysisUI_keypreview_zerocolumns);

			actualKeyLength = 0;
			labelKeypreview.setEnabled(false);
		}

		boolean changeLength = (actualKeyLength != this.blocklength);
		if (changeLength)
			setBlocklength(actualKeyLength);
	}

	private void decipherIntoEditor() {
		// mode: not using the transposition algorithm for now because of special
		// characters issue
		TextInputWithSourceDisplayer.openTextInEditor(lastPreviewedText, "deciphered_transposition"); //$NON-NLS-1$
		return;
//
//		final String TRANSPOSITION_ALGORITHM = "org.jcryptool.crypto.classic.transposition.algorithm"; //$NON-NLS-1$
//
//		OperationsPlugin op = OperationsPlugin.getDefault();
//		IAction[] actions = op.getAlgorithmsManager().getShadowAlgorithmActions();
//		for (final IAction action : actions) {
//			if (TRANSPOSITION_ALGORITHM.equals(action.getId())) {
//				ClassicDataObject myDO = new ClassicDataObject();
//				AbstractAlphabet ascii = AlphabetsManager.getInstance().getAlphabetByName("Printable ASCII"); //$NON-NLS-1$
//				myDO.setAlphabet(ascii);
//				char[] key = new char[keyUsedToEncrypt.getLength() + 2];
//				key[0] = getInputMethodDecryption() ? ascii.getCharacterSet()[0] : ascii.getCharacterSet()[1];
//				key[1] = getOutputMethodDecryption() ? ascii.getCharacterSet()[0] : ascii.getCharacterSet()[1];
//				System.arraycopy(keyUsedToEncrypt.toUnformattedChars(ascii).toCharArray(), 0, key, 2,
//					keyUsedToEncrypt.getLength());
//				myDO.setKey(key);
//				myDO.setKey2("".toCharArray()); //$NON-NLS-1$
//				try {
//					myDO.setInputStream(new BufferedInputStream(new ByteArrayInputStream(calcText().getBytes(
//						IConstants.UTF8_ENCODING))));
//				} catch (UnsupportedEncodingException e) {
//					LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e);
//				}
//				myDO.setOpmode(AbstractAlgorithm.DECRYPT_MODE);
//				myDO.setFilterNonAlphaChars(true);
//				myDO.setTransformData(new TransformData());
//
//				((ShadowAlgorithmAction) action).run(myDO);
//				break;
//			}
//		}
	}
}