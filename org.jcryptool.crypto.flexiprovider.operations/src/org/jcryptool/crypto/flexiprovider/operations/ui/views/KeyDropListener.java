// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.views;

import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.EntryNode;
import org.jcryptool.crypto.flexiprovider.ui.nodes.ITreeNode;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

public class KeyDropListener extends DropTargetAdapter {
    private IFlexiProviderOperation entryNode;

    public KeyDropListener() {
    }

    public void drop(DropTargetEvent event) {
        Widget item = event.item;
        Object tmp = event.data;
        if (item != null && tmp != null) {
            if (item instanceof TreeItem) {
                entryNode = getTopLevel((TreeNode) ((TreeItem) item).getData());
                LogUtil.logInfo("target: " + entryNode.getTimestamp()); //$NON-NLS-1$
                entryNode.setKeyStoreAlias(new KeyStoreAlias((String) tmp));
            }
        } else {
            LogUtil.logInfo("item is null"); //$NON-NLS-1$
        }
    }

    private EntryNode getTopLevel(ITreeNode node) {
        if (node instanceof EntryNode) {
            LogUtil.logInfo("RETURNING: " + ((EntryNode) node).getEntryName()); //$NON-NLS-1$
            return (EntryNode) node;
        } else {
            return getTopLevel(node.getParent());
        }
    }

}
