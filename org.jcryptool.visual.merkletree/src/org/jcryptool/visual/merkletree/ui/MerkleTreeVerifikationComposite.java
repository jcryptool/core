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
import org.eclipse.swt.widgets.Button;
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
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSNode;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;

/**
 * Class for the Composite of Tabpage "MerkleTree"
 * @author Kevin Muehlboeck
 *
 */
public class MerkleTreeVerifikationComposite extends Composite implements IZoomableWorkbenchPart {

	//private Composite parent;
	private Composite compositeTree;

	private GraphViewer viewer;
	private StyledText binaryValue;
	private StyledText styledTextTree;
	private int layoutCounter = 1;
	// private Composite compositeCT;
	private ArrayList<GraphConnection> markedConnectionList;
	//private ISimpleMerkle merkle;

	/**
	 * Create the composite.
	 * Including Description, GraphItem, GraphView, Description for GraphView
	 * @param parent
	 * @param style
	 */
	public MerkleTreeVerifikationComposite(Composite parent, int style, ISimpleMerkle merkle, int leafNumber, String signature, String message) {
		super(parent, style);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		markedConnectionList = new ArrayList<GraphConnection>();
		// to make the text wrap lines automatically

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
		
		// this divide has been made to allow selection of text in this section
		// but not of the
		// heading
		// while not allowing modification of either section
		Label descText = new Label(compositeTree, SWT.WRAP);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		//descText.setCaret(null);
		descText.setText(Descriptions.MerkleTreeVerify_0);
		compositeTree.setLayout(new GridLayout(1, true));	
		
		styledTextTree = new StyledText(compositeTree, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		styledTextTree.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		styledTextTree.setText(Descriptions.MerkleTreeVerify_4);
		
		/**
		 * Verify Button
		 */
		Button bt_Verify = new Button(compositeTree,SWT.WRAP);
		bt_Verify.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bt_Verify.setText(Descriptions.MerkleTreeVerify_1);
		bt_Verify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int currentLeaf=-1;
				
				for(GraphConnection con : markedConnectionList) {
					/*if(((Node)con.getSource().getData()).getLeafNumber()>=0) {
						currentLeaf=((Node)con.getSource().getData()).getLeafNumber();
					}*/
					if(((Node)con.getDestination().getData()).isLeaf()) {
						currentLeaf=((Node)con.getDestination().getData()).getLeafNumber();
					}
					/*if(((Node)con.getData()).getLeafNumber() >= 0) {
						currentLeaf=((Node)con.getData()).getLeafNumber();
					}*/
				}
				if(currentLeaf >= 0) {
					if(merkle.verify(message, signature,currentLeaf)){
						//set the Screen color based on the result
						//green if verification success
						//red if verification fails
						styledTextTree.setBackground(ColorConstants.green);
						styledTextTree.setText(Descriptions.MerkleTreeVerify_2);
					}
					else{
						styledTextTree.setBackground(ColorConstants.red);
						styledTextTree.setText(Descriptions.MerkleTreeVerify_3);
					}
				}
				else {
					styledTextTree.setText("Bitte wählen Sie ein Blatt aus um die Signatur zu verifizieren. Ein Knoten kann nicht als gültiger öffentlicher Schlüssel verwendet werden!");
				}
			}
		});
		
		viewer = new GraphViewer(compositeTree, SWT.NONE);
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider(ColorConstants.white));
		//select the layout of the connections -> CONNECTIONS_DIRECTED would be a ->
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
		linkMerkleTree(merkle);
		
		/*
		 * Text field for the binary representation of the node
		 */
		binaryValue = new StyledText(compositeTree, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		binaryValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		binaryValue.setText("Test");

		Control control = viewer.getControl();
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Graph graph = viewer.getGraphControl();
		
		@SuppressWarnings("unchecked")
		List<GraphNode> gNodes = graph.getNodes();
		for(GraphNode gnode : gNodes) {
			if(((Node)gnode.getData()).getLeafNumber() == leafNumber) {
				if (markedConnectionList.size() == 0) {
					markBranch(gnode);
					markAuthPath(markedConnectionList);
				} else {
					unmarkBranch(markedConnectionList);
					markedConnectionList.clear();
					markBranch(gnode);
					markAuthPath(markedConnectionList);
				}
			}
		}
		
		
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
			
						binaryValue.setText(n.getCode() + ") = " + n.getAuthPath());

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
	 * Marks the whole branch beginning from the leaf node
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
	 *            
	 */
	private void unmarkBranch(List<GraphConnection> markedConnectionList) {
		GraphConnection authPath;
		for (GraphConnection connection : markedConnectionList) {
			
			//set the line color to gray and Root and Leaf to green
			connection.setLineColor(ColorConstants.lightGray);
			connection.getSource().setBackgroundColor(ColorConstants.white);
			connection.getDestination().setBackgroundColor(ColorConstants.white);
			
			//set the rest of the authentication path to green
			authPath = (GraphConnection) connection.getSource().getSourceConnections().get(0);
			authPath.getDestination().setBackgroundColor(ColorConstants.white);
			authPath = (GraphConnection) connection.getSource().getSourceConnections().get(1);
			authPath.getDestination().setBackgroundColor(ColorConstants.white);

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
			//this.merkle = merkle;
			viewer.setInput(merkle.getTree());

			LayoutAlgorithm layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
			viewer.setLayoutAlgorithm(layout, true);
			viewer.applyLayout();
		}

	}

}
