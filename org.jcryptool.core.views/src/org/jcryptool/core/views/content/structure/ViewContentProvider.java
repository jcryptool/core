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
package org.jcryptool.core.views.content.structure;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * ContentProvider for a TreeViewer
 *
 * @author mwalthart
 */
public class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    public void dispose() {
    }

    /**
     * returns all elements for the given parent
     */
    public Object[] getElements(Object parent) {
        return getChildren(parent);
    }

    /**
     * returns the parent for the given child
     */
    public Object getParent(Object child) {
        if (child instanceof TreeObject) {
            return ((TreeObject) child).getParent();
        }
        return null;
    }

    /**
     * returns all children for the given parent
     */
    public Object[] getChildren(Object parent) {
        if (parent instanceof TreeParent) {
            return ((TreeParent) parent).getChildren();
        }
        return new Object[0];
    }

    /**
     * returns true if the given parent has children
     */
    public boolean hasChildren(Object parent) {
        if (parent instanceof TreeParent)
            return ((TreeParent) parent).hasChildren();
        return false;
    }
}
