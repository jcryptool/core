// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.fileexplorer.views.FileExplorerView;

/**
 * Copies the selection (file(s) or directory (directories)) into the system clipboard.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class CopyHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
            int size = ((IStructuredSelection) selection).size();
            Clipboard clipboard = ((FileExplorerView) HandlerUtil.getActivePart(event)).getClipboard();
            String[] data = new String[size];

            for (int i = 0; i < size; i++) {
                IFileStore file = (IFileStore) ((IStructuredSelection) selection).toArray()[i];
                data[i] = file.toString();
            }

            clipboard.setContents(new Object[] {data}, new Transfer[] {FileTransfer.getInstance()});
        }

        return null;
    }
}
