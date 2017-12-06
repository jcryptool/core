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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.huffmanCoding.HuffmanCodingPlugin;
import org.jcryptool.visual.huffmanCoding.algorithm.BitString;
import org.jcryptool.visual.huffmanCoding.algorithm.Huffman;
import org.jcryptool.visual.huffmanCoding.algorithm.InvalidCharacterException;
import org.jcryptool.visual.huffmanCoding.algorithm.Node;

/**
 * 
 * @author Miray Inel
 * 
 */
public class HuffmanCodingView extends ViewPart implements IZoomableWorkbenchPart {

	public static final String ID = "org.jcryptool.visual.huffmanCoding.views.HuffmanCodingView"; //$NON-NLS-1$
	public final static int COMPRESS = 1;
	public final static int UNCOMPRESS = 2;

	private Composite parent;
	private TabFolder tabFolder;
	private StyledText textInput;
	private Text textOutput;
	private Button btnUncompress;

	private Huffman huffmanCode;
	private int[] huffmanCodeBinary;

	private Text textFileUncompName;
	private Text textFileUncompSize;
	private Text textFileCompName;
	private Text textFileCompSize;
	private Button btnOpenUncompFile;
	private Button btnOpenCompFile;
	private File fileUncomp;
	private File fileComp;
	private Composite compositeTree;

	private GraphViewer viewer;
	private TabItem tbtmCodeTable;
	private Composite compositeCT;

	private int layoutCounter = 1;

	private boolean tblclmnCharacterMode = false;
	private boolean tblclmnProbabilityMode = false;
	private boolean tblclmnCodeMode = false;
	private boolean tblclmnCodeLengthMode = false;

	private Table table;

	private boolean isCompressed = false;
	private boolean isUncompressed = false;
	private StyledText styledTextTree;

	private ArrayList<GraphConnection> markedConnectionList;
	private ArrayList<Control> codeTableControls;
	private StyledText styledTextDescription;
	private Button btnRadioCompress;
	private Button btnRadioUncompress;
	private Group grpSzenario;
	private Group grpCompress;
	private Composite composite;

	private MenuManager zoom;

	private DecimalFormat df = new DecimalFormat();
	private Group grpNextSteps;
	private Button btnCompress;
	private Button btnApplyFile;
	private Button btnRadioExampleText;
	private Button btnRadioContentFromFile;
	private StyledText styledTextCodetable;
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

	private TabItem testTreeTab;
	private HuffmanCodingViewTree testTree;
	private HuffmanCodingViewTable testTable;

	private int TABLE_WIDTH = 560;

	public HuffmanCodingView() {
		markedConnectionList = new ArrayList<GraphConnection>();
		codeTableControls = new ArrayList<Control>();
		new ArrayList<TableEditor>();
		new Hashtable<Integer, Button>();
	}

	private HuffmanCodingView mainView;

	// TODO
	// *****************************
	//
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

					btnApplyFile.setEnabled(false);

					/*
					 * reset tabs
					 */
					resetCodeTable();

					viewer.setInput(null);
					isCompressed = false;

					styledTextTree.setText(Messages.HuffmanCodingView_28);
					styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
					styledTextTree.setAlignment(SWT.LEFT);
					styledTextTree.setFont(FontService.getNormalFont());
					markedConnectionList = new ArrayList<GraphConnection>();
					layoutCounter = 1;
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
					resetCodeTable();

					viewer.setInput(null);
					// isCompressed = false;

					styledTextTree.setText(Messages.HuffmanCodingView_28);
					styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
					styledTextTree.setAlignment(SWT.LEFT);
					styledTextTree.setFont(FontService.getNormalFont());
					markedConnectionList = new ArrayList<GraphConnection>();
					layoutCounter = 1;
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

		// grpNextSteps = createUncompressButtonGroup();
		// grpCompress = createUncompressGroup();

		TabItem tbtmHuffmanTree = new TabItem(tabFolder, SWT.NONE);
		tbtmHuffmanTree.setText(Messages.HuffmanCodingView_13);

