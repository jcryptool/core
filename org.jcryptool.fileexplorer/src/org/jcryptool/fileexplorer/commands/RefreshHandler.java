// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.fileexplorer.views.FileExplorerView;

/**
 * Refreshes the complete tree.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class RefreshHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ((FileExplorerView) HandlerUtil.getActivePart(event)).refresh();

        return null;
    }
}
