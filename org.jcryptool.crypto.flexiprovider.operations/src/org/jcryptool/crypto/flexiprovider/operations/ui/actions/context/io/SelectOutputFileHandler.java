// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.ISelectedOperationListener;

public class SelectOutputFileHandler extends AbstractHandler {
    private IFlexiProviderOperation entryNode;
    private ISelectedOperationListener listener;

    public SelectOutputFileHandler(ISelectedOperationListener listener) {
        // this.setText(Messages.SelectOutputFileAction_0);
        // this.setToolTipText(Messages.SelectOutputFileAction_1);
        this.listener = listener;
    }

    public Object execute(ExecutionEvent event) {
        this.entryNode = listener.getFlexiProviderOperation();
        LogUtil.logInfo("select output file... (" + entryNode.getTimestamp() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterExtensions(new String[] {IConstants.ALL_FILTER_EXTENSION});
        dialog.setFilterNames(new String[] {IConstants.ALL_FILTER_NAME});
        dialog.setFilterPath(DirectoryService.getUserHomeDir());

        String filename = dialog.open();
        if (filename != null) {
            entryNode.setOutput(filename);
        }
        return(null);
    }

}