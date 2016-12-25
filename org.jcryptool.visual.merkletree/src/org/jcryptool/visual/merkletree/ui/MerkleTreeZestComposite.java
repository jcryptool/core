package org.jcryptool.visual.merkletree.ui;

import java.awt.Event;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
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
import org.jcryptool.visual.merkletree.algorithm.Node;
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
	Point cursorLoc;
	Point oldMouse;
	Point newMouse;
	Point oldSash;
	Point cameraPoint;
	Object lock = new Object();

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
	 * @param verfahren
	 */
	public MerkleTreeZestComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT verfahren) {
		super(parent, style);
		this.setLayout(new GridLayout(1, true));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, MerkleConst.DESC_HEIGHT + 1));
		Composite parentComposite = this;
		curDisplay = getDisplay();

		/*
		 * the description label for the chosen mode
		 */
		descLabel = new Label(this, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));

		descriptionExpander = new ExpandBar(this, SWT.NONE);
		descriptionExpander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		ExpandItem collapsablePart = new ExpandItem(descriptionExpander, SWT.NONE, 0);

		expandComposite = new Composite(descriptionExpander, SWT.NONE);
		GridLayout expandLayout = new GridLayout();
		expandComposite.setLayout(expandLayout);

		/*
		 * description text of the chosen mode
		 */
		descText = new StyledText(expandComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// this.setLayout(new GridLayout(1, true));

		styledTextTree = new StyledText(expandComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		collapsablePart.setText("Beschreibung ausblenden");
		collapsablePart.setExpanded(true);
		collapsablePart.setControl(expandComposite);
		collapsablePart.setHeight(parent.getSize().y / 2);
		descriptionExpander.setBackground(curDisplay.getSystemColor(SWT.COLOR_WHITE));

		/*
		 * update the item's height if needed in response to changes in the
		 * text's size
		 */

		// Composite which contains a SashForm which contains the MerkleTree
		// Zest Graph
		zestComposite = new Composite(this, SWT.NONE);
		zestComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		zestLayout = new GridLayout();
		zestComposite.setLayout(zestLayout);

		// sets the size for Composite zestComposite
		this.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				zestComposite.setSize(1920, 1080);
			}
		});

		zestSash = new SashForm(zestComposite, SWT.NO_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(zestSash);

		// Beginning of the Graph
		viewer = new GraphViewer(zestSash, SWT.H_SCROLL | SWT.V_SCROLL);

		// Camera Movement
		viewer.getGraphControl().addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				distinctListener = false;
				mouseDragging = false;
				cameraPoint = zestSash.getLocation();
			}

			@Override
			public void mouseDown(MouseEvent e) {
				mouseDragging = true;
				oldMouse = Display.getCurrent().getCursorLocation();
				oldSash = zestSash.getLocation();

				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						while (mouseDragging) {
							if (distinctListener == false)
								updateSashPosition();
							try {
								Thread.sleep(10);
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
		});

		descriptionExpander.addExpandListener(new ExpandListener() {

			Point currentShellSize;

			@Override
			public void itemExpanded(ExpandEvent e) {
				curDisplay.asyncExec(() -> {
					currentShellSize = parent.getSize();
					collapsablePart.setHeight(currentShellSize.y / 2);
					descriptionExpander.pack();
					parentComposite.layout();
					zestSash.setLocation(cameraPoint.x, cameraPoint.y - (currentShellSize.y / 2));
					// System.err.println("old y: " + cameraPoint.y + "
					// subracting " + currentShellSize.y / 2 + "\nNew position:
					// " + (cameraPoint.y - currentShellSize.y / 2));

					// System.err.println("composite position: " +
					// zestComposite.getSize().toString());
					// zestSash.getLocation().y);

				});

			}

			@Override
			public void itemCollapsed(ExpandEvent e) {
				curDisplay.asyncExec(() -> {
					currentShellSize = parent.getSize();

					descriptionExpander.pack();
					parentComposite.layout();
					// System.err.println("old y: " + cameraPoint.y + "\nnew y:
					// " + (cameraPoint.y + (currentShellSize.y / 2)));
					synchronized (Display.getDefault().getSynchronizer()) {
						try {
							Display.getDefault().getSynchronizer().wait(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					zestSash.setLocation(cameraPoint.x, cameraPoint.y + (currentShellSize.y / 2));
					// System.err.println("sash position: " +
					// zestSash.getLocation().toString());
					// System.err.println("actual position: " +
					// zestSash.getLocation().y);

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

		switch (verfahren) {
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
		Graph graph = viewer.getGraphControl();

		// Makes the nodes fixed (they cannot be dragged around with the mouse
		// by overriding the mouseMovedListener with empty event
		graph.getLightweightSystem().setEventDispatcher(new SWTEventDispatcher() {
			public void dispatchMouseMoved(MouseEvent e) {
			}

		});

		graph.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.
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
				if (newMouse.x - oldMouse.x != 0 || newMouse.y - oldMouse.y != 0) {
					zestSash.setLocation((newMouse.x - oldMouse.x) + oldSash.x, (newMouse.y - oldMouse.y) + oldSash.y);
					oldSash.x = (newMouse.x - oldMouse.x) + oldSash.x;
					oldSash.y = (newMouse.y - oldMouse.y) + oldSash.y;
					oldMouse = newMouse;
					System.err.println(zestSash.getLocation().toString());
				}

			}
		});
	}

}
