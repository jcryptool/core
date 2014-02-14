package org.jcryptool.visual.huffmanCoding.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
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
import org.jcryptool.core.operations.OperationsPlugin;
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
	private Composite parent;
	private TabFolder tabFolder;
	private Text textInput;
	private Text textOutput;
	private Button btnUncompress;

	private Huffman huffmanCode;

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
	private ScrolledComposite scrolledCompositeCT;
	private Composite compositeCT;

	private int layoutCounter = 1;

	private boolean isCompressed = false;
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
//	private Group grpCompressButton;
	private Button btnCompress;
	private Button btnApplyFile;

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
		gd_styledTextDescription.heightHint = 110;
		styledTextDescription.setLayoutData(gd_styledTextDescription);

		grpSzenario = new Group(composite, SWT.NONE);
		GridData gd_grpSzenario = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpSzenario.widthHint = 160;
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

					styledTextTree.setText(""); //$NON-NLS-1$
					styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
					styledTextTree.setAlignment(SWT.LEFT);
					styledTextTree.setFont(FontService.getNormalFont());
					markedConnectionList = new ArrayList<GraphConnection>();
					layoutCounter = 1;
					codeTableControls.clear();
					
					loadExampleText();
				}

			}
		});
		GridData gd_btnRadioCompress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRadioCompress.widthHint = 110;
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

//					btnApplyFile.setEnabled(false);

					/*
					 * reset tabs
					 */
					resetCodeTable();

					viewer.setInput(null);
					isCompressed = false;

					styledTextTree.setText(""); //$NON-NLS-1$
					styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
					styledTextTree.setAlignment(SWT.LEFT);
					styledTextTree.setFont(FontService.getNormalFont());
					markedConnectionList = new ArrayList<GraphConnection>();
					layoutCounter = 1;
					codeTableControls.clear();
				}
			}
		});
		GridData gd_btnRadioUncompress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRadioUncompress.widthHint = 110;
		btnRadioUncompress.setLayoutData(gd_btnRadioUncompress);
		btnRadioUncompress.setText(Messages.HuffmanCodingView_7);
		
				

		/*
		 * create the compress / uncompress group
		 */

		grpNextSteps = createCompressButtonGroup();		
		grpCompress = createCompressGroup();
		
		
//		grpNextSteps = createUncompressButtonGroup();
//		grpCompress = createUncompressGroup();

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
		styledTextTree.setFont(FontService.getNormalFont());
		
//		styledTextTree.setForeground(new Color(null, new RGB(1, 70, 122)));
//		styledTextTree.setAlignment(SWT.CENTER);
		
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
						styledTextTree.setText(Messages.ZestLabelProvider_5 + " '" + n.getNameAsString() + "': " + n.getCode());

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
						styledTextTree.setText(Messages.ZestLabelProvider_4); //$NON-NLS-1$
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
		
        loadExampleText();
		
	}

	private void loadExampleText() {
		try {
        	URL url = HuffmanCodingPlugin.getDefault().getBundle().getEntry("/");
        	File template = new File(FileLocator.toFileURL(url).getFile() + "templates" + File.separatorChar + Messages.HuffmanCodingView_inputText);
			
			Scanner scanner = new Scanner(template);
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
		grpNextSteps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpNextSteps.setText(Messages.HuffmanCodingView_grpNextSteps_text);
		
		btnCompress = new Button(grpNextSteps, SWT.NONE);
		GridData gd_btnCompress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCompress.widthHint = 180;
		btnCompress.setLayoutData(gd_btnCompress);
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

					createCodeTable(huffmanCode.getCodeTable());

					btnCompress.setEnabled(false);
					btnOpenUncompFile.setEnabled(false);
					textInput.setEditable(false);

					isCompressed = true;

					String message = Messages.HuffmanCodingView_20;
					if (fileUncomp != null) {
						double compressRate = (1 - (double) fileComp.length() / (double) fileUncomp.length());
						String tmp = String.format("%2.2f", compressRate * 100); //$NON-NLS-1$
						
						
						message += "\n\n" + Messages.HuffmanCodingView_23 + df.format(fileUncomp.length()) + " " + Messages.HuffmanCodingView_24 + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								+ Messages.HuffmanCodingView_25 + df.format(fileComp.length()) + " " + Messages.HuffmanCodingView_24; //$NON-NLS-1$

						if (compressRate < 0) {
							String smallFile = Messages.HuffmanCodingView_27;
							message += smallFile;
						} else {
							message += "\n" + Messages.HuffmanCodingView_26 + tmp + " %"; //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
					MessageDialog.openInformation(HuffmanCodingView.this.parent.getShell(), Messages.HuffmanCodingView_19, message);

					btnApplyFile.setEnabled(true);
				}
			}
		});
		btnCompress.setText(Messages.HuffmanCodingView_6);
		btnCompress.setEnabled(false);
		
		btnApplyFile = new Button(grpNextSteps, SWT.NONE);
		GridData gd_btnApplyFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnApplyFile.widthHint = 180;
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

