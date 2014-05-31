//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.editor.text.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.editor.text.JCTTextEditorPlugin;
import org.jcryptool.editor.text.editor.JCTTextEditor;

/**
 * This NewFileJCTTextEditorAction class opens a new text file with the JCT Texteditor.
 *
 * @author amro
 * @author T. Kern
 */
public class NewEmptyFileJCTTextEditorAction implements IWorkbenchWindowActionDelegate {
    /**
     * Disposes this action delegate.
     */
    public void dispose() {
    }

    /**
     * Initializes this action delegate with the workbench window it will work in.
     *
     * @param window the window that provides the context for this delegate
     */
    public void init(IWorkbenchWindow window) {
    }

    /**
     * Performs this action. This method is called by the proxy action when the action has been triggered.
     *
     * @param action the action proxy that handles the presentation portion of the action
     */
    public void run(IAction action) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            page.openEditor(AbstractEditorService.createTemporaryEmptyFile(), JCTTextEditor.ID);
        } catch (PartInitException e) {
            LogUtil.logError(JCTTextEditorPlugin.PLUGIN_ID, Messages.NewFileJCTTextEditorAction_0, e, true);
        }
    }

    /**
     * Notifies this action delegate that the selection in the workbench has changed.
     *
     * @param action action the action proxy that handles presentation portion of the action
     * @param selection @param selection the current selection, or <code>null</code> if there is no selection.
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }
}