package org.jcryptool.visual.merkletree.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.MultiTree;
import org.jcryptool.visual.merkletree.algorithm.Node;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Class for the Composite of Tabpage "MerkleTree"
 * 
 * @author Kevin Muehlboeck
 *
 */
public class MerkleTreeVerifikationComposite extends Composite {

	private ISimpleMerkle merkle;
	private GraphViewer viewer;
	private Graph graph;
	private int graphOffset;
	private StyledText binaryValue;
	private Label verificationFeedbackLabel;
	private int currentIndex;
	private int latestIndex;
	private ArrayList<GraphConnection> markedConnectionList;
	private ArrayList<GraphNode> markedAuthpathList;
	private String messages[];
	private String signatures[];
	private Color distinguishableColors[];
	private Label descLabel;
	private StyledText descText;

	private Composite zestComposite;

	private Composite topBar;
	private Composite stackComposite;
	private Composite descriptionComposite;
	private Composite signatureSelectionComposite;
	private StackLayout stackLayout;
	private Button descriptionButton;
	private Button signatureSelectionButton;
	private GridData leftTextLayout;
	private GridData rightTextLayout;
	private StyledText leftText;
	private StyledText rightText;
	private Group leftGroup;
	private Group rightGroup;
	private Combo selectionCombo;

	private SUIT mode;

	private List<?> graphNodeRetriever;
	private GraphNode leaves[];
	private GraphNode[] nodes;
	protected boolean mouseDragging;
	protected boolean distinctListener;
	protected Point oldMouse;
	protected org.eclipse.draw2d.geometry.Point viewLocation;
	private Display curDisplay;
	private ZoomManager zoomManager;

	private Runnable currentlyHighlighted;
	private Runnable highlightedAuthpath;

	private Color[] greySteps;
	private Color[] redSteps;

