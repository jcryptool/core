// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.webbrowser.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.webbrowser.BrowserPlugin;

/**
 * Open action for the main toolbar It opens the webbrowser view
 *
 * @author mwalthart
 * @author Holger Friedrich (now extending AbstractHandler
 * 			to use Commands rather than Actions)
 *
 */
public class OpenWebBrowser extends AbstractHandler {
    public OpenWebBrowser() {
    }

    /**
     * opens the webbrowser view
     */
    public Object execute(ExecutionEvent event) {
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("org.jcryptool.webbrowser.browser"); //$NON-NLS-1$
        } catch (PartInitException ex) {
            LogUtil.logError(BrowserPlugin.PLUGIN_ID, Messages.OpenWebBrowserAction_0, ex, true);
        }
        return null;
    }
}
