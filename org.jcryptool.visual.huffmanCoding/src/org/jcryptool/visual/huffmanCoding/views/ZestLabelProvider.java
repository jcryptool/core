//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.huffmanCoding.views;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.jcryptool.visual.huffmanCoding.algorithm.Node;

/**
 * 
 * @author Miray Inel
 * 
 */
public class ZestLabelProvider extends LabelProvider implements IEntityStyleProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Node) {
			Node node = (Node) element;
			String tmp = null;
			if (node.getName() == -1) {
				double value = node.getValue();

				if (value == 0.0) {
					tmp = " "; //$NON-NLS-1$
				} else {
					tmp = String.format("%2.3f", value * 100); //$NON-NLS-1$
					tmp += " %"; //$NON-NLS-1$
				}

				if (node.getParent() == null) {
					if (node.getValue() != 0.0) {
						return String.valueOf(Messages.ZestLabelProvider_0 + "100 %"); //$NON-NLS-1$
					} else {
						return "Root"; //$NON-NLS-1$
					}
				} else {
					return tmp;
				}

			} else {
				return node.getNameAsString();
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
				return "1"; //$NON-NLS-1$
			}

			if (rightNode != null && destNode == rightNode) {
				return "0"; //$NON-NLS-1$
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
			if (n.getName() != -1) {
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

	@Override
	public IFigure getTooltip(Object entity) {
		if (entity instanceof Node) {
			Node n = (Node) entity;

			if (n.isLeaf()) {
				IFigure tooltip1 = new Label(n.getCode(), null);
				return tooltip1;
			} else if (n.getValue() == 0.0 && n.getParent() != null) {
				IFigure tooltip1 = new Label(Messages.ZestLabelProvider_2, null);
				return tooltip1;
			} else if (n.getParent() == null) {
				IFigure tooltip1 = new Label(Messages.ZestLabelProvider_3, null);
				return tooltip1;
			} else {
				IFigure tooltip1 = new Label(Messages.ZestLabelProvider_1, null);
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