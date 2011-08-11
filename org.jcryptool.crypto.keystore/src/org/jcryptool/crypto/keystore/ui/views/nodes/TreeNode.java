//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
/**
 *
 */
package org.jcryptool.crypto.keystore.ui.views.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.crypto.keystore.KeyStorePlugin;

/**
 * @author tkern
 *
 */
public class TreeNode implements ITreeNode {

	private String name;

	private ITreeNode parent;

	private Map<String, ITreeNode> children = new HashMap<String, ITreeNode>();

	public TreeNode(String name) {
		this.name = name;
	}
	
	public void setName(String string) {
		this.name = string;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode#addChild(org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode)
	 */
	public void addChild(ITreeNode child) {
		synchronized (children) {
			child.setParent(this);
			String childName = child.getName();
//			if(children.containsKey(child.getName())) {
//				for(int i=1; childName.equals(child.getName()); i++) {
//					while(getChildren().hasNext()) {
//						ITreeNode childT = getChildren().next();
//						if(! childT.getName().equals(String.format("%s (%s)", child.getName(), i))) {
//							childName = childName+" ("+i+")";
//							break;
//						}
//					}
//				}
//			}
			children.put(childName, child);
		}
	}
	
	public void removeChild(ITreeNode child) {
		synchronized (children) {
			children.remove(child.getName());
		}
	}

	/**
	 * @see org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode#getChild(java.lang.String)
	 */
	public ITreeNode getChild(String name) {
		synchronized (children) {
			return children.get(name);
		}
	}

	/**
	 * @see org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode#getChildren()
	 */
	public Iterator<ITreeNode> getChildren() {
		synchronized (children) {
			return children.values().iterator();
		}
	}

	public Object[] getChildrenArray() {
		List<ITreeNode> list = new ArrayList<ITreeNode>();
		Iterator<ITreeNode> it = getChildren();
		while (it.hasNext()) {
			list.add(it.next());
		}
		Collections.sort(list);
		return ((ArrayList<ITreeNode>)list).toArray();
	}

	/**
	 * @see org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode#getParent()
	 */
	public ITreeNode getParent() {
		return parent;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode#hasChildren()
	 */
	public boolean hasChildren() {
		synchronized (children) {
			if (!children.isEmpty()) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	/**
	 * @see org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode#setParent(org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode)
	 */
	public void setParent(ITreeNode parent) {
		this.parent = parent;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ITreeNode node) {
		return getName().compareTo(node.getName());
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o instanceof TreeNode) {
			return compareTo((TreeNode)o) == 0;
		} else {
			return false;
		}
	}

	public String toString() {
		return name;
	}

	public ImageDescriptor getImageDescriptor() {
		return KeyStorePlugin.getImageDescriptor("icons/sample.gif"); //$NON-NLS-1$
	}

}
