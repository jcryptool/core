// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----

package org.jcryptool.core.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
//import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.ui.PartInitException;
//import org.eclipse.ui.handlers.HandlerUtil;
//import org.jcryptool.core.logging.utils.LogUtil;
//import org.jcryptool.core.operations.editors.AbstractEditorService;
//import org.jcryptool.editor.text.JCTTextEditorPlugin;
//import org.jcryptool.editor.text.commands.Messages;
//import org.jcryptool.editor.text.editor.JCTTextEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;

/**
 * 
 * @author Thorben Groos
 * @email thorben.groos@student.uni-siegen.de
 *
 */

public class OpenNewEmptyTextEditorCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
        try {
            page.openEditor(AbstractEditorService.createTemporaryEmptyFile(), "org.jcryptool.editor.text.editor.JCTTextEditor");
        } catch (PartInitException e) {
            LogUtil.logError(e);
        }
		return null;
	}

}
