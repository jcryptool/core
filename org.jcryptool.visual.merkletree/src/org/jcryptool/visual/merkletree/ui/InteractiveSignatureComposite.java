package org.jcryptool.visual.merkletree.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
import org.jcryptool.visual.merkletree.algorithm.Node;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

public class InteractiveSignatureComposite extends Composite {
	/* implements IZoomableWorkbenchPart */
	private GraphViewer viewer;
	private ArrayList<GraphConnection> markedConnectionList;
	private ViewPart masterView;
	private MerkleTreeSignatureComposite signatureComposite;
	Composite footerComposite;
	Composite graphComposite;
	Composite zestComposite;
	GridLayout zestLayout;
	SashForm zestSash;
	Display curDisplay;
	boolean distinctListener = false;
	boolean mouseDragging;
	Point oldMouse;
	Point newMouse;
	int differenceMouseX;
	int differenceMouseY;
	Point oldSash;
	Point cameraPoint;
	Point oldPopup;
	Object lock = new Object();
	ISimpleMerkle merkle;
	Graph graph;
	Composite parent;
	boolean expandedFlag = true;
	Text signatureText;
	String temporaryResize;
	Label spacers[];
	GridData signatureTextLayout;
	ScrolledComposite scrolledComposite;
	int authpathSize;
	Label signaturSize;

	// Interactive Variables
	String message;
	int step = 0;

