// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.crypto.keystore.ui.views.KeyDragListener;
import org.jcryptool.crypto.keystore.ui.views.nodes.keys.KeyPairNode;
import org.jcryptool.crypto.keystore.ui.views.providers.KeyStoreViewContentProvider;
import org.jcryptool.crypto.keystore.ui.views.providers.KeyStoreViewLabelProvider;

/**
 * @author Anatoli Barski
 *
 */
public class KeystoreViewer extends TreeViewer {

    private static final int dragOps = DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_LINK | DND.DROP_MOVE;
    private final Transfer[] transfers = new Transfer[] {TextTransfer.getInstance()};
    private KeyDragListener dragListener;
    
    private Action doubleClickAction;
    
    /**
     * Create the composite.
     * @param parent
     * @wbp.parser.entryPoint
     */
    public KeystoreViewer(Composite parent) {
        super(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        this.getTree().setLayoutData(gridData);
        
        dragListener = new KeyDragListener(this);
        this.addDragSupport(dragOps, transfers, dragListener);
        
        setLabelProvider(new KeyStoreViewLabelProvider());
        setContentProvider(new KeyStoreViewContentProvider(this));
        
        hookActions();
        
        this.setInput("input does not matter but triggers initialization");
    }
    
    public void reload()
    {
        setContentProvider(new KeyStoreViewContentProvider(this));
    }
    
    /**
     * Adds a listener, which will fold or unfold the nodes.
     */
    private void hookActions() {
        doubleClickAction = new Action() {
            @Override
            public void run() {
                ISelection selection = getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();

                if (obj instanceof org.jcryptool.crypto.keystore.ui.views.nodes.TreeNode) {
                    if (getTree().getSelection()[0].getExpanded()) {
                        collapseToLevel(obj, 1);
                    } else {
                        expandToLevel(obj, 1);
                    }
                } else if (obj instanceof KeyPairNode) {
                    // OperationsManager.getInstance().algorithmCalled(((AlgorithmNode) obj).getAlgorithm());
                }
            }
        };

        getControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(final MouseEvent e) {
                if (e.button == 1) { // only left button double clicks
                    doubleClickAction.run(); // run assigned action
                }
            }

        });
        
    }
}
