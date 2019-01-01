//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.views.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.operations.OperationsManager;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.IOperationChangedListener;
import org.jcryptool.crypto.flexiprovider.ui.nodes.ITreeNode;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;

public class FlexiProviderOperationsViewContentProvider implements IStructuredContentProvider,
        ITreeContentProvider, IOperationChangedListener {
    private ITreeNode invisibleRoot;
    private ViewPart viewPart;
    private TreeViewer viewer;

    public FlexiProviderOperationsViewContentProvider(ViewPart viewPart, TreeViewer viewer) {
        this.viewPart = viewPart;
        this.viewer = viewer;
        OperationsManager.getInstance().addOperationChangedListener(this);
    }

    private void initialize() {
        invisibleRoot = OperationsManager.getInstance().getTreeModel();
    }

    public void update(TreeNode updated) {
        if (!viewer.isBusy()) {
            viewer.refresh();
        }
    }

    public void addOperation() {
        initialize();
        if (!viewer.isBusy()) {
            viewer.refresh();
        }
    }

    public void removeOperation() {
        initialize();
        if (!viewer.isBusy()) {
            viewer.refresh();
        }
    }

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    public void dispose() {
        OperationsManager.getInstance().removeOperationChangedListener(this);
    }

    public Object[] getElements(Object object) {
        LogUtil.logInfo("getting Elements"); //$NON-NLS-1$
        if (object.equals(viewPart.getViewSite())) {
            if (invisibleRoot == null) {
                initialize();
            }
            return getChildren(invisibleRoot);
        }
        return getChildren(object);
    }

    public Object[] getChildren(Object object) {
        if (object instanceof TreeNode) {
            return ((TreeNode) object).getChildrenArray();
        }
        return new Object[0];
    }

    public Object getParent(Object object) {
        if (object instanceof TreeNode) {
            return ((TreeNode) object).getParent();
        }
        return null;
    }

    public boolean hasChildren(Object object) {
        if (object instanceof TreeNode) {
            return ((TreeNode) object).hasChildren();
        }
        return false;
    }
}
