//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.huffmanCoding.views;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.visual.huffmanCoding.HuffmanCodingPlugin;
import org.jcryptool.visual.huffmanCoding.algorithm.BitString;
import org.jcryptool.visual.huffmanCoding.algorithm.Huffman;
import org.jcryptool.visual.huffmanCoding.algorithm.InvalidCharacterException;
import org.jcryptool.visual.huffmanCoding.algorithm.Node;

/**
 * @author Miray Inel
 * @author <i>revised by</i>
 * @author Michael Altenhuber
 * 
 */
public class HuffmanCodingView extends ViewPart {

	// Constants
	public static final String ID = "org.jcryptool.visual.huffmanCoding.views.HuffmanCodingView"; //$NON-NLS-1$
	public final static int COMPRESS = 1;
	public final static int UNCOMPRESS = 2;
	public final static int VIEWMAIN = 0;
	public final static int VIEWTREE = 1;
	public final static int VIEWTABLE = 2;
	private int mode = HuffmanCodingView.COMPRESS;

	// Algorithm attributes
	private Huffman huffmanCode;
	private int[] huffmanCodeBinary;
	private boolean isCompressed = false;
	private boolean isUncompressed = false;

	private File fileUncomp;
	private File fileComp;
	private String filePath;

	// Main UI components
	private Composite parent;
	private Composite mainViewComposite;
	private TabFolder tabFolder;
	private TabItem tbtmParameter;
	private TabItem tbtmHuffmanTree;
	private TabItem tbtmCodeTable;
	private HuffmanCodingView mainView;
	private HuffmanCodingViewTree treeView;
	private HuffmanCodingViewTable tableView;

	
	private TitleAndDescriptionComposite titleAndDescription;
	private Group grpSzenario;
	private Group grpCompress;
	private Group grpNextSteps;
	private Button btnRadioCompress;
	private Button btnRadioUncompress;

	// Shared variables (by Compress/Decompress scenario) in the main view
	private Button btnRadioExampleText;
	private Button btnRadioContentFromFile;
	private Button btnOpenUncompFile;
	private Text textFileUncompName;
	private Label horizontalSeparator;
	private Composite compInputOutput;
	private Composite compInputText;
	private Composite compHexTable;
	private Label inputHeader;
	private StyledText textInput;
	private Label hexTableHeader;
	private Table hexTable;

	// Buttons
	private Button btnCompress;
	private Button btnUncompress;

	// Center information group
	private Group grpCenterData;
	private Label inputSizeDescription;
	private Label outputSizeDescription;
	private Label compressionRateDescription;
	private Text inputSize;
	private Text outputSize;
	private Text compressionRate;
	private Button btnSaveResult;

	private Color labelEnabledColor;
	private Color labelDisabledColor;

