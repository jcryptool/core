// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.editor.hex.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.editor.hex.HexEditorConstants;

public class NewEmptyFile extends AbstractHandler {

	/**
	 * creates a new window with the sample input of JCrypTool
	 */
	public Object execute(ExecutionEvent event) {
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				page.openEditor(AbstractEditorService.createTemporaryEmptyFile(), HexEditorConstants.EditorID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
            LogUtil.logError(e);
		}
		return null;
	}
}
