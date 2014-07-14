/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.jcryptool.fileexplorer.views;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * Tree content provider for objects that can be adapted to the interface
 * {@link org.eclipse.ui.model.IWorkbenchAdapter IWorkbenchAdapter}.
 * <p>
 * This class may be instantiated, or subclassed.
 * </p>
 * 
 * @see IWorkbenchAdapter
 * @since 3.0
 */
@SuppressWarnings("restriction")
public class FileExplorerContentProvider implements ITreeContentProvider {

    private boolean hideInvisible;

	/**
     * Creates a new workbench content provider.
     *
     */
    public FileExplorerContentProvider() {
        super();
    }

    /* (non-Javadoc)
     * Method declared on IContentProvider.
     */
    public void dispose() {
        // do nothing
    }

    /**
     * Returns the implementation of IWorkbenchAdapter for the given
     * object.  Returns null if the adapter is not defined or the
     * object is not adaptable.
     * <p>
     * </p>
     * 
     * @param element the element
     * @return the corresponding workbench adapter object
     */
    protected IWorkbenchAdapter getAdapter(Object element) {
        return (IWorkbenchAdapter)Util.getAdapter(element, IWorkbenchAdapter.class);
    }

    private boolean canAdd(Object o) {
		if(o instanceof IFileStore) {
			File file;
			try {
				file = ((IFileStore)o).toLocalFile(0, null);
				return ! ( isHideInvisible() && file.isHidden());
			} catch (CoreException e) {
				LogUtil.logWarning("retrieving file hiddenInfo failed.");
			}
		}
		return true;
    }
    
    /* (non-Javadoc)
     * Method declared on ITreeContentProvider.
     */
    public Object[] getChildren(Object element) {
        List<Object> result = new LinkedList<Object>();
        
    	IWorkbenchAdapter adapter = getAdapter(element);
        if (adapter != null) {
        	Object[] children = adapter.getChildren(element);
        	for(Object child: children) {
        		if(canAdd(child)) result.add(child);
        	}
        }
        return result.toArray();
    }

    /* 
     * builds an EFS out of the given File objects 
     * @author mwalthart
     */
    public Object[] getElements(Object element) {
    	if(element instanceof File)
			try {
				return new Object[]{EFS.getStore(((File) element).toURI())};
			} catch (CoreException e) {
				return new Object[]{EFS.getNullFileSystem()};
			}
        if(element instanceof File[]){
        	File[] f_roots = (File[]) element;
        	Object[] o_roots = new Object[f_roots.length];
        	for(int i=0; i<f_roots.length; i++){
        		try {
					o_roots[i] = EFS.getStore(f_roots[i].toURI());
				} catch (CoreException e) {
					o_roots[i] = EFS.getNullFileSystem();
				}
        	}
        	return o_roots;
        }
    	return new Object[0];
    }

    /* (non-Javadoc)
     * Method declared on ITreeContentProvider.
     */
    public Object getParent(Object element) {
        IWorkbenchAdapter adapter = getAdapter(element);
        if (adapter != null) {
            return adapter.getParent(element);
        }
        return null;
    }

    /* (non-Javadoc)
     * Method declared on ITreeContentProvider.
     */
    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    /* (non-Javadoc)
     * Method declared on IContentProvider.
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // do nothing
    }

	public void setHideInvisible(boolean hideInvisible) {
		this.hideInvisible = hideInvisible;
	}

	public boolean isHideInvisible() {
		return hideInvisible;
	}

}
