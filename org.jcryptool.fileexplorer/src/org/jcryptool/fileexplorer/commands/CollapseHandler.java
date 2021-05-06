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
package org.jcryptool.fileexplorer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.fileexplorer.views.FileExplorerView;

/**
 * Collapse handler. Collapses all tree elements in the <b>File Explorer</b> view.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class CollapseHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActivePart(event) instanceof FileExplorerView) {
            ((FileExplorerView) HandlerUtil.getActivePart(event)).getViewer().collapseAll();
        }

        return null;
    }
}
