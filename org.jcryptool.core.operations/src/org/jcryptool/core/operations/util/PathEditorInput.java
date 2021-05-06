// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.jcryptool.core.operations.util;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.editors.JCTElementFactory;

/**
 * EditorInput that stores a path. The class is taken from eclipse-rcp text editor example.
 * 
 */
public class PathEditorInput implements IPathEditorInput, IPersistableElement {
    private IPath fPath;

    /** The title of the Editor */
    private String title;

    private String tooltip;

    /**
     * Creates an editor input based of the given path resource.
     * 
     * @param path the file
     */
    public PathEditorInput(IPath path) {
        if (path == null) {
            throw new IllegalArgumentException();
        }
        this.fPath = path;
        this.tooltip = fPath.makeRelative().toOSString();
        setTitle(fPath.lastSegment());
    }

    /**
     * Creates an editor input based of the given String resource.
     * 
     * @param path the file
     */
    public PathEditorInput(String path) {
        if (path == null) {
            throw new IllegalArgumentException();
        }
        this.fPath = new Path(path);
        this.tooltip = fPath.makeRelative().toOSString();
        setTitle(fPath.lastSegment());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return fPath.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof PathEditorInput))
            return false;
        PathEditorInput other = (PathEditorInput) obj;
        return fPath.equals(other.fPath);
    }

    /**
     * @see org.eclipse.ui.IEditorInput#exists()
     */
    public boolean exists() {
        return fPath.toFile().exists();
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(fPath.toString());
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getName()
     */
    public String getName() {
        return title;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getToolTipText()
     */
    public String getToolTipText() {
        return tooltip;
    }

    /**
     * @see org.eclipse.ui.IPathEditorInput#getPath()
     */
    public IPath getPath() {
        return fPath;
    }

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter) {
        return null;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getPersistable()
     */
    public IPersistableElement getPersistable() {
        return new PathEditorInput(fPath);
    }

    /**
     * @see org.eclipse.ui.IPersistableElement#getFactoryId()
     */
    public String getFactoryId() {
        return JCTElementFactory.ID;
    }

    /**
     * Saves the state of the object in the given memento
     * 
     * @param memento the memento in which the object's state is saved
     */
    public void saveState(IMemento memento) {
        memento.putString("path", fPath.toString()); //$NON-NLS-1$
    }

    /**
     * Sets the Title value to the given parameter.
     * 
     * @param title The filename that will be displayed in the Title
     */
    private void setTitle(String title) {
        this.title = title;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

}
