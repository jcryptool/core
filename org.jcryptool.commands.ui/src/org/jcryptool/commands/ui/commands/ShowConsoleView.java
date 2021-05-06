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
package org.jcryptool.commands.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.commands.ui.CommandsUIPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * Shows the <b>Console View</b> at the configured position of the perspective extension.
 *
 * @author Dominik Schadow
 * @version 0.6.0
 */
public class ShowConsoleView extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView("org.eclipse.ui.console.ConsoleView"); //$NON-NLS-1$
        } catch (PartInitException ex) {
            LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, Messages.ShowConsoleView_0, ex, true);
        }

        return null;
    }
}
