//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
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

	public static final String ID = "org.jcryptool.visual.huffmanCoding.views.HuffmanCodingView"; //$NON-NLS-1$
	public final static int COMPRESS = 1;
	public final static int UNCOMPRESS = 2;

	private Composite parent;
	private TabFolder tabFolder;
	private TabItem tbtmHuffmanTree;
	private StyledText textInput;
	private Button btnUncompress;

	private Huffman huffmanCode;
	private int[] huffmanCodeBinary;

	private Text textFileUncompName;
	private Text textFileUncompSize;
	private Button btnOpenUncompFile;
	private File fileUncomp;
	private File fileComp;

	private TabItem tbtmCodeTable;
	private Composite compositeCT;

	private Table table;

	private boolean isCompressed = false;
	private boolean isUncompressed = false;
	private StyledText styledTextTree;

	private ArrayList<Control> codeTableControls;
	private StyledText styledTextDescription;
	private Button btnRadioCompress;
	private Button btnRadioUncompress;
	private Group grpSzenario;
	private Group grpCompress;
	private Composite composite;

	private DecimalFormat df = new DecimalFormat();
	private Group grpNextSteps;
	private Button btnCompress;
	private Button btnRadioExampleText;
	private Button btnRadioContentFromFile;
	private int mode = HuffmanCodingView.COMPRESS;
	private Button btnShowBranch;
	private Composite compInputText;
	private Composite compHexTable;

	private Label inputSizeDescription;
	private Label outputSizeDescription;
	private Label compressionRateDescription;
	private Text inputSize;
	private Text outputSize;
	private Text compressionRate;
	private Group grpCenterData;
	private Button btnSaveResult;

	private String filePath;

	private Color labelEnabled;
	private Color labelDisabled;

	private Label inputHeader;
	private Label hexTableHeader;

	private HuffmanCodingViewTree treeView;
	private HuffmanCodingViewTable tableView;

	private int TABLE_WIDTH = 560;

	public HuffmanCodingView() {
		codeTableControls = new ArrayList<Control>();
		new ArrayList<TableEditor>();
		new Hashtable<Integer, Button>();
	}

	private HuffmanCodingView mainView;

	private Table hexTable;
	private ArrayList<Integer> readBytes;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(new GridLayout(1, false));
		this.mainView = this;

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		tabFolder = new TabFolder(scrolledComposite, SWT.NONE);

		TabItem tbtmParameter = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter.setText(Messages.HuffmanCodingView_0);

		composite = new Composite(tabFolder, SWT.NONE);
		tbtmParameter.setControl(composite);
		composite.setLayout(new GridLayout(2, false));

		styledTextDescription = new StyledText(composite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextDescription.setText(Messages.HuffmanCodingView_16 + "\n" + Messages.HuffmanCodingView_1); //$NON-NLS-1$
		StyleRange title = new StyleRange();
		title.start = 0;
		title.length = Messages.HuffmanCodingView_16.length();
		title.fontStyle = SWT.BOLD;
		styledTextDescription.setStyleRange(title);
		GridData gd_styledTextDescription = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gd_styledTextDescription.widthHint = 960;

		if (System.getProperties().get("osgi.nl").toString().compareToIgnoreCase("de") == 0) {
			gd_styledTextDescription.heightHint = 110;
		} else if (System.getProperties().get("osgi.nl").toString().compareToIgnoreCase("en") == 0) {
			gd_styledTextDescription.heightHint = 95;
		}

		styledTextDescription.setLayoutData(gd_styledTextDescription);

		grpSzenario = new Group(composite, SWT.NONE);
		GridData gd_grpSzenario = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		// gd_grpSzenario.widthHint = 200;
		grpSzenario.setLayoutData(gd_grpSzenario);
		grpSzenario.setText(Messages.HuffmanCodingView_grpSzenario_text);
		grpSzenario.setLayout(new GridLayout(1, false));

		btnRadioCompress = new Button(grpSzenario, SWT.RADIO);
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
					composite.layout();
					setFocus();

					/*
					 * reset tabs
					 */
					isCompressed = false;

					styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
					styledTextTree.setAlignment(SWT.LEFT);
					styledTextTree.setFont(FontService.getNormalFont());
					codeTableControls.clear();

					loadExampleText();

					mode = HuffmanCodingView.COMPRESS;
				}

			}
		});
		GridData gd_btnRadioCompress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		// gd_btnRadioCompress.widthHint = 110;
		btnRadioCompress.setLayoutData(gd_btnRadioCompress);
		btnRadioCompress.setSelection(true);
		btnRadioCompress.setText(Messages.HuffmanCodingView_6);

		btnRadioUncompress = new Button(grpSzenario, SWT.RADIO);
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
					composite.layout();

					/*
					 * reset tabs
					 */

					styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
					styledTextTree.setAlignment(SWT.LEFT);
					styledTextTree.setFont(FontService.getNormalFont());
					codeTableControls.clear();

					mode = HuffmanCodingView.UNCOMPRESS;
				}
			}
		});
		GridData gd_btnRadioUncompress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		// gd_btnRadioUncompress.widthHint = 110;
		btnRadioUncompress.setLayoutData(gd_btnRadioUncompress);
		btnRadioUncompress.setText(Messages.HuffmanCodingView_7);

		/*
		 * create the compress / uncompress group
		 */

		grpNextSteps = createCompressButtonGroup();
		grpCompress = createCompressGroup();

		tbtmHuffmanTree = new TabItem(tabFolder, SWT.NONE);
		tbtmHuffmanTree.setText(Messages.HuffmanCodingView_13);

		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selection = tabFolder.getSelectionIndex();

				if (selection == 1 && (isCompressed || isUncompressed)) {
					if (treeView == null)
						treeView = new HuffmanCodingViewTree(tabFolder, SWT.NONE, table,
								huffmanCode.getResultNodeList());
					tbtmHuffmanTree.setControl(treeView);
					tabFolder.setSelection(selection);
				}
				if (selection == 2 && (isCompressed || isUncompressed)) {
					if (tableView == null)
						tableView = new HuffmanCodingViewTable(tabFolder, SWT.NONE, mainView);
					tbtmCodeTable.setControl(tableView);
					tabFolder.setSelection(selection);
				}

				if (selection != 0 && (!isCompressed && !isUncompressed)) {
					tabFolder.setSelection(0);
					MessageBox notReady = new MessageBox(tabFolder.getShell(), SWT.OK);
					notReady.setText(Messages.HuffmanCodingView_28);
					notReady.setMessage(Messages.HuffmanCodingView_29);
					notReady.open();

				}

			}
		});

		tbtmCodeTable = new TabItem(tabFolder, SWT.NONE);
		tbtmCodeTable.setText(Messages.HuffmanCodingView_14);

		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		loadExampleText();

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HuffmanCodingPlugin.PLUGIN_ID + ".view");
	}

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
			LogUtil.logError(e1);
		} catch (IOException e1) {
			LogUtil.logError(e1);
		}
	}

	private Group createCompressButtonGroup() {
		Group grpNextSteps = new Group(composite, SWT.NONE);
		GridLayout gl_grpNextSteps = new GridLayout(2, false);
		grpNextSteps.setLayout(gl_grpNextSteps);
		grpNextSteps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpNextSteps.setText(Messages.HuffmanCodingView_grpNextSteps_text);

		btnCompress = new Button(grpNextSteps, SWT.NONE);
		GridData gd_btnCompress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
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
					outputSize.setText(Integer.toString(huffmanCodeBinary.length));

					float compressedSize = huffmanCodeBinary.length;
					float rawSize = inputString.length();
					float compRate;

					compRate = (rawSize - compressedSize) / rawSize * 100;
					compressionRate.setText(String.format("%.2f%%", compRate));

					btnCompress.setEnabled(false);
					btnOpenUncompFile.setEnabled(false);
					textInput.setEditable(false);
					hexTableHeader.setForeground(labelEnabled);

					setCenterEnabled(true);

					isCompressed = true;

					String message = Messages.HuffmanCodingView_20;
					MessageDialog.openInformation(HuffmanCodingView.this.parent.getShell(),
							Messages.HuffmanCodingView_19, message);

					compositeCT.layout();
				}
			}
		});
		btnCompress.setText(Messages.HuffmanCodingView_6);
		return grpNextSteps;
	}

	private Group createUncompressButtonGroup() {
		Group grpNextSteps = new Group(composite, SWT.NONE);
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
				outputSize.setText(Integer.toString(message.length()));
				inputHeader.setForeground(labelEnabled);

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

		Group grpCompress = new Group(composite, SWT.NONE);
		grpCompress.setText(Messages.HuffmanCodingView_6);
		grpCompress.setLayout(new GridLayout(7, false));
		grpCompress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1));

		btnRadioExampleText = new Button(grpCompress, SWT.RADIO);
		btnRadioExampleText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 7, 1));
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
							inputSize.setText(Integer.toString(huffmanCodeBinary.length));
						} else {
							hexTable.removeAll();
							hexTable.clearAll();
							inputSize.setText("");
							textFileUncompName.setText("");
							btnUncompress.setEnabled(false);
						}
						textInput.setText("");
						inputHeader.setForeground(labelDisabled);
						btnSaveResult.setEnabled(false);
						outputSize.setText("");
						compressionRate.setText("");
						btnOpenUncompFile.setEnabled(false);
					}
				}
			}
		});
		btnRadioExampleText.setSelection(true);
		btnRadioExampleText.setText(Messages.HuffmanCodingView_radioUncompFromMem);

		btnRadioContentFromFile = new Button(grpCompress, SWT.RADIO);
		btnRadioContentFromFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button tmp = null;

				if (e.getSource() instanceof Button) {
					tmp = (Button) e.getSource();

					if (tmp.getSelection()) {

						hexTable.removeAll();
						hexTable.clearAll();
						inputSize.setText("");
						outputSize.setText("");
						textInput.setText("");
						inputHeader.setForeground(labelDisabled);

						btnOpenUncompFile.setEnabled(true);
						btnUncompress.setEnabled(false);

						btnSaveResult.setEnabled(false);
						compressionRate.setText("");

					}
				}
			}
		});
		GridData gd_btnRadioContentFromFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRadioContentFromFile.widthHint = 200;
		btnRadioContentFromFile.setLayoutData(gd_btnRadioContentFromFile);
		btnRadioContentFromFile.setText(Messages.HuffmanCodingView_radioUncompFromFile);

		btnOpenUncompFile = new Button(grpCompress, SWT.NONE);
		btnOpenUncompFile.setEnabled(false);
		GridData gd_btnOpenUncompFile = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		// gd_btnOpenUncompFile.widthHint = 180;
		btnOpenUncompFile.setLayoutData(gd_btnOpenUncompFile);
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
					inputSize.setText(Integer.toString(huffmanCodeBinary.length));

					btnUncompress.setEnabled(true);
					btnUncompress.setFocus();
					isCompressed = false;
				}
			}
		});
		btnOpenUncompFile.setText(Messages.HuffmanCodingView_3);

		textFileUncompName = new Text(grpCompress, SWT.BORDER | SWT.READ_ONLY);
		textFileUncompName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblFileUncompSize = new Label(grpCompress, SWT.RIGHT);
		lblFileUncompSize.setText(Messages.HuffmanCodingView_4);
		lblFileUncompSize.setVisible(false);

		textFileUncompSize = new Text(grpCompress, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		GridData gd_textFileUncompSize = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textFileUncompSize.widthHint = 140;
		textFileUncompSize.setLayoutData(gd_textFileUncompSize);
		textFileUncompSize.setVisible(false);

		Label horizontalSeparator = new Label(grpCompress, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gdHorizontalSeparator = new GridData(SWT.FILL, SWT.TOP, true, false, 7, 1);
		gdHorizontalSeparator.heightHint = 35;
		horizontalSeparator.setLayoutData(gdHorizontalSeparator);

		Composite compAllData = new Composite(grpCompress, SWT.NONE);
		compAllData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		compAllData.setLayout(new GridLayout(7, false));

		compHexTable = new Composite(compAllData, SWT.NONE);
		compHexTable.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 3, 1));
		compHexTable.setLayout(new GridLayout(1, true));

		hexTableHeader = new Label(compHexTable, SWT.NONE);
		hexTableHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		hexTableHeader.setText(Messages.HuffmanCodingView_headerIn);

		hexTable = new Table(compHexTable, SWT.V_SCROLL | SWT.BORDER);
		GridData gdHexTable = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gdHexTable.widthHint = TABLE_WIDTH;
		hexTable.setLayoutData(gdHexTable);
		hexTable.setHeaderVisible(true);
		try {
			Font testFont = new Font(Display.getDefault(), "Courier", 10, SWT.NORMAL);
			hexTable.setFont(testFont);
		} catch (SWTError e) {
			LogUtil.logError("Could not load font Courier");
		}

		TableColumn offset = new TableColumn(hexTable, SWT.NONE);
		offset.setText(Messages.HuffmanCodingView_offset);
		offset.setWidth(95);
		offset.setResizable(false);

		TableColumn[] hexDigits = new TableColumn[16];
		for (int i = 0; i < hexDigits.length; ++i) {
			hexDigits[i] = new TableColumn(hexTable, SWT.NONE);
			hexDigits[i].setText("0" + Integer.toHexString(i).toUpperCase());
			hexDigits[i].setWidth(30);
			hexDigits[i].setResizable(false);

		}

		if (huffmanCodeBinary != null)
			parseBinaryHuffman(huffmanCodeBinary);

		grpCenterData = new Group(compAllData, SWT.NONE);
		grpCenterData.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		grpCenterData.setLayout(new GridLayout(4, true));

		inputSizeDescription = new Label(grpCenterData, SWT.NONE);
		inputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		inputSizeDescription.setText(Messages.HuffmanCodingView_compInputSize);

		inputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		inputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Label spacerLabel = new Label(grpCenterData, SWT.NONE);
		spacerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));

		outputSizeDescription = new Label(grpCenterData, SWT.NONE);
		outputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		outputSizeDescription.setText(Messages.HuffmanCodingView_compOutputSize);

		outputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		outputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		compressionRateDescription = new Label(grpCenterData, SWT.NONE);
		compressionRateDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		compressionRateDescription.setText(Messages.HuffmanCodingView_compressionRate);

		compressionRate = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		compressionRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		btnSaveResult = new Button(grpCenterData, SWT.PUSH);
		btnSaveResult.setText(Messages.HuffmanCodingView_saveText);
		btnSaveResult.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1));
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
			inputSize.setText(Integer.toString(huffmanCodeBinary.length));
		}

		compInputText = new Composite(compAllData, SWT.NONE);
		compInputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		compInputText.setLayout(new GridLayout(1, true));

		inputHeader = new Label(compInputText, SWT.NONE);
		inputHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		inputHeader.setText(Messages.HuffmanCodingView_headerOut);

		labelDisabled = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		labelEnabled = hexTableHeader.getForeground();
		inputHeader.setForeground(labelDisabled);

		textInput = new StyledText(compInputText, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textInput.setAlwaysShowScrollBars(false);
		GridData gdTextInput = new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1);
		gdTextInput.widthHint = TABLE_WIDTH;
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

		Group grpCompress = new Group(composite, SWT.NONE);
		grpCompress.setText(Messages.HuffmanCodingView_6);
		grpCompress.setLayout(new GridLayout(7, false));
		grpCompress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1));

		btnRadioExampleText = new Button(grpCompress, SWT.RADIO);
		btnRadioExampleText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 7, 1));
		btnRadioExampleText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (textInput.getText().isEmpty()) {
					loadExampleText();
					btnOpenUncompFile.setEnabled(false);
					textInput.setEditable(true);
					textFileUncompName.setText(""); //$NON-NLS-1$
					textFileUncompSize.setText(""); //$NON-NLS-1$
					fileUncomp = null;

					isCompressed = false;

					styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
					styledTextTree.setAlignment(SWT.LEFT);
					styledTextTree.setFont(FontService.getNormalFont());
					codeTableControls.clear();

					// compInputText.setText(Messages.HuffmanCodingView_5);

				}
			}
		});
		btnRadioExampleText.setSelection(true);
		btnRadioExampleText.setText(Messages.HuffmanCodingView_btnExampleText_text);

		btnRadioContentFromFile = new Button(grpCompress, SWT.RADIO);
		btnRadioContentFromFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textInput.setText(""); //$NON-NLS-1$
				textInput.setEditable(false);
				textFileUncompName.setText(""); //$NON-NLS-1$
				textFileUncompSize.setText(""); //$NON-NLS-1$
				fileUncomp = null;
				btnOpenUncompFile.setEnabled(true);

				isCompressed = false;

				styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
				styledTextTree.setAlignment(SWT.LEFT);
				styledTextTree.setFont(FontService.getNormalFont());
				codeTableControls.clear();

			}
		});
		GridData gd_btnRadioContentFromFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRadioContentFromFile.widthHint = 200;
		btnRadioContentFromFile.setLayoutData(gd_btnRadioContentFromFile);
		btnRadioContentFromFile.setText(Messages.HuffmanCodingView_btnContentFromFile_text);

		btnOpenUncompFile = new Button(grpCompress, SWT.NONE);
		btnOpenUncompFile.setEnabled(false);
		GridData gd_btnOpenUncompFile = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		// gd_btnOpenUncompFile.widthHint = 180;
		btnOpenUncompFile.setLayoutData(gd_btnOpenUncompFile);
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
					int filesize = (int) fileUncomp.length();
					textFileUncompSize.setText(df.format(filesize));

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
					hexTableHeader.setForeground(labelDisabled);

				}
			}
		});
		btnOpenUncompFile.setText(Messages.HuffmanCodingView_3);

		textFileUncompName = new Text(grpCompress, SWT.BORDER | SWT.READ_ONLY);
		textFileUncompName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblFileUncompSize = new Label(grpCompress, SWT.RIGHT);
		lblFileUncompSize.setText(Messages.HuffmanCodingView_4);
		lblFileUncompSize.setVisible(false);

		textFileUncompSize = new Text(grpCompress, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		GridData gd_textFileUncompSize = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textFileUncompSize.widthHint = 140;
		textFileUncompSize.setLayoutData(gd_textFileUncompSize);
		textFileUncompSize.setVisible(false);

		Label horizontalSeparator = new Label(grpCompress, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gdHorizontalSeparator = new GridData(SWT.FILL, SWT.TOP, true, false, 7, 1);
		gdHorizontalSeparator.heightHint = 35;
		horizontalSeparator.setLayoutData(gdHorizontalSeparator);

		compInputText = new Composite(grpCompress, SWT.NONE);
		compInputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		compInputText.setLayout(new GridLayout(1, true));

		inputHeader = new Label(compInputText, SWT.NONE);
		inputHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		inputHeader.setText(Messages.HuffmanCodingView_5);

		textInput = new StyledText(compInputText, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textInput.setAlwaysShowScrollBars(false);
		GridData gdTextInput = new GridData(SWT.RIGHT, SWT.FILL, true, true, 1, 1);
		gdTextInput.widthHint = TABLE_WIDTH;
		textInput.setLayoutData(gdTextInput);
		loadExampleText();

		grpCenterData = new Group(grpCompress, SWT.NONE);
		grpCenterData.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		grpCenterData.setLayout(new GridLayout(4, true));

		inputSizeDescription = new Label(grpCenterData, SWT.NONE);
		inputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		inputSizeDescription.setText(Messages.HuffmanCodingView_inputSize);

		inputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		inputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Label spacerLabel = new Label(grpCenterData, SWT.NONE);
		spacerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));

		outputSizeDescription = new Label(grpCenterData, SWT.NONE);
		outputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		outputSizeDescription.setText(Messages.HuffmanCodingView_outputSize);

		outputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		outputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		compressionRateDescription = new Label(grpCenterData, SWT.NONE);
		compressionRateDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		compressionRateDescription.setText(Messages.HuffmanCodingView_compressionRate);

		compressionRate = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		compressionRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		btnSaveResult = new Button(grpCenterData, SWT.PUSH);
		btnSaveResult.setText(Messages.HuffmanCodingView_saveBinary);
		btnSaveResult.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1));
		btnSaveResult.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// String outputFilename;

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

		compHexTable = new Composite(grpCompress, SWT.NONE);
		compHexTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		compHexTable.setLayout(new GridLayout(6, true));

		hexTableHeader = new Label(compHexTable, SWT.NONE);
		hexTableHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		hexTableHeader.setText(Messages.HuffmanCodingView_compHeaderOut);

		labelDisabled = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		labelEnabled = hexTableHeader.getForeground();

		hexTableHeader.setForeground(labelDisabled);

		hexTable = new Table(compHexTable, SWT.V_SCROLL | SWT.BORDER);
		GridData gdHexTable = new GridData(SWT.LEFT, SWT.FILL, true, true, 6, 1);
		gdHexTable.widthHint = TABLE_WIDTH;
		hexTable.setLayoutData(gdHexTable);
		hexTable.setHeaderVisible(true);
		try {
			Font testFont = new Font(Display.getDefault(), "Courier", 10, SWT.NORMAL);
			hexTable.setFont(testFont);
		} catch (SWTError e) {
			LogUtil.logError("Could not load font Courier");
		}

		TableColumn offset = new TableColumn(hexTable, SWT.NONE);
		offset.setText("Offset (h)");
		offset.setWidth(95);
		offset.setResizable(false);

		TableColumn[] hexDigits = new TableColumn[16];
		for (int i = 0; i < hexDigits.length; ++i) {
			hexDigits[i] = new TableColumn(hexTable, SWT.NONE);
			hexDigits[i].setText("0" + Integer.toHexString(i).toUpperCase());
			hexDigits[i].setWidth(30);
			hexDigits[i].setResizable(false);

		}

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
				inputSize.setText(Integer.toString(textInput.getText().length()));

				if (textInput.getText().isEmpty()) {
					btnCompress.setEnabled(false);
				} else {
					btnCompress.setEnabled(true);
				}

			}
		});

		return grpCompress;
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
		composite.layout();

		huffmanCode = null;
		btnCompress.setEnabled(false);

		isCompressed = false;
		textInput.setFocus();

		styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
		styledTextTree.setAlignment(SWT.LEFT);
		styledTextTree.setFont(FontService.getNormalFont());

		codeTableControls.clear();
		mode = HuffmanCodingView.COMPRESS;

		btnShowBranch.setEnabled(false);

		loadExampleText();
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

	private void parseBinaryHuffman(String path) {
		DataInputStream readFile = null;
		readBytes = new ArrayList<>();
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

	public void setAndHiglightGraph(String nodeValue) {
		if (treeView != null) {
			treeView.highlightNode(nodeValue);
			tabFolder.setSelection(tbtmHuffmanTree);
		}
	}

	public int getMode() {
		return mode;
	}

}
