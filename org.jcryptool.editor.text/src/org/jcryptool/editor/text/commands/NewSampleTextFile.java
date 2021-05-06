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
package org.jcryptool.editor.text.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.editor.text.JCTTextEditorPlugin;
import org.jcryptool.editor.text.editor.JCTTextEditor;

/**
 * Creates a new temporary text file with the JCrypTool sample contents and opens this file in the text editor.
 *
 * @author Dominik Schadow
 */
public class NewSampleTextFile extends AbstractHandler {
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

        try {
            page.openEditor(AbstractEditorService.createTemporaryFile(), JCTTextEditor.ID);
        } catch (PartInitException e) {
            LogUtil.logError(JCTTextEditorPlugin.PLUGIN_ID, Messages.TextEditorError, e, true);
        }

        return null;
    }
}
