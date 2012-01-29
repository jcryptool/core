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
package org.jcryptool.analysis.transpositionanalysis.ui;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
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
import org.jcryptool.analysis.transpositionanalysis.TranspositionAnalysisPlugin;
import org.jcryptool.analysis.transpositionanalysis.ui.TextInputWithSourceDisplayer.Style;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizard;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage.PageConfiguration;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.inputs.TextInputWithSource;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithm;
import org.jcryptool.core.operations.algorithm.ShadowAlgorithmAction;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionTable;
import org.jcryptool.crypto.classic.transposition.ui.TranspositionKeyInputWizard;
import org.jcryptool.crypto.classic.transposition.ui.TranspositionKeyInputWizardPage;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class TranspAnalysisUI extends org.eclipse.swt.widgets.Composite implements Observer {

	private TranspositionTableComposite transpTable;

	private TextInputWithSource text = null;
	private Composite composite7;
	private Label labelReadDir;
	private Composite compReadDir;
	private Link linkExport;
	private Composite composite8;
	private Button btnDecipher;
	private Label textpreviewDescription;
	private Group previewGroup;
	private Text previewText;
	private Composite compResults;
	private Composite compTable;
	private Label labelKeypreview;
	private Composite composite4;
	private Label label2;
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
	private TransformData textTransformData;
	private Composite compLoadTextBtn;
	private Label lblLoadA;
	private Button btnOpenTextWizard;
	private Composite compInstructions;
	private Label lblTryTo;
	private Label lblTryToRecover;
	private Label lblHoweverTheColumn;
	private Composite composite_2;
	private Label lblColumnCount;
	private Spinner spinner;
	private Composite compTextSource;
	private Label label;
	private Label lblYouHaveSelected;
	private TextInputWithSourceDisplayer sourceDisplayer;

	private Link linkChooseText;
	private Link linkSetKey;
	private Label label_1;

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(TextInputWithSource text) {
		setText(text, true);
	}

	private void setText(TextInputWithSource text, boolean refresh) {
		this.text = text;
		if (refresh) transpTable.setText(calcText());
	}

	/**
	 * @param crop
	 *            the crop to set
	 */
	public void setCrop(boolean crop) {
		setCrop(crop, true);
	}

	public void setCrop(boolean crop, boolean refresh) {
		this.crop = crop;
		if (refresh) transpTable.setText(calcText(), blocklength, !crop, croplength);
	}

	/**
	 * @param croplength
	 *            the croplength to set
	 */
	public void setCroplength(int croplength) {
		setCroplength(croplength, true);
	}

	public void setCroplength(int croplength, boolean refresh) {
		this.croplength = croplength;
		if (refresh) transpTable.setText(calcText(), blocklength, !crop, croplength);
	}

	/**
	 * @param blocklength
	 *            the blocklength to set
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

	private void setReadInMode(boolean readInDirection, boolean refresh) {
		this.readInMode = readInDirection;
		if (refresh) {
			transpTable.setReadInOrder(readInMode);
		}
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */

	public TranspAnalysisUI(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.numColumns = 2;
			this.setLayout(thisLayout);
			{
				compLoadTextBtn = new Composite(this, SWT.NONE);
				compLoadTextBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
				GridLayout compLoadTextBtnLayout = new GridLayout(2, false);
				compLoadTextBtnLayout.marginHeight = 3;
				compLoadTextBtn.setLayout(compLoadTextBtnLayout);
				{
					lblLoadA = new Label(compLoadTextBtn, SWT.NONE);
					lblLoadA.setBounds(0, 0, 55, 15);
					lblLoadA.setText("1)"); //$NON-NLS-1$
				}
				{
					btnOpenTextWizard = new Button(compLoadTextBtn, SWT.NONE);
					btnOpenTextWizard.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							mainButton();
						}
					});
					btnOpenTextWizard.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					btnOpenTextWizard.setText(Messages.TranspAnalysisUI_btnOpenTextWizard_text);
				}
			}
			{
				compTextSource = new Composite(this, SWT.NONE);
				compTextSource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
				GridLayout compTextSourceLayout = new GridLayout(3, false);
				compTextSourceLayout.verticalSpacing = 2;
				compTextSourceLayout.marginHeight = 3;
				compTextSource.setLayout(compTextSourceLayout);
				{
					label = new Label(compTextSource, SWT.NONE);
					label.setText("1)"); //$NON-NLS-1$
				}
				{
					lblYouHaveSelected = new Label(compTextSource, SWT.NONE);
					lblYouHaveSelected.setText(Messages.TranspAnalysisUI_lblYouHaveSelected_text);
				}
				{
					sourceDisplayer = new TextInputWithSourceDisplayer(compTextSource, this, new TextInputWithSource("abcdef"), new Style(true)); //$hide$ //$NON-NLS-1$
					GridData sourceDisplayerLData = new GridData(SWT.FILL, SWT.CENTER, false, false);
					sourceDisplayerLData.horizontalIndent = 3;
					sourceDisplayer.setLayoutData(sourceDisplayerLData); //$hide$
				}
				{
					new Label(compTextSource, SWT.NONE);
				}
				{
					linkChooseText = new Link(compTextSource, SWT.NONE);
					linkChooseText.setText(Messages.TranspAnalysisUI_lblChooseAnotherText);
					GridData linkChooseTextLData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
					linkChooseText.setLayoutData(linkChooseTextLData);
					linkChooseTextLData.horizontalSpan = 2;
					linkChooseText.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							mainButton();
						}
					});
				}
			}
			{
				compInstructions = new Composite(this, SWT.NONE);
				compInstructions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
				GridLayout compInstructionsLayout = new GridLayout(2, false);
				compInstructionsLayout.marginHeight = 3;
				compInstructions.setLayout(compInstructionsLayout);
				{
					lblTryTo = new Label(compInstructions, SWT.NONE);
					lblTryTo.setBounds(0, 0, 55, 15);
					lblTryTo.setText("2)"); //$NON-NLS-1$
				}
				{
					lblTryToRecover = new Label(compInstructions, SWT.NONE);
					lblTryToRecover.setText(Messages.TranspAnalysisUI_lblTryToRecover_text);
				}
				new Label(compInstructions, SWT.NONE);
				{
					lblHoweverTheColumn = new Label(compInstructions, SWT.WRAP);
					{
						GridData gd_lblHoweverTheColumn = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
						gd_lblHoweverTheColumn.widthHint = 200;
						lblHoweverTheColumn.setLayoutData(gd_lblHoweverTheColumn);
					}
					lblHoweverTheColumn.setText(Messages.TranspAnalysisUI_lblHoweverTheColumn_text);
				}
			}
			{
				compTable = new Composite(this, SWT.NONE);
				GridLayout gl_compTable = new GridLayout();
				gl_compTable.makeColumnsEqualWidth = true;
				GridData gd_compTable = new GridData();
				gd_compTable.grabExcessHorizontalSpace = true;
				gd_compTable.horizontalAlignment = GridData.FILL;
				gd_compTable.verticalAlignment = GridData.FILL;
				gd_compTable.grabExcessVerticalSpace = true;
				compTable.setLayoutData(gd_compTable);
				compTable.setLayout(gl_compTable);
				{
					composite_2 = new Composite(compTable, SWT.NONE);
					composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					GridLayout gl_composite_2 = new GridLayout(2, false);
					gl_composite_2.marginHeight = 0;
					gl_composite_2.marginWidth = 0;
					composite_2.setLayout(gl_composite_2);
					{
						lblColumnCount = new Label(composite_2, SWT.NONE);
						lblColumnCount.setBounds(0, 0, 55, 15);
						lblColumnCount.setText(Messages.TranspAnalysisUI_lblColumnCount_text_1);
					}
					{
						spinner = new Spinner(composite_2, SWT.BORDER);
						spinner.setMaximum(1000);
						spinner.setMinimum(1);
						spinner.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
						spinner.setEnabled(false);
						spinner.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								spinnerSelected();
							}
						});
					}
				}
				{
					GridData transpTableLData = new GridData();
					transpTableLData.grabExcessHorizontalSpace = true;
					transpTableLData.horizontalAlignment = GridData.FILL;
					transpTableLData.verticalAlignment = GridData.FILL;
					transpTableLData.grabExcessVerticalSpace = true;
					// transpTableLData.minimumWidth = 200;
					// transpTableLData.widthHint = 370;
					transpTable = new TranspositionTableComposite(compTable, SWT.NONE);
					transpTable.setLayoutData(transpTableLData);

					transpTable.setColReorderObserver(this);
				}
			}
			{
				compResults = new Composite(this, SWT.NONE);
				GridLayout gl_compResults = new GridLayout();
				gl_compResults.makeColumnsEqualWidth = true;
				GridData gd_compResults = new GridData();
				gd_compResults.grabExcessHorizontalSpace = true;
				gd_compResults.horizontalAlignment = GridData.FILL;
				gd_compResults.verticalAlignment = GridData.FILL;
				gd_compResults.grabExcessVerticalSpace = true;
				compResults.setLayoutData(gd_compResults);
				compResults.setLayout(gl_compResults);
				{
					previewGroup = new Group(compResults, SWT.NONE);
					GridLayout previewGroupLayout = new GridLayout();
					previewGroupLayout.makeColumnsEqualWidth = true;
					previewGroup.setLayout(previewGroupLayout);
					GridData previewGroupLData = new GridData();
					previewGroupLData.grabExcessHorizontalSpace = true;
					previewGroupLData.horizontalAlignment = GridData.FILL;
					previewGroupLData.grabExcessVerticalSpace = true;
					previewGroupLData.verticalAlignment = GridData.FILL;
					previewGroup.setLayoutData(previewGroupLData);
					previewGroup.setText(Messages.TranspAnalysisUI_Results);
					{
						composite4 = new Composite(previewGroup, SWT.NONE);
						GridLayout composite4Layout = new GridLayout();
						composite4Layout.makeColumnsEqualWidth = true;
						composite4Layout.marginWidth = 0;
						composite4Layout.marginHeight = 0;
						GridData composite4LData = new GridData();
						composite4LData.grabExcessHorizontalSpace = true;
						composite4LData.horizontalAlignment = GridData.FILL;
						composite4.setLayoutData(composite4LData);
						composite4.setLayout(composite4Layout);
						{
							label2 = new Label(composite4, SWT.NONE);
							GridData label2LData = new GridData();
							label2LData.grabExcessHorizontalSpace = true;
							label2.setLayoutData(label2LData);
							label2.setText(Messages.TranspAnalysisUI_keyfound);
						}
						{
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
							{
								labelKeypreview = new Label(composite8, SWT.BORDER);
								GridData label3LData = new GridData();
								label3LData.grabExcessHorizontalSpace = true;
								label3LData.horizontalAlignment = GridData.FILL;
								labelKeypreview.setLayoutData(label3LData);
								labelKeypreview.setText(""); //$NON-NLS-1$
							}
							{
								linkSetKey = new Link(composite8, SWT.NONE);
								linkSetKey.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										TranspositionKeyInputWizard wiz = new TranspositionKeyInputWizard();
										dialog = new WizardDialog(TranspAnalysisUI.this.getShell(), wiz);

										TranspositionKey oldKey = getKeyUsedToEncrypt();
										wiz.setPageConfig(new TranspositionKeyInputWizardPage.PageConfiguration(oldKey));
										
										int dialogResult = dialog.open();
										
										if(dialogResult == Window.OK) {
											TranspositionKey key = wiz.getPageConfig().getKey();
											spinner.setSelection(key.getLength());
											spinnerSelected();
											transpTable.setColumnOrder(key.toArray());
											columnsReordered(transpTable.getColumnOrder());
										}
									}
								});
								linkSetKey.setText(Messages.TranspAnalysisUI_link_text);
								linkSetKey.setEnabled(false);
							}
							{
								label_1 = new Label(composite8, SWT.NONE);
								label_1.setText("|"); //$NON-NLS-1$
							}
							{
								linkExport = new Link(composite8, SWT.NONE);
								linkExport.setText("<a>" + Messages.TranspAnalysisUI_Export + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ 
								linkExport.setEnabled(false);
								linkExport.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent evt) {
										if (keyUsedToEncrypt != null) {
											KeyViewer myKeyViewer = new KeyViewer(getShell(),
												Messages.TranspAnalysisUI_keyviewer, keyUsedToEncrypt);
											myKeyViewer.open();
										}
									}
								});
							}
						}
						{
							composite7 = new Composite(composite4, SWT.NONE);
							GridLayout composite7Layout = new GridLayout();
							composite7Layout.marginHeight = 0;
							composite7Layout.marginWidth = 0;
							GridData composite7LData = new GridData();
							composite7LData.grabExcessHorizontalSpace = true;
							composite7LData.horizontalAlignment = GridData.FILL;
							composite7LData.verticalIndent = 12;
							composite7.setLayoutData(composite7LData);
							composite7.setLayout(composite7Layout);
							{
								textpreviewDescription = new Label(composite7, SWT.NONE);
								textpreviewDescription.setText(Messages.TranspAnalysisUI_preview);
							}
							{
								compReadDir = new Composite(composite7, SWT.NONE);
								GridLayout compReadDirLayout = new GridLayout();
								compReadDirLayout.numColumns = 2;
								compReadDirLayout.marginWidth = 0;
								compReadDirLayout.marginHeight = 0;
								GridData compReadDirLData = new GridData();
								compReadDirLData.grabExcessHorizontalSpace = true;
								compReadDir.setLayoutData(compReadDirLData);
								compReadDir.setLayout(compReadDirLayout);
								{
									labelReadDir = new Label(compReadDir, SWT.NONE);
									labelReadDir.setText(Messages.TranspAnalysisUI_read_out_mode_label);
								}
								{
									readoutDirChooser = new ReadDirectionChooser(compReadDir, true);
									readoutDirChooser.setDirection(false);

									readoutDirChooser.getInput().addObserver(new Observer() {
										@Override
										public void update(Observable o, Object arg) {
											if (arg == null) previewPlaintext();
										}
									});
								}
							}
						}
					}
					{
						previewText = new Text(previewGroup, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
						GridData previewTextLData = new GridData();
						previewTextLData.grabExcessHorizontalSpace = true;
						previewTextLData.horizontalAlignment = GridData.FILL;
						previewTextLData.grabExcessVerticalSpace = true;
						previewTextLData.verticalAlignment = GridData.FILL;
						previewTextLData.minimumHeight = 20;
						previewTextLData.widthHint = 150;
						previewText.setLayoutData(previewTextLData);
						previewText.setEditable(false);
					}
					{
						btnDecipher = new Button(previewGroup, SWT.PUSH | SWT.CENTER);
						GridData btnDecipherLData = new GridData();
						btnDecipherLData.grabExcessHorizontalSpace = true;
						btnDecipherLData.horizontalAlignment = GridData.END;
						btnDecipher.setLayoutData(btnDecipherLData);
						btnDecipher.setText(Messages.TranspAnalysisUI_decipher);
						btnDecipher.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent evt) {
								decipherIntoEditor();
							}
						});
						btnDecipher.setEnabled(false);
					}
				}
			}
			
			//hide source display
			displayTextSource(null, false, false);
			this.layout();
			pack();
		} catch (Exception e) {
			LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e);
		}
	}
	
	
	protected TranspositionKey getKeyUsedToEncrypt() {
		return keyUsedToEncrypt;
	}

	private void spinnerSelected() {
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
		if (transpTable != null && !transpTable.isDisposed()) columnsReordered(transpTable.getColumnOrder());

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
		if (textTransformData != null) { // same as above.. just separated to
											// make sure no mistake happens
			textWizard.setTransformData(textTransformData);
		}
		int result = dialog.open();

		if (result == Window.OK) {
			textPageConfiguration = textWizard.getTextPageConfig();
			textTransformData = textWizard.getTransformData();
			setText(textPageConfiguration.getText(), false);
			setCrop(textPageConfiguration.isCrop(), false);
			setCroplength(textPageConfiguration.getCropLength(), false);
			setBlocklength(textPageConfiguration.getColumnCount(), false);
			setReadInMode(textPageConfiguration.getReadInDirection(), false);

			doAutoCrop = (blocklength > 0 && !crop || blocklength == 0 && crop);

			transpTable.setReadInOrder(readInMode, false);
			transpTable.setText(calcText(), blocklength, !crop, croplength);

			columnsReordered(transpTable.getColumnOrder());

			displayTextSource(textPageConfiguration.getText(), true, true);
			
			spinner.setEnabled(true);
			linkSetKey.setEnabled(true);
			linkExport.setEnabled(true);
		}
	}

	/**
	 * switch load text / show text source composites.
	 * 
	 * @param text2 if the source composite is displayed: what should be displayed there. (else this parameter is mute)
	 * @param show whether to show the source composite (true) or the load Text button (false)
	 * @param layout whether to layout after this operation (recommended if another layout of master composite does not follow directly anyway)
	 */
	private void displayTextSource(TextInputWithSource text, boolean show, boolean layout) {
		
		((GridData) compTextSource.getLayoutData()).exclude = !show;
		((GridData) compLoadTextBtn.getLayoutData()).exclude = show;
		
		compTextSource.setVisible(show);
		compLoadTextBtn.setVisible(!show);
		
		if(show) {
			sourceDisplayer.setText(text);
		}
		
		if(layout) {
			this.layout(new Control[]{compTextSource, compLoadTextBtn});
		}
	}

	private String calcText() {
		return Transform.transformText(text.getText(), textTransformData);
	}

	private void refreshTable() {
		transpTable.setReadInOrder(readInMode, false);
		transpTable.setText(calcText(), blocklength, !crop, croplength);
	}

	private void columnsReordered(int[] cols) {
		int[] order;
		if (cols.length != this.croplength) order = cols.clone();
		else order = null;
		setKeyUsedToEncrypt(order);

		if (transpTable != null) {
			previewPlaintext();
		}
	}

	private void previewPlaintext() {
		if (transpTable != null) {
			TranspositionTable table = new TranspositionTable(transpTable.getColumnCountDisplayed());
			table.fillCharsIntoTable(transpTable.getText().toCharArray(), false);
			previewText.setText(String.valueOf(table.readOutContent(readoutDirChooser.getInput().getContent())));
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof int[]) {
			columnsReordered((int[]) arg1);
		}
	}

	/**
	 * @param actualKey
	 *            the actualKey to set
	 */
	public void setKeyUsedToEncrypt(int[] key) {
		int actualKeyLength;
		if (key != null) {
			this.keyUsedToEncrypt = new TranspositionKey(key);
			labelKeypreview.setText(" " + keyUsedToEncrypt.toStringOneRelative() + "|"); //$NON-NLS-1$ //$NON-NLS-2$
			btnDecipher.setEnabled(true);
			actualKeyLength = keyUsedToEncrypt.getLength();
			labelKeypreview.setEnabled(true);
		} else {
			this.keyUsedToEncrypt = null;
			btnDecipher.setEnabled(false);
			labelKeypreview.setText(Messages.TranspAnalysisUI_keypreview_zerocolumns);
			actualKeyLength = 0;
			labelKeypreview.setEnabled(false);
		}

		boolean changeLength = (actualKeyLength != this.blocklength);
		if (changeLength) setBlocklength(actualKeyLength);
	}

	private void decipherIntoEditor() {
		final String TRANSPOSITION_ALGORITHM = "org.jcryptool.crypto.classic.transposition.algorithm"; //$NON-NLS-1$

		OperationsPlugin op = OperationsPlugin.getDefault();
		IAction[] actions = op.getAlgorithmsManager().getShadowAlgorithmActions();
		for (final IAction action : actions) {
			if (TRANSPOSITION_ALGORITHM.equals(action.getId())) {
				ClassicDataObject myDO = new ClassicDataObject();
				AbstractAlphabet ascii = AlphabetsManager.getInstance().getAlphabetByName("Printable ASCII"); //$NON-NLS-1$
				myDO.setAlphabet(ascii);
				char[] key = new char[keyUsedToEncrypt.getLength() + 2];
				key[0] = getInputMethodDecryption() ? ascii.getCharacterSet()[0] : ascii.getCharacterSet()[1];
				key[1] = getOutputMethodDecryption() ? ascii.getCharacterSet()[0] : ascii.getCharacterSet()[1];
				System.arraycopy(keyUsedToEncrypt.toUnformattedChars(ascii).toCharArray(), 0, key, 2,
					keyUsedToEncrypt.getLength());
				myDO.setKey(key);
				myDO.setKey2("".toCharArray()); //$NON-NLS-1$
				try {
					myDO.setInputStream(new BufferedInputStream(new ByteArrayInputStream(calcText().getBytes(
						IConstants.UTF8_ENCODING))));
				} catch (UnsupportedEncodingException e) {
					LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e);
				}
				myDO.setOpmode(AbstractAlgorithm.DECRYPT_MODE);
				myDO.setFilterNonAlphaChars(true);
				myDO.setTransformData(new TransformData());

				((ShadowAlgorithmAction) action).run(myDO);
				break;
			}
		}
	}

	private boolean getOutputMethodDecryption() {
		return readInMode;
	}

	private boolean getInputMethodDecryption() {
		// its vice-versa, because the entered mode was the read-OUT of the
		// DECRYPTION
		return readoutDirChooser.getInput().getContent();
	}
}
