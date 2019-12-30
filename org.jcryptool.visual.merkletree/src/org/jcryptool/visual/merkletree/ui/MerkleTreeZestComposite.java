//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2016, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
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
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.MultiTree;
import org.jcryptool.visual.merkletree.algorithm.Node;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Class for the Composite of Tabpage "MerkleTree"
 * Depicts a Graph of the MerkleTree and a description
 * 
 * @author Kevin Muehlboeck
 * @author Maximilian Lindpointner
 *
 */
public class MerkleTreeZestComposite extends Composite {

	private GraphViewer viewer;
	private int graphOffset;
	private SUIT mode;
	private Composite merkleTreeZestComposite;
	private StyledText styledTextTree;
	private ArrayList<GraphConnection> markedConnectionList;
	private ExpandBar descriptionExpander;
	private Label descLabel;
	private StyledText descText;
	private Composite expandComposite;
	private Composite zestComposite;
	private Display curDisplay;
	private boolean distinctListener = false;
	private boolean mouseDragging;
	private Point oldMouse;
	private Point newMouse;
	private int differenceMouseX;
	private int differenceMouseY;
	private org.eclipse.draw2d.geometry.Point viewLocation;
	private ISimpleMerkle merkle;
	private Graph graph;
	private List<?> graphNodeRetriever;
	private GraphNode[] leaves;
	private GraphNode[] nodes;
	private Color distinguishableColors[];
	private ZoomManager zoomManager;
	private Runnable currentlyHighlighted;
	private Runnable highlightedAuthpath;

	private Color[] greySteps;
	private Color[] redSteps;
	private ArrayList<GraphNode> markedAuthpathList;

	/**
	 * Creates the GUI elements and the Graph, as well as all listeners
	 * 
	 * @param parent
	 * @param style
	 *        SWT Composite style bits
	 * @param merkle
	 * @param mode
	 */
	public MerkleTreeZestComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT mode, ViewPart masterView) {
		super(parent, style);
		this.setLayout(new GridLayout(2, true));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, MerkleConst.DESC_HEIGHT + 1));
		curDisplay = getDisplay();
		merkleTreeZestComposite = this;
		this.merkle = merkle;
		this.mode = mode;

		// ***********************************
		// Beginning of GUI elements
		// ***********************************

		descLabel = new Label(this, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));

		descriptionExpander = new ExpandBar(this, SWT.NONE);
		descriptionExpander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		ExpandItem collapsablePart = new ExpandItem(descriptionExpander, SWT.NONE, 0);

		expandComposite = new Composite(descriptionExpander, SWT.NONE);
		GridLayout expandLayout = new GridLayout(2, true);
		expandComposite.setLayout(expandLayout);

		descText = new StyledText(expandComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		styledTextTree = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		// sets descriptions depending on mode
		switch (mode) {
		case XMSS:
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
			descText.setText(Descriptions.XMSS.Tab1_Txt0);
			styledTextTree.setText(Descriptions.XMSS.Tab1_Txt1);
			break;
		case XMSS_MT:
			descLabel.setText(Descriptions.XMSS_MT.Tab1_Head0);
			descText.setText(Descriptions.XMSS_MT.Tab1_Txt0);
			break;
		case MSS:
		default:
			descLabel.setText(Descriptions.MSS.Tab1_Head0);
			descText.setText(Descriptions.MSS.Tab1_Txt0);
			styledTextTree.setText(Descriptions.MSS.Tab1_Txt1);
			break;

		}

		int preferredHeight = descText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		collapsablePart.setText(Descriptions.Tab1_Button_1);
		collapsablePart.setExpanded(true);
		collapsablePart.setControl(expandComposite);
		collapsablePart.setHeight(preferredHeight + 60);
		descriptionExpander.setBackground(curDisplay.getSystemColor(SWT.COLOR_WHITE));

		zestComposite = new Composite(this, SWT.DOUBLE_BUFFERED | SWT.BORDER);
		zestComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		zestComposite.setLayout(new GridLayout());
		zestComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		zestComposite.setBackgroundMode(SWT.INHERIT_FORCE);
		zestComposite.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));

		// Beginning of the Graph
		viewer = new GraphViewer(zestComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider(ColorConstants.lightGreen));
		graph = viewer.getGraphControl();
		graph.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
		graph.setScrollBarVisibility(FigureCanvas.NEVER);
		viewer.getControl().forceFocus();
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
		viewer.setInput(merkle.getTree());
		viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		viewer.applyLayout();
		GridDataFactory.fillDefaults().grab(true, true).applyTo(viewer.getControl());

		markedConnectionList = new ArrayList<GraphConnection>();
		markedAuthpathList = new ArrayList<GraphNode>();

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

		graphNodeRetriever = graph.getNodes();
		nodes = new GraphNode[graphNodeRetriever.size()];
		leaves = new GraphNode[graphNodeRetriever.size() / 2 + 1];

		if (mode == SUIT.XMSS_MT) {
			colorizeMultitrees();
		}

		// ***********************************
		// End of GUI elements
		// ***********************************

		// Thumbnail test = new Thumbnail(graph.getRootLayer());
		// FigureCanvas canvas = new FigureCanvas(this);
		// canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		// canvas.setContents(test);

		// Here start many diffenret Listeners
		graph.addSelectionListener(new SelectionAdapter() {
			// when selecting a node of the tree, its path and authentication path gets highlighted
			// also displays the hash value of the tree
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
							unmarkBranch();
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

		descriptionExpander.addExpandListener(new ExpandListener() {
			@Override
			public void itemExpanded(ExpandEvent e) {
				curDisplay.asyncExec(() -> {
					collapsablePart.setHeight(preferredHeight + 60);
					descriptionExpander.pack();
					merkleTreeZestComposite.layout();
					collapsablePart.setText(Descriptions.Tab1_Button_1);

				});

			}

			@Override
			public void itemCollapsed(ExpandEvent e) {
				curDisplay.asyncExec(() -> {
					descriptionExpander.pack();
					merkleTreeZestComposite.layout();
					collapsablePart.setText(Descriptions.Tab1_Button_2);
				});
			}
		});

		// Color arrays for animate(); in MultiTree
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

	}

	/**
	 * Marks the whole branch beginning from the selected node
	 * 
	 * @param node
	 *        any node of the graph
	 */
	@SuppressWarnings("unchecked")
	private void markBranch(GraphNode node) {
		ArrayList<GraphItem> items = new ArrayList<GraphItem>();

		try {
			GraphConnection connection = (GraphConnection) node.getTargetConnections().get(0);

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
			items.add(((GraphConnection) (node.getSourceConnections().get(0))).getSource());
		}

		if (mode == SUIT.XMSS_MT) {
			currentlyHighlighted = animate(items.toArray(new GraphNode[items.size()]), greySteps);
		}
	}

	/**
	 * Unmark a previous marked branch stored in the Lists markedConnectionList and
	 * markedAuthpathList
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
