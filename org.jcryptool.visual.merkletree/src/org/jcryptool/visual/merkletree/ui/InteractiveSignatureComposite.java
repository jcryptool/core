package org.jcryptool.visual.merkletree.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.MultiTree;
import org.jcryptool.visual.merkletree.algorithm.Node;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Provides an step by step guide tour with a graph through a Merkle Signature generation
 * 
 * @author Michael Altenhuber
 *
 */
public class InteractiveSignatureComposite extends Composite {
	private GraphViewer viewer;
	private ArrayList<GraphConnection> markedConnectionList;
	private ArrayList<GraphNode> markedAuthpathList;
	private ViewPart masterView;
	private MerkleTreeSignatureComposite signatureComposite;
	private Composite footerComposite;
	private Composite graphComposite;
	private Composite zestComposite;
	private GridLayout zestLayout;
	private SashForm zestSash;
	private Display curDisplay;
	private boolean distinctListener = false;
	private boolean mouseDragging;
	private Point oldMouse;
	private Point newMouse;
	private int differenceMouseX;
	private int differenceMouseY;
	private Point oldSash;
	private Point oldPopup;
	private ISimpleMerkle merkle;
	private Graph graph;;
	private Text signatureText;
	private GridData signatureTextLayout;
	private ScrolledComposite scrolledComposite;
	private Label signatureSize;
	private Color[] greySteps;
	private Color[] redSteps;
	private SUIT mode;
	private boolean movementLinked;

	private Runnable currentlyHighlighted;
	private Runnable highlightedAuthpath;

	private String message;
	private int step = 0;

	private Color[] distinguishableColors;