	/**
	 * Create the composite. Including Description, GraphItem, GraphView,
	 * Description for GraphView
	 * 
	 * @param parent
	 * @param style
	 */
	public MerkleTreeVerifikationComposite(Composite parent, int style, ISimpleMerkle merkle, String[] signatures, String[] messages, SUIT mode) {
		super(parent, style);

		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN + 5, MerkleConst.DESC_HEIGHT + 1));

		this.signatures = signatures;
		this.messages = messages;
		this.merkle = merkle;
		curDisplay = getDisplay();
		this.mode = mode;

		// finds the latest signature and its index
		for (int i = this.signatures.length - 1; i >= 0; --i) {
			if (this.signatures[i] != null) {
				currentIndex = i;
				latestIndex = i;
				i = -1;
			}
		}

		// ***********************************
		// Beginning of GUI elements
		// ***********************************

		topBar = new Composite(this, SWT.NONE);
		topBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 8, 1));
		topBar.setLayout(new GridLayout(8, true));

		Label spacerHeader = new Label(topBar, SWT.NONE);
		spacerHeader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		descriptionButton = new Button(topBar, SWT.PUSH);
		descriptionButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		descriptionButton.setText(Descriptions.MerkleTreeVerify_4);
		descriptionButton.setEnabled(false);

		signatureSelectionButton = new Button(topBar, SWT.PUSH);
		signatureSelectionButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		signatureSelectionButton.setText(Descriptions.MerkleTreeVerify_5);

		descLabel = new Label(topBar, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 2, 1));

		// A stack composite for the Description/Signature Selection window
		stackComposite = new Composite(this, SWT.NONE);
		stackComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		stackLayout = new StackLayout();
		stackComposite.setLayout(stackLayout);

		descriptionComposite = new Composite(stackComposite, SWT.NONE);
		descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		descriptionComposite.setLayout(new GridLayout(1, true));

		descText = new StyledText(descriptionComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Set Strings according to mode
		switch (mode) {
		case XMSS:
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
			descText.setText(Descriptions.XMSS.MerkleTreeVerify_0);
			break;
		case XMSS_MT:
			descLabel.setText(Descriptions.XMSS_MT.Tab1_Head0);
			descText.setText(Descriptions.XMSS_MT.MerkleTreeVerify_0);
			break;
		case MSS:
			descLabel.setText(Descriptions.MSS.Tab1_Head0);
			descText.setText(Descriptions.MSS.MerkleTreeVerify_0);
		default:
			break;
		}
		// Sets the initial top of StackLayout
		stackLayout.topControl = descriptionComposite;

		signatureSelectionComposite = new Composite(stackComposite, SWT.NONE);
		signatureSelectionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		signatureSelectionComposite.setLayout(new GridLayout(5, true));

		leftGroup = new Group(signatureSelectionComposite, SWT.NONE);
		leftGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		leftGroup.setLayout(new GridLayout(1, true));
		leftGroup.setText(Descriptions.MerkleTreeVerify_1);
		leftGroup.setFont(FontService.getNormalBoldFont());

		leftTextLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		leftTextLayout.heightHint = stackComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		leftText = new StyledText(leftGroup, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		leftText.setLayoutData(leftTextLayout);
		leftText.setCaret(null);
		leftText.setEditable(false);

		rightGroup = new Group(signatureSelectionComposite, SWT.NONE);
		rightGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		rightGroup.setLayout(new GridLayout(1, true));
		rightGroup.setText(Descriptions.MerkleTreeVerify_2);
		rightGroup.setFont(FontService.getNormalBoldFont());

		rightTextLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		rightTextLayout.heightHint = stackComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		rightText = new StyledText(rightGroup, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		rightText.setLayoutData(rightTextLayout);
		rightText.setCaret(null);
		rightText.setEditable(false);

		leftText.setText(messages[currentIndex]);
		rightText.setText(signatures[currentIndex]);

		selectionCombo = new Combo(signatureSelectionComposite, SWT.READ_ONLY);
		selectionCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		descriptionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				stackLayout.topControl = descriptionComposite;
				signatureSelectionButton.setEnabled(true);
				descriptionButton.setEnabled(false);
				stackComposite.layout();
			}
		});

		signatureSelectionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				stackLayout.topControl = signatureSelectionComposite;
				signatureSelectionButton.setEnabled(false);
				descriptionButton.setEnabled(true);
				stackComposite.layout();
			}
		});

		selectionCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				currentIndex = selectionCombo.getSelectionIndex();
				leftText.setText(messages[currentIndex]);
				rightText.setText(signatures[currentIndex]);
				verificationFeedbackLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				verificationFeedbackLabel.setText("");

				unmarkBranch();
				markedConnectionList.clear();
				markBranch(leaves[currentIndex]);
				markAuthPath(markedConnectionList);
			}
		});

		this.setLayout(new GridLayout(1, true));

		verificationFeedbackLabel = new Label(this, SWT.WRAP | SWT.CENTER);
		verificationFeedbackLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		verificationFeedbackLabel.setText(Descriptions.MerkleTreeVerify_9);

		zestComposite = new Composite(this, SWT.DOUBLE_BUFFERED | SWT.BORDER);
		zestComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		zestComposite.setLayout(new GridLayout());
		zestComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		zestComposite.setBackgroundMode(SWT.INHERIT_FORCE);
		zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));

		viewer = new GraphViewer(zestComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.getControl().forceFocus();
		viewer.setLabelProvider(new ZestLabelProvider(ColorConstants.white));
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
		viewer.setInput(merkle.getTree());
		viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		viewer.applyLayout();
		GridDataFactory.fillDefaults().grab(true, true).applyTo(viewer.getControl());

		graph = viewer.getGraphControl();
		graph.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
		graph.setScrollBarVisibility(FigureCanvas.NEVER);
		graphNodeRetriever = graph.getNodes();
		markedConnectionList = new ArrayList<GraphConnection>();
		markedAuthpathList = new ArrayList<GraphNode>();
		nodes = new GraphNode[graphNodeRetriever.size()];
		leaves = new GraphNode[graphNodeRetriever.size() / 2 + 1];

		graphOffset = 0;

		// Sets the size of the graph
		viewer.getControl().addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				Point currentShellSize;
				currentShellSize = parent.getSize();
				double x, y;

				switch (merkle.getLeafCounter()) {

				case 2:
					x = currentShellSize.x;
					y = currentShellSize.y / 2;
					break;
				case 4:
					x = currentShellSize.x;
					y = currentShellSize.y / 1.7;
					break;
				case 8:
					x = currentShellSize.x;
					y = currentShellSize.y;
					break;
				case 16:
					x = currentShellSize.x * 1.2;
					y = currentShellSize.y;
					graphOffset = 150;
					break;
				case 32:
					x = currentShellSize.x * 1.5;
					y = currentShellSize.y * 1.2;
					graphOffset = 450;
					break;
				case 64:
					x = currentShellSize.x * 2;
					y = currentShellSize.y * 1.5;
					graphOffset = 925;
					break;
				default:
					x = currentShellSize.x;
					y = currentShellSize.y;
					break;
				}
				graph.getViewport().setSize((int) x, (int) y);
			}
		});
		graph.getViewport().setViewLocation(graphOffset, 0);

		/*
		 * Text field for the binary representation of the node this The textbox
		 * is filled in the method markAuthPath()
		 */
		binaryValue = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		binaryValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		// ***********************************
		// End of GUI elements
		// Beginning of Graph / Listeners
		// ***********************************

		if (mode == SUIT.XMSS_MT) {
			colorizeMultitrees();
		}

		graph.addSelectionListener(new SelectionAdapter() {
			// if a node is selected, mark the path, set the path in StyledText binaryValue, and set it for verification process
			@Override
			public void widgetSelected(SelectionEvent e) {
				distinctListener = true;
				if (e.item instanceof GraphNode) {
					GraphNode node = (GraphNode) e.item;
					Node n = (Node) node.getData();

					verificationFeedbackLabel.setForeground(new Color(null, new RGB(0, 0, 0)));
					verificationFeedbackLabel.setBackground(ColorConstants.white);
					verificationFeedbackLabel.setText("");
					if (n.isLeaf()) {
						verificationFeedbackLabel.setForeground(new Color(null, new RGB(1, 70, 122)));
						if (markedConnectionList.size() == 0) {
							markBranch(node);
							markAuthPath(markedConnectionList);
						} else {
							unmarkBranch();
							markedConnectionList.clear();
							markBranch(node);
							markAuthPath(markedConnectionList);
						}
					} else {
						if (markedConnectionList.size() == 0) {
							markBranch(node);
							markAuthPath(markedConnectionList);
						} else {
							unmarkBranch();
							markBranch(node);
							markAuthPath(markedConnectionList);
						}
					}
					/* Deselects immediately to allow dragging */
					viewer.setSelection(new ISelection() {

						@Override
						public boolean isEmpty() {
							return false;
						}
					});
				}
			}
		});

		// The mouselistener for dragging the graph
		MouseListener dragQueen = new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				distinctListener = false;
				mouseDragging = false;
				zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_CROSS));

			}

			@Override
			public void mouseDown(MouseEvent e) {
				mouseDragging = true;
				oldMouse = Display.getCurrent().getCursorLocation();
				viewLocation = graph.getViewport().getViewLocation();

				if (distinctListener == false)
					zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						while (mouseDragging) {
							if (distinctListener == false)
								updateViewLocation();
							try {
								Thread.sleep(2);

							} catch (InterruptedException e) {
							}
						}

					}
				};
				new Thread(runnable).start();

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// do nothing
			}
		};

		viewer.getGraphControl().addMouseListener(dragQueen);
		zestComposite.addMouseListener(dragQueen);

		Button verifyButton = new Button(this, SWT.WRAP);
		verifyButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		verifyButton.setText(Descriptions.MerkleTreeVerify_6);
		verifyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int currentLeaf = -1;

				for (GraphConnection con : markedConnectionList) {
					if (((Node) con.getDestination().getData()).isLeaf()) {
						currentLeaf = ((Node) con.getDestination().getData()).getLeafNumber();
					}
				}
				if (currentLeaf >= 0) {
					if (merkle.verify(messages[currentIndex], signatures[currentIndex], currentLeaf)) {
						/*
						 * set the Screen color based on the result green if
						 * verification success red if verification fails
						 */
						verificationFeedbackLabel.setBackground(ColorConstants.green);
						verificationFeedbackLabel.setText(Descriptions.MerkleTreeVerify_7);
					} else {
						verificationFeedbackLabel.setBackground(ColorConstants.red);
						verificationFeedbackLabel.setText(Descriptions.MerkleTreeVerify_8);
					}
				} else {
					/*
					 * if selected item is a node then show message that nodes
					 * cant be used to verify signature
					 */
					verificationFeedbackLabel.setBackground(ColorConstants.red);
					verificationFeedbackLabel.setText(Descriptions.MerkleTreeVerify_13);
				}
			}
		});
		this.pack();

		graphNodeRetriever = graph.getNodes();
		leaves = new GraphNode[graphNodeRetriever.size() / 2 + 1];

		for (int i = 0, j = 0; i < graphNodeRetriever.size(); ++i) {
			if (((GraphNode) graphNodeRetriever.get(i)).getSourceConnections().isEmpty()) {
				leaves[j] = (GraphNode) graphNodeRetriever.get(i);
				++j;
			}
		}
		// This performs MouseWheel zooming
		zoomManager = new ZoomManager(graph.getRootLayer(), graph.getViewport());
		graph.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseScrolled(MouseEvent e) {
				if (e.count < 0) {
					zoomManager.zoomOut();
				} else {
					zoomManager.zoomIn();
				}
			}
		});

		// Makes the nodes fixed (they cannot be dragged around with the mouse
		// by overriding the mouseMovedListener with empty event
		graph.getLightweightSystem().setEventDispatcher(new SWTEventDispatcher() {
			public void dispatchMouseMoved(MouseEvent e) {
			}

		});

		// Initializes the color arrays for MultiTree animation
		String os;
		try {
			os = System.getProperty("os.name");
		} catch (Exception e) {
			os = "";
		}
		os = os.toLowerCase();
		int osGrey;
		if (os.indexOf("win") >= 0) {
			osGrey = 255;
		} else if (os.indexOf("mac") >= 0) {
			osGrey = 232;
		} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
			osGrey = 237;
		} else {
			osGrey = 240;
		}

		int arrayLength = (int) Math.ceil((osGrey - 50f) / 10);
		int startingColor = 50;
		for (; ((arrayLength * 10) + startingColor) % osGrey != 0; startingColor--) {
		}

		greySteps = new Color[arrayLength];
		redSteps = new Color[arrayLength];
		Display currentDisplay = getDisplay();
		for (int i = 0, colorValues = startingColor; i < greySteps.length; colorValues += 10, ++i) {
			greySteps[i] = new Color(currentDisplay, new RGB(colorValues, colorValues, colorValues));
		}
		for (int i = 0, colorValues = startingColor; i < redSteps.length; colorValues += 10, ++i) {
			redSteps[i] = new Color(currentDisplay, new RGB(osGrey, colorValues, colorValues));
		}

		@SuppressWarnings("unchecked")
		List<GraphNode> gNodes = graph.getNodes();
		for (GraphNode gnode : gNodes) {
			if (((Node) gnode.getData()).getLeafNumber() == currentIndex) {
				if (markedConnectionList.size() == 0) {
					markBranch(gnode);
					markAuthPath(markedConnectionList);
				} else {
					unmarkBranch();
					markedConnectionList.clear();
					markBranch(gnode);
					markAuthPath(markedConnectionList);
				}
			}
		}

	}

	protected Point newMouse;
	protected int differenceMouseX;
	protected int differenceMouseY;

	/**
	 * Marks the whole branch beginning from the leaf node
	 * 
	 * @param leaf
	 *        - the leaf node of the branch
	 */
	@SuppressWarnings("unchecked")
	private void markBranch(GraphNode leaf) {
		ArrayList<GraphItem> items = new ArrayList<GraphItem>();

		try {
			GraphConnection connection = (GraphConnection) leaf.getTargetConnections().get(0);

			connection.setLineColor(viewer.getGraphControl().DARK_BLUE);
			if (mode != SUIT.XMSS_MT) {
				connection.getSource().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);
				connection.getDestination().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);
			}

			items.add(connection.getSource());
			items.add(connection.getDestination());

			markedConnectionList.add(connection);
			List<GraphConnection> l = connection.getSource().getTargetConnections();

			while (l.size() != 0) {
				connection = (GraphConnection) connection.getSource().getTargetConnections().get(0);
				if (mode != SUIT.XMSS_MT) {
					connection.getSource().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);
					connection.getDestination().setBackgroundColor(viewer.getGraphControl().HIGHLIGHT_COLOR);
				}
				connection.setLineColor(viewer.getGraphControl().DARK_BLUE);

				items.add(connection.getSource());
				items.add(connection.getDestination());

				markedConnectionList.add(connection);

				l = connection.getSource().getTargetConnections();
			}
		} catch (IndexOutOfBoundsException ex) {
			items.add(((GraphConnection) (leaf.getSourceConnections().get(0))).getSource());
		}

		if (mode == SUIT.XMSS_MT) {
			currentlyHighlighted = animate(items.toArray(new GraphNode[items.size()]), greySteps);
		}
		Node selectedNode = null;
		if (markedConnectionList.size() != 0)
			selectedNode = (Node) markedConnectionList.get(0).getDestination().getData();
		if (selectedNode != null && selectedNode.isLeaf()) {
			binaryValue.setText(Descriptions.MerkleTreeVerify_10 + " " + selectedNode.getAuthPath());
			if (selectedNode.getLeafNumber() == currentIndex) {
				binaryValue.append(". " + Descriptions.MerkleTreeVerify_11);
			} else {
				binaryValue.append(". " + Descriptions.MerkleTreeVerify_12);
			}

		} else {
			binaryValue.setText(Descriptions.MerkleTreeVerify_13);
		}
	}

	/**
	 * Unmark a previous marked branch
	 * 
	 * @param markedConnectionList
	 * 
	 */
	private void unmarkBranch() {
		GraphConnection authPath;
		if (mode == SUIT.XMSS_MT) {
			markedConnectionList.get(0).getDestination().setBorderWidth(0);
		}
		for (GraphConnection connection : markedConnectionList) {
			connection.setLineColor(ColorConstants.lightGray);
			if (mode == SUIT.XMSS_MT) {
				connection.getSource().setBorderWidth(0);
			} else {

				if (((GraphNode) connection.getSource()).getTargetConnections().isEmpty())
					connection.getSource().setBackgroundColor(ColorConstants.white);

				authPath = (GraphConnection) connection.getSource().getSourceConnections().get(0);
				authPath.getDestination().setBackgroundColor(ColorConstants.white);
				authPath = (GraphConnection) connection.getSource().getSourceConnections().get(1);
				authPath.getDestination().setBackgroundColor(ColorConstants.white);
			}
			// color the nodes back to light green
			Node leaf = (Node) connection.getDestination().getData();
			if (leaf.isLeaf()) {
				if (mode == SUIT.XMSS_MT) {
					connection.getDestination().setBorderWidth(0);
				} else {
					connection.getDestination().setBackgroundColor(ColorConstants.white);
				}
			}

		}
		for (GraphNode authNode : markedAuthpathList) {
			((GraphConnection) authNode.getTargetConnections().get(0)).setLineColor(ColorConstants.lightGray);
			authNode.setBorderWidth(0);
		}
		if (mode == SUIT.XMSS_MT) {
			if (currentlyHighlighted != null)
				getDisplay().timerExec(-1, currentlyHighlighted);
			if (highlightedAuthpath != null)
				getDisplay().timerExec(-1, highlightedAuthpath);
		}

		markedConnectionList.clear();
		markedAuthpathList.clear();
	}

	/**
	 * Marks the authentification path of the leaf
	 * 
	 * @param markedConnectionList
	 *        - Contains marked elements of the Changing Path
	 */
	private void markAuthPath(List<GraphConnection> markedConnectionList) {
		// ArrayList<GraphNode> items = new ArrayList<GraphNode>();
		GraphConnection authPath;
		// List<GraphConnection> connections = leaf.getTargetConnections();
		for (GraphConnection connect : markedConnectionList) {
			Node myNode = (Node) connect.getDestination().getData();
			Node parentNode = (Node) connect.getSource().getData();

			if (myNode.equals(parentNode.getLeft())) {
				authPath = (GraphConnection) connect.getSource().getSourceConnections().get(1);
				// ((GraphConnection)
				// connect.getSource()).getSourceConnections().get(1);
				((GraphConnection) connect.getSource().getSourceConnections().get(1)).setLineColor(getDisplay().getSystemColor(SWT.COLOR_RED));
				markedAuthpathList.add(authPath.getDestination());
			} else {
				authPath = (GraphConnection) connect.getSource().getSourceConnections().get(0);
				((GraphConnection) connect.getSource().getSourceConnections().get(0)).setLineColor(getDisplay().getSystemColor(SWT.COLOR_RED));
				// connect.setLineColor(getDisplay().getSystemColor(SWT.COLOR_RED));
				markedAuthpathList.add(authPath.getDestination());
			}
		}

		if (mode != SUIT.XMSS_MT) {
			for (int i = 0; i < markedAuthpathList.size(); ++i) {
				markedAuthpathList.get(i).setBackgroundColor(ColorConstants.red);
			}
		} else {
			highlightedAuthpath = animate(markedAuthpathList.toArray(new GraphNode[markedAuthpathList.size()]), redSteps);
		}
	}

	/**
	 * adds a signature/message pair
	 */
	public void setSignatureMessagePair(String signatures[], String[] messages) {
		this.signatures = signatures;
		this.messages = messages;
		refreshCombo();
	}

	/**
	 * takes all signature/message pairs and puts them into the selection combobox
	 */
	private void refreshCombo() {
		if (selectionCombo.getItemCount() > 0)
			selectionCombo.removeAll();

		verificationFeedbackLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		verificationFeedbackLabel.setText("");

		for (int i = 0; i < signatures.length; ++i) {
			if (messages[i] != null) {
				if (messages[i].length() > 80) {
					selectionCombo.add(Descriptions.MerkleTreeVerify_1 + " " + i + ": " + messages[i].substring(0, 80).replaceAll("\n", " ") + "...");
				} else {
					selectionCombo.add(Descriptions.MerkleTreeVerify_1 + " " + i + ": " + messages[i]);
				}

			}
		}
		if (selectionCombo.getSelectionIndex() == -1)
			selectionCombo.select(currentIndex);

		for (int i = signatures.length - 1; i >= 0; --i) {
			if (signatures[i] != null) {
				if (i != latestIndex) {
					currentIndex = i;
					latestIndex = i;

					selectionCombo.select(currentIndex);
					leftText.setText(messages[currentIndex]);
					rightText.setText(signatures[currentIndex]);

					unmarkBranch();
					markedConnectionList.clear();
					markBranch(leaves[currentIndex]);
					markAuthPath(markedConnectionList);
				}
				i = -1;
			}
		}
	}

	/**
	 * Sets the current view location based on mouse movement
	 */
	private void updateViewLocation() {
		curDisplay.asyncExec(new Runnable() {
			@Override
			public void run() {
				newMouse = getDisplay().getCursorLocation();
				differenceMouseX = newMouse.x - oldMouse.x;
				differenceMouseY = newMouse.y - oldMouse.y;

				if (differenceMouseX != 0 || differenceMouseY != 0) {
					if (mouseDragging) {
						graph.getViewport().setViewLocation(viewLocation.x -= differenceMouseX, viewLocation.y -= differenceMouseY);
						oldMouse = newMouse;
					}
				}

			}
		});
	}

	/**
	 * If the suite is MultiTree, this method can be called to highlight the
	 * single trees
	 */
	private void colorizeMultitrees() {
		int singleTreeHeight = ((MultiTree) merkle).getSingleTreeHeight();
		int singleTreeLeaves = (int) Math.pow(2, singleTreeHeight - 1);
		int treeCount = 0;
		int leafCounter = merkle.getLeafCounter();

		for (int i = leafCounter; i >= 1;) {
			i /= singleTreeLeaves;
			treeCount += i;
		}

		GraphNode[] rootNodes = new GraphNode[treeCount];
		GraphNode helper;

		for (int i = 0, j = 0; i < graphNodeRetriever.size(); ++i) {
			if (((GraphNode) graphNodeRetriever.get(i)).getSourceConnections().isEmpty()) {
				leaves[j] = (GraphNode) graphNodeRetriever.get(i);
				++j;
			}
			nodes[i] = (GraphNode) graphNodeRetriever.get(i);
		}
		leafCounter = merkle.getLeafCounter();

		for (int i = 0, p = 0; i < rootNodes.length;) {

			for (int k = 0; k < leafCounter; k += singleTreeLeaves, ++i) {
				rootNodes[i] = leaves[k];
				for (int j = 1; j < singleTreeHeight; ++j) {
					helper = ((GraphConnection) rootNodes[i].getTargetConnections().get(0)).getSource();
					rootNodes[i] = helper;

				}
			}
			for (int q = 0; q < leafCounter / singleTreeLeaves; ++p, ++q) {
				leaves[q] = rootNodes[p];
			}
			leafCounter /= singleTreeLeaves;
			// rootNodes[i].highlight();
		}

		distinguishableColors = new Color[7];
		distinguishableColors[0] = new Color(getDisplay(), 186, 186, 0);
		distinguishableColors[1] = new Color(getDisplay(), 186, 0, 186);
		distinguishableColors[2] = new Color(getDisplay(), 205, 183, 158);
		distinguishableColors[3] = new Color(getDisplay(), 0, 186, 186);
		distinguishableColors[4] = new Color(getDisplay(), 0, 186, 0);
		distinguishableColors[5] = new Color(getDisplay(), 176, 0, 0);
		distinguishableColors[6] = new Color(getDisplay(), 210, 105, 30);

		for (int i = rootNodes.length - 1, j = 0; i >= 0; --i, ++j) {
			if (j >= distinguishableColors.length)
				j = 0;
			recursive(rootNodes[i], distinguishableColors[j]);
			if (rootNodes[i].getTargetConnections().size() != 0) {
				rootNodes[i].setText(Descriptions.XMSS_MT.Tab1_Node);
			}

		}

	}

	/**
	 * Recursively colors the nodes
	 * 
	 * @param node
	 *        a root node of a single tree
	 * @param color
	 *        a color which should be used
	 */
	@SuppressWarnings("unchecked")
	private void recursive(GraphNode node, Color color) {
		if (node.getSourceConnections() == null) {
			node.setBackgroundColor(color);

			return;
		}
		List<GraphConnection> connection = node.getSourceConnections();
		for (int i = 0; i < connection.size(); ++i) {
			recursive(connection.get(i).getDestination(), color);
		}
		node.setBackgroundColor(color);
		if (color == distinguishableColors[5] || color == distinguishableColors[1]) {
			node.setForegroundColor(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		} else {
			node.setForegroundColor(new Color(null, new RGB(1, 70, 122)));
		}
	}

	int colorIndex = 0;
	int direction = 1;
	int darkCounter = 0;
	boolean shouldUpdate = true;

	/**
	 * Provides a blinking animation border for a number of given nodes and given colors
	 * the animation will go through the given color array
	 * 
	 * @param node
	 *        the nodes which will get an animated border
	 * @param colors
	 *        all steps of the animations colors
	 * @return the thread which performs the animation. With this instance the animation can be cancelled
	 */
	private Runnable animate(GraphNode[] node, Color[] colors) {
		for (int i = 0; i < node.length; ++i)
			node[i].setBorderWidth(3);

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < node.length; ++i) {
					if (node[i].isDisposed())
						return;
				}

				if (colorIndex + 1 >= colors.length)
					direction = -1;

				if (colorIndex - 1 < 0) {
					darkCounter++;
					shouldUpdate = false;
					if (darkCounter >= 20) {
						direction = 1;
						darkCounter = 0;
						shouldUpdate = true;
					}
				}
				if (shouldUpdate) {
					colorIndex += direction;
					for (int i = 0; i < node.length; ++i) {
						node[i].setBorderColor(colors[colorIndex]);
					}
				}
				graph.redraw();

				getDisplay().timerExec(40, this);
			}
		};
		getDisplay().timerExec(40, runnable);
		return runnable;
	}

}
