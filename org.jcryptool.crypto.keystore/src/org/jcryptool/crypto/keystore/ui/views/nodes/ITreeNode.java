// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
/**
 * 
 */
package org.jcryptool.crypto.keystore.ui.views.nodes;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author tkern
 * 
 */
public interface ITreeNode extends IAdaptable, Comparable<ITreeNode> {
    ITreeNode getChild(String name);

    Iterator<ITreeNode> getChildren();

    Object[] getChildrenArray();

    ImageDescriptor getImageDescriptor();

    String getName();

    ITreeNode getParent();

    boolean hasChildren();

    void setParent(ITreeNode parent);

    void addChild(ITreeNode child);

    void removeChild(ITreeNode child);
}
