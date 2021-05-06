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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.ui.part.EditorInputTransfer;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.InputNode;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io.OutputNode;

public class EditorDragListener extends DragSourceAdapter {
    private TreeViewer viewer;

    public EditorDragListener(TreeViewer viewer) {
        this.viewer = viewer;
    }

    public void dragStart(DragSourceEvent event) {
        Object selected = viewer.getTree().getSelection()[0].getData();
        LogUtil.logInfo("Selected for drag: " + selected.getClass().getName()); //$NON-NLS-1$
        if (!(selected instanceof InputNode) && !(selected instanceof OutputNode)) {
            event.doit = false;
            return;
        }
        if (selected instanceof InputNode) {
            InputNode node = (InputNode) selected;
            if (node.getInput().equals("-1") || node.getInput().equals(Messages.InputType)) { //$NON-NLS-1$
                LogUtil.logInfo("not doing it"); //$NON-NLS-1$
                event.doit = false;
            }
        } else if (selected instanceof OutputNode) {
            OutputNode node = (OutputNode) selected;
            if (!node.getOutput().equals("-1") && !node.getOutput().equals("<Editor>")) { //$NON-NLS-1$
                LogUtil.logInfo("not doing it"); //$NON-NLS-1$
                event.doit = false;
            }
        }
    }

    public void dragSetData(DragSourceEvent event) {
        Object selected = viewer.getTree().getSelection()[0].getData();
        if (selected instanceof InputNode) {
            InputNode node = (InputNode) selected;
            LogUtil.logInfo("Input: " + node.getInput()); //$NON-NLS-1$
            if (!node.getInput().equals("-1") && !node.getInput().equals(Messages.InputType)) { //$NON-NLS-1$
                EditorInputTransfer.EditorInputData data = EditorInputTransfer.createEditorInputData(
                        IOperationsConstants.ID_HEX_EDITOR, new PathEditorInput(node.getInput())); //$NON-NLS-1$
                event.data = new EditorInputTransfer.EditorInputData[] { data };
            }
        } else if (selected instanceof OutputNode) {
            OutputNode node = (OutputNode) selected;
            LogUtil.logInfo("Output: " + node.getOutput()); //$NON-NLS-1$
            if (!node.getOutput().equals("-1") && !node.getOutput().equals("<Editor>")) { //$NON-NLS-1$
                EditorInputTransfer.EditorInputData data = EditorInputTransfer.createEditorInputData(
                		IOperationsConstants.ID_HEX_EDITOR, new PathEditorInput(node.getOutput())); //$NON-NLS-1$
                event.data = new EditorInputTransfer.EditorInputData[] { data };
            }
        }
    }

}
