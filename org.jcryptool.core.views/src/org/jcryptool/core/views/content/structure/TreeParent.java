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

import java.util.ArrayList;

/**
 * Represents an inner node within the tree
 *
 * @author mwalthart
 */
public class TreeParent extends TreeObject {
    private ArrayList<TreeObject> children;

    /**
     * creates a new TreeParent with the given name
     *
     * @param name
     */
    public TreeParent(String name) {
        super(name);
        children = new ArrayList<TreeObject>();
    }

    /**
     * adds a child to the TreeParent
     *
     * @param child the TreeObject to add
     */
    public void addChild(TreeObject child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * removes a child of the TreeParent
     *
     * @param child the TreeObject to remove
     */
    public void removeChild(TreeObject child) {
        children.remove(child);
        child.setParent(null);
    }

    /**
     * returns all children of the TreeParent
     *
     * @return all children of the TreeParent
     */
    public TreeObject[] getChildren() {
        return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
    }

    /**
     * returns true if the TreeParent has children
     *
     * @return true if the TreeParent has children
     */
    public boolean hasChildren() {
        return children.size() > 0;
    }
}
