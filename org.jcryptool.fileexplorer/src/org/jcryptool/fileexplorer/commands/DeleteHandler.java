// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.fileexplorer.FileExplorerPlugin;
import org.jcryptool.fileexplorer.views.FileExplorerView;

/**
 * Deletes a file or directory.
 *
 * @author Dominik Schadow
 * @version 0.9.3
 */
public class DeleteHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
            IFileStore file = (IFileStore) ((IStructuredSelection) selection).getFirstElement();

            if (file.getParent() == null) {
                MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
                box.setText(Messages.DeleteHandler_0);
                box.setMessage(Messages.DeleteHandler_6);
                box.open();

                return null;
            }

            boolean isDirectory = file.fetchInfo().isDirectory();

            MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            box.setText(Messages.DeleteHandler_0);

            if (isDirectory) {
                box.setMessage(Messages.DeleteHandler_1);
            } else {
                box.setMessage(Messages.DeleteHandler_2);
            }

            if (box.open() == SWT.YES) {
                try {
                    file.delete(EFS.NONE, null);

                    ((FileExplorerView) HandlerUtil.getActivePart(event)).refresh();
                } catch (CoreException ex) {
                    if (isDirectory) {
                        LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, Messages.DeleteHandler_4, ex, true);
                    } else {
                        LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, Messages.DeleteHandler_5, ex, true);
                    }
                }
            }
        }

        return null;
    }
}
