// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.webbrowser.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.webbrowser.BrowserPlugin;

/**
 * Open action for the main toolbar It opens the webbrowser view
 *
 * @author mwalthart
 *
 */
public class OpenWebBrowserAction implements IWorkbenchWindowActionDelegate {
    public OpenWebBrowserAction() {
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    /**
     * opens the webbrowser view
     */
    public void run(IAction action) {
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("org.jcryptool.webbrowser.browser"); //$NON-NLS-1$
        } catch (PartInitException ex) {
            LogUtil.logError(BrowserPlugin.PLUGIN_ID, Messages.OpenWebBrowserAction_0, ex, true);
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