	/**
	 * Create the GUI elements, including the graph, a guidewindow, and a signature footer
	 * 
	 * @param parent
	 * @param style
	 *        SWT composite style bits
	 * @param merkle
	 * @param mode
	 * @param signatureComposite
	 *        the parent class needed for method calls
	 * @param masterView
	 *        needed for setTab call
	 */
	public InteractiveSignatureComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT mode, ViewPart masterView, MerkleTreeSignatureComposite signatureComposite) {
		super(parent, style);
		this.setLayout(new GridLayout(8, true));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, MerkleConst.DESC_HEIGHT + 1));
		this.masterView = masterView;
		curDisplay = getDisplay();
		this.merkle = merkle;
		this.mode = mode;
		this.signatureComposite = signatureComposite;
		movementLinked = false;

		graphComposite = new Composite(this, SWT.BORDER);
		graphComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		GridLayout graphCompositeLayout = new GridLayout(1, true);
		graphCompositeLayout.marginWidth = 0;
		graphCompositeLayout.marginHeight = 0;
		graphComposite.setLayout(graphCompositeLayout);

		// Note: The mouse dragging in this class works the following way:
		// zestComposite is used as Control which fills the given space in the Layout
		// zestSash inherits from zestComposite, but has a custom size
		// the graph is put onto the zestSash

		// now the zestSash can be moved around and it looks like the graph would scroll

		zestComposite = new Composite(graphComposite, SWT.NO_REDRAW_RESIZE);
		zestComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		zestComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));

		zestLayout = new GridLayout();
		zestComposite.setLayout(zestLayout);

		zestSash = new SashForm(zestComposite, SWT.NONE);
		zestSash.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(zestSash);
		zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));

		// sets the size for zestSash
		this.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				Point currentShellSize;
				currentShellSize = parent.getSize();
				double x, y;
				Point startingSashLocation;

				switch (merkle.getLeafCounter()) {

				case 2:
					x = currentShellSize.x;
					// y = currentShellSize.y / 2;
					y = currentShellSize.y;
					startingSashLocation = new Point(70, 10);
					break;
				case 4:
					x = currentShellSize.x;
					// y = currentShellSize.y / 1.7;
					y = currentShellSize.y;
					startingSashLocation = new Point(40, 10);
					break;
				case 8:
					x = currentShellSize.x;
					y = currentShellSize.y;
					startingSashLocation = new Point(20, 0);
					break;
				case 16:
					x = currentShellSize.x * 1.2;
					y = currentShellSize.y;
					startingSashLocation = new Point(-150, 0);
					break;
				case 32:
					x = currentShellSize.x * 1.5;
					y = currentShellSize.y * 1.2;
					startingSashLocation = new Point(-450, 0);
					break;
				case 64:
					x = currentShellSize.x * 2;
					y = currentShellSize.y * 1.5;
					startingSashLocation = new Point(-925, 0);
					break;
				default:
					x = currentShellSize.x;
					y = currentShellSize.y;
					startingSashLocation = new Point(80, 10);
					break;
				}

				zestComposite.setSize((int) x, (int) y);
				zestSash.setLocation(startingSashLocation);

			}
		});

		// Beginning of the Graph
		viewer = new GraphViewer(zestSash, SWT.NONE);
		// Camera Movement
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
				oldSash = zestSash.getLocation();
				if (distinctListener == false)
					zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						while (mouseDragging) {
							if (distinctListener == false)
								updateSashPosition();
							try {
								Thread.sleep(25);

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

		viewer.getGraphControl().setHorizontalScrollBarVisibility(FigureCanvas.NEVER);
		viewer.getGraphControl().setVerticalScrollBarVisibility(FigureCanvas.NEVER);
		markedConnectionList = new ArrayList<GraphConnection>();
		markedAuthpathList = new ArrayList<GraphNode>();
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider(ColorConstants.lightGreen));

		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
		viewer.setInput(merkle.getTree());
		viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		viewer.applyLayout();

		GridDataFactory.fillDefaults().grab(true, true).applyTo(viewer.getControl());

		graph = viewer.getGraphControl();
		graph.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));

		// Makes the nodes fixed (they cannot be dragged around with the mouse
		// by overriding the mouseMovedListener with empty event
		graph.getLightweightSystem().setEventDispatcher(new SWTEventDispatcher() {
			public void dispatchMouseMoved(MouseEvent e) {
			}
		});

		if (mode == SUIT.XMSS_MT) {
			colorizeMultitrees();

			// prevents a MultiTree node from being colorized yellow when
			// clicked
			graph.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (e.item instanceof GraphNode) {
						((GraphNode) e.item).unhighlight();
					}

				}
			});
		}

		footerComposite = new Composite(this, SWT.NO_REDRAW_RESIZE);
		footerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 8, 1));
		footerComposite.setLayout(new GridLayout(8, true));

		Button startoverButton = new Button(footerComposite, SWT.PUSH);
		startoverButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		startoverButton.setText(Descriptions.InteractiveSignature_Button_3);
		scrolledComposite = new ScrolledComposite(footerComposite, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 4));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		signatureText = new Text(scrolledComposite, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		signatureText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		signatureText.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		scrolledComposite.setContent(signatureText);
		Label spacer = new Label(footerComposite, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		Button startoverButton3 = new Button(footerComposite, SWT.PUSH);
		startoverButton3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		startoverButton3.setVisible(false);

		signatureSize = new Label(footerComposite, SWT.READ_ONLY | SWT.WRAP);
		signatureSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		signatureSize.setText(Descriptions.InteractiveSignature_11 + " 0" + " Byte");

		startoverButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (step != 7)
					withdrawSignature();
				startOver();
			}

		});

		// color arrays for the animate() funcion used in MultiTree
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

		// Uncomment to activate zooming, however Composite popup not supported
		// yet
		// ZoomManager zoomManager = new ZoomManager(graph.getRootLayer(),
		// graph.getViewport());
		// graph.addMouseWheelListener(new MouseWheelListener() {
		//
		// @Override
		// public void mouseScrolled(MouseEvent e) {
		// if (e.count < 0) {
		// zoomManager.zoomOut();
		// } else {
		// zoomManager.zoomIn();
		// }
		// }
		// });

	}

	/**
	 * Marks the whole branch begining from the leaf node
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
	}

	/**
	 * Unmark a previous marked branch
	 * 
	 * @param markedConnectionList
	 *        - Contains marked elements
	 */
	private void unmarkBranch() {
		GraphConnection authPath;
		for (GraphConnection connection : markedConnectionList) {
			connection.setLineColor(ColorConstants.lightGray);
			// connection.getSource().setBackgroundColor(viewer.getGraphControl().LIGHT_BLUE);
			if (mode == SUIT.XMSS_MT) {
				connection.getSource().setBorderWidth(0);
			} else {

				if (((GraphNode) connection.getSource()).getTargetConnections().isEmpty())
					connection.getSource().setBackgroundColor(ColorConstants.lightGreen);

				authPath = (GraphConnection) connection.getSource().getSourceConnections().get(0);
				authPath.getDestination().setBackgroundColor(ColorConstants.lightGreen);
				authPath = (GraphConnection) connection.getSource().getSourceConnections().get(1);
				authPath.getDestination().setBackgroundColor(ColorConstants.lightGreen);
			}
			// color the nodes back to light green
			Node leaf = (Node) connection.getDestination().getData();
			if (leaf.isLeaf()) {
				if (mode == SUIT.XMSS_MT) {
					connection.getDestination().setBorderWidth(0);
				} else {
					connection.getDestination().setBackgroundColor(ColorConstants.lightGreen);
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
	 * Sets the current zestSash location based on mouse movement
	 */
	private void updateSashPosition() {
		curDisplay.asyncExec(new Runnable() {
			@Override
			public void run() {
				newMouse = getDisplay().getCursorLocation();
				differenceMouseX = newMouse.x - oldMouse.x;
				differenceMouseY = newMouse.y - oldMouse.y;

				if (differenceMouseX != 0 || differenceMouseY != 0) {
					zestSash.setLocation((newMouse.x - oldMouse.x) + oldSash.x, (newMouse.y - oldMouse.y) + oldSash.y);
					oldSash.x = (newMouse.x - oldMouse.x) + oldSash.x;
					oldSash.y = (newMouse.y - oldMouse.y) + oldSash.y;

					if (popup != null && !popup.isDisposed() && movementLinked) {
						popupPosition.x = (newMouse.x - oldMouse.x) + oldPopup.x;
						popupPosition.y = (newMouse.y - oldMouse.y) + oldPopup.y;
						popup.setLocation(popupPosition);
						oldPopup = popupPosition;
					}

					oldMouse = newMouse;
				}

			}
		});
	}

	private Composite popup;
	private Rectangle currentView;
	private Point popupPosition;
	private Point currentSashPosition;

	// these two variables currently here for future planned feature
	private Point popupShouldPosition;
	private Point sashShouldPosition;

	private StyledText guideText;
	private Text inputText;
	private Button nextButton;
	private Button backButton;
	private Button verifyButton;

	private ModifyListener emptyListener;

	private String plainSignature;
	private String signature[];
	private GraphNode rootNode;
	private GraphNode[] leaves;
	private Runnable singleHighlightNode;
	boolean isNextListener = true;
	boolean goingBack = false;

	int currentIndex;
	int sigStringIndex;

	public void interactiveSignatureGeneration() {
		// TODO
		signatureTextLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 4);
		signatureTextLayout.heightHint = scrolledComposite.getBounds().height;
		scrolledComposite.setLayoutData(signatureTextLayout);

		// Create the guide window
		// popup = new Composite((Composite) viewer.getControl(), SWT.BORDER);
		popup = new Composite(zestComposite, SWT.BORDER);
		popup.setVisible(false);

		Point popupSize = popup.computeSize(550, 175);
		popup.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				popup.setSize(popupSize);
				popup.setLocation(popupPosition);
			}
		});

		popup.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		popup.setLayout(new GridLayout(6, true));
		popup.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
		zestSash.moveBelow(popup);
		sigStringIndex = 0;

		// Get leaves and root node
		List<?> graphNodeRetriever = graph.getNodes();
		leaves = new GraphNode[graphNodeRetriever.size() / 2 + 1];

		for (int i = 0, j = 0; i < graphNodeRetriever.size(); ++i) {
			if (((GraphNode) graphNodeRetriever.get(i)).getSourceConnections().isEmpty()) {
				leaves[j] = (GraphNode) graphNodeRetriever.get(i);
				++j;
			}
			if (((GraphNode) graphNodeRetriever.get(i)).getTargetConnections().isEmpty()) {
				rootNode = (GraphNode) graphNodeRetriever.get(i);
			}
		}
		guideText = new StyledText(popup, SWT.WRAP);
		guideText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));
		guideText.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		guideText.setEditable(false);
		guideText.setCaret(null);

		inputText = new Text(popup, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		inputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));

		Composite buttonRow = new Composite(popup, SWT.DOUBLE_BUFFERED);
		buttonRow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		buttonRow.setLayout(new GridLayout(6, true));

		backButton = new Button(buttonRow, SWT.PUSH);
		backButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		backButton.setText(Descriptions.InteractiveSignature_1);

		verifyButton = new Button(buttonRow, SWT.PUSH);
		verifyButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		verifyButton.setText(Descriptions.InteractiveSignature_Button_4);

		nextButton = new Button(popup, SWT.PUSH);
		nextButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		nextButton.setEnabled(false);

		// Step by Step Listeners
		/* nextListener */
		nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isNextListener) {
					++step;
					stepByStep();
				} else {
					startOver();
				}
			}
		});

		backButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				--step;
				goingBack = true;
				stepByStep();
			}
		});

		verifyButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				((MerkleTreeView) masterView).setTab(4);
				startOver();
			}
		});

		this.addControlListener(new ControlListener() {
			// Prevents drawing when the control is resized
			// TODO this creates many unnecessary threads
			@Override
			public void controlResized(ControlEvent e) {
				popup.setVisible(false);
				zestComposite.setVisible(false);
				Runnable runnable = new Runnable() {
					@Override
					public void run() {

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
						renderVisible();

					}
				};

				new Thread(runnable).start();
			};

			@Override
			public void controlMoved(ControlEvent e) {
				// nothing happens when moved

			}
		});

		emptyListener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (inputText.getText().length() > 0) {
					nextButton.setEnabled(true);
				} else {
					nextButton.setEnabled(false);
				}

			}
		};

		stepByStep();
	}

	Point leafPosition = new Point(0, 0);

	private void stepByStep() {

		switch (step) {

		// Initial Step 0:
		// Window Position: Centered, Task: Enter Text -> next
		case 0:
			popup.setVisible(false);
			if (goingBack) {
				signatureComposite.updateIndexLabel(currentIndex - 1);
				merkle.setIndex(currentIndex);

				guideText.setText(Descriptions.InteractiveSignature_2);
				guideText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));
				inputText.setText(message);
				inputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
				popup.layout();
			}
			inputText.requestLayout();
			currentIndex = merkle.getKeyIndex();
			popup.setVisible(true);

			// *****Position*****//
			currentView = signatureComposite.getBounds();
			currentView.height -= (footerComposite.getBounds().height + 70);
			// Initial Middle Position by taking the half of the currentView
			// window, and subtract half the size of the popup
			popupPosition = new Point(currentView.width / 2, currentView.height / 2);
			popupPosition.x -= popup.getBounds().width / 2;
			popupPosition.y -= popup.getBounds().height / 2;
			popupShouldPosition = new Point(popupPosition.x, popupPosition.y);
			currentSashPosition = zestSash.getLocation();
			sashShouldPosition = currentSashPosition;
			movementLinked = false;

			// *****Listeners*****//
			nextButton.setText(Descriptions.InteractiveSignature_Button_2);
			inputText.addModifyListener(emptyListener);
			isNextListener = true;

			// *****Content*****//
			signatureComposite.setInteractiveStatus(false);
			backButton.setText(Descriptions.InteractiveSignature_Button_1);
			backButton.setVisible(false);
			verifyButton.setVisible(false);
			inputText.setVisible(true);
			inputText.setEditable(true);
			guideText.setText(Descriptions.InteractiveSignature_1);
			popup.setLocation(popupPosition);
			oldPopup = popup.getLocation();

			goingBack = false;
			break;
		// Step 1: First Description
		// Window Position: still centered, Task, -> next
		case 1:
			if (!goingBack) {
				message = inputText.getText();
				movementLinked = false;
			}
			signatureComposite.setInteractiveStatus(true);
			guideText.setText(Descriptions.InteractiveSignature_2);
			guideText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
			inputText.setVisible(false);
			inputText.setText("");
			inputText.removeModifyListener(emptyListener);
			inputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));

			nextButton.setEnabled(true);

			popup.pack();
			popup.layout();
			backButton.setVisible(true);
			inputText.setEditable(false);
			inputText.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

			if (goingBack) {
				merkle.setIndex(currentIndex);
				if (mode == SUIT.XMSS_MT) {
					getDisplay().timerExec(-1, singleHighlightNode);
					leaves[currentIndex].setBorderWidth(0);
				} else {
					leaves[currentIndex].unhighlight();
				}

			} else {
				plainSignature = merkle.sign(message);
				if (plainSignature == "") {
					signatureComposite.keysExceededMessage();
					break;
				}
				signature = plainSignature.split("\\|");
				signatureComposite.updateIndexLabel(currentIndex);
			}

			signatureText.setText("");
			goingBack = false;
			break;
		// Step 2: Index/Leaf
		// Window Position: bottom-left at leaf, Task: -> next
		case 2:

			// *****Position*****//
			leafPosition = new Point(leaves[currentIndex].getLocation().x, leaves[currentIndex].getLocation().y);
			currentSashPosition.x = -leafPosition.x + popup.getBounds().width + 30;
			currentSashPosition.y = -leafPosition.y + currentView.height - (popup.getBounds().height / 2) - 40;
			zestSash.setLocation(currentSashPosition);
			popupPosition.x = 20;
			popupPosition.y = currentView.height - popup.getBounds().height - 40;
			popup.setLocation(popupPosition);
			popupShouldPosition = new Point(popupPosition.x, popupPosition.y);
			oldPopup = popup.getLocation();
			movementLinked = true;

			// *****Content*****//
			guideText.setText(Descriptions.InteractiveSignature_3_1 + " " + currentIndex + " " + Descriptions.InteractiveSignature_3_2);
			sigStringIndex = goingBack ? sigStringIndex - 1 : sigStringIndex;
			signatureText.setText(signature[sigStringIndex] + "|");
			signatureSize.setText(Descriptions.InteractiveSignature_11 + " " + Converter._numberToPrefix(signatureText.getText().length() / 2));
			if (mode == SUIT.XMSS_MT && goingBack == false) {
				GraphNode[] tmpArray = new GraphNode[1];
				tmpArray[0] = leaves[currentIndex];
				singleHighlightNode = animate(tmpArray, greySteps);
			} else {
				leaves[currentIndex].highlight();
			}

			goingBack = false;
			break;
		// Step 3: Seed if needed (XMSS and MT only)
		// Window Position: bottom-left at leaf, Task: -> next
		case 3:
			if (merkle instanceof SimpleMerkleTree) {
				step = goingBack ? step - 1 : step + 1;
				stepByStep();

			} else {
				guideText.setText(Descriptions.InteractiveSignature_Seed);
				signatureText.setText("");
				sigStringIndex = goingBack ? sigStringIndex - 1 : sigStringIndex + 1;
				for (int i = 0; i <= sigStringIndex; ++i) {
					signatureText.append(signature[i] + "|");
				}
				goingBack = false;

			}
			break;
		// Step 4: set OTS signature
		// Window Position: bottom-left at leaf Task: -> next
		case 4:
			if (goingBack) {
				unmarkBranch();
				if (mode == SUIT.XMSS_MT) {
					GraphNode[] tmpArray = new GraphNode[1];
					tmpArray[0] = leaves[currentIndex];
					singleHighlightNode = animate(tmpArray, greySteps);
				} else {
					leaves[currentIndex].highlight();
				}
			}
			// *****Content*****//
			guideText.setText(Descriptions.InteractiveSignature_4_1 + " " + currentIndex + " " + Descriptions.InteractiveSignature_4_2);

			signatureText.setText("");
			sigStringIndex = goingBack ? sigStringIndex : sigStringIndex + 1;
			for (int i = 0; i <= sigStringIndex; ++i) {
				signatureText.append(signature[i] + "|");
			}
			signatureSize.setText(Descriptions.InteractiveSignature_11 + " " + Converter._numberToPrefix(signatureText.getText().length() / 2));
			goingBack = false;
			break;

		// Step 5: authentication path explanation
		// Window Position: bottom-left at leaf Task: -> next
		case 5:
			if (mode == SUIT.XMSS_MT) {
				if (singleHighlightNode != null)
					getDisplay().timerExec(-1, singleHighlightNode);
			}
			guideText.setText(Descriptions.InteractiveSignature_5);
			if (goingBack == false) {
				markBranch(leaves[currentIndex]);
				markAuthPath(markedConnectionList);
			} else {
				verifyButton.setVisible(false);
				nextButton.setText(Descriptions.InteractiveSignature_Button_2);
				isNextListener = true;
			}
			// *****Position (when using back)*****//
			leafPosition = new Point(leaves[currentIndex].getLocation().x, leaves[currentIndex].getLocation().y);
			currentSashPosition.x = -leafPosition.x + popup.getBounds().width + 30;
			currentSashPosition.y = -leafPosition.y + currentView.height - (popup.getBounds().height / 2) - 40;
			zestSash.setLocation(currentSashPosition);
			popupPosition.x = 20;
			popupPosition.y = currentView.height - popup.getBounds().height - 40;
			popup.setLocation(popupPosition);
			popupShouldPosition = new Point(popupPosition.x, popupPosition.y);
			oldPopup = popup.getLocation();

			// *****Content*****//
			signatureComposite.setInteractiveStatus(true);
			nextButton.setText(Descriptions.InteractiveSignature_Button_2);
			signatureText.setText("");
			for (int i = 0; i < signature.length; ++i) {
				if (i + 1 >= signature.length) {
					signatureText.append(signature[i]);
				} else {
					signatureText.append(signature[i] + "|");
				}
			}
			signatureSize.setText(Descriptions.InteractiveSignature_11 + " " + Converter._numberToPrefix(signatureText.getText().length() / 2));

			isNextListener = true;
			goingBack = false;
			break;
		case 6:
			if (mode != SUIT.XMSS_MT) {
				step = goingBack ? step - 1 : step + 1;
				stepByStep();

			} else {
				int reducedSignatureCount = 0;
				for (int q = 2; q < signature.length; ++q) {
					if (signature[q].length() > signature[1].length()) {
						reducedSignatureCount++;
					}
				}
				guideText.setText(Descriptions.InteractiveSignature_12 + " " + reducedSignatureCount + ".");
				int singleTreeHeight = ((MultiTree) merkle).getSingleTreeHeight();
				GraphNode nextRoot = leaves[currentIndex];
				for (int i = 1; i < singleTreeHeight; ++i) {
					nextRoot = ((GraphConnection) nextRoot.getTargetConnections().get(0)).getSource();
				}
				Point nextTreePosition = new Point(nextRoot.getLocation().x, nextRoot.getLocation().y);
				currentSashPosition.x = -nextTreePosition.x + popup.getBounds().width + 30;
				int nextRootOffset = leaves[currentIndex].getLocation().y - currentView.height + leaves[currentIndex].getSize().height + 20;

				currentSashPosition.y = -nextRootOffset;
				zestSash.setLocation(currentSashPosition);
				popupPosition.x = 20;
				popupPosition.y = currentSashPosition.y + nextTreePosition.y - popup.getBounds().height / 2;

				// popupPosition.y = currentView.height -
				// popup.getBounds().height - 40;
				signatureComposite.setInteractiveStatus(true);
				popup.setLocation(popupPosition);
				popupShouldPosition = new Point(popupPosition.x, popupPosition.y);
				oldPopup = popup.getLocation();

				if (goingBack) {
					verifyButton.setVisible(false);
					nextButton.setText(Descriptions.InteractiveSignature_Button_2);
					isNextListener = true;
				}

				goingBack = false;

			}
			break;
		// Final step: the signature is ready dialogue
		// Window position: over root, Task: create new or verify
		case 7:
			// *****Position*****//
			Point rootPosition = new Point(rootNode.getLocation().x, rootNode.getLocation().y);
			currentSashPosition.x = -rootPosition.x + currentView.width / 2;
			currentSashPosition.y = -rootPosition.y + popup.getBounds().height + 15;
			zestSash.setLocation(currentSashPosition);

			popupPosition.x = currentView.width / 2 - popup.getBounds().width / 2;
			popupPosition.y = 10;
			popup.setLocation(popupPosition);
			popupShouldPosition = new Point(popupPosition.x, popupPosition.y);
			oldPopup = popup.getLocation();

			// signatureComposite.updateIndexLabel(currentIndex - 1);
			signatureComposite.addSignatureAndMessage(plainSignature, message);

			if (mode != SUIT.XMSS_MT) {
				guideText.setText(Descriptions.InteractiveSignature_6);
			} else {
				guideText.setText(Descriptions.InteractiveSignature_6_MT);
			}

			signatureComposite.setInteractiveStatus(false);

			verifyButton.setVisible(true);
			// *****Listener*****//
			nextButton.setText(Descriptions.InteractiveSignature_Button_3);
			isNextListener = false;
			goingBack = false;
			break;
		// case 9:
		// popup.dispose();
		// step = 0;
		// break;
		default:
			break;
		}
	}

	public String getSignature() {
		return plainSignature;
	}

	public String getMessage() {
		return message;
	}

	private void renderVisible() {
		getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				popup.setVisible(true);
				zestComposite.setVisible(true);

			}
		});
	}

	public void withdrawSignature() {
		merkle.setIndex(currentIndex);
	}

	private void colorizeMultitrees() {
		int singleTreeHeight = ((MultiTree) merkle).getSingleTreeHeight();
		// int treeHeight = ((MultiTree) merkle).getTreeHeight();
		int singleTreeLeaves = (int) Math.pow(2, singleTreeHeight - 1);
		int treeCount = 0;
		int leafCounter = merkle.getLeafCounter();

		List<?> graphNodeRetriever = graph.getNodes();
		GraphNode[] nodes = new GraphNode[graphNodeRetriever.size()];
		GraphNode[] leaves = new GraphNode[graphNodeRetriever.size() / 2 + 1];

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

	private void startOver() {
		step = 0;
		inputText.setText("");
		signatureText.setText("");
		signatureSize.setText("");
		plainSignature = null;
		zestSash.setLocation(0, 0);

		guideText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));
		inputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));

		if (singleHighlightNode != null) {
			getDisplay().timerExec(-1, singleHighlightNode);
			leaves[currentIndex].setBorderWidth(0);
		}
		unmarkBranch();

		stepByStep();
	}

}
