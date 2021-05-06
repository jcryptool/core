// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;

public class OpenFileHandler extends AbstractHandler {

    @Override
	public void dispose() {
    }

    public void init(final IWorkbenchWindow window) {
    }

    public void run(final IAction action) {
        try {
			this.execute(null);
		} catch (ExecutionException ex) {
		    LogUtil.logError(ex);
		}
    }

    public void selectionChanged(final IAction action, final ISelection selection) {
    }

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] {IConstants.ALL_FILTER_EXTENSION, IConstants.TXT_FILTER_EXTENSION});
        dialog.setFilterNames(new String[] {IConstants.ALL_FILTER_NAME, IConstants.TXT_FILTER_NAME});
        dialog.setFilterPath(DirectoryService.getUserHomeDir());

        final String filename = dialog.open();

        if (filename != null && !"".equals(filename)) { //$NON-NLS-1$
        	FileOpener.open(filename);
        }

		return null;
	}
}
