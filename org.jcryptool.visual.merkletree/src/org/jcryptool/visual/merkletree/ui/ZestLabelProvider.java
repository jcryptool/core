package org.jcryptool.visual.merkletree.ui;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.Node;

/**
 * @author Christoph Sonnberger
 * This class defines the labeling, color and tooltips of the nodes
 * Used when building the MerkleTree
 */

public class ZestLabelProvider extends LabelProvider implements IEntityStyleProvider {

	Color backgroundColor;
	
	/**
	 * @author Christoph Sonnberger
	 * Constructor for the Class ZestLabelProvider
	 * @param backgroundColor: sets the backgroundColor of the Nodes used in the Tree
	 * 
	 */
	public ZestLabelProvider(Color backgroundColor){
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * @author Christoph Sonnberger
	 * Method used to label the Nodes and Connectionlines of the Merkle Scheme
	 * Label for Root, Node and Leaf
	 * Label 0/1 for left/right lines
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof Node) {
			Node node = (Node) element;
			
			//set root description
			if(node.getParent() == null){
				return String.valueOf(Descriptions.ZestLabelProvider_3);
			}
			//set leaf description
			else if(node.isLeaf() == true){
				return(String.valueOf(Descriptions.ZestLabelProvider_0 + " " + node.getLeafNumber()));
			}
			//set node description
			else{
				return String.valueOf(Descriptions.ZestLabelProvider_1);
			}

		}

		/*
		 * Handling for the edges. Left side is 1 and right side is 0
		 */
		if (element instanceof EntityConnectionData) {
			EntityConnectionData connection = (EntityConnectionData) element;
			Node sourceNode = (Node) connection.source;
			Node destNode = (Node) connection.dest;

			Node leftNode = sourceNode.getLeft();
			Node rightNode = sourceNode.getRight();

			if (leftNode != null && destNode == leftNode) {
				return Descriptions.TestLabelProvider_5; //1
			}

			if (rightNode != null && destNode == rightNode) {
				return Descriptions.TestLabelProvider_6; //2
			}
		}
		throw new RuntimeException("Wrong type: " + element.getClass().toString()); //$NON-NLS-1$
	}

	@Override
	public Color getNodeHighlightColor(Object entity) {
		// TODO Auto-generated method stub
		return null;
	} 

	@Override
	public Color getBorderColor(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBorderHighlightColor(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBorderWidth(Object entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @author Christoph Sonnberger
	 * sets the background color (defined in constructor) of the nodes when tree is created
	 * @return background color
	 */
	@Override
	public Color getBackgroundColour(Object entity) {
		if (entity instanceof Node) {
			Node n = (Node) entity;
			if (n.getCode() != "") {
				return backgroundColor;
			}
			
		}
		return null;
	}

	@Override
	public Color getForegroundColour(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @author Christoph Sonnberger
	 * sets the Tooltip when mouse hovers of a node
	 * Every node is showing its hash
	 */
	@Override
	public IFigure getTooltip(Object entity) {
		if (entity instanceof Node) {
			Node n = (Node) entity;

			IFigure tooltip1 = new Label("Hash = (" + n.getNameAsString() +")", null);
			return tooltip1;
		}
		return null;
	}

	@Override
	public boolean fisheyeNode(Object entity) {
		// TODO Auto-generated method stub
		return false;
	}

}