	public HuffmanCodingView() {
		new ArrayList<TableEditor>();
		new Hashtable<Integer, Button>();
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		this.mainView = this;

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		tabFolder = new TabFolder(scrolledComposite, SWT.NONE);

		// Tabs
		tbtmParameter = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter.setText(Messages.HuffmanCodingView_0);

		tbtmHuffmanTree = new TabItem(tabFolder, SWT.NONE);
		tbtmHuffmanTree.setText(Messages.HuffmanCodingView_13);

		tbtmCodeTable = new TabItem(tabFolder, SWT.NONE);
		tbtmCodeTable.setText(Messages.HuffmanCodingView_14);

		// Tab selection listener
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selection = tabFolder.getSelectionIndex();

				if (selection == VIEWTREE && (isCompressed || isUncompressed)) {
					if (treeView == null || treeView.isDisposed())
						treeView = new HuffmanCodingViewTree(tabFolder, SWT.NONE, huffmanCode.getResultNodeList(),
								getViewSite().getActionBars());
					tbtmHuffmanTree.setControl(treeView);
					visibleGraphMenu(true);
					tabFolder.setSelection(selection);
				}
				if (selection == VIEWTABLE && (isCompressed || isUncompressed)) {
					if (tableView == null || treeView.isDisposed())
						tableView = new HuffmanCodingViewTable(tabFolder, SWT.NONE, mainView);

					visibleGraphMenu(false);
					tbtmCodeTable.setControl(tableView);
					tabFolder.setSelection(selection);
				}

				if (selection != VIEWMAIN && (!isCompressed && !isUncompressed)) {
					tabFolder.setSelection(0);
					MessageBox notReady = new MessageBox(tabFolder.getShell(), SWT.OK);
					notReady.setText(Messages.HuffmanCodingView_28);
					notReady.setMessage(Messages.HuffmanCodingView_29);
					notReady.open();

				}
				if (selection == VIEWMAIN)
					visibleGraphMenu(false);

			}
		});

		// Init first tab
		mainViewComposite = new Composite(tabFolder, SWT.NONE);
		tbtmParameter.setControl(mainViewComposite);
		GridLayout gl_mainViewComposite = new GridLayout(2, false);
		gl_mainViewComposite.marginHeight = 0;
		gl_mainViewComposite.marginWidth = 0;
		mainViewComposite.setLayout(gl_mainViewComposite);

		// Content of first tab
		titleAndDescription = new TitleAndDescriptionComposite(mainViewComposite);
		titleAndDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		titleAndDescription.setTitle(Messages.HuffmanCodingView_16);
		titleAndDescription.setDescription(Messages.HuffmanCodingView_1);

		createSzenarioGroup(mainViewComposite);

		grpNextSteps = createCompressButtonGroup();
		
		/*
		 * create the initial compress group
		 */
		grpCompress = createCompressGroup();

		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HuffmanCodingPlugin.PLUGIN_ID + ".view");
	}
	
	/**
	 * This creates the group where the user can select the scenario, so whether he
	 * wants to compress or decompress.
	 * @param parent
	 */
	private void createSzenarioGroup(Composite parent) {
		grpSzenario = new Group(parent, SWT.NONE);
		grpSzenario.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		grpSzenario.setText(Messages.HuffmanCodingView_grpSzenario_text);
		grpSzenario.setLayout(new GridLayout(1, false));

		btnRadioCompress = new Button(grpSzenario, SWT.RADIO);
		btnRadioCompress.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		btnRadioCompress.setSelection(true);
		btnRadioCompress.setText(Messages.HuffmanCodingView_6);
		btnRadioCompress.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button tmp = null;

				if (e.getSource() instanceof Button) {
					tmp = (Button) e.getSource();
				}

				if (tmp.getSelection()) {
					grpCompress.dispose();
					grpNextSteps.dispose();

					grpNextSteps = createCompressButtonGroup();
					grpCompress = createCompressGroup();
					mainViewComposite.layout();
					setFocus();

					/*
					 * reset tabs
					 */
					isCompressed = false;
					loadExampleText();

					mode = HuffmanCodingView.COMPRESS;
				}

			}
		});


		btnRadioUncompress = new Button(grpSzenario, SWT.RADIO);
		btnRadioUncompress.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		btnRadioUncompress.setText(Messages.HuffmanCodingView_7);
		btnRadioUncompress.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button tmp = null;

				if (e.getSource() instanceof Button) {
					tmp = (Button) e.getSource();
				}

				if (tmp.getSelection()) {
					grpCompress.dispose();
					grpNextSteps.dispose();

					grpNextSteps = createUncompressButtonGroup();
					grpCompress = createUncompressGroup();
					mainViewComposite.layout();

					/*
					 * reset tabs
					 */
					mode = HuffmanCodingView.UNCOMPRESS;
				}
			}
		});
	}

	/**
	 * Loads the default JCrypTool sample text and puts it into the inputText field
	 */
	private void loadExampleText() {
		try {
			URL url = HuffmanCodingPlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
			File template = new File(FileLocator.toFileURL(url).getFile() + "templates" + File.separatorChar //$NON-NLS-1$
					+ Messages.HuffmanCodingView_inputText);

			Scanner scanner = new Scanner(template, "UTF-8"); //$NON-NLS-1$
			String fileString = scanner.useDelimiter("\\Z").next(); //$NON-NLS-1$
			scanner.close();

			textInput.setText(fileString);

		} catch (FileNotFoundException e1) {
			LogUtil.logError(HuffmanCodingPlugin.PLUGIN_ID, e1);
		} catch (IOException e1) {
			LogUtil.logError(HuffmanCodingPlugin.PLUGIN_ID, e1);
		}
	}

	/**
	 * Creates the group with the compress button in it
	 * 
	 * @return the group
	 */
	private Group createCompressButtonGroup() {
		Group grpNextSteps = new Group(mainViewComposite, SWT.NONE);
		GridLayout gl_grpNextSteps = new GridLayout(1, false);
		grpNextSteps.setLayout(gl_grpNextSteps);
		grpNextSteps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpNextSteps.setText(Messages.HuffmanCodingView_grpNextSteps_text);

		btnCompress = new Button(grpNextSteps, SWT.NONE);
		GridData gd_btnCompress = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_btnCompress.widthHint = 180;
		btnCompress.setLayoutData(gd_btnCompress);
		// btnCompress.setEnabled(false);
		btnCompress.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!textInput.getText().isEmpty()) {

					String inputString = textInput.getText();

					huffmanCode = new Huffman();

					try {
						huffmanCode.compress(inputString);
					} catch (IOException e1) {
						LogUtil.logError(e1);
					} catch (InvalidCharacterException e2) {
						LogUtil.logError(e2);
					}
					huffmanCodeBinary = huffmanCode.getHuffmanBinary();
					parseBinaryHuffman(huffmanCodeBinary);
					outputSize
							.setText(Integer.toString(huffmanCodeBinary.length) + " " + Messages.HuffmanCodingView_24);

					float compressedSize = huffmanCodeBinary.length;
					float rawSize = inputString.length();
					float compRate;

					compRate = (rawSize - compressedSize) / rawSize * 100;
					compressionRate.setText(String.format("%.2f%%", compRate));

					btnCompress.setEnabled(false);
					btnOpenUncompFile.setEnabled(false);
					textInput.setEditable(false);
					hexTableHeader.setForeground(labelEnabledColor);

					setCenterEnabled(true);

					isCompressed = true;

					String message = Messages.HuffmanCodingView_20;
					MessageDialog.openInformation(HuffmanCodingView.this.parent.getShell(),
							Messages.HuffmanCodingView_19, message);
				}
			}
		});
		btnCompress.setText(Messages.HuffmanCodingView_6);
		return grpNextSteps;
	}

	/**
	 * Creates the group with the decompress button in it
	 * 
	 * @return the group
	 */
	private Group createUncompressButtonGroup() {
		Group grpNextSteps = new Group(mainViewComposite, SWT.NONE);
		grpNextSteps.setLayout(new GridLayout(1, false));
		grpNextSteps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpNextSteps.setText(Messages.HuffmanCodingView_grpNextSteps_text);

		btnUncompress = new Button(grpNextSteps, SWT.NONE);
		GridData gd_btnUncompress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnUncompress.widthHint = 180;
		btnUncompress.setLayoutData(gd_btnUncompress);
		btnUncompress.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String message;
				huffmanCode = new Huffman();
				if (huffmanCodeBinary != null)
					huffmanCode.uncompress(huffmanCodeBinary);

				message = huffmanCode.getMessage();
				textInput.setText(message);
				btnSaveResult.setEnabled(true);
				outputSize.setText(Integer.toString(message.length()) + " " + Messages.HuffmanCodingView_24);
				inputHeader.setForeground(labelEnabledColor);

				float compressedSize = huffmanCodeBinary.length;
				float rawSize = message.length();
				float compRate;

				compRate = (rawSize - compressedSize) / rawSize * 100;
				compressionRate.setText(String.format("%.2f%%", compRate));
				isUncompressed = true;

				treeView = null;
				tableView = null;
			}
		});
		btnUncompress.setEnabled(false);
		btnUncompress.setText(Messages.HuffmanCodingView_7);

		return grpNextSteps;
	}

	/**
	 * Creates the decompress main view containing radio buttons to select huffman
	 * binary and the input/output views, as well as statistic views.
	 * 
	 * @return the Group with all the elements for the decompress main view
	 */
	private Group createUncompressGroup() {
		fileComp = null;
		fileUncomp = null;

		Group grpCompress = new Group(mainViewComposite, SWT.NONE);
		grpCompress.setText(Messages.HuffmanCodingView_6);
		grpCompress.setLayout(new GridLayout(3, false));
		grpCompress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		btnRadioExampleText = new Button(grpCompress, SWT.RADIO);
		btnRadioExampleText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		btnRadioExampleText.setSelection(true);
		btnRadioExampleText.setText(Messages.HuffmanCodingView_radioUncompFromMem);
		btnRadioExampleText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button tmp = null;

				if (e.getSource() instanceof Button) {
					tmp = (Button) e.getSource();

					if (tmp.getSelection()) {

						if (isCompressed) {
							parseBinaryHuffman(huffmanCodeBinary);
							btnUncompress.setEnabled(true);
							inputSize.setText(
									Integer.toString(huffmanCodeBinary.length) + " " + Messages.HuffmanCodingView_24);
						} else {
							hexTable.removeAll();
							hexTable.clearAll();
							resizeHexTable();
							inputSize.setText("");
							textFileUncompName.setText("");
							btnUncompress.setEnabled(false);
						}
						textInput.setText("");
						inputHeader.setForeground(labelDisabledColor);
						btnSaveResult.setEnabled(false);
						outputSize.setText("");
						compressionRate.setText("");
						btnOpenUncompFile.setEnabled(false);
					}
				}
			}
		});


		btnRadioContentFromFile = new Button(grpCompress, SWT.RADIO);
		btnRadioContentFromFile.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		btnRadioContentFromFile.setText(Messages.HuffmanCodingView_radioUncompFromFile);
		btnRadioContentFromFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button tmp = null;

				if (e.getSource() instanceof Button) {
					tmp = (Button) e.getSource();

					if (tmp.getSelection()) {

						hexTable.removeAll();
						hexTable.clearAll();
						resizeHexTable();
						inputSize.setText("");
						outputSize.setText("");
						textInput.setText("");
						inputHeader.setForeground(labelDisabledColor);

						btnOpenUncompFile.setEnabled(true);
						btnUncompress.setEnabled(false);

						btnSaveResult.setEnabled(false);
						compressionRate.setText("");

					}
				}
			}
		});


		btnOpenUncompFile = new Button(grpCompress, SWT.NONE);
		btnOpenUncompFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnOpenUncompFile.setText(Messages.HuffmanCodingView_3);
		btnOpenUncompFile.setEnabled(false);
		btnOpenUncompFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				fd.setFilterExtensions(new String[] { "*.huffman" }); //$NON-NLS-1$
				fd.setFilterNames(new String[] { Messages.HuffmanCodingView_8 });

				filePath = fd.open();

				if (filePath != null) {
					textFileUncompName.setText(filePath);
					textFileUncompName.setSelection(filePath.length());
					parseBinaryHuffman(filePath);
					inputSize.setText(Integer.toString(huffmanCodeBinary.length) + " " + Messages.HuffmanCodingView_24);

					btnUncompress.setEnabled(true);
					btnUncompress.setFocus();
					isCompressed = false;
				}
			}
		});


		textFileUncompName = new Text(grpCompress, SWT.BORDER | SWT.READ_ONLY);
		textFileUncompName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		horizontalSeparator = new Label(grpCompress, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gdHorizontalSeparator = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1);
		gdHorizontalSeparator.heightHint = 35;
		horizontalSeparator.setLayoutData(gdHorizontalSeparator);

		compInputOutput = new Composite(grpCompress, SWT.NONE);
		compInputOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		GridLayout gl_compInputOutput = new GridLayout(3, false);
		gl_compInputOutput.marginHeight = 0;
		gl_compInputOutput.marginWidth = 0;
		compInputOutput.setLayout(gl_compInputOutput);
		

		compHexTable = new Composite(compInputOutput, SWT.NONE);
		compHexTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compHexTable = new GridLayout();
		gl_compHexTable.marginHeight = 0;
		gl_compHexTable.marginWidth = 0;
		compHexTable.setLayout(gl_compHexTable);

		hexTableHeader = new Label(compHexTable, SWT.NONE);
		hexTableHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		hexTableHeader.setText(Messages.HuffmanCodingView_headerIn);

		hexTable = new Table(compHexTable, SWT.V_SCROLL | SWT.BORDER);
		GridData gdHexTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdHexTable.widthHint = hexTable.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		gdHexTable.heightHint = hexTable.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		hexTable.setLayoutData(gdHexTable);
		hexTable.setHeaderVisible(true);
		hexTable.setFont(FontService.getNormalMonospacedFont());

		TableColumn offset = new TableColumn(hexTable, SWT.NONE);
		offset.setText(Messages.HuffmanCodingView_offset);


		TableColumn[] hexDigits = new TableColumn[16];
		for (int i = 0; i < hexDigits.length; ++i) {
			hexDigits[i] = new TableColumn(hexTable, SWT.NONE);
			hexDigits[i].setText("0" + Integer.toHexString(i).toUpperCase());
		}
		
		resizeHexTable();

		if (huffmanCodeBinary != null)
			parseBinaryHuffman(huffmanCodeBinary);

		grpCenterData = new Group(compInputOutput, SWT.NONE);
		grpCenterData.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		grpCenterData.setLayout(new GridLayout(2, true));

		inputSizeDescription = new Label(grpCenterData, SWT.NONE);
		inputSizeDescription.setText(Messages.HuffmanCodingView_inputSize);

		inputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		inputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		Label spacerLabel = new Label(grpCenterData, SWT.NONE);
		spacerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		outputSizeDescription = new Label(grpCenterData, SWT.NONE);
		outputSizeDescription.setText(Messages.HuffmanCodingView_compOutputSize);

		outputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		outputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		compressionRateDescription = new Label(grpCenterData, SWT.NONE);
		compressionRateDescription.setText(Messages.HuffmanCodingView_compressionRate);

		compressionRate = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		compressionRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		btnSaveResult = new Button(grpCenterData, SWT.PUSH);
		btnSaveResult.setText(Messages.HuffmanCodingView_saveText);
		btnSaveResult.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnSaveResult.setEnabled(false);
		btnSaveResult.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// String outputFilename;

				FileDialog fd = new FileDialog(parent.getShell(), SWT.SAVE);
				fd.setText(Messages.HuffmanCodingView_saveText);
				fd.setFilterPath(System.getProperty("user.home") + System.getProperty("file.separator"));
				String[] filterExt = { "*.txt" };
				fd.setFilterExtensions(filterExt);
				String outputFilename = fd.open();

				if (outputFilename != null) {

					fileComp = new File(outputFilename);
					try (BufferedWriter out = new BufferedWriter(new FileWriter(fileComp))) {

						out.write(huffmanCode.getMessage());

					} catch (IOException ex) {
						MessageBox errorOnSave = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
						errorOnSave.setText(Messages.HuffmanCodingView_error0);
						errorOnSave.setMessage(Messages.HuffmanCodingView_error1);
						errorOnSave.open();
						LogUtil.logError(ex);
					} catch (InvalidCharacterException e2) {
						LogUtil.logError(e2);
						fileComp.delete();
						btnCompress.setEnabled(false);
						MessageBox errorOnSave = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
						errorOnSave.setText(Messages.HuffmanCodingView_error0);
						errorOnSave.setMessage(Messages.HuffmanCodingView_error1);
						errorOnSave.open();
					}
				}
			}
		});

		if (isCompressed && btnUncompress != null && !btnUncompress.isDisposed())
			btnUncompress.setEnabled(true);

		if (isCompressed) {
			inputSize.setText(Integer.toString(huffmanCodeBinary.length) + " " + Messages.HuffmanCodingView_24);
		}

		compInputText = new Composite(compInputOutput, SWT.NONE);
		compInputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compInputText = new GridLayout();
		gl_compInputText.marginHeight = 0;
		gl_compInputText.marginWidth = 0;
		compInputText.setLayout(gl_compInputText);

		inputHeader = new Label(compInputText, SWT.NONE);
		inputHeader.setText(Messages.HuffmanCodingView_headerOut);

		labelDisabledColor = ColorService.GRAY;
		labelEnabledColor = hexTableHeader.getForeground();
		inputHeader.setForeground(labelDisabledColor);

		textInput = new StyledText(compInputText, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textInput.setAlwaysShowScrollBars(false);
		GridData gdTextInput = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdTextInput.widthHint = textInput.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		gdTextInput.heightHint = textInput.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		textInput.setLayoutData(gdTextInput);

		return grpCompress;
	}

	/**
	 * Creates the compress main view containing radio buttons to select text and
	 * the input/output views, as well as statistic views
	 * 
	 * @return the Group with all the elements for the compress main view
	 */
	private Group createCompressGroup() {
		fileComp = null;
		fileUncomp = null;
		isUncompressed = false;

		Group grpCompress = new Group(mainViewComposite, SWT.NONE);
		grpCompress.setText(Messages.HuffmanCodingView_6);
		grpCompress.setLayout(new GridLayout(3, false));
		grpCompress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		btnRadioExampleText = new Button(grpCompress, SWT.RADIO);
		btnRadioExampleText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		btnRadioExampleText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (textInput.getText().isEmpty()) {
					loadExampleText();
					btnOpenUncompFile.setEnabled(false);
					textInput.setEditable(true);
					textFileUncompName.setText(""); //$NON-NLS-1$
					fileUncomp = null;

					isCompressed = false;
				}
			}
		});
		btnRadioExampleText.setSelection(true);
		btnRadioExampleText.setText(Messages.HuffmanCodingView_btnExampleText_text);

		btnRadioContentFromFile = new Button(grpCompress, SWT.RADIO);
		btnRadioContentFromFile.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		btnRadioContentFromFile.setText(Messages.HuffmanCodingView_btnContentFromFile_text);
		btnRadioContentFromFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textInput.setText(""); //$NON-NLS-1$
				textInput.setEditable(false);
				textFileUncompName.setText(""); //$NON-NLS-1$
				fileUncomp = null;
				btnOpenUncompFile.setEnabled(true);
				isCompressed = false;

			}
		});


		btnOpenUncompFile = new Button(grpCompress, SWT.NONE);
		btnOpenUncompFile.setText(Messages.HuffmanCodingView_3);
		btnOpenUncompFile.setEnabled(false);
		btnOpenUncompFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		btnOpenUncompFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				fd.setFilterExtensions(new String[] { "*.txt" }); //$NON-NLS-1$
				fd.setFilterNames(new String[] { Messages.HuffmanCodingView_2 });
				String filePath = fd.open();

				if (filePath != null) {
					fileUncomp = new File(filePath);
					textFileUncompName.setText(filePath);
					textFileUncompName.setSelection(filePath.length());

					try {
						Scanner scanner = new Scanner(fileUncomp, "ISO-8859-1"); //$NON-NLS-1$
						String fileString = scanner.useDelimiter("\\Z").next(); //$NON-NLS-1$
						scanner.close();

						textInput.setText(fileString);
						textInput.setEditable(false);

						btnCompress.setFocus();
					} catch (FileNotFoundException ex) {
						LogUtil.logError(ex);
						MessageBox errorOnRead = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
						errorOnRead.setText(Messages.HuffmanCodingView_error2);
						errorOnRead.setMessage(Messages.HuffmanCodingView_error3);
						errorOnRead.open();
					}

					outputSize.setText("");
					compressionRate.setText("");
					hexTable.clearAll();
					hexTable.removeAll();
					resizeHexTable();
					hexTableHeader.setForeground(labelDisabledColor);

				}
			}
		});

		textFileUncompName = new Text(grpCompress, SWT.BORDER | SWT.READ_ONLY);
		textFileUncompName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		horizontalSeparator = new Label(grpCompress, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gdHorizontalSeparator = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1);
		gdHorizontalSeparator.heightHint = 35;
		horizontalSeparator.setLayoutData(gdHorizontalSeparator);

		
		
		
		compInputOutput = new Composite(grpCompress, SWT.NONE);
		compInputOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		GridLayout gl_compInputOutput = new GridLayout(3, false);
		// The following 2 lines remove the 5 px lightgrey border
		// on the outside of the composite.
		gl_compInputOutput.marginHeight = 0;
		gl_compInputOutput.marginWidth = 0;
		compInputOutput.setLayout(gl_compInputOutput);

		compInputText = new Composite(compInputOutput, SWT.NONE);
		compInputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compInputText = new GridLayout();
		gl_compInputText.marginHeight = 0;
		gl_compInputText.marginWidth = 0;
		compInputText.setLayout(gl_compInputText);

		inputHeader = new Label(compInputText, SWT.NONE);
		inputHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		inputHeader.setText(Messages.HuffmanCodingView_5);

		textInput = new StyledText(compInputText, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textInput.setAlwaysShowScrollBars(false);
		GridData gdTextInput = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdTextInput.widthHint = textInput.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		gdTextInput.heightHint = textInput.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		textInput.setLayoutData(gdTextInput);

		loadExampleText();
		
		grpCenterData = new Group(compInputOutput, SWT.NONE);
		grpCenterData.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		grpCenterData.setLayout(new GridLayout(2, true));


		inputSizeDescription = new Label(grpCenterData, SWT.NONE);
		inputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		inputSizeDescription.setText(Messages.HuffmanCodingView_inputSize);

		inputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		inputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		inputSize.setText(textInput.getText().length() + " " + Messages.HuffmanCodingView_24);

		Label spacerLabel = new Label(grpCenterData, SWT.NONE);
		spacerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

		outputSizeDescription = new Label(grpCenterData, SWT.NONE);
		outputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		outputSizeDescription.setText(Messages.HuffmanCodingView_outputSize);

		outputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		outputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		compressionRateDescription = new Label(grpCenterData, SWT.NONE);
		compressionRateDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		compressionRateDescription.setText(Messages.HuffmanCodingView_compressionRate);

		compressionRate = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		compressionRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		btnSaveResult = new Button(grpCenterData, SWT.PUSH);
		btnSaveResult.setText(Messages.HuffmanCodingView_saveBinary);
		btnSaveResult.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnSaveResult.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(parent.getShell(), SWT.SAVE);
				fd.setText(Messages.HuffmanCodingView_saveBinary);
				fd.setFilterPath(System.getProperty("user.home") + System.getProperty("file.separator"));
				String[] filterExt = { "*.huffman" };
				fd.setFilterExtensions(filterExt);
				String outputFilename = fd.open();

				if (outputFilename != null) {

					fileComp = new File(outputFilename);
					try {
						if (!fileComp.exists()) {
							fileComp.createNewFile();
							huffmanCode.writeHuffmanBinary(fileComp);
						}

					} catch (IOException ex) {
						MessageBox errorOnSave = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
						errorOnSave.setText(Messages.HuffmanCodingView_error0);
						errorOnSave.setMessage(Messages.HuffmanCodingView_error1);
						errorOnSave.open();
						LogUtil.logError(ex);
					} catch (InvalidCharacterException e2) {
						LogUtil.logError(e2);
						fileComp.delete();
						btnCompress.setEnabled(false);
						MessageBox errorOnSave = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
						errorOnSave.setText(Messages.HuffmanCodingView_error0);
						errorOnSave.setMessage(Messages.HuffmanCodingView_error1);
						errorOnSave.open();
						reset();
					} catch (IllegalStateException e3) {
						LogUtil.logError(e3);
					}

				}
			}
		});

		compHexTable = new Composite(compInputOutput, SWT.NONE);
		compHexTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compHexTable = new GridLayout();
		gl_compHexTable.marginHeight = 0;
		gl_compHexTable.marginWidth = 0;
		compHexTable.setLayout(gl_compHexTable);

		hexTableHeader = new Label(compHexTable, SWT.NONE);
		hexTableHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		hexTableHeader.setText(Messages.HuffmanCodingView_compHeaderOut);

		labelDisabledColor = ColorService.GRAY;
		labelEnabledColor = hexTableHeader.getForeground();

		hexTableHeader.setForeground(labelDisabledColor);

		hexTable = new Table(compHexTable, SWT.V_SCROLL | SWT.BORDER);
		GridData gdHexTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdHexTable.widthHint = hexTable.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		gdHexTable.heightHint = hexTable.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		hexTable.setLayoutData(gdHexTable);
		hexTable.setHeaderVisible(true);
		hexTable.setFont(FontService.getNormalMonospacedFont());

		TableColumn offset = new TableColumn(hexTable, SWT.NONE);
		offset.setText("Offset (h)");

		TableColumn[] hexDigits = new TableColumn[16];
		for (int i = 0; i < hexDigits.length; ++i) {
			hexDigits[i] = new TableColumn(hexTable, SWT.NONE);
			hexDigits[i].setText("0" + Integer.toHexString(i).toUpperCase());
		}
		
		resizeHexTable();

		setCenterEnabled(false);

		textInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.stateMask == SWT.CTRL && e.keyCode == 'a') {
					textInput.selectAll();
				}
			}
		});
		textInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				inputSize.setText(Integer.toString(textInput.getText().length()) + " " + Messages.HuffmanCodingView_24);

				if (textInput.getText().isEmpty()) {
					btnCompress.setEnabled(false);
				} else {
					btnCompress.setEnabled(true);
				}

			}
		});

		return grpCompress;
	}
	
	/**
	 * Resizes the tablecolumns in the hex table according tp the 
	 * space each column needs.</br>
	 * Shoulb be called, after the content of the hex table changes.
	 */
	private void resizeHexTable() {
		for (TableColumn tc : hexTable.getColumns()) {
			tc.pack();
		}
	}

	@Override
	public void setFocus() {
		if (!textInput.isDisposed()) {
			textInput.setFocus();
		}
	}

	/**
	 * reset the view
	 */
	public void resetView() {
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
		parent.layout();

		reset();
	}

	/**
	 * reset all used elements
	 */
	private void reset() {
		grpCompress.dispose();
		grpNextSteps.dispose();

		grpNextSteps = createCompressButtonGroup();
		grpCompress = createCompressGroup();
		mainViewComposite.layout();

		huffmanCode = null;
		btnCompress.setEnabled(false);

		isCompressed = false;
		textInput.setFocus();

		mode = HuffmanCodingView.COMPRESS;

		loadExampleText();
	}

	private void setCenterEnabled(boolean enabled) {
		if (inputSizeDescription == null || inputSize == null || outputSizeDescription == null || outputSize == null
				|| compressionRateDescription == null || compressionRateDescription == null || btnSaveResult == null)
			return;
		outputSizeDescription.setEnabled(enabled);
		outputSize.setEnabled(enabled);
		compressionRateDescription.setEnabled(enabled);
		compressionRate.setEnabled(enabled);
		btnSaveResult.setEnabled(enabled);
	}

	private void parseBinaryHuffman(int[] huffmanEncodedAsInt) {
		String digits;
		int[] bytes = huffmanEncodedAsInt;

		TableItem[] hexValues = new TableItem[bytes.length / 16 + 1];
		for (int i = 0, file_index = 0; i < hexValues.length; ++i) {
			hexValues[i] = new TableItem(hexTable, SWT.NONE);
			String tmpOffset = Integer.toHexString(i * 16).toUpperCase();
			hexValues[i].setText(0, "00000000".replaceAll("(?<=^.{" + (8 - tmpOffset.length()) + "}).*", tmpOffset));

			for (int j = 1; j <= 16 && file_index < bytes.length; ++j, ++file_index) {
				if ((digits = Integer.toHexString(bytes[file_index]).toUpperCase()).length() == 1)
					digits = "0" + digits;
				hexValues[i].setText(j, digits);
			}
		}
		
		resizeHexTable();

	}

	private void parseBinaryHuffman(String path) {
		DataInputStream readFile = null;
		ArrayList<Integer> readBytes = new ArrayList<>();

		try {
			readFile = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(path))));
			int lineOfBytes;
			while ((lineOfBytes = readFile.read()) != -1) {
				readBytes.add(lineOfBytes);
			}
		} catch (IOException e1) {
			LogUtil.logError(e1);
			MessageBox errorOnRead = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
			errorOnRead.setText(Messages.HuffmanCodingView_error2);
			errorOnRead.setMessage(Messages.HuffmanCodingView_error3);
			errorOnRead.open();
		} finally {
			try {
				readFile.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		int[] tmp = new int[readBytes.size()];

		for (int i = 0; i < readBytes.size(); ++i) {
			tmp[i] = readBytes.get(i);
		}
		huffmanCodeBinary = tmp;
		parseBinaryHuffman(tmp);

	}

	public Node[] getCompressedNodes() {
		if (huffmanCode != null) {
			ArrayList<Node> nodeArray = huffmanCode.getResultNodeList();
			return nodeArray.toArray(new Node[nodeArray.size()]);
		}
		return new Node[0];
	}

	public BitString[] getBitStringTable() {
		if (huffmanCode != null) {
			return huffmanCode.getCodeTable();
		}
		return new BitString[0];
	}

	/**
	 * Switch to huffman tree tab and highlight the path to the selected node.
	 * @param nodeValue
	 */
	public void setAndHiglightGraph(String nodeValue) {

		// This opens the Huffman tree tab folder
		tabFolder.setSelection(VIEWTREE);
		tabFolder.notifyListeners(SWT.Selection, new Event());

		// This highlights the path to the selected char.
		treeView.highlightNode(nodeValue);
		tabFolder.setSelection(tbtmHuffmanTree);
	}

	public int getMode() {
		return mode;
	}

	public HuffmanCodingViewTree getViewTree() {
		if (treeView != null)
			return treeView;
		else
			return null;
	}

	public void visibleGraphMenu(boolean visible) {
		IActionBars actionBar = getViewSite().getActionBars();
		IContributionItem[] items = actionBar.getToolBarManager().getItems();

		for (IContributionItem item : items) {
			String id = item.getId();
			if (id.contains("Zoom") || id.contains("ChangeLayout") || id.contains("Separator"))
				item.setVisible(visible);
		}

		actionBar.updateActionBars();
	}

}
