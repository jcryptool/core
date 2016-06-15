package org.jcryptool.visual.merkletree.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
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
//import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.Node;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Class for the Composite of Tabpage "MerkleTree"
 * @author Kevin Muehlboeck
 *
 */
public class MerkleTreeZestComposite extends Composite implements IZoomableWorkbenchPart {

	private Composite compositeTree;
	private GraphViewer viewer;
	private StyledText styledTextTree;
	private int layoutCounter = 1;
	private ArrayList<GraphConnection> markedConnectionList;

	/**
	 * Create the composite.
	 * Including Description, GraphItem, GraphView, Description for GraphView
	 * @param parent
	 * @param style
	 */
	public MerkleTreeZestComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT verfahren) {
		super(parent, style);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		
		markedConnectionList = new ArrayList<GraphConnection>();

		compositeTree = new Composite(this, SWT.WRAP | SWT.BORDER | SWT.LEFT | SWT.FILL);
		compositeTree.setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN+5, MerkleConst.DESC_HEIGHT+1));
		compositeTree.setLayout(new GridLayout(1, true));
		compositeTree.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				viewer.applyLayout();
			}
		});
		

		Label descLabel = new Label(compositeTree, SWT.NONE);

		StyledText descText = new StyledText(compositeTree, SWT.WRAP);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		descText.setCaret(null);

		descText.setEditable(false);
		compositeTree.setLayout(new GridLayout(1, true));

		styledTextTree = new StyledText(compositeTree, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);

		// styledTextTree.setFont(FontService.getNormalFont());

		GridData gd_styledTextTree = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		gd_styledTextTree.widthHint = 960;
		gd_styledTextTree.heightHint = 40;
		styledTextTree.setLayoutData(gd_styledTextTree);

		viewer = new GraphViewer(compositeTree, SWT.NONE);
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider(ColorConstants.lightGreen));
		//select the layout of the connections -> CONNECTIONS_DIRECTED would be a ->
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
		linkMerkleTree(merkle);

		Control control = viewer.getControl();
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		switch(verfahren){
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
		graph.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 * Click-Event to get the Selected Node and to mark the other Nodes
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof GraphNode) {
					GraphNode node = (GraphNode) e.item;
					Node n = (Node) node.getData();

					if (n.isLeaf()) {
						styledTextTree.setForeground(new Color(null, new RGB(1, 70, 122)));
						// styledTextTree.setFont(FontService.getHugeFont());
						styledTextTree.setText(
								Descriptions.ZestLabelProvider_5 + " " + n.getLeafNumber() + " = " + n.getNameAsString()); //$NON-NLS-1$ //$NON-NLS-2$

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

					/*
					 * Table table = (Table) compositeCT.getChildren()[2];
					 * TableItem[] tmpItem = table.getItems(); for (int i = 0; i
					 * < tmpItem.length; i++) { if
					 * (n.getNameAsString().compareTo(tmpItem[i].getText(0)) ==
					 * 0) { table.setSelection(tmpItem[i]);
					 * table.showSelection(); break; } else {
					 * table.showItem(table.getItem(0)); table.deselectAll(); }
					 * }
					 */

				}
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
		
		try
		{
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
		}catch(IndexOutOfBoundsException ex) {
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
			
			//color the nodes back to light green
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
	 * @param markedConnectionList 
	 * 				- Contains marked elements of the Changing Path
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

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

	/**
	 * Change the layout of the merkle tree
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
	 * Synchronize the merklTree with the other Tabpages
	 * @param merkle
	 */
	private void linkMerkleTree(ISimpleMerkle merkle) {
		if (merkle.getMerkleRoot() != null) {
			viewer.setInput(merkle.getTree());

			LayoutAlgorithm layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
			viewer.setLayoutAlgorithm(layout, true);
			viewer.applyLayout();
		}

	}

}