//				textFileCompName.setText(tmpFileComp.getAbsolutePath());
//				textFileCompSize.setText(String.valueOf(tmpFileComp.length()));
//
//				btnOpenCompFile.setEnabled(false);
//				btnUncompress.setEnabled(true);
//				btnApplyFile.setEnabled(false);

			}
		});
		btnApplyFile.setEnabled(false);
		btnApplyFile.setText(Messages.HuffmanCodingView_btnPfadbernehmen_text);
						
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
						styledTextTree.setText(Messages.ZestLabelProvider_4);
						resetCodeTable();
						createCodeTable(huffmanCode.getCodeTable());
					}

					btnUncompress.setEnabled(false);
					textOutput.setEditable(false);
					textOutput.setEnabled(true);

					btnOpenCompFile.setEnabled(false);

					MessageDialog.openInformation(HuffmanCodingView.this.parent.getShell(), Messages.HuffmanCodingView_21,
							Messages.HuffmanCodingView_22);
				} catch (IOException ex) {
					LogUtil.logError(ex);
				}
			}
		});
		btnUncompress.setEnabled(false);
		btnUncompress.setText(Messages.HuffmanCodingView_7);
		btnUncompress.setEnabled(false);
		
		return grpNextSteps;
	}
	
	private Group createUncompressGroup() {
		fileUncomp = null;

//		btnCompress.setEnabled(false);
//		btnUncompress.setEnabled(false);

		Group grpCompress = new Group(composite, SWT.NONE);
		grpCompress.setText(Messages.HuffmanCodingView_7);
		grpCompress.setLayout(new GridLayout(4, false));
		grpCompress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		btnOpenCompFile = new Button(grpCompress, SWT.NONE);
		GridData gd_btnOpenCompFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOpenCompFile.widthHint = 180;
		btnOpenCompFile.setLayoutData(gd_btnOpenCompFile);
		btnOpenCompFile.setSize(180, 25);
		btnOpenCompFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				fd.setFilterExtensions(new String[] { "*.huffman" }); //$NON-NLS-1$
				fd.setFilterNames(new String[] { Messages.HuffmanCodingView_8 });
				String filePath = fd.open();

				if (filePath != null) {
					fileComp = new File(filePath);
					textFileCompName.setText(filePath);
					textFileCompName.setSelection(filePath.length());
					int filesize = (int) fileComp.length();
					textFileCompSize.setText(df.format(filesize));

//					btnCompress.setEnabled(false);
					btnUncompress.setEnabled(true);
					btnUncompress.setFocus();
				}
			}
		});
		btnOpenCompFile.setText(Messages.HuffmanCodingView_9);

		textFileCompName = new Text(grpCompress, SWT.BORDER | SWT.READ_ONLY);
		textFileCompName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblFileCompSize = new Label(grpCompress, SWT.RIGHT);
		lblFileCompSize.setText(Messages.HuffmanCodingView_4);

		textFileCompSize = new Text(grpCompress, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		textFileCompSize.setEnabled(false);
		GridData gd_textFileCompSize = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textFileCompSize.widthHint = 140;
		textFileCompSize.setLayoutData(gd_textFileCompSize);

		Group grpOutputText = new Group(grpCompress, SWT.NONE);
		grpOutputText.setLayout(new GridLayout(1, false));
		GridData gd_grpOutputText = new GridData(SWT.FILL, SWT.FILL, false, true, 4, 1);
		gd_grpOutputText.heightHint = 120;
		grpOutputText.setLayoutData(gd_grpOutputText);
		grpOutputText.setText(Messages.HuffmanCodingView_11);

		textOutput = new Text(grpOutputText, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textOutput.setEnabled(false);
		textOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		if (fileComp != null) {
			textFileCompName.setText(fileComp.getAbsolutePath());
			textFileCompSize.setText(String.valueOf(fileComp.length()));

			btnOpenCompFile.setEnabled(false);
			btnUncompress.setEnabled(true);
		}

		return grpCompress;
	}

	private Group createCompressGroup() {
		fileComp = null;
		fileUncomp = null;

		Group grpCompress = new Group(composite, SWT.NONE);
		grpCompress.setText(Messages.HuffmanCodingView_6);
		grpCompress.setLayout(new GridLayout(4, false));
		grpCompress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		btnOpenUncompFile = new Button(grpCompress, SWT.NONE);
		GridData gd_btnOpenUncompFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOpenUncompFile.widthHint = 180;
		btnOpenUncompFile.setLayoutData(gd_btnOpenUncompFile);
		btnOpenUncompFile.setSize(180, 25);
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
						Scanner scanner = new Scanner(fileUncomp);
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
		textFileUncompName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblFileUncompSize = new Label(grpCompress, SWT.RIGHT);
		lblFileUncompSize.setSize(100, 15);
		lblFileUncompSize.setText(Messages.HuffmanCodingView_4);

		textFileUncompSize = new Text(grpCompress, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		GridData gd_textFileUncompSize = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textFileUncompSize.widthHint = 140;
		textFileUncompSize.setLayoutData(gd_textFileUncompSize);
		textFileUncompSize.setEnabled(false);

		Group grpInputText = new Group(grpCompress, SWT.NONE);
		grpInputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 4, 1));
		grpInputText.setLayout(new GridLayout(1, false));
		grpInputText.setText(Messages.HuffmanCodingView_5);

		textInput = new Text(grpInputText, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Text textField = null;

				if (e.getSource() instanceof Text) {
					textField = (Text) e.getSource();

					if (textField.getText().isEmpty()) {
						btnOpenUncompFile.setEnabled(true);
						btnCompress.setEnabled(false);
					} else {
						btnOpenUncompFile.setEnabled(false);
						btnCompress.setEnabled(true);
					}
				}
			}
		});
		textInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		return grpCompress;
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
					case 38:
						labelElement.setText("&&"); // Space //$NON-NLS-1$
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
										styledTextTree.setForeground(new Color(null, new RGB(1, 70, 122)));
										styledTextTree.setFont(FontService.getHugeFont());
										styledTextTree.setText(Messages.ZestLabelProvider_5 + " '" + n.getNameAsString() + "': " + n.getCode());
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
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().removeAll();

