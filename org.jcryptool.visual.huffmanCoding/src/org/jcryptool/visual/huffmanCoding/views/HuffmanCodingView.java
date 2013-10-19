package org.jcryptool.visual.huffmanCoding.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
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
	private Composite parent;
	private TabFolder tabFolder;
	private Text textInput;
	private Text textOutput;
	private Button btnCompress;
	private Button btnUncompress;

	private Huffman huffmanCode;
	private Text txtFileUncompName;
	private Text txtFileUncompSize;
	private Text txtFileCompName;
	private Text txtFileCompSize;
	private Button btnOpenUncompFile;
	private Button btnOpenCompFile;
	private Button btnReset;
	private File fileUncomp;
	private File fileComp;
	private Composite compositeTree;

	private GraphViewer viewer;
	private TabItem tbtmCodeTable;
	private ScrolledComposite scrolledCompositeCT;
	private Composite compositeCT;

	private int layoutCounter = 1;

	private boolean isCompressed = false;
	private StyledText styledTextTree;

	private ArrayList<GraphConnection> markedConnectionList;
	private ArrayList<Control> codeTableControls;
	private StyledText styledTextDescription;

	public HuffmanCodingView() {
		markedConnectionList = new ArrayList<GraphConnection>();
		codeTableControls = new ArrayList<Control>();
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(new GridLayout(1, false));

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		tabFolder = new TabFolder(scrolledComposite, SWT.NONE);

		TabItem tbtmParameter = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter.setText(Messages.HuffmanCodingView_0);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmParameter.setControl(composite);
		composite.setLayout(new GridLayout(4, false));

		styledTextDescription = new StyledText(composite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextDescription.setText(Messages.HuffmanCodingView_16 + "\n" + Messages.HuffmanCodingView_1); //$NON-NLS-1$
		StyleRange title = new StyleRange();
		title.start = 0;
		title.length = Messages.HuffmanCodingView_16.length();
		title.fontStyle = SWT.BOLD;
		styledTextDescription.setStyleRange(title);
		GridData gd_styledTextDescription = new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1);
		gd_styledTextDescription.widthHint = 960;
		gd_styledTextDescription.heightHint = 80;
		styledTextDescription.setLayoutData(gd_styledTextDescription);

		btnOpenUncompFile = new Button(composite, SWT.NONE);
		btnOpenUncompFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				fd.setFilterExtensions(new String[] { "*.txt" }); //$NON-NLS-1$
				fd.setFilterNames(new String[] { Messages.HuffmanCodingView_2 });
				String filePath = fd.open();

				if (filePath != null) {
					fileUncomp = new File(filePath);
					txtFileUncompName.setText(filePath);
					txtFileUncompName.setSelection(filePath.length());
					int filesize = (int) fileUncomp.length();
					txtFileUncompSize.setText(String.valueOf(filesize));

					try {
						Scanner scanner = new Scanner(fileUncomp);
						String fileString = scanner.useDelimiter("\\Z").next(); //$NON-NLS-1$
						scanner.close();

						textInput.setText(fileString);
						textInput.setEditable(false);

						btnOpenUncompFile.setEnabled(false);
						btnOpenCompFile.setEnabled(false);

						btnCompress.setFocus();
					} catch (FileNotFoundException ex) {
						LogUtil.logError(ex);
					}
				}
			}
		});
		GridData gd_btnOpenUncompFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOpenUncompFile.widthHint = 180;
		btnOpenUncompFile.setLayoutData(gd_btnOpenUncompFile);
		btnOpenUncompFile.setText(Messages.HuffmanCodingView_3);

		txtFileUncompName = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtFileUncompName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblFileUncompSize = new Label(composite, SWT.RIGHT);
		GridData gd_lblFileUncompSize = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblFileUncompSize.widthHint = 100;
		lblFileUncompSize.setLayoutData(gd_lblFileUncompSize);
		lblFileUncompSize.setText(Messages.HuffmanCodingView_4);

		txtFileUncompSize = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		txtFileUncompSize.setEnabled(false);
		txtFileUncompSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group grpText = new Group(composite, SWT.NONE);
		grpText.setLayout(new GridLayout(1, false));
		GridData gd_grpText = new GridData(SWT.FILL, SWT.FILL, false, true, 4, 1);
		gd_grpText.heightHint = 120;
		grpText.setLayoutData(gd_grpText);
		grpText.setText(Messages.HuffmanCodingView_5);

		textInput = new Text(grpText, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Text textField = null;

				if (e.getSource() instanceof Text) {
					textField = (Text) e.getSource();

					if (textField.getText().isEmpty()) {
						btnOpenUncompFile.setEnabled(true);
						btnOpenCompFile.setEnabled(true);
						btnCompress.setEnabled(false);
					} else {
						btnOpenUncompFile.setEnabled(false);
						btnOpenCompFile.setEnabled(false);
						btnCompress.setEnabled(true);
					}
				}
			}
		});
		textInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		btnCompress = new Button(composite, SWT.NONE);
		btnCompress.setEnabled(false);
		btnCompress.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!textInput.getText().isEmpty()) {

					String inputString = textInput.getText();

					huffmanCode = new Huffman();

					String outputFilename;
					if (fileUncomp == null) {
						String outputPath = System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								+ System.getProperty("file.separator"); //$NON-NLS-1$ 
						outputFilename = outputPath + "out.huffman"; //$NON-NLS-1$
					} else {
						outputFilename = fileUncomp.getParent() + System.getProperty("file.separator") //$NON-NLS-1$
								+ fileUncomp.getName().substring(0, fileUncomp.getName().lastIndexOf(".txt")) + ".huffman"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					fileComp = new File(outputFilename);
					try {
						if (!fileComp.exists()) {
							fileComp.createNewFile();
						}

						huffmanCode.compress(inputString, new FileOutputStream(fileComp));

					} catch (IOException ex) {
						LogUtil.logError(ex);
					} catch (InvalidCharacterException e2) {
						LogUtil.logError(e2);
						fileComp.delete();
						btnCompress.setEnabled(false);

						// TODO meldung "ungültige Datei" in statusleiste
						// ausgeben

						return;
					}
					txtFileCompName.setText(fileComp.getAbsolutePath());
					txtFileCompName.setSelection(fileComp.getAbsolutePath().length());
					int filesize = (int) fileComp.length();
					txtFileCompSize.setText(String.valueOf(filesize));

					viewer.setInput(huffmanCode.getResultNodeList());
					LayoutAlgorithm layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
					viewer.setLayoutAlgorithm(layout, true);
					viewer.applyLayout();

					createCodeTable(huffmanCode.getCodeTable());

					btnCompress.setEnabled(false);
					btnUncompress.setEnabled(true);
					btnOpenUncompFile.setEnabled(false);
					textInput.setEditable(false);

					isCompressed = true;
				}
			}
		});
		GridData gd_btnCompress = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
		gd_btnCompress.widthHint = 180;
		btnCompress.setLayoutData(gd_btnCompress);
		btnCompress.setText(Messages.HuffmanCodingView_6);

		btnUncompress = new Button(composite, SWT.NONE);
		btnUncompress.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {

					if (!isCompressed) {
						huffmanCode = new Huffman();
					}
					huffmanCode.uncompress(fileComp.getAbsolutePath());
					textOutput.setText(huffmanCode.getSb().toString());

					if (!isCompressed) {
						viewer.setInput(huffmanCode.getResultNodeList());
						LayoutAlgorithm layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
						viewer.setLayoutAlgorithm(layout, true);
						viewer.applyLayout();
						resetCodeTable();
						createCodeTable(huffmanCode.getCodeTable());
					}

					btnUncompress.setEnabled(false);
					textOutput.setEditable(false);
					textOutput.setEnabled(true);
				} catch (IOException ex) {
					LogUtil.logError(ex);
				}
			}
		});
		btnUncompress.setEnabled(false);
		GridData gd_btnUncompress = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
		gd_btnUncompress.widthHint = 180;
		btnUncompress.setLayoutData(gd_btnUncompress);
		btnUncompress.setText(Messages.HuffmanCodingView_7);

		btnOpenCompFile = new Button(composite, SWT.NONE);
		btnOpenCompFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				fd.setFilterExtensions(new String[] { "*.huffman" }); //$NON-NLS-1$
				fd.setFilterNames(new String[] { Messages.HuffmanCodingView_8 });
				String filePath = fd.open();

				if (filePath != null) {
					fileComp = new File(filePath);
					txtFileCompName.setText(filePath);
					txtFileCompName.setSelection(filePath.length());
					int filesize = (int) fileComp.length();
					txtFileCompSize.setText(String.valueOf(filesize));

					btnCompress.setEnabled(false);
					btnUncompress.setEnabled(true);

					btnOpenCompFile.setEnabled(false);
					btnOpenUncompFile.setEnabled(false);

					textInput.setEnabled(false);
					txtFileUncompName.setEnabled(false);
					btnUncompress.setFocus();
				}
			}
		});
		GridData gd_btnOpenCompFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOpenCompFile.widthHint = 180;
		btnOpenCompFile.setLayoutData(gd_btnOpenCompFile);
		btnOpenCompFile.setText(Messages.HuffmanCodingView_9);

		txtFileCompName = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtFileCompName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblFileCompSize = new Label(composite, SWT.RIGHT);
		lblFileCompSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblFileCompSize.setText(Messages.HuffmanCodingView_4);

		txtFileCompSize = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		txtFileCompSize.setEnabled(false);
		txtFileCompSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group grpBeispiel = new Group(composite, SWT.NONE);
		grpBeispiel.setLayout(new GridLayout(1, false));
		GridData gd_grpBeispiel = new GridData(SWT.FILL, SWT.FILL, false, true, 4, 1);
		gd_grpBeispiel.heightHint = 120;
		grpBeispiel.setLayoutData(gd_grpBeispiel);
		grpBeispiel.setText(Messages.HuffmanCodingView_11);

		textOutput = new Text(grpBeispiel, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textOutput.setEnabled(false);
		textOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		btnReset = new Button(composite, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reset();
			}
		});
		GridData gd_btnReset = new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1);
		gd_btnReset.widthHint = 180;
		btnReset.setLayoutData(gd_btnReset);
		btnReset.setText(Messages.HuffmanCodingView_12);

		TabItem tbtmHuffmanTree = new TabItem(tabFolder, SWT.NONE);
		tbtmHuffmanTree.setText(Messages.HuffmanCodingView_13);

		compositeTree = new Composite(tabFolder, SWT.NONE);
		compositeTree.setLayout(new GridLayout(1, false));

		styledTextTree = new StyledText(compositeTree, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		styledTextTree.setForeground(new Color(null, new RGB(1, 70, 122)));
		styledTextTree.setAlignment(SWT.CENTER);
		styledTextTree.setFont(FontService.getHugeFont());
		styledTextTree.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

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
						styledTextTree.setText(n.getCode());

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
						styledTextTree.setText(""); //$NON-NLS-1$
					}
				}
			}
		});

		tbtmHuffmanTree.setControl(compositeTree);

		tbtmCodeTable = new TabItem(tabFolder, SWT.NONE);
		tbtmCodeTable.setText(Messages.HuffmanCodingView_14);

		scrolledCompositeCT = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tbtmCodeTable.setControl(scrolledCompositeCT);
		scrolledCompositeCT.setExpandHorizontal(true);
		scrolledCompositeCT.setExpandVertical(true);

		compositeCT = new Composite(scrolledCompositeCT, SWT.NONE);
		compositeCT.setLayout(new GridLayout(4, false));

		/*
		 * BEGIN: for designing the code table tab
		 */
		// Label labelElement = new Label(compositeCT, SWT.BORDER);
		// labelElement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
		// false, 1, 1));
		// labelElement.setText("NUL");
		//
		// Label labelEqual = new Label(compositeCT, SWT.NONE);
		// labelEqual.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
		// false, 1, 1));
		// labelEqual.setText("=");
		//
		// Text textCode = new Text(compositeCT, SWT.BORDER | SWT.READ_ONLY);
		// textCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		// false, 1, 1));
		//
		// btnAnimate = new Button(compositeCT, SWT.NONE);
		// btnAnimate.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		//
		// }
		// });
		// GridData gd_btnAnimate = new GridData(SWT.LEFT, SWT.CENTER, false,
		// false, 1, 1);
		// gd_btnAnimate.widthHint = 120;
		// btnAnimate.setLayoutData(gd_btnAnimate);
		// btnAnimate.setText("Animate");
		/*
		 * END for designing the code table tab
		 */

		scrolledCompositeCT.setContent(compositeCT);
		scrolledCompositeCT.setMinSize(compositeCT.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		fillToolBar();
	}

	/**
	 * Creation of the code table tab
	 * 
	 * @param bitStrings
	 *            - the bitstring array which contains all elements of the
	 *            huffman tree
	 */
	private void createCodeTable(BitString[] bitStrings) {

		if (compositeCT.getChildren().length > 0) {
			for (int i = 0; i < compositeCT.getChildren().length; i++) {
				compositeCT.getChildren()[i].dispose();
			}
		}

		if (bitStrings != null) {
			for (int i = 0; i < bitStrings.length; i++) {
				if (bitStrings[i] != null) {
					Label labelElement = new Label(compositeCT, SWT.NONE);
					labelElement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
					switch (i) {
					case 0:
						labelElement.setText("NUL"); // Null //$NON-NLS-1$
						break;
					case 9:
						labelElement.setText("TAB"); // Tabulator //$NON-NLS-1$
						break;
					case 10:
						labelElement.setText("LF"); // Line Feed //$NON-NLS-1$
						break;
					case 13:
						labelElement.setText("CR"); // Carriage Return //$NON-NLS-1$
						break;
					case 32:
						labelElement.setText("\u2423"); // Space //$NON-NLS-1$
						break;
					default:
						labelElement.setText(String.valueOf((char) i));
					}

					Label labelEqual = new Label(compositeCT, SWT.NONE);
					labelEqual.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
					labelEqual.setText("="); //$NON-NLS-1$

					Text textCode = new Text(compositeCT, SWT.BORDER | SWT.READ_ONLY);
					textCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					textCode.setText(bitStrings[i].toString());

					Button btnShowBranch = new Button(compositeCT, SWT.NONE);
					btnShowBranch.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							if (e.getSource() instanceof Button) {
								Button b = (Button) e.getSource();

								int index = codeTableControls.indexOf(b);
								String code = ((Text) codeTableControls.get(index - 1)).getText();
								GraphNode graphNode = null;

								List<GraphNode> graphNodeList = viewer.getGraphControl().getNodes();
								for (GraphNode gn : graphNodeList) {
									Node n = (Node) gn.getData();
									if (n.isLeaf() && n.getCode().compareTo(code) == 0) {
										graphNode = gn;
										styledTextTree.setText(n.getCode());
										break;
									}
								}

								if (!markedConnectionList.isEmpty()) {
									unmarkBranch(markedConnectionList);
								}
								markBranch(graphNode);
								tabFolder.setSelection(1);
							}
						}
					});
					GridData gd_btnAnimate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
					gd_btnAnimate.widthHint = 120;
					btnShowBranch.setLayoutData(gd_btnAnimate);
					btnShowBranch.setText(Messages.HuffmanCodingView_15);

					codeTableControls.add(textCode);
					codeTableControls.add(btnShowBranch);
				}
			}
		}
		scrolledCompositeCT.setContent(compositeCT);
		scrolledCompositeCT.setMinSize(compositeCT.computeSize(SWT.DEFAULT, SWT.DEFAULT));

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
		 ZoomContributionViewItem toolbarZoomContributionViewItem = new
		 ZoomContributionViewItem(this);
		 IActionBars bars = getViewSite().getActionBars();
		 bars.getMenuManager().add(toolbarZoomContributionViewItem);
	}

	@Override
	public void setFocus() {
		textInput.setFocus();
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
			c[i].dispose();
		}

		scrolledCompositeCT.setMinSize(0, 0);
		scrolledCompositeCT.layout();
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
		textInput.setText(""); //$NON-NLS-1$
		textOutput.setText(""); //$NON-NLS-1$
		txtFileUncompName.setText(""); //$NON-NLS-1$
		txtFileUncompSize.setText(""); //$NON-NLS-1$
		txtFileCompName.setText(""); //$NON-NLS-1$
		txtFileCompSize.setText(""); //$NON-NLS-1$

		textInput.setEditable(true);
		textInput.setEnabled(true);

		textOutput.setEditable(false);
		textOutput.setEnabled(false);

		fileUncomp = null;
		fileComp = null;

		huffmanCode = null;

		btnOpenCompFile.setEnabled(true);
		btnOpenUncompFile.setEnabled(true);

		btnCompress.setEnabled(false);
		btnUncompress.setEnabled(false);
		txtFileUncompName.setEnabled(true);

		resetCodeTable();

		viewer.setInput(null);
		isCompressed = false;
		textInput.setFocus();

		styledTextTree.setText(""); //$NON-NLS-1$
		markedConnectionList = new ArrayList<GraphConnection>();
		layoutCounter = 1;
		codeTableControls.clear();
	}
}
