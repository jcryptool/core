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
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.util.PathEditorInput;

/**
 * The OpenInHexAction class takes an input of an active text editor, shuts the text editor down and
 * opens it with the hex editor.
 *
 * @author amro
 * @author Holger Friedrich (now extending AbstractHandler in order to use Commands instead of Actions)
 * @version 0.9.3
 */
public class OpenInHex extends AbstractHandler {
    /** The active editor. */
    private IEditorPart editor;
    /** Active workbench page. */
    private IWorkbenchPage page;

    /**
     * Sets the active editor for the delegate.
     *
     * @param action the action proxy that handles presentation portion of the action
     * @param targetEditor the new editor target
     */
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        editor = targetEditor;
        if (editor != null) {
            page = editor.getSite().getPage();
        }
    }

    /**
     * Creates the editor input for the hex editor
     *
     * @param absolutePath the absolute file path
     * @return the created editor input
     */
    private IEditorInput createEditorInput(String absolutePath) {
        return new PathEditorInput(absolutePath);
    }

    /**
     * Performs this action.
     *
     * @param action the action proxy that handles the presentation portion of the action
     */
    @Override
    public Object execute(ExecutionEvent event) {
    	page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    	editor = page.getActiveEditor();
    	if(editor == null) {
    		return null;
    	}
        IPathEditorInput originalInput = (IPathEditorInput) editor.getEditorInput();
        IEditorInput input = createEditorInput(originalInput.getPath().toString());

        // check if hex editor plug-in is loaded
        if (Platform.getBundle(IOperationsConstants.ID_HEX_EDITOR_PLUGIN) != null) {
            try {
                page.closeEditor(editor, true);
                page.openEditor(input, IOperationsConstants.ID_HEX_EDITOR, true);
            } catch (PartInitException e) {
                MessageDialog.openError(page.getWorkbenchWindow().getShell(), Messages.OpenTextInHexEditor_3,
                        Messages.OpenTextInHexEditor_1);
            }
        } else {
            MessageDialog.openError(page.getWorkbenchWindow().getShell(), Messages.OpenTextInHexEditor_3,
                    Messages.OpenTextInHexEditor_2);
        }
        return null;
    }
}