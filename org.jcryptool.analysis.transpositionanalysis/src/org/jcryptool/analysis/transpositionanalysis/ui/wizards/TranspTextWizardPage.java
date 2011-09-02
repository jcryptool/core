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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.transpositionanalysis.TranspositionAnalysisPlugin;
import org.jcryptool.analysis.transpositionanalysis.ui.ReadDirectionChooser;
import org.jcryptool.analysis.transpositionanalysis.ui.TranspositionTableComposite;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;

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
public class TranspTextWizardPage extends WizardPage implements Listener {
	private Group group1;
	private Spinner blocklengthSpinner;
	private Label label1;
	private Combo editorChooser;
	private Composite editorChooseComp;
	private ReadDirectionChooser directionChooserIn;
	private Label labelReadIn;
	private Group groupReadInDirection;
	private Button bottonOwnText;
	private Label labelOr;
	private Composite fillcomposite;
	private Button chooseEditorButton;
	private Group previewGroup;
	private Label label3;
	private Composite textfieldComp;
	private TranspositionTableComposite transpositionTable1;
	private Text analysisText;
	private Group group2;
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
	private String init_text;

	private boolean doAutoCheckbox = true;
	private List<IEditorReference> editorRefs;

	/**
	 * state 0 ~ pure loaded file <br />
	 * state 1 ~ file + manual input <br />
	 * state 2 ~ pure manual input (either click delete all button or delete
	 * text in one chunk manually) <br />
	 */
	private int manualInputState = 0;
	private String fileName = ""; //$NON-NLS-1$
	private String previousText;
	private boolean iniDirection = false;

	/**
	 * Creates a new instance of TranspTextWizardPage.
	 *
	 **/
	public TranspTextWizardPage() {
		super(Messages.TranspTextWizardPage_textwizardtitle, "", null); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
		setTitle(Messages.TranspTextWizardPage_pagetitle);
		setMessage(Messages.TranspTextWizardPage_pagedescription);
	}

