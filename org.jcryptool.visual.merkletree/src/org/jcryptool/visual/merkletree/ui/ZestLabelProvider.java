package org.jcryptool.visual.merkletree.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.Node;

/**
 * Class to define visible Content of each Node
 * @author Kevin Mühlböck
 * 
 */
public class ZestLabelProvider extends LabelProvider implements IEntityStyleProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 * Declares the Content of the Node
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof Node) {
			Node node = (Node) element;
			if (node.getCode() == null) {

				if (node.getParent() == null) {
					if (node.getCode() == "") {
						return String.valueOf(Descriptions.ZestLabelProvider_0); // $NON-NLS-1$
					} else {
						return String.valueOf(Descriptions.ZestLabelProvider_3); //$NON-NLS-1$
					}
				} else {
					return "    ";
				}

			} else {
				return "  " + String.valueOf(node.getLeafNumber()) + "  ";
			}

		}

		/*
		 * Handling for the edges. Left side is 1 and right side is 0
		 */
		if (element instanceof EntityConnectionData) {
			EntityConnectionData test = (EntityConnectionData) element;
			Node sourceNode = (Node) test.source;
			Node destNode = (Node) test.dest;

			Node leftNode = sourceNode.getLeft();
			Node rightNode = sourceNode.getRight();

			if (leftNode != null && destNode == leftNode) {
				return Descriptions.TestLabelProvider_5; //$NON-NLS-1$
			}

			if (rightNode != null && destNode == rightNode) {
				return Descriptions.TestLabelProvider_6; //$NON-NLS-1$
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

	@Override
	public Color getBackgroundColour(Object entity) {
		if (entity instanceof Node) {
			Node n = (Node) entity;
			if (n.getCode() != "") {
				return ColorConstants.lightGreen;
			}
		}
		return null;
	}

	@Override
	public Color getForegroundColour(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.zest.core.viewers.IEntityStyleProvider#getTooltip(java.lang.Object)
	 * Declares the Tooltip of each Node
	 */
	@Override
	public IFigure getTooltip(Object entity) {
		if (entity instanceof Node) {
			Node n = (Node) entity;

			if (n.isLeaf()) {
				IFigure tooltip1 = new Label("Hash(" + n.getCode() + ")", null);
				return tooltip1;
			} else if (n.getName() != null && n.getParent() != null) {
				IFigure tooltip1 = new Label(Descriptions.ZestLabelProvider_1, null);
				return tooltip1;
			} else if (n.getParent() == null) {
				IFigure tooltip1 = new Label(Descriptions.ZestLabelProvider_0, null);
				return tooltip1;
			} else {
				IFigure tooltip1 = new Label(Descriptions.ZestLabelProvider_1, null);
				return tooltip1;
			}
		}
		return null;
	}

	@Override
	public boolean fisheyeNode(Object entity) {
		// TODO Auto-generated method stub
		return false;
	}

}