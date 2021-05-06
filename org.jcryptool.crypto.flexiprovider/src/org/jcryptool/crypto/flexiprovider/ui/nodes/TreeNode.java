//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.ui.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.flexiprovider.FlexiProviderPlugin;

public class TreeNode implements ITreeNode {

	private String name;

	private ITreeNode parent;

	private Map<String, ITreeNode> children = new HashMap<String, ITreeNode>();

	public TreeNode(String name) {
		this.name = name;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.contacts.nodes.ITreeNode#addChild(org.jcryptool.crypto.keystore.contacts.nodes.ITreeNode)
	 */
	public void addChild(ITreeNode child) {
		synchronized (children) {
			child.setParent(this);
			children.put(child.getName(), child);
		}
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

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.contacts.nodes.ITreeNode#getChild(java.lang.String)
	 */
	public ITreeNode getChild(String name) {
		synchronized (children) {
			return children.get(name);
		}
	}

	/**
	 * @see org.jcryptool.crypto.keystore.contacts.nodes.ITreeNode#getChildren()
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

	public ImageDescriptor getImageDescriptor() {
		return ImageService.getImageDescriptor(FlexiProviderPlugin.PLUGIN_ID, "icons/sample.gif");
	}

	/**
	 * @see org.jcryptool.crypto.keystore.contacts.nodes.ITreeNode#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.contacts.nodes.ITreeNode#getParent()
	 */
	public ITreeNode getParent() {
		return parent;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.contacts.nodes.ITreeNode#hasChildren()
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

	public void removeChild(ITreeNode child) {
		synchronized (children) {
			children.remove(child.getName());
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.contacts.nodes.ITreeNode#setParent(org.jcryptool.crypto.keystore.contacts.nodes.ITreeNode)
	 */
	public void setParent(ITreeNode parent) {
		this.parent = parent;
	}

	public String toString() {
		return name;
	}

}
