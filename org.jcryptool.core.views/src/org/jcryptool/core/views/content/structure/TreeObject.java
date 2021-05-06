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

import org.eclipse.core.runtime.IAdaptable;

/**
 * Represents an object within the tree
 *
 * @author mwalthart
 */
public class TreeObject implements IAdaptable {
    private String name;
    private TreeParent parent;
    private boolean isFlexiProviderAlgorithm = false;

    /**
     * creates a new TreeObject with the given name
     *
     * @param name
     */
    public TreeObject(String name) {
        this.name = name;
    }

    /**
     * returns the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * set the algorithm to be from the FlexiProvider
     */
    public void setIsFlexiProviderAlgorithm() {
        isFlexiProviderAlgorithm = true;
    }

    /**
     * sets the parent of this TreeObject
     *
     * @param parent
     */
    public void setParent(TreeParent parent) {
        this.parent = parent;
    }

    /**
     * returns the parent of this TreeObject
     *
     * @return the parent of this TreeObject
     */
    public TreeParent getParent() {
        return parent;
    }

    /**
     * returns a string representation of the object
     */
    public String toString() {
        return getName();
    }

    /**
     * returns true if the algorithm is from the FlexiProvider
     *
     * @return true if the algorithm is from the FlexiProvider
     */
    public boolean isFlexiProviderAlgorithm() {
        return isFlexiProviderAlgorithm;
    }

    @SuppressWarnings("rawtypes")
	public Object getAdapter(Class key) {
        return null;
    }
}
