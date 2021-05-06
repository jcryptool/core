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

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;

public interface ITreeNode extends IAdaptable, Comparable<ITreeNode> {

	public void addChild(ITreeNode child);
	
	public ITreeNode getChild(String name);
	
	public Iterator<ITreeNode> getChildren();
	
	public Object[] getChildrenArray();
	
	public ImageDescriptor getImageDescriptor();
	
	public String getName();
	
	public ITreeNode getParent();
	
	public boolean hasChildren();
	
	public void setParent(ITreeNode parent);
	
}