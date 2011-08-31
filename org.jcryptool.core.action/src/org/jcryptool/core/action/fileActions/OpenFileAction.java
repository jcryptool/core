// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.action.fileActions;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;

public class OpenFileAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;
    private static final String TEXT_EDITOR = "org.jcryptool.editor.text.editor.JCTTextEditor"; //$NON-NLS-1$
    private static final String HEX_EDITOR = "net.sourceforge.javahexeditor.editors.BinaryEditor"; //$NON-NLS-1$

    public void dispose() {
    }

    private String getEditorId(final String OSString) {
        final IEditorDescriptor descriptor = this.window.getWorkbench().getEditorRegistry().getDefaultEditor(OSString);

        if (descriptor != null) {
            return descriptor.getId();
        } else {
            // no file association; opening the file with the hex editor
            return HEX_EDITOR;
        }
    }

    public void init(final IWorkbenchWindow window) {
        this.window = window;
    }

    private void run() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION});
        dialog.setFilterNames(new String[] {IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME});
        dialog.setFilterPath(DirectoryService.getUserHomeDir());

        final String filename = dialog.open();

        if (filename != null && !"".equals(filename)) { //$NON-NLS-1$
            final IPath path = new Path(filename);
            final String editorId = this.getEditorId(path.toOSString());

            if (editorId != null) {
                try {
                    if (editorId.equals(TEXT_EDITOR)) {
                        this.window.getActivePage().openEditor(new PathEditorInput(path.toOSString()), editorId, true,
                                IWorkbenchPage.MATCH_NONE);
                    } else if (editorId.equals(HEX_EDITOR)) {
                        this.window.getActivePage().openEditor(new PathEditorInput(path.toOSString()), editorId, true,
                                IWorkbenchPage.MATCH_NONE);
                    }
                } catch (final PartInitException ex) {
                    MessageDialog.openError(this.window.getShell(), Messages.OpenFileAction_title_could_not_open,
                            NLS.bind(Messages.OpenFileAction_message_could_not_open, editorId));
                    LogUtil.logError(ex);
                }
            } else { // no editor is associated
                MessageDialog.openInformation(this.window.getShell(),
                        Messages.OpenFileAction_title_could_not_open,
                        NLS.bind(Messages.OpenFileAction_message_assign_editor, path.getFileExtension()));
            }
        }

    }

    public void run(final IAction action) {
        this.run();
    }

    public void selectionChanged(final IAction action, final ISelection selection) {
    }
}
