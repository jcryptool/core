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
package org.jcryptool.editor.text.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.editor.text.editor.JCTTextEditor;

/**
 * Command handler to include the text editor in the File Explorer view context menu (open with...).
 *
 * @author Dominik Schadow
 */
public class OpenEditorHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
            IFileStore file = (IFileStore) ((IStructuredSelection) selection).getFirstElement();

            if (file != null) {
                try {
                    page.openEditor(new PathEditorInput(new Path(file.toURI().getPath())), JCTTextEditor.ID, true,
                            IWorkbenchPage.MATCH_NONE);
                } catch (PartInitException ex) {
                    LogUtil.logError(ex);
                }
            }
        }

        return null;
    }
}