	private void fillEditorChooser() {
		editorRefs = EditorsManager.getInstance().getEditorReferences();
		IEditorReference activeReference = EditorsManager.getInstance()
				.getActiveEditorReference();

		editorChooser.setText(""); //$NON-NLS-1$
		for (int i = 0; i < editorRefs.size(); i++) {
			editorChooser.add(editorRefs.get(i).getTitle());
			if (activeReference.equals(editorRefs.get(i))) {
				editorChooser.setText(editorRefs.get(i).getTitle());
				editorChooser.select(i);
			}
		}
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
		Composite pageComposite = new Composite(parent, SWT.NULL);
		pageComposite.setLayout(pageCompositeLayout);
		pageComposite.setLayoutData(pageCompositeLayoutData);

		{
			group2 = new Group(pageComposite, SWT.NONE);
			GridLayout group2Layout = new GridLayout();
			group2Layout.makeColumnsEqualWidth = true;
			group2.setLayout(group2Layout);
			GridData group2LData = new GridData();
			group2LData.grabExcessHorizontalSpace = true;
			group2LData.horizontalAlignment = GridData.FILL;
			group2.setLayoutData(group2LData);
			group2.setText(Messages.TranspTextWizardPage_text);
			{
				textfieldComp = new Composite(group2, SWT.NONE);
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
					analysisText = new Text(textfieldComp, SWT.MULTI | SWT.WRAP
							| SWT.V_SCROLL | SWT.BORDER);
					GridData text1LData = new GridData();
					text1LData.grabExcessHorizontalSpace = true;
					text1LData.horizontalAlignment = GridData.FILL;

					GC temp = new GC(analysisText);
					int lines = 6;
					int charHeight = temp.getFontMetrics().getAscent() + 2
							* temp.getFontMetrics().getLeading();
					int height = lines * charHeight;
					temp.dispose();
					text1LData.widthHint = 400;
					text1LData.heightHint = height;

					analysisText.setLayoutData(text1LData);
					analysisText.setText(init_text);

					analysisText.addModifyListener(new ModifyListener() {
						public void modifyText(ModifyEvent evt) {
							textFieldChanged(previousText,
									analysisText.getText());
							previousText = analysisText.getText();
						}
					});
				}
			}
			{
				editorChooseComp = new Composite(group2, SWT.NONE);
				GridLayout composite3Layout = new GridLayout();
				composite3Layout.numColumns = 4;
				composite3Layout.marginHeight = 0;
				composite3Layout.marginWidth = 0;
				GridData composite3LData = new GridData();
				composite3LData.grabExcessHorizontalSpace = true;
				composite3LData.horizontalAlignment = GridData.FILL;
				editorChooseComp.setLayoutData(composite3LData);
				editorChooseComp.setLayout(composite3Layout);
				{
					editorChooser = new Combo(editorChooseComp, SWT.NONE);
					GridData combo1LData = new GridData();
					combo1LData.horizontalAlignment = GridData.FILL;
					combo1LData.grabExcessHorizontalSpace = true;
					editorChooser.setLayoutData(combo1LData);
					editorChooser.setText(""); //$NON-NLS-1$

					fillEditorChooser();
				}
				{
					chooseEditorButton = new Button(editorChooseComp, SWT.PUSH
							| SWT.CENTER);
					GridData chooseEditorButtonLData = new GridData();
					chooseEditorButton.setLayoutData(chooseEditorButtonLData);
					chooseEditorButton
							.setText(Messages.TranspTextWizardPage_loadtextfromeditor);
					chooseEditorButton
							.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									if (editorRefs != null
											&& editorRefs.size() >= editorChooser
													.getItemCount()) {
										IEditorPart editor = editorRefs.get(editorChooser
												.getSelectionIndex())
												.getEditor(false);

										if (editor != null) {
											InputStream input = EditorsManager
													.getInstance()
													.getContentInputStream(
															editor);
											String text = InputStreamToString(input);
											setAnalysisText(text);
											fileName = editorChooser.getText();
											manualInputState = 0;
										} else {

										}
									}
								}
							});
				}
				{
					labelOr = new Label(editorChooseComp, SWT.NONE);
					GridData labelOrLData = new GridData();
					labelOr.setLayoutData(labelOrLData);
					labelOr.setText(Messages.TranspTextWizardPage_or);
				}
				{
					bottonOwnText = new Button(editorChooseComp, SWT.PUSH
							| SWT.CENTER);
					GridData bottonOwnTextLData = new GridData();
					bottonOwnText.setLayoutData(bottonOwnTextLData);
					bottonOwnText
							.setText(Messages.TranspTextWizardPage_inputowntext);
					bottonOwnText.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							inputOwnText();
						}
					});
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
				RowLayout composite5Layout = new RowLayout(
						org.eclipse.swt.SWT.HORIZONTAL);
				GridData composite5LData = new GridData();
				composite5LData.grabExcessHorizontalSpace = true;
				composite5LData.horizontalAlignment = GridData.FILL;
				parttextCompRow.setLayoutData(composite5LData);
				parttextCompRow.setLayout(composite5Layout);
				{
					parttextCheck = new Button(parttextCompRow, SWT.CHECK
							| SWT.LEFT);
					parttextCheck
							.setText(Messages.TranspTextWizardPage_takeonlyapartofthetext);
					parttextCheck.setSelection(init_croptext);

					parttextCheck.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							parttextCheckWidgetSelected(evt);
						}
					});

				}
				{
					RowData spinner2LData = new RowData();
					parttextCount = new Spinner(parttextCompRow, SWT.NONE);
					parttextCount.setLayoutData(spinner2LData);
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
					blocklengthSpinner
							.addSelectionListener(new SelectionAdapter() {
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
			groupReadInDirection
					.setText(Messages.TranspTextWizardPage_readinmode);
			{
				labelReadIn = new Label(groupReadInDirection, SWT.NONE);
				GridData labelReadInLData = new GridData();
				labelReadIn.setLayoutData(labelReadInLData);
				labelReadIn
						.setText(Messages.TranspTextWizardPage_readinmode_description);
			}
			{
				GridData directionChooserInLData = new GridData();
				directionChooserIn = new ReadDirectionChooser(
						groupReadInDirection, true);
				directionChooserIn.setLayoutData(directionChooserInLData);
				directionChooserIn.setDirection(iniDirection);

				directionChooserIn.getInput().addObserver(new Observer() {
					public void update(Observable o, Object arg) {
						if (arg == null)
							preview();
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
				transpositionTable1 = new TranspositionTableComposite(
						previewGroup, SWT.NONE);
				transpositionTable1.setLayout(transpTableLayout);
				transpositionTable1.setLayoutData(transpositionTable1LData);

			}
		}

		setControl(pageComposite);
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(getControl(),
						TranspositionAnalysisPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$

		setPageComplete(true);

		isPageBuilt = true;
		preview();
	}

	protected void inputOwnText() {
		analysisText.setText(""); //$NON-NLS-1$
		previousText = ""; //$NON-NLS-1$
		manualInputState = 2;
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
		String chosenText = getText();

		int blockL = getBlocksize();

		transpositionTable1.setReadInOrder(directionChooserIn.getInput()
				.getContent());
		transpositionTable1.setText(chosenText, blockL, !getCrop(),
				getCropsize());
	}

	public void handleEvent(Event event) {
	}

	/**
	 * reads the current value from an input stream
	 *
	 * @param in
	 *            the input stream
	 */
	private String InputStreamToString(InputStream in) {
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

	public String getText() {
		return analysisText.getText();
	}

	public int getBlocksize() {
		return blocklengthSpinner.getSelection();
	}

	public boolean getReadInDirection() {
		return directionChooserIn.getInput().getContent();
	}

	public boolean getCrop() {
		return parttextCheck.getSelection();
	}

	public int getCropsize() {
		return parttextCount.getSelection();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getManualInputState() {
		return manualInputState;
	}

	public void setManualInputState(int manualInputState) {
		this.manualInputState = manualInputState;
	}

	public void setReadInDirection(boolean dir) {
		if (directionChooserIn != null) {
			directionChooserIn.setDirection(dir);
		} else {
			this.iniDirection = dir;
		}
	}

	/**
	 * @param initFirstTabText
	 *            the init_firstTabText to set
	 */
	public void setAnalysisText(String text) {
		if ((analysisText != null) && (!analysisText.isDisposed())) {
			analysisText.setText(text);
			previousText = text;
			preview();
		} else {
			this.init_text = text;
		}
	}

	/**
	 * @param initBlocklength
	 *            the init_blocklength to set
	 */
	public void setBlocklength(int length) {
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
	public void setCroptext(boolean initCroptext) {
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
	public void setCroplength(int initCroplength) {
		if ((parttextCount != null) && (!parttextCount.isDisposed())) {
			parttextCount.setSelection(initCroplength);
			parttextCountWidgetSelected(null);
		} else {
			this.init_croplength = initCroplength;
		}
	}

	private void parttextCountWidgetSelected(SelectionEvent evt) {
		preview();
	}

	private void parttextCheckWidgetSelected(SelectionEvent evt) {
		parttextCount.setEnabled(parttextCheck.getSelection());
		if (blocklengthSpinner.getSelection() > 0
				&& parttextCheck.getSelection())
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

}
