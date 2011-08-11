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
package net.sourceforge.javahexeditor.actions;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.operations.util.PathEditorInput;

/**
 * The OpenInTextAction class takes an input of an active hex editor, shuts the hex editor down and opens it with the
 * text editor.
 *
 * @author mwalthart
 * @version 0.1
 */
public class OpenInTextEditorAction implements IEditorActionDelegate {

    /**
     * the active editor
     */
    private IEditorPart editor;

    /**
     * active workbench page
     */
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
    public void run(IAction action) {
        IPathEditorInput originalInput = (IPathEditorInput) editor.getEditorInput();
        final String absolutePath = originalInput.getPath().toString();
        IEditorInput input = createEditorInput(absolutePath);

        final String textEditorID = "org.jcryptool.editor.text.editor.JCTTextEditor"; //$NON-NLS-1$

        final String TextEditorPluginID = "org.jcryptool.editor.text"; //$NON-NLS-1$

        // check if hex editor plugin is loaded

        if (Platform.getBundle(TextEditorPluginID) != null) {
            try {
                page.closeEditor(editor, true);
                page.openEditor(input, textEditorID, true);
            } catch (PartInitException e) {
                MessageDialog.openWarning(page.getWorkbenchWindow().getShell(), Messages.OpenInTextEditorAction_0,
                        Messages.OpenInTextEditorAction_1);
            }
        } else {
            MessageDialog.openError(page.getWorkbenchWindow().getShell(), Messages.OpenInTextEditorAction_0,
                    Messages.OpenInTextEditorAction_2);
        }

    }

    /**
     * Notifies this action delegate that the selection in the workbench has changed.
     *
     * @param action action the action proxy that handles presentation portion of the action
     * @param selection the current selection, or <code>null</code> if there is no selection.
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

}
