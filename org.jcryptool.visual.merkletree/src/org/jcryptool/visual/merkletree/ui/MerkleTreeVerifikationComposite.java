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
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.Node;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;
/**
 * Class for the Composite of Tabpage "MerkleTree"
 * @author Kevin Muehlboeck
 *
 */
public class MerkleTreeVerifikationComposite extends Composite implements IZoomableWorkbenchPart {

	private GraphViewer viewer;
	private StyledText binaryValue;
	private StyledText styledTextTree;
	private int layoutCounter = 1;
	private ArrayList<GraphConnection> markedConnectionList;
	Label descLabel;
	Label descText;

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

		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN+5, MerkleConst.DESC_HEIGHT+1));
		this.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				viewer.applyLayout();
			}
		});
		

		/*
		 * The Text is based on the used suite
		 * if there will implemented an other suite, just add an else if and type the name of the instance
		 * Example for MultiTree -> merkle instanceof XMSSMT
		 */
		
		descLabel = new Label(this, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));

		if(merkle instanceof XMSSTree){
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
		}
		else if(merkle instanceof SimpleMerkleTree){

			descLabel.setText(Descriptions.MSS.Tab1_Head0);

		}

		/*
		 * The Description Text for the verification
		 */
		descText = new Label(this, SWT.WRAP);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		descText.setText(Descriptions.MerkleTreeVerify_0);
		this.setLayout(new GridLayout(1, true));	
		
		styledTextTree = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		styledTextTree.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		styledTextTree.setText(Descriptions.MerkleTreeVerify_4);

		
		viewer = new GraphViewer(this, SWT.NONE);
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider(ColorConstants.white));
		//select the layout of the connections -> CONNECTIONS_DIRECTED would be a ->
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
		linkMerkleTree(merkle);
		
		/*
		 * Text field for the binary representation of the node this
		 * The textbox is filled in the method markAuthPath()
		 */
		binaryValue = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		binaryValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

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
				}
			}
		});
		
		
		/**
		 * Verify Button
		 */
		Button bt_Verify = new Button(this,SWT.WRAP);
		bt_Verify.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		bt_Verify.setText(Descriptions.MerkleTreeVerify_1);
		bt_Verify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int currentLeaf=-1;
				
				for(GraphConnection con : markedConnectionList) {
					if(((Node)con.getDestination().getData()).isLeaf()) {
						currentLeaf=((Node)con.getDestination().getData()).getLeafNumber();
					}
				}
				if(currentLeaf >= 0) {
					if(merkle.verify(message, signature,currentLeaf)){
						/*
						 *set the Screen color based on the result
						 *green if verification success
						 *red if verification fails
						 */
						styledTextTree.setBackground(ColorConstants.green);
						styledTextTree.setText(Descriptions.MerkleTreeVerify_2);
					}
					else{
						styledTextTree.setBackground(ColorConstants.red);
						styledTextTree.setText(Descriptions.MerkleTreeVerify_3);
					}
				}
				else {
					/*
					 * if selected item is a node then show message that node cant be used to verify signature
					 */
					styledTextTree.setBackground(ColorConstants.red);
					styledTextTree.setText(Descriptions.MerkleTreeVerify_6);
				}
			}
		});
		
	}

	
	
	
	/**
	 * Marks the whole branch beginning from the leaf node
	 * @param leaf - the leaf node of the branch
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
	 * Marks the authentication path of the leaf and set the binary path to root in the textbox
	 * @param markedConnectionList - Contains marked elements of the Changing Path
	 */
	private void markAuthPath(List<GraphConnection> markedConnectionList) {
		GraphConnection authPath;
		for (GraphConnection connect : markedConnectionList) {
			Node myNode = (Node) connect.getDestination().getData();
			Node parentNode = (Node) connect.getSource().getData();
			
			/*
			 * Set the Binary Value of the Leaf and update if clicked on an other Leaf
			 */	
			if(myNode.isLeaf() == true){
			binaryValue.setText(Descriptions.MerkleTreeVerify_5 + myNode.getAuthPath());
			}
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