	/**
	 * Create the composite. Including Description, GraphItem, GraphView,
	 * Description for GraphView
	 * 
	 * @param parent
	 * @param style
	 */
	/**
	 * @param parent
	 * @param style
	 * @param merkle
	 * @param mode
	 */
	public InteractiveSignatureComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT mode, ViewPart masterView, MerkleTreeSignatureComposite signatureComposite) {
		super(parent, style);
		this.setLayout(new GridLayout(8, true));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, MerkleConst.DESC_HEIGHT + 1));
		this.masterView = masterView;
		curDisplay = getDisplay();
		this.merkle = merkle;
		this.parent = parent;
		this.signatureComposite = signatureComposite;

		// spacers = new Label[4];
		// for (int i = 0; i < spacers.length; ++i) {
		// spacers[i] = new Label(this, SWT.NONE);
		// spacers[i].setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_MAGENTA));
		// spacers[i].setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false,
		// false, 1, 1));
		// }

		graphComposite = new Composite(this, SWT.NONE);
		graphComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		graphComposite.setLayout(new GridLayout(1, true));

		// Composite which contains a SashForm which contains the MerkleTree
		// Zest Graph
		zestComposite = new Composite(graphComposite, SWT.NO_REDRAW_RESIZE);
		zestComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		zestComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
		// zestComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));

		zestLayout = new GridLayout();
		zestComposite.setLayout(zestLayout);

		zestSash = new SashForm(zestComposite, SWT.NONE);
		zestSash.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(zestSash);
		zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));

		// sets the size for Composite zestComposite

		this.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				Point currentShellSize;
				currentShellSize = parent.getSize();
				double x, y;

				switch (merkle.getLeafCounter()) {

				case 2:
					x = currentShellSize.x;
					// y = currentShellSize.y / 2;
					y = currentShellSize.y;
					zestSash.setLocation(70, 10);
					break;
				case 4:
					x = currentShellSize.x;
					// y = currentShellSize.y / 1.7;
					y = currentShellSize.y;
					zestSash.setLocation(40, 10);
					break;
				case 8:
					x = currentShellSize.x;
					y = currentShellSize.y;
					zestSash.setLocation(20, 0);
					break;
				case 16:
					x = currentShellSize.x * 1.2;
					y = currentShellSize.y;
					zestSash.setLocation(-150, 0);
					break;
				case 32:
					x = currentShellSize.x * 1.5;
					y = currentShellSize.y * 1.2;
					zestSash.setLocation(-450, 0);
					break;
				case 64:
					x = currentShellSize.x * 2;
					y = currentShellSize.y * 1.5;
					zestSash.setLocation(-925, 0);
					break;
				default:
					x = currentShellSize.x;
					y = currentShellSize.y;
					zestSash.setLocation(80, 10);
					break;
				}

				// zestComposite.setSize(1920, 1080);
				zestComposite.setSize((int) x, (int) y);

			}
		});

		cameraPoint = zestSash.getLocation();
		// Beginning of the Graph
		viewer = new GraphViewer(zestSash, SWT.NONE);
		// Camera Movement
		MouseListener dragQueen = new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				distinctListener = false;
				mouseDragging = false;
				cameraPoint = zestSash.getLocation();
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
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider(ColorConstants.lightGreen));

		// select the layout of the connections -> CONNECTIONS_DIRECTED would be
		// a ->
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
		linkMerkleTree(merkle);
		viewer.applyLayout();

		Control control = viewer.getControl();
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		graph = viewer.getGraphControl();

		// Makes the nodes fixed (they cannot be dragged around with the mouse
		// by overriding the mouseMovedListener with empty event
		graph.getLightweightSystem().setEventDispatcher(new SWTEventDispatcher() {
			public void dispatchMouseMoved(MouseEvent e) {
			}

		});

		graph.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));

		footerComposite = new Composite(this, SWT.NO_REDRAW_RESIZE);
		footerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 8, 1));
		footerComposite.setLayout(new GridLayout(8, true));

		Button testLabel = new Button(footerComposite, SWT.PUSH);
		testLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		testLabel.setText(Descriptions.InteractiveSignature_Button_3);
		scrolledComposite = new ScrolledComposite(footerComposite, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 4));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		signatureText = new Text(scrolledComposite, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		signatureText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		signatureText.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		scrolledComposite.setContent(signatureText);
		Label asdf = new Label(footerComposite, SWT.NONE);
		asdf.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		// Button testLabel2 = new Button(footerComposite, SWT.PUSH);
		// testLabel2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
		// 2, 1));
		// testLabel2.setText("Button 2");
		Button testLabel3 = new Button(footerComposite, SWT.PUSH);
		testLabel3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		testLabel3.setVisible(false);

		signaturSize = new Label(footerComposite, SWT.READ_ONLY | SWT.WRAP);
		signaturSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		signaturSize.setText(Descriptions.MerkleTreeSign_6 + " 0");

		testLabel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				step = 0;
				inputText.setText("");
				signatureText.setText("");
				stepByStep();
			}

		});

	}

	/**
	 * Marks the whole branch begining from the leaf node
	 * 
	 * @param leaf
	 *            - the leaf node of the branch
	 */
	@SuppressWarnings("unchecked")
	private void markBranch(GraphNode leaf) {
		ArrayList<GraphItem> items = new ArrayList<GraphItem>();

		try {
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
		} catch (IndexOutOfBoundsException ex) {
			items.add(((GraphConnection) (leaf.getSourceConnections().get(0))).getSource());
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
		GraphConnection authPath;
		for (GraphConnection connection : markedConnectionList) {
			connection.setLineColor(ColorConstants.lightGray);
			connection.getSource().setBackgroundColor(viewer.getGraphControl().LIGHT_BLUE);
			authPath = (GraphConnection) connection.getSource().getSourceConnections().get(0);
			authPath.getDestination().setBackgroundColor(ColorConstants.lightGreen);
			authPath = (GraphConnection) connection.getSource().getSourceConnections().get(1);
			authPath.getDestination().setBackgroundColor(ColorConstants.lightGreen);

			// color the nodes back to light green
			Node leaf = (Node) connection.getDestination().getData();
			if (leaf.isLeaf()) {
				connection.getDestination().setBackgroundColor(ColorConstants.lightGreen);
			} else {
				connection.getDestination().setBackgroundColor(ColorConstants.lightGreen); // viewer.getGraphControl().LIGHT_BLUE
			}
		}
	}

	/**
	 * Marks the authentification path of the leaf
	 * 
	 * @param markedConnectionList
	 *            - Contains marked elements of the Changing Path
	 */
	private void markAuthPath(List<GraphConnection> markedConnectionList) {
		GraphConnection authPath;
		// List<GraphConnection> connections = leaf.getTargetConnections();
		for (GraphConnection connect : markedConnectionList) {
			Node myNode = (Node) connect.getDestination().getData();
			Node parentNode = (Node) connect.getSource().getData();

			if (myNode.equals(parentNode.getLeft())) {
				authPath = (GraphConnection) connect.getSource().getSourceConnections().get(1);
				authPath.getDestination().setBackgroundColor(ColorConstants.red);
			} else {
				authPath = (GraphConnection) connect.getSource().getSourceConnections().get(0);
				authPath.getDestination().setBackgroundColor(ColorConstants.red);
			}
		}
	}

	/**
	 * Synchronize the merklTree with the other Tabpages
	 * 
	 * @param merkle
	 */
	private void linkMerkleTree(ISimpleMerkle merkle) {
		if (merkle.getMerkleRoot() != null) {
			viewer.setInput(merkle.getTree());
			viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			viewer.applyLayout();
		}

	}

	/**
	 * 
	 * 
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

					if (popup != null && !popup.isDisposed()) {
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

	Composite popup;
	Rectangle compositePosition;
	Rectangle windowPosition;
	Rectangle currentView;
	Point popupPosition;
	Point popupShouldPosition;
	Point currentSashPosition;
	Point sashShouldPosition;

	Label guideLabel;
	Text inputText;
	Button nextButton;
	Button backButton;
	Button verifyButton;

	SelectionListener nextListener;
	SelectionListener newRoundListener;

	String plainSignature;
	String signature[];
	GraphNode rootNode;
	GraphNode[] leaves;
	Image highlightedNode;

	int currentIndex;

	public void interactiveSignatureGeneration() {
		// TODO
		signatureTextLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 4);
		signatureTextLayout.heightHint = scrolledComposite.getBounds().height;
		scrolledComposite.setLayoutData(signatureTextLayout);

		// Create the guide window
		popup = new Composite(zestComposite, SWT.BORDER);
		popup.setVisible(false);
		popup.setBounds(0, 0, 500, 150);
		popup.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		popup.setLayout(new GridLayout(6, true));
		popup.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
		zestSash.moveBelow(popup);

		// Initialize current size of the view
		// compositePosition = zestComposite.getBounds();
		windowPosition = getShell().getBounds();

		// Get leaves and root node
		List<?> graphNodeRetriever = graph.getNodes();
		Object[] nodeRetriever = viewer.getNodeElements();
		leaves = new GraphNode[nodeRetriever.length / 2 + 1];

		for (int i = 0, j = 0; i < graphNodeRetriever.size(); ++i) {
			if (((GraphNode) graphNodeRetriever.get(i)).getSourceConnections().isEmpty()) {
				leaves[j] = (GraphNode) graphNodeRetriever.get(i);
				++j;
			}
			if (((GraphNode) graphNodeRetriever.get(i)).getTargetConnections().isEmpty()) {
				rootNode = (GraphNode) graphNodeRetriever.get(i);
			}
		}

		currentIndex = merkle.getKeyIndex();
		guideLabel = new Label(popup, SWT.WRAP);
		guideLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));

		inputText = new Text(popup, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		inputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));

		backButton = new Button(popup, SWT.PUSH);
		backButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		backButton.setText(Descriptions.InteractiveSignature_1);

		Label spacer = new Label(popup, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

		verifyButton = new Button(popup, SWT.PUSH);
		verifyButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		verifyButton.setText(Descriptions.InteractiveSignature_Button_4);

		nextButton = new Button(popup, SWT.PUSH);
		nextButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		authpathSize = 1;

		popup.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				popup.setSize(500, 150);
				popup.setLocation(popupPosition);
			}
		});

		// Step by Step Listeners
		nextListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				++step;
				stepByStep();
			}
		};
		newRoundListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				step = 0;
				inputText.setText("");
				signatureText.setText("");
				stepByStep();
				plainSignature = null;
			}
		};

		backButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				--step;
				stepByStep();
			}
		});

		verifyButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				((MerkleTreeView) masterView).setTab(4);
				popup.dispose();
				step = 0;
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

		stepByStep();
		popup.layout(true);
		popup.setVisible(true);
	}

	Point leafPosition = new Point(0, 0);

	private void stepByStep() {

		switch (step) {

		// Initial Step 0:
		// Window Position: Centered, Task: Enter Text -> next
		case 0:
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

			// *****Listeners*****//
			// nextButton.setText(Descriptions.InteractiveSignature_2);
			nextButton.setText(Descriptions.InteractiveSignature_Button_2);
			nextButton.removeSelectionListener(newRoundListener);
			nextButton.removeSelectionListener(nextListener);
			nextButton.addSelectionListener(nextListener);

			// *****Content*****//
			signatureComposite.setInteractiveStatus(false);
			backButton.setText(Descriptions.InteractiveSignature_Button_1);
			backButton.setVisible(false);
			verifyButton.setVisible(false);
			inputText.setVisible(true);
			inputText.setEditable(true);
			guideLabel.setText(Descriptions.InteractiveSignature_1);
			popup.setLocation(popupPosition);
			oldPopup = popup.getLocation();

			inputText.setFocus();
			break;
		// Step 1: First Description
		// Window Position: still centered, Task, -> next
		case 1:
			message = inputText.getText();
			if (message.length() != 0) {
				signatureComposite.setInteractiveStatus(true);
				guideLabel.setText(Descriptions.InteractiveSignature_2);
				inputText.setVisible(false);
				backButton.setVisible(true);
				inputText.setEditable(false);
				inputText.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

				if (plainSignature != null) {
					merkle.setIndex(currentIndex);
				}

				plainSignature = merkle.sign(message);
				if (plainSignature == "") {
					signatureComposite.keysExceededMessage();
				}
				signature = plainSignature.split("\\|");
			} else {
				step--;
			}
			signatureText.setText("");
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

			// *****Content*****//
			guideLabel.setText(Descriptions.InteractiveSignature_3_1 + currentIndex + " " + Descriptions.InteractiveSignature_3_2);
			signatureText.setText(currentIndex + " |");
			signaturSize.setText(Descriptions.MerkleTreeSign_6 + " " + signatureText.getText().length());
			leaves[currentIndex].highlight();
			break;
		// Step 3: Further Leaf explanation
		// Window Position: bottom-left at leaf Task: -> next
		case 3:
			// *****Content*****//
			guideLabel.setText(Descriptions.InteractiveSignature_4_1 + currentIndex + " " + Descriptions.InteractiveSignature_4_2);
			break;

		// Step 4: Add WOTS part to signature
		// Window Position: bottom-left at leaf Task: -> next
		case 4:
			signatureText.setText(currentIndex + " | ");
			signatureText.append(signature[1]);
			signaturSize.setText(Descriptions.MerkleTreeSign_6 + " " + signatureText.getText().length() / 2);
			break;
		// Step 5: authentication path explanation
		// Window Position: bottom-left at leaf Task: -> next
		case 5:
			guideLabel.setText(Descriptions.InteractiveSignature_5);
			markBranch(leaves[currentIndex]);
			markAuthPath(markedConnectionList);
			break;
		case 6:
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
			signatureText.setText(currentIndex + " | ");
			signatureText.append(signature[1] + " | ");
			signatureText.append(signature[2]);
			signaturSize.setText(Descriptions.MerkleTreeSign_6 + " " + signatureText.getText().length() / 2);
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

			signatureComposite.addSignatureAndMessage(plainSignature, message);

			guideLabel.setText(Descriptions.InteractiveSignature_6);
			signatureComposite.setInteractiveStatus(false);

			verifyButton.setVisible(true);
			nextButton.setText(Descriptions.InteractiveSignature_Button_3);

			nextButton.removeSelectionListener(nextListener);
			nextButton.addSelectionListener(newRoundListener);
			break;
		case 8:
			popup.dispose();
			step = 0;
			break;
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

	// public void updateWindowManually() {
	// popup.setLocation(popupPosition);
	// popup.redraw();
	// }
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

}
