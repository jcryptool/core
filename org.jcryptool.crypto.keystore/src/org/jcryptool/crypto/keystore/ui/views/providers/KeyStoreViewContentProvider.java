// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.ui.views.interfaces.IKeyStoreListener;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;
import org.jcryptool.crypto.keystore.ui.views.nodes.ITreeNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode;

/**
 * View content provider for the keystore view.
 * 
 * @author tkern
 * 
 */
public class KeyStoreViewContentProvider implements IStructuredContentProvider, ITreeContentProvider, IKeyStoreListener {
    private TreeViewer viewer;

    /** Tree root element that serves as an 'invisible' anchor. */
    private ITreeNode invisibleRoot;

    public KeyStoreViewContentProvider(TreeViewer viewer) {
        this.viewer = viewer;
        ContactManager.getInstance().addKeyStoreListener(this);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.jcryptool.crypto.keystore.ui.views.interfaces.IKeyStoreListener#fireKeyStoreModified(org.jcryptool.crypto
     * .keystore.ui.views.nodes.ITreeNode)
     */
    public void fireKeyStoreModified(ITreeNode node) {
        if (!viewer.isBusy()) {
            PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
                public void run() {
                    viewer.refresh();
                }
            });
        }
    }

    private void initialize() {
        LogUtil.logInfo("initializing"); //$NON-NLS-1$
        invisibleRoot = ContactManager.getInstance().getTreeModel();
        if (!viewer.isBusy()) {
            viewer.refresh();
        }
        LogUtil.logInfo("initialized (children: " + invisibleRoot.getChildrenArray().length + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object object) {
        if (invisibleRoot == null) {
            initialize();
        }
        if (invisibleRoot != null) {
            return getChildren(invisibleRoot);
        } else {
            return getChildren(object);
        }
    }

    /**
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object object) {
        if (object instanceof TreeNode) {
            return ((TreeNode) object).getParent();
        }
        return null;
    }

    /**
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object object) {
        if (object instanceof TreeNode) {
            return ((TreeNode) object).getChildrenArray();
        }
        return new Object[0];
    }

    /**
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object object) {
        if (object instanceof TreeNode) {
            return ((TreeNode) object).hasChildren();
        }
        return false;
    }

    /**
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     *      java.lang.Object)
     */
    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        v.refresh();
    }

    /**
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
        ContactManager.getInstance().removeKeyStoreListener(this);
    }
}