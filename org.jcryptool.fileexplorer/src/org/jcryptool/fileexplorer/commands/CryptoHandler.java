//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.fileexplorer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.fileexplorer.FileExplorerPlugin;
import org.jcryptool.fileexplorer.preferences.PreferenceConstants;
import org.jcryptool.fileexplorer.views.FileExplorerView;

/**
 * Cryptographic handler. Executes the chosen cryptographic operation on the selected file.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class CryptoHandler extends AbstractHandler {
    private boolean openSourceFile = true;

    public Object execute(ExecutionEvent event) throws ExecutionException {
        getPreferenceValues();

        if (openSourceFile) {
            IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
            ISelection selection = HandlerUtil.getCurrentSelection(event);
            if (selection instanceof IStructuredSelection) {
                IFileStore file = (IFileStore) ((IStructuredSelection) selection).getFirstElement();

                if (file != null) {
                    try {
                        page.openEditor(new PathEditorInput(new Path(file.toURI().getPath())), FileExplorerView.EDITOR_ID_HEX, true,
                                IWorkbenchPage.MATCH_NONE);
                    } catch (PartInitException ex) {
                        LogUtil.logError(ex);
                    }
                }
            }
        }

        return null;
    }

    private void getPreferenceValues() {
        IPreferenceStore store = FileExplorerPlugin.getDefault().getPreferenceStore();

        openSourceFile = store.getBoolean(PreferenceConstants.P_OPEN_SOURCE);
    }
}
