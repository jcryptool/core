// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.games.divide.DividePlugin;

/**
 * This handler displays the dynamic help identified by the given context help id.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class HelpHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        PlatformUI.getWorkbench().getHelpSystem().displayHelp(DividePlugin.PLUGIN_ID + ".helpView"); //$NON-NLS-1$

        return null;
    }
}