//		zoom = new MenuManager("Zoom"); //$NON-NLS-1$
//		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
//		zoom.add(toolbarZoomContributionViewItem);
//		bars.getMenuManager().add(zoom);

		/*
		 * adding popup menu
		 * 
		 * final ZoomContributionViewItem zoomContributionItem = new
		 * ZoomContributionViewItem(this);
		 * 
		 * MenuManager menuMgr = new MenuManager("#PopupMenu");
		 * menuMgr.setRemoveAllWhenShown(true); menuMgr.addMenuListener(new
		 * IMenuListener() { public void menuAboutToShow(IMenuManager manager) {
		 * fillContextMenu(manager); }
		 * 
		 * private void fillContextMenu(IMenuManager manager) { manager.add(new
		 * Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		 * manager.add(zoomContributionItem); } });
		 * 
		 * Menu menu = menuMgr.createContextMenu(viewer.getControl());
		 * viewer.getControl().setMenu(menu);
		 * getSite().registerContextMenu(menuMgr, viewer);
		 */
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
		grpCompress.dispose();
		grpNextSteps.dispose();
		
		grpNextSteps = createCompressButtonGroup();
		grpCompress = createCompressGroup();
		composite.layout();

		huffmanCode = null;
		btnCompress.setEnabled(false);
//		btnUncompress.setEnabled(false);
//		btnApplyFile.setEnabled(false);

		resetCodeTable();

		viewer.setInput(null);
		isCompressed = false;
		textInput.setFocus();

		styledTextTree.setText(""); //$NON-NLS-1$
		styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
		styledTextTree.setAlignment(SWT.LEFT);
		styledTextTree.setFont(FontService.getNormalFont());
				
		markedConnectionList = new ArrayList<GraphConnection>();
		layoutCounter = 1;
		codeTableControls.clear();
		
		loadExampleText();
	}
}
