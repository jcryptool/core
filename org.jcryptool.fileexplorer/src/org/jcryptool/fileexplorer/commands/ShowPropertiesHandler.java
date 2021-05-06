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

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.fileexplorer.FileExplorerPlugin;
import org.jcryptool.fileexplorer.views.FileExplorerView;

/**
 * shows properties for a file or directory.
 * 
 * @author Anatoli Barski
 * @version 1.0.0
 */

public class ShowPropertiesHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
            IFileStore file = (IFileStore) ((IStructuredSelection) selection).getFirstElement();

            if (file != null) {
                String oldName = file.getName();
                String path = file.getParent().toURI().getPath();

                
                InputDialog dialog = new InputDialog(null, Messages.RenameHandler_0, Messages.RenameHandler_1,
                        file.getName(), null);
                int status = dialog.open();

                if (status != Window.OK) {
                    return null;
                }

                String newName = dialog.getValue().trim();

                if (oldName.equals(newName) || newName.equals("")) { //$NON-NLS-1$
                    return null;
                }

                try {
                    file.toLocalFile(EFS.NONE, null).renameTo(new File(path + File.separatorChar + newName));

                    ((FileExplorerView) HandlerUtil.getActivePart(event)).refresh();
                } catch (CoreException ex) {
                    LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, Messages.RenameHandler_4, ex, true);
                }
            }
        }

        return null;
    }

}