		compositeTree = new Composite(tabFolder, SWT.NONE);
		compositeTree.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				viewer.applyLayout();
			}
		});
		compositeTree.setLayout(new GridLayout(1, false));

		styledTextTree = new StyledText(compositeTree, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextTree.setText(Messages.HuffmanCodingView_28);
		styledTextTree.setFont(FontService.getNormalFont());

		GridData gd_styledTextTree = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		gd_styledTextTree.widthHint = 960;
		gd_styledTextTree.heightHint = 40;
		styledTextTree.setLayoutData(gd_styledTextTree);

		viewer = new GraphViewer(compositeTree, SWT.NONE);
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider());
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);

		Control control = viewer.getControl();
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Graph graph = viewer.getGraphControl();
		graph.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof GraphNode) {
					GraphNode node = (GraphNode) e.item;
					Node n = (Node) node.getData();

					if (n.isLeaf()) {
						styledTextTree.setForeground(new Color(null, new RGB(1, 70, 122)));
						styledTextTree.setFont(FontService.getHugeFont());
						styledTextTree.setText(
								Messages.ZestLabelProvider_5 + " '" + n.getNameAsString() + "': " + n.getCode()); //$NON-NLS-1$ //$NON-NLS-2$

						if (markedConnectionList.size() == 0) {
							markBranch(node);
						} else {
							unmarkBranch(markedConnectionList);
							markedConnectionList.clear();
							markBranch(node);
						}
					} else {
						if (markedConnectionList.size() != 0) {
							unmarkBranch(markedConnectionList);
							markedConnectionList.clear();
						}
						styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
						styledTextTree.setAlignment(SWT.LEFT);
						styledTextTree.setFont(FontService.getNormalFont());
						styledTextTree.setText(Messages.ZestLabelProvider_4);
					}

					Table table = (Table) compositeCT.getChildren()[2];
					TableItem[] tmpItem = table.getItems();
					for (int i = 0; i < tmpItem.length; i++) {
						if (n.getNameAsString().compareTo(tmpItem[i].getText(0)) == 0) {
							table.setSelection(tmpItem[i]);
							table.showSelection();
							break;
						} else {
							table.showItem(table.getItem(0));
							table.deselectAll();
							btnShowBranch.setEnabled(false);
						}
					}

				}
			}
		});

		tbtmHuffmanTree.setControl(compositeTree);

		// TODO

		testTreeTab = new TabItem(tabFolder, SWT.NONE);
		testTreeTab.setText("Class Tree");

		TabItem testTableTab = new TabItem(tabFolder, SWT.NONE);
		testTableTab.setText("Class Table");

		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selection = tabFolder.getSelectionIndex();

				if (selection == 2 && (isCompressed || isUncompressed)) {
					if (testTree == null)
						testTree = new HuffmanCodingViewTree(tabFolder, SWT.NONE, table,
								huffmanCode.getResultNodeList());
					testTreeTab.setControl(testTree);
					tabFolder.setSelection(selection);
				}
				if (selection == 3 && (isCompressed || isUncompressed)) {
					if (testTable == null)
						testTable = new HuffmanCodingViewTable(tabFolder, SWT.NONE, mainView);
					testTableTab.setControl(testTable);
					tabFolder.setSelection(selection);
				}

				if (selection != 0 && (!isCompressed && !isUncompressed)) {
					tabFolder.setSelection(0);
					MessageBox notReady = new MessageBox(tabFolder.getShell(), SWT.OK);
					notReady.setText("Compress a text or file before proceeding");
					notReady.setMessage("Stop");
					notReady.open();

				}

			}
		});

		tbtmCodeTable = new TabItem(tabFolder, SWT.NONE);
		tbtmCodeTable.setText(Messages.HuffmanCodingView_14);

		compositeCT = new Composite(tabFolder, SWT.NONE);
		tbtmCodeTable.setControl(compositeCT);
		compositeCT.setLayout(new GridLayout(2, false));

		styledTextCodetable = new StyledText(compositeCT, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextCodetable.setText(Messages.HuffmanCodingView_29);
		styledTextCodetable.setFont(FontService.getNormalFont());
		styledTextCodetable.setEditable(false);
		GridData gd_styledTextCodetable = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_styledTextCodetable.heightHint = 90;
		styledTextCodetable.setLayoutData(gd_styledTextCodetable);

		btnShowBranch = new Button(compositeCT, SWT.NONE);
		btnShowBranch.setEnabled(false);
		btnShowBranch.setText(Messages.HuffmanCodingView_15);
		btnShowBranch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selection = table.getSelectionIndex();

				TableItem tmpItem = table.getItem(selection);
				String code;
				if (mode == HuffmanCodingView.COMPRESS) {
					code = tmpItem.getText(2);
				} else {
					code = tmpItem.getText(1);
				}
				GraphNode graphNode = null;

				List<GraphNode> graphNodeList = viewer.getGraphControl().getNodes();
				for (GraphNode gn : graphNodeList) {
					Node n = (Node) gn.getData();
					if (n.isLeaf() && n.getCode().compareTo(code) == 0) {
						graphNode = gn;
						styledTextTree.setForeground(new Color(null, new RGB(1, 70, 122)));
						styledTextTree.setFont(FontService.getHugeFont());
						styledTextTree.setText(Messages.ZestLabelProvider_5 + " '" + n.getNameAsString() + "': " //$NON-NLS-1$ //$NON-NLS-2$
								+ n.getCode());
						break;
					}
				}

				if (!markedConnectionList.isEmpty()) {
					unmarkBranch(markedConnectionList);
				}
				markBranch(graphNode);
				tabFolder.setSelection(1);

				table.setSelection(selection);
			}
		});

		GridData gd_btnShowBranch = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnShowBranch.heightHint = 40;
		gd_btnShowBranch.widthHint = 120;
		btnShowBranch.setLayoutData(gd_btnShowBranch);

		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		fillToolBar();

		loadExampleText();

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HuffmanCodingPlugin.PLUGIN_ID + ".view");
	}

	/**
	 * Creation of the code table tab
	 * 
	 * @param bitStrings
	 *            - the bitstring array which contains all elements of the huffman
	 *            tree
	 */
	private void createCodeTableCompress(BitString[] bitStrings) {
		if (compositeCT.getChildren().length > 0) {
			for (int i = 0; i < compositeCT.getChildren().length; i++) {
				if (compositeCT.getChildren()[i] instanceof Table)
					compositeCT.getChildren()[i].dispose();
			}
		}
		styledTextCodetable.setText(""); //$NON-NLS-1$

		table = new Table(compositeCT, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnShowBranch.setEnabled(true);
			}

		});

		final TableColumn tblclmnCharacter = new TableColumn(table, SWT.NONE);
		tblclmnCharacter.setWidth(100);
		tblclmnCharacter.setText(Messages.HuffmanCodingView_tblclmnCharacter_text);
		tblclmnCharacter.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] items = table.getItems();
				Collator collator = Collator.getInstance(Locale.getDefault());
				for (int i = 1; i < items.length; i++) {
					String value1 = items[i].getText(0);
					for (int j = 0; j < i; j++) {
						String value2 = items[j].getText(0);
						if (tblclmnCharacterMode) {
							if (collator.compare(value1, value2) < 0) {

								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (collator.compare(value1, value2) > 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnCharacterMode) {
					tblclmnCharacterMode = false;
				} else {
					tblclmnCharacterMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		TableColumn tblclmnProbability = new TableColumn(table, SWT.NONE);
		tblclmnProbability.setWidth(140);
		tblclmnProbability.setText(Messages.HuffmanCodingView_tblclmnPropability_text);
		tblclmnProbability.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				TableItem[] items = table.getItems();
				Collator collator = Collator.getInstance(Locale.getDefault());
				for (int i = 1; i < items.length; i++) {
					String value1 = items[i].getText(1);
					for (int j = 0; j < i; j++) {
						String value2 = items[j].getText(1);
						if (tblclmnProbabilityMode) {
							if (collator.compare(value1, value2) < 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (collator.compare(value1, value2) > 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnProbabilityMode) {
					tblclmnProbabilityMode = false;
				} else {
					tblclmnProbabilityMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		TableColumn tblclmnCode = new TableColumn(table, SWT.NONE);
		tblclmnCode.setWidth(140);
		tblclmnCode.setText(Messages.HuffmanCodingView_tblclmnCode_text);
		tblclmnCode.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] items = table.getItems();
				Collator collator = Collator.getInstance(Locale.getDefault());
				for (int i = 1; i < items.length; i++) {
					String value1 = items[i].getText(2);
					for (int j = 0; j < i; j++) {
						String value2 = items[j].getText(2);
						if (tblclmnCodeMode) {
							if (collator.compare(value1, value2) < 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (collator.compare(value1, value2) > 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnCodeMode) {
					tblclmnCodeMode = false;
				} else {
					tblclmnCodeMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		TableColumn tblclmnCodeLength = new TableColumn(table, SWT.NONE);
		tblclmnCodeLength.setWidth(100);
		tblclmnCodeLength.setText(Messages.HuffmanCodingView_tblclmnCodeLength_text);
		tblclmnCodeLength.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] items = table.getItems();
				for (int i = 1; i < items.length; i++) {
					int value1 = Integer.parseInt(items[i].getText(3));
					for (int j = 0; j < i; j++) {
						int value2 = Integer.parseInt(items[j].getText(3));
						if (tblclmnCodeLengthMode) {
							if (value1 > value2) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (value1 < value2) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnCodeLengthMode) {
					tblclmnCodeLengthMode = false;
				} else {
					tblclmnCodeLengthMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		int counter = 0;
		double avarageCodelength = 0.0;
		int maxCodelenght = bitStrings[0].getLength();
		int minCodelenght = bitStrings[0].getLength();

		if (bitStrings != null) {
			for (int i = 0; i < bitStrings.length; i++) {
				if (bitStrings[i] != null) {

					TableItem item = new TableItem(table, SWT.NONE);

					switch (i) {
					case 0:
						item.setText(0, "NUL"); // Null //$NON-NLS-1$
						break;
					case 9:
						item.setText(0, "TAB"); // Tabulator //$NON-NLS-1$
						break;
					case 10:
						item.setText(0, "LF"); // Line Feed //$NON-NLS-1$
						break;
					case 13:
						item.setText(0, "CR"); // Carriage Return //$NON-NLS-1$
						break;
					case 32:
						item.setText(0, "\u2423"); // Space //$NON-NLS-1$
						break;
					case 38:
						item.setText(0, "&&"); // & //$NON-NLS-1$

						break;
					default:
						item.setText(0, String.valueOf((char) i));
					}
					item.setText(2, bitStrings[i].toString());
					item.setText(3, String.valueOf(bitStrings[i].toString().length()));

					Node tmp = null;
					for (Node n : huffmanCode.getResultNodeList()) {
						if (n.getName() == i) {
							tmp = n;
							break;
						}
					}

					item.setText(1, String.valueOf(String.format("%2.9f", tmp.getValue()))); //$NON-NLS-1$

					if (minCodelenght > bitStrings[i].getLength())
						minCodelenght = bitStrings[i].getLength();

					if (maxCodelenght < bitStrings[i].getLength())
						maxCodelenght = bitStrings[i].getLength();

					counter++;
					avarageCodelength += tmp.getValue() * bitStrings[i].getLength();
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append(Messages.HuffmanCodingView_codetable_stat_header);

			sb.append(Messages.HuffmanCodingView_codetable_stat_1 + counter);
			sb.append("\t\t\t"); //$NON-NLS-1$

			sb.append(Messages.HuffmanCodingView_codetable_stat_3 + minCodelenght);
			sb.append("\t\t\t"); //$NON-NLS-1$

			sb.append(Messages.HuffmanCodingView_codetable_stat_4 + maxCodelenght);
			sb.append("\t\t\t"); //$NON-NLS-1$

			if (mode == HuffmanCodingView.COMPRESS) {
				sb.append(Messages.HuffmanCodingView_codetable_stat_5
						+ String.valueOf(String.format("%2.3f", avarageCodelength))); //$NON-NLS-1$
			}

			sb.append("\n\n" + Messages.HuffmanCodingView_codetable_stat_2);
			styledTextCodetable.setText(sb.toString());

			StyleRange title = new StyleRange();
			title.start = 0;
			title.length = Messages.HuffmanCodingView_codetable_stat_header.length();
			title.fontStyle = SWT.BOLD;
			styledTextCodetable.setStyleRange(title);
		}
	}

	/**
	 * Creation of the code table tab
	 * 
	 * @param bitStrings
	 *            - the bitstring array which contains all elements of the huffman
	 *            tree
	 */
	private void createCodeTableUncompress(BitString[] bitStrings) {
		if (compositeCT.getChildren().length > 0) {
			for (int i = 0; i < compositeCT.getChildren().length; i++) {
				if (compositeCT.getChildren()[i] instanceof Table)
					compositeCT.getChildren()[i].dispose();
			}
		}
		styledTextCodetable.setText(""); //$NON-NLS-1$

		table = new Table(compositeCT, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnShowBranch.setEnabled(true);
			}

		});

		final TableColumn tblclmnCharacter = new TableColumn(table, SWT.NONE);
		tblclmnCharacter.setWidth(100);
		tblclmnCharacter.setText(Messages.HuffmanCodingView_tblclmnCharacter_text);
		tblclmnCharacter.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] items = table.getItems();
				Collator collator = Collator.getInstance(Locale.getDefault());
				for (int i = 1; i < items.length; i++) {
					String value1 = items[i].getText(0);
					for (int j = 0; j < i; j++) {
						String value2 = items[j].getText(0);
						if (tblclmnCharacterMode) {
							if (collator.compare(value1, value2) < 0) {

								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (collator.compare(value1, value2) > 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnCharacterMode) {
					tblclmnCharacterMode = false;
				} else {
					tblclmnCharacterMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		TableColumn tblclmnCode = new TableColumn(table, SWT.NONE);
		tblclmnCode.setWidth(140);
		tblclmnCode.setText(Messages.HuffmanCodingView_tblclmnCode_text);
		tblclmnCode.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] items = table.getItems();
				Collator collator = Collator.getInstance(Locale.getDefault());
				for (int i = 1; i < items.length; i++) {
					String value1 = items[i].getText(1);
					for (int j = 0; j < i; j++) {
						String value2 = items[j].getText(1);
						if (tblclmnCodeMode) {
							if (collator.compare(value1, value2) < 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (collator.compare(value1, value2) > 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnCodeMode) {
					tblclmnCodeMode = false;
				} else {
					tblclmnCodeMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		TableColumn tblclmnCodeLength = new TableColumn(table, SWT.NONE);
		tblclmnCodeLength.setWidth(100);
		tblclmnCodeLength.setText(Messages.HuffmanCodingView_tblclmnCodeLength_text);
		tblclmnCodeLength.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] items = table.getItems();
				for (int i = 1; i < items.length; i++) {
					int value1 = Integer.parseInt(items[i].getText(2));
					for (int j = 0; j < i; j++) {
						int value2 = Integer.parseInt(items[j].getText(2));
						if (tblclmnCodeLengthMode) {
							if (value1 > value2) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (value1 < value2) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnCodeLengthMode) {
					tblclmnCodeLengthMode = false;
				} else {
					tblclmnCodeLengthMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		int counter = 0;
		int maxCodelenght = bitStrings[0].getLength();
		int minCodelenght = bitStrings[0].getLength();

		if (bitStrings != null) {
			for (int i = 0; i < bitStrings.length; i++) {
				if (bitStrings[i] != null) {

					TableItem item = new TableItem(table, SWT.NONE);

					switch (i) {
					case 0:
						item.setText(0, "NUL"); // Null //$NON-NLS-1$
						break;
					case 9:
						item.setText(0, "TAB"); // Tabulator //$NON-NLS-1$
						break;
					case 10:
						item.setText(0, "LF"); // Line Feed //$NON-NLS-1$
						break;
					case 13:
						item.setText(0, "CR"); // Carriage Return //$NON-NLS-1$
						break;
					case 32:
						item.setText(0, "\u2423"); // Space //$NON-NLS-1$
						break;
					case 38:
						item.setText(0, "&&"); // & //$NON-NLS-1$

						break;
					default:
						item.setText(0, String.valueOf((char) i));
					}
					item.setText(1, bitStrings[i].toString());
					item.setText(2, String.valueOf(bitStrings[i].toString().length()));

					Node tmp = null;
					for (Node n : huffmanCode.getResultNodeList()) {
						if (n.getName() == i) {
							tmp = n;
							break;
						}
					}

					if (minCodelenght > bitStrings[i].getLength())
						minCodelenght = bitStrings[i].getLength();

					if (maxCodelenght < bitStrings[i].getLength())
						maxCodelenght = bitStrings[i].getLength();

					counter++;
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append(Messages.HuffmanCodingView_codetable_stat_header);

			sb.append(Messages.HuffmanCodingView_codetable_stat_1 + counter);
			sb.append("\t\t\t"); //$NON-NLS-1$

			sb.append(Messages.HuffmanCodingView_codetable_stat_3 + minCodelenght);
			sb.append("\t\t\t"); //$NON-NLS-1$

			sb.append(Messages.HuffmanCodingView_codetable_stat_4 + maxCodelenght);
			sb.append("\t\t\t"); //$NON-NLS-1$

			sb.append("\n\n" + Messages.HuffmanCodingView_codetable_stat_2);
			styledTextCodetable.setText(sb.toString());

			StyleRange title = new StyleRange();
			title.start = 0;
			title.length = Messages.HuffmanCodingView_codetable_stat_header.length();
			title.fontStyle = SWT.BOLD;
			styledTextCodetable.setStyleRange(title);
		}
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

					String outputFilename;
					if (fileUncomp == null) {
						String outputPath = System.getProperty("user.home") + System.getProperty("file.separator") //$NON-NLS-1$ //$NON-NLS-2$
								+ "Documents" //$NON-NLS-1$
								+ System.getProperty("file.separator"); //$NON-NLS-1$
						if (System.getProperties().get("osgi.nl").toString().compareToIgnoreCase("de") == 0) {
							outputFilename = outputPath + "out_de.huffman"; //$NON-NLS-1$
						} else if (System.getProperties().get("osgi.nl").toString().compareToIgnoreCase("en") == 0) {
							outputFilename = outputPath + "out_en.huffman"; //$NON-NLS-1$
						} else {
							outputFilename = outputPath + "out.huffman"; //$NON-NLS-1$
						}
					} else {
						outputFilename = fileUncomp.getParent() + System.getProperty("file.separator") //$NON-NLS-1$
								+ fileUncomp.getName().substring(0, fileUncomp.getName().lastIndexOf(".txt")) //$NON-NLS-1$
								+ ".huffman"; //$NON-NLS-1$
					}
					fileComp = new File(outputFilename);
					try {
						if (!fileComp.exists()) {
							fileComp.createNewFile();
						}

						huffmanCode.compress(inputString);
						huffmanCodeBinary = huffmanCode.getHuffmanBinary();
						parseBinaryHuffman(huffmanCodeBinary);
						outputSize.setText(Integer.toString(huffmanCodeBinary.length));

						float compressedSize = huffmanCodeBinary.length;
						float rawSize = inputString.length();
						float compRate;

						compRate = (rawSize - compressedSize) / rawSize * 100;
						compressionRate.setText(String.format("%.2f%%", compRate));

					} catch (IOException ex) {
						LogUtil.logError(ex);
					} catch (InvalidCharacterException e2) {
						LogUtil.logError(e2);
						fileComp.delete();
						btnCompress.setEnabled(false);

						MessageDialog.openError(HuffmanCodingView.this.parent.getShell(), Messages.HuffmanCodingView_17,
								Messages.HuffmanCodingView_18);
						reset();

						return;
					}

					viewer.setInput(huffmanCode.getResultNodeList());
					LayoutAlgorithm layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
					viewer.setLayoutAlgorithm(layout, true);
					viewer.applyLayout();
					styledTextTree.setText(Messages.ZestLabelProvider_4);

					createCodeTableCompress(huffmanCode.getCodeTable());
					setCenterEnabled(true);

					btnCompress.setEnabled(false);
					btnOpenUncompFile.setEnabled(false);
					textInput.setEditable(false);
					hexTableHeader.setForeground(labelEnabled);

					isCompressed = true;

					String message = Messages.HuffmanCodingView_20;
					if (fileUncomp != null) {
						double compressRate = (1 - (double) fileComp.length() / (double) fileUncomp.length());
						String tmp = String.format("%2.2f", compressRate * 100); //$NON-NLS-1$

						message += "\n\n" + Messages.HuffmanCodingView_23 + df.format(fileUncomp.length()) + " " //$NON-NLS-1$ //$NON-NLS-2$
								+ Messages.HuffmanCodingView_24 + "\n" //$NON-NLS-1$
								+ Messages.HuffmanCodingView_25 + df.format(fileComp.length()) + " " //$NON-NLS-1$
								+ Messages.HuffmanCodingView_24;

						if (compressRate < 0) {
							String smallFile = Messages.HuffmanCodingView_27;
							message += smallFile;
						} else {
							message += "\n" + Messages.HuffmanCodingView_26 + tmp + " %"; //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
					MessageDialog.openInformation(HuffmanCodingView.this.parent.getShell(),
							Messages.HuffmanCodingView_19, message);

					btnApplyFile.setEnabled(true);

					compositeCT.layout();
				}
			}
		});
		btnCompress.setText(Messages.HuffmanCodingView_6);

		btnApplyFile = new Button(grpNextSteps, SWT.NONE);
		GridData gd_btnApplyFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		// gd_btnApplyFile.widthHint = 325;
		btnApplyFile.setLayoutData(gd_btnApplyFile);
		btnApplyFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnRadioUncompress.setSelection(true);
				btnRadioCompress.setSelection(false);

				File tmpFileComp = fileComp;

				grpCompress.dispose();
				HuffmanCodingView.this.grpNextSteps.dispose();

				HuffmanCodingView.this.grpNextSteps = createUncompressButtonGroup();
				grpCompress = createUncompressGroup();
				composite.layout();

				textFileCompName.setText(tmpFileComp.getAbsolutePath());
				textFileCompSize.setText(df.format(tmpFileComp.length()));

				fileComp = tmpFileComp;

				// btnOpenCompFile.setEnabled(false);
				btnUncompress.setEnabled(true);
			}
		});
		btnApplyFile.setEnabled(false);
		btnApplyFile.setText(Messages.HuffmanCodingView_btnPfadbernehmen_text);

		return grpNextSteps;
	}

	// TODO
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
				hexTableHeader.setForeground(labelEnabled);

				float compressedSize = huffmanCodeBinary.length;
				float rawSize = message.length();
				float compRate;

				compRate = (rawSize - compressedSize) / rawSize * 100;
				compressionRate.setText(String.format("%.2f%%", compRate));
				isUncompressed = true;

				testTree = null;
				testTable = null;
			}
		});
		btnUncompress.setEnabled(false);
		btnUncompress.setText(Messages.HuffmanCodingView_7);

		return grpNextSteps;
	}

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
						btnSaveResult.setEnabled(false);
						outputSize.setText("");
						compressionRate.setText("");
						btnOpenUncompFile.setEnabled(false);
					}
				}
			}
		});
		btnRadioExampleText.setSelection(true);
		btnRadioExampleText.setText("Just compressed"); // marker

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
		btnRadioContentFromFile.setText("Huffman-Kodierung aus Datei"); // marker

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
					// fileComp = new File(filePath);
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

		// TODO
		compHexTable = new Composite(compAllData, SWT.NONE);
		compHexTable.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 3, 1));
		compHexTable.setLayout(new GridLayout(1, true));

		hexTableHeader = new Label(compHexTable, SWT.NONE);
		hexTableHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		hexTableHeader.setText("Komprimierte Eingabe");

		hexTable = new Table(compHexTable, SWT.V_SCROLL | SWT.BORDER);
		GridData gdHexTable = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gdHexTable.widthHint = TABLE_WIDTH;
		hexTable.setLayoutData(gdHexTable);
		hexTable.setHeaderVisible(true);
		try {
			Font testFont = new Font(Display.getDefault(), "Courier", 10, SWT.NORMAL);
			hexTable.setFont(testFont);
		} catch (Exception e2) {
			System.err.println("Could not load font Courier");
			e2.printStackTrace(System.out);

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

		if (huffmanCodeBinary != null)
			parseBinaryHuffman(huffmanCodeBinary);

		grpCenterData = new Group(compAllData, SWT.NONE);
		grpCenterData.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		grpCenterData.setLayout(new GridLayout(4, true));

		inputSizeDescription = new Label(grpCenterData, SWT.NONE);
		inputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		inputSizeDescription.setText("Komprimierter Huffmanbaum in Bytes");

		inputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		inputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Label spacerLabel = new Label(grpCenterData, SWT.NONE);
		spacerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));

		outputSizeDescription = new Label(grpCenterData, SWT.NONE);
		outputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		outputSizeDescription.setText("Dekomprimierte Text in Bytes");

		outputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		outputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		compressionRateDescription = new Label(grpCenterData, SWT.NONE);
		compressionRateDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		compressionRateDescription.setText("Kompressionsrate");

		compressionRate = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		compressionRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		btnSaveResult = new Button(grpCenterData, SWT.PUSH);
		btnSaveResult.setText("Ergebnis speichern");
		btnSaveResult.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1));
		btnSaveResult.setEnabled(false);
		btnSaveResult.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// String outputFilename;

				FileDialog fd = new FileDialog(parent.getShell(), SWT.SAVE);
				fd.setText("Save");
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
						errorOnSave.setMessage("Could not save to the specified file to " + fileComp.getPath() + ".");
						errorOnSave.setText("Error while saving");
						errorOnSave.open();
						LogUtil.logError(ex);
					} catch (InvalidCharacterException e2) {
						LogUtil.logError(e2);
						fileComp.delete();
						btnCompress.setEnabled(false);
						MessageBox errorOnSave = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
						errorOnSave.setMessage("Could not save to the specified file to " + fileComp.getPath() + ".");
						errorOnSave.setText("Error while saving");
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
		inputHeader.setText("Dekomprimierte Ausgabe"); // marker

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
					btnApplyFile.setEnabled(false);
					textInput.setEditable(true);
					textFileUncompName.setText(""); //$NON-NLS-1$
					textFileUncompSize.setText(""); //$NON-NLS-1$
					fileUncomp = null;

					resetCodeTable();

					viewer.setInput(null);
					isCompressed = false;

					styledTextTree.setText(Messages.HuffmanCodingView_28);
					styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
					styledTextTree.setAlignment(SWT.LEFT);
					styledTextTree.setFont(FontService.getNormalFont());
					markedConnectionList = new ArrayList<GraphConnection>();
					layoutCounter = 1;
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
				btnApplyFile.setEnabled(false);
				textFileUncompName.setText(""); //$NON-NLS-1$
				textFileUncompSize.setText(""); //$NON-NLS-1$
				fileUncomp = null;
				btnOpenUncompFile.setEnabled(true);

				resetCodeTable();

				viewer.setInput(null);
				isCompressed = false;

				styledTextTree.setText(Messages.HuffmanCodingView_28);
				styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
				styledTextTree.setAlignment(SWT.LEFT);
				styledTextTree.setFont(FontService.getNormalFont());
				markedConnectionList = new ArrayList<GraphConnection>();
				layoutCounter = 1;
				codeTableControls.clear();

				// compInputText.setText(Messages.HuffmanCodingView_17);

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
					}
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

		// TODO
		// tabFolder.getTabList()[0].setFont(FontService.getLargeFont());

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
		// textInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));

		grpCenterData = new Group(grpCompress, SWT.NONE);
		grpCenterData.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		grpCenterData.setLayout(new GridLayout(4, true));

		inputSizeDescription = new Label(grpCenterData, SWT.NONE);
		inputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		inputSizeDescription.setText("Eingabegröße in Bytes");

		inputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		inputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		Label spacerLabel = new Label(grpCenterData, SWT.NONE);
		spacerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));

		outputSizeDescription = new Label(grpCenterData, SWT.NONE);
		outputSizeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		outputSizeDescription.setText("Ausgabegröße in Bytes");

		outputSize = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		outputSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		compressionRateDescription = new Label(grpCenterData, SWT.NONE);
		compressionRateDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		compressionRateDescription.setText("Kompressionsrate");

		compressionRate = new Text(grpCenterData, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		compressionRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		btnSaveResult = new Button(grpCenterData, SWT.PUSH);
		btnSaveResult.setText("Ergebnis speichern");
		btnSaveResult.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1));
		btnSaveResult.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// String outputFilename;

				FileDialog fd = new FileDialog(parent.getShell(), SWT.SAVE);
				fd.setText("Save");
				fd.setFilterPath(System.getProperty("user.home") + System.getProperty("file.separator"));
				String[] filterExt = { "*.huffman" };
				fd.setFilterExtensions(filterExt);
				String outputFilename = fd.open();

				if (outputFilename != null) {

					fileComp = new File(outputFilename);
					try {
						if (!fileComp.exists()) {
							fileComp.createNewFile();
							huffmanCode.writeHuffmanBinary(new FileOutputStream(fileComp));
						}

					} catch (IOException ex) {
						MessageBox errorOnSave = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
						errorOnSave.setMessage("Could not save to the specified file.");
						errorOnSave.setText("Error while saving");
						errorOnSave.open();
						LogUtil.logError(ex);
					} catch (InvalidCharacterException e2) {
						LogUtil.logError(e2);
						fileComp.delete();
						btnCompress.setEnabled(false);
						MessageBox errorOnSave = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
						errorOnSave.setMessage("Could not save to the specified file.");
						errorOnSave.setText("Error while saving");
						errorOnSave.open();

						// MessageDialog.openError(HuffmanCodingView.this.parent.getShell(),
						// Messages.HuffmanCodingView_17,
						// Messages.HuffmanCodingView_18);
						reset();
					}
				}
			}
		});

		compHexTable = new Composite(grpCompress, SWT.NONE);
		compHexTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		compHexTable.setLayout(new GridLayout(6, true));

		hexTableHeader = new Label(compHexTable, SWT.NONE);
		hexTableHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		hexTableHeader.setText("Komprimierte Ausgabe");

		labelDisabled = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		labelEnabled = hexTableHeader.getForeground();

		hexTableHeader.setForeground(labelDisabled);

		// TODO
		hexTable = new Table(compHexTable, SWT.V_SCROLL | SWT.BORDER);
		GridData gdHexTable = new GridData(SWT.LEFT, SWT.FILL, true, true, 6, 1);
		gdHexTable.widthHint = TABLE_WIDTH;
		hexTable.setLayoutData(gdHexTable);
		// hexTable.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true, 6, 1));
		hexTable.setHeaderVisible(true);
		try {
			Font testFont = new Font(Display.getDefault(), "Courier", 10, SWT.NORMAL);
			hexTable.setFont(testFont);
		} catch (Exception e2) {
			System.err.println("Could not load font Courier");
			e2.printStackTrace(System.out);

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

	/**
	 * Marks the whole branch begining from the leaf node
	 * 
	 * @param leaf
	 *            - the leaf node of the branch
	 */
	private void markBranch(GraphNode leaf) {
		ArrayList<GraphItem> items = new ArrayList<GraphItem>();

		GraphConnection connection = (GraphConnection) leaf.getTargetConnections().get(0);

		connection.setLineColor(viewer.getGraphControl().DARK_BLUE);
		connection.getSource().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);
		connection.getDestination().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);

		items.add(connection.getSource());
		items.add(connection.getDestination());

		markedConnectionList.add(connection);
		List<GraphConnection> l = connection.getSource().getTargetConnections();

		while (l.size() != 0) {
			connection = (GraphConnection) connection.getSource().getTargetConnections().get(0);
			connection.setLineColor(viewer.getGraphControl().DARK_BLUE);
			connection.getSource().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);
			connection.getDestination().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);

			items.add(connection.getSource());
			items.add(connection.getDestination());

			markedConnectionList.add(connection);

			l = connection.getSource().getTargetConnections();
		}

		viewer.getGraphControl().setSelection(items.toArray(new GraphItem[items.size()]));
	}

	/**
	 * Unmark a previous marked branch
	 * 
	 * @param markedConnectionList
	 *            - Contains marked elements
	 */
	private void unmarkBranch(List<GraphConnection> markedConnectionList) {
		for (GraphConnection connection : markedConnectionList) {
			connection.setLineColor(ColorConstants.lightGray);
			connection.getSource().setBackgroundColor(viewer.getGraphControl().LIGHT_BLUE);
			Node leaf = (Node) connection.getDestination().getData();
			if (leaf.isLeaf()) {
				connection.getDestination().setBackgroundColor(ColorConstants.lightGreen);
			} else {
				connection.getDestination().setBackgroundColor(viewer.getGraphControl().LIGHT_BLUE);
			}
		}
	}

	/**
	 * Initialize the zoom bar
	 */
	private void fillToolBar() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().removeAll();

		zoom = new MenuManager("Zoom"); //$NON-NLS-1$
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		zoom.add(toolbarZoomContributionViewItem);
		bars.getMenuManager().add(zoom);
	}

	@Override
	public void setFocus() {
		if (!textInput.isDisposed()) {
			textInput.setFocus();
		}
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

	/**
	 * Change the layout of the huffman tree
	 */
	public void setLayoutManager() {
		switch (layoutCounter) {
		case 1:
			viewer.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			viewer.applyLayout();

			layoutCounter++;
			break;
		case 2:
			viewer.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			viewer.applyLayout();

			layoutCounter++;
			break;
		case 3:
			viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			viewer.applyLayout();

			layoutCounter = 1;
			break;
		}
	}

	/**
	 * reset only the code table in the code table tab
	 */
	private void resetCodeTable() {
		Control[] c = compositeCT.getChildren();

		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof Table)
				c[i].dispose();
		}

		styledTextCodetable.setText(Messages.HuffmanCodingView_29);

		tblclmnCharacterMode = false;
		tblclmnProbabilityMode = false;
		tblclmnCodeMode = false;
		tblclmnCodeLengthMode = false;

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

		resetCodeTable();

		viewer.setInput(null);
		isCompressed = false;
		textInput.setFocus();

		styledTextTree.setText(Messages.HuffmanCodingView_28);
		styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
		styledTextTree.setAlignment(SWT.LEFT);
		styledTextTree.setFont(FontService.getNormalFont());

		markedConnectionList = new ArrayList<GraphConnection>();
		layoutCounter = 1;
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

	// TODO Auto-generated catch block
	private void parseBinaryHuffman(String path) {
		DataInputStream readFile = null;
		readBytes = new ArrayList<>();
		// String digits;
		try {
			readFile = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(path))));
			int lineOfBytes;
			while ((lineOfBytes = readFile.read()) != -1) {
				readBytes.add(lineOfBytes);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
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
		if (testTree != null) {
			testTree.highlightNode(nodeValue);
			tabFolder.setSelection(testTreeTab);
		}
	}

	public int getMode() {
		return mode;
	}

}
