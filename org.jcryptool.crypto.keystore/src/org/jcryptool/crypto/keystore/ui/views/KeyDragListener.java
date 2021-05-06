// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.CertificateNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.PrivateKeyNode;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.SecretKeyNode;

public class KeyDragListener extends DragSourceAdapter {

    private TreeViewer viewer;

    public KeyDragListener(TreeViewer viewer) {
        this.viewer = viewer;
    }

    public void dragStart(DragSourceEvent event) {
        Object selected = viewer.getTree().getSelection()[0].getData();
        if (!(selected instanceof SecretKeyNode) && !(selected instanceof PrivateKeyNode)
                && !(selected instanceof CertificateNode)) {
            event.doit = false;
        }
    }

    public void dragSetData(DragSourceEvent event) {
        Object selected = viewer.getTree().getSelection()[0].getData();
        if (selected instanceof SecretKeyNode) {
            event.data = ((SecretKeyNode) selected).getAlias().getAliasString();
        } else if (selected instanceof PrivateKeyNode) {
            event.data = ((PrivateKeyNode) selected).getAlias().getAliasString();
        } else if (selected instanceof CertificateNode) {
            event.data = ((CertificateNode) selected).getAlias().getAliasString();
        }
    }

}
