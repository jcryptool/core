package org.jcryptool.visual.merkletree.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
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
import org.jcryptool.visual.merkletree.algorithm.MultiTree;
import org.jcryptool.visual.merkletree.algorithm.Node;
import org.jcryptool.visual.merkletree.files.MathUtils;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Class for the Composite of Tabpage "MerkleTree"
 * 
 * @author Kevin Muehlboeck
 * @author Maximilian Lindpointner
 *
 */
public class MerkleTreeZestComposite
		extends Composite /* implements IZoomableWorkbenchPart */
{

	private GraphViewer viewer;
	private StyledText styledTextTree;
	private ArrayList<GraphConnection> markedConnectionList;
	private ViewPart masterView;
	ExpandBar descriptionExpander;
	Label descLabel;
	StyledText descText;
	Composite expandComposite;
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
	List<?> graphNodeRetriever;
	GraphNode[] leaves;
	GraphNode[] nodes;
	private Color distinguishableColors[];

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
	public MerkleTreeZestComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT mode, ViewPart masterView) {
		super(parent, style);
		this.setLayout(new GridLayout(2, true));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, MerkleConst.DESC_HEIGHT + 1));
		this.masterView = masterView;
		Composite parentComposite = this;
		curDisplay = getDisplay();
		this.merkle = merkle;
		this.parent = parent;

		/*
		 * the description label for the chosen mode
		 */
		descLabel = new Label(this, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));

		descriptionExpander = new ExpandBar(this, SWT.NONE);
		descriptionExpander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		ExpandItem collapsablePart = new ExpandItem(descriptionExpander, SWT.NONE, 0);

		expandComposite = new Composite(descriptionExpander, SWT.NONE);
		GridLayout expandLayout = new GridLayout(2, true);
		expandComposite.setLayout(expandLayout);

		/*
		 * description text of the chosen mode
		 */
		descText = new StyledText(expandComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		// this.setLayout(new GridLayout(1, true));

		styledTextTree = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		switch (mode) {
		case XMSS:
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
			descText.setText(Descriptions.XMSS.Tab1_Txt0);
			styledTextTree.setText(Descriptions.XMSS.Tab1_Txt1);
			break;
		case XMSS_MT:
			descLabel.setText(Descriptions.XMSS_MT.Tab1_Head0);
			descText.setText(Descriptions.XMSS_MT.Tab1_Txt0);
			styledTextTree.setText(Descriptions.XMSS_MT.Tab1_Txt1);
			break;
		case MSS:
		default:
			descLabel.setText(Descriptions.MSS.Tab1_Head0);
			descText.setText(Descriptions.MSS.Tab1_Txt0);
			styledTextTree.setText(Descriptions.MSS.Tab1_Txt1);
			break;

		}
		int preferredHeight = descText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		// System.err.println(preferredHeight);

		collapsablePart.setText("Beschreibung ausblenden");
		collapsablePart.setExpanded(true);
		collapsablePart.setControl(expandComposite);
		// collapsablePart.setHeight((parent.getSize().y - 250) / 2);
		collapsablePart.setHeight(preferredHeight + 60);
		descriptionExpander.setBackground(curDisplay.getSystemColor(SWT.COLOR_WHITE));

		// Composite which contains a SashForm which contains the MerkleTree
		// Zest Graph
		zestComposite = new Composite(this, SWT.NONE);
		zestComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		zestComposite.setLayout(new GridLayout());
		zestComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
		zestComposite.setBackgroundMode(SWT.INHERIT_FORCE);

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
				Point startingSashLocation;

				switch (merkle.getLeafCounter()) {

				case 2:
					x = currentShellSize.x;
					y = currentShellSize.y / 2;
					startingSashLocation = new Point(70, 10);
					break;
				case 4:
					x = currentShellSize.x;
					y = currentShellSize.y / 1.7;
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

		descriptionExpander.addExpandListener(new ExpandListener() {

			Point currentShellSize;

			@Override
			public void itemExpanded(ExpandEvent e) {
				curDisplay.asyncExec(() -> {
					currentShellSize = parent.getSize();
					collapsablePart.setHeight(preferredHeight + 60);
					descriptionExpander.pack();
					parentComposite.layout();
					zestSash.setLocation(cameraPoint.x, cameraPoint.y - (currentShellSize.y / 2));
					collapsablePart.setText("Beschreibung ausblenden");
					expandedFlag = true;
				});

			}

			@Override
			public void itemCollapsed(ExpandEvent e) {
				curDisplay.asyncExec(() -> {
					currentShellSize = parent.getSize();
					descriptionExpander.pack();
					parentComposite.layout();
					zestSash.setLocation(cameraPoint.x, cameraPoint.y + (currentShellSize.y / 2));
					collapsablePart.setText("Beschreibung einblenden");
					expandedFlag = false;
				});
			}
		});

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
		graph.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));

		// Makes the nodes fixed (they cannot be dragged around with the mouse
		// by overriding the mouseMovedListener with empty event
		graph.getLightweightSystem().setEventDispatcher(new SWTEventDispatcher() {
			public void dispatchMouseMoved(MouseEvent e) {
			}

		});
		graphNodeRetriever = graph.getNodes();
		nodes = new GraphNode[graphNodeRetriever.size()];
		leaves = new GraphNode[graphNodeRetriever.size() / 2 + 1];

		if (mode == SUIT.XMSS_MT) {
			drawTreeLines();
		} else {

			graph.addSelectionListener(new SelectionAdapter() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.
				 * eclipse.swt.events. SelectionEvent) Click-Event to get the
				 * Selected Node and to mark the other Nodes
				 */
				@Override
				public void widgetSelected(SelectionEvent e) {
					distinctListener = true;
					if (e.item instanceof GraphNode) {
						GraphNode node = (GraphNode) e.item;
						Node n = (Node) node.getData();

						if (n.isLeaf()) {
							styledTextTree.setForeground(new Color(null, new RGB(1, 70, 122)));
							// styledTextTree.setFont(FontService.getHugeFont());
							styledTextTree.setText(Descriptions.ZestLabelProvider_5 + " " + n.getLeafNumber() + " = " //$NON-NLS-1$ //$NON-NLS-2$
									+ n.getNameAsString());

							if (markedConnectionList.size() == 0) {
								markBranch(node);
								markAuthPath(markedConnectionList);
							} else {
								unmarkBranch(markedConnectionList);
								markedConnectionList.clear();
								markBranch(node);
								markAuthPath(markedConnectionList);
							}
						} else {
							if (markedConnectionList.size() != 0) {
								unmarkBranch(markedConnectionList);
								markedConnectionList.clear();
								markBranch(node);
							} else {
								markBranch(node);
							}
							styledTextTree.setForeground(new Color(null, new RGB(0, 0, 0)));
							styledTextTree.setAlignment(SWT.LEFT);
							styledTextTree.setText(Descriptions.ZestLabelProvider_6 + " = " + n.getNameAsString());
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
			});
		}
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

					oldMouse = newMouse;
				}

			}
		});
	}

	public void drawTreeLines() {
		int singleTreeHeight = ((MultiTree) merkle).getSingleTreeHeight();
		int treeHeight = ((MultiTree) merkle).getTreeHeight();
		int singleTreeLeaves = 0;// = (int) Math.pow(2, singleTreeHeight);
		int treeCount = 0;
		int leafCounter = merkle.getLeafCounter();

		if (leafCounter == 16) {
			treeCount = 5;
			singleTreeLeaves = 4;
		} else if (leafCounter == 64) {
			// if (singleTreeHeight == 2) {
			// singleTreeLeaves = 4;
			// treeCount = 31;
			// } else {
			// singleTreeLeaves = 8;
			// treeCount = 7;
			// }
			return;
		}
		// for (int i = 0; leafCounter >= singleTreeLeaves; ++i) {
		// treeCount += leafCounter / singleTreeLeaves;
		// leafCounter = leafCounter / singleTreeLeaves;
		// }
		// treeCount = 0;
		// int d = ((MultiTree) merkle).getD();
		// int h = ((MultiTree) merkle).getH();
		// for (int i = 0; i < d; ++i) {
		// treeCount += ((MultiTree) merkle).getXMSSTreeCount(h, i);
		// }

		// for (int i = 0; i < d; ++i)

		GraphNode[] rootNodes = new GraphNode[treeCount];
		GraphNode helper;

		for (int i = 0, j = 0; i < graphNodeRetriever.size(); ++i) {
			if (((GraphNode) graphNodeRetriever.get(i)).getSourceConnections().isEmpty()) {
				leaves[i] = (GraphNode) graphNodeRetriever.get(i);
				++j;
			}
			nodes[i] = (GraphNode) graphNodeRetriever.get(i);
		}
		leafCounter = merkle.getLeafCounter();

		for (int i = 0; i < rootNodes.length;) {

			for (int k = 0; k < leafCounter; k += singleTreeLeaves, ++i) {
				rootNodes[i] = leaves[k];
				for (int j = 0; j < singleTreeHeight; ++j) {
					helper = ((GraphConnection) rootNodes[i].getTargetConnections().get(0)).getSource();
					rootNodes[i] = helper;

				}
			}
			for (int p = 0; p * singleTreeLeaves < leafCounter / singleTreeLeaves; ++p) {
				leaves[p] = rootNodes[p];
			}
			leafCounter /= singleTreeLeaves;
			// rootNodes[i].highlight();
		}

		distinguishableColors = new Color[5];
		distinguishableColors[0] = new Color(getDisplay(), 186, 186, 0);
		distinguishableColors[1] = new Color(getDisplay(), 186, 0, 186);
		distinguishableColors[2] = new Color(getDisplay(), 0, 186, 186);
		distinguishableColors[3] = new Color(getDisplay(), 0, 186, 0);
		distinguishableColors[4] = new Color(getDisplay(), 176, 0, 0);

		for (int i = rootNodes.length - 1; i >= 0; --i) {
			recursive(rootNodes[i], distinguishableColors[i]);
		}

	}

	@SuppressWarnings("unchecked")
	private void recursive(GraphNode node, Color color) {
		if (node.getSourceConnections() == null) {
			node.setBackgroundColor(color);
		}
		List<GraphConnection> connection = node.getSourceConnections();
		for (int i = 0; i < connection.size(); ++i) {
			recursive(connection.get(i).getDestination(), color);
		}
		node.setBackgroundColor(color);

	}

}
