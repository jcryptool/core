// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2014 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.action.fileActions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;

public class OpenFile extends AbstractHandler {
    private static final String TEXT_EDITOR = "org.jcryptool.editor.text.editor.JCTTextEditor"; //$NON-NLS-1$
    private static final String HEX_EDITOR = "net.sourceforge.ehep.editors.HexEditor"; //$NON-NLS-1$

    public void dispose() {
    }

    private String getEditorId(final String osString, ExecutionEvent event) {
        final IEditorDescriptor descriptor = HandlerUtil.getActiveWorkbenchWindow(event).getWorkbench().getEditorRegistry().getDefaultEditor(osString);

        if (descriptor != null) {
            return descriptor.getId();
        } else {
            // no file association; opening the file with the hex editor
            return HEX_EDITOR;
        }
    }

    public Object execute(ExecutionEvent event) {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION});
        dialog.setFilterNames(new String[] {IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME});
        dialog.setFilterPath(DirectoryService.getUserHomeDir());

        final String filename = dialog.open();

        if (filename != null && !"".equals(filename)) { //$NON-NLS-1$
            final IPath path = new Path(filename);
            final String editorId = this.getEditorId(path.toOSString(), event);

            IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
            
            if (editorId != null) {
                try {
                    if (editorId.equals(TEXT_EDITOR)) {
                        window.getActivePage().openEditor(new PathEditorInput(path.toOSString()), editorId, true,
                                IWorkbenchPage.MATCH_NONE);
                    } else if (editorId.equals(HEX_EDITOR)) {
                        window.getActivePage().openEditor(new PathEditorInput(path.toOSString()), editorId, true,
                                IWorkbenchPage.MATCH_NONE);
                    }
                } catch (final PartInitException ex) {
                    MessageDialog.openError(window.getShell(), Messages.OpenFileAction_title_could_not_open,
                            NLS.bind(Messages.OpenFileAction_message_could_not_open, editorId));
                    LogUtil.logError(ex);
                }
            } else { // no editor is associated
                MessageDialog.openInformation(window.getShell(),
                        Messages.OpenFileAction_title_could_not_open,
                        NLS.bind(Messages.OpenFileAction_message_assign_editor, path.getFileExtension()));
            }
        }
        return null;
    }
}